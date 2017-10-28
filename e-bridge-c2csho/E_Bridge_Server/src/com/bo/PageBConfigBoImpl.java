package com.bo;

import com.dao.IPageBconfigDao;
import com.vo.PageBconfig;

public class PageBConfigBoImpl implements IPageBconfigBo{
	private IPageBconfigDao pageBConfigDao;

	public IPageBconfigDao getPageBConfigDao() {
		return pageBConfigDao;
	}

	public void setPageBConfigDao(IPageBconfigDao pageBConfigDao) {
		this.pageBConfigDao = pageBConfigDao;
	}

	public PageBconfig queryBConfigPage() throws Exception {		
		return pageBConfigDao.queryBConfigPage();
	}
}
