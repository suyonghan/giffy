package com.safe.traveler.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safe.traveler.dao.AdminDAO;
import com.safe.traveler.dao.api.TokenDAO;
import com.safe.traveler.service.TokenService;
import com.safe.traveler.vo.api.TokenVO;

@Service
public class TokenServiceImpl implements TokenService {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm", Locale.KOREA);
	String currentDate, expiryDate;

	@Autowired
	private TokenDAO tokenDAO;

	@Autowired
	private ResponseServiceImpl responseImpl;

	public String getServiceKey(String name) {
		TokenVO token = tokenDAO.getTokenByName(name);
		if (token == null)
			return null;
		else
			try {
				return AESEncryptor.decrypt(token.getServiceKey());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return null;
			}
	}

	@Override
	public JSONObject generateToken(String name) {
		String serviceKey = null;
		try {
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(secureRandom.generateSeed(128));
			serviceKey = secureRandom.nextLong() + "";
		} catch (NoSuchAlgorithmException e) {
			return responseImpl.generateTokenResponse(500, null);// "failed, server error";
		}

		currentDate = sdf.format(new Date());
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(currentDate));
		} catch (ParseException e) {
			e.printStackTrace();
			return responseImpl.generateTokenResponse(500, null);
		}
		cal.add(Calendar.HOUR, 1);

		expiryDate = sdf.format(cal.getTime());
		TokenVO token;
		try {
			token = new TokenVO(name, currentDate, expiryDate, AESEncryptor.encrypt(serviceKey), "active");
			if (tokenDAO.generateToken(token))
				return responseImpl.generateTokenResponse(200, token);
			else
				return responseImpl.generateTokenResponse(500, null);
		} catch (Exception e) {
			e.printStackTrace();
			return responseImpl.generateTokenResponse(500, null);
		}
	}

	@Override
	public JSONObject deleteToken(String name) {
		if (tokenDAO.deleteToken(name))
			return responseImpl.generateTokenResponse(200, null);
		else
			return responseImpl.generateTokenResponse(404, null);
	}

	@Override
	public JSONObject checkValidatedToken(Boolean isName, String value) {
		TokenVO token = null;
		if (isName) {
			token = tokenDAO.getTokenByName(value);
		}

		else
			try {
				token = tokenDAO.getTokenByServiceKey(AESEncryptor.encrypt(value));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		if (token != null) {
			System.out.println(token.toString());
			if (token.getState().equals("expiration"))
				return responseImpl.generateTokenResponse(403, null);
			else {
				expiryDate = token.getExpiryDate();
				currentDate = sdf.format(new Date());
				try {
					if (sdf.parse(expiryDate).getTime() <= sdf.parse(currentDate).getTime()) {
						tokenDAO.updateTokenState(token);
						return responseImpl.generateTokenResponse(403, null);
					} else {
						return responseImpl.generateTokenResponse(200, token);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return responseImpl.generateTokenResponse(500, null);
				}
			}
		} else {
			return responseImpl.generateTokenResponse(404, null);
		}
	}
}
