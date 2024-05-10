package org.zerock.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/*
 *  예외 처리하기
 * 클래스에 @ControllerAdvice 애노테이션 처리
 * 각 매소드에 @ExceptionHandler를 이용해서 적절한 타입의 Exception 처리
 * 
 */
@ControllerAdvice // CommonExceptionAdvice Exception을 전문적으로 처리하는 클래스라는 것을 명시
public class CommonExceptionAdvice {
	private static final Logger logger = LoggerFactory.getLogger(CommonExceptionAdvice.class);

	@ExceptionHandler(Exception.class) // 적절한 타입의 Exception을 처리
	private ModelAndView common(Exception ex) { // common() 메소드를 이용해서 Exception 타입으로 처리되는 모든 예외를 처리하도록 설정
		// ModelAndView Model데이터와 view의 처리를 동시에 할있는 객체
		ModelAndView mdoelAndView = new ModelAndView();
		mdoelAndView.setViewName("/error_common");
		mdoelAndView.addObject("exception", ex);

		return mdoelAndView;
	}
}
