<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

function get_credit(&$dbo,$table,$id) {
	$sql="select SUM(seller_credit) from `$table` where seller=$id";
//	echo $sql;
	return $dbo->getRow($sql);
}

function get_integral(&$dbo,$table,$credit){
	if(!$credit) { return false;}
	$sql="select int_grade from `$table` where int_min<=$credit and $credit<=int_max";
//	echo $sql;
	return $dbo->getRow($sql);
}

function get_integral_list(&$dbo,$table){
	$sql="select * from `$table`";
//	echo $sql;
	return $dbo->getRs($sql);
}

function update_credit(&$dbo,$table,$update_items,$order_id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where order_id='$order_id'";
//	echo $sql;
	return $dbo->exeUpdate($sql);
}

function insert_credit(&$dbo,$table,$insert_items){
	$item_sql = get_insert_item($insert_items);
	$sql = "insert `$table` $item_sql";
	return $dbo->exeUpdate($sql);
}
?>