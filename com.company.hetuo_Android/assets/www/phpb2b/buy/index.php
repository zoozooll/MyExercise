<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision$
 */
define('CURSCRIPT', 'index');
require("../libraries/common.inc.php");
require("../offer/common.inc.php");
setvar("nav_id", 2);
/**public**/
require(PHPB2B_ROOT.'libraries/page.class.php');
$page = new Pages();
$page->pagetpl_dir = $theme_name;
$viewhelper->setTitle(L('buyer', 'tpl'));
$viewhelper->setPosition(L('buyer', 'tpl'));
$type_id = $_GET['typeid'] = 1;
$conditions[]= "t.type_id='$type_id'";
if (isset($_GET['industryid'])) {
	$industry_id = intval($_GET['industryid']);
	$tmp_info = $industry->setInfo($industry_id);
	if (!empty($tmp_info)) {
		$conditions[] = "t.industry_id".$tmp_info['level']."=".$tmp_info['id'];
		$viewhelper->setTitle($tmp_info['name']);
		$viewhelper->setPosition($tmp_info['name'], "offer/list.php?industryid=".$tmp_info['id']);
	}
}
setvar("OtherIndustry", $industry->getSubIndustry($industry_id, true));
if (isset($_GET['areaid'])) {
	$area_id = intval($_GET['areaid']);
	$tmp_info = $area->setInfo($area_id);
	if (!empty($tmp_info)) {
		$conditions[] = "t.area_id".$tmp_info['level']."=".$tmp_info['id'];
		$viewhelper->setTitle($tmp_info['name']);
		$viewhelper->setPosition($tmp_info['name'], "offer/list.php?areaid=".$tmp_info['id']);
	}
}
setvar("OtherArea", $area->getSubArea($area_id, true));
if (isset($_GET['type'])) {
	if($_GET['type']=="urgent"){
		$conditions[]="t.if_urgent='1'";
	}
}
//$trade->setCondition($conditions);
$amount = $trade->findCount(null, $conditions, null, "t");
$page->setPagenav($amount);
$result = $trade->getRenderDatas($conditions, $_PB_CACHE['setting1']['offer_filter']);
$important_result = $trade->getStickyDatas();
if (!empty($important_result)) {
	setvar("StickyItems", $important_result);
}
setvar('Items', $result);
uaAssign(array("ByPages"=>$page->getPagenav(), "Industries"=>$industry->getIndustry(), "Areas"=>$area->getCacheArea()));
setvar("TradeTypes", $trade_controller->getTradeTypes());
/**end public**/
setvar("typeid", 1);
render("offer.list");
?>