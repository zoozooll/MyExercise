<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/reg/register3.html
 * 如果您的模型要进行修改，请修改 models/modules/reg/register3.php
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
if(filemtime("templates/default/modules/reg/register3.html") > filemtime(__file__) || (file_exists("models/modules/reg/register3.php") && filemtime("models/modules/reg/register3.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/reg/register3.html",1);
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

/* GET */
$user_id = intval(get_args('uid'));
$email_check_code = get_args('ucode');

/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

/* 定义文件表 */
$t_users = $tablePreStr."users";

if($user_id && $email_check_code) {
	$sql = "select email_check_code,email_check from `$t_users` where user_id='$user_id'";
	$row = $dbo->getRow($sql);
	if($row['email_check']) {
		echo '<script language="JavaScript">alert("您已通过验证，请勿重复验证。"); location.href="modules.php"</script>';
		exit;
	}else{
		if($row['email_check_code'] == $email_check_code){
			/* 数据库操作 */
			dbtarget('w',$dbServs);
			$dbo=new dbex();

			$sql = "update `$t_users` set email_check=1 where user_id='$user_id'";
			$dbo->exeUpdate($sql);

			echo '<script language="JavaScript">alert("恭喜您已通过验证。"); location.href="modules.php"</script>';
			exit;
		}else{
			echo '<script language="JavaScript">alert("邮箱验证码错误，请重新发送。"); location.href="modules.php?app=send_code"</script>';
			exit;
		}
	}
} else {
	echo '<script language="JavaScript">alert("url不完整，请确认。"); location.href="modules.php?app=reg2"</script>';
	exit;
}
?><?php } ?>