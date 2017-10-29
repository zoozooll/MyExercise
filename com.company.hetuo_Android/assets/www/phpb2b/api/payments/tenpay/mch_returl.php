<?php
require_once ("./classes/MediPayResponseHandler.class.php");

/* 平台商密钥 */
$key = "123456";

/* 创建支付应答对象 */
$resHandler = new MediPayResponseHandler();
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
				
				break;
			case "4":	//卖家发货成功
			
				break;
			case "5":	//买家收货确认，交易成功
			
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
	
	//------------------------------
	//处理业务完毕
	//------------------------------	
	
	//调用doShow
	$resHandler->doShow();
	
	
} else {
	echo "<br/>" . "认证签名失败" . "<br/>";
}

//echo $resHandler->getDebugInfo();

?>