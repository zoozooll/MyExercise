<?php


/**
 * class.PayNotifyAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	��Ȩ���У���������ǼǺţ�2009SR06466 ����
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
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
	 * ��ʾ 
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
		//���׵���
			$transaction_id = $resHandler->getParameter("transaction_id");
			
			//���,�Է�Ϊ��λ
			$total_fee = $resHandler->getParameter("total_fee");
			
			//֧�����
			$pay_result = $resHandler->getParameter("pay_result");
			$sp_billno = $resHandler->getParameter("sp_billno");
			
			if( "0" == $pay_result ) {
				if($sp_billno > 0) {
					ShopDao::updateBillInfo($sp_billno, NULL, 1);
					$msg = 'success';
				}
			} else {
				//�������ɹ�����
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
		//��ȡ֧�����ķ�������
		$dingdan=$objRequest -> out_trade_no;   //��ȡ������
		$total_fee=$objRequest -> price;    //��ȡ�ܼ۸�
		
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
		 //��ȡ֧�����ķ�������
			$dingdan=$objRequest -> out_trade_no;    //��ȡ֧�������ݹ����Ķ�����
			$total=$objRequest -> price;    //��ȡ֧�������ݹ������ܼ۸�
						
			$trade_status=$objRequest -> trade_status;    //��ȡ֧��������������״̬,���ݲ�ͬ��״̬���������ݿ� WAIT_BUYER_PAY(��ʾ�ȴ���Ҹ���);WAIT_SELLER_SEND_GOODS(��ʾ��Ҹ���ɹ�,�ȴ����ҷ���);WAIT_BUYER_CONFIRM_GOODS(�����Ѿ������ȴ����ȷ��);TRADE_FINISHED(��ʾ�����Ѿ��ɹ�����)
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