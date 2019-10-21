package com.safe.traveler.dao.api;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safe.traveler.vo.DateVO;
import com.safe.traveler.vo.api.SafetyVO;

@Repository
public class SafetyDAO {

	@Autowired
	SqlSession session = null;

	private DateVO date;
	
	public SafetyVO getSafetyById(String sid) {
		return session.selectOne("safetyMapper.getSafetyBySid", sid);
	}

	public List<SafetyVO> getSafetyByIsoCode(String isoCode) {
		return session.selectList("safetyMapper.getSafetyByIsoCode", isoCode);
	}

	public List<SafetyVO> getSafetyByDate(DateVO date) {
		return session.selectList("safetyMapper.getSafetyByDate", date);
	}

	public boolean insertSafety(SafetyVO safety) {
		try {
			SafetyVO temp = this.getSafetyById(safety.getSid());
			if (temp != null && temp.getSid() == safety.getSid()) {
				return false;
			} else {
				int result = session.insert("safetyMapper.insertSafety", safety);
				if (result == 1)
					return true;
				else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteSafety(String sid) {
		int result = session.delete("safetyMapper.deleteSafety", sid);
		if (result == 1)
			return true;
		else
			return false;
	}
}
