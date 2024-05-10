package org.zerock.service;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.SearchCriteria;
import org.zerock.persistence.BoardDAO;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Inject
	private BoardDAO dao;
	
	//작성
	@Transactional
	@Override
	public void regist(BoardVO board) throws Exception {
		
		dao.create(board); // 개시물 작성
		
		String[] files = board.getFiles(); //파일명을 가지고옴
		
		if(files == null) {return;}
		
		for(String fileName : files) {
			System.out.println("fileName:"+fileName);
			dao.addAttach(fileName); //게시물 첨부파일명 insert
		}
	}
	/*전파(PROPAGTION) 속성
	 * PROPAGATION_MADATORY : 작업은 반드시 특정한 트랜잭션이 존재한 상태에서만 가능
	 * PROPAGATION_NESTED: 기존에 트랜잭션이 있는경우, 포함되어 실행
	 * PROPAGATION_NEVER:트랜잭션 상황에 실행되면 예외 발생
	 * PROPAGATION_NOT_SUPPORTED: 트랜잭션이 있는 경우에는 트랜잭션이 끝날 때까지 보류된 후 실행
	 * PROPAGATION_REQUIRED: 트랜잭션이 있으면 그 상황에서 실행, 없으면 새로운 트랜잭션 실행(기본 설정)
	 * PROPAGATION_REQUIRED_NEW: 대상은 자신만의 고유한 트랜잭션으로 실행
	 * PROPAGATION_SUPPORTS: 트랜잭션을 필요로 하지 않으나, 트랜잭션 상황에 있따면 포함되어 실행
	 * 
	 * 격리(Isolation) 레벨
	 * DEFAULT: DB설정, 기본격리 수준(기본설정)
	 * SERIALIZABLE: 가장 높은 격리, 성능 저하의 우려가 있음
	 * READ_COMMITED: 커밋된 데이터에 대해 읽기 허용
	 * REPEATEABLE_READ: 동일 필드에 대해 다중 접근 시 ㅣ모두 동일한 결과를 보장
	 * 
	 * Read-only 속성 : true인 경우 insert,update,delete 실행시 예외 발생, 기본 설정 false
	 * Rollback-for-예외 : 틀정 예외가 발생 시 강제로 Rollback
	 * No-rollack-for-예외 : 특정 예외의 발생 시 Rollback 처리되지 않음
	 */
	
	
	//읽기
	@Transactional(isolation = Isolation.READ_COMMITTED) //커밋된 데이터에 대해 읽기 허용
	@Override
	public BoardVO read(Integer bno) throws Exception {
		dao.updateViewCnt(bno);
		return dao.read(bno);
	}
	
	//수정
	@Transactional
	@Override
	public void modify(BoardVO board) throws Exception {
		
		dao.update(board);		
		Integer bno = board.getBno();
		
		dao.deleteAttach(bno);
		
		String[] files = board.getFiles();
		
		if(files == null) {return;}
		
		for(String fileName : files) {
			dao.replaceAttach(fileName, bno);
		}
		
	}
	//삭제
	@Transactional
	@Override
	public void remove(Integer bno) throws Exception { //삭제 작업의 경우 tbl_attach가 tbl_board를 참조하기 떄문에 반드시 첨부파일과 관련된 정보부터 삭제하고,게시글을 삭제
		dao.deleteAttach(bno);
		dao.delete(bno);
	}
	//리스트 보기
	@Override
	public List<BoardVO> listAll() throws Exception {
		return dao.listAll();
	}
	//Criteria를 이용한 페이징 처리
	@Override
	public List<BoardVO> listCriteria(Criteria cri) throws Exception {

		return dao.listCriteria(cri);
	}
	//게시글 총갯수
	@Override
	public int listCountCriteria(Criteria cri) throws Exception {
		
		return dao.countPaging(cri);
	}
	// 검색하기
	@Override
	public List<BoardVO> listSearchCriteria(SearchCriteria cri) throws Exception {

		return dao.listSearch(cri);
	}
	//검색한 목록의 갯수
	@Override
	public int listSearchCount(SearchCriteria cri) throws Exception {
		return dao.listSearchCount(cri);
	}

	//게시글 첨부파일 조회하기
	@Override
	public List<String> getAttach(Integer bno) throws Exception {
	
		return dao.getAttach(bno);
	}
	

}
