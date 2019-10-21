package com.safe.traveler.dao.user;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DeleteDAO {

	@Autowired
	SqlSession session = null;
	public boolean deleteUser(String email) {
		int result;
		String statement = "DeleteMapper.deleteUser";
		result = session.delete(statement, email);
		if(result == 1) {
			return true;
		}else {
			return false;
		}
	}
}
