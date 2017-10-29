package com.iskinfor.servicedata.pojo;

public class BusinesRecord {
	private String type;//交易类型00：支出 01：充值 02:返现 03子女关联
	private String oprNum;//操作金额
	private String prrDesc;//备注描述
	private String createDate;//操作时间YYYY-MM-DD HH:MI:SS
	private String balance;//余额
	private String userName;//操作人姓名
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOprNum() {
		return oprNum;
	}
	public void setOprNum(String oprNum) {
		this.oprNum = oprNum;
	}
	public String getPrrDesc() {
		return prrDesc;
	}
	public void setPrrDesc(String prrDesc) {
		this.prrDesc = prrDesc;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
