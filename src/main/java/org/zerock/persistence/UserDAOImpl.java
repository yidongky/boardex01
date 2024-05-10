package org.zerock.persistence;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.stereotype.Repository;
import org.zerock.domain.UserVO;
import org.zerock.dto.LoginDTO;

@Repository //DAO를 스프링에 인식시키기 위해서  사용
public class UserDAOImpl implements UserDAO {
	
	@Inject
	private SqlSession session;
	
	static String namespace = "org.zerock.mapper.UserMapper";

	//로그인
	@Override
	public UserVO login(LoginDTO dto) throws Exception {
		
		return session.selectOne(namespace + ".login" ,dto);
	}
	//sessionKey ,sessionLimit 업데이트
	@Override
	public void keepLogin(String uid, String sessionId, Date next) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("uid", uid);
		paramMap.put("sessionId", sessionId);
		paramMap.put("next", next);
		
		session.update(namespace + ".keepLogin",paramMap);
	}
	//loginCookie 에 기록된 값으로 사용자 정보 조회기능
	@Override
	public UserVO CheckUserWithSessionkey(String value) {
		
		return session.selectOne(namespace + ".checkUserWithSessionKey", value);
	}

}
