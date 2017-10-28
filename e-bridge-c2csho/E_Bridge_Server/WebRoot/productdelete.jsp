<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="head.jsp"%>

<%-- 删除产品页面 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>巨匠电子</title>
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
			<span style="text-align: left;">您现在所在的位置是:Today&gt;&gt;<a
				href="index.jsp">首页</a>&gt;&gt;<a
				href="myshopproductlist.jsp">产品中心</a>&gt;&gt;<a
				style="color: red; font-weight: bold;">删除产品</a>&gt;&gt;</span>
			<jsp:include page="mycompanyleft.jsp"></jsp:include>
			<div id="main"
				style="border: 1px solid red; border-top: none; padding-bottom: 50px;">				
						<div id="latest" style="height: 240px; top: 67px; left: 152px;">
					<div class="pdelete">	<center>						
						<div class="userlist"><form action="delProduct.action" method="post">
					<input type="text" name="porId"
						style="font-size: 14px; height: 25px; width: 200px; margin-left: 30px;"/>
					<select name="typeName"
						style="font-size: 14px; width: 140px; margin-left: 30px;margin-top: 0px;">
						<option value="purName">
							按名称删除
						</option>
						<option value="purId" selected="selected">
							按ID删除
						</option>						
					</select>
					<input
						style="font-size: 14px; height: 30px; width: 80px; margin-left: 30px;"
						type="submit" value="确认"/>
				</form>
		</div>
					<div style="margin-top: 30px;">
						
						<c:set var="flag" value="true" />
						<p/>
						<p/>
						<table class="datalist" width="100%;">							
							<tr>
							<th scope="col">
									产品编号
								</th>
							<th scope="col">
									产品图片预览
								</th>								
								<th scope="col">
									产品名称
								</th>
								<th scope="col">
									产品价格
								</th>
								<th scope="col">
									产品库存
								</th>	
								<th scope="col">
									产品操作
								</th>								
							</tr>
							<c:forEach var="pro" items="${purchaser.vender.productgroup.products}">
								<c:if test="${purchaser.purIsvendot=='yes'}">
									<c:choose>
										<c:when test="${flag==true}">
											<tr>
												<td>
													${pro.proId}
												</td>
												<td>
													<img src="${pro.proImagepath}" style="width: 30%;"/>
												</td>
												<td>
													<a href="myshopshowProduct.action?proId=${pro.proId}">${pro.proName}</a>	
												</td>
												<td>
												${pro.proPrice}
												</td>
												<td>
													${pro.stock.stoAmount}${pro.proUnit}
												</td>
												<td>
													<a href="#"><img src="images/common/delete.jpg" alt="删除 " style="width:50px;height:18px;"/></a>
								<a href="#"><img src="images/common/modify.jpg" alt="修改 " style="width:50px;height:18px;"/></a>	
												</td>
												
											</tr>
											<c:set var="flag" value="false" />
										</c:when>
										<c:otherwise>
											<tr class="altrow">
													<td>
													${pro.proId}
												</td>
												<td>
													<img src="${pro.proImagepath}" style="width: 30%;"/>
												</td>
												<td>
													<a href="myshopshowProduct.action?proId=${pro.proId}">${pro.proName}</a>	
												</td>
												<td>
												${pro.proPrice}
												</td>
												<td>
													${pro.stock.stoAmount}${pro.proUnit}
												</td>
												<td>
													<a href="#"><img src="images/common/delete.jpg" alt="删除 " style="width:50px;height:18px;"/></a>
								<a href="#"><img src="images/common/modify.jpg" alt="修改 " style="width:50px;height:18px;"/></a>	
												</td>
											</tr>
											<c:set var="flag" value="true" />
										</c:otherwise>
									</c:choose>
								</c:if>
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
