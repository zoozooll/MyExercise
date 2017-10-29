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
uses("trade");
$trade = new Trade();
$trade_model = new Trades();
if (isset($_POST['del'])) {
	pb_submit_check('id');
	$ids = implode(",", $_POST['id']);
	$ids = "(".$ids.")";
	$sql = "DELETE FROM {$tb_prefix}favorites WHERE id IN ".$ids." AND member_id=".$_SESSION['MemberID'];
	$res = $pdb->Execute($sql);
	if (!$res) {
		flash("action_failed");
	}
}
if(isset($_POST['do']) && isset($_POST['id'])){
	if ($trade_model->checkExist($_POST['id'])) {
		$sql = "INSERT INTO {$tb_prefix}favorites (target_id,member_id,type_id,created,modified) VALUE (".$_POST['id'].",".$_SESSION['MemberID'].",1,".$time_stamp.",".$time_stamp.")";
		$result = $pdb->Execute($sql);
	}
	if($result){
		echo "<script language='javascript'>window.close();</script>";
		exit;
	}else {
		flash("been_favorited", '', 0);
	}
}
$tpl_file = "favor";
$sql = "select f.id,t.id as offerid,t.title,t.type_id,f.created from {$tb_prefix}trades as t,{$tb_prefix}favorites as f where f.member_id=".$pb_userinfo['pb_userid']." and f.target_id=t.id";
$result = $pdb->GetArray($sql);
if (!empty($result)) {
	for($i=0; $i<count($result); $i++){
		$result[$i]['pubdate'] = date("Y-m-d", $result[$i]['created']);
	}
	setvar("Items", $result);
}
setvar("TradeTypes", $trade->getTradeTypes());
template($tpl_file);
?>