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
$product = new Products();
$conditions = "Product.status=1 and Product.company_id='".$company->info['id']."'";
$result = $product->findAll('id,picture,name,picture as image',null, $conditions,"Product.id DESC",0,8);
if (!empty($result)) {
	$count = count($result);
	for($i=0; $i<$count; $i++){
		$result[$i]['picture'] = URL. pb_get_attachmenturl($result[$i]['image']);
		$result[$i]['url'] = $space->rewriteDetail("product", $result[$i]['id']);
	}
}
setvar("Products", $result);
$space->render("index");
?>