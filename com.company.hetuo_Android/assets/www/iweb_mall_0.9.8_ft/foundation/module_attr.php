<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

function get_attribute_info(&$dbo,$table,$cat_id) {
	$sql = "select * from `$table` where cat_id='$cat_id' order by sort_order asc";
	$result = $dbo->getRs($sql);
	$array = array();
	if($result) {
		$i = 0;
		foreach($result as $k=>$v) {
			$array[$i]['attr_id'] = $v['attr_id'];
			$array[$i]['attr_name'] = $v['attr_name'];
			$array[$i]['input_type'] = $v['input_type'];
			$array[$i]['attr_values'] = $v['attr_values'];
			$i++;
		}
	}
	return $array;
}

function get_one_attribute_info(&$dbo,$table,$attr_id) {
	$sql = "select * from `$table` where attr_id='$attr_id'";
	$result = $dbo->getRow($sql);
	return $result;
}


function insert_attr_info(&$dbo,$table,$insert_items){
	$item_sql = get_insert_item($insert_items);
	$sql = "insert `$table` $item_sql";
	if($dbo->exeUpdate($sql)) {
		return mysql_insert_id();
	} else {
		return false;
	}
}

function update_attr_info(&$dbo,$table,$update_items,$attr_id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where attr_id='$attr_id'";
	return $dbo->exeUpdate($sql);
}

function del_attr_info(&$dbo,$table,$id) {
	$sql = "delete from `$table` where attr_id='$id'";
	return $dbo->exeUpdate($sql);
}
?>