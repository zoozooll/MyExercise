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
session_cache_limiter('nocache');
require("../libraries/common.inc.php");
require("session_cp.inc.php");
include(CACHE_PATH. "cache_type.php");
uses("friendlink", "industry", "typeoption");
$link = new Friendlinks();
$industry = new Industries();
$typeoption = new Typeoption();
$conditions = null;
$tpl_file = "friendlink";
setvar("AskAction", $typeoption->get_cache_type("common_option"));
if (!empty($_PB_CACHE['friendlinktype'])) {
	setvar("FriendlinkTypes", $_PB_CACHE['friendlinktype']);
}
if (isset($_POST['save']) && !empty($_POST['data']['friendlink']['title'])) {
	$vals = array();
	$vals = $_POST['data']['friendlink'];
	$vals['industry_id'] = $industry->getMinalId($_POST['data']['industry_id1'], $_POST['data']['industry_id2'], $_POST['data']['industry_id3']);
	$vals['area_id'] = $industry->getMinalId($_POST['data']['area_id1'], $_POST['data']['area_id2'], $_POST['data']['area_id3']);
	if(isset($_POST['id'])){
		$id = intval($_POST['id']);
	}
	if (!preg_match("/^(http|ftp):/", $_POST['data']['friendlink']['url'])) {
		$vals['url'] = 'http://'.$_POST['data']['friendlink']['url'];
	}
	if (!empty($id)) {
		$vals['modified'] = $time_stamp;
		$updated = $link->save($vals, "update", $id);
	} else {
		$vals['created'] = $vals['modified'] = $time_stamp;
		$updated = $link->save($vals);
	}
	if (!$updated) {
		flash();
	}
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if ($do == "del" && !empty($id)) {
		$result = $link->del($id);
	}
	if ($do == "edit") {
		$tpl_file = "friendlink.edit";
		if(!empty($id)){
			$fields = "*";
			$link_info = $link->read($fields,$id);
			setvar("item",$link_info);
		}
		template($tpl_file);
		exit;
	}
}
if(isset($_POST['del']) && !empty($_POST['id'])){
	$result = $link->del($_POST['id']);
	if(!$result){
		flash();
	}
}
$fields = "*";
setvar("Items",$link->findAll($fields, null, $conditions, "priority ASC,id DESC"));
template($tpl_file);
?>