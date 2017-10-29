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
define('CURSCRIPT', 'upgrade');
require("../libraries/common.inc.php");
require("../share.inc.php");
if (session_id() == '' ) { 
	require_once(LIB_PATH. "session_php.class.php");
	$session = new PbSessions();
}
uses("member","order","goods");
$member = new Members();
$goods = new Goods();
$order = new Orders();
$adzones = $pdb->GetArray("SELECT id,name,price FROM {$tb_prefix}adzones");
$good = $goods->findAll("id,name,price");
$payment = $pdb->GetArray("SELECT id,title FROM {$tb_prefix}payments WHERE available=1");
if (!empty($pb_userinfo)) {
	$member_info = $pdb->GetRow("SELECT m.email,mf.tel,mf.first_name,mf.last_name FROM {$tb_prefix}members m LEFT JOIN {$tb_prefix}memberfields mf ON m.id=mf.member_id WHERE m.id=".$pb_userinfo['pb_userid']);
	setvar("MemberInfo", $member_info);
}else{
	flash("please_login_first", URL."logging.php");
}

if(isset($_GET['do'])){
    $do = trim($_GET['do']);
	if($do=="upgrade"){
        setvar("index",0);
	}elseif($do=="buy"){
        setvar("index",2);
    }elseif($do=="charge"){
	   setvar("index",1);
	}
}
if (isset($_POST['do'])) {
	$do = trim($_POST['do']);
	if ($do == "apply") {
		pb_submit_check('product_id');
		$add = array();
		$goods_id = intval($_POST['product_id']);
		if (!empty($_POST['tel'])) {
			$add[] = "mf.tel='".addslashes($_POST['tel'])."'";
		}
		if (!empty($_POST['email'])) {
			$add[] = "m.email='".addslashes($_POST['email'])."'";
		}
		if (!empty($add)) {
			$add = implode(",", $add);
			$pdb->Execute("UPDATE {$tb_prefix}members m,{$tb_prefix}memberfields mf SET {$add} WHERE m.id={$pb_userinfo['pb_userid']} AND mf.member_id={$pb_userinfo['pb_userid']}");
		}
		$vals['content'] = $_POST['content'];
		$vals['member_id'] = $pb_userinfo['pb_userid'];
		$vals['cache_username'] = $vals['username'] = $pb_userinfo['pb_username'];
		$online = $pdb->GetRow("SELECT if_online_support,config,description FROM {$tb_prefix}payments WHERE id='".intval($_POST['payment_id'])."'");
		$product_id = intval($_POST['product_id']);
		switch ($product_id) {
			case 2:
				if(is_numeric($_POST['total_price'])){
					$total_price = $vals['total_price'] = $_POST['total_price'];
					$last_order_id = $order->Add($vals);
					if($last_order_id){
						$pdb->Execute("INSERT INTO {$tb_prefix}ordergoods (order_id,goods_id) VALUE ('".$last_order_id."','".$_POST['product_id']."')");
						setvar("total_price", $total_price);
						setvar("payment", $payment);
						$configs = unserialize($online['config']);
						if($online['if_online_support']){
							setvar("OnlineSupport", 1);
							setvar("OnlineSupportUrl", $configs['gateway']);
						}else{
							setvar("OnlineSupport", 0);
						}
						setvar("Description", $online['description']);
						render("member.pay", 1);
						exit;
					}else{
						flash();
					}
				}else{
					flash("charge_check", null, 0);
				}
				break;
			case 1:
				$vals['content'] = $_POST['product_id']."|".$vals['content'];
				$vals['total_price'] = $pdb->GetOne("SELECT price FROM {$tb_prefix}goods WHERE id=".$_POST['item_id']);
				$last_order_id = $order->Add($vals);
				if($last_order_id){
					$pdb->Execute("INSERT INTO {$tb_prefix}ordergoods (order_id,goods_id) VALUE ('".$last_order_id."','".$_POST['item_id']."')");
					flash("order_submited", null, 0);
				}else{
					flash();
				}
			break;
			case 3:
				$vals['content'] = $_POST['product_id']."|".$vals['content'];
				$vals['total_price'] = $pdb->GetOne("SELECT price FROM {$tb_prefix}adzones WHERE id=".$_POST['item_id']);
				$last_order_id = $order->Add($vals);
				if($last_order_id){
					$pdb->Execute("INSERT INTO {$tb_prefix}ordergoods (order_id,goods_id) VALUE ('".$last_order_id."','".$_POST['item_id']."')");
					flash("order_submited", null, 0);
				}else{
					flash();
				}
			break;
		}
	}
}
formhash();
$viewhelper->setPosition(L("select_buy_service", "tpl"));
setvar("Items",$good);
setvar("Adzones",$adzones);
setvar("payments",$payment);
render("member.purchase");
?>