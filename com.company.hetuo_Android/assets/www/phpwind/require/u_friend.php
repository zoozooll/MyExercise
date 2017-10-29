<?php
!function_exists('readover') && exit('Forbidden');

InitGP(array('job'));

if (empty($job)) {
	InitGP(array('o','page','fuid'));
	$fuid = (int)$fuid;
	if ($o == 'check') {
		!$isU && Showmsg('space_over_right');
		$sqlon = 'f.uid';
		$sqlwhere = "WHERE f.friendid=".pwEscape($userdb['uid'])." AND status='1'";
	} else {
		$sqlon = 'f.friendid';
		$sqlwhere = "WHERE f.uid=".pwEscape($userdb['uid'])." AND status='0'";
	}
	(!is_numeric($page) || $page < 1) && $page = 1;
	$db_perpage = 20;
	$total = $db->get_value("SELECT COUNT(*) FROM pw_friends f $sqlwhere");
	$numofpage = ceil($total/$db_perpage);
	$pages = numofpage($total,$page,$numofpage,"u.php?action=friend&uid=$uid&o=$o&");
	$limit = pwLimit(($page-1)*$db_perpage,$db_perpage);
	$frienddb = array();
	$query = $db->query("SELECT f.*,m.uid AS muid,m.username,m.icon,md.thisvisit FROM pw_friends f LEFT JOIN pw_members m ON m.uid=$sqlon LEFT JOIN pw_memberdata md ON md.uid=$sqlon $sqlwhere ORDER BY f.joindate DESC $limit");
	while ($rt = $db->fetch_array($query)) {
		list($rt['icon']) = showfacedesign($rt['icon'],true);
		$rt['joindate'] = get_date($rt['joindate']);
		$frienddb[] = $rt;
	}

	require_once PrintEot('u');
	footer();

} elseif ($job == 'delete') {

	PostCheck();
	!$isU && Showmsg('space_over_right');
	InitGP(array('selid'));
	!$selid && Showmsg('id_error');
	!is_array($selid) && $selid = array($selid);

	if ($selid) {
		$db->update('DELETE FROM pw_friends WHERE uid='.pwEscape($userdb['uid'])." AND friendid IN (".pwImplode($selid).")");
		$count = count($selid);
		$db->update("UPDATE pw_memberdata SET f_num=f_num-$count WHERE uid=".pwEscape($winduid)." AND f_num>=$count");
	}
	refreshto('u.php?action=friend','operate_success');

} elseif ($job == 'acceptadd' || $job == 'accept') {

	PostCheck();
	!$isU && Showmsg('space_over_right');
	InitGP(array('selid'));
	!$selid && Showmsg('id_error');
	!is_array($selid) && $selid = array($selid);

	$frdb  = $deldb = $adddb = $msgdb = $feed = array();
	$query = $db->query("SELECT f.uid,m.uid AS ifu,m.username,mf.uid AS iffriend FROM pw_friends f LEFT JOIN pw_members m ON f.uid=m.uid LEFT JOIN pw_friends mf ON f.friendid=mf.uid AND f.uid=mf.friendid AND mf.status='0' WHERE f.friendid=".pwEscape($userdb['uid'])." AND f.uid IN(".pwImplode($selid).") AND f.status='1'");

	while ($rt = $db->fetch_array($query)) {
		if ($rt['ifu']) {
			$frdb[]  = $rt['uid'];
			if ($job == 'acceptadd' && !$rt['iffriend']) {
				$adddb[]	= array($userdb['uid'],$rt['uid'],0,$timestamp,'');
				$title		= Char_cv(getLangInfo('writemsg','friend_acceptadd_title',array('username'=>$windid)));
				$writemsg	= Char_cv(getLangInfo('writemsg','friend_acceptadd_content',array('uid'=>$winduid,'username'=>$windid)));
				$feed[$winduid] = array('uid' => $rt['uid'], 'friend' => $rt['username']);
			} else {
				$title		= Char_cv(getLangInfo('writemsg','friend_accept_title',array('username'=>$windid)));
				$writemsg	= Char_cv(getLangInfo('writemsg','friend_accept_content',array('uid'=>$winduid,'username'=>$windid)));
			}
			$feed[$rt['uid']] = array('uid' => $winduid, 'friend' => $windid);
			$msgdb[] = array($rt['uid'],'0','SYSTEM','rebox','1',$timestamp,$title,$writemsg);
		} else {
			$deldb[] = $rt['uid'];
		}
	}

	if ($frdb) {
		$db->update("UPDATE pw_friends"
		. " SET status='0',descrip='',joindate=".pwEscape($timestamp)
		. " WHERE friendid=".pwEscape($userdb['uid'])
		. " AND uid IN(".pwImplode($frdb).")");
		$fcount = count($frdb);
		if ($adddb) {
			$db->update("REPLACE INTO pw_friends (uid,friendid,status,joindate,descrip) VALUES ".pwSqlMulti($adddb,false));
			$count = count($adddb);
		}
		require_once(R_P.'require/msg.php');
		require_once(R_P.'require/postfunc.php');
		send_msgc($msgdb);
		foreach ($feed as $key => $log) {
			pwAddFeed($key,'friend','',$log);
		}
	}
	if ($deldb) {
		$db->update("DELETE FROM pw_friends WHERE friendid=".pwEscape($userdb['uid'])." AND uid IN(".pwImplode($deldb).")");
		$count -= count($deldb);
	}
	if ($fcount) {
		$db->update("UPDATE pw_memberdata SET f_num=f_num+1 WHERE uid IN (".pwImplode($frdb).")");
	}
	$db->update("UPDATE pw_memberdata SET f_num=f_num+".pwEscape($count)." WHERE uid=".pwEscape($winduid));
	refreshto('u.php?action=friend','friend_accept_success');

} elseif ($job == 'refuse') {

	PostCheck();
	!$isU && Showmsg('space_over_right');
	InitGP(array('selid','refusemsg'));
	!$selid && Showmsg('id_error');
	!is_array($selid) && $selid = array($selid);

	$title		= Char_cv(getLangInfo('writemsg','friend_refuse_title',array('username'=>$windid)));
	$writemsg	= Char_cv(getLangInfo('writemsg','friend_refuse_content',array('uid'=>$winduid,'username'=>$windid,'msg'=>stripslashes($refusemsg))));

	$msgdb = array();
	$query = $db->query("SELECT uid FROM pw_friends WHERE friendid=".pwEscape($userdb['uid']).' AND uid IN('.pwImplode($selid).") AND status='1'");
	while ($rt = $db->fetch_array($query)) {
		$msgdb[] = array($rt['uid'],0,'SYSTEM','rebox','1',$timestamp,$title,$writemsg);
	}
	$db->update('DELETE FROM pw_friends WHERE friendid='.pwEscape($userdb['uid']).' AND uid IN('.pwImplode($selid).") AND status='1'");

	if ($msgdb) {
		require_once(R_P.'require/msg.php');
		send_msgc($msgdb);
	}
	refreshto('u.php?action=friend','operate_success');

} elseif ($job == 'edit') {

	!$isU && Showmsg('space_over_right');

	if (empty($_POST['step'])) {

		InitGP(array('touid'));
		$friend = $db->get_one("SELECT f.*,m.username FROM pw_friends f LEFT JOIN pw_members m ON f.friendid=m.uid WHERE f.uid=".pwEscape($userdb['uid'])." AND status='0' AND f.friendid=".pwEscape($touid));
		empty($friend) && Showmsg('data_error');

		require_once PrintEot('u');
		footer();

	} else {

		InitGP(array('touid','descrip'));
		$db->update("UPDATE pw_friends SET descrip=".pwEscape($descrip)." WHERE uid=".pwEscape($userdb['uid'])." AND status='0' AND friendid=".pwEscape($touid));

		refreshto('u.php?action=friend','operate_success');
	}
}
?>