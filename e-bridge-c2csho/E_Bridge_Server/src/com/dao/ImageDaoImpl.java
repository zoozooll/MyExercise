package com.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vo.Image;

public class ImageDaoImpl extends HibernateDaoSupport implements IImageDao {

	public void saveImage(Image image) {
		this.getHibernateTemplate().save(image);

	}

}
