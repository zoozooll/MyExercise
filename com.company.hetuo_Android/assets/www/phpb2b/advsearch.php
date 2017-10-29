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
define('CURSCRIPT', 'advsearch');
require("libraries/common.inc.php");
require("share.inc.php");
$viewhelper->setPosition(L("adv_search", "tpl"));
render("advsearch");
?>