<?php


/**
 * class.RegisterAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 */

class RegisterAction extends BaseAction {
	
	protected function check($objRequest, $objResponse) {
	}

	protected function service($objRequest, $objResponse) {

		switch($objRequest->getSwitch()) {
			case "register":
				$this -> doRegUser($objRequest, $objResponse);
				break;
				
			case "login":
				$this -> doLogin($objRequest, $objResponse);
				break;
				
			case "checklogin":
				$this -> doCheckLoginUser($objRequest, $objResponse);
				break;	
				
			case "logout":
				$this -> doLogout($objRequest, $objResponse);
				break;	

			default:
			$this->doShowPage($objRequest, $objResponse);
				break;
		}
	}

	/**
	 * 显示 
	 */
	protected function doShowPage($objRequest, $objResponse) {
		$objResponse -> setTplValue("__Meta", Common::getMeta(conutf8('网站注册')));
		$objResponse->setTplName("#Register");
	}
	
	protected function doLogin($objRequest, $objResponse) {
		$objResponse -> setTplValue("__Meta", Common::getMeta(conutf8('网站登陆')));
		$objResponse->setTplName("#Login");
	}
	
	/**
	 * reg 
	 */
	protected function doRegUser($objRequest, $objResponse) {
		$validate = $objRequest -> validate;
		
		$objCookie = new Cookie;
		$cookievalidate = $objCookie -> cookievalidate;
		$md5id = $objCookie -> muantshopid;
		
		$objResponse->setTplValue("reg_success", 'no');
		$objResponse -> setTplValue("__Meta", Common::getMeta());
		if(empty($validate)) {
			$objResponse->setTplName("#Register");
			return;
		}
		if((getHis() - $objCookie -> cookietime) > 60*5) {
			$objResponse->setTplName("#Register");
			return;
		}
		if($validate == "" || strtolower($validate) != strtolower($cookievalidate)) {
			$objResponse->setTplName("#Register");
			return;
		} else {
			unset($objCookie -> cookievalidate);
		}
		$arrValue["name"] = trim($objRequest -> username);
		$arrValue["email"] = trim($objRequest -> email);
		if(Check::checkEmail($arrValue["email"]) == false) {
			$objResponse->setTplName("#Register");
			return;
		}
		if($arrValue["name"] != NULL and $arrValue["email"] != NULL) {
			$arrUserInfo = UserDao::checkUser($arrValue["email"]);
			if(!empty($arrUserInfo)) {
				$objResponse->setTplValue("checkemail", 'no');
				$objResponse->setTplName("#Register");
				return;
			}
		} else {
			$objResponse->setTplName("#Register");
			return;
		}
		$arrValue["password"] = trim($objRequest -> password);
		$arrValue["password"] = empty($arrValue["password"]) ? NULL : md5($arrValue["password"]);
		$pass = md5(trim($objRequest -> repassword));
		if($pass != $arrValue["password"]) {
			$objResponse->setTplName("#Register");
			return;
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
		$arrValue["alibaba"] = '';
		$arrValue["regip"] = onLineIp();
		$validateMd5 = md5(uniqid(rand(), true));
		$arrValue["loginid"] = $validateMd5;
		$arrValue["rember_times"] = 0;
		$arrValue["isreg"] = 1;
		$time = 0;
		if(($objRequest -> remember) == "yes") {
			$time = 24*7*3600*30*12;
			$arrValue["rember_times"] = time() + $time;
		}

		UserDao::insertUser($arrValue);
		$uid = UserDao::getInsertUserId();
		if(!empty($md5id)) {
			ShopDao::deleteTmpShop(NULL, NULL, $uid);
			ShopDao::updateTmpShopUid($md5id, $uid);
		}
		
		$objCookie -> setCookie("muantshop", $uid.'	'.$arrValue["name"].'	'.$arrValue["email"].'	'.$validateMd5, $time);
		$objResponse -> setTplValue("reg_success", 'yes');
		$objResponse -> setTplName("#Register");
	}
	
	protected function doCheckLoginUser($objRequest, $objResponse) {
		$objCookie = new Cookie;

		$arrValue["email"] = trim($objRequest -> email);
		if(Check::checkEmail($arrValue["email"]) == false) {
			redirect("login.php"); 
		}
		$arrValue["password"] = trim($objRequest -> password) ? md5(trim($objRequest -> password)) : NULL;
		if(($userInfo = UserDao::checkUser($arrValue["email"], $arrValue["password"]))) {
			$time = 0;
			if(($objRequest -> remember) == "yes") {
				$time = 24*7*3600*30*12;
			}
			$loginid = $userInfo['loginid'];
			if($userInfo["rember_times"] <= time()) {
				$loginid = md5(uniqid(rand(), true));
				UserDao::updateUserLoginId($loginid, $time + time(), $arrValue["email"], $arrValue["password"]);
			}
			$objCookie -> setCookie("muantshop", $userInfo["id"].'	'.$userInfo["name"].'	'.$arrValue["email"].'	'.$loginid, $time);
		} else {
			redirect("login.php"); 
		}
		$md5id = $objCookie -> muantshopid;
		if(!empty($md5id)) {
			ShopDao::deleteTmpShop(NULL, NULL, $userInfo["id"]);
			ShopDao::updateTmpShopUid($md5id, $userInfo["id"]);
		}
		redirect("myhome.php"); 
	} 
	
	protected function doLogout($objRequest, $objResponse) {
		$objCookie = new Cookie;
		unset($objCookie -> muantshop);
		redirect("./"); 
	}
	
}
?>