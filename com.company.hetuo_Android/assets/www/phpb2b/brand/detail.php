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
uses("brand", "brandtype");
$brand = new Brands();
$brandtype = new Brandtypes();
$condition = null;
$conditions = array();
$viewhelper->setPosition(L("brands", "tpl"), "brand.php");
if (isset($_GET['name'])) {
	$brand_name = trim(rawurldecode($_GET['name']));
	$id = $pdb->GetOne("SELECT id FROM {$tb_prefix}brands WHERE name='".$brand_name."'");
}
if (isset($_GET['id'])) {
	$id = intval($_GET['id']);
}
if(!empty($id))	{
	$result = $pdb->GetRow("SELECT * FROM {$tb_prefix}brands WHERE id='".$id."'");
	if(!empty($result)){
		$result['img'] = pb_get_attachmenturl($result['picture'], '', 'middle');
		$result['title'] = $result['name'];
		$viewhelper->setPosition($pdb->GetOne("SELECT name FROM ".$tb_prefix."brandtypes WHERE id=".$result['type_id']), "brand/list.php?catid=".$result['type_id']);
		$viewhelper->setTitle($result['title']);
		$viewhelper->setPosition($result['title']);
	}
}else{
	L("data_not_exists");
}
setvar("item",$result);
render("brand.detail");
?>