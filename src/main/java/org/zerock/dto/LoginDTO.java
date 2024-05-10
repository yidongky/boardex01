package org.zerock.dto;

public class LoginDTO { // LoginDTO 의 용도는 화면에서 전달되는 데이터를 수집하는 용도로 사용합니다.
	
	// VO 와 DTO : 데이터를 수집하는 용도로 사용할수 있다는 공통점이 있음
	// VO는 테이블 구조를 이용해서 작성하는경우가 많고 DTO 화면에 전달되는 데이터를 수집하느 용도로 사용하는 경우가 많습니다.
	
	private String uid;
	private String upw;
	private boolean useCookie;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUpw() {
		return upw;
	}
	public void setUpw(String upw) {
		this.upw = upw;
	}
	public boolean isUseCookie() {
		return useCookie;
	}
	public void setUseCookie(boolean useCookie) {
		this.useCookie = useCookie;
	}
	@Override
	public String toString() {
		return "LoginDTO [uid=" + uid + ", upw=" + upw + ", useCookie=" + useCookie + "]";
	}
	
	
	
	

}
