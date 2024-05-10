<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../include/header.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <style type="text/css">
    .popup {position: absolute;}
    .back { background-color: gray; opacity:0.5; width: 100%; height: 300%; overflow:hidden;  z-index:1101;}
    .front { 
       z-index:1110; opacity:1; boarder:1px; margin: auto; 
      }
     .show{
       position:relative;
       max-width: 1200px; 
       max-height: 800px; 
       overflow: auto;       
     } 
  	
    </style>
    
    <div class='popup back' style="display:none;"></div>
    <div id="popup_front" class='popup front' style="display:none;">
     <img id="popup_img">
    </div>

<meta charset="EUC-KR">
<title>읽기페이지(페이지마커)</title>

</head>
<body>

<!-- Mian content -->
	<form role="form" method="post">	
		<input type="hidden" name='bno' value="${boardVO.bno}"> <!-- 번호 -->	
		<input type="hidden" name='page' value="${cri.page}">	<!-- 페이지번호 -->
		<input type="hidden" name='perPageNum' value="${cri.perPageNum }">	<!-- 한페이지당 표시되는 갯수 -->
		<input type="hidden" name='searchType' value="${cri.searchType }">	<!-- 검색타입 -->
		<input type="hidden" name='keyword' value="${cri.keyword }">	<!-- 검색어 -->
	</form>
	
		<div class="box-body">
			<div class="form-group">
				<label for="examleInputEmail1">title</label>
				<input type="text" name="title" class="form-control" value="${boardVO.title}" readonly="readonly">
			</div>
			<div class="form-group">
				<label for="exampleInputPassword1">Content</label>
				<textarea class="form-control" name="content" rows="3" readonly="readonly">
				${boardVO.content}
				</textarea>
			</div>
			<div class="form-group">
				<label for="exampleInputEmail1">Writer</label>
				<input type="text" name="writer" class="form-control" value="${boardVO.writer}"  readonly="readonly">
			</div>
		</div>
		
		<!--  /.box body -->
		<div class="box-footer">
		
    <div><hr></div>
	<!-- 첨부파일이 보여지는곳 -->
    <ul class="mailbox-attachments clearfix uploadedList">
    </ul>
    <c:if test="${login.uid == boardVO.writer}">	
			<button type="submit" class="btn btn-warning">Modify</button>
			<button type="submit" class="btn btn-danger">REMOVE</button>
	</c:if>		
		<!-- <button type="submit" class="btn btn-primary">LIST ALL</button>  기본 전체보기 페이지-->
			<button type="submit" class="btn btn-primary">listPage</button>
		</div>
		
		<!-- 댓글 등록 -->
		<div class="row">
			<div class="col-md-12">
				<div class="col-md-12">
				
					<div class="box box-success">
						<div class="box-header">
							<h3 class="box-title">ADD NEW REPLY</h3>
						</div>
					<c:if test="${not empty login}">
						<div class="box-body">
							<label for="exampleInputEmail">Writer</label><input class="form-control" type="text" value="${login.uname}"  id="newReplyWriter" readonly="readonly">
							<label for="exampleInputEmail1">Reply Text</label><input class="form-control" type="text" placeholder="REPLY TEXT" id="newReplyText">
						</div>
						<!--  /.box-body -->
						<div class="box-footer">
							<button type="submit" class="bg-green"   id="replyAddBtn">ADD REPLY</button>
						</div>
					</c:if>
					
					<c:if test="${empty login}">
						<div class="box-body">
							<a href="javascript:goLogin();" >Login Please</a>
						</div>
					</c:if>	
					</div>
					
				</div>
			</div>
		</div>
		<!-- 댓글의 목록과 페이징 처리 -->
		<!-- the time line -->
			<!-- The time line -->
			<ul class="timeline">
				<!-- timeline time label -->
				<li class="time-label" id="repliesDiv"><span class="bg-green">
						Replies List

						<small id='replycntSmall'>
							<c:if test="${boardVO.replycnt > 0}">
								[${boardVO.replycnt}]</small> 
						</c:if>	
						</span>
				</li>
			</ul>
		
			<div class='text-center'>
				<ul id="pagination" class="pagination pagination-sm no-margin ">

				</ul>
			</div>		
	
<!-- Modal -->
<div id="modifyModal" class="modal modal-primary fade" role="dialog">
  <div class="modal-dialog">
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title"></h4>
      </div>
      <div class="modal-body" data-rno>
        <p><input type="text" id="replytext" class="form-control"></p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-info" id="replyModBtn">Modify</button>
        <button type="button" class="btn btn-default" id="replyDelBtn">DELETE</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>


</body>

<script type="text/javascript">
$( document ).ready( function() {
	
	var formObj = $("form[role='form']");
	
	console.log(formObj);
	
	/* 기존 ListAll 페이지로 이동
	$(".btn-primary").on("click", function(){
		self.location = "/board/listAll";
	});
	*/
	
	//listPage로 이동
	$(".btn-primary").on("click", function(){
		formObj.attr("method","get");
		formObj.attr("action", "/sboard/list");
		formObj.submit();
	});
	
	$(".btn-warning").on("click",function(){
		formObj.attr("action","/sboard/modifyPage"); //attr 함수는 테그내에 속성을 그대로 가져온다.
		formObj.attr("method","get");
		formObj.submit();		
	});
	
	
	$(".btn-danger").on("click",function(){
		
		var replyCnt = $("#replycntSmall").html().trim().replace("[", ""); // 댓글 갯수
		
		if(replyCnt.replace("]", "") > 0 ){ // replyCnt 이 0보타 크면
			alert("댓글이 달린 게시물을 삭제할 수 없습니다.");
			return;
		}
		
		/*
		•push 메서드 : 배열의 마지막에 새로운 요소를 추가한 후, 변경된 배열의 길이를 반환
		•pop 메서드 : 배열의 마지막 요소를 제거한 후, 제거한 요소를 반환
		•unshift 메서드 : 배열의 첫 번째 자리에 새로운 요소를 추가한 후, 변경된 배열의 길이를 반환
		•shift 메서드 : 배열의 첫 번째 요소를 제거한 후, 제거한 요소를 반환
		
		*/
		
		var arr=[];
		$(".uploadedList li").each(function(index){
			arr.push($(this).attr("data-src")); //파일의 풀명을 배열에 넣는다.
		});
		
		if(arr.length > 0){
			
			$.post("/deleteAllFiles",{files:arr}, function(){ // 배열에 길이가 0이 되기 전까지 배열의 파일명을 삭제
				
			});

			/*          // $.post 방식을 $.ajax를 이용해서 하는법
			  $.ajax({
				   url:"/deleteAllFiles",
				   type:"POST",
				   data: {files:arr},
				   dataType:"text",
				   success:function(result){
				   }
			  });
			*/
		}
		
		formObj.attr("method","post");
		formObj.attr("action","/sboard/removePage");
		formObj.submit();
	});
	
	var bno = ${boardVO.bno};
	var template = Handlebars.compile($("#templateAttach").html());

	$.getJSON("/sboard/getAttach/"+bno,function(list){ // 컨트롤러에서 문자열 리스트를 반환하기 떄문에 JSON형태의 데이터를 전송하게 되고 이를 getJSON()을 이용해서 처리
		$(list).each(function(){
			
			var fileInfo = getFileInfo(this); //upload.js 파일을 통해서 파일정보 변환
			
			var html = template(fileInfo);
			
			 $(".uploadedList").append(html); //.append()선택된 요소의 마지막에 새로운 요소나 콘텐츠를 추가한다.
			
		});
	});
	
	//이미지 클릭시 크게 보여주기
	$(".uploadedList").on("click", ".mailbox-attachment-info a", function(event){
		
		var fileLink = $(this).attr("href");
		
		if(checkImageType(fileLink)){ //upload.js 를 통해 정규표현식을 이용해서 파일의 확장자가 존재하는지를 검사
			
			event.preventDefault(); //화면 이동 방지
					
			var imgTag = $("#popup_img");
			imgTag.attr("src", fileLink); //원본 파일의 경로를 SRC르로 
			
			console.log(imgTag.attr("src")); //경로 로그로 찍기
					
			$(".popup").show('slow'); //천천히  팝업 div 테그에 이미지 보여주기
			imgTag.addClass("show");		
		}	
	});
	//상세이미지 보여주기화면 닫기
	$("#popup_img").on("click", function(){ // 팝업 이미지 클릭시 hide  속성으로 변경 이미지 닫기
		
		$(".popup").hide('slow');
		
	});		
	
} );
</script>
<!-- 템플릿 코드 -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.1/handlebars.js"></script>
<script type="text/javascript" src="/resources/js/upload.js"></script>



<script id="template" type="text/x-handlebars-template">
{{#each .}}
<li class="replyLi" data-rno={{rno}}>
<i class="fa fa-comments bg-blue"></i>
 <div class="timeline-item" >
  <span class="time">
    <i class="fa fa-clock-o"></i>{{prettifyDate regdate}}
  </span>
  <h3 class="timeline-header"><strong>{{rno}}</strong> - {{replyer}}</h3>
  <div class="timeline-body">{{replytext}} </div>
    <div class="timeline-footer">
	{{#eqReplyer replyer }}
     <a class="btn btn-primary btn-xs" 
	    data-toggle="modal" data-target="#modifyModal">Modify</a>
	{{/eqReplyer}}
    </div>
  </div>			
</li>
{{/each}}
</script>

<script>
	Handlebars.registerHelper("prettifyDate", function(timeValue){ //registerHelper  prettifyDate 에 regdate 를 인자로 받아서 형식을 변환해서 리턴해준다.
		//registerHelper()를 이용해서 사용자가 새로운 기능을 추가할수 있습니다.
		var dateObj = new Date(timeValue);
		var year = dateObj.getFullYear(); //연도만 추출
		var month = dateObj.getMonth() + 1; //달력만 추출
		var date = dateObj.getDate(); // 날짜만 추출
		return year+"/"+month+"/"+date;// {{prettifyDate regdate}} 로 반환
	});
	
	// 로그인한 사용자의 경우 값을 비교 하도록 작성
	Handlebars.registerHelper("eqReplyer", function(replyer, block) { //replyer 작성자 블록처리
		var accum = '';
		if (replyer == '${login.uid}') {
			accum += block.fn(); //아이디가 일치하면 작성자 블록 해제
		}
		return accum;
	});
	
	var printData = function(replyArr, target, templateObject) {

		var template = Handlebars.compile(templateObject.html());

		var html = template(replyArr);
		$(".replyLi").remove();//기존 댓글 지우기  .remove()는 선택한 요소를 HTML 문서에서 제거합니다.
		target.after(html); //.after()	선택한 요소의 바로 뒤쪽에 새로운 요소나 콘텐츠를 추가한다.

	}
	
	var bno =${boardVO.bno};
	
	var replyPage = 1;
	// 댓글 가져오기 
	function getPage(pageInfo){
		
		$.getJSON(pageInfo,function(data){
			printData(data.list, $("#repliesDiv") ,$('#template')); //댓글처리
			printPaging(data.pageMaker, $(".pagination")); //페이징 처리
			
			$("#modifyModal").modal('hide'); //수정창 숨김설정
			
			if(data.pageMaker.totalCount > 0){
				$("#replycntSmall").html("[ " + data.pageMaker.totalCount +" ]");
			}else{
				$("#replycntSmall").html(" ");
			}
		});
	}
	var printPaging = function(pageMaker, target){ //댓글 페이징 처리
		
		var str = "";
		
		if(pageMaker.prev){
			str += "<li><a href='" +(pageMaker.startPage - 1) + "'> << </a></li>"
		}
		for(var i=pageMaker.startPage, len = pageMaker.endPage; i <= len; i++){
			var strClass = pageMaker.cri.page == i?'class=active':'';
			str += "<li "+ strClass+"><a href='"+i+"'>"+i+"</a><li>";
		}
		if(pageMaker.next){
			str += "<li><a href='"+(pageMaker.endPage + 1)+"'> >> </a></li>";
		}
		target.html(str);
	};
	//페이지 리스트 보기 클릭
	$("#repliesDiv").on("click", function(){
		
		if($(".timeline li").size() > 1){ //timeline li의 갯수가 1보다 작으면 리턴
			return;
		}
		getPage("/replies/" + bno + "/1"); 
	});
	// 페이지 번호 클릭시
	$(".pagination").on("click", "li a", function(event){
		
		event.preventDefault();
		
		replyPage = $(this).attr("href");
		
		getPage("/replies/"+bno+"/"+replyPage);
		
	});
	// 댓글 등록
	$('#replyAddBtn').on("click",function(){
		
		 var replyerObj = $("#newReplyWriter"); //작성자
		 var replytextObj = $("#newReplyText"); //댓글내용
		 var replyer = replyerObj.val();
		 var replytext = replytextObj.val();
		 
		 $.ajax({
			 type:'post',
			 url: "/replies/",
			 headers: {
				 "Content-type": "application/json",
				 "X-HTTP-Method-Override": "POST" },
				 dataType:'text',
				 data: JSON.stringify({bno:bno, replyer:replyer, replytext:replytext}), //JSON.stringify( )는 자바스크립트의 값을 JSON 문자열로 변환한다.
				 success:function(result){
					 console.log("result:" + result);
					 if(result == 'success'){
						 alert("등록 되었습니다.");
						 replyPage = 1; //첫번째 페잊로 변경
						 getPage("/replies/"+bno+"/"+replyPage);
						// replyerObj.val("");
						 replytextObj.val("");
					 }
				 }})
	});
	
	$(".timeline").on("click", ".replyLi", function(event){
		
		var reply = $(this);//크릭한객체
		$("#replytext").val(reply.find('.timeline-body').text());
		$(".modal-title").html(reply.attr("data-rno"));
		
	});
	
	$("#replyModBtn").on("click",function(){
		  
		  var rno = $(".modal-title").html();
		  var replytext = $("#replytext").val();
		  
		  $.ajax({
				type:'put',
				url:'/replies/'+rno,
				headers: { 
				      "Content-Type": "application/json",
				      "X-HTTP-Method-Override": "PUT" },
				data:JSON.stringify({replytext:replytext}), 
				dataType:'text', 
				success:function(result){
					console.log("result: " + result);
					if(result == 'success'){
						alert("수정 되었습니다.");
						getPage("/replies/"+bno+"/"+replyPage );
						// $("#modifyModal").modal("hide");
					}
			}});
	});

	$("#replyDelBtn").on("click",function(){
		  
		  var rno = $(".modal-title").html(); //.html()은 선택한 요소 안의 내용을 가져오거나, 다른 내용으로 바꿉니다
		  var replytext = $("#replytext").val();
		  
		  $.ajax({
				type:'delete',
				url:'/replies/'+rno,
				headers: { 
				      "Content-Type": "application/json",
				      "X-HTTP-Method-Override": "DELETE" },
				dataType:'text', 
				success:function(result){
					console.log("result: " + result);
					if(result == 'success'){
						alert("삭제 되었습니다.");
						getPage("/replies/"+bno+"/"+replyPage );
						// $("#modifyModal").modal("hide");
					}
			}});
	});
	
	function goLogin(){ //로그인 페이지로 이동ss
		self.location ="/user/login";
	}
	
	
</script>

<script id="templateAttach" type="text/x-handlebars-template">
<li data-src='{{fullName}}'>
	<span class= "mailbox-attachment-icon has-img"><img src="{{imgsrc}}" alt="Attachment"></span>
	<div class="mailbox-attachment-info">
		<a href="{{getLink}}" class="mailbox-attachment-name">{{fileName}}</a>
	</div>
</li>
</script>

<script>




</script>

   
<%@include file="../include/footer.jsp"%>

</html>