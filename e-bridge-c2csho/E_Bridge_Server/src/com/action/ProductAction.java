package com.action;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.bo.IProductBo;
import com.vo.Brand;
import com.vo.DeliveryBill;
import com.vo.PageBean;
import com.vo.Product;
import com.vo.ProductGroup;
import com.vo.ProductType;
import com.vo.Purchaser;
import com.vo.ReceiptBill;

public class ProductAction extends BasicAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4879256382579414895L;

	private IProductBo productBo;

	private PageBean pageBean;

	private Integer page = 1;

	private Integer pageSize = 24;

	private Integer protypeId;

	private Integer brandId;

	private Integer proId;

	public Integer getProId() {
		return proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public Integer getProtypeId() {
		return protypeId;
	}

	public void setProtypeId(Integer protypeId) {
		this.protypeId = protypeId;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public IProductBo getProductBo() {
		return productBo;
	}

	public void setProductBo(IProductBo productBo) {
		this.productBo = productBo;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	public String fingAllBrands() {
		String resutl = null;
		try {
			productBo = (IProductBo) this.getBean("productBo");
			HttpSession session = this.getRequest().getSession();
			// this.pageBean//=productBo.getCategory();
			List<Product> products = pageBean.getList();
			session.setAttribute("products", products);
			resutl = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			resutl = ERROR;
		}
		return resutl;
	}

	// ��ҳ��ѯ������Ʒ��
	public String fingProduct() {
		String resutl = null;
		try {
			productBo = (IProductBo) this.getBean("productBo");
			HttpSession session = this.getRequest().getSession();
			this.pageBean = productBo.getPerProduct(pageSize, page);
			List<Product> products = pageBean.getList();
			session.setAttribute("productss", products);
			resutl = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			resutl = ERROR;
		}
		return resutl;
	}

	// ��ҳ��ѯ����Ʒ�ƣ�
	public String fingBrandByAll() {
		String resutl = null;
		try {
			productBo = (IProductBo) this.getBean("productBo");
			HttpSession session = this.getRequest().getSession();
			this.pageBean = productBo.getBrand(pageSize, page);
			List<Brand> brands = pageBean.getListBrand();
			session.setAttribute("brands", brands);
			resutl = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			resutl = ERROR;
		}
		return resutl;
	}

	// ������ѯ���е���Ʒ
	public String findProductByType() {
		String resutl = null;
		try {
			productBo = (IProductBo) this.getBean("productBo");
			HttpSession session = this.getRequest().getSession();
			this.pageBean = productBo.getPerProductByType(pageSize, page,
					protypeId);
			List<Product> products = pageBean.getList();
			session.setAttribute("productss", products);
			getRequest().setAttribute("protypeId", protypeId);
			resutl = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			resutl = ERROR;
		}
		return resutl;
	}

	// ��Ʒ�Ʋ�ѯ���е���Ʒ
	public String findProductByBrand() {
		String resutl = null;
		try {
			productBo = (IProductBo) this.getBean("productBo");
			HttpSession session = this.getRequest().getSession();
			this.pageBean = productBo.getPerProductByType(pageSize, page,
					brandId);
			List<Product> products = pageBean.getList();
			session.setAttribute("productss", products);
			getRequest().setAttribute("brandId", brandId);
			resutl = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			resutl = ERROR;
		}
		return resutl;
	}

	// ��ѯÿ����Ʒ����ϸ��Ϣ
	public String showProduct() {
		String resutl = null;
		try {
			productBo = (IProductBo) this.getBean("productBo");
			HttpSession session = this.getRequest().getSession();
			Product product = productBo.getProductById(proId);
			session.setAttribute("product", product);
			resutl = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			resutl = ERROR;
		}
		return resutl;
	}

	// ��ѯ���з���
	public String findAllProductTypes() {
		String resutl = null;
		try {
			productBo = (IProductBo) this.getBean("productBo");
			List<ProductType> types = types = productBo.getCategory();
			this.getRequest().setAttribute("types", types);
			resutl = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			resutl = ERROR;
		}
		return resutl;
	}

	// ��������һ����Ʒ
	public String addProduct() {
		Purchaser purchaser = (Purchaser) (this.getRequest().getSession()
				.getAttribute("purchaser"));
		if (purchaser.getVender().getVenStatus() == 1) {
			Product product = new Product();
			product.setProName(this.getRequest().getParameter("proName"));
			product.setProCode(this.getRequest().getParameter("proCode"));
			product.setProFeature(this.getRequest().getParameter("proFeature"));
			String str = this.getRequest().getParameter("proPrice");
			if (str != null) {
				product.setProPrice(Double.valueOf(str));
			}
			product.setProRemark(this.getRequest().getParameter("proRemark"));
			product.setProUnit(this.getRequest().getParameter("proUnit"));
			Brand brand = new Brand();
			str=this.getRequest().getParameter("brandId");
			if(str!=null)
			brand.setBrandId(Integer.valueOf(str));
			product.setBrand(brand);
			ProductGroup group = new ProductGroup();
			str=this.getRequest().getParameter(
			"progId");
			if(str!=null)
			group.setProgId(Integer.valueOf(str));
			product.setProductGroup(group);
			ProductType type = new ProductType();
			str=this.getRequest().getParameter(
					"protypeId");
			if(str!=null)
			type.setProtypeId(Integer.valueOf(str));
			product.setProductType(type);
			productBo = (IProductBo) this.getBean("productBo");
			productBo.addProduct(product);
			return SUCCESS;
		} else
			return "false";
	}

	// ɾ��һ����Ʒ
	public String delProduct() {
		Purchaser purchaser = (Purchaser) (this.getRequest().getSession()
				.getAttribute("purchaser"));
		if (purchaser.getVender().getVenStatus() == 1) {
			productBo = (IProductBo) this.getBean("productBo");
			productBo.delProductById(Integer.valueOf(this.getRequest()
					.getParameter("proId")));
			return SUCCESS;
		} else
			return "false";
	}

	// �����޸�һ����Ʒ
	public String updateProduct() {
		Purchaser purchaser = (Purchaser) (this.getRequest().getSession()
				.getAttribute("purchaser"));
		if (purchaser.getVender().getVenStatus() == 1) {
			Product product = new Product();
			product.setProName(this.getRequest().getParameter("proName"));
			product.setProCode(this.getRequest().getParameter("proCode"));
			product.setProFeature(this.getRequest().getParameter("proFeature"));
			product.setProPrice(Double.valueOf(this.getRequest().getParameter(
					"proPrice")));
			product.setProRemark(this.getRequest().getParameter("proRemark"));
			product.setProUnit(this.getRequest().getParameter("proUnit"));
			Brand brand = new Brand();
			brand.setBrandName(this.getRequest().getParameter("brandName"));
			product.setBrand(brand);
			ProductGroup group = new ProductGroup();
			group.setProgId(Integer.valueOf(this.getRequest().getParameter(
					"progId")));
			product.setProductGroup(group);
			ProductType type = new ProductType();
			type.setProtypeId(Integer.valueOf(this.getRequest().getParameter(
					"protypeId")));
			product.setProductType(type);

			productBo.updateProduct(product);
			return SUCCESS;
		} else
			return "false";
	}

	// ��������һ����Ʒ��
	public String addProductGroup() {
		Purchaser purchaser = (Purchaser) (this.getRequest().getSession()
				.getAttribute("purchaser"));
		if (purchaser.getVender().getVenStatus() == 1) {
			ProductGroup productGroup = new ProductGroup();
			productGroup.setProgId(Integer.valueOf(this.getRequest()
					.getParameter("progId")));
			productGroup.setVender(purchaser.getVender());

			return SUCCESS;
		} else
			return "false";
	}

	// ��ѯ���ҵĲ�Ʒ��
	public String getPerGroup() {
		Purchaser purchaser = (Purchaser) (this.getRequest().getSession()
				.getAttribute("purchaser"));
		if (purchaser.getVender().getVenStatus() == 1) {
			productBo = (IProductBo) this.getBean("productBo");
			List<ProductGroup> list = productBo
					.findGroup(purchaser.getVender());
			this.getRequest().setAttribute("productGroups", list);
			return SUCCESS;
		} else
			return "false";
	}

	// ɾ��һ����Ʒ��
	public String delPerGroup() {
		Purchaser purchaser = (Purchaser) (this.getRequest().getSession()
				.getAttribute("purchaser"));
		if (purchaser.getVender().getVenStatus() == 1) {
			productBo = (IProductBo) this.getBean("productBo");
			productBo.delGroup(Integer.valueOf(this.getRequest().getParameter(
					"groupId")));
			return SUCCESS;
		} else
			return "false";
	}

	public String beforeAddProduct() {
		Purchaser pur = (Purchaser) (this.getRequest().getSession()
				.getAttribute("purchaser"));
		System.out.println(pur);
		if (pur.getVender().getVenStatus() == 1) {
			productBo = (IProductBo) this.getBean("productBo");
			List<ProductGroup> productGroups = productBo.findGroup(pur
					.getVender());
			this.getRequest().setAttribute("productGroups", productGroups);
			List<Brand> brands = productBo.getBrand(100, 1).getListBrand();
			this.getRequest().setAttribute("brands", brands);
			List<ProductType> types = productBo.getCategory();
			this.getRequest().setAttribute("types", types);
			return SUCCESS;
		} else
			return "fail";
	}

	// �鿴�����տ
	public String findAllRecivery() {
		productBo = (IProductBo) this.getBean("productBo");
		List<ReceiptBill> list = productBo.findAllRecivery();
		this.getRequest().setAttribute("receiptBills", list);
		return SUCCESS;
	}

	// �鿴���н�����
	public String findAllDelivery() {
		productBo = (IProductBo) this.getBean("productBo");
		List<DeliveryBill> list = productBo.findAllDelivery();
		this.getRequest().setAttribute("deliveryBills", list);
		return SUCCESS;
	}
}
