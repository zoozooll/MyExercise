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
require(PHPB2B_ROOT.'libraries/page.class.php');
uses("message");
$pms = new Messages();
$page = new Pages();
$fields = "id,title,content";
$conditions[] = "to_member_id=".$_SESSION['MemberID'];
if (isset($_GET['type'])) {
	$type = trim($_GET['type']);
	if (in_array($type, array("system", "user", "inquery"))) {
		$conditions[] = "type='".$type."'";
	}
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if ($do=="send") {
		$item = array();
		if (isset($_GET['to'])) {
			$item['to'] = $_GET['to'];
		}
		setvar("item", $item);
		template("pms_send");
		exit;
	}
	if($do == "view" && !empty($id)){
		$message_info = $pms->read("*", $id, null, $conditions);
		if(!$message_info || empty($message_info)){
			flash();
		}else{
			$pdb->Execute("UPDATE {$tb_prefix}messages SET status=1 WHERE to_member_id=".$_SESSION['MemberID']." AND id=".$id);
			$message_info['pubdate'] = date("Y-m-d", $message_info['created']);
			setvar("item",$message_info);
			$tpl_file = "pms_detail";
			template($tpl_file);
			exit;
		}
	}
}
if (isset($_POST['send']) && !empty($_POST['pms'])) {
	pb_submit_check('pms');
	$vals = array();
	$vals = $_POST['pms'];
	$vals['type'] = 'user';
	if (is_int($_POST['to'])) {
		$to_memberid = intval($_POST['to']);
		$member_info = $pdb->GetRow("SELECT id,username FROM {$tb_prefix}members WHERE id='".$to_memberid."'");
	}else{
		$member_info = $pdb->GetRow("SELECT id,username FROM {$tb_prefix}members WHERE username='".$_POST['to']."'");
	}
	if (!$member_info || empty($member_info) || $member_info['id']==$_SESSION['MemberID']) {
		flash();
	}
	$result = $pms->SendToUser($_SESSION['MemberName'], $member_info['username'], $vals);
	if (!$result) {
		flash();
	}
}

if (isset($_POST['del'])) {
	$result = $pms->del($_POST['id'],"to_member_id=".$_SESSION['MemberID']);
	if ($result) {
		pheader("location:pms.php");
	}else {
		flash();
	}
}
$tpl_file = "pms";
$page->displaypg = 15;
$amount = $pms->findCount(null, $conditions);
$page->setPagenav($amount);
$result = $pms->findAll("id,from_member_id,cache_from_username,title,content,status,created", null, $conditions, "id DESC", $page->firstcount, $page->displaypg);
setvar("MessageStatus", $pms->getReadStatus());
if (!empty($result)) {
	for($i=0; $i<count($result); $i++){
		$result[$i]['senddate'] = date("Y-m-d", $result[$i]['created']);
		switch ($result[$i]['type']) {
			case 'user':
				$result[$i]['typename'] = L("private_message", "tpl");
				break;
			case 'inquery':
				$result[$i]['typename'] = L("inquery_message", "tpl");
				break;
			default:
				$result[$i]['typename'] = L("system_message", "tpl");
				break;
		}
	}
	setvar("Items",$result);
}

setvar("ByPages",$page->pagenav);
template($tpl_file);
?>