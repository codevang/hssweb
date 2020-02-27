package userInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import control.Command;
import control.Ctrl;
import db.UserInfoDAO;

public class GetUserInfo implements Command {

	HttpServletRequest request;

	public GetUserInfo(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public int execute() {

		HttpSession session = request.getSession();

		// 이미 세션에 userInfo가 담겨 있는 경우라면
		if (session.getAttribute("userInfo") != null) {
			return Ctrl.TRUE;

			// 로그인된 세션이 맞는지 한번 더 확인
		} else if (session.getAttribute("userID") != null) {
			UserInfoDAO dao = new UserInfoDAO(request);
			int result = dao.getUserInfo();

			// 결과 그대로 리턴
			return result;
		}
		
		return Ctrl.EXCEPT;
	}
}
