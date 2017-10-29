<?php 
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1147 $
 */
define('CURSCRIPT', 'industry');
require("../libraries/common.inc.php");
require("../share.inc.php");
uses("industry");
$industry = new Industries();
$viewhelper->setPosition(L("sub_special", "tpl"), "special/");
$viewhelper->setTitle(L("sub_special", "tpl"));
$viewhelper->setPosition(L("sub_industry", "tpl"), "special/index.php?type=industry");
$viewhelper->setTitle(L("sub_industry", "tpl"));
if (isset($_GET['id']) || isset($_GET['name'])) {
	if (!empty($_GET['id'])) {
		$industry_id = intval($_GET['id']);
	}elseif (!empty($_GET['name']) && $rewrite_compatible){
		$name = trim(rawurldecode($_GET['name']));
		if (is_numeric($name)) {
			$industry_id = intval($name);
		}else{
			$industry_id = $industry->field("id", "name='".$name."'");
		}
	}
	$product_amount = $pdb->GetOne("SELECT count(id) FROM ".$tb_prefix."products WHERE industry_id1={$industry_id} OR industry_id3={$industry_id} OR industry_id3={$industry_id}");
	if ($product_amount>0) {
		setvar("HaveProducts", true);
	}
	$industry->setInfo($industry_id);
	if (!empty($industry->info)) {
		$cache_id = $industry_id;
		$viewhelper->setTitle($industry->info['name']);
		$viewhelper->setPosition($industry->info['name']);
		if (empty($industry->info['description'])) {
			$industry->info['description'] = nl2br(L("sub_default_desc", "tpl", $industry->info['name']));
		}
		$viewhelper->setMetaDescription($industry->info['description']);
		$viewhelper->setMetaKeyword(implode(",", array($industry->info['name'].L("offer", "tpl"), $industry->info['name'].L("market", "tpl"), $industry->info['name'].L("product_center", "tpl"), $industry->info['name'].L("yellow_page", "tpl"), $industry->info['name'].L("info", "tpl"))));
		setvar("item", $industry->info);
	}else{
		flash("data_not_exists", URL, 0);
	}
	render("special.industry");
}else{
	flash("data_not_exists", URL, 0);
}
?>