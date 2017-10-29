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
uses("membertype", "typeoption");
require(PHPB2B_ROOT.'./libraries/page.class.php');
require(LIB_PATH. "cache.class.php");
require(CACHE_PATH. "cache_membergroup.php");
$cache = new Caches();
$typeoption = new Typeoption();
$conditions = null;
$page = new Pages();
$membertype = new Membertypes();
$tpl_file = "membertype";
setvar("MembertypeStatus", $typeoption->get_cache_type("common_option"));
foreach ($_PB_CACHE['membergroup'] as $key=>$val) {
	$membergroups[$key] = $val['name'];
}
setvar("Membergroups", $membergroups);
if (isset($_POST['del']) && !empty($_POST['id'])) {
	$result = $membertype->del($_POST['id']);
	if (!$result) {
		flash();
	}else{
		$cache->updateTypes();
	}
}
if(isset($_POST['save'])){
	$id = $_POST['id'];
	$vals = $_POST['data']['membertype'];
	if(!empty($id)){
		$result = $membertype->save($vals, "update", $id);
	}else{
		$result = $membertype->save($vals);
	}
	if(!$result){
		flash();
	}else{
		$cache->updateTypes();
	}
}

if (isset($_POST['updateDefault']) && !empty($_POST['default_id'])) {
	$vals = array();
	$vals['if_default'] = 1;
	$pdb->Execute("update ".$membertype->getTable()." set if_default=0");
	$result = $pdb->Execute("update ".$membertype->getTable()." set if_default=1 where id=".intval($_POST['default_id']));
}
if (isset($_POST['putIndex']) && !empty($_POST['index_id'])) {
	$vals = array();
	$vals['if_index'] = 1;
	$pdb->Execute("update ".$membertype->getTable()." set if_index=0");
	$result = $pdb->Execute("update ".$membertype->getTable()." set if_index=1 where id=".intval($_POST['index_id']));
}
if (isset($_POST['quickadd']) && !empty($_POST['membertype']['name'])) {
	$vals = array();
	$vals = $_POST['membertype'];
	$result = $membertype->save($vals);
	if (!$result) {
		flash();
	}
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if ($do == "edit") {
		$tpl_file = "membertype.edit";
		if (!empty($id)) {
			$result = $membertype->read("*",$id);
			setvar("item",$result);
		}
	}
}
$sql = "SELECT * FROM {$tb_prefix}membertypes";
$result = $pdb->GetArray($sql);
if (!empty($result)) {
	for($i=0; $i<count($result); $i++){
		$result[$i]['default_group'] = $_PB_CACHE['membergroup'][$result[$i]['default_membergroup_id']]['name'];
	}
}
setvar("Items",$result);
template($tpl_file);
?>