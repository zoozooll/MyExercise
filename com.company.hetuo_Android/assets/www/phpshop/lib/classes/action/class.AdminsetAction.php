<?php


/**
 * class.AdminsetAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 */

class AdminsetAction extends BaseAction {
	
	protected function check($objRequest, $objResponse) {
		AdminAction::checkLoginAdmin();
	}

	protected function service($objRequest, $objResponse) {
		switch($objRequest->getSwitch()) {
			default:
				$this -> doAdminset($objRequest, $objResponse);
				break;
		}
	}

	/**
	 * 显示 
	 */
	protected function doAdminset($objRequest, $objResponse) {
		$objRequest -> stripTagsParameters("/<script(.+)script>/i");
		$lid = $objRequest -> lid;
		$mid = $objRequest -> mid;
		$cid = $objRequest -> cid;
		$pid = $objRequest -> pid;
		$nid = $objRequest -> nid;
		$actid = '';
		$bid = '';

		$succid = '';
		$arrParameter = array();
		
		$objSession = new Session;
		$aid = $objSession -> glo_id;
		
		if(!($admintpl = AdminDao::getFunc($lid, $aid))) {
			alert(conutf8('没有权限！'));
			redirect("admin/login.php"); 
		}
		$fun = $admintpl["fun"];
		$success = false;
		if(!empty($pid)) {
			$fun = 'addgoods';
		}
		if(!empty($nid)) {
			$fun = 'addnews';
		}
		switch($fun) {
			case "searchoptim":
				$arrValue = $this -> getBaseseVal($objRequest);
				if(AdminDao::updateBasesetValue($fun, $arrValue)) {
					UpdateCommonDao::updateSiteInfo("searchoptim");
					$success = true;
				}
				break;
				
			case "baseset":
				$arrValue = $this -> getBaseseVal($objRequest);
				if(AdminDao::updateBasesetValue($fun, $arrValue)) {
					UpdateCommonDao::updateSiteInfo();
					$success = true;
				}
				break;
				
			case "regAndCon":
				$arrValue = $this -> getBaseseVal($objRequest);
				if(AdminDao::updateBasesetValue($fun, $arrValue)) {
					UpdateCommonDao::updateSiteInfo("regAndCon");
					$success = true;
				}
				break;
			case "service":
				$arrValue = $this -> getBaseseVal($objRequest);
				if(AdminDao::updateBasesetValue($fun, $arrValue)) {
					UpdateCommonDao::updateSiteInfo("service");
					$success = true;
				}
				break;
			case "interface":
				if(($objRequest -> sendemail) == NULL) {
					$arrValue = $this -> getBaseseVal($objRequest);
					if(AdminDao::updateBasesetValue($fun, $arrValue)) {
						UpdateCommonDao::updateSiteInfo("interface");
						$success = true;
					}
				} else {
					$toemail = $objRequest -> toemail;
					$fromemail = $objRequest -> fromemail;
					if(Common::sendMail($toemail, $fromemail, conutf8('测试发送Email'), conutf8('来自'.$fromemail.'的测试信件'), 'utf-8')) {
						$success = true;
					} else {
						alert(conutf8('发送失败，请检查设置！'));
					}
				}
				break;	
			
			case "watermark":
				$arrValue = $this -> getBaseseVal($objRequest);
				if(AdminDao::updateBasesetValue($fun, $arrValue)) {
					UpdateCommonDao::updateSiteInfo("watermark");
					$success = true;
				}
				break;	
				
			case "setclasses":
				$rValue["classesinfo"] = trim($objRequest -> classesinfo);
				$ismv = $objRequest -> ismv;
				$updatecid = false;
				$updateccid = false;
				if($ismv == NULL) {
					if($cid == NULL) {
						if($rValue["classesinfo"] == '0') {
							$rValue["orderid"] = 0;
							$rValue["depth"] = 0;
							$rValue["cid"] = 1;
							$rValue["cpid"] = 0;
							$rValue["sortid"] = AdminDao::getMaxSortId() + 1;
							$updatecid = true;
						} elseif($rValue["classesinfo"] >0) {
							$arrGetid = AdminDao::getMAXOrderId($rValue["classesinfo"]);
							$getorderid = $arrGetid["orderid"];
							$rValue["orderid"] = $arrGetid["orderid"] + 1;
							$rValue["depth"] = AdminDao::getDepth($rValue["classesinfo"]) + 1;
							$rValue["cid"] = $arrGetid["cid"];
							$rValue["cpid"] = $rValue["classesinfo"];
							$rValue["sortid"] = $arrGetid["sortid"];
							$updateccid = true;
						}
					}
					$rValue["name"] = trim($objRequest -> name);
					$rValue["enname"] = trim($objRequest -> enname);
					if($rValue["name"] == '') {
						break;
					}
					
					//if(!preg_match('/^[0-9a-z]{1,100}$/i', $rValue["enname"])) {
						//break;
					//}
					$rValue["des"] = trim($objRequest -> des);
					$rValue["hidden"] = trim($objRequest -> hidden);
					$rValue["espegroup"] = trim($objRequest -> ucid) != NULL? trim($objRequest -> ucid) : 3;
					$rValue["espeuser"] = trim($objRequest -> espeuser);
					$rValue["url"] = trim($objRequest -> url);
					$rValue["picurl"] = trim($objRequest -> picurl);
					$rValue["spliter"] = trim($objRequest -> spliter);
	
					$rValue["affiche"] = trim($objRequest -> affiche);
				
					if($updateccid == true) {
						AdminDao::updateCcidnum($rValue["classesinfo"]);
						AdminDao::updateClassesOrderid($getorderid, $arrGetid["cid"]);
					}
					if(AdminDao::updateClassesValue($rValue, $cid)) {
						if($updatecid == true) {
							AdminDao::updateClassesCid();
						}
						$success = true;
					}
				} else {
					if($rValue["classesinfo"] == 'mv') alert(conutf8('没有进行类别更改！'));
					$arrParameter['mv'] = $ismv;
					$arrSelfinfo = Common::getClassInfo($ismv);
					$arrSortIdSelfInfo = Common::getClassSmallInfo($ismv);
					$snum = count($arrSortIdSelfInfo);
					if($rValue["classesinfo"] > 0) {
						if($rValue["classesinfo"] == $arrSelfinfo['cpid']) alert(conutf8('没有进行类别更改！'));
						
						$arrSortIdMvtoInfo = Common::getClassSmallInfo($rValue["classesinfo"]);
						$arrMvtoinfo = Common::getClassInfo($rValue["classesinfo"]);
						$mnum = count($arrSortIdMvtoInfo);
												
						//
						$ucid = $arrMvtoinfo['cid'];//更新的 cid
						$depth = $arrMvtoinfo['depth'];//将要移动到的父深度 
						$sDepth = $arrSelfinfo['depth'];//本来的深度
						//$dif_depth = $arrMvtoinfo['depth'] - $arrSelfinfo['depth'];
						$maxOrderid = $arrMvtoinfo['orderid'];//新的排序
						$cpid = $rValue["classesinfo"];//新的父id
						//$mvtoCcidnum = $arrMvtoinfo['ccidnum'];//子类别个数

						$arrUpdate['cid'] = $ucid;
						$maxOrderid++;
						$arrUpdate['orderid'] = $maxOrderid;
						$arrUpdate['depth'] = $depth + 1;
						$arrUpdate['cpid'] = $cpid;
						AdminDao::updateMoveClasses($arrUpdate, $ismv);//更新新类别的orderid 等
						unset($arrUpdate);
						//移到到新父类 生成新的 orderid depth
						$arrIsDoOrderId = array();
						if(!empty($arrSortIdSelfInfo)) {
							$arrUpdate['cid'] = $ucid;
							for($i = 0; $i<$snum; $i++) {
								$maxOrderid++;
								$arrUpdate['orderid'] = $maxOrderid;
								$arrUpdate['depth'] =  $depth + 1 + $arrSortIdSelfInfo[$i]['depth'] - $sDepth; 
								AdminDao::updateMoveClasses($arrUpdate, $arrSortIdSelfInfo[$i]['id']);//self smallclass down
								$arrIsDoOrderId[$arrSortIdSelfInfo[$i]['id']] = 1;
							}
						} 
						unset($arrUpdate);
						//更新新父类的 ccidnum
						$snum = empty($snum) ? 1 : $snum;
						$arrUpdate['ccidnum'] = $arrMvtoinfo['ccidnum'] + $snum;
						AdminDao::updateMoveClasses($arrUpdate, $arrMvtoinfo['id']);
						if(!empty($arrSelfinfo['cpid'])) {
							AdminDao::updateMoveClassesSelfCcid($arrSelfinfo['cpid']);
						}
						//更新一级排序类别
						AdminDao::updateMoveSortClasses($arrMvtoinfo['sortid'], $arrMvtoinfo['cid']);
						unset($arrUpdate);
						//取得新类别整个类别cid数据
						$arrSortIdMvtoInfo = Common::getClassDown($arrMvtoinfo['cid']);
						$num = count($arrSortIdMvtoInfo);
						//更新插进去的类别后面的类别的orderid
						if(!empty($arrSortIdMvtoInfo)) {
							for($i = 0; $i<$num; $i++) {
								if($arrSortIdMvtoInfo[$i]['id'] == $arrSelfinfo['id'] || isset($arrIsDoOrderId[$arrSortIdMvtoInfo[$i]['id']])) continue;
								if($arrSortIdMvtoInfo[$i]['orderid'] > $arrMvtoinfo['orderid']) {
									$maxOrderid++;
									$arrUpdate['orderid'] = $maxOrderid;
									AdminDao::updateMoveClasses($arrUpdate, $arrSortIdMvtoInfo[$i]['id']);//self smallclass down
								}
							}
						}
						$success = true;
					} else {
						//移到顶级	
						$arrUpdate['cid'] = $arrSelfinfo['id'];
						$arrUpdate['orderid'] = 0;
						$arrUpdate['depth'] = 0;
						$arrUpdate['cpid'] = 0;
						AdminDao::updateMoveClasses($arrUpdate, $ismv);//更新新类别的orderid 等
						unset($arrUpdate);
						if(!empty($arrSelfinfo['cpid'])) {
							AdminDao::updateMoveClassesSelfCcid($arrSelfinfo['cpid']);
						}
						//改变子类depth
						if(!empty($arrSortIdSelfInfo)) {
							$arrUpdate['cid'] = $arrSelfinfo['id'];
							for($i = 0; $i<$snum; $i++) {
								$arrUpdate['orderid'] = $i + 1;
								$arrUpdate['depth'] =  $arrSortIdSelfInfo[$i]['depth'] - $arrSelfinfo['depth']; 
								AdminDao::updateMoveClasses($arrUpdate, $arrSortIdSelfInfo[$i]['id']);//self smallclass down
							}
						} 
						unset($arrUpdate);
						$success = true;
					}
				}
				if($success == true) {
					UpdateCommonDao::updateClassesInfo();
				}
				break;	
				
			case 'attribute':
				if(empty($cid)) {
					break;
				}
				$arrValue = $objRequest -> value;
				$arrIsFilter = $objRequest -> isfilter;
				$arrAttid = $objRequest -> attid;
				$arrMainatr = $objRequest -> mainatr;
				if($arrMainatr[0] != 1) break;
				$success = AdminDao::updateAttribute($cid, $arrValue, $arrIsFilter, $arrAttid, $arrMainatr);
				break;
			
			case 'brand':
				if(($objRequest -> add) == 'yes') {
					if(!empty($objRequest -> name) && !empty($objRequest -> enname) && !is_array($objRequest -> name)) {
						AdminDao::insertBrand($objRequest -> name, $objRequest -> enname);
						$success = true;
					}
				} else {
					if(!empty($objRequest -> modify) && is_array($objRequest -> id)) {
						$arrName = $objRequest -> name;
						$arrEnName = $objRequest -> enname;
						
						$arrId = $objRequest -> id;
						$arrOid = $objRequest -> oid;
						$num = count($arrId);
						$numOid = count($arrOid);
						
						for($i = 0; $i < $num; $i++) {
							for($j = 0; $j <$numOid; $j++) {
								if($arrId[$i] == $arrOid[$j]) {
									AdminDao::updateBrand($arrId[$i], $arrName[$j], $arrEnName[$j]);
								}
							}
						}
						$success = true;
					}
				}
				break;
			case 'freight':
				if(($objRequest -> add) == 'yes') {
					if(!empty($objRequest -> name) && !is_array($objRequest -> name)) {
						AdminDao::insertFreight($objRequest -> name, $objRequest -> freight, $objRequest -> shop_price, $objRequest -> describes);
						$success = true;
					}
				} else {
					if(!empty($objRequest -> modify) && is_array($objRequest -> id)) {
						$arrName = $objRequest -> name;
						$arrFreight = $objRequest -> freight;
						$arrShop_price = $objRequest -> shop_price;
						$arrDescribes = $objRequest -> describes;
						
						$arrId = $objRequest -> id;
						$arrOid = $objRequest -> oid;
						$num = count($arrId);
						$numOid = count($arrOid);
						
						for($i = 0; $i < $num; $i++) {
							for($j = 0; $j <$numOid; $j++) {
								if($arrId[$i] == $arrOid[$j]) {
									AdminDao::updateFreight($arrId[$i], $arrName[$j], $arrFreight[$j], $arrShop_price[$j], $arrDescribes[$j]);
								}
							}
						}
						$success = true;
					}
				}
				break;
			case 'bank':
				$arrParameter['type'] = $objRequest -> tpye;
				if($arrParameter['type'] == 'W') {
					//alipay
					$usealipay = (($objRequest -> usealipay) == 1) ? 1 : 0;
					$partner = $objRequest -> partner;
					$security_code = $objRequest -> security_code;
					$seller_email = $objRequest -> seller_email;
					//tenpay
					$usetenpay = (($objRequest -> usetenpay) == 1) ? 1 : 0;
					$strSpid = $objRequest -> strSpid;
					$strSpkey = $objRequest -> strSpkey;
					$web_url = Common::getSiteInfo('value3');
					
					include_once('pay/alipay/alipay_config.sample.php');
					include_once('pay/tenpay/tenpay_config.sample.php');
					//UpdateCommonDao::updatePayInfo('alipay_config', $alipay, 'pay/alipay/');
					//UpdateCommonDao::updatePayInfo('tenpay_config', $tenpay, 'pay/tenpay/');
					if(($objRequest -> isusepay) == 1) {
						$strpay = '$isusepay = 1;$usealipay = "'.$usealipay.'";$usetenpay = "'
							     .$usetenpay.'";'."\r\n".$alipay.";\r\n".$tenpay;
						UpdateCommonDao::updatePayInfo('payinfo', $strpay);
					} else {
						$strpay = '$isusepay = 0;$usealipay = "'.$usealipay.'";$usetenpay = "'
						         .$usetenpay.'";'."\r\n".$alipay.";\r\n".$tenpay;
						UpdateCommonDao::updatePayInfo('payinfo', $strpay);
					}
					$success = true;
					break;
				}
				
				if(($objRequest -> add) == 'yes' && $arrParameter['type'] != 'W') {
					if(!empty($objRequest -> name) && !is_array($objRequest -> name)) {
						AdminDao::insertBank($objRequest -> name, $objRequest -> payee, $objRequest -> accounts, $objRequest -> describes, $arrParameter['type']);
						$success = true;
					}
				} else {
					if(!empty($objRequest -> modify) && is_array($objRequest -> id) && $arrParameter['type'] != 'W') {
						$arrName = $objRequest -> name;
						$arrPayee = $objRequest -> payee;
						$arrAccounts = $objRequest -> accounts;
						$arrDescribes = $objRequest -> describes;
						
						$arrId = $objRequest -> id;
						$arrOid = $objRequest -> oid;
						$num = count($arrId);
						$numOid = count($arrOid);
						
						for($i = 0; $i < $num; $i++) {
							for($j = 0; $j <$numOid; $j++) {
								if($arrId[$i] == $arrOid[$j]) {
									AdminDao::updateBank($arrId[$i], $arrName[$j], $arrPayee[$j], $arrAccounts[$j], $arrDescribes[$j]);
								}
							}
						}
						$success = true;
					}
				}
				break;
			case 'supply':
				if(($objRequest -> add) == 'yes') {
					if(!empty($objRequest -> name) && !is_array($objRequest -> name)) {
						AdminDao::insertSupply($objRequest -> name, $objRequest -> phone, $objRequest -> mobile, $objRequest -> email, $objRequest -> address, $objRequest -> describes);
						$success = true;
					}
				} else {
					if(!empty($objRequest -> modify) && is_array($objRequest -> id)) {
						$arrName = $objRequest -> name;
						$arrPhone = $objRequest -> phone;
						$arrMobile = $objRequest -> mobile;
						$arrEmail = $objRequest -> email;
						$arrAddress = $objRequest -> address;
						$arrDescribes = $objRequest -> describes;
						
						$arrId = $objRequest -> id;
						$arrOid = $objRequest -> oid;
						$num = count($arrId);
						$numOid = count($arrOid);
						
						for($i = 0; $i < $num; $i++) {
							for($j = 0; $j <$numOid; $j++) {
								if($arrId[$i] == $arrOid[$j]) {
									AdminDao::updateSupply($arrId[$i], $arrName[$j], $arrPhone[$j], $arrMobile[$j], $arrEmail[$j], $arrAddress[$j], $arrDescribes[$j]);
								}
							}
						}
						$success = true;
					}
				}
				break;

			case 'addgoods':
				if(empty($cid)) {
					break;
				}
				$d_cid = $objRequest -> d_cid;
				$d_pid = $objRequest -> d_pid;
				if(!empty($d_cid) || !empty($d_pid)) {
					AdminDao::deleteProduct($d_cid, $d_pid);
					File::deleteFile($d_pid.".php", __PRODUCT_XML);
					$success = true;
					break;
				}
				$arrProduct = self::getProductParam($objRequest);

				if($arrProduct != false) {
					$arrAtr[0] = $objRequest -> atrid;
					if(is_array($arrAtr[0]) && !empty($arrAtr[0])) {
						foreach($arrAtr[0] as $k => $v) {
							$arrAtr[0][$k] = trim($v);
						}
					}
					$arrAtr[1] = $objRequest -> atrvalue;
					if(is_array($arrAtr[1]) && !empty($arrAtr[1])) {
						foreach($arrAtr[1] as $k => $v) {
							$arrAtr[1][$k] = trim($v);
						}
					}

					if($pid > 0 && is_numeric($pid)) {
						$prod = $pid;
						AdminDao::updateProduct($arrProduct, $cid, $pid);
						$success = true;
					} else {
						if(!empty($arrProduct['number'])) {
							if(AdminDao::checkInsertProduct($arrProduct['number']) > 0) { 
								alert(conutf8('已经有相同的产品编号，请重新填写！'));
								break;
							}
						}
						AdminDao::insertProduct($cid, $arrProduct);
						$prod = AdminDao::getInsertProductId();
						$succid = $prod;
						$success = true;
					}
					$actid = $prod;
					AdminDao::insertProductAttribute($cid, $pid, Utilities::formArrayTurnStr($arrAtr,"$cid','$prod"));
					
					$arrAtrValue = ClassesDao::getAttribute($cid);
					$num = count($arrAtrValue);
					$j = 0;
					$arrProdSpec = array();
					$sqlfilter = '';
					for($i = 0; $i<$num; $i++) {
						if(empty($arrAtr[0])) break;
						foreach($arrAtr[0] as $k => $v) {
							if($v == $arrAtrValue[$i]['id']) {
								if($arrAtrValue[$i]['mainatr'] == '1') {
									$arrProdSpec[$k]['title_name'] = $arrAtrValue[$i]['name'];
									$arrProdSpec[$k]['name'] = "";
									$arrProdSpec[$k]['value'] = "";
									$k++;
								} else {
									$arrProdSpec[$k]['title_name'] = "";
									$arrProdSpec[$k]['name'] = $arrAtrValue[$i]['name'];
									$arrProdSpec[$k]['value'] = $arrAtr[1][$k];
									if($arrAtrValue[$i]['isfilter'] == '1' && $j < 8) {
										//$sqlfilter .= "'".$arrAtrValue[$i]['id']."','".$arrAtr[1][$k]."',";
										$sqlfilter .= 'atr'.($j+1)."='".$arrAtrValue[$i]['id']."',value".($j+1)."='".$arrAtr[1][$k]."',";
										$j += 1;
									}
								}
							}
						}
					}

					//xml2php file
					$contant = "<?php\r\n".'$rs'." = ".Utilities::arrayTurnStr($arrProdSpec).";\r\n?>";
					File::creatFile($prod.".php", $contant, __PRODUCT_XML);
					//product filter
					if($sqlfilter != '') {
						for($i = $j; $i<8; $i++) {
							$sqlfilter .= 'atr'.($j+1)."='0',value".($j+1)."='',";
						}
						$sqlfilter = trim($sqlfilter, ',');
						AdminDao::updateProductFilter($cid, $pid, $sqlfilter);
					}
				}
				break;
			
			case 'series':
				$bid = $objRequest -> bid;
				if(($objRequest -> add) == 'yes' && !empty($bid)) {
					if(!empty($objRequest -> name) && !empty($objRequest -> enname) && !is_array($objRequest -> name)) {
						AdminDao::insertSeries($bid, $objRequest -> name, $objRequest -> enname);
						$success = true;
					}
				} else {
					if(!empty($objRequest -> modify) && is_array($objRequest -> id)) {
						$arrName = $objRequest -> name;
						$arrEnName = $objRequest -> enname;
						
						$arrId = $objRequest -> id;
						$arrOid = $objRequest -> oid;
						$num = count($arrId);
						$numOid = count($arrOid);
						
						for($i = 0; $i < $num; $i++) {
							for($j = 0; $j <$numOid; $j++) {
								if($arrId[$i] == $arrOid[$j]) {
									AdminDao::updateSeries($arrId[$i], $arrName[$j], $arrEnName[$j]);
								}
							}
						}
						$success = true;
					}
				}
				break;
			
			case 'addnews':
				$arrNews['title'] = $objRequest -> news_title;
				$arrNews['author'] = $objRequest -> news_author;
				$arrNews['source_from'] = $objRequest -> news_source;
				$arrNews['content'] = $objRequest -> news_content;
				if(empty($arrNews['title']) || empty($arrNews['content'])) {
					break;
				}
				if(!empty($nid)) {
					AdminDao::updateNews($cid, $nid, $arrNews);
					$success = true;
				} 
				if(empty($nid) && !empty($arrNews['title']) && !empty($arrNews['content'])) {
					AdminDao::insertNews($cid, $arrNews);
					$nid = AdminDao::getInsertNewsId();
					$success = true;
				}
				$actid = $nid;
				break;
			case 'ad':
				if($cid > 0) {
					$arrValue['value1'] = $objRequest -> value1;
					$arrValue['value2'] = $objRequest -> value2;
					$arrValue['value3'] = $objRequest -> value3;
					$arrValue['value4'] = $objRequest -> value4;
					$arrValue['value5'] = $objRequest -> value5;
					AdminDao::updateadvertisingValue($arrValue, $cid);
					UpdateCommonDao::updateAdvertising($cid);
					$success = true;
				}
				break;
			case 'password':
				$oldpass = $objRequest -> oldpass;
				$password = $objRequest -> password;
				$pass = $objRequest -> pass;
				if(!empty($oldpass) && !empty($password)) {
					if($password != $pass) {
						alert(conutf8('两次填入的密码不相等！'));
						break;
					}
					if($oldpass != ($objSession -> glo_adminpassword)) {
						alert(conutf8('旧密码填入不正确！'));
						break;
					}
					AdminDao::updateAdmin($objSession -> glo_id, $objSession -> glo_adminname, $password);
					$objSession -> glo_adminpassword = $password;
					$success = true;
				}
				if(($objRequest -> changeemail) == 1) {
					$email = $objRequest -> email;
					if(!empty($email)) {
						AdminDao::updateAdmin($objSession -> glo_id, $objSession -> glo_adminname, NULL, $email);
						$success = true;
					}
				}
				break;
			case "commend":
				$arrValue = $this -> getBaseseVal($objRequest);
				if(AdminDao::updateBasesetValue($fun, $arrValue)) {
					UpdateCommonDao::updateSiteInfo("commend");
					$success = true;
				}
				break;
				
			case "admin":
				$aid = $objRequest -> aid;
				if(($objRequest -> addadmin) == 1) {
					$name = $objRequest -> name;
					$email = $objRequest -> email;
					$password = $objRequest -> password;
					$pass = $objRequest -> pass;
					$arrPower = $objRequest -> power;
					if($password != $pass) {
						alert(conutf8('两次填入的密码不相等！'));
						break;
					}
					if(!empty($name) && !empty($email)) {
						if(empty($aid)) {
							if(!empty($password)) {
								$adminRs = AdminDao::getAdmin($name, $password);
								if(!empty($adminRs)) {
									alert(conutf8('已有相同管理员名称！'));
									break;
								}
								AdminDao::addAdmin($name, $email, $password);
								$aid = AdminDao::getInsertProductId();
							}
						} else {
							if(!empty($password)) {
								AdminDao::updateAdmin($aid, $name, $password);
							}
							AdminDao::deleteAdminPower($aid);
						}
						$num = count($arrPower);
						$strsql = '('.$aid.',' . 39 .'),';
						for($i = 0; $i<$num; $i++) {
							$strsql .= '('.$aid.',' . $arrPower[$i] .'),';
						}
						$strsql = trim($strsql, ',');
						AdminDao::addAdminPower($strsql);
						$success = true;
					}
				}
				break;
				
			default:
				break;
		}
		if(!empty($cid)) {
			$cid = "&cid=".$cid;
		}
		if(!empty($succid)) {
			$succid = "&succid=".$succid;
		}
		$str = '';
		if(!empty($arrParameter)) {
			foreach($arrParameter as $k => $v) {
				$str .= '&'.$k.'='.$v;
			}
		}
		$success = $success ? "操作成功！" : "操作失败，请检查！";
		alert(conutf8($success), 0);
		//alert($success, 0);
		redirect("admincp.php?mid=$mid&lid=$lid".$cid."&pid=".$pid."&bid=".$bid."&actid=".$actid.$succid.$str); 
	}
	
	protected function getBaseseVal($objRequest) {
		$rValue["value1"] = trim($objRequest -> value1);
		$rValue["value2"] = trim($objRequest -> value2);
		$rValue["value3"] = trim($objRequest -> value3);
		$rValue["value4"] = trim($objRequest -> value4);
		$rValue["value5"] = trim($objRequest -> value5);
		$rValue["value6"] = trim($objRequest -> value6);
		$rValue["value7"] = trim($objRequest -> value7);
		$rValue["value8"] = trim($objRequest -> value8);
		$rValue["value9"] = trim($objRequest -> value9);
		$rValue["value10"] = trim($objRequest -> value10);
		$rValue["value11"] = trim($objRequest -> value11);
		$rValue["value12"] = trim($objRequest -> value12);
		$rValue["value13"] = trim($objRequest -> value13);
		$rValue["value14"] = trim($objRequest -> value14);
		$rValue["value15"] = trim($objRequest -> value15);
		$rValue["value16"] = trim($objRequest -> value16);
		$rValue["value17"] = trim($objRequest -> value17);
		$rValue["value18"] = trim($objRequest -> value18);
		$rValue["value19"] = trim($objRequest -> value19);
		$rValue["value20"] = trim($objRequest -> value20);
		$rValue["value21"] = trim($objRequest -> value21);
		$rValue["value22"] = trim($objRequest -> value22);
		$rValue["value23"] = trim($objRequest -> value23);
		$rValue["value24"] = trim($objRequest -> value24);
		$rValue["value25"] = trim($objRequest -> value25);
		$rValue["value26"] = trim($objRequest -> value26);
		$rValue["value27"] = trim($objRequest -> value27);
		$rValue["value28"] = trim($objRequest -> value28);
		$rValue["value29"] = trim($objRequest -> value29);
		$rValue["value30"] = trim($objRequest -> value30);
		return $rValue;
	}

	protected function getProductParam($objRequest) {
		if(empty($objRequest -> product_name)) {
			alert(conutf8("没有产品名称，请重新填写！"));
			return false;
		}
		if(empty($objRequest -> product_price)) {
			alert(conutf8("没有售价！，请重新填写！"));
			return false;
		}
		$arrProduct["psid"] = trim($objRequest -> psid);
		$arrProduct["psid"] = empty($arrProduct["psid"]) ? 'NULL' : $arrProduct["psid"];
		$arrProduct['name'] = trim($objRequest -> product_name);
		$arrProduct["enname"] = trim($objRequest -> product_enname);
		$arrProduct['price'] = trim($objRequest -> product_price);
		$arrProduct['price_mill'] = trim($objRequest -> product_mill_price);
		$arrProduct['price_member'] = trim($objRequest -> product_member_price);
		$arrProduct['price_special'] = trim($objRequest -> product_special_price);
		$arrProduct['price_market'] = trim($objRequest -> price_market);
		$arrProduct['number'] = trim($objRequest -> product_no);
		$arrProduct['factory_number'] = trim($objRequest -> product_factory_number);
		$arrProduct['store'] = $objRequest -> product_store < 1 ? 0 : $objRequest -> product_store;
		$arrProduct['store'] = trim($arrProduct['store']);
		$arrProduct['online'] = empty($objRequest -> online) ? '0' : '1';
		$arrProduct['special_offer'] = empty($objRequest -> special_offer) ? '0' : '1';
		$arrProduct['market_offer'] = empty($objRequest -> market_offer) ? '0' : '1';
		$arrProduct['member_offer'] = empty($objRequest -> member_offer) ? '0' : '1';
		$arrProduct['series'] = trim($objRequest -> product_series);
		$arrProduct["series"] = empty($arrProduct["series"]) ? 'NULL' : $arrProduct["series"];
		$arrProduct['brand'] = trim($objRequest -> product_brand);
		$arrProduct["brand"] = empty($arrProduct["brand"]) ? 'NULL' : $arrProduct["brand"];
		$arrProduct['model'] = trim($objRequest -> product_model);
		$arrProduct['product_related'] = trim($objRequest -> product_related);
		$arrProduct['describe'] = trim($objRequest -> product_des);
		$arrProduct['s_pic'] = trim($objRequest -> product_s);
		$arrProduct['m_pic'] = trim($objRequest -> product_m);
		$arrProduct['b_pic'] = trim($objRequest -> product_b);
		$nopic = trim($objRequest -> nopic);
		if($nopic == 1) {
			$arrProduct['s_pic'] = $arrProduct['number']."_s";
			$arrProduct['m_pic'] = $arrProduct['number']."_m";
			$arrProduct['b_pic'] = $arrProduct['number']."_b";
		}
		return $arrProduct;
	}
}
?>