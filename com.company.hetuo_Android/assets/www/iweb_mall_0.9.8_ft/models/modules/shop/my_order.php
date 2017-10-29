<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/acheck_shop_creat.php");
require("foundation/module_payment.php");

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_order_info = $tablePreStr."order_info";
$t_goods = $tablePreStr."goods";
$t_shop_info = $tablePreStr."shop_info";
$t_payment = $tablePreStr."payment";

$group_id = intval(get_args('id'));
$dbo = new dbex;
dbtarget('r',$dbServs);
if(empty($group_id)){
	$sql = "select a.order_id,a.payid,a.pay_id,a.get_back_time,a.shop_id,a.goods_id,a.goods_name,a.buyer_reply,a.order_amount,a.order_time,a.order_status,a.pay_status,a.transport_status,a.seller_reply,a.group_id,b.goods_thumb,c.user_id,c.shop_name from `$t_order_info` as a, `$t_goods` as b, `$t_shop_info` as c where a.shop_id='$shop_id' and a.goods_id=b.goods_id and a.shop_id=c.shop_id";
	$sql .= " order by order_time desc";
	
	$result = $dbo->fetch_page($sql,13);
}else{
	$sql = "select a.order_id,a.payid,a.pay_id,a.get_back_time,a.shop_id,a.goods_id,a.goods_name,a.order_amount,a.order_time,a.order_status,a.pay_status,a.transport_status,a.seller_reply,a.group_id,b.goods_thumb,c.user_id,c.shop_name from `$t_order_info` as a, `$t_goods` as b, `$t_shop_info` as c where a.group_id='$group_id' and a.goods_id=b.goods_id and a.shop_id=c.shop_id";
	$sql .= " order by order_time desc";
	$result = $dbo->fetch_page($sql,13);
}

$payment_info = get_payment_info($dbo,$t_payment,1);
?>