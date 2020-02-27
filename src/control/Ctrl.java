package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import login.LoginAction;
import login.LogoutAction;
import login.RegisterAction;
import userInfo.GetUserInfo;

@WebServlet("/")
public class Ctrl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// command에서 리턴받을 종류
	public static final int TRUE = 0;
	public static final int FALSE = 1;
	public static final int EXCEPT = 2;

	public Ctrl() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		doAction(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doAction(request, response);
	}

	protected void doAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		String uri = request.getRequestURI();
		
		String page = null;  // 리다이렉트할 페이지 넣을 문자열
		Command command = null; // 실행할 커멘드 클래스를 담아줄 인터페이스 객체 
		
		/* 메인에서 로그인 버튼 눌렀을 때 */
		if(uri.equals("/login")) {
			page = "/join/login.jsp";
	
		/* 메인에서 회원가입 버튼 눌렀을 때 */
		} else if(uri.equals("/register")) {
			page = "/join/register.jsp";		

		/* join/login.jsp에서 계정 넣고 로그인 요청 했을 때 */
		} else if(uri.equals("/join/loginAsk")) {
			command = new LoginAction(request, response);
			int result = command.execute();
			
			// TRUE일 경우 메인 화면으로
			if(result == Ctrl.TRUE) {
				page = "/main.jsp";
			// FALSE일 경우 다시 로그인 페이지로
			} else if(result == Ctrl.FALSE) { 
				page = "/join/login.jsp";
			// EXCEPT일 경우 에러 페이지로
			} else {
				page = "/exception/exception.jsp";
			}			
			
		/* join/register.jsp에서 회원가입 폼 작성하고 가입 눌렀을 때 */
		} else if(uri.equals("/join/registerAsk")) {
			command = new RegisterAction(request);
			int result = command.execute();
			
			// TRUE일 경우 로그인 페이지로
			if (result == Ctrl.TRUE) {
				page = "/join/login.jsp";
			// FALSE일 경우 회원가입 페이지로 (session에 추가된 메세지 출력) 
			} else if(result == Ctrl.FALSE){
				page = "/join/register.jsp";
			// EXCEPT일 경우 에러 페이지로 
			} else {
				page = "/exception/exception.jsp";
			}

		/* 메인에서 로그아웃 버튼 눌렀을 때 세션 및 쿠키 제거 */
		} else if(uri.equals("/logout")) {
			command = new LogoutAction(request, response);
			command.execute();
			page = "/main.jsp";
			
		/* 메인에서 회원정보조회 버튼 눌렀을 때 */
		} else if(uri.equals("/getUserInfo")) {
			command = new GetUserInfo(request);
			int result = command.execute();
			
			if(result == Ctrl.TRUE) {
				page = "/userInfo/getUserInfo.jsp";
			// FALSE일 경우 로그인 페이지로
			} else if(result == Ctrl.FALSE) {
				page = "/join/login.jsp";
			} else {
				page = "/exception/exception.jsp";
			}
			
		/* 작업 시 예외 발생할 경우 */
		} else if(uri.equals("/exception")) {
			page = "/exception/exception.jsp";
			
		/* 그 외 URI일 경우 메인 첫화면으로 돌려줌  */
		} else {
			page = "/main.jsp";
		}
		
		response.sendRedirect(page);
	}
}
