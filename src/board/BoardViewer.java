package board;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import control.Command;
import control.Ctrl;
import db.BoardDAO;

public class BoardViewer implements Command {

	HttpServletRequest request;
	HttpSession session;

	public BoardViewer(HttpServletRequest request) {
		this.request = request;
		this.session = request.getSession();
	}

	@Override
	public int execute() {

		BoardDAO dao = new BoardDAO(request);
		int requestBdNum = Integer
				.parseInt((String) request.getParameter("requestBdNum"));
		int result = dao.readBoard(requestBdNum);
		if (result == Ctrl.TRUE) {
			return result;
		} else if (result == Ctrl.FALSE) {
			session.setAttribute("bdViewerMsg", "삭제된 게시물입니다.");
			return result;
		}

		return Ctrl.EXCEPT;

	}
}
