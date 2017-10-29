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
$order_info_rs = get_order_info_list($dbo,$t_order_info);

dbtarget('w',$dbServs);
$dbo = new dbex;

foreach ($order_info_rs as $key=>$val){
	if ($val['shipping_time']){
		$shipping_time = $val['shipping_time'];
		$shipping_time = date('Y-m-d H:i:s',$shipping_time+14*24*60*60);
		$now = $ctime->long_time();
		if ($now>$shipping_time){
			$update_items['order_status']='3';
			$update_items['receive_time']=$now;
			upd_order_info($dbo,$t_order_info,$update_items,$val['order_id']);

			$post['order_id']=$val['order_id'];
			$post['goods_id']=$val['goods_id'];
			$post['buyer']=$val['user_id'];
			$post['seller']=$val['shop_id'];

			$suc=insert_credit($dbo,$t_credit,$post);
		}
	}
}


?>