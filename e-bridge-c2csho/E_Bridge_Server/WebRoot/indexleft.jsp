<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<jsp:directive.page import="org.springframework.web.context.WebApplicationContext"/>
<jsp:directive.page import="org.springframework.web.context.support.WebApplicationContextUtils"/>
<jsp:directive.page import="com.bo.IProductBo"/>
<jsp:directive.page import="com.vo.ProductType"/>
<jsp:directive.page import="com.vo.Brand"/>
<jsp:directive.page import="com.vo.PageBean"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	WebApplicationContext context=WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
	IProductBo pbo=(IProductBo)context.getBean("productBo");
	List<ProductType> types=pbo.getCategory();
	int N=8;
	int pageIndex=1;
	PageBean pageBean=pbo.getBrand(N,pageIndex);
	List<Brand> brands=pageBean.getListBrand();	
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<title>巨匠电子</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<link href="css/global.css" rel="stylesheet" type="text/css" />
		<link href="css/css.css" rel="stylesheet" type="text/css" />		
		<script src="js/jquery-1[1].2.1.pack.js" type="text/javascript"></script>
		<script src="js/slide.js" type="text/javascript"></script>
</head>

	<body>			
			<div id="left">
				<div id="login">&nbsp; 
					<span style="padding-left: 5px;"><font style="font-size: 14px;color: white;"><b>用户登录</b></font></span>
					<c:choose>
					<c:when test="${purchaser.purIsvendot=='yes'}">
						<form name="loginForm" style="font-size: 14px;">
						<div style="margin: 10px;">用户信息</div>
						<div style="margin: 10px;"> 卖家:<span style="color:red;">${purchaser.purName }</span></div>
						</form>
					</c:when>
					<c:when test="${purchaser.purIsvendot=='no'}">
						<form name="loginForm" style="font-size: 14px;">
						<div style="margin: 10px;">用户信息</div>
					<div style="margin: 10px;">买家:<span style="color:red;">${purchaser.purName}</span></div>
						<div style="margin: 10px;"><a href="userupdatetovender.jsp">申请为卖家</a></div>
						</form>
					</c:when>
					<c:otherwise>
						<form name="loginForm" action="loginAction.action" method="post">
						<p>
							用户:
							<input name="purName" type="text" class="text" />
						</p>
						<p>
							密码:
							<input name="purPassword" type="password" class="text" />
						</p>
						<p>
							<input name="button" type="submit"
									class="btn" value="登录" />
							<input type="button" class="btn"
									value="注册" onclick="window.location='register.jsp'"/>
							<a href="passwordcomeback.jsp">忘记秘密</a>
						</p>
					</form>
					</c:otherwise>
					</c:choose>
				</div>
				<div id="category" style="margin-bottom:0px;">
				<span style="padding-left: 5px;"><font style="font-size: 14px;color: white;"><b>商品分类</b></font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="fingProduct.action?page=1">全部商品&gt;&gt;&gt;</a></span>
				<div id="catborder">
					<c:forEach var="type" items="<%=types %>">	
								<c:choose>	
								<c:when test="${type.productType==null}">
									<h4><font color="white" size="3px"><b>${type.typeName}</b></font><br/></h4>				
									<ul><c:forEach var="t" items="${type.productTypes}"> 
											<font color="black" size="2px"><b>${t.typeName}</b></font><br/>
													<c:forEach var="tp" items="${t.productTypes}"> 
													<li><a href="findProductByType.action?page=1&protypeId=${tp.protypeId}">${tp.typeName}</a></li>
													</c:forEach>
											</c:forEach>
										</ul>																	
									</c:when>
									</c:choose>	
							</c:forEach>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="findAllProductTypes.action">更多分类</a>&gt;&gt;&gt;
					</div>
				</div>
				<div id="category" style="margin-top: -30px;">
				<span style="padding-left: 5px;"><font style="font-size: 14px;color: white;"><b>商品品牌</b></font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="fingBrandByAll.action">更多品牌&gt;&gt;&gt;</a></span>
				<div id="catborder">
				<ul><c:forEach var="brand" items="<%=brands %>">	
					<li style="padding-left: 20px;" ><a href="findProductByBrand.action?page=1&brandId=${brand.brandId }"><img src="${brand.brandImage}" alt="${brand.brandName}"/></a></li>
				</c:forEach></ul>
				</div>
				</div>
			</div>	
	</body>
</html>

