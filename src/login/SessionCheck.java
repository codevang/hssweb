package login;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

public class SessionCheck {

	public static boolean loginCheck(HttpSession session, Cookie[] cookie) {

		// user_id가 등록된 세션인 경우 (이미 로그인 돼 있음) true 반환
		if (session.getAttribute("userID") != null) {
			return true;

		// 세션이 없을 경우 쿠키 정보를 확인
		} else if (cookie != null) {

			// 쿠키 확인
			for (int i = 0; i < cookie.length; i++) {

				// userID라는 이름을 가진 쿠키가 있다면 true 반환
				if (cookie[i].getName().equals("userID")) {
					
					// 쿠키에 들어있는 userID를 세션에 추가해줌
					session.setAttribute("userID", cookie[i].getValue());
					return true;
				}
			}
		}

		// 세션도 없고 쿠키도 없는 경우
		return false;
	}
}