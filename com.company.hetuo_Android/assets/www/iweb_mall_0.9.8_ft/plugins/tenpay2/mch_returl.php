<?php

//---------------------------------------------------------
//财付通中介担保支付应答（处理回调）示例，商户按照此文档进行开发即可
//---------------------------------------------------------
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require_once ("./classes/MediPayResponseHandler.class.php");
require("../../configuration.php");
require("includes.php");
require("../../foundation/module_order.php");
require_once("../../foundation/ctime.class.php");

$dbo = new dbex;
dbtarget('r',$dbServs);
$t_order_info = $tablePreStr."order_info";
$t_shop_payment = $tablePreStr."shop_payment";

/* 创建支付应答对象 */
$resHandler = new MediPayResponseHandler();

$mch_vno = $resHandler->getParameter("mch_vno");

$orderinfo = get_order_info_bypayid($dbo,$t_order_info,$mch_vno);
if(!$orderinfo) {exit("非法操作");}

$shop_id=$orderinfo['shop_id'];
$pay_id = 2;

$sql="select * from $t_shop_payment where pay_id=$pay_id and shop_id=$shop_id";
$row = $dbo->getRow($sql);
$payment_info = unserialize($row['pay_config']);
/* 平台商密钥 */
$key = $payment_info['key'];

$resHandler->setKey($key);

//判断签名
if($resHandler->isTenpaySign()) {
	
	//财付通交易单号
	$cft_tid = $resHandler->getParameter("cft_tid");
	
	//金额,以分为单位
	$total_fee = $resHandler->getParameter("total_fee");
	
	//返回码
	$retcode = $resHandler->getParameter("retcode");
	
	//状态
	$status = $resHandler->getParameter("status");	
		
	//------------------------------
	//处理业务开始
	//------------------------------
	
	//注意交易单不要重复处理
	//注意判断返回金额
	
	//返回码判断
	if( "0" == $retcode ) {
		switch ($status) {
			case "1":	//交易创建
			
				break;
			case "2":	//收获地址填写完毕
			
				break;
			case "3":	//买家付款成功，注意判断订单是否重复的逻辑
				if($orderinfo['order_amount']*100==$total_fee){
					dbtarget('w',$dbServs);
					$pay_time = $ctime->long_time();
					$sql="update $t_order_info set pay_id=$pay_id,pay_status=1,pay_time='$pay_time' where payid=$sp_billno";
					$row = $dbo->exeUpdate($sql);
				} else {
					echo "支付的金额不对。";
				}
				break;
			case "4":	//卖家发货成功
				dbtarget('w',$dbServs);
				$shipping_time = $ctime->long_time();
				
				if($orderinfo) {
					$sql = "update $t_order_info set transport_status=1,shipping_time='$shipping_time' where payid='$sp_billno'";
					$dbo->exeUpdate($sql);
				}

				break;
			case "5":	//买家收货确认，交易成功
				dbtarget('w',$dbServs);
				$receive_time = $ctime->long_time();

				if($orderinfo) {
					$sql = "update $t_order_info set order_status=3,receive_time='$receive_time' where payid='$dingdan'";
					$dbo->exeUpdate($sql);
					echo "success";
				}
				break;
			case "6":	//交易关闭，未完成超时关闭
			
				break;
			case "7":	//修改交易价格成功
			
				break;
			case "8":	//买家发起退款
				
				break;
			case "9":	//退款成功
			
				break;
			case "10":	//退款关闭			
				
				break;
			default:
				//nothing to do
				break;
		}
	} else {
		echo "支付失败";
	}

	//调用doShow
	$resHandler->doShow();
	
	
} else {
	echo "<br/>" . "认证签名失败" . "<br/>";
}

//echo $resHandler->getDebugInfo();

?>