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
require(PHPB2B_ROOT.'./libraries/page.class.php');
require("session_cp.inc.php");
require(LIB_PATH .'time.class.php');
require(CACHE_PATH. 'cache_type.php');
uses("product","attachment", "tag", "typeoption");
$typeoption = new Typeoption();
$attachment = new Attachment('pic');
$tag = new Tags();
$product = new Products();
$page = new Pages();
$conditions = array();
$tpl_file = "product";
setvar("CheckStatus", $typeoption->get_cache_type("common_status"));
setvar("BooleanVars", $typeoption->get_cache_type("common_option"));
setvar("ProductSorts",$_PB_CACHE['productsort']);
if (isset($_POST['save']) && !empty($_POST['data']['product']['name'])) {
	$result = false;
	$vals = array();
	$vals = $_POST['data']['product'];
	if (isset($_POST['data']['company_name'])) {
		if (!pb_strcomp($_POST['data']['company_name'], $_POST['company_name'])) {
			$vals['company_id'] = $pdb->GetOne("SELECT id FROM {$tb_prefix}companies WHERE name='".$_POST['data']['company_name']."'");
		}else{
			$vals['company_id'] = $pdb->GetOne("SELECT id FROM {$tb_prefix}companies WHERE name='".$_POST['company_name']."'");
		}
	}
	if (isset($_POST['data']['username'])) {
		if (!pb_strcomp($_POST['data']['username'], $_POST['username'])) {
			$vals['member_id'] = $pdb->GetOne("SELECT id FROM {$tb_prefix}members WHERE username='".$_POST['data']['username']."'");
		}else{
			$vals['member_id'] = $pdb->GetOne("SELECT id FROM {$tb_prefix}members WHERE username='".$_POST['username']."'");
		}
	}
	$attachment->rename_file = "product-".$time_stamp;
	if(isset($_POST['id'])){
		$id = intval($_POST['id']);
	}
	if(!empty($id)){
		$attachment->rename_file = "product-".$id;
	}
	if (!empty($vals['content'])) {
		$vals['content'] = stripcslashes($vals['content']);
	}
	if (!empty($_FILES['pic']['name'])) {
		$attachment->upload_process();
		$vals['picture'] = $attachment->file_full_url;
	}
	$vals['tag_ids'] = $tag->setTagId($_POST['data']['tag']);
	if(!empty($id)){
		$vals['modified'] = $time_stamp;
		$result = $product->save($vals, "update", $id);
	}else{
		$vals['created'] = $vals['modified'] = $time_stamp;
		$result = $product->save($vals);
	}
	if (!$result) {
		flash();
	}
}
if (isset($_POST['recommend'])) {
	foreach($_POST['id'] as $val){
		$commend_now = $product->field("ifcommend", "id=".$val);
		if($commend_now=="0"){
			$result = $product->saveField("ifcommend", "1", intval($val));
		}else{
			$result = $product->saveField("ifcommend", "0", intval($val));
		}
	}
	if ($result) {
		flash("success");
	}else{
		flash();
	}
}
if (isset($_POST['status'])) {
	if(!empty($_POST['id'])){
		$tmp_to = intval($_POST['status']);
		$result = $product->checkProducts($_POST['id'], $tmp_to);
	}
}
if(isset($_POST['pass'])){
	if(!empty($_POST['id'])){
		$result = $product->checkProducts($_POST['id'], '1');
	}
}
if(isset($_POST['forbid'])){
	if(!empty($_POST['id'])){
		$result = $product->checkProducts($_POST['id'], '0');
	}
}

if (isset($_POST['checkin_product']) && !empty($_POST['id'])) {
	$result = $product->checkProducts($_POST['id'], 1);
}

if (isset($_POST['checkout_product']) && !empty($_POST['id'])) {
	$result = $product->checkProducts($_POST['id'], 2);
}
if (isset($_POST['del'])) {
	$deleted = false;
    foreach ($_POST['id'] as $val) {
    	$picture = $product->field("picture", "id=".$val);
    	@unlink($media_paths['attachment_dir'].$picture);
    	@unlink($media_paths['attachment_dir'].$picture.".small.jpg");
    }
	if (is_array($_POST['id'])) {
		$deleted = $product->del($_POST['id']);
		if(!$deleted){
			flash();
		}
	}
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if($do == "edit"){
		if(!empty($id)){
			$res = $product->getInfo($id);
			if (!empty($res['picture'])) {
				$res['image'] = pb_get_attachmenturl($res['picture'], '../', 'small');
			}
			$tag->getTagsByIds($res['tag_ids'], true);
			$res['tag'] = $tag->tag;
			setvar("item",$res);
			unset($res);
		}
		$tpl_file = "product.edit";
		template($tpl_file);
		exit;
	}
	if ($do == 'search') {
		if(!empty($_GET['data']['username'])) {
			$member_id = $pdb->GetOne("SELECT id from {$tb_prefix}members where username=".$_GET['data']['username']);
			$conditions[]= "Product.member_id='".$member_id."'";
		}
		if(!empty($_GET['data']['companyname'])) {
			$conditions[]= "c.name like '%".$_GET['data']['companyname']."%'";
			$joins[] = "LEFT JOIN {$tb_prefix}companies c ON c.id=Product.company_id";
		}
		if($_GET['product']['status']!="-1") $conditions[]= "Product.status=".$_GET['product']['status'];
		if(!empty($_GET['data']['q'])) $conditions[]= "Product.name like '%".$_GET['data']['q']."%'";
		if($_GET['industryid']) $conditions[]= "Product.industry_id =".$_GET['industryid'];
		if($_GET['provinceid']) $conditions[]= "c.province_code_id =".$_GET['provinceid'];
		if ($_GET['FromDate'] && $_GET['FromDate']!="None" && $_GET['ToDate'] && $_GET['ToDate']!="None") {
			$condition= "Product.created BETWEEN ";
			$condition.= Times::dateConvert($_GET['FromDate']);
			$condition.= " AND ";
			$condition.= Times::dateConvert($_GET['ToDate']);
			$conditions[] = $condition;
		}
	}
}
$amount = $product->findCount(null, $conditions,"Product.id", null);
unset($joins);
$joins[] = "LEFT JOIN {$tb_prefix}companies c ON c.id=Product.company_id";
$page->setPagenav($amount);
$fields = "Product.id,Product.company_id AS CompanyID,c.id AS CID,c.name AS companyname,Product.name AS ProductName,Product.status AS ProductStatus,Product.created,Product.ifcommend as Ifcommend, Product.state as ProductState,Product.picture as ProductPicture ";
$result = $product->findAll($fields, $joins, $conditions,"Product.id DESC",$page->firstcount,$page->displaypg);
if (!empty($result)) {
	for($i=0; $i<count($result); $i++){
		$result[$i]['pubdate'] = date("Y-m-d", $result[$i]['created']);
		if(!empty($result[$i]['picture'])){
			$result[$i]['image'] = pb_get_attachmenturl($result[$i]['ProductPicture'], "../", "small");
		}
	}
}
setvar("Items", $result);
setvar("ByPages", $page->pagenav);
template($tpl_file);
?>