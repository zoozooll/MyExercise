<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<title>������ϸ</title>
		

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
				font-family: "��������", "���Ŀ���", "΢���ź�", "����-���������ַ���", "����";
			}
					
			-->
		</style>


	</head>

	<body>
	<%@include file="/head.jsp" %>
	<center>
		
			
				<div id="latest">
					<div class="STYLE4">
						������ϸ��Ϣ
					</div>
					<table border="1" style="width: 650px;">
						<tr> 
							 
							<td>
								<div align="center">��ƷId �� 
								</div>
							</td>
							<td>
								<div align="center">
								 	��&nbsp;&nbsp;�� ��
								</div>
							</td>
							<td>
								<div align="center">
									��&nbsp;��&nbsp;&nbsp;�� ��
								</div>
							</td>
							<td>
								<div align="center">
									��&nbsp;��&nbsp;&nbsp;�� ��
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
<!-- �ײ�ҳ�濪ʼ -->
<%@ include file="footer.jsp" %>
<!-- �ײ�ҳ����� -->