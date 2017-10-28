package com.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionFactory;
	//���ع������ʱ�򴴽�SessionFactory����
	static{
		if(sessionFactory==null){
			Configuration config = new Configuration().configure();
			sessionFactory = config.buildSessionFactory();
		}
	}
	private HibernateUtil(){
		
	}
	//��ȡSessionFactory����
	public static SessionFactory getSessionFactory(){
		return sessionFactory;
	}
	//��ȡSession����
	public static Session getSession(){
		Session session = null;
		if(sessionFactory!=null){
			session = sessionFactory.openSession();
		}
		return session;
	}
	//�ر�Session����
	public static void close(Session session){
		if(session!=null){
			try{
				session.close();
			}catch(Exception ex){
				System.out.println("�ر�session������ִ���");
				ex.printStackTrace();
			}
		}
	}
	//����һ������
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
