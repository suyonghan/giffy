package com.safe.traveler.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.safe.traveler.dao.AdminDAO;
import com.safe.traveler.dao.api.SafetyDAO;
import com.safe.traveler.dao.api.WarningDAO;
import com.safe.traveler.vo.api.SafetyVO;
import com.safe.traveler.vo.api.WarningVO;

@Service
public class APIConnector {
	@Autowired
	AdminDAO admin;
	@Autowired
	WarningDAO warn;
	@Autowired
	SafetyDAO safe;
	@Autowired
	LogService log;

	TextMining tm;
	URL url;
	String result;
	NodeList body = null;
	Element items = null;
	NodeList item = null;
	HashSet<String> checkDup = new HashSet<String>();

	void executeAccidentSchedule() {
		ArrayList<SafetyVO> sList;
		int page = 1;
		safetyLoop: while (true) {
			log.writelog(1, "load_Safety page", "pageNo : " + page);
			sList = this.getSafeData(admin.getAdmin().getUrl2(), admin.getAdmin().getServiceKey(), page);
			if (sList.size() != 0) {
				for (SafetyVO temp : sList) {
					boolean check = safe.insertSafety(temp);
					log.writelog(1, "result loading Safety page", "result : " + page);
					if (!check)
						break safetyLoop;
				}
			} else {
				break;
			}
			page++;
		}
	}

	void executeWarningSchedule() {
		ArrayList<WarningVO> wList;
		int page = 1;

		warningLoop: while (true) {
			log.writelog(1, "warning pageNo", Integer.toString(page));
			wList = this.getWarningData(admin.getAdmin().getUrl3(), admin.getAdmin().getServiceKey(), page);
			if (wList.size() != 0) {
				for (WarningVO temp : wList) {
					if (!warn.insertWarning(temp))
						break warningLoop;
				}
			} else {
				break;
			}
			page++;
		}

		page = 1;
		SpecialLoop: while (true) {
			log.writelog(1, "special-warning page", Integer.toString(page));
			wList = this.getSpecialWarningData(admin.getAdmin().getUrl4(), admin.getAdmin().getServiceKey(), page);
			if (wList.size() != 0) {
				for (WarningVO temp : wList) {
					if (!warn.insertWarning(temp))
						break SpecialLoop;
				}
				page++;
			} else {
				break;
			}
		}
	}

	private Element connectAPI(URL url) {
		StringBuilder sb = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(1000);
			conn.setReadTimeout(8000);
			conn.setRequestMethod("GET");
			conn.setDoOutput(false);
			sb = new StringBuilder();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}
				br.close();
			} else
				log.writelog(2, "HTTP_URL_CONNECTION_ERROR", conn.getResponseMessage());
			conn.disconnect();
		} catch (Exception e) {
			if (e instanceof SocketTimeoutException) {
				log.writelog(2, "connect api error", "read time out");
				return connectAPI(url);
			} else if (e instanceof MalformedURLException) {
				log.writelog(2, "MalFormedURLerror", e.getMessage());
			}
		}
		return this.parseStringToXML(sb.toString());
	}

	@SuppressWarnings("unused")
	private Element parseStringToXML(String result) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		InputSource is = null;
		Document d = null;
		NodeList body = null;
		Element items = null;
		try {
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(result));
			d = db.parse(is);
			body = d.getElementsByTagName("body").item(0).getChildNodes();
			items = (Element) body.item(0);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			log.writelog(2, "error", e.getMessage());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			log.writelog(2, "error", e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.writelog(2, "error", e.getMessage());
		}
		return items;
	}

	@SuppressWarnings("unused")
	ArrayList<SafetyVO> getSafeData(String serviceURL, String serviceKey, int pageNo) {
		log.writelog(1, "safety pageNo", Integer.toString(pageNo));
		SafetyVO result = null;
		ArrayList<SafetyVO> resultList = new ArrayList<SafetyVO>();
		try {
			url = new URL(serviceURL + "?serviceKey=" + serviceKey + "&pageNo=" + pageNo);
			items = this.connectAPI(url);
			if (items == null) {
				return null;
			}
			loop: for (int i = 0; i < items.getChildNodes().getLength(); i++) {
				item = items.getChildNodes().item(i).getChildNodes();
				result = new SafetyVO();
				for (int j = 0; j < item.getLength(); j++) {
					String key = item.item(j).getNodeName();
					if (item.item(j).getChildNodes().item(0) == null) {
						continue;
					} else {
						String value = item.item(j).getChildNodes().item(0).getNodeValue();
						if (key.equals("countryName")) {
							value = admin.convertNameToCode(value);
							if (value != null)
								result.setIsoCode(value);
							else
								result.setRemarks(value);
						} else if (key.equals("title"))
							result.setTitle((value));
						else if (key.equals("id")) {
							result.setSid(value);
							if (checkDup.contains(value))
								continue loop;
							else
								checkDup.add(value);
						} else if (key.equals("wrtDt"))
							result.setWriteDate(value);
						else if (key.equals("content"))
							result.setContent(value);
					}
				}
				if (tm == null) {
					tm = new TextMining();
					tm.setRisk(admin.getRiskMap());
				}
				result.setRiskType(tm.classifyText(result.getTitle(), result.getContent()));
				resultList.add(result);
			}
		} catch (IOException e) {
			if (e instanceof SocketTimeoutException) {
				log.writelog(2, "connect api error", e.getMessage());
				return getSafeData(serviceURL, serviceKey, pageNo);
			} else if (e instanceof MalformedURLException) {
				log.writelog(2, "connect api error", e.getMessage());
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@SuppressWarnings("unused")
	ArrayList<WarningVO> getWarningData(String serviceURL, String serviceKey, int pageNo) {
		WarningVO result = null;
		ArrayList<WarningVO> resultList = new ArrayList<WarningVO>();
		try {
			url = new URL(serviceURL + "?serviceKey=" + serviceKey + "&pageNo=" + pageNo);
			items = this.connectAPI(url);
			if (items == null) {
				return null;
			}
			for (int i = 0; i < items.getChildNodes().getLength(); i++) {
				item = items.getChildNodes().item(i).getChildNodes();
				result = new WarningVO();
				for (int j = 0; j < item.getLength(); j++) {
					String key = item.item(j).getNodeName();
					String value = item.item(j).getChildNodes().item(0).getNodeValue();
					if (key.equals("continent"))
						result.setContinent(value);
					else if (key.equals("countryName")) {
						result.setCountryName(value);
						value = admin.convertNameToCode(value);
						if (value != null)
							result.setIsoCode(value);
						else
							result.setIsoCode("None");
					} else if (key.equals("control") || key.equals("attentionPartial") || key.equals("attention")
							|| key.equals("controlPartial"))
						result.setWarning(value);
					else if (key.equals("controlNote") || key.equals("attentionNote"))
						result.setWarningArea(value);
					else if (key.equals("id")) {
						result.setWid(value);
					} else if (key.equals("wrtDt"))
						result.setWriteDate(value);
					else if (key.equals("limitaPartial") || key.equals("limita"))
						result.setLimitStatus(value);
					else if (key.equals("limitaNote"))
						result.setLimitArea(value);
				}
				resultList.add(result);
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@SuppressWarnings("unused")
	ArrayList<WarningVO> getSpecialWarningData(String serviceURL, String serviceKey, int pageNo) {
		WarningVO result = null;
		ArrayList<WarningVO> resultList = new ArrayList<WarningVO>();
		try {
			url = new URL(serviceURL + "?serviceKey=" + serviceKey + "&pageNo=" + pageNo);
			items = this.connectAPI(url);
			for (int i = 0; i < items.getChildNodes().getLength(); i++) {
				item = items.getChildNodes().item(i).getChildNodes();
				result = new WarningVO();
				for (int j = 0; j < item.getLength(); j++) {
					String key = item.item(j).getNodeName();
					String value = item.item(j).getChildNodes().item(0).getNodeValue();
					if (key.equals("continent"))
						result.setContinent(value);
					else if (key.equals("countryName")) {
						result.setCountryName(value);
						value = admin.convertNameToCode(value);
						if (value != null)
							result.setIsoCode(value);
						else
							result.setIsoCode("None");
					} else if (key.equals("splimitPartial") || key.equals("spbanYna") || key.equals("spbanYnPartial"))
						result.setWarning(value);
					else if (key.equals("spbanNote") || key.equals("splimitNote"))
						result.setWarningArea(value);
					else if (key.equals("id")) {
						result.setWid("s" + value);
					} else if (key.equals("wrtDt"))
						result.setWriteDate(value);
					else if (key.equals("splimitPartial"))
						result.setLimitStatus(value);
					else if (key.equals("splimitNote"))
						result.setLimitArea(value);
				}
				resultList.add(result);
			}
		} catch (MalformedURLException e1) {
			log.writelog(2, "error", e1.getMessage());
		} catch (NullPointerException e) {
			log.writelog(2, "error", e.getMessage());
		}
		return resultList;
	}
}
