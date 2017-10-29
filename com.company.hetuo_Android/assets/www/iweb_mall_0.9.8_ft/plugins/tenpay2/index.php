<?php
//---------------------------------------------------------
//财付通中介担保支付请求示例，商户按照此文档进行开发即可
//---------------------------------------------------------
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require_once ("classes/MediPayRequestHandler.class.php");
require("../../configuration.php");
require("includes.php");
require("../../foundation/module_order.php");
require("../../foundation/fstring.php");

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

//date_default_timezone_set(PRC);
$curDateTime = date("YmdHis");
$randNum = rand(1000, 9999);

/* 平台商密钥 */
$key = $payment_info['key'];

/* 平台商帐号 */
$chnid = $payment_info['bargainor_id'];

/* 卖家 */
$seller = $payment_info['bargainor_id'];

/* 交易说明 */
$mch_desc = sub_str(preg_replace("/[^a-z0-9\x80-\xff]+/i",'',$orderinfo['goods_name']),42,false);

/* 商品名称 */
$mch_name = sub_str(preg_replace("/[^a-z0-9\x80-\xff]+/i",'',$orderinfo['goods_name']),20,false);

/* 商品总价，单位为分 */
$mch_price = $orderinfo['order_amount']*100;

/* 回调通知URL */
$mch_returl = $baseUrl."plugins/tenpay2/mch_returl.php";

/* 商家的定单号 */
$mch_vno = $orderinfo['payid'];

/* 支付后的商户支付结果展示页面 */
$show_url = $baseUrl."plugins/tenpay2/show_url.php";

/* 物流公司或物流方式说明 */
$transport_desc = "0";

/* 需买方另支付的物流费用,以分为单位 */
$transport_fee = "0";

/* 创建支付请求对象 */
$reqHandler = new MediPayRequestHandler();
$reqHandler->init();
$reqHandler->setKey($key);

//----------------------------------------
//设置支付参数
//----------------------------------------
$reqHandler->setParameter("chnid", $chnid);						//平台商帐号
$reqHandler->setParameter("encode_type", "2");					//编码类型 1:gbk 2:utf-8
$reqHandler->setParameter("mch_desc", $mch_desc);				//交易说明
$reqHandler->setParameter("mch_name", $mch_name);				//商品名称
$reqHandler->setParameter("mch_price", $mch_price);				//商品总价，单位为分
$reqHandler->setParameter("mch_returl", $mch_returl);			//回调通知URL
$reqHandler->setParameter("mch_type", "1");						//交易类型：1、实物交易，2、虚拟交易
$reqHandler->setParameter("mch_vno", $mch_vno);					//商家的定单号
$reqHandler->setParameter("need_buyerinfo", "2");				//是否需要在财付通填定物流信息，1：需要，2：不需要。
$reqHandler->setParameter("seller", $seller);					//卖家财付通帐号
$reqHandler->setParameter("show_url",	$show_url);				//支付后的商户支付结果展示页面
$reqHandler->setParameter("transport_desc", $transport_desc);	//物流公司或物流方式说明
$reqHandler->setParameter("transport_fee", $transport_fee);		//需买方另支付的物流费用

//请求的URL
$reqUrl = $reqHandler->getRequestURL();

/*
echo '<br />$chnid='.$chnid;
echo '<br />$$mch_desc='.$mch_desc;
echo '<br />$$mch_name='.$mch_name;
echo '<br />$$mch_price='.$mch_price;
echo '<br />$$mch_returl='.$mch_returl;
echo '<br />$$mch_vno='.$mch_vno;
echo '<br />$$seller='.$seller;
echo '<br />$$show_url='.$show_url;
echo '<br />$$transport_desc='.$transport_desc;
echo '<br />$$transport_fee='.$transport_fee;

debug信息
$debugInfo = $reqHandler->getDebugInfo();
echo "<br/>" . $reqUrl . "<br/>";
echo "<br/>" . $debugInfo . "<br/>";
*/

//重定向到财付通支付
$reqHandler->doSend();
?>