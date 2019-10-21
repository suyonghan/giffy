package com.safe.traveler.dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Repository;

import com.safe.traveler.vo.AdminVO;

//admin json
@Repository
public class AdminDAO {
	private JSONParser parser = new JSONParser();
	private String isoCodePath = null;
	private String riskPath = null;
	private String flagPath = null;
	private JSONObject jsonObj;
	private AdminVO admin = null;
	private HashMap<String, String> nameToCodeMap;
	private HashMap<String, String> codeToNameMap;
	private HashMap<String, String> riskMap;
	private String geoKey;
//	private JSONArray countryList;
	private List<JSONObject> countryList;

	public AdminDAO() {
		this.readAdminJson();
		this.getApiConf();
		this.readIsoCode();
		this.readRiskDic();
		countryList = new ArrayList<JSONObject>();
		this.setCountryList();
	}

	public AdminVO getAdmin() {
		return admin;
	}

	public String convertNameToCode(String key) {
		if (nameToCodeMap.containsKey(key))
			return nameToCodeMap.get(key);
		else
			return null;
	}

	public String convertCodeToName(String key) {
		if (codeToNameMap.containsKey(key))
			return codeToNameMap.get(key);
		else
			return null;
	}

	public boolean checkIsoCodeValidation(String key) {
		return codeToNameMap.containsKey(key);
	}

	public HashMap<String, String> getRiskMap() {
		return riskMap;
	}

//	public JSONArray getCountryList() {
//		return this.countryList;
//	}

	public List<JSONObject> getCountryList() {
		return this.countryList;
	}

	private void readAdminJson() {
		Object obj;
		try {
			obj = parser.parse(new FileReader(
					"C:\\Users\\student\\Desktop\\s\\s\\eclipse_workspace\\SafeProject\\src\\main\\resources\\admin\\config\\admin.json"));
			jsonObj = (JSONObject) obj;
			this.isoCodePath = (String) jsonObj.get("isoCodePath");
			this.riskPath = (String) jsonObj.get("riskPath");
			this.flagPath = (String) jsonObj.get("flagPath");
			this.geoKey = (String) jsonObj.get("geoKey");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("파싱에러다 임마");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getFlagPath() {
		return flagPath;
	}

	public String getGeoKey() {
		return this.geoKey;
	}

	public void setFlagPath(String flagPath) {
		this.flagPath = flagPath;
	}

	private void getApiConf() {
		admin = null;
		String url1 = (String) jsonObj.get("url1");
		String url2 = (String) jsonObj.get("url2");
		String url3 = (String) jsonObj.get("url3");
		String url4 = (String) jsonObj.get("url4");
		String serviceKey = (String) jsonObj.get("serviceKey");
		admin = new AdminVO(url1, url2, url3, url4, serviceKey);
	}

	private void readIsoCode() {
		this.nameToCodeMap = new HashMap<String, String>();
		this.codeToNameMap = new HashMap<String, String>();
		try {
			jsonObj = (JSONObject) parser.parse(new FileReader(this.isoCodePath));
			JSONArray jsonArray = (JSONArray) jsonObj.get("codeMap");
			for (int i = 0; i < jsonArray.size(); i++) {
				jsonObj = (JSONObject) jsonArray.get(i);
				nameToCodeMap.put(jsonObj.get("name").toString(), jsonObj.get("code").toString());
				codeToNameMap.put(jsonObj.get("code").toString(), jsonObj.get("name").toString());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void readRiskDic() {
		riskMap = new HashMap<String, String>();
		try {
			jsonObj = (JSONObject) parser.parse(new FileReader(this.riskPath));
			JSONObject temp = (JSONObject) jsonObj.get("riskMap");
			for (Object data : temp.keySet()) {
				String key = data.toString();
				riskMap.put(key, temp.get(key).toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void setCountryList() {
		JSONObject temp;
		for (String country : this.nameToCodeMap.keySet()) {
			temp = new JSONObject();
			temp.put("countryName", country);
			this.countryList.add(temp);
		}
	}
}
