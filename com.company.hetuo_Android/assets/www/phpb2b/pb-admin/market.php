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
require(LIB_PATH. 'page.class.php');
require("session_cp.inc.php");
include(CACHE_PATH. "cache_industry.php");
include(CACHE_PATH. "cache_area.php");
uses("market", "attachment", "typeoption");
$attachment = new Attachment('pic');
$market = new Markets();
$typeoption = new Typeoption();
$page = new Pages();
$conditions = null;
$tpl_file = "market";
if (isset($_POST['del']) && !empty($_POST['id'])) {
	$market->del($_POST['id']);
}
if (isset($_POST['check']) && !empty($_POST['id'])) {
	$ids = implode(",", $_POST['id']);
	$condition = " id in (".$ids.")";
	$sql = "update {$tb_prefix}markets set status=1 where ".$condition;
	$result = $pdb->Execute($sql);
}
if (isset($_POST['uncheck']) && !empty($_POST['id'])) {
	$ids = implode(",", $_POST['id']);
	$condition = " id IN (".$ids.")";
	$sql = "UPDATE {$tb_prefix}markets SET status=0 WHERE ".$condition;
	$result = $pdb->Execute($sql);
}
if (isset($_POST['recommend'])) {
	foreach($_POST['id'] as $val){
		$commend_now = $market->field("if_commend", "id=".$val);
		if($commend_now=="0"){
			$result = $market->saveField("if_commend", "1", intval($val));
		}else{
			$result = $market->saveField("if_commend", "0", intval($val));
		}
	}
	if ($result) {
		flash("success");
	}else{
		flash();
	}
}
if (isset($_POST['save']) && !empty($_POST['data']['market'])) {
	$vals = array();
	$vals = $_POST['data']['market'];
	$id = intval($_POST['id']);
	if (!empty($_FILES['pic']['name'])) {
		$attachment->if_thumb_large = true;
		$attachment->large_scale = "410*226";
		$attachment->if_watermark = false;
		if (!empty($id)) {
			$attachment->rename_file = "market-".$id;
		}else{
			$attachment->rename_file = "market-".($market->getMaxId()+1);
		}
		$attachment->upload_process();
		$vals['picture'] = $attachment->file_full_url;
	}
	if (!empty($id)) {
		$vals['modified'] = $time_stamp;
		$result = $market->save($vals, "update", $id);
	}else {
		$vals['created'] = $vals['modified'] = $time_stamp;
		$result = $market->save($vals);
	}
	if(!$result){
		flash();
	}
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	$res = null;
	if(!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if ($do == "edit") {
		if(!empty($id)){
			$sql = "select * FROM {$tb_prefix}markets WHERE id=".$id;
			$res = $pdb->GetRow($sql);
			$res['image'] = pb_get_attachmenturl($res['picture'], '../', 'small');
			setvar("item", $res);
		}
		setvar("MarketStatus", $typeoption->get_cache_type("common_status"));
		setvar("AskAction", $typeoption->get_cache_type("common_option"));
		$tpl_file = "market.edit";
		template($tpl_file);
		exit;
	}
	if ($do == "del" && !empty($id)) {
		$market->del($id);
	}
}
$amount = $market->findCount();
$page->setPagenav($amount);
$result = $market->findAll("*", null, $conditions, "id desc", $page->firstcount, $page->displaypg);
if (!empty($result)) {
	$count = count($result);
	for($i=0; $i<$count; $i++) {
		$result[$i]['industryname'] = $_PB_CACHE['industry'][1][$result[$i]['industry_id1']].$_PB_CACHE['industry'][2][$result[$i]['industry_id2']];
		$result[$i]['areaname'] = $_PB_CACHE['area'][1][$result[$i]['area_id1']].$_PB_CACHE['area'][2][$result[$i]['area_id2']];
	}
	setvar("Items", $result);
	setvar("ByPages", $page->pagenav);
}
template($tpl_file);
?>