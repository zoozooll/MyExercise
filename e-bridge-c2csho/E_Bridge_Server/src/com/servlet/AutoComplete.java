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

public class AutoComplete extends HttpServlet{
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*System.out.println("已经运行了........");
		//表示页面传过来的字符串，用于和服务器段的单词进行完整匹配
		String word=req.getParameter("word");
		//将字符串保存在 request对象中
		req.setAttribute("word", word);
		//将请求转发给视图层（注意AJAX中这个所谓的视图层不返回页面，只返回数据，所心也可以称作是一个数据层）
		req.getRequestDispatcher("wordxml.jsp").forward(req, resp);*/
		request.setCharacterEncoding("gbk");
		response.setCharacterEncoding("gbk");
		response.setContentType("text/html;charset=gbk");
		System.out.println("AutoComplete已经运行了 .............");
		String condition = request.getParameter("word");
		String entityName = request.getParameter("entityName");
		System.out.println("取到的值condition="+condition+",entityName="+entityName);
		ApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(this.getServletContext());
		productBo = (IProductBo) context.getBean("productBo");
		userBo = (IUserBo) context.getBean("userBo");
		String[] words = null;
		try {
			if ("Product".equals(entityName)) {
				words = productBo.findProductName(condition);
				/*for (String string : words) {
					System.out.println("-------------------"+string);
				}*/
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
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	doGet(req, resp);
	}
}
