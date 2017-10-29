<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

// 获取需要更新的计划任务
function get_rates(&$dbo,$table) {
	$sql="select * from `$table` ";
	echo $sql;
	return $dbo->getRs($sql);
}

?>