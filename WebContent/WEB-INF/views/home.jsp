<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
This is Home
<a href="<c:url value='/loginPage.do'/>">Sign in HERE</a>
<a href="<c:url value='/logout.do'/>">Sign out HERE</a>
<script>
const msg = '${msg}'
if(msg) { //메시지가 있다면 alert창 띄우기
	alert(msg);
}
</script>
</body>
</html>