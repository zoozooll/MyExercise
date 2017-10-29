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
require(LIB_PATH. "file.class.php");
if (file_exists($cache_userpage = CACHE_PATH. "cache_userpage.php")) {
	require($cache_userpage);
}
uses("userpage");
$cache = new Caches();
$userpage = new Userpages();
$conditions = null;
$tpl_file = "userpage";
$file = new Files();
if (isset($_POST['del']) && is_array($_POST['id'])) {
	$deleted = $userpage->del($_POST['id']);
	if (!$deleted) {
		flash();
	}
	$cache->writeCache("userpage", "userpage");
}
if (isset($_POST['save'])) {
	$vals = array();
	$vals = $_POST['data']['userpage'];
	if(!empty($vals['title'])&&!empty($vals['name'])){
	if (!empty($_POST['id'])) {
		$vals['modified'] = $time_stamp;
		$result = $userpage->save($vals, "update", $_POST['id']);
	}else{
		$vals['created'] = $vals['modified'] = $time_stamp;
		$result = $userpage->save($vals);
	}
  }
	if (!$result) {
		flash();
	}
	$cache->writeCache("userpage", "userpage");
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if ($do=="del" && !empty($id)) {
		$deleted = $userpage->del($id);
		$cache->writeCache("userpage", "userpage");
	}
	if ($do == "edit") {
		if(!empty($id)){
			$res= $userpage->read("*",$id);
			setvar("item",$res);
		}
		setvar("tplext", $smarty->tpl_ext);
		$tmp_pagetemplets = $file->getFiles(PHPB2B_ROOT."templates".DS.$theme_name);
		if (!empty($tmp_pagetemplets)) {
			$page_templets = "<optgroup label='".L("other_templet", "tpl")."'>";
			foreach ($tmp_pagetemplets as $p_val) {
				if (strstr($p_val['name'], "page.")) {
					$page_templets.= "<option value=".$p_val['name'].">".$p_val['name']."</option>";
				}
			}
			$page_templets.="</optgroup>";
			setvar("other_templets", $page_templets);
		}
		$tpl_file = "userpage.edit";
		template($tpl_file);
		exit;
	}
}
$result = $userpage->findAll("id,title,name,url,digest,display_order", null, $conditions, "display_order ASC,id ASC");
if (empty($result) && !empty($_PB_CACHE['userpage'])) {
	$result = $_PB_CACHE['userpage'];
	while (list($key, $val) = each($result)) {
		$tmp_arr[] = "('".$val['name']."','".$val['digest']."','".$val['title']."','".$val['url']."')";
	}
	$tmp_str = implode(",", $tmp_arr);
	$pdb->Execute("INSERT INTO ".$tb_prefix."userpages (name,digest,title,url) VALUES ".$tmp_str);
}
setvar("Items", $result);
template($tpl_file);
?>