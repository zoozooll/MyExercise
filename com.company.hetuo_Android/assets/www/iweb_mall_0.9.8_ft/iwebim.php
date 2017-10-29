<?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require("foundation/asession.php");
require("configuration.php");
require("foundation/fsession.php");

$user_id = get_sess_user_id();

if(!$user_id) {
	echo '<script>alert("请先登陆！");location.href="login.php"</script>';
	exit;
}

if(!$im_enable) {
	echo '<script>location.href="index.php"</script>';
	exit;
}
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户中心</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="iweb IM" />
<meta name="description" content="您正在使用iweb IM !" />
</head>
<body style="padding:0px; margin:0px; border:0px;">
<div style="padding:0px; margin:0px auto; border:1px; solid #ccc; width:990px">
<iframe src="modules.php" name="imall_modules" id="imall_modules" style="width:990px; height:1000px; border:0; margin:0px;" frameborder="0" marginwidth="0" marginheight="0" onload="document.getElementById('imall_modules').style.height=imall_modules.document.body.scrollHeight+20+'px'"></iframe>
</div>
<script src='im/im_formall_js.php'></script>
<script language="JavaScript">
<!--
var tempuid = 0;
function newimopen() {
	var url = location.href;
	var uid = url.replace(/[^#]+#/,'');
	uid = parseInt(uid);
	if(uid != tempuid && uid>0) {
		tempuid = uid;
		i_im_talkWin(uid,'imWin');
		alert("好友聊天窗口正在打开！");
		location.href = "iwebim.php#";
	}
}
setInterval("newimopen()",'1000');
//-->
</script>
</body>
</html>