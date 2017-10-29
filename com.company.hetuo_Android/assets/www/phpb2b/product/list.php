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
uses("company","industry","product","area");
require(LIB_PATH. 'page.class.php');
include(CACHE_PATH. "cache_industry.php");
include(CACHE_PATH. "cache_area.php");
$page = new Pages();
$page->pagetpl_dir = $theme_name;
$industry = new Industries();
$area = new Areas();
$product = new Products();
$conditions = array();
$conditions[] = "Product.status=1 ";
$industry_id = $area_id = 0;
$viewhelper->setPosition(L("product_center", 'tpl'), 'product/');
$viewhelper->setTitle(L("product_center", 'tpl'));
if (isset($_GET['industryid'])) {
	$industry_id = intval($_GET['industryid']);
	$tmp_info = $industry->setInfo($industry_id);
	if (!empty($tmp_info)) {
		$conditions[] = "Product.industry_id".$tmp_info['level']."=".$tmp_info['id'];
		$viewhelper->setTitle($tmp_info['name']);
		$viewhelper->setPosition($tmp_info['name'], "product/list.php?industryid=".$tmp_info['id']);
	}
}
setvar("OtherIndustry", $industry->getSubIndustry($industry_id, true));
if (isset($_GET['areaid'])) {
	$area_id = intval($_GET['areaid']);
	$tmp_info = $area->setInfo($area_id);
	if (!empty($tmp_info)) {
		$conditions[] = "Product.area_id".$tmp_info['level']."=".$tmp_info['id'];
		$viewhelper->setTitle($tmp_info['name']);
		$viewhelper->setPosition($tmp_info['name'], "product/list.php?areaid=".$tmp_info['id']);
	}
}
setvar("OtherArea", $area->getSubArea($area_id, true));
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if ($do == "search") {
		if(isset($_GET['q'])) {
			$searchkeywords = strip_tags(urldecode($_GET['q']));
			setvar("searchwords","<font color=\"red\">".$searchkeywords."</font>");
			$conditions[]= "Product.name like '%".$searchkeywords."%'";
		}
		if (isset($_GET['pubdate'])) {
			switch ($_GET['pubdate']) {
				case "l3":
					$conditions[] = "Product.created>".($time_stamp-3*86400);
					break;
				case "l10":
					$conditions[] = "Product.created>".($time_stamp-10*86400);
					break;
				case "l30":
					$conditions[] = "Product.created>".($time_stamp-30*86400);
					break;
				default:
					break;
			}
		}
	}
}
if (isset($_GET['type'])) {
	if($_GET['type']=="commend"){
	    $conditions[] = "Product.ifcommend='1'";
	}
}
$amount = $product->findCount(null, $conditions,"Product.id");
$page->setPagenav($amount);
$joins[] = 'LEFT JOIN '.$tb_prefix.'members m ON m.id=Product.member_id';
$result = $product->findAll("m.username,m.space_name AS userid,m.membergroup_id,m.credits,Product.cache_companyname AS companyname,Product.*", $joins, $conditions, "Product.ifcommend desc,Product.id desc", $page->firstcount, $page->displaypg);
$result = $product->formatResult($result);
setvar("Items", $result);
uaAssign(array("ByPages"=>$page->pagenav,"Industries"=>$_PB_CACHE['industry'], "Areas"=>$_PB_CACHE['area']));
unset($subs);
render("product.list");
?>