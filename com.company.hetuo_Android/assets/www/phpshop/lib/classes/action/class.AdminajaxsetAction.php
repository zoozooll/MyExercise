<?php


/**
 * class.AdminajaxsetAction.php
 *-------------------------
 *
 *
 * PHP versions 5
 *
 * @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 */

class AdminajaxsetAction extends BaseAction {
	protected $arrAdmin_fun = NULL;
	
	protected function check($objRequest, $objResponse) {
		AdminAction::checkLoginAdmin();
		$lid = $objRequest -> lid;
		$objSession = new Session;
		$aid = $objSession -> glo_id;
		
		if(!($admintpl = AdminDao::getFunc($lid, $aid))) {
			alert(conutf8('没有权限！'));
			redirect("login.php"); 
		}
		$this -> arrAdmin_fun = $admintpl;
		//set compiler false
		$this -> setCompiler();
	}

	protected function service($objRequest, $objResponse) {
		switch($objRequest->getSwitch()) {
			case "sortclass":
				$this -> doAdminSortClass($objRequest, $objResponse);
				break;
			case "iframecheck":
				$this -> doAdminIframeCheck($objRequest, $objResponse);
				break;	
			case 'uploadpic':
				$this -> doUploadPic($objRequest, $objResponse);
				break;
			case 'uploadlogo':
				$this -> doUploadLogo($objRequest, $objResponse);
				break;
			case 'uploadwaterimg':
				$this -> doUploadWaterimg($objRequest, $objResponse);
				break;	
			case 'uploadnewspic':
				$this -> doUploadNewspic($objRequest, $objResponse);
				break;
			case 'fun':
				$fun = 'do' . $this -> arrAdmin_fun['fun'];
				$this -> $fun($objRequest, $objResponse);
				break;		
			default:
				$this -> doDefaultShow($objRequest, $objResponse);
				break;
		}
	}

	/**
	 * 显示 
	 */
	protected function doDefaultShow($objRequest, $objResponse) {
		//set tpl
		$objResponse -> setTplName("../admin/#no");
	}
	
	protected function doAdminSortClass($objRequest, $objResponse) {
		$lid = $objRequest -> lid;
		$mid = $objRequest -> mid;
		$arrSortId = explode(',', $objRequest -> sortid);
		$arrSortclass = explode(',', $objRequest -> sortclass); 
		$scid = $objRequest -> scid;
		$sortval = $objRequest -> sortval;
		
		$arrClasses = AdmincpAction::getClasssSet();
		$num = count($arrClasses);
		if(empty($scid)) {
			$arrUpCpid = array();
			$j = 0;
			for($i = 0; $i<$num; $i++) {
				if($arrClasses[$i]['cpid'] == '0') {
					$arrUpCpid[$j]['id'] = $arrClasses[$i]['id'];
					$arrUpCpid[$j]['sortid'] = $arrSortclass[$j];
					$j++;
				}
			}
			if(!empty($arrUpCpid)) {
				foreach($arrUpCpid as $k => $v) {
					AdminDao::updateClassesSort($v['id'], $v['sortid']);
				}
			}
		} else {
			$sortval = $objRequest -> sortval;
			$secid = '';
			$arrSelfinfo = Common::getClassInfo($scid);
			$arrSelfClass = Common::getClassSmallInfo($arrSelfinfo['cpid']);
			$selfClassnum = count($arrSelfClass);
			$arrUpInfo = array();
			$arrDownInfo = array();
			$down = 1;
			for($i = 0; $i<$selfClassnum; $i++) {
				if($arrSelfinfo['cpid'] == $arrSelfClass[$i]['cpid'] && $arrSelfinfo['depth'] == $arrSelfClass[$i]['depth']) {
					if($arrSelfinfo['orderid'] > $arrSelfClass[$i]['orderid']) {
						$arrUpInfo = $arrSelfClass[$i];
					}
					if($arrSelfinfo['orderid'] < $arrSelfClass[$i]['orderid'] && $down == 1) {
						$arrDownInfo = $arrSelfClass[$i];
						$down++;
					}
				}
			}

			$arrSortIdDInfo = array();
			$arrSortIdUInfo = array();
			if($sortval == 'up') {
				$arrSortIdUInfo = Common::getClassSmallInfo($arrUpInfo['id']);
				$unum = count($arrSortIdUInfo);
				$arrSortIdDInfo = Common::getClassSmallInfo($arrSelfinfo['id']);
				$dnum = count($arrSortIdDInfo);
				//
				AdminDao::updateClassesOrder($arrSelfinfo['id'], ($arrSelfinfo['orderid'] - 1 - $unum));//self up
				for($i = 0; $i<$dnum; $i++) {
					AdminDao::updateClassesOrder($arrSortIdDInfo[$i]['id'], ($arrSortIdDInfo[$i]['orderid'] - 1 - $unum));//self smallclass up
				}
				//
				AdminDao::updateClassesOrder($arrUpInfo['id'], $arrUpInfo['orderid'] + 1 + $dnum);//down
				for($i = 0; $i<$unum; $i++) {
					AdminDao::updateClassesOrder($arrSortIdUInfo[$i]['id'], ($arrSortIdUInfo[$i]['orderid'] + 1 + $dnum));//up smallclass down
				}
			}
			
			if($sortval == 'down') {
				$arrSortIdUInfo = Common::getClassSmallInfo($arrSelfinfo['id']);
				$unum = count($arrSortIdUInfo);
				$arrSortIdDInfo = Common::getClassSmallInfo($arrDownInfo['id']);
				$dnum = count($arrSortIdDInfo);
				//
				AdminDao::updateClassesOrder($arrSelfinfo['id'], ($arrSelfinfo['orderid'] + 1 + $dnum));//self down
				for($i = 0; $i<$unum; $i++) {
					AdminDao::updateClassesOrder($arrSortIdUInfo[$i]['id'], ($arrSortIdUInfo[$i]['orderid'] + 1 + $dnum));//self smallclass down
				}
				//
				AdminDao::updateClassesOrder($arrDownInfo['id'], ($arrDownInfo['orderid'] - 1 - $unum));//up
				for($i = 0; $i<$dnum; $i++) {
					AdminDao::updateClassesOrder($arrSortIdDInfo[$i]['id'], ($arrSortIdDInfo[$i]['orderid'] - 1 - $unum));//down smallclass up
				}
			}
		}

		UpdateCommonDao::updateClassesInfo();
		$objResponse -> setTplValue('value', AdmincpAction::getClasssSet());
		$objResponse -> setTplValue('lid', $lid);
		$objResponse -> setTplValue('mid', $mid);
		//set tpl
		$objResponse -> setTplName("../admin/#sortclass");
	}
	
	protected static function sortCidDepth($arrUpCid) {
		if(count($arrUpCid) < 2) return;
		sort($arrUpCid);
		foreach($arrUpCid as $k => $v) {
			$arrSortId[$k] = $v['sortid'];
			$arrOrderId[$k] = $v['orderid'];
		}
		array_multisort($arrSortId, SORT_ASC, $arrOrderId, SORT_DESC, $arrUpCid);
		print_r($arrUpCid);
		echo "\r\n<br>";
	}
	
	protected function doAdminIframeCheck($objRequest, $objResponse) {
		if($this -> arrAdmin_fun['fun'] != 'check') {
			alert(conutf8('没有权限！'));
			redirect("login.php"); 
		}
		$mid = $objRequest->mid;
		$lid = $objRequest->lid;
		$deluid = $objRequest->deluid;
		$billno = $objRequest -> billno;
		$update = $objRequest -> update;
		$del = $objRequest -> del;
		$uid = $objRequest -> uid;
		$md5id = $objRequest -> md;
		
		//set tpl
		$objResponse -> setTplName("../admin/#iframecheck");
		//do
		if($del == 'yes') {
			AdminDao::deleteCheck($billno);
			$objResponse -> setTplValue("del", 'yes');
			return;
		}
		if(is_numeric($deluid) && !empty($deluid)) {
			AdminDao::deleteCheckProduct($deluid, $billno);
			ShopAction::getTmpShop(NULL, $uid, $objRequest, $objResponse, $billno);
			$allprice = $objResponse -> getTplValues('allprice');
			AdminDao::updatePayPrice($billno, $allprice);
		}
		if(!empty($objRequest->addproduct) && is_numeric($prod = $objRequest->pid)) {
			$arrValue['price'] = trim($objRequest->price);
			$arrValue['number'] = trim($objRequest->number) < 1 ? 1 : $objRequest->number;
			$prodtype = $objRequest->prodtype;
			$sellprice = 0;
			if($prod > 0) {
				if($prodtype == 0) {
					$arrPrice = AdminDao::getProductPrice($prod, NULL);
					if(AdminDao::checkBillProduct($prod, NULL, $billno) == false) {
						AdminDao::addBuyProduct($billno, $prod, NULL, $arrValue, $md5id);
					} else {
						alert(conutf8('已有相同商品，请更改数量即可！'));
					}
				}
				if($prodtype == 1) {
					$arrPrice = AdminDao::getProductPrice(NULL, $prod);
					//$arrValue['price'] = $arrValue['price'] != NULL ? $arrValue['price'] : $price;
					if(AdminDao::checkBillProduct(NULL, $prod, $billno) == false) {
						AdminDao::addBuyProduct($billno, NULL, $prod, $arrValue, $md5id);
					} else {
						alert(conutf8('已有相同商品，请更改数量即可！'));
					}
				}	
				if($arrValue['price'] != NULL) {
					$sellprice = $arrValue['price'] * $arrValue['number'];
				} else {
					$arrValue['price'] = $arrPrice['price'];
					if($arrPrice['member_offer'] == 1 && $uid > 0) {
						$arrValue['price'] = $arrPrice['price_member'];
					}
					if($arrPrice['special_offer'] == 1) {
						$arrValue['price'] = $arrPrice['price_special'];
					} 
					$sellprice = $arrValue['price'] * $arrValue['number'];
				}
				$sellprice = empty($sellprice) ? '0' : $sellprice;
				AdminDao::updateBuyProductSellPrice($billno, $sellprice);
			}
		}
		if($update == 'price') {
			$arrPid = $objRequest->pid;
			$arrSellPrice = $objRequest->sellprice;
			$arrNum = $objRequest->num;
			$num = count($arrSellPrice);
			$sellprice = '0';
			for($i = 0; $i<$num; $i++) {
				$arrNum[$i] = empty($arrNum[$i]) ? 1 : $arrNum[$i];
				$arrSellPrice[$i] = empty($arrSellPrice[$i]) ? '0' : $arrSellPrice[$i];
				$sellprice += $arrSellPrice[$i]*$arrNum[$i];
				AdminDao::updateSellPrice($billno, $arrPid[$i], $arrSellPrice[$i], $arrNum[$i]);
			}
			$sellprice = empty($sellprice) ? '0' : $sellprice;
			AdminDao::updatePayPrice($billno, $sellprice);
		}
		if($update == 'base') {
			$arrValue['name'] = $objRequest->name;
			$arrValue['phone'] = $objRequest->phone;
			$arrValue['mobile'] = $objRequest->mobile;
			$arrValue['address'] = $objRequest->address;
			$arrValue['postcode'] = $objRequest->postcode;
			$arrValue['paymethod'] = $objRequest->paymethod;
			$arrValue['freight'] = $objRequest->freight;
			$arrValue['ss'] = $objRequest->ss;
			$arrValue['remark'] = $objRequest->remark;
			$arrValue['email'] = $objRequest->email;
			$arrValue['name'] = $objRequest->name;
			$arrValue['freight_price'] = $objRequest->freightprice;
			$arrValue['freight_price'] = empty($arrValue['freight_price']) ? '0' : $arrValue['freight_price'];
			AdminDao::updateCheck($billno, Utilities::formArrayTurn2Str($arrValue));
			AdminDao::updatePayPrice($billno, NULL, $arrValue['freight_price']);
		}
		if(trim($state = $objRequest->state) != NULL) {
			$time = $state == '2' ? "'".Common::getCommonTime()."'" : 'NULL';
			AdminDao::updateCheck($billno, NULL, $state, $time);
		}
		if(trim($pay_success = $objRequest->pay_success) != NULL) {
			AdminDao::updateCheck($billno, "pay_success='$pay_success'");
		}
		$arrCheckUser = AdminDao::getCheckUser($billno);
		if(empty($arrCheckUser)) {
			$objResponse -> setTplValue("del", 'isdel');
			return;
		}
		$this -> getBillnoInfo($billno, $objResponse);

		$shop_web_phone = Common::getSiteInfo('value6', 'service');
		$shop_web_mobile = Common::getSiteInfo('value7', 'service');
		//send mail
		if($state == '2' || ($objRequest -> mailtype) == 'order_del') {
			$shop_web_name = Common::getSiteInfo('value1');
			$shop_user_name = $arrCheckUser["name"];
			$shop_user_address = $arrCheckUser["address"];
			$shop_user_phone = $arrCheckUser["phone"];
			$shop_user_mobile = $arrCheckUser["mobile"];
			$shop_user_pay = $arrCheckUser['pay_price'];
			$shop_user_freight = $arrCheckUser['freight'];
			$shop_web_billno_url = 'http://'.Common::getSiteInfo('value3').'/getshopinfo.php?billno='.$billno.'&md='.$arrCheckUser['md5id'];
			$shop_web_url = Common::getSiteInfo('value3');
			if($state == '2') {
				$shop_web_date = Common::getCommonTime();
				$shop_web_enddate = Common::getCommonTime(24 * 3); 
				include_once(__ROOT_PATH.'etc/confirmmail.php');
				$confirmmail = conutf8($confirmmail, 'UTF-8', 'GB2312');
				Common::sendMail($arrCheckUser["email"], Common::getSiteInfo('value9', "service"), $confirmmail, conutf8(Common::getSiteInfo('value1'), 'UTF-8', 'GB2312') . ' ( 出货通知 )', 'gb2312');
			}
			if(($objRequest -> mailtype) == 'order_del') {
				$title = $objRequest -> title;
				$message = $objRequest -> message;
				include_once(__ROOT_PATH.'etc/messagemail.php');
				$messagemail = conutf8($messagemail, 'UTF-8', 'GB2312');
				Common::sendMail($arrCheckUser["email"], Common::getSiteInfo('value9', "service"), $messagemail, conutf8(Common::getSiteInfo('value1') . ' ( '.$title.' )', 'UTF-8', 'GB2312'), 'gb2312');
				alert($title . conutf8(' 发送成功！'), 0);
			}
			$this -> sendHeader();
		}
		//end send mail
		$objResponse -> setTplValue("checkuser", $arrCheckUser);
		$objResponse -> setTplValue("billno", $billno);
		$objResponse -> setTplValue("lid", $lid);
		$objResponse -> setTplValue("mid", $mid);
		$objResponse -> setTplValue("md", $md5id);
		$objResponse -> setTplValue("webphone", $shop_web_phone);
		$objResponse -> setTplValue("webmobile", $shop_web_mobile);	
	}
	
	public function getBillnoInfo($billno, $objResponse) {
		$arrBillno = AdminDao::getCheck(NULL, NULL, NULL, $billno);
		$num = count($arrBillno);
		$uid = NULL;
		for($i = 0; $i<$num; $i++) {
			$arrBillno[$i]['url'] =  Patch::getProductUrl($arrBillno[$i]['pid']);
			$arrBillno[$i]['m_pic'] =  Patch::getProductMImg($arrBillno[$i]['m_pic']);
			$arrBillno[$i]['sell_price'] = $arrBillno[$i]['price'];
			if($arrBillno[$i]['member_offer'] == 1 && $arrBillno[$i]['uid'] > 0) {
				$arrBillno[$i]['sell_price'] = $arrBillno[$i]['price_member'];
			}
			if($arrBillno[$i]['special_offer'] == 1) {
				$arrBillno[$i]['sell_price'] = $arrBillno[$i]['price_special'];
			} 
			if($arrBillno[$i]['sellprice'] != NULL) $arrBillno[$i]['sell_price'] = $arrBillno[$i]['sellprice'];
			$arrBillno[$i]['all_price'] = $arrBillno[$i]['sell_price']*$arrBillno[$i]['num'];
			$uid = $arrBillno[$i]['uid'];
		}
		//set value
		$objResponse -> setTplValue("uid", $uid);
		$objResponse -> setTplValue("productnum", $num);
		$objResponse -> setTplValue("mybillno", $arrBillno);
	}
	
	protected function doreview($objRequest, $objResponse) {
		$v = $objRequest -> v;
		$id = $objRequest -> id;
		if($v == 1) {
			AdminDao::updateReviewInit($id, 1);
			echo conutf8('已审');
		} else {
			AdminDao::updateReviewInit($id, 0);
			echo conutf8('未审');
		}
		$this -> setDisplay();
	}
	
	protected function doUploadNewspic($objRequest, $objResponse) {
		$lid = $objRequest -> lid;
		$upload_success_script =  '';
		if(!empty($objRequest -> uploadpic)){
			if(is_uploaded_file($_FILES["userfile"]['tmp_name']) && $_FILES["userfile"]['error'] == '0') {
				$success=true;		
				list($width, $height, $type, $attr) = getimagesize($_FILES["userfile"]['tmp_name']);		
				if($type == 1) {
					$type = ".gif";
				} elseif($type == 2) {
					$type = ".jpg";
				} elseif($type == 3) {
					$type = ".png";
				} else {
					$result = "0";
					$success=false;
				}
			
				if($success){
					$fileid = getDateTimeId();
					$upfile = __PRODUCT_IMG.'news/' . $fileid;
					
					foreach($_FILES["userfile"] as $k => $v) {
						writeLog("upload_logo_log_".date("Ym").".txt", $k." --> ".$v);
					}
					if(move_uploaded_file($_FILES["userfile"]['tmp_name'], $upfile.$type)) {
						$upload_success_script = "<script language='javascript'>parent.setPic('".__IMG.'news/'.$fileid.$type."');</script>";
					} else {
						alert(conutf8('上传错误，写入文件失败！'));
					}
				} else {
					alert(conutf8('上传错误，不支持这种类型的图片上传！'));
				}
			} else {
				alert(conutf8('上传错误，不支持这种类型的图片上传！'));
				redirect("adminajaxset.php?switch=uploadnewspic&lid=$lid"); 
			}
		}
		$objResponse -> setTplValue("upload_success_script", $upload_success_script);
		$objResponse -> setTplValue("lid", $lid);
		$objResponse -> setTplValue("upload", 'uploadnewspic');
		//set tpl
		$objResponse -> setTplName("../admin/#uploadpic");
	}
	protected function doUploadLogo($objRequest, $objResponse) {
		$lid = $objRequest -> lid;
		$upload_success_script = '';
		if(!empty($objRequest -> uploadpic)){
			if(is_uploaded_file($_FILES["userfile"]['tmp_name']) && $_FILES["userfile"]['error'] == '0') {
				$success=true;		
				list($width, $height, $type, $attr) = getimagesize($_FILES["userfile"]['tmp_name']);		
				if($type == 1) {
					$type = ".gif";
				} elseif($type == 2) {
					$type = ".jpg";
				} elseif($type == 3) {
					$type = ".png";
				} else {
					$result = "0";
					$success=false;
				}
			
				if($success){
					$fileid = 'logo';
					$upfile = __PRODUCT_IMG.'logo/' . $fileid;
					
					foreach($_FILES["userfile"] as $k => $v) {
						writeLog("upload_logo_log_".date("Ym").".txt", $k." --> ".$v);
					}
					if(move_uploaded_file($_FILES['userfile']['tmp_name'], $upfile.$type)) {
						$upload_success_script = "<script language='javascript'>parent.setPic('product/logo/".$fileid.$type."');</script>";
					} else {
						alert(conutf8('上传错误，写入文件失败！'));
					}
				} else {
					alert(conutf8('上传错误，不支持这种类型的图片上传！'));
				}
			} else {
				alert(conutf8('上传错误，不支持这种类型的图片上传！'));
				redirect("adminajaxset.php?switch=uploadlogo&lid=$lid"); 
			}
		}
		$objResponse -> setTplValue("upload_success_script", $upload_success_script);
		$objResponse -> setTplValue("lid", $lid);
		$objResponse -> setTplValue("upload", 'uploadlogo');
		$objResponse -> setTplName("../admin/#uploadpic");
	}
	protected function doUploadWaterimg($objRequest, $objResponse) {
		$lid = $objRequest -> lid;
		$upload_success_script = '';
		if(!empty($objRequest -> uploadpic)){
			if(is_uploaded_file($_FILES["userfile"]['tmp_name']) && $_FILES["userfile"]['error'] == '0') {
				$success=true;		
				list($width, $height, $type, $attr) = getimagesize($_FILES["userfile"]['tmp_name']);		
				if($type == 1) {
					$type = ".gif";
				} elseif($type == 2) {
					$type = ".jpg";
				} elseif($type == 3) {
					$type = ".png";
				} else {
					$result = "0";
					$success=false;
				}
			
				if($success){
					foreach($_FILES["userfile"] as $k => $v) {
						writeLog("upload_mypic_log_".date("Ym").".txt", $k." --> ".$v);
					}
					$uploadfile = __PRODUCT_IMG . 'src/waterimg'.$type;
					if(move_uploaded_file($_FILES["userfile"]['tmp_name'], $uploadfile)) {
						$base50b = __PRODUCT_IMG."basewaterimage/50x50.png";
						$water50b = __PRODUCT_IMG."basewaterimage/watermark50x50.png";
						$base100b = __PRODUCT_IMG."basewaterimage/100x100.png";
						$water100b = __PRODUCT_IMG."basewaterimage/watermark100x100.png";
						$base200b = __PRODUCT_IMG."basewaterimage/200x200.png";
						$water200b = __PRODUCT_IMG."basewaterimage/watermark200x200.png";
						
						$base50 = __PRODUCT_IMG."waterimage/50x50.png";
						$water50 = __PRODUCT_IMG."waterimage/watermark50x50.png";
						$base100 = __PRODUCT_IMG."waterimage/100x100.png";
						$water100 = __PRODUCT_IMG."waterimage/watermark100x100.png";
						$base200 = __PRODUCT_IMG."waterimage/200x200.png";
						$water200 = __PRODUCT_IMG."waterimage/watermark200x200.png";

						Utilities::rectifyImages($uploadfile, $position,$base50b, $water50b, 50, 50, $water50, 'png');
						@copy($base50b, $base50);
						Utilities::rectifyImages($uploadfile, $position,$base100b, $water100b, 100,100, $water100, 'png');
						@copy($water100b, $water100);
						Utilities::rectifyImages($uploadfile, $position,$base200b, $water200b, 200, 200, $water200, 'png');
						@copy($base200b, $base200);
						$upload_success_script = "<script language='javascript'>parent.setPic('".$uploadfile."');</script>";
					}
				} else {
					alert(conutf8('上传错误，不支持这种类型的图片上传！'));
				}
			} else {
				alert(conutf8('上传错误，不支持这种类型的图片上传！'));
				redirect("adminajaxset.php?switch=uploadwaterimg&lid=$lid"); 
			}
		}
		$objResponse -> setTplValue("lid", $lid);
		$objResponse -> setTplValue("upload", 'uploadwaterimg');
		$objResponse -> setTplValue("upload_success_script", $upload_success_script);
		//set tpl
		$objResponse -> setTplName("../admin/#uploadpic");
	}
	protected function doUploadPic($objRequest, $objResponse) {
		$lid = $objRequest -> lid;
		$upload_success_script = '';
		if(!empty($objRequest -> uploadpic)){
			if(is_uploaded_file($_FILES["userfile"]['tmp_name']) && $_FILES["userfile"]['error'] == '0') {
				$success=true;		
				list($width, $height, $type, $attr) = getimagesize($_FILES["userfile"]['tmp_name']);		
				if($type == 1) {
					$type = ".gif";
				} elseif($type == 2) {
					$type = ".jpg";
				} elseif($type == 3) {
					$type = ".png";
				} else {
					$result = "0";
					$success=false;
				}
			
				if($success){
					$fileid = getDateTimeId();
					$mimg = __PRODUCT_IMG.'m/'.$fileid;
					$simg = __PRODUCT_IMG.'s/'.$fileid;
					$bimg = __PRODUCT_IMG.'b/'.$fileid;
					$srcimg = __PRODUCT_IMG.'src/'.$fileid;
					
					foreach($_FILES["userfile"] as $k => $v) {
						writeLog("upload_mypic_log_".date("Ym").".txt", $k." --> ".$v);
					}
					if(move_uploaded_file($_FILES["userfile"]['tmp_name'], $srcimg.$type)) {
						if(($position = Common::getSiteInfo('value1', 'watermark')) < 0) {
							$base100 = __PRODUCT_IMG."basewaterimage/100x100.png";
							$water100 = __PRODUCT_IMG."basewaterimage/watermark100x100.png";
							$base50 = __PRODUCT_IMG."basewaterimage/50x50.png";
							$water50 = __PRODUCT_IMG."basewaterimage/watermark50x50.png";
							$base200 = __PRODUCT_IMG."basewaterimage/200x200.png";
							$water200 = __PRODUCT_IMG."basewaterimage/watermark200x200.png";
						} else {
							$base100 = __PRODUCT_IMG."waterimage/100x100.png";
							$water100 = __PRODUCT_IMG."waterimage/watermark100x100.png";
							$base50 = __PRODUCT_IMG."waterimage/50x50.png";
							$water50 = __PRODUCT_IMG."waterimage/watermark50x50.png";
							$base200 = __PRODUCT_IMG."waterimage/200x200.png";
							$water200 = __PRODUCT_IMG."waterimage/watermark200x200.png";
						}

						$result = Utilities::rectifyImages($srcimg.$type, $position, $base100, $water100, 100,100, $mimg.'.jpg');
						$result = Utilities::rectifyImages($srcimg.$type, $position, $base50, $water50, 50, 50, $simg.'.jpg');
						$result = Utilities::rectifyImages($srcimg.$type, $position, $base200, $water200, 200, 200, $bimg.'.jpg');
						
						$upload_success_script = "<script language='javascript'>parent.setPic('".$fileid."');</script>";
						if($result == false) {
							alert(conutf8('上传错误，不支持这种类型的图片上传！'));
						}
					}
				} else {
					alert(conutf8('上传错误，不支持这种类型的图片上传！'));
				}
			} else {
				alert(conutf8('上传错误，不支持这种类型的图片上传！'));
				redirect("adminajaxset.php?switch=uploadpic&lid=$lid"); 
			}
		}
		$objResponse -> setTplValue("lid", $lid);
		$objResponse -> setTplValue("upload", 'uploadpic');
		$objResponse -> setTplValue("upload_success_script", $upload_success_script);
		//set tpl
		$objResponse -> setTplName("../admin/#uploadpic");
	}
}
?>