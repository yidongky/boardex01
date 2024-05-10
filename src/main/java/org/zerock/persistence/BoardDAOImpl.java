package org.zerock.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.SearchCriteria;

/*
 * @Repository 설정
 * @Inject SqlSession 주업
 * namspace 작성
 * */

@Repository //DAO를 스프링에 인식시키기 위해서 주료 사용
public class BoardDAOImpl implements BoardDAO{
//extends는 일반 클래스와 abstract 클래스 상속에 사용되고, implement는 interface 상속에 사용된다.
//Implements의 가장 큰 특징은 이렇게 부모의 메소드를 반드시 오버라이딩(재정의)해야 한다.
	
	@Inject
	private SqlSession session;
	
	private static String namspace = "org.zerock.mapper.BoardMapper";

	//작성
	@Override
	public void create(BoardVO vo) throws Exception {
		session.insert(namspace+".insert", vo);
	}
	//읽기
	@Override
	public BoardVO read(Integer bno) throws Exception {
		
		return session.selectOne(namspace+".read",bno);
	}
	//수정
	@Override
	public void update(BoardVO vo) throws Exception {
		session.update(namspace+".update",vo);
		
	}
	//삭제
	@Override
	public void delete(Integer bno) throws Exception {
		session.delete(namspace+".delete",bno);
		
	}
	//리스트 보기
	@Override
	public List<BoardVO> listAll() throws Exception {
		return session.selectList(namspace+".listAll");
	}
	//페이징 처리 리스트 보기 
	@Override
	public List<BoardVO> listPage(int page) throws Exception {
		if(page <= 0) { //0보다 작거나 같으면 1
			page = 1;
		}
		page = (page -1) * 10; //첫번째 페이지는 0부터 시작 두번째 페이지는 10부터 시작
		
		return session.selectList(namspace+".listPage",page);
	}
	//페이징 처리된 실제리스트
	@Override
	public List<BoardVO> listCriteria(Criteria cri) throws Exception {
		
		return session.selectList(namspace+".listCriteria",cri);
	}
	//총개시글 갯수구하기
	@Override
	public int countPaging(Criteria cri) throws Exception {

		return session.selectOne(namspace+".countPaging", cri);
	}
	//검색 목록 리스트
	@Override
	public List<BoardVO> listSearch(SearchCriteria cri) throws Exception {
		
		return session.selectList(namspace+ ".listSearch",cri);
	}
	//검색 게시글 총갯수
	@Override
	public int listSearchCount(SearchCriteria cri) throws Exception {
		
		return session.selectOne(namspace+".listSearchCount",cri);
	}
	//게시글에 댓글 갯수 
	@Override
	public void updateReplyCnt(Integer bno, int amount) throws Exception {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		paramMap.put("bno", bno);
		paramMap.put("amount",amount);
		
		session.update(namspace + ".updateReplyCnt", paramMap);
	}
	//게시글 조회수 업데이트
	@Override
	public void updateViewCnt(Integer bno) throws Exception {
		session.update(namspace + ".updateViewCnt",bno);
	}
	//게시글 파일 업로드
	@Override
	public void addAttach(String fullName) throws Exception {
		session.insert(namspace + ".addAttach",fullName);
	}
	//게시글 첨푸파일 조회
	@Override
	public List<String> getAttach(Integer bno) throws Exception {
		return session.selectList(namspace + ".getAttach",bno);
	}
	//게시글 첨부파일 삭제
	@Override
	public void deleteAttach(Integer bno) throws Exception {
		session.delete(namspace + ".deleteAttach",bno);
		
	}
	//게시글 첨부파일 교체
	@Override
	public void replaceAttach(String fullName, Integer bno) throws Exception {
		 //2개 이상의 데이터를 다룰 때는 VO(Value Object)를 사용하거나 해시맵을 이용합니다
		
		Map<String, Object> ParaMap = new HashMap<String, Object>();
		
		ParaMap.put("bno",bno);
		ParaMap.put("fullName", fullName);

		session.insert(namspace + ".replaceAttach",ParaMap);
	}
}
