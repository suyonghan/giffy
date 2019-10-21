package com.safe.traveler.dao.user;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safe.traveler.vo.user.UsersVO;

@Repository
public class LoginDAO {
	@Autowired
	SqlSession session = null;

	public UsersVO login(UsersVO vo) {
		UsersVO userVO = null;
		String statement = "LoginMapper.login";
		userVO = session.selectOne(statement, vo);
		return userVO;
	}
}
