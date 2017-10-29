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
require(CACHE_PATH. "cache_typeoption.php");
require(PHPB2B_ROOT.'libraries/page.class.php');
uses("service");
$page = new Pages();
$page->displaypg = 15;
$service = new Services();
$conditions[] = "status=1";
$viewhelper->setPosition(L("customer_service_center", "tpl"), "service.php");
$viewhelper->setTitle(L("customer_service_center", "tpl"));
if (isset($_GET['typeid'])) {
	$type_id = intval($_GET['typeid']);
	$conditions[] = "type_id=".$type_id;
	setvar("TypeName", $_PB_CACHE['service_type'][$type_id]);
	$viewhelper->setPosition($_PB_CACHE['service_type'][$type_id]);
	$viewhelper->setTitle($_PB_CACHE['service_type'][$type_id]);
}
$amount = $service->findCount(null, $conditions,"id");
$page->setPagenav($amount);
$result = $service->findAll("id,title,created,revert_content,revert_date,type_id", null, $conditions, "id DESC", $page->firstcount, $page->displaypg);
setvar("Items", $service->formatResult($result));
setvar("ServiceTypes", $_PB_CACHE['service_type']);
uaAssign(array("ByPages"=>$page->pagenav));
render("service.list");
?>