<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require("foundation/module_users.php");
require("foundation/module_areas.php");
require("foundation/module_goods.php");
require("foundation/module_payment.php");
//require_once("foundation/asession.php");
//引入语言包
$m_langpackage=new moduleslp;

$group_id = intval(get_args('gid'));
if(!$group_id) { exit("非法操作"); }
//数据表定义区
$t_users = $tablePreStr."users";
$t_goods = $tablePreStr."goods";
$t_user_info = $tablePreStr."user_info";
$t_areas = $tablePreStr."areas";
$t_shop_payment = $tablePreStr."shop_payment";
$t_payment = $tablePreStr."payment";
$t_user_address = $tablePreStr."user_address";
$t_shop_groupbuy = $tablePreStr."groupbuy";
$t_shop_groupbuy_log = $tablePreStr."groupbuy_log";

$dbo=new dbex;
//读写分离定义方法
dbtarget('r',$dbServs);
$sql = "select * from $t_shop_groupbuy where group_id = $group_id";
$groupbuyinfo = $dbo->getRow($sql);

$sql="select * from $t_user_address where user_id=$user_id";

$address_rs=$dbo->getRs($sql);

$user_info=array(
	'user_country'=>'',
	'user_id'=>'',
	'user_province'=>'',
	'user_city'=>'',
	'to_user_name'=>'',
	'user_district'=>'',
	'full_address'=>'',
	'zipcode'=>'',
	'mobile'=>'',
	'telphone'=>'',
	'email'=>'',

);
if ($address_rs) {
	$user_info = $address_rs[0];
	$address_id = $address_rs[0]['address_id'];
}
// 用户所选国家， 如果没选默认为1（中国）
$user_info['user_country'] = $user_info['user_country'] ? $user_info['user_country'] : 1;
$shop_id=get_sess_shop_id();

$sql = "select g.*,l.*,t.transport_price,t.goods_name from $t_shop_groupbuy g left join $t_shop_groupbuy_log l on g.group_id = l.group_id left join $t_goods t on g.goods_id = t.goods_id where g.group_id = $group_id";
$goods_info = $dbo->getRow($sql);
if(!$goods_info) { exit("非法操作"); }

$user_info['user_id'] = $user_id;
// 用户所选国家， 如果没选默认为1（中国）
$user_info['user_country'] = $user_info['user_country'] ? $user_info['user_country'] : 1;
$areas_info = get_areas_info($dbo,$t_areas);
$sql = "select * from `$t_shop_payment` where shop_id='$goods_info[shop_id]'";
$payment_info = $dbo->getRow($sql);

$payment = get_payment_info($dbo,$t_payment);
?>