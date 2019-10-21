package com.safe.traveler.controller.api;

import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.safe.traveler.service.impl.LogService;
import com.safe.traveler.service.impl.TokenServiceImpl;
import com.safe.traveler.vo.user.UsersVO;

/**
 * Handles requests for the application home page.
 */
@RestController
public class TokenController {
	@Autowired
	TokenServiceImpl t;
	@Autowired
	LogService log;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@CrossOrigin
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "home";
	}

	@CrossOrigin
	@RequestMapping(value = "/generateToken", method = RequestMethod.GET)
	public JSONObject generateToken(UsersVO user) {
		log.writelog(1, true, "generate Token", "user info : " + user.toString());
		return t.generateToken(Integer.toString(user.getUserNo()));
	}

	@CrossOrigin
	@RequestMapping(value = "/generateToken", method = RequestMethod.POST)
	public JSONObject generateTokenById(UsersVO user, HttpServletResponse response) {
		log.writelog(1, false, "generate Token", "user info : " + user.toString());
		return t.generateToken(Integer.toString(user.getUserNo()));
	}

	@CrossOrigin
	@RequestMapping(value = "/checkValidateToken", method = RequestMethod.GET)
	public JSONObject checkValidateTokenGet(String serviceKey) {
		log.writelog(1, true, "check token validation", "serviceKey : " + serviceKey);
		return t.checkValidatedToken(false, serviceKey);
	}

	@CrossOrigin
	@RequestMapping(value = "/checkValidateToken", method = RequestMethod.POST)
	public JSONObject checkValidateTokenPost(UsersVO user) {
		log.writelog(1, false, "check token validation", "user-info : " + user.toString());
		return t.checkValidatedToken(true, Integer.toString(user.getUserNo()));
	}

	@CrossOrigin
	@RequestMapping(value = "/deleteToken", method = RequestMethod.GET)
	public JSONObject deleteToken(String userNo) {
		return t.deleteToken(userNo);
	}
}
