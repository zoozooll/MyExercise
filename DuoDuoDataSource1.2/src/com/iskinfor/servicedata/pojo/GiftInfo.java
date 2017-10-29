package com.iskinfor.servicedata.pojo;
/**
 * 赠送信息组
 * @author Administrator
 *
 */
public class GiftInfo {
	
	private String[] userArray;//用户数组
	private String userId;//用户数组
	private String giftDate;//时间
	private String reason; //原因
	private String proId;//商品ID
	private String proName;//商品名称
	private String smallImgPath;//商品小图片
	private int totalNum;//总条数
	
	public String[] getUserArray() {
		return userArray;
	}
	public void setUserArray(String[] userArray) {
		this.userArray = userArray;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGiftDate(){
		return giftDate;
	}
	public void setGiftDate(String giftDate){
		this.giftDate=giftDate;
	}
	public String getReason(){
		return reason;
	}
	public void setReason(String reason){
		this.reason=reason;
	}
	public String getProId() {return proId;}
	public void setProId(String proId) {
		this.proId = proId;
		}
	public String getProName() {return proName;}
	public void setProName(String proName) {
		this.proName = proName;
		}
	public String getSmallImgPath() {return smallImgPath;}
	public void setSmallImgPath(String smallImgPath) {
		this.smallImgPath = smallImgPath;
		}
	public int getTotalNum(){return totalNum;}
	public void setTotalNum(int totalNum){
		this.totalNum = totalNum;
		}	
}
