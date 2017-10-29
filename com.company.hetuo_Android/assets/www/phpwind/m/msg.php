<?php
require_once('wap_global.php');

!$windid && wap_msg('not_login');
wap_header('msg',$db_bbsname);

InitGP(array('action'));

if (!$action) {

	$allnum = $newnum = 0;
	$query  = $db->query("SELECT COUNT(*) AS num,ifnew FROM pw_msg WHERE touid=".pwEscape($winduid)." AND type='rebox' GROUP BY ifnew=0");
	while ($rt = $db->fetch_array($query)) {
		 $allnum += $rt['num'];
		$rt['ifnew'] && $newnum = $rt['num'];
	}
	require_once PrintEot('wap_msg');
	wap_footer();

} elseif ($action == 'new') {

	$msgdb = array();
	$query = $db->query("SELECT m.*,mc.title FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.touid=".pwEscape($winduid)." AND m.type='rebox' AND m.ifnew=1 ORDER BY m.mdate DESC LIMIT 15");
	while ($rt = $db->fetch_array($query)) {
		$rt['title']	= wap_cv($rt['title']);
		$rt['username'] = wap_cv($rt['username']);
		$rt['mdate']	= get_date($rt['mdate']);
		$msgdb[] = $rt;
	}
	require_once PrintEot('wap_msg');
	wap_footer();

} elseif ($action == 'all') {

	$msgdb = array();
	$query = $db->query("SELECT m.*,mc.title FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.touid=".pwEscape($winduid)." AND m.type='rebox' ORDER BY m.mdate DESC LIMIT 15");
	while ($rt = $db->fetch_array($query)) {
		$rt['title']	= wap_cv($rt['title']);
		$rt['username'] = wap_cv($rt['username']);
		$rt['mdate']	= get_date($rt['mdate'],"m-d H:i");
		$msgdb[] = $rt;
	}
	require_once PrintEot('wap_msg');
	wap_footer();

} elseif ($action == 'read') {
	InitGP(array('mid'),'GP',2);
	$rt  = $db->get_one("SELECT m.*,mc.title,mc.content FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.touid=".pwEscape($winduid)." AND m.type='rebox' AND m.mid=".pwEscape($mid));
	if (!$rt) {
		wap_msg('no_msg');
	}
	if ($rt['ifnew']) {
		$db->update("UPDATE pw_msg SET ifnew=0 WHERE mid=".pwEscape($rt['mid']));
	}
	$rt['content']	= strip_tags($rt['content']);
	$rt['content']  = substrs($rt['content'],$db_waplimit);
	$rt['content']  = wap_cv($rt['content']);
	$rt['content']  = wap_code($rt['content']);
	$rt['title']	= wap_cv($rt['title']);
	$rt['username'] = wap_cv($rt['username']);
	$rt['mdate']	= get_date($rt['mdate']);
	require_once PrintEot('wap_msg');
	wap_footer();

} elseif ($action == 'write') {

	if (!$_POST['pwuser'] || !$_POST['title'] || !$_POST['content']) {

		InitGP(array('touid'),'GP',2);
		if ($touid) {
			$rt = $db->get_one("SELECT username FROM pw_members WHERE uid=".pwEscape($touid));
			if ($rt) {
				$pwuser = $rt['username'];
			}
		}
		require_once PrintEot('wap_msg');
		wap_footer();

	} else {

		InitGP(array('pwuser','title','content'),'P');
		$rt = $db->get_one("SELECT uid,newpm,banpm,msggroups FROM pw_members WHERE username=".pwEscape($pwuser));
		if (!$rt) {
			wap_msg('user_not_exists');
		}
		if ($rt['msggroups'] && strpos($rt['msggroups'],",$groupid,")===false || strpos($rt['banpm'],",$windid,")!==false) {
			wap_msg('msg_refuse');
		}
		$title	 = wap_cv($title);
		$content = wap_cv($content);
		$db->update("INSERT INTO pw_msg"
			. " SET ".pwSqlSingle(array(
				'touid'		=> $rt['uid'],
				'fromuid'	=> $winduid,
				'username'	=> $windid,
				'type'		=> 'rebox',
				'ifnew'		=> 1,
				'mdate'		=> $timestamp
		)));
		$mid = $db->insert_id();
		$db->update("REPLACE INTO pw_msgc"
			. " SET ".pwSqlSingle(array(
				'mid'		=> $mid,
				'title'		=> $title,
				'content'	=> $content
		)));

		$db->update("UPDATE pw_members SET newpm=newpm+1 WHERE uid=".pwEscape($rt['uid']));
		wap_msg('msg_success','msg.php');
	}
} elseif ($action == 'delete') {

	InitGP(array('mid'),'GP',2);
	$db->update("DELETE FROM pw_msg WHERE mid=".pwEscape($mid)." AND type='rebox' AND touid=".pwEscape($winduid));
	$db->update("UPDATE pw_members SET newpm=newpm-1 WHERE uid=".pwEscape($winduid));
	if ($db->affected_rows() > 0) {
		require_once(R_P.'require/msg.php');
		delete_msgc($mid);
	}
	wap_msg('msg_delete','msg.php');
}
?>