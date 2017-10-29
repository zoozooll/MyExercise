package com.iskinfor.servicedata.pojo;

import java.io.Serializable;

/**
 * 商品信息
 * @author WuNan2
 * @date Apr 14, 2011
 */
@SuppressWarnings("serial")
public class Product implements Serializable {
	
	private String proId;	// 商品ID
	private String userId;
	private String proName; // 商品名称
	private String bigImgPath;	// 商品大图路径
	private String smallImgPath; // 商品小图路径
	private String proPrice;	// 价格
	private int proNum;//一件商品在某个订单中的数量
	
	private String proVolume;
	
	private String proRating;	// 商品评分
	private String tranNum;	// 销售量
	private String ather;	// 作者
	private String publisher;	// 出版社
	private String addDate;
	private String intimeDate;	// 出版日期
	private String quantity;	// 购买数量
	private float subtotal ;//商品小计（商品的购买数量*商品的价格）
	private String proStatus;//商品状态
	private String proAccoutn;//购买金额
	

	public String getProAccoutn() {
		return proAccoutn;
	}
	public void setProAccoutn(String proAccoutn) {
		this.proAccoutn = proAccoutn;
	}
	private String editorChoice;	// 编辑推荐
	private String introContent;	// 内容简介
	private int ratingNum;	// 评价人数
	private String collectionNum;	// 收藏人数
	
	private String proViewPath;	// 预览路径
	private String proRealPath;	// 真实路径
	
	private String sourseId;	// 来源
	private String jobsTitle;	// 职称
	
	private String offerNum; //优惠金额
	private int integral;//积分
	public float getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(float subtotal) {
		this.subtotal = subtotal;
	}
	public int getIntegral() {
		return integral;
	}
	public void setIntegral(int integral) {
		this.integral = integral;
	}
	public String getOfferNum() {
		return offerNum;
	}
	public void setOfferNum(String offerNum) {
		this.offerNum = offerNum;
	}
	public String getProRating() {
		return proRating;
	}
	public void setProRating(String proRating) {
		this.proRating = proRating;
	}
	public String getSourseId() {
		return sourseId;
	}
	public void setSourseId(String sourseId) {
		this.sourseId = sourseId;
	}
	public String getJobsTitle() {
		return jobsTitle;
	}
	public void setJobsTitle(String jobsTitle) {
		this.jobsTitle = jobsTitle;
	}
	public String getProViewPath() {
		return proViewPath;
	}
	public void setProViewPath(String proViewPath) {
		this.proViewPath = proViewPath;
	}
	public String getProRealPath() {
		return proRealPath;
	}
	public void setProRealPath(String proRealPath) {
		this.proRealPath = proRealPath;
	}
	public String getTranNum() {
		return tranNum;
	}
	public void setTranNum(String tranNum) {
		this.tranNum = tranNum;
	}
	public String getIntimeDate() {
		return intimeDate;
	}
	public void setIntimeDate(String intimeDate) {
		this.intimeDate = intimeDate;
	}
	public String getAther() {
		return ather;
	}
	public void setAther(String ather) {
		this.ather = ather;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getEditorChoice() {
		return editorChoice;
	}
	public void setEditorChoice(String editorChoice) {
		this.editorChoice = editorChoice;
	}
	public String getIntroContent() {
		return introContent;
	}
	public void setIntroContent(String introContent) {
		this.introContent = introContent;
	}
	public int getRatingNum() {
		return ratingNum;
	}
	public void setRatingNum(int ratingNum) {
		this.ratingNum = ratingNum;
	}
	public String getCollectionNum() {
		return collectionNum;
	}
	public void setCollectionNum(String collectionNum) {
		this.collectionNum = collectionNum;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getBigImgPath() {
		return bigImgPath;
	}
	public void setBigImgPath(String bigImgPath) {
		this.bigImgPath = bigImgPath;
	}
	public String getSmallImgPath() {
		return smallImgPath;
	}
	public void setSmallImgPath(String smallImgPath) {
		this.smallImgPath = smallImgPath;
	}
	public String getProPrice() {
		return proPrice;
	}
	public void setProPrice(String proPrice) {
		this.proPrice = proPrice;
	}
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
	}
	public String getProVolume() {
		return proVolume;
	}
	public void setProVolume(String proVolume) {
		this.proVolume = proVolume;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAddDate() {
		return addDate;
	}
	public void setAddDate(String addDate) {
		this.addDate = addDate;
	}
	public int getProNum() {
		return proNum;
	}
	public void setProNum(int proNum) {
		this.proNum = proNum;
	}
	
	public String getProStatus() {
		return proStatus;
	}
	public void setProStatus(String proStatus) {
		this.proStatus = proStatus;
	}
}
