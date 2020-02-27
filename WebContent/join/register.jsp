<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<body>

	<!-- 헤드 레이아웃 include -->
	<jsp:include page="/layout/topLayout.jspf" flush="false" />

	<!-- 회원가입 (페이지 직접 접속할 경우에도 세션에 로그인 값 있으면 안보여줌)  -->
	<%
		if (session.getAttribute("userID") == null) {
	%>

	<div class="container mt-2">

		<div class="row">

			<!-- 입력폼 그리드 -->
			<div class="col-lg-6">
				<form action="registerAsk" method="post">
					<!-- ID 입력 -->
					<div class="input-group mt-3 mb-1">
						<div class="input-group-prepend">
							<span class="input-group-text" id="basic-addon1">ID</span>
						</div>
						<input type="text" name="userID" class="form-control"
							placeholder="Input ID" aria-label="Input ID"
							aria-describedby="basic-addon1" required>
					</div>
					<!-- PW입력 -->
					<div class="input-group mb-1">
						<div class="input-group-prepend">
							<span class="input-group-text" id="basic-addon1">PW</span>
						</div>
						<input type="password" name="userPW" class="form-control"
							placeholder="Input Password" aria-label="Input Password"
							aria-describedby="basic-addon1" required>
					</div>
					<!-- PW 확인 -->
					<div class="input-group mb-2">
						<div class="input-group-prepend">
							<span class="input-group-text" id="basic-addon1">PW확인</span>
						</div>
						<input type="password" name="userPW2" class="form-control"
							placeholder="Confirm Password" aria-label="Confirm Password"
							aria-describedby="basic-addon1" required>
					</div>

					<!-- 이름 입력 -->
					<div class="input-group mb-2">
						<div class="input-group-prepend">
							<span class="input-group-text" id="basic-addon1">이름</span>
						</div>
						<input type="text" name="userName" class="form-control"
							placeholder="Input Name" aria-label="Input Name"
							aria-describedby="basic-addon1" required>
					</div>

					<!-- 이메일 입력 -->
					<div class="input-group mb-2">
						<div class="input-group-prepend">
							<span class="input-group-text" id="basic-addon1">이메일</span>
						</div>
						<input type="text" name="userEmail" class="form-control"
							placeholder="Input Email" aria-label="Input Email"
							aria-describedby="basic-addon1" required>
					</div>

					<!-- 전화번호 입력 -->
					<div class="input-group mb-2">
						<div class="input-group-prepend">
							<span class="input-group-text" id="basic-addon1">전화번호</span>
						</div>
						<input type="text" name="userPhone" class="form-control"
							placeholder="Input PhoneNumber" aria-label="Input PhoneNumber"
							aria-describedby="basic-addon1" required>
					</div>

					<!-- 성별 체크 -->
					<div class="input-group mb-2">
						<div class="input-group-prepend">
							<span class="input-group-text" id="basic-addon1">성별</span>
						</div>
						<span class="form-control" id="basic-addon1"> <input
							type="radio" name="userGender" value="men"
							aria-label="Input Gender" required> <a class="ml-1">남</a>
						</span> <span class="form-control" id="basic-addon1"> <input
							type="radio" name="userGender" value="women"
							aria-label="Input Gender" required> <a class="ml-1">여</a>
						</span>
					</div>
					<!-- 가입 버튼 -->
					<button type="submit" class="btn btn-dark btn-sm btn-block mb-3">
						가입 하기</button>
				</form>
			</div>

			<%
				}
			%>

			<!-- 그림넣을 그리드 -->
			<div="col-lg-6"></div>
		</div>

		<div class="container mt-2">

			<%
				// 회원가입 성공, 로그인 실패 등 메세지가 있으면 출력 후 삭제
				Object obj = session.getAttribute("formMsg");
				if (obj != null) {
					String msg = (String) obj;
			%>

			<div class="row">
				<div class="col-lg-6">
					<div class="alert alert-danger text-center" role="alert"><%=msg%></div>
				</div>
				<div class="col-lg-6"></div>
			</div>

			<%
				// 출력한 메세지는 지워줌
					session.removeAttribute("formMsg");
				}
			%>
		</div>
	</div>

	<jsp:include page="/layout/bottomLayout.jspf" flush="false" />
</body>
</html>