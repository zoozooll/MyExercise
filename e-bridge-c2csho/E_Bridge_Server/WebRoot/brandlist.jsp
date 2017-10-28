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
		<div id="container" style="height: auto;">
			<span style="text-align: left;">您现在所在的位置是:Today&gt;&gt;<a href="index.jsp">首页</a>&gt;&gt;<a style="color:red;font-weight: bold;">品牌列表</a>&gt;&gt;</span>			
			<jsp:include page="indexleft.jsp"></jsp:include>
			<div id="main">	
				<div id="recommend" style="width:716px;">	
					<ul>
					<c:forEach var="brand" items="${brands}">										 
						<li style="height:200px;padding:5px 1px 5px 4px;margin:5px 5px 5px 2px;"><a href="findProductByBrand.action?page=1&brandId=${brand.brandId }">											
						<img src="${brand.brandImage}" alt="${brand.brandName}"/>
						</a></li>
					</c:forEach>
					</ul>
					<jsp:include page="pagemanbrand.jsp"></jsp:include>	
				</div>	
			</div>
			</div>
	</body>
</html>
<%@ include file="footer.jsp" %>

