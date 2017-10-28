<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ page errorPage="error.jsp" %>
<jsp:directive.page import="com.bo.IProductBo"/>
<jsp:directive.page import="org.springframework.web.context.support.WebApplicationContextUtils"/>
<jsp:directive.page import="com.vo.PageBean"/>
<jsp:directive.page import="org.springframework.web.context.WebApplicationContext"/>
<jsp:directive.page import="com.vo.Product"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
	WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
	IProductBo pbo=(IProductBo)context.getBean("productBo");
	int pageIndex=1;//显示的第几页
	int N=8;//每页显示的条数
	//需要显示程序的变量
	    //pageBean;
	    //products;
	    //brands;
	    //newProducts;
	PageBean pageBean=(PageBean)pbo.getPerProduct(N,pageIndex);
	List<Product> products=pageBean.getList();
	//List<ProductType> types=pbo.getCategory();
	pageBean=pbo.getBrand(8,pageIndex);
	//List<Brand> brands=pageBean.getListBrand();	
	List<Product> newproducts=pbo.newProducts();
	List<Product> cheapproducts=pbo.cheapProducts();
//	List<Product> hotproducts=pbo.hotProducts();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<title>巨匠E-Bridge电子商务网站</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<link href="css/global.css" rel="stylesheet" type="text/css" />
		<link href="css/css.css" rel="stylesheet" type="text/css" />
		<link rel="shortcut icon" type=image/x-icon href="images/favicon.ico" />		
		<script src="js/jquery-1[1].2.1.pack.js" type="text/javascript"></script>
		<script src="js/slide.js" type="text/javascript"></script>

<style type="text/css">
<!--
#Layer1 {
	position: absolute;
	left: 750px;
	top: 712px;
	width: 162px;
	height: 138px;
	z-index: 1;
}
.proimportant{
	color: red;
}
.STYLE2 {
	color: #FF0000;
	font-weight: bold;
}
-->
</style>
</head>	
	<body>
	<%@ include file="head.jsp" %>
		<div id="container" style="height: auto">
			<span style="text-align: left;">aToday&gt;&gt;<a href="index.jsp">首页</a>&gt;&gt;</span>							
			<jsp:include page="indexleft.jsp"></jsp:include>
			<div id="main">
				<div id="latest" style="height: 240px; width: 500px; float: left">
					<jsp:include page="mainimage.jsp"></jsp:include>
						<div id="right" style="height: 273px; width: 211px; position: absolute; left: 692px; top: 20px;z-index: 3;">
						<div id="bor"><p style="font-weight: bolder;">
								热点导购：
							</p>							
							<ul>
								<li>
									<div align="left"><br />
										今日最新头条：<br/>
										<marquee id="right_news" scrollamount="4" direction="up"
							onmouseover="stop(this)" onmouseout="start(this)">
										<jsp:include page="news.jsp"></jsp:include>
										</marquee>
									</div>
								</li>
							</ul>
							</div>
						</div>
						<div id="right" style="height: 273px; width: 211px; position: absolute; left: 692px; top: 300px;z-index: 3;">
						<div id="bor"><p style="font-weight: bolder;">
								热点导购：
							</p>							
							<ul>
								<li>
									<div align="left"><br />
										今日最新头条：<br/>
										<marquee id="right_news" scrollamount="4" direction="up"
							onmouseover="stop(this)" onmouseout="start(this)">
										<jsp:include page="news.jsp"></jsp:include>
										</marquee>
									</div>
								</li>
							</ul>
							</div>
							<div id="right" style="height: 273px; width: 211px;margin-top: 20px;z-index: 3;">
						<div id="bor"><p style="font-weight: bolder;">
								热点导购：
							</p>							
							<ul>
								<li>
									<div align="left"><br />
										今日最新头条：<br/>
										<marquee id="right_news" scrollamount="4" direction="up"
							onmouseover="stop(this)" onmouseout="start(this)">
										<jsp:include page="news.jsp"></jsp:include>
										</marquee>
									</div>
								</li>
							</ul>
							</div>
						</div>
						</div>
						<div>
				<div id="recommend" style="height:200px;">	
					<ul>	
						
					<c:forEach var="pro" items="<%=products %>">										 
						<li><a href="showProduct.action?proId=${pro.proId}">											
						<img src="${pro.proImagepath}"/>						
						<p style="height:50px;overflow:hidden;">产品名:<span class="proimportant">${pro.proName}</span>|价格:￥<span class="proimportant">${pro.proPrice}</span>|剩余:<span class="proimportant">${pro.stock.stoAmount}</span> ${pro.proUnit}
						</p></a></li>			
						<div align="center" style="background: none;">						
								<a href="addProductToCartAction.action?productId=${pro.proId}"><img src="images/common/cartbuy.jpg" alt="购买" style="width:86px;height:23px;border: none;"/></a>								
						</div>				
					</c:forEach>
					</ul>
				</div>
						<div id="new" style="height:200px;">
							<ul>
								<c:forEach var="pro" items="<%=newproducts %>">										 
						<li><a href="showProduct.action?proId=${pro.proId}">											
						<img src="${pro.proImagepath}"/>						
						<p style="height:50px;overflow:hidden;">产品名:<span class="proimportant">${pro.proName}</span>|价格:￥<span class="proimportant">${pro.proPrice}</span>|剩余:<span class="proimportant">${pro.stock.stoAmount}</span> ${pro.proUnit}
						</p></a></li>			
						<div align="center" style="background: none;">						
								<a href="addProductToCartAction.action?productId=${pro.proId}"><img src="images/common/cartbuy.jpg" alt="购买" style="width:86px;height:23px;border: none;"/></a>								
						</div>				
					</c:forEach>
							</ul>
						</div>
						<div id="tips" style="height:200px;">
							<ul>
								<c:forEach var="pro" items="<%=cheapproducts %>">										 
						<li><a href="showProduct.action?proId=${pro.proId}">											
						<img src="${pro.proImagepath}"/>						
						<p style="height:50px;overflow:hidden;">产品名:<span class="proimportant">${pro.proName}</span>|价格:￥<span class="proimportant">${pro.proPrice}</span>|剩余:<span class="proimportant">${pro.stock.stoAmount}</span> ${pro.proUnit}
						</p></a></li>			
						<div align="center" style="background: none;">						
								<a href="addProductToCartAction.action?productId=${pro.proId}"><img src="images/common/cartbuy.jpg" alt="购买" style="width:86px;height:23px;border: none;"/></a>								
						</div>				
					</c:forEach>
							</ul>
						</div>
						</div>	
					</div>
				</div>
			</div>
			</div>	
	<%@ include file="footer.jsp" %>
				
	</body>
</html>
