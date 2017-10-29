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
uses("newscomment");
require(LIB_PATH. 'page.class.php');
require("session_cp.inc.php");
$newscomment = new Newscomments();
$page = new Pages();
$conditions = array();
$tpl_file = "newscomment";
$amount = $newscomment->findCount(null, $conditions,"id");
$page->setPagenav($amount);
$joins[] = "LEFT JOIN {$tb_prefix}newses n ON n.id=Newscomment.news_id";
$newscomment_list = $newscomment->findAll("Newscomment.id,Newscomment.news_id,Newscomment.message,Newscomment.cache_username as username,Newscomment.date_line AS pubdate,n.title", $joins, $conditions, "id DESC", $page->firstcount, $page->displaypg);
setvar("Items",$newscomment_list);
uaAssign(array("ByPages"=>$page->pagenav));
if (isset($_POST['del']) && is_array($_POST['id'])) {
	$deleted = $newscomment->del($_POST['id']);
	if (!$deleted) {
		flash();
	}
}
template($tpl_file);
?>