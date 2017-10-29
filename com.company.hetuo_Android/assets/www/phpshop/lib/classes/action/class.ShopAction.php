<?php


/**
 * class.ShopAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	��Ȩ���У���������ǼǺţ�2009SR06466 ����
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
 */

class ShopAction extends BaseAction {
	
	protected function check($objRequest, $objResponse) {
	}

	protected function service($objRequest, $objResponse) {

		switch($objRequest -> getSwitch()) {
			case 'iframeshop':
				$this -> doIframeShop($objRequest, $objResponse);
				break;
			case 'buy':
				$this -> doBuy($objRequest, $objResponse);
				break;	
			case 'checkout':
				$this -> doCheckout($objRequest, $objResponse);
				break;	
			case 'pay':
				$this -> doPay($objRequest, $objResponse);
				break;
			case 'billno':
				$this -> doShowBillno($objRequest, $objResponse);
				break;
			default:
				$this -> doShowPage($objRequest, $objResponse);
				break;
		}
	}

	protected function doIframeShop($objRequest, $objResponse) {
		$act = $objRequest -> act;
		$pid = $objRequest -> pid;
		
		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);
		if(!isset($cookieUser['id'])) $cookieUser['id'] = '';
		$md5id = $objCookie -> muantshopid;
		
		$arrAddProduct  = array();
		if($act == 'add' && !empty($pid)) {
			ShopDao::insertTmpShop($pid, $cookieUser['id'], $md5id, onLineIp());
			$arrAddProduct = ProductDao::getProduct(NULL, $pid);
			unset($objCookie -> ischeckout);
		}
		if($act == 'del' && !empty($pid)) {
			ShopDao::deleteTmpShop($pid, $md5id, $cookieUser['id']);
		}
		if($act == 'clear') {
			ShopDao::deleteTmpShop(NULL, $md5id, $cookieUser['id']);
		}
		if($act == 'num') {
		    $arrNum = $objRequest -> num;
			$num = count($pid);
			for($i = 0; $i<$num; $i++) {
				ShopDao::updateTmpShop($pid[$i], $md5id, $cookieUser['id'], $arrNum[$i]);
			}
		}
		self::getTmpShop($md5id, $cookieUser['id'], $objRequest, $objResponse);
		//�����ؼ���
		$objResponse -> setTplValue("keyword", '');
		$objResponse -> setTplValue("addproduct", $arrAddProduct);
		$objResponse -> setTplName("#Iframeshop");
	}
	
	protected function doBuy($objRequest, $objResponse) {
		$isusepay = NULL;
		$usealipay = NULL;
		$usetenpay = NULL;
		@include_once(__COMMSITE . 'payinfo.php');
		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);
		
		if(!isset($cookieUser['id'])) $cookieUser['id'] = '';
		$md5id = $objCookie -> muantshopid;
		self::getTmpShop($md5id, $cookieUser['id'], $objRequest, $objResponse);
		$arrUserInfo = array();
		if(!empty($cookieUser['id'])) {
			$arrUserInfo = UserDao::getUserByLoginid($cookieUser['email'], $cookieUser['loginid']);
			if(empty($arrUserInfo)) {
				unset($objCookie -> muantshop);
				unset($objCookie -> muantshopid);
				throw new Exception('error user id:'.$cookieUser['id']);
			}
		}
		//$nofreight = Common::getSiteInfo('value1', "regAndCon");
		$sellprice = $objResponse -> getTplValues("allprice");
		//if(!empty($nofreight)) {
			//if($sellprice >= $nofreight) $nofreight = 'yes';
		//}
		
		//$objResponse -> setTplValue("nofreight", $nofreight);
		$objResponse -> setTplValue("freight", ClassesDao::getFreight());
		$objResponse -> setTplValue("bank", ClassesDao::getBank());
		$objResponse -> setTplValue("isusepay", $isusepay);
		$objResponse -> setTplValue("usealipay", $usealipay);
		$objResponse -> setTplValue("usetenpay", $usetenpay);
		$objResponse -> setTplValue("userinfo", $arrUserInfo);
		$objResponse -> setTplValue("buy", 1);
		//�����ؼ���
		$objResponse -> setTplValue("keyword", '');
		$objResponse -> setTplValue("__Meta", Common::getMeta());
		$objResponse -> setTplName("#Buy");
	}
	
	protected function doCheckout($objRequest, $objResponse) {
		$objCookie = new Cookie;
		if(($objCookie -> ischeckout) == 'yes') {
			redirect('pay.php');
		}
		
		$cookieUser = Common::getLoginUser($objCookie);
		$md5id = $objCookie -> muantshopid;
		if(!isset($cookieUser['id'])) $cookieUser['id'] = '';
		
		self::getTmpShop($md5id, $cookieUser['id'], $objRequest, $objResponse);
		$arrTmpshop = $objResponse -> getTplValues("tmpshop");
		if(empty($arrTmpshop)) {
			//set common value
			$objResponse -> setTplValue("userinfo", $cookieUser);
			$objResponse -> setTplValue("__Meta", Common::getMeta());
			$objResponse -> setTplName("#NoPay");
			return;
		}
		//if(empty($cookieUser['id'])) {
		$arrValues["name"] = trim($objRequest -> name);
		$arrValues["email"] = trim($objRequest -> email);
		if(Check::checkEmail($arrValues["email"]) == false || empty($arrValues["name"])) {
			alert(conutf8('Email �������޸ġ�'));
			redirect('-1');
		}

		//$arrValues["password"] = '';
		$arrValues["phone"] = trim($objRequest -> phone);
		$arrValues["mobile"] = trim($objRequest -> mobile);
		$arrValues["address"] = trim($objRequest -> address);
		$arrValues["postcode"] = trim($objRequest -> postcode);
		$arrValues["ss"] = trim($objRequest -> ss);
		//$arrValues["sex"] = '';
		//$arrValues["birthday"] = '';
		//$arrValues["qq"] = '';
		//$arrValues["msn"] = '';
		//$arrValues["taobao"] = '';
		//$arrValues["skype"] = '';
		$arrValues["userip"] = onLineIp();
		//$validateMd5 = md5(uniqid(rand(), true));
		//$arrValues["loginid"] = $validateMd5;
		//$arrValues["rember_times"] = 0;
		//$arrValues["isreg"] = 0;
		//UserDao::insertUser($arrValues);
		//$uid = UserDao::getInsertUserId();
		//unset($arrValues);
		//} else {
			//
		//}
		include_once(__ROOT_PATH.'etc/paymetho.php');
		$freightid = $objRequest -> freight;
		$paymethodid = $objRequest -> paymethod;
		if($paymethodid == 'W') {
			$bank_type = $objRequest -> bank_type;
		}
		$arrFreight = ClassesDao::getFreight($freightid);
		$bank = $arrPaymetho[$paymethodid];

		$billno = '1';
		$arrValues['billno'] = $billno;
		$arrValues['uid'] = $cookieUser['id'];
		$arrValues['sellprice'] = $objResponse -> getTplValues("allprice");
		//$nofreight = Common::getSiteInfo('value1', "regAndCon");
		$freight = $arrFreight['freight'];
		//if(!empty($nofreight)) {
			//if($arrValues['sellprice'] >= $nofreight) $freight = '0';
		//}
		//�˷�
		$freight = '0';
		if($arrFreight['freight'] > 0) {
			if($arrFreight['shop_price'] > 0) {
				if($arrFreight['shop_price'] > $arrValues['sellprice']) {
					$freight = $arrFreight['freight'];
				}
			} else {
				$freight = $arrFreight['freight'];
			}
		}
		//
		$arrValues['pay_price'] = $arrValues['sellprice'] + $freight;
		$arrValues['state'] = 0;
		$arrValues['md5id'] = $md5id;
		$arrValues['freight'] = $arrFreight['name'];
		$arrValues['freight_price'] = $freight;//$arrFreight['freight'];
		$arrValues['paymethod'] = $bank;
		$arrValues['web_pay_code'] = trim($objRequest -> bank_type);
		$arrValues['host'] = getHost();
		$arrValues['cps'] = $objCookie -> cps;
		
		ShopDao::insertOrderBill($arrValues);
		$billid = ShopDao::getInsertId();
		
		$billno = $billid.'0';
		for($i = strlen($billno) + 1; $i<=10; $i++) {
			$billno .= rand(1, 9);
		}
		ShopDao::updateOrderBillId($billno, $billid);
		ShopDao::insertProductBill($billno, $md5id);
		ShopDao::deleteTmpShop(NULL, $md5id, $cookieUser['id']);
		//���¿��
		//$str_pid = '';
		$num = count($arrTmpshop);
		for($i =0; $i < $num; $i++) {
			//$str_pid = "'" . $arrTmpshop[$i]['pid'] . "',";
			ShopDao::updatePrdecuStore($arrTmpshop[$i]['pid'], $arrTmpshop[$i]['num']);
		}
		//$str_pid = trim($str_pid, ',');
		//if($str_pid != '') {
		//}
		//
		
		$objCookie -> ischeckout = 'yes';
		$objCookie -> billno = $billno;
		//send email
		$shop_web_name = Common::getSiteInfo('value1');
		$shop_user_name = $arrValues["name"];
		$shop_web_phone = Common::getSiteInfo('value6', 'service');
		$shop_web_mobile = Common::getSiteInfo('value7', 'service');
		$shop_user_address = $arrValues["address"];
		$shop_user_phone = $arrValues["phone"];
		$shop_user_mobile = $arrValues["mobile"];
		$shop_user_pay = $arrValues['pay_price'];
		$shop_user_freight = $arrValues['freight'];
		$shop_web_billno_url = 'http://'.Common::getSiteInfo('value3').'/getshopinfo.php?billno='.$billno.'&md='.$md5id;
		$shop_web_url = Common::getSiteInfo('value3');
		include_once(__ROOT_PATH.'etc/ordermail.php');
		$ordermail = conutf8($ordermail, 'UTF-8', 'GB2312');
		Common::sendMail($arrValues["email"], Common::getSiteInfo('value9', "service"), $ordermail, conutf8(Common::getSiteInfo('value1'), 'UTF-8', 'GB2312') . ' ( ����ȷ��֪ͨ )', 'gb2312');
		//
		redirect('pay.php');
	}
	
	protected function doPay($objRequest, $objResponse) {
		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);

		if(!isset($cookieUser['id'])) $cookieUser['id'] = '';
		$md5id = $objCookie -> muantshopid;
		$billno = $objCookie -> billno;
		//set common value
		$objResponse -> setTplValue("userinfo", $cookieUser);
		$objResponse -> setTplValue("__Meta", Common::getMeta());
		
		if(($objCookie -> ischeckout) != 'yes') {
			$objResponse -> setTplName("#NoPay");
			return;
		} 
		
		if(empty($billno)) {
			$objResponse -> setTplName("#NoPay");
			return;
		}
		self::getTmpShop($md5id, $cookieUser['id'], $objRequest, $objResponse, $billno);
		$arrTmpshop = $objResponse -> getTplValues("tmpshop");
		if(empty($arrTmpshop)) {
			$objResponse -> setTplName("#NoPay");
			return;
		}

		$arrBillInfo = ShopDao::getBillInfo($billno);
		if(empty($arrBillInfo)) {
			$objResponse -> setTplName("#NoPay");
			return;
		}
		$web_url = "http://".Common::getSiteInfo('value3') . '/';
		//web pay
		$this -> webPay($billno, $arrBillInfo, $web_url, $objResponse);
		//end web pay		
		
		$objResponse -> setTplValue("billnoinfo", $arrBillInfo);
		$objResponse -> setTplValue("bank", ClassesDao::getBank());
		//$objResponse -> setTplValue("weburl", $web_url);
		//�����ؼ���
		$objResponse -> setTplValue("keyword", '');
		$objResponse -> setTplName("#Pay");
	}
	
	protected function webPay($billno, $arrBillInfo, $web_url, $objResponse) {
		$usealipay = '';
		include_once(__COMMSITE . 'payinfo.php');
		$alipaylink = NULL;
		$tenpaylink = NULL;
		$arrTenpay = NULL;
		/*$objResponse -> setTplValue("isusepay", $isusepay);
		$objResponse -> setTplValue("usealipay", $usealipay);
		$objResponse -> setTplValue("usetenpay", $usetenpay);*/
		//web url
		//$web_url = "http://".Common::getSiteInfo('value3') . '/';
		//alipay
		if($usealipay == '1') {
			require_once("pay/alipay/alipay_service.php");
			
			$parameter = array(
			/////////////////������ĵı������/////////////////////////////
				"service"        => "create_partner_trade_by_buyer",  //�������� ʵ���׼˫�ӿڷ������ƣ�trade_create_by_buyer �� �н鵣�����ף����������ף��������ƣ�create_partner_trade_by_buyer
				"payment_type"   => "1",                      //Ĭ��Ϊ1,����Ҫ�޸�
				"_input_charset" => $_input_charset,          //�ַ�����Ĭ��ΪGBK
				
			/////////////////����Ĳ���////////////////////////////////////
				"partner"        => $partner,                 //���������ID
				"seller_email"   => $seller_email,            //�տ�֧�����˺ţ�һ����ǩԼ֧�����˺�
				"out_trade_no"   => $billno,                  //�̼���վΨһ�����ţ���֤�̼���վ����ϵͳ����Ķ�����Ψһ��
				"subject"        => $subject . $billno,                 //��Ʒ���ƣ���������
				"price"          => $arrBillInfo['pay_price'],                   //��Ʒ���ۣ������ܼۣ��۸���Ϊ0��
				"quantity"       => "1",                      //��Ʒ������������Ʒ����ʱ��������
				
				"logistics_fee"      => '0.00',               //�������ͷ���
				"logistics_payment"  => 'BUYER_PAY',          //�������ø��ʽ��SELLER_PAY(����֧��)��BUYER_PAY(���֧��)��BUYER_PAY_AFTER_RECEIVE(��������)
				"logistics_type"     => 'EXPRESS',            //�������ͷ�ʽ��POST(ƽ��)��EMS(EMS)��EXPRESS(�������)
				
			/////////////////������д����Ҫ�Ĳ���/////////////////////////
				"return_url"     => $return_url,              //ͬ������,����ҳ·����ַ
				"notify_url"     => $notify_url,              //�첽���أ�֪ͨҳ·����ַ
				"body"           => $body,                    //��Ʒ������������ע
			
				"show_url"       => $show_url,                 //��Ʒ�����վ
			
			////////////////�������õ�ѡ�����///////////////////////////
				//�ջ�����Ϣ�������ٺ�����Щ�����е�receive_name��receive_address��receive_zipʱ������֧��������̨�Ժ�Ĳ��������л���������ջ�����Ϣ��һ��
				"receive_name"	 => '',//$receive_name,		   //�ջ�������
				"receive_address"=> '',//$receive_address,	   //�ջ��˵�ַ
				"receive_zip"	 => '',//$receive_zip,		   //�ջ����ʱ�
				"receive_phone"  => '',//$receive_phone,		   //�ջ�����ϵ�绰
				"receive_mobile" => '',//$receive_mobile,	   //�ջ����ֻ�
				
				"buyer_email"	 => ''//$buyer_email		   //���֧�����˺�
			/*
				"logistics_fee_1"    => $logistics_fee_1,             //�������ͷ���
				"logistics_payment_1"=> $logistics_payment_1,        //�������ø��ʽ��SELLER_PAY(����֧��)��BUYER_PAY(���֧��)��BUYER_PAY_AFTER_RECEIVE(��������)
				"logistics_type_1"   => $logistics_type_1,              //�������ͷ�ʽ��POST(ƽ��)��EMS(EMS)��EXPRESS(�������)
				
				"logistics_fee_2"    => $logistics_fee_2,             //�������ͷ���
				"logistics_payment_2"=> $logistics_payment_2,        //�������ø��ʽ��SELLER_PAY(����֧��)��BUYER_PAY(���֧��)��BUYER_PAY_AFTER_RECEIVE(��������)
				"logistics_type_2"   => $logistics_type_2             //�������ͷ�ʽ��POST(ƽ��)��EMS(EMS)��EXPRESS(�������)
			*/
			);
			$alipay = new alipay_service($parameter,$security_code,$sign_type);
			
			
			//POST��ʽ���ݣ��õ����ܽ���ַ���
			$sign = $alipay->Get_Sign();
			
			
			//���ĳ�GET��ʽ���ݣ���ȡ�����������ע��
			$alipaylink = $alipay->create_url();
		}
		//end alipay
		if(trim($arrBillInfo['web_pay_code']) != NULL) {
			//alipay
			/*if($arrBillInfo['web_pay_code'] != 'alipay') {
				require_once("pay/alipay/alipay_service.php");
				$parameter = array(
				"service" => "trade_create_by_buyer", //�������ͣ�����ʵ�ｻ�ף�trade_create_by_buyer����Ҫ��д������
				"partner" =>$partner,                                               //�����̻���
				"return_url" =>$return_url."?billno=$billno&",  //ͬ������
				"notify_url" =>$notify_url."?billno=$billno&",  //�첽����
				"_input_charset" => $_input_charset,                                //�ַ�����Ĭ��ΪGBK
				"subject" => $subject.$billno,//"������:",                                                //��Ʒ���ƣ�����
				"body" => $body,//"ͨ��֧��������֧��",                                           //��Ʒ����������
				"out_trade_no" => date(Ymdhms),                      //��Ʒ�ⲿ���׺ţ�����,ÿ�β��Զ����޸�
				"logistics_fee"=>'0',                       //�������ͷ���
				"logistics_payment"=>'BUYER_PAY',               // �������ͷ��ø��ʽ��SELLER_PAY(����֧��)��BUYER_PAY(���֧��)��BUYER_PAY_AFTER_RECEIVE(��������)
				"logistics_type"=>'EXPRESS',                    // �������ͷ�ʽ��POST(ƽ��)��EMS(EMS)��EXPRESS(�������)
				"price" => $arrBillInfo['pay_price'],                                 //��Ʒ���ۣ�����
				"payment_type"=>"1",                               // Ĭ��Ϊ1,����Ҫ�޸�
				"quantity" => "1",                                 //��Ʒ����������
				"show_url" => $show_url,            //��Ʒ�����վ
				"seller_email" => $seller_email                //�������䣬����
				);
				$alipay = new alipay_service($parameter,$security_code,$sign_type);
				$alipaylink=$alipay->create_url();
			//} else {*/
			if($arrBillInfo['web_pay_code'] != 'alipay' && $usetenpay == '1') {
				require_once("pay/tenpay/classes/PayRequestHandler.class.php");

				/* �̻��� */
				$bargainor_id = $strSpid;
				
				/* ��Կ */
				$key = $strSpkey;
				
				/* ���ش����ַ */
				$return_url = $strRetUrl;
				
				//date_default_timezone_set(PRC);
				$strDate = substr(str_replace('-', '', Common::getCommonTime()), 0, 8);
				//$strTime = date("His");
				
				//4λ�����
				//$randNum = rand(1000, 9999);
				
				//10λ���к�,�������е�����
				//$strReq = $strTime . $randNum;
				
				/* �̼Ҷ�����,����������32λ��ȡǰ32λ���Ƹ�ֻͨ��¼�̼Ҷ����ţ�����֤Ψһ�� */
				$sp_billno = $billno;
				
				/* �Ƹ�ͨ���׵��ţ�����Ϊ��10λ�̻���+8λʱ�䣨YYYYmmdd)+10λ��ˮ�� */
				$transaction_id = $bargainor_id . $strDate . $sp_billno;
				
				/* ��Ʒ�۸񣨰����˷ѣ����Է�Ϊ��λ */
				$total_fee = $arrBillInfo['pay_price']*100;
				
				/* ��Ʒ���� */
				$desc = "�����ţ�" . $billno;
				
				/* ����֧��������� */
				$reqHandler = new PayRequestHandler();
				$reqHandler->init();
				$reqHandler->setKey($key);
				
				//----------------------------------------
				//����֧������
				//----------------------------------------
				$reqHandler->setParameter("bargainor_id", $bargainor_id);			//�̻���
				$reqHandler->setParameter("sp_billno", $sp_billno);					//�̻�������
				$reqHandler->setParameter("transaction_id", $transaction_id);		//�Ƹ�ͨ���׵���
				$reqHandler->setParameter("total_fee", $total_fee);					//��Ʒ�ܽ��,�Է�Ϊ��λ
				$reqHandler->setParameter("return_url", $return_url);				//���ش����ַ
				$reqHandler->setParameter("desc", "�����ţ�" . $transaction_id);	//��Ʒ����
				
				//�û�ip,���Ի���ʱ��Ҫ�����ip��������ʽ�����ټӴ˲���
				//$reqHandler->setParameter("spbill_create_ip", $_SERVER['REMOTE_ADDR']);
				
				//�����URL
				$tenpaylink = $reqHandler->getRequestURL();
			//tenpay

			}
		}

		$objResponse -> setTplValue("alipaylink", $alipaylink);
		$objResponse -> setTplValue("tenpaylink", $tenpaylink);
	}
	/**
	 * ��ʾ 
	 */
	protected function doShowPage($objRequest, $objResponse) {
		$objResponse -> setTplValue("__Meta", Common::getMeta());
		$objResponse -> setTplName("#NoPay");
	}
	
	public static function getTmpShop($md5id, $uid = NULL, $objRequest, $objResponse, $billno = NULL) {
		$arrTmpShop = NULL;
		if(!empty($billno)) {
			$arrTmpShop = ShopDao::getProductBill($billno, $md5id);
		} else {
			$arrTmpShop = ShopDao::getTmpShop($md5id, $uid);
		}
		$num = count($arrTmpShop);
		$allprice = 0;
		for($i = 0; $i<$num; $i++) {
			$arrTmpShop[$i]['s_pic'] = Patch::getProductSImg($arrTmpShop[$i]['s_pic']);
			$arrTmpShop[$i]['m_pic'] = Patch::getProductMImg($arrTmpShop[$i]['m_pic']);
			$arrTmpShop[$i]['url'] = Patch::getProductUrl($arrTmpShop[$i]['id']);
			if($arrTmpShop[$i]['member_offer'] == 1 && $uid > 0) {
				$arrTmpShop[$i]['price'] = $arrTmpShop[$i]['price_member'];
			}
			if($arrTmpShop[$i]['special_offer'] == 1) {
				$arrTmpShop[$i]['price'] = $arrTmpShop[$i]['price_special'];
			} 
			$arrTmpShop[$i]['num'] = empty($arrTmpShop[$i]['num']) ? 0 : $arrTmpShop[$i]['num'];
			$arrTmpShop[$i]['allprice'] = $arrTmpShop[$i]['price']*$arrTmpShop[$i]['num'];
			$allprice += $arrTmpShop[$i]['allprice'];
		}
		$objResponse -> setTplValue("allprice", $allprice);
		$objResponse -> setTplValue("tmpshop", $arrTmpShop);
	}
	
	protected function simpleRegUser($objRequest) {
		$arrValue["name"] = trim($objRequest -> username);
		$arrValue["email"] = trim($objRequest -> email);
		$arrValue["password"] = trim($objRequest -> password);
		if(Check::checkEmail($arrValue["email"]) == false || empty($arrValue["name"]) || empty($arrValue["password"])) {
			redirect(-1);
		}
		$arrValue["password"] = md5($arrValue["password"]);
		$arrValue["phone"] = trim($objRequest -> phone);
		$arrValue["mobile"] = trim($objRequest -> mobile);
		$arrValue["address"] = trim($objRequest -> address);
		$arrValue["postcode"] = trim($objRequest -> postcode);
		$arrValue["sex"] = '';
		$arrValue["birthday"] = '';
		$arrValue["qq"] = '';
		$arrValue["msn"] = '';
		$arrValue["taobao"] = '';
		$arrValue["skype"] = '';
		$arrValue["regip"] = onLineIp();
		$validateMd5 = md5(uniqid(rand(), true));
		$arrValue["loginid"] = $validateMd5;
		$arrValue["rember_times"] = 0;
		$arrValue["isreg"] = 0;
		UserDao::insertUser($arrValue);
		unset($arrValue);
		return UserDao::getInsertUserId();
	} 
	
	protected function doShowBillno($objRequest, $objResponse) {
		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);

		$billno = $objRequest -> billno;
		$md5id = $objRequest -> md;
		$arrBillInfo = '';
		$arrProductBill = '';
		if(!empty($billno) && !empty($md5id)) {
			$arrBillInfo = ShopDao::getBillInfo($billno, $md5id);
			$this -> getTmpShop($md5id, NULL, $objRequest, $objResponse, $billno);
			if(!empty($arrBillInfo['web_pay_code']) || $arrBillInfo['pay_success'] == 0) {
				$web_url = "http://".Common::getSiteInfo('value3') . '/';
				//web pay
				$this -> webPay($billno, $arrBillInfo, $web_url, $objResponse);
			}
		}

		$objResponse -> setTplValue("billnoinfo", $arrBillInfo);
		$objResponse -> setTplValue("bank", ClassesDao::getBank());
		$objResponse -> setTplValue("__Meta", Common::getMeta());
		$objResponse -> setTplName("#ShowBillno");
	}
}
?>