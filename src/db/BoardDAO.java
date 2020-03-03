package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import board.BoardList;
import control.Ctrl;

public class BoardDAO {

	private HttpServletRequest request;
	private Context context;
	private DataSource datasource;
	private Connection conn;
	private Statement stmt;
	private PreparedStatement preStmt;
	private ResultSet result;

	public BoardDAO(HttpServletRequest request) {
		this.request = request;
	}

	/* 리스트 보기 */
	public int readList(int page) {

		try {

			// 페이징 범위 산출 (null값은 게시물이 하나도 없을 경우)
			int[] paging = paging(page); // DB 커넥션은 여기서 해줌
			if (paging == null) {
				return Ctrl.FALSE;
			} else {
				request.setAttribute("bdPaging", paging); // 페이징 객체 전달
			}
			
			// 리스트 정보 가져오기
			String query = "select * from "
					+ "(select ROWNUM as rnum, A.* from "
					+ "(select * from board_chat order by bdgroup desc, bdorder)A ) "
					+ "where rnum >= ? and rnum <= ?";
			preStmt = conn.prepareStatement(query);
			// 요청된 페이지에 따른 게시물 범위 지정
			int startPage = (page - 1) * BoardList.pagePerList + 1; // 시작 게시물
			int endPage = startPage + BoardList.pagePerList - 1; // 끝 게시물
			preStmt.setInt(1, startPage);
			preStmt.setInt(2, endPage);
			result = preStmt.executeQuery();
			
			// 결과를 ArrayList에 추가
			ArrayList<BoardDTO> list = new ArrayList<>(); // 리스트 정보 담아줄 객체
			while (result.next()) {
				BoardDTO dto = new BoardDTO();
				dto.setBdNum(result.getInt("bdnum"));
				dto.setBdIndent(result.getInt("bdindent"));
				dto.setBdTitle(result.getString("bdtitle"));
				dto.setBdUserID(result.getString("bduserid"));
				dto.setBdViewCount(result.getInt("bdviewcount"));
				dto.setBdDate(result.getString("bddate"));

				list.add(dto);
			}

			// 일반 배열로 지정해서 전달 (제네릭 타입은 타입변환에서 warning 발생)
			BoardDTO[] bdList = list.toArray(new BoardDTO[list.size()]);
			request.setAttribute("bdList", bdList); // 리스트 전달

			return Ctrl.TRUE;

		} catch (Exception e) {
			System.out.println("readList db working Fail");
			e.printStackTrace();
		} finally {
			dbClose();
		}

		return Ctrl.EXCEPT;
	}

	/* 컨텐츠 보기 */
	public int readBoard(int bdNum) {

		BoardDTO dto = new BoardDTO();

		try {
			dbConnect(); // db 연결
			stmt = conn.createStatement();

			String query = "select * from board_chat where bdnum = " + bdNum;
			result = stmt.executeQuery(query);
			int i = 0;
			while (result.next()) {
				i++;
				dto.setBdNum(result.getInt("bdnum"));
				dto.setBdGroup(result.getInt("bdgroup"));
				dto.setBdOrder(result.getInt("bdorder"));
				dto.setBdIndent(result.getInt("bdindent"));
				dto.setBdTitle(result.getString("bdtitle"));
				dto.setBdContent(result.getString("bdcontent"));
				dto.setBdUserID(result.getString("bduserid"));
				dto.setBdViewCount(result.getInt("bdviewcount"));
				dto.setBdDate(result.getString("bddate"));
			}

			// 검색된 게시물이 하나도 없을 경우 (삭제된 게시물 등)
			if (i == 0) {
				return Ctrl.FALSE;
			}
			
			// 조회수 늘리기 (이미 게시글을 본 세션에 대해서는 조회수를 늘리지 않음)
			HttpSession session = request.getSession();
			String view = "viewBdNum" + bdNum;
			
			// 게시물을 이미 본 세션이 아니면 조회수 올려줌
			if (session.getAttribute(view) == null) {
				query = "update board_chat set "
						+ "bdviewcount = bdviewcount + 1 where bdnum = "
						+ bdNum;
				stmt.executeUpdate(query);
				conn.commit();
				// 조회수를 올렸으면 세션에 표시
				session.setAttribute(view, true);
			}
			
			// 최종적으로 객체를 담고 리턴
			request.setAttribute("boardDTO", dto);
			return Ctrl.TRUE;
		
		// 업데이트 쿼리 예외 발생 시 롤백 수행 
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch(Exception rollbackEx) {
				rollbackEx.printStackTrace();
				System.out.println("view update rollaback fail");
			}
			System.out.println("board db select fail");
			e.printStackTrace();
		} finally {
			dbClose(); // 모든 자원 해제
		}
		return Ctrl.EXCEPT;
	}

	/* 글쓰기 */
	public int writeBoard(BoardDTO dto) {

		try {
			dbConnect(); // DB 연결

			String query;
			// 새글쓰기일 경우, 그룹넘버는 시퀀스로 처리
			if (dto.getBdGroup() < 0) {
				query = "insert into board_chat "
						+ "(bdnum, bdorder, bdindent, bdtitle, "
						+ "bdcontent, bduserid, bdviewcount,bdgroup) "
						+ "values (BDNUM_SEQ.nextval,?,?,?,?,?,?,BDGROUP_SEQ.nextval)";
				preStmt = conn.prepareStatement(query);

				// 답글쓰기일 경우, 그룹넘버는 dto에서 지정된 숫자로 처리
			} else {
				query = "insert into board_chat "
						+ "(bdnum, bdorder, bdindent, bdtitle, "
						+ "bdcontent, bduserid, bdviewcount,bdgroup) "
						+ "values (BDNUM_SEQ.nextval,?,?,?,?,?,?,?) ";
				preStmt = conn.prepareStatement(query);
				preStmt.setInt(7, dto.getBdGroup()); // 그룹넘버

				// 답글달기의 경우 먼저 같은 그룹의 나머지 글들의 순서를 1씩 증가시킴
				stmt = conn.createStatement();
				query = "update board_chat set bdorder = bdorder + 1 "
						+ "where bdgroup = " + dto.getBdGroup()
						+ "and bdorder >= " + dto.getBdOrder();
				stmt.executeUpdate(query);

			}

			// 그룹넘버 외에는 공통적으로 처리
			preStmt.setInt(1, dto.getBdOrder());
			preStmt.setInt(2, dto.getBdIndent());
			preStmt.setString(3, dto.getBdTitle());
			preStmt.setString(4, dto.getBdContent());
			preStmt.setString(5, dto.getBdUserID());
			preStmt.setInt(6, 0); // 글 작성시 조회수는 0

			// 입력 쿼리 실행
			preStmt.executeUpdate();

			// 커밋
			conn.commit();
			return Ctrl.TRUE;

			// 예외 시 쿼리 롤백
		} catch (

		Exception e) {
			try {
				conn.rollback();
			} catch (Exception rollbackEx) {
				System.out.println("BoardWriter rollbakcEx");
				rollbackEx.printStackTrace();
			}

			System.out.println("boardWriter DB Working Ex");
			e.printStackTrace();

			// 자원 해제
		} finally {
			dbClose();
		}

		return Ctrl.EXCEPT;
	}

	/* 페이징 범위 구하기 */
	private int[] paging(int page) throws Exception {

		dbConnect();
		stmt = conn.createStatement();
		String query = "select count(*) from board_chat";
		result = stmt.executeQuery(query);

		// 전체 게시물 갯수로 총 페이지 수 산출 (하나도 없으면 null 리턴)
		int totalContent = 0;
		int totalPage = 0;
		while (result.next()) {
			totalContent += result.getInt(1);
		}
		if (totalContent == 0) {
			return null;
		}
		totalPage = totalContent / BoardList.pagePerList; // 최종 전체 페이지 갯수
		if (totalContent % BoardList.pagePerList > 0) {
			totalPage++;	// 나머지가 있다면 1을 더해줌
		}

		// 페이징 범위 계산
		int startPage, endPage; // 시작과 끝 페이지
		startPage = ((page - 1) / BoardList.pagingCount) * BoardList.pagingCount
				+ 1;
		endPage = startPage + BoardList.pagingCount - 1;
		if (endPage > totalPage) {
			endPage = totalPage;
		}
		int[] startEnd = new int[2]; // 결과를 전달해줄 배열
		startEnd[0] = startPage;
		startEnd[1] = endPage;
		
		
		// result 객체는 호출한 곳에서 재활용할 것이므로 자원해제
		try {
			result.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return startEnd;
	}

	/* DB 연결 (Spring 전환 시 별도 클래스 분리 필요) */
	private void dbConnect() throws Exception {

		context = new InitialContext();
		datasource = (DataSource) context
				.lookup("java:comp/env/jdbc/Oracle11g");
		conn = datasource.getConnection();
		conn.setAutoCommit(false); // 오토커밋 해제
	}

	/* DB close (모든 객체 자원 해제, Spring 전환 시 별도 클래스 분리 필요) */
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