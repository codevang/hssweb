package board;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import control.Command;
import db.BoardDAO;
import db.BoardDTO;

public class BoardWriter implements Command {

	HttpServletRequest request;
	HttpSession session;
	BoardDTO dto;
	BoardDAO dao;

	public BoardWriter(HttpServletRequest request) {
		this.request = request;
		this.session = request.getSession();
	}

	@Override
	public int execute() {

		makeDTO();
		dao = new BoardDAO(request);
		int result = dao.writeBoard(dto);
		return result;
	}

	// DTO 생성
	private void makeDTO() {

		dto = new BoardDTO();
		dto.setBdGroup(Integer.parseInt((String)request.getParameter("bdGroup")));
		dto.setBdOrder(Integer.parseInt((String)request.getParameter("bdOrder")));
		dto.setBdIndent(Integer.parseInt((String)request.getParameter("bdIndent")));
		dto.setBdTitle((String)request.getParameter("bdTitle"));
		dto.setBdContent((String)request.getParameter("bdContent"));
		dto.setBdUserID((String)session.getAttribute("userID"));
	}
}