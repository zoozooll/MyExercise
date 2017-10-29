<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require("foundation/module_order.php");

//语言包引入
$m_langpackage=new moduleslp;

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

//定义文件表
$t_order_info = $tablePreStr."order_info";
$t_cart = $tablePreStr."cart";
$t_groupbuy = $tablePreStr."groupbuy";
$t_groupbuy_log = $tablePreStr."groupbuy_log";

// 处理post变量

$post['group_id'] = intval(get_args('group_id'));
$post['shop_id'] = intval(get_args('sshop_id'));
$post['user_id'] = $user_id;
$post['goods_id'] = intval(get_args('goods_id'));
//$post['pay_id'] = intval(get_args('pay_id'));
//$post['pay_name'] = short_check(get_args('pay_name'));
$post['goods_name'] = short_check(get_args('goods_name'));
$post['goods_price'] = floatval(get_args('goods_price'));
$post['transport_price'] = floatval(get_args('transport_price'));
$post['order_amount'] = floatval(get_args('order_amount'));
$post['order_num'] = intval(get_args('order_num'));
$post['consignee'] = short_check(get_args('to_user_name'));
$post['country'] = intval(get_args('country'));
$post['province'] = intval(get_args('province'));
$post['city'] = intval(get_args('city'));
$post['district'] = intval(get_args('district'));
$post['mobile'] = short_check(get_args('mobile'));
$post['email'] = short_check(get_args('email'));
$post['telphone'] = short_check(get_args('telphone'));
$post['zipcode'] = short_check(get_args('zipcode'));
$post['address'] = long_check(get_args('full_address'));
$post['message'] = long_check(get_args('message'));
$post['order_time'] = $ctime->long_time();
$rand = rand(10,99);
$post['payid'] = date("YmdHis".$rand);
$goods_id=$post['goods_id'];
$group_id=$post['group_id'];

if(insert_order_info($dbo,$t_order_info,$post)) {
	if ($post['group_id']){
		$group_id=$post['group_id'];
		$sql = "update `$t_groupbuy` set order_num=order_num+1  where group_id=$group_id ";
		$dbo->exeUpdate($sql);
	}

	$sql = "delete from `$t_cart` where user_id=$user_id and goods_id=$goods_id ";
	$dbo->exeUpdate($sql);
	$sql = "delete from `$t_groupbuy_log` where user_id=$user_id and group_id=$group_id ";
	$dbo->exeUpdate($sql);
	action_return(1,'','modules.php?app=user_order_success&id='.$post['payid']);
} else {
	action_return(0,$m_langpackage->m_order_fail,'-1');
}
exit;
?>