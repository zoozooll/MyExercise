<%@ page language="java" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
	<head>
		<title>��������</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
		<link rel="stylesheet" href="css/common.css" type="text/css" />
	</head>
	<body>		
		<center>
			<h3>
				����Ա��������
			</h3>
			<div id="man_zone">
			<form action="modifyAdmin.action?adminId=${admin.adminId}" method="post">
				<table width="99%" border="0" align="center" cellpadding="3"
					cellspacing="1" class="table_style">					
					<tr>
						<td class="left_title_1">
							����Ա����:
						</td>
						<td>
							<input type="text" name="adminName" value="${admin.adminName }"/>
						</td>
					</tr>
					<tr>
						<td class="left_title_2">
							����Ա����:
						</td>
						<td>
							<input type="text" name="adminPassword" value="${admin.adminPassword}"/>
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							����Աסַ:
						</td>
						<td>
							<input type="text" name="adminAddress" value="${admin.adminAddress }"/>
						</td>
					</tr>
					<tr>
						<td class="left_title_2">
							����Ա���֤:
						</td>
						<td>
							<input type="text" name="adminIdcard" value="${admin.adminIdcard }"/>
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							����Ա��ϵ�绰:
						</td>
						<td>
							<input type="text" name="adminPhone" value="${admin.adminPhone }"/>
						</td>
					</tr>
					<tr>
						<td class="left_title_2">
							����Ա�����ʼ�:
						</td>
						<td>
							<input type="text" name="adminEmail" value="${admin.adminEmail }"/>
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							����Ա��ע:
						</td>
						<td>
							<textarea rows="5" cols="40" name="adminRemark">${admin.adminRemark }</textarea>
						</td>
					</tr>
					<tr>
						<td style="text-align: center;" colspan="2">
							<input type="submit" value="�ύ">
						</td>
					</tr>
				</table>
				</form>
			</div>
		</center>
	</body>
</html>