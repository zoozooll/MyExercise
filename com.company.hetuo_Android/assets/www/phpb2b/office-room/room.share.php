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
if (session_id() == '' ) { 
	require_once(LIB_PATH. "session_php.class.php");
	$session = new PbSessions();
}
$office_theme_name = "";
require(PHPB2B_ROOT.'languages'.DS.$app_lang.DS.'template.room.inc.php');
require(CACHE_PATH. "cache_membergroup.php");
uses("member", "memberfield", "company");
$member = new Members();
$memberfield = new Memberfields();
$company = new Companies();
$company_controller = new Company();
$smarty->template_dir = "template/";
$smarty->setCompileDir("office-room".DS);
$smarty->flash_layout = "flash";
$check_invite_code = false;
$pdb->setFetchMode(ADODB_FETCH_ASSOC);
if (isset($_PB_CACHE['setting']['register_type'])) {
	$register_type = $_PB_CACHE['setting']['register_type'];
	if ($register_type=="open_invite_reg"){
	    setvar("IfInviteCode", true);
	}
}
if (empty($_SESSION['MemberID']) || empty($_SESSION['MemberName'])) {
	uclearcookies();
	pheader("location:".URL."logging.php?forward=".urlencode(pb_get_host().$php_self));
}
//if caches
$cache_data = array();
if (isset($_PB_CACHE['setting']['member_cache']) && $_PB_CACHE['setting']['member_cache']) {
	$pdb->Execute("DELETE FROM {$tb_prefix}membercaches WHERE expiration<".$time_stamp);
	$cache_data = $pdb->GetRow("SELECT data1 AS member,data2 AS company FROM {$tb_prefix}membercaches WHERE member_id='".$_SESSION['MemberID']."'");
}
if (!empty($cache_data)) {
	$memberinfo = @unserialize($cache_data['member']);
	if (!empty($cache_data['company'])) {
		$companyinfo = @unserialize($cache_data['company']);
		setvar("COMPANYINFO", $companyinfo);
	}
	if(empty($memberinfo) || !is_array($memberinfo)){
		$memberinfo = $member->getInfoById($_SESSION['MemberID']);
		if ($memberinfo['membertype_id']==2) {
			$companyinfo = $pdb->GetRow("SELECT c.* FROM {$tb_prefix}companies c LEFT JOIN {$tb_prefix}companyfields cf ON c.id=cf.company_id WHERE c.member_id=".$memberinfo['id']);
			if (!empty($companyinfo)) {
				$company_id = $companyinfo['id'];
				$companyinfo['space_url'] = $company_controller->rewrite($companyinfo['cache_spacename']);
				setvar("COMPANYINFO", $companyinfo);
			}
		}
		if($_PB_CACHE['setting']['member_cache']) $pdb->Execute("REPLACE INTO {$tb_prefix}membercaches (member_id,data1,data2,expiration) VALUE ('".$_SESSION['MemberID']."','".@serialize($memberinfo)."','".@serialize($companyinfo)."',".($time_stamp+3600).")");
	}
}else{
	$memberinfo = $member->getInfoById($_SESSION['MemberID']);
	if ($memberinfo['membertype_id']==2) {
		$companyinfo = $pdb->GetRow("SELECT c.* FROM {$tb_prefix}companies c LEFT JOIN {$tb_prefix}companyfields cf ON c.id=cf.company_id WHERE c.member_id=".$memberinfo['id']);
		if (!empty($companyinfo)) {
			$company_id = $companyinfo['id'];
			$companyinfo['space_url'] = $company_controller->rewrite($companyinfo['cache_spacename']);
			setvar("COMPANYINFO", $companyinfo);
		}
	}
	if(isset($_PB_CACHE['setting']['member_cache']) && $_PB_CACHE['setting']['member_cache']) $pdb->Execute("REPLACE INTO {$tb_prefix}membercaches (member_id,data1,data2,expiration) VALUE ('".$_SESSION['MemberID']."','".@serialize($memberinfo)."','".@serialize($companyinfo)."',".($time_stamp+3600).")");
}
$g = $_PB_CACHE['membergroup'][$memberinfo['membergroup_id']];
if (!empty($g['auth_level'])) {
	$auth = sprintf("%05b", $g['auth_level']);
	$menu['basic'] = $auth[0];
	$menu['offer'] = $auth[1];
	$menu['product'] = $auth[2];
	$menu['company'] = $auth[3];
	$menu['pms'] = $auth[4];
	setvar("menu", $menu);
}
function check_permission($perm)
{
	global $g, $smarty;
	$allow = ($perm=="space")? "allow_space" : $perm."_allow";
	if (!$g[$allow]) {
		$message = L("have_no_perm", "msg", L($allow, "tpl"));
		$smarty->assign('action_img', "failed.png");
		$smarty->assign('url', 'javascript:;');
		$smarty->assign('message', $message);
		$smarty->assign('title', $message);
		$smarty->assign('page_title', strip_tags($message));
		template($smarty->flash_layout);
		exit();
	}
}
$new_pm = $pdb->GetOne("SELECT count(id) AS amount FROM {$tb_prefix}messages WHERE status='0' AND  to_member_id=".$_SESSION['MemberID']);
setvar("newpm", (empty($new_pm) || !$new_pm)? false : $new_pm);
if (!empty($arrTemplate)) {
    $smarty->assign($arrTemplate);
}
$today_start = mktime(0, 0, 0, date("m"), date("d"), date("Y"));
formhash();
?>