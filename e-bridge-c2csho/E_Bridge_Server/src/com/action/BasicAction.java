package com.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionSupport;

public class BasicAction extends ActionSupport implements SessionAware,
                                 ServletRequestAware, ServletResponseAware  {
	 
	private HttpServletRequest request;
	private HttpServletResponse response;
	 
	
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	private static final long serialVersionUID = 1L;
	public BasicAction()
	{
		
	}
	//根据上下文获得ApplicationContext对象
	public Object getBean(String beanid)
	{
		Object obj = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext()).getBean(beanid);
		return obj;
		
	} 
	public void setServletRequest(HttpServletRequest arg0) {
		request=arg0;
	}
	public void setServletResponse(HttpServletResponse arg0) {
		response=arg0;
		
	}
	public void setSession(Map arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
