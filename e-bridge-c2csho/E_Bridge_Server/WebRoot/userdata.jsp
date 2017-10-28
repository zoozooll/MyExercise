<%@ page language="java" import="java.sql.*" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include  file="head.jsp"   %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
	<head>
		<title>个人资料</title>
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
			<span style="text-align: left;">您现在所在的位置是:Today&gt;&gt;<a href="index.jsp">首页</a>&gt;&gt;<a href="findAllPurchasersAction.action">个人中心</a>&gt;&gt;<a style="color:red;font-weight: bold;">用户列表</a>&gt;&gt;</span>			
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
					<span style="font: bolder; font-size: 15px;">您的个人信息</span>					
				</div>		
				
				
				
				<div class="borde">
							<span>&nbsp;&nbsp;用户类型</span>
							<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span><c:if test="${purchaser.purIsvendot=='no'}">买家</c:if> <c:if test="${purchaser.purIsvendot=='yes'}">卖家</c:if>  </span>
				</div>

				<div class="borde">
							<span>&nbsp;&nbsp;公司名称</span>
							<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span>${purchaser.purName }</span>					
				</div>

				<div class="borde">
							<span>&nbsp;&nbsp;公司电话</span>
							<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span>${purchaser.purTelephone }</span>
					
				</div>

				<%--<div class="borde">
				<span>&nbsp;&nbsp;公司地址</span>
							<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;
							<span> ${purchaser.purProvince }省 &nbsp;&nbsp;&nbsp;${purchaser.purCity } 市 </span>
					
				</div>
				--%>
				<div class="borde">
							<span>&nbsp;&nbsp;经营地址</span>
							<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span>${purchaser.purAddress}</span>
					
				</div>

				<div class="borde">
							<span>&nbsp; 公司邮编</span>
							<span class="red">: </span>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span>${purchaser.purPostalcode}</span>
					
				</div>
				<c:if test="${purchaser.purIsvendot=='yes'}">
				<div id="venderstyle">
					<div class="borde">
								<span>&nbsp;&nbsp;公司简称</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span>${purchaser.vender.venShortname}</span>
							
					</div>

					<div class="borde">
								<span>&nbsp;&nbsp;营业执照</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span>${purchaser.vender.venShopcard}</span>						
					</div>

					<div class="borde">
								<span>&nbsp;&nbsp;公司传真</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span>${purchaser.vender.venFax}</span>							
					</div>

					<div class="borde">
								<span>&nbsp;&nbsp;公司人员</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;
								<span>${purchaser.vender.venLinkman}</span>
						
					</div>

					<div class="borde">
								<span>&nbsp;&nbsp;联系电话</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;
								<span>${purchaser.vender.venLinkmanphone}</span>
						
					</div>

					<div class="borde">
								<span>&nbsp;&nbsp;公司邮箱</span>
								<span class="red">: </span> &nbsp;
								<span>${purchaser.vender.venEmail}</span>
						
					</div>

					<div class="borde"><span>&nbsp; 申请情况</span>
								<span class="red">: </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><c:choose>
    			<c:when test="${purchaser.vender.venStatus==-1}">
    				审核未通过
    			</c:when>    			
    		</c:choose>
    		<c:choose>
    			<c:when test="${purchaser.vender.venStatus==0}">
    				未审核
    			</c:when>    			
    		</c:choose>
    		<c:choose>
    			<c:when test="${purchaser.vender.venStatus==1}">
    				审核已通过
    			</c:when>    			
    		</c:choose>
								</span>						
					</div>
				</div>
				</c:if> 
				
				<div class="borde" style="margin-left: 150px;margin-top: 20px">
					<a href="userupdate.jsp"><img src="images/common/modify.jpg" alt="修改信息"/></a>
				</div>
				</div>
			</center>
				
				</div>
		</div>
	</div>
	
	
	</body>
</html>
<!-- 底部页面开始 -->
<%@ include file="footer.jsp" %>
<!-- 底部页面结束 -->