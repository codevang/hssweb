package login;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import control.Command;
import control.Ctrl;
import db.UserInfoDAO;

public class LoginAction implements Command {

	HttpServletRequest request;
	HttpServletResponse response;
	public LoginAction
	(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	@Override
	public int execute() {

		HttpSession session = request.getSession();
		// 이미 로그인 돼있는경우
		if (session.getAttribute("userID") != null) {
			return Ctrl.TRUE;
		}
		
		UserInfoDAO dao = new UserInfoDAO(request);
		int result = dao.loginAccept();

		// 로그인 성공 시 세션 및 쿠키 설정
		if (result == Ctrl.TRUE) {
			String userID = request.getParameter("userID");
			session.setAttribute("userID", userID);
			
			// 자동로그인 체크됐다면 쿠키 추가
			String isAuto = request.getParameter("isAutoLogin"); 
			if (isAuto != null && isAuto.equals("true")) {
				Cookie cookie = new Cookie("userID", userID);
				cookie.setMaxAge(60*2); // 테스트를 위해 2분으로 설정
				cookie.setPath("/");	// 쿠키 저장 위치
				response.addCookie(cookie);
			}
			
			return Ctrl.TRUE;

		// 실패 시
		} else if (result == Ctrl.FALSE) {
			session.setAttribute
			("loginMsg", "로그인에 실패했습니다. 계정을 확인해주세요.");
			return Ctrl.FALSE;

			// 예외 발생 시
		} else {
			return Ctrl.EXCEPT;
		}
	}
}
