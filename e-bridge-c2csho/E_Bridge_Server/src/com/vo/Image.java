package com.vo;

/**
 * ͼƬ
 */

public class Image implements java.io.Serializable {

	// Fields

	private Integer imgId;
	//private Integer productId;
	private String imgName;
	private String imgPath;
	private String imgMemo;
	
	private Product product;

	// Constructors

	/** default constructor */
	public Image() {
	}



	// Property accessors

	public Integer getImgId() {
		return this.imgId;
	}

	public void setImgId(Integer imgId) {
		this.imgId = imgId;
	}


	public String getImgName() {
		return this.imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getImgPath() {
		return this.imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getImgMemo() {
		return this.imgMemo;
	}

	public void setImgMemo(String imgMemo) {
		this.imgMemo = imgMemo;
	}



	public Product getProduct() {
		return product;
	}



	public void setProduct(Product product) {
		this.product = product;
	}

}