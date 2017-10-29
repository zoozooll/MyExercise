<?php


/**
 * class.SpecialProductAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 */

class SpecialProductAction extends BaseAction {
	
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
		
		$cid = $objRequest->cid;
		$pn = $objRequest->pn;
		$pn = $pn < 1 ? 1 : $pn;
		$pn = $pn > 3 ? 3 : $pn;
		$count = 20;
		$str_title = '';
				
		$switch = $objRequest -> getSwitch();
		switch($switch) {
			case 'specialoffer':
				$str_title = conutf8('特价商品');
				$arrRecProduct = ProductDao::getProduct($cid, NULL, NULL, $pn, $count, " AND special_offer = '1' AND store > 0 ORDER BY hit_times DESC, store DESC");
				$objResponse -> setTplValue("menu_nav", 'specialoffer');
			break;
			case 'hotproduct':
				$str_title = conutf8('热销商品');
				$arrRecProduct = ProductDao::getProduct($cid, NULL, NULL, $pn, $count, " AND store > 0 ORDER BY hit_times DESC, store DESC");
				$objResponse -> setTplValue("menu_nav", 'hotproduct');
			break;
			case 'newproduct':
				$str_title = conutf8('最新商品');
				$arrRecProduct = ProductDao::getProduct($cid, NULL, NULL, $pn, $count, " AND store > 0 ORDER BY id DESC, store DESC");
				$objResponse -> setTplValue("menu_nav", 'newproduct');
			break;
			default:
				$str_title = conutf8('最新商品');
				$arrRecProduct = ProductDao::getProduct($cid, NULL, NULL, $pn, $count, " AND store > 0 ORDER BY hit_times DESC,  store DESC");
				$objResponse -> setTplValue("menu_nav", 'xxx');
			break;
		}
		$num = count($arrRecProduct);
		for($k = 0; $k < $num; $k++) {
			$arrRecProduct[$k]['m_pic'] = Patch::getProductMImg($arrRecProduct[$k]['m_pic']);
			$arrRecProduct[$k]['url'] = Patch::getProductUrl($arrRecProduct[$k]['id']);
			$arrRecProduct[$k]['price_member'] = $arrRecProduct[$k]['price_member'];
		}
		$allPage = 3;
		$arrPages = '';
		$arrPage = Patch::getPageArray($allPage, $pn);
		$firstpage = Patch::getSpecialUrl($cid, 1, $switch);
		$lastpage = Patch::getSpecialUrl($cid, $allPage, $switch);
		$prvepage = Patch::getSpecialUrl($cid, $pn-1, $switch);
		$nextpage = Patch::getSpecialUrl($cid, $pn+1, $switch);
		for($i = 0; $i<$allPage; $i++) {
			$arrPages[$i]['url'] = Patch::getSpecialUrl($cid, $arrPage[$i], $switch);
			$arrPages[$i]['pn'] = $arrPage[$i];
		}
		//销毁变量
		unset($arrPage);
		
		$objResponse -> setTplValue("pn", $pn);
		$objResponse -> setTplValue("allPage", 3);
		$objResponse -> setTplValue("firstpage", $firstpage);
		$objResponse -> setTplValue("lastpage", $lastpage);
		$objResponse -> setTplValue("prvepage", $prvepage);
		$objResponse -> setTplValue("nextpage", $nextpage);
		$objResponse -> setTplValue("arrPages", $arrPages);

		$objResponse -> setTplValue("product", $arrRecProduct);
		$objResponse -> setTplValue("keyword", '');
		$objResponse -> setTplValue("se", '');
		$objResponse -> setTplValue("userinfo", $cookieUser);
		$objResponse -> setTplValue("__Meta", Common::getMeta($str_title,$str_title));
		$objResponse -> setTplName("#Specialoffer");
	}
}
?>