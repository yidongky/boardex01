package org.zerock.persistence;

import java.util.Date;

public interface PointDAO {
	
	//포인트 접수 업데이트
	public void updatePoint(String uid, int point)throws Exception; 
}
