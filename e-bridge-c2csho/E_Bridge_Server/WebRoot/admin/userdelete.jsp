<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" type="text/css" href="css/adminmain.css" />
	
</style>
	</head>

	<body>
		<center>
			<h2>
				�û��б�
			</h2>
			<div>
			<form action="admindeleteUserBlur.action" method="post">
				<input type="text" name="delcondition"
					style="font-size: 14px; height: 25px; width: 200px; margin-left: 30px;">
				<select name="typeName"
					style="font-size: 14px; height: 2px; width: 120px; margin-left: 30px;">
					<option value="purName">
						������ɾ��
					</option>
					<option value="purId" selected="selected">
						��IDɾ��
					</option>					
				</select>
				<input
					style="font-size: 14px; height: 30px; width: 80px; margin-left: 30px;"
					type="submit" value="ȷ��">
					</form>
			</div>
			<div>
			<table class="datalist" width="100%">
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
			
			<table class="datalist" width="100%">
				<caption>
					�����б�
				</caption>
				<tr>
					<th scope="col">
						�û����
					</th>
					<th scope="col">
						�û�����
					</th>
									
					<th scope="col">
						��˾Ӫҵִ��
					</th>
					<th scope="col">
						��˾��ϵ��
					</th>
					<th scope="col">
						��˾��ϵ�˵绰
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
										${pur_ven.vender.venShopcard}
									</td>
									<td>
										${pur_ven.vender.venLinkman}
									</td>
									<td>
										${pur_ven.vender.venLinkmanphone}
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
										${pur_ven.vender.venShopcard}
									</td>
									<td>
										${pur_ven.vender.venLinkman}
									</td>
									<td>
										${pur_ven.vender.venLinkmanphone}
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
	</body>
</html>

