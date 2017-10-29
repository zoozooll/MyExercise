<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

function get_select_item($select_items){
	$sql_part = '';
	if($select_items!='*' and $select_items!=''){
		if(is_array($select_items)) {
			$sql_part = '`'.implode("`,`",$select_items).'`';
		} else {
			$sql_part = "`$select_items`";
		}
	} else {
		$sql_part = '*';
	}
	return $sql_part;
}

function get_update_item($update_items){
	$sql_part = '';
	if(is_array($update_items)){
		$set = $dot = '';
		foreach($update_items as $k=>$v) {
			$sql_part .= $dot."`$k` = '$v'";
			$dot = ",";
		}
		return $sql_part;
	} else {
		exit('update_items not array');
		return false;
	}
}

function get_insert_item($insert_items){
	$sql_part = '';
	if(is_array($insert_items)){
		$set = $value = $dot = '';
		foreach($insert_items as $k=>$v) {
			$set .= $dot."`$k`";
			$value .= $dot."'$v'";
			$dot = ",";
		}
		$sql_part = "($set) values ($value)";
		return $sql_part;
	} else {
		exit('insert_items not array');
		return false;
	}
}
?>