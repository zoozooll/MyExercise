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
uses("company","member","area","companytype", "attachment", "membergroup");
require(LIB_PATH .'time.class.php');
require(LIB_PATH .'page.class.php');
require(CACHE_PATH. 'cache_area.php');
require(CACHE_PATH. 'cache_industry.php');
require(CACHE_PATH. 'cache_type.php');
require("session_cp.inc.php");
include(CACHE_PATH. "cache_typeoption.php");
$membergroup = new Membergroup();
$page = new Pages();
$company = new Companies();
$member = new Members();
$conditions = array();
$tpl_file = "company";
setvar('Membergroups', $membergroup->getUsergroups('define'));
setvar('AllMembergroups', $membergroup->getUsergroups('all'));
setvar("CompanyTypes", $_PB_CACHE['companytype']);
setvar("CheckStatus", $_PB_CACHE['check_status']);
setvar("Industries",$_PB_CACHE['industry']);
setvar("Areas",$_PB_CACHE['area']);
if (isset($_POST['del']) && !empty($_POST['id'])) {
	$result = $company->del($_POST['id']);
	if (!$result) {
		flash();
	}
}
if (isset($_POST['check'])){
	if (isset($_POST['check']['in'])) {
		$result = $company->check($_POST['id'],1);
	}elseif (isset($_POST['check']['out'])){
		$result = $company->check($_POST['id'],0);
	}
	if(!$result){
		flash();
	}
}
if (isset($_POST['set_group']) && !empty($_POST['id']) &&!empty($_POST['set_group'])) {
	$ids = "IN (".implode(",", array_unique($_POST['id'])).")";
	$member_ids = "IN (".implode(",", array_unique($_POST['member_id'])).")";
	$sql = "UPDATE {$tb_prefix}members m,{$tb_prefix}companies c SET c.cache_membergroupid='{$_POST['set_group']}',m.membergroup_id='{$_POST['set_group']}' WHERE c.member_id=m.id AND c.id ".$ids." AND m.id ".$member_ids;
	$pdb->Execute($sql);
}
if (isset($_POST['recommend'])){
	foreach($_POST['id'] as $val){
		$commend_now = $company->field("if_commend", "id=".$val);
		if($commend_now=="0"){
			$result = $company->saveField("if_commend", "1", intval($val));
		}else{
			$result = $company->saveField("if_commend", "0", intval($val));
		}
	}
	$company_ids = implode(",", $_POST['id']);
	$result = $pdb->Execute("update ".$company->getTable()." set status='1' where id in (".$company_ids.")");
	if($result){
		flash("success");
	}else {
		flash();
	}
}
if (isset($_POST['save'])) {
	$company_id = $_POST['id'];
	$vals = $_POST['data']['company'];
	if(isset($_POST['manage_type']))
	{
		$managetype = implode(",",$_POST['manage_type']);
		$vals['manage_type'] = $managetype;
	}
	if (isset($_POST['FoundDate'])) {
		$vals['found_date'] = Times::dateConvert($_POST['FoundDate']);
	}
	if(isset($_POST['company']['main_market'])) {
		$mainmarket = implode(",",$_POST['company']['main_market']);
		$vals['main_market'] = $mainmarket;
	}
	if (isset($_POST['data']['username'])) {
		$username = trim($_POST['data']['username']);
		$user_info = $member->checkUserExist($username, true);
		if ($user_info) {
			$vals['member_id'] = $member->info['id'];
		}
	}
	if(!empty($company_id)){
		$vals['modified'] = $time_stamp;
		$result = $company->save($vals, "update", $company_id);
	}else{
		$vals['created'] = $vals['modified'] = $time_stamp;
		$result = $company->save($vals);
	}
	if(!$result){
		flash();
	}
}

if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (isset($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if ($do == "search") {
		if (!empty($_GET['member']['username'])) {
			$joins[] = "LEFT JOIN {$tb_prefix}members m ON m.id=Company.member_id";
			$conditions[]= "m.username like '%".$_GET['member']['username']."%'";
		}
		if (!empty($_GET['company']['name'])) $conditions[]= "Company.name like '%".$_GET['company']['name']."%'";
		if (!empty($_GET['FromDate']) && $_GET['FromDate']!="None" && $_GET['ToDate'] && $_GET['ToDate']!="None") {
			$condition= "m.created BETWEEN ";
			$condition.= Times::dateConvert($_GET['FromDate']);
			$condition.= " AND ";
			$condition.= Times::dateConvert($_GET['ToDate']);
			$conditions[] = $condition;
		}
		if (!empty($_GET['industryid'])) $conditions[]= "Company.industry_id=".$_GET['industryid'];
		if (isset($_GET['status']) && $_GET['status']>=0) $conditions[]= "Company.status=".$_GET['status'];
		if ($_GET['companytype']) $conditions[]= "Company.type_id=".$_GET['companytype'];
	}	
    if ($do == "edit") {
    	if(!empty($id)){
    		$sql = "SELECT c.*,m.username,m.membergroup_id,m.credits FROM {$tb_prefix}companies c LEFT JOIN {$tb_prefix}members m ON c.member_id=m.id WHERE c.id=".$id;
    		$res = $pdb->GetRow($sql);
    		setvar("item",$res);
    		$selected['properties'] = explode(",",$res['manage_type']);
    		setvar("SelectedManageType",$selected['properties']);
    		$selected['markets'] = explode(",",$res['main_market']);
    		setvar("SelectedMarket",$selected['markets']);
    	}
    	uaAssign(array(
    	"CompanyProperty"=>$_PB_CACHE['economic_type'],
    	"ManageTypes"=>$_PB_CACHE['manage_type'],
    	"MainMarkets"=>$_PB_CACHE['main_market'],
    	"CompanyFunds"=>$_PB_CACHE['reg_fund'],
    	"CompanyAnual"=>$_PB_CACHE['year_annual'],
    	"LinkmanPositions"=>$_PB_CACHE['position'],
    	"EmployeeAmounts"=>$_PB_CACHE['employee_amount'],
    	"Genders"=>$_PB_CACHE['gender'])
    	);
    	$tpl_file = "company.edit";
    	template($tpl_file);
    	exit;
    }
}
$fields = "Company.id,m.space_name,Company.cache_spacename,m.membergroup_id,m.credits,member_id,m.username,Company.name AS CompanyName,Company.status AS CompanyStatus,Company.created AS pubdate,Company.if_commend,area_id1,area_id2,area_id3,industry_id1,industry_id2,industry_id3,cache_credits";
$total_amount = $pdb->GetOne("SELECT COUNT(id) AS amount FROM ".$tb_prefix."companies WHERE status='0'");
$amount = $company->findCount(null, $conditions,"Company.id");
$page->setPagenav($amount);
$joins = array();
$joins[] = "LEFT JOIN {$tb_prefix}members m ON m.id=Company.member_id";
if(empty($lists)){
    $lists = $company->findAll($fields,$joins,$conditions,"Company.id DESC",$page->firstcount,$page->displaypg);
}
setvar("Items", $lists);
uaAssign(array("ByPages"=>$page->pagenav, "TotalAmount"=>$total_amount));
template($tpl_file);
?>