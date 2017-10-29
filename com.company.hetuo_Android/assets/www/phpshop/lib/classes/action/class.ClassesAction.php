<?php


/**
 * class.ClassesAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	��Ȩ���У���������ǼǺţ�2009SR06466 ����
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
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
	 * ��ʾ 
	 */
	protected function doShowPage($objRequest, $objResponse) {
		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);
		
		$objResponse -> setTplValue("class", Common::getClassInfo());
		$objResponse -> setTplValue("userinfo", $cookieUser);
		$objResponse -> setTplValue("menu_nav", 'classes');
		$objResponse -> setTplValue("__Meta", Common::getMeta(conutf8('��Ʒ���')));
		$objResponse -> setTplName("#Classes");
	}
}
?>