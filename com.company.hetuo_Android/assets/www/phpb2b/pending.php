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
define('CURSCRIPT', 'pending');
require("libraries/common.inc.php");
require("share.inc.php");
uses("member");
$member = new Members();
$hash = trim($_GET['hash']);
if (empty($hash)) {
	flash("invalid_request", null, 0);
}
$validate_str = rawurldecode(authcode($hash, "DECODE"));
if (empty($validate_str)) {
	flash("invalid_request", null, 0);
}
formhash();
if (!empty($validate_str)) {
	list($tmp_username, $exp_time) = explode("\t", $validate_str);
    if ($exp_time<$time_stamp) {
    	flash("auth_expired", null, 0);
    }
    $user_exists = $member->checkUserExist($tmp_username, true);
    if ($user_exists && isset($_GET['action'])) {
    	switch ($_GET['action']) {
    		case "activate":
    			$result = $member->updateUserStatus($member->info['id']);
    			if ($result) {
    				flash("actived_and_login", "logging.php");
    			}
    			break;
    		case "getpasswd":
    			setvar("username", $member->info['username']);
    			$viewhelper->setPosition(L("reset_your_password", "tpl"));
    			render("getpasswd.reset");
    			break;
    		default:
    			break;
    	}
    }else{
        flash("member_not_exists", null, 0);
    }
}else{
	flash("invalid_request", null, 0);
}
?>