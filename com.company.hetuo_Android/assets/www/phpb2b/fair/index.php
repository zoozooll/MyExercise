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
uses("expo");
$fair = new Expoes();
require(CACHE_PATH."cache_area.php");
require(CACHE_PATH."cache_type.php");
setvar("today_timestamp", mktime(0,0,0,date("m") ,date("d"),date("Y")));
setvar("Expotypes", $_PB_CACHE['expotype']);
setvar("Area", $_PB_CACHE['area']);
render("fair.index");
?>