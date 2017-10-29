<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//引入模块公共方法文件
require("foundation/module_goods.php");
require_once("foundation/fsqlitem_set.php");

/* post 数据处理 */
$group_id = intval(get_args('id'));
if(!$group_id) {
	if (!$group_id){
		exit();
	}
}
//数据表定义区
$t_goods_groupbuy = $tablePreStr."groupbuy";
$t_goods_groupbuy_log = $tablePreStr."groupbuy_log";

//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;
$sql = "select min_quantity,purchase_num,order_num from `$t_goods_groupbuy` where group_id='$group_id'";
$groupbuyinfo = $dbo->getRow($sql);

$sql = "select quantity from `$t_goods_groupbuy_log` where group_id='$group_id'";
$exitgroupinfo = $dbo->getRow($sql);
//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql = "delete from `$t_goods_groupbuy_log` where group_id = $group_id ";
if($dbo->exeUpdate($sql)) {
	echo "1";
//	$new_groupbuy_num = $groupbuyinfo['min_quantity'] + $exitgroupinfo['quantity'];
	$new_groupbuy_order_num = $groupbuyinfo['purchase_num'] - $exitgroupinfo['quantity'];
	$new_groupbuy_order_number = $groupbuyinfo['order_num'];//订单数
	$sql = "update `$t_goods_groupbuy` set order_num='$new_groupbuy_order_number' where group_id='$group_id'";
	$sql_order = "update `$t_goods_groupbuy` set purchase_num='$new_groupbuy_order_num' where group_id='$group_id'";
	$dbo->exeUpdate($sql);
	$dbo->exeUpdate($sql_order);
}
?>