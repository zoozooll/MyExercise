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
define('CURSCRIPT', 'add');
require("../libraries/common.inc.php");
require("../share.inc.php");
uses("market");
$market = new Markets();
if (isset($_POST['do']) && !empty($_POST['data']['market']['name'])) {
	pb_submit_check("data");
	$market->setParams();
	$result = $market->Add();
	if ($result) {
		flash('thanks_for_adding_market');
	}else {
		pheader("location:add.php");
	}
}
formhash();
$viewhelper->setPosition(L("added_market_info", "tpl"));
render("market.add");
?>