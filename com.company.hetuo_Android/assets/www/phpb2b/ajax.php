<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 121 $
 */
define('CURSCRIPT', 'ajax');
define('NOROBOT', TRUE);
require_once 'libraries/common.inc.php';
require("share.inc.php");
require_once 'libraries/json_config.php';
$return = array();
$result = array();
$post_actions = array("checkpasswd");
$get_actions = array("checkusername");
uses("member", "company");
$member = new Members();
$company = new Companies();
if (isset($_GET['action'])) {
	$action = trim($_GET['action']);
	switch ($action) {
		case "checkusername":
			if(isset($_GET['username'])) {
				$result = call_user_func_array($action, array($_GET['username']));		
				if($result == true){
					$return['isError'] = 1;
				}else{
					$return['isError'] = 0;
				}
			}
			ajax_exit($return);
			break;
		case "addtag":
			break;
		case "checkemail":
			if(isset($_GET['email'])) {
				$result = call_user_func_array($action, array($_GET['email']));		
				if($result == true){
					$return['isError'] = 1;
				}else{
					$return['isError'] = 0;
				}
			}
			ajax_exit($return);
			break;
		default:
			break;
	}
}

if (isset($_POST['action'])) {
	$action = trim($_POST['action']);
	switch ($action) {
		case "checkpasswd":
			if(isset($_POST['oldpass'])) {
				$result = call_user_func_array($action, array($_POST['oldpass'], $pb_userinfo['pb_userid']));		
				if($result){
					$return['isError'] = 0;
					$return['oldpass'] = $_POST['oldpass'];
				}else{
					$return['isError'] = 1;
				}
			}
			ajax_exit($return);
			break;
		default:
			break;
	}
}

function checkpasswd($input_passwd, $member_id)
{
	global $member;
	return $member->checkUserPasswdById($input_passwd, $member_id);
}

function checkusername($input_username)
{
	global $member;
	return $member->checkUserExist($input_username, false);
}

function checkemail($email)
{
	global $member;
	return $member->checkUserExistsByEmail($email);
}

function checkcompanyname($company_name)
{
	global $company;
	return $company->checkNameExists($company_name);
}
?>