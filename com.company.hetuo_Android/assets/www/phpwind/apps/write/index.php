<?php
!defined('A_P') && exit('Forbidden');
!$winduid && Showmsg('not_login');
require_once(R_P.'require/showimg.php');
list($faceimg) = showfacedesign($winddb['icon'],1,'m');

$isGM = CkInArray($windid,$manager);
!$isGM && $groupid==3 && $isGM=1;

InitGP(array('space'));
$db_perpage = 10;
$do = Char_cv(GetGP('do'));
$u = (int)GetGP('u');

if ($space == 1 && $do != 'del') {
	$do = 'view';
} else {
	$u = $winduid;
}

if ($do == 'post') {
	define('AJAX','1');
	require_once(R_P.'require/postfunc.php');
/**
* 禁止受限制用户发言
*/
	banUser();
	InitGP(array('id','source','encode','tosign'));
	$ruid = 0;$minLenText = 3;$maxLenText = 255;


	$text = GetGP('text','P');

	if (!CkInArray(strtolower($encode),array('gbk','utf8','big5'))) {
		$encode = $charset;
	} elseif ($charset != $encode) {
		$text = pwConvert($text,$charset,$encode,true);
	}

	$textlen = strlen(trim($text));

	$textlen < $minLenText && Showmsg('mode_o_write_textminlen');
	$textlen > $maxLenText && Showmsg('mode_o_write_textmaxlen');
	$text = trim($text);

	require_once(R_P.'require/bbscode.php');
	$wordsfb = L::loadClass('FilterUtil');
	if (($banword = $wordsfb->comprise($text)) !== false) {
		Showmsg('content_wordsfb');
	}
	$firstchar = substr($text,0,1);
	switch ($firstchar) {
		case '@' :
			$uname = trim(substr($text,1,strpos($text,32)-1));
			$ruid = $db->get_value("SELECT uid FROM pw_members WHERE username=".pwEscape($uname));
			break;
		default :
			$ruid = (int)$id;
			$firstchar = '';
	}
	if ($ruid) {
		//$rt = $db->get_one("SELECT uid FROM pw_friends WHERE uid=".pwEscape($winduid)."AND friendid=".pwEscape($ruid)."AND status='0' LIMIT 1");
		//if (!$rt) {//只有好友才可以回复
			//$ruid = 0;
		//} else
		if ($firstchar) {
			$text = ltrim(strstr($text,32));
			strlen($text) < $minLenText && Showmsg('mode_o_write_textminlen');
		}
	}

	$rt = $db->get_one("SELECT postdate,content FROM pw_owritedata WHERE uid=".pwEscape($winduid)."ORDER BY id DESC LIMIT 1");
	if ($rt['content'] == $text) {
		Showmsg('mode_o_write_sametext');
	} elseif ($timestamp - $rt['postdate'] < 1) {
		Showmsg('mode_o_write_timelimit');
	}


	$db_blogsource = array('web'=>1,'other'=>1);//通过哪种客户端发表

	if (empty($source)) {
		$source = 'web';
	} elseif (!isset($db_blogsource[$source])) {
		$source = 'other';
	}
	$text = Char_cv($text);
	$db->update("INSERT INTO pw_owritedata SET"
		. pwSQLSingle(array(
			'uid'		=> $winduid,
			'touid'		=> $ruid,
			'postdate'	=> $timestamp,
			'isshare'	=> 0,
			'source'	=> $source,
			'content'	=> $text
		)));
	$f_id = $db->insert_id();

	updateUserAppNum($winduid,'owrite','add');

	if (getstatus($winddb['userstatus'],20,3) != 2) {
		pwAddFeed($winduid, 'write',$f_id, array('lang' => 'o_write', 'text' => $text));
	}
	if ($tosign && $winddb['honor'] != stripslashes($text)) {
		$db->update("UPDATE pw_members SET honor=".pwEscape($text)." WHERE uid=".pwEscape($winduid));
		$_cache = getDatastore();
		$_cache->delete("UID_".$winduid);/* remove db cache */
	}
	countPosts('+1');

	//会员资讯缓存
	$usercachedata = array();
	$usercache = L::loadDB('Usercache');
	$usercachedata['content'] = substrs(stripWindCode($text),100,N);
	$usercachedata['postdate'] = $timestamp;
	$usercache->update($winduid,'write',$f_id,$usercachedata);

	//积分变动
	require_once(R_P.'require/credit.php');
	$o_write_creditset = unserialize($o_write_creditset);
	$creditset = getCreditset($o_write_creditset['Post']);
	$creditset = array_diff($creditset,array(0));
	if (!empty($creditset)) {
		$credit->sets($winduid,$creditset,true);
		updateMemberid($winduid);
	}

	if ($creditlog = unserialize($o_write_creditlog)) {
		addLog($creditlog['Post'],$windid,$winduid,'write_Post');
	}
	Showmsg('mode_o_write_success');

} elseif ($do == 'del') {
	define('AJAX','1');
	$id = (int)GetGP('id');

	if ($groupid != 3) {
		$sql = "AND uid=".pwEscape($winduid);
		$authorid = $winduid;
		$author = $windid;
	} else {
		$owritedata = $db->get_one("SELECT o.*,m.username FROM pw_owritedata o LEFT JOIN pw_members m ON o.uid=m.uid WHERE o.id=".pwEscape($id));
		if (empty($owritedata)) {
			Showmsg('mode_o_write_del_error');
		}
		$sql = "AND uid=".pwEscape($owritedata['uid']);
		$authorid = $owritedata['uid'];
		$author = $owritedata['username'];
	}

	$db->update("DELETE FROM pw_owritedata WHERE id=".pwEscape($id).$sql);
	if ($db->affected_rows() == 0) {
		Showmsg('mode_o_write_del_error');
	} else {
		$affected_rows = delAppAction('write',$id)+1;
		countPosts("-$affected_rows");

		$usercache = L::loadDB('Usercache');
		$usercache->delete($winduid,'write',$id);

		//积分变动
		require_once(R_P.'require/credit.php');
		$o_write_creditset = unserialize($o_write_creditset);
		$creditset = getCreditset($o_write_creditset['Delete'],false);
		$creditset = array_diff($creditset,array(0));
		if (!empty($creditset)) {
			require_once(R_P.'require/postfunc.php');
			$credit->sets($authorid,$creditset,true);
			updateMemberid($authorid,false);
		}

		if ($creditlog = unserialize($o_write_creditlog)) {
			addLog($creditlog['Delete'],$author,$authorid,'write_Delete');
		}
		updateUserAppNum($authorid,'owrite','minus');
		Showmsg('mode_o_write_del');
	}
} elseif ($do == 'my') {
	$u != $winduid && Showmsg('undefined_action');
	$writedata = array();
	$count = $db->get_value("SELECT COUNT(*) FROM pw_owritedata WHERE touid=".pwEscape($winduid));
	if ($count) {
		$page = (int)GetGP('page');
		list($pages,$limit) = pwLimitPages($count,$page,"{$basename}do=my&");

		$query = $db->query("SELECT w.*,m.username,m.icon,m.groupid FROM pw_owritedata w LEFT JOIN pw_members m ON w.uid=m.uid WHERE w.touid=".pwEscape($winduid)."ORDER BY w.id DESC $limit");
		while ($rt = $db->fetch_array($query)) {
			if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
				$rt['content'] = appShield('ban_write');
			}
			list($rt['postdate']) = getLastDate($rt['postdate']);
			list($rt['icon']) = showfacedesign($rt['icon'],1,'m');
			if ($rt['touid'] && $rt['username']) {
				$rt['content'] = '<a href="u.php?uid='.$val[uid].'">'.$val[username].'</a> @<a href="u.php?&uid='.$winduid.'">'.$windid.'</a> '.$rt['content'];
			}
			$writedata[] = $rt;
		}
	}
	$username = $windid;
} elseif ($do == 'view') {
	if ($space == 1 && defined('F_M')) {
		//$basename .= 'space=1&';
		//$spaceurl = $basename."u=$u&";
		$spaceurl = $baseUrl;
	}
	if ($u && $u != $winduid) {
		$uinfo = $db->get_one("SELECT m.uid,m.username,m.icon,m.groupid,ud.index_privacy,ud.owrite_privacy FROM pw_members m LEFT JOIN pw_ouserdata ud ON m.uid=ud.uid WHERE m.uid=".pwEscape($u,false));
		$username = $uinfo['username'];
		$pageurl = "{$basename}do=view&";
	} else {
		$u = (int)$winduid;
		$uinfo = $winddb;
		$username = $windid;
		$pageurl = "{$basename}do=view&";
	}
	list($isU,$privacy) = pwUserPrivacy($u,$uinfo);
	if ($groupid == 3 || $isU == 2 || $isU !=2 && $privacy['index']) {
		$SpaceShow = 1;
	}
	if ($u != $winduid) {
		if (!$privacy['owrite'] && !$isGM || !$SpaceShow) {
			Showmsg('mode_o_write_right');
		}
	}
	list($faceimg) = showfacedesign($uinfo['icon'],1,'m');
	$writedata = array();
	$count = $db->get_value("SELECT COUNT(*) FROM pw_owritedata WHERE uid=".pwEscape($u,false));

	if ($count) {
		$page = (int)GetGP('page');
		list($pages,$limit) = pwLimitPages($count,$page,$pageurl);

		$query = $db->query("SELECT w.*,m.username,m.groupid FROM pw_owritedata w LEFT JOIN pw_members m ON w.touid=m.uid WHERE w.uid=".pwEscape($u,false)."ORDER BY w.id DESC $limit");
		while ($rt = $db->fetch_array($query)) {
			if ($uinfo['groupid'] == 6 && $db_shield && $groupid != 3) {
				$rt['content'] = appShield('ban_write');
			}
			list($rt['postdate']) = getLastDate($rt['postdate']);
			if ($rt['touid'] && $rt['username']) {
				$rt['content'] = '@<a href="u.php?uid='.$rt['touid'].'">'.$rt['username'].'</a> '.$rt['content'];
			}
			$writedata[] = $rt;
		}
	}
} else {//HOME
	$friends = array($winduid);
	$query = $db->query("SELECT f.friendid FROM pw_friends f left join pw_ouserdata o ON o.uid=f.friendid WHERE f.uid=".pwEscape($winduid)."AND f.status=0 AND o.owrite_privacy != 2");
	while ($rt = $db->fetch_array($query)) {
		$friends[] = $rt['friendid'];
	}
	$writedata = $touids = array();
	$count = $db->get_value("SELECT COUNT(*) FROM pw_owritedata WHERE uid IN (".pwImplode($friends,false).") ");
	if ($count) {
		require_once(R_P.'require/showimg.php');
		$page = (int)GetGP('page');
		list($pages,$limit) = pwLimitPages($count,$page,"{$basename}");
		$query = $db->query("SELECT w.*,m.username,m.icon,m.groupid FROM pw_owritedata w LEFT JOIN pw_members m ON w.uid=m.uid WHERE w.uid IN (".pwImplode($friends,false).") ORDER BY w.id DESC $limit");
		while ($rt = $db->fetch_array($query)) {
			if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
				$rt['content'] = appShield('ban_write');
			}
			$rt['touid'] && $touids[$rt['touid']] = $rt['touid'];
			list($rt['postdate']) = getLastDate($rt['postdate']);
			list($rt['icon']) = showfacedesign($rt['icon'],1,'m');
			$writedata[] = $rt;
		}
		if ($touids) {
			$query = $db->query("SELECT uid,username FROM pw_members WHERE uid IN (".pwImplode($touids,false).")");
			while ($rt = $db->fetch_array($query)) {
				$touids[$rt['uid']] = $rt['username'];
			}
		}
	}
	$username = $windid;
}
//require_once(M_P.'require/header.php');
if ($space == 1 && defined('F_M')) {

	require_once(R_P.'require/credit.php');
	list($userdb,$ismyfriend,$friendcheck,$usericon,$usercredit,$totalcredit,$appcount,$p_list) = getAppleftinfo($u);
	//require_once(M_P.'require/header.php');
	require_once PrintEot('header');
	require_once PrintEot('user_write');
	footer();
} else {
	//require_once PrintEot('m_write');
	list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_write",true);
}
//footer();

?>