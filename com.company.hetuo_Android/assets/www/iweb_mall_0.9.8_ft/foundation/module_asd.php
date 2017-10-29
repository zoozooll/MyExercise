<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

function update_asd_position_info(&$dbo,$table,$update_items,$position_id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where position_id='$position_id'";
	return $dbo->exeUpdate($sql);
}

function insert_asd_position_info(&$dbo,$table,$insert_items) {
	$item_sql = get_insert_item($insert_items);
	$sql = "insert into `$table` $item_sql ";
	$dbo->exeUpdate($sql);
	return mysql_insert_id();
}

function get_asd_position_info(&$dbo,$table,$position_id) {
	$sql = "select * from `$table` where position_id='$position_id'";
	return $dbo->getRow($sql);
}

function get_asd_position_all(&$dbo,$table) {
	$sql = "select * from `$table`";
	$result = $dbo->getRs($sql);
	$array = array();
	foreach($result as $value) {
		$array[$value['position_id']] = $value;
	}
	return $array;
}

function insert_asd_info(&$dbo,$table,$insert_items) {
	$item_sql = get_insert_item($insert_items);
	$sql = "insert into `$table` $item_sql ";
	$dbo->exeUpdate($sql);
	return mysql_insert_id();
}

function del_asd_position_info(&$dbo,$table,$position_id) {
	$sql = "delete from `$table` where position_id='$position_id'";
	return $dbo->exeUpdate($sql);
}

function get_asd_info(&$dbo,$table,$asd_id) {
	$sql = "select * from `$table` where asd_id='$asd_id'";
	return $dbo->getRow($sql);
}

function update_asd_info(&$dbo,$table,$update_items,$asd_id) {
	$item_sql = get_update_item($update_items);
	$sql = "update `$table` set $item_sql where asd_id='$asd_id'";
	return $dbo->exeUpdate($sql);
}

function del_asd_info(&$dbo,$table,$asd_id) {
	$sql = "delete from `$table` where asd_id='$asd_id'";
	return $dbo->exeUpdate($sql);
}

function put_asd_position_file($position_id,$link,$content,$type,$w,$h,$asd_name='') {
	$html = '';
	if($type==3) {
		$html = '<a href="'.$link.'" title="'.$asd_name.'">'.$content.'</a>';
	} elseif ($type==2) {
		$html = '<embed src="'.$content.'" menu="false" wmode="transparent" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="'.$w.'" height="'.$h.'"></embed>';
	} elseif ($type==1) {
		$html = '<a href="'.$link.'"><img src="'.$content.'" width="'.$w.'" height="'.$h.'" alt="'.$asd_name.'"></a>';
	}

	$htmls = "document.write('$html');";

	$file = "../uploadfiles/asd/$position_id.js";
	return @file_put_contents($file,$htmls);
}

function update_asd_position_file(&$dbo,$asd_content,$asd_position,$asd_id=0) {
	$sql = "SELECT a.*,b.asd_height,b.asd_width FROM $asd_content AS a, $asd_position AS b WHERE a.position_id=b.position_id ";
	if($asd_id) {
		$sql .= " AND a.asd_id='$asd_id' ";
	}
	$sql .= "order by a.last_update_time desc";

	$result = $dbo->getRow($sql);
	return put_asd_position_file($result['position_id'],$result['asd_link'],$result['asd_content'],$result['media_type'],$result['asd_width'],$result['asd_height'],$result['asd_name']);
}
function update_asd_file(&$dbo,$asd_content,$asd_position,$asd_id=0) {
	$sql = "SELECT a.*,b.asd_height,b.asd_width FROM $asd_content AS a, $asd_position AS b WHERE a.position_id=b.position_id ";
	if($asd_id) {
		$sql .= " AND a.asd_id='$asd_id' ";
	}
	$sql .= "order by a.last_update_time desc";

	$result = $dbo->getRow($sql);
	return put_asd_position_file($result['asd_id'],$result['asd_link'],$result['asd_content'],$result['media_type'],$result['asd_width'],$result['asd_height'],$result['asd_name']);
}
?>
