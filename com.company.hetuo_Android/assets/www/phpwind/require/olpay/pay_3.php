<?php
!function_exists('readover') && exit('Forbidden');

$g = $db->get_one("SELECT p.gid,p.rvalue AS allowbuy,u.grouptitle FROM pw_permission p LEFT JOIN pw_usergroups u ON p.gid=u.gid WHERE p.uid='0' AND p.fid='0' AND p.gid=" . pwEscape($rt['paycredit']) . " AND p.rkey='allowbuy' AND u.gptype='special'");

if ($g && $g['allowbuy']) {

	if ($rt['extra_1'] == 1) {
		if ($rt['groupid'] == '-1') {
			$db->update("UPDATE pw_members SET groupid=" . pwEscape($g['gid']) . " WHERE uid=" . pwEscape($rt['uid']));
		} else {
			$groups = $rt['groups'] ? $rt['groups'].$rt['groupid'].',' : ",$rt[groupid],";
			$db->update("UPDATE pw_members SET " . pwSqlSingle(array('groupid' => $g['gid'], 'groups' => $groups)) . ' WHERE uid=' . pwEscape($rt['uid']));
		}
	} else {
		$groups = $rt['groups'] ? $rt['groups'].$g['gid'].',' : ",$g[gid],";
		$db->update("UPDATE pw_members SET groups=" . pwEscape($groups,false) . " WHERE uid=" . pwEscape($rt['uid']));
	}
	$db->pw_update(
		"SELECT uid FROM pw_extragroups WHERE uid=" . pwEscape($rt['uid']) . " AND gid=" . pwEscape($g['gid']),
		"UPDATE pw_extragroups SET ". pwSqlSingle(array(
			'togid'		=> $rt['groupid'],
			'startdate'	=> $timestamp,
			'days'		=> $rt['number']
		)) . " WHERE uid=" . pwEscape($rt['uid']) . " AND gid=" . pwEscape($g['gid'])
		,
		"INSERT INTO pw_extragroups SET " . pwSqlSingle(array(
			'uid'		=> $rt['uid'],
			'togid'		=> $rt['groupid'],
			'gid'		=> $g['gid'],
			'startdate'	=> $timestamp,
			'days'		=> $rt['number']
		))
	);

	require_once(R_P.'require/msg.php');
	$message = array(
		'toUser'	=> $rt['username'],
		'subject'	=> 'groupbuy_title',
		'content'	=> 'groupbuy_content',
		'other'		=> array(
			'fee'		=> $fee,
			'gname'		=> $g['grouptitle'],
			'number'	=> $rt['number']
		)
	);
	pwSendMsg($message);
	$ret_url = 'profile.php?action=buy';
}
?>