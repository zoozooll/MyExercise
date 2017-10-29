<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
if($USER['shop_id']) {
	$url=shop_url($USER['shop_id']);
} else {
	$url='modules.php?app=shop_info';
}
?>