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
		<!--<body>-->
		<center>
			<h3>
				����Ա��������
			</h3>
			<div id="man_zone">
				<table width="99%" border="0" align="center" cellpadding="3"
					cellspacing="1" class="table_style">
					<tr>
						<td class="left_title_2">
							�û�����:
						</td>
						<td>
							��������Ա
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							����Ա����:
						</td>
						<td>
							${admin.adminName }
						</td>
					</tr>
					<tr>
						<td class="left_title_2">
							����Ա����:
						</td>
						<td>
							${admin.adminPassword}
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							����Աסַ:
						</td>
						<td>
							${admin.adminAddress }
						</td>
					</tr>
					<tr>
						<td class="left_title_2">
							����Ա���֤:
						</td>
						<td>
							${admin.adminIdcard }
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							����Ա��ϵ�绰:
						</td>
						<td>
							${admin.adminPhone }
						</td>
					</tr>
					<tr>
						<td class="left_title_2">
							����Ա�����ʼ�:
						</td>
						<td>
							${admin.adminEmail }
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							����Ա��ע:
						</td>
						<td>
							${admin.adminRemark }
						</td>
					</tr>
				</table>
			</div>
		</center>
	</body>
</html>