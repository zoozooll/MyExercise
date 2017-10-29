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
require("session_cp.inc.php");
require(LIB_PATH. "cache.class.php");
require(LIB_PATH. "file.class.php");
uses("setting");
$setting_controller = new Setting();
$setting = new Settings();
$cache = new Caches();
$file = new Files();
$conditions = null;
$tpl_file = "language";
if (isset($_POST['save'])) {
	$vals = $datas = array();
	foreach ($_POST['data']['item'] as $key=>$val) {
		$vals[$val] = $_POST['data']['language'][$val];
	}
	$datas['languages'] = serialize($vals);
	$setting->replace($datas);
	$cache->writeCache("setting", "setting");
	flash("success");
}
$result = $file->getFolders("../languages/");
$items = array();
$installed_languages = array();
if (!empty($_PB_CACHE['setting']['languages'])) {
	$installed_languages = unserialize($_PB_CACHE['setting']['languages']);
}
if (!empty($result)) {
	foreach ($result as $key=>$val) {
		if(file_exists($templet_file = "../languages/".$val['name']."/template.inc.php")){
			$data = $setting_controller->getSkinData($templet_file);
			$items[$val['name']]['name'] = $val['name'];
			$items[$val['name']]['title'] = $data['Name'];
			$items[$val['name']]['img'] = "languages/".$val['name']."/icon.gif";
			$items[$val['name']]['available'] = array_key_exists($val['name'], $installed_languages)?1:0;
		}
	}
}
setvar("Items", $items);
template($tpl_file);
?>