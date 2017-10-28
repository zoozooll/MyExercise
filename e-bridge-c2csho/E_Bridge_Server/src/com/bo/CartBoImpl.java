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
	//用来存放购买的产品
	private Map<Integer,CartItem> cart=new HashMap<Integer,CartItem>();
	  
	//加入购物车
	public void addProductToCart(Product p) {
		   
		CartItem cartItem=cart.get(p.getProId());
		  if(cartItem==null){
			  cartItem=new CartItem(p,1,p.getProPrice());
			  //第一次购买该产品
			  cart.put(p.getProId(), cartItem);
			  System.out.println("第一次购买.........");
		  }else{
			  System.out.println("第"+cartItem.getProductSum()+"次购买.........");
			  cartItem.setProductSum(cartItem.getProductSum()+1);
		  } 
		  System.out.println("购买的产品总数是==>"+cartItem.getProductSum()); 
	}

	//删除购物车中产品
	public void delProductFormCart(Product product) { 
		 System.out.println("cartBo 删除产品====>");
		 cart.remove(product.getProId());
	}
	
    //修改购物车中产品数量
	public void updateProductSumFromCart(int  pid, int sum) { 
		 CartItem c=cart.get(pid); 
		  c.setProductSum(sum);  
	}
	
	//清空购物车
	public void removeCart() { 
		cart.clear(); 
	}
	
	public Map<Integer, CartItem> getCart() {
		return cart;
	}

	public void setCart(Map<Integer, CartItem> cart) {
		this.cart = cart;
	}
	
   //得到购物车中所有的产品对象
	public Collection<CartItem> getMapValue(){
		return cart.values();
	}
	//得到购物车中的产品总数量
	public int getProductAllSumOnCart(){
		int allSum=0;
		Collection<CartItem> c=this.getMapValue();
		for(CartItem item:c){
			allSum+=item.getProductSum();
		}
		return allSum;
	}
	
	//得到购物车中所有产品的总价格
	public double getAllMoneyOnCart(){
		double allMoney=0;
		Collection<CartItem> c=getMapValue();
		for(CartItem item: c){
			allMoney+=item.getSumMoney();
		}
		return allMoney;
	}
}
