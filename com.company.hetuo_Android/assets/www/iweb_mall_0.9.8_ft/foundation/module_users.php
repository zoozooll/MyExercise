<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入公共方法

function get_user_info_item(&$dbo,$select_items,$t_user,$user_id) {
	$item_sql = get_select_item($select_items);
	$sql="select $item_sql from `$t_user` where user_id='$user_id'";
	return $dbo->getRow($sql);
}

function get_user_info(&$dbo,$table,$user_id) {
	return get_user_info_item($dbo,'*',$table,$user_id);
}

function update_user_info(&$dbo,$table,$update_items,$user_id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where user_id='$user_id'";
	return $dbo->exeUpdate($sql);
}

function insert_user_info(&$dbo,$table,$insert_items) {
	$item_sql = get_insert_item($insert_items);
	$sql = "insert into `$table` $item_sql ";
	$dbo->exeUpdate($sql);
	return mysql_insert_id();
}

function get_userrank_info(&$dbo,$table,$rank_id) {
	$sql="select * from `$table` where rank_id='$rank_id'";
	return $dbo->getRow($sql);
}

function get_privilege_list(&$dbo,$table) {
	$sql = "select * from `$table` order by privilege_id asc";
	$result = $dbo->getRs($sql);
	$array = '';
	foreach($result as $v) {
		$array[$v['privilege_id']] = $v;
	}
	return $array;
}

function update_userrank_info(&$dbo,$table,$update_items,$rank_id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where rank_id='$rank_id'";
	return $dbo->exeUpdate($sql);
}

function insert_userrank_info(&$dbo,$table,$insert_items) {
	$item_sql = get_insert_item($insert_items);
	$sql = "insert into `$table` $item_sql ";
	$dbo->exeUpdate($sql);
	return mysql_insert_id();
}

function del_userrank_info(&$dbo,$table,$rank_id) {
	$sql = "delete from `$table` where rank_id='$rank_id' ";
	return $dbo->exeUpdate($sql);
}

function get_userrank_list(&$dbo,$table) {
	$sql = "select * from `$table` order by rank_id asc";
	$user_rank = $dbo->getRs($sql);
	foreach($user_rank as $value) {
		$userrank[$value['rank_id']] = $value;
	}
	return $userrank;
}
//address---------------------------------------------------------------------------------------------------
function insert_user_address(&$dbo,$table,$insert_items){
	$item_sql = get_insert_item($insert_items);
	$sql = "insert into `$table` $item_sql ";
	$dbo->exeUpdate($sql);
	return mysql_insert_id();
	
}

function update_address(&$dbo,$table,$update_items,$address_id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where address_id=$address_id";
	return $dbo->exeUpdate($sql);
}

function del_address(&$dbo,$table,$address_id) {
	$sql = "delete from `$table` where address_id=$address_id";
	return $dbo->exeUpdate($sql);
}
?>