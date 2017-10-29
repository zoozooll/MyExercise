<?php


/**
 * class.ClassesAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 */

class ClassesAction extends BaseAction {
	
	protected function check($objRequest, $objResponse) {
	}

	protected function service($objRequest, $objResponse) {

		switch($objRequest -> getSwitch()) {
			default:
				$this -> doShowPage($objRequest, $objResponse);
			break;
		}
	}

	/**
	 * 显示 
	 */
	protected function doShowPage($objRequest, $objResponse) {
		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);
		
		$objResponse -> setTplValue("class", Common::getClassInfo());
		$objResponse -> setTplValue("userinfo", $cookieUser);
		$objResponse -> setTplValue("menu_nav", 'classes');
		$objResponse -> setTplValue("__Meta", Common::getMeta(conutf8('商品类别')));
		$objResponse -> setTplName("#Classes");
	}
}
?>