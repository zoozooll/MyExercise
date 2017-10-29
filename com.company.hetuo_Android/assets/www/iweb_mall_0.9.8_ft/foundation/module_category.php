<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

function get_category_info(&$dbo,$table) {
	$sql = "select * from `$table` order by sort_order asc, cat_id asc";
	return $dbo->getRs($sql);
}

function get_sub_category(&$dbo,$table,$cat_id) {
	$sql = "select * from `$table` where parent_id='$cat_id' order by sort_order asc, cat_id asc";
	return $dbo->getRs($sql);
}

function get_shop_category_list(&$dbo,$table,$shop_id) {
	$sql = "select * from `$table` where shop_id='$shop_id' order by sort_order asc";
	return $dbo->getRs($sql);
}

function html_format_category($array,$value,$parentid=0,$level=0) {
	$newarray = get_dg_category($array,$parentid=0,$level=0,$add=1);

	foreach($newarray as $v) {
		$selected = '';
		if($value == $v['cat_id']) { $selected = 'selected';}
		$str .= "<option value='".$v['cat_id']."' ".$selected.">".$v['str_pad'].$v['cat_name']."</option>";
	}
	return $str;
}

function html_format_shop_category($array,$value,$parentid=0,$level=0) {
	$str = '';
	$str_pad = getnbsp($level);

	foreach($array as $v) {
		$selected = '';
		if($v['parent_id'] == $parentid) {
			if($value == $v['shop_cat_id']) { $selected = 'selected';}
			$str .= "<option value='".$v['shop_cat_id']."' ".$selected.">".$str_pad.$v['shop_cat_name']."</option>";
			$str .= html_format_shop_category($array,$value,$v['shop_cat_id'],($level+1));
		}
	}
	return $str;
}

function getnbsp($i) {
	$str = '';
	if($i) {
		for($j=0; $j<$i; $j++) {
			$str .= "　";
		}
	}
	return $str;
}

function insert_category_info(&$dbo,$table,$insert_items){
	$item_sql = get_insert_item($insert_items);
	$sql = "insert `$table` $item_sql";
//	echo $sql;
	$suc=$dbo->exeUpdate($sql);
	if($suc){
		return mysql_insert_id();
	}else{
		return false;
	}
}

function update_category_info($dbo,$table,$update_items,$cat_id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where cat_id='$cat_id'";
	return $dbo->exeUpdate($sql);
}

function get_dg_category($array,$parentid=0,$level=0,$add=2) {
	$str_pad = getnbsp($level);
	$newarray = array();
	$temp = array();
	foreach($array as $v) {
		if($v['parent_id'] == $parentid) {
			$newarray[] = array(
				'cat_id' => $v['cat_id'],
				'cat_name' => $v['cat_name'],
				'parent_id' => $v['parent_id'],
				'goods_num' => $v['goods_num'],
				'sort_order' => $v['sort_order'],
				'str_pad' => $str_pad
			);
			$temp = get_dg_category($array,$v['cat_id'],($level+$add));
			if($temp) {
				$newarray = array_merge($newarray, $temp);
			}
		}
	}
	return $newarray;
}
?>