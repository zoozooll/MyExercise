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

	// ��Ʒ�б�
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

	// ��ƷƷ���б�
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

	// ��Ʒ���
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

	// �@ȡ���®aƷ��Ϣ�����⽲��Ʒ���������ĵ������У��Ժ��޸�δ���ղ�Ʒ�ļ���ʱ��ĵ������У�
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

	// ���ռ۸�˳�����в�Ʒ��
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

	// ������Ʒ�����տ�ļ�¼�в鿴���ֲ�Ʒ����;����Ҫ��Ҫ�����޸�;
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

	// �鿴��Ŀ��
	public int getCount(String hql) throws Exception {
		return this.getHibernateTemplate().find(hql).size();
	}

	// �鿴��Ʒ����ϸ��Ϣ
	public Product getProduct(int id) throws Exception {
		return (Product) (this.getHibernateTemplate().get(Product.class, id));
	}

	// ���Ӳ�Ʒ��Ϣ
	public void addProduct(Product p) throws Exception {
		this.getHibernateTemplate().save(p);

	}

	// ɾ���ò�Ʒ
	public void delProduct(Product p) throws Exception {
		this.getHibernateTemplate().delete(p);
	}

	// �޸Ĳ�Ʒ��Ϣ
	public void updateProduct(Product p) throws Exception {
		this.getHibernateTemplate().update(p);

	}

	// ���Ӳ�Ʒ��
	public void addGroup(ProductGroup productGroup) throws Exception {
		this.getHibernateTemplate().save(productGroup);

	}

	// ɾ����Ʒ��
	public void delGroup(ProductGroup productGroup) throws Exception {
		this.getHibernateTemplate().delete(productGroup);
	}

	// ��Ʒ���б�Ϊĳһ����ҵĲ�Ʒ��
	public List<ProductGroup> getPerGroup(int venderId) throws Exception {
		String hql = "select g form ProductGroup g where g.vender.venId=?";
		return this.getHibernateTemplate().find(hql, new Object[] { venderId });

	}

	// ��Ʒ����ϸ��Ϣ
	public ProductGroup getGroupById(int productGroupId) throws Exception {
		return (ProductGroup) (this.getHibernateTemplate().get(
				ProductGroup.class, productGroupId));
	}

	// �ͻ���
	public List<DeliveryBill> findAllDelivery() throws Exception {
		return this.getHibernateTemplate().find("from DeliveryBill");
	}

	// �տ
	public List<ReceiptBill> findAllRecivery() throws Exception {
		return this.getHibernateTemplate().find("from ReceiptBill");
	}

	// ������Ʒ�����أ���Ҫ����
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
