package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bo.IProductBo;
import com.bo.ProductBoImpl;
import com.vo.Brand;
import com.vo.PageBean;
import com.vo.Product;
import com.vo.ProductType;

public class ProductList extends HttpServlet {

	private IProductBo productBo;
	private int page;	//  //�ڼ�ҳ
	private PageBean pageBean;	//��ֲ���Ϣ��bean
	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return the pageBean
	 */
	public PageBean getPageBean() {
		return pageBean;
	}

	/**
	 * @param pageBean the pageBean to set
	 */
	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	public IProductBo getProductBo() {
		return productBo;
	}

	public void setProductBo(IProductBo productBo) {
		this.productBo = productBo;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		try {
			//System.out.println("productlist��Servletִ����,,,,,,,,,,,,,,,,");
			ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
			productBo=(IProductBo) context.getBean("productBo");
			HttpSession session=request.getSession();
			page=1;
			//���в�Ʒ
			pageBean=productBo.getPerProduct(8,page);
			List<Product> products=pageBean.getList();
			session.setAttribute("products",products);
			//����Ʒ
			List<ProductType> types=productBo.getCategory();
			session.setAttribute("types", types);		
			//Ʒ�Ʋ�Ʒ
			pageBean=productBo.getBrand(8,page);
			List<Brand> brands=pageBean.getListBrand();		
			session.setAttribute("brands", brands);
			
			//���²�Ʒ
			List<Product> newproducts=productBo.newProducts();
			//System.out.println("���²�Ʒ"+newproducts);
			session.setAttribute("news", newproducts);	
			//�ؼ۲�Ʒ
			List<Product> cheapproducts=productBo.cheapProducts();
			//System.out.println("�ؼ۲�Ʒ"+newproducts);
			session.setAttribute("cheaps", cheapproducts);	
			
			//��ѯ���Ų�Ʒ
			List<Product> hotproducts=productBo.hotProducts();
			System.out.println("���Ų�Ʒ"+hotproducts);
			session.setAttribute("hots", hotproducts);	
			
			//System.out.println("�õ���Ʒ����==========>"+brands);
			request.getRequestDispatcher("index.jsp").forward(request, response);
		} catch (BeansException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
