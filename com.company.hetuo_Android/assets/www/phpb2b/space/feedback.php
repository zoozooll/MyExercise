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
uses("message");
$companymessage = new Messages();
if (isset($_POST['companyid']) && !empty($_POST['feed']) && !empty($pb_user)) {
	$vals = $_POST['feed'];
	$vals['created'] = $time_stamp;
	$vals['status'] = 0;
	$vals['from_member_id'] = $pb_user['pb_userid'];
	$vals['cache_from_username'] = $pb_user['pb_username'];
	if (empty($member->info)) {
		$vals['to_member_id'] = $administrator_id;
		$vals['cache_to_username'] = $pdb->GetOne("SELECT username FROM {$tb_prefix}members WHERE id=".$administrator_id);
	}else{
		$vals['to_member_id'] = $member->info['id'];
		$vals['cache_to_username'] = $member->info['username'];
	}
	$vals['title'] = L("pms_from_space", "tpl");
	if($companymessage->save($vals)){
		$smarty->flash('feedback_already_submit', null, 0);
	}
}
formhash();
$space->render("feedback");
?>