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
define('CURSCRIPT', 'friendlink');
require("libraries/common.inc.php");
require("share.inc.php");
uses("setting", "message", "friendlink");
$pms = new Messages();
$friendlink = new Friendlinks();
$setting = new Settings();
if (isset($_POST['do']) && !empty($_POST['friendlink'])) {
	pb_submit_check('friendlink');
	$data = $_POST['friendlink'];
	$result = false;
	$data['status'] = 0;
	$data['created'] = $data['modified'] = $time_stamp;
	$result = $friendlink->save($data);
	if ($result) {
		$pms->SendToAdmin('', array(
		"title"=>$data['title'].L("apply_friendlink"),
		"content"=>$data['title'].L("apply_friendlink")."\n".$_POST['data']['email']."\n".$data['description'],
		));
		$smarty->flash('wait_apply', URL);
	}
}
$viewhelper->setPosition(L("apply_friendlink", "tpl"));
formhash();
render("friendlink");
?>