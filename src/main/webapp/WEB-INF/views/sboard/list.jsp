<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@include file="../include/header.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<script type="text/javascript">
	var result = '${msg}';
	
	if(result == "success"){
		alert("처리가 완료 되었습니다.");
	}
	//javascript를 통한  URI 처리
	$(".pagination li a").on("click", function(event){
		event.preventDefault(); 
		
		var targetPage = $(this).attr("href");
		
		var jobForm = $("#jobForm");
		jobForm.find("[name='page']").val(targetPage);
		jobForm.attr("action","/sboard/list").attr("method", "get");
		jobForm.submit();
	});
	
	$(document).ready(
			//MakeQuery()는 검색조건이 없는 상황에서 사용하는 메소드 입니다. 검색 조건이 없는 링크를 생성하고 ,필요한 링크를 뒤에 연결 시키는 방식으로 처리
			function() {
				//검색타입 설정
				$('#searchBtn').on(
						"click",
						function(event) {

							str = "list"
									+ '${pageMaker.MakeQuery(1)}'
									+ "&searchType="
									+ $("select option:selected").val()
									+ "&keyword=" + encodeURIComponent($('#keywordInput').val());
									
							console.log(str);
							self.location = str;

						});
				// 등록하기
				$('#newBtn').on("click", function(evt) {


					self.location = "register?page=${cri.page}&perPageNum=${cri.perPageNum}&searchType=${cri.searchType}&keyword=${cri.keyword}";

				});

			});
	
	function enterkey() {
        if (window.event.keyCode == 13) {
        	$('#searchBtn').click();
        }
	}
</script>
<title>listAll</title>
</head>
<body>

<div class="box-body">
	<select name="searchType">
		<option value="n" <c:out value="${cri.searchType == null?'selected':'' }"/>>---</option>
		<option value="t" <c:out value="${cri.searchType eq 't'?'selected':'' }"/>>Title</option>
		<option value="c" <c:out value="${cri.searchType eq 'c'?'selected':'' }"/>>Content</option>
		<option value="w" <c:out value="${cri.searchType eq 'w'?'selected':'' }"/>>Writer</option>
		<option value="tc" <c:out value="${cri.searchType eq 'tc'?'selected':'' }"/>>Title OR Content</option>
		<option value="cw" <c:out value="${cri.searchType eq 'cw'?'selected':'' }"/>>Content Or Writer</option>
		<option value="tcw" <c:out value="${cri.searchType eq 'tcw'?'selected':'' }"/>>Title OR Content OR Writer</option>
	</select>
	<input type="text" name= "keyword" id="keywordInput" value='${cri.keyword}'onkeyup="enterkey();">
	<button id='searchBtn'>search</button>
	<button id='newBtn'>New Board</button>
</div>
	<table class="table table-bordered">
		<tr>
			<th style="width: 10px">NO</th>
			<th>TITLE</th>
			<th>WRITER</th>
			<th>REGDATE</th>
			<th style="width: 40px">VIEWCNT</th>
		</tr>
		<c:set var="num" value="${(((pageMaker.cri.page)-1) * pageMaker.cri.perPageNum)+1}"/>
		<c:forEach items="${list}" var="boardVO">

		
			<tr>
				<td>${num}</td>
				<td><a href='/sboard/readPage${pageMaker.makeSearch(pageMaker.cri.page)}&bno=${boardVO.bno}'>${boardVO.title}
				<c:if test="${boardVO.replycnt > 0}">
				<strong>( ${boardVO.replycnt } )</strong>
				</c:if>
				</a></td>				
				<td>${boardVO.writer}</td>
				<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm"
						value="${boardVO.regdate}" /></td>
				<td><span class="badge bg-red">${boardVO.viewcnt}</span></td>
			</tr>
		<c:set var="num" value="${num+1 }"></c:set>
		</c:forEach>		
	</table>
	
	<div class="text-center">
		<ul class="pagination">
		 <c:if test="${pageMaker.prev}">
			<li><a href="list${pageMaker.makeSearch(1) }">&#9664;</a></li>		 
		 	<li><a href="list${pageMaker.makeSearch(pageMaker.startPage - 1) }">&laquo;</a></li>
		 </c:if>
		 
		 <c:forEach begin="${pageMaker.startPage}" end="${pageMaker.endPage}" var="idx">
		 
		 	<li <c:out value="${pageMaker.cri.page == idx?'class = active':''}"/>>
		 		<!-- <a href="list?page=${idx}">${idx}</a> 기존 방식 -->

		 		<!-- <a href="${idx}">${idx}</a> --> <!-- javascript를 이용하는 링크의 처리 -->
		 		<a href="list${pageMaker.makeSearch(idx)}">${idx}</a> <!--UrilComponets를 이용한방법 -->
		 	</li>
		 </c:forEach>
		 
		 <c:if test="${pageMaker.next && pageMaker.endPage > 0}">
		 	<li>
		 	<!--<a href="list?page=${pageMaker.endPage + 1}">&raquo;</a>  기존방식-->
		 	<a href="list${pageMaker.makeSearch(pageMaker.endPage + 1)}">&raquo;</a> <!--UrilComponets를 이용한방법 -->
		 	</li>
		 	<li>
		 	<!--<a href="list?page=${pageMaker.endPage + 1}">&raquo;</a>  기존방식-->
		 	<a href="list${pageMaker.makeSearch(pageMaker.lastPage)}">&#9654;</a> <!--UrilComponets를 이용한방법 -->
		 	</li>
		 </c:if>
		 
		</ul>
	</div>
	

</body>
	<form id="jobForm">
	  <input type='hidden' name="page" value=${pageMaker.cri.page}>
	  <input type='hidden' name="perPageNum" value=${pageMaker.cri.perPageNum}>
	</form>
<%@include file="../include/footer.jsp"%>	
</html>
