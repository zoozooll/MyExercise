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
require(LIB_PATH .'time.class.php');
require(LIB_PATH .'page.class.php');
require(LIB_PATH .'xml.class.php');
uses("adzone","ad","attachment","typeoption");
$tpl_file = "ad";
$attachment = new Attachment('attach');
$adzone = new Adzones();
$ads = new Adses();
$page = new Pages();
$typeoption = new Typeoption();
$conditions = array();
setvar("AdsStatus", $typeoption->get_cache_type("common_option"));
setvar("Adzones",$adzone->findAll("id,name",null, null,"id desc"));
if (isset($_POST['save'])) {
	$vals = $_POST['ad'];
	if(isset($_POST['id'])){
	$id = intval($_POST['id']);
	}
	if (!empty($_FILES['attach']['name'])) {
		$aname = (empty($id))?($ads->getMaxId()+1):$id;
		$attachment->if_thumb=false;
		$attachment->if_thumb_large = false;
		$attachment->if_watermark = false;
		$attachment->insert_new = false;
		$attachment->rename_file = $vals['adzone_id']."-".$aname;
		$attachment->upload_process();
		$vals['source_url'] = URL.$attachment_dir."/".$attachment->file_full_url;
		$vals['source_type'] = $_FILES['attach']['type'];
		$vals['is_image'] = $attachment->is_image;
		$vals['width'] = (!empty($vals['width']))?$vals['width']:$attachment->width;
		$vals['height'] = (!empty($vals['height']))?$vals['height']:$attachment->height;
	}
	if(!empty($_POST['data']['end_date'])) {
	    $vals['end_date'] = Times::dateConvert($_POST['data']['end_date']);
	}
	if (!empty($id)) {
		$vals['modified'] = $time_stamp;
		$result = $ads->save($vals, "update", $id);
	}else{
		$vals['created'] = $vals['modified'] = $vals['start_date'] = $time_stamp;
		$result = $ads->save($vals);
	}
	if (!$result) {
		flash();
	}
	$adzone->updateBreathe($vals['adzone_id']);
}
if (isset($_POST['del']) && !empty($_POST['id'])) {
	$result = $ads->del($_POST['id']);
}
if(isset($_POST['up'])&&!empty($_POST['id'])){
	$ids = $_POST['id'];
	foreach($ids as $id){
		$pdb->Execute("UPDATE {$tb_prefix}adses set state=1 where id=".$id);
    }
}
if(isset($_POST['down'])&&!empty($_POST['id'])){
	$ids = $_POST['id'];
	foreach($ids as $id){
		$pdb->Execute("UPDATE {$tb_prefix}adses set state=0 where id=".$id);
    }
}
if (isset($_GET['do'])){
	$do = trim($_GET['do']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if($do=="del" && !empty($id)) {
		$result = $ads->del($_GET['id'])	;
		if (!empty($_GET['adzone_id'])) {
			$adzone->updateBreathe($_GET['adzone_id']);
		}
	}
	if ($do == "edit") {
		if (!empty($id)) {
			$result = $ads->read("*", $id);
			if (!empty($result['end_date'])) {
				$result['end_date'] = date("Y-m-d", $result['end_date']);
			}
			setvar("item",$result);
		}
		$tpl_file = "ad.edit";
		template($tpl_file);
		exit;
	}
	if ($do == "search") {
		if (!empty($_GET['adzone_id'])) {
			$conditions[] = "Ads.adzone_id=".$_GET['adzone_id'];
		}
	}
}
$amount = $ads->findCount();
$page->setPagenav($amount);
$joins[] = "LEFT JOIN {$tb_prefix}adzones az ON az.id=Ads.adzone_id";
$result = $ads->findAll("Ads.*,az.name AS adzone",$joins, $conditions, " Ads.id desc", $page->firstcount, $page->displaypg);
for($i=0; $i<count($result); $i++){
	if (!empty($result[$i]['source_url'])) {
		if (strstr($result[$i]['source_url'], "http")) {
			$result[$i]['src'] = $result[$i]['source_url'];
		}else{
			$result[$i]['src'] = URL.$result[$i]['source_url'];
		}
	}
}
setvar("Items",$result);
setvar("ByPages",$page->pagenav);
template($tpl_file);
?>