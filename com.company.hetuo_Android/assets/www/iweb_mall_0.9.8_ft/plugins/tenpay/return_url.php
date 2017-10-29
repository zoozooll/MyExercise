<?php

//---------------------------------------------------------
//财付通即时到帐支付应答（处理回调）示例，商户按照此文档进行开发即可
//---------------------------------------------------------

header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require_once ("./classes/PayResponseHandler.class.php");
require("../../configuration.php");
require("includes.php");
require("../../foundation/module_order.php");
require_once("../../foundation/ctime.class.php");

$dbo = new dbex;
dbtarget('r',$dbServs);
$t_order_info = $tablePreStr."order_info";
$t_shop_payment = $tablePreStr."shop_payment";

/* 创建支付应答对象 */
$resHandler = new PayResponseHandler();

$sp_billno = $resHandler->getParameter("sp_billno");

$orderinfo = get_order_info_bypayid($dbo,$t_order_info,$sp_billno);
if(!$orderinfo) {exit("非法操作");}

$shop_id=$orderinfo['shop_id'];
$pay_id = 2;

$sql="select * from $t_shop_payment where pay_id=$pay_id and shop_id=$shop_id";
$row = $dbo->getRow($sql);
$payment_info = unserialize($row['pay_config']);
//print_r($row);

/* 密钥 */
$key = $payment_info['key'];

$resHandler->setKey($key);

//判断签名
if($resHandler->isTenpaySign()) {
	
	//交易单号
	$transaction_id = $resHandler->getParameter("transaction_id");
	
	//金额,以分为单位
	$total_fee = $resHandler->getParameter("total_fee");
	
	//支付结果
	$pay_result = $resHandler->getParameter("pay_result");

	if( "0" == $pay_result ) {
		if($orderinfo['order_amount']*100==$total_fee){
			//------------------------------
			//处理业务开始
			//------------------------------
			dbtarget('w',$dbServs);
			$pay_time = $ctime->long_time();

			$sql="update $t_order_info set pay_id=$pay_id,pay_status=1,pay_time='$pay_time' where payid=$sp_billno";
			$row = $dbo->exeUpdate($sql);

			$show = $baseUrl."plugins/tenpay/show.php";
			$resHandler->doShow($show);
		}else{
			echo "<br/>" . "支付失败" . "<br/>";
		}
	} else {
		//当做不成功处理
		echo "<br/>" . "支付失败" . "<br/>";
	}
	
} else {
	echo "<br/>" . "认证签名失败" . "<br/>";
}

//echo $resHandler->getDebugInfo();

?>