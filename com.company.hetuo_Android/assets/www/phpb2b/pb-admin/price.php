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
uses("productprice","productcategory");
require(LIB_PATH. 'page.class.php');
require(LIB_PATH. 'cache.class.php');
require("session_cp.inc.php");
include(CACHE_PATH. "cache_typeoption.php");
$productprices = new Productprices();
$productcategory = new Productcategories();
$cache = new Caches();
$page = new Pages();
$conditions = array();
$tpl_file = "price";
setvar("Measuries", $_PB_CACHE['measuring']);
setvar("Monetaries", $_PB_CACHE['monetary']);
setvar("PriceTypes", $_PB_CACHE['price_type']);
setvar("PriceTrends", $_PB_CACHE['price_trends']);
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	$action = trim($_GET['action']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if ($do == "search") {
		if (isset($_GET['q'])) $conditions[]= "title like '%".trim($_GET['q'])."%'";
	}
	if ($do == "del" && !empty($id)) {
		$productprices->del($_GET['id']);
	}
	if ($do == "edit") {
		if(!empty($id)){
			$item_info = $productprices->getInfo($id);
		}
		if (!empty($item_info)) {
			setvar("item",$item_info);
		}
		setvar("Productcategories", $productcategory->getTypeOptions());
		$tpl_file = "price.edit";
		template($tpl_file);
		exit;
	}
}
if (isset($_POST['del']) && is_array($_POST['id'])) {
	$deleted = $productprices->del($_POST['id']);
	if (!$deleted) {
		flash();
	}
}
if (isset($_POST['save']) && !empty($_POST['data'])) {
	if (isset($_POST['id'])) {
		$id = intval($_POST['id']);
	}
	$vals = array();
	$vals = $_POST['data'];
		if (isset($_POST['price']['company_name'])) {
		if (!pb_strcomp($_POST['price']['company_name'], $_POST['company_name'])) {
			$vals['company_id'] = $pdb->GetOne("SELECT id FROM {$tb_prefix}companies WHERE name='".$_POST['price']['company_name']."'");
		}else{
			$vals['company_id'] = $pdb->GetOne("SELECT id FROM {$tb_prefix}companies WHERE name='".$_POST['company_name']."'");
		}
	}
	if (isset($_POST['price']['username'])) {
		if (!pb_strcomp($_POST['price']['username'], $_POST['username'])) {
			$vals['member_id'] = $pdb->GetOne("SELECT id FROM {$tb_prefix}members WHERE username='".$_POST['price']['username']."'");
		}else{
			$vals['member_id'] = $pdb->GetOne("SELECT id FROM {$tb_prefix}members WHERE username='".$_POST['username']."'");
		}
	}
		if (isset($_POST['price']['brand_name'])) {
		if (!pb_strcomp($_POST['price']['brand_name'], $_POST['brand_name'])) {
			$vals['brand_id'] = $pdb->GetOne("SELECT id FROM {$tb_prefix}brands WHERE name='".$_POST['price']['brand_name']."'");
		}else{
			$vals['brand_id'] = $pdb->GetOne("SELECT id FROM {$tb_prefix}brands WHERE name='".$_POST['brand_name']."'");
		}
	}
	if (isset($_POST['price']['product_name'])) {
		if (!pb_strcomp($_POST['price']['product_name'], $_POST['product_name'])) {
			$vals['product_id'] = $pdb->GetOne("SELECT id FROM {$tb_prefix}products WHERE name='".$_POST['price']['product_name']."'");
		}else{
			$vals['product_id'] = $pdb->GetOne("SELECT id FROM {$tb_prefix}products WHERE name='".$_POST['product_name']."'");
		}
	}
	if(!empty($_POST['price']['area_id1'])){
			if($_POST['price']['area_id3']!=0){
				$vals['area_id'] = $_POST['price']['area_id3'];
			}elseif($_POST['price']['area_id3']==0&&$_POST['price']['area_id2']!=0){
				$vals['area_id'] = $_POST['price']['area_id2'];
			}else{
				$vals['area_id'] = $_POST['price']['area_id1'];
			}
		}
	if(!empty($id)){
		$vals['modified'] = $time_stamp;
		$result = $productprices->save($vals, "update", $id);
	}else{
		$vals['created'] = $vals['modified'] = $time_stamp;
		$result = $productprices->save($vals);
	}
	if (!$result) {
		flash();
	}
}
$amount = $productprices->findCount(null, $conditions, null);
$joins[] = "LEFT JOIN {$tb_prefix}members m ON Productprice.member_id=m.id";
$page->setPagenav($amount);
$fields = "Productprice.id AS id,Productprice.title AS title,Productprice.units AS units, Productprice.price AS price,Productprice.currency AS currency,m.username AS name";
$result = $productprices->findAll($fields, $joins, $conditions,"Productprice.id DESC",$page->firstcount,$page->displaypg);
uaAssign(array("ByPages"=>$page->pagenav));
setvar("Items", $result);
template($tpl_file);