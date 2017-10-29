<?php
/*
 * 注意：此文件由itpl_engine编译型模板引擎编译生成。
 * 如果您的模板要进行修改，请修改 templates/default/modules/user/order_view.html
 * 如果您的模型要进行修改，请修改 models/modules/user/order_view.php
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
if(filemtime("templates/default/modules/user/order_view.html") > filemtime(__file__) || (file_exists("models/modules/user/order_view.php") && filemtime("models/modules/user/order_view.php") > filemtime(__file__)) ) {
	tpl_engine("default","modules/user/order_view.html",1);
	include(__file__);
} else {
/* debug模式运行生成代码 结束 */
?><?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
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

$info = get_order_info($dbo,$t_order_info,$order_id,$user_id);


$areas = get_areas_kv($dbo,$t_areas);
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?php echo $m_langpackage->m_u_center;?></title>
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/modules.css">
<link rel="stylesheet" type="text/css" href="skin/<?php echo  $SYSINFO['templates'];?>/css/layout.css">
<script type="text/javascript" src="skin/<?php echo  $SYSINFO['templates'];?>/js/changeStyle.js"></script>
<style type="text/css">
td span{color:red;}
#reg_step {
	width: 770px;
	height: 29px;
	margin: 5px auto;
	line-height: 29px;
}
#reg_step ol li {
	width: 138px;
	float: left;
	font-size: 14px;
	font-weight: bold;
	background: #dedede url(skin/<?php echo  $SYSINFO['templates'];?>/images/steps_bg.gif) right 0 no-repeat;
	padding: 0 16px 0 0;
	overflow: hidden;
	text-align:center;
}
#reg_step ol li span,#reg_step ol li strong {
	display: block;.
	text-align:center;
}
#reg_step ol li .first {
	background: #dedede url(skin/<?php echo  $SYSINFO['templates'];?>/images/steps_bg.gif) -10px -58px no-repeat;
}
#reg_step ol li.last {
	background-position: right -116px;
}
#reg_step ol li.current {
	color: #ffffff;
	background-color: #F6A248;
}
#reg_step ol li.current .first {
	background-color: #F6A248;
	background-position: -10px -87px;
}
#reg_step ol li.current_prev {
	background-position: right -29px;
}
#reg_step ol li.last_current {
	background-color: #F6A248;
	color: #fff;
	background-position: right -145px;
}
td{text-align:left;}
</style>
</head>
<body>
<div class="container">
	<?php  require("modules/header.php");?>
    <div class="clear"></div>
    <div class="apart">
    	<?php  require("modules/left_menu.php");?>
        <div class="bigpart">
			<div class="title_uc"><h3><?php echo  $m_langpackage->m_view_orderinfo2;?></h3></div>
            <div id="reg_step">
			<ol>
			<?php  if($info['order_status']==0) {?>
				<li class=""><strong class="first">1、<?php echo $m_langpackage->m_view_card;?></strong></li>
				<li class=""><span>2、<?php echo $m_langpackage->m_check_order_info;?></span></li>
				<li class=""><span>3、<?php echo $m_langpackage->m_pay_to_alipay;?></span></li>
				<li class="current_prev"><span>4、<?php echo $m_langpackage->m_check_get_goods;?></span></li>
				<li class="last_current"><span>5、<?php echo $m_langpackage->m_thisorder_cancel;?></span></li>
			<?php  } elseif($info['order_status']==3) {?>
				<li class=""><strong class="first">1、<?php echo $m_langpackage->m_view_card;?></strong></li>
				<li class=""><span>2、<?php echo $m_langpackage->m_check_order_info;?></span></li>
				<li class=""><span>3、<?php echo $m_langpackage->m_pay_to_alipay;?></span></li>
				<li class="current_prev"><span>4、<?php echo $m_langpackage->m_check_get_goods;?></span></li>
				<li class="last_current"><span>5、<?php echo $m_langpackage->m_com_deal;?></span></li>
			<?php  } elseif($info['transport_status']==1){?>
				<li class=""><strong class="first">1、<?php echo $m_langpackage->m_view_card;?></strong></li>
				<li class=""><span>2、<?php echo $m_langpackage->m_check_order_info;?></span></li>
				<li class="current_prev"><span>3、<?php echo $m_langpackage->m_pay_to_alipay;?></span></li>
				<li class="current"><span>4、<?php echo $m_langpackage->m_check_get_goods;?></span></li>
				<li class="last"><span>5、<?php echo $m_langpackage->m_com_deal;?></span></li>
			<?php  } elseif($info['pay_status']==1) {?>
				<li class=""><strong class="first">1、<?php echo $m_langpackage->m_view_card;?></strong></li>
				<li class="current_prev"><span>2、<?php echo $m_langpackage->m_check_order_info;?></span></li>
				<li class="current"><span>3、<?php echo $m_langpackage->m_pay_to_alipay;?></span></li>
				<li class=""><span>4、<?php echo $m_langpackage->m_check_get_goods;?></span></li>
				<li class="last"><span>5、<?php echo $m_langpackage->m_com_deal;?></span></li>
			<?php  }elseif($info['order_status']==2) {?>
				<li class="current_prev"><strong class="first">1、<?php echo $m_langpackage->m_view_card;?></strong></li>
				<li class="current"><span>2、<?php echo $m_langpackage->m_check_order_info;?></span></li>
				<li class=""><span>3、<?php echo $m_langpackage->m_pay_to_alipay;?></span></li>
				<li class=""><span>4、<?php echo $m_langpackage->m_check_get_goods;?></span></li>
				<li class="last"><span>5、<?php echo $m_langpackage->m_com_deal;?></span></li>
			<?php  } else {?>
				<li class="current"><strong class="first">1、<?php echo $m_langpackage->m_view_card;?></strong></li>
				<li class=""><span>2、<?php echo $m_langpackage->m_check_order_info;?></span></li>
				<li class=""><span>3、<?php echo $m_langpackage->m_pay_to_alipay;?></span></li>
				<li class=""><span>4、<?php echo $m_langpackage->m_check_get_goods;?></span></li>
				<li class="last"><span>5、<?php echo $m_langpackage->m_com_deal;?></span></li>
			<?php }?>
			</ol>
		</div>
		<table align="center" class="form_table">
			<tr>
				<th style="background:#FFF2E6;" colspan="4" class="textright">&nbsp;&nbsp;<?php echo  $m_langpackage->m_goods_info;?></th>
			</tr>
			<tr>
				<th><?php echo  $m_langpackage->m_goods_name;?></th>
				<th><?php echo  $m_langpackage->m_goods_price;?></th>
				<th><?php echo  $m_langpackage->m_buy_num;?></th>
				<th><?php echo  $m_langpackage->m_transport_price;?></th>
			</tr>
			<tr>
			<td><?php echo  $info['goods_name'];?></td><td><?php echo  $info['goods_price'];?><?php echo  $m_langpackage->m_yuan;?></td>
			<td><?php echo  $info['order_num'];?></td><td><?php echo  $info['transport_price'];?><?php echo  $m_langpackage->m_yuan;?></td></tr>
			<tr><td colspan="4"><?php echo  $m_langpackage->m_order_thisbuyprice;?>：<span><?php echo  $info['order_amount'];?></span><?php echo  $m_langpackage->m_yuan;?></td></tr>
		</table>

		<table align="center" class="form_table">
			<tr>
				<th style="background:#FFF2E6;" colspan="2" class="textright">&nbsp;&nbsp;<?php echo  $m_langpackage->m_order_getsting;?></th>
			</tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_contact;?>：</td><td><?php echo  $info['consignee'];?></td></tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_mobile;?>：</td><td><?php echo  $info['mobile'];?></td></tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_telphone;?>：</td><td><?php echo  $info['telphone'];?></td></tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_email;?>：</td><td><?php echo  $info['email'];?></td></tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_stayarea;?>：</td>
			<td><?php echo  $areas[$info['province']];?> <?php echo  $areas[$info['city']];?> <?php echo  $areas[$info['district']];?></td></tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_address;?>：</td><td><?php echo  $info['address'];?></td></tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_zipcode;?>：</td><td><?php echo  $info['zipcode'];?></td></tr>
		</table>
		<table align="center" class="form_table">
			<tr>
				<th style="background:#FFF2E6;" colspan="2" class="textright">&nbsp;&nbsp;<?php echo  $m_langpackage->m_order_poststing;?></th>
			</tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_shipping_name;?>：</td><td><?php echo  $info['shipping_name'];?></td></tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_shipping_no;?>：</td><td><?php echo  $info['shipping_no'];?></td></tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_shipping_type;?>：</td><td><?php echo  $info['shipping_type'];?></td></tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_shipping_time;?>：</td><td><?php echo  $info['shipping_time'];?></td></tr>
		</table>
		<table align="center" class="form_table">
			<tr>
				<th style="background:#FFF2E6;" colspan="2" class="textright">&nbsp;&nbsp;<?php echo  $m_langpackage->m_order_info;?></th>
			</tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_order_message;?>：</td><td><?php echo  $info['message'];?></td></tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_payment_message;?>:</td><td><?php echo  $info['pay_message'];?></td></tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_alipay_tradeno;?>：</td><td><?php echo  $info['trade_no'];?></td></tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_order_time;?>：</td><td><?php echo  $info['order_time'];?></td></tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_pay_time;?>：</td><td><?php echo  $info['pay_time'];?></td></tr>
			<tr><td class="textright"><?php echo  $m_langpackage->m_receive_time;?>：</td><td><?php echo  $info['receive_time'];?></td></tr>
		</table>
        </div>
    </div>
    <div class="clear"></div>
    <?php  require("modules/footer.php");?>
</div>
</body>
</html><?php } ?>