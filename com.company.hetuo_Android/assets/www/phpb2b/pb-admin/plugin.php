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
define('CURSCRIPT', 'plugin');
require("../libraries/common.inc.php");
uses("plugin");
require("session_cp.inc.php");
$plugin_model = new Plugins();
$plugin = new Plugin();
$conditions = null;
$tpl_file = "plugin";
if(isset($_GET['do'])){
	$do = trim($_GET['do']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if ($do == "uninstall" && !empty($id)) {
		$plugin_model->del($id);
	}
	if ($do == "install" && !empty($_GET['entry'])) {
		$entry = trim($_GET['entry']);
		$last_id = $plugin->install($entry);
		if($plugin->need_config){
			flash("plugin_installed_and_config", "plugin.php?do=edit&id=".$last_id, 0);
		}else{
			flash("success");
		}
	}
	if($do == "edit"){
		if (!empty($id)) {
			$row = $plugin_model->read("*", $id);
			if (!empty($row['pluginvar'])) {
				$plugin_var = unserialize($row['pluginvar']);
				unset($row['pluginvar']);
				$item = array_merge($row, $plugin_var);
			}else {
				$item = $row;
			}
			$plugin->plugin_name = $row['name'];
			require($plugin->plugin_path.$row['name'].DS.$row['name'].".php");
			$smarty->assign($item);
			formhash();
			template($plugin->plugin_path.$row['name'].DS."template".DS."admin");
			exit;
		}
	}
}
if (isset($_POST['save']) && !empty($_POST['pluginvar']) && !empty($_POST['entry'])) {
	$plugin_var = serialize($_POST['pluginvar']);
	$entry = trim($_POST['entry']);
	$plugin->plugin_name = $entry;
	require($plugin->plugin_path.$entry.DS.$entry.".php");
	if (isset($_POST['id'])) {
		$id = intval($_POST['id']);
		$result = $pdb->Execute("UPDATE {$tb_prefix}plugins SET pluginvar='".$plugin_var."' WHERE id=".$id);
	}else{
		$result = $pdb->Execute("UPDATE {$tb_prefix}plugins SET pluginvar='".$plugin_var."' WHERE entry='".$entry."'");
	}
	if(!$result){
		flash();
	}else{
		flash("success");
	}
}
$result = $plugin->getPlugins();
setvar("Items", $result);
template($tpl_file);
?>