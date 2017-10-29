<?php
$m_langpackage=new moduleslp;
$t_user_favorite = $tablePreStr."user_favorite";
$t_users = $tablePreStr."users";
$t_cart = $tablePreStr."cart";
$t_goods = $tablePreStr."goods";
$t_shop_info = $tablePreStr."shop_info";
$t_remind_info = $tablePreStr."remind_info";
$t_order_info = $tablePreStr."order_info";
$t_groupbuy_log = $tablePreStr."groupbuy_log";
$t_groupbuy = $tablePreStr."groupbuy";
//读写分离定义方法
$dbo=new dbex;
dbtarget('r',$dbServs);

$sql = "SELECT COUNT(favorite_id) FROM `$t_user_favorite` WHERE user_id='$user_id'";
$row = $dbo->getRow($sql);
$my_favorite_num = $row[0];

$sql = "SELECT COUNT(cart_id) FROM `$t_cart` WHERE user_id='$user_id'";
$row = $dbo->getRow($sql);
$my_cart_num = $row[0];

$sql = "select goods_id,is_best,is_new,is_promote,is_hot,goods_number from `$t_goods` where shop_id='$shop_id'";
$rs = $dbo->getRs($sql);
$countgoods = 0;
$hot_num = 0;
$best_num = 0;
$new_num = 0;
$promote_num = 0;
$kucun_num = 0;
foreach($rs as $value) {
	if($value['is_best']) { $best_num++; }
	if($value['is_hot']) { $hot_num++; }
	if($value['is_new']) { $new_num++; }
	if($value['is_promote']) { $promote_num++; }
	if($value['goods_number']<5) { $kucun_num++; }
	$countgoods++;
}
//判断商铺是否关闭
$sql = "select open_flg from `$t_shop_info` where shop_id='$shop_id'";
$rs = $dbo->getRow($sql);
$_SESSION['shop_open']=$rs['open_flg'];
//判断商铺是否锁定
$sql = "select lock_flg from `$t_shop_info` where shop_id='$shop_id'";
$rs = $dbo->getRow($sql);
$_SESSION['shop_lock']=$rs['lock_flg'];
//获取用户信息
$sql="SELECT last_login_time,last_ip FROM $t_users WHERE user_id='$user_id'";
$user_info = $dbo->getRow($sql);
//获取未读站内信
$sql="SELECT COUNT(rinfo_id) FROM $t_remind_info WHERE user_id='$user_id' AND isread='0'";
$row = $dbo->getRow($sql);
$remind_num = $row[0];
//获得未付款订单数
$sql="SELECT COUNT(order_id) FROM $t_order_info WHERE user_id='$user_id' AND pay_status='0' AND transport_status='0' AND order_status<>'0' AND order_status<>'3'";
$row = $dbo->getRow($sql);
$order_num_need_pay=$row[0];
//获得已发货订单数
$sql="SELECT COUNT(order_id) FROM $t_order_info WHERE user_id='$user_id' AND pay_status='1' AND transport_status='1' AND order_status<>0 AND order_status<>'3'";
$row = $dbo->getRow($sql);
$order_num_send=$row[0];
//获得已完成订单数
$sql="SELECT COUNT(order_id) FROM $t_order_info WHERE user_id='$user_id' AND pay_status='1' AND transport_status='1' AND order_status='3' AND buyer_reply='0' ";
$row = $dbo->getRow($sql);
$order_num=$row[0];
//获得参加的团购活动数量
$sql="SELECT group_id FROM $t_groupbuy_log WHERE user_id='$user_id' ";
$row = $dbo->getRs($sql);
$str='0,';
foreach ($row as $value){
	$str.=$value['group_id'].",";
}
$str = substr($str,0,-1);
$group_buy_num="";

$sql="SELECT COUNT(group_id) FROM $t_groupbuy WHERE group_id IN ($str) AND recommended=1";
$row=$dbo->getRow($sql);
$group_buy_num = $row[0];
?>