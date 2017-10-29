<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

function get_goods_type(&$dbo,$table) {
	$sql="select * from `$table`";
	$rowset = $dbo->getRs($sql);
	$return_array = array();
	if($rowset) {
		foreach($rowset as $value) {
			$return_array[$value['type_id']] = $value['type_name'];
		}
	}
	return $return_array;
}

?>