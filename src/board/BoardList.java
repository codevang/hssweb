package board;

import javax.servlet.http.HttpServletRequest;

import control.Command;
import control.Ctrl;
import db.BoardDAO;

public class BoardList implements Command {

	// 한 화면에 보여줄 리스트 갯수, 페이징 범위의 갯수
	public static final int pagePerList = 10;
	public static final int pagingCount = 10;

	HttpServletRequest request;

	public BoardList(HttpServletRequest request) {
		this.request = request;

	}

	@Override
	public int execute() {

		// 요청받은 페이지
		int page = Integer.parseInt(request.getParameter("requestedPage"));
		BoardDAO dao = new BoardDAO(request);
		int result = dao.readList(page);
		if (result == Ctrl.TRUE) {
			return result;
		} else if (result == Ctrl.FALSE) {
			request.setAttribute("bdListMsg", "등록된 게시물이 없습니다.");
			return result;
		} else {
			return result;
		}
	}
}