<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html"; charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h4>${exception.getMessage()}</h4>
	
	<ul>
		<c:forEach items="${exception.getStackTrace()}" var="stack">
		<li>${stack.toString()}</li>
		</c:forEach>
	</ul>
</body>
</html>