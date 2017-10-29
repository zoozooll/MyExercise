<?php
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
?>