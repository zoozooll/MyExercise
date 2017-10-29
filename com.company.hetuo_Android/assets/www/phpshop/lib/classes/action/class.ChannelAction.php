<?php


/**
 * class.ChannelAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 */

class ChannelAction extends BaseAction {
	
	protected function check($objRequest, $objResponse) {
		checkNum($objRequest -> cid);
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
		$cid = $objRequest -> cid;

		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);
		
		//channel info
		$arrClasses = Common::getClassInfo();
		//catnav
		$arrCatnav = Common::getClassNav($cid);
		
		$arrChannel = Common::getClassInfo(NULL, $cid);
		if(empty($arrChannel)) {
			$cid = $arrClasses[0]['cid'];
			$arrChannel = Common::getClassInfo(NULL, $cid);
			errorLog($cid . ' get channel empty.');
		}
		$num = count($arrChannel);
		$str_cid = '';
		for($i = 0; $i<$num; $i++) {
			$str_cid .= $arrChannel[$i]['id'].',';
		}
		$str_cid = trim($str_cid, ',');
		$arrChannelInfo = Common::getClassInfo($cid);
		$arrChannelInfo['cat_url'] = Patch::getCategoryUrl($arrChannelInfo['enname'], $arrChannelInfo['id'], $arrChannelInfo['enname']);
		$arrChannelNews = NewsDao::getNews($str_cid);
		$num = count($arrChannelNews);
		for($i = 0; $i<$num; $i++) {
			$arrChannelNews[$i]['url'] = Patch::getNewsUrl($arrChannelNews[$i]['id']);
			$arrChannelNews[$i]['shot_title'] = Utilities::cutString($arrChannelNews[$i]['title'], '36');
		}
		
		$hotProductNum = Common::getSiteInfo('value2', 'commend');
		$hotProductNum = empty($hotProductNum) ? '15' : $hotProductNum; 
		$arrProduct = ProductDao::getProduct($str_cid, NULL, NULL, 1, $hotProductNum, " AND store > 0 ORDER BY id DESC, store DESC");
		$num = count($arrProduct);
		for($i = 0; $i<$num; $i++) {
			$arrProduct[$i]['m_pic'] = Patch::getProductMImg($arrProduct[$i]['m_pic']);
			$arrProduct[$i]['url'] = Patch::getProductUrl($arrProduct[$i]['id']);
			//$arrProduct[$i]['price_member'] = $arrProduct[$i]['price']*0.9;
		}
		
		$recProductNum = Common::getSiteInfo('value3', 'commend');
		$recProductNum = empty($recProductNum) ? '5' : $recProductNum; 
		$arrRecProduct = ProductDao::getProduct($str_cid, NULL, NULL, 1, $recProductNum, " AND store > 0 ORDER BY hit_times DESC, store DESC");
		$num = count($arrRecProduct);
		for($i = 0; $i<$num; $i++) {
			$arrRecProduct[$i]['m_pic'] = Patch::getProductMImg($arrRecProduct[$i]['m_pic']);
			$arrRecProduct[$i]['url'] = Patch::getProductUrl($arrRecProduct[$i]['id']);
			//$arrRecProduct[$i]['price_member'] = $arrRecProduct[$i]['price']*0.9;
		}
		//ad
		$arrAd = Common::getCommonAdvertising($cid);

		$objResponse -> setTplValue("product", $arrProduct);
		$objResponse -> setTplValue("recproduct", $arrRecProduct);
		$objResponse -> setTplValue("news", $arrChannelNews);
		$objResponse -> setTplValue("channelinfo", $arrChannelInfo);
		$objResponse -> setTplValue("channel", $arrChannel);
		$objResponse -> setTplValue("catnav", $arrCatnav);
		$objResponse -> setTplValue("class", $arrClasses);
		$objResponse -> setTplValue("adinfo", $arrAd);
		$objResponse -> setTplValue("userinfo", $cookieUser);
		//搜索关键字
		$objResponse -> setTplValue("keyword", '');
		$objResponse -> setTplValue("se", '');
		$objResponse -> setTplValue("__Meta", Common::getMeta($arrChannelInfo['name'],$arrChannelInfo['name'],$arrChannelInfo['name']));
		$objResponse -> setTplName("#Channel");
	}

}
?>