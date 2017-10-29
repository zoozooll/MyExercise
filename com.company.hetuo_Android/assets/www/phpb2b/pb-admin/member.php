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
require("session_cp.inc.php");
uses("member","membergroup","typeoption");
require(LIB_PATH. 'time.class.php');
require(PHPB2B_ROOT. 'libraries/page.class.php');
include CACHE_PATH. "cache_type.php";
include CACHE_PATH. "cache_membergroup.php";
include CACHE_PATH. "cache_trusttype.php";
$typeoption = new Typeoption();
$membergroup = new Membergroup();
$member = new Members();
$page = new Pages();
$tpl_file = "member";
$conditions = array();
setvar("MembergroupOptions", $membergroup->getUsergroups());
setvar("Membergroups", $_PB_CACHE['membergroup']);
setvar("Membertypes", $_PB_CACHE['membertype']);
foreach($_PB_CACHE['trusttype'] as $key=>$val){
	$tmp_trusttypes[$key] = $val['name'];
}
setvar("Trusttypes", $tmp_trusttypes);
if (isset($_POST['del'])) {
      $member->Delete($_POST['id']);
	  flash("success");
}
if (isset($_POST['check_in'])){
	$vals['status'] = 1;
	if(!$member->save($vals, "update", $member_id)){
		flash();
	}
}
if (isset($_POST['check_out'])){
	$vals['status'] = 0;
	if(!$member->save($vals, "update", $member_id)){
		flash();
	}
}
if (isset($_POST['pb_action']) && !empty($_POST['id'])) {
	list($action_name, $action_id) = explode("_", $_POST['pb_action']);
	$ids = "(".implode(",", $_POST['id']).")";
	switch ($action_name) {
		case "status":
			$sql = "UPDATE {$tb_prefix}members SET status='".$action_id."' WHERE id IN ".$ids;
			break;
		case "membertype":
			$sql = "UPDATE {$tb_prefix}members SET membertype_id='".$action_id."' WHERE id IN ".$ids;
			break;
		case "membergroup":
			$sql = "UPDATE {$tb_prefix}members SET membergroup_id='".$action_id."' WHERE id IN ".$ids;
			break;
		default:
			break;
	}
	$result = $pdb->Execute($sql);
	if (!$result) {
		flash();
	}
}
if (isset($_POST['save'])) {
	if (isset($_POST['id'])) {
		$member_id = $_POST['id'];
	}
	$vals = $_POST['data']['member'];
	if(!empty($_POST['data']['userpass'])) {
		if (!pb_strcomp($_POST['data']['userpass'],$_POST['data']['re_userpass'])) {
			flash("invalid_password");
		}else{
			$vals['userpass'] = $member->authPasswd($_POST['data']['userpass']);
		}
	}
		if (!empty($_POST['data']['trusttype'])) {
			$vals['trusttype_ids'] = implode(",", $_POST['data']['trusttype']);
		}
	if (!empty($_POST['data']['service_start_date'])) {
		$vals['service_start_date'] = Times::dateConvert($_POST['data']['service_start_date']);
	}	if (!empty($_POST['data']['service_end_date'])) {
		$vals['service_end_date'] = Times::dateConvert($_POST['data']['service_end_date']);
	}
	if(!empty($member_id)){
		$vals['modified'] = $time_stamp;
		if (!empty($vals['space_name'])) {
			$member->updateSpaceName(array('id'=>$member_id), $vals['space_name']);
		}
		$result = $member->save($vals, "update", $member_id);
	}else{
		$vals['status'] = 1;
		$vals['created'] = $vals['modified'] = $time_stamp;
		$result = $member->save($vals);
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
	if ($do == "edit") {
		$vals =  null;
		if (!empty($id)){
			$res = $pdb->GetRow("SELECT m.*,mf.* FROM {$tb_prefix}members m LEFT JOIN {$tb_prefix}memberfields mf ON m.id=mf.member_id WHERE m.id={$id}");
			if (empty($res)) {
				flash("data_not_exists");
			}
			if (!empty($res['trusttype_ids'])) {
				$tmp_user_trusttype = explode(",", $res['trusttype_ids']);
				$res['selected_trusttypeid'] = $tmp_user_trusttype;
				unset($tmp_user_trusttype);
			}
			if (!empty($res['membergroup_id'])) {
				$res['groupimage'] = URL."images/group/".$_PB_CACHE['membergroup'][$res['membergroup_id']]['avatar'];
			}
			$res['service_start_date'] = @date("Y-m-d", $res['service_start_date']);
			$res['service_end_date'] = @date("Y-m-d", $res['service_end_date']);
			setvar("item", $res);
		}
		uaAssign(array("Genders"=> $typeoption->get_cache_type("gender")));
		setvar("MemberStatus", $typeoption->get_cache_type("check_status"));
		$tpl_file = "member.edit";
		template($tpl_file);
		exit;
	}
	if ($do == "search") {
		if(!empty($_GET['member']['name'])) $conditions[] = "Member.username like '%".$_GET['member']['name']."%'";
		if(isset($_GET['member']['status']) && $_GET['member']['status']>=0) $conditions[] = "Member.status='".$_GET['member']['status']."'";
		if (!empty($_GET['groupid'])) {
			$conditions[] = "Member.membergroup_id=".intval($_GET['groupid']);
		}
	}
	if ($do=="del" && !empty($id)) {
		$member->Delete($id);
	}
}
$fields = "id,username,CONCAT(mf.first_name,mf.last_name) AS NickName,points,credits,membergroup_id,status,created AS pubdate,last_login,trusttype_ids";
$amount = $member->findCount(null, $conditions,"id");
$page->setPagenav($amount);
$joins[] = "LEFT JOIN {$tb_prefix}memberfields mf ON Member.id=mf.member_id";
$result = $member->findAll($fields, $joins, $conditions, "Member.id DESC ",$page->firstcount,$page->displaypg);
if (!empty($result)) {
	for($i=0; $i<count($result); $i++){
		$tmp_img = null;
		if ($result[$i]['id']!=$administrator_id) {
			$result[$i]['candelete'] = 1;
		}else{
			$result[$i]['candelete'] = 0;
		}
		if (!empty($result[$i]['trusttype_ids'])) {
			$tmp_str = explode(",", $result[$i]['trusttype_ids']);
			foreach ($tmp_str as $key=>$val){
				$tmp_img.="<img src='".URL."images/icon/".$_PB_CACHE['trusttype'][$val]['avatar']."' alt='".$_PB_CACHE['trusttype'][$val]['name']."' />";
			}
			$result[$i]['trust_image'] = $tmp_img;
		}
	}
	setvar("Items", $result);
}
uaAssign(array("MemberStatus"=> $typeoption->get_cache_type("check_status"),"ByPages"=>$page->pagenav));
template($tpl_file);
?>