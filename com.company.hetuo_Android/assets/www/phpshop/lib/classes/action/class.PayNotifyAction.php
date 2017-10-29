<?php


/**
 * class.PayNotifyAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 */

class PayNotifyAction extends BaseAction {
	
	protected function check($objRequest, $objResponse) {
	}

	protected function service($objRequest, $objResponse) {

		switch($objRequest -> getSwitch()) {
			case "tenpay":
				$this -> doTenpay($objRequest, $objResponse);
				break;
			case "alipayreturn":
				$this -> doAlipayReturn($objRequest, $objResponse);
				break;
			case "alipaynotify":
				$this -> doAlipayNotify($objRequest, $objResponse);
				break;
			default:
				$this -> doShowPage($objRequest, $objResponse);
				break;
		}
	}

	/**
	 * 显示 
	 */
	protected function doShowPage($objRequest, $objResponse) {
		$msg = $objRequest -> msg;
		$billno = $objRequest -> b;
		
		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);
		
		$billnosuccess = ShopDao::getBillnoPaySuccess($billno);
		$billnosuccess = empty($billnosuccess) ? '0' : '1';

		$objResponse -> setTplValue("billno", $billno);
		$objResponse -> setTplValue("billnosuccess", $billnosuccess);
		$objResponse -> setTplValue("userinfo", $cookieUser);

		$objResponse -> setTplValue("__Meta", Common::getMeta());
		$objResponse -> setTplName("#Paynotyfy");
	}
	
	protected function doTenpay($objRequest, $objResponse){
		include_once(__COMMSITE . 'payinfo.php');
		require_once (__ROOT_PATH . "pay/tenpay/classes/PayResponseHandler.class.php");
		
		$resHandler = new PayResponseHandler();
		$resHandler->setKey($strSpkey);
		if($resHandler->isTenpaySign()) {
		//交易单号
			$transaction_id = $resHandler->getParameter("transaction_id");
			
			//金额,以分为单位
			$total_fee = $resHandler->getParameter("total_fee");
			
			//支付结果
			$pay_result = $resHandler->getParameter("pay_result");
			$sp_billno = $resHandler->getParameter("sp_billno");
			
			if( "0" == $pay_result ) {
				if($sp_billno > 0) {
					ShopDao::updateBillInfo($sp_billno, NULL, 1);
					$msg = 'success';
				}
			} else {
				//当做不成功处理
				$msg = 'error';
				if($sp_billno > 0) {
					ShopDao::updateBillInfo($sp_billno, NULL, 0);
				}
			}
			
		} else {
			$msg = 'error';
		}
		redirect("../paynotify.php?msg=$msg&b=$strSpBillno");
	}
	
	protected function doAlipayReturn($objRequest, $objResponse) {
		include_once(__COMMSITE . 'payinfo.php');
		require_once(__ROOT_PATH . "pay/alipay/alipay_notify.php");
		$alipay = new alipay_notify($partner,$security_code,$sign_type,$_input_charset,$transport);
		$verify_result = $alipay->return_verify();
		var_dump($verify_result);
		//获取支付宝的反馈参数
		$dingdan=$objRequest -> out_trade_no;   //获取订单号
		$total_fee=$objRequest -> price;    //获取总价格
		
		$msg = 'error';
		if($verify_result) {
			if(($objRequest -> trade_status) == 'WAIT_SELLER_SEND_GOODS') {
				$msg = 'success';
				ShopDao::updateBillInfo($dingdan, NULL, 1);
			}
		}
		//log_result ("verify_failed");
		redirect("../paynotify.php?msg=$msg&b=$dingdan");
	}
	
	protected function doAlipayNotify($objRequest, $objResponse) {
		include_once(__COMMSITE . 'payinfo.php');
		require_once(__ROOT_PATH . "pay/alipay/alipay_notify.php");
		$alipay = new alipay_notify($partner,$security_code,$sign_type,$_input_charset,$transport);
		$verify_result = $alipay->notify_verify();
		if($verify_result) {
		 //获取支付宝的反馈参数
			$dingdan=$objRequest -> out_trade_no;    //获取支付宝传递过来的订单号
			$total=$objRequest -> price;    //获取支付宝传递过来的总价格
						
			$trade_status=$objRequest -> trade_status;    //获取支付宝反馈过来的状态,根据不同的状态来更新数据库 WAIT_BUYER_PAY(表示等待买家付款);WAIT_SELLER_SEND_GOODS(表示买家付款成功,等待卖家发货);WAIT_BUYER_CONFIRM_GOODS(卖家已经发货等待买家确认);TRADE_FINISHED(表示交易已经成功结束)
			$PHPShopPay_success = 0;
			if($trade_status == 'WAIT_BUYER_PAY') $PHPShopPay_success = 0;
			if($trade_status == 'WAIT_SELLER_SEND_GOODS') $PHPShopPay_success = 1;
			if($trade_status == 'WAIT_BUYER_CONFIRM_GOODS') $PHPShopPay_success = 1;
			if($trade_status == 'TRADE_FINISHED') $PHPShopPay_success = 1;
			if(!empty($dingdan)) {
				ShopDao::updateBillInfo($dingdan, NULL, $PHPShopPay_success);
			}
			echo "success";
		}  else {
			echo "fail";
		}
		//log_result ("verify_failed");
		$this -> setDisplay();
	}
	
}
?>