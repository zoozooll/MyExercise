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
include(CACHE_PATH. "cache_type.php");
$cache = new Caches();
$conditions = array();
$fields = null;
$tpl_file = "albumtype";
if (isset($_POST['do'])) {
	$do = trim($_POST['do']);
	if ($do == "save") {
		$ins_arr = array();
		$tmp_arr = explode("\r\n", $_POST['data']['sort']);
		array_filter($tmp_arr);
		$i = 1;
		foreach ($tmp_arr as $key=>$val) {
			$ins_arr[$i] = "(".$i.",'".$val."')";
			$i++;
		}
		if (!empty($ins_arr)) {
			$ins_str = "REPLACE INTO {$tb_prefix}albumtypes (id,name) VALUES ".implode(",", $ins_arr).";";
			$pdb->Execute($ins_str);
		}
		if($cache->updateTypes()){
			flash("success");
		}else{
			flash();
		}
	}
}
$albumtypes = $pdb->GetArray("SELECT * FROM {$tb_prefix}albumtypes");
if (!empty($_PB_CACHE['albumtype'])) {
	setvar("sorts", implode("\r\n", $_PB_CACHE['albumtype']));
}elseif (!empty($albumtypes)){
	foreach ($albumtypes as $key=>$val) {
		$tmp_arr[$val['id']] = $val['name'];
	}
	setvar("sorts", implode("\r\n", $tmp_arr));
}
template($tpl_file);
?>