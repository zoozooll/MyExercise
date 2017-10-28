<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<title>订单明细</title>
		

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
	 
	 		
		<style type="text/css">
			<!--
			body {
				background-color: #blue;
			}
			#latest{				
				padding:35px 0px 0px 0px;
				width:600px;
				position:absolute;
				margin-left:200px;
				margin-top:15px;
				left: 45px;
				top: 270px;
			}	
			
			.STYLE4 {
				font-size: 60px;
				font-family: "华文隶书", "华文楷体", "微软雅黑", "宋体-方正超大字符集", "黑体";
			}
					
			-->
		</style>


	</head>

	<body>
	<%@include file="/head.jsp" %>
	<center>
		
			
				<div id="latest">
					<div class="STYLE4">
						订单详细信息
					</div>
					<table border="1" style="width: 650px;">
						<tr> 
							 
							<td>
								<div align="center">产品Id ： 
								</div>
							</td>
							<td>
								<div align="center">
								 	数&nbsp;&nbsp;量 ：
								</div>
							</td>
							<td>
								<div align="center">
									库&nbsp;存&nbsp;&nbsp;地 ：
								</div>
							</td>
							<td>
								<div align="center">
									总&nbsp;金&nbsp;&nbsp;额 ：
								</div>
							</td>						
						</tr>
						<c:forEach var="map" items="${sessionScope.cart.cart}">
						<tr>		 
							<td>
								${map.value.product.proId}
							</td>
							<td>
								${map.value.productSum}
							</td>
							<td>
								${map.value.product.stock.storeHouse}
							</td>
							<td>
								${map.value.sumMoney} 
							</td>
						</tr>						
						</c:forEach>
					</table>

				</div>
			
		
	</center>
	</body>
</html>
<!-- 底部页面开始 -->
<%@ include file="footer.jsp" %>
<!-- 底部页面结束 -->