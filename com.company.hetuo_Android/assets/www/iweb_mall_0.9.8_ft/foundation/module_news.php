<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

function get_news_cat_list(&$dbo,$table) {
	$sql = "select * from `$table`";
	$result = $dbo->getRs($sql);
	$array = array();
	foreach($result as $value) {
		$array[$value['cat_id']] = $value;
	}
	return $array;
}

function get_news_list(&$dbo,$table,$cat_id=0) {
	$sql = "select * from `$table`";
	if($cat_id) {
		$sql .= " where cat_id='$cat_id' ";
	}
	$sql .= "order by article_id asc";
	$result = $dbo->getRs($sql);
	$array = array();
	foreach($result as $value) {
		$array[$value['article_id']] = $value;
	}
	return $array;
}

function insert_news_info(&$dbo,$table,$insert_items) {
	$item_sql = get_insert_item($insert_items);
	$sql = "insert into `$table` $item_sql ";
	$dbo->exeUpdate($sql);
	return mysql_insert_id();
}

function del_news_info(&$dbo,$table,$article_id) {
	$sql = "delete from `$table` where article_id='$article_id'";
	return $dbo->exeUpdate($sql);
}

function get_news_info(&$dbo,$table,$article_id) {
	$sql = "select * from `$table` where article_id='$article_id'";
	return $dbo->getRow($sql);
}

function update_news_info(&$dbo,$table,$update_items,$article_id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where article_id='$article_id'";
	return $dbo->exeUpdate($sql);
}

function update_news_cat(&$dbo,$table,$update_items,$cat_id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where cat_id='$cat_id'";
	return $dbo->exeUpdate($sql);
}
?>
