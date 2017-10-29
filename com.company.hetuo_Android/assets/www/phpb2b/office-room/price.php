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
require("room.share.php");
require(LIB_PATH. 'page.class.php');
uses("productprice","productcategory");
require(LIB_PATH .'time.class.php');
require(CACHE_PATH. "cache_typeoption.php");
$productprices = new Productprices();
$productcategory = new Productcategories();
$page = new Pages();
//check_permission("price");
$tpl_file = "price";
if (empty($companyinfo)) {
	flash("pls_complete_company_info", "company.php", 0);
}
if (isset($_POST['save'])) {
	$company->newCheckStatus($companyinfo['status']);
	if(!empty($_POST['price']['title'])){
		$vals = $_POST['price'];
		$vals['description'] = strip_tags(trim($vals['description']));
		$vals['member_id'] = $_SESSION['MemberID'];
		$vals['company_id'] = $company_id;
		if (!empty($_POST['product_id'])) {
			$vals['product_id'] = intval($_POST['product_id']);
			$pinfo = $pdb->GetRow("SELECT * FROM {$tb_prefix}products WHERE id='".$vals['product_id']."'");
			if (!empty($pinfo)) {
				if (!empty($pinfo['category_id'])) {
					$vals['category_id'] = $pinfo['category_id'];
				}
				if (!empty($pinfo['brand_id'])) {
					$vals['brand_id'] = $pinfo['brand_id'];
				}
			}
		}else{
			$pinfo = $pdb->GetRow("SELECT * FROM {$tb_prefix}products WHERE name='".$_POST['price']['title']."'");
			if (!empty($pinfo)) {
				$vals['product_id'] = $pinfo['id'];
				if (!empty($pinfo['category_id'])) {
					$vals['category_id'] = $pinfo['category_id'];
				}
				if (!empty($pinfo['brand_id'])) {
					$vals['brand_id'] = $pinfo['brand_id'];
				}
			}
		}
		if (!empty($_POST['data']['brand'])) {
			$brand_name = trim($_POST['data']['brand']);
			$binfo = $pdb->GetRow("SELECT * FROM {$tb_prefix}brands WHERE name='".$brand_name."'");
			if (!empty($binfo)) {
				$vals['brand_id'] = $binfo['id'];
			}
		}
		if(!empty($_POST['data']['area_id1'])){
			if($_POST['data']['area_id3']!=0){
				$vals['area_id'] = $_POST['data']['area_id3'];
			}elseif($_POST['data']['area_id3']==0&&$_POST['data']['area_id2']!=0){
				$vals['area_id'] = $_POST['data']['area_id2'];
			}else{
				$vals['area_id'] = $_POST['data']['area_id1'];
			}
		}
		if(isset($_POST['id'])){
			$id = intval($_POST['id']);
		}
		if (!empty($id)) {
			$vals['modified'] =$time_stamp;
			$result = $productprices->save($vals, "update", $id);
		}else{
			$vals['created'] = $vals['modified'] =$time_stamp;
			$result = $productprices->save($vals);
		}
		if ($result) {
			flash($message_info?$message_info:"success");
		}else {
			flash();
		}
	}

}
setvar("Measuries", $_PB_CACHE['measuring']);
setvar("Monetaries", $_PB_CACHE['monetary']);
setvar("PriceTypes", $_PB_CACHE['price_type']);
setvar("PriceTrends", $_PB_CACHE['price_trends']);
setvar("Productcategories", $productcategory->getTypeOptions());
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if(isset($_GET['productid'])){
		$product_id = intval($_GET['productid']);
	}
	if($do=="del" && !empty($id)) {
		$result = $productprices->del(intval($id), "member_id=".$_SESSION['MemberID']);
	}
	if ($do=="edit") {
		if (!empty($id)) {
			$price_info = $pdb->GetRow("SELECT id,product_id,type_id,brand_id,area_id,title,description,price,units,price_trends,currency,source,category_id FROM {$tb_prefix}productprices WHERE member_id=".$_SESSION['MemberID']." AND id={$id}");
			if(!empty($price_info['product_id'])){
			$price_info['productname'] = $pdb->GetOne("SELECT name FROM {$tb_prefix}products WHERE id=".$price_info['product_id']);
			$price_info['brand'] = $pdb->GetOne("SELECT name FROM {$tb_prefix}brands WHERE id=".$price_info['brand_id']);
			}
			$price_info['category'] = $pdb->GetOne("SELECT name FROM {$tb_prefix}productcategories WHERE id=".$price_info['category_id']);
		}elseif(!empty($product_id)){
			$pinfo = $pdb->GetRow("SELECT * FROM {$tb_prefix}products WHERE id=".$product_id);
			if(!empty($pinfo)){
			$price_info['title'] = $price_info['productname'] = $pinfo['name'];
			$price_info['product_id'] = $pinfo['id'];
			$price_info['category_id'] = $pinfo['category_id'];
			$brand_id = $pdb->GetOne("SELECT brand_id FROM {$tb_prefix}products WHERE id=".$product_id);
			$price_info['brand'] = $pdb->GetOne("SELECT name FROM {$tb_prefix}brands WHERE id=".$brand_id) ;
			$price_info['category'] = $pdb->GetOne("SELECT name FROM {$tb_prefix}productcategories WHERE id=".$price_info['category_id']);
			}
		}
		if(!empty($price_info)){
		setvar("item", $price_info);
		}
		$tpl_file = "price_edit";
		template($tpl_file);
		exit;
}
}
$conditions[] = "Productprice.member_id=".$_SESSION['MemberID'];
$amount = $productprices->findCount('', $conditions, "id");
$joins[] = "LEFT JOIN {$tb_prefix}products p ON Productprice.product_id=p.id";
$page->setPagenav($amount);
$result = $productprices->findAll("Productprice.id AS id,Productprice.type_id,Productprice.title AS title,Productprice.price AS price,Productprice.units AS units,Productprice.currency AS currency,p.name AS productname", $joins, $conditions, "Productprice.id DESC", $page->firstcount, $page->displaypg);
setvar("Items", $result);
setvar("ByPages", $page->pagenav);
setvar("Amount", $amount);
template($tpl_file);