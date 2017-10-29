<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入公共方法
function get_groupbuy_info(&$dbo,$select_items,$table,$group_id) {
	$item_sql = get_select_item($select_items);
	$sql="select $item_sql from `$table` where group_id='$group_id'";
	return $dbo->getRow($sql);
}

function get_groupbuy_list(&$dbo,$select_items,$table,$shop_id) {
	$item_sql = get_select_item($select_items);
	$sql="select $item_sql from `$table` where shop_id='$shop_id'";
	return $dbo->getRs($sql);
}

function insert_groupbuy(&$dbo,$table,$insert_items){
	$item_sql = get_insert_item($insert_items);
	$sql = "insert into `$table` $item_sql ";
	$dbo->exeUpdate($sql);
	return mysql_insert_id();

}

function update_groupbuy_release(&$dbo,$table,$update_items,$group_id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where group_id=$group_id";
	return $dbo->exeUpdate($sql);
}

function del_groupbuy(&$dbo,$table,$group_id) {
	$sql = "delete from `$table` where group_id=$group_id";
	return $dbo->exeUpdate($sql);
}

//===========================
function get_groupbuylog_list(&$dbo,$select_items,$table,$group_id) {
	$item_sql = get_select_item($select_items);
	$sql="select $item_sql from `$table` where group_id='$group_id'";
	return $dbo->getRs($sql);
}
?>
