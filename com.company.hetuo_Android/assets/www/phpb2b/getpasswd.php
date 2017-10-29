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
define('CURSCRIPT', 'getpasswd');
require("libraries/common.inc.php");
require("share.inc.php");
require("libraries/sendmail.inc.php");
uses("member");
$member = new Members();
if (isset($_POST['do'])) {
	pb_submit_check("data");
	$do = trim($_POST['do']);
	if ($do == "reset") {
		$username = trim($_POST['data']['username']);
		$userpass = trim($_POST['data']['password1']);
		if (!empty($userpass) && !empty($username)) {
			$user_exists = $member->checkUserExist($username, true);
			if (!$user_exists) {
				flash("member_not_exists");
			}else{
				$result = $pdb->Execute("UPDATE {$tb_prefix}members SET userpass='".$member->authPasswd($userpass)."' WHERE id=".$member->info['id']." AND status='1'");
				if ($result) {
					flash("reset_and_login", "logging.php");
				}
			}
		}
	}
}
if (isset($_POST['action'])) {
	pb_submit_check("data");
	$checked = true;
	$login_name = trim($_POST['data']['username']);
	$user_email = trim($_POST['data']['email']);
	if(!pb_check_email($user_email)){
		setvar("ERRORS", L("wrong_email_format"));
		$checked = false;
	}else{
		$member->setInfoByUserName($login_name);
		$member_info = $member->getInfo();
		if(!$member_info || empty($member_info)){
			setvar("ERRORS", L('member_not_exists'));
			setvar("postLoginName", $login_name);
			setvar("postUserEmail", $user_email);
			$checked = false;
		}elseif (!pb_strcomp($user_email, $member_info['email'])){
			setvar("ERRORS", L("please_input_email"));
			$checked = false;
		}
		if(!pb_check_email($member_info['email'])){
			$checked = false;
		}
		if ($checked) {
			$exp_time = $time_stamp + 86400;
			$hash = authcode(addslashes($member_info['username'])."\t".$exp_time,"ENCODE");
			setvar("hash", rawurlencode($hash));
			setvar("expire_date", date("Y-m-d",strtotime("+1 day")));
			$sended = pb_sendmail(array($member_info['email'], $login_name), L("pls_reset_passwd"), "getpasswd");
			if(!$sended)
			{
				flash("email_send_false");
			}else{
				flash("getpasswd_email_sended");
			}
		}
	}
}
$viewhelper->setPosition(L("get_password", "tpl"));
formhash();
render("getpasswd");
?>