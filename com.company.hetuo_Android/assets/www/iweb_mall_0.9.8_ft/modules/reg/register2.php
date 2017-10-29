<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/reg/register2.html
 * 如果您的模型要进行修改，请修改 models/modules/reg/register2.php
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
if(filemtime("templates/default/modules/reg/register2.html") > filemtime(__file__) || (file_exists("models/modules/reg/register2.php") && filemtime("models/modules/reg/register2.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/reg/register2.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$i_langpackage=new indexlp;

require_once("foundation/asystem_info.php");
//require_once("foundation/fsession.php");
$user_id=get_sess_user_id();
$email_check=get_sess_email_check();


if(!$user_id) {
	echo '<script language="JavaScript">location.href="modules.php?app=reg"</script>';
	exit;
}
if($email_check) {
	echo '<script language="JavaScript">location.href="modules.php"</script>';
	exit;
}
?><?php } ?>