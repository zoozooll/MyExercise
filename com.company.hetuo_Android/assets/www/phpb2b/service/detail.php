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
define('CURSCRIPT', 'detail');
require("../libraries/common.inc.php");
require("../share.inc.php");
require(CACHE_PATH. "cache_typeoption.php");
uses("service");
$service = new Services();
$viewhelper->setTitle(L("customer_service_center", "tpl"));
$viewhelper->setPosition(L("customer_service_center", "tpl"), "service.php");
if (isset($_GET['id'])) {
	$id = intval($_GET['id']);
	$result = $service->findById($id);
	if (!empty($result)) {
		$result['revertdate'] = date("Y-m-d H:i", $result['revert_date']);
		$viewhelper->setPosition($_PB_CACHE['service_type'][$result['type_id']], "service/list.php?typeid=".$result['type_id']);
		$viewhelper->setTitle($result['title']);
		$viewhelper->setPosition($result['title']);
		setvar("item", $result);
		render("service.detail", true);
	}
}
render("service");
?>