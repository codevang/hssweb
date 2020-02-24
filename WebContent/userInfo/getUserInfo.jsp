<%@page import="db.UserInfoDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<!-- 헤드삽입 -->
<jsp:include page = "/layout/headLayout.jspf" flush = "false"/>

<!-- 로그인된 사용자 및 userInfo가 있는 사용자만 보여줌 (페이지로 직접 접근할 경우 대비) -->
<%! UserInfoDTO dto; %>
<% if (session.getAttribute("userID") != null &&
	(dto = (UserInfoDTO)session.getAttribute("userInfo")) != null) { %>

	<p>
		아이디 : <%= dto.getUserID() %><br>
		이름 : <%= dto.getUserName() %><br>
		이메일 : <%= dto.getUserEmail() %><br>
		전화번호 : <%= dto.getUserPhone() %><br>
	</p>

<% } else { %>
	
	<p>로그인 후 회원정보를 조회해 주세요. </p>
	
<% } %>

</body>
</html>