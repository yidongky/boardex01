<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../include/header.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="/resources/js/upload.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.1/handlebars.js"></script>
<style>
.fileDrop {
  width: 80%;
  height: 100px;
  border: 1px dotted gray;
  background-color: lightslategrey;
  margin: auto;
  
}
</style>

<meta charset="EUC-KR">
<title>등록페이지</title>
</head>
<body>
	<input type="hidden" name="page" value="${cri.page}">
	<input type="hidden" name="perPageNum" value="${cri.perPageNum}">
	<input type="hidden" name='searchType' value="${cri.searchType }">	<!-- 검색타입 -->
	<input type="hidden" name='keyword' value="${cri.keyword }">	<!-- 검색어 -->

<!-- Main content -->

	<form id='registerForm' role="form" method="post"> <!-- form의 action 속성이 지정되지 않으면 현재 경로를 그대로 action의 대상 경로로 잡는다. -->
		<div class="box-body">
			<div class="form-group">
				<label for="exampleInputEmail1">Title</label> <input type="text" name='title' class="form-control" placeholder="Enter Title">
			</div>
			<div class="form-group">
				<label for="exampleInputPassword1">Content</label>
				<textarea class="form-control" name="content"  rows="3" placeholder="Enter ..."></textarea>
			</div>		
			<div class="form-group">
				<label for="exampleInputEmail1">Writer</label> 
				<input type="text" name="writer" class="form-control" value="${login.uid }" readonly>
			</div>
	
			<div class="form-group">
				<label for="exampleInputEmail1">File DROP Here</label>
				<div class="fileDrop"></div>
			</div>
		</div>
	
		<!-- /.box-body -->
	
		<div class="box-footer">
			<div>
				<hr>
			</div>
	
			<ul class="mailbox-attachments clearfix uploadedList">
			</ul>
	
			<button type="submit" class="btn btn-primary">Submit</button>
	
		</div>
	</form>
</body>



<script id="template" type="text/x-handlebars-template">
<li>
  <span class="mailbox-attachment-icon has-img"><img src="{{imgsrc}}" alt="Attachment"></span>
  <div class="mailbox-attachment-info">
	<a href="{{getLink}}" class="mailbox-attachment-name">{{fileName}}</a>
	<a href="{{fullName}}" 
     class="btn btn-default btn-xs pull-right delbtn"><i class="fa fa-fw fa-remove"></i></a>
	</span>
  </div>
</li>                
</script>    

<script>


var template = Handlebars.compile($("#template").html());

$(".fileDrop").on("dragenter dragover", function(event){  //dragenter :이동할 데이터를 마우스로 끌어서 드롭 대상에 올려 놓는 동안 계속해서 발생하는 이벤트이다  dragover: 이동할 데이터를 마우스로 끌어서 드롭 대상에 올려 놓는 동안 계속해서 발생하는 이벤트이다
	event.preventDefault();
});


$(".fileDrop").on("drop", function(event){
	event.preventDefault();
	
	var files = event.originalEvent.dataTransfer.files;
	/*event.originalEvent 는 jQuery를 이용하는 경우 event가 순수한 DOM 이벤트가 아니기 떄문에 evnet.originalEvent 를 이용해서 순수한 원래의 DOM 이벤트를 가져옵니다.
	/event.originalEvent  이벤트와 같이 전달된 데이터를 의미 하고 데이터를 찾아내기 위해서는 dataTransfer.files를 이용
	originalEvent 브라우저가 직접 생성한 이벤트 오브젝트는, jQuery 가 알아서 사용하기 편한 method 와 properties 로 감싸버린다.
	그런데 어쩔 땐 jQuery 가 감싸지 않은 기존 이벤트를 접근해야할 필요가 있을텐데 그때 event.originalEvent 를 사용하면 된다
	*/		
	
	var file = files[0];

	var formData = new FormData(); //formData 객체를 이용하면 <form>태그로 만든 데이터의 전송 방식과 동일하게 파일 데이터를 전송할수 있습니다.
	
	formData.append("file", file);	
	
	
	$.ajax({
		  url: '/uploadAjax',
		  data: formData,
		  dataType:'text',
		  processData: false,
		  contentType: false,
		  type: 'POST',
		  success: function(data){
			  
			  var fileInfo = getFileInfo(data); //fileName,imgsrc, getLink 등 파일의 정보 가져오기
			  
			  var html = template(fileInfo); //템플릿에 정보 적용
			  
			  $(".uploadedList").append(html); //uploadedList 에  템플릿 작성
		  }
		});	
});


//파일 업로드 취소
$(".uploadedList").on("click", ".delbtn",function(event){
	event.preventDefault();
	
	var that = $(this);
	
	alert("delete file");
	
	  $.ajax({
		   url:"/deleteFile",
		   type:"post",
		   data: {fileName:$(this).attr("href")},
		   dataType:"text",
		   success:function(result){
			   console.log("RESULT: " + result);
			   if(result == 'deleted'){
				   that.closest("li").remove();
			   }
		   }
	  });
	
});


$("#registerForm").submit(function(event){
	event.preventDefault();
	
	var that = $(this);
	
	var str ="";
	$(".uploadedList .delbtn").each(function(index){ //each() 배열과 length 속성을 갖는 배열과 유사 배열 객체들을 index를 기준으로 반복할 수 있습니다.
		 str += "<input type='hidden' name='files["+index+"]' value='"+$(this).attr("href") +"'> ";
		 //현재까지 업로된 파일들을 hidden으로 추가  이떄 각파일들은 file[0]과 같은 이름으로 추가되는데, 이 배열 표시를 이용해서 컨트롤러에서는 BoardVO의 files 파라미터를 수집 (fullName)
	});
	
	that.append(str);

	that.get(0).submit();//JQuery get(0)은 순수한 DOM 객체를 얻어내기 위해서 사용
});

 

<%@include file="../include/footer.jsp"%>
</html>
