package org.zerock.persistence;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.zerock.domain.MessageVO;

@Repository
public class MessageDAOImpl implements MessageDAO {
	
	@Inject
	private SqlSession session;
	
	private static String namspace = "org.zerock.mapper.messageMapper";
	
	//메시지 작성
	@Override
	public void create(MessageVO vo) throws Exception {
		session.insert(namspace + ".create",vo);		
	}
	//메시지 읽기
	@Override
	public MessageVO readMessage(Integer mid) throws Exception {
		return session.selectOne(namspace + ".readMessage",mid);
	}
	//메시기 읽은 시간 업데이트
	@Override
	public void updateState(Integer mid) throws Exception {
		session.update(namspace + ".updateState",mid);
		
	}

}
