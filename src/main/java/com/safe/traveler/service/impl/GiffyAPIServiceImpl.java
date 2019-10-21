package com.safe.traveler.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safe.traveler.dao.AdminDAO;
import com.safe.traveler.dao.api.GiffyAPIDAO;
import com.safe.traveler.vo.DateVO;
import com.safe.traveler.vo.api.GiffyAPIVO;

@Service
public class GiffyAPIServiceImpl {
	@Autowired
	AdminDAO adminDAO;

	@Autowired
	ResponseServiceImpl responseImpl;

	@Autowired
	GiffyAPIDAO giffyDAO;

	GiffyAPIVO giffyVO;

	JSONObject result;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm", Locale.KOREA);

	@SuppressWarnings("unchecked")
	public JSONObject getInfoBySingleIsoCode(String countryName) {
		try {
			String isoCode = adminDAO.convertNameToCode(countryName);
			giffyVO = giffyDAO.getInfoByIsoCode(isoCode);
			result = responseImpl.generateSingleIsoCode(giffyVO);
			if (result.get("response-message") == null) {
				if (adminDAO.checkIsoCodeValidation(isoCode))
					result.put("response-message", adminDAO.convertCodeToName(isoCode) + "은(는) 안전한 나라입니다");
				else {
					result.put("response-message", adminDAO.convertCodeToName(isoCode) + "은(는) 안전한 나라입니다");
					Exception e1 = new Exception();
					throw e1;
				}

			}
			result.put("response-status", "200");
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.put("response-status", "404");
			result.put("response-message", "check your URL or token-validation");
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	public JSONObject getInfoByMultipleIsoCode(List<String> isoCodeList) {
		try {
			List<GiffyAPIVO> list = new ArrayList<GiffyAPIVO>();
			String isoCode;
			for (String temp : isoCodeList) {
				isoCode = adminDAO.convertNameToCode(temp);
				giffyVO = giffyDAO.getInfoByIsoCode(isoCode);
				list.add(giffyVO);
			}
			result = responseImpl.generateMultipleIsoCode(list);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.put("response-status", "404");
			result.put("response-message", "check URL or token-validation");
			return result;
		}
	}

	public JSONObject getInfoByDate(String startDate, String endDate) {
		if (startDate == null)
			startDate = "2009-01-01";
		if (endDate == null)
			endDate = sdf.format(new Date());
		giffyVO = giffyDAO.getInfoByTime(new DateVO(startDate, endDate));
		return responseImpl.generateDate(giffyVO, 200);
	}
}
