<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入公共方法

function insert_complaint(&$dbo,$table,$insert_items) {
	$item_sql = get_insert_item($insert_items);
	$sql = "insert into `$table` $item_sql ";
	$dbo->exeUpdate($sql);
	return mysql_insert_id();
}

function get_complaint_all(&$dbo,$select_items,$table) {
	$item_sql = get_select_item($select_items);
	$sql="select $item_sql from `$table` ";
	return $dbo->getRs($sql);
}

function del_complaint(&$dbo,$table,$complaints_id) {
	$sql = "delete from `$table` where complaints_id='$complaints_id'";
	return $dbo->exeUpdate($sql);
}

function get_complaint_type_all(&$dbo,$select_items,$table) {
	$item_sql = get_select_item($select_items);
	$sql="select $item_sql from `$table` ";
	return $dbo->getRs($sql);
}

function get_complaint_type_byid(&$dbo,$select_items,$table,$type_id) {
	$item_sql = get_select_item($select_items);
	$sql="select $item_sql from `$table` where  type_id='$type_id'";
	return $dbo->getRow($sql);
}

function update_complaint_type_byid(&$dbo,$table,$update_items,$type_id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where type_id='$type_id'";
	return $dbo->exeUpdate($sql);
}

function insert_complaint_type(&$dbo,$table,$insert_items) {
	$item_sql = get_insert_item($insert_items);
	$sql = "insert into `$table` $item_sql ";
	$dbo->exeUpdate($sql);
	return mysql_insert_id();
}

function del_complaint_type(&$dbo,$table,$type_id) {
	$sql = "delete from `$table` where type_id='$type_id'";
	return $dbo->exeUpdate($sql);
}

?>
