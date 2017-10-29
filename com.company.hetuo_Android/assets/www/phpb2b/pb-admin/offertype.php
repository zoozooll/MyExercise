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
uses("tradetype");
require(LIB_PATH. "cache.class.php");
require(LIB_PATH. "file.class.php");
$cache = new Caches();
$file = new Files();
$conditions = null;
$tradetype = new Tradetypes();
$tpl_file = "offertype";
if (isset($_POST['del']) && !empty($_POST['id'])) {
	$result = $tradetype->del($_POST['id']);
	if (!$result) {
		flash();
	}else{
		$cache->updateTypes();
	}
}
if (isset($_POST['update'])) {
	if (!empty($_POST['tid'])) {
		$type_count = count($_POST['tid']);
		for($i=0; $i<$type_count; $i++){
			if (!empty($_POST['name'][$i])) {
				$pdb->Execute("UPDATE {$tb_prefix}tradetypes SET name='".$_POST['name'][$i]."',display_order='".$_POST['display_order'][$i]."',id=".$_POST['tid'][$i]." WHERE id=".$_POST['tid'][$i]);
			}
		}
		$name_count = count($_POST['name']);
		if ($name_count<=$type_count) {
			break;
		}else{
			for($j=$type_count; $j<$name_count; $j++){
				if (!empty($_POST['name'][$j])) {
					$pdb->Execute("INSERT INTO {$tb_prefix}tradetypes (name) values ('".$_POST['name'][$j]."')");
				}
			}
		}
		$cache->updateTypes();
		flash("success");;
	}
}
if(isset($_POST['save'])){
	$id = $_POST['id'];
	$vals = $_POST['data']['tradetype'];
	if(!empty($id)){
		$result = $tradetype->save($vals, "update", $id);
	}else{
		$result = $tradetype->save($vals);
	}
	if(!$result){
		flash();
	}else{
		$cache->updateTypes();
	}
}
$fileststus = $file->safe_glob("../templates/default/offer.list*.html");
setvar("OfferListTemplates", $fileststus);
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if ($do == "edit") {
		$tpl_file = "tradetype.edit";
		if (!empty($id)) {
			$result = $tradetype->read("*",$id);
			setvar("item",$result);
		}
	}
	if ($do == "del" && !empty($_GET['id'])){
		$result = $tradetype->del($_GET['id']);
		if (!$result) {
			flash();
		}else{
			$cache->updateTypes();
		}
	}
}
$sql = "SELECT * FROM {$tb_prefix}tradetypes";
$result = $pdb->GetArray($sql);
setvar("Items",$result);
template($tpl_file);
?>