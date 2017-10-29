<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

function get_all(&$dbo,$table) {
	$sql="select * from `$table`";
	return $dbo->getRs($sql);
}

function get_tplid(&$dbo,$table,$id){
	$sql="select * from `$table` where tpl_id=$id";
	return $dbo->getRow($sql);
}

function update_mailtpl(&$dbo,$table,$update_items,$id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where tpl_id='$id'";
	return $dbo->exeUpdate($sql);
}
?>