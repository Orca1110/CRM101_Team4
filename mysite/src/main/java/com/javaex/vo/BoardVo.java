package com.javaex.vo;

public class BoardVo {

	private int no;
	private String title;
	private String content;
	private int hit;
	private String regDate;
	private int userNo;
	private String userName;
	private String fileName;
	private int fileSize;

	public int getFileSize() {
		return fileSize;
	}

	public BoardVo(int no, String title, String content, int hit, String regDate, int userNo, String userName,
			String fileName, int fileSize) {
		super();
		this.no = no;
		this.title = title;
		this.content = content;
		this.hit = hit;
		this.regDate = regDate;
		this.userNo = userNo;
		this.userName = userName;
		this.fileName = fileName;
		this.fileSize = fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public BoardVo() {
	}
	
	public BoardVo(int no, String title, String content) {
		this.no = no;		
		this.title = title;
		this.content = content;
	}

	public BoardVo(String title, String content, int userNo) {
		this.title = title;
		this.content = content;
		this.userNo = userNo;
	}
	
	
	
	public BoardVo(String fileName) {
		super();
		this.fileName = fileName;
	}

	public BoardVo(int no, String title, int hit, String regDate, int userNo, String userName) {
		this.no = no;
		this.title = title;
		this.hit = hit;
		this.regDate = regDate;
		this.userNo = userNo;
		this.userName = userName;
	}
	
	public BoardVo(int no, String title, String content, int hit, String regDate, int userNo, String userName) {
		this(no, title, hit, regDate, userNo, userName);
		this.content = content;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public int getUserNo() {
		return userNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "BoardVo [no=" + no + ", title=" + title + ", content=" + content + ", hit=" + hit + ", regDate="
				+ regDate + ", userNo=" + userNo + ", userName=" + userName + ", fileName=" + fileName + ", fileSize="
				+ fileSize + "]";
	}

	public void setFileSize(long size) {
		// TODO Auto-generated method stub
		
	}

	

}
