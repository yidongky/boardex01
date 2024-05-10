// 정규표현식을 이용해서 파일의 확장자가 존재하는지를 검사
function checkImageType(fileName){
	
	var pattern = /jpg|gif|png|jpeg/i; //i 의미는 대,소문자 구분 없음
	
	return fileName.match(pattern); //fileName 에서 일치하는 확장자를 찾아서 리턴

}

function getFileInfo(fullName){
		
	var fileName,imgsrc, getLink;
	
	var fileLink;
	
	if(checkImageType(fullName)){ //이미지 타입일 경우
		imgsrc = "/displayFile?fileName="+fullName; //썸네일 이미지 경로
		fileLink = fullName.substr(14); //날짜 폴더르 제외한 파일명
		
		var front = fullName.substr(0,12); // /2015/07/01/ 
		var end = fullName.substr(14); // 파일 이름 앞의 's_'제거하는 용도로 사용합니다.	
		
		getLink = "/displayFile?fileName="+front + end; //썸네일이미지가 아닌 실제이미지 경로 
		
	}else{
		imgsrc ="/resources/dist/img/file.png"; // 이미지 파일이 아닌 일반파일을 알리기위한 이미지
		fileLink = fullName.substr(12); // 2015/00/00/ 날짜 경로를 제외한 파일명
		getLink = "/displayFile?fileName="+fullName; // 날짜 경로를 포함한 파일경로
	}
	fileName = fileLink.substr(fileLink.indexOf("_")+1); // UUID의 키값을 제외한 파일명
	
	return  {fileName:fileName, imgsrc:imgsrc, getLink:getLink, fullName:fullName};
	
}


