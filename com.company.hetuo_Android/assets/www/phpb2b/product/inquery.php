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
define('CURSCRIPT', 'query');
require("../libraries/common.inc.php");
require("../share.inc.php");
uses("product","member","message","typeoption");
$pms = new Messages();
$typeoption = new Typeoption();
$member = new Members();
$product = new Products();
if (isset($_POST['id']) && !empty($_POST['do'])) {
	pb_submit_check('inquery');
	$vals['type'] = 'inquery';
	$vals['title'] = $_POST['title'];
	$vals['content'] = implode("<br />", $_POST['inquery']);
	$result = $pms->SendToUser($pb_userinfo['pb_username'], $pdb->GetOne("SELECT username FROM {$tb_prefix}members WHERE id=".intval($_POST['to_member_id'])), $vals);
	if(!$result){
		flash("failed", '', 0);
	}else{
		flash("success", '', 0);
	}
}
$pid = intval($_GET['id']);
$sql = "SELECT * FROM {$tb_prefix}products WHERE id=".$pid;
$res = $pdb->GetRow($sql);
if (empty($res) || !$res) {
	flash('data_not_exists', 'product/', 0);
}else {
	if (!empty($res['picture'])) {
		$res['imgsmall'] = "attachment/".$res['picture'].".small.jpg";
		$res['imgbig'] = "attachment/".$res['picture'];
		$res['image'] = "attachment/".$res['picture'].".middle.jpg";
	}else{
		$res['image'] = pb_get_attachmenturl('', '', 'middle');
	}
	setvar("ImTypes", $typeoption->get_cache_type("im_type"));
	setvar("TelTypes", $typeoption->get_cache_type("phone_type"));
	setvar("item",$res);
}

$viewhelper->setTitle($res['name']);
$member_info = $pdb->GetRow("SELECT mf.first_name,mf.last_name,m.email as MemberEmail FROM {$tb_prefix}members m LEFT JOIN {$tb_prefix}memberfields mf ON mf.member_id=m.id WHERE m.id=".$res['member_id']);
formhash();
setvar("CompanyUser",$member_info['first_name'].$member_info['last_name']);
render("product.inquery");
?>