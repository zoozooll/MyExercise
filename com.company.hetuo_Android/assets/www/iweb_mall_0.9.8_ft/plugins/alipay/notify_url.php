<?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;
require("../../configuration.php");
require("includes.php");
require("../../foundation/module_order.php");
require_once("alipay_notify.php");

$dbo = new dbex;
dbtarget('r',$dbServs);
$t_order_info = $tablePreStr."order_info";
$t_shop_payment = $tablePreStr."shop_payment";

//获取支付宝的反馈参数
$dingdan    = $_GET['out_trade_no'];   //获取订单号
$total_fee  = $_GET['total_fee'];      //获取总价格
$receive_name    = $_GET['receive_name'];    //获取收货人姓名
$receive_address = $_GET['receive_address']; //获取收货人地址
$receive_zip     = $_GET['receive_zip'];     //获取收货人邮编
$receive_phone   = $_GET['receive_phone'];   //获取收货人电话
$receive_mobile  = $_GET['receive_mobile'];  //获取收货人手机

// 获取订单信息
$orderinfo = get_order_info_bypayid($dbo,$t_order_info,$dingdan);

if(!$orderinfo) {exit("非法操作");}

// 获取支付配置信息
$sql = "SELECT * FROM $t_shop_payment WHERE shop_id='".$orderinfo['shop_id']."' and pay_id=1";

$row = $dbo->getRow($sql);
$payment_info = unserialize($row['pay_config']);
$partner        = $payment_info['partner'];
$security_code  = $payment_info['security_code'];
$seller_email   = $payment_info['seller_email'];
$_input_charset = "utf-8";
$sign_type      = "MD5";
$transport      = "http";
$notify_url     = $baseUrl."plugins/alipay/notify_url.php";
$return_url     = $baseUrl."plugins/alipay/return_url.php";
$show_url       = $baseUrl."goods.php?id=".$orderinfo['goods_id'];
unset($row);
unset($payment_info);

$alipay = new alipay_notify($partner,$security_code,$sign_type,$_input_charset,$transport);
$verify_result = $alipay->notify_verify();

if($verify_result) {   //认证合格

	if($_POST['trade_status'] == 'WAIT_BUYER_PAY') {         //等待买家付款
		echo "success";
		log_result("verify_success.WAIT_BUYER_PAY");
	} else if($_POST['trade_status'] == 'WAIT_SELLER_SEND_GOODS') {      //买家付款成功,等待卖家发货

		dbtarget('w',$dbServs);
		$pay_time = $ctime->long_time();

		if($orderinfo) {
			$sql = "update $t_order_info set pay_id=1,pay_status=1,pay_time='$pay_time',trade_no='$trade_no' where payid='$dingdan'";
			$dbo->exeUpdate($sql);
			echo "success";
		}
		
		log_result("verify_success.WAIT_SELLER_SEND_GOODS");
	} else if($_POST['trade_status'] == 'WAIT_BUYER_CONFIRM_GOODS') {    //卖家已经发货等待买家确认

		dbtarget('w',$dbServs);
		$shipping_time = $ctime->long_time();
		
		if($orderinfo) {
			$sql = "update $t_order_info set transport_status=1,shipping_time='$shipping_time' where payid='$dingdan'";
			$dbo->exeUpdate($sql);
			echo "success";
		}
		log_result("verify_success.WAIT_BUYER_CONFIRM_GOODS");
	} else if($_POST['trade_status'] == 'TRADE_FINISHED' || $_POST['trade_status'] == 'TRADE_SUCCESS') {	//交易成功结束

		dbtarget('w',$dbServs);
		$receive_time = $ctime->long_time();

		if($orderinfo) {
			$sql = "update $t_order_info set order_status=3,receive_time='$receive_time' where payid='$dingdan'";
			$dbo->exeUpdate($sql);
			echo "success";
		}
		
		log_result("verify_success.TRADE_FINISHED.TRADE_SUCCESS");
	} else {
		echo "fail";
		log_result ("verify_failed");
	}
} else {
	echo "fail";
	log_result ("verify_failed");
}

function  log_result($word) { 
	//$fp = fopen("log.txt","a");	
	//flock($fp, LOCK_EX) ;
	//fwrite($fp,$word."：执行日期：".strftime("%Y%m%d%H%I%S",time())."\t\n");
	//flock($fp, LOCK_UN); 
	//fclose($fp);
}
?>