package org.zerock.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

public class MediaUtils { //확장자를 가지고 이미지 타입인지를 판단해 주는 역할

		private static Map<String, MediaType> mediaMap;
		
		static {
			mediaMap = new HashMap<String, MediaType>();
			mediaMap.put("JPG", MediaType.IMAGE_JPEG); //IMAGE_JPEG 의 데이터만 담고있는 요청을 처리하겠다
			mediaMap.put("GIF", MediaType.IMAGE_GIF); //IMAGE_GIF 의 데이터만 담고있는 요청을 처리하겠다
			mediaMap.put("PNG", MediaType.IMAGE_PNG); //IMAGE_PNG 의 데이터만 담고있는 요청을 처리하겠다
		}
		
		public static MediaType getMediaType(String type) { //이미지 파일인 경우와 그렇지 않은 경우를 나누어 처리
			
			System.out.println("MediaType 반환값:"+mediaMap.get(type.toUpperCase()));
			return mediaMap.get(type.toUpperCase()); //type.toUpperCase() 문자열을 대문자로 변환해서 반환 // toLowerCase() 는 대상 문자열을 모두 소문자로 변환
		}
}
