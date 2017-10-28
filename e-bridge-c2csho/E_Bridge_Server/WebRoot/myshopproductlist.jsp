<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<title>巨匠电子</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<link href="css/global.css" rel="stylesheet" type="text/css" />
		<link href="css/css.css" rel="stylesheet" type="text/css" />		
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
	<div id="container" style="height: auto">
		<span>	<a
				href="myshopcartproductlist.jsp">我的商铺</a>&gt;&gt;<a
				href="findAllPurchasersAction.action">产品中心</a>&gt;&gt;<a
				style="color: red; font-weight: bold;">产品列表</a>&gt;&gt;</span>
				<c:choose>
					<c:when test="${purchaser.purIsvendot=='yes'}">
			<jsp:include page="mycompanyleft.jsp"></jsp:include>
			</c:when>
			<c:when test="${purchaser.purIsvendot=='no'}"><jsp:include page="purleft.jsp"></jsp:include></c:when>
			</c:choose>		
			<div id="main">	
				<div id="recommend" style="width:716px;">	
					<ul>	
					<c:remove var="product"></c:remove>	
					<c:forEach var="pro" items="${purchaser.vender.productgroup.products}">										 
						<li style="height:200px;padding:5px 1px 5px 4px;margin:5px 5px 5px 2px;"><a href="myshopshowProduct.action?proId=${pro.proId}">											
						<img src="${pro.proImagepath}"/>
						<p style="height:48px;overflow: hidden;">产品名:<span class="proimportant">${pro.proName}</span>|价格:￥<span class="proimportant">${pro.proPrice}</span>|剩余:<span class="proimportant">${pro.stock.stoAmount}</span> ${pro.proUnit}
						</p></li>
						<div align="center">
								<a href="#"><img src="images/common/delete.jpg" alt="删除 " style="width:50px;height:18px;"/></a>
								<a href="#"><img src="images/common/modify.jpg" alt="修改 " style="width:50px;height:18px;"/></a>							
						</div>	
					</c:forEach>
					</ul>					
				</div>	
			</div>
			</div>
	</body>
</html>
<%@ include file="footer.jsp" %>

