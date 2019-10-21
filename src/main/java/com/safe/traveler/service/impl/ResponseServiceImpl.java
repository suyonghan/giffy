package com.safe.traveler.service.impl;

import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safe.traveler.dao.AdminDAO;
import com.safe.traveler.vo.api.GiffyAPIVO;
import com.safe.traveler.vo.api.SafetyVO;
import com.safe.traveler.vo.api.TokenVO;
import com.safe.traveler.vo.api.WarningVO;

@Repository
class ResponseServiceImpl {
	@Autowired
	AdminDAO admin;
	@Autowired
	LogService log;

	JSONObject body;
	JSONObject result;

	@SuppressWarnings("unchecked")
	public JSONObject generateTokenResponse(int status, TokenVO token) {
		body = new JSONObject();
		result = new JSONObject();
		result.put("response-status", status);
		if (status == 200) {
			if (token != null) {
				try {
					body.put("serviceKey", AESEncryptor.decrypt(token.getServiceKey()));
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
				body.put("startDate", token.getStartDate());
				body.put("expiryDate", token.getExpiryDate());
			} else {
				body.put("delete", "success");
			}
		} else if (status == 403)
			body.put("response-message-detail", "expiration token, you must reissue token");
		else if (status == 404)
			body.put("response-message-detail", "check url or your serviceKey");
		else if (status == 405)
			body.put("response-message-detail", "Method Not Allowed");
		else if (status == 500)
			body.put("response-message-detail", "server error, try again later");
		result.put("response-message", body);
		log.writelog(1, "generate token result", result.toString());
		return result;
	}

	@SuppressWarnings("unchecked")
	public JSONObject generateSingleIsoCode(GiffyAPIVO giffy) {
		body = new JSONObject();
		result = new JSONObject();
		WarningVO warning;
		List<WarningVO> warningList = giffy.getWarningList();
		String w, wa, l, la, sw, swa, c;
		w = wa = l = la = sw = swa = c = null;
		if (warningList != null) {
			for (int i = 0; i < warningList.size(); i++) {
				warning = warningList.get(i);
				if (warning.getWid().charAt(0) == 's') {
					sw = warning.getWarning();
					swa = warning.getWarningArea();
				} else {
					w = warning.getWarning();
					wa = warning.getWarningArea();
					l = warning.getLimitStatus();
					la = warning.getLimitArea();
				}
				c = warning.getContinent();
			}
		}
		if (c == null)
			c = "None";
		if (w == null)
			w = "None";
		if (wa == null)
			wa = "None";
		if (l == null)
			l = "None";
		if (la == null)
			la = "None";
		if (sw == null)
			sw = "None";
		if (swa == null)
			swa = "None";
		body.put("countryName", giffy.getCountryName());
		body.put("isoCode", giffy.getIsoCode());
		body.put("contenient", c);
		HashMap<String, String> warnType = new HashMap<String, String>();
		warnType.put("warning", w);
		warnType.put("warningArea", wa);
		warnType.put("limit", l);
		warnType.put("limitArea", la);
		warnType.put("specialWarning", sw);
		warnType.put("specialWarningArea", swa);
		HashMap<String, Integer> riskType = new HashMap<String, Integer>();
		for (SafetyVO temp : giffy.getSafetyList()) {
			String key = temp.getRiskType();
			String k = "";
			if (key == null)
				k = "etc";
			else {
				if (key.equals("질병"))
					k = "disease";
				else if (key.equals("유의사항"))
					k = "notice";
				else if (key.equals("범죄사고"))
					k = "crime";
				else if (key.equals("사건사고"))
					k = "accident";
				else if (key.equals("전쟁및테러"))
					k = "warAndTerrorism";
				else if (key.equals("자연재해"))
					k = "naturalDisasters";
			}
			if (riskType.containsKey(k))
				riskType.put(k, riskType.get(k) + 1);
			else
				riskType.put(k, 1);
		}
		body.put("risk", riskType);
		body.put("warning", warnType);
		result.put("response-message", body);
		log.writelog(1, "search country result", result.toString());
		return result;
	}

	@SuppressWarnings("unchecked")
	public JSONObject generateDate(GiffyAPIVO giffy, int status) {
		HashMap<String, Integer> tempRisk;
		HashMap<String, HashMap<String, Integer>> risk = new HashMap<String, HashMap<String, Integer>>();
		result = new JSONObject();
		body = new JSONObject();
		JSONArray arr = new JSONArray();
		for (SafetyVO temp : giffy.getSafetyTimeList()) {
			JSONObject country = new JSONObject();
			String countryName;
			String isoCode = temp.getIsoCode();
			String key = temp.getRiskType();
			if (key == null)
				key = "etc";
			if (isoCode == null)
				countryName = temp.getRemarks();
			else
				countryName = admin.convertCodeToName(isoCode);
			if (risk.containsKey(countryName)) {
				if (risk.get(countryName).containsKey(key))
					risk.get(countryName).put(key, risk.get(countryName).get(key) + 1);
				else
					risk.get(countryName).put(key, 1);
			} else {
				tempRisk = new HashMap<String, Integer>();
				tempRisk.put(key, 1);
				risk.put(countryName, tempRisk);
			}
			body.put(countryName, country);
		}
		JSONObject tCountry;
		for (String countryName : risk.keySet()) {
			tCountry = (JSONObject) body.get(countryName);
			tCountry.put("risk", risk.get(countryName));
			tCountry.put("countryname", countryName);
			tCountry.put("isocode", admin.convertNameToCode(countryName));
			arr.add(tCountry);
		}
		result.put("response-status", status);
		result.put("response-message", arr);
		log.writelog(1, "search country result", result.toString());
		return result;
	}

	@SuppressWarnings("unchecked")
	public JSONObject generateMultipleIsoCode(List<GiffyAPIVO> giffyList) {
		JSONObject result = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject temp;
		for (GiffyAPIVO giffy : giffyList) {
			temp = generateSingleIsoCode(giffy);
			arr.add(temp.get("response-message"));
		}
		result.put("response-message", arr);
		log.writelog(1, "search country result", result.toString());
		return result;
	}
}
