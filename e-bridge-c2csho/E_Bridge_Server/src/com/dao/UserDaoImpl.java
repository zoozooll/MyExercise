package com.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vo.ProductGroup;
import com.vo.Purchaser;
import com.vo.Vender;

public class UserDaoImpl extends HibernateDaoSupport implements IUserDao {

	public Purchaser login(String name, String password) throws Exception {
		String hql = "from Purchaser p where p.purName=? and p.purPassword=?";
		Query query = getSession().createQuery(hql);
		query.setString(0, name);
		query.setString(1, password);
		List<Purchaser> list = query.list();
		Purchaser p = null;
		if (list.size() > 0) {
			p = list.get(0);
			System.out.println("----------------登录成功!");
		}
		return p;
	}

	/***
	 * 加事务切片。。。
	 */
	public void register(Object... objects) throws Exception {
		Purchaser purchaser = (Purchaser) objects[0];
		ProductGroup productgroup = (ProductGroup) objects[1];
		Vender vender = (Vender) objects[2];
			purchaser.setVender(vender);
			purchaser.getVender().setProductgroup(productgroup);		
		getHibernateTemplate().save(purchaser);
		System.out.println("----------------注册成功!");
	}

	public void update(Object... objects) throws Exception {
		Purchaser purchaser = (Purchaser) objects[0];
		
		getHibernateTemplate().update(purchaser);

		System.out.println("----------------更新成功!");
	}

	public String checkUserName(String name) throws Exception {
		String nameMes = null;
		if (!name.equals("") && name != null) {
			String hql = "from Purchaser p where p.purName='" + name + "'";
			List<Purchaser> user = this.getHibernateTemplate().find(hql);

			if (user != null && user.size() > 0) {
				nameMes = "用户名已注册";
				System.out.println("----------------用户名已注册");
			} else {
				nameMes = "该用户名可以使用";
				System.out.println("----------------该用户名可以使用");
			}
		} else {
			nameMes = "";
		}
		return nameMes;
	}
	
	public String[] findUserName(String name) throws Exception {
		String[] names=null;
		if (!name.equals("") && name != null) {
			String hql = "from Purchaser p where p.purName like '%" + name + "%'";
			List<Purchaser> user = this.getHibernateTemplate().find(hql);			
			if (user != null && user.size() > 0) {
				names=new String[user.size()];
				for (int i = 0; i < names.length; i++) {
					names[i]=user.get(i).getPurName();
					if(i>6){
						return names;
					}
				}
			}
		}
		return names;
	}
	

	@SuppressWarnings("unchecked")
	public Purchaser findByName(String name) throws Exception {
		Purchaser purchaser = null;
		List<Purchaser> list = null;
		String hql = "from Purchaser p where p.purName='" + name + "'";
		System.out.println("------------" + hql);
		list = getHibernateTemplate().find(hql);
		if (list.size() > 0) {
			purchaser = list.get(0);
			System.out.println("----------------查找成功!");
		}
		return purchaser;
	}

	@SuppressWarnings("unchecked")
	public List<Purchaser> getAllPurchasers() throws Exception {
		String hql = "from Purchaser p";
		List<Purchaser> list = getHibernateTemplate().find(hql);
		System.out.println("----------------所有2查找成功!");
		return list;
	}

	public void modifyPassword(String password, Integer purId) {
		Purchaser purchaser = (Purchaser) this.getHibernateTemplate().get(
				Purchaser.class, purId);
		System.out.println("*********************" + purchaser.getPurName());
		purchaser.setPurPassword(password);
		this.getHibernateTemplate().update(purchaser);
		System.out.println("----------------修改密码成功!");
	}

	public Purchaser purchasertoVender(Object... objects) throws Exception {
		Purchaser purchaser = (Purchaser) objects[0];
		getHibernateTemplate().update(purchaser);
		System.out.println("----------------更新卖家成功!");
		return (Purchaser)this.getHibernateTemplate().get(Purchaser.class, purchaser.getPurId());
	}

}
