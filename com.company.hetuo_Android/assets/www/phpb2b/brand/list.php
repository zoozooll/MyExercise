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
require(PHPB2B_ROOT.'libraries/page.class.php');
uses("brand", "brandtype");
$page = new Pages();
$brand = new Brands();
$brandtype = new Brandtypes();
$condition = null;
$conditions = array();
$viewhelper->setTitle(L("brands", "tpl"));
$viewhelper->setPosition(L("brands", "tpl"), "brand.php");
if (isset($_GET['catid'])) {
	$typeid = intval($_GET['catid']);
	$conditions[] = "type_id='".$typeid."'";
	$viewhelper->setTitle($type_name = $pdb->GetOne("SELECT name FROM {$tb_prefix}brandtypes WHERE id='".$typeid."'"));
	$viewhelper->setPosition($type_name, "brand/list.php?catid=".$typeid);
	$rs = $pdb->GetArray("SELECT id,name FROM {$tb_prefix}brandtypes WHERE parent_id=".$typeid);
}
if (empty($rs)) {
	$rs = $pdb->GetArray("SELECT id,name FROM {$tb_prefix}brandtypes WHERE parent_id=0");
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if ($do == "search") {
		if (isset($_GET['q'])) {
			$searchkeywords = urldecode($_GET['q']);
			$conditions[] = "name like '%".$searchkeywords."%'";
		}
		if (isset($_GET['letter'])) {
			$viewhelper->setTitle(L("brands_with_letter", "tpl", $_GET['letter']));
			$viewhelper->setPosition(L("brands_with_letter", "tpl", $_GET['letter']));
			$conditions[] = "letter='".trim($_GET['letter'])."'";
		}
	}
}
$brand->setCondition($conditions);
$amount = $brand->findCount(null, $conditions);
$page->setPagenav($amount);
$sql = "SELECT * FROM {$tb_prefix}brands".$brand->getCondition()." LIMIT ".$page->firstcount.",".$page->displaypg;
$result = $pdb->GetArray($sql);
$result = $brand->formatResult($result);
setvar('Items', $result);
setvar('Types',$rs);
setvar("ByPages", $page->getPagenav());
render("brand.list");
?>