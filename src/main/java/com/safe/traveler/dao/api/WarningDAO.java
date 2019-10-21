package com.safe.traveler.dao.api;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safe.traveler.vo.api.WarningVO;

@Repository
public class WarningDAO {
	@Autowired
	SqlSession session = null;

	public List<WarningVO> getWarningByIsoCode(String isoCode) {
		return session.selectList("warningMapper.getWarningByIsoCode", isoCode);
	}

	public boolean insertWarning(WarningVO warning) {
		try {
			List<WarningVO> list = this.getWarningByIsoCode(warning.getIsoCode());
			if (list != null) {
				WarningVO temp = list.get(0);
				if (temp.getWid() == warning.getWid())
					return false;
				else {
					if (list.size() == 2) {
						temp = list.get(1);
						if (temp.getWid() == warning.getWid())
							return false;
					}
				}
			}
			int result = session.insert("warningMapper.insertWarning", warning);
			if (result == 1)
				return true;
			else
				return false;

		} catch (Exception e) {
			// e.printStackTrace();
			return false;
			// TODO: handle exception
		}
	}

	public boolean deleteWarning(String wid) {
		int result = session.delete("warningMapper.deleteWarning", wid);
		if (result == 1)
			return true;
		else
			return false;
	}
}
