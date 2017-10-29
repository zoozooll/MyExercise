package com.iskinfor.servicedata.pojo;

import java.util.ArrayList;

/**
 * 购物车
 * @author Administrator
 *
 */
public class ShopCar {
	private float totalSum;//订单的总价格
	private ArrayList<Product> productList; //订单中的商品

	public float getTotalSum() {
		return totalSum;
	}
	public void setTotalSum(float totalSum) {
		this.totalSum = totalSum;
	}
	public ArrayList<Product> getProductList() {
		return productList;
	}
	public void setProductList(ArrayList<Product> productList) {
		this.productList = productList;
	}

}
