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
define('CURSCRIPT', 'detail');
require("../libraries/common.inc.php");
require("../share.inc.php");
uses("news","tag");

$news = new Newses();
$tag = new Tags();
$conditions = array();
$viewhelper->setTitle(L("info", "tpl"));
$viewhelper->setPosition(L("info", "tpl"), "news/");
if (isset($_GET['title'])) {
	$title = rawurldecode(trim($_GET['title']));
	$res = $news->findByTitle($title);
	$id = $res['id'];
}
if (isset($_GET['id'])) {
	$id = intval($_GET['id']);
}
if (!empty($id)) {
    require(CACHE_PATH."cache_type.php");
	$news->clicked($id);
	$info = $news->read("*",$id);
	if (empty($info) or !$info) {
		flash("data_not_exists", '', 0);
	}
	if(!empty($info['tag_ids'])){
    	$tag->getTagsByIds($info['tag_ids'], true);
    	$info['tag'] = $tag->tag;
	}
	if (!empty($info['picture'])) {
		$info['image'] = pb_get_attachmenturl($info['picture'], '', 'small');
	}
	$info['pubdate'] = date("Y-m-d", $info['created']);
	$info['typename'] = $_PB_CACHE['newstype'][$info['type_id']];
	$viewhelper->setTitle($info['typename']);
	$viewhelper->setPosition($info['typename'], "news/list.php?typeid=".$info['type_id']);
	$viewhelper->setTitle($info['title']);
	$viewhelper->setPosition($info['title']);
	if (!empty($info['require_membertype'])) {
		$require_ids = explode(",", $info['require_membertype']);
		if (!empty($pb_userinfo)) {
			$membertype_id = $pdb->GetOne("SELECT membertype_id FROM {$tb_prefix}members WHERE id='".$pb_user['pb_userid']."'");
			if (!in_array($membertype_id, $require_ids)) {
				$info['content'] = L("news_membertype_not_allowed", "tpl");
			}
		}else{
			$info['content'] = L("news_membertype_not_allowed", "tpl");
		}
	}
	if ($info['type'] == 1) {
		$info['source'] = L("company_news", "tpl");
		$info['content'] = "<a href='".$info['content']."'>".$info['content']."</a>";
	}
	setvar("item",$info);
}else{
    flash();
}
setvar("Newstypes",$_PB_CACHE['newstype']);
render("news.detail");
?>