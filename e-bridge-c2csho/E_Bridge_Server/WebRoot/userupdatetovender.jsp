<%@ page language="java" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="head.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
	<HEAD>
		<TITLE></TITLE>
		<META http-equiv=Content-Type content="text/html; charset=gb2312">
		<link href="css/register.css" rel="stylesheet" type="text/css">
		<!-- 表单验证 -->
		<script src="js/prototype.js" type="text/javascript"></script>
		<script src="js/validation_cn.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" href="js/style_min.css" />
		
		
	
<style type="text/css">
	.stylediv{
		margin-top: 15px;
		display: block;	
		width:500px;
	}
	span {
	display: inline;
}
</style>

	</HEAD>
<BODY>
		<div id="container" style="height: auto">
			<span style="text-align: left;">您现在所在的位置是:Today&gt;&gt;<a href="index.jsp">首页</a>&gt;&gt;<a href="findAllPurchasersAction.action">个人中心</a>&gt;&gt;<a style="color:red;font-weight: bold;">个人资料修改</a>&gt;&gt;</span>			
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
						<DIV>
							<ul>
								<LI>
									<font>申请成为卖家</font>
								</LI>
							</ul>
						</DIV>

						<form action="purchasertoVenderAction.action" method="post" name="reg"
					class='required-validate'>
					<div style="display: none;">
					<div class="stylediv">
						<ul>
							<li style="float:left; font:bolder;">
								<span>&nbsp;&nbsp;公司名称:</span>
								 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT maxLength=20 size=32 name="purName" value="${purchaser.purName}"
									class="required min-length-4 max-length-20" readonly="readonly"></span>
								<span id="mes"></span>
							</li>
						</ul>
					</div>

					<div class="stylediv">
						<ul>
							<li style="float:left; font:bolder">
								<span>&nbsp;&nbsp;公司密码:</span>
								 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT type="text" maxLength=20 size=32 value="${purchaser.purPassword}"
										name="purPassword" class="required min-length-6 max-length-20">
								</span>
							</li>
						</ul>
					</div>

					<div class="stylediv">
						<ul>
							<li style="float:left; font:bolder">
								<span>&nbsp;&nbsp;公司电话:</span>
								 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT maxLength=20 size=32 name="purTelephone" value="${purchaser.purTelephone}"
										class="required validate-phone"> </span>
							</li>
						</ul>
					</div>

					<div class="stylediv">
						<ul>
							<li style="float:left; font:bolder">
								<span>&nbsp;&nbsp;公司地址:</span>
								 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="text" name="purProvince" value="${purchaser.purProvince}" class="required"/>
									
								<input type="text" name="purCity" value="${purchaser.purCity}" class="required"/>									
							</li>
						</ul>
					</div>

					<div class="stylediv">
						<ul>
							<li style="float:left; font:bolder">
								<span>&nbsp;&nbsp;经营地址:</span>
								 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT maxLength=100 size=32 name="purAddress" value="${purchaser.purAddress}"
										class="required"> </span>
							</li>
						</ul>
					</div>

					<div class="stylediv">
						<ul>
							<li style="float:left; font:bolder">
								<span>&nbsp;&nbsp;邮政编码:</span>
								 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT maxLength=100 size=32 name="purPostalcode" value="${purchaser.purPostalcode}"
										class="required validate-zip"> </span>
							</li>
						</ul>
					</div>
					<div class="stylediv">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;公司备注:</span>
								 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT maxLength=100 size=32 name="purRemark" value="${purchaser.purRemark}"
										class="required"> </span>
							</li>
						</ul>
					</div>	
					</div>
					
						<div class="stylediv">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;公司简称:</span>
									 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<span><INPUT maxLength=100 size=32 name="venShortname" value="${purchaser.vender.venShortname}"
											class='required'> </span>
								</li>
							</ul>
						</div>

						<div class="stylediv">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;营业执照:</span>
									 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<span><INPUT maxLength=100 size=32 name="venShopcard" value="${purchaser.vender.venShopcard}"
											class='required' > </span>
								</li>
							</ul>
						</div>

						<div class="stylediv">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;公司传真:</span>
									 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<span><INPUT maxLength=100 size=32 name="venFax" value="${purchaser.vender.venFax}"
											class="required validate-phone"> </span>
								</li>
							</ul>
						</div>

						<div class="stylediv">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;公司人员:</span> 
									 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
									<span><INPUT maxLength=100 size=32 name="venLinkman" value="${purchaser.vender.venLinkman}"
											class="required"> </span>
								</li>
							</ul>
						</div>

						<div class="stylediv">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;联系电话:</span> 
									 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
									<span><input maxLength=100 size=32	name="venLinkmanphone" value="${purchaser.vender.venLinkmanphone}"
									class='required validate-mobile-phone'>
									</span>
								</li>
							</ul>
						</div>

						<div class="stylediv">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;公司邮箱:</span> 
									 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
									<span><INPUT maxLength=100 size=32 name="venEmail" value="${purchaser.vender.venEmail}"
											class='required validate-email'> </span>
								</li>
							</ul>
						</div>						
					<DIV style="margin: 10px;">
						<INPUT style="FONT-SIZE: 14px; HEIGHT: 30px" type="submit"
							class="button" value="修改用户">
					</DIV>
				</form>						
				</center>
			</div>
				</div>
		</div>
	</BODY>
</HTML>
<%@ include file="footer.jsp"%>