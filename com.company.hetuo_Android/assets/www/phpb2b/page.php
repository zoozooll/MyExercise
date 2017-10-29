<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 430 $
 */
define('CURSCRIPT', 'page');
require("libraries/common.inc.php");
require("share.inc.php");
uses("userpage");
$userpage = new Userpages();
$conditions = array();
if (isset($_GET['id'])) {
	$id = intval($_GET['id']);
	$conditions[] = "id=".$id;
}
if (isset($_GET['name'])) {
	$conditions[] = "name='".$_GET['name']."'";
}
$userpage->setCondition($conditions);
$result = $pdb->GetRow("SELECT id,name,title,content,url FROM {$tb_prefix}userpages".$userpage->getCondition());
if (!empty($result)) {
	$viewhelper->setTitle($result['title']);
	setvar("item", $result);
	render("page.index");
}else{
	flash("failed", URL, 0);
}
?>