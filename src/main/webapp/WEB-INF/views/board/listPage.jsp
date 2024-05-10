<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page session="false"%>
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
		jobForm.attr("action","/board/listPage").attr("method", "get");
		jobForm.submit();
	});
</script>
<title>listAll</title>
</head>
<body>
	<table class="table table-bordered">
		<tr>
			<th style="width: 10px">BNO</th>
			<th>TITLE</th>
			<th>WRITER</th>
			<th>REGDATE</th>
			<th style="width: 40px">VIEWCNT</th>
		</tr>
 
		<c:forEach items="${list}" var="boardVO">
		
			<tr>
				<td>${boardVO.bno}</td>
				<td><a href='/board/readPage${pageMaker.MakeQuery(pageMaker.cri.page)}&bno=${boardVO.bno}'>${boardVO.title}</a></td>				
				<td>${boardVO.writer}</td>
				<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm"
						value="${boardVO.regdate}" /></td>
				<td><span class="badge bg-red">${boardVO.viewcnt}</span></td>
			</tr>
		
		</c:forEach>		
	</table>
	
	<div class="text-center">
		<ul class="pagination">
		
		 <c:if test="${pageMaker.prev}">
		 	<li><a href="listPage?page=${pageMaker.startPage - 1}">&laquo;</a></li>
		 </c:if>
		 
		 <c:forEach begin="${pageMaker.startPage}" end="${pageMaker.endPage}" var="idx">
		 
		 	<li <c:out value="${pageMaker.cri.page == idx?'class = active':''}"/>>
		 		<!-- <a href="listPage?page=${idx}">${idx}</a> 기존 방식 -->

		 		<!-- <a href="${idx}">${idx}</a> --> <!-- javascript를 이용하는 링크의 처리 -->
		 		<a href="listPage${pageMaker.MakeQuery(idx)}">${idx}</a> <!--UrilComponets를 이용한방법 -->
		 	</li>
		 </c:forEach>
		 
		 <c:if test="${pageMaker.next && pageMaker.endPage > 0}">
		 	<li>
		 	<!--<a href="listPage?page=${pageMaker.endPage + 1}">&raquo;</a>  기존방식-->
		 	<a href="listPage?page=${pageMaker.endPage + 1}">&raquo;</a> <!--UrilComponets를 이용한방법 -->
		 	</li>
		 </c:if>
		 
		</ul>
	</div>
	

</body>
	<form id="jobForm">
	  <input type='hidden' name="page" value=${pageMaker.cri.page}>
	  <input type='hidden' name="perPageNum" value=${pageMaker.cri.perPageNum}>
	</form>
</html>
<%@include file="../include/footer.jsp"%>