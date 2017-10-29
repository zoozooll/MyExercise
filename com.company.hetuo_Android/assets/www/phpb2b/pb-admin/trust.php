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
require("../libraries/common.inc.php");
require("session_cp.inc.php");
uses("setting");
require(LIB_PATH. "cache.class.php");
$cache = new Caches();
$setting = new Settings();
$conditions = null;
$tpl_file = "trust";
$item = $setting->getValues(1);
if (isset($_POST['save_trust_rule'])) {
	$cache->writeCache("setting1", "setting1");
	$setting->replace($_POST['data']['setting1'], 1);
	flash("success");
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if ($do == "rule") {
		$tpl_file = "trust.rule";
		setvar("item", $item);
		template($tpl_file, 1);
	}
}
if (isset($_POST['del']) && !empty($_POST['id'])) {
	$result = $setting->del($_POST['id'], null, "trusttypes");
	if (!$result) {
		flash();
	}else{
		$cache->writeCache("trusttype","trusttype");
	}
}
if (isset($_POST['update'])) {
	if (!empty($_POST['tid'])) {
		$type_count = count($_POST['tid']);
		for($i=0; $i<$type_count; $i++){
			if (!empty($_POST['name'][$i])) {
				$pdb->Execute("REPLACE INTO {$tb_prefix}trusttypes (id,name,image) VALUES ('".$_POST['tid'][$i]."','".$_POST['name'][$i]."','".$_POST['avatar'][$i]."')");
			}
		}
		$name_count = count($_POST['name']);
		if ($name_count<=$type_count) {
			break;
		}else{
			for($j=$type_count; $j<$name_count; $j++){
				if (!empty($_POST['name'][$j])) {
					$pdb->Execute("INSERT INTO {$tb_prefix}trusttypes (name,image) values ('".$_POST['name'][$j]."','".$_POST['avatar'][$j]."')");
				}
			}
		}
		$cache->writeCache("trusttype", "trusttype");
		flash("success");;
	}
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if ($do == "del" && !empty($_GET['id'])){
		$result = $setting->del($_GET['id'], null, "trusttypes");
		if (!$result) {
			flash();
		}else{
			$cache->writeCache("trusttype","trusttype");
		}
	}
}
$sql = "SELECT *,image AS avatar FROM {$tb_prefix}trusttypes";
$result = $pdb->GetArray($sql);
if (empty($result) && file_exists($cache_file = CACHE_PATH. "cache_trusttype.php")) {
	require($cache_file);
	foreach ($_PB_CACHE['trusttype'] as $key=>$val) {
		$_PB_CACHE['trusttype'][$key]['id'] = $key;
	}
	$result = $_PB_CACHE['trusttype'];
}
setvar("Items",$result);
template($tpl_file);
?>