<%@page import="db.BoardDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
%>

<!-- TOP 레이아웃 -->
<jsp:include page="/layout/topLayout.jspf" flush="false" />

<!-- 게시물 내용  -->
<%
	BoardDTO dto = (BoardDTO) request.getAttribute("boardDTO");
	if (dto != null) {
%>

<!-- 답글 버튼, 로그인한 사용자만 답글쓰기 열어줌 -->
<div class="container my-3">
	<div class="row">
		<div class="col"></div>
		<div class="col-md-auto">
			<%
				if (session.getAttribute("userID") == null) {
			%>

			답글 작성은 로그인이 필요합니다.

			<%-- <% --%>
			<%
				} else {
			%>

			<%-- 답글작성 요청 시 가질 값을 미리 셋팅해서 요청(그룹은 같게, order와 Indent는 1증가) --%>
			<form action="boardWriter" method="post">
				<input type="hidden" name="bdGroup" value=<%=dto.getBdGroup()%>>
				<input type="hidden" name="bdOrder" value=<%=dto.getBdOrder() + 1%>>
				<input type="hidden" name="bdIndent"
					value=<%=dto.getBdIndent() + 1%>
				>
				<button type="submit" class="btn btn-secondary">답글쓰기</button>
			</form>

			<%
				}
			%>
		</div>
	</div>
</div>


<!-- 게시물 내용 출력 -->
<div class="container my-1">
	<div class="row">
		<table class="table">
			<thead>
				<tr class="table-active">
					<th scope="col" style="width: 60%"><%=dto.getBdTitle()%><br>
						<%=dto.getBdUserID()%></th>
					<th scope="col" style="width: 40%" class="text-right">조회 : <%=dto.getBdViewCount()%>
						<br> <%=dto.getBdDate()%></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><pre><%=dto.getBdContent()%></pre></td>
				</tr>
			</tbody>
		</table>
	</div>
</div>


<%-- 게시물이 없을 경우 메세지 출력 (없거나 삭제된 게시물) --%>
<%
	} else {
		Object obj = request.getAttribute("bdViewerMsg");
		if (obj != null) {
			String msg = (String) obj;
%>

<div class="container">
	<div class="row">
		<div class="col"></div>
		<div class="col-md-auto"><%=msg%></div>
		<div class="col"></div>
	</div>
</div>

<%
		}
	}
%>



<!-- Bottom 레이아웃 -->
<jsp:include page="/layout/bottomLayout.jspf" flush="false" />
