<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入公共方法

function get_gallery_list(&$dbo,$table,$goods_id) {
	$sql = "select * from `$table` where goods_id='$goods_id'";
	return $dbo->getRs($sql);
}

function get_gallery_info(&$dbo,$table,$goods_id,$img_id) {
	$sql = "select * from `$table` where goods_id='$goods_id' and img_id='$img_id'";
	return $dbo->getRow($sql);
}

function update_gallery_info(&$dbo,$table,$update_items,$goods_id,$img_id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where goods_id='$goods_id' and img_id='$img_id'";
	return $dbo->exeUpdate($sql);
}

function update_gallery_desc(&$dbo,$table,$goods_id,$array) {
	$i = 0;
	foreach($array as $k=>$v) {
		$sql = "update `$table` set img_desc='$v' where goods_id='$goods_id' and img_id='$k'";
		if($dbo->exeUpdate($sql)) {
			$i++;
		}
	}
	return $i;
}

function insert_gallery_info(&$dbo,$table,$goods_id,$insert_array) {
	if(empty($insert_array)) { return false;}
	$i = 0;
	foreach($insert_array as $v) {
		$insert_items = $v;
		$insert_items['goods_id'] = $goods_id;
		$item_sql = get_insert_item($insert_items);
		$sql = "insert `$table` $item_sql";
		if($dbo->exeUpdate($sql)) {
			$i++;
		}
	}
	return $i;
}

function unset_gallery_setimg(&$dbo,$table,$goods_id) {
	$sql = "update `$table` set is_set=0 where goods_id='$goods_id' ";
	return $dbo->exeUpdate($sql);
}
?>