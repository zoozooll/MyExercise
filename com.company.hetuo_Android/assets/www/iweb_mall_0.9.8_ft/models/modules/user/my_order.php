<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_order_info = $tablePreStr."order_info";
$t_goods = $tablePreStr."goods";
$t_shop_info = $tablePreStr."shop_info";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);
$user_id = get_sess_user_id();
$sql = "select a.order_id,a.pay_time,a.get_back_time,a.payid,a.shop_id,a.goods_id,a.goods_name,a.order_amount,a.order_time,a.order_status,a.pay_status,a.transport_status,a.seller_reply,a.group_id,a.complaint,b.goods_thumb,c.user_id,c.shop_name from `$t_order_info` as a, `$t_goods` as b, `$t_shop_info` as c where a.goods_id=b.goods_id and a.shop_id=c.shop_id and a.user_id='$user_id' ";

$sql .= " order by order_time desc";
$result = $dbo->fetch_page($sql,13);
?>