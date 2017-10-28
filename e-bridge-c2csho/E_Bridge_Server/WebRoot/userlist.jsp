<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="head.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" type="text/css" href="css/global.css" />

		<style type="text/css">
		.userlist input{
			border:1px solid black;
		}
</style>
	</head>
	<body>
		<div id="container" style="height: auto">
			<span style="text-align: left;">您现在所在的位置是:Today&gt;&gt;<a
				href="index.jsp">首页</a>&gt;&gt;<a
				href="findAllPurchasersAction.action">个人中心</a>&gt;&gt;<a
				style="color: red; font-weight: bold;">用户列表</a>&gt;&gt;</span>
				<c:choose>
					<c:when test="${purchaser.purIsvendot=='yes'&&purchaser.vender.venStatus==1}">
			<jsp:include page="mycompanyleft.jsp"></jsp:include>
			</c:when>
			<c:when test="${purchaser.purIsvendot=='no'}"><jsp:include page="purleft.jsp"></jsp:include></c:when>
			<c:otherwise><jsp:include page="indexleft.jsp"></jsp:include></c:otherwise>
			</c:choose>
			<div id="main"
				style="border: 1px solid red; border-top: none; padding-bottom: 50px;">
				<div id="latest" style="height: 240px; top: 67px; left: 152px;">
					<center>
						<h3>
							用户列表
						</h3>
						<div class="userlist"><form action="adminQueryUserBlur.action" method="post">
					<input type="text" name="condition"
						style="font-size: 14px; height: 25px; width: 200px; margin-left: 30px;">
					<select name="typeName"
						style="font-size: 14px; height: 2px; width: 140px; margin-left: 30px;">
						<option value="purName">
							按名称查找
						</option>
						<option value="purId">
							按ID查找
						</option>
						<option value="purNameBlur" selected="selected">
							按模糊名称查找
						</option>						
					</select>
					<input
						style="font-size: 14px; height: 30px; width: 80px; margin-left: 30px;"
						type="submit" value="确认">
				</form>
		</div>
					<div>
						<table class="datalist" summary="list of members in EE Studay"
							width="100%;" style="margin: 50px 0px;">
							<caption>
								买家列表
							</caption>
							<tr>
								<th scope="col">
									用户编号
								</th>
								<th scope="col">
									用户名称
								</th>
								<th scope="col">
									用户住址
								</th>
								<th scope="col">公司备注 
								</th>
								<th scope="col">
									是否是经销商
								</th>
							</tr>
							<c:set var="flag" value="true" />
							<c:forEach var="pur" items="${purchasers}">
								<c:if test="${pur.purIsvendot=='no'}">
									<c:choose>
										<c:when test="${flag==true}">
											<tr>
												<td>
													${pur.purId}
												</td>
												<td>
													<a href="adminFindUserByName.action?purName=${pur.purName}">${pur.purName}</a>
												</td>
												<td>
													${pur.purAddress}
												</td>
												<td>
													${pur.purRemark}
												</td>
												<td>
													${pur.purIsvendot}
												</td>
											</tr>
											<c:set var="flag" value="false" />
										</c:when>
										<c:otherwise>
											<tr class="altrow">
												<td>
													${pur.purId}
												</td>
												<td>
													<a href="adminFindUserByName.action?purName=${pur.purName}">${pur.purName}</a>
												</td>
												<td>
													${pur.purAddress}
												</td>
												<td>
													${pur.purRemark}
												</td>
												<td>
													${pur.purIsvendot}
												</td>
											</tr>
											<c:set var="flag" value="true" />
										</c:otherwise>
									</c:choose>
								</c:if>
							</c:forEach>
						</table>


						<c:set var="flag" value="true" />
						<c:set var="para1" value="-1" />
						<c:set var="para2" value="1" />

						<p>
						<p>
						<table class="datalist" width="100%;">
							<caption>
								卖家列表
							</caption>
							<tr>
								<th scope="col">编号
								</th>
								<th scope="col">名称 
								</th>
								<th scope="col">简称 
								</th>
								<th scope="col">执照 
								</th>
								<th scope="col">
									公司人员
								</th>
								<th scope="col">
									联系电话
								</th>
								<th scope="col">状态 
								</th>
							</tr>
							<c:forEach var="pur_ven" items="${purchasers}">
								<c:if test="${pur_ven.purIsvendot=='yes'}">
									<c:choose>
										<c:when test="${flag==true}">
											<tr>
												<td>
													${pur_ven.purId}
												</td>
												<td>
													<a
														href="adminFindUserByName.action?purName=${pur_ven.purName }">${pur_ven.purName}</a>
												</td>
												<td>
													${pur_ven.vender.venShortname}
												</td>
												<td>
													${pur_ven.vender.venShopcard}
												</td>
												<td>
													${pur_ven.vender.venLinkman}
												</td>
												<td>
													${pur_ven.vender.venLinkmanphone}
												</td>
												<td>
													<c:choose>
														<c:when test="${pur_ven.vender.venStatus==-1}">
		    				审核未通过
		    			</c:when>
														<c:when test="${pur_ven.vender.venStatus==0}">
		    				未审核
		    			</c:when>
														<c:when test="${pur_ven.vender.venStatus==1}">
		    				已审核
		    			</c:when>
													</c:choose>
												</td>
											</tr>
											<c:set var="flag" value="false" />
										</c:when>
										<c:otherwise>
											<tr class="altrow">
												<td>
													${pur_ven.purId}
												</td>
												<td>
													<a
														href="adminFindUserByName.action?purName=${pur_ven.purName }">${pur_ven.purName}</a>
												</td>
												<td>
													${pur_ven.vender.venShortname}
												</td>
												<td>
													${pur_ven.vender.venShopcard}
												</td>
												<td>
													${pur_ven.vender.venLinkman}
												</td>
												<td>
													${pur_ven.vender.venLinkmanphone}
												</td>
												<td>
													<c:choose>
														<c:when test="${pur_ven.vender.venStatus==-1}">
		    				审核未通过
		    			</c:when>
														<c:when test="${pur_ven.vender.venStatus==0}">
		    				未审核
		    			</c:when>
														<c:when test="${pur_ven.vender.venStatus==1}">
		    				已审核
		    			</c:when>
													</c:choose>
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
	</body>
</html>
<%@ include file="footer.jsp"%>