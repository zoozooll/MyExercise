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
require("../share.inc.php");
require(PHPB2B_ROOT.'./libraries/page.class.php');
require(CACHE_PATH. "cache_type.php");
include(CACHE_PATH. "cache_industry.php");
include(CACHE_PATH. "cache_area.php");
uses("expo","area");
$page = new Pages();
$expo = new Expoes();
$page = new Pages();
$area = new Areas();
$conditions = array();
$viewhelper->setTitle(L("fair", "tpl"));
$viewhelper->setPosition(L("fair", "tpl"), "fair/");
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if ($do == "search") {
		if (!empty($_GET['q'])) {
			$conditions[] = "name like '%".$_GET['q']."%'";
		}
	}
}
if (isset($_GET['type'])) {
	if ($_GET['type']=="commend") {
		$conditions[] = "if_commend=1";
	}
}
if(isset($_GET['typeid'])){
	$type_id = intval($_GET['typeid']);
	$conditions[] = "expotype_id=".$type_id;
	$type_name = $_PB_CACHE['expotype'][$type_id];
	$viewhelper->setTitle($type_name);
	$viewhelper->setPosition($type_name, "fair/list.php?typeid=".$type_id);
}
if (isset($_GET['areaid'])) {
	$area_id = intval($_GET['areaid']);
	$tmp_info = $area->setInfo($area_id);
	if (!empty($tmp_info)) {
		$conditions[] = "area_id".$tmp_info['level']."=".$tmp_info['id'];
		$viewhelper->setTitle($tmp_info['name']);
		$viewhelper->setPosition($tmp_info['name'], "fair/list.php?areaid=".$tmp_info['id']);
	}
}
$amount = $expo->findCount(null, $conditions);
$page->setPagenav($amount);
$result = $expo->findAll("*", null, $conditions, "id desc", $page->firstcount, $page->displaypg);
if (!empty($result)) {
	for ($i=0; $i<count($result); $i++){
		if($result[$i]['begin_time']) $result[$i]['begin_date'] = @date("Y-m-d", $result[$i]['begin_time']);
		if($result[$i]['end_time']) $result[$i]['end_date'] = @date("Y-m-d", $result[$i]['end_time']);
		$result[$i]['description'] = mb_substr(strip_tags(trim($result[$i]['description'])), 0, 100);
		$result[$i]['typename'] = $_PB_CACHE['expotype'][$result[$i]['expotype_id']];
		$result[$i]['title'] = $result[$i]['name'];
		if(isset($result[$i]['picture'])) $result[$i]['image'] = pb_get_attachmenturl($result[$i]['picture'], '', 'small');
		if(!empty($result[$i]['area_id1'])){
			$result[$i]['area'] = "(".$_PB_CACHE['area'][1][$result[$i]['area_id1']].$_PB_CACHE['area'][2][$result[$i]['area_id2']].$_PB_CACHE['area'][3][$result[$i]['area_id1']].")";
		}
	}
	setvar("Items", $result);
}
setvar("Areas", $_PB_CACHE['area']);	
setvar("Type",$_PB_CACHE['expotype']);
$viewhelper->setTitle(L("search", "tpl"));
$viewhelper->setPosition(L("search", "tpl"));
setvar("ByPages",$page->pagenav);
render("fair.list");
?>