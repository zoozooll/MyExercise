package com.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vo.Brand;

public class SearchDaoImpl extends HibernateDaoSupport implements ISearchDao {
	
	private String ClassName;
	public String getClassName() {
		return ClassName;
	}

	public void setClassName(String className) {
		ClassName = className;
	}

	/**
	 *查询所有相关信息
	 * @param entityName pojo类名//传入的名
	 * @param start 当前页
	 * @param maxResults 一页最在显示数
	 * @param condition 查询条件
	 * @return
	 */
	public List<Object> list(String entityName, Integer start,
			Integer maxResults, String condition) throws Exception {
		String tabelName=null;
		Session session = this.getSession();
		Transaction tx = null;
		List<Object> list = null;	
		try{
		if("Purchaser".equals(entityName)){
			tabelName="Purchaser";		
			
		}else if("Product".equals(entityName)){
			tabelName="Product";	
		}
		
			setClassName(entityName);
			
			tabelName=tabelName.substring(0, 3);
			tabelName=tabelName.toLowerCase();
			System.out.println("-------------"+tabelName);
			
			int firstResult = (start - 1) * maxResults;
			String hql="select o from "+entityName+" o where o."+tabelName+"Name  like '%"+condition+"%'";
			Query q = session.createQuery(hql);
			if("Product".equals(entityName))
			{
				q.setFirstResult(firstResult);
				q.setMaxResults(maxResults);
			}	
			list=q.list();
		}finally
		{
			if(session!=null)
			{
				session.close();
			}
		}	
		
		return list;
	}
	
	/**
	 * 查询相关信息的所有记录数
	 */
	public Integer countList(String entityName) throws Exception {
		String hql="from "+entityName;
		int count =this.getHibernateTemplate().find(hql).size();		
System.out.println("---------------count="+count);		
		return count;
	}
	
	

}
