package org.zerock.controller;
/*
 * HttpServletRequest
Http프로토콜의 request 정보를 서블릿에게 전달하기 위한 목적으로 사용
Header정보, Parameter, Cookie, URI, URL 등의 정보를 읽어들이는 메소드를 가진 클래스
Body의 Stream을 읽어들이는 메소드를 가지고 있음
HttpServletResponse
Servlet은 HttpServletResponse객체에 Content Type, 응답코드, 응답 메시지등을 담아서 전송함
 * 
 * 
 */
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;
import org.zerock.domain.UserVO;
import org.zerock.dto.LoginDTO;
import org.zerock.service.UserService;

@Controller
@RequestMapping("/user/*")
public class UserController {
	
	@Inject UserService service;
	
	//로그인 화면
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public void loginGET(@ModelAttribute("dtd") LoginDTO dtd) {
		
	}
	//로그인 결과 처리
	@RequestMapping(value = "/loginPost", method = RequestMethod.POST)
	public void loginPOST(LoginDTO dto, HttpSession session, Model model) throws Exception {
		 //HttpSession과 관련된 아무런 작업도 처리된 적이 없기 때문에,HttpSession에 관련된 모든 설정은 고스란히 인터셉터에서 처리됩니다.
		UserVO vo = service.login(dto);
		
		if(vo == null) {
			return;
		}
		model.addAttribute("userVO", vo); // 사용자가 존재하는 경우에는 'userVO'라는 이름으로 저장
		
		//자동로그인 loginCookie 값이 유지되는 시간 정보를 데이터베이스에 저장
		if(dto.isUseCookie()) {
			int amount = 60 * 60 * 24 * 7; //일주일 초*분*시간*요일 초단위로 계산
			Date sessionLimit = new Date(System.currentTimeMillis()+(1000*amount));
			// 자동로그인 클릭하고 로그인시 해당 userID에 sessionID sessionLimit에 시간기록
			service.KeepLogin(vo.getUid(), session.getId(), sessionLimit);
		}
		
	}
	
	//로그아웃
	@RequestMapping(value= "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, 
			HttpServletResponse response, HttpSession session) throws Exception{
		
		Object obj = session.getAttribute("login");
		
		  if (obj != null) {
			  UserVO vo = (UserVO) obj;

			  session.removeAttribute("login"); //세션에서 login 객체 제거
			  session.invalidate(); //세션 비우기
			

			 Cookie loginCookie = WebUtils.getCookie(request, "loginCookie"); //쿠키 가져오기;
			
			  if (loginCookie != null) { //쿠기가 null 이 아니라면

				  loginCookie.setPath("/"); // 쿠키의 유효한 디렉토리를 "/" 로 설정한다.
				  loginCookie.setMaxAge(0); //쿠기 유효기간 초기화
				  response.addCookie(loginCookie); ////쿠키 객체를 웹 브라우저로 보낸다.
				  service.KeepLogin(vo.getUid(), session.getId(), new Date()); //user 디비에 시간 업데이트
				  }
		}
		  return "user/logout";
	}
}
