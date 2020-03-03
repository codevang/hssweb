package db;

public class BoardDTO {

	private int bdNum; // 게시물 고유번호
	private int bdGroup; // 게시물 그룹
	private int bdOrder; // 그룹 내 순서
	private int bdIndent; // 답글 들여쓰기
	private String bdTitle; // 제목
	private String bdContent; // 내용
	private String bdUserID; // 작성자
	private int bdViewCount; // 조회수
	private String bdDate; // 작성일자
	
	public int getBdNum() {
		return bdNum;
	}
	public void setBdNum(int bdNum) {
		this.bdNum = bdNum;
	}
	public int getBdGroup() {
		return bdGroup;
	}
	public void setBdGroup(int bdGroup) {
		this.bdGroup = bdGroup;
	}
	public int getBdOrder() {
		return bdOrder;
	}
	public void setBdOrder(int bdOrder) {
		this.bdOrder = bdOrder;
	}
	public int getBdIndent() {
		return bdIndent;
	}
	public void setBdIndent(int bdIndent) {
		this.bdIndent = bdIndent;
	}
	public String getBdTitle() {
		return bdTitle;
	}
	public void setBdTitle(String bdTitle) {
		this.bdTitle = bdTitle;
	}
	public String getBdContent() {
		return bdContent;
	}
	public void setBdContent(String bdContent) {
		this.bdContent = bdContent;
	}
	public String getBdUserID() {
		return bdUserID;
	}
	public void setBdUserID(String bdUserID) {
		this.bdUserID = bdUserID;
	}
	public int getBdViewCount() {
		return bdViewCount;
	}
	public void setBdViewCount(int bdViewCount) {
		this.bdViewCount = bdViewCount;
	}
	public String getBdDate() {
		return bdDate;
	}
	public void setBdDate(String bdDate) {
		this.bdDate = bdDate;
	}
}
