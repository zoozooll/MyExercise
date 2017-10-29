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
require("../share.inc.php");
require(PHPB2B_ROOT.'libraries/page.class.php');
uses("dicttype","dict");
$dict = new Dicts();
$page = new Pages();
$dicttype = new Dicttypes();
$conditions = array();
$viewhelper->setPosition(L("dictionary", "tpl"), "dict/");
$viewhelper->setTitle(L("dictionary", "tpl"));
if (isset($_GET['typeid'])) {
	$type_id = intval($_GET['typeid']);
	$conditions[] = "Dicts.dicttype_id='".$type_id."'";
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if ($do == "search") {
		if (!empty($_GET['q'])) {
			$conditions[] = "Dicts.word like '%".$_GET['q']."%'";
		}
	}
}
$amount = $dict->findCount(null, $conditions);
$page->setPagenav($amount);
$result = $dict->findAll("Dicts.*,dp.name AS typename", array("LEFT JOIN {$tb_prefix}dicttypes dp ON dp.id=Dicts.dicttype_id"), $conditions, "Dicts.id DESC", $page->firstcount, $page->displaypg);
if (!empty($result)) {
	setvar("Items", $result);
	setvar("ByPages",$page->pagenav);
}
render("dict.list");
?>