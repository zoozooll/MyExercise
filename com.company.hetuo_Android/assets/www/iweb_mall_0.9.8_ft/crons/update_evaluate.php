<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

include("foundation/module_credit.php");
include("foundation/module_order.php");

//数据表定义区
$t_order_info = $tablePreStr."order_info";
$t_credit = $tablePreStr."credit";


//定义操作
dbtarget('r',$dbServs);
$dbo = new dbex;
$order_info_rs = get_order_info_orderstatus($dbo,$t_order_info);

dbtarget('w',$dbServs);
$dbo = new dbex;
foreach ($order_info_rs as $key=>$val){
	if ($val['order_status']==3){
		$receive_time = $val['receive_time'];
		$receive_time = date('Y-m-d H:i:s',$receive_time+14*24*60*60);
		$now = $ctime->long_time();
		if ($now>$receive_time){
			$update_items['seller_credit']='1';
			$update_items['seller_evaluate']='超过14天没有评价，系统自动评价为好';
			$update_items['seller_evaltime']=$now;

			update_credit($dbo,$t_credit,$update_items,$val['order_id']);
		}
	}
}

//if(!file_put_contents($file,$data)) {
//	exit("-1");
//}
?>