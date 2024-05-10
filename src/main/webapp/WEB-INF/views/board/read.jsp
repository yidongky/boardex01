<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../include/header.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>읽기페이지</title>
</head>
<body>

<!-- Mian content -->
	<form role="form" method="post">	
		<input type="hidden" name='bno' value="${boardVO.bno}"> <!-- 번호 -->		
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
			<button type="submit" class="btn btn-warning">Modify</button>
			<button type="submit" class="btn btn-danger">REMOVE</button>
			<button type="submit" class="btn btn-primary">LIST ALL</button>
		</div>
	

</body>

<script type="text/javascript">
$( document ).ready( function() {
	
	var formObj = $("form[role='form']");
	
	console.log(formObj);
	
	$(".btn-primary").on("click", function(){
		self.location = "/board/listAll";
	});
	
	$(".btn-warning").on("click",function(){
		formObj.attr("action","/board/modify");
		formObj.attr("method","get");
		formObj.submit();		
	});
	
	
	$(".btn-danger").on("click",function(){
		formObj.attr("action","/board/remove");
		formObj.submit();
	});
	

	
	
} );
</script>
</html>