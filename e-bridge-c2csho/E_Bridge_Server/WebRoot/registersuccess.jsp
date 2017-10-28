<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>欢迎进入电子商务网站</title>

		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="this is my page">
		<meta http-equiv="content-type" content="text/html; charset=GB18030">
		<!-- <meta http-equiv="refresh" content="5;url=http://localhost:8088/E_Bridge/ProductList">-->
		<meta http-equiv="refresh" content="5;url=welcome.jsp">
		<!--<link rel="stylesheet" type="text/css" href="./styles.css">-->
	</head>
	<body>
		<center>
			<br>
			<br>
			<b><font color="red">恭喜！注册用户成功</font>&nbsp;&nbsp; <span id="time"
				style="color: blue; font-size: 28px">5<font color="red">秒钟后自动跳转,如果不跳转,请点击下面的连接:
				</font> </span> </b>
		</center>
		<script language="JavaScript1.2" type="text/javascript">
	function delayURL(url) {
		var delay = document.getElementById("time").innerHTML;
		if (delay > 0) {
			delay--;
			document.getElementById("time").innerHTML = delay;
		} else {
			window.top.location.href = url;
		}

		setTimeout("delayURL('" + url + "')", 1000);
	}
</script>
		<span style="color: red;"><a href="index.jsp">&gt;&gt;&gt;&gt;&gt;</a>
		</span>
		<script type="text/javascript">
	delayURL("index.jsp");
</script>
	</body>
</html>

