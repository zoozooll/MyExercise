<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

$dbo = new dbex;

// 数据表定义区
$t_shop_info = $tablePreStr."shop_info";
$t_goods = $tablePreStr."goods";

// 定义读操作
dbtarget('r',$dbServs);
$sql = "select goods_id,shop_id from `$t_goods` where is_on_sale=1";
$rs = $dbo->getRs($sql);
$updateshoparray = array();
if($rs) {
	foreach($rs as $value) {
		if(!isset($updateshoparray[$value['shop_id']])) {
			$updateshoparray[$value['shop_id']]=0;
		}
		$updateshoparray[$value['shop_id']]++;
	}
}

// 定义写操作
dbtarget('w',$dbServs);
if($updateshoparray) {
	foreach($updateshoparray as $k=>$v) {
		$sql = "update `$t_shop_info` set goods_num='$v' where shop_id='$k'";
		$dbo->exeUpdate($sql);
	}
}
?>