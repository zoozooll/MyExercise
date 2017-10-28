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
			<span style="text-align: left;">���������ڵ�λ����:Today&gt;&gt;<a
				href="index.jsp">��ҳ</a>&gt;&gt;<a
				href="findAllPurchasersAction.action">��������</a>&gt;&gt;<a
				style="color: red; font-weight: bold;">�û��б�</a>&gt;&gt;</span>
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
							�û��б�
						</h3>
						<div class="userlist"><form action="adminQueryUserBlur.action" method="post">
					<input type="text" name="condition"
						style="font-size: 14px; height: 25px; width: 200px; margin-left: 30px;">
					<select name="typeName"
						style="font-size: 14px; height: 2px; width: 140px; margin-left: 30px;">
						<option value="purName">
							�����Ʋ���
						</option>
						<option value="purId">
							��ID����
						</option>
						<option value="purNameBlur" selected="selected">
							��ģ�����Ʋ���
						</option>						
					</select>
					<input
						style="font-size: 14px; height: 30px; width: 80px; margin-left: 30px;"
						type="submit" value="ȷ��">
				</form>
		</div>
					<div>
						<table class="datalist" summary="list of members in EE Studay"
							width="100%;" style="margin: 50px 0px;">
							<caption>
								����б�
							</caption>
							<tr>
								<th scope="col">
									�û����
								</th>
								<th scope="col">
									�û�����
								</th>
								<th scope="col">
									�û�סַ
								</th>
								<th scope="col">��˾��ע 
								</th>
								<th scope="col">
									�Ƿ��Ǿ�����
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
								�����б�
							</caption>
							<tr>
								<th scope="col">���
								</th>
								<th scope="col">���� 
								</th>
								<th scope="col">��� 
								</th>
								<th scope="col">ִ�� 
								</th>
								<th scope="col">
									��˾��Ա
								</th>
								<th scope="col">
									��ϵ�绰
								</th>
								<th scope="col">״̬ 
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
		    				���δͨ��
		    			</c:when>
														<c:when test="${pur_ven.vender.venStatus==0}">
		    				δ���
		    			</c:when>
														<c:when test="${pur_ven.vender.venStatus==1}">
		    				�����
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
		    				���δͨ��
		    			</c:when>
														<c:when test="${pur_ven.vender.venStatus==0}">
		    				δ���
		    			</c:when>
														<c:when test="${pur_ven.vender.venStatus==1}">
		    				�����
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