<?php


/**
 * class.NewsAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 */

class NewsAction extends BaseAction {
	
	protected function check($objRequest, $objResponse) {
	}

	protected function service($objRequest, $objResponse) {

		switch($objRequest -> getSwitch()) {
			case 'productnews':
			$this -> doProductnews($objRequest, $objResponse);
			break;
			default:
				$this -> doShowPage($objRequest, $objResponse);
			break;
		}
	}

	/**
	 * 显示 
	 */
	protected function doShowPage($objRequest, $objResponse) {
		$id = $objRequest -> id;
		checkNum($id);
		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);

		$arrChannelNews = NewsDao::getNews(NULL, $id);
		
		//channel info
		$arrClasses = Common::getClassInfo();
		//catnav
		$arrCatnav = Common::getClassNav($arrChannelNews['cid']);
		
		$arrProductRelated = ProductDao::getProduct($arrChannelNews['cid'], NULL, NULL, 1, 5);
		$num = count($arrProductRelated);
		for($i = 0; $i < $num; $i++) {
			$arrProductRelated[$i]['m_pic'] = Patch::getProductMImg($arrProductRelated[$i]['m_pic']);
			$arrProductRelated[$i]['url'] = Patch::getProductUrl($arrProductRelated[$i]['id']);
		}


		$objResponse -> setTplValue("arrProductRelated", $arrProductRelated);
		$objResponse -> setTplValue("news", $arrChannelNews);
		$objResponse -> setTplValue("userinfo", $cookieUser);
		$objResponse -> setTplValue("catnav", $arrCatnav);
		$objResponse -> setTplValue("__Meta", Common::getMeta($arrChannelNews['title']));
		$objResponse -> setTplName("#News");
	}
	
	protected function doProductnews($objRequest, $objResponse) {
		$pn = $objRequest -> pn;
		$cid = $objRequest -> cid;
		
		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);
		
		$newNum = NewsDao::getNumNews($cid);
		$count = 20;
		$allPage = ceil($newNum/$count);
		$pn = $pn > $allPage ? $allPage : $pn;
		$pn = $pn < 1 || empty($pn) ? 1 : $pn;
		
		$arrNews = NewsDao::getNews($cid, NULL, NULL, $pn, $count);
		//$arrChannelNews = NewsDao::getNews($cid);
		$num = count($arrNews);
		for($i = 0; $i<$num; $i++) {
			$arrNews[$i]['url'] = Patch::getNewsUrl($arrNews[$i]['id']);
			$arrNews[$i]['br'] = 0;
			if($i%5 == 4) $arrNews[$i]['br'] = 1;
			//$arrNews[$i]['shot_title'] = Utilities::cutString($arrNews[$i]['title'], '36');
		}
		
		$arrPages = array();
		$arrPage = Patch::getPageArray($allPage, $pn);
		$arrParam['pn'] = '';
		$firstpage = Patch::getProductNewsUrl($cid, 1);
		$lastpage = Patch::getProductNewsUrl($cid, $allPage);
		$prvepage = Patch::getProductNewsUrl($cid, $pn-1);
		$nextpage = Patch::getProductNewsUrl($cid, $pn+1);
		for($i = 0; $i<$allPage; $i++) {
			$arrPages[$i]['url'] = Patch::getProductNewsUrl($cid, $arrPage[$i]);
			$arrPages[$i]['pn'] = $arrPage[$i];
		}
		//销毁变量
		unset($arrPage);

		//channel info
		$arrClasses = Common::getClassInfo();
		//catnav
		$arrCatnav = '';
		$objResponse -> setTplValue("pn", $pn);
		$objResponse -> setTplValue("allPage", $allPage);
		$objResponse -> setTplValue("firstpage", $firstpage);
		$objResponse -> setTplValue("lastpage", $lastpage);
		$objResponse -> setTplValue("prvepage", $prvepage);
		$objResponse -> setTplValue("nextpage", $nextpage);
		$objResponse -> setTplValue("arrPages", $arrPages);
		$objResponse -> setTplValue("menu_nav", 'productnews');
		$objResponse -> setTplValue("news", $arrNews);
		$objResponse -> setTplValue("userinfo", $cookieUser);
		$objResponse -> setTplValue("catnav", $arrCatnav);
		$objResponse -> setTplValue("__Meta", Common::getMeta(conutf8('商品新闻'), conutf8('商品新闻')));
		$objResponse -> setTplName("#Productnews");
	}

}
?>