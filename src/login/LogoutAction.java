package login;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import control.Command;
import control.Ctrl;

public class LogoutAction implements Command {

	HttpServletRequest request;
	HttpServletResponse response;

	public LogoutAction(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	@Override
	public int execute() {
		// userID 네임의 쿠키 찾아서 무효화
		Cookie[] cookie = request.getCookies();

		if (cookie != null) {
			for (int i = 0; i < cookie.length; i++) {
				if (cookie[i].getName().equals("userID")) {
					cookie[i].setMaxAge(0);
					cookie[i].setPath("/"); // 패스 설정
					response.addCookie(cookie[i]);
					break;
				}
			}
			request.getSession().invalidate();
			return Ctrl.TRUE;
		}
		return Ctrl.FALSE;
	}
}