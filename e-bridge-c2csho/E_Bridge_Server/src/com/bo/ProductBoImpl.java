package com.bo;

import java.util.List;

import com.dao.IProductDao;
import com.vo.Brand;
import com.vo.DeliveryBill;
import com.vo.PageBean;
import com.vo.Product;
import com.vo.ProductGroup;
import com.vo.ProductType;
import com.vo.ReceiptBill;
import com.vo.Vender;

public class ProductBoImpl implements IProductBo {
	private IProductDao productDao;

	public IProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(IProductDao productDao) {
		this.productDao = productDao;
	}

	// ��Ӳ�Ʒ��Ϣ
	public void addProduct(Product product) {

		try {
			productDao.addProduct(product);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ɾ��ָ���Ĳ�Ʒ��Ϣ
	public void delProductById(int productid) {
		Product product;
		try {
			product = productDao.getProduct(productid);
			productDao.delProduct(product);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// �鿴���в�Ʒ����
	public List<ProductType> getCategory() {
		String hql = "from ProductType";
		List<ProductType> list = null;
		try {
			list = productDao.getType(hql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	// ��ѯ���в�Ʒ��Ϣ
	/**
	 * ��ҳ��ѯ
	 * 
	 * @param currentPage
	 *            ��ǰ�ڼ�ҳ
	 * @param pageSize
	 *            ÿҳ��С
	 * @return ����˷�ҳ��Ϣ(��(��¼��list)��Bean
	 */

	public PageBean getPerProduct(int pageSize, int page) {
		final String hql = "from Product"; // ��ѯ���
		int allRow;
		PageBean pageBean = null;
		try {
			allRow = productDao.getCount(hql);
			int totalPage = PageBean.countTotalPage(pageSize, allRow); // ��ҳ��
			final int offset = PageBean.countOffset(pageSize, page); // ��ǰҳ��ʼ��¼

			final int length = pageSize; // ÿҳ��¼��
			final int currentPage = PageBean.countCurrentPage(page);
			List<Product> list = productDao.gerPerProdcut(hql, offset, length); // "һҳ"�ļ�¼;
			// �ѷ�ҳ��Ϣ���浽Bean��
			pageBean = new PageBean();
			pageBean.setPageSize(pageSize);
			pageBean.setCurrentPage(currentPage);
			pageBean.setAllRow(allRow);
			pageBean.setTotalPage(totalPage);
			pageBean.setList(list);
			pageBean.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageBean;
	}

	// �����ѯ��Ʒ��Ϣ(�õ���ҳ)
	public PageBean getPerProductByType(int pageSize, int page, int typeId) {

		final String hql = "select p from Product p where p.productType.protypeId="
				+ typeId + "";
		int allRow = 0;
		PageBean pageBean = null;
		try {
			allRow = productDao.getCount(hql);
			int totalPage = PageBean.countTotalPage(pageSize, allRow);
			final int offset = PageBean.countOffset(pageSize, page);
			final int length = pageSize;
			final int currentPage = PageBean.countCurrentPage(page);
			List<Product> list = productDao.gerPerProdcut(hql, offset, length);
			pageBean = new PageBean();
			pageBean.setPageSize(pageSize);
			pageBean.setCurrentPage(currentPage);
			pageBean.setAllRow(allRow);
			pageBean.setTotalPage(totalPage);
			pageBean.setList(list);
			pageBean.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageBean;
	}

	// ���Ʒ�Ʋ��Ҳ�Ʒ��
	public PageBean getProductByBrand(int pageSize, int page, int brandId) {

		final String hql = "select p from Product p where p.brand.brandId="
				+ brandId + ""; // ��ѯ���
		int allRow;
		PageBean pageBean = null;
		try {
			allRow = productDao.getCount(hql);
			int totalPage = PageBean.countTotalPage(pageSize, allRow); // ��ҳ��
			final int offset = PageBean.countOffset(pageSize, page); // ��ǰҳ��ʼ��¼
			final int length = pageSize; // ÿҳ��¼��
			final int currentPage = PageBean.countCurrentPage(page);
			List<Product> list = productDao.gerPerProdcut(hql, offset, length); // "һҳ"�ļ�¼;
			pageBean = new PageBean();
			pageBean.setPageSize(pageSize);
			pageBean.setCurrentPage(currentPage);
			pageBean.setAllRow(allRow);
			pageBean.setTotalPage(totalPage);
			pageBean.setList(list);
			pageBean.init();
		} catch (Exception e) {
			e.printStackTrace();
		} // �ܼ�¼��
		return pageBean;
	}

	// ���id������Ӧ�Ĳ�Ʒ��Ϣ
	public Product getProductById(int productId) {
		Product product = null;
		try {
			product = productDao.getProduct(productId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	// �鿴���û����еĲ�Ʒ��
	public List<Product> getProductByUser(int venId) {

		return null;
	}

	// �޸Ĳ�Ʒ��Ϣ
	public void updateProduct(Product product) {

		try {
			productDao.updateProduct(product);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public PageBean getBrand(int pageSize, int page) {
		final String hql = "from Brand"; // ��ѯ���
		int allRow;
		PageBean pageBean = null;
		try {
			allRow = productDao.getCount(hql);
			int totalPage = PageBean.countTotalPage(pageSize, allRow); // ��ҳ��
			final int offset = PageBean.countOffset(pageSize, page); // ��ǰҳ��ʼ��¼
			final int length = pageSize; // ÿҳ��¼��
			final int currentPage = PageBean.countCurrentPage(page);
			List<Brand> list = productDao.getBrand(hql, offset, length); // "һҳ"�ļ�¼;
			pageBean = new PageBean();
			pageBean.setPageSize(pageSize);
			pageBean.setCurrentPage(currentPage);
			pageBean.setAllRow(allRow);
			pageBean.setTotalPage(totalPage);
			pageBean.setListBrand(list);
			pageBean.init();
		} catch (Exception e) {
			e.printStackTrace();
		} // �ܼ�¼��
		return pageBean;
	}

	public void addGroup(ProductGroup productGroup) {
		try {
			productDao.addGroup(productGroup);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void delGroup(int productGroupId) {
		ProductGroup group;
		try {
			group = productDao.getGroupById(productGroupId);
			productDao.delGroup(group);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<ProductGroup> findGroup(Vender vender) {
		List<ProductGroup> proGroup = null;
		try {
			proGroup = productDao.getPerGroup(vender.getVenId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return proGroup;
	}

	public List<Product> cheapProducts() {
		List<Product> products = null;
		try {
			products = productDao.cheapProducts("from Product order by proPrice asc");// hql
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return products;
	}

	public List<Product> newProducts() {
		List<Product> products = null;
		try {
			products = productDao.newProducts("from Product order by proId desc");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// hql
		return products;
	}

	// ��ѯ���Ų�Ʒ
	public List<Product> hotProducts() {
		List<Product> products = null;
		try {
			products = productDao.hotProducts("from ReceiptBill");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// hql
		return products;
	}

	public PageBean findProductByName(int pageSize, int page, String name) {
		String hql = "select p from Product p where p.name like '%" + name
				+ "%'";
		int allRow;
		PageBean pageBean = null;
		try {
			allRow = productDao.getCount(hql);
			int totalPage = PageBean.countTotalPage(pageSize, allRow); // ��ҳ��
			final int offset = PageBean.countOffset(pageSize, page); // ��ǰҳ��ʼ��¼
			final int length = pageSize; // ÿҳ��¼��
			final int currentPage = PageBean.countCurrentPage(page);
			List<Product> list = productDao.gerPerProdcut(hql, offset, length); // "һҳ"�ļ�¼;
			pageBean = new PageBean();
			pageBean.setPageSize(pageSize);
			pageBean.setCurrentPage(currentPage);
			pageBean.setAllRow(allRow);
			pageBean.setTotalPage(totalPage);
			pageBean.setList(list);
			pageBean.init();
		} catch (Exception e) {
			e.printStackTrace();
		} // �ܼ�¼��

		return pageBean;
	}

	public List<DeliveryBill> findAllDelivery() {
		List<DeliveryBill> deliveryBills = null;
		try {
			deliveryBills = productDao.findAllDelivery();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return deliveryBills;
	}

	public List<ReceiptBill> findAllRecivery() {
		List<ReceiptBill> receiptBills = null;
		try {
			receiptBills = productDao.findAllRecivery();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return receiptBills;
	}

	public String[] findProductName(String name) {
		String[] names = null;
		try {
			names = productDao.findProductName(name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return names;
	}

}
