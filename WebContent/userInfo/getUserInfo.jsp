<%@page import="db.UserInfoDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<body>

	<!-- 헤드삽입 -->
	<jsp:include page="/layout/topLayout.jspf" flush="false" />

	<!-- 로그인된 사용자 및 userInfo가 있는 사용자만 보여줌 (페이지로 직접 접근할 경우 대비) -->
	<%!UserInfoDTO dto;%>
	<%
		if (session.getAttribute("userID") != null
				&& (dto = (UserInfoDTO) session
						.getAttribute("userInfo")) != null) {
	%>

	<div class="container mt-2">

		<!-- 회원정보 출력 그리드 -->
		<div class="row">
			<div class="col-lg-6">

				<table class="table">
					<thead>
						<tr>
							<th scope="col" class="text-center table-success mr-1">구분</th>
							<th scope="col" class="text-center table-success">회원정보</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<th scope="row" class="text-center">ID</th>
							<td class="text-center"><%=dto.getUserID()%></td>
						</tr>
						<tr>
							<th scope="row" class="text-center">이름</th>
							<td class="text-center"><%=dto.getUserName()%></td>
						</tr>
						<tr>
							<th scope="row" class="text-center">이메일</th>
							<td class="text-center"><%=dto.getUserEmail()%></td>
						</tr>
						<tr>
							<th scope="row" class="text-center">전화번호</th>
							<td class="text-center"><%=dto.getUserPhone()%></td>
						</tr>

					</tbody>
				</table>

			</div>
		</div>

		<!-- 여백 그림 넣을 그리드 -->
		<div class="col-lg-6"></div>
	</div>

	<%
		} else {
			response.sendRedirect("/login");
			session.setAttribute("loginMsg", "로그인 후 회원 정보를 조회해 주세요.");
		}
	%>

	<jsp:include page="/layout/bottomLayout.jspf" flush="false" />

</body>
</html>