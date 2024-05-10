package org.zerock.controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageMaker;
import org.zerock.domain.SearchCriteria;
import org.zerock.service.BoardService;

@Controller
@RequestMapping("/sboard/*")
public class SearchBoardController {
	
	private static final Logger logger = LoggerFactory.getLogger(SearchBoardController.class);
	
	@Inject
	private BoardService service;
	
	@RequestMapping(value = "/list", method= RequestMethod.GET) //Criteria 로부터 상속받은 SearchCriteria 사용
	public void listPage(@ModelAttribute("cri") SearchCriteria cri , Model model)throws Exception {
		logger.info(cri.toString());
		
		System.out.println("키워드:" + cri.getKeyword());
		//model.addAttribute("list",service.listCriteria(cri));
		model.addAttribute("list",service.listSearchCriteria(cri)); //list 목록 넣기
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri); //페이지 계산
		
		pageMaker.setTotalCount(service.listSearchCount(cri));	//	검색어의 총갯수를 가지고와서 pageMaker 객체를 통해서 게시물을 갯수 계산
		model.addAttribute("pageMaker",pageMaker); //페이지 갖고오기
	}
	
	//읽기
	@RequestMapping(value = "/readPage", method = RequestMethod.GET)
	public void readPage(@RequestParam("bno") int bno, @ModelAttribute("cri") SearchCriteria cri, Model model) throws Exception{ //@ModelAttribute - 여러 곳(URI 패스, 요청 매개변수, 세션 등)에 있는 단순 타입 데이터를 복합 타입 객체로 받아오거나 해당 객체를 새로 만들 때 사용할 수 있다.
		
		model.addAttribute(service.read(bno)); //객체명이 BoardVO 클래스의 객체이므로 boardVO라는 이름으로 저장
	}
	
	//수정페이지 읽기
	@RequestMapping(value="/modifyPage", method=RequestMethod.GET)
	public void modifyPage(int bno, @ModelAttribute("cri") SearchCriteria cri, Model model) throws Exception {
		model.addAttribute(service.read(bno));
	}
	//수정페이지 수정
	@RequestMapping(value="/modifyPage", method=RequestMethod.POST)
	public String modifyPage(BoardVO board, SearchCriteria cri, RedirectAttributes rttr)throws Exception{
		
		logger.info(cri.toString());
		service.modify(board);
		
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		rttr.addAttribute("SearchType",cri.getSearchType());
		rttr.addAttribute("Keyword",cri.getKeyword());
		rttr.addFlashAttribute("msg","success");
		
		return "redirect:/sboard/list";
	}
	// 동록
	@RequestMapping(value = "/register", method= RequestMethod.GET)
	public void registerGET(BoardVO board, SearchCriteria cri , Model model) throws Exception{
		logger.info("register get.....................");
		
		model.addAttribute("page",cri.getPage());
		model.addAttribute("perPageBum", cri.getPerPageNum());
		model.addAttribute("SearchType",cri.getSearchType());
		model.addAttribute("keyword",cri.getKeyword());
	}
	
	//등록
	@RequestMapping(value = "/register", method= RequestMethod.POST)
	public String registPOST(BoardVO board, RedirectAttributes rttr)throws Exception {
		//RedirectAttributes 객체는 리다이렉트 시점에 한번만 사용되는 데이터를 전송할 수 있는 addFlashAttribute라는 기능을 지원합니다. RedirectAttributes는 페이지를 다시 이동할때 데이터를 전달하기위해서 사용됨
		logger.info("regist post.......................");
		logger.info(board.toString());
		
		service.regist(board);
		
		rttr.addFlashAttribute("msg","success");
		
		//return "/board/success";
		return "redirect:/sboard/list";
	}
	
	//삭제
	@RequestMapping(value="/removePage", method=RequestMethod.POST)
	public String removePage(@RequestParam("bno") int bno, SearchCriteria cri, RedirectAttributes rttr) throws Exception{ //RedirectAttributes 임시한번의 메시지 전달
		service.remove(bno);
		
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		rttr.addAttribute("SearchType", cri.getSearchType());
		rttr.addAttribute("keyword", cri.getKeyword());
		rttr.addFlashAttribute("msg", "success");
		
		return "redirect:/sboard/list";
	}
	//첨푸파일을 처리하는 메소드
	@RequestMapping("/getAttach/{bno}")
		@ResponseBody
		public List<String> getAttach(@PathVariable("bno") Integer bno)throws Exception{
		
		return service.getAttach(bno);
	}
		

}
