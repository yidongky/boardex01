package org.zerock.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageMaker;
import org.zerock.service.BoardService;

/*
 * @Controller 
 * @RequestMapping
 * 	@Inject service
 */

@Controller
@RequestMapping("/board/*")
public class BoardController {
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Inject
	private BoardService service;
	// 동록
	@RequestMapping(value = "/register", method= RequestMethod.GET)
	public void registerGET(BoardVO board, Model model) throws Exception{
		logger.info("register get.....................");
	}
/*	
	@RequestMapping(value = "/register", method= RequestMethod.POST)
	public String registPOST(BoardVO board, Model model)throws Exception {
		
		logger.info("regist post.......................");
		logger.info(board.toString());
		
		service.regist(board);
		
		model.addAttribute("result","success");
		
		//return "/board/success";
		return "redirect:/board/listAll";
	}
*/	
	//등록
	@RequestMapping(value = "/register", method= RequestMethod.POST)
	public String registPOST(BoardVO board, RedirectAttributes rttr)throws Exception {
		//RedirectAttributes 객체는 리다이렉트 시점에 한번만 사용되는 데이터를 전송할 수 있는 addFlashAttribute라는 기능을 지원합니다. RedirectAttributes는 페이지를 다시 이동할때 데이터를 전달하기위해서 사용됨
		logger.info("regist post.......................");
		logger.info(board.toString());
		
		service.regist(board);
		
		rttr.addFlashAttribute("msg","success");
		
		//return "/board/success";
		return "redirect:/board/listAll"; //redirect 새로고침을 이용하는 것을 방지하기 위해 쓰임
	}
	//전체 읽기
	@RequestMapping(value = "listAll", method = RequestMethod.GET)
	public void listAll(Model model) throws Exception {
		logger.info("show all list.................................");
		model.addAttribute("list",service.listAll());
	}
	//읽기
	@RequestMapping(value = "/read", method = RequestMethod.GET)
	public void read(@RequestParam("bno") int bno, Model model) throws Exception{
		
		model.addAttribute(service.read(bno)); //객체명이 BoardVO 클래스의 객체이므로 boardVO라는 이름으로 저장
	}
	//읽기
	@RequestMapping(value = "/readPage", method = RequestMethod.GET)
	public void readPage(@RequestParam("bno") int bno, @ModelAttribute("cri") Criteria cri, Model model) throws Exception{ //@ModelAttribute - 여러 곳(URI 패스, 요청 매개변수, 세션 등)에 있는 단순 타입 데이터를 복합 타입 객체로 받아오거나 해당 객체를 새로 만들 때 사용할 수 있다.
		
		model.addAttribute(service.read(bno)); //객체명이 BoardVO 클래스의 객체이므로 boardVO라는 이름으로 저장
	}
	//삭제 기존방법
	@RequestMapping(value="/remove", method=RequestMethod.POST)
	public String remove(@RequestParam("bno") int bno, RedirectAttributes rttr) throws Exception{ //addFlashAttribute 임시한번의 메시지 전달
		service.remove(bno);
		
		rttr.addFlashAttribute("msg", "success");
		
		return "redirect:/board/listAll";
	}
	
	//삭제
	@RequestMapping(value="/removePage", method=RequestMethod.POST)
	public String removePage(@RequestParam("bno") int bno, Criteria cri, RedirectAttributes rttr) throws Exception{ //addFlashAttribute 임시한번의 메시지 전달
		service.remove(bno);
		
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		rttr.addFlashAttribute("msg", "success");

		
		return "redirect:/board/listPage";
	}	
	
	//수정페이지 읽기 기존
	@RequestMapping(value="/modify", method=RequestMethod.GET)
	public void Mdodfiy(int bno, Model model) throws Exception {
		model.addAttribute(service.read(bno));
	}
	//수정페이지 수정 기존
	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public String Mdodfiy(BoardVO board, RedirectAttributes rttr)throws Exception{
		
		logger.info("mod post...................");
		service.modify(board);
		
		rttr.addFlashAttribute("msg","success");
		
		return "redirect:/board/listAll";
		
	}
	//수정페이지 읽기
	@RequestMapping(value="/modifyPage", method=RequestMethod.GET)
	public void modifyPage(int bno, @ModelAttribute("cri") Criteria cri, Model model) throws Exception {
		model.addAttribute(service.read(bno));
	}
	//수정페이지 수정
	@RequestMapping(value="/modifyPage", method=RequestMethod.POST)
	public String modifyPage(BoardVO board, Criteria cri, RedirectAttributes rttr)throws Exception{
		
		logger.info("mod post...................");
		service.modify(board);
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		rttr.addFlashAttribute("msg","success");
		
		return "redirect:/board/listPage";
		
	}
	//listCri를 이용한 페이징 처리 테스트 criteria
	@RequestMapping(value="/listCri", method=RequestMethod.GET) //리스트페이지의 기본적으로 get 방식을 사용합니다.
	public void listAll(Criteria cri, Model model) throws Exception{
		
		model.addAttribute("list",service.listCriteria(cri));
	}
	//listPage
	@RequestMapping(value="/listPage", method=RequestMethod.GET)
	public void listPage(Criteria cri, Model model)throws Exception{
	
		logger.info(cri.toString());
		
		model.addAttribute("list", service.listCriteria(cri));
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(service.listCountCriteria(cri)); //게시글 총갯수 구하기
		//pageMaker.setTotalCount(131);
		
		model.addAttribute("pageMaker",pageMaker);
	}
	
}
