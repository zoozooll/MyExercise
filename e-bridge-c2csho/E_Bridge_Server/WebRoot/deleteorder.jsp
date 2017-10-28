<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<title>�����б�</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<link href="css/global.css" rel="stylesheet" type="text/css" />
		<link href="css/css.css" rel="stylesheet" type="text/css" />
		<script src="js/jquery-1[1].2.1.pack.js" type="text/javascript"></script>
		<script src="js/slide.js" type="text/javascript"></script>
		<script type="text/javascript">
	function show(obj) {
		var elem = document.getElementById(obj);
		if (elem.style.display == '') {
			elem.style.display = 'none';
			return;
		}
		elem.style.display = '';
	}
</script>
		<style type="text/css">
.required {
	border: 1px solid black;
	width: 200px;
}

.bor {
	margin: 10px 2px 3px 12px;
	padding-left: 10px;
	text-align: center;
}
</style>

	</head>
	<body>

		<div id="container" style="height: auto">
			<span style="text-align: left;">���������ڵ�λ����:Today&gt;&gt;<a
				href="index.jsp">��ҳ</a>&gt;&gt;<a href="Unaudited_order.jsp">��������</a>&gt;&gt;<a
				style="color: red; font-weight: bold;">�����б�</a>&gt;&gt;</span>
			<c:choose>
					<c:when test="${purchaser.purIsvendot=='yes'&&purchaser.vender.venStatus==1}">
			<jsp:include page="mycompanyleft.jsp"></jsp:include>
			</c:when>
			<c:when test="${purchaser.purIsvendot=='no'}"><jsp:include page="purleft.jsp"></jsp:include></c:when>
			<c:otherwise><jsp:include page="indexleft.jsp"></jsp:include></c:otherwise>
			</c:choose>			
			<div id="main"
				style="border: 1px solid red; border-top: none; padding-bottom: 50px;">
				<div id="latest" style="height: 240px;">
					<div align="center" style="font-size: 20px;font-weight: bold;margin: 10px;">
						<span >�����б�</span>
					</div>
					<div class="userlist"><form action="delOrderAction.action" method="post">
					<input type="text" name="orderlineId"
						style="font-size: 14px; height: 20px; width: 200px; margin-left: 30px;border: 1px solid black;"/>
					<select name="typeName"
						style="font-size: 14px; width: 140px; margin-left: 30px;margin-top: 0px;border: 1px solid black;">
						<option value="purName">
							������ɾ��
						</option>
						<option value="purId" selected="selected">
							��IDɾ��
						</option>						
					</select>
					<input
						style="font-size: 14px; height: 30px; width: 80px; margin-left: 30px;border: 1px solid black;"
						type="submit" value="ȷ��"/>
				</form>
			</div>
					<table class="datalist" border="0.2" align="center">
						<tr>
							<th>
								<div align="left">
									�������
								</div>
							</th>
							<th>
								<div align="left">
									��������
								</div>
							</th>
							<th>
								<div align="left">
									������Դ
								</div>
							</th>
							<th>
								<div align="left">
									���ʽ
								</div>
							</th>
							<th>
								<div align="left">
									��������
								</div>
							</th>
							<th>
								<div align="left">
									Ҫ�󵽴�ʱ��
								</div>
							</th>
							<th>
								<div align="left">
									����״̬
								</div>
							</th>							
						</tr>
						<c:set var="flag" value="true" />
						<c:forEach var="orderline" items="${userorderline}">
							<c:choose>
								<c:when test="${flag==true}">
									<tr>
										<td>
											<div align="left">
												${orderline.order.orderCode}
											</div>
										</td>
										<td>
											<div align="left">
												${orderline.order.orderName}
											</div>
										</td>
										<td>
											<div align="left">
												${orderline.order.orderSource}
											</div>
										</td>
										<td>
											<div align="left">
												${orderline.order.payway}
											</div>
										</td>
										<td>
											<div align="left">
												${orderline.order.paylater}
											</div>
										</td>
										<td>
											<div align="left">
												${orderline.order.arrivetime}
											</div>
										</td>
										<td>
											<div align="left">												
			                      ${orderline.state}			                   
											</div>
										</td>										
									</tr>
									<c:set var="flag" value="false" />
								</c:when>
								<c:otherwise>
									<tr class="altrow">
										<td>
											<div align="left">
												${orderline.order.orderCode}
											</div>
										</td>
										<td>
											<div align="left">
												${orderline.order.orderName}
											</div>
										</td>
										<td>
											<div align="left">
												${orderline.order.orderSource}
											</div>
										</td>
										<td>
											<div align="left">
												${orderline.order.payway}
											</div>
										</td>
										<td>
											<div align="left">
												${orderline.order.paylater}
											</div>
										</td>
										<td>
											<div align="left">
												${orderline.order.arrivetime}
											</div>
										</td>
										<td>
											<div align="left">												
			                      ${orderline.state}			                   
											</div>
										</td>	
									</tr>
									<c:set var="flag" value="true" />
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</table>
				</div>
			</div>
		</div>
	</body>
</html>