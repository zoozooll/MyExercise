<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//引入模块公共方法文件
require("../foundation/csmtp.class.php");

//语言包引入
$a_langpackage=new adminlp;

$sys_smtptest = $_GET['smtp_test'];
//数据表定义区
$t_settings = $tablePreStr."settings";
//数据读操作
dbtarget('r',$dbServs);
$dbo=new dbex;
$sql = "select * from `$t_settings`";
$result = $dbo->getRs($sql);
if($result) {
	foreach($result as $v) {
		$SYSINFO[$v['variable']] = $v['value'];
	}
}
if ($SYSINFO['email_send']=='true'){
	if($sys_smtptest){
		$mailbody = $a_langpackage->a_test_emaill;
		$mailbody = iconv('UTF-8','GBK',$mailbody);
		$mailtitle = $SYSINFO['sys_name'].$a_langpackage->a_message_authentication;
		$mailtitle = iconv('UTF-8','GBK',$mailtitle);
		$smtp = new smtp($SYSINFO['sys_smtpserver'],$SYSINFO['sys_smtpserverport'],true,$SYSINFO['sys_smtpuser'],$SYSINFO['sys_smtppass']);
		if($smtp->sendmail($sys_smtptest, $SYSINFO['sys_smtpusermail'], $mailtitle, $mailbody, 'HTML')){
			echo"<script language=javascript>
				alert('$a_langpackage->a_send_emaill_ok');
				location.href = \"m.php?app=sys_setting\"
				</script>";
		}else {
			 echo"<script language=javascript>
				alert('$a_langpackage->a_send_emaill_fail');
				location.href = \"m.php?app=sys_setting\"
				</script>";
		}
	}else {
		echo"<script language=javascript>
				alert('$a_langpackage->a_test_mailbox');
				location.href = \"m.php?app=sys_setting\"
				</script>";
	}
}else {
	echo"<script language=javascript>
				alert('$a_langpackage->a_open_smtp');
				location.href = \"m.php?app=sys_setting\"
				</script>";
}

?>