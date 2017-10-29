<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require("foundation/csmtp.class.php");
require_once("foundation/asystem_info.php");
$m_langpackage=new moduleslp;

// 数据表定义区
$t_users = $tablePreStr."users";
$t_user_info = $tablePreStr."user_info";
$t_mailtpl = $tablePreStr."mailtpl";

$user_id = get_sess_user_id();
$user_email = short_check(get_args('user_email'));

// 清空验证码值
$_SESSION['verifyCode'] = '';

if(empty($user_email)) {
	action_return(0,$m_langpackage->m_useremail_notnonoe,"-1");
}
if(!ereg("^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+",$user_email)) {
	action_return(0,$m_langpackage->m_inputtrue_useremail,"-1");
}

dbtarget('r',$dbServs);
$dbo=new dbex();
$sql = "select * from `$t_users` where user_id=$user_id";
$row = $dbo->getRow($sql);
$sql = "select * from `$t_mailtpl` where tpl_id=2";
$mail_row = $dbo->getRow($sql);

if($row['user_email']!=$user_email) {
	action_return(0,"请填写正确的邮箱地址","-1");
}else{

	$arr1 = array('{user_name}','{site_name}','{baseUrl}','{user_id}','{email_check_code}');
	$arr2 = array($post['user_name'],$SYSINFO['sys_name'],$baseUrl,$user_id,$post['email_check_code']);
	$mailbody = str_replace($arr1,$arr2,$mail_row['tpl_content']);
//	$mailbody = "亲爱的".$post['user_name']."，欢迎您成为".$SYSINFO['sys_name']."的一员，现在开始商城之旅。<br />
//	激活你的帐号请<a href='".$baseUrl."modules.php?app=reg3&uid=".$user_id."&ucode=".$post['email_check_code']."' target='_blank'>点击这里</a>， 如果无法点击，请将下列地址复制到地址栏进行激活。<br />
//	".$baseUrl."modules.php?app=reg3&uid=".$user_id."&ucode=".$post['email_check_code']."<br />
//	感谢对".$SYSINFO['sys_name']."的支持，再次希望您在".$SYSINFO['sys_name']."完成交易！<br /><br />
//	( 这是系统自动产生的邮件，请勿回复。) <br />";
	$mailbody = iconv('UTF-8','GBK',$mailbody);
	$mailtitle = str_replace('{site_name}',$SYSINFO['sys_name'],$mail_row['tpl_title']);
	$mailtitle = iconv('UTF-8','GBK',$mailtitle);

	$smtp = new smtp($SYSINFO['sys_smtpserver'],$SYSINFO['sys_smtpserverport'],true,$SYSINFO['sys_smtpuser'],$SYSINFO['sys_smtppass']);
	$smtp->sendmail($row['user_email'], $SYSINFO['sys_smtpusermail'], $mailtitle, $mailbody, 'HTML');

	action_return(1,"发送成功，请进行邮箱验证",'modules.php?app=email_verify');
}