package com.listener;
  
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import com.bo.CartBoImpl; 
import com.bo.ICartBo;
 
//购物车监听器
public class CartListener implements HttpSessionListener {

	
	public void sessionCreated(HttpSessionEvent session) {
	System.out.println("购物车 is called............");
	ICartBo cart=new CartBoImpl(); 
		//当创建一个Session对象的时候就给用户一个购物车对象
		 session.getSession().setAttribute("cart", cart);
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		System.out.println("退出时销毁购物车..............");
		arg0.getSession().removeAttribute("cart");

	}
 
 

}
