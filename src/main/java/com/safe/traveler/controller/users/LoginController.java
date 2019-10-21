package com.safe.traveler.controller.users;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safe.traveler.dao.AdminDAO;
import com.safe.traveler.dao.user.LoginDAO;
import com.safe.traveler.service.impl.LogService;
import com.safe.traveler.service.impl.TokenServiceImpl;
import com.safe.traveler.vo.user.UsersVO;

@RestController
@SessionAttributes("user")
public class LoginController {
	@Autowired
	AdminDAO adminDAO;

	@Autowired
	LoginDAO dao;

	@Autowired
	TokenServiceImpl t;

	@Autowired
	LogService log;

	JSONObject result;
	@SuppressWarnings("unchecked")
	@CrossOrigin
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public JSONObject postLogin(@RequestBody String filterJSON, HttpServletRequest request,
			HttpServletResponse response) {
		result = new JSONObject();
		ObjectMapper objectMapper = new ObjectMapper();
		UsersVO vo = null;
		try {
			vo = objectMapper.readValue(filterJSON, UsersVO.class);
			log.writelog(1, false, "sign-in", "user-info : " + vo.toString());
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			log.writelog(2, false, "sign-in", "error-msg : " + e.getMessage());
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			log.writelog(2, false, "sign-in", "error-msg : " + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.writelog(2, false, "sign-in", "error-msg : " + e.getMessage());
		}

		vo = dao.login(vo);
		JSONObject c = new JSONObject();
		if (vo != null) {
			result.put("response-status", "200");
			result.put("response-message", "success");
			String serviceKey = t.getServiceKey(Integer.toString(vo.getUserNo()));
			if (serviceKey == null)
				c.put("serviceKey", "None");
			else
				c.put("serviceKey", serviceKey);
			c.put("userNo", vo.getUserNo());
			c.put("geoKey", adminDAO.getGeoKey());
			c.put("flagPath", adminDAO.getFlagPath());
			c.put("countryList", adminDAO.getCountryList());
			result.put("cookie", c);
		} else {
			result.put("response-status", "401");
			result.put("response-message", "failed");
			result.put("response-message-detail", "wrong email or password");
		}
		log.writelog(1, false, "sign-in", "result : " + result.toString());
		return result;
	}

	@SuppressWarnings("unchecked")
	@CrossOrigin
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public JSONObject getLogin(@RequestBody UsersVO vo) {
		result = new JSONObject();
		vo = dao.login(vo);
		JSONObject c = new JSONObject();
		if (vo != null) {
			result.put("response-status", "200");
			result.put("response-message", "success");
			log.writelog(1, true, "sign-in", "user-info : " + vo.toString());
			String serviceKey = t.getServiceKey(Integer.toString(vo.getUserNo()));
			if (serviceKey == null)
				c.put("serviceKey", "None");
			else
				c.put("serviceKey", serviceKey);
			c.put("userNo", vo.getUserNo());
			c.put("geoKey", adminDAO.getGeoKey());
			c.put("flagPath", adminDAO.getFlagPath());
			c.put("countryList", adminDAO.getCountryList());
			result.put("cookie", c);
		} else {
			result.put("response-status", "401");
			result.put("response-message", "failed");
			result.put("response-message-detail", "wrong email or password");
		}
		log.writelog(1, false, "sign-in", "result : " + result.toString());
		return result;
	}

	@SuppressWarnings("unchecked")
	@CrossOrigin
	@RequestMapping(value = "/getCountryList", method = RequestMethod.GET)
	public JSONObject getCountryList() {
		JSONObject result = new JSONObject();
		result.put("countryList", adminDAO.getCountryList());
		System.out.println(result.toString());
		return result;
	}
}
