package org.zerock.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
//RestController 샘플
@RestController            //해당컨트롤러의 모든 뷰 처리가 JSP가 아니라는 것을 의미합니다.  @ResponseBody 없어도 동일하게 동작 생략되었다고해도 무방
@RequestMapping("/sample")
public class SampleController {
	
	@RequestMapping("hello")
	public String sayHello() {
		return "hello wordl";
	}

}
