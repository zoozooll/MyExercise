<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<title>�޽�����</title>
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
			<span style="text-align: left;">���������ڵ�λ����:Today&gt;&gt;<a href="index.jsp">��ҳ</a>&gt;&gt;<a style="color:red;font-weight: bold;">��Ʒ�б�</a>&gt;&gt;</span>			
			<jsp:include page="indexleft.jsp"></jsp:include>
			<div id="main">	
				<div id="recommend" style="width:716px;">	
					<ul>		
					<c:forEach var="pro" items="${products}">										 
						<li style="height:200px;padding:5px 1px 5px 4px;margin:5px 5px 5px 2px;"><a href="showProduct.action?proId=${pro.proId}">											
						<img src="${pro.proImagepath}"/>
						<p style="height:48px;overflow: hidden;">��Ʒ��:<span class="proimportant">${pro.proName}</span>|�۸�:��<span class="proimportant">${pro.proPrice}</span>|ʣ��:<span class="proimportant">${pro.stock.stoAmount}</span> ${pro.proUnit}
						</p></li>
						<div align="center">
						<c:choose>
							<c:when test="${purchaser.purIsvendot=='no'||purchaser==null}">
								<a href="addProductToCartAction.action?productId=${product.proId}"><img src="images/common/cartbuy.jpg" alt="����" style="width:57px;height:21px;"/></a>								
							</c:when>
							<c:otherwise>
								<a href="#"><img src="images/common/delete.jpg" alt="ɾ�� " style="width:50px;height:18px;"/></a>
								<a href="#"><img src="images/common/modify.jpg" alt="�޸� " style="width:50px;height:18px;"/></a>
							</c:otherwise>
						</c:choose> 
						</div>	
					</c:forEach>
					</ul>
					<jsp:include page="brandPageman.jsp"></jsp:include>	
				</div>	
			</div>
			</div>
	</body>
</html>
<%@ include file="footer.jsp" %>
