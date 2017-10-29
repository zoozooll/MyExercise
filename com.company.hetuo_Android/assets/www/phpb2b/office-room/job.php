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
require(LIB_PATH .'time.class.php');
uses("job", "typeoption");
check_permission("job");
$job = new Jobs();
$typeoption = new Typeoption();
$tpl_file = "job";
if (empty($companyinfo)) {
	flash("pls_complete_company_info", "company.php", 0);
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (isset($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	if($do=="del" && !empty($id)){
		$job->del($id, "member_id=".$_SESSION['MemberID']);
	}
	if($do == "edit"){
		setvar("Genders", $typeoption->get_cache_type('gender'));
		setvar("Educations", $typeoption->get_cache_type('education'));
		setvar("Salary", $typeoption->get_cache_type('salary'));
		setvar("Worktype", $typeoption->get_cache_type('work_type'));
		if(!empty($id)){
			$res = $job->read("*", $id, null, "Job.member_id=".$_SESSION['MemberID']);
			if (empty($res)) {
				flash("action_failed");
			}
			$res['expire_date'] = date("Y-m-d", $res['expire_time']);
			setvar("item",$res);
		}
		$tpl_file = "job_edit";
		template($tpl_file);
		exit;
	}
}
if (!empty($_POST['job']) && $_POST['save']) {
	$vals = $_POST['job'];
	pb_submit_check('job');
	$now_job_amount = $job->findCount(null, "created>".$today_start." AND member_id=".$_SESSION['MemberID']);
	if (isset($_POST['id'])) {
		$id = $_POST['id'];
	}
	if(!empty($_POST['expire_time'])) {
		$vals['expire_time'] = Times::dateConvert($_POST['expire_time']);
	}
	$check_job_update = $g['job_check'];
	if ($check_job_update=="0") {
		$vals['status'] = 1;
        $message_info = 'msg_wait_success';
	}else {
		$vals['status'] = 0;
		$message_info = 'msg_wait_check';
	}
	if(!empty($id)){
		$vals['modified'] = $time_stamp;
		$result = $job->save($vals, "update", $id, null, "member_id=".$_SESSION['MemberID']);
	}else{
    	if ($g['max_job'] && $now_job_amount>=$g['max_job']) {
    		flash('one_day_max');
    	}
		$vals['created'] = $vals['modified'] = $time_stamp;
		$vals['company_id'] = $companyinfo['id'];
		$vals['member_id'] = $_SESSION['MemberID'];
		$vals['cache_spacename'] = $pdb->GetOne("SELECT space_name FROM {$tb_prefix}members WHERE id=".$_SESSION['MemberID']);
		$result = $job->save($vals);
	}
	if(!$result){
		flash();
	}else{
		flash($message_info);
	}
}
$result = $job->findAll("*", null, "Job.member_id=".$_SESSION['MemberID'], "id DESC", 0, 10);
if (!empty($result)) {
	for ($i=0; $i<count($result); $i++){
		$result[$i]['pubdate'] = date("Y-m-d", $result[$i]['created']);
		$result[$i]['expire_date'] = date("Y-m-d", $result[$i]['expire_time']);
	}
	setvar("Items",$result);
}
setvar("Worktype", $typeoption->get_cache_type("work_type"));
setvar("Salary", $typeoption->get_cache_type("salary"));
template($tpl_file);
?>