package com.vo;

public class CartItem implements java.io.Serializable{

	private int id; 
	private Product product;   //��Ʒ
	private int productSum;   //��Ʒ����
	private double baseprice;   //��Ʒ����
	private double sumMoney;  //�ܼ۸�


	public double getSumMoney() {
		return  productSum*baseprice;
	}
	public void setSumMoney(double sumMoney) {
		this.sumMoney = sumMoney;
	}
	public CartItem() {
		 
	}
	public CartItem( Product product, int productSum,double baseprice) {
		 
		this.baseprice = baseprice;
		this.product = product;
		this.productSum = productSum;
	} 
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getProductSum() {
		return productSum;
	}
	public void setProductSum(int productSum) {
		this.productSum = productSum;
	}
	public double getBaseprice() {
		return baseprice;
	}
	public void setBaseprice(double baseprice) {
		this.baseprice = baseprice;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
