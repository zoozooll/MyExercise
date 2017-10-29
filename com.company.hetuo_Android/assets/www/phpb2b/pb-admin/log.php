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
uses("log");
require(PHPB2B_ROOT.'./libraries/page.class.php');
require("session_cp.inc.php");
$log = new Logs();
$page = new Pages();
$conditions = array();
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if ($do == "clear") {
		$result = $pdb->Execute("truncate {$tb_prefix}logs");
	}
	if ($do == "del" && !empty($_GET['id'])) {
		$log->del($_GET['id']);
	}
	if($do == 'search'){
		if(!empty($_GET['q'])){
			$conditions[] = "description like '%".$_GET['q']."%'";
		}
	}
}
$amount = $log->findCount(null, $conditions, "id");
$page->setPagenav($amount);
$result = $log->findAll("id,handle_type,source_module,description,created,created AS pubdate", null, $conditions, "id DESC ",$page->firstcount,$page->displaypg);
if(!empty($result)){
	for($i=0; $i<count($result); $i++){
		$result[$i]['label'] = "images/e_".$result[$i]['handle_type'].".gif";
		$result[$i]['pubdate'] = date("Y-m-d H:i:s", $result[$i]['created']);
	}
	setvar("Items", $result);
}
setvar("ByPages",$page->pagenav);
template("log");
?>