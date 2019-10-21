package com.safe.traveler.controller.users;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safe.traveler.dao.user.RegisterDAO;
import com.safe.traveler.service.impl.LogService;
import com.safe.traveler.vo.user.UsersVO;

@Controller
public class RegisterController {
	@Autowired
	RegisterDAO dao;

	@Autowired
	LogService log;

	JSONObject result;

	@CrossOrigin
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public void get(@RequestBody String filterJSON, HttpServletRequest request, HttpServletResponse response) {
		log.writelog(1, true, "sign-up", "request : " + request.toString());
		log.writelog(1, true, "sign-up", "response" + response.toString());
	}

	@CrossOrigin
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public JSONObject post(@RequestBody String filterJSON, HttpServletRequest request, HttpServletResponse response) {
		result = new JSONObject();
		ObjectMapper objectMapper = new ObjectMapper();
		UsersVO vo = null;
		try {
			vo = objectMapper.readValue(filterJSON, UsersVO.class);
			log.writelog(1, false, "sign-up", "user-info : " + vo.toString());
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			log.writelog(2, false, "sign-up", "msg : " + e.getMessage());
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			log.writelog(2, false, "sign-up", "msg : " + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.writelog(2, false, "sign-up", "msg : " + e.getMessage());
		}

		if (vo != null) {
			if (vo.getSex().equals("1") || vo.getSex().equals("3")) {
				vo.setSex("남자");
			} else if (vo.getSex().equals("2") || vo.getSex().equals("4")) {
				vo.setSex("여자");
			}

			if (dao.insert(vo)) {
				result.put("response-status", "200");
				result.put("response-message", "success");
			} else {
				result.put("response-status", "500");
				result.put("response-message", "failed");
			}
			log.writelog(1, false, "sign-up", "result : " + result.toString());
		}
		return result;
	}
}