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
require(CACHE_PATH. "cache_setting1.php");
uses("service");
setvar("item", $_PB_CACHE['setting1']);
$service = new Services();
$viewhelper->setTitle(L("customer_service_center", "tpl"));
$viewhelper->setPosition(L("customer_service_center", "tpl"), "service.php");
$viewhelper->setTitle(L("service_client", "tpl"));
$viewhelper->setPosition(L("service_client", "tpl"));
render("service.client");
?>