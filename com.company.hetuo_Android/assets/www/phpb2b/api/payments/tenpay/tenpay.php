<?php
function pay($product,$price){
require_once ("classes/MediPayRequestHandler.class.php");
//date_default_timezone_set(PRC);
$curDateTime = date("YmdHis");
$randNum = rand(1000, 9999);

/* 平台商密钥 */
$key = "123456";

/* 平台商帐号 */
$chnid = "88881481";

/* 卖家 */
$seller = "88881481";

/* 交易说明 */
$mch_desc = "交易说明";

/* 商品名称 */
$mch_name = $product;

/* 商品总价，单位为分 */
$mch_price = $price;

/* 回调通知URL */
$mch_returl = "http://localhost/athena/api/payments/tenpay/mch_returl.php";

/* 商家的定单号 */
$mch_vno = $curDateTime . $randNum;

/* 支付后的商户支付结果展示页面 */
$show_url = "http://localhost/athena/api/payments/tenpay/show_url.php";

/* 物流公司或物流方式说明 */
$transport_desc = "";

/* 需买方另支付的物流费用,以分为单位 */
$transport_fee = "";

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
$reqHandler->setParameter("mch_type", "2");						//交易类型：1、实物交易，2、虚拟交易
$reqHandler->setParameter("mch_vno", $mch_vno);					//商家的定单号
$reqHandler->setParameter("need_buyerinfo", "2");				//是否需要在财付通填定物流信息，1：需要，2：不需要。
$reqHandler->setParameter("seller", $seller);					//卖家财付通帐号
$reqHandler->setParameter("show_url",	$show_url);				//支付后的商户支付结果展示页面
$reqHandler->setParameter("transport_desc", $transport_desc);	//物流公司或物流方式说明
$reqHandler->setParameter("transport_fee", $transport_fee);		//需买方另支付的物流费用

//请求的URL
$reqUrl = $reqHandler->getRequestURL();

header("location:$reqUrl");
}
?>