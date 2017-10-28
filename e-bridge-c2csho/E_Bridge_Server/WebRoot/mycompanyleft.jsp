<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
				<div id="category">
				<c:choose>
				<c:when test="">
					<span style="padding-left: 5px;"><font style="font-size: 14px;color: white;"><b>我是卖家</b></font></span>
				</c:when>
				<c:otherwise>
					<span style="padding-left: 5px;"><font style="font-size: 14px;color: white;"><b>我是卖家</b></font></span>
				</c:otherwise>
				</c:choose>	<div id="catborder">
							<h4><font color="white" size="3px"><b>产品中心</b></font><br/></h4>				
										<ul>
											<li><a href="productadd.jsp">增加产品</a></li>
											<li><a href="addimage.jsp">增加产品图片</a></li>
											<li><a href="addbrand.jsp">增加产品品牌</a></li>
											<li><a href="myshopproductlist.jsp">查看商品</a></li>											
											<li><a href="productdelete.jsp">删除产品</a></li>											
										</ul>
							<h4><font color="white" size="3px"><b>订单中心</b></font><br/></h4>				
										<ul>
											<li><a href="findAllOrderAction.action">查看订单</a></li>											
											<li><a href="deleteorder.jsp">删除订单</a></li>											
										</ul>							
							<h4><font color="white" size="3px"><b>物流中心</b></font><br/></h4>				
										<ul>
										<li><a href="findAllDelivery.action">所有交货单</a></li>
											<li><a href="">等待发货的订单</a></li>
											<li><a href="">发货中的订单</a></li>
											<li><a href="">已发货的订单</a></li>	
											<li><a href="shophelp.jsp">运费查询</a></li>										
										</ul>
							<h4><font color="white" size="3px"><b>个人中心</b></font><br/></h4>				
										<ul>
											<li><a href="userdata.jsp">个人资料</a></li>
											<li><a href="findAllPurchasersAction.action">查看所有用户</a></li>
											<li><a href="usermodifypassword.jsp">修改密码</a></li>	
											<li><a href="userupdate.jsp">修改资料</a></li>											
										</ul>
								<h4><font color="white" size="3px"><b>服务中心</b></font><br/></h4>				
										<ul>
											<li><a href="">购买咨询</a></li>
											<li><a href="">我的投诉</a></li>	
											<li><a href="">我的返修</a></li>		
											<li><a href="helpprice.jsp">价格保护</a></li>	
											<li><a href="">我的举报</a></li>										
								</ul>
								<h4><font color="white" size="3px"><b>留言中心</b></font><br/></h4>				
										<ul>
											<li><a href="">我的留言</a></li>
											<li><a href="">我要留言</a></li>																				
								</ul>
							<h4><font color="white" size="3px"><b>财务中心</b></font><br/></h4>				
										<ul>
											<li><a href="findAllRecivery.action">查看收款单列表</a></li>																														
										</ul>
					</div>
				</div>				
			</div>				
	</body>
</html>
