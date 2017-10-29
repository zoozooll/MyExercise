<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1285 $
 */
define('CURSCRIPT', 'logging');
require("libraries/common.inc.php");
require("share.inc.php");
require_once(LIB_PATH. "session_php.class.php");
require(LIB_PATH. "validation.class.php");
require(PHPB2B_ROOT. 'libraries/sendmail.inc.php');
require(LIB_PATH.'passport.class.php');
$session = new PbSessions();
uses("member","company","point");
$validate = new Validation();
$passport = new Passports();
$company = new Companies();
$point = new Points();
$member = new Members();
$referer = "";
capt_check("capt_logging");
if(isset($_POST['action']) && ($_POST['action']=="logging")){
	if(!empty($_POST['data']['login_name']) && !empty($_POST['data']['login_pass'])){
		unset($_SESSION['authnum_session']);
		$tmpUserName = $_POST['data']['login_name'];
		$tmpUserPass = $_POST['data']['login_pass'];
		$checked = $member->checkUserLogin($tmpUserName,$tmpUserPass);
		$tmp_memberinfo = array();
		if ($checked > 0) {
			$tmp_memberinfo = $member->info;
			$point->update("logging", $member->info['id']);
			if (!empty($_REQUEST['forward'])) {
				pheader("location:".$_REQUEST['forward']);
			}
			switch ($tmp_memberinfo['office_redirect']) {
				case 1:
					$goto_page = URL;
					break;
				case 2:
					$goto_page = "office-room/";
					break;
				case 3:
					$goto_page = "office-room/offer.php";
					break;
				case 4:
					$goto_page = "office-room/pms.php";
					break;
				default:
					$goto_page = URL."index.php";
					break;
			}
			pheader('location: '.$goto_page);
		}elseif ($checked == (-2) ) {
			$member->validationErrors[] = L('member_not_exists');
		}elseif ($checked == (-3)) {
			$member->validationErrors[] = L('login_pwd_false');
		}elseif ($checked == (-4)) {
			$member->validationErrors[] = L('member_checking');
		}else {
			$member->validationErrors[] = L('login_faild');
		}
		setvar("LoginError", $validate->show($member));
	}
}

function ua_referer($default = '') {
	global $referer;
	$indexname = URL."index.php";
	$default = empty($default) ? $indexname : '';
	$referer = pb_htmlspecialchar($referer);
	if(!preg_match("/(\.php|[a-z]+(\-\d+)+\.html)/", $referer) || strpos($referer, 'logging.php')) {
		$referer = $default;
	}
	return $referer;
}
if(isset($_GET['action']) && ($_GET['action'] == "logout")){
	$referer = null;
	$referer = ua_referer();
	session_destroy();
	uclearcookies();
	if (isset($_GET['fr'])) {
		if ($_GET['fr']=="cp") {
			usetcookie("admin", '');
		}
	}
	$member->logOut();
	$gopage = $referer;
	if (!empty($_GET['forward'])) {
		pheader("location:".$_GET['forward']);
	}else{
		pheader("location:".$gopage);
		exit;
	}
}
formhash();
render("logging");
?>