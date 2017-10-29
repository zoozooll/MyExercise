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
define('CURSCRIPT', 'detail');
require("../libraries/common.inc.php");
require("../share.inc.php");
uses("market");
$market = new Markets();
$viewhelper->setTitle(L("market", "tpl"));
$viewhelper->setPosition(L("market", "tpl"), "market/");
if (isset($_GET['id'])) {
	$id = intval($_GET['id']);
	$sql = "select * from {$tb_prefix}markets m where id={$id}";
	$item = $pdb->GetRow($sql);
}
if (!empty($item)) {
	$viewhelper->setMetaDescription($item['content']);
	$item['content'] = nl2br($item['content']);
	$viewhelper->setTitle($item['name']);
	$viewhelper->setPosition($item['name']);
	if (isset($item['status'])) {
		if($item['status']==0){
			$item['content'] = L('under_checking', 'msg', $item['name']);
		}
	}
	$item['image'] = pb_get_attachmenturl($item['picture']);
	setvar("item",$item);	
}else{
	flash("data_not_exists");
}
render("market.detail");
?>