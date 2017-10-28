package com.listener;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.action.BasicAction;
import com.dao.IBeforeOrderDao;
import com.vo.OrderStatus;
import com.vo.Payway;

public class  BeforeOrderListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) { 
		arg0.getServletContext().removeAttribute("payway");
		
	}
	
     //付款方式和订单状态监听器
	public void contextInitialized(ServletContextEvent context) {
		 
		BeanFactory factory=new ClassPathXmlApplicationContext("applicationContext.xml");
		 
		IBeforeOrderDao dao=(IBeforeOrderDao)factory.getBean("beforeOrderDao");
		List<Payway> payway=dao.findPayWay();
		 
		 
		if(payway!=null && payway.size()>0){
			System.out.println("付款方式监听器得到的付款方式..........."+payway);
			context.getServletContext().setAttribute("payway", payway);
		}
	 
		
	}
 
}
