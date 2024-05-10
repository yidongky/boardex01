<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../include/header.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>수정페이지</title>
</head>
<body>

<!-- Mian content -->
	<form role="form" method="post">
		
		<div class="box-body">
			<div class="form-group">
				<label for="examleInputEmail1">BNO</label>
				<input type="text" name="bno" class="form-control" value="${boardVO.bno}" readonly="readonly">
			</div>
			<div class="form-group">
				<label for="examleInputEmail1">title</label>
				<input type="text" name="title" class="form-control" value="${boardVO.title}">
			</div>
			<div class="form-group">
				<label for="exampleInputPassword1">Content</label>
				<textarea class="form-control" name="content" rows="3">
				${boardVO.content}
				</textarea>
			</div>
			<div class="form-group">
				<label for="exampleInputEmail1">Writer</label>
				<input type="text" name="writer" class="form-control" value="${boardVO.writer}"  readonly="readonly">
			</div>
		</div>
		
	</form>
		
		<!--  /.box body -->
		<div class="box-footer">
			<button type="submit" class="btn btn-primary">SAVE</button>
			<button type="submit" class="btn btn-warning">CANCEL</button>
		</div>
	

</body>

<script type="text/javascript">
$( document ).ready( function() {
	
	var formObj = $("form[role='form']");
	
	console.log(formObj);
	
	$(".btn-warning").on("click", function(){
		self.location = "/board/listAll";
	});
	
	$(".btn-primary").on("click", function(){
		formObj.submit();
	});
	
} );
</script>
</html>