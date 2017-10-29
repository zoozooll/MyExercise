<?php 
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 126 $
 */
define('CURSCRIPT', 'index');
require("../libraries/common.inc.php");
require("../share.inc.php");
$viewhelper->setPosition(L("sub_special", "tpl"), "special/");
$viewhelper->setTitle(L("sub_special", "tpl"));
if (isset($_GET['type']) && in_array($_GET['type'], arraY("area", "industry"))) {
	switch ($_GET['type']) {
		case "area":
			setvar("special_name", L("sub_area", "tpl"));
			$viewhelper->setTitle(L("sub_area", "tpl"));
			break;
		case "industry":
			setvar("special_name", L("sub_industry", "tpl"));
			$viewhelper->setTitle(L("sub_industry", "tpl"));
			break;
		default:
			break;
	}
	$cache_id = $_GET['type'];
}
render("special.index");
?>