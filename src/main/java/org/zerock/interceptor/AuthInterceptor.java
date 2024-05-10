package org.zerock.interceptor;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;
import org.zerock.domain.UserVO;
import org.zerock.service.UserService;

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


//  사용자가 로그인 한 상태인지 체크
public class AuthInterceptor extends HandlerInterceptorAdapter{
	
	private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
	
	//login이 존재하지 않지만 쿠키중에 loginCookie 가 존재할 때 처리 
	@Inject
	private UserService service; //UserService 서비스타입을 주입 받는다.

	// 로그인 성공 후 원래의 URI로 이동시시키
	private void saveDest(HttpServletRequest req) {
		
		String uri = req.getRequestURI(); //  ex URI:   /sboard/register   
		
		String query = req.getQueryString(); //파라메타 ex query: page=1&perPageNum=10&searchType=&keyword=
		
		if(query == null || query.equals("null")) {
			query = ""; // query 가 null이면  "" 으로 변경
		}else {
			query = "?" + query;
		}
		
		if(req.getMethod().equals("GET")) { // GET 타입이면  uri + query dest 로 세션에 담아서 반환
			logger.info("dest:" + (uri + query));
			req.getSession().setAttribute("dest", uri + query);
		}
	}	
	
	
	//	preHandle() - 컨트롤러 메소드 실행 직전에 수행됩니다. true 를 반환하면 계속 진행이 되고  false 를 리턴하면 실행 체인(다른 인터셉터, 컨트롤러 실행)이 중지되고 반환 됩니다.  필터의 응답 처리가 있다면 그것은 실행이 됩니다.	
	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response ,Object handler) throws Exception{
		//Object handler 현재 실행하려는 메소드 자체를 의미
		
		HttpSession session = request.getSession();
		
		if(session.getAttribute("login") == null) {
			
			logger.info("current user is not logined");
			
			saveDest(request); //원래있던페이지의 URI로 이동시킨다.
			
			Cookie loginCookie = WebUtils.getCookie(request, "loginCookie"); //loginCookie 불러오기
			
			//WebUtils 클래스를 사용하면 Session에 담겨있는 객체들을 보다 짧은 코드로 넣고 빼고 할 수 있으며, 세션 객체나 쿠키 객체를 받아올 수 있습니다.	
			//UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
			//UserSession userSession = (UserSession) WebUtils.getSessionAttribute(request, "userSession");

			if(loginCookie != null) { //만약에 쿠기가 있다면
				UserVO userVO = service.checkLoginBefore(loginCookie.getValue());
				//세션아디로 사용자 정보를 조회해서 로그인
				
				logger.info("USERVO" + userVO);
				
				if(userVO != null) {
					session.setAttribute("login", userVO); //사용자정보가 있다면 세션에 기록
					return true;
				}

			}
			
			response.sendRedirect("/user/login"); // 로그인하지 않은 상태라면 로그인 페이지로 이동
			return false;
		}
		return true;
	}
}
