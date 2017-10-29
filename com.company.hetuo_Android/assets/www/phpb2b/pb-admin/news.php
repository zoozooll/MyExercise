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
uses("news","newstype", "membertype","attachment", "tag","typeoption");
require(LIB_PATH .'time.class.php');
require(PHPB2B_ROOT.'libraries/page.class.php');
require("session_cp.inc.php");
$tag = new Tags();
$page = new Pages();
$attachment = new Attachment('pic');
$typeoption = new Typeoption();
$membertype = new Membertypes();
$news = new Newses();
$newstype = new Newstypes();
$conditions = array();
$fields = null;
$tpl_file = "news";
setvar("AskAction", $typeoption->get_cache_type("common_option"));
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (isset($_GET['action'])) {
		$action = trim($_GET['action']);
	}
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if ($do == "search") {
		if (isset($_GET['keywords'])) $conditions[]= "News.keywords like '%".trim($_GET['news']['keywords'])."%'";
		if (isset($_GET['source'])) $conditions[]= "News.source like '%".trim($_GET['news']['source'])."%'";
		if (isset($_GET['q'])) $conditions[]= "News.title like '%".trim($_GET['q'])."%'";
		if (!empty($_GET['typeid'])) {
			$conditions[]= "News.type_id=".$_GET['typeid'];
		}
		if (isset($_GET['topicid'])) {
			setvar("Items", $pdb->GetArray("SELECT n.* FROM {$tb_prefix}topicnews tn RIGHT JOIN {$tb_prefix}newses n ON tn.news_id=n.id WHERE tn.topic_id=".intval($_GET['topicid'])));
			setvar("Newstypes", $newstype->getCacheTypes());
			template($tpl_file);
			exit;
		}
	}
	if ($do == "del" && !empty($id)) {
		$sql = "SELECT picture FROM {$tb_prefix}newses WHERE id=".$id;
		$attach_filename = $pdb->GetOne($sql);
		$news->del($_GET['id']);
		$attachment->deleteBySource($attach_filename);
	}
	if ($do == "edit") {
		$news_info = null;
		include(CACHE_PATH. "cache_area.php");
		include(CACHE_PATH. "cache_industry.php");
		setvar("CacheAreas", $_PB_CACHE['area']);
		setvar("CacheIndustries", $_PB_CACHE['industry']);		
		$result = $membertype->findAll("id,name",null, $conditions, " id desc");
		$user_types = array();
		foreach ($result as $key=>$val) {
			$user_types[$val['id']] = $val['name'];
		}
		setvar("Membertypes", $user_types);
		setvar("NewstypeOptions", $newstype->getTypeOptions());
		if(!empty($id)){
			$item_info = $news->read("*",$id);
			if(($item_info['picture'])) $item_info['image'] = pb_get_attachmenturl($item_info['picture'], "../", 'small');
			$tag->getTagsByIds($item_info['tag_ids'], true);
			$item_info['tag'] = $tag->tag;
		}
		if ($action == "convert") {
			if (!empty($_GET['companynewsid'])) {
				$item_info['title'] = $pdb->GetOne("SELECT title FROM {$tb_prefix}companynewses WHERE id=".intval($_GET['companynewsid']));
			}
		}
		if (!empty($item_info)) {
			setvar("item",$item_info);
		}
		$tpl_file = "news.edit";
		template($tpl_file);
		exit;
	}	
}
if (isset($_POST['update']) && !empty($_POST['if_focus'])) {
	$pdb->Execute("update ".$news->getTable()." set if_focus=0");
	$pdb->Execute("update ".$news->getTable()." set if_focus=1 where id=".intval($_POST['if_focus']));
}
if (isset($_POST['del']) && is_array($_POST['id'])) {
	foreach ($_POST['id'] as $key=>$val){
	    $attach_filename = $pdb->GetOne("select picture from {$tb_prefix}newses where id=".$val);
	    $attachment->deleteBySource($attach_filename);
	}
	$deleted = $news->del($_POST['id']);
	if (!$deleted) {
		flash();
	}
}

if (isset($_POST['commend']) && is_array($_POST['id'])) {
	$news->saveField("if_commend", 1, $_POST['id']);
	flash("success");
}

if (isset($_POST['cancel_commend']) && is_array($_POST['id'])) {
	$news->saveField("if_commend", 0, $_POST['id']);
	flash("success");
}
if (isset($_POST['save']) && !empty($_POST['data']['news'])) {
	if (isset($_POST['id'])) {
		$id = intval($_POST['id']);
	}
	$attachment->if_orignal = false;
	$attachment->if_watermark = false;
	$attachment->if_thumb_middle = true;
	$vals = array();
	$vals = $_POST['data']['news'];
	if(!empty($_POST['require_membertype']) && !in_array(0, $_POST['require_membertype'])){
		$reses = implode(",", $_POST['require_membertype']);
		$vals['require_membertype'] = $reses;
	}elseif(!empty($_POST['require_membertype'])){
		$vals['require_membertype'] = 0;
	}
	$vals['tag_ids'] = $tag->setTagId($_POST['data']['tag']);
	if(!empty($id)){
		$vals['modified'] = $time_stamp;
		if (!empty($_FILES['pic']['name'])) {
			$attachment->rename_file = "news-".$id;	
			$attachment->insert_new = false;
			$attachment->upload_process();
			$vals['picture'] = $attachment->file_full_url;
		}
		$result = $news->save($vals, "update", $id);
	}else{
		$vals['created'] = $vals['modified'] = $time_stamp;
		if (!empty($_FILES['pic']['name'])) {
			$attachment->rename_file = "news-".($news->getMaxId()+1);	
			$attachment->upload_process();
			$vals['picture'] = $attachment->file_full_url;
		}
		$result = $news->save($vals);
	}
	if (!$result) {
		flash();
	}
}
$amount = $news->findCount(null, $conditions);
$page->setPagenav($amount);
setvar("Items",$news->findAll("*", null, $conditions, "id DESC ",$page->firstcount,$page->displaypg));
uaAssign(array("ByPages"=>$page->pagenav, "Newstypes"=>$newstype->getCacheTypes()));
template($tpl_file);
?>