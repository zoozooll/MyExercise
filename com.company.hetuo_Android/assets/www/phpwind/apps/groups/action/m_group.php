<?php
!defined('A_P') && exit('Forbidden');
!$winduid && Showmsg('not_login');

if ($db_question && ($o_share_qcheck || $o_groups_p_qcheck)) {
	$qkey = array_rand($db_question);
}

InitGP(array('a'));
InitGP(array('cyid','page'), null, 2);
$db_perpage = 20;

!$db_groups_open && Showmsg('groups_close');

$colony = $db->get_one("SELECT c.*,cm.id AS ifcyer,cm.ifadmin,cm.lastvisit FROM pw_colonys c LEFT JOIN pw_cmembers cm ON c.id=cm.colonyid AND cm.uid=" . pwEscape($winduid) . ' WHERE c.id=' . pwEscape($cyid));
empty($colony) && Showmsg('data_error');

$colony['createtime'] = get_date($colony['createtime'], 'Y-m-d');
$colony['annouce'] && $colony['annouce_temp'] = substrs($colony['annouce'],20);
if ($colony['cnimg']) {
	list($cnimg,$colony['imgtype']) = geturl("cn_img/$colony[cnimg]",'lf');
} else {
	$cnimg = $pwModeImg.'/groupnopic.gif';
}
if ($colony['banner']) {
	list($colony['banner']) = geturl("cn_img/$colony[banner]",'lf');
}
if ($colony['ifcyer']) {
	if ($timestamp - $colony['lastvisit'] > 3600) {
		$db->update("UPDATE pw_cmembers SET lastvisit=" . pwEscape($timestamp) . ' WHERE id=' . pwEscape($colony['ifcyer']));
	}
}

$a_key = 'index';
$ifadmin = ($colony['ifadmin'] == '1' || $colony['admin'] == $windid || $groupid == 3);

if (empty($a)) {

	require_once(R_P.'require/showimg.php');
	$colony['annouce'] = nl2br($colony['annouce']);
	list($faceurl) = showfacedesign($winddb['icon'],1,'m');

	$udb = $uids = $newvisit = $newphoto = $magdb = $memdb = $argdb = array();
	$query = $db->query("SELECT cp.pid,cp.path,cp.ifthumb,m.groupid FROM pw_cnalbum ca LEFT JOIN pw_cnphoto cp ON ca.aid=cp.aid LEFT JOIN pw_members m ON cp.uploader=m.username WHERE ca.atype='1' AND ca.ownerid=" . pwEscape($cyid) . ' ORDER BY cp.pid DESC LIMIT 8');
	while ($rt = $db->fetch_array($query)) {
		if (!$rt['pid']) continue;
		$rt['path'] = getphotourl($rt['path'], $rt['ifthumb']);
		if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
			$rt['path'] = $pwModeImg.'/banuser.gif';
		}
		$newphoto[] = $rt;
	}

	$query = $db->query("SELECT uid,username FROM pw_cmembers WHERE colonyid=" . pwEscape($cyid) . " AND ifadmin='1' ORDER BY addtime ASC");
	while ($rt = $db->fetch_array($query)) {
		$uids[] = $rt['uid'];
		$magdb[] = $rt;
	}

	$totalMembers = $db->get_value("SELECT COUNT(*) as count FROM pw_cmembers WHERE colonyid=" . pwEscape($cyid) . " LIMIT 1");

	$query = $db->query("SELECT uid,username,lastvisit FROM pw_cmembers WHERE colonyid=" . pwEscape($cyid) . " ORDER BY lastvisit DESC LIMIT 9");
	while ($rt = $db->fetch_array($query)) {
		$newvisit[$rt['uid']] = $rt['lastvisit'];
		$uids[] = $rt['uid'];
		$memdb[] = $rt;
	}

	$query = $db->query("SELECT a.topped,t.tid,t.author,t.authorid,t.postdate,t.lastpost,t.subject FROM pw_argument a LEFT JOIN pw_threads t ON a.tid=t.tid WHERE a.cyid=" . pwEscape($cyid) . ' ORDER BY a.topped,a.lastpost DESC LIMIT 10');
	while ($rt = $db->fetch_array($query)) {
		$rt['postdate']	= get_date($rt['postdate'],'m-d');
		$rt['lastpost'] = get_date($rt['lastpost']);
		$argdb[] = $rt;
	}
	$db->free_result($query);

	$visitor = $colony['visitor'] ? (array)unserialize($colony['visitor']) : array();
	if (!$colony['ifcyer'] && (!isset($visitor[$winduid]) || $timestamp - $visitor[$winduid] > 3600)) {
		$visitor[$winduid] = $timestamp;
		arsort($visitor);
		while (count($visitor) > 50) {
			array_pop($visitor);
		}
		$db->update("UPDATE pw_colonys SET visitor=" . pwEscape(serialize($visitor)) . ' WHERE id=' . pwEscape($cyid));
	}
	if ($visitor) {
		$uids = array_merge($uids, array_keys($visitor));
		$newvisit += $visitor;
		arsort($newvisit);
		while (count($newvisit) > 12) {
			array_pop($newvisit);
		}
	}
	if ($uids) {
		$query = $db->query("SELECT uid,username,icon FROM pw_members WHERE uid IN (" . pwImplode($uids) . ')');
		while ($rt = $db->fetch_array($query)) {
			list($rt['faceurl']) = showfacedesign($rt['icon'], 1, 'm');
			$udb[$rt['uid']] = $rt;
		}
	}
//	require_once(M_P.'require/header.php');
//	require_once PrintEot('m_group');
//	footer();
	list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_group",true);

} elseif ($a == 'thread') {

	$a_key = 'thread';
	if (!$colony['ifopen'] && !$ifadmin && (!$colony['ifcyer'] || $colony['ifadmin'] == '-1')) {
		Showmsg('colony_cnmenber');
	}
	$argdb = array();
	$count = $db->get_value("SELECT COUNT(*) AS sum FROM pw_argument WHERE cyid=" . pwEscape($cyid));
	//$count = $db->get_value("SELECT COUNT(*) AS sum FROM pw_argument WHERE gid=" . pwEscape($cyid) . " AND tpcid='0'");

	if ($count) {
		list($pages,$limit) = pwLimitPages($count, $page, "{$basename}a=$a&cyid=$cyid&");
		$query = $db->query("SELECT t.tid,t.author,t.authorid,t.postdate,t.lastpost,t.subject FROM pw_argument a LEFT JOIN pw_threads t ON a.tid=t.tid WHERE a.cyid=" . pwEscape($cyid) . " ORDER BY a.topped,a.lastpost DESC $limit");
		//$query = $db->query("SELECT tid,author,authorid,lastpost,subject,topped,toppedtime FROM pw_argument WHERE gid=" . pwEscape($cyid) . " AND tpcid='0' ORDER BY topped DESC,lastpost DESC $limit");
		while ($rt = $db->fetch_array($query)) {
			/*
			if ($rt['topped']>0 && $rt['toppedtime'] && ($timestamp > $rt['toppedtime'])) {
				$db->update("UPDATE pw_argument SET topped='0',toppedtime='0' WHERE tid=".pwEscape($rt['tid']));
			}
			*/
			$rt['lastpost'] = get_date($rt['lastpost']);
			$argdb[] = $rt;
		}
		$db->free_result($query);
	}

//	require_once(M_P.'require/header.php');
//	require_once PrintEot('m_group');
//	footer();
	list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_group",true);

} elseif ($a == 'read') {

	if (!$colony['ifopen'] && !$ifadmin && (!$colony['ifcyer'] || $colony['ifadmin'] == '-1')) {
		Showmsg('colony_cnmenber');
	}
	$a_key = 'thread';
	InitGP(array('tid'), null, 2);

	$page < 1 && $page = 1;
	$lou = ($page - 1) * $db_perpage;

	$S_sql = $J_sql = '';
	if ($page == 1) {
		$pw_tmsgs = GetTtable($tid);
		$S_sql = ',tm.*,m.uid,m.username,m.groupid,m.memberid,m.icon,m.userstatus';
		$J_sql = " LEFT JOIN $pw_tmsgs tm ON t.tid=tm.tid LEFT JOIN pw_members m ON m.uid=t.authorid";
	}
	$read = $db->get_one("SELECT t.*{$S_sql},a.cyid FROM pw_threads t LEFT JOIN pw_argument a ON a.tid=t.tid{$J_sql} WHERE t.tid=" . pwEscape($tid) . ' AND a.tid IS NOT NULL');

	if (empty($read) || $read['cyid'] != $cyid || $read['fid'] != $colony['classid']) {
		Showmsg('data_error');
	}
	$foruminfo = L::forum($read['fid']);
	
	$readdb = $_pids = $attachdb = array();
	$pw_posts = GetPtable($read['ptable']);

	require_once(R_P.'require/showimg.php');
	require_once(R_P.'require/bbscode.php');
//	require_once(M_P.'require/header.php');

	$wordsfb = L::loadClass('FilterUtil');
	if ($page == 1) {
		$read['pid'] = 'tpc';
		$readdb[] = $read;
		$read['aid'] && $_pids['tpc'] = 0;
		$lou--;
	}
	if ($read['replies'] > 0) {
		list($pages, $limit) = pwLimitPages($read['replies'], $page, "{$basename}a=$a&cyid=$cyid&tid=$tid&");
		$query = $db->query("SELECT t.*,m.uid,m.username,m.groupid,m.memberid,m.icon,m.userstatus FROM $pw_posts t LEFT JOIN pw_members m ON m.uid=t.authorid WHERE t.tid=".pwEscape($tid)." AND t.ifcheck='1' ORDER BY t.postdate $limit");
		while ($read = $db->fetch_array($query)) {
			$read['aid'] && $_pids[$read['pid']] = $read['pid'];
			$readdb[] = $read;
		}
	}
	if ($_pids) {
		$query = $db->query('SELECT * FROM pw_attachs WHERE tid=' . pwEscape($tid) . " AND pid IN (" . pwImplode($_pids) . ")");
		while ($rt = $db->fetch_array($query)) {
			if ($rt['pid'] == '0') $rt['pid'] = 'tpc';
			$attachdb[$rt['pid']][$rt['aid']] = $rt;
		}
	}
	foreach ($readdb as $key => $read) {
		$tpc_pid = $read['pid'];
		$tpc_author = $read['author'];
		$read['lou'] = ++$lou;
		$db_menuinit .= ",'read_$read[lou]' : 'read_1_$read[lou]'";
		$attachs = $aids = array();
		if ($read['aid'] && isset($attachdb[$read['pid']])) {
			$attachs = $attachdb[$read['pid']];
			$read['ifhide'] > 0 && ifpost($tid) >= 1 && $read['ifhide'] = 0;
			if (is_array($attachs) && !$read['ifhide']) {
				$aids = attachment($read['content']);
			}
		}
		$tpc_shield = 0;
		$read['ifsign'] < 2 && $read['content'] = str_replace("\n","<br />",$read['content']);
		if ($read['ifshield'] || $read['groupid'] == 6 && $db_shield) {
			if ($read['ifshield'] == 2) {
				$read['content'] = shield('shield_del_article');
				$read['subject'] = '';
				$tpc_shield = 1;
			} else {
				if ($groupid == '3') {
					$read['subject'] = shield('shield_title');
				} else {
					$read['content'] = shield($read['ifshield'] ? 'shield_article' : 'ban_article');
					$read['subject'] = '';
					$tpc_shield = 1;
				}
			}
		}
		if (!$tpc_shield) {
			if (!$wordsfb->equal($read['ifwordsfb'])) {
				$read['content'] = $wordsfb->convert($read['content'], array(
					'id'	=> $read['pid'] == 'tpc' ? $tid : $read['pid'],
					'type'	=> $read['pid'] == 'tpc' ? 'topic' : 'posts',
					'code'	=> $read['ifwordsfb']
				));
			}
			if ($read['ifconvert'] == 2) {
				$read['content'] = convert($read['content'], $db_windpost);
			} else {
				//$tpc_tag && $db_readtag && $read['content'] = relatetag($read['content'], $tpc_tag);
				strpos($read['content'],'[s:') !== false && $read['content'] = showface($read['content']);
			}
			if ($attachs && is_array($attachs) && !$read['ifhide']) {
				if ($winduid == $read['authorid'] || $isGM || $pwSystem['delattach']) {
					$dfadmin = 1;
				} else {
					$dfadmin = 0;
				}
				foreach ($attachs as $at) {
					$atype = '';
					$rat = array();
					if ($at['type'] == 'img' && $at['needrvrc'] == 0 && (!$GLOBALS['downloadimg'] || !$GLOBALS['downloadmoney'] || $_G['allowdownload'] == 2)) {
						$a_url = geturl($at['attachurl'],'show');
						if (is_array($a_url)) {
							$atype = 'pic';
							$dfurl = '<br>'.cvpic($a_url[0], 1, $db_windpost['picwidth'], $db_windpost['picheight'], $at['ifthumb']);
							$rat = array('aid' => $at['aid'], 'img' => $dfurl, 'dfadmin' => $dfadmin, 'desc' => $at['descrip']);
						} elseif ($a_url == 'imgurl') {
							$atype = 'picurl';
							$rat = array('aid' => $at['aid'], 'name' => $at['name'], 'dfadmin' => $dfadmin, 'verify' => md5("showimg{$tid}{$read[pid]}{$fid}{$at[aid]}{$GLOBALS[db_hash]}"));
						}
					} else {
						$atype = 'downattach';
						if ($at['needrvrc'] > 0) {
							!$at['ctype'] && $at['ctype'] = $at['special'] == 2 ? 'money' : 'rvrc';
							$at['special'] == 2 && $GLOBALS['db_sellset']['price'] > 0 && $at['needrvrc'] = min($at['needrvrc'], $GLOBALS['db_sellset']['price']);
						}
						$rat = array('aid' => $at['aid'], 'name' => $at['name'], 'size' => $at['size'], 'hits' => $at['hits'], 'needrvrc' => $at['needrvrc'], 'special' => $at['special'], 'cname' => $GLOBALS['creditnames'][$at['ctype']], 'type' => $at['type'], 'dfadmin' => $dfadmin, 'desc' => $at['desc'], 'ext' => strtolower(substr(strrchr($at['name'],'.'),1)));
					}
					if (!$atype) continue;
					if (in_array($at['aid'], $aids)) {
						$read['content'] = attcontent($read['content'], $atype, $rat);
					} else {
						$read[$atype][$at['aid']] = $rat;
					}
				}
			}
		}
		list($read['icon']) = showfacedesign($read['icon'],1);
		$read['postdate'] = get_date($read['postdate']);
		$readdb[$key] = $read;
	}

//	require_once(M_P.'require/header.php');
//	require_once PrintEot('m_group');
//	footer();
	list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_group",true);

} elseif ($a == 'post') {

	$a_key = 'thread';
	$_G['uploadtype'] && $db_uploadfiletype = $_G['uploadtype'];
	$db_uploadfiletype = !empty($db_uploadfiletype) ? (is_array($db_uploadfiletype) ? $db_uploadfiletype : unserialize($db_uploadfiletype)) : array();

	if (!$ifadmin && (!$colony['ifcyer'] || $colony['ifadmin'] == '-1')) {
		if ($colony['ifadmin'] == '-1') {
			Showmsg('colony_post');
		} else {
			Showmsg('colony_post2');
		}
	}

	if (empty($_POST['step'])) {

//		require_once(M_P.'require/header.php');
//		require_once PrintEot('m_group');
//		footer();
		$editor = getstatus($winddb['userstatus'],11) ? 'wysiwyg' : 'textmode';
		$uploadfiletype = $uploadfilesize = ' ';
		foreach ($db_uploadfiletype as $key => $value) {
			$uploadfiletype .= $key.' ';
			$uploadfilesize .= $key.':'.$value.'KB; ';
		}
		$mutiupload = 0;
		if ($db_allowupload && $_G['allowupload']) {
			$mutiupload = $db->get_value("SELECT COUNT(*) AS sum FROM pw_attachs WHERE tid=0 AND pid='0' AND uid=" . pwEscape($winduid));
		}
		list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_group",true);

	} else {

		PostCheck(1,$o_groups_p_gdcheck,$o_groups_p_qcheck);
		/**
		* 禁止受限制用户发言
		*/
		banUser();

		require_once(R_P . 'lib/post.class.php');
		require_once(R_P . 'lib/forum.class.php');
		require_once(R_P . 'require/bbscode.php');

		$pwforum = new PwForum($colony['classid']);
		$pwpost  = new PwPost($pwforum);

		require_once(R_P . 'lib/topicpost.class.php');
		//require_once(R_P . 'apps/groups/lib/group.class.php');
		$topicpost = new topicPost($pwpost);
		//$topicpost->group = new PwGroup($colony['id']);
		$topicpost->check();

		InitGP(array('atc_title','atc_content','atc_convert','flashatt'), 'P');

		$postdata = new topicPostData($pwpost);
		$postdata->setTitle($atc_title);
		$postdata->setContent($atc_content);
		$postdata->setConvert($atc_convert);
		$postdata->setIfsign(1, 0);
		$postdata->setStatus(1);
		$postdata->conentCheck();

		require_once(R_P . 'lib/upload/attupload.class.php');
		if (PwUpload::getUploadNum() || $flashatt) {
			$postdata->att = new AttUpload($winduid, $flashatt);
			$postdata->att->check();
			$postdata->att->transfer();
			PwUpload::upload($postdata->att);
		}
		$topicpost->execute($postdata);

		$tid = $topicpost->getNewId();
		$db->update("INSERT INTO pw_argument SET " . pwSqlSingle(array('tid' => $tid, 'cyid' => $colony['id'], 'postdate' => $timestamp, 'lastpost' => $timestamp)));

		pwAddFeed($winduid, 'colony_post', $cyid, array(
			'lang'			=> 'colony_post',
			'cyid'			=> $cyid,
			'colony_name'	=> $colony_name,
			'username'		=> $windid,
			'uid'			=> $winduid,
			'tid'			=> $tid,
			'title'			=> $atc_title
		));

		refreshto("{$basename}a=read&cyid=$cyid&tid=$tid",'colony_postsuccess');
	}
} elseif ($a == 'reply') {

	if (!$ifadmin && (!$colony['ifcyer'] || $colony['ifadmin'] == '-1')) {
		if ($colony['ifadmin'] == '-1') {
			Showmsg('colony_reply');
		} else {
			Showmsg('colony_reply2');
		}
	}
	PostCheck(1,$o_groups_p_gdcheck,$o_groups_p_qcheck);
	banUser();

	InitGP(array('tid'), null, 2);
	InitGP(array('atc_content'));
	$tpc = $db->get_one("SELECT t.tid,t.fid,t.locked,t.ifcheck,t.author,t.authorid,t.postdate,t.lastpost,t.ifmail,t.special,t.subject,t.type, t.ifshield,t.anonymous,t.ptable,t.replies,t.tpcstatus,a.cyid FROM pw_threads t LEFT JOIN pw_argument a ON a.tid=t.tid WHERE t.tid=" . pwEscape($tid) . ' AND a.tid IS NOT NULL');

	if (empty($tpc) || $tpc['cyid'] != $cyid || $tpc['fid'] != $colony['classid']) {
		Showmsg('data_error');
	}
	require_once(R_P . 'lib/post.class.php');
	require_once(R_P . 'lib/forum.class.php');
	require_once(R_P . 'require/bbscode.php');

	$pwforum = new PwForum($colony['classid']);
	$pwpost  = new PwPost($pwforum);

	require_once(R_P . 'lib/replypost.class.php');
	$replypost = new replyPost($pwpost);
	$replypost->check();
	$replypost->setTpc($tpc);

	if (!$pwpost->isGM && $tpc['locked']%3<>0 && !pwRights($pwpost->isBM,'replylock')) {
		Showmsg('reply_lockatc');
	}
	$postdata = new replyPostData($pwpost);
	$postdata->setTitle('');
	$postdata->setContent($atc_content);
	$postdata->setConvert(1);
	$postdata->setIfsign(1, 0);
	$postdata->conentCheck();

	$replypost->execute($postdata);

	refreshto("{$basename}a=read&cyid=$cyid&tid=$tid",'colony_postsuccess');

} elseif ($a == 'member') {

	$a_key = 'member';

	if (empty($_POST['step'])) {

		require_once(R_P.'require/showimg.php');
		InitGP(array('group'));
		if ($group == 1 || $group == 2 || $group == 3 || empty($group)){
			$sqlsel = '';
			if ($group == 1) {
				$sqlsel = " AND cm.ifadmin='1'";
			} elseif ($group == 2) {
				$sqlsel = " AND cm.ifadmin='0'";
			} elseif ($group == 3) {
				$sqlsel = " AND cm.ifadmin='-1'";
			}
			$total = $db->get_value("SELECT COUNT(*) AS sum FROM pw_cmembers cm WHERE cm.colonyid=" . pwEscape($cyid) . $sqlsel);
			if ($total) {
				list($pages,$limit) = pwLimitPages($total,$page,"{$basename}a=member&cyid=$cyid&group=$group&");
				$memdb = array();
				$query = $db->query("SELECT cm.*,m.icon FROM pw_cmembers cm LEFT JOIN pw_members m ON cm.uid=m.uid WHERE cm.colonyid=" . pwEscape($cyid) . $sqlsel . " ORDER BY cm.ifadmin DESC $limit");
				while ($rt = $db->fetch_array($query)) {
					list($rt['icon']) = showfacedesign($rt['icon'],1);
					$memdb[] = $rt;
				}
			}
		} elseif ($group == 4) {
			$visitor = unserialize($colony['visitor']);
			$total = count($visitor);
			$numofpage = ceil($total/$db_perpage);
			$numofpage = ($db_maxpage && $numofpage > $db_maxpage) ? $db_maxpage : $numofpage;
			$page < 1 ? $page = 1 : ($page > $numofpage ? $page = $numofpage : null);
			$pageurl = "{$basename}a=member&cyid=$cyid&group=4&";
			$pages = numofpage($total,$page,$numofpage,$pageurl,$db_maxpage);
			$colony['visitor'] = $colony['visitor'] ? unserialize($colony['visitor']) : array();
			$visitor = array_slice($colony['visitor'],($page-1)*$db_perpage,$db_perpage,true);
			$visitorids = array_keys($visitor);
			if ($visitorids) {
				$query = $db->query("SELECT uid,username,icon FROM pw_members WHERE uid IN(".pwImplode($visitorids).")");
				while ($rt = $db->fetch_array($query)) {
					list($rt['icon']) = showfacedesign($rt['icon'],1);
					$memdb[] = $rt;
				}
			} else {
				$memdb = array();
			}
		}
//		require_once(M_P.'require/header.php');
//		require_once PrintEot('m_group');
//		footer();
		list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_group",true);

	} else {

		!$ifadmin && Showmsg('undefined_action');

		InitGP(array('selid'), 'P', 2);
		if (!$selid || !is_array($selid)) {
			Showmsg('id_error');
		}
		$msgdb = array(
			'toUser'	=> array(),
			'fromUid'	=> 0,
			'fromUser'	=> '',
			'subject'	=> 'o_' . $_POST['step'] . '_title',
			'content'	=> 'o_' . $_POST['step'] . '_content',
			'other'		=> array(
				'cname'	=> Char_cv($colony['cname']),
				'curl'	=> "$db_bbsurl/{$basename}cyid=$cyid"
			)
		);
		switch ($_POST['step']) {
			case 'addadmin':
				$colony['admin'] != $windid && Showmsg('colony_manager');
				$query = $db->query("SELECT username FROM pw_cmembers WHERE colonyid=" . pwEscape($cyid) . ' AND uid IN(' . pwImplode($selid) . ") AND ifadmin!='1'");
				while ($rt = $db->fetch_array($query)) {
					$msgdb['toUser'][] = $rt['username'];
				}
				$db->update("UPDATE pw_cmembers SET ifadmin='1' WHERE colonyid=" . pwEscape($cyid) . ' AND uid IN(' . pwImplode($selid) . ") AND ifadmin!='1'");
				break;
			case 'deladmin':
				$colony['admin'] != $windid && Showmsg('colony_manager');
				$query = $db->query("SELECT username FROM pw_cmembers WHERE colonyid=" . pwEscape($cyid) . ' AND uid IN(' . pwImplode($selid) . ") AND ifadmin='1'");
				while ($rt = $db->fetch_array($query)) {
					$colony['admin'] == $rt['username'] && Showmsg('colony_delladminfail');
					$msgdb['toUser'][] = $rt['username'];
				}
				$db->update("UPDATE pw_cmembers SET ifadmin='0' WHERE colonyid=" . pwEscape($cyid) . ' AND uid IN(' . pwImplode($selid) . ") AND ifadmin='1'");
				break;
			case 'check':
				$query = $db->query("SELECT uid,username FROM pw_cmembers WHERE colonyid=" . pwEscape($cyid) . ' AND uid IN(' . pwImplode($selid) . ") AND ifadmin='-1'");
				while ($rt = $db->fetch_array($query)) {
					$check_uids[] = $rt['uid'];
					$msgdb['toUser'][] = $rt['username'];
				}
				$db->update("UPDATE pw_cmembers SET ifadmin='0' WHERE colonyid=" . pwEscape($cyid) . ' AND uid IN(' . pwImplode($selid) . ") AND ifadmin='-1'");
				updateUserAppNum($check_uids,'group');
				break;
			case 'del':
				$query = $db->query("SELECT username,ifadmin FROM pw_cmembers WHERE colonyid=" . pwEscape($cyid) . ' AND uid IN(' . pwImplode($selid) . ")");
				while ($rt = $db->fetch_array($query)) {
					if ($rt['username'] == $colony['admin']) {
						Showmsg('colony_delfail');
					}
					if ($rt['ifadmin'] == '1' && $colony['admin'] != $windid) {
						Showmsg('colony_manager');
					}
					$msgdb['toUser'][] = $rt['username'];
				}
				$db->update("DELETE FROM pw_cmembers WHERE colonyid=" . pwEscape($cyid) . " AND uid IN(" . pwImplode($selid) . ")");
				$count = $db->affected_rows();
				$db->update("UPDATE pw_colonys SET members=members-" . pwEscape($count,false) . " WHERE id=" . pwEscape($cyid));
				break;
			default:
				Showmsg('undefined_action');
		}
		if ($msgdb['toUser']) {
			require_once(R_P.'require/msg.php');
			pwSendMsg($msgdb);
		}
		refreshto("{$basename}a=$a&cyid=$cyid",'operate_success');
	}

} elseif ($a == 'uintro') {

	$a_key = 'uintro';
	define('F_M',true);

	if (empty($_POST['step'])) {

		InitGP(array('uid'), null, 2);
		InitGP(array('job'));
		if ($job != 'set') {
			define('AJAX','1');
		}
		empty($uid) && $uid = $winduid;

		$uinfo = $db->get_one("SELECT uid,username,realname,gender,tel,email,address,introduce FROM pw_cmembers WHERE colonyid=" . pwEscape($cyid) . ' AND uid=' . pwEscape($uid));
		!$uinfo && Showmsg('data_error');
		if (defined('AJAX')) {
			require_once PrintEot('m_ajax');
			footer();
		} else {
//			require_once(M_P.'require/header.php');
			//require_once PrintEot('m_group');
			list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_group",true);
		}
//		footer();

	} else {

		require_once(R_P.'require/postfunc.php');
		InitGP(array('realname','tel','email','address','introduce'),'P');
		InitGP(array('gender'), null, 2);

		require_once(R_P.'require/bbscode.php');
		$wordsfb = L::loadClass('FilterUtil');
		foreach (array($realname, $address, $introduce) as $key => $value) {
			if (($banword = $wordsfb->comprise($value)) !== false) {
				Showmsg('content_wordsfb');
			}
		}

		if (!empty($email) && !ereg("^[-a-zA-Z0-9_\.]+\@([0-9A-Za-z][0-9A-Za-z-]+\.)+[A-Za-z]{2,5}$",$email)) {
			Showmsg('illegal_email');
		}
		$db->update("UPDATE pw_cmembers SET " . pwSqlSingle(array(
				'realname'	=> $realname,
				'gender'	=> $gender,
				'tel'		=> $tel,
				'email'		=> $email,
				'address'	=> $address,
				'introduce'	=> $introduce,
			)) . " WHERE colonyid=" . pwEscape($cyid) . " AND uid=".pwEscape($winduid)
		);
		refreshto("{$basename}a=$a&cyid=$cyid&job=set",'colony_cardsuccess');
	}

} elseif ($a == 'set') {

	!$ifadmin && Showmsg('undefined_action');
	$a_key = 'set';

	if (empty($_POST['step'])) {

	

		$ifcheck_0 = $ifcheck_1 = $ifcheck_2 = $ifopen_Y = $ifopen_N = $albumopen_Y = $albumopen_N = '';
		${'ifcheck_'.$colony['ifcheck']} = 'selected';
		${'ifopen_'.($colony['ifopen'] ? 'Y' : 'N')} = 'checked';
		${'albumopen_'.($colony['albumopen'] ? 'Y' : 'N')} = 'checked';
		$filetype = (is_array($db_uploadfiletype) ? $db_uploadfiletype : unserialize($db_uploadfiletype));
		$default_type = array('gif','jpg','jpeg','bmp','png');
		foreach($default_type as $value) {
			$cnimg_1[$value] = $o_imgsize ? $o_imgsize :  $filetype[$value];
			$cnimg_2[$value] = 2048;
		}

//		require_once(M_P.'require/header.php');
//		require_once PrintEot('m_group');
//		footer();
		list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_group",true);

	} else {

		//require_once(R_P.'require/postfunc.php');
		InitGP(array('cname','annouce','descrip'),'P');
		!$cname && Showmsg('colony_emptyname');
		strlen($cname) > 20 && Showmsg('colony_cnamelimit');
		(!$descrip || strlen($descrip) > 255) && Showmsg('colony_descriplimit');

		require_once(R_P.'require/bbscode.php');
		$wordsfb = L::loadClass('FilterUtil');
		foreach (array($cname, $annouce, $descrip) as $key => $value) {
			if (($banword = $wordsfb->comprise($value)) !== false) {
				Showmsg('content_wordsfb');
			}
		}
		strlen($annouce) > 500 && Showmsg('colony_annoucelimit');
		$annouce = explode("\n",$annouce,5);
		end($annouce);
		$annouce[key($annouce)] = str_replace(array("\r","\n"),'',current($annouce));
		$annouce = implode("\r\n",$annouce);

		if ($colony['cname'] != stripcslashes($cname) && $db->get_value("SELECT id FROM pw_colonys WHERE cname=" . pwEscape($cname))) {
			Showmsg('colony_samename');
		}
		InitGP(array('ifcheck','ifopen','albumopen'), 'P', 2);

		require_once(A_P . 'groups/lib/imgupload.class.php');
		$img = new CnimgUpload($cyid);
		PwUpload::upload($img);

		$banner = new BannerUpload($cyid);
		PwUpload::upload($banner);

		pwFtpClose($ftp);

		$pwSQL = array(
			'cname'		=> $cname,
			'ifcheck'	=> $ifcheck,
			'albumopen'	=> $albumopen,
			'annouce'	=> $annouce,
			'ifopen'	=> $ifopen,
			'descrip'	=> $descrip
		);
		if ($cnimg = $img->getImgUrl()) {
			$pwSQL['cnimg'] = substr(strrchr($cnimg,'/'),1);
		}
		if ($cnbanner = $banner->getImgUrl()) {
			$pwSQL['banner'] = substr(strrchr($cnbanner,'/'),1);
		}
		$db->update("UPDATE pw_colonys SET " . pwSqlSingle($pwSQL) . ' WHERE id=' . pwEscape($cyid));

		refreshto("{$basename}cyid=$cyid",'colony_setsuccess');
	}
} elseif ($a == 'del') {

	define('AJAX',1);
	define('F_M',true);
	InitGP(array('tid', 'pid'), null, 2);
	$rt = $db->get_one("SELECT t.authorid,t.author,t.ptable FROM pw_argument a LEFT JOIN pw_threads t ON a.tid=t.tid WHERE a.tid=" . pwEscape($tid) . ' AND a.cyid=' . pwEscape($cyid));

	if (empty($rt) || !$ifadmin) {
		Showmsg('data_error');
	}
	if (empty($_POST['step'])) {

		require_once PrintEot('m_ajax');
		ajax_footer();

	} else {

		if ($pid && is_numeric($pid)) {

			$pw_posts = GetPtable($rt['ptable']);
			$reply = $db->get_one("SELECT pid,fid,tid,aid,author,authorid,postdate,subject,content,anonymous,ifcheck FROM $pw_posts WHERE tid='$tid' AND pid=" . pwEscape($pid));
			if (empty($reply)) {
				Showmsg('data_error');
			}
			$reply['ptable'] = $rt['ptable'];
			!$reply['subject'] && $reply['subject'] = substrs($reply['content'],35);
			$reply['postdate'] = get_date($reply['postdate']);

			require_once(R_P.'require/msg.php');
			require_once(R_P.'require/writelog.php');
			require_once(R_P.'lib/forum.class.php');
			require_once(R_P.'require/credit.php');
			$pwforum = new PwForum($colony['classid']);
			$creditset = $credit->creditset($pwforum->creditset, $db_creditset);
			$d_key = 'Deleterp';
			$credit->addLog("topic_$d_key", $creditset[$d_key], array(
				'uid'		=> $reply['authorid'],
				'username'	=> $reply['author'],
				'ip'		=> $onlineip,
				'fname'		=> strip_tags($pwforum->name),
				'operator'	=> $windid
			));
			$credit->sets($reply['authorid'],$creditset[$d_key],false);
			$msg_delrvrc  = abs($creditset[$d_key]['rvrc']);
			$msg_delmoney = abs($creditset[$d_key]['money']);

			$msg = array(
				'toUser'	=> $reply['author'],
				'subject'	=> 'delrp_title',
				'content'	=> 'delrp_content',
				'other'		=> array(
					'manager'	=> $windid,
					'fid'		=> $pwforum->fid,
					'tid'		=> $tid,
					'subject'	=> substrs($reply['subject'],28),
					'postdate'	=> $reply['postdate'],
					'forum'		=> strip_tags($pwforum->name),
					'affect'	=> "{$db_rvrcname}：-{$msg_delrvrc}，{$db_moneyname}：-{$msg_delmoney}",
					'admindate'	=> get_date($timestamp),
					'reason'	=> ''
				)
			);
			pwSendMsg($msg);

			$log = array(
				'type'      => 'delete',
				'username1' => $reply['author'],
				'username2' => $windid,
				'field1'    => $pwforum->fid,
				'field2'    => '',
				'field3'    => '',
				'descrip'   => 'delrp_descrip',
				'timestamp' => $timestamp,
				'ip'        => $onlineip,
				'tid'		=> $tid,
				'forum'		=> $pwforum->name,
				'subject'	=> substrs($val['subject'],28),
				'affect'	=> "{$db_rvrcname}：-{$msg_delrvrc}，{$db_moneyname}：-{$msg_delmoney}",
				'reason'	=> ''
			);
			writelog($log);

			$credit->runsql();
			$delarticle = L::loadClass('DelArticle');
			$delarticle->delReply(array($reply), true, $db_recycle);
			echo "del\t$pid";

		} else {

			$db->update("DELETE FROM pw_argument WHERE tid=" . pwEscape($tid));
			countPosts('-1');
			require_once(R_P.'require/credit.php');
			$o_groups_creditset = unserialize($o_groups_creditset);
			$creditset = getCreditset($o_groups_creditset['Deletearticle'],false);
			$creditset = array_diff($creditset,array(0));
			if (!empty($creditset)) {
				require_once(R_P.'require/postfunc.php');
				$credit->sets($rt['authorid'],$creditset,true);
				updateMemberid($rt['authorid'],false);
			}
			if ($creditlog = unserialize($o_groups_creditlog)) {
				addLog($creditlog['Deletearticle'], $rt['author'], $rt['authorid'], 'groups_Deletearticle');
			}
			echo 'jump';
		}
		ajax_footer();
	}
} elseif ($a == 'edit') {

	define('AJAX',1);
	define('F_M', true);
	InitGP(array('tid','pid'), null, 2);

	require_once(R_P.'lib/forum.class.php');
	require_once(R_P.'lib/post.class.php');

	$pwforum = new PwForum($colony['classid']);
	$pwpost  = new PwPost($pwforum);
	$pwpost->forumcheck();
	$pwpost->postcheck();

	require_once(R_P . 'lib/postmodify.class.php');
	if ($pid && is_numeric($pid)) {
		$postmodify = new replyModify($tid, $pid, $pwpost);
	} else {
		$postmodify = new topicModify($tid, 0, $pwpost);
	}
	$atcdb = $postmodify->init();

	if (empty($atcdb) || ($postmodify->type == 'topic' && !($atcdb['tpcstatus'] & 1)) || ($atcdb['authorid'] != $winduid && !$ifadmin)) {
		Showmsg('data_error');
	}
	if (empty($_POST['step'])) {

		$atc_content = str_replace(array('<','>','&nbsp;'),array('&lt;','&gt;',' '),$atcdb['content']);
		if (strpos($atc_content,$db_bbsurl) !== false) {
			$atc_content = str_replace('p_w_picpath',$db_picpath,$atc_content);
			$atc_content = str_replace('p_w_upload',$db_attachname,$atc_content);
		}
		$atc_title = $atcdb['subject'];

		require_once PrintEot('m_ajax');
		ajax_footer();

	} else {

		InitGP(array('atc_title','atc_content'), 'P', 0);
		require_once(R_P.'require/bbscode.php');

		if ($postmodify->type == 'topic') {
			$postdata = new topicPostData($pwpost);
		} else {
			$postdata = new replyPostData($pwpost);
		}
		$postdata->initData($postmodify);
		$postdata->setTitle($atc_title);
		$postdata->setContent($atc_content);
		$postdata->setConvert(1);
		$postmodify->execute($postdata);

		extract(L::style());

		$aids = array();
		if ($atcdb['attachs']) {
			$aids = attachment($atc_content);
		}
		$leaveword = $atcdb['leaveword'] ? leaveword($atcdb['leaveword']) : '';
		$content   = convert($atc_content.$leaveword, $db_windpost);

		if (strpos($content,'[p:') !== false || strpos($content,'[s:') !== false) {
			$content = showface($content);
		}
		if ($atcdb['ifsign'] < 2) {
			$content = str_replace("\n",'<br />',$content);
		}
		if ($postdata->data['ifwordsfb'] == 0) {
			$wordsfb = L::loadClass('FilterUtil');
			$content = addslashes($wordsfb->convert(stripslashes($content)));
		}
		$creditnames = pwCreditNames();

		if ($aids) {
			if ($winduid == $atcdb['authorid'] || $pwpost->isGM || pwRights($pwpost->isBM, 'delattach')) {
				$dfadmin = 1;
			} else {
				$dfadmin = 0;
			}
			foreach ($atcdb['attachs'] as $at) {
				if (!in_array($at['aid'], $aids)) {
					continue;
				}
				$atype = '';
				$rat = array();
				if ($at['type'] == 'img' && $at['needrvrc'] == 0 && (!$downloadimg || !$downloadmoney || $_G['allowdownload'] == 2)) {
					$a_url = geturl($at['attachurl'],'show');
					if (is_array($a_url)) {
						$atype = 'pic';
						$dfurl = '<br>'.cvpic($a_url[0], 1, $db_windpost['picwidth'], $db_windpost['picheight'], $at['ifthumb']);
						$rat = array('aid' => $at['aid'], 'img' => $dfurl, 'dfadmin' => $dfadmin, 'desc' => $at['descrip']);
					} elseif ($a_url == 'imgurl') {
						$atype = 'picurl';
						$rat = array('aid' => $at['aid'], 'name' => $at['name'], 'dfadmin' => $dfadmin, 'verify' => md5("showimg{$tid}{$read[pid]}{$fid}{$at[aid]}{$db_hash}"));
					}
				} else {
					$atype = 'downattach';
					if ($at['needrvrc'] > 0) {
						!$at['ctype'] && $at['ctype'] = $at['special'] == 2 ? 'money' : 'rvrc';
						$at['special'] == 2 && $db_sellset['price'] > 0 && $at['needrvrc'] = min($at['needrvrc'], $db_sellset['price']);
					}
					$rat = array('aid' => $at['aid'], 'name' => $at['name'], 'size' => $at['size'], 'hits' => $at['hits'], 'needrvrc' => $at['needrvrc'], 'special' => $at['special'], 'cname' => $creditnames[$at['ctype']], 'type' => $at['type'], 'dfadmin' => $dfadmin, 'desc' => $at['descrip'], 'ext' => strtolower(substr(strrchr($at['name'],'.'),1)));
				}
				if ($atype) {
					$content = attcontent($content, $atype, $rat);
				}
			}
		}
		$alterinfo && $content .= "<div id=\"alert_$pid\" style=\"color:gray;margin-top:30px\">[ $alterinfo ]</div>";
		$atcdb['icon'] = $atcdb['icon'] ? "<img src=\"$imgpath/post/emotion/$atcdb[icon].gif\" align=\"left\" border=\"0\" />" : '';
		echo "success\t".stripslashes($atc_title)."\t".str_replace(array("\r","\t"), array("",""), stripslashes($content));
		ajax_footer();
	}
} elseif ($a == 'join') {

	define('AJAX',1);
	define('F_M',true);
	$groupid == 'guest' && Showmsg('not_login');
	$colony['ifcyer'] && Showmsg('colony_alreadyjoin');

	

	if (!$colony['ifcheck']) {
		Showmsg('colony_joinrefuse');
	}
	if ($o_memberfull > 0 && $colony['members'] >= $o_memberfull) {
		Showmsg('colony_memberlimit');
	}
	include_once (D_P.'data/bbscache/o_config.php');
	require_once(R_P.'require/credit.php');
	$o_groups_creditset = unserialize($o_groups_creditset);
	$o_groups_creditset['Joingroup'] = @array_diff($o_groups_creditset['Joingroup'],array(0));
	if (!empty($o_groups_creditset['Joingroup'])) {
		foreach ($o_groups_creditset['Joingroup'] as $key => $value) {
			if ($value > 0) {
				$moneyname = $credit->cType[$key];
				if ($value > $credit->get($winduid,$key)) {
                                        $GLOBALS['o_joinmoney'] = $value;
					Showmsg('colony_joinfail');
				}
			}
		}
	}
	if ($o_allowjoin > 0 && $o_allowjoin <= $db->get_value("SELECT COUNT(*) as sum FROM pw_cmembers WHERE uid=" . pwEscape($winduid))) {
		Showmsg('colony_joinlimit');
	}

	if (empty($_POST['step'])) {

		require_once PrintEot('m_ajax');
		ajax_footer();

	} else {

		if ($o_joinmoney > 0) {
			$credit->addLog('hack_cyjoin',array($o_moneytype => -$o_joinmoney),array(
				'uid'		=> $winduid,
				'username'	=> $windid,
				'ip'		=> $onlineip,
				'cnname'	=> $colony['cname']
			));
			$credit->set($winduid, $o_moneytype, -$o_joinmoney);
		}

		//积分变动
		if (!empty($o_groups_creditset['Joingroup'])) {
			require_once(R_P.'require/postfunc.php');
			$creditset = getCreditset($o_groups_creditset['Joingroup'],false);
			$credit->sets($winduid,$creditset,true);
			updateMemberid($winduid);
		}

		if ($creditlog = unserialize($o_groups_creditlog)) {
			addLog($creditlog['Joingroup'],$windid,$winduid,'groups_Joingroup');
		}

		$db->update("INSERT INTO pw_cmembers SET " . pwSqlSingle(array(
			'uid'		=> $winduid,
			'username'	=> $windid,
			'ifadmin'	=> $colony['ifcheck'] == 2 ? '0' : '-1',
			'colonyid'	=> $cyid,
			'addtime'	=> $timestamp
		)));

		pwAddFeed($winduid, 'colony', $cyid, array(
			'lang'		=> 'colony_pass',
			'colonyid'	=> $cyid,
			'cname'		=> $colony['cname'],
			'link'		=> "{$db_bbsurl}/{#APPS_BASEURL#}q=group&cyid=$cyid"
		));
		$db->update("UPDATE pw_colonys SET members=members+1 WHERE id=" . pwEscape($cyid));
		if ($colony['ifcheck'] == 2) {
			updateUserAppNum($winduid,'group');
			Showmsg('colony_joinsuccess');
		} else {
			Showmsg('colony_joinsuccess_check');
		}
	}
} elseif ($a == 'out') {

	define('AJAX',1);
	define('F_M',true);
	!$colony['ifcyer'] && Showmsg('undefined_action');
	if (empty($_POST['step'])) {

		require_once PrintEot('m_ajax');
		ajax_footer();

	} else {

		if ($windid == $colony['admin']) {

			if($groupid != 3 && !$o_remove) Showmsg('colony_out_right');

			if ($db->get_value("SELECT COUNT(*) as sum FROM pw_cnalbum WHERE atype=1 AND ownerid=" . pwEscape($cyid)) > 0) {
				Showmsg('colony_del_photo');
			}
			if ($colony['cnimg']) {
				pwDelatt("cn_img/$colony[cnimg]",$db_ifftp);
				pwFtpClose($ftp);
			}
			$query = $db->query("SELECT uid FROM pw_cmembers WHERE colonyid=".pwEscape($cyid)." AND ifadmin != '-1'");
			while ($rt = $db->fetch_array($query)) {
				$cMembers[] = $rt['uid'];
			}
			updateUserAppNum($cMembers,'group','minus');
			$db->update("DELETE FROM pw_cmembers WHERE colonyid=" . pwEscape($cyid));
			$db->update("DELETE FROM pw_colonys WHERE id=" . pwEscape($cyid));
			$db->update("UPDATE pw_cnclass SET cnsum=cnsum-1 WHERE fid=" . pwEscape($colony['classid']) . " AND cnsum>0");
			$db->update("DELETE FROM pw_argument WHERE cyid=" . pwEscape($cyid));
			Showmsg('colony_cancelsuccess');

		} else {

			$db->update("UPDATE pw_colonys SET members=members-1 WHERE id=" . pwEscape($cyid));
			$db->update("DELETE FROM pw_cmembers WHERE colonyid=" . pwEscape($cyid) . " AND uid=" . pwEscape($winduid));
			updateUserAppNum($winduid,'group','recount');
			Showmsg('colony_outsuccess');
		}
	}
} elseif ($a == 'fanoutmsg') {

	define('AJAX',1);
	!$ifadmin && Showmsg('undefined_action');

	if ($_G['postpertime'] || $_G['maxsendmsg']) {
		$rp = $db->get_one("SELECT COUNT(*) AS tdmsg,MAX(mdate) AS lastwrite FROM pw_msg WHERE fromuid=".pwEscape($winduid)." AND mdate>".pwEscape($tdtime));
		if ($_G['postpertime'] && $timestamp - $rp['lastwrite'] <= $_G['postpertime']) {
			Showmsg('msg_limit');
		} elseif ($_G['maxsendmsg'] && $rp['tdmsg']>=$_G['maxsendmsg']) {
			Showmsg('msg_num_limit');
		}
	}

	if (empty($_POST['step'])) {

		InitGP(array('selid', 'group'), null, 2);

		$uids = $usernames = array();

		if ($selid) {
			$query = $db->query("SELECT uid,username FROM pw_members WHERE uid IN (" . pwImplode($selid) . ')');
			while ($rt = $db->fetch_array($query)) {
				$uids[] = $rt['uid'];
				$usernames[] = $rt['username'];
			}
		} else {
			$sql = ' WHERE colonyid=' . pwEscape($cyid) . ' AND uid<>' . pwEscape($winduid);
			switch ($group) {
				case '1': $sql .= " AND ifadmin='1'";break;
				case '2': $sql .= " AND ifadmin='0'";break;
				case '3': $sql .= " AND ifadmin='-1'";break;
				default :$group = 0;
			}
			$total = $db->get_value("SELECT COUNT(*) AS sum FROM pw_cmembers $sql");
			$query = $db->query("SELECT uid,username FROM pw_cmembers $sql LIMIT 3");
			while ($rt = $db->fetch_array($query)) {
				$usernames[] = $rt['username'];
			}
		}
		if (!$usernames) {
			Showmsg('selid_error');
		}
		$uids = implode(',', $uids);
		$usernames = implode(', ', $usernames);

		require_once PrintEot('m_ajax');
		ajax_footer();

	} else {

		InitGP(array('group'), null, 2);
		InitGP(array('uids', 'subject', 'content'));

		if (!$content || !$subject) {
			Showmsg('msg_empty');
		} elseif (strlen($subject)>75 || strlen($content)>1500) {
			Showmsg('msg_subject_limit');
		}
		require_once(R_P . 'require/bbscode.php');
		$wordsfb = L::loadClass('FilterUtil');
		if (($banword = $wordsfb->comprise($subject)) !== false) {
			Showmsg('title_wordsfb');
		}
		if (($banword = $wordsfb->comprise($content, false)) !== false) {
			Showmsg('content_wordsfb');
		}
		if ($uids) {
			$selid = explode(',', $uids);
			$query = $db->query("SELECT uid FROM pw_members WHERE uid IN (" . pwImplode($selid) . ')');
		} else {
			$sql = ' WHERE colonyid=' . pwEscape($cyid) . ' AND uid<>' . pwEscape($winduid);
			switch ($group) {
				case '1': $sql .= " AND ifadmin='1'";break;
				case '2': $sql .= " AND ifadmin='0'";break;
				case '3': $sql .= " AND ifadmin='-1'";break;
			}
			$query = $db->query("SELECT uid FROM pw_cmembers $sql LIMIT 500");
		}
		$touid = array();
		while ($rt = $db->fetch_array($query)) {
			$touid[] = $rt['uid'];
		}
		if ($touid) {
			require_once(R_P.'require/msg.php');
			$msg = array(
				'toUid'		=> $touid,
				'fromUid'	=> $winduid,
				'fromUser'	=> $windid,
				'subject'	=> $subject,
				'content'	=> stripslashes($content)
			);
			pwSendMsg($msg);
		}

		Showmsg('send_success');
	}
}
?>