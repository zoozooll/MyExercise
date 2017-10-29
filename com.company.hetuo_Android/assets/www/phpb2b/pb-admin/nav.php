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
require("session_cp.inc.php");
uses("nav");
require(LIB_PATH. "cache.class.php");
$cache = new Caches();
$nav = new Navs();
$conditions = null;
$tpl_file = "nav";
if (isset($_POST['del']) && is_array($_POST['id'])) {
	$deleted = $nav->del($_POST['id']);
	if (!$deleted) {
		flash();
	}
	$cache->writeCache("setting", "setting");
}
if (isset($_POST['update_prior'])) {
	if (!empty($_POST['nid'])) {
		for ($i=0; $i<count($_POST['nid']); $i++){
			$pdb->Execute("UPDATE {$tb_prefix}navs SET display_order='".$_POST['display_order'][$i]."' WHERE id='".$_POST['nid'][$i]."'");
		}
		$cache->writeCache("setting", "setting");
	}
}
if (isset($_POST['save'])) {
	$vals = array();
	$vals = $_POST['data']['nav'];
	if (!empty($_POST['id'])) {
		$vals['modified'] = $time_stamp;
		$result = $nav->save($vals, "update", $_POST['id']);
	}else{
		$vals['created'] = $vals['modified'] = $time_stamp;
		$result = $nav->save($vals);
	}
	if (!$result) {
		flash();
	}
	$cache->writeCache("setting", "setting");
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if ($do == "edit") {
		if(!empty($id)){
			$res= $nav->read("*",$id);
			setvar("item",$res);
		}
		$tpl_file = "nav.edit";
		template($tpl_file);
		exit;
	}
}
$result = $nav->findAll("*", null, $conditions, "display_order ASC,id ASC");
if (!empty($result)) {
	for($i=0; $i<count($result); $i++){
		$result[$i]['title'] = "<a".parse_highlight($result[$i]['highlight']).">".$result[$i]['name']."</a>";
	}
}
setvar("Items", $result);
template($tpl_file);
?>