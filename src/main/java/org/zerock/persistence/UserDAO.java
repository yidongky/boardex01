package org.zerock.persistence;

import java.util.Date;

import org.zerock.domain.UserVO;
import org.zerock.dto.LoginDTO;

public interface UserDAO {
	
	public UserVO login(LoginDTO dto)throws Exception;
	
	public void keepLogin(String uid, String sessionId, Date next); //sessionKey ,sessionLimit 업데이트
	
	public UserVO CheckUserWithSessionkey(String value); //loginCookie 에 기록된 값으로 사용자 정보 조회기능

}
