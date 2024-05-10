package org.zerock.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//인터셉터 설정  // servlet-context.xml 네임스페이스에서 spring mvc관련 설정이 추가되어 있어야한다.

// preHandle(request, response, handler) -지정된 컨트롤러의 동작 이전에 가로채는 역하롤 사용
// postHandle(requet, response, handler, modelAndView) - 지정된 컨트롤러의 동작 이후에 처리 ,Spring MVC의 Front Controller인 DispatcherServlet 이화면
// 을 처리하기 전에 동작
// afterCompletion(request,response,handler,exception) - DispatcherServlet 의 화면 처리가 완료된 상태에서 처리
/*
 *  1. preHandle() - 컨트롤러 메소드 실행 직전에 수행됩니다. true 를 반환하면 계속 진행이 되고  false 를 리턴하면 실행 체인(다른 인터셉터, 컨트롤러 실행)이 중지되고 반환 됩니다.  필터의 응답 처리가 있다면 그것은 실행이 됩니다.
	2. postHandle() - 컨트롤러 메소드 실행 직후에 실행 됩니다. View 페이지가 렌더링 되기전에   ModelAndView 객체를 조작 할 수 있습니다.
	3. afterCompletion() -  View 페이지가 렌더링 되고 난 후 에 실행됩니다.
	4. afterConcurrentHandlingStarted() - Servlet 3.0 부터 비동기 요청이 가능해 졌습니다.   비동기 요청시 postHandle와 afterCompletion 은 실행되지 않고, 이 메소드가 실행됩니다.
 */
public class SampleInterceptor extends HandlerInterceptorAdapter{
	
	@Override
	public void postHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception{
		System.out.println("post handle............");
		
		Object result =modelAndView.getModel().get("result");
		
		if(result != null) {
			request.getSession().setAttribute("result", result);
			System.out.println("result:"+result);
			response.sendRedirect("/doA");
		}
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception { //Object handler 현재 실행하려는 메소드 자체를 의미
		
		System.out.println("pre handle..............");
		
		HandlerMethod method = (HandlerMethod) handler;
		//HandlerMethod 는 후에 실행할 컨트롤러의 메소드 입니다. 
		// 메소드명, 인스턴스, 타입, 사용된 Annotation 들을 확인할 수 있습니다.
		Method methodObj = method.getMethod(); //메소드
		
		System.out.println("Bean:" + method.getBean()); //객체(빈)
		System.out.println("Method:" + methodObj); //메소드
		
		return true; //preHandle()의 경우 리턴 타입이 boolean으로 설계합니다. 이를이용해서 다음 Interceptor나 대상 컨트롤러를 호출하도록 할 것인지를 결정합니다.
	}
}
