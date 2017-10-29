<?php
!function_exists('readover') && exit('Forbidden');
PwNewDB();
if ($hash == appkey($o_u,$app) && $winduid) {
	$friend = $db->get_one("SELECT username FROM pw_members WHERE uid=".pwEscape($o_u));
	if ($friend) {
		$pwSQL = $feed = array();
		if (empty($windid)){
			$windid = $db->get_value("SELECT username FROM pw_members WHERE uid=".pwEscape($winduid));
		} 
		$feed[$o_u] 	= array('uid' => $winduid, 'friend' => $windid);
		$feed[$winduid] = array('uid' => $o_u, 'friend' => $friend['username']);
		$pwSQL[] = array($winduid,$o_u,$timestamp,0);
		$pwSQL[] = array($o_u,$winduid,$timestamp,0);
		$db->update("REPLACE INTO pw_friends(uid,friendid,joindate,status) VALUES ".pwSqlMulti($pwSQL,false));
		$myurl = $basename."q=home&u=".$o_u;
		$msg = array(
				'toUid'		=> $o_u,
				'subject'	=> 'o_friend_success_title',
				'content'	=> 'o_friend_success_cotent',
				'other'		=> array(
					'uid'		=> $winduid,
					'username'	=> $windid,
					'myurl'		=> $myurl
				)
			);
		require_once R_P.'require/msg.php';
		pwSendMsg($msg);
		$db->update("UPDATE pw_memberdata SET f_num=f_num+1 WHERE uid=".pwEscape($winduid)." OR uid=".pwEscape($o_u));
		if ($feed) {
			foreach ($feed as $key => $log) {
				pwAddFeed($key,'friend','',$log);
			}
		}
	}

}
?>