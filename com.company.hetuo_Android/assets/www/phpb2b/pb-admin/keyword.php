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
uses("keyword");
require(PHPB2B_ROOT.'./libraries/page.class.php');
require("session_cp.inc.php");
include(CACHE_PATH. "cache_typeoption.php");
$keyword = new Keywords();
$conditions = null;
$tpl_file = "keyword";
$joins = array();
$page = new Pages();
setvar("Status", $_PB_CACHE['common_status']);
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if ($do == "edit") {
		if ($id) {
			setvar("item", $keyword->read("*", $id));
		}
		$tpl_file = "keyword.edit";
		template($tpl_file);
		exit;
	}
	if ($do == "search" && !empty($_GET['q'])) {
		$conditions[]= "Keyword.name like '%".trim($_GET['q'])."%'";
	}
	if ($do == "del" && !empty($id)) {
		$keyword->del($id);
	}
}
if (isset($_POST['del']) && !empty($_POST['id'])) {
	$keyword->del($_POST['id']);
}
if (isset($_POST['save']) && !empty($_POST['data']['keyword'])) {
	if (isset($_POST['id'])) {
		$id = intval($_POST['id']);
	}
	if ($id) {
		$keyword->save($_POST['data']['keyword'], "update", $id);
	}else{
		$keyword->save($_POST['data']['keyword']);
	}
}
$amount = $keyword->findCount(null, $conditions);
$page = new Pages();
$page->setPagenav($amount);
$result = $keyword->findAll("Keyword.id,Keyword.title,Keyword.hits,Keyword.status,Keyword.type_name", '', $conditions, "Keyword.id DESC ", $page->firstcount, $page->displaypg);
setvar("Items", $result);
setvar("ByPages", $page->getPagenav());
template($tpl_file);
?>