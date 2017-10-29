<?php
!function_exists('readover') && exit('Forbidden');

if (empty($_POST['step'])) {

	$userdb = $db->get_one("SELECT index_privacy,profile_privacy,info_privacy,credit_privacy,owrite_privacy,msgboard_privacy FROM pw_ouserdata WHERE uid=" . pwEscape($winduid));
	${'index_'.$userdb['index_privacy']} = 'selected="selected"';
	//${'profile_'.$userdb['profile_privacy']} = 'selected="selected"';
	//${'info_'.$userdb['info_privacy']} = 'selected="selected"';
	//${'credit_'.$userdb['credit_privacy']} = 'selected="selected"';
	//${'owrite_'.$userdb['owrite_privacy']} = 'selected="selected"';
	${'msgboard_'.$userdb['msgboard_privacy']} = 'selected="selected"';
	${'friend_'.getstatus($winddb['userstatus'],3,3)} = 'checked';

	require_once PrintEot('profile_privacy');
	footer();

} else {

	PostCheck();
	InitGP(array('privacy', 'friendcheck'), 'P', 2);
	$pwSQL = array('uid' => $winduid);
	$pwSQL['index_privacy'] = $privacy['index'] < 0 || $privacy['index'] > 2 ? 0 : $privacy['index'];
	//$pwSQL['profile_privacy'] = $privacy['profile'] < 0 || $privacy['profile'] > 2 ? 0 : $privacy['profile'];
	//$pwSQL['info_privacy'] = $privacy['info'] < 0 || $privacy['info'] > 2 ? 0 : $privacy['info'];
	//$pwSQL['credit_privacy'] = $privacy['credit'] < 0 || $privacy['credit'] > 2 ? 0 : $privacy['credit'];
	//$pwSQL['owrite_privacy'] = $privacy['owrite'] < 0 || $privacy['owrite'] > 2 ? 0 : $privacy['owrite'];
	$pwSQL['msgboard_privacy'] = $privacy['msgboard'] < 0 || $privacy['msgboard'] > 2 ? 0 : $privacy['msgboard'];
	$db->pw_update(
		"SELECT uid FROM pw_ouserdata WHERE uid=" . pwEscape($winduid),
		"UPDATE pw_ouserdata SET " . pwSqlSingle($pwSQL) . " WHERE uid=" . pwEscape($winduid),
		"INSERT INTO pw_ouserdata SET " . pwSqlSingle($pwSQL)
	);
	if ($friendcheck != getstatus($winddb['userstatus'],3,3)) {
		switch ($friendcheck) {
			case 1:
				$ustatus = 'userstatus=userstatus|(1<<2),userstatus=userstatus&~(1<<3)';break;
			case 2:
				$ustatus = 'userstatus=userstatus&~(1<<2),userstatus=userstatus|(1<<3)';break;
			default:
				$ustatus = 'userstatus=userstatus&~(3<<2)';break;
		}
		$db->update("UPDATE pw_members SET $ustatus WHERE uid=" . pwEscape($winduid));
	}

	refreshto('profile.php?action=privacy','operate_success');
}
?>