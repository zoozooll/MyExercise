<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/user/safe.html
 * 如果您的模型要进行修改，请修改 models/modules/user/safe.php
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
if(filemtime("templates/default/modules/user/safe.html") > filemtime(__file__) || (file_exists("models/modules/user/safe.php") && filemtime("models/modules/user/safe.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/user/safe.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo  $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<style type="text/css">
.right_operate .title {font-size:12px; color:#fff; background-color:#1E88C0; padding-left:10px; font-weight:bold; line-height:24px; height:24px;}
table { border-collapse:collapse; border:solid #999; border-width:1px 0 0 1px; margin:5px auto;} 
table th, table td { border:solid #999; border-width:0 1px 1px 0; padding:2px; } 
.textright {text-align:right; width:160px;}
</style>
</head>
<body>
<div class="header"><?php  require("modules/header.php");?></div>
<div class="main">
	<div class="left_menu">
		<?php  require("modules/left_menu.php");?>
	</div>
	<div class="right_operate">
		<div class="title"><?php echo $m_langpackage->m_safe_setting;?></div>
		<form action="do.php?act=user_profile" method="post" name="form_profile">
		<table width="98%">

		</table>
		</form>
	</div>
	<div class="clear"></div>
</div>
<div class="footer"><?php  require("modules/footer.php");?></div>
</body>
</html><?php } ?>