package org.zerock.service;

import java.util.Date;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.zerock.domain.UserVO;
import org.zerock.dto.LoginDTO;
import org.zerock.persistence.UserDAO;

@Service
public class UserServiceImpl implements UserService {
	
	
	@Inject //객체 생성
	private UserDAO dao; 

	//로그인
	@Override
	public UserVO login(LoginDTO dto) throws Exception {

		return dao.login(dto);
	}
	//sessionKey ,sessionLimit 업데이트
	@Override
	public void KeepLogin(String uid, String sessionId, Date next) throws Exception {
		dao.keepLogin(uid, sessionId, next);
		
	}
	//loginCookie 에 기록된 값으로 사용자 정보 조회기능
	@Override
	public UserVO checkLoginBefore(String value) {

		return dao.CheckUserWithSessionkey(value);
	}

}
