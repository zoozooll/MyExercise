<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1393 $
 */
require("../libraries/common.inc.php");
require(PHPB2B_ROOT.'languages'.DS.$app_lang.DS.'template.admin.inc.php');
require(DATA_PATH.'phpb2b_version.php');
if (session_id() == '' ) { 
	require_once(LIB_PATH. "session_php.class.php");
	$session = new PbSessions(DATA_PATH."tmp");
}
uses("adminfield","setting", "member");
$adminer = new Adminfields();
$member = new Members();
$setting = new Settings();
if (isset($_GET['action'])) {
	if ($_GET['action']=="dereg") {
		usetcookie("admin", "");
		unset($_SESSION['last_adminer_time']);
	}
}
capt_check("capt_login_admin");
if (isset($_POST['do'])) {
	$do = trim($_POST['do']);
	if ($do == "login") {
		pb_submit_check('data');
	    if (!empty($_POST['data']['username']) && (!empty($_POST['data']['userpass']))) {
	    	$checked = false;
	    	$uname = $_POST['data']['username'];
	    	$upass = $_POST['data']['userpass'];
	    	$checked = $adminer->checkUserLogin($uname,$upass);
	    	if($checked > 0){
	    		pheader("Location:index.php");
	    	}else{
	    		setvar("LoginError",$adminer->error);
	    	}
	    }
	}
}
formhash();
$smarty->template_dir = "template/";
$smarty->setCompileDir("pb-admin".DS);
if (!empty($arrTemplate)) {
    $smarty->assign($arrTemplate);
}
template("login");
exit;
?>