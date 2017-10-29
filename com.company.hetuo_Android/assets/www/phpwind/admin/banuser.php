<?php
!function_exists('adminmsg') && exit('Forbidden');
$basename = "$admin_file?adminjob=banuser";

if (empty($action)) {
	InitGP(array('username'),'G');
	$select[$db_banby] = 'selected';
	$db_banlimit = (int)$db_banlimit;
	$db_autoban ? $autoban_Y='checked' : $autoban_N='checked';
	$db_bantype==2 ? $bantype_2='checked' : $bantype_1='checked';
	include PrintEot('banuser');exit;

}  elseif($_POST['action'] == 'banuser') {

	InitGP(array('username', 'ban_reason'),'P');
	InitGP(array('limit'),'P',2);
	$userdb = $db->get_one("SELECT uid,groupid,userstatus FROM pw_members WHERE username=".pwEscape($username));
	if (!$userdb) {
		$errorname = $username;
		adminmsg('user_not_exists');
	}
	if ($userdb['groupid'] == '-1') {
		if ($type == 1 && !$limit) {
			adminmsg('ban_limit');
		}
		$db->update("UPDATE pw_members SET groupid='6' WHERE uid=".pwEscape($userdb['uid']));
		$db->update("REPLACE INTO pw_banuser"
			. " SET " .pwSqlSingle(array(
				'uid'		=> $userdb['uid'],
				'fid'		=> 0,
				'type'		=> $type,
				'startdate'	=> $timestamp,
				'days'		=> $limit,
				'admin'		=> $admin_name,
				'reason'	=> $ban_reason,
		),false));

		$_cache = getDatastore();
		$_cache->delete('UID_'.$userdb['uid']);

		adminmsg('operate_success');
	} else {
		adminmsg('ban_error');
	}
} elseif($_POST['action'] == 'freeuser') {

	InitGP(array('username'),'P');
	$userdb = $db->get_one("SELECT uid,groupid,userstatus FROM pw_members WHERE username=".pwEscape($username));
	if (!$userdb) {
		$errorname = $username;
		adminmsg('user_not_exists');
	}
	if ($userdb['groupid'] == 6 || $userdb['userstatus'] & 1) {
		$db->update("UPDATE pw_members SET groupid='-1',userstatus=userstatus&(~1) WHERE uid=".pwEscape($userdb['uid']));
		$db->update("DELETE FROM pw_banuser WHERE uid=".pwEscape($userdb['uid']));

		$_cache = getDatastore();
		$_cache->delete('UID_'.$userdb['uid']);

		adminmsg('operate_success');
	} else {
		adminmsg('not_banned');
	}
} elseif ($_POST['action'] == 'autoban') {

	InitGP(array('ban'),'P');
	foreach ($ban as $key => $value) {
		if (${'db_'.$key} != $value) {
			setConfig('db_' . $key, $value);
		}
	}
	updatecache_c();
	adminmsg('operate_success');
}
?>