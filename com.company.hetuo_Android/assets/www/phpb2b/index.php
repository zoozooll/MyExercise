<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 326 $
 */
define('CURSCRIPT', 'index');
require("libraries/common.inc.php");
require("share.inc.php");
require(CACHE_PATH. "cache_setting1.php");
if (!empty($_PB_CACHE['setting']['redirect_url'])) {
	if(isset($_SERVER['REQUEST_URI']) && !strstr($_SERVER['REQUEST_URI'], ".php")){;
		$url = $_PB_CACHE['setting']['redirect_url'];
		header("HTTP/1.1 301 Moved Permanently");
		header("Location:$url");
	}
}
$viewhelper->Start();
uses("industry");
$industry = new Industries();
setvar("IndustryList", $industry->getCacheIndustry());
$viewhelper->setMetaDescription($_PB_CACHE['setting1']['site_description']);
formhash();
render("index");
?>