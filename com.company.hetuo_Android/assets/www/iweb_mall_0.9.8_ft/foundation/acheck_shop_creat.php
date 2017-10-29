<?php 
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

if(get_sess_shop_id()>0) {
	$shop_id = get_sess_shop_id();
} else {
	echo '<script language="JavaScript">location.href="modules.php?app=shop_create_notice"</script>';
	exit();
}
?>