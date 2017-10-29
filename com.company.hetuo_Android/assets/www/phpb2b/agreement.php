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
define('CURSCRIPT', 'agreement');
require("libraries/common.inc.php");
require("share.inc.php");
include(CACHE_PATH. "cache_setting1.php");
if (empty($_PB_CACHE['setting1']['agreement']) && file_exists($_PB_CACHE['setting1']['agreement'])) {
	$content = file_get_contents(CACHE_PATH. "cache_agreement.php");
}else{
	$content = $_PB_CACHE['setting1']['agreement'];
}
$viewhelper->setPosition(L("agreement", "tpl"));
setvar("content", $content);
render("agreement");
?>