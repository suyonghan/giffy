package com.safe.traveler.controller.users;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.safe.traveler.dao.user.LoginDAO;
import com.safe.traveler.dao.user.UsersDAO;
import com.safe.traveler.vo.user.UsersVO;

@RestController
@SessionAttributes("user")
public class MypageController {
	@Autowired
	LoginDAO loginDAO;
	@Autowired
	UsersDAO userDAO;

	JSONObject result;
	
	@SuppressWarnings("unchecked")
	@CrossOrigin
	@RequestMapping(value = "/mypage", method = RequestMethod.POST)
	public JSONObject postMypage(UsersVO vo) {
		result = new JSONObject();
		UsersVO info = loginDAO.login(vo);
		if (info != null) {
			result.put("response-status", "200");
			result.put("response-message", "success");
		} else {
			result.put("response-status", "200");
			result.put("response-message", "fail");
		}
		return result;
	}
}
