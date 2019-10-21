package com.safe.traveler.dao.user;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.safe.traveler.vo.user.UsersVO;

@Repository
public class RegisterDAO {
	@Autowired
	SqlSession session = null;

	public boolean insert(UsersVO vo) {
		String statement = "RegisterMapper.insert";
		if (session.insert(statement, vo) != 1)
			return false;
		return true;
	}

	public boolean searchId(String id) {
		boolean check = false;
		List<UsersVO> list = null;
		String statement = "RegisterMapper.searchid";
		list = session.selectList(statement, id);
		if (list.size() > 0) {
			check = true;
		}

		return check;
	}

	public boolean searchNickname(String nickname) {
		boolean check = false;
		List<UsersVO> list = null;
		String statement = "RegisterMapper.searchNickname";
		list = session.selectList(statement, nickname);
		if (list.size() > 0) {
			check = true;
		}

		return check;
	}

	public boolean searchEmail(String email) {
		boolean check = false;
		List<UsersVO> list = null;
		String statement = "RegisterMapper.searchEmail";
		list = session.selectList(statement, email);
		if (list.size() > 0) {
			check = true;
		}

		return check;
	}

	public int checkEmail(String email) {
		int count = 0;
		List<UsersVO> list = null;
		String statement = "RegisterMapper.searchEmail";
		list = session.selectList(statement, email);
		if (list.size() > 0) {
			count++;
		}

		return count;
	}

	public boolean checkIdReduplication(UsersVO vo) {
		/*
		 * String statement = "AuthMapper.checkIdReduplication";
		 */ String statement = "RegisterMapper.searchEmail";

		boolean result = true;
		System.out.println(vo.toString());
		if (session.selectOne(statement, vo) != null)
			result = false;
		return result;
	}

}
