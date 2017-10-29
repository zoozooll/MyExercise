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
define('CURSCRIPT', 'content');
require("../libraries/common.inc.php");
require("../share.inc.php");
include(CACHE_PATH."cache_industry.php");
include(CACHE_PATH."cache_area.php");
uses("product","company","member","form");
$company = new Companies();
$member = new Members();
$product = new Products();
$form = new Forms();
$tmp_status = explode(",",L('product_status', 'tpl'));
$viewhelper->setPosition(L("product_center", 'tpl'), 'product/');
$viewhelper->setTitle(L("product_center", 'tpl'));
if (isset($_GET['title'])) {
	$title = rawurldecode(trim($_GET['title']));
	$res = $product->findByName($title);
	$id = $res['id'];
}
if (isset($_GET['id'])) {
	$id = intval($_GET['id']);
}
$info = $product->getProductById($id);
if(empty($info) || !$info){
	flash("data_not_exists", '', 0);
}
if (isset($info['formattribute_ids'])) {
	$form_vars = $form->getAttributes(explode(",", $info['formattribute_ids']));
	setvar("ObjectParams", $form_vars);
}
if ($info['state']!=1) {
	flash("unvalid_product", '', 0);
}
if($info['status']!=1){
	$tmp_key = intval($info['status']);
	flash("wait_apply", '', 0);
}
if (!empty($info['member_id'])) {
	$member_info = $member->getInfoById($info['member_id']);
	$info['space_name'] = $member_info['space_name'];
	setvar("MEMBER", $member_info);
}
if (!empty($info['company_id'])) {
	$company_info = $company->getInfoById($info['company_id']);
	if (!empty($company_info)) {
	$info['companyname'] = $company_info['name'];
	$info['link_people'] = $company_info['link_man'];
	$info['address'] = $company_info['address'];
	$info['zipcode'] = $company_info['zipcode'];
	$info['site_url'] = $company_info['site_url'];
	$info['tel'] = $company_info['tel'];
	$info['fax'] = $company_info['fax'];
	setvar("COMPANY", $company_info);
	}
}
if (!empty($info['industry_id1'])) {
	$viewhelper->setTitle($_PB_CACHE['industry'][1][$info['industry_id1']]);
	$viewhelper->setPosition($_PB_CACHE['industry'][1][$info['industry_id1']], "product/list.php?industryid=".$info['industry_id1']);
}
$viewhelper->setTitle($info['name'], $info['picture']);
$viewhelper->setPosition($info['name'], $info['picture']);
setvar("Areas", $_PB_CACHE['area']);
setvar("Industry", $_PB_CACHE['industry']);
$info['title'] = strip_tags($info['name']);
setvar("item", $info);
$product->clicked($id);
render("product.detail");
?>