<?php


/**
 * class.AdmincpAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 */

class AdmincpAction extends BaseAction {
	
	protected function check($objRequest, $objResponse) {
		AdminAction::checkLoginAdmin();
	}

	protected function service($objRequest, $objResponse) {
		switch($objRequest->getSwitch()) {
			default:
				$this -> doAdmincp($objRequest, $objResponse);
				break;
		}
	}
	
	protected function doAdmincp($objRequest, $objResponse) {
		$lid = $objRequest -> lid;
		$mid = $objRequest -> mid;
		$cid = $objRequest -> cid;
		$pid = $objRequest -> pid;
		$actid = $objRequest -> actid;
		$rs = '';

		if(empty($lid)) {
			$objSession = new Session;
			$adminid = $objSession -> glo_id;
			$arrAdminSetInfo = AdminDao::getAdminInfo($adminid);
			$objResponse -> setTplValue("fun_name", '');
			//set compiler false
			$this -> setCompiler();
			//set tpl
			$objResponse -> setTplValue("adminsetinfo", $arrAdminSetInfo);
			$objResponse -> setTplName("../admin/#welcome");
			return;
		}
		
		$objSession = new Session;
		$aid = $objSession -> glo_id;
		
		if(!($admintpl = AdminDao::getFunc($lid, $aid))) {
			alert(conutf8('没有权限！'));
			redirect("admin/login.php"); 
		}
		
		$fun = $admintpl["fun"];
		$adminenname = AdminDao::getEnName($mid);
		switch($adminenname) {
			case "Baseset":
				$func = "get".$adminenname."Value";
				$rs = AdminDao::$func($fun);
				if($fun == 'regAndCon') {
					$objResponse -> setTplValue("__Meta", Common::getMeta());
				}
				break;
				
			case "ClasssSet":
				if(($objRequest -> act) == "delattr") {
					AdminDao::delAttribute($cid, $objRequest -> orderid);
				}
				if(($objRequest -> act) == 'del') {
					if(AdminDao::getClassesProduceCount($cid) > 0) {
						alert(conutf8('此类别下面还有商品，请先删除或者更改商品的类别再删除！'));
					}
					AdminDao::delClasses($cid);
					AdminDao::delAttribute($cid);
				}
				$rs = $this -> getClasssSet();
				break;
				
			case "news":
				$rs = $this -> getClasssSet();
				break;
			case "Merchandise":
				$objResponse -> setTplValue("classes", $this -> getClasssSet());
				break;
				
			default:
				break;
		}
		
		if(!empty($pid)) {
			$fun = 'addgoods';
		}

		switch($fun) {
			case "baseset":
				$arrDir = File::getDir(__ROOT_TPLS_TPATH);
				unset($arrDir['admin']);
				unset($arrDir['templates_c']);
				sort($arrDir);
				$objResponse -> setTplValue("dir", $arrDir);
				break;
			case "setclasses":
				$mv = $objRequest -> mv;
				for($i = 0; $i < count($rs); $i++) {
					if($rs[$i]['id'] == $cid) {
						$cpid = empty($rs[$i]["cpid"]) ? $rs[$i]['cid'] : $rs[$i]["cpid"];
						$objResponse -> setTplValue("rsname", $rs[$i]);
						$objResponse -> setTplValue("cpid", $cpid);
						$objResponse -> setTplValue("cid", $cid);
					}
				}
				if($mv == 1) $mv = $cid;
				$objResponse -> setTplValue("classes", $rs);
				$objResponse -> setTplValue("mv", $mv);
				break;
				
			case "attribute":
				$k = 0;
				for($i = 0; $i < count($rs); $i++) {
					if($rs[$i]['id'] == $cid) {
						$objResponse -> setTplValue("cid", $cid);
					}
				}
				$objResponse -> setTplValue("classes", $rs);
				unset($rs);
				if($cid > 0) {
					$rs = ClassesDao::getAttribute($cid);
					$objResponse -> setTplValue("attclass", ClassesDao::getClasssSetValue($cid));
					$objResponse -> setTplValue("attnum", count($rs));
				}
				break;
				
			case "brand":
				unset($rs);
				if($objRequest -> act == 'del') {
					AdminDao::deleteBrand($objRequest -> delbid);
				}
				$rs = ClassesDao::getBrand();
				break;
			case "supply":
				unset($rs);
				if($objRequest -> act == 'del') {
					AdminDao::deleteSupply($objRequest -> sid);
				}
				$rs = ClassesDao::getSupply();
				break;
			
			case "series":
				if($objRequest -> act == 'del') {
					AdminDao::deleteSeries($objRequest -> delsid);
				}
				$bid = $objRequest -> bid;
				$arrBrand = ClassesDao::getBrand();
				$objResponse -> setTplValue("brand", $arrBrand);
				unset($rs);
				if(!empty($bid)) {
					$rs = ClassesDao::getSeries($bid);
					$objResponse -> setTplValue("bid", $bid);
				}
				break;
			case 'freight':
				unset($rs);
				if($objRequest -> act == 'del') {
					AdminDao::deleteFreight($objRequest -> fid);
				}
				$rs = ClassesDao::getFreight();
				break;
			case 'bank':				
				unset($rs);
				$type = $objRequest -> type;
				$type = empty($type) ? 'B' : $type;
				if($objRequest -> act == 'del') {
					AdminDao::deleteBank($objRequest -> bid);
				}
				if($type == 'W') {
					$isusepay = '';
					$partner = '';
					$security_code = '';
					$seller_email = '';
					$usealipay = '';
					//
					$strSpid = '';
					$strSpkey = '';
					$usetenpay = '';
					
					@include_once(__COMMSITE . 'payinfo.php');
					$objResponse -> setTplValue("isusepay", $isusepay);
					//alipay
					$objResponse -> setTplValue("partner", $partner);
					$objResponse -> setTplValue("security_code", $security_code);
					$objResponse -> setTplValue("seller_email", $seller_email);
					$objResponse -> setTplValue("usealipay", $usealipay);
					//tenpay
					$objResponse -> setTplValue("strspid", $strSpid);
					$objResponse -> setTplValue("strspkey", $strSpkey);
					$objResponse -> setTplValue("usetenpay", $usetenpay);
				}
				$objResponse -> setTplValue("type", $type);
				$rs = ClassesDao::getBank($type);
				break;	
			
			case "goods":
				$delpid = $objRequest -> delpid;
				if(!empty($delpid)) {
					AdminDao::deleteProduct(NULL, $delpid);
					alert(conutf8("删除成功！"), 0);
					$this -> sendHeader();
				}
				if(!empty($cid)) {
					$objResponse -> setTplValue("cid", $cid);
				}
				$count = 10;
				$keyword = trim($objRequest -> keyword);
				$productNum = ProductDao::getNumProduct($cid, $keyword);
				
				$pid = $objRequest -> pid;
				$pn = $objRequest -> pn;
				$allPage = ceil($productNum/$count);
				$pn = $pn > $allPage ? $allPage : $pn;
				$pn = $pn < 1 || empty($pn) ? 1 : $pn;

				$arrProduct = ProductDao::getProduct($cid, $pid, $keyword, $pn, $count);
				$objResponse -> setTplValue("products", $arrProduct);
				$objResponse -> setTplValue("pn", $pn);
				$objResponse -> setTplValue("allpage", $allPage);
				$objResponse -> setTplValue("keyword", $keyword);
				$objResponse -> setTplValue("productnum", $productNum);
				$objResponse -> setTplValue("page", Utilities::getPageArray($allPage, $pn));
				break;
			
			case "addgoods":
				$succid =  $objRequest -> succid;
				if(!empty($cid)) {
					$arrAtr = ClassesDao::getAttribute($cid);
					if(!empty($pid)) {
						$arrProduct = ProductDao::getProduct($cid, $pid);
						$arrAtrValue =  ProductDao::getProductAttribute($pid);
						$num = count($arrAtr);
						if($num > 0 && count($arrAtrValue) > 0) {
							for($i = 0; $i<$num; $i++) {
								foreach($arrAtrValue as $k => $v) {
									if($arrAtr[$i]['id'] == $v['atrid']) {
										$arrAtr[$i]['value'] = $v['value'];
									}
								}
							}
						}
						$objResponse -> setTplValue("product", $arrProduct);
						$objResponse -> setTplValue("pid", $pid);
					}
					if(!empty($actid)) {
						$objResponse -> setTplValue("actid", $actid);
					}
					$arrSuccessProduct = array();
					if(!empty($succid)) {
						$arrSuccessProduct = ProductDao::getProduct(NULL, $succid);
						$arrSuccessProduct['url'] = Patch::getProductUrl($succid);
					}
					
					$objResponse -> setTplValue("successproduct", $arrSuccessProduct);
					$objResponse -> setTplValue("atr", $arrAtr);
					$objResponse -> setTplValue("brand", ClassesDao::getBrand());
					$objResponse -> setTplValue("supply", ClassesDao::getSupply());
					$objResponse -> setTplValue("cid", $cid);
					$objResponse -> setTplValue("series", ClassesDao::getSeries());
				}
				break;
			
			case 'productnews':
				$delnid = $objRequest -> delnid;
				if(!empty($delnid)) {
					AdminDao::deleteNews($delnid);
					alert(conutf8("删除成功！"), 0);
					$this -> sendHeader();
				}
				if(!empty($cid)) {
					$objResponse -> setTplValue("cid", $cid);
				}
				$count = 10;
				$newsNum = NewsDao::getNumNews($cid);
				$keyword = trim($objRequest -> keyword);
				$nid = $objRequest -> nid;
				$pn = $objRequest -> pn;
				$allPage = ceil($newsNum/$count);
				$pn = $pn > $allPage ? $allPage : $pn;
				$pn = $pn < 1 || empty($pn) ? 1 : $pn;

				$arrNews = NewsDao::getNews($cid, $nid, $keyword, $pn, $count);
				if(($objRequest -> modify) == 'yes') {
					$fun = 'addnews';
				} else {
					$objResponse -> setTplValue("pn", $pn);
					$objResponse -> setTplValue("allpage", $allPage);
					$objResponse -> setTplValue("page", Utilities::getPageArray($allPage, $pn));
				}
				$objResponse -> setTplValue("news", $arrNews);
				break;
				
			case "addnews":
				$nid = $objRequest -> nid;
				if(!empty($cid)) {
					$objResponse -> setTplValue("cid", $cid);
				}
				if(!empty($nid)) {
					$arrNews = NewsDao::getNews(NULL, $nid);
					$objResponse -> setTplValue("news", $arrNews[0]);
				}
				if(!empty($actid)) {
					$objResponse -> setTplValue("actid", $actid);
				}
				break;
			case "check":
				$state = trim($objRequest -> state);
				$keyword = trim($objRequest -> keyword);
				$bdata = trim($objRequest -> date8b);
				$edata = trim($objRequest -> date8a);
				$billno = trim($objRequest -> billno);
				
				$count = 5;
				$newsNum = AdminDao::getNumCheck($state, $bdata, $edata, $billno, $keyword);
				$pid = $objRequest -> pid;
				$pn = $objRequest -> pn;
				$allPage = ceil($newsNum/$count);
				$pn = $pn > $allPage ? $allPage : $pn;
				$pn = $pn < 1 || empty($pn) ? 1 : $pn;//getCheck( = NULL, $b = 1, $c = 10)

				$arrCheck = AdminDao::getCheck($state, $bdata, $edata, $billno, $keyword, $pn, $count);
				$num = count($arrCheck);
				for($i = 0; $i<$num; $i++) {
					$arrCheck[$i]['url'] = Patch::getProductUrl($arrCheck[$i]['id']);
					//
					$arrCheck[$i]['sell_price'] = isset($arrCheck[$i]['price']) ? $arrCheck[$i]['price'] : NULL;
					$arrCheck[$i]['member_offer'] = isset($arrCheck[$i]['member_offer']) ? $arrCheck[$i]['member_offer'] : NULL;
					if($arrCheck[$i]['member_offer'] == 1 && $arrCheck[$i]['uid'] > 0) {
						$arrCheck[$i]['sell_price'] = $arrCheck[$i]['price_member'];
					}
					//
					$arrCheck[$i]['special_offer'] = isset($arrCheck[$i]['special_offer']) ? $arrCheck[$i]['special_offer'] : NULL;
					if($arrCheck[$i]['special_offer'] == 1) {
						$arrCheck[$i]['sell_price'] = $arrCheck[$i]['price_special'];
					} 
					//
					$arrCheck[$i]['num'] = isset($arrCheck[$i]['num']) ? $arrCheck[$i]['num'] : 0;
					$arrCheck[$i]['sellprice'] = isset($arrCheck[$i]['sellprice']) ? $arrCheck[$i]['sellprice'] : NULL;
					if($arrCheck[$i]['sellprice'] != NULL) $arrCheck[$i]['sell_price'] = $arrCheck[$i]['sellprice'];
					$arrCheck[$i]['all_price'] = $arrCheck[$i]['sell_price']*$arrCheck[$i]['num'];
				}
				
				$objResponse -> setTplValue("billno", $billno);
				$objResponse -> setTplValue("keyword", $keyword);
				$objResponse -> setTplValue("date8b", $bdata);
				$objResponse -> setTplValue("date8a", $edata);
				$objResponse -> setTplValue("check", $arrCheck);
				$objResponse -> setTplValue("state", $state);
				$objResponse -> setTplValue("pn", $pn);
				$objResponse -> setTplValue("uppn", $pn-1);
				$objResponse -> setTplValue("downpn", $pn+1);
				$objResponse -> setTplValue("allpage", $allPage);
				$objResponse -> setTplValue("page", Utilities::getPageArray($allPage, $pn));
				break;
			case "modifymenber":
				$deluid = $objRequest -> deluid;
				$uid = $objRequest -> uid;
				$act = $objRequest -> act;
				if(!empty($deluid)) {
					AdminDao::deleteUser($deluid);
					alert(conutf8("删除成功！"), 0);
					$this -> sendHeader();
				}
				if($act == 'edituser') {
					$arrValue['name'] = trim($objRequest -> name);
					$arrValue['email'] = trim($objRequest -> email);
					$arrValue['phone'] = trim($objRequest -> phone);
					$arrValue['mobile'] = trim($objRequest -> mobile);
					$arrValue['address'] = trim($objRequest -> address);
					$arrValue['postcode'] = trim($objRequest -> postcode);
					$arrValue['sex'] = trim($objRequest -> sex);
					$arrValue['birthday'] = trim($objRequest -> birthday);
					$arrValue['qq'] = trim($objRequest -> qq);
					$arrValue['msn'] = trim($objRequest -> msn);
					$arrValue['taobao'] = trim($objRequest -> taobao);
					$arrValue['skype'] = trim($objRequest -> skype);
					if(!empty($arrValue['name']) && !empty($arrValue['email']) && !empty($arrValue['phone'])) AdminDao::updateUserInfo($arrValue, $uid);
				}
				$pn = $objRequest -> pn;
				$keyword = trim($objRequest -> keyword);
				$count = 10;
				$userNum = AdminDao::getNumUser($uid, $keyword);
				$allPage = ceil($userNum/$count);
				$pn = $pn > $allPage ? $allPage : $pn;
				$pn = $pn < 1 || empty($pn) ? 1 : $pn;//getCheck( = NULL, $b = 1, $c = 10)
				$arrUser = AdminDao::getUser($uid, $keyword, $pn, $count);

				$objResponse -> setTplValue("act", $act);
				$objResponse -> setTplValue('uid', $uid);
				$objResponse -> setTplValue("user", $arrUser);
				$objResponse -> setTplValue("pn", $pn);
				$objResponse -> setTplValue("uppn", $pn-1);
				$objResponse -> setTplValue("downpn", $pn+1);
				$objResponse -> setTplValue("allpage", $allPage);
				$objResponse -> setTplValue("page", Utilities::getPageArray($allPage, $pn));
				break;
			case "ad":
				$arrClassInfo = NULL;
				$arrAd = NULL;
				$rs = $this -> getClasssSet();
				if($cid > 0) {
					$arrClassInfo = Common::getClassInfo($cid);
					$arrAd = AdminDao::getAdvertising($cid);
				}
				$objResponse -> setTplValue("cid", $cid);
				$objResponse -> setTplValue("classinfo", $arrClassInfo);
				$objResponse -> setTplValue("advertising", $arrAd);
				break;
			case "commend":
				$rs = AdminDao::getBasesetValue('commend');
				if(empty($rs)) {
					$rs['value1'] = 20;
					$rs['value2'] = 20;
					$rs['value3'] = 20;
					AdminDao::updateBasesetValue('commend', $rs);
					UpdateCommonDao::updateSiteInfo("commend");
				}
				break;	
			case "password":
				$arrAdmin = AdminDao::getAdmin($objSession -> glo_adminname, $objSession -> glo_adminpassword);
				$objResponse -> setTplValue("admin", $arrAdmin);
				break;
			case "admin":
				$delaid =  $objRequest -> delaid;
				if($delaid > 0 && $delaid != 1 && $delaid != $aid) {
					AdminDao::deleteAdmin($delaid);
					AdminDao::deleteAdminPower($delaid);
				}
				$pn = $objRequest -> pn;
				$adminid = $objRequest -> aid;
				$keyword = $objRequest -> keyword;
				$count = 10;
				$adminNum = AdminDao::getNumAdminAll($adminid, $keyword);
				$allPage = ceil($adminNum/$count);
				$pn = $pn > $allPage ? $allPage : $pn;
				$pn = $pn < 1 || empty($pn) ? 1 : $pn;//getCheck( = NULL, $b = 1, $c = 10)
				$arrAdmin = AdminDao::getAdminAll($adminid, $keyword, $pn, $count);
				
				$arrAdminSetInfo = AdminDao::getAdminSetInfo($adminid);
				$objResponse -> setTplValue("admin", $arrAdmin);
				$objResponse -> setTplValue("aid", $adminid);
				$objResponse -> setTplValue("loginaid", $aid);
				$objResponse -> setTplValue("adminsetinfo", $arrAdminSetInfo);
				$objResponse -> setTplValue("pn", $pn);
				$objResponse -> setTplValue("allpage", $allPage);
				$objResponse -> setTplValue("page", Utilities::getPageArray($allPage, $pn));
				break;
			case "review":
				$delrid = $objRequest -> delrid;
				if($delrid > 0) {
					AdminDao::deleteProductReview($delrid);
				}
				$count = 12;
				$reviewNum = AdminDao::getNumProductReview();

				$pn = $objRequest -> pn;
				$allPage = ceil($reviewNum/$count);
				$pn = $pn > $allPage ? $allPage : $pn;
				$pn = $pn < 1 || empty($pn) ? 1 : $pn;//
				
				$rs = AdminDao::getProductReview(NULL,  $pn, $count);
				$objResponse -> setTplValue("pn", $pn);
				$objResponse -> setTplValue("allpage", $allPage);
				$objResponse -> setTplValue("page", Utilities::getPageArray($allPage, $pn));
				break;
			default:
				break;
		}
		if(isset($rs)) {
			$objResponse -> setTplValue("value", $rs);
		}
		$objResponse -> setTplValue("lid", $lid);
		$objResponse -> setTplValue("mid", $mid);
		$objResponse -> setTplValue("fun_name", $admintpl['name']);
		$objResponse -> setTplValue("__Meta", Common::getMeta());
		//set compiler false
		$this -> setCompiler();
		//set tpl
		$objResponse -> setTplName("../admin/#".$fun);
	}
	
	public function getClasssSet() {
		$rs = ClassesDao::getClasssSetValue();
		$arrUpCid = array();
		$num = count($rs);
		for($i = 0; $i < $num; $i++) {
			if(!isset($arrUpCid[$rs[$i]['cpid']])) $arrUpCid[$rs[$i]['cpid']]['min'] = $rs[$i]['orderid'];
			$arrUpCid[$rs[$i]['cpid']]['max'] = $rs[$i]['orderid'];
		}
		for($i = 0; $i < $num; $i++) {
			$f = '';
			$font = '';
			$dot = !empty($rs[$i]['spliter']) ? $rs[$i]['spliter'] : " &#8212; ";
			for($j = 0; $j < $rs[$i]['depth']; $j++) {
				$f .= "$dot";//"&#8212;"
			}
			$rs[$i]['_name'] = $f.$rs[$i]['name'];
			$rs[$i]['down'] = '0';
			$rs[$i]['up'] = '0';
			if($rs[$i]['cpid'] > '0') {
				if($rs[$i]['orderid'] < $arrUpCid[$rs[$i]['cpid']]['max']) $rs[$i]['down'] = '1';
				if($rs[$i]['orderid'] > $arrUpCid[$rs[$i]['cpid']]['min']) $rs[$i]['up'] = '1';
			}
		}
		
		return $rs;
	}
}
?>