<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="head.jsp" %>
<!doctype html public "-//w3c//dtd html 4.01 transitional//en">
<html>
	<head>
		<title>�޽�����</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="this is my page">		
		<meta http-equiv="content-type" content="text/html; charset=gb2312" />
			<style type="text/css">
			
		div form div table tr td input,span input{
					border-left-style: solid;
					border-width:1px;
					border-color: orange; 
					}
		</style>
	</head>

	<body>
			<div id="container">
				<div style="margin:3px 0px 0px 500px;border: 1px dotted orange;">
				<form name="loginform" action="loginAction.action" method="post">
					<input type="hidden" value="signin" name="action"/>
					<input type="hidden" value="anywhere" name="eventsubmit_dopost">
					<div style="padding:0px 0px 5px 20px;">
						<div style="padding:3px 0px 5px 10px;">
							<h3>�����ǻ�Ա�����ϼ��룺</h3>
						</div>
						<div style="height: 42px;padding:0px 0px 5px 10px;">
							<a href="register.jsp"><img src="image/post18.gif">
							</a>
						</div>
					</div>
					<div style="padding:0px 0px 5px 25px;">
						<div>
							<h3>�Ѿ��ǻ�Ա�����Ͻ��룺</h3>
						</div>
						<div align="center"><font color="red">
											${errorMsg}${message }
										</font></div>
						<div style="padding:0px 0px 5px 20px;">
							<table>
								<tbody>
									<tr>
										<td style="font: bolder;">
											&nbsp;&nbsp;&nbsp;&nbsp;��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;¼��
										</td>
										<td>
											<input tabindex="1" maxlength="32"
												name="purName">
										</td>
									</tr>
									<tr>
										<td height=43  style="font: bolder;">
											&nbsp;&nbsp;&nbsp;&nbsp;��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�룺
										</td>
										<td>
											<input tabindex="2" type="password"
												maxlength="32" name="purPassword">
										</td>
									</tr>
								</tbody>
							</table>
							<span style="padding-left: 120px;">
								<input class="signinbutton" tabindex="3" type="submit"
									value="��&nbsp;¼" name="submit">
							</span>
						</div>
						<div>
							<a href="login.html">���˵�¼����</a>|&nbsp;
							<a href="login.html">�������룿</a>|&nbsp;
							<a href="login.html">���ð�ȫ���룿</a>
						</div>
					</div>
				</form>
				</div>
			</div>		
	</body>
</html>
<%@ include file="footer.jsp"%>