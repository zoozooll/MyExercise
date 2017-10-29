<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/reg/forgot3.html
 * 如果您的模型要进行修改，请修改 models/modules/reg/forgot3.php
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
if(filemtime("templates/default/modules/reg/forgot3.html") > filemtime(__file__) || (file_exists("models/modules/reg/forgot3.php") && filemtime("models/modules/reg/forgot3.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/reg/forgot3.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("foundation/asystem_info.php");
require("foundation/csmtp.class.php");

$i_langpackage=new indexlp;
$m_langpackage=new moduleslp;

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

/* POST */
$post['user_name'] = short_check(get_args('user_name'));
$post['user_email'] = short_check(get_args('user_email'));

/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

/* 定义文件表 */
$t_users = $tablePreStr."users";
$t_mailtpl = $tablePreStr."mailtpl";

$sql = "select user_id from `$t_users` where user_name='$post[user_name]' and user_email='$post[user_email]'";
$row = $dbo->getRow($sql);

if($row) {
	$sql = "select * from `$t_mailtpl` where tpl_id=1";
	$mail_row = $dbo->getRow($sql);

	$send_time = $ctime->long_time();
	$user_id = $row['user_id'];
	$post['forgot_check_code'] = md5($post['user_name'].$post['user_email'].time());
	$forgot_check_code_url = $baseUrl."modules.php?app=user_forgot&uid=".$user_id."&ucode=".$post['forgot_check_code'];

	$mailtitle = str_replace('{site_name}',$SYSINFO['sys_name'],$mail_row['tpl_title']);
	$mailtitle = iconv('UTF-8','GBK',$mailtitle);

	$arr1 = array('{user_name}','{forgot_check_code_url}','{send_date}');
	$arr2 = array($post['user_name'],$forgot_check_code_url,$send_time);
	$mailbody = str_replace($arr1,$arr2,$mail_row['tpl_content']);
	$mailbody = iconv('UTF-8','GBK',$mailbody);

	$smtp = new smtp($SYSINFO['sys_smtpserver'],$SYSINFO['sys_smtpserverport'],true,$SYSINFO['sys_smtpuser'],$SYSINFO['sys_smtppass']);
	$smtp->sendmail($post['user_email'], $SYSINFO['sys_smtpusermail'], $mailtitle, $mailbody, 'HTML');

	$sql = "update $t_users set forgot_check_code='".$post['forgot_check_code']."' where user_id='$user_id'";
	$dbo->exeUpdate($sql);
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
.pro_class div {margin-top:10px; text-align:center;}
</style>
</head>
<body>
<div id="wrapper">
<?php  include("shop/index_header.php");?>
<!--search end -->
<div class="path"><?php echo  $i_langpackage->i_location;?>：<a href="index.php"><?php echo  $i_langpackage->i_index;?></a> > <?php echo  $m_langpackage->m_getback_pw;?></div>
<div class="pro_class"><?php echo  $m_langpackage->m_caution_app;?>
	<div class="find_pwd">
		<input class="f14 bold" type="button" value="<?php echo  $m_langpackage->m_accomplish;?>" onclick="jump()" />
	</div>
</div>
<div class="top5 clear"></div>
<script language="JavaScript">
<!--
function jump() {
	location.href='index.php'
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