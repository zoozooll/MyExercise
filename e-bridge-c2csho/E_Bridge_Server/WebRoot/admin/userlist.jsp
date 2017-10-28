<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
		<link rel="stylesheet" type="text/css" href="css/adminmain.css" />
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<style type="text/css">

caption {
	font-size: 18px;
}
</style>
		<script type="text/javascript">
	function show1(obj) {
		var elem = document.getElementById(obj);
		var x = window.event.x;
		var y = window.event.y;
		
		elem.style.visibility = 'visible';
		elem.style.position = 'absolute';
		
		elem.style.FILTER = 'ALPHA(opacity=opacity,finishopacity=finishopacity,style=style,startx=startx,starty=starty,finishx=finishx,finishy=finishy)'
		//alert(elem.style.position);
		elem.style.left = 400;
		elem.style.top = 150;

	}

	function hide(obj) {
		var elem = document.getElementById(obj);
		elem.style.visibility = 'hidden';
	}
</script>

	</head>

	<body>
		<center>
			<div>
				<form action="adminQueryUserBlur.action" method="post">
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
				<table class="datalist" style="width: 100%;">
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
							�û���˾��ע
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

				<table class="datalist" style="width: 100%;">
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
						<th scope="col">
							���
						</th>
						<th scope="col">
							����Ա����
						</th>
					</tr>

					<c:forEach var="pur_ven" items="${purchasers}">
						<c:set var="param3" value="${pur_ven.purId}"></c:set>
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
										<td>
										<a
												href="admindeleteUser.action?purId=${pur_ven.purId}" style="color: red;">ɾ��</a>
											
											<a
												href="adminFindVenById.action?purId=${pur_ven.purId}" style="color: red;">�޸�</a>
											
											<a onclick="show1('aa${pur_ven.purId}');" style="color: red;">���
											</a>
											<div id="aa${pur_ven.purId}"
												style="visibility: hidden; position: absolute; border: 1px dotted red; background-color: white; width: 200px; height: 200px;">
												<div style="width: 100%;text-align: right;background-color: red;">
													<a onclick="hide('aa${pur_ven.purId}');" style="color: red;"><img src="images/close.gif" alt="�ر�" style="border: none;"/></a>
												</div>
												<div>
													<div id="auditNo" style="margin: 30px;float: left;"><a href="adminmodifyVenStatus.action?venStatus=${para1}&venId=${pur_ven.vender.venId}">
															<img src="images/audiNo.gif" alt="��˲�ͨ��" style="border: none;"/></a>	</div>												
													<div id="auditYes" style="margin: 30px;float: left;">	<a
															href="adminmodifyVenStatus.action?venStatus=${para2}&venId=${pur_ven.vender.venId}"><img src="images/audiYes.gif" alt="���ͨ��" style="border: none;"/></a></div>
												</div>
											</div>
											
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
										<td>
											<a
												href="admindeleteUser.action?purId=${pur_ven.purId}" style="color: red;">ɾ��</a>
											
											<a
												href="adminFindVenById.action?purId=${pur_ven.purId}" style="color: red;">�޸�</a>
											
											<a onclick="show1('aa${pur_ven.purId}');" style="color: red;">���
											</a>
												<div id="aa${pur_ven.purId}"
												style="visibility: hidden; position: absolute; border: 1px dotted red; background-color: white; width: 200px; height: 200px;">
												<div style="width: 100%;text-align: right;background-color: red;">
													<a onclick="hide('aa${pur_ven.purId}');" style="color: red;"><img src="images/close.gif" alt="�ر�" style="border: none;"/></a>
												</div>
												<div>
													<div id="auditNo" style="margin: 30px;float: left;"><a href="adminmodifyVenStatus.action?venStatus=${para1}&venId=${pur_ven.vender.venId}">
															<img src="images/audiNo.gif" alt="��˲�ͨ��" style="border: none;"/></a>	</div>												
													<div id="auditYes" style="margin: 30px;float: left;">	<a
															href="adminmodifyVenStatus.action?venStatus=${para2}&venId=${pur_ven.vender.venId}"><img src="images/audiYes.gif" alt="���ͨ��" style="border: none;"/></a></div>
												</div>
											</div>																		
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