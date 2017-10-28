<%@ page language="java" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="head.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
	<HEAD>
		<TITLE></TITLE>
		<META http-equiv=Content-Type content="text/html; charset=gb2312">
		<link href="css/register.css" rel="stylesheet" type="text/css">
		<!-- ����֤ -->
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
			<span style="text-align: left;">���������ڵ�λ����:Today&gt;&gt;<a href="index.jsp">��ҳ</a>&gt;&gt;<a href="findAllPurchasersAction.action">��������</a>&gt;&gt;<a style="color:red;font-weight: bold;">���������޸�</a>&gt;&gt;</span>			
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
									<font>�����Ϊ����</font>
								</LI>
							</ul>
						</DIV>

						<form action="purchasertoVenderAction.action" method="post" name="reg"
					class='required-validate'>
					<div style="display: none;">
					<div class="stylediv">
						<ul>
							<li style="float:left; font:bolder;">
								<span>&nbsp;&nbsp;��˾����:</span>
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
								<span>&nbsp;&nbsp;��˾����:</span>
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
								<span>&nbsp;&nbsp;��˾�绰:</span>
								 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT maxLength=20 size=32 name="purTelephone" value="${purchaser.purTelephone}"
										class="required validate-phone"> </span>
							</li>
						</ul>
					</div>

					<div class="stylediv">
						<ul>
							<li style="float:left; font:bolder">
								<span>&nbsp;&nbsp;��˾��ַ:</span>
								 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="text" name="purProvince" value="${purchaser.purProvince}" class="required"/>
									
								<input type="text" name="purCity" value="${purchaser.purCity}" class="required"/>									
							</li>
						</ul>
					</div>

					<div class="stylediv">
						<ul>
							<li style="float:left; font:bolder">
								<span>&nbsp;&nbsp;��Ӫ��ַ:</span>
								 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT maxLength=100 size=32 name="purAddress" value="${purchaser.purAddress}"
										class="required"> </span>
							</li>
						</ul>
					</div>

					<div class="stylediv">
						<ul>
							<li style="float:left; font:bolder">
								<span>&nbsp;&nbsp;��������:</span>
								 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT maxLength=100 size=32 name="purPostalcode" value="${purchaser.purPostalcode}"
										class="required validate-zip"> </span>
							</li>
						</ul>
					</div>
					<div class="stylediv">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;��˾��ע:</span>
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
									<span>&nbsp;&nbsp;��˾���:</span>
									 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<span><INPUT maxLength=100 size=32 name="venShortname" value="${purchaser.vender.venShortname}"
											class='required'> </span>
								</li>
							</ul>
						</div>

						<div class="stylediv">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;Ӫҵִ��:</span>
									 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<span><INPUT maxLength=100 size=32 name="venShopcard" value="${purchaser.vender.venShopcard}"
											class='required' > </span>
								</li>
							</ul>
						</div>

						<div class="stylediv">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;��˾����:</span>
									 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<span><INPUT maxLength=100 size=32 name="venFax" value="${purchaser.vender.venFax}"
											class="required validate-phone"> </span>
								</li>
							</ul>
						</div>

						<div class="stylediv">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;��˾��Ա:</span> 
									 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
									<span><INPUT maxLength=100 size=32 name="venLinkman" value="${purchaser.vender.venLinkman}"
											class="required"> </span>
								</li>
							</ul>
						</div>

						<div class="stylediv">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;��ϵ�绰:</span> 
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
									<span>&nbsp;&nbsp;��˾����:</span> 
									 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
									<span><INPUT maxLength=100 size=32 name="venEmail" value="${purchaser.vender.venEmail}"
											class='required validate-email'> </span>
								</li>
							</ul>
						</div>						
					<DIV style="margin: 10px;">
						<INPUT style="FONT-SIZE: 14px; HEIGHT: 30px" type="submit"
							class="button" value="�޸��û�">
					</DIV>
				</form>						
				</center>
			</div>
				</div>
		</div>
	</BODY>
</HTML>
<%@ include file="footer.jsp"%>