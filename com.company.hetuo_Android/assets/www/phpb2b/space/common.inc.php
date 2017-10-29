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
require(PHPB2B_ROOT.'languages/'.$app_lang.'/'.'template.space.inc.php');
require(CACHE_PATH. 'cache_membergroup.php');
uses("templet","member","company","membertype","space");
$member = new Members();
$space = new Space();
$membertype= new Membertypes();
$company = new Companies();
$templet = new Templets();
$space_name = '';
if (empty($theme_name)) {
	include("share.inc.php");
}
$pdb->setFetchMode(ADODB_FETCH_ASSOC);
$smarty->flash_layout = $theme_name."/flash";
$smarty->assign("theme_img_path", "templates/".$theme_name."/");
$smarty->assign('ThemeName', $theme_name);
//if caches
$cache_data = array();
if (isset($_PB_CACHE['setting']['space_cache']) && $_PB_CACHE['setting']['space_cache']) {
	$pdb->Execute("DELETE FROM {$tb_prefix}spacecaches WHERE expiration<".$time_stamp);
}
if (!empty($userid)) {
	$userid = rawurldecode($userid);
	if (isset($_PB_CACHE['setting']['space_cache']) && $_PB_CACHE['setting']['space_cache']) {
		$cache_data = $pdb->GetRow("SELECT data1 AS member,data2 AS company FROM {$tb_prefix}spacecaches WHERE cache_spacename='".$userid."'");
		$company->info = @unserialize($cache_data['company']);
		$member->info = @unserialize($cache_data['member']);
		//re else below.
		if (empty($company->info) || !is_array($company->info)) {
			$member->setInfoBySpaceName($userid);
			if(!empty($member->info['id'])) {
				$company->setInfoByMemberId($member->info['id']);
			}else{
				$company->setInfoBySpaceName($userid);
			}
			if ($_PB_CACHE['setting']['space_cache']) $pdb->Execute("REPLACE INTO {$tb_prefix}spacecaches (cache_spacename,company_id,data1,data2,expiration) VALUE ('".$userid."','".$company->info['id']."','".@serialize($member->info)."','".@serialize($company->info)."',".($time_stamp+3600).")");
		}		
	}else{
		$member->setInfoBySpaceName($userid);
		if(!empty($member->info['id'])) {
			$company->setInfoByMemberId($member->info['id']);
		}else{
			$company->setInfoBySpaceName($userid);
		}
		if (isset($_PB_CACHE['setting']['space_cache']) && $_PB_CACHE['setting']['space_cache']) $pdb->Execute("REPLACE INTO {$tb_prefix}spacecaches (cache_spacename,company_id,data1,data2,expiration) VALUE ('".$userid."','".$company->info['id']."','".@serialize($member->info)."','".@serialize($company->info)."',".($time_stamp+3600).")");
	}
}elseif(!empty($_GET['id'])) {
	$id = intval($_GET['id']);
	if ($_PB_CACHE['setting']['space_cache']) {
		$cache_data = $pdb->GetRow("SELECT data1 AS member,data2 AS company FROM {$tb_prefix}spacecaches WHERE company_id='".$id."'");
		$company->info = @unserialize($cache_data['company']);
		$member->info = @unserialize($cache_data['member']);
	}else{
		$company->setId($id);
		if (!empty($company->info['member_id'])) {
			$member->setId($company->info['member_id']);
		}
		if ($_PB_CACHE['setting']['space_cache']) $pdb->Execute("REPLACE INTO {$tb_prefix}spacecaches (cache_spacename,company_id,data1,data2,expiration) VALUE ('".$company->info['cache_spacename']."','".$id."','".@serialize($member->info)."','".@serialize($company->info)."',".($time_stamp+3600).")");
	}
}
if (isset($company->info['status']) && $company->info['status']===0) {
    $smarty->flash('company_checking', null, 0);
}elseif (empty($company->info) || !$company->info) {
	$smarty->flash('data_not_exists', null, 0);
}
if(!empty($company->info['created'])){
	$time_tmp = $time_stamp-$company->info['created'];
	$company->info['year_sep'] = $time_tmp = ceil($time_tmp/(3600*24*365));
}
if (empty($company->info['email'])) {
	$company->info['email'] = $_PB_CACHE['setting']['service_email'];
}
if (empty($company->info['picture'])) {
	$company->info['logo'] = URL.pb_get_attachmenturl('', '', 'big');
}else{
	$company->info['logo'] = URL.$attachment_url.$company->info['picture'];
}
$pdb->setFetchMode(ADODB_FETCH_BOTH);
$company->info['description'] = nl2br(strip_tags($company->info['description']));
$member_templet_id = $member->info['templet_id'];
if (isset($_GET['force_templet_id'])) {
	$member_templet_id = intval($_GET['force_templet_id']);
}
if(!empty($member_templet_id)){
	$skin_path_info = $pdb->GetRow("SELECT name,directory FROM {$tb_prefix}templets WHERE type='user' AND status='1' AND id='".$member_templet_id."'");
}
if (empty($skin_path_info)) {
	$skin_path_info = $pdb->GetRow("SELECT name,directory FROM {$tb_prefix}templets WHERE type='user' AND is_default='1'");
	if (empty($skin_path_info)) {
		$skin_path_info = array();
		$skin_path_info[] = "default";
		$skin_path_info[] = "skins/default/";
	}
}
list($skin_path, $skin_dir) = $skin_path_info;
uaAssign(array(
"SkinName"=>$skin_path,
"ThemeName"=>$skin_path,
"SkinPath"=>$skin_dir,
"COMPANY"=>$company->info,
"MEMBER"=>$member->info,
));
$smarty->template_dir = PHPB2B_ROOT ."skins".DS;
$smarty->flash_layout = $skin_path."/flash";
if (!$smarty->template_exists($skin_path."flash")) {
	setvar("SkinName", "default");
	$smarty->template_dir = PHPB2B_ROOT ."skins".DS;
	$smarty->flash_layout = 'default/flash';
}
$smarty->setCompileDir("skin".DS.$skin_path.DS);
if(isset($member->info['id'])) $space->setLinks($member->info['id']);
$space->setMenu($company->info['cache_spacename'], $space_actions);
$product_types = $pdb->GetArray("SELECT id as typeid,name as typename,created,level FROM {$tb_prefix}producttypes WHERE company_id=".$company->info['id']);
if (!empty($product_types)) {
	for($i=0; $i<count($product_types); $i++){
		if($rewrite_able) {
			$product_types[$i]['url'] = $space->rewriteList("product")."type-".$product_types[$i]['typeid'].".html";
		}else{
			$product_types[$i]['url'] = $space->rewriteList("product", "&typeid=".$product_types[$i]['typeid']);
		}
	}
}
setvar("ProductTypes",$product_types);
$group_info = array();
$group_info['year'] = $time_tmp;
if (!empty($member->info['membergroup_id']['name'])) {
	$group_info['name'] = $_PB_CACHE['membergroup'][$member->info['membergroup_id']]['name'];
}else{
	$group_info['name'] = L("undefined_image", "tpl");
}
if (!empty($member->info['membergroup_id']['avatar'])) {
	$group_info['image'] = URL."images/group/".$_PB_CACHE['membergroup'][$member->info['membergroup_id']]['avatar'];
}else{
	$group_info['image'] = URL."images/group/formal.gif";
}
setvar("GROUP", $group_info);
//for old version
if(isset($member->info['membergroup_id']['name'])) setvar("GroupName", $_PB_CACHE['membergroup'][$member->info['membergroup_id']]['name']);
if(isset($member->info['membergroup_id']['avatar'])) setvar("GroupImage", URL."images/group/".$_PB_CACHE['membergroup'][$member->info['membergroup_id']]['avatar']);
//:~
setvar("Menus", $space->getMenu());
setvar("Links", $space->getLinks());
setvar("BASEMAP", URL.$skin_dir);
if (!empty($arrTemplate)) {
    $smarty->assign($arrTemplate);
}
?>