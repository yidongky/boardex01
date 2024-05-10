package org.zerock.test;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.SearchCriteria;
import org.zerock.persistence.BoardDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/**/root-context.xml" })
public class BoardDAOTest {
	
	@Inject
	private BoardDAO dao;
	 private static Logger logger = LoggerFactory.getLogger(BoardDAOTest.class);
	
	  @Test
	  public void testListPage() throws Exception {

	    int page = 3;

	    List<BoardVO> list = dao.listPage(page);

	    for (BoardVO boardVO : list) {
	      logger.info(boardVO.getBno() + ":" + boardVO.getTitle());
	    }
	  }
	  
	  @Test
	  public void testListCriteria()throws Exception{
		  
		  Criteria cri = new Criteria();
		  cri.setPage(2);
		  cri.setPerPageNum(20);
		  
		  List<BoardVO> list = dao.listCriteria(cri);
		  
		  for(BoardVO boardvo : list) {
			  logger.info(boardvo.getBno() + ":" + boardvo.getTitle());
		  }
	  }
	  
	  @Test
	  public void testURI()throws Exception{ // UriComponents클래스 path나 query에 해당하는 문자열들을 추가해서 원하는 URI를 생성할 때 사용합니다.
		  UriComponents uriComponents =
				  UriComponentsBuilder.newInstance().path("/board/read")
				  .queryParam("bno", 12).queryParam("perPageNum", 20).build();
		  
		  logger.info("/board/read?bno=12&perPageNum=20");
		  logger.info(uriComponents.toString());
	  }
	  
	  @Test
	  public void testURI2()throws Exception{ // UriComponents클래스 path나 query에 해당하는 문자열들을 추가해서 원하는 URI를 생성할 때 사용합니다.
		  UriComponents uriComponents =
				  UriComponentsBuilder.newInstance().path("/{module}/{page}")
				  .queryParam("bno", 12).queryParam("perPageNum", 20).build().expand("board","read").encode();
		  
		  logger.info("/board/read?bno=12&perPageNum=20");
		  logger.info(uriComponents.toString());
	  }
	  
	  @Test
	  public void testDynamic1() throws Exception {
		  SearchCriteria cri = new SearchCriteria();
		  cri.setPage(1);
		  cri.setKeyword("글");
		  cri.setSearchType("t");
		  
		  logger.info("======================================================");
		  
		  List<BoardVO> list = dao.listSearch(cri);
		  
		  for(BoardVO boardVO : list) {
			  logger.info(boardVO.getBno() + ": " + boardVO.getTitle());
		  }
		  
		  logger.info("======================================================");
		  
		  logger.info("COUNT: " + dao.listSearchCount(cri));
		  
	  }

}
