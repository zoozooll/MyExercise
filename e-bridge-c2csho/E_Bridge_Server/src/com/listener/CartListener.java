package com.listener;
  
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import com.bo.CartBoImpl; 
import com.bo.ICartBo;
 
//���ﳵ������
public class CartListener implements HttpSessionListener {

	
	public void sessionCreated(HttpSessionEvent session) {
	System.out.println("���ﳵ is called............");
	ICartBo cart=new CartBoImpl(); 
		//������һ��Session�����ʱ��͸��û�һ�����ﳵ����
		 session.getSession().setAttribute("cart", cart);
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		System.out.println("�˳�ʱ���ٹ��ﳵ..............");
		arg0.getSession().removeAttribute("cart");

	}
 
 

}
