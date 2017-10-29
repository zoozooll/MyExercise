<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

function get_areas_info(&$dbo,$table) {
	$sql = "select * from $table";
	$array = $dbo->getRs($sql);
	$return_array = array();
	foreach($array as $k=>$v) {
		$return_array[$v['area_type']][$v['area_id']]['area_id'] = $v['area_id'];
		$return_array[$v['area_type']][$v['area_id']]['parent_id'] = $v['parent_id'];
		$return_array[$v['area_type']][$v['area_id']]['area_name'] = $v['area_name'];
		$return_array[$v['area_type']][$v['area_id']]['area_name'] = $v['area_name'];
	}
	return $return_array;
}

function get_areas_list(&$dbo,$table,$parent_id) {
	$list = array();
	$sql = "select * from $table where parent_id='$parent_id'";
	$list = $dbo->getRs($sql);
	return $list;
}

function get_areas_kv(&$dbo,$table) {
	$sql = "select * from $table";
	$array = $dbo->getRs($sql);
	$return_array = array();
	foreach($array as $k=>$v) {
		$return_array[$v['area_id']] = $v['area_name'];
	}
	return $return_array;
}

function insert_area(&$dbo,$table,$insert_items){
	$item_sql = get_insert_item($insert_items);
	$sql = "insert `$table` $item_sql";
	return $dbo->exeUpdate($sql);
}

function del_area(&$dbo,$table,$id) {
	$sql = "delete from `$table` where area_id=$id";
	return $dbo->exeUpdate($sql);
}

function update_area(&$dbo,$table,$update_items,$brand_id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where area_id='$brand_id'";
	return $dbo->exeUpdate($sql);
}
function get_area_list_bytype(&$dbo,$table,$type=0){
	$sql = "SELECT * FROM `$table` WHERE area_type='$type' ";
	return $dbo->getRs($sql);
}
?>