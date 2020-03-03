<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
%>

<!-- TOP 레이아웃 -->
<jsp:include page="/layout/topLayout.jspf" flush="false" />

<!-- 로그인된 사용자가 아니라면 로그인창으로 돌려줌 -->
<%
	if(session.getAttribute("userID") == null) {
		session.setAttribute("loginMsg", "게시물 작성은<br>로그인이 필요합니다.");
		response.sendRedirect("/login");
		return;
	}
%>

<!-- 게시물 계층 정보 -->
<%!int bdGroup, bdOrder, bdIndent;%>
<%
	bdGroup = Integer
			.parseInt((String) request.getParameter("bdGroup"));
	bdOrder = Integer
			.parseInt((String) request.getParameter("bdOrder"));
	bdIndent = Integer
			.parseInt((String) request.getParameter("bdIndent"));
%>

<%-- 입력 폼 --%>
<form action="writerAction" method="post">
	<input type="hidden" name="bdGroup" value=<%=bdGroup%>>
	<input type="hidden" name="bdOrder" value=<%=bdOrder%>>
	<input type="hidden" name="bdIndent" value=<%=bdIndent%>>
	<input type="text" name="bdTitle" class="form-control mt-4 mb-2"
		placeholder="제목을 입력해주세요." required
	>
	<div class="form-group">
		<textarea class="form-control" rows="10" name="bdContent"
			placeholder="내용을 입력해주세요" required
		></textarea>
	</div>
	<button type="submit" class="btn btn-secondary mb-3">제출하기</button>
</form>



<!-- Bottom 레이아웃 -->
<jsp:include page="/layout/bottomLayout.jspf" flush="false" />

