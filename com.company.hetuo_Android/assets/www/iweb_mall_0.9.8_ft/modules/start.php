<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/start.html
 * 如果您的模型要进行修改，请修改 models/modules/start.php
 *
 * 修改完成之后需要您进入后台重新编译，才会重新生成。
 * 如果您开启了debug模式运行，那么您可以省去上面这一步，但是debug模式每次都会判断程序是否更新，debug模式只适合开发调试。
 * 如果您正式运行此程序时，请切换到service模式运行！
 *
 * 如您有问题请到官方论坛（http://tech.jooyea.com/bbs/）提问，谢谢您的支持。
 */
?><?php
/*
 * 此段代码由debug模式下生成运行，请勿改动！
 * 如果debug模式下出错不能再次自动编译时，请进入后台手动编译！
 */
/* debug模式运行生成代码 开始 */
if(!function_exists("tpl_engine")) {
	require("foundation/ftpl_compile.php");
}
if(filemtime("templates/default/modules/start.html") > filemtime(__file__) || (file_exists("models/modules/start.php") && filemtime("models/modules/start.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/start.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
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
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
<script type="text/javascript" src="skin/<?php echo  $SYSINFO['templates'];?>/js/changeStyle.js"></script>
<style type="text/css">

</style>
</head>
<body>
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
        <div class="bigpart">
        	<div class="infobox">
                <h3 class="welcome"><?php echo $m_langpackage->m_welcome_ucenter;?></h3>
                <p class="darkgray"><?php echo  $m_langpackage->m_last_login;?>: <?php echo $user_info['last_login_time'];?></p>
                <p class="darkgray"><?php echo $m_langpackage->m_last_ip;?>: <?php echo $user_info['last_ip'];?></p>
                <p class="darkgray"><?php echo $m_langpackage->m_you_have;?> <span class="highlight bold"><?php echo $remind_num;?></span> <?php echo $m_langpackage->m_site_rem;?>，<a href="modules.php?app=user_remind_info" class="highlight bold"><?php echo $m_langpackage->m_click_view;?></a></p>
            </div>
        	<div class="title_uc"><h3><?php echo $m_langpackage->m_order_remind;?></h3></div>
            <div class="infobox">
                <p class="darkgray"><?php echo $m_langpackage->m_you_have;?> <span class="highlight bold"><?php echo $order_num_need_pay;?></span> <?php echo $m_langpackage->m_pending_payment_order;?>“<a href="modules.php?app=user_my_order" class="highlight bold"><?php echo $m_langpackage->m_payment_orders_be;?></a>”<?php echo $m_langpackage->m_in_payment;?></p>
                <p class="darkgray"><?php echo $m_langpackage->m_you_have;?> <span class="highlight bold"><?php echo $order_num_send;?></span> <?php echo $m_langpackage->m_seller_shipped_orders;?>“<a href="modules.php?app=user_my_order" class="highlight bold"><?php echo $m_langpackage->m_shipped_orders;?></a>”</p>
                <p class="darkgray"><?php echo $m_langpackage->m_you_have;?> <span class="highlight bold"><?php echo $order_num;?></span> <?php echo $m_langpackage->m_order_not_evaluated;?>“<a href="modules.php?app=user_my_order" class="highlight bold"><?php echo $m_langpackage->m_completed_orders;?></a>”<?php echo $m_langpackage->m_confirmed;?></p>
            </div>
        	<div class="title_uc"><h3><?php echo $m_langpackage->m_groupbuy_remind;?></h3></div>
            <div class="infobox">
                <p class="darkgray"><?php echo $m_langpackage->m_you_have;?> <span class="highlight bold"><?php echo $group_buy_num;?></span> <?php echo $m_langpackage->m_groupbuy_attended_activities;?>“<a href="modules.php?app=user_group" class="highlight bold"><?php echo $m_langpackage->m_group_buy_completed;?></a>”<?php echo $m_langpackage->m_purchased;?></p>
            </div>
        </div>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
</div>
</body>
</html><?php } ?>