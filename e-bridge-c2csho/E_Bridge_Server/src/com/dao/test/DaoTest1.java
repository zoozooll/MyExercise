package com.dao.test;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import com.dao.IProductDao;
import com.vo.Image;
import com.vo.Product;

public class DaoTest1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		String hql="select p from Product p";
		try {
			IProductDao productDao = (IProductDao) ac.getBean("productDao");
			List<Product> list=productDao.gerPerProdcut(hql, 11,20);
			for(Product p:list){
				Set<Image> imgs=p.getImages();
				for(Image i:imgs){
					System.out.println(i.getImgPath());
				}
			}
		} catch (BeanCreationException be) {
			System.err.println();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println();
		}
		System.out.println("OVER");	
	}
}
