package com.safe.traveler.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.safe.traveler.service.impl.GiffyAPIServiceImpl;
import com.safe.traveler.service.impl.LogService;
import com.safe.traveler.service.impl.TokenServiceImpl;
import com.safe.traveler.vo.user.UsersVO;

/**
 * Handles requests for the application home page.
 */
@RestController
public class APIController {
	@Autowired
	LogService log;

	@Autowired
	TokenServiceImpl t;
	@Autowired
	GiffyAPIServiceImpl giffy;

	@SuppressWarnings("unchecked")
	@CrossOrigin
	@RequestMapping(value = "/searchBySingleIsoCode", method = RequestMethod.GET)
	public JSONObject searchSingleIsoCodeGET(String serviceKey, String countryName) {
		log.writelog(1, true, "search single country", "serviceKey : " + serviceKey + ", countryName : " + countryName);
		JSONObject result;
		JSONObject token = t.checkValidatedToken(false, serviceKey);
		if (token.get("response-status").toString().equals("200")) {
			result = giffy.getInfoBySingleIsoCode(countryName);
			JSONObject temp = (JSONObject) result.get("response-message");
			if (temp.get("countryName") == null) {
				temp.put("countryName", countryName);
				result.put("response-message", temp);
			}
		} else
			result = token;

		log.writelog(1, true, "search single country", "result : " + result.toJSONString());
		System.out.println(result.toString());
		return result;
	}

	@SuppressWarnings("unchecked")
	@CrossOrigin
	@RequestMapping(value = "/searchBySingleIsoCode", method = RequestMethod.POST)
	public JSONObject searchSingleIsoCodeByPOST(UsersVO userVO, String countryName) {
		JSONObject result;
		log.writelog(1, false, "search single country",
				"user-info : " + userVO.toString() + ", countryName : " + countryName);
		JSONObject token = t.checkValidatedToken(true, Integer.toString(userVO.getUserNo()));
		if (token.get("response-status").toString().equals("200")) {
			result = giffy.getInfoBySingleIsoCode(countryName);
			JSONObject temp = (JSONObject) result.get("response-message");
			if (temp.get("countryName") == null) {
				temp.put("countryName", countryName);
				result.put("response-message", temp);
			}
		} else
			result = token;

		log.writelog(1, true, "search single country", "result : " + result.toJSONString());
		System.out.println(result.toString());
		return result;
	}

	@CrossOrigin
	@RequestMapping(value = "/searchByMultipleIsoCode", method = RequestMethod.GET)
	public @ResponseBody JSONObject searchMultipleIsoCode(String serviceKey, String countryName1, String countryName2) {
		List<String> list = new ArrayList<String>();
		list.add(countryName1);
		list.add(countryName2);
		JSONObject token = t.checkValidatedToken(false, serviceKey);
		if (token.get("response-status").toString().equals("200"))
			return giffy.getInfoByMultipleIsoCode(list);
		else
			return token;
	}

	@CrossOrigin
	@RequestMapping(value = "/searchByMultipleIsoCode", method = RequestMethod.POST)
	public JSONObject searchMultipleIsoCodeByID(UsersVO userVO, String serviceKey, String countryName1,
			String countryName2) {
		List<String> list = new ArrayList<String>();
		list.add(countryName1);
		list.add(countryName2);
		JSONObject token = t.checkValidatedToken(true, Integer.toString(userVO.getUserNo()));
		if (token.get("response-status").toString().equals("200"))
			return giffy.getInfoByMultipleIsoCode(list);
		else
			return token;
	}

	@RequestMapping(value = "/searchByDate", method = RequestMethod.GET)
	public JSONObject searchByDateGET(String serviceKey, String startDate, String endDate) {
		JSONObject token = t.checkValidatedToken(false, serviceKey);
		if (token.get("response-status").toString().equals("200"))
			return giffy.getInfoByDate(startDate, endDate);
		else
			return token;
	}

	@CrossOrigin
	@RequestMapping(value = "/searchByDate", method = RequestMethod.POST)
	public JSONObject searchByDatePOST(UsersVO usersVO, String startDate, String endDate) {
		JSONObject token = t.checkValidatedToken(true, Integer.toString(usersVO.getUserNo()));
		if (token.get("response-status").toString().equals("200"))
			return giffy.getInfoByDate(startDate, endDate);
		else
			return token;
	}
}
