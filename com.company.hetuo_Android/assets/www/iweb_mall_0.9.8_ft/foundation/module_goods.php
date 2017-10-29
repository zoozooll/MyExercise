<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入公共方法

function insert_goods_info (&$dbo,$table,$insert_items){
	$item_sql = get_insert_item($insert_items);
	$sql = "insert `$table` $item_sql";
	if($dbo->exeUpdate($sql)) {
		return mysql_insert_id();
	} else {
		return false;
	}
}

function update_goods_info(&$dbo,$table,$update_items,$goods_id,$shop_id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where goods_id='$goods_id' and shop_id='$shop_id'";
	return $dbo->exeUpdate($sql);
}

function get_goods_info(&$dbo,$table,$select_items,$goods_id,$shop_id=0) {
	$item_sql = get_select_item($select_items);
	if($shop_id) {
		$sql = "select $item_sql from `$table` where goods_id='$goods_id' and shop_id='$shop_id'";
	} else {
		$sql = "select $item_sql from `$table` where goods_id='$goods_id'";
	}

	return $dbo->getRow($sql);
}

function get_goods_num(&$dbo,$table,$shop_id=0) {
	if($shop_id) {
		$sql = "select count(*) from `$table` where shop_id='$shop_id'";
	} else {
		$sql = "select count(*) from `$table`";
	}
	$count = $dbo->getRow($sql);
	return $count[0];
}

function get_goods_isname_num(&$dbo,$table,$shop_id=0,$isname) {
//	if($shop_id) {
		$sql = "select count(*) from `$table` where shop_id='$shop_id' and is_".$isname."=1";
//		echo $sql;
//	} else {
//		$sql = "select count(*) from `$table`";
//	}
	$count = $dbo->getRow($sql);
	return $count[0];
}

function delete_goods(&$dbo,$table,$goods_id,$shop_id) {
	$sql = "delete from `$table` where goods_id='$goods_id' and shop_id='$shop_id'";
	return $dbo->exeUpdate($sql);
}

function get_goods_attr(&$dbo,$table,$goods_id) {
	$sql = "select * from `$table` where goods_id='$goods_id'";
	return $dbo->getRs($sql);
}

function get_isname_num(&$dbo,$table,$shop_id) {
	$sql = "select goods_id,is_best,is_new,is_hot,is_promote from `$table` where shop_id='$shop_id'";
	return $dbo->getRs($sql);
}

function update_goods_attr(&$dbo,$table,$array,$goods_id) {
	if(empty($array)) {
		return false;
	}
	$i = 0;
	foreach($array as $key=>$value) {
		if(is_array($value)) {
			$value = implode("\n",$value);
		}
		$sql = "update `$table` set attr_values='$value' where goods_id='$goods_id' and attr_id='$key'";
		//echo $sql;
		if($dbo->exeUpdate($sql)){
			$i++;
		}
	}
	return $i;
}

function insert_goods_attr(&$dbo,$table,$array,$goods_id) {
	if(empty($array)) {
		return false;
	}
	$dot = '';
	$sql = "insert into `$table` (goods_id,attr_id,attr_values) values";
	foreach($array as $key=>$value) {
		if($value) {
			if(is_array($value)) {
				$value = implode("\n",$value);
				$sql .= $dot . " ('$goods_id','$key','$value')";
				$dot = ',';
			} else {
				$sql .= $dot . " ('$goods_id','$key','$value')";
				$dot = ',';
			}
		}
	}
	return $dbo->exeUpdate($sql);
}

function delete_goods_attr(&$dbo,$table,$array,$goods_id) {
	if(empty($array)) {
		return false;
	}
	$attr_id = $dot = '';
	foreach($array as $k=>$v) {
		$attr_id .= $dot.$k;
		$dot = ',';
	}
	$sql = "delete from `$table` where attr_id in ($attr_id) and goods_id='$goods_id'";
	return $dbo->exeUpdate($sql);
}
function get_transport_template_list(&$dbo,$table,$shop_id=''){
	if (empty($shop_id)) {
		$shop_id = get_sess_shop_id();
	}

	$sql = "SELECT * FROM `$table` WHERE shop_id = '$shop_id' ORDER BY id DESC";
	return $dbo->getRs($sql);
}
?>