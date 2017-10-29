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
define('CURSCRIPT', 'list');
require("../libraries/common.inc.php");
require("../share.inc.php");
uses("news","newstype");
require(LIB_PATH. 'page.class.php');
require(CACHE_PATH. "cache_type.php");
$news = new Newses();
$newstype = new Newstypes();
$page = new Pages();
$page->pagetpl_dir = $theme_name;
$conditions = array();
$tpl_file = "news.list";
setvar("Newstypes", $newstype->getCacheTypes());
$orderby = null;
$viewhelper->setTitle(L("info", "tpl"));
$viewhelper->setPosition(L("info", "tpl"), "news/");
if(isset($_GET['q']) && !empty($_GET['q'])){
	$title = trim($_GET['q']);
	$conditions[] = "News.title like '%".$title."%'";
}
if (isset($_GET['topicid'])) {
	$topic_id = intval($_GET['topicid']);
	$topic_res = $pdb->GetRow("SELECT * FROM {$tb_prefix}topics WHERE id=".$topic_id);
	$viewhelper->setTitle(L("topic_news", "tpl"));
	$viewhelper->setPosition(L("topic_news", "tpl"));
	if(!empty($topic_res)){
		setvar("Items", $pdb->GetArray("SELECT n.*,n.created AS pubdate FROM {$tb_prefix}topicnews tn RIGHT JOIN {$tb_prefix}newses n ON tn.news_id=n.id WHERE tn.topic_id=".$topic_id));
		$viewhelper->setTitle($topic_res['title']);
		$viewhelper->setPosition($topic_res['title'], "news/list.php?topicid=".$topic_id);
		render($tpl_file, true);
	}
}
$newstype_id = 0;
if(isset($_GET['typeid'])){
	$newstype_id = intval($_GET['typeid']);
	if (!empty($newstype_id)) {
		$newstype_name = $_PB_CACHE['newstype'][$newstype_id];
		$conditions[] = "News.type_id=".$newstype_id;
		$viewhelper->setTitle($newstype_name);
		$viewhelper->setPosition($newstype_name, "news/list.php?typeid=".$newstype_id);
	}

}
if (isset($_GET['filter'])) {
	$filter = intval($_GET['filter']);
	$conditions[] = "News.created>".($time_stamp-$filter);
}
$sub_result = $pdb->GetArray("SELECT id,name FROM ".$tb_prefix."newstypes WHERE parent_id='".$newstype_id."'");
if (!empty($sub_result)) {
	setvar("SubCats", $sub_result);
}
if (isset($_GET['type'])) {
	$type = trim($_GET['type']);
	if ($type == "hot") {
		$orderby = "News.clicked DESC,";
	}
}
$amount = $news->findCount(null, $conditions);
$page->setPagenav($amount);
$result = $news->findAll("News.*", null, $conditions, $orderby."News.id DESC", $page->firstcount, $page->displaypg);
if (!empty($result)) {
	for($i=0; $i<count($result); $i++){
		$result[$i]['pubdate'] = date("Y-m-d", $result[$i]['created']);
		$result[$i]['digest'] = mb_substr(ltrim(strip_tags($result[$i]['content'])), 0, 100);
		$result[$i]['image'] = pb_get_attachmenturl($result[$i]['picture'], '', 'small');
	}
	setvar("Items", $result);
}
setvar("ByPages", $page->pagenav);
render($tpl_file);
?>