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
require(LIB_PATH. 'page.class.php');
require("session_cp.inc.php");
require(LIB_PATH. "json_config.php");
require(LIB_PATH. "cache.class.php");
include(CACHE_PATH. "cache_type.php");
include(CACHE_PATH. "cache_area.php");
uses("area", "typeoption");
$cache = new Caches();
$area = new Areas();
$typeoption = new Typeoption();
$condition = null;
$conditions = array();
$tpl_file = "area";
$page = new Pages();
$cache_items = $_PB_CACHE['area'];
setvar("Types", $_PB_CACHE['areatype']);
setvar("AskAction", $typeoption->get_cache_type("common_option"));
if (isset($_POST['del'])) {
	if (!empty($_POST['id'])) {
		$area->del($_POST['id']);
	}
}
if (isset($_POST['do'])) {
	$do = trim($_POST['do']);
	if ($do == "clear") {
		foreach ($_POST['data']['level'] as $key=>$val){
			$result = $pdb->Execute("DELETE FROM {$tb_prefix}areas WHERE level='".$val."'");
		}
		if(!$result){
			flash();
		}
	}
}
if (isset($_POST['update_batch'])) {
	if (!empty($_POST['data']['aname'])) {
		for($i=0; $i<count($_POST['data']['aname']); $i++) {
			$pdb->Execute("UPDATE {$tb_prefix}areas SET name = '".$_POST['data']['aname'][$i]."' WHERE id='".$_POST['aid'][$i]."'");
		}		
		for($i=0; $i<count($_POST['data']['aname']); $i++) {
			$pdb->Execute("UPDATE {$tb_prefix}areas SET display_order = '".$_POST['data']['display_order'][$i]."' WHERE id='".$_POST['aid'][$i]."'");
		}
	}
	flash("success");
}
if (isset($_POST['save'])) {
	if (isset($_POST['data']['area']['parent_id'])) {
		$parent_id = $_POST['data']['area']['parent_id'];
		if ($parent_id == 0) {
			$top_parentid = $_POST['data']['area']['top_parentid'] = 0;
			$level = $_POST['data']['area']['level'] = 1;
		}else{
			if (array_key_exists($parent_id, $cache_items[1])) {
				$level = $_POST['data']['area']['level'] = 2;
				$top_parentid = $_POST['data']['area']['top_parentid'] = $parent_id;
			}elseif (array_key_exists($parent_id, $cache_items[2])){
				$level = $_POST['data']['area']['level'] = 3;
				$top_parentid = $_POST['data']['area']['top_parentid'] = $pdb->GetOne("SELECT parent_id FROM {$tb_prefix}areas WHERE id=".$parent_id);
			}
		}
	}
	if (isset($_POST['id'])) {
		$id = intval($_POST['id']);
		$result = $area->save($_POST['data']['area'], "update", $id);
	}elseif (!empty($_POST['data']['names'])){
		$names = explode("\r\n", $_POST['data']['names']);
		$tmp_name = array();
		if (!empty($names)) {
			foreach ($names as $val) {
				$name = $val;
				if(!empty($name)) $tmp_name[] = "('".$name."','".$_POST['data']['area']['url']."','".$parent_id."','".$top_parentid."','".$level."','".$_POST['data']['area']['display_order']."','".$_POST['data']['area']['areatype_id']."')";
			}
			$values = implode(",", $tmp_name);
			$sql = "INSERT INTO {$tb_prefix}areas (name,url,parent_id,top_parentid,level,display_order,areatype_id) VALUES ".$values;
			$result = $pdb->Execute($sql);
		}
	}
	if ($result) {
		$cache->writeCache("area", "area");
	}
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if ($do == "level") {
		if(!empty($id)){
			if ($_GET['action']=="up") {
				$pdb->Execute("UPDATE {$tb_prefix}areas SET display_order=display_order-1 WHERE id=".$id);
			}elseif ($_GET['action']=="down"){
				$pdb->Execute("UPDATE {$tb_prefix}areas SET display_order=display_order+1 WHERE id=".$id);
			}
		}
	}
	if ($do == "refresh") {
		$cache->writeCache("area", "area");
		flash("success");
	}
	if ($do == "search") {
		if (!empty($_GET['name'])) {
			$conditions[] = "name LIKE '%".$_GET['name']."%'";
		}
		if (isset($_GET['parentid'])) {
			$conditions[] = "parent_id=".intval($_GET['parentid']);
		}
		if (isset($_GET['level'])) {
			$conditions[] = "level=".intval($_GET['level']);
		}
		if (isset($_GET['typeid'])) {
			$conditions[] = "areatype_id=".intval($_GET['typeid']);
		}
	}
	if ($do == "edit") {
		setvar("CacheItems", $area->getTypeOptions());
		if (!empty($id)) {
			$res = $pdb->GetRow("SELECT * FROM {$tb_prefix}areas WHERE id=".$id);
			setvar("item", $res);
		}
		$tpl_file = "area.edit";
		template($tpl_file);
		exit;
	}
	if ($do == "clear") {
		$tpl_file = "area.clear";
		template($tpl_file);
		exit;
	}
}
$amount = $area->findCount(null, $conditions);
$page->setPagenav($amount);
$result = $area->findAll("id,name,name as title,highlight,parent_id,areatype_id,top_parentid,level,display_order", null, $conditions, "level ASC,display_order ASC,id ASC", $page->firstcount, $page->displaypg);
if (!empty($result)) {
	for($i=0; $i<count($result); $i++){
		$tmp_name = array();
		if($result[$i]['level']>1){
			if($result[$i]['level']>2){
				$tmp_name[] = $result[$i]['name'];
				if($_PB_CACHE['area'][2][$result[$i]['parent_id']]) $tmp_name[] = "<a href='area.php?do=search&parentid=".$result[$i]['parent_id']."'>".$_PB_CACHE['area'][2][$result[$i]['parent_id']]."</a>";
				if($_PB_CACHE['area'][1][$result[$i]['top_parentid']]) $tmp_name[] = "<a href='area.php?do=search&parentid=".$result[$i]['top_parentid']."'>".$_PB_CACHE['area'][1][$result[$i]['top_parentid']]."</a>";
			}else{
				$tmp_name[] = "<a href='area.php?do=search&parentid=".$result[$i]['id']."'>".$result[$i]['name']."</a>";
				if($_PB_CACHE['area'][1][$result[$i]['parent_id']]) $tmp_name[] = "<a href='area.php?do=search&parentid=".$result[$i]['parent_id']."'>".$_PB_CACHE['area'][1][$result[$i]['parent_id']]."</a>";
			}
		}else{
			$tmp_name[] = "<a href='area.php?do=search&parentid=".$result[$i]['id']."'>".$result[$i]['name']."</a>";
		}
		$result[$i]['title'] = implode("&laquo;", $tmp_name);
	}
	setvar("Items", $result);
	setvar("ByPages", $page->pagenav);
}
$stats = $pdb->GetArray("SELECT level,count(id) as amount FROM ".$tb_prefix."areas GROUP BY level");
setvar("LevelStats", $stats);
template($tpl_file);
?>