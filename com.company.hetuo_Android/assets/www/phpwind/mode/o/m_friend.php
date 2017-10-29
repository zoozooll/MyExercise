<?php
!defined('M_P') && exit('Forbidden');
if(!$winduid) ObHeader('index.php?m=o');
require_once(R_P.'require/showimg.php');

InitGP(array('type','u','space'));
$u = (int)$u;
!$u && $u = $winduid;
$thisbase = $basename."q=friend&";
$u != $winduid && $space = 1;
$space == 1 && $type = '';
if ($type == 'find') {
	InitGP(array('step','page','accord','according'));
	!empty($according) && $accord = $according;
	empty($accord) && $accord = 'username';
	if ($step == 2) {
		$where = '';
		if ($accord == 'uid') {
			InitGP('keyword','P',2);
			$uid = $keyword;
			!$uid && Showmsg('mode_o_find_uid_err');
			$uid == $winduid && Showmsg('mode_o_find_self');
			$where .= 'uid='.pwEscape($uid);
		} elseif ($accord == 'username') {
			InitGP(array('keyword','username'));
			!empty($username) && $keyword = $username;
			!$keyword && Showmsg('illegal_author');
			$input_username = $keyword;
			$sql_username = $keyword.'%';
			$where .= 'username LIKE'.pwEscape($sql_username);
			$keyword == $windid && Showmsg('mode_o_find_self');
		} elseif ($accord == 'email') {
			InitGP('keyword','P',1);
			$email = $keyword;
			if (!preg_match("/^[-a-zA-Z0-9_\.]+@([0-9A-Za-z][0-9A-Za-z-]+\.)+[A-Za-z]{2,5}$/",$email)) {
				Showmsg('mode_o_email_format_err');
			}
			$email == $winddb['email'] && Showmsg('mode_o_find_self');
			$where .= 'email='.pwEscape($email);
		} else {
			Showmsg('undefined_action');
		}
		$db_perpage = 18;
		$addpage = 'type=find&step=2&accord='.$accord.'&keyword='.rawurlencode($keyword);

		$count = $db->get_value("SELECT COUNT(*) AS count FROM pw_members WHERE $where");
		list($pages,$limit) = pwLimitPages($count,$page,"$thisbase$addpage&");

		$query = $db->query("SELECT uid,username,email,icon as face,regdate,honor FROM pw_members WHERE $where "."ORDER BY regdate DESC ".$limit);
		while ($rt = $db->fetch_array($query)) {
			list($rt['face'])	= showfacedesign($rt['face'],'1','m');
			$rt['regdate'] = get_date($rt['regdate']);
			$rt['honor'] = substrs($rt['honor'],50);
			$members[] = $rt;
		}

	} else {
		$mfriendkeys = $friendkeys = array();
		if ($myfriends = getFriends($winduid,0)) {
			$friendkeys = array_keys($myfriends);
			if ($friendkeys) {
				$query = $db->query('SELECT friendid FROM pw_friends WHERE uid IN('.pwImplode($friendkeys).') AND status=0 LIMIT 30');
				$mfriends = array();
				while ($rt = $db->fetch_array($query)) {
					if ($rt['friendid'] != $winduid && !CkInArray($rt['friendid'],$friendkeys)) {
						isset($mfriends[$rt['friendid']]) ? $mfriends[$rt['friendid']]++ : $mfriends[$rt['friendid']] = 1;
					}
				}
				arsort($mfriends);
				if (count($mfriends)>6) {
					$i = 0;
					$temp_friend = array();
					foreach ($mfriends as $key => $value) {
						$temp_friend[$key] = $value;
						$i++;
						if ($i==6) {
							break;
						}
					}
					$mfriends = $temp_friend;
					unset($temp_friend);
				}
				$mfriendkeys = array_keys($mfriends);
			}
		}
		if (isset($mfriendkeys) && count($mfriendkeys)>0) {
			$query = $db->query('SELECT uid,username,icon as face FROM pw_members WHERE uid IN('.pwImplode($mfriendkeys).')');
			while ($rt = $db->fetch_array($query)) {
				list($rt['face']) = showfacedesign($rt['face'],'1','m');
				$mosort[] = $rt;
			}
		} else {
			@include_once(D_P.'data/bbscache/mode_o_mosort.php');
			if (!$mosort || $mosort['time']<$timestamp-86400) {
				$mosort = array();
				if ($db_ifpwcache & 1) {
					$sql = "SELECT m.uid,m.username,m.icon as face FROM pw_elements e LEFT JOIN pw_members m ON e.id=m.uid WHERE e.type='usersort' AND e.mark='monthpost' ORDER BY e.value DESC ".pwLimit(6);
				} else {
					$montime = PwStrtoTime(get_date($timestamp,'Y-m').'-1');
					$sql = "SELECT m.uid,m.username,m.icon as face FROM pw_memberdata md LEFT JOIN pw_members m USING(uid) WHERE md.lastpost>".pwEscape($montime)." AND md.postnum>0 ORDER BY md.monthpost DESC ".pwLimit(6);
				}
				$query = $db->query($sql);
				while ($rt = $db->fetch_array($query)) {
					list($rt['face'])	= showfacedesign($rt['face'],'1','m');
					$mosort[] = $rt;
				}
				$mosort['time'] = $timestamp;
				writeover(D_P.'data/bbscache/mode_o_mosort.php',"<?php\r\n\$mosort=".pw_var_export($mosort).";\r\n?>");
			}
			$ifmosortcache = 1;
			unset($mosort['time']);
		}
		$lastposttable =  GetPtable($db_ptable); #'pw_posts'.$db_ptable;
		//经常回复我帖子的用户
		$query = $db->query("SELECT tid FROM pw_threads WHERE authorid=".pwEscape($winduid)." ORDER BY RAND() LIMIT 10");
		while ($rt = $db->fetch_array($query)) {
			$tiddb[] = $rt['tid'];
		}
		if (isset($tiddb) && count($tiddb)>0) {
			$query = $db->query("SELECT COUNT(*) AS count,p.authorid as uid,m.username,m.icon as face FROM $lastposttable p LEFT JOIN pw_members m ON p.authorid=m.uid WHERE p.tid IN(".pwImplode($tiddb).") AND p.authorid!=".pwEscape($winduid)." GROUP BY p.authorid ORDER BY count DESC LIMIT 9");
			while ($rt = $db->fetch_array($query)) {
				if (in_array($rt['uid'],$friendkeys)) continue;
				list($rt['face'])	= showfacedesign($rt['face'],'1','m');
				$reply_to_me_users[] = $rt;
			}
		}

		//我经常回复的用户
		$three_month_ago = $timestamp - 7776000;
		$query = $db->query("SELECT COUNT(*) AS count,t.authorid as uid,m.username,m.icon as face FROM $lastposttable p LEFT JOIN pw_threads t ON p.tid=t.tid LEFT JOIN pw_members m ON t.authorid=m.uid WHERE p.authorid=".pwEscape($winduid)." AND t.authorid !=".pwEscape($winduid)." AND p.postdate>".pwEscape($three_month_ago)." GROUP BY t.authorid ORDER BY count DESC LIMIT 9");
		while ($rt = $db->fetch_array($query)) {
			if (in_array($rt['uid'],$friendkeys)) continue;
			list($rt['face'])	= showfacedesign($rt['face'],'1','m');
			$reply_to_other_users[] = $rt;
		}
	}
	$username = $windid;
} elseif ($type == 'invite') {
	empty($o_invite) && Showmsg('mode_o_invite_close');
	require_once(D_P.'data/bbscache/dbreg.php');
	@include_once(D_P.'data/bbscache/inv_config.php');
	$inv_open && Showmsg('mode_o_invite_have');
	!$rg_allowregister && Showmsg('mode_o_invite_reg');
	InitGP('step');
	$hash = appkey($winduid);
	$invite_url = $db_bbsurl.'/'.$basename.'q=invite&u='.$winduid.'&hash='.$hash;
	if ($step == 2) {
		InitGP(array('emails','extranote'),'P',1);
		strlen($emails)>200 && Showmsg('mode_o_email_toolang');
		strlen('extranote')>200 && Showmsg('mode_o_extra_toolang');
		$emails = explode(',',str_replace(array("\r","\n"),array('',','),$emails));
		count($emails)>5 && Showmsg('mode_o_email_toolang');
		if ($emails) {
			foreach ($emails as $key=>$email) {
				$emails[$key] = trim($email);
				if (!$email) {
					unset($emails[$key]);
				} elseif (!preg_match("/^[-a-zA-Z0-9_\.]+@([0-9A-Za-z][0-9A-Za-z-]+\.)+[A-Za-z]{2,5}$/",$email)) {
					Showmsg('mode_o_email_format_err');
				}
			}
		}
		!$emails && Showmsg('mode_o_email_empty');
		if (!preg_match("/^http:/",$faceurl)) {
			$faceurl = $db_bbsurl.'/'.$faceurl;
		}
		require_once(R_P.'require/sendemail.php');
		foreach ($emails as $email) {
			sendemail($email,'email_mode_o_title','email_mode_o_content');
		}
		Showmsg('operate_success');
	}
	$username = $windid;
} elseif ($type == 'viewer') {
	$username = $windid;
	$userdb = $db->get_one("SELECT m.uid,m.username,m.email,m.groupid,m.memberid,m.icon,ud.index_privacy,ud.profile_privacy,ud.info_privacy,ud.credit_privacy,ud.owrite_privacy,ud.msgboard_privacy,ud.visits,ud.whovisit FROM pw_members m LEFT JOIN pw_ouserdata ud ON m.uid=ud.uid WHERE m.uid=".pwEscape($u));
	$whovisit = unserialize($userdb['whovisit']);
	is_array($whovisit) || $whovisit = array();
	$visituids = array_keys($whovisit);
	if ($visituids) {
		$query = $db->query("SELECT m.uid,m.username,m.icon,m.honor,md.thisvisit FROM pw_members m LEFT JOIN pw_memberdata md ON m.uid=md.uid WHERE m.uid IN (".pwImplode($visituids,false).")");
		while($rt = $db->fetch_array($query)) {
			list($rt['face']) = showfacedesign($rt['icon'],1,'m');
			$whovisit[$rt['uid']] = get_date($whovisit[$rt['uid']],($whovisit[$rt['uid']] < $tdtime ? 'm-d' : 'H:i'));
			$whovisit[$rt['uid']] = array('visittime'=>$whovisit[$rt['uid']]) + $rt;
		}
	}
} else {
	$db_perpage= 10;
	$page	= (int) GetGP('page');
	$page<1 && $page = 1;
	$start	= ($page-1)*$db_perpage;
	$ftype	= false;

	if ($space == 1) {
		$userdb = $db->get_one("SELECT index_privacy FROM pw_ouserdata WHERE uid=".pwEscape($u));
		list($isU,$privacy) = pwUserPrivacy($u,$userdb);
		if ($groupid == 3 || $isU == 2 || $isU !=2 && $privacy['index']) {
			$SpaceShow = 1;
		}
		if (!$SpaceShow) Showmsg('mode_o_index_right');

		$friend = $db->get_one("SELECT m.uid,m.username,m.icon,md.f_num FROM pw_members m LEFT JOIN pw_memberdata md ON m.uid=md.uid WHERE m.uid=".pwEscape($u));

		$username = $friend['username'];
		$count	= $friend['f_num'];
		$addurl = 'u='.$u.'&';
		list($pages,$limit) = pwLimitPages($count,$page,"$thisbase$addurl");

	} else {
		InitGP('ftid','',2);
		if (isset($_GET['ftid'])) $ftype = (int) GetGP('ftid');
		$username = $windid;
		$query = $db->query("SELECT * FROM pw_friendtype WHERE uid=".pwEscape($winduid)." ORDER BY ftid");
		$friendtype = array();
		while ($rt = $db->fetch_array($query)) {
			$friendtype[$rt['ftid']] = $rt;
		}
		$count	= $winddb['f_num'];
		list($pages,$limit) = pwLimitPages($count,$page,"$thisbase");
	}
	$friends = getFriends($u,$start,$db_perpage,$ftype,1);
	if ($friends) {
		foreach ($friends as $key => $value) {
			$value['isfriend'] = isFriend($winduid,$value['uid']);
			$friends[$key] = $value;
		}
	}
}

require_once(M_P.'require/header.php');
if ($space == 1) {

	require_once(R_P.'require/credit.php');
	list($userdb,$ismyfriend,$friendcheck,$usericon,$usercredit,$totalcredit,$appcount,$p_list) = getAppleftinfo($u);

	require_once PrintEot('user_friend');
} else {
	require_once PrintEot('m_friend');
}
footer();
?>