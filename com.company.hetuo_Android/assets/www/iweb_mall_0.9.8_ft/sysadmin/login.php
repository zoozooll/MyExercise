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
<link rel="stylesheet" type="text/css" media="all" href="skin/css/login.css" />
<title><?php echo $a_langpackage->a_admin_system; ?></title>
<meta http-equiv="Content-Type"	content="text/html;	charset=utf-8" />
</head>
<style>
td {font-size:12px; line-height:18px;}
</style>
<script type='text/javascript' src='../servtools/md5.js'></script>
<body>
<div id="login">
	<div class="warp">
    	<div class="content">
            <h1></h1>
            <form action="a.php?act=login" method="post" onsubmit="return checkform();">
            <div class="item"><div class="input"><div class="icon" title="<?php echo $a_langpackage->a_user; ?>"></div><input tabindex="1" type="text" name="admin_name" value=""  id="admin_name" /></div><label><?php echo $a_langpackage->a_user; ?>：</label></div>
            <div class="item"><div class="input"><div class="icon2" title="<?php echo $a_langpackage->a_password; ?>"></div><input tabindex="2" type="password" name="admin_password" value="" id="admin_password"/></div><label><?php echo $a_langpackage->a_password; ?>：</label></div><input type="submit" tabindex="3" value="" class="submit" />
            </form>
            <p class="copyright">Powered by <?php echo $SYSINFO['sys_name'] ?> <?php echo $SYSINFO['version'] ?> <?php echo $SYSINFO['sys_copyright'] ?></p>
        </div>
    </div>
</div>
<script language="JavaScript">
<!--
function checkform() {
	var admin_name = document.getElementById("admin_name").value;
	if(!admin_name) {
		alert('<?php echo $a_langpackage->a_admin_name_none;?>');
		return false;
	}
	var admin_password = document.getElementById("admin_password").value;
	if(admin_password) {
		document.getElementById("admin_password").value = MD5(admin_password);
		return true;
	} else {
		alert('<?php echo $a_langpackage->a_admin_password;?>');
		return false;
	}
}
//-->
</script>
</body>
</html>