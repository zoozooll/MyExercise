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
define('CURSCRIPT', 'index');
require("../libraries/common.inc.php");
require("../share.inc.php");
require("common.inc.php");
$tpl_file = "help.detail";
$viewhelper->setTitle(L("help_center", "tpl"));
$viewhelper->setPosition(L("help_center", "tpl"), "help/");
if (isset($_GET['id'])) {
	$id = intval($_GET['id']);
	$help_result = $pdb->GetRow("SELECT * FROM {$tb_prefix}helps WHERE id='".$id."'");
	if (!empty($help_result)) {
		$viewhelper->setTitle($help_result['title']);
		$viewhelper->setPosition($help_result['title']);		
		setvar("item", $help_result);
	}
}
render($tpl_file);
?>