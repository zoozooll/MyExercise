package com.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.bo.IUserBo;
import com.vo.Admin;
import com.vo.ProductGroup;
import com.vo.Purchaser;
import com.vo.Vender;

public class AdminDaoImpl extends HibernateDaoSupport implements IAdminDao{
	private IUserBo userBo;

	public IUserBo getUserBo() {
		return userBo;
	}

	public void setUserBo(IUserBo userBo) {
		this.userBo = userBo;
	}

	//修改成在DAO层直接调用方法增加，在bo层调用方法来组成用户的对象。
	public void addUser(Object... objects) throws Exception {
		Purchaser purchaser=(Purchaser)objects[0];
		ProductGroup productgroup=(ProductGroup)objects[1];
		Vender vender=(Vender)objects[2];
		getHibernateTemplate().save(purchaser);		
		if((purchaser.getPurIsvendot().equals("yes"))){
			getHibernateTemplate().save(productgroup);
			getHibernateTemplate().save(vender);
		}
	}

	public void deleteUser(Integer purId) throws Exception {
		String hql="delete from Purchaser p where p.purId="+purId+"";				
		Query query=getSession().createQuery(hql);
		query.executeUpdate();
	}

	public void modifyUser(Purchaser purchaser) throws Exception {
		getHibernateTemplate().update(purchaser);		
		
	}

	public List<Purchaser> queryUser() throws Exception {		
		String hql="from Purchaser";
		return this.getHibernateTemplate().find(hql);
	}

	public Admin adminLogin(String... strings) throws Exception {
		String hql="from Admin a where a.adminName=? and a.adminPassword=?";
		Query query=getSession().createQuery(hql);
		query.setString(0, strings[0]);
		query.setString(1, strings[1]);
		List<Admin> list=query.list();
		Admin a=null;
		if(list.size()>0){
			 a=list.get(0);
		}
		return a;
	}

	public Purchaser findUserById(Integer purId) throws Exception {			
			return (Purchaser)this.getHibernateTemplate().get(Purchaser.class, purId);
		
	}

	public void modifyPassword(String password,Integer adminId) throws Exception {
		final String hql="update Admin a set a.adminPassword='"+password+"' where a.adminId="+adminId+"";
		this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {				
				session.createQuery(hql).executeUpdate();
				return null;
			}
			
		});
	}

	public void modifyStatus(Integer status,Integer venId) throws Exception {
		final String hql="update Vender v set v.venStatus='"+status+"' where v.venId="+venId+"";
		this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {				
				int i=session.createQuery(hql).executeUpdate();
				return i;
			}
			
		});
	}

	public Admin findAdminById(Integer adminId) throws Exception {
		return (Admin)this.getHibernateTemplate().get(Admin.class, adminId);
	}

	public void deleteUserByName(String delcondition) throws Exception {
		String hql="delete from Purchaser p where p.purName="+delcondition+"";				
		Query query=getSession().createQuery(hql);
		query.executeUpdate();
	}

	public List<Purchaser> findUserByName(String condition) throws Exception {
		String hql="from Purchaser p where p.purName='"+condition+"'";		
		return this.getHibernateTemplate().find(hql);
	}

	public List<Purchaser> findUserByNameBlur(String condition) throws Exception {		
		String hql="from Purchaser p where p.purName like '%"+condition+"%'";		
		return this.getHibernateTemplate().find(hql);
	}

	public Admin modifyAdmin(Admin admin) throws Exception {
		this.getHibernateTemplate().update(admin);
		return (Admin)this.getHibernateTemplate().get(Admin.class, admin.getAdminId());
	}

}
