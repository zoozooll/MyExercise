<?php


/**
 * class.ProductAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 */

class ProductAction extends BaseAction {
	
	protected function check($objRequest, $objResponse) {
		checkNum($objRequest -> pid);
	}

	protected function service($objRequest, $objResponse) {

		switch($objRequest -> getSwitch()) {
			case 'review':
				$this -> doReview($objRequest, $objResponse);
			break;
			case 'repeatprice':
				$this -> repeatPrice($objRequest, $objResponse);
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
		$cps = $objRequest -> cps;
		if(!empty($cps)) {
			$cps = decode($cps);
			$arrCps = explode(' ', $cps);
		}
		$pid = $objRequest -> pid;

		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);
		if(isset($arrCps)) {
			if($arrCps[0] > 0) $objCookie -> cps = $arrCps[0];
		}

		$arrProduct = ProductDao::getProduct(NULL, $pid);
		$rs = array();
		$arrCategoryInfo = array();
		$arrCatnav = array();
		if(!empty($arrProduct)) {
			ProductDao::updateProductHit($pid);
			$arrProduct['m_pic'] = Patch::getProductMImg($arrProduct['m_pic']);
			$arrProduct['b_pic'] = Patch::getProductBImg($arrProduct['b_pic']);
			$arrProduct['src_pic'] = Patch::getProductImg($arrProduct['s_pic']);
			$arrProduct['url'] = Patch::getProductUrl($pid);
			$arrCategoryInfo = Common::getClassInfo($arrProduct['cid']);
			//catnav
			$arrCatnav = Common::getClassNav($arrProduct['cid']);
			if(file_exists(__PRODUCT_XML.$pid.'.php')) {
				include_once(__PRODUCT_XML.$pid.'.php');
			}
		}
		//next product 
		$arrNextProduct = ProductDao::getProduct(NULL, $pid + 1);
		if(!empty($arrNextProduct)) {
			$arrNextProduct['m_pic'] = Patch::getProductMImg($arrNextProduct['m_pic']);
			$arrNextProduct['url'] = Patch::getProductUrl($pid + 1);
		}
		//product_related 
		$arrProductRelated = array();
		if(isset($arrProduct['product_related'])) {
			if($arrProduct['product_related'] == '0') {
				$max = $pid + 10;
				$min = $pid - 10;
				$arrProductRelated = ProductDao::getProduct($arrProduct['cid'], NULL, NULL, 1, 5, " AND id <= $max AND id >=$min");
				$num = count($arrProductRelated);
				for($i = 0; $i < $num; $i++) {
					$arrProductRelated[$i]['m_pic'] = Patch::getProductMImg($arrProductRelated[$i]['m_pic']);
					$arrProductRelated[$i]['url'] = Patch::getProductUrl($arrProductRelated[$i]['id']);
				}
			} else {
				$arrPid = explode($arrProduct['product_related']);
				$str_pid = '';
				foreach($arrPid  as $k => $v) {
					if(is_numeric($v)) {
						$str_pid .= $v . ',';
					}
				}
				$str_pid = trim($str_pid, ',');
				if(!empty($str_pid)) {
					$arrProductRelated = ProductDao::getProduct(NULL, $str_pid);
					$num = count($arrProductRelated);
					for($i = 0; $i < $num; $i++) {
						$arrProductRelated[$i]['m_pic'] = Patch::getProductMImg($arrProductRelated[$i]['m_pic']);
						$arrProductRelated[$i]['url'] = Patch::getProductUrl($arrProductRelated[$i]['id']);
					}
				}
			}
		}
		
		$arrReview = ProductDao::getProductReview($pid);
		$num = count($arrReview);
		for($i = 0; $i < $num; $i++) {
			$arrReview[$i]['str_billno'] = substr($arrReview[$i]['billno'], 0, 5);
		}
		$usealipay = '';
		@include_once(__COMMSITE . 'payinfo.php');
		$objResponse -> setTplValue("usealipay", $usealipay);
		$objResponse -> setTplValue("product", $arrProduct);
		$objResponse -> setTplValue("arrReview", $arrReview);
		$objResponse -> setTplValue("arrNextProduct", $arrNextProduct);
		$objResponse -> setTplValue("arrProductRelated", $arrProductRelated);
		$objResponse -> setTplValue("productspec", $rs);//xml
		$objResponse -> setTplValue("categoryinfo", $arrCategoryInfo);
		$objResponse -> setTplValue("userinfo", $cookieUser);
		$objResponse -> setTplValue("catnav", $arrCatnav);
		//$objResponse -> setTplValue("class", $arrClasses);
		//搜索关键字$description = NULL, $keywords = NULL, $content = NULL
		$objResponse -> setTplValue("keyword", '');
		$objResponse -> setTplValue("__Meta", Common::getMeta($arrProduct['name'], $arrProduct['name'] . '的详细信息，'. $arrProduct['name'] . '的价格，'.$arrProduct['name'].'的购买途径',$arrProduct['name'],$arrProduct['name'].'最详细的商品介绍'));
		$objResponse -> setTplName("#Product");
	}
	
	protected function doReview($objRequest, $objResponse) {
		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);
		$arrValue['uid'] = $cookieUser['id'];
		$arrValue['pid'] = trim($objRequest -> pid);
		$arrValue['title'] = trim($objRequest -> title);
		$arrValue['good'] = trim($objRequest -> good);
		$arrValue['bad'] = trim($objRequest -> bad);
		$arrValue['billno'] = trim($objRequest -> billno);
		$arrValue['contents'] = trim($objRequest -> contents);
		$arrValue['point'] = trim($objRequest -> point);
		if(empty($arrValue['title'])) alert('标题不能为空！');
		if(empty($arrValue['billno'])) alert('订单号不能为空！');
		if(empty($arrValue['good']) && empty($arrValue['bad'])) alert('优点和不足至少填1个！');
		if(empty($arrValue['point'])) alert('平分不能为空！');
		if(ProductDao::checkOrderByBillno($arrValue['billno'], $arrValue['pid']) > 0) {
		} else {
			alert('该订单号不符合该产品，请检查！');
		}
		ProductDao::insertReview($arrValue);
		alert('评论成功，等待审核！', 0);
		redirect(Patch::getProductUrl($arrValue['pid']));
	
	}
	
	protected function repeatPrice($objRequest, $objResponse) {
		$pid = $objRequest -> pid;
		$url = $objRequest -> url;
		$price = $objRequest -> price;
		$objCookie = new Cookie;
		$cookieUser = Common::getLoginUser($objCookie);
		$this -> setDisplay();
		if(ProductDao::insertLowerPrice($pid, $cookieUser['id'], $url, $price, onLineIp(), Common::getCommonTime())) {
			echo 0;
		} else {
			echo 1;	
		}
	}

}
?>