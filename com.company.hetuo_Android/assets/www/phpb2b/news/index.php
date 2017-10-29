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
require(LIB_PATH .'time.class.php');
require(CACHE_PATH. "cache_type.php");
uses("news","newstype");
$news = new Newses();
$newstype = new Newstypes();
//setvar("Newstypes", $_PB_CACHE['newstype']);
render("news.index");
?>