<?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;
require("../foundation/asession.php");
require("../configuration.php");
require("includes.php");
//引入语言包
$a_langpackage=new adminlp;
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><?php echo $a_langpackage->a_admin_system; ?></title>
<meta http-equiv="Content-Type"	content="text/html;	charset=utf-8"	/>
<script language="JavaScript">
<!--
if (window.top != window) {
	window.top.location.href = document.location.href;
}
//-->
</script>
<frameset rows="100,*" framespacing="0" border="0">
	<frame src="m.php?app=top" id="header-frame" name="header-frame" frameborder="no" scrolling="no">
	<frameset cols="180,*"	framespacing="0" border="0"	id="frame-body">
		<frame src="m.php?app=menu&value="	id="menu-frame"	name="menu-frame" frameborder="no" scrolling="no">
		<frame src="m.php?app=main"	id="main-frame"	name="main-frame" frameborder="no" scrolling="yes">
	</frameset>
</frameset>
</head>
<body>

</body>
</html>