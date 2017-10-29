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
uses("attachment", "typeoption");
$attachment = new Attachment('photo');
$member = new Members();
$member_controller = new Member();
$typeoption = new Typeoption();
$conditions = null;
if (isset($_POST['save'])) {
	pb_submit_check('member');
	$vals['office_redirect'] = $_POST['member']['office_redirect'];
	$vals['email'] =  $_POST['member']['email'];
	if (!empty($_FILES['photo']['name'])) {
		$attachment->insert_new = false;
		$attachment->if_orignal = false;
		$attachment->if_watermark = false;
		$attachment->rename_file = "photo-".$_SESSION['MemberID'];
		$attachment->upload_process();
		$vals['photo'] = $attachment->file_full_url;
	}
	$result = $member->save($vals, "update", $_SESSION['MemberID']);
	$memberfield->primaryKey = "member_id";
	$result = $memberfield->save($_POST['memberfield'], "update", $_SESSION['MemberID']);
	$member->clearCache($_SESSION['MemberID']);
	if(isset($_POST['personal']['resume_status']))
	$result = $pdb->Execute("REPLACE INTO {$tb_prefix}personals (member_id,resume_status,max_education) VALUE (".$_SESSION['MemberID'].",'".$_POST['personal']['resume_status']."','".$_POST['personal']['max_education']."')");
	if(!$result){
		flash('action_failed');
	}else{
		flash('success');
	}
}
setvar("Genders", $typeoption->get_cache_type('gender', null, array(-1)));
setvar("Educations", $typeoption->get_cache_type('education'));
setvar("OfficeRedirects", explode(",", L("office_redirects", "tpl")));
$personal =  $pdb->GetRow("SELECT * FROM {$tb_prefix}personals WHERE member_id=".$_SESSION['MemberID']);
setvar("resume_status",$personal['resume_status']);
setvar("max_education",$personal['max_education']);
if (!empty($memberinfo['photo'])) {
	$memberinfo['image'] = pb_get_attachmenturl($memberinfo['photo'], "../", "small");
}
setvar("item",$memberinfo);
template("personal");
?>