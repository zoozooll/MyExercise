<?php


/**
 * class.AboutAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	��Ȩ���У���������ǼǺţ�2009SR06466 ����
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
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
	 * ��ҳ��ʾ 
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