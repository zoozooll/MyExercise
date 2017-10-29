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
uses("industry","company","area");
include(PHPB2B_ROOT.'libraries/page.class.php');
include(CACHE_PATH. "cache_industry.php");
include(CACHE_PATH. "cache_membergroup.php");
include(CACHE_PATH."cache_area.php");
include(CACHE_PATH. 'type_manage_type.php');
$page = new Pages();
$page->pagetpl_dir = $theme_name;
$industry = new Industries();
$area = new Areas();
$company = new Companies();
$conditions = array();
$conditions[] = "Company.status=1 ";
$industry_id = $area_id = 0;
$viewhelper->setTitle(L("yellow_page", 'tpl'));
$viewhelper->setPosition(L("yellow_page", 'tpl'), 'company/');
if (isset($_GET['industryid'])) {
	$industry_id = intval($_GET['industryid']);
	$tmp_info = $industry->setInfo($industry_id);
	if (!empty($tmp_info)) {
		$conditions[] = "Company.industry_id".$tmp_info['level']."=".$tmp_info['id'];
		$viewhelper->setTitle($tmp_info['name']);
		$viewhelper->setPosition($tmp_info['name'], "company/list.php?industryid=".$tmp_info['id']);
	}
}
setvar("OtherIndustry", $industry->getSubIndustry($industry_id, true));
if (isset($_GET['areaid'])) {
	$area_id = intval($_GET['areaid']);
	$tmp_info = $area->setInfo($area_id);
	if (!empty($tmp_info)) {
		$conditions[] = "Company.area_id".$tmp_info['level']."=".$tmp_info['id'];
		$viewhelper->setTitle($tmp_info['name']);
		$viewhelper->setPosition($tmp_info['name'], "company/list.php?areaid=".$tmp_info['id']);
	}
}
setvar("OtherArea", $area->getSubArea($area_id, true));
if (isset($_GET['type'])) {
	if ($_GET['type']=="commend") {
		$conditions[]="Company.if_commend='1'";
		$viewhelper->setPosition(L("commend", 'tpl'));
	}
}
if(!empty($_GET['le'])){
	$conditions[]="Company.first_letter='".strtolower($_GET['le'])."'";
	$viewhelper->setTitle(L('search_for', 'tpl', $_GET['le']));
	$viewhelper->setPosition(L('search_for', 'tpl', $_GET['le']));
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if ($do == "search") {
		$viewhelper->setTitle(L('search', 'tpl'));
		if(isset($_GET['typeid'])){
			$conditions[]="Company.type_id=".intval($_GET['typeid']);
		}
		if(isset($_GET['q'])) {
			$searchwords = strip_tags($_GET['q']);
			$conditions[]= "Company.name like '%".$searchwords."%'";
			$viewhelper->setTitle(L('search_for', 'tpl', $searchwords));
		}
		if (isset($_GET['main_prod'])) {
			$conditions[]= "Company.main_prod='".$_GET['main_prod']."'";
		}
	}
}
$tpl_file = "list";
$fields = "Company.id,cache_membergroupid,Company.name,Company.cache_spacename AS userid,Company.if_commend,Company.member_id,Company.created as pubdate,Company.main_prod,Company.description,Company.manage_type,Company.picture,Company.area_id1,Company.area_id2,Company.area_id3,Company.industry_id1,Company.industry_id2,Company.industry_id3";
$joins = array();
$joins[] = "LEFT JOIN {$tb_prefix}members m ON m.id=Company.member_id";
$amount = $company->findCount($joins, $conditions, "Company.id");
$page->setPagenav($amount);
$result = $company->findAll($fields.",m.username,m.credits,m.points,m.membergroup_id,m.space_name",$joins,$conditions,"Company.id desc",$page->firstcount,$page->displaypg);
$result = $company->formatResult($result);
setvar("Items", $result);
uaAssign(array(
"ByPages"=>$page->getPagenav(),
"Industry"=>$_PB_CACHE['industry'],
"Areas"=> $_PB_CACHE['area']
));
$viewhelper->setTitle(L("search", "tpl"));
$viewhelper->setPosition(L("search", "tpl"));
render("company.".$tpl_file);
?>