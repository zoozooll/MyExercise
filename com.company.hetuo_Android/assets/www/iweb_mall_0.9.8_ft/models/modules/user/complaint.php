<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require("foundation/module_complaint.php");

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_order_info = $tablePreStr."order_info";
$t_shop_info = $tablePreStr."shop_info";
$t_complaint_type = $tablePreStr."complaint_type";

//变量定义区
$order_id=intval(get_args('order_id'));

$dbo=new dbex;
//读写分离定义方法
dbtarget('r',$dbServs);

$sql = "select a.order_id,a.payid,a.shop_id,a.goods_id,a.goods_name,a.order_amount,a.order_time,a.order_status,a.pay_status,a.transport_status,a.seller_reply,a.group_id,c.user_id,c.shop_name from `$t_order_info` as a, `$t_shop_info` as c where a.shop_id=c.shop_id and a.order_id='$order_id' ";

$order_rs=$dbo->getRow($sql);

$complaints_title=get_complaint_type_all($dbo,"*",$t_complaint_type);

//$complaints_title=array(
//	'1'=>'成交不卖',
//	'2'=>'收款不发货',
//	'3'=>'商品与描述不符',
//	'4'=>'评价纠纷',
//	'5'=>'卖家拒绝履行交易',
//	'6'=>'退款纠纷',
//	'7'=>'卖家要求买家先确认收货，卖家再发货',
//	'8'=>'诚保代充-未按时发货索赔',
//	'9'=>'卖家缺货无法交易',
//);

?>