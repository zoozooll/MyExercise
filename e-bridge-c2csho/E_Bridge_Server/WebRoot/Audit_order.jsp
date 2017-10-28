<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="head.jsp"   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>巨匠电子</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<script src="js/jquery-1[1].2.1.pack.js" type="text/javascript"></script>
<script src="js/slide.js" type="text/javascript"></script>
</head>

<body>
<div id="container" style="height: auto">
			<span style="text-align: left;">您现在所在的位置是:Today&gt;&gt;<a href="index.jsp">首页</a>&gt;&gt;<a href="Unaudited_order.jsp">订单中心</a>&gt;&gt;<a style="color:red;font-weight: bold;">提交订单</a>&gt;&gt;</span>			
			<jsp:include page="mycompanyleft.jsp"></jsp:include>
			<div id="main" style="border:1px solid red;border-top: none;padding-bottom: 50px;">
				<div id="latest" style="height: 240px; top: 67px; left: 152px;">
					<div align="center" style="font-size: 25px;margin: 10px;">订单信息</div>						
						<table class="datalist">	
						<tr>
						<td > <div align="center">产品名</div></td>
						 <c:forEach var="pro" items="${orderline.products}">
							<td>${pro.proName}&nbsp;&nbsp;</td>
						</c:forEach> 
						</tr>	
						<tr class="altrow">
						<td > <div align="center">产品数量</div></td>						
							<td>${userorderline.amount}&nbsp;&nbsp;</td>						
						</tr>
						<tr>
						<td > <div align="center">产品总金额</div></td>
						<td>  ${userorderline.sumMoney}</td>
						</tr>											
						<tr class="altrow">
						<td > <div align="center">送达方</div></td>
						<td>  ${userorderline.order.sendto}</td>
						</tr>				
						<tr align="center">
						<td colspan="2">
						<input name="submit" type="submit" value="生成交货单 " style="border: 1px solid black;" /></td></tr>
						</table>						
						
				</div>
				</div>
			</div>	
</body>
</html>

<%@ include file="footer.jsp" %>