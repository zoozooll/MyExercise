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
uses("companynews","company","member");
require(PHPB2B_ROOT.'libraries/page.class.php');
require("session_cp.inc.php");
$member = new Members();
$page = new Pages();
$company = new Companies();
$companynews = new Companynewses();
$conditions = $joins = array();
$tpl_file = "companynews";
if (isset($_POST['del']) && is_array($_POST['id'])) {
	if (!$companynews->del($_POST['id'])) {
		flash();
	}
}
if (isset($_POST['check']) && (!empty($_POST['id'])) && is_array($_POST['id'])) {
	$strCompanyNewsId = implode(",", $_POST['id']);
	$strCompanyNewsId = "(".$strCompanyNewsId.")";
	$arrResult = $pdb->GetArray("select id,status from ".$companynews->getTable()." where id in ".$strCompanyNewsId);
	if (!empty($arrResult)){
	    foreach ($arrResult as $key=>$val){
	        if (1 == $val['status']) {
	        	$result = $pdb->Execute("update ".$companynews->getTable()." set status='0' where id=".$val['id']);
	        }else{
	            $result = $pdb->Execute("update ".$companynews->getTable()." set status='1' where id=".$val['id']);
	        }
	    }
	    if(!$result){
	    	flash();
	    }else{
	    	flash("success");
	    }
	}
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if ($do == "del" && $id) {
		if (!$companynews->del($id)) {
			flash();
		}
	}
	if ($do == 'search') {
		if (!empty($_GET['topic'])) $conditions[]= "Companynews.title like '%".trim($_GET['topic'])."%'";
		if (!empty($_GET['membername'])) $conditions[]= "Member.name='".$_GET['membername']."'";
		if (!empty($_GET['companyname'])) $conditions[]= "Company.company_name like '%".$_GET['companyname']."%'";
	}
	if ($do == "view") {
		if($id){
			$news_info = $pdb->GetRow("SELECT cn.*,c.name companyname FROM {$tb_prefix}companynewses cn LEFT JOIN {$tb_prefix}companies c ON c.id=cn.company_id WHERE cn.id=".$id);
			setvar("item",$news_info);
		}
		$tpl_file = "companynews.view";
		template($tpl_file);
		exit;
	}
}
$fields = "company_id,Companynews.id,Companynews.title,Companynews.status as CompanynewsStatus,Companynews.created,Companynews.clicked,c.name AS companyname";
$amount = $companynews->findCount(null, $conditions,"Companynews.id");
$page->setPagenav($amount);
$joins[] = "LEFT JOIN {$tb_prefix}companies c ON c.id=Companynews.company_id";
$result = $companynews->findAll($fields, $joins, $conditions, "Companynews.id DESC ",$page->firstcount,$page->displaypg);
if (!empty($result)) {
	for($i=0; $i<count($result); $i++){
		$result[$i]['pubdate'] = date("Y-m-d", $result[$i]['created']);
	}
	setvar("Items", $result);
}
template($tpl_file);
?>