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
if(!defined('IN_PHPB2B')) {
	exit('Access Denied');
}
require("../share.inc.php");
uses("trade","industry","area","tradefield","form");
require(CACHE_PATH. 'cache_trusttype.php');
require(CACHE_PATH. 'cache_setting1.php');
require(CACHE_PATH. 'cache_membergroup.php');
$area = new Areas();
$offer = new Tradefields();
$trade = new Trades();
$trade_controller = new Trade();
$form = new Forms();
$industry = new Industries();
$conditions = array();
$industry_id = $area_id = 0;
$conditions[]= "t.status=1";
if (isset($_GET['navid'])) {
	setvar("nav_id", intval($_GET['navid']));
}
?>