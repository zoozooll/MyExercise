package com.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionFactory;
	//加载工具类的时候创建SessionFactory对象
	static{
		if(sessionFactory==null){
			Configuration config = new Configuration().configure();
			sessionFactory = config.buildSessionFactory();
		}
	}
	private HibernateUtil(){
		
	}
	//获取SessionFactory对象
	public static SessionFactory getSessionFactory(){
		return sessionFactory;
	}
	//获取Session对象
	public static Session getSession(){
		Session session = null;
		if(sessionFactory!=null){
			session = sessionFactory.openSession();
		}
		return session;
	}
	//关闭Session对象
	public static void close(Session session){
		if(session!=null){
			try{
				session.close();
			}catch(Exception ex){
				System.out.println("关闭session对象出现错误");
				ex.printStackTrace();
			}
		}
	}
	//保存一个对象
	public static void save(Object obj){
		Session session = getSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.save(obj);
			tx.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			HibernateUtil.close(session);
		}
	}
}
