<?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require("../../configuration.php");
require("includes.php");
require("../../foundation/module_order.php");
require_once("alipay_service.php");

// 连接数据库 初始操作
$dbo = new dbex;
dbtarget('r',$dbServs);
$order_id = intval($_GET['id']);
$pay_id = intval($_GET['pay_id']);

if(!$order_id && !$pay_id) {exit("Error");}
$t_order_info = $tablePreStr."order_info";
$t_shop_payment = $tablePreStr."shop_payment";

// 获取订单信息
$orderinfo = get_order_info($dbo,$t_order_info,$order_id);
if(!$orderinfo) {exit("非法操作");}

// 获取支付配置信息
$sql = "SELECT * FROM $t_shop_payment WHERE shop_id='".$orderinfo['shop_id']."' and pay_id=$pay_id";
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

$parameter = array(
	"service"        => "create_partner_trade_by_buyer",
	"partner"        => $partner,
	"return_url"     => $baseUrl."plugins/alipay/return_url.php",
	"notify_url"     => $baseUrl."plugins/alipay/notify_url.php",
	"_input_charset" => $_input_charset,
	"subject"        => iconv("UTF-8", "GBK", $orderinfo['goods_name']),

	"body"           => $orderinfo['message'],
	"out_trade_no"   => $orderinfo['payid'],
	"price"          => $orderinfo['order_amount'],
	"payment_type"	 => "1",
	"quantity"       => "1",

	"logistics_fee"      =>'0.00',
	"logistics_payment"  =>'BUYER_PAY',
	"logistics_type"     =>'EXPRESS',

	"show_url"       => $show_url,
	"seller_email"   => $seller_email
);

$alipay = new alipay_service($parameter,$security_code,$sign_type);
$link=$alipay->create_url();
echo "<script>window.location =\"$link\";</script>";
?>