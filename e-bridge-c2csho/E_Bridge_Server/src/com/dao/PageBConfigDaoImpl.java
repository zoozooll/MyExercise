package com.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vo.PageBconfig;

public class PageBConfigDaoImpl extends HibernateDaoSupport implements IPageBconfigDao{	
	public PageBconfig queryBConfigPage() throws Exception{		
		return (PageBconfig)this.getHibernateTemplate().get(PageBconfig.class, 1);		
	}
}
