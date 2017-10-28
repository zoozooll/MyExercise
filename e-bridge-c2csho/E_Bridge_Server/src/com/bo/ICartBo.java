package com.bo;

import com.vo.Product;

public interface ICartBo {

	//加入购物车
	public void addProductToCart(Product p);
	//查看购物车
//	public Collection<Cart> getProductFromCart();
	//删除购物车中的商品
	public void delProductFormCart(Product product);
	//修改产品数量
	public void updateProductSumFromCart(int productid,int sum);
	 //清空购物车
	public void removeCart();
}
