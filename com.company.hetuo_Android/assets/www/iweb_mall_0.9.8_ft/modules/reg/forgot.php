<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/reg/forgot.html
 * 如果您的模型要进行修改，请修改 models/modules/reg/forgot.php
 *
 * 修改完成之后需要您进入后台重新编译，才会重新生成。
 * 如果您开启了debug模式运行，那么您可以省去上面这一步，但是debug模式每次都会判断程序是否更新，debug模式只适合开发调试。
 * 如果您正式运行此程序时，请切换到service模式运行！
 *
 * 如您有问题请到官方论坛（http://tech.jooyea.com/bbs/）提问，谢谢您的支持。
 */
?><?php
/*
 * 此段代码由debug模式下生成运行，请勿改动！
 * 如果debug模式下出错不能再次自动编译时，请进入后台手动编译！
 */
/* debug模式运行生成代码 开始 */
if(!function_exists("tpl_engine")) {
	require("foundation/ftpl_compile.php");
}
if(filemtime("templates/default/modules/reg/forgot.html") > filemtime(__file__) || (file_exists("models/modules/reg/forgot.php") && filemtime("models/modules/reg/forgot.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/reg/forgot.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$i_langpackage=new indexlp;
$m_langpackage=new moduleslp;

require_once("foundation/asystem_info.php");

if(get_sess_user_id()) {
	$USER['login'] = 1;
	$USER['user_name'] = get_sess_user_name();
	$USER['user_id'] = get_sess_user_id();
	$USER['user_email'] = get_sess_user_email();
	$USER['shop_id'] = get_sess_shop_id();
} else {
	$USER['login'] = 0;
	$USER['user_name'] = '';
	$USER['user_id'] = '';
	$USER['user_email'] = '';
	$USER['shop_id'] = '';
}
$nav_selected = '1';
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="开源商城,shop源码,iweb_shop" />
<meta name="description" content="开源商城,shop源码,iweb_shop" />
<title><?php echo  $m_langpackage->m_getback_pw;?></title>
<link href="skin/<?php echo  $SYSINFO['templates'];?>/css/index.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="skin/<?php echo  $SYSINFO['templates'];?>/js/changeStyle.js"></script>
<script type="text/javascript" src="skin/<?php echo  $SYSINFO['templates'];?>/js/area.js"></script>
<style>
.forgotleft {float:left; width:330px;}
.forgotright {float:left; width:600px; border:1px solid #eee; line-height:24px; padding-left:10px;}
.forgotleft div {margin-top:10px;}
</style>
</head>
<body>
<div id="wrapper">
<?php  include("shop/index_header.php");?>
<!--search end -->
<div class="path"><?php echo  $i_langpackage->i_location;?>：<a href="index.php"><?php echo  $i_langpackage->i_index;?></a> > <?php echo  $m_langpackage->m_getback_pw;?></div>
<div class="pro_class">
	<div class="forgotleft">
		<form action="modules.php?app=forgot3" method="post" onsubmit="return checkform();">
		<div style="font-size:14px; font-weight:bold;"><?php echo  $m_langpackage->m_getback_pw;?></div>
		<div><?php echo  $m_langpackage->m_mail_register;?>：<input type="text" name="user_email" /></div>
		<div>　<?php echo  $m_langpackage->m_name_register;?>：<input type="text" name="user_name" /></div>
		<div style="text-align:right;"><input type="submit" value="<?php echo  $m_langpackage->m_getback_pw;?>" /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
		</form>
	</div>
	<div class="forgotright">
		<?php echo  $m_langpackage->m_caution_register;?>
	</div>
</div>
<div class="top5 clear"></div>
<script language="JavaScript">
<!--
function checkform() {
	if(document.getElementsByName("user_email")[0].value=='') {
		alert("<?php echo  $m_langpackage->m_mail_null;?>");
		return false;
	}
	if(document.getElementsByName("user_name")[0].value=='') {
		alert("<?php echo  $m_langpackage->m_name_null;?>");
		return false;
	}
	return true;
}
//-->
</script>
<!-- main end -->
<!--main right end-->
<?php  require("shop/index_footer.php");?>
<!--footer end-->
</div>
</body>
</html>
<?php } ?>