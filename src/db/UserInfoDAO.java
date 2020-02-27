package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import control.Ctrl;

public class UserInfoDAO {

	private HttpServletRequest request;
	private Context context;
	private DataSource datasource;
	private Connection conn;
	private Statement stmt;
	private PreparedStatement preStmt;
	private ResultSet result;

	/* 생성자 */
	public UserInfoDAO(HttpServletRequest request) {
		this.request = request;
	}

	/* 회원가입 정보 DB 입력 */
	public int registerUserInfo(UserInfoDTO dto) {

		try {
			int check = isUserID(dto);

			// 이미 존재하는 ID일 경우
			if (check == Ctrl.TRUE) {
				return Ctrl.FALSE;

				// 존재하지 않는 경우 등록
			} else if (check == Ctrl.FALSE) {

				preStmt = conn.prepareStatement(
						"insert into ORG_USER values (?,?,?,?,?,?)");
				preStmt.setString(1, dto.getUserID());
				preStmt.setString(2, dto.getUserPW());
				preStmt.setString(3, dto.getUserName());
				preStmt.setString(4, dto.getUserEmail());
				preStmt.setString(5, dto.getUserPhone());
				preStmt.setString(6, dto.getUserGender());

				// 커밋하고 리턴
				preStmt.executeUpdate();
				conn.commit();
				return Ctrl.TRUE;
			}

			// DB접속 및 쿼리 과정에서 예외 발생 시 롤백 수행
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception rollbackEx) {
				System.out.println("rollback Exception");
				rollbackEx.printStackTrace();
			}

			System.out.println("DB Working Exception");
			e.printStackTrace();

			// 최종 자원 해제
		} finally {
			dbClose();
		}

		// 여기까지 리턴이 넘어왔으면 예외 발생의 경우
		return Ctrl.EXCEPT;
	}

	/* 로그인 검증 */
	public int loginAccept() {

		try {

			dbConnect();
			stmt = conn.createStatement();
			String userID = request.getParameter("userID");
			String userPW = request.getParameter("userPW");
			String query = "select userID, userPW from ORG_USER "
					+ "where userID = " + "'" + userID + "'";
			result = stmt.executeQuery(query);

			// 있으면 TRUE
			while (result.next()) {
				if (result.getString("USERID").equals(userID)
						&& result.getString("USERPW")
								.equals(userPW)) {
					return Ctrl.TRUE;
				}
			}

			// 없으면 FALSE
			return Ctrl.FALSE;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}

		// 리턴이 여기까지 왔다면 예외 발생 상황
		return Ctrl.EXCEPT;
	}

	/* 회원정보 출력 */
	public int getUserInfo() {

		try {
			HttpSession session = request.getSession();
			dbConnect();
			stmt = conn.createStatement();
			String query = "select * from ORG_USER where userID = "
					+ "'" + session.getAttribute("userID") + "'";
			result = stmt.executeQuery(query);

			UserInfoDTO dto = new UserInfoDTO();
			while (result.next()) {
				dto.setUserID(result.getString("userID"));
				dto.setUserName(result.getString("userName"));
				dto.setUserEmail(result.getString("userEmail"));
				dto.setUserPhone(result.getString("userPhone"));
			}

			// 세션에 dto 객체 추가
			session.setAttribute("userInfo", dto);
			return Ctrl.TRUE;

		} catch (Exception e) {
			System.out.println("UserInfo DB Select Fail");
			e.printStackTrace();

		} finally {
			dbClose();
		}

		// 예외 발생
		return Ctrl.EXCEPT;
	}

	/* ID 존재 여부 확인 */
	private int isUserID(UserInfoDTO dto) throws Exception {

		// DB 연결
		dbConnect();
		String query = "select userID from ORG_USER "
				+ "where userID = " + "'" + dto.getUserID() + "'";
		result = stmt.executeQuery(query);

		// userID가 있다면
		if (result.next()) {
			return Ctrl.TRUE;

			// userID가 없다면
		} else {

			// 자원해제 (호출한 메소드에서 result 필드를 재사용할 것이므로)
			if (result != null) {
				try {
					result.close();
				} catch (Exception resultCloseEx) {
					resultCloseEx.printStackTrace();
				}
			}

			return Ctrl.FALSE;
		}
	}

	/* DB 연결 */
	private void dbConnect() throws Exception {

		context = new InitialContext();
		datasource = (DataSource) context
				.lookup("java:comp/env/jdbc/Oracle11g");
		conn = datasource.getConnection();
		conn.setAutoCommit(false); // 오토커밋 해제
		stmt = conn.createStatement();
	}

	/* DB close (모든 객체 자원 해제) */
	private void dbClose() {
		if (result != null) {
			try {
				result.close();
			} catch (Exception resultCloseEx) {
				resultCloseEx.printStackTrace();
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception resultCloseEx) {
				resultCloseEx.printStackTrace();
			}
		}
		if (preStmt != null) {
			try {
				preStmt.close();
			} catch (Exception resultCloseEx) {
				resultCloseEx.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception resultCloseEx) {
				resultCloseEx.printStackTrace();
			}
		}
	}
}