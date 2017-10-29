<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

function get_all(&$dbo,$table) {
	$sql="select * from `$table`";
	return $dbo->getRs($sql);
}

function get_remind(&$dbo,$table,$id){
	$sql="select * from `$table` where remind_id=$id";
	return $dbo->getRow($sql);
}

function update_remind(&$dbo,$table,$update_items,$id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where remind_id='$id'";
	return $dbo->exeUpdate($sql);
}

function get_remind_user(&$dbo,$table,$user_id,$remind_id) {
	$sql = "select * from `$table` where user_id=$user_id and remind_id=$remind_id";
	return $dbo->getRow($sql);
}

function insert_remind_info(&$dbo,$table,$insert_items) {
	$item_sql = get_insert_item($insert_items);
	$sql = "INSERT INTO $table $item_sql";
	return $dbo->exeUpdate($sql);
}

function remind_info_replace($tpl,$array) {
	$arr1 = $arr2 = array();
	foreach($array as $k=>$v) {
		$arr1[] = '{'.$k.'}';
		$arr2[] = $v;
	}
	return str_replace($arr1,$arr2,$tpl);
}
?>