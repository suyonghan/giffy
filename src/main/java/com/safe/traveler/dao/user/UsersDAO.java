package com.safe.traveler.dao.user;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safe.traveler.vo.user.UsersVO;

@Repository
public class UsersDAO {
	@Autowired
	SqlSession session = null;
	
	public String selectEmail(String nickname) {
		UsersVO temp = null;
		String statement = "UserMapper.selectEmail";
		temp = session.selectOne(statement, nickname);
		return temp.getEmail();
	}
	
	public String selectNickname(String email) {
		UsersVO temp = null;
		String statement = "UserMapper.selectNickname";
		temp = session.selectOne(statement, email);
		return temp.getNickname();
	}
	
	public String selectAdmin(String email) {
		if(email != null) {
			UsersVO temp = null;
			String statement = "UserMapper.selectName";
			temp = session.selectOne(statement, email);
			return temp.getName();
		} else {
			return null;
		}
	}
	
	public boolean updatePassword(UsersVO vo) {
		boolean result = true;
		String statement = "UserMapper.updatePassword";
		if(session.update(statement, vo) != 1) {
			result = false;
		}
		return result;
	}
}
