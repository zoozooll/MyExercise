<?php


/**
 * class.CategoryAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 */

class CategoryAction extends BaseAction {
	
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
		$arrAtr = $this -> getAtr($objRequest);
		$arrParam = $arrAtr;
		$arrParam['mp'] = $objRequest -> mp;
		$arrParam['lp'] = $objRequest -> lp;
		$arrParam['brand'] = $objRequest -> brand;
		$arrParam['se'] = $objRequest -> se;
		$arrParam['v'] = $objRequest -> v;
		$arrParam['c'] = $objRequest -> c;
		$arrParam['s'] = $objRequest -> s;
		
		$keyword = $arrParam['se'];
		$pn = $objRequest -> pn;
		$arrParam['pn'] = $pn;
		
		$ext_title = '';
		if(!empty($arrParam['se'])) $ext_title .= conutf8(',关键字:').$arrParam['se'];
		if(!empty($arrParam['mp']) && !empty($arrParam['lp'])) {
			$ext_title .= conutf8(',最低价格:').$arrParam['lp'] . ',' . conutf8(',最高价格:') . $arrParam['mp'];
		} elseif(!empty($arrParam['mp'])) {
			$ext_title .= conutf8(',最低价格:').$arrParam['mp'];
		} elseif(!empty($arrParam['lp'])) {
			$ext_title .= conutf8(',最高价格:').$arrParam['lp'];
		}

		
		$count = empty($arrParam['c']) ? 12 : $arrParam['c'];
		$count = $count < 12 ? 12 : $count;
		$count = $count > 30 ? 30 : $count;
		//cookie user
		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);
		
		//channel info
		$arrClasses = Common::getClassInfo();
		//category info
		$arrCategoryInfo = Common::getClassInfo($cid);
		$arrChannel = Common::getClassInfo($arrCategoryInfo['cid']);
		//catnav
		$arrCatnav = Common::getClassNav($cid);
		
		//cateory info AND fliter info
		$arrCategory = array();
		$arrFilter = '';
		$isleaf = 1;
		$filtersql = '';
		$arrRangePrice = '';

		if(!empty($arrParam['lp'])) {
			$filtersql .= " AND price >='".$arrParam['mp']."'";
		}
		if(!empty($arrParam['mp'])) {
			$filtersql .= " AND price <='".$arrParam['mp']."'";
		}
		
		//定义变量
		$arrBrand = array();//品牌
		$nobrandurl = '';
		$nopriceurl = '';
		
		if(!empty($arrCategoryInfo['ccidnum'])) {
			foreach($arrClasses as $k => $v) {
				if($v['cid'] == $arrCategoryInfo['cid'] && $v['orderid'] > $arrCategoryInfo['orderid']) {
					if($v['orderid'] <= ($arrCategoryInfo['orderid'] + $arrCategoryInfo['ccidnum'])) {
						$arrCategory[] = $v;
					}
				}
			}
			$num = count($arrCategory);
			$cid = '';
			for($i = 0; $i<$num; $i++) {
				$cid .= $arrCategory[$i]['id'].',';
			}
			$cid = trim($cid, ',');
			$isleaf = 0;
			if(!empty($cid)) $arrSBrand = ProductDao::getProductBrand($cid);
		} else {
			//produc filter
			$arrFilter = ProductDao::getFilterProductValue($cid);
			if(!empty($arrFilter)) {
				$num = count($arrFilter);
				$aid = 1;
				$select = 0;
				$mi = 0;
				for($i = 0; $i<$num; $i++) {
					$arrFilter[$i]['more'] = '';
					if(isset($arrFilter[$i - 1]['name'])) {
						if(trim($arrFilter[$i - 1]['name']) != trim($arrFilter[$i]['name'])) {
							$arrFilter[$i]['ar'] = '1';
							$arrFilter[$i - 1]['br'] = '2';
							$arrFilter[$i]['select'] = '';
							$arrFilter[$i]['selecturl'] = '';
							$select = $i;
							$aid += 1;
							if($mi > 4)  $arrFilter[$i - 1]['more'] = '2';
							$mi = 0;
						} else {
							$arrFilter[$i]['ar'] = NULL;	
							$arrFilter[$i]['br'] = NULL;	
							$arrFilter[$i]['select'] = '';
							$arrFilter[$i]['selecturl'] = '';
							$mi +=1;
						}
					}
					if($mi == 5) $arrFilter[$i]['more'] = '1';
	
					if(trim($arrAtr['atr'.$aid]) == trim($arrFilter[$i]['value'])) {
						$ext_title .= ','.trim($arrFilter[$i]['name']).':'.trim($arrFilter[$i]['value']);
						$filtersql .= " AND atr$aid='".$arrFilter[$i]['id']."' AND value$aid='".$arrAtr['atr'.$aid]."'";
						$arrParam['atr'.$aid] = '';
						$arrFilter[$select]['select'] = $arrFilter[$i]['value'];
						$arrFilter[$select]['selecturl'] = Patch::getCategoryUrl($arrChannel['enname'], $arrCategoryInfo['id'], $arrCategoryInfo['enname'], $arrParam);
						$arrFilter[$i]['url'] = '';
						$arrParam['atr'.$aid] = urlencode(trim($arrFilter[$i]['value']));
					} else {
						$arrParam['atr'.$aid] = urlencode(trim($arrFilter[$i]['value']));
						$arrFilter[$i]['url'] = Patch::getCategoryUrl($arrChannel['enname'], $arrCategoryInfo['id'], $arrCategoryInfo['enname'], $arrParam);
						unset($arrParam['atr'.$aid]);
						if($arrAtr['atr'.$aid] != '') $arrParam['atr'.$aid] = urlencode(trim($arrAtr['atr'.$aid]));
					}
				}
				unset($select);
				unset($arrAtr);
				$arrFilter[0]['ar'] = '1';
				$arrFilter[$num - 1]['br'] = '2';
				if($mi > 4)  $arrFilter[$i - 1]['more'] = '2';
				unset($mi);
			}
			//fliter product price range
			$rangenum = 0;
			$arrRangePrice = ProductDao::getProductPriceRange($cid);
			if(($arrRangePrice['mp'] - $arrRangePrice['lp']) >= 100) {
				$rangenum = 2;
			}
			if(($arrRangePrice['mp'] - $arrRangePrice['lp']) >= 200) {
				$rangenum = 4;
			}
			if(($arrRangePrice['mp'] - $arrRangePrice['lp']) >= 1200) {
				$rangenum = 6;
			}
			
			if($rangenum > 0) {
				$range = ceil(($arrRangePrice['mp'] - $arrRangePrice['lp'])/$rangenum);
				$mp = $arrParam['mp'];
				$lp = $arrParam['lp'];
				for($i = 0; $i<$rangenum; $i++) {
					$arrParam['mp'] = $arrRangePrice['lp'] + $range * ($i+1);
					if($i == ($rangenum - 1)) unset($arrParam['mp']);
					$arrParam['lp'] = $arrRangePrice['lp'] + $range * ($i);
					if($i == 0) unset($arrParam['lp']);
					$arrRangePrice[$i]['url'] = Patch::getCategoryUrl($arrChannel['enname'], $arrCategoryInfo['id'], $arrCategoryInfo['enname'], $arrParam);
					$arrRangePrice[$i]['price'] = $arrRangePrice['lp'] + $range * ($i+1);
				}
				
				if($mp > 0 || $lp > 0) {
					$arrParam['mp'] = '';
					$arrParam['lp'] = '';
					$nopriceurl = Patch::getCategoryUrl($arrChannel['enname'], $arrCategoryInfo['id'], $arrCategoryInfo['enname'], $arrParam);
					if($mp > 0) $filtersql .= " AND price <= '{$mp}'";
					if($lp > 0) $filtersql .= " AND price >= '{$lp}'";
				}
				$arrParam['mp'] = $mp;
				$arrParam['lp'] = $lp;
			} else {
				$arrRangePrice = '';
			}
			//fliter product brand
			$arrSBrand = ProductDao::getProductBrand($cid);
			$num = count($arrSBrand);
			$brand = $arrParam['brand'];
			if($brand > 0) {
				$filtersql .= " AND bid = '{$brand}'";
				$arrParam['brand'] = '';
				$nobrandurl['url'] = Patch::getCategoryUrl($arrChannel['enname'], $arrCategoryInfo['id'], $arrCategoryInfo['enname'], $arrParam);
			}
			$j = 0;
			$more = 0;
			for($i = 0; $i<$num; $i++) {
				if($brand == $arrSBrand[$i]['id']) {
					$nobrandurl['name'] = $arrSBrand[$i]['name'];
					continue;
				}
				$arrParam['brand'] = $arrSBrand[$i]['id'];
				$arrBrand[$j] = $arrSBrand[$i];
				$arrBrand[$j]['url'] = Patch::getCategoryUrl($arrChannel['enname'], $arrCategoryInfo['id'], $arrCategoryInfo['enname'], $arrParam);
				if($j == 4 && $num > 5) {
					$arrBrand[$j]['more'] = 1;
					$more = 1;
				} else {
					$arrBrand[$j]['more'] = 0;
				}
				$j++;
			}
			if($more == 1) $arrBrand[$j - 1]['more'] = 2;
			$arrParam['brand'] = $brand;
			unset($brand);
		}
		if(isset($nobrandurl['name'])) $ext_title .= conutf8(',品牌:').$nobrandurl['name'];
		$arrProduct = NULL;
		/*if($filtersql == '') {
			//not Filter cateory product
			$productNum = ProductDao::getNumProduct($cid, $keyword);
			$allPage = ceil($productNum/$count);
			$pn = $pn > $allPage ? $allPage : $pn;
			$pn = $pn < 1 || empty($pn) ? 1 : $pn;
			$arrProduct = ProductDao::getProduct($cid, NULL, $keyword, $pn, $count);
			$num = count($arrProduct);
			for($i = 0; $i<$num; $i++) {
				$arrProduct[$i]['url'] = Patch::getProductUrl($arrProduct[$i]['id']);
				$arrProduct[$i]['m_pic'] = Patch::getProductMImg($arrProduct[$i]['m_pic']);
				$arrProduct[$i]['b_pic'] = Patch::getProductBImg($arrProduct[$i]['b_pic']);
				$arrProduct[$i]['describes'] = Utilities::cutString($arrProduct[$i]['describes'], '150');
			}
		} else {
			//Filter cateory product*/
			if($arrParam['s'] == 'hot') {
				$filtersql .= " ORDER BY hit_times DESC, store DESC";
			} else {
				$filtersql .= " ORDER BY id DESC, store DESC";
			}
			$productNum = ProductDao::getNumProduct($cid, $keyword, $filtersql);
			$allPage = ceil($productNum/$count);
			$pn = $pn > $allPage ? $allPage : $pn;
			$pn = $pn < 1 || empty($pn) ? 1 : $pn;
			$arrProduct = ProductDao::getProduct($cid, NULL, $keyword, $pn, $count, $filtersql);

			$num = count($arrProduct);
			for($i = 0; $i<$num; $i++) {
				$arrProduct[$i]['url'] = Patch::getProductUrl($arrProduct[$i]['id']);
				$arrProduct[$i]['brand'] = '';
				$arrProduct[$i]['m_pic'] = Patch::getProductMImg($arrProduct[$i]['m_pic']);
				$arrProduct[$i]['b_pic'] = Patch::getProductBImg($arrProduct[$i]['b_pic']);
				$arrProduct[$i]['describes'] = Utilities::cutString($arrProduct[$i]['describes'], '150');
				if(!empty($arrSBrand)) {
					foreach($arrSBrand as $k => $v) {
						if($arrProduct[$i]['bid'] == $v['id']) {
							$arrProduct[$i]['brand'] = $v['name'];
						}
					}
				}
			}
		//}
		unset($arrSBrand);
		//page
		$arrPages = '';
		$arrPage = Patch::getPageArray($allPage, $pn);
		$arrParam['pn'] = '';
		$firstpage = Patch::getCategoryUrl($arrChannel['enname'], $arrCategoryInfo['id'], $arrCategoryInfo['enname'], $arrParam);
		for($i = 0; $i<$allPage; $i++) {
			$arrParam['pn'] = $arrPage[$i];
			$arrPages[$i]['url'] = Patch::getCategoryUrl($arrChannel['enname'], $arrCategoryInfo['id'], $arrCategoryInfo['enname'], $arrParam);
			$arrPages[$i]['pn'] = $arrPage[$i];
		}
		$arrParam['pn'] = $allPage;
		$lastpage = Patch::getCategoryUrl($arrChannel['enname'], $arrCategoryInfo['id'], $arrCategoryInfo['enname'], $arrParam);
		$arrParam['pn'] =  $pn + 1;
		$nextpage = Patch::getCategoryUrl($arrChannel['enname'], $arrCategoryInfo['id'], $arrCategoryInfo['enname'], $arrParam);
		$arrParam['pn'] =  $pn - 1;
		$prvepage = Patch::getCategoryUrl($arrChannel['enname'], $arrCategoryInfo['id'], $arrCategoryInfo['enname'], $arrParam);
		//销毁变量
		unset($arrPage);
		//retpage
		$arrParam['pn'] = $pn;
		$retpage = Patch::getCategoryUrl($arrChannel['enname'], $arrCategoryInfo['id'], $arrCategoryInfo['enname'], $arrParam);
		
		//cateory url
		$arrParam['s'] = 'hot';
		$arrCatUrl['hoturl']= Patch::getCategoryUrl($arrChannel['enname'], $arrCategoryInfo['id'], $arrCategoryInfo['enname'], $arrParam);
		$arrParam['s'] = 'new';
		$arrCatUrl['newurl']= Patch::getCategoryUrl($arrChannel['enname'], $arrCategoryInfo['id'], $arrCategoryInfo['enname'], $arrParam);
		$arrParam['s'] = $objRequest -> s;
		
		$arrParam['v'] = '';
		$arrCatUrl['norurl']= Patch::getCategoryUrl($arrChannel['enname'], $arrCategoryInfo['id'], $arrCategoryInfo['enname'], $arrParam);
		$arrParam['v'] = 'list';
		$arrCatUrl['listurl']= Patch::getCategoryUrl($arrChannel['enname'], $arrCategoryInfo['id'], $arrCategoryInfo['enname'], $arrParam);
		$arrParam['v'] = $objRequest -> v;
		$view = $objRequest -> v;
		
		$arrParam['c'] = '';
		$arrCatUrl['s_url']= Patch::getCategoryUrl($arrChannel['enname'], $arrCategoryInfo['id'], $arrCategoryInfo['enname'], $arrParam);
		$arrParam['c'] = '30';
		$arrCatUrl['m_url']= Patch::getCategoryUrl($arrChannel['enname'], $arrCategoryInfo['id'], $arrCategoryInfo['enname'], $arrParam);
		$arrParam['c'] = '30';
		$arrCatUrl['l_url']= Patch::getCategoryUrl($arrChannel['enname'], $arrCategoryInfo['id'], $arrCategoryInfo['enname'], $arrParam);

		//销毁变量
		unset($arrParam);
		unset($arrChannel);

		//hotProduct
		$arrHotProduct = ProductDao::getProduct($cid, NULL, NULL, 1, 3, " AND store > 0 ORDER BY hit_times DESC,  store DESC");
		$num = count($arrHotProduct);
		for($i = 0; $i<$num; $i++) {
			$arrHotProduct[$i]['url'] = Patch::getProductUrl($arrHotProduct[$i]['id']);
			$arrHotProduct[$i]['m_pic'] = Patch::getProductMImg($arrHotProduct[$i]['m_pic']);
			$arrHotProduct[$i]['b_pic'] = Patch::getProductBImg($arrHotProduct[$i]['b_pic']);
			$arrHotProduct[$i]['describes'] = Utilities::cutString($arrHotProduct[$i]['describes'], '150');
		}
		//news
		$arrChannelNews = NewsDao::getNews($cid);
		$num = count($arrChannelNews);
		for($i = 0; $i<$num; $i++) {
			$arrChannelNews[$i]['url'] = Patch::getNewsUrl($arrChannelNews[$i]['id']);
			$arrChannelNews[$i]['shot_title'] = Utilities::cutString($arrChannelNews[$i]['title'], '36');
		}
		
		//ad
		$arrAd = Common::getCommonAdvertising($cid);
		//set value
		$objResponse -> setTplValue("retpage", urlencode($retpage));
		$objResponse -> setTplValue("isleaf", $isleaf);
		$objResponse -> setTplValue("category", $arrCategory);
		$objResponse -> setTplValue("caturl", $arrCatUrl);
		$objResponse -> setTplValue("view", $view);
		$objResponse -> setTplValue("product", $arrProduct);
		$objResponse -> setTplValue("filter", $arrFilter);
		$objResponse -> setTplValue("brand", $arrBrand);
		$objResponse -> setTplValue("nobrandurl", $nobrandurl);
		$objResponse -> setTplValue("rangeprice", $arrRangePrice);
		$objResponse -> setTplValue("nopriceurl", $nopriceurl);
		$objResponse -> setTplValue("hotproduct", $arrHotProduct);
		$objResponse -> setTplValue("news", $arrChannelNews);
		$objResponse -> setTplValue("pn", $pn);
		$objResponse -> setTplValue("firstpage", $firstpage);
		$objResponse -> setTplValue("lastpage", $lastpage);
		$objResponse -> setTplValue("prvepage", $prvepage);
		$objResponse -> setTplValue("nextpage", $nextpage);
		$objResponse -> setTplValue("allpage", $allPage);
		$objResponse -> setTplValue("page", $arrPages);
		$objResponse -> setTplValue("categoryinfo", $arrCategoryInfo);
		$objResponse -> setTplValue("catnav", $arrCatnav);
		$objResponse -> setTplValue("class", $arrClasses);
		$objResponse -> setTplValue("adinfo", $arrAd);
		$objResponse -> setTplValue("userinfo", $cookieUser);
		//搜索关键字
		$objResponse -> setTplValue("keyword", $keyword);
		$objResponse -> setTplValue("se", '');
		$objResponse -> setTplValue("__Meta", Common::getMeta($arrCategoryInfo['name'] . $ext_title,$arrCategoryInfo['name'] . $ext_title,$arrCategoryInfo['name'] . $ext_title));
		$objResponse -> setTplName("#Category");
	}

	protected function getAtr($objRequest) {
		for($i = 1; $i <= 8; $i++) {
			$str = 'atr'.$i;
			$arrAtr[$str] = urldecode(trim($objRequest -> $str));
		}
		return $arrAtr;
	}
}
?>