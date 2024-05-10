package org.zerock.interceptor;



import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.zerock.controller.CommonExceptionAdvice;
//인터셉터 설정  // servlet-context.xml 네임스페이스에서 spring mvc관련 설정이 추가되어 있어야한다.

//preHandle(request, response, handler) -지정된 컨트롤러의 동작 이전에 가로채는 역하롤 사용
//postHandle(requet, response, handler, modelAndView) - 지정된 컨트롤러의 동작 이후에 처리 ,Spring MVC의 Front Controller인 DispatcherServlet 이화면
//을 처리하기 전에 동작
//afterCompletion(request,response,handler,exception) - DispatcherServlet 의 화면 처리가 완료된 상태에서 처리
/*
*  1. preHandle() - 컨트롤러 메소드 실행 직전에 수행됩니다. true 를 반환하면 계속 진행이 되고  false 를 리턴하면 실행 체인(다른 인터셉터, 컨트롤러 실행)이 중지되고 반환 됩니다.  필터의 응답 처리가 있다면 그것은 실행이 됩니다.
	2. postHandle() - 컨트롤러 메소드 실행 직후에 실행 됩니다. View 페이지가 렌더링 되기전에   ModelAndView 객체를 조작 할 수 있습니다.
	3. afterCompletion() -  View 페이지가 렌더링 되고 난 후 에 실행됩니다.
	4. afterConcurrentHandlingStarted() - Servlet 3.0 부터 비동기 요청이 가능해 졌습니다.   비동기 요청시 postHandle와 afterCompletion 은 실행되지 않고, 이 메소드가 실행됩니다.
*/

public class LoginInterceptor extends HandlerInterceptorAdapter {
	
	private static final String LOGIN = "login";
	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
// postHandle() - 컨트롤러 메소드 실행 직후에 실행 됩니다. View 페이지가 렌더링 되기전에   ModelAndView 객체를 조작 할 수 있습니다.	
	@Override
	public void postHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler, ModelAndView modelAndView)throws Exception{
		
		HttpSession session =request.getSession(); // HttpSession이 존재하면 현재 HttpSession을 반환하고 존재하지 않으면 새로 세션을 생성합니다.
		
		ModelMap modelMap = modelAndView.getModelMap();
		Object userVO = modelMap.get("userVO"); //userVO 대한 정보를 가져온다
		
/*		이렇게도 사용가능
 * 		Map<String, Object> model = modelAndView.getModel();
		Object userVO = model.get("userVO"); //userVO 대한 정보를 가져온다
		
		차이점
		Model - 인터페이스 ModelMap - 클래스		
 */
		
		if(userVO != null) { //로그인 성공시
			logger.info("new login success");
			session.setAttribute(LOGIN, userVO);
			//쿠키 생성
		      if (request.getParameter("useCookie") != null) { //useCookie에 체크가 되어있다면 쿠키생성

		          logger.info("remember me................");
		          Cookie loginCookie = new Cookie("loginCookie", session.getId());
		          loginCookie.setPath("/");
		          loginCookie.setMaxAge(60 * 60 * 24 * 7); //setMaxAge 초단의 시간동안 쿠키 유호 
		          response.addCookie(loginCookie);
		        }			
			//response.sendRedirect("/");
			Object dest = session.getAttribute("dest");
			
			response.sendRedirect(dest != null ? (String)dest : "/sboard/list");
			//dest 의 경로가 null 이아니면 dest의 경로를 찾아서 sendRedirect 아니면  / 경로로 이동
		}
		
	}
//	preHandle() - 컨트롤러 메소드 실행 직전에 수행됩니다. true 를 반환하면 계속 진행이 되고  false 를 리턴하면 실행 체인(다른 인터셉터, 컨트롤러 실행)이 중지되고 반환 됩니다.  필터의 응답 처리가 있다면 그것은 실행이 됩니다.
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		//로그인전에 남아 있는정보가 있다면 삭제
		HttpSession session = request.getSession();
		
		if(session.getAttribute(LOGIN) != null) {
			logger.info("clear login data before");
			session.removeAttribute(LOGIN); //로그인전에 남아 있느 세션정보가 있다면 삭제
		}
		
		return true;
		
	}

}
