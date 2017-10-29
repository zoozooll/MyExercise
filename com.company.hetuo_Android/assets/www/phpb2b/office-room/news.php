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
require("room.share.php");
require(PHPB2B_ROOT.'./libraries/page.class.php');
include(CACHE_PATH. "cache_type.php");
uses("companynews", "typeoption");
check_permission("companynews");
$companynews = new Companynewses();
$typeoption = new Typeoption();
$tables = $companynews->getTable(true);
$tpl_file = "news";
$page = new Pages();
setvar("CompanynewsTypes", $_PB_CACHE['companynewstype']);
if(isset($company_id)){
$conditions = "company_id=".$company_id;
}
if (empty($companyinfo)) {
	flash("pls_complete_company_info", "company.php", 0);
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if ($do == "edit") {
		$company->newCheckStatus($companyinfo['status']);
		if(!empty($id)){
			$res = $companynews->read("Companynews.id AS ID,title AS Title,content AS Content,type_id,created AS CreateDate",$id);
			setvar("item",$res);
			setvar("ShowCaption","none");
		}
		$tpl_file = "news_edit";
		template($tpl_file);
		exit;
	}
}
if (isset($_POST['save'])) {
	pb_submit_check('title');
	$vals = null;
	$vals['title'] = trim($_POST['title']);
	$vals['content'] = trim($_POST['content']);
	$vals['type_id'] =$_POST['type_id'];
	$now_companynews_amount = $companynews->findCount(null, "created>".$today_start." AND member_id=".$_SESSION['MemberID']);
    if ($g['companynews_check']) {
        $vals['status'] = 0;
        $msg = 'msg_wait_check';
    }else {
        $vals['status'] = 1;
        $msg = 'success';
    }	
	if(!empty($_POST['newsid'])){
		$vals['modified'] = $time_stamp;
		$companynews->save($vals, "update",$_POST['newsid'],null, "member_id=".$_SESSION['MemberID']);
		pheader("location:news.php?action=list");
	}else {
    	if ($g['max_companynews'] && $now_companynews_amount>=$g['max_companynews']) {
    		flash('one_day_max');
    	}
		$vals['created'] = $time_stamp;
		$vals['member_id'] = $_SESSION['MemberID'];
		$vals['company_id'] = $company_id;
		$result = $companynews->save($vals);
		flash($msg);
	}
}
if (isset($_POST['del'])) {
	$result = $companynews->del($_POST['newsid'], $conditions);
	if ($result) {
		flash("success");
	}else {
		flash("action_failed");
	}
}
$amount = $companynews->findCount(null, $conditions);
$page->setPagenav($amount);
$fields = "title as CompanynewsTitle,status,created,type_id,id as CompanynewsId";
$result = $companynews->findAll($fields,null, $conditions,"id DESC",$page->firstcount,$page->displaypg);
setvar("CheckStatus", $typeoption->get_cache_type("check_status"));
for($i=0;$i<count($result);$i++){
	$result[$i]['pubdate'] = date("Y-m-d", $result[$i]['created']);
}
setvar("Items", $result);
setvar("ByPages",$page->pagenav);
template($tpl_file);
?>