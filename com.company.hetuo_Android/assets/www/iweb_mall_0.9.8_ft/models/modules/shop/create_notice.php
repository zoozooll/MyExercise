<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//引入语言包
$m_langpackage=new moduleslp;
if(!$user_privilege[1]) {
	//$_SESSION['error_message'] = $m_langpackage->m_error_createshop;
	set_sess_err_msg($m_langpackage->m_error_createshop);
	echo '<script language="JavaScript">location.href="modules.php?app=message"</script>';
	exit;
}
?>