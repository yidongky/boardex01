package org.zerock.service;

import java.util.Date;

import org.zerock.domain.UserVO;
import org.zerock.dto.LoginDTO;

public interface UserService {
	//로그인
	public UserVO login(LoginDTO dto) throws Exception;
	//sessionKey ,sessionLimit 업데이트
	//sessionKey : 로그인 하는 시점의 세션아이디 , sessionLimit:  유효한 기간에 다시 접속을 했는지를 판단하기위한컬럼
	public void KeepLogin(String uid, String sessionId, Date next)throws Exception;
	//loginCookie 에 기록된 값으로 사용자 정보 조회기능
	public UserVO checkLoginBefore(String value);
}
