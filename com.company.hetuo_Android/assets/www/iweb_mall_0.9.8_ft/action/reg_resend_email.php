<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require("foundation/module_users.php");
require("foundation/csmtp.class.php");
require_once("foundation/asystem_info.php");
$m_langpackage=new moduleslp;
// 数据表定义区
$t_users = $tablePreStr."users";
$t_mailtpl = $tablePreStr."mailtpl";

//$user_id;
$post['user_name'] = get_sess_user_name();
$post['user_email'] = get_sess_user_email();

$post['email_check_code'] = md5($post['user_name'].$ctime->time_stamp());

dbtarget('r',$dbServs);
$dbo=new dbex();
$sql = "select * from `$t_mailtpl` where tpl_id=2";
$mail_row = $dbo->getRow($sql);

dbtarget('w',$dbServs);
$dbo=new dbex();

if($user_id) {

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
	$smtp->sendmail($post['user_email'], $SYSINFO['sys_smtpusermail'], $mailtitle, $mailbody, 'HTML');

	$sql = "update $t_users set email_check_code='".$post['email_check_code']."' where user_id='$user_id'";
	$dbo->exeUpdate($sql);
}
action_return(1,$m_langpackage->m_sendagain_viewyoumail);
?>