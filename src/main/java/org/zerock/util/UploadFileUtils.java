package org.zerock.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.Media;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

/*
 * UploadFileUtils 는 스프링의 fileCopyUtils 와 유사하게 static 기능을 호출해서 파일을 업로드 할 수 있도록 설계 할 것입니다.
 * 
 * 파일 저장경로(UploadPath)
 * 원본파일의 이름(originalName)
 * 파일 데이터(byte[])
 * */

public class UploadFileUtils {
	
	private static final Logger logger= LoggerFactory.getLogger(UploadFileUtils.class); //별도의 데이터가 보관될필요 없기 때문에 static으로 설계
	
	public static String uploadFile(String uploadPath, String orginalName, byte[] fileData)throws Exception{
		
		UUID uid = UUID.randomUUID(); //고유한키를 랜덤으로 생성
		
		String savedName = uid.toString() + "_" + orginalName; //UID 생성값과 합쳐진 저장될 파일 이름
		
		String savedPath = calcPath(uploadPath); //날짜 및 시간을 가지고와서 저장될 경로를 계산합니다.
		
		File target = new  File(uploadPath + savedPath,savedName); //uploadPath 경로 + savedPath 경로 ,저장될 파일이름
		
		FileCopyUtils.copy(fileData, target); //원본파일을 저장하는 부분입니다.
		
		String formatName = orginalName.substring(orginalName.lastIndexOf(".")+1); //원본파일의 확장자를 의미합니다.
		
		String uploadeFileName = null;
		
		if(MediaUtils.getMediaType(formatName) != null) { // 이미지파일인지 그렇지 않은경우인지 판단 JPG,GIF,PNG 판단
			System.out.println(MediaUtils.getMediaType(formatName));
			uploadeFileName = makeThumbnail(uploadPath, savedPath, savedName); //이미지 파일인 경우 썸네일 이미지 생성
		}else {
			
			uploadeFileName = makeIcon(uploadPath, savedPath, savedName); //그렇지 낳은경우 결과를 만들어냄
			//makeIcon은 경로 처리를 하는 문자열의 치환용도
			
		}
		return uploadeFileName;
	}
	
	private static String makeIcon(String uploadPath, String path, String fileName) {
		
		String iconName = uploadPath + path + File.separator + fileName;
		
		return iconName.substring(uploadPath.length()).replace(File.separatorChar, '/');
		//메소드 리턴시 문자열로 치환하는 이유는 브라우저에서 윈도우의 경로로 사용하는 '\' 문자가 정상적으로 인식되지 않기 떄문에 '/'로 치환
		//  존재하는 윈도우 전체 경로에서 윈도우의 파일 구분자인 \ 를 브라우저에서  제대로 인식하지 못하기 때문에 U에서 사용하는 / 로 치환한 다음 반환.	
	}

	private static String calcPath(String uploadPath) { //최종  결과 폴더를 반환
		//java.util.Calendar 클래스를 이용해서 현재 시스템에 날짜에 맞는 데이터를 얻음
		Calendar cal = Calendar.getInstance(); //getInstance() 메소드를 사용하여 현재 날짜와 시간의 객체를 얻어올 수 있다
		//File.pathSeparator는 ;로 파일들을 구분해주는겁니다.  File.separator 은  경로를 분리해주는 메소드 ( \ 와같이)
		String yearPath = File.separator+cal.get(Calendar.YEAR);
		
		String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH)+1);
		
		String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));
		
		makeDir(uploadPath,yearPath,monthPath,datePath); //폴더 생성
		
		logger.info(datePath);
		
		return datePath;
	}
	
	private static void makeDir(String uploadPath, String... paths) { //파일이 저장될 '년/월/일' 정보로  폴더생성
		
		if(new File(paths[paths.length-1]).exists()) {
			return;
		}
		
		for(String path : paths) {
			
			System.out.println("여기:"+path);
			File dirPath = new File(uploadPath + path);
			
			if(! dirPath.exists()) { //디렉토리 존재유무
				dirPath.mkdirs(); //현재 파일객체가 참조하는 경로로 디렉토리를 생성한다. 반드시 부모디렉토리(uploadPath)가 있어야 한다.
			}
		}
		
	}
	//썸네일 이미지 생성
	private static String makeThumbnail(String uploadPath, String path, String fileName)throws Exception{
		//BufferedImage 는 실제 이미지가 아닌 메모리상의 이미지를 의미하는 객체
		// 원본 파일을 메모리상으로 로딩하고 정해친 크기에 맞게 작은 이미지 파일에 원본 이미지를 복사합니다.
		
		//썸네일을 생성하는 작업은 메소드의 파라미터에 사용된 기본경로(uploadPath) 와 '년/월/일/ 퓰더(path), 현재 업로드 된 파일의 이름을 이용
		BufferedImage sourceImg = ImageIO.read(new File(uploadPath + path, fileName)); //ImageIO.read 이미지 읽기
		
		BufferedImage destImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT,100);
		//Scalr.Mode.FIT_TO_HEIGHT,100 : 이미지의 높이를 100px 동일하게 만들어주는역할   Scalr.Method.AUTOMATIC 이미지 가로/세로 비율 유지
		String thumbnailName =uploadPath + path + File.separator+ "s_" + fileName;
		// s로 시작하면 썸네일 이미지를 표시하기 위해서
		File newFile = new File(thumbnailName); //부모 디렉토리를 파라미터로 인스턴스 생성
		
		String formatName = fileName.substring(fileName.lastIndexOf(".")+1); //확장자
		
		ImageIO.write(destImg, formatName.toUpperCase(), newFile); // 이미지 파일을 생성
		//File.separatorChar    / 경로구분
		return thumbnailName.substring(uploadPath.length()).replace(File.separatorChar, '/');
		//메소드 리턴시 문자열로 치환하는 이유는 브라우저에서 윈도우의 경로로 사용하는 '\' 문자가 정상적으로 인식되지 않기 떄문에 '/'로 치환
		// 섬네일이 존재하는 윈도우 전체 경로에서 윈도우의 파일 구분자인 \ 를 브라우저에서  제대로 인식하지 못하기 때문에 U에서 사용하는 / 로 치환한 다음 반환.
	}

}
