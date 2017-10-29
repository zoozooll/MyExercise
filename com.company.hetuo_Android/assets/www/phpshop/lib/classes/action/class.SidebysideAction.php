<?php


/**
 * class.SidebysideAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 */

class SidebysideAction extends BaseAction {
	
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
		$arrSdid = $objRequest -> sdid;
		$retpage = empty($objRequest -> retpage) ? './' : urldecode($objRequest -> retpage);
		
		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);
		$cookie_sdid = trim(trim($objCookie -> getSimpleCookie('muant_sdid'), ','), '#');
		$strpid = str_replace('#,', ',', $cookie_sdid);
		unset($cookie_sdid);
		if(empty($strpid) && !empty($arrSdid)) {
			$strpid = implode(',', $arrSdid);
		} 
		if(empty($strpid)) {
			redirect($retpage);			
		} else {
			$strpid .= ',0';
		}

		$arrProduct = ProductDao::getProduct(NULL, $strpid);
		$num = count($arrProduct);

		$arrXML = array();//
		
		for($i = 0; $i<$num; $i++) {
			$arrPid[$i] = $arrProduct[$i]['id'];
		}

		$rs =  array();
		for($i = 0; $i<$num; $i++) {
			$arrProduct[$i]['m_pic'] = Patch::getProductMImg($arrProduct[$i]['m_pic']);
			$arrProduct[$i]['url'] = Patch::getProductUrl($arrProduct[$i]['id']);
			$arrProduct[$i]['reurl'] = self::setSdidUrl($arrPid, $num, $arrProduct[$i]['id']);
			@include_once(__PRODUCT_XML.$arrProduct[$i]['id'].'.php');
			if(!empty($rs)) {
				foreach($rs as $k => $v) {
					if($v['title_name'] != '') {
						$title_name = $v['title_name'];
					} else {
						if(!isset($arrXML[$title_name][$v['name']]['eq'])) {
							$arrXML[$title_name][$v['name']]['eq'] = 1;
						}
						if(isset($arrXML[$title_name][$v['name']]['value'][$i - 1])) {
							if($arrXML[$title_name][$v['name']]['value'][$i - 1] != $arrXML[$title_name][$v['name']]['value'][$i]){
								$arrXML[$title_name][$v['name']]['eq'] = 0;
							}
						}
						$arrXML[$title_name][$v['name']]['title_name'] = $title_name;
						$arrXML[$title_name][$v['name']]['name'] = $v['name'];
						$v['value'] = $v['value'] == '' ? '-' : $v['value'];
						$arrXML[$title_name][$v['name']]['value'][$i] = $v['value'];
					}
				}
			}
			$rs =  array();
		}
		$arrProdXML = array();
		foreach($arrXML as $k => $v) {
			foreach($v as $kk => $vv) {
				$arrProdXML[] = $vv;
			}
		}
		unset($rs);
		unset($arrXML);
		unset($v);
		unset($k);
		unset($vv);
		unset($kk);
		unset($arrPid);

		//ProductDao::updateProductHit($pid);
		//$arrProduct['m_pic'] = Patch::getProductMImg($arrProduct['m_pic']);
		//$arrProduct['b_pic'] = Patch::getProductBImg($arrProduct['b_pic']);
		//$arrProduct['url'] = Patch::getProductUrl($pid);
		//$arrCategoryInfo = Common::getClassInfo($arrProduct['cid']);
		//catnav
		//$arrCatnav = Common::getClassNav($arrProduct['cid']);
		//include_once(__PRODUCT_XML.$pid.'.php');
		
		$objResponse -> setTplValue("product", $arrProduct);
		$objResponse -> setTplValue("cols", $num);
		$objResponse -> setTplValue("xml", $arrProdXML);
		$objResponse -> setTplValue("userinfo", $cookieUser);
		$objResponse -> setTplValue("retpage", $retpage);
		$objResponse -> setTplValue("__Meta", Common::getMeta());
		$objResponse -> setTplName("#Sidebyside");
	}
	
	protected static function setSdidUrl($arrPid, $num, $pid) {
		$strpid = '';
		$k = 0;
		for($i = 0; $i<$num; $i++) {
			if($arrPid[$i] == $pid) continue;
			$strpid .= '&sdid['.$k.']='.$arrPid[$i];
			$k++;
		}
		return 'sidebyside.php?'.trim($strpid, '&');
	}
}
?>