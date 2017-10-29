<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1025 $
 */
define('CURSCRIPT', 'register');
require("libraries/common.inc.php");
require("share.inc.php");
if (session_id() == '' ) { 
	require_once(LIB_PATH. "session_php.class.php");
	$session = new PbSessions();
}
require(PHPB2B_ROOT."libraries/sendmail.inc.php");
require(CACHE_PATH. 'cache_membergroup.php');
require(CACHE_PATH. 'cache_setting1.php');
require(LIB_PATH.'passport.class.php');
require(PHPB2B_ROOT. "languages/".$app_lang."/emails.inc.php");
$passport = new Passports();
uses("member","company","companyfield", "memberfield","membergroup");
$cfg['reg_time_seperate'] = 3*60;
$memberfield = new Memberfields();
$member = new Members();
$membergroup = new Membergroups();
$company = new Companies();
$companyfield = new Companyfields();
$check_invite_code = false;
$register_type = $_PB_CACHE['setting']['register_type'];
$ip_reg_sep = $_PB_CACHE['setting']['ip_reg_sep'];
$forbid_ip = $_PB_CACHE['setting']['forbid_ip'];
$conditions = array();
capt_check("capt_register");
$tpl_file = "register";
$member_reg_auth = $_PB_CACHE['setting']['new_userauth'];
if (isset($_GET['action'])) {
	$do = trim($_GET['action']);
	if ($do == "done") {
		$tpl_file = "register.done";
		$reg_tips = null;
		$reg_result = true;
		$is_company = false;
		if ($member_reg_auth) {
			switch ($member_reg_auth) {
				case 1:
					$reg_tips = L("pls_active_your_account");
					$reg_result = false;
					break;
				default:
					$reg_tips = L("pls_wait_for_check");
					$reg_result = false;
					break;
			}
		}else{
			$member_info = $pdb->GetRow("SELECT membergroup_id,membertype_id FROM {$tb_prefix}members WHERE id='".$pb_user['pb_userid']."'");
			$gid = $member_info['membergroup_id'];
			$smarty->assign("groupname", $_PB_CACHE['membergroup'][$gid]['name']);
			$smarty->assign("groupimg", "images/group/".$_PB_CACHE['membergroup'][$gid]['avatar']);
			if ($member_info['membertype_id'] == 2) {
				$is_company = true;
			}
		}
		$smarty->assign("is_company", $is_company);
		$smarty->assign("result", $reg_result);
		$smarty->assign("RegTips", $reg_tips);
		render($tpl_file, true);
	}
}
if (!empty($ip_reg_sep)) {
	$cfg['reg_time_seperate'] = $ip_reg_sep*60*60;
}
if ($register_type=="close_register") {
	flash("register_closed", URL);
}elseif ($register_type=="open_invite_reg"){
	setvar("IfInviteCode", true);
	$check_invite_code = true;
}
if(isset($_POST['register'])){
	$is_company = false;
	$if_need_check = false;
	$register_type = trim($_POST['register']);
	switch ($register_type) {
		case "personal":
			$default_membergroupid = $pdb->GetOne("SELECT default_membergroup_id FROM {$tb_prefix}membertypes WHERE id=1");
			break;
		case "company":
			$default_membergroupid = $pdb->GetOne("SELECT default_membergroup_id FROM {$tb_prefix}membertypes WHERE id=2");
			$is_company = true;
			break;
		default:
			$default_membergroupid = $membergroup->field("id","is_default=1");
			break;
	}
	$member->setParams();
	$memberfield->setParams();
	$member->params['data']['member']['membergroup_id'] = $default_membergroupid;
	$time_limits = $pdb->GetOne("SELECT default_live_time FROM {$tb_prefix}membergroups WHERE id={$default_membergroupid}");
	$member->params['data']['member']['service_start_date'] = $time_stamp;
	$member->params['data']['member']['service_end_date'] = $membergroup->getServiceEndtime($time_limits);
	$member->params['data']['member']['membertype_id'] = ($is_company)?2:1;
	if($member_reg_auth=="1" || $member_reg_auth!=0 || !empty($_PB_CACHE['setting']['new_userauth'])){
		$member->params['data']['member']['status'] = 0;
		$if_need_check = true;
	}else{
		$member->params['data']['member']['status'] = 1;
	}
	$updated = false;
	$updated = $member->Add();
	if ($member_reg_auth == 1) {
		$if_need_check = true;
		$exp_time = $time_stamp+86400;
		$tmp_username = $member->params['data']['member']['username'];
		$hash = rawurlencode(authcode("{$tmp_username}\t".$exp_time, "ENCODE"));
		setvar("hash", $hash);
		setvar("expire_date", date("Y-m-d",strtotime("+1 day")));
		$sended = pb_sendmail(array($member->params['data']['member']['email'], $member->params['data']['member']['username']), L("pls_active_your_account", "tpl"), "activite");
	}
	if (!empty($_PB_CACHE['setting1']['welcome_msg'])) {
		setvar("user_name", $member->params['data']['member']['username']);
		$sended = pb_sendmail(array($member->params['data']['member']['email'], $member->params['data']['member']['username']), L("thx_for_your_reg", "tpl", $_PB_CACHE['setting']['site_name']), "welcome");
	}
	if($updated){
		$key = $member->table_name."_id";
		$last_member_id = $member->$key;
		$gopage = URL.'register.php?action=done';
		pheader("location:".$gopage);
	}else{
		setvar("member", $_POST['data']['member']);
		if(isset($_POST['data']['memberfield'])) setvar("memberfield", $_POST['data']['memberfield']);
	}
}
formhash();
setvar("sid",md5(uniqid($time_stamp)));
setvar("testname",pb_radom(6));
render($tpl_file);
?>