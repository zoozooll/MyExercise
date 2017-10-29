<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入公共方法

function get_honor_list(&$dbo,$table,$shop_id) {
	$sql = "select * from `$table` where shop_id='$shop_id'";
	return $dbo->getRs($sql);
}

function update_honor_desc(&$dbo,$table,$shop_id,$array) {
	$i = 0;
	foreach($array as $k=>$v) {
		$sql = "update `$table` set honor_desc='$v' where shop_id='$shop_id' and honor_id='$k'";
		if($dbo->exeUpdate($sql)) {
			$i++;
		}
	}
	return $i;
}

function insert_honor_info(&$dbo,$table,$shop_id,$insert_array) {
	if(empty($insert_array)) { return false;}
	$i = 0;
	foreach($insert_array as $v) {
		$insert_items = $v;
		$insert_items['shop_id'] = $shop_id;
		$item_sql = get_insert_item($insert_items);
		$sql = "insert `$table` $item_sql";
		if($dbo->exeUpdate($sql)) {
			$i++;
		}
	}
	return $i;
}

function get_honor_info(&$dbo,$table,$shop_id,$honor_id) {
	$sql = "select * from `$table` where shop_id='$shop_id' and honor_id='$honor_id'";
	return $dbo->getRow($sql);
}
?>