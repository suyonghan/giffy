package com.safe.traveler.dao.api;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safe.traveler.dao.AdminDAO;
import com.safe.traveler.vo.DateVO;
import com.safe.traveler.vo.api.GiffyAPIVO;

@Repository
public class GiffyAPIDAO {
	@Autowired
	SqlSession session;

	@Autowired
	AdminDAO adminDAO;

	@Autowired
	WarningDAO warning;

	@Autowired
	SafetyDAO safe;

	private GiffyAPIVO result;

	public GiffyAPIVO getInfoByIsoCode(String isoCode) {
		result = new GiffyAPIVO();
		result.setIsoCode(isoCode);
		result.setCountryName(adminDAO.convertCodeToName(isoCode));
		result.setSafetyList(safe.getSafetyByIsoCode(isoCode));
		result.setWarningList(warning.getWarningByIsoCode(isoCode));
		return result;
	}

	public GiffyAPIVO getInfoByTime(DateVO date) {
		result = new GiffyAPIVO();
		result.setSafetyTimeList(safe.getSafetyByDate(date));
		return result;
	}
}
