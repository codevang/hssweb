<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<!-- 헤드 레이아웃 include -->
<jsp:include page = "/layout/headLayout.jspf" flush="false"/>

<!-- 회원가입 (페이지 직접 접속할 경우에도 세션에 로그인 값 있으면 안보여줌)  -->
<% if (session.getAttribute("userID") == null) { %>
<p>
<form action = "registerAsk" method = "post">
	ID : <input type="text" name="userID" size="10" required/><br>
	패스워드 : <input type="password" name="userPW" size="12" required/><br>
	패스워드 확인 : <input type="password" name="userPW2" size="12" required/><br>
	이름 : <input type="text" name="userName" size="10" required/><br>
	이메일 : <input type="text" name="userEmail" size="30" required/><br>
	전화번호 : <input type="text" name="userPhone" size="15" required/><br>
	성별 : <input type="radio" name="userGender" value="men" required/>남 
	<input type="radio" name="userGender" value="women">여<br>
	<input type="submit" value="가입하기">
</form>
</p>
<% } %>

<%
	// 회원가입 체크에서 실패 했을 경우 실패 메세지를 받아서 출력해줌
	Object obj = session.getAttribute("formMsg");
	if(obj != null) {
		String msg = (String)obj;
		out.println(msg);
		session.removeAttribute("formMsg");
	}
%>

</body>
</html>