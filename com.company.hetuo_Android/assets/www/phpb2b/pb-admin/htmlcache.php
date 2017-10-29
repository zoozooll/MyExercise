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
require(LIB_PATH. "file.class.php");
uses("brand");
$brand = new Brands();
require(LIB_PATH. "cache.class.php");
$cache = new Caches();
$tpl_file = "htmlcache";
if (isset($_POST['do'])) {
	require(LIB_PATH. "json_config.php");
	$do = trim($_POST['do']);
	$file = new Files();
	switch ($do) {
		case "clear":
			if (in_array("membercache", $_POST['data']['type'])) {
				$pdb->Execute("TRUNCATE `{$tb_prefix}membercaches`");
			}
			if (in_array("smartycache", $_POST['data']['type'])) {
				$smarty->clear_all_cache();
			}
			if (in_array("smartycompile", $_POST['data']['type'])) {
				$smarty->clear_compiled_tpl();
				$file->rmDirs(DATA_PATH. "templates_c");
			}
			if (in_array("dbcache", $_POST['data']['type'])) {
				$file->exclude[] = "index.htm";
				$file->rmDirs(DATA_PATH. "dbcache", false, false);
				$file->rmDirs(DATA_PATH. "dbcache", false, true);
			}
			flash("success", "htmlcache.php?do=clear");
			break;
		case "update":
			if (in_array("area", $_POST['data']['type'])) {
				$cache->writeCache("area", "area");
			}
			if (in_array("options", $_POST['data']['type'])) {
				$cache->updateTypevars();
			}
			if (in_array("industry", $_POST['data']['type'])) {
				uses("industry");
				$industry = new Industries();
				$industry->updateCache();
				$cache->writeCache("industry", "industry");
			}
			if (in_array("setting", $_POST['data']['type'])) {
				$cache->updateIndexCache();
				$cache->updateTypes();
				$cache->writeCache("setting", "setting");
			}
			if (in_array("setting1", $_POST['data']['type'])) {
				$cache->writeCache("setting1", "setting1");
				$cache->writeCache("userpage", "userpage");
				$cache->writeCache("trusttype", "trusttype");
				$cache->writeCache("membergroup", "membergroup");
				$cache->writeCache("form", "form");
			}
			flash("success", "htmlcache.php?do=update");
			break;
		default:
			break;
	}
}
template($tpl_file);
?>