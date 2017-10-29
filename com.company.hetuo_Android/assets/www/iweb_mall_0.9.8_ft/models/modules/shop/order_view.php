<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require("foundation/acheck_shop_creat.php");
require("foundation/module_areas.php");
require("foundation/module_order.php");

//引入语言包
$m_langpackage=new moduleslp;

$order_id = intval(get_args('order_id'));
if(!$order_id) { exit("非法操作"); }

//数据表定义区
$t_order_info = $tablePreStr."order_info";
$t_areas = $tablePreStr."areas";

$dbo=new dbex;
//读写分离定义方法
dbtarget('r',$dbServs);

$info = get_order_info($dbo,$t_order_info,$order_id,0,$shop_id);


$areas = get_areas_kv($dbo,$t_areas);
?>