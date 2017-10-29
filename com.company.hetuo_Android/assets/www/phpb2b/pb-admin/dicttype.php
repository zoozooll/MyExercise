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
uses("dicttype");
require(LIB_PATH. 'page.class.php');
require("session_cp.inc.php");
$dicttype = new dicttypes();
$page = new Pages();
$tpl_file = "dicttype";
$conditions = array();
if (isset($_POST['del']) && is_array($_POST['id'])) {
	$deleted = $dicttype->del($_POST['id']);
	if (!$deleted) {
		flash();
	}
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if ($do =="search" && !empty($_GET['q'])) {
		$conditions[] = "name like '%".trim($_GET['q'])."%'";
	}
	if ($do == "del" && !empty($_GET['id'])) {
		$dicttype->del($_GET['id']);
	}
	if($do == "edit"){
		setvar("dicttypeOptions", $dicttype->getTypeOptions());
		if(isset($_GET['id'])){
			$id = intval($_GET['id']);
			$res= $dicttype->read("*",$id);
			setvar("item",$res);
		}
		$tpl_file = "dicttype.edit";
		template($tpl_file);
		exit;
	}
}
if (isset($_POST['save'])) {
	$vals = array();
	$vals = $_POST['data']['dicttype'];
	if (!empty($_POST['id'])) {
		$result = $dicttype->save($vals, "update", $_POST['id']);
	}elseif (!empty($vals['name'])){
		$names = explode("\r\n", $vals['name']);
		$tmp_name = array();
		if (!empty($names)) {
			foreach ($names as $val) {
				$name = $val;
				if(!empty($name)) $tmp_name[] = "('".$name."','".$vals['parent_id']."','".$vals['display_order']."')";
			}
			$values = implode(",", $tmp_name);
			$sql = "INSERT INTO {$tb_prefix}dicttypes (name,parent_id,display_order) VALUES ".$values;
			$result = $pdb->Execute($sql);
		}
	}
	if (!$result) {
		flash();
	}
}
$amount = $dicttype->findCount(null, $conditions,"id");
$page->setPagenav($amount);
setvar("Items", $dicttype->findAll("*", null, $conditions, "parent_id ASC,display_order ASC", $page->firstcount, $page->displaypg));
setvar("ByPages",$page->pagenav);
template($tpl_file);
?>