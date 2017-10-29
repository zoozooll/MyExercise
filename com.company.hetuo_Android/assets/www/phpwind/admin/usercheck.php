<?php
!function_exists('adminmsg') && exit('Forbidden');
$basename="$admin_file?adminjob=usercheck&admintype=$admintype";

if(empty($_POST['action'])){
	InitGP(array('page'),'GP',2);
	if($admintype=='checkemail'){
		$page < 1 && $page = 1;
		$limit = pwLimit(($page-1)*$db_perpage,$db_perpage);
		$rt    = $db->get_one("SELECT COUNT(*) AS sum FROM pw_members WHERE yz>1");
		$pages = numofpage($rt['sum'],$page,ceil($rt['sum']/$db_perpage),"$basename&");

		$memdb_E = array();
		$query = $db->query("SELECT uid,username,regdate,email FROM pw_members WHERE yz>1 ORDER BY regdate DESC $limit");
		while($member=$db->fetch_array($query)){
			$member['regdate'] = get_date($member['regdate']);
			$memdb_E[] = $member;
		}
	} elseif($admintype=='checkreg'){
		$page < 1 && $page = 1;
		$limit = pwLimit(($page-1)*$db_perpage,$db_perpage);
		$rt    = $db->get_one("SELECT COUNT(*) AS sum FROM pw_members WHERE groupid='7'");
		$pages = numofpage($rt['sum'],$page,ceil($rt['sum']/$db_perpage),"$basename&");

		$memdb_R = array();
		$query = $db->query("SELECT m.uid,username,regdate,email,i.regreason FROM pw_members m LEFT JOIN pw_memberinfo i ON i.uid=m.uid WHERE groupid='7' ORDER BY regdate DESC $limit");
		while($member=$db->fetch_array($query)){
			$member['regdate'] = get_date($member['regdate']);
			$memdb_R[] = $member;
		}
	}
	include PrintEot('usercheck');exit;
} elseif($action=='check'){
	!$_POST['yzmem'] && adminmsg('operate_error');
	$uids = array();
	foreach($_POST['yzmem'] as $value){
		is_numeric($value) && $uids[] = $value;
	}
	if($uids){
		$uids = pwImplode($uids);
		if($type=='pass'){
			if($admintype=='checkemail'){
				$db->update("UPDATE pw_members SET yz='1' WHERE uid IN($uids)");
			} elseif($admintype=='checkreg'){
				$db->update("UPDATE pw_members SET groupid='-1' WHERE uid IN($uids)");
			}
		} else{
			$db->update("DELETE FROM pw_members WHERE uid IN ($uids)");
			$db->update("DELETE FROM pw_memberdata WHERE uid IN ($uids)");
			$db->update("DELETE FROM pw_memberinfo WHERE uid IN ($uids)");
			@extract($db->get_one("SELECT count(*) AS count FROM pw_members"));
			@extract($db->get_one("SELECT username FROM pw_members ORDER BY uid DESC LIMIT 1"));
			$db->update("UPDATE pw_bbsinfo SET ".pwSqlSingle(array('newmember'=>$username,'totalmember'=>$count))."WHERE id='1'");
		}
	}
	adminmsg('operate_success');
}
?>