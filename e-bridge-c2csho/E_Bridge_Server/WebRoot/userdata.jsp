<%@ page language="java" import="java.sql.*" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include  file="head.jsp"   %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
	<head>
		<title>��������</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gbk" />

		<link href="css/register.css" rel="stylesheet" type="text/css" />	
		<style type="text/css">
			.borde{
				margin-top: 10px;
				text-align: left;
				margin-left: 100px;
			}
		</style>
			
</head>
	<body>
	<div id="container" style="height: auto">
			<span style="text-align: left;">���������ڵ�λ����:Today&gt;&gt;<a href="index.jsp">��ҳ</a>&gt;&gt;<a href="findAllPurchasersAction.action">��������</a>&gt;&gt;<a style="color:red;font-weight: bold;">�û��б�</a>&gt;&gt;</span>			
				<c:choose>
					<c:when test="${purchaser.purIsvendot=='yes'&&purchaser.vender.venStatus==1}">
			<jsp:include page="mycompanyleft.jsp"></jsp:include>
			</c:when>
			<c:when test="${purchaser.purIsvendot=='no'}"><jsp:include page="purleft.jsp"></jsp:include></c:when>
			<c:otherwise><jsp:include page="indexleft.jsp"></jsp:include></c:otherwise>
			</c:choose>
		<div id="main" style="border:1px solid red;border-top: none;padding-bottom: 50px;">
				<div id="latest" style="height: 240px; top: 67px; left: 152px;">
					<center>
				<div style="width: 100%">
				<div>
					<span style="font: bolder; font-size: 15px;">���ĸ�����Ϣ</span>					
				</div>		
				
				
				
				<div class="borde">
							<span>&nbsp;&nbsp;�û�����</span>
							<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span><c:if test="${purchaser.purIsvendot=='no'}">���</c:if> <c:if test="${purchaser.purIsvendot=='yes'}">����</c:if>  </span>
				</div>

				<div class="borde">
							<span>&nbsp;&nbsp;��˾����</span>
							<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span>${purchaser.purName }</span>					
				</div>

				<div class="borde">
							<span>&nbsp;&nbsp;��˾�绰</span>
							<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span>${purchaser.purTelephone }</span>
					
				</div>

				<%--<div class="borde">
				<span>&nbsp;&nbsp;��˾��ַ</span>
							<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;
							<span> ${purchaser.purProvince }ʡ &nbsp;&nbsp;&nbsp;${purchaser.purCity } �� </span>
					
				</div>
				--%>
				<div class="borde">
							<span>&nbsp;&nbsp;��Ӫ��ַ</span>
							<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span>${purchaser.purAddress}</span>
					
				</div>

				<div class="borde">
							<span>&nbsp; ��˾�ʱ�</span>
							<span class="red">: </span>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span>${purchaser.purPostalcode}</span>
					
				</div>
				<c:if test="${purchaser.purIsvendot=='yes'}">
				<div id="venderstyle">
					<div class="borde">
								<span>&nbsp;&nbsp;��˾���</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span>${purchaser.vender.venShortname}</span>
							
					</div>

					<div class="borde">
								<span>&nbsp;&nbsp;Ӫҵִ��</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span>${purchaser.vender.venShopcard}</span>						
					</div>

					<div class="borde">
								<span>&nbsp;&nbsp;��˾����</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span>${purchaser.vender.venFax}</span>							
					</div>

					<div class="borde">
								<span>&nbsp;&nbsp;��˾��Ա</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;
								<span>${purchaser.vender.venLinkman}</span>
						
					</div>

					<div class="borde">
								<span>&nbsp;&nbsp;��ϵ�绰</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;
								<span>${purchaser.vender.venLinkmanphone}</span>
						
					</div>

					<div class="borde">
								<span>&nbsp;&nbsp;��˾����</span>
								<span class="red">: </span> &nbsp;
								<span>${purchaser.vender.venEmail}</span>
						
					</div>

					<div class="borde"><span>&nbsp; �������</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><c:choose>
    			<c:when test="${purchaser.vender.venStatus==-1}">
    				���δͨ��
    			</c:when>    			
    		</c:choose>
    		<c:choose>
    			<c:when test="${purchaser.vender.venStatus==0}">
    				δ���
    			</c:when>    			
    		</c:choose>
    		<c:choose>
    			<c:when test="${purchaser.vender.venStatus==1}">
    				�����ͨ��
    			</c:when>    			
    		</c:choose>
								</span>						
					</div>
				</div>
				</c:if> 
				
				<div class="borde" style="margin-left: 150px;margin-top: 20px">
					<a href="userupdate.jsp"><img src="images/common/modify.jpg" alt="�޸���Ϣ"/></a>
				</div>
				</div>
			</center>
				
				</div>
		</div>
	</div>
	
	
	</body>
</html>
<!-- �ײ�ҳ�濪ʼ -->
<%@ include file="footer.jsp" %>
<!-- �ײ�ҳ����� -->