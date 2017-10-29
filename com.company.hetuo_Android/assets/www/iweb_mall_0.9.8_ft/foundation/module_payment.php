<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

function get_payment_info(&$dbo,$table,$enabled=0) {
	$sql = "select * from `$table`";
	if($enabled>0) {
		$sql = $sql . " where enabled='$enabled' ";
	}
	$array = $dbo->getRs($sql);
	$payment = array();
	foreach($array as $value) {
		$payment[$value['pay_id']] = $value;
	}
	return $payment;
}

function get_one_payment(&$dbo,$table,$pay_id) {
	$sql = "select * from `$table` where pay_id='$pay_id'";
	return $dbo->getRow($sql);
}

function get_one_shop_payment(&$dbo,$table,$shop_id,$pay_id) {
	$sql = "select * from `$table` where shop_id='$shop_id' and pay_id='$pay_id'";
	$rowset = $dbo->getRow($sql);
	$info = array();
	$temp = unserialize($rowset['pay_config']);
	if(is_array($temp)){
		$info = array_merge($rowset, $temp);
	}
	return $info;
}

function get_shop_payment_info(&$dbo,$table,$shop_id,$enabled=0) {
	$sql = "select * from `$table` where shop_id='$shop_id' ";
	if($enabled>0) {
		$sql = $sql . " and enabled='$enabled' ";
	}
	$rowset = $dbo->getRs($sql);
	$array = array();
	if($rowset) {
		foreach($rowset as $info) {
			$temp = unserialize($info['pay_config']);
			if(is_array($temp)){
				$info = array_merge($info, $temp);
			}
			$array[$info['pay_id']] = $info;
		}
	}
	return $array;
}

function update_payment_info(&$dbo,$table,$post,$pay_id) {
	$update_item = get_update_item($post);
	$sql = "update `$table` set $update_item where pay_id='$pay_id'";
	return $dbo->exeUpdate($sql);
}
?>