<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 121 $
 */
define('CURSCRIPT', 'sitemap');
require("libraries/common.inc.php");
require("share.inc.php");
require(CACHE_PATH. "cache_type.php");
require(PHPB2B_ROOT.'libraries/page.class.php');
uses("standard","standardtype");
$standard = new Standards();
$standardtype = new Standardtypes();
$page = new Pages();
$tpl_file = "standard.list";
$viewhelper->setTitle(L("standards", "tpl"));
$viewhelper->setPosition(L("standards", "tpl"), "standard.php");
if (isset($_GET['title'])) {
	$title = rawurldecode(trim($_GET['title']));
	$res = $standard->findByTitle($title);
	$id = $res['id'];
}
if (isset($_GET['id'])) {
	$id = intval($_GET['id']);
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
}
if (!empty($id)) {
	$info = $standard->read("*",$id);
	if (empty($info) or !$info) {
		flash("data_not_exists", '', 0);
	}
	if($do == "downloadtxt") {
		//$file_name = DATA_PATH."tmp/".$id.".txt";
		//file_put_contents($file_name, $info['title']."\n\r".$info['content']);
		header("Content-type:   application/octet-stream");
		header("Accept-Ranges:   bytes");
		Header("Accept-Length: ".filesize($file_name));
		header("Content-Disposition:   attachment;   filename=".rawurlencode($info['title']).".txt");
		header("Expires:   0");
		header("Cache-Control:   must-revalidate,   post-check=0,   pre-check=0");
		header("Pragma:   public");
		echo $info['title']."\r\n".$info['content'];
		exit();	
	}
	$info['pubdate'] = date("Y-m-d", $info['created']);
	$info['typename'] = $_PB_CACHE['standardtype'][$info['type_id']];
	$viewhelper->setTitle($info['typename']);
	$viewhelper->setPosition($info['typename'], "standard.php?typeid=".$info['type_id']);
	$viewhelper->setTitle($info['title']);
	$viewhelper->setPosition($info['title']);
	if (!empty($info['attachment_id'])) {
		$info['attach_hashid'] = rawurlencode(authcode($info['attachment_id']));
	}
	setvar("item",$info);
	$tpl_file = "standard.detail";
	$standard->clicked($id);
	render($tpl_file,true);
}
if (isset($_GET['typeid'])) {
	$typeid = intval($_GET['typeid']);
	$conditions[] = "type_id='".$typeid."'";
	$viewhelper->setTitle($type_name = $pdb->GetOne("SELECT name FROM {$tb_prefix}standardtypes WHERE id='".$typeid."'"));
	$viewhelper->setPosition($type_name, "standard.php?do=search&typeid=".$typeid);
	setvar("TypeName", $type_name);
}
if (isset($_GET['filter'])) {
	$filter = intval($_GET['filter']);
	$conditions[] = "created>".($time_stamp-$filter);
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if ($do == "search") {
		if (isset($_GET['q'])) {
			$searchkeywords = urldecode($_GET['q']);
			$conditions[] = "title like '%".$searchkeywords."%'";
		}
		if(isset($_GET['typeid'])){
			$typeid = $_GET['typeid'];
			$conditions[] = "type_id='".$typeid."'";
		}
	}
}
$standard->setCondition($conditions);
$amount = $standard->findCount(null, $conditions);
$page->setPagenav($amount);
$sql = "SELECT sd.*,sdt.name AS type_name FROM {$tb_prefix}standards sd LEFT JOIN ".$tb_prefix."standardtypes sdt ON sd.type_id=sdt.id ".$standard->getCondition()." LIMIT ".$page->firstcount.",".$page->displaypg;
$result = $pdb->GetArray($sql);
if(!empty($result)){
	$count = count($result);
	for ($i=0; $i<$count; $i++){
		$result[$i]['create_date'] = @date("Y-m-d", $result[$i]['created']);
		if(!empty($result[$i]['publish_time'])) $result[$i]['publish_date'] = @date("Y-m-d", $result[$i]['publish_time']);
		if(!empty($result[$i]['force_time'])) $result[$i]['force_date'] = @date("Y-m-d", $result[$i]['force_time']);
		if(!empty($result[$i]['digest'])) $result[$i]['digest'] = mb_substr(strip_tags(ltrim($result[$i]['digest'])), 0, 100);
	}
}
setvar('Items', $result);
setvar('Types', $_PB_CACHE['standardtype']);
setvar("ByPages", $page->getPagenav());
render($tpl_file);
?>