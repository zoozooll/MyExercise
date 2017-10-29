<?php


/**
 * class.ShopAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
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
		//搜索关键字
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
		//搜索关键字
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
			alert(conutf8('Email 错误，请修改。'));
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
		//运费
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
		//更新库存
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
		Common::sendMail($arrValues["email"], Common::getSiteInfo('value9', "service"), $ordermail, conutf8(Common::getSiteInfo('value1'), 'UTF-8', 'GB2312') . ' ( 订单确认通知 )', 'gb2312');
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
		//搜索关键字
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
			/////////////////不会更改的必填参数/////////////////////////////
				"service"        => "create_partner_trade_by_buyer",  //交易类型 实物标准双接口服务名称：trade_create_by_buyer ； 中介担保交易（纯担保交易）服务名称：create_partner_trade_by_buyer
				"payment_type"   => "1",                      //默认为1,不需要修改
				"_input_charset" => $_input_charset,          //字符集，默认为GBK
				
			/////////////////必填的参数////////////////////////////////////
				"partner"        => $partner,                 //合作身份者ID
				"seller_email"   => $seller_email,            //收款支付宝账号，一般是签约支付宝账号
				"out_trade_no"   => $billno,                  //商家网站唯一订单号（保证商家网站订单系统的里的订单号唯一）
				"subject"        => $subject . $billno,                 //商品名称，订单主题
				"price"          => $arrBillInfo['pay_price'],                   //商品单价，订单总价（价格不能为0）
				"quantity"       => "1",                      //商品数量（用于商品单价时的数量）
				
				"logistics_fee"      => '0.00',               //物流配送费用
				"logistics_payment"  => 'BUYER_PAY',          //物流费用付款方式：SELLER_PAY(卖家支付)、BUYER_PAY(买家支付)、BUYER_PAY_AFTER_RECEIVE(货到付款)
				"logistics_type"     => 'EXPRESS',            //物流配送方式：POST(平邮)、EMS(EMS)、EXPRESS(其他快递)
				
			/////////////////建议填写的重要的参数/////////////////////////
				"return_url"     => $return_url,              //同步返回,返回页路径地址
				"notify_url"     => $notify_url,              //异步返回，通知页路径地址
				"body"           => $body,                    //商品描述，订单备注
			
				"show_url"       => $show_url,                 //商品相关网站
			
			////////////////其他有用的选填参数///////////////////////////
				//收货人信息，当至少含有这些参数中的receive_name、receive_address、receive_zip时，到达支付宝收银台以后的操作步骤中会减少设置收货人信息的一步
				"receive_name"	 => '',//$receive_name,		   //收货人姓名
				"receive_address"=> '',//$receive_address,	   //收货人地址
				"receive_zip"	 => '',//$receive_zip,		   //收货人邮编
				"receive_phone"  => '',//$receive_phone,		   //收货人联系电话
				"receive_mobile" => '',//$receive_mobile,	   //收货人手机
				
				"buyer_email"	 => ''//$buyer_email		   //买家支付宝账号
			/*
				"logistics_fee_1"    => $logistics_fee_1,             //物流配送费用
				"logistics_payment_1"=> $logistics_payment_1,        //物流费用付款方式：SELLER_PAY(卖家支付)、BUYER_PAY(买家支付)、BUYER_PAY_AFTER_RECEIVE(货到付款)
				"logistics_type_1"   => $logistics_type_1,              //物流配送方式：POST(平邮)、EMS(EMS)、EXPRESS(其他快递)
				
				"logistics_fee_2"    => $logistics_fee_2,             //物流配送费用
				"logistics_payment_2"=> $logistics_payment_2,        //物流费用付款方式：SELLER_PAY(卖家支付)、BUYER_PAY(买家支付)、BUYER_PAY_AFTER_RECEIVE(货到付款)
				"logistics_type_2"   => $logistics_type_2             //物流配送方式：POST(平邮)、EMS(EMS)、EXPRESS(其他快递)
			*/
			);
			$alipay = new alipay_service($parameter,$security_code,$sign_type);
			
			
			//POST方式传递，得到加密结果字符串
			$sign = $alipay->Get_Sign();
			
			
			//若改成GET方式传递，请取消下面的两行注释
			$alipaylink = $alipay->create_url();
		}
		//end alipay
		if(trim($arrBillInfo['web_pay_code']) != NULL) {
			//alipay
			/*if($arrBillInfo['web_pay_code'] != 'alipay') {
				require_once("pay/alipay/alipay_service.php");
				$parameter = array(
				"service" => "trade_create_by_buyer", //交易类型，必填实物交易＝trade_create_by_buyer（需要填写物流）
				"partner" =>$partner,                                               //合作商户号
				"return_url" =>$return_url."?billno=$billno&",  //同步返回
				"notify_url" =>$notify_url."?billno=$billno&",  //异步返回
				"_input_charset" => $_input_charset,                                //字符集，默认为GBK
				"subject" => $subject.$billno,//"订单号:",                                                //商品名称，必填
				"body" => $body,//"通过支付宝网上支付",                                           //商品描述，必填
				"out_trade_no" => date(Ymdhms),                      //商品外部交易号，必填,每次测试都须修改
				"logistics_fee"=>'0',                       //物流配送费用
				"logistics_payment"=>'BUYER_PAY',               // 物流配送费用付款方式：SELLER_PAY(卖家支付)、BUYER_PAY(买家支付)、BUYER_PAY_AFTER_RECEIVE(货到付款)
				"logistics_type"=>'EXPRESS',                    // 物流配送方式：POST(平邮)、EMS(EMS)、EXPRESS(其他快递)
				"price" => $arrBillInfo['pay_price'],                                 //商品单价，必填
				"payment_type"=>"1",                               // 默认为1,不需要修改
				"quantity" => "1",                                 //商品数量，必填
				"show_url" => $show_url,            //商品相关网站
				"seller_email" => $seller_email                //卖家邮箱，必填
				);
				$alipay = new alipay_service($parameter,$security_code,$sign_type);
				$alipaylink=$alipay->create_url();
			//} else {*/
			if($arrBillInfo['web_pay_code'] != 'alipay' && $usetenpay == '1') {
				require_once("pay/tenpay/classes/PayRequestHandler.class.php");

				/* 商户号 */
				$bargainor_id = $strSpid;
				
				/* 密钥 */
				$key = $strSpkey;
				
				/* 返回处理地址 */
				$return_url = $strRetUrl;
				
				//date_default_timezone_set(PRC);
				$strDate = substr(str_replace('-', '', Common::getCommonTime()), 0, 8);
				//$strTime = date("His");
				
				//4位随机数
				//$randNum = rand(1000, 9999);
				
				//10位序列号,可以自行调整。
				//$strReq = $strTime . $randNum;
				
				/* 商家订单号,长度若超过32位，取前32位。财付通只记录商家订单号，不保证唯一。 */
				$sp_billno = $billno;
				
				/* 财付通交易单号，规则为：10位商户号+8位时间（YYYYmmdd)+10位流水号 */
				$transaction_id = $bargainor_id . $strDate . $sp_billno;
				
				/* 商品价格（包含运费），以分为单位 */
				$total_fee = $arrBillInfo['pay_price']*100;
				
				/* 商品名称 */
				$desc = "订单号：" . $billno;
				
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
				$reqHandler->setParameter("desc", "订单号：" . $transaction_id);	//商品名称
				
				//用户ip,测试环境时不要加这个ip参数，正式环境再加此参数
				//$reqHandler->setParameter("spbill_create_ip", $_SERVER['REMOTE_ADDR']);
				
				//请求的URL
				$tenpaylink = $reqHandler->getRequestURL();
			//tenpay

			}
		}

		$objResponse -> setTplValue("alipaylink", $alipaylink);
		$objResponse -> setTplValue("tenpaylink", $tenpaylink);
	}
	/**
	 * 显示 
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