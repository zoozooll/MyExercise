package com.iskinfor.servicedata.pojo;
/**
 * 看过的书
 * @author Administrator
 *
 */
public class BookReaded {
	/**
	 * 笔记总数
	 */
	private String totalNote;
	/**
	 * 书
	 */
	private Product product;
	/**
	 * 书的ID
	 */
	private String proId;
	/**
	 * 用户的ID
	 */
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getTotalNote() {
		return totalNote;
	}

	public void setTotalNote(String totalNote) {
		this.totalNote = totalNote;
	}
}
