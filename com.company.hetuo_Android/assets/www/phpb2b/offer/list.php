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
define('CURSCRIPT', 'list');
require("../libraries/common.inc.php");
require("common.inc.php");
require(PHPB2B_ROOT.'libraries/page.class.php');
include(CACHE_PATH. "cache_setting1.php");
uses("tag");
$tag = new Tags();
$page = new Pages();
$page->pagetpl_dir = $theme_name;
$viewhelper->setTitle(L('offer', 'tpl'));
$viewhelper->setPosition(L('offer', 'tpl'), "offer/");
$trade->setParams();
$tmp_q = http_build_query($trade->params['url']);
if (!empty($tmp_q)) {
	setvar("addParams", $tmp_q."&");
}
if (isset($_GET['typeid'])) {
	$type_id = intval($_GET['typeid']);
	$conditions[]= "t.type_id='".$type_id."'";
	setvar("typeid", $type_id);
	$trade_controller->setTypeInfo($type_id);
	$type_name = $trade_controller->type_info['name'];
	$viewhelper->setTitle($type_name);
	$viewhelper->setPosition($type_name, "offer/list.php?typeid=".$type_id);
}
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
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if ($do == "search") {
		if (isset($_GET['q'])) {
			$searchkeywords = urldecode($_GET['q']);
			$tag->Add($searchkeywords);
			usetcookie("latest_search", implode(",", array($_COOKIE[$cookiepre."latest_search"], $searchkeywords)));
			$viewhelper->setTitle(L("search_in_keyword", "tpl", $searchkeywords));
			$viewhelper->setPosition(L("search_in_keyword", "tpl", $searchkeywords));
			require(LIB_PATH. "segment.class.php");
			$segment = new Segments();
			$searchkeywords = $segment->formatStr($searchkeywords);
			$conditions[]= "t.title like '%".$searchkeywords."%'";
			setvar("highlight_str", $segment->hilight_str);
		}
	}
	if (isset($_GET['pubdate'])) {
		switch ($_GET['pubdate']) {
			case "l3":
				$conditions[] = "t.submit_time>".($time_stamp-3*86400);
				break;
			case "l10":
				$conditions[] = "t.submit_time>".($time_stamp-10*86400);
				break;
			case "l30":
				$conditions[] = "t.submit_time>".($time_stamp-30*86400);
				break;
			default:
				break;
		}
	}
}
if ($_PB_CACHE['setting1']['offer_expire_method']==2 || $_PB_CACHE['setting1']['offer_expire_method']==3) {
	$conditions[] = "t.expire_time>".$time_stamp;
}
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
render("offer.list");
?>