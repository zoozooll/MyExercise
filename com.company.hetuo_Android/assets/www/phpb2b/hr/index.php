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
$tpl_file = "hr.index";
include(CACHE_PATH. "cache_area.php");
uses("industry", "job", "typeoption");
$industry = new Industries();
$job = new Jobs();
$typeoption = new Typeoption();
$conditions = array();
setvar("Areas", $_PB_CACHE['area']);
/**types**/
$job->findIt("jobtypes");
if (!empty($job->params['data'])) {
	setvar("Items", $job->params['data'][1]);
}
if (!empty($job->params['data'])) {
	setvar("IndustryList", $job->params['data'][1]);
}else{
	setvar("IndustryList", $industry->getCacheIndustry());
}
/**types,not sim, but name->title**/
setvar("Salary", $typeoption->get_cache_type("salary"));
render($tpl_file);
?>