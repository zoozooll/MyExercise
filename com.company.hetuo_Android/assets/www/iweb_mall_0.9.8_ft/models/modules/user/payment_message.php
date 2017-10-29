<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require("foundation/module_order.php");
require("foundation/module_payment.php");

//引入语言包
$m_langpackage=new moduleslp;

$order_id = intval(get_args('id'));
if(!$order_id) { exit("非法操作"); }

//数据表定义区
$t_order_info = $tablePreStr."order_info";
$t_shop_payment = $tablePreStr."shop_payment";
$t_payment = $tablePreStr."payment";

$dbo=new dbex;
//读写分离定义方法
dbtarget('r',$dbServs);

$order_info = get_order_info($dbo,$t_order_info,$order_id,$user_id);
$payment_shop_info = get_shop_payment_info($dbo,$t_shop_payment,$order_info['shop_id'],1);
$payment_info = get_payment_info($dbo,$t_payment,1);

?>