<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1393 $
 */
require("../libraries/common.inc.php");
require("room.share.php");
require(PHPB2B_ROOT.'libraries/page.class.php');
require(CACHE_PATH. "cache_setting1.php");
check_permission("offer");
$tpl_file = "offer";
$page = new Pages();
uses("trade", "tradefield", "product","tag","attachment", "form", "typeoption", "point");
$attachment = new Attachment("pic");
$form = new Forms();
$point = new Points();
$tradefield = new Tradefields();
$tag = new Tags();
$trade = new Trades();
$trade_controller = new Trade();
$typeoption = new Typeoption();
$conditions = array();
$conditions[]= "member_id = ".$_SESSION['MemberID'];
setvar("TradeTypes", $trade_controller->getTradeTypes());
setvar("TradeNames", $trade_controller->getTradeTypeNames());
$tmp_personalinfo = $memberinfo;
setvar("MemberInfo", $tmp_personalinfo);
$expires = $trade_controller->getOfferExpires();
setvar("TradeTypes", $trade_controller->getTradeTypes());
setvar("PhoneTypes", $typeoption->get_cache_type("phone_type"));
setvar("ImTypes", $typeoption->get_cache_type("im_type"));
setvar("OfferExpires",$expires);
if(isset($company_id))
setvar("CompanyId", $company_id);
$tMaxDay = (!empty($_PB_CACHE['setting1']['offer_refresh_lower_day']))?$_PB_CACHE['setting1']['offer_refresh_lower_day']:3;
$tMaxHours = (!empty($_PB_CACHE['setting1']['offer_update_lower_hour']))?$_PB_CACHE['setting1']['offer_update_lower_hour']:24;
$prod_info = array();
$conditions[] = "member_id='".$_SESSION['MemberID']."'";
if(!empty($company_info)){
    $tmp_personalinfo['MemberTel'] = $company_info['tel'];
    $tmp_personalinfo['ContactEmail'] = $company_info['email']?$company_info['email']:$tmp_personalinfo['email'];
}
if (isset($_GET['typeid'])) {
	$conditions[] = "type_id='".intval($_GET['typeid'])."'";
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (isset($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	switch ($do) {
		case "moderate":
			$tomorrow_start = $today_start+86400;
			$tomorrow_end = $today_start+172800;
			if($memberinfo['points']<$_PB_CACHE['setting1']['offer_moderate_point']){
				flash("not_enough_point");
			}
			$point->actions['moderate'] = array("rule"=>"every","do"=>"dec","point"=>$_PB_CACHE['setting1']['offer_moderate_point']);
			if (!empty($id)) {
				$item = $pdb->GetRow("SELECT * FROM {$tb_prefix}trades WHERE id=".$id." AND status=1 AND expire_time>".$time_stamp." AND member_id=".$_SESSION['MemberID']);
				if (empty($item)) {
					flash("failed");
				}
				if ($item['display_expiration']>$tomorrow_start && $item['display_expiration']<$tomorrow_end) {
					flash("one_time_within_24_h");
				}
				$sql = "UPDATE {$tb_prefix}trades SET display_order='1',display_expiration='".($time_stamp+86400)."' WHERE id=".$item['id'];
				$result = $pdb->Execute($sql);
				$point->update("moderate", $_SESSION['MemberID']);
				flash("success");
			}
			break;
		case "edit":		
			if(!empty($company_id)) {
				$company->primaryKey = "member_id";
				$company->newCheckStatus($companyinfo['status']);
				$company_info = $company->getInfoById($company_id);
				setvar("CompanyInfo",$company_info);
			}
			setvar("Forms", $form->getAttributes());
			if(!empty($id)){
			    $trade_info = $trade->read("*",$id,null," member_id=".$_SESSION['MemberID']);
			    if (empty($trade_info) || !$trade_info) {
			        flash('data_not_exists');
			    }
			    if(!empty($trade_info['formattribute_ids'])){
			    	setvar("Forms", $form->getAttributes(explode(",", $trade_info['formattribute_ids'])));
			    }
			    $trade_info['expire_date'] = date("Y-m-d", $trade_info['expire_time']);
			    if (!empty($trade_info['picture'])) {
			    	$trade_info['image'] = pb_get_attachmenturl($trade_info['picture'], "../");
			    }
			    if(isset($trade_info['OfferPrimImaccount'])) {
			    	$tmp_personalinfo['MemberQQ'] = $trade_info['OfferPrimImaccount'];
			    }
			    if(isset($trade_info['OfferPrimTelnumber'])) {
			    	$tmp_personalinfo['MemberTel'] = $trade_info['OfferPrimTelnumber'];
			    }
			    if(!empty($trade_info['tag_ids'])){
			        $tag->getTagsByIds($trade_info['tag_ids'], true);
			        $trade_info['tag'] = $tag->tag;
			    }
			}else{
				if(!empty($companyinfo)){
					$trade_info['industry_id1'] = $companyinfo['industry_id1'];
					$trade_info['industry_id2'] = $companyinfo['industry_id2'];
					$trade_info['industry_id3'] = $companyinfo['industry_id3'];
					$trade_info['area_id1'] = $companyinfo['area_id1'];
					$trade_info['area_id2'] = $companyinfo['area_id2'];
					$trade_info['area_id3'] = $companyinfo['area_id3'];
				}else{
					$trade_info['area_id1'] = $memberinfo['area_id1'];
					$trade_info['area_id2'] = $memberinfo['area_id2'];
					$trade_info['area_id3'] = $memberinfo['area_id3'];
				}
			}
			setvar("item",$trade_info);
			$tpl_file = "offer_edit";
			template($tpl_file);
			exit;
			break;
		case "stat":
	    	$tpl_file = "tradestat";
	    	$amount = $trade->findAll("Trade.type_id AS TradeTypeId,COUNT(Trade.id) AS CountTrade",null, $conditions,"Trade.type_id",0,10,"Trade.type_id");
	    	foreach ($amount as $val) {
	    		$stats[$val['TradeTypeId']] = $val['CountTrade'];
	    	}
	    	setvar("UserTradeStat",$stats);
	    	setvar("ProductAmount",$product->findCount(null, $conditions,"Product.id"));			
			break;
		default:
			break;
	}  
	if($do=="pro2offer" && !empty($_GET['productid'])){
        $product = new products();
        $item = $product->read("*", $_GET['productid'], null, "member_id=".$_SESSION['MemberID']);
        if (!empty($item)) {
        	$item['type_id'] = 2;
        	$item['title'] = $item['name'];
        	if (isset($item['picture'])) {
        		$item['image'] = pb_get_attachmenturl($item['picture'], "../");
        	}
        	setvar("item", $item);
        }
       	$tpl_file = "offer_edit";
       	template($tpl_file);
       	exit;
    }
    if ($do=="update" && !empty($id)) {
    	$vals = array();
    	$vals['modified'] = $time_stamp;
    	$conditions[]= "status='1'";
    	$pre_update_time = $pdb->GetOne("select modified from {$tb_prefix}trades where id=".$id." and member_id=".$_SESSION['MemberID']);
    	if ($pre_update_time>($time_stamp-$tMaxHours*3600)) {
    		flash("only_one_time_within_hours");
    	}
    	$result = $trade->save($vals, "update", $id, null, $conditions);
    	if (!$result) {
    		flash("action_failed");
    	}else{
    		flash("success");
    	}
    }
	if ($do=="refresh" && !empty($id)) {
    	$vals = array();
    	$pre_submittime = $pdb->GetOne("select submit_time from {$tb_prefix}trades where id=".$id." and member_id=".$_SESSION['MemberID']);
    	if ($pre_submittime>($time_stamp-$tMaxDay*86400)) {
    		flash("allow_refresh_day");
    	}
    	$vals['submit_time'] = $time_stamp;
    	$vals['expire_days'] = 1;
    	$vals['expire_time'] = $time_stamp+(24*3600*$vals['expire_days']);
    	$conditions[]= "status='1'";
    	$result = $trade->save($vals, "update", $id, null, $conditions);
    	if (!$result) {
    		flash("action_failed");
    	}else{
    		flash("success");
    	}
    }
}
if (isset($_POST['do']) && !empty($_POST['data']['trade'])) {
	pb_submit_check('data');
    $res = $_POST['data']['trade'];
    $now_offer_amount = $trade->findCount(null, "created>".$today_start." AND member_id=".$_SESSION['MemberID']);
    if(isset($_POST['id'])){
    	$id = intval($_POST['id']);
    }
    if ($g['offer_check']) {
        $res['status'] = 0;
        $msg = 'msg_wait_check';
    }else {
        $res['status'] = 1;
        $msg = 'success';
    }
    if (!empty($_FILES['pic']['name'])) {
    	$attach_id = (empty($id))?"offer-".$_SESSION['MemberID']."-".($trade->getMaxId()+1):"offer-".$_SESSION['MemberID']."-".$id;
    	$attachment->rename_file = $attach_id;
		$attachment->upload_process();
        $res['picture'] = $attachment->file_full_url;
    }
	$res['tag_ids'] = $tag->setTagId($_POST['data']['tag']);
    $form_type_id = 1;
    if (!empty($company_id)) {
    	$res['company_id'] = $company_id;
    }else{
    	$res['company_id'] = 0;
    }
    $res['cache_contacts'] = serialize(array('qq'=>$memberinfo['qq'], 'icq'=>$memberinfo['icq'], 'skype'=>$memberinfo['skype'], 'yahoo'=>$memberinfo['yahoo'], 'msn'=>$memberinfo['msn']));
    if (!empty($id)) {
		$item_ids = $form->Add($id,$_POST['data']['formitem']);
		$res['formattribute_ids'] = $item_ids;
    	$tradefield_res['trade_id'] = $id;
        $res['modified'] = $time_stamp;
        $res = $trade->save($res, "update", $id, null, $conditions);
    }else {
    	if ($g['max_offer'] && $now_offer_amount>=$g['max_offer']) {
    		flash('one_day_max');
    	}
        $res['member_id'] = $_SESSION['MemberID'];
        $res['company_id'] = $company_id;
        $res['submit_time'] = $res['created'] = $res['modified'] = $time_stamp;
        if (!empty($_POST['expire_days'])) {
        	if (array_key_exists($_POST['expire_days'], $trade_controller->getOfferExpires())) {
        		$res['expire_days'] = $_POST['expire_days'];
        		$res['expire_time'] = $res['expire_days']*86400+$time_stamp;
        	}
        }else{
        	$res['expire_days'] = 10;
        	$res['expire_time'] = $res['expire_days']*86400+$time_stamp;
        }
        if(isset($companyinfo['name'])) {
        	$res['cache_companyname'] = $companyinfo['name'];
        }
        $res = $trade->save($res);
        $new_id = $trade->table_name."_id";
        $new_id = $trade->$new_id;;
        $last_trade_id = $trade->getMaxId();
        $item_ids = $form->Add($last_trade_id, $_POST['data']['formitem']);
        if($item_ids){
        	$pdb->Execute("UPDATE {$tb_prefix}trades SET formattribute_ids='{$item_ids}' WHERE id=".$last_trade_id);
        }
        $tradefield_res['trade_id'] = $last_trade_id;
    }
    $tradefield->replace($tradefield_res);
    if(!$res) {
        flash("action_failed");
    }else{
    	flash($msg?$msg:"success");
    }
}
if (isset($_POST['del']) && !empty($_POST['tradeid'])) {
	$tRes = $trade->del($_POST['tradeid'], "member_id = ".$_SESSION['MemberID']);
	if($tRes) $pdb->Execute("DELETE from {$tb_prefix}tradefields WHERE trade_id IN (".implode(",",$_POST['tradeid']).")");
}
if(isset($_POST['refresh'])){
	if (!empty($_POST['refresh']) && !empty($_POST['tradeid'])) {
		$vals = array();
		$pre_submittime = $pdb->GetOne("select max(submit_time) from {$tb_prefix}trades where member_id=".$_SESSION['MemberID']);
		if ($pre_submittime>($time_stamp-$tMaxDay*86400)) {
			flash("allow_refresh_day");
		}
		$vals['submit_time'] = $time_stamp;
		$vals['expire_days'] = 10;
		$vals['expire_time'] = $time_stamp+(24*3600*$vals['expire_days']);
		$conditions[]= "status='1'";
		$ids = implode(",", $_POST['tradeid']);
		$conditions[]= "id in (".$ids.")";
		$condition = implode(" AND ", $conditions);
		$sql = "update ".$trade->getTable()." set submit_time=".$time_stamp.",expire_days=10,expire_time=".$vals['expire_time']." where ".$condition;
		$result = $pdb->Execute($sql);
		if ($result) {
			flash("success");
		}else{
			flash("action_failed");
		}
	}
}
$amount = 0;
$amount = $trade->findCount(null, $conditions);
$page->setPagenav($amount);
$result = $trade->findAll("*", null, $conditions, "Trade.submit_time DESC,Trade.id DESC", $page->firstcount,$page->displaypg);
if (!empty($result)) {
	for($i=0; $i<count($result); $i++){
		$result[$i]['expire_date'] = date("Y-m-d", $result[$i]['expire_time']);
	}
	setvar("Items", $result);
}
uaAssign(array("ByPages"=>$page->getPagenav()));
setvar("OFFER_MODERATE_POINT", $_PB_CACHE['setting1']['offer_moderate_point']);
setvar("CheckStatus", $typeoption->get_cache_type("check_status"));
setvar("Amount", $amount);
setvar("TimeStamp", $time_stamp);
template($tpl_file);
?>