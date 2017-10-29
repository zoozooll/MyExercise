<?php


/**
 * class.MyhomeAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 */

class MyhomeAction extends BaseAction {
	
	protected function check($objRequest, $objResponse) {
	}

	protected function service($objRequest, $objResponse) {

		switch($objRequest->getSwitch()) {
			case 'me':
				$this->doShowMe($objRequest, $objResponse);
				break;
			case 'modify':
				$this->doModifyMe($objRequest, $objResponse);
				break;
			case 'showmybillno':
				$this->doBillnoMe($objRequest, $objResponse);
				break;
			default:
				$this->doShowPage($objRequest, $objResponse);
				break;	
		}
	}

	/**
	 * 首页显示 
	 */
	protected function doShowPage($objRequest, $objResponse) {

		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);
		
		$objResponse -> setTplValue("__Meta", Common::getMeta());
		if(empty($cookieUser)) {
			$objResponse->setTplName("#Login");
			return;
		}
		$arrMybillno = array();
		if(isset($cookieUser['id'])) {
			$arrMybillno = UserDao::getUserBillInfo($cookieUser['id'], NULL, "ob.add_date >= '" . Common::getCommonTime(-(24*30)) . "'");
			$num = count($arrMybillno);
			for($i = 0; $i < $num; $i++) {
				$arrMybillno[$i]['m_pic'] = Patch::getProductMImg($arrMybillno[$i]['m_pic']);
				$arrMybillno[$i]['url'] = Patch::getProductUrl($arrMybillno[$i]['id']);
			}
		}
		$arrUserInfo = UserDao::getUserByLoginid($cookieUser['email'], $cookieUser['loginid']);
		
		$objResponse->setTplValue("mybillno", $arrMybillno);
		$objResponse->setTplValue("userinfo", $arrUserInfo);
		//set common value
		$objResponse->setTplName("#Myhome");
	}
	
	protected function doModifyMe($objRequest, $objResponse) {
		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);
		
		$error = 0;
		$success = 1;
		$erroremail = 0;
		$arrValue["name"] = trim($objRequest -> username);
		$arrValue["email"] = trim($objRequest -> email);
		if(Check::checkEmail($arrValue["email"]) == false) {
			$erroremail = 1;
			$error = true;
		}
		
		$errorpassword = 0;
		$oldpassword = trim($objRequest -> oldpassword);
		$arrValue["password"] = trim($objRequest -> password);
		if($arrValue["password"] != '') {
			$arrValue["password"] = empty($arrValue["password"]) ? NULL : md5($arrValue["password"]);
			$pass = md5(trim($objRequest -> repassword));
			if($pass != $arrValue["password"]) {
				$errorpassword = 1;
				$error = true;
			}
		}
		$arrValue["phone"] = trim($objRequest -> phone);
		$arrValue["mobile"] = trim($objRequest -> mobile);
		$arrValue["address"] = trim($objRequest -> address);
		$arrValue["postcode"] = trim($objRequest -> postcode);
		$arrValue["sex"] = trim($objRequest -> sex);
		$arrValue["birthday"] = trim($objRequest -> birthday);
		$arrValue["qq"] = trim($objRequest -> qq);
		$arrValue["msn"] = trim($objRequest -> msn);
		$arrValue["taobao"] = trim($objRequest -> taobao);
		$arrValue["skype"] = trim($objRequest -> skype);
		if(!$error && $oldpassword != '') {
			$success = UserDao::updateUser($arrValue, $email, $oldpassword);
		}
		$arrUserInfo = UserDao::getUserByLoginid($cookieUser['email'], $cookieUser['loginid']);
		$objResponse->setTplValue("erroremail", $erroremail);
		$objResponse->setTplValue("errorpassword", $errorpassword);
		$objResponse->setTplValue("success", $success);
		$objResponse->setTplValue("userinfo", $arrUserInfo);
		$objResponse->setTplName("#ModifyMe");
	}
	
	protected function doShowMe($objRequest, $objResponse) {

		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);
		//set common value
		$objResponse -> setTplValue("__Meta", Common::getMeta());
		if(empty($cookieUser)) {
			$objResponse->setTplName("#Login");
			return;
		}
		$arrUserInfo = UserDao::getUserByLoginid($cookieUser['email'], $cookieUser['loginid']);

		$objResponse->setTplValue("userinfo", $arrUserInfo);
		$objResponse->setTplName("#ModifyMe");
	}
	
	protected function doBillnoMe($objRequest, $objResponse) {
		$act = $objRequest -> act;
		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);
		$arrMybillno = array(); 
		if(!empty($cookieUser)) {
			if(isset($cookieUser['id'])) {
				$str_sql = "ob.add_date >= '" . Common::getCommonTime(-(24*30)) . "'";
				if($act == 'nopay') $str_sql .= " AND ob.pay_success = '0'";
				if($act == 'wait') $str_sql .= " AND (ob.state = '0' OR ob.state = '1')";
				if($act == 'issend') $str_sql .= " AND ob.state = '2'";
				if($act == 'success') $str_sql .= " AND ob.pay_success = '1'";
				if($act == 'history') $str_sql = '';//"ob.add_date >= '" . Common::getCommonTime(24*30) . "'";
				if($cookieUser['id'] > 0) {
					$arrMybillno = UserDao::getUserBillInfo($cookieUser['id'], NULL, $str_sql);
					$num = count($arrMybillno);
					for($i = 0; $i < $num; $i++) {
						$arrMybillno[$i]['m_pic'] = Patch::getProductMImg($arrMybillno[$i]['m_pic']);
						$arrMybillno[$i]['url'] = Patch::getProductUrl($arrMybillno[$i]['id']);
					}	
				}
			}
		}
		
		$objResponse->setTplValue("mybillno", $arrMybillno);
		$objResponse->setTplName("#MeBillInfo");
	} 
}
?>