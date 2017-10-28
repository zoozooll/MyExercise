<%@ page language="java" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
	<head>
		<title>个人资料</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
		<link rel="stylesheet" href="css/common.css" type="text/css" />
	</head>
	<body>
		<!--<body>-->
		<center>
			<h3>
				管理员个人资料
			</h3>
			<div id="man_zone">
				<table width="99%" border="0" align="center" cellpadding="3"
					cellspacing="1" class="table_style">
					<tr>
						<td class="left_title_2">
							用户类型:
						</td>
						<td>
							超级管理员
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							管理员名称:
						</td>
						<td>
							${admin.adminName }
						</td>
					</tr>
					<tr>
						<td class="left_title_2">
							管理员密码:
						</td>
						<td>
							${admin.adminPassword}
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							管理员住址:
						</td>
						<td>
							${admin.adminAddress }
						</td>
					</tr>
					<tr>
						<td class="left_title_2">
							管理员身份证:
						</td>
						<td>
							${admin.adminIdcard }
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							管理员联系电话:
						</td>
						<td>
							${admin.adminPhone }
						</td>
					</tr>
					<tr>
						<td class="left_title_2">
							管理员电子邮件:
						</td>
						<td>
							${admin.adminEmail }
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							管理员备注:
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