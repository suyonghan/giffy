package com.safe.traveler.dao.api;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safe.traveler.vo.api.TokenVO;

@Repository
public class TokenDAO {
	@Autowired
	SqlSession session = null;

	public TokenVO getTokenByName(String userName) {
		return session.selectOne("tokenMapper.getTokenByName", userName);
	}

	public TokenVO getTokenByServiceKey(String serviceKey) {
		return session.selectOne("tokenMapper.getTokenByServiceKey", serviceKey);
	}
	
	public boolean generateToken(TokenVO token) {
		TokenVO temp = session.selectOne("tokenMapper.getTokenByName", token.getUserName());
		if (temp != null)
			deleteToken(temp.getUserName());

		int result = session.insert("tokenMapper.insertToken", token);
		if (result == 1)
			return true;
		else
			return false;

	}

	public boolean updateTokenState(TokenVO token) {
		int result = session.update("tokenMapper.updateToken", token);
		if (result == 1) {
			return true;
		} else {
			return true;
		}
	}

	public boolean deleteToken(String name) {
		int result = session.delete("tokenMapper.deleteToken", name);
		if (result == 1)
			return true;
		else
			return false;
	}
}
