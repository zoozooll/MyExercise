<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

function insert_order_info(&$dbo,$table,$insert_items) {
	$item_sql = get_insert_item($insert_items);
	$sql = "insert into `$table` $item_sql ";
	//echo $sql;
	return $dbo->exeUpdate($sql);
	//exit;
	//echo mysql_insert_id();
	//return mysql_insert_id();
}

function get_order_info(&$dbo,$table,$order_id,$user_id=0,$shop_id=0) {
	$sql="select * from `$table` where order_id='$order_id' ";
	if($user_id) {
		$sql .= " and user_id='$user_id'";
	}
	if($shop_id) {
		$sql .= " and shop_id='$shop_id'";
	}
	return $dbo->getRow($sql);
}

function get_order_info_bypayid(&$dbo,$table,$payid) {
	$sql="select * from `$table` where payid='$payid' ";
	if($user_id) {
		$sql .= " and user_id='$user_id'";
	}
//	echo $sql;
	return $dbo->getRow($sql);
}

function get_order_info_orderstatus(&$dbo,$table) {
	$sql="select * from `$table` where order_status='1'";
//	echo $sql;
	return $dbo->getRow($sql);
}

function get_order_info_list(&$dbo,$table){
	$sql = "select * from $table";
//	echo $sql;
	return $dbo->getRs($sql);
}

function upd_order_info(&$dbo,$table,$update_items,$order_id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where order_id='$order_id'";
//	echo $sql;
	return $dbo->exeUpdate($sql);
}
?>