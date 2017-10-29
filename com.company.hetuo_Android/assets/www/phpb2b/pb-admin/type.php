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
require(PHPB2B_ROOT.'libraries/page.class.php');
require("session_cp.inc.php");
require(LIB_PATH. "cache.class.php");
$cache = new Caches();
$page = new Pages();
$tpl_file = "type";
$conditions = array();
$condition = null;
if (isset($_POST['do'])) {
	if (isset($_POST['id'])) {
		$id = intval($_POST['id']);
	}
	$vals = $_POST['data']['typeoption'];
	if (!empty($id)) {
		$result = $pdb->Execute("UPDATE {$tb_prefix}typeoptions SET option_label='".$vals['option_label']."',option_value='".$vals['option_value']."',typemodel_id='".$vals['typemodel_id']."' WHERE id={$id}");
	}else{
		$result = $pdb->Execute("INSERT INTO {$tb_prefix}typeoptions (option_label,option_value,typemodel_id) VALUE ( '".$vals['option_label']."','".$vals['option_value']."','".$vals['typemodel_id']."')");
	}
	flash("success");
}
if (isset($_POST['update']) && !empty($_POST['option_label'])) {
	for($i=0; $i<count($_POST['optid']); $i++){
		$pdb->Execute("UPDATE {$tb_prefix}typeoptions SET option_label='".$_POST['option_label'][$i]."',option_value='".$_POST['option_value'][$i]."' WHERE id='".$_POST['optid'][$i]."'");
	}
	flash("success");
}
if (isset($_GET['modelid'])) {
	$conditions[] = "typemodel_id='".$_GET['modelid']."'";
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (isset($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if ($do =="del" && !empty($id)) {
		$pdb->Execute("DELETE FROM {$tb_prefix}typeoptions WHERE id={$id}");
	}
	if ($do == "refresh") {
		$cache->updateTypevars();
		flash("success", 'type.php');
	}
	if ($do == "edit") {
		$tmp_models = $pdb->GetArray("SELECT * FROM {$tb_prefix}typemodels");
		if (!empty($tmp_models)) {
			foreach ($tmp_models as $key=>$val) {
				$type_models[$val['id']] = $val['title'];
			}
			setvar("TypeModels", $type_models);
		}
		if(!empty($id)){
			$tmp_item = $pdb->GetRow("SELECT tp.*,tm.title AS modelname FROM {$tb_prefix}typeoptions tp LEFT JOIN {$tb_prefix}typemodels tm ON tp.typemodel_id=tm.id WHERE tp.id={$id}");
			if (!empty($tmp_item)) {
				setvar("item", $tmp_item);
			}
		}
		$tpl_file = "type.edit";
	}
}
if (!empty($conditions) && is_array($conditions)) {
	$condition = " WHERE ".implode(" AND ", $conditions);
}
$amount = $pdb->GetOne("SELECT count(id) FROM ".$tb_prefix."typeoptions{$condition}");
$page->setPagenav($amount);
$result = $pdb->GetArray("SELECT tp.*,tm.title AS typename,tm.title FROM {$tb_prefix}typeoptions tp LEFT JOIN {$tb_prefix}typemodels tm ON tp.typemodel_id=tm.id {$condition} ORDER BY tm.id DESC,tp.id DESC LIMIT {$page->firstcount},{$page->displaypg}");
setvar("Items", $result);
setvar("ByPages", $page->pagenav);
template($tpl_file);
?>