<?php
//---------------------------------------------------------
//财付通即时到帐支付请求示例，商户按照此文档进行开发即可
//---------------------------------------------------------
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require_once ("classes/PayRequestHandler.class.php");
require("../../configuration.php");
require("includes.php");
require("../../foundation/module_order.php");

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

/* 财付通商户号 */
$bargainor_id = $payment_info['bargainor_id'];

/* 财付通密钥 */
$key = $payment_info['key'];

/* 返回处理地址 */
$return_url = $baseUrl."plugins/tenpay/return_url.php";

//date_default_timezone_set(PRC);
$strDate = date("Ymd");
$strTime = date("His");

//4位随机数
$randNum = rand(1000, 9999);

//10位序列号,可以自行调整。
$strReq = $strTime . $randNum;
//
/* 商家订单号,长度若超过32位，取前32位。财付通只记录商家订单号，不保证唯一。 */
$sp_billno = $orderinfo['payid'];

/* 财付通交易单号，规则为：10位商户号+8位时间（YYYYmmdd)+10位流水号 */
$transaction_id = $bargainor_id . $strDate . $strReq;

/* 商品价格（包含运费），以分为单位 */
$total_fee = $orderinfo['order_amount']*100;

/* 商品名称 */
//$transaction_id=$orderinfo['payid'];
$desc = "订单号：" . $transaction_id;

/* 创建支付请求对象 */
$reqHandler = new PayRequestHandler();
$reqHandler->init();
$reqHandler->setKey($key);

//----------------------------------------
//设置支付参数
//----------------------------------------
$reqHandler->setParameter("bargainor_id", $bargainor_id);			//商户号
$reqHandler->setParameter("sp_billno", $sp_billno);					//商户订单号
$reqHandler->setParameter("transaction_id", $transaction_id);		//财付通交易单号
$reqHandler->setParameter("total_fee", $total_fee);					//商品总金额,以分为单位
$reqHandler->setParameter("return_url", $return_url);				//返回处理地址
$reqHandler->setParameter("desc", "订单号：" . $transaction_id);	    //商品名称
$reqHandler->setParameter("cs", "utf8");	    					//编码参数

//用户ip,测试环境时不要加这个ip参数，正式环境再加此参数
//$reqHandler->setParameter("spbill_create_ip", $_SERVER['REMOTE_ADDR']);

//请求的URL
$reqUrl = $reqHandler->getRequestURL();

//debug信息
//$debugInfo = $reqHandler->getDebugInfo();
//echo "<br/>" . $reqUrl . "<br/>";
//echo "<br/>" . $debugInfo . "<br/>";

//重定向到财付通支付
$reqHandler->doSend();
?>