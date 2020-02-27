package board;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import control.Command;
import control.Ctrl;

public class BoardList implements Command {

	HttpServletRequest request;
	HttpSession session;
	private int totalContents; // 총 게시물 수
	private int listCount; // 한 페이지에 출력할 갯수
	private int totalPage; // 전체 페이지 수
	private int startNum; // 시작 페이지
	private int endNum; // 끝 페이지
	private int currentPage; // 사용자가 요청한 페이지 번호

	public BoardList(HttpServletRequest request) {
		this.request = request;
		this.session = request.getSession();

	}

	@Override
	public int execute() {
		return Ctrl.TRUE;	// 작성중
	}

	private int paging() {

		// 사용자에게 요청온 페이지 정보와, 한 페이지에 출력할 게시물 갯수
		currentPage = Integer.parseInt(request.getParameter("page"));
		listCount = 10;

		// DB에서 받아온 총 게시물 수가 하나 이상일 때 (테스트 데이터)
		totalContents = 1016;

		if (totalContents > 0) {

			// 전체 페이지는 "전체 게시물 수 / 한페이지에 출력할 갯수"
			// 나머지가 있다면 1페이지 추가
			totalPage = (totalContents / listCount);
			if (totalContents % listCount > 0) {
				totalPage++;
			}

			// 출력해줄 페이지 범위 지정
			startNum = ((currentPage - 1) / 10) * 10 + 1;
			endNum = startNum + listCount - 1;
			// 마지막 페이징 범위에서 끝 페이지 갯수 조정
			if (endNum > totalPage) {
				endNum = totalPage;
			}

			// 페이징 범위를 배열로 만듦
			ArrayList<Integer> temp = new ArrayList<>();
			for (int i = startNum; i <= endNum; i++) {
				temp.add(i);
			}

			Integer[] paging = temp.toArray(new Integer[temp.size()]);
			session.setAttribute("paging", paging);
			return Ctrl.TRUE;

			// 게시물이 하나도 없을 경우
		} else {
			return Ctrl.FALSE;
		}
	}
}
