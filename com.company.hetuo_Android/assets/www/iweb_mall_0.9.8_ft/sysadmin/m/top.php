<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//引入语言包
$a_langpackage=new adminlp;
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="skin/css/admin.css">
<script type='text/javascript' src="skin/js/jy.js"></script>
<script type='text/javascript'>

</script>
</head>
<body onload="nTabs()">
<div id="jywrap">
    <div id="jyhead">
        <div class="logo"></div>
        <div class="nav">
            <ul class="menu">
            <li class="active"><a href="m.php?app=menu&value=index" target="menu-frame" hidefocus="true"><?php echo $a_langpackage->a_global_settings; ?></a></li>
        	<li><a href="m.php?app=menu&value=application" target="menu-frame" hidefocus="true"><?php echo $a_langpackage->a_application_management; ?></a></li>
        	<li><a href="m.php?app=menu&value=member" target="menu-frame" hidefocus="true"><?php echo $a_langpackage->a_m_member_oprate; ?></a></li>
        	<li><a href="m.php?app=menu&value=shops" target="menu-frame" hidefocus="true"><?php echo $a_langpackage->a_m_shop_mengament; ?></a></li>
        	<li><a href="m.php?app=menu&value=order" target="menu-frame" hidefocus="true"><?php echo $a_langpackage->a_m_order_mengament; ?></a></li>
			<li><a href="m.php?app=menu&value=commodity" target="menu-frame" hidefocus="true"><?php echo $a_langpackage->a_m_aboutgoods_management; ?></a></li>
        	<li><a href="m.php?app=menu&value=ad" target="menu-frame" hidefocus="true"><?php echo $a_langpackage->a_m_asd_management; ?></a></li>
        	<li><a href="m.php?app=menu&value=article" target="menu-frame" hidefocus="true"><?php echo $a_langpackage->a_m_article_management; ?></a></li>
            </ul>
        </div>
		<div class="uinfo">
		<?php echo $_SESSION['admin_name']; ?><?php echo $a_langpackage->a_welcom_lastlogintime; ?>：<?php echo $_SESSION['admin_last_login']; ?> <br />
		<?php echo $a_langpackage->a_right_v; ?>：<?php echo $_SESSION['group_name']; ?> | <a href="../" target="_blank"><?php echo $a_langpackage->a_site_index; ?></a> | <a href="m.php?app=main" target="main-frame"><?php echo $a_langpackage->a_sysadmin_index; ?></a> | <a href="m.php?app=change_password" target="main-frame"><?php echo $a_langpackage->a_password_edit; ?></a> | <a href="a.php?act=logout" target="main-frame"><?php echo $a_langpackage->a_logout; ?></a>
		</div>
    </div>
    <div>&nbsp;</div>
</div>
</body>
</html>