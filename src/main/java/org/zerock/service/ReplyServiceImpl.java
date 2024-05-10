package org.zerock.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;
import org.zerock.persistence.BoardDAO;
import org.zerock.persistence.ReplyDAO;

@Service
public class ReplyServiceImpl implements ReplyService{
	
	@Inject
	private ReplyDAO replyDAO;
	
	@Inject
	private BoardDAO boardDAO;

	
	// 댓글 불러오기
	@Override
	public List<ReplyVO> listReply(Integer bno) throws Exception {
		
		return replyDAO.list(bno);
	}
	@Transactional
	// 댓글 쓰기
	@Override
	public void addReply(ReplyVO vo) throws Exception {
		replyDAO.create(vo);
		boardDAO.updateReplyCnt(vo.getBno(), 1); // 댓글 등록시 댓글수 1증가
		
	}
	
	// 댓글 수정
	@Override
	public void modifyReply(ReplyVO vo) throws Exception {
		replyDAO.update(vo);
	}
	
	@Transactional
	//댓글 삭제
	@Override
	public void removeReply(Integer rno) throws Exception {
		int bno = replyDAO.getBno(rno);
		replyDAO.delete(rno);
		boardDAO.updateReplyCnt(bno, -1); // 댓글 등록시 댓글수 1감소
	}
	
	//댓글 리스트 불러오기 (페이징처리)
	@Override
	public List<ReplyVO> listReplyPage(Integer bno, Criteria cri) throws Exception {
		
		return replyDAO.listPage(bno, cri);
	}
	
	//해당 게시글에 댓글 총합
	@Override
	public int count(Integer bno) throws Exception {
		
		return replyDAO.count(bno);
	}

}