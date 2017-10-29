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
if(!defined('IN_PHPB2B')) exit('Not A Valid Entry Point');
require(PHPB2B_ROOT.'libraries/page.class.php');
uses("companynews");
$page = new Pages();
$page->is_rewrite = true;
$page->_url = $space->rewriteList("news");
$companynews = new Companynewses();
$conditions = "Companynews.company_id=".$company->info['id'];
if (isset($_GET['nid'])) {
	$id = intval(($_GET['nid']));
	if ($id) {
		$info = $companynews->read("*", intval($_GET['nid'], $conditions));
		if (empty($info)) {
			flash('data_not_exists', null, 0);
		}
		$tpl_file = "news_detail";
		setvar("item",$info);
		$space->render($tpl_file);
		exit;
	}
}
$amount = $companynews->findCount(null, $conditions,"Companynews.id");
$page->setPagenav($amount);
$result = $companynews->findAll("id,title,content,created,created AS pubdate",null, $conditions,"Companynews.id DESC",$page->firstcount,$page->displaypg);
if (!empty($result)) {
	for($i=0; $i<count($result); $i++){
		$result[$i]['url'] = $space->rewriteDetail("news", $result[$i]['id']);
	}
	setvar("Items", $result);
	setvar("ByPages",$page->pagenav);
}
$space->render("news");
?>