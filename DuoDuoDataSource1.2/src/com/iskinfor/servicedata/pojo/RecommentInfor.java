package com.iskinfor.servicedata.pojo;
/**
 * 推荐信息组
 * @author Administrator
 */
public class RecommentInfor {

	private String userId;//用户ID	
	private String username;//用户名
	private String proId;//书ID
	private String proName;//书名
	private String proSmallPic;//书大图
	private String proBigPic;//书小图
	private String recommendDate;//时间	
	private String reason;//原因
	private int pags;//总条数	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return username;
	}
	public void setUserName(String username) {
		this.username = username;
	}
	
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
	}
	
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	
	public String getProSmallPic() {
		return proSmallPic;
	}
	public void setProSmallPic(String proSmallPic) {
		this.proSmallPic = proSmallPic;
	}
	
	public String getProBigPic() {
		return proBigPic;
	}
	public void setProBigPic(String proSmallPic) {
		this.proBigPic = proBigPic;
	}
	
	public String getRecommendDate() {
		return recommendDate;
	}
	public void setRecommendDate(String recommendDate) {
		this.recommendDate = recommendDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getPags() {
		return pags;
	}
	public void setPags(int pags) {
		this.pags = pags;
	}
	
}
