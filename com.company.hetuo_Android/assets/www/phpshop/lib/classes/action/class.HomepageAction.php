<?php


/**
 * class.HomepageAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 */

class HomepageAction extends BaseAction {
	
	protected function check($objRequest, $objResponse) {
		Common::chenkSite();
	}

	protected function service($objRequest, $objResponse) {
		switch($objRequest -> getSwitch()) {
			case 'getadxml':
				$this -> doGetAdXml($objRequest, $objResponse);
			break;
			case 'sitemap':
				$this -> doSiteMap($objRequest, $objResponse);
			break;
			case 'googlesitemap':
				$this -> doGoogleSiteMap($objRequest, $objResponse);
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
		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);
		$hotProductNum = Common::getSiteInfo('value1', 'commend');
		$hotProductNum = empty($hotProductNum) ? '17' : $hotProductNum + 1; 
		$productCount = ProductDao::getNumProduct();
		$pid_rand = rand(0, ($productCount - $hotProductNum));
		$arrHotproduct = ProductDao::getProduct(NULL, NULL, NULL, 1, $hotProductNum, " AND id > $pid_rand AND store > 0 ORDER BY hit_times DESC, store DESC");
		$num = count($arrHotproduct);
		$k = 0;
		for($i = 0; $i<$num; $i++) {
			if($i%4 == 0 || $i == 0) {
				$k++;
				$arrHotproduct[$i]['hid'] = $k;
				if($i > 0) $arrHotproduct[$i - 1]['hid'] = 8;
			} else {
				$arrHotproduct[$i]['hid'] = 7;
			}
			$arrHotproduct[$i]['m_pic'] = Patch::getProductMImg($arrHotproduct[$i]['m_pic']);
			$arrHotproduct[$i]['url'] = Patch::getProductUrl($arrHotproduct[$i]['id']);
			//$arrHotproduct[$i]['price_member'] = $arrHotproduct[$i]['price']*0.9;
		}
		$specialProduct = NULL;
		if($num > 0) {
			$arrHotproduct[$num - 2]['hid'] = 8;
			$specialProduct = $arrHotproduct[$num - 1];
			unset($arrHotproduct[$num - 1]);
		}
		
		$arrNews = NewsDao::getNews();
		$num = count($arrNews);
		for($i = 0; $i<$num; $i++) {
			$arrNews[$i]['url'] = Patch::getNewsUrl($arrNews[$i]['id']);
			$arrNews[$i]['shot_title'] = Utilities::cutString($arrNews[$i]['title'], '40');
		}
		
		$arrClasses = Common::getClassInfo();
		$num = count($arrClasses);
		$channel_num = 0;
		$arrChannelProduct = array();
		for($i = 0; $i < $num; $i++) {
			if($arrClasses[$i]['cpid'] == 0 && $arrClasses[$i]['id'] == $arrClasses[$i]['cid']) {
				$str_cid = '';
				$arrChannel = Common::getClassInfo(NULL, $arrClasses[$i]['id']);
				$channelNum = count($arrChannel);
				for($j = 0; $j<$channelNum; $j++) {
					$str_cid .= $arrChannel[$j]['id'].',';
				}
				$str_cid = trim($str_cid, ',');
				if(!empty($str_cid)) {
					$arrPruduct = ProductDao::getProduct($str_cid, NULL, NULL, 1, 3, " AND store > 0 ORDER BY hit_times DESC, store DESC");
					if(!empty($arrPruduct)) {
						$prod_num = count($arrPruduct);
						for($k = 0; $k < $prod_num; $k++) {
							$arrPruduct[$k]['m_pic'] = Patch::getProductMImg($arrPruduct[$k]['m_pic']);
							$arrPruduct[$k]['url'] = Patch::getProductUrl($arrPruduct[$k]['id']);
						}
						$arrChannelProduct[$channel_num]['product'] = $arrPruduct;
						$arrChannelProduct[$channel_num]['url'] = $arrClasses[$i]['url'];
						$arrChannelProduct[$channel_num]['name'] = $arrClasses[$i]['name'];
						$channel_num++;
					}
				}
			}
		}

		$arrAd = Common::getCommonAdvertising('1000000000');
		$objResponse -> setTplValue("product_num", $k);
		$objResponse -> setTplValue("hotproduct", $arrHotproduct);
		$objResponse -> setTplValue("specialProduct", $specialProduct);
		$objResponse -> setTplValue("arrNews", $arrNews);
		$objResponse -> setTplValue("class", $arrClasses);
		$objResponse -> setTplValue("arrChannelProduct", $arrChannelProduct);
		$objResponse -> setTplValue("userinfo", $cookieUser);
		$objResponse -> setTplValue("adinfo", $arrAd);
		$objResponse -> setTplValue("menu_nav", 'homepage');
		//搜索关键字
		$objResponse -> setTplValue("keyword", '');
		$objResponse -> setTplValue("__Meta", Common::getMeta());
		$objResponse -> setTplName("#Homepage");
	}
	
	protected function doGetAdXml($objRequest, $objResponse) {
		$this -> setDisplay();
		$arrAd = Common::getCommonAdvertising('1000000000');
		$arrAd = $arrAd['value1'];
		$num = count($arrAd);
		$xml = '<?xml version="1.0" encoding="UTF-8"?><list>';
		for($i = 0; $i < $num; $i++) {
			$xml .= "<show><title>".Utilities::formatXmlSpecialChar($arrAd[$i]['text'])
				 ."</title><subTitle>".Utilities::formatXmlSpecialChar($arrAd[$i]['title'])
				 ."</subTitle><description>".Utilities::formatXmlSpecialChar($arrAd[$i]['contents'])."</description><path>"
			     .Utilities::formatXmlSpecialChar($arrAd[$i]['pic'])."</path><link>"
				 .Utilities::formatXmlSpecialChar($arrAd[$i]['url'])."</link></show>";
		}
		$xml .= '</list>';
		header("Content-type:application/xml");
		echo $xml;		
	}
	
	protected function doSiteMap($objRequest, $objResponse) {
		$productCount = ProductDao::getNumProduct();
		$arrPruduct = ProductDao::getProduct(NULL, NULL, NULL, 1, $productCount);
		$prod_num = count($arrPruduct);
		$arrMeta = Common::getMeta();
		for($k = 0; $k < $prod_num; $k++) {
			$arrPruduct[$k]['m_pic'] = Patch::getProductMImg($arrPruduct[$k]['m_pic']);
			$arrPruduct[$k]['url'] = 'http://' . $arrMeta['Url'] . '/' . Patch::getProductUrl($arrPruduct[$k]['id']);
		}
		$this -> createfile(true, 'product.html', __PRODUCT_IMG . 'sitemap/');
		$objResponse -> setTplValue("arrPruduct", $arrPruduct);
		$objResponse -> setTplValue("keyword", '');
		$objResponse -> setTplValue("__Meta",$arrMeta );
		$objResponse -> setTplName("#Sitemap");
	}
	
	protected function doGoogleSiteMap($objRequest, $objResponse) {
		$this -> setDisplay();
		$productCount = ProductDao::getNumProduct();
		$arrPruduct = ProductDao::getProduct(NULL, NULL, NULL, 1, $productCount);
		$prod_num = count($arrPruduct);
		$arrMeta = Common::getMeta();
		$xml = '<?xml version="1.0" encoding="UTF-8"?><urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9"'
			.' xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9'
			.' http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd">';
		for($k = 0; $k < $prod_num; $k++) {
			$arrPruduct[$k]['m_pic'] = Patch::getProductMImg($arrPruduct[$k]['m_pic']);
			$arrPruduct[$k]['url'] = 'http://' . $arrMeta['Url'] . '/' . Patch::getProductUrl($arrPruduct[$k]['id']);
			$xml .= '<url><loc>' . $arrPruduct[$k]['url'] . '</loc><changefreq>daily</changefreq></url>';
		}
		$xml .= '</urlset>';
		header("Content-type:application/xml");
		echo $xml;
		File::creatFile('sitemap.xml', $xml, __PRODUCT_IMG . 'sitemap/');
	}
}
?>