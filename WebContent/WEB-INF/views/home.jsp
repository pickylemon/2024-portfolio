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
<h3><a href="<c:url value='/loginPage.do'/>">Sign in HERE</a></h3>
<h3><a href="<c:url value='/logout.do'/>">Sign out HERE</a></h3>
<h3><a href="<c:url value='/forum/notice/listPage.do'/>">BOARD</a></h3>

<script>
const msg = '${msg}'
if(msg) { //메시지가 있다면 alert창 띄우기
	alert(msg);
}
</script>
</body>
</html>