<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="head.jsp"%>

<%-- ɾ����Ʒҳ�� --%>
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
.pdelete input {
	border: 1px solid black;
	width: 200px;
	margin: 0px 5px;
}
</style>
	</head>

	<body>
		<div id="container" style="height: auto">
			<span style="text-align: left;">���������ڵ�λ����:Today&gt;&gt;<a
				href="index.jsp">��ҳ</a>&gt;&gt;<a
				href="myshopproductlist.jsp">��������</a>&gt;&gt;<a
				style="color: red; font-weight: bold;">�����տ</a>&gt;&gt;</span>
			<jsp:include page="mycompanyleft.jsp"></jsp:include>
			<div id="main"
				style="border: 1px solid red; border-top: none; padding-bottom: 50px;">				
						<div id="latest" style="height: 240px; top: 67px; left: 152px;">
					<div class="pdelete">	<center>					
						
					<div style="margin-top: 30px;">
					
						<p/>
						<table class="datalist" width="100%;">
											<tr><th>
													�տ���
												</th>
												<th>
													��Ʊ��
												</th>
												
												<th>
													���ۼ�
												</th>
												
												<th>
													�տ�����
												</th>
												
												<th>
													��������
												</th>
												<th>
													����
												</th>
											</tr>
											<c:set var="flag" value="true" />	
							<c:forEach var="rece" items="${receiptBills}">							
									<c:choose>
										<c:when test="${flag==true}">										
											<tr>													
												<td>
													${rece.receiId}
												</td>
												<td>
													${rece.invoiceno }
												</td><td>
												${rece.price}
												</td><td>
													${rece.receiptdate}	
												</td><td>
												${rece.salesdate}
												</td>
												<td>
													<a href="#">�鿴</a>
													<a href="#">ɾ��</a>
												</td>
											</tr>
											<c:set var="flag" value="false" />
										</c:when>
										<c:otherwise>
											<tr class="altrow">													
												<td>
													${rece.receiId}
												</td>
												<td>
													${rece.invoiceno }
												</td><td>
												${rece.price}
												</td><td>
													${rece.receiptdate}	
												</td><td>
												${rece.salesdate}
												</td>
												<td>
													<a href="#">�鿴</a>
													<a href="#">ɾ��</a>
												</td>
											</tr>	<c:set var="flag" value="true" />
											</c:otherwise>
											</c:choose>
							</c:forEach>
						</table>
						</div>
					</center>
					</div>
				</div>
				</div>
				</div>	
	</body>
</html>
<%@ include file="footer.jsp"%>
