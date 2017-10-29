<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1172 $
 */
define('CURSCRIPT', 'announce');
require("libraries/common.inc.php");
require("share.inc.php");
uses("announcement");
$announce = new Announcements();
$viewhelper->setTitle(L("announce", "tpl"));
$viewhelper->setPosition(L("announce", "tpl"), "announce.php");
$viewhelper->setPosition(L("lists", "tpl"));
if (isset($_GET['title'])) {
	$title = rawurldecode(trim($_GET['title']));
	$res = $announce->findBySubject($title);
	$id = $res['id'];
}
if (isset($_GET['id'])) {
	$id = intval($_GET['id']);
}
if(!empty($id)){
	$result = $announce->findById($id);
	if (!empty($result)) {
		$result['message'] = nl2br($result['message']);
		$viewhelper->setTitle($result['subject']);
		$viewhelper->setPosition($result['subject']);
		$viewhelper->setMetaDescription($result['message']);
		setvar("item", $result);
		setvar("PageTitle", strip_tags($result['subject']));
		render("announce.detail", true);
	}
}
$result = $announce->findAll("*", null, null, "display_order ASC,id DESC");
if (!empty($result)) {
	for($i=0; $i<count($result); $i++){
		if (!empty($result[$i]['created'])) {
			$result[$i]['pubdate'] = date("Y-m-d", $result[$i]['created']);
		}
	}
	setvar("Items", $result);
}
render("announce");
?>