package com.iskinfor.servicedata.pojo;
/**
 * 获奖者
 * @author Administrator
 *
 */
public class Winner {
	private String userName;//获奖者姓名
	private String awardDate;//获奖日期
	private String awardCourse;//获奖课程
	private String winnerInfor;//获奖情况
	private int pags;//总条数
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAwardDate() {
		return awardDate;
	}
	public void setAwardDate(String awardDate) {
		this.awardDate = awardDate;
	}
	public String getAwardCourse() {
		return awardCourse;
	}
	public void setAwardCourse(String awardCourse) {
		this.awardCourse = awardCourse;
	}
	public String getWinnerInfor() {
		return winnerInfor;
	}
	public void setWinnerInfor(String winnerInfor) {
		this.winnerInfor = winnerInfor;
	}
	public int getPags() {
		return pags;
	}
	public void setPags(int pags) {
		this.pags = pags;
	}
}
