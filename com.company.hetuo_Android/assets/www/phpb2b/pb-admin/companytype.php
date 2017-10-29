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
require("../libraries/common.inc.php");
require("session_cp.inc.php");
require(LIB_PATH. "cache.class.php");
include(CACHE_PATH. "cache_type.php");
$cache = new Caches();
$tpl_file = "companytype";
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
			$ins_str = "REPLACE INTO {$tb_prefix}companytypes (id,name) VALUES ".implode(",", $ins_arr).";";
			$pdb->Execute($ins_str);
		}		
		if($cache->updateTypes()){
			flash("success");
		}else{
			flash();
		}
	}
}
if (!empty($_PB_CACHE['companytype'])) {
	setvar("sorts", implode("\r\n", $_PB_CACHE['companytype']));
}
template($tpl_file);
?>