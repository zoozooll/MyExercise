<?php


/**
 * class.AboutAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 */

class AboutAction extends BaseAction {
	
	protected function check($objRequest, $objResponse) {
	}

	protected function service($objRequest, $objResponse) {

		switch($objRequest->getSwitch()) {
			default:
				$this->doShowPage($objRequest, $objResponse);
				break;	
		}
	}

	/**
	 * 首页显示 
	 */
	protected function doShowPage($objRequest, $objResponse) {
		$switchValue = $objRequest->getSwitch();
		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);
		
		//set common value
		$objResponse -> setTplValue("shop_about_all", Utilities::stripSlashesStr(Common::getSiteInfo($switchValue, 'regAndCon')));
		$objResponse -> setTplValue("__Meta", Common::getMeta());
		$objResponse -> setTplValue("userinfo", $cookieUser);
		$objResponse -> setTplName("#shop_about_all");
	}
	
}
?>