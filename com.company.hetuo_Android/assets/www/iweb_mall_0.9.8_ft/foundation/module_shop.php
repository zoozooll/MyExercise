<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入公共方法

function get_shop_info_item(&$dbo,$select_items,$table,$shop_id)
{
	$item_sql = get_select_item($select_items);
	$sql = "select $item_sql from `$table` where shop_id='$shop_id' ";
	return $dbo->getRow($sql);
}

function get_shop_info(&$dbo,$table,$shop_id)
{
	return get_shop_info_item($dbo,'*',$table,$shop_id);
}
	
function update_shop_info(&$dbo,$table,$update_items,$shop_id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where shop_id='$shop_id'";
	return $dbo->exeUpdate($sql);
}

function insert_shop_info(&$dbo,$table,$insert_items) {
	$item_sql = get_insert_item($insert_items);
	$sql = "insert `$table` $item_sql";
	return $dbo->exeUpdate($sql);
}

function get_shop_category_list(&$dbo,$table,$shop_id) {
	$sql = "select * from `$table` where shop_id='$shop_id' order by sort_order asc";
	return $dbo->getRs($sql);
}
?>