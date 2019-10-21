package com.safe.traveler.service;

import org.json.simple.JSONObject;

public interface TokenService {
	JSONObject generateToken(String name);

	JSONObject deleteToken(String name);

	JSONObject checkValidatedToken(Boolean isName, String value);
}
