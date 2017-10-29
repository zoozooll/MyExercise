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
if(!defined('IN_PHPB2B')) exit('Not A Valid Entry Point');
uses("product");
require(PHPB2B_ROOT.'libraries/page.class.php');
$product = new Products();
$page = new Pages();
$page->displaypg = 20;
$page->is_rewrite = $rewrite_able;
$page->_url = $space->rewriteList("product");
$tpl_file = "product";
$conditions = null;
$conditions[] = "Product.status=1 AND Product.company_id='".$company->info['id']."'";
if (isset($_GET['typeid'])) {
	$conditions[]= "producttype_id=".intval($_GET['typeid']);
}
if (isset($_GET['new']) && $_GET['new'] == 1) {
	$conditions[]= "ifnew=1";
}
$amount = $product->findCount(null, $conditions,"id");
$page->setPagenav($amount);
$result = $product->findAll('id,picture,name',null, $conditions,"id DESC",$page->firstcount,$page->displaypg);
if (!empty($result)) {
	$count = count($result);
	for($i=0; $i<$count; $i++){
		$result[$i]['image'] = URL. pb_get_attachmenturl($result[$i]['picture'], '', 'small');
		$result[$i]['url'] = $space->rewriteDetail("product", $result[$i]['id']);
	}
}
setvar("Items",$result);
setvar("ByPages",$page->pagenav);
$space->render($tpl_file);
?>