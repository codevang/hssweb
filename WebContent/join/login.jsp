<%@ page import="login.SessionCheck" language="java"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<!-- 헤드 레이아웃 include -->
<jsp:include page = "/layout/headLayout.jspf" flush="false"/>

<!-- 세션에 정보가 없을 경우에만 로그인 창 보여주기 -->
<% if (session.getAttribute("userID") == null) { %>
	<p>
	<form action="loginAsk" method="post">
		ID : <input type="text" name="userID" size="10" required/><br> 
		PW : <input type="password" name="userPW" size="10" required/>
		<input type="submit" value="로그인"> <br>
		<input type = "checkbox" name = "isAutoLogin" value = "true"> 자동 로그인
	</form>	
	</p>
<% } %>

<%
	// 회원가입 성공, 로그인 실패 등 메세지가 있으면 출력 후 삭제
	Object obj = session.getAttribute("loginMsg");
	if(obj != null) {
		String msg = (String)obj;
		out.println(msg);
		session.removeAttribute("loginMsg");
	}
%>

</body>
</html>