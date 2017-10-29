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
require(PHPB2B_ROOT.'./libraries/page.class.php');
uses("member");
$member = new Members();
$tpl_file = "adminer";
$page = new Pages();
if (isset($_POST['changepass']) && !empty($_POST['data']['adminer'])) {
	$old_pass = trim($_POST['data']['old_pass']);
	if (!pb_strcomp($current_pass, md5($old_pass))) {
		flash();
	}
	$result = $adminer->updatePasswd($current_adminer_id, $_POST['data']['adminer']['user_pass']);
	if(!$result) {
		flash();
	}
}
   if(isset($_POST['del']) && !empty($_POST['id'])){
	    $ids = $_POST['id'];
	foreach($ids as $val){
	   if (pb_strcomp($val, $current_adminer_id)||pb_strcomp($val, $administrator_id)) {
			flash();
	   }else{
         $adminer->primaryKey = "member_id";
		 $result = $adminer->del(intval($val));
	    }
     }
   }
if (isset($_POST['save']) && !empty($_POST['data']['adminfield'])) {
	$vals = array();
	$vals = $_POST['data']['adminfield'];
	if ($_POST['auth'] == 1) {
		if (!empty($_POST['priv']) && is_array($_POST['priv'])) {
			$vals['permissions'] = implode(",", $_POST['priv']);
		}
	}else{
		$vals['permissions'] = '';
	}
	if (!empty($_POST['data']['adminer']['user_pass'])) {
		$vals['user_pass'] = $member->authPasswd($_POST['data']['adminer']['user_pass']);
	}
	$adminer->primaryKey = "member_id";
	if (!empty($_POST['data']['expired'])) {
		include(LIB_PATH. "time.class.php");
		$vals['expired'] = Times::dateConvert($_POST['data']['expired']);
	}
	if (!empty($_POST['member_id'])) {
		$member_id = intval($_POST['member_id']);
		$member->save($_POST['data']['member'], "update", $member_id);
		$result = $adminer->save($vals, "update", $member_id);
	}else{
		//search member_id
		if (!empty($_POST['data']['username'])) {
			$sql = "SELECT id FROM {$tb_prefix}members WHERE username='".$_POST['data']['username']."'";
			$member_id = $pdb->GetOne($sql);
			if ($member_id) {
				$vals['member_id'] = $member_id;
				$result = $adminer->save($vals);
			}else{
				flash("member_not_exists");
			}
		}else{
			flash();
		}
	}
	if(!$result){
		flash('', '', 0);
	}else{
		flash("success");
	}
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if (!empty($_GET['id'])) {
		$id = intval($_GET['id']);
	}
	
	if ($do == "del" && !empty($id)) { 
		if (pb_strcomp($id, $current_adminer_id)||pb_strcomp($id,$administrator_id)) {
			flash();
		}else {
			$adminer->primaryKey = "member_id";
			$result = $adminer->del(intval($id));
		}
	}	
	if ($do == "profile") {
		$res = $pdb->GetRow("SELECT m.*,af.* FROM {$tb_prefix}adminfields af LEFT JOIN {$tb_prefix}members m ON m.id=af.member_id WHERE af.member_id={$current_adminer_id}");
		$res['member_id'] = $res['id'];
		setvar("item",$res);
		$tpl_file = "adminer.edit";
		template($tpl_file);
		exit;
	}
	if ($do == "edit") {
		require("menu.php");
		if(!empty($id)){
			$res = $pdb->GetRow("SELECT m.*,af.* FROM {$tb_prefix}adminfields af LEFT JOIN {$tb_prefix}members m ON m.id=af.member_id WHERE af.member_id={$id}");
			if($res['expired']) $res['expire_date'] = @date("Y-m-d", $res['expired']);
			$allowed_permissions = explode(",", $res['permissions']);
			foreach ($menus as $key=>$val) {
				if (in_array($key, $allowed_permissions)) {
					$menus[$key]['check'] = 1;
					foreach ($val['children'] as $key1=>$val1) {
						if (in_array($key1, $allowed_permissions)) {
							$menus[$key]['children'][$key1]['check'] = 1;
						}
					}
				}
			}
			setvar("item",$res);
		}
		setvar("Privileges", $menus);
		$tpl_file = "adminer.edit";
		template($tpl_file);
		exit;
	}
	if($do=="password"){
		$tpl_file = "adminer.password";
		template($tpl_file);
		exit;
	}
}
setvar("AdministratorId", $administrator_id);
setvar("Items", $result = $pdb->GetArray("SELECT m.username,af.first_name,af.last_login,af.last_ip,af.last_name,m.id,af.member_id FROM {$tb_prefix}adminfields af LEFT JOIN {$tb_prefix}members m ON m.id=af.member_id"));
template($tpl_file);
?>