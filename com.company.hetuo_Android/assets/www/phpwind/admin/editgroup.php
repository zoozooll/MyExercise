<?php
!function_exists('adminmsg') && exit('Forbidden');
$basename = "$admin_file?adminjob=editgroup";

if (!$action) {

	$groupselect = '';
	$query = $db->query("SELECT gid,grouptitle FROM pw_usergroups WHERE gptype IN ('system','special','default') AND gid NOT IN (1,2,5)");
	while ($group = $db->fetch_array($query)){
		$groupselect .= "<option value='$group[gid]'>$group[grouptitle]</option>";
	}
	include PrintEot('editgroup');exit;

} elseif ($_POST['action'] == 'add') {

	InitGP(array('members'),'P');
	InitGP(array('gid'),'P',2);
	!$members && adminmsg('operate_fail');
	if ($gid == 3 && !If_manager) {
		adminmsg('manager_right');
	} elseif ($gid == 4 && !If_manager && $admin_gid != 3) {
		adminmsg('chiefadmin_right');
	} elseif ($gid == 5) {
		adminmsg('setuser_forumadmin');
	}
	$groups = explode(",",$members);
	$groups = array_unique($groups);
	$uids = $memberdb = array();
	foreach ($groups as $value) {
		if ($value) {
			$member = $db->get_one("SELECT uid,username,groupid,groups FROM pw_members WHERE username=".pwEscape($value));
			if (!$member['uid']) {
				$errorname = $value;
				adminmsg('user_not_exists');
			} elseif ($member['groupid'] != '-1') {
				adminmsg('member_only');
			}
			$uids[] = $member['uid'];
			$memberdb[] = $member;
		}
	}
	!$uids && adminmsg('operate_fail');

	$gids  = array();
	$query = $db->query("SELECT gid FROM pw_usergroups WHERE gptype IN ('system','special','default') AND gid NOT IN (1,2,5)");
	while ($rt = $db->fetch_array($query)) {
		$gids[] = $rt['gid'];
	}
	if (in_array($gid,$gids)) {
		foreach ($memberdb as $member) {
			admincheck($member['uid'],$member['username'],$gid,$member['groups'],'update');
		}
	}
	$uids && $db->update("UPDATE pw_members SET groupid=".pwEscape($gid).'WHERE uid IN('.pwImplode($uids).')');
	adminmsg('operate_success');
}
?>