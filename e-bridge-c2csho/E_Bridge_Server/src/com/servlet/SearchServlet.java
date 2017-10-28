package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bo.IProductBo;
import com.bo.IUserBo;

public class SearchServlet extends HttpServlet {
	private IProductBo productBo;

	private IUserBo userBo;

	public IUserBo getUserBo() {
		return userBo;
	}

	public void setUserBo(IUserBo userBo) {
		this.userBo = userBo;
	}

	public IProductBo getProductBo() {
		return productBo;
	}

	public void setProductBo(IProductBo productBo) {
		this.productBo = productBo;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("SearchServlet已经运行了 .............");
		String condition = request.getParameter("condition");
		String entityName = request.getParameter("entityName");
		ApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(this.getServletContext());
		productBo = (IProductBo) context.getBean("productBo");
		userBo = (IUserBo) context.getBean("userBo");
		String[] words = null;
		try {
			if ("Product".equals(entityName)) {
				words = productBo.findProductName(condition);
				request.setAttribute("searchs", words);
				request.getRequestDispatcher("wordxml.jsp").forward(request,
						response);
			} else if ("Purchaser".equals(entityName)) {
				words=userBo.findUserName(condition);
				request.setAttribute("searchs", words);
				request.getRequestDispatcher("wordxml.jsp").forward(request,
						response);
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
	}

}
