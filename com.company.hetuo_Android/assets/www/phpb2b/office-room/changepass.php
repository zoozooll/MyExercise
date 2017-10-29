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
require("room.share.php");
if (isset($_POST['do'])) {
	$do = trim($_POST['do']);
	if($do == "checkpasswd"){
		pb_submit_check('oldpass');
		$OldPassCheck = $member->checkUserLogin($_SESSION['MemberName'],$_POST['oldpass']);
		if ($OldPassCheck>0) {
			$vals = array();
			$vals['userpass'] = $member->authPasswd(trim($_POST['newpass']));
			if (!empty($_POST['question']) && !empty($_POST['answer'])) {
				$vals['question'] = $_POST['question'];
				$vals['answer'] = $_POST['answer'];
			}
			$result = $member->save($vals, "update", $_SESSION['MemberID']);
			flash("success");
		}else {
			flash('old_pwd_error');
		}
	}
}
template("changepass");
?>