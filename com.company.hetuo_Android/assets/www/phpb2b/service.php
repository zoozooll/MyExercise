<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 445 $
 */
define('CURSCRIPT', 'service');
require("libraries/common.inc.php");
require("share.inc.php");
require(CACHE_PATH. "cache_typeoption.php");
uses("service");
$service = new Services();
$answered_result = $service->findAll("id,title,created,revert_content,revert_date,type_id", null, "status=1 AND revert_content!=''", "id DESC", 0, 15);
$result = $service->findAll("id,title,created,revert_content,revert_date,type_id", null, "status=1", "id DESC", 0, 15);
setvar("AnsweredService", $service->formatResult($answered_result));
setvar("LatestService", $service->formatResult($result));
formhash();
setvar("ServiceTypes", $_PB_CACHE['service_type']);
render("service");
?>