<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../include/header.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>등록페이지</title>
</head>
<body>

<!-- Mian content -->
	<form role="form" method="post">
		<div class="box-body">
			<div class="form-group">
				<label for="examleInputEmail1">title</label>
				<input type="text" name="title" class="form-control" placeholder="Enter Title">
			</div>
			<div class="form-group">
				<label for="exampleInputPassword1">Content</label>
				<textarea class="form-control" name="content" rows="3" placeholder="Enter ..."></textarea>
			</div>
			<div class="form-group">
				<label for="exampleInputEmail1">Writer</label>
				<input type="text" name="writer" class="form-control" placeholder="Enter Writer">
			</div>
		</div>
		
		<!--  /.box body -->
		<div class="box-footer">
			<button type="submit" class="btn btn-primary">submit</button>
		</div>
	
	</form>

</body>
</html>