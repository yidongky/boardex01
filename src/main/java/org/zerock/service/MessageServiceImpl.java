package org.zerock.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.MessageVO;
import org.zerock.persistence.MessageDAO;
import org.zerock.persistence.PointDAO;

@Service
public class MessageServiceImpl implements MessageService{
	
	@Inject
	private MessageDAO messageDAO;
	
	@Inject
	private PointDAO pointDAO;

	@Transactional //트랜잭션 처리 둘중 하나 실패시 원상복귀
	@Override
	public void addMessage(MessageVO vo) throws Exception { //before 타입의 Advice가 적용됨
		
		messageDAO.create(vo); //메시지 작성
		pointDAO.updatePoint(vo.getSender(), 10); //포인트 점수 10점 추가

	}

	@Override
	public MessageVO readMessage(String uid, Integer mid) throws Exception {
		
		messageDAO.updateState(mid); // 읽은 시긴 업데이트
		pointDAO.updatePoint(uid, 5); //포인트 점수 5점추가
		
		return messageDAO.readMessage(mid);// 읽은 메시지 반환
	}

}
