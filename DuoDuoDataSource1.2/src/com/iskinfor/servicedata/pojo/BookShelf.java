package com.iskinfor.servicedata.pojo;
/**
 * 书架中的书
 * @author Administrator
 *
 */
public class BookShelf {
	/**
	 * 书架的id
	 */
	private String shiefId="";

	/**
	 * 书的id
	 */
	private String proId="";
	/**
	 * 笔记总数
	 */
	private String totalNote="";
	/**
	 * 书签总数
	 */
	private String totalMark="";
	
	/**
	 * 商品的赠送状态
	 *	Y:赠送 N:无状态
	 */
	private String donoteState="N";
	/**
	 * 商品的推荐状态
	 * Y:推荐 N:无状态
	 */
	private String recommendState="N";
	
	/**
	 * 书架中某本书的数量
	 */
	private int bookNum;
	
	public int getBookNum() {
		return bookNum;
	}
	public void setBookNum(int bookNum) {
		this.bookNum = bookNum;
	}
	/**
	 * 最近的阅读时间
	 */
	private String lastReadData="";
	
	public String getLastReadData() {
		return lastReadData;
	}
	public void setLastReadData(String lastReadData) {
		this.lastReadData = lastReadData;
	}
	public String getDonoteState() {
		return donoteState;
	}
	public void setDonoteState(String donoteState) {
		this.donoteState = donoteState;
	}
	public String getRecommendState() {
		return recommendState;
	}
	public void setRecommendState(String recommendState) {
		this.recommendState = recommendState;
	}
	private int pgs=1;//总条数
	private Product product;//书
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
	}
	public String getTotalNote() {
		return totalNote;
	}
	public void setTotalNote(String totalNote) {
		this.totalNote = totalNote;
	}
	

	public int getPgs() {
		return pgs;
	}
	public void setPgs(int pgs) {
		this.pgs = pgs;
	}
	public String getShiefId() {
		return shiefId;
	}
	public void setShiefId(String shiefId) {
		this.shiefId = shiefId;
	}
	
	public String getTotalMark() {
		return totalMark;
	}
	public void setTotalMark(String totalMark) {
		this.totalMark = totalMark;
	}
}
