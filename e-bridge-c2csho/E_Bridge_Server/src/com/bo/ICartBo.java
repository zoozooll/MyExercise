package com.bo;

import com.vo.Product;

public interface ICartBo {

	//���빺�ﳵ
	public void addProductToCart(Product p);
	//�鿴���ﳵ
//	public Collection<Cart> getProductFromCart();
	//ɾ�����ﳵ�е���Ʒ
	public void delProductFormCart(Product product);
	//�޸Ĳ�Ʒ����
	public void updateProductSumFromCart(int productid,int sum);
	 //��չ��ﳵ
	public void removeCart();
}
