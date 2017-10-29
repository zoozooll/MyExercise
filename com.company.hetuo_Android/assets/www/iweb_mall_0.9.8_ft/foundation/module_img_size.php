<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//function get_img_size(&$dbo,$table,$id){
//	$sql = "select * from `$table` where uid='$id' ";
//	return $dbo->getRow($sql);
//}

function insert_img_size(&$dbo,$table,$insert_items){
	$item_sql = get_insert_item($insert_items);
	$sql = "insert `$table` $item_sql";
	return $dbo->exeUpdate($sql);
}

function img_size(&$dbo,$t_img_size,$insert_items,$t_shop_info,$count_imgsize,$shop_id){
	if(is_array($insert_items)){
		$set = $value = $dot = '';
		foreach($insert_items as $key=>$val){
			$count_imgsize+=$val['img_size'];
			$value .= $dot."(";
			$dot = '';
			foreach($val as $k=>$v) {
				if($key==0){
					$set .= $dot."`$k`";
				}
				$value .= $dot."'$v'";
				$dot = ",";
			}
			$value .= ")";
		}
		$sql_part = "($set) values $value";
	} else {
		exit('insert_items not array');
	}
	
	$sql = "insert `$t_img_size` $sql_part";
	$suc = $dbo->exeUpdate($sql);
	
	if($suc){
		$update_items['count_imgsize']=$count_imgsize;
		update_shop_info($dbo,$t_shop_info,$update_items,$shop_id);
	}
}
?>