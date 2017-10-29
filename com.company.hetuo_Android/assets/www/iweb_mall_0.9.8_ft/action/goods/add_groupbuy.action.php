<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
//引入模块公共方法文件
require("foundation/module_goods.php");
require_once("foundation/fsqlitem_set.php");

/* post 数据处理 */
$group_id = intval(get_args('id'));
$num = intval(get_args('num'));
$groupbuyname = short_check(get_args('groupbuyname'));
$groupbuytel = short_check(get_args('groupbuytel'));

if(!$group_id || !$num || !$groupbuyname || !$groupbuytel) {
	if (!$group_id){
		exit();
	}
	if (!$num){
		exit('-1');
	}
	if (!$groupbuyname){
		exit('-2');
	}
	if (!$groupbuytel){
		exit('-3');
	}
}
//数据表定义区
$t_cart = $tablePreStr."cart";
$t_goods = $tablePreStr."goods";
$t_goods_groupbuy = $tablePreStr."groupbuy";
$t_goods_groupbuy_log = $tablePreStr."groupbuy_log";

//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;
$sql = "select min_quantity,shop_id,purchase_num,order_num from `$t_goods_groupbuy` where group_id='$group_id'";
$groupbuyinfo = $dbo->getRow($sql);
//if ($groupbuyinfo['min_quantity'] < $num){
//	exit('-4');
//}
//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;
$insert_array = array(
	'group_id' => $group_id,
	'user_id' => $user_id,
	'user_name' => $user_name,
	'quantity' => $num,//购买数量
	'linkman' => $groupbuyname,//真实姓名
	'tel' => $groupbuytel,//联系电话
	'add_time' => $ctime->long_time(),
);
$item_sql = get_insert_item($insert_array);
$sql = "insert into `$t_goods_groupbuy_log` $item_sql ";
if($dbo->exeUpdate($sql)) {
	echo "1";
//	$new_groupbuy_num = $groupbuyinfo['min_quantity'] - $num;
	$new_groupbuy_order_num = $groupbuyinfo['purchase_num'] + $num;//订购数
	$new_groupbuy_order_number = $groupbuyinfo['order_num'];//订单数
	$sql = "update `$t_goods_groupbuy` set order_num='$new_groupbuy_order_number' where group_id='$group_id'";
	$sql_order = "update `$t_goods_groupbuy` set purchase_num='$new_groupbuy_order_num' where group_id='$group_id'";
	$dbo->exeUpdate($sql);
	$dbo->exeUpdate($sql_order);
}
?>