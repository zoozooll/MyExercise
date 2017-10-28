package com.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vo.Brand;
import com.vo.DeliveryBill;
import com.vo.Product;
import com.vo.ProductGroup;
import com.vo.ProductType;
import com.vo.Purchaser;
import com.vo.ReceiptBill;

public class ProductDaoImpl extends HibernateDaoSupport implements IProductDao {

	// 产品列表
	public List<Product> gerPerProdcut(String hql, int start, int size)
			throws Exception {
		Session session = this.getSession();
		Transaction tx = null;
		List<Product> list = null;
		tx = session.beginTransaction();
		Query query = session.createQuery(hql);
		query.setCacheable(true);
		query.setFirstResult(start);
		query.setMaxResults(size);
		list = query.list();
		tx.commit();
		session.close();
		return list;
	}

	// 产品品牌列表
	public List<Brand> getBrand(String hql, int start, int size)
			throws Exception {
		Session session = this.getSession();
		Transaction tx = null;
		List<Brand> list = null;
		tx = session.beginTransaction();
		Query query = session.createQuery(hql);
		query.setCacheable(true);
		query.setFirstResult(start);
		query.setMaxResults(size);
		list = query.list();
		tx.commit();
		if (session != null) {
			session.close();
		}
		return list;
	}

	// 产品类别
	public List<ProductType> getType(String hql) throws Exception {
		Session session = this.getSession();
		Transaction tx = null;
		List<ProductType> list = null;
		tx = session.beginTransaction();
		Query query = session.createQuery(hql);
		query.setCacheable(true);
		list = query.list();
		tx.commit();
		if (session != null) {
			session.close();
		}
		return list;
	}

	// 獲取最新產品信息；本意讲产品根据主键的倒序排列，以后修改未按照产品的加入时间的倒序排列；
	public List<Product> newProducts(String hql) throws Exception {
		// String sql="from Product order by proId desc";
		Query query = this.getSession().createQuery(hql);
		List<Product> list = new ArrayList<Product>();
		for (int i = 0; i < query.list().size(); i++) {
			list.add((Product) query.list().get(i));
			if (i >= 3) {
				return list;
			}
		}
		return list;
	}

	// 按照价格顺序排列产品；
	public List<Product> cheapProducts(String hql) throws Exception {
		// String sql="from Product order by proPrice asc";
		Query query = this.getSession().createQuery(hql);
		List<Product> list = new ArrayList<Product>();
		for (int i = 0; i < query.list().size(); i++) {
			list.add((Product) query.list().get(i));
			if (i >= 3) {
				return list;
			}
		}
		return list;
	}

	// 热卖产品。从收款单的记录中查看那种产品热卖;此需要需要经过修改;
	public List<Product> hotProducts(String hql) throws Exception {
		// String hql="from ReceiptBill";
		Query query = this.getSession().createQuery(hql);
		List<ReceiptBill> receiptBill = query.list();
		List<Product> list = new ArrayList<Product>();
		for (ReceiptBill receiptBill2 : receiptBill) {
			Set<Product> list2 = receiptBill2.getOrderLine().getProducts();
			for (Product product : list2) {
				list.add(product);
			}
		}
		List<Product> list3 = new ArrayList<Product>();
		for (int i = 0; i < list.size(); i++) {
			list3.add((Product) list.get(i));
			if (i >= 3) {
				return list3;
			}
		}
		return list3;
	}

	// 查看数目：
	public int getCount(String hql) throws Exception {
		return this.getHibernateTemplate().find(hql).size();
	}

	// 查看产品的详细信息
	public Product getProduct(int id) throws Exception {
		return (Product) (this.getHibernateTemplate().get(Product.class, id));
	}

	// 增加产品信息
	public void addProduct(Product p) throws Exception {
		this.getHibernateTemplate().save(p);

	}

	// 删除该产品
	public void delProduct(Product p) throws Exception {
		this.getHibernateTemplate().delete(p);
	}

	// 修改产品信息
	public void updateProduct(Product p) throws Exception {
		this.getHibernateTemplate().update(p);

	}

	// 增加产品组
	public void addGroup(ProductGroup productGroup) throws Exception {
		this.getHibernateTemplate().save(productGroup);

	}

	// 删除产品组
	public void delGroup(ProductGroup productGroup) throws Exception {
		this.getHibernateTemplate().delete(productGroup);
	}

	// 产品组列表，为某一个玩家的产品组
	public List<ProductGroup> getPerGroup(int venderId) throws Exception {
		String hql = "select g form ProductGroup g where g.vender.venId=?";
		return this.getHibernateTemplate().find(hql, new Object[] { venderId });

	}

	// 产品组详细信息
	public ProductGroup getGroupById(int productGroupId) throws Exception {
		return (ProductGroup) (this.getHibernateTemplate().get(
				ProductGroup.class, productGroupId));
	}

	// 送货单
	public List<DeliveryBill> findAllDelivery() throws Exception {
		return this.getHibernateTemplate().find("from DeliveryBill");
	}

	// 收款单
	public List<ReceiptBill> findAllRecivery() throws Exception {
		return this.getHibernateTemplate().find("from ReceiptBill");
	}

	// 搜索产品功能呢，需要完善
	public String[] findProductName(String name) throws Exception {
		String[] names = null;
		if (!name.equals("") && name != null) {
			String hql = "from Product p where p.proName like '%" + name + "%'";
			List<Product> products = this.getHibernateTemplate().find(hql);
			if (products != null && products.size() > 0) {
				names = new String[products.size()];
				for (int i = 0; i < names.length; i++) {
					names[i] = products.get(i).getProName();
					if (i > 6) {
						return names;
					}
				}
			}
		}
		return names;
	}

}
