<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>
	我的购物车
</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="css/shopcart/shopcart.css" rel="stylesheet" type="text/css"/>
</head>
  <body>
     <div class="container">
		<div class="top">
		<div class="logo" style="margin:0px;"><a href="index.jsp"><img src="images/common/logo.jpg" alt="logo图标" style="width:150px;border: none;"/></a></div>		
		<div class="title">			
					&nbsp;&nbsp;1.我的购物车 
					&nbsp;&nbsp;2.填写核对订单信息
					&nbsp;&nbsp;&nbsp;3.成功提交订单
			</div>
		</div>
		<span></span>
		<div class="mycart"><img src="images/shopcart/cart_001.gif" style="vertical-align: middle;"/>购物满400元，部分地区免运费，
		<a href="shophelp.jsp" target="_blank" style="font-size: 13px;color:#8BB4F3;">详细了解&gt;&gt;</a>
		</div>
		<!--mycart_tip end -->
	<div>	
	<div class="myselect">
	<h3 style="padding:8px;"><strong>我挑选的商品</strong></h3></div>
	<div class="listcart" style="border:3px solid #AACDED; width:100%;
	 border:3px solid #AACDED;
	border-top: none;margin-top:-19px;">
	 <!--商品列表开始-->
	    <div style="padding-top: 20px;">
				    
				<table width="95%" cellspacing="0" cellpadding="0" border="1px" bordercolor="#AACDED" style="margin:20px 25px;background-color: #EBF4FB;font-size: 13px;">
				<tr>
				    <td width="7%" style="height: 30px;">商品编号</td>
				    <td>商品名称</td>
				    <td width="10%">商品价格</td>
				    <td width="8%">商品数量</td>
				    <td width="8%">商品总价</td>
				    <td width="9%">删除商品</td>
				    <td width="7%">修改商品数量</td>
				 </tr>
				    <c:forEach var="map" items="${sessionScope.cart.cart}">
		<tr>   
		<form method="post" action="updateProductSumFromCartAction.action?productId=${map.value.product.proId}" name="f1">
			<input type="hidden" name="productid" value="2">
			<input type="hidden" name="number" value="1">
			<td>${map.value.product.proId}</td>
			<td>&nbsp;&nbsp;<a href="showProduct.action?proId=${map.value.product.proId}">${map.value.product.proName}</a></td>
			<td><br>${map.value.baseprice}</td>			
			<td><input type="text" name="productSum" id=${map.value.product.proId} value="${map.value.productSum}" size="8" /></td>
			<td>&nbsp;${map.value.productSum*map.value.baseprice} </td>
			<td>
			<input type="button" value="删除" onClick="javascript:window.location='delProductFormCartAction.action?productId=${map.value.product.proId}';"></td>
			<td> <input type="submit" value="保存修改"></td>
    	 </form>
		</tr> 
            </c:forEach> 
				 <tr>
				    <td style="height: 30px;text-align: right;" colspan="7">原始金额：￥${sessionScope.cart.allMoneyOnCart}元 - 返现：￥0.00元<br/>商品总金额(不含运费)：<span style="color:red;font-weight: bolder;font-size: 16">￥${sessionScope.cart.allMoneyOnCart}</span>元</td>
				 </tr>				
				</table>
	
	    </div>

    	<div>
		    <!--商品列表结束-->
				<ul style="margin-bottom: 0px; list-style-type: none;font-size:13px;">
					<li style="float: left;"><img src="images/shopcart/gwc_03.jpg" style="vertical-align: middle;"/>&nbsp; <a href="index.jsp">继续购物</a>&nbsp;</li>
					<li style="float: left;"><img src="images/shopcart/gwc_05.jpg" style="vertical-align: middle;"/>&nbsp;&nbsp;<a href="javascript:void(0);" onclick="this.blur();saveCart();">寄存购物车</a> <br></li>
					<li><img src="images/shopcart/gwc_07.jpg" style="vertical-align: middle;"/>&nbsp; <a href="removeCartAction.action">清空购物车</a>&nbsp;</li>
				</ul>
					<div style="text-align: right;">
						<img alt="进入分期订单" style="border: medium none ; cursor: pointer;" src="images/shopcart/fqbtn.gif"/>
				<a href="beforeCreateOrderAction.action?method=beforeCreateOrder" ><img alt="进入订单信息页" style="border: medium none ; cursor: pointer;"  src="images/shopcart/gwc_10.jpg"/>
				</a>	</div>					
				
		</div>
	
		<div style="background:url('images/shopcart/shopcartcorner.jpg') no-repeat;margin: 5px 25px;padding: 0px 10px;color:#2D72B6;height: 200px;">
			<!--购买本商品的用户还购买了...开始-->
			<h3 style="margin: 20"><img src="images/shopcart/arrow_2.gif">选超值礼品，获更多优惠</h3>			
			<hr style="margin-top:-25px;"/>
			<div style="margin-top: -30px;">
				<ul style="list-style-type: none;">
					<li style="float:left;font-size: 13px;width: 200px;margin-right: 15px;margin-right: 15px;">
					<div style="float: left;"><img width="85" height="64" src="images/shopcart/shoubiao.jpg" style="border:none;"/></div>
					<div><a href="">卡西欧(Casio)10年电池系列男表AW-80D-7A</a></div>
						<div>仅需￥169.00元</div>
						<div><a href="#none"><img src="images/shopcart/addcart2.gif" style="border:none;"/></a></div>
					</li>
					
					<li style="float:left;font-size: 13px;width: 200px;margin-right: 15px;">
					<div style="float: left;"><img width="85" height="64" src="images/shopcart/yumaoqiu.jpg" /></div>
						<div><a href="">红双喜12支装羽毛球402</a></div>
						<div>仅需￥32.00元</div>
						<div><a href="#none"><img src="images/shopcart/addcart2.gif" style="border:none;"/></a></div>
					</li>
					
					<li style="float:left;font-size: 13px;width: 200px; margin-right: 15px;">
					<div style="float: left;"><img width="85" height="64" src="images/shopcart/shoubiao.jpg" /></div>
						<div><a href="">卡西欧(Casio)10年电池系列男表AW-80D-1A</a></div>
						<div>仅需￥169.00元</div>
						<div><a href="#none"><img src="images/shopcart/addcart2.gif" style="border:none;"/></a></div>
					</li>
					
					<li style="float:left;font-size: 13px;width: 200px;margin-right: 15px;">
					<div style="float: left;"><img width="85" height="64" src="images/shopcart/shoudiantong.jpg"/></div>
						<div><a href="">次世代大功率耐摔手电筒CF06</a></div>
						<div>仅需￥21.00元</div>
						<div><a href="#none"><img src="images/shopcart/addcart2.gif" style="border:none;"/></a></div>
					</li>
				</ul>
				<!--[if !IE]>购买本商品的用户还购买了...结束<![endif]-->
				</div>	
			</div>		
		 </div>
	   </div>	
	</div>
	</body>
</html>
<jsp:include page="footer.jsp"></jsp:include>