package org.zerock.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.util.MediaUtils;
import org.zerock.util.UploadFileUtils;


@Controller
public class UploadController {
	
	  private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
	  
	  @Resource(name = "uploadPath") //uploadPath 의 bean을 찾아서 Inject
	  private String uploadPath;
	
	  @RequestMapping(value = "/uploadForm", method = RequestMethod.GET)
	  public void uploadForm() {
	  }
	
	// Multipart 구성된 데이터의 처리르 프로젝트의 시작단계에서 적용한 multipartResolver 설정을 통해서 처리됩니다.
	@RequestMapping(value = "/uploadForm", method = RequestMethod.POST)
	public String uploadForm(MultipartFile file, Model model) throws Exception { //MultipartFile 운 POST 방식으로 들어온 파일 데이터를 의미
		
	    logger.info("originalName: " + file.getOriginalFilename());
	    logger.info("size: " + file.getSize());
	    logger.info("contentType: " + file.getContentType());
	    
	    String savedName = 
	    		uploadFile(file.getOriginalFilename(),file.getBytes()); //uploadFile()은 원본 파일의 이름과 파일 데이터를 byte[] 로 변환한 정보를 파라미터로 처리해서 실제로 파일을 업로드 합니다.
	    
	    	model.addAttribute("savedName", savedName);
	    	
	    	return "uploadResult";
	}

	private String uploadFile(String orginalName, byte[] fileData) throws Exception { //byte[] fileData 파일의 사이즈
		
		UUID uid = UUID.randomUUID(); //UUID 는 중복되지 앟는 고유한 키 값을 설정할 때 사용합니다.
		
		String savedName = uid.toString() + "_" + orginalName;
		
	    logger.info("savedName: " + savedName);
		
		File target = new File(uploadPath,savedName); 
		//File(String parent, String child) 첫번째 매개변수에 디렉터리의 경로를 두번째 매개변수에 그 하위 파일명을 지정하여 File 객체를 생성할 수 있습니다.

		
		
		// FileCopyUtils 는 'org.springframework.util' 패키지에 설정된 클래스 입니다.
		// FileCopyUtils 는 파일 데이터를 파일로 처리하거나 ,복사하는 등의 작업에 유요하게 사용 될수 있습니다.
		/*
		 * copy(byte[] in, file out) 데이터가 담긴 바이트의 배열(in)을 파일에 기록한다.
		 * copy(byte[] in, OutputStream out) 데이터가 담긴 바이트의 배열(in)을 특정한 OutputStream으로 전송
		 * copy(InputStream in, OutputStream out) 특정한 InputStream을 특정 OutputStream으로 전송한다.
		 * copy(String in, Writer out) 문자열 그대로 Writer을 이용해서 기록한다.
		 * copyToByteArray(InputStream in) 주어진 InputStream을 읽어서 
		 * */
		FileCopyUtils.copy(fileData, target);
		/*
		 * 파일 및 스트림 복사를 위한 간단한 유틸리티 메소드의 집합체입니다.
		 * 모든 복사 방법은 4096byte의 블록크기를 사용하고 완료 되면 모든 영향을 받는 스트림을 닫습니다. 
		 * 스트림을 열어두는 클래스의 복사 메소드 변형은 StreamUtils 클래스를 사용하시면 됩니다 FuleCopyUtils는 사용후에 스트림을 무조건 닫습니다.
		 * 프레임워크 안에서도 주로 사용되는 클래스이며, 많은 개발자들이 애용하고 있습니다.
		 * 
		 */
		return savedName;
	}
	// Ajax 업로드
	@RequestMapping(value = "/uploadAjax", method = RequestMethod.GET)
	public void uploadAjax() {
		
	}
	
	// 실제로 파일을 업로드 하는 '/uploadAjax' 처리를 UploadController에서 진행합니다.
	@ResponseBody  //메소드에 @ResponseBody 로 어노테이션이 되어 있다면 메소드에서 리턴되는 값은 View 를 통해서 출력되지 않고 HTTP Response Body 에 직접 쓰여지게 됩니다.
	@RequestMapping(value = "/uploadAjax", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> uploadAjax(MultipartFile file) throws Exception{
		
		logger.info("orginalName:" + file.getOriginalFilename());
		logger.info("size:" + file.getSize());
		logger.info("contentType:" + file.getContentType());
		
		//return new ResponseEntity<String>(file.getOriginalFilename(), HttpStatus.CREATED);
		
		return new ResponseEntity<>(
				UploadFileUtils.uploadFile(uploadPath,
						file.getOriginalFilename(),
						file.getBytes()),
				HttpStatus.CREATED);
		
		//@ResponseEntity 결과 데이터 + HTTP의 상태코드
		
	}
	
	//전송된 파일을 화면에 표시하기
	@ResponseBody  //메소드에 @ResponseBody 로 어노테이션이 되어 있다면 메소드에서 리턴되는 값은 View 를 통해서 출력되지 않고 HTTP Response Body 에 직접 쓰여지게 됩니다.
	@RequestMapping("/displayFile")  // * MIME (Multipurpose Internet Mail Extensions)
	public ResponseEntity<byte[]> displayFile(String fileName)throws Exception{ //파라미터로 브라우저에서 전송받기를 원하는 파일의 이름을 받습니다.
		//파일의 이름은 '/년/월/일/파일명'의 형태로 입력 받습니다.
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;
		
		logger.info("FILE NAME:" + fileName);
		
		try {
			String formatName = fileName.substring(fileName.lastIndexOf(".")+1); //파일 이름의 확장자
			
			MediaType mType = MediaUtils.getMediaType(formatName); //이미지 유무 확인
			
			HttpHeaders headers = new HttpHeaders();
			
			in = new FileInputStream(uploadPath+fileName);
			
			if(mType != null) {
				headers.setContentType(mType);
			}else {
				
				fileName = fileName.substring(fileName.indexOf("_")+1); //순수 파일명
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);//표준으로 정의되어있지 않은 파일인 경우 지정하는 타입이다.octet stream이 8비트로 된 데이터이니, 순수한 바이너리 데이터로 받아온다는 의미이다.받는 것은 byte 배열로 받으면 된다.

				headers.add("Content-Disposition", "attachment; filename=\""+ new String(fileName.getBytes("UTF-8"), "ISO-8859-1")+"\"");
				//headers.add 지정된 헤더 및 헤더 값을 HttpHeaders 컬렉션에 추가
				/*Content-Disposition은 컨텐트 타입의 옵션이기도 하며, 실제로 지정된 파일명을 지정함으로써 더 자세한 파일의 속성을 알려줄 수 있다.
				 * content-disposition:attachment는 브라우저 인식 파일 확장자를 포함하여 모든 확장자의 파일들에 대해 다운로드시 무조건 파일 다운로드 대화상자가 뜨도록 하는 헤더속성.
				 * ISO-8859-1 8비트 
				 */


			}
			
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED); //IOUtils.toByteArray() 대상 파일에서 데이터를 읽어냄
			
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		}finally {
			in.close(); // inputStream 닫음
		}
		return entity;
	}
	
	//파일 삭제처리 
	@ResponseBody //메소드에 @ResponseBody 로 어노테이션이 되어 있다면 메소드에서 리턴되는 값은 View 를 통해서 출력되지 않고 HTTP Response Body 에 직접 쓰여지게 됩니다.
	@RequestMapping(value="/deleteFile" , method=RequestMethod.POST)
	public ResponseEntity<String> deleteFile(String fileName){
		
		logger.info("delete file: " + fileName);
		
		String formatName = fileName.substring(fileName.lastIndexOf(".")+1); // 확장자
		
		MediaType mType= MediaUtils.getMediaType(formatName); //확장자 검사
		
		if(mType != null) {
			String front = fileName.substring(0,12); // UUID 랜덤키
			String end = fileName.substring(14); // _S 를 제거한 파일명
			new File(uploadPath + (front+end).replace('/',File.separatorChar)).delete(); //이미지의 경우 썸네일 파일을 원래파일명으로 바꿔서 삭제
			//메소드 리턴시 문자열로 치환하는 이유는 브라우저에서 윈도우의 경로로 사용하는 '\' 문자가 정상적으로 인식되지 않기 떄문에 '/'로 치환
			// 섬네일이 존재하는 윈도우 전체 경로에서 윈도우의 파일 구분자인 \ 를 브라우저에서  제대로 인식하지 못하기 때문에 U에서 사용하는 / 로 치환한 다음 반환.
		}
		new File(uploadPath + fileName.replace('/', File.separatorChar)).delete(); //썸네일 파일또는 일반파일 삭제  File.delete(string path파일경로)
		
		return new ResponseEntity<String>("deleted",HttpStatus.OK);
		
	}
	//첨부파일 삭제
	@ResponseBody //메소드에 @ResponseBody 로 어노테이션이 되어 있다면 메소드에서 리턴되는 값은 View 를 통해서 출력되지 않고 HTTP Response Body 에 직접 쓰여지게 됩니다.
	@RequestMapping(value="/deleteAllFiles", method=RequestMethod.POST)
	public ResponseEntity<String> deleteFile(@RequestParam("files[]") String[] files){ //@RequestBody : 전송된 JSON 데이터를 객체로 변환해주는 애노테이션 으로 @ModelAttribute 유사한 역할을 하지만 JSON에서 사용된다는 점이 차이 
		
		logger.info("delete all files: " + files);
		
		if(files == null || files.length == 0) { //파일 길이와 filess 널값이 삭제완로 리턴
			return new ResponseEntity<String>("deleted",HttpStatus.OK);
		}
		
		for(String fileName : files) { //for( A : B ) B에서 차례대로 객체를 꺼내서 A에다가 넣겠다
			String formatName = fileName.substring(fileName.lastIndexOf(".")+1);
			
			MediaType mType = MediaUtils.getMediaType(formatName); //확장자 검사
			
			if(mType != null) {
				String front = fileName.substring(0,12); //날짜 경로 
				String end = fileName.substring(14); //S_ 썸네일 표시를 제외한 파일명
				new File(uploadPath + (front+end).replace('/', File.separatorChar)).delete(); //이미지 파일 삭제
			}
			
			new File(uploadPath + fileName.replace('/', File.separatorChar)).delete(); //썸네일 이미지 및 일반 파일 삭제
			
		}// 삭제 작업 포문으로 반복
		
		
		return new ResponseEntity<String>("deleted",HttpStatus.OK);
		
	}

}
