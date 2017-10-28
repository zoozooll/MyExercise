<%@ page language="java" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
	<head>
		<title>个人资料</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
		<link rel="stylesheet" href="css/common.css" type="text/css" />
		<script type="text/javascript">
	function tech() {
		if (document.getElementById("ap").value != document
				.getElementById("arp").value) {
			document.getElementById("repassword").innerHTML = "再次密码输入不一致!";
			return false;
		}
		return true;
	}

	function subm() {
		return tech();
	}
</script>
	</head>
	<body>
		<center>
			<h3>
				管理员修改密码
			</h3>
			<div id="man_zone">
				<form action="adminmodifyAdminPassword.action" method="post"
					onsubmit="return subm();">
					<table width="99%" border="0" align="center" cellpadding="3"
						cellspacing="1" class="table_style">
						<tr>
							<td class="left_title_2">
								原密码:
							</td>
							<td>
								<input type="password" name="adminBeforePassword" />
								<span>${message}</span>
							</td>
						</tr>
						<tr>
							<td class="left_title_1">
								现密码:
							</td>
							<td>
								<input type="password" id="ap" name="adminPassword" />
								<span id="repassword"></span>
							</td>
						</tr>
						<tr>
							<td class="left_title_2">
								确认密码:
							</td>
							<td>
								<input type="password" id="arp" name="adminRePassword"
									onblur="tech();" />
							</td>
						</tr>
						<tr>
							<td colspan="2" style="text-align: center;">
								<input type="submit" value="修改密码" />
							</td>
						</tr>
					</table>
				</form>
			</div>
		</center>
	</body>
</html>