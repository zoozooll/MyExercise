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
require(CACHE_PATH. "cache_typeoption.php");
uses("membergroup", "trade");
$membergroup = new Membergroups();
$trade = new Trade();
setvar("MenuHide", "display:none;");
if (!empty($memberinfo)) {
	$service_info = false;
	$membergroup_id = $memberinfo['membergroup_id'];
	if (empty($memberinfo['service_end_date']) or empty($memberinfo['service_start_date'])) {
		$service_info = false;
	}else{
		$total_days = ($memberinfo['service_end_date'] - $memberinfo['service_start_date']);
		if($total_days<=0) {
			$total_days=1;
			$service_interation = 1;
		}else{
			$service_interation = intval((($time_stamp - $memberinfo['service_start_date'])/$total_days)*100);
		}
		setvar("service_days", $service_interation>100?100:$service_interation);
		$service_info = true;
	}
	if(isset($service_interation))
	if ($service_interation>=100) {
		$group_info = $pdb->GetRow("SELECT default_live_time,after_live_time FROM {$tb_prefix}membergroups WHERE id=".$membergroup_id);
		$membergroup_id = $group_info['after_live_time'];
		$time_add = $membergroup->getServiceEndtime($group_info['default_live_time']);
		$pdb->Execute("UPDATE {$tb_prefix}members SET membergroup_id='".$group_info['after_live_time']."',service_start_date='".$time_stamp."',service_end_date='".$time_add."' WHERE id=".$_SESSION['MemberID']);
	}
	uaAssign(array(
		"UserName"=>$memberinfo['first_name'].$memberinfo['last_name'],
		"LastLogin"=>date("Y-m-d H:i",$memberinfo['last_login']))
	);
	$offer_count = $pdb->GetArray("SELECT count(id) AS amount,type_id AS typeid FROM {$tb_prefix}trades WHERE member_id=".$_SESSION['MemberID']." GROUP BY type_id");
	$offer_stat = array();
	$types = $trade->getTradeTypes();
	if (!empty($offer_count)) {
		foreach ($offer_count as $offer_key=>$offer_val) {
			$offer_stat[$types[$offer_val['typeid']]] = $offer_val['amount'];
		}
		setvar("items_offer", $offer_stat);
	}
	$pm_count = $pdb->GetArray("SELECT count(id) AS amount,type AS typename FROM {$tb_prefix}messages WHERE to_member_id=".$_SESSION['MemberID']." GROUP BY type");
	if (!empty($pm_count)) {
		$pm_result = array();
		foreach ($pm_count as $pm_val) {
			$pm_result[$pm_val['typename']] = intval($pm_val['amount']);
		}
		setvar("pm", $pm_result);
	}
	setvar("ServiceInfo", $service_info);
	$memberinfo['start_date'] = date("Y-m-d", $memberinfo['service_start_date']);
	$memberinfo['end_date'] = date("Y-m-d", $memberinfo['service_end_date']);
	$memberinfo['gender_name'] = $_PB_CACHE['calls'][$memberinfo['gender']];
	$memberinfo['avatar'] = (!empty($memberinfo['photo']))?pb_get_attachmenturl($memberinfo['photo'], "../", "small"):(($memberinfo['gender']==2)?"images/female.png":"images/male.png");
	setvar("MemberInfo", $memberinfo);
	$group['name'] = $g['name'];
	$group['image'] = $g['avatar'];
	setvar("group", $group);
	template("index");
}else{
	flash('invalid_user');
}
?>