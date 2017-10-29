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
require("../libraries/common.inc.php");
require("room.share.php");
uses("templet");
check_permission("space");
$templet = new Templets();
if (empty($companyinfo)) {
	flash("pls_complete_company_info", "company.php", 0);
}
if (isset($_POST['updateSpaceName']) && !empty($_POST['data']['space_name'])) {
	$space_name = trim($_POST['data']['space_name']);
	$space_name = L10n::translateSpaceName($space_name);
	$result = $member->updateSpaceName(array("id"=>$_SESSION['MemberID']), $space_name);
	if ($result) {
		flash("success");
	}
}
if (isset($_POST['save']) && !empty($_POST['data']['member']['styleid'])) {
	$templet_id = intval($_POST['data']['member']['styleid']);
	$pdb->Execute("UPDATE {$tb_prefix}members SET templet_id=".$templet_id." WHERE id=".$_SESSION['MemberID']);
	flash("success");
}
setvar("templet_id", $memberinfo['templet_id']);
$result = $templet->getInstalled($memberinfo['membergroup_id'], $memberinfo['membertype_id']);
setvar("Items", $result);
template("space");
?>