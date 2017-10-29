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
require(LIB_PATH .'page.class.php');
require("session_cp.inc.php");
uses("goods");
$goods = new Goods();
$tpl_file = "goods";
$page = new Pages();
if (isset($_POST['save'])) {
	$vals = $_POST['goods'];
	$id = $_POST['id'];
	if (!empty($id)) {
		$vals['modified'] = $time_stamp;
		$result = $goods->save($vals, "update", $id);
	}else{
		$vals['created'] = $vals['modified'] = $time_stamp;
		$result = $goods->save($vals);
	}
	if (!$result) {
		flash();
	}
}
if (isset($_POST['del']) && !empty($_POST['id'])) {
	$result = $goods->del($_POST['id']);
}
if (isset($_GET['do'])){
	$do = trim($_GET['do']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if($do=="del" && !empty($id)) {
		$result = $goods->del($_GET['id'])	;
	}
	if ($do == "edit") {
		if (!empty($id)) {
			$result = $goods->read("*", $id);
			setvar("item",$result);
		}
		$tpl_file = "goods.edit";
		template($tpl_file);
		exit;
	}
}
$amount = $goods->findCount();
$page->setPagenav($amount);
$result = $goods->findAll("*", null, $conditions, "id desc", $page->firstcount, $page->displaypg);
setvar("Items",$result);
setvar("ByPages",$page->pagenav);
template($tpl_file);
?>