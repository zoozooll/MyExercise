package com.bo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import com.action.BasicAction;
import com.vo.CartItem;
import com.vo.Product;

public class CartBoImpl  implements ICartBo {
	//������Ź���Ĳ�Ʒ
	private Map<Integer,CartItem> cart=new HashMap<Integer,CartItem>();
	  
	//���빺�ﳵ
	public void addProductToCart(Product p) {
		   
		CartItem cartItem=cart.get(p.getProId());
		  if(cartItem==null){
			  cartItem=new CartItem(p,1,p.getProPrice());
			  //��һ�ι���ò�Ʒ
			  cart.put(p.getProId(), cartItem);
			  System.out.println("��һ�ι���.........");
		  }else{
			  System.out.println("��"+cartItem.getProductSum()+"�ι���.........");
			  cartItem.setProductSum(cartItem.getProductSum()+1);
		  } 
		  System.out.println("����Ĳ�Ʒ������==>"+cartItem.getProductSum()); 
	}

	//ɾ�����ﳵ�в�Ʒ
	public void delProductFormCart(Product product) { 
		 System.out.println("cartBo ɾ����Ʒ====>");
		 cart.remove(product.getProId());
	}
	
    //�޸Ĺ��ﳵ�в�Ʒ����
	public void updateProductSumFromCart(int  pid, int sum) { 
		 CartItem c=cart.get(pid); 
		  c.setProductSum(sum);  
	}
	
	//��չ��ﳵ
	public void removeCart() { 
		cart.clear(); 
	}
	
	public Map<Integer, CartItem> getCart() {
		return cart;
	}

	public void setCart(Map<Integer, CartItem> cart) {
		this.cart = cart;
	}
	
   //�õ����ﳵ�����еĲ�Ʒ����
	public Collection<CartItem> getMapValue(){
		return cart.values();
	}
	//�õ����ﳵ�еĲ�Ʒ������
	public int getProductAllSumOnCart(){
		int allSum=0;
		Collection<CartItem> c=this.getMapValue();
		for(CartItem item:c){
			allSum+=item.getProductSum();
		}
		return allSum;
	}
	
	//�õ����ﳵ�����в�Ʒ���ܼ۸�
	public double getAllMoneyOnCart(){
		double allMoney=0;
		Collection<CartItem> c=getMapValue();
		for(CartItem item: c){
			allMoney+=item.getSumMoney();
		}
		return allMoney;
	}
}
