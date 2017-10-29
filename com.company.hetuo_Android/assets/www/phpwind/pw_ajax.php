<?php
define('AJAX','1');
require_once('global.php');

InitGP(array('action'));

if (!$windid && !in_array($action,array('tag','login','showface','showsmile','getverify','showcard','asearch','poplogin','pingpage','jobpop'))) {
	Showmsg('not_login');
}

if ($action == 'leaveword') {

	!$_G['leaveword'] && Showmsg('leaveword_right');

	if (empty($_POST['step'])) {

		InitGP(array('pid'));
		$tpc = $db->get_one('SELECT authorid,ptable FROM pw_threads WHERE tid='.pwEscape($tid));
		if ($tpc['authorid'] != $winduid) {
			Showmsg('leaveword_error');
		}
		$pw_posts = GetPtable($tpc['ptable']);
		$rt = $db->get_one("SELECT leaveword FROM $pw_posts WHERE pid=".pwEscape($pid).' AND tid='.pwEscape($tid));
		$reason_sel = '';
		$reason_a = explode("\n",$db_adminreason);
		foreach ($reason_a as $k => $v) {
			if ($v = trim($v)) {
				$reason_sel .= "<option value=\"$v\">$v</option>";
			} else {
				$reason_sel .= "<option value=\"\">-------</option>";
			}
		}
		$rt['leaveword'] = str_replace('&nbsp;',' ',$rt['leaveword']);
		require_once PrintEot('ajax');ajax_footer();

	} else {

		PostCheck();
		InitGP(array('pid','atc_content','ifmsg'),'P');
		$tpc = $db->get_one("SELECT t.authorid,t.ptable,f.forumadmin,f.fupadmin FROM pw_threads t LEFT JOIN pw_forums f USING(fid) WHERE t.tid=".pwEscape($tid));
		if ($tpc['authorid'] != $winduid && !CkInArray($windid,$manager) && !admincheck($tpc['forumadmin'],$tpc['fupadmin'],$windid)) {
			Showmsg('leaveword_error');
		}
		require_once(R_P.'require/bbscode.php');
		$atc_content = str_replace('&#61;','=',$atc_content);
		$ptable   = $tpc['ptable'];
		$content  = convert($atc_content,$db_windpost);
		$sqladd   = $atc_content == $content ? '' : ",ifconvert='2'";
		$pw_posts = GetPtable($ptable);
		if ($ifmsg && !empty($atc_content)) {
			require_once(R_P.'require/msg.php');
			include_once(D_P.'data/bbscache/forum_cache.php');
			$atc = $db->get_one("SELECT author,fid,subject,content,postdate FROM $pw_posts WHERE pid=".pwEscape($pid).' AND tid='.pwEscape($tid));
			!$atc['subject'] && $atc['subject'] = substrs($atc['content'],35);
			$msg = array(
				'toUser'	=> $atc['author'],
				'subject'	=> 'leaveword_title',
				'content'	=> 'leaveword_content',
				'other'		=> array(
					'fid'		=> $atc['fid'],
					'tid'		=> $tid,
					'author'	=> $windid,
					'subject'	=> $atc['subject'],
					'postdate'	=> get_date($atc['postdate']),
					'forum'		=> strip_tags($forum[$atc['fid']]['name']),
					'affect'    => '',
					'admindate'	=> get_date($timestamp),
					'reason'	=> stripslashes($atc_content)
				)
			);
			pwSendMsg($msg);
		}
		$wordsfb = L::loadClass('FilterUtil');
		if (($banword = $wordsfb->comprise($atc_content)) !== false) {
			Showmsg('content_wordsfb');
		}
		$db->update("UPDATE $pw_posts SET leaveword=".pwEscape($atc_content)." $sqladd WHERE pid=".pwEscape($pid).' AND tid='.pwEscape($tid));
		echo "success\t".str_replace(array("\n","\t"),array('<br />',''),stripslashes($content));
		ajax_footer();
	}
} elseif ($action == 'favor') {

	PostCheck();
	$rs = $db->get_one('SELECT tids,type FROM pw_favors WHERE uid='.pwEscape($winduid));
	if ($rs) {
		$count = 0;
		$tiddb = getfavor($rs['tids']);
		foreach ($tiddb as $key => $t) {
			if (is_array($t)) {
				if (CkInArray($tid,$t)) {
					Showmsg('job_favor_error');
				}
				$count += count($t);
			} else {
				unset($tiddb[$key]);
			}
		}
		$count > $_G['maxfavor'] && Showmsg('job_favor_full');

		InitGP(array('type'),GP,2);
		$typeid = array('0'=>'default');
		if ($rs['type']) {
			$typeid = array_merge($typeid,explode(',',$rs['type']));
			if (!isset($type)) {
				require_once PrintEot('ajax');ajax_footer();
			}
		} else {
			$type = 0;
		}
		!isset($typeid[$type]) && Showmsg('data_error');
		$read = $db->get_one('SELECT subject FROM pw_threads WHERE tid='.pwEscape($tid));
		!$read && Showmsg('data_error');
		require_once(R_P.'require/posthost.php');
		PostHost("http://push.phpwind.net/push.php?type=collect&url=".rawurlencode("$db_bbsurl/read.php?tid=$tid")."&tocharset=$db_charset&title=".rawurlencode($read['subject'])."&bbsname=".rawurlencode($db_bbsname),"");
		$tiddb[$type][] = $tid;
		$newtids = makefavor($tiddb);
		$db->update("UPDATE pw_favors SET tids=".pwEscape($newtids).' WHERE uid='.pwEscape($winddb['uid']));
	} else {
		$db->update("INSERT INTO pw_favors SET ".pwSqlSingle(array('uid'=>$winddb['uid'],'tids'=>$tid)));
	}

	$db->update("UPDATE pw_threads SET favors=favors+1 WHERE tid=".pwEscape($tid));

	//Start Here pwcache
	require_once(R_P.'lib/elementupdate.class.php');
	$elementupdate = new ElementUpdate();
	$elementupdate->newfavorUpdate($tid,$fid);
	if ($db_ifpwcache&1024) {
		$elementupdate->hotfavorUpdate($tid,$fid);
	}
	updateDatanalyse($tid,'threadFav',1);
	//End Here
	Showmsg('job_favor_success');

} elseif ($action == 'tag') {

	$cachetime = pwFilemtime(D_P."data/bbscache/tagdb.php");
	if (!file_exists(D_P."data/bbscache/tagdb.php") || $timestamp-$cachetime>3600) {
		$tagnum=max($db_tagindex,200);
		$tagdb = array();
		$query = $db->query("SELECT * FROM pw_tags WHERE ifhot='0' ORDER BY num DESC".pwLimit($tagnum));
		while ($rs = $db->fetch_array($query)) {
			$tagdb[$rs['tagname']] = $rs['num'];
		}
		writeover(D_P."data/bbscache/tagdb.php","<?php\r\n\$tagdb=".pw_var_export($tagdb).";\r\n?>");
	} else {
		include_once(D_P."data/bbscache/tagdb.php");
	}
	foreach ($tagdb as $key => $num) {
		echo $key.','.$num."\t";
	}
	ajax_footer();

} elseif ($action == 'relatetag') {

	InitGP(array('tagname'));
	$rs = $db->get_one("SELECT tagid,num FROM pw_tags WHERE tagname=".pwEscape($tagname));
	if (!$rs || $rs['num']<1) {
		Showmsg('tag_limit');
	}
	$query = $db->query("SELECT tg.tid,t.subject FROM pw_tagdata tg LEFT JOIN pw_threads t USING(tid) WHERE tg.tagid=".pwEscape($rs['tagid'])." LIMIT 5");
	$readdb = array();
	while ($rt = $db->fetch_array($query)) {
		$rt['subject'] = substrs($rt['subject'],65);
		$readdb[] = $rt;
	}
	require_once PrintEot('ajax');ajax_footer();

} elseif ($action == 'deldownfile') {

	PostCheck();
	InitGP(array('aid','page'));
	empty($aid) && Showmsg('job_attach_error');

	$attach = $db->get_one("SELECT * FROM pw_attachs WHERE aid=".pwEscape($aid));
	!$attach && Showmsg('job_attach_error');
	if (empty($attach['attachurl']) || strpos($attach['attachurl'],'..') !== false) {
		Showmsg('job_attach_error');
	}
	$fid = $attach['fid']; $aid = $attach['aid']; $tid = $attach['tid']; $pid = $attach['pid'];
	if (!($foruminfo = L::forum($fid))) Showmsg('data_error');
	require_once(R_P.'require/forum.php');
	require_once(R_P.'require/updateforum.php');
	wind_forumcheck($foruminfo);

	//获取管理权限
	$isGM = CkInArray($windid,$manager);
	$isBM = admincheck($foruminfo['forumadmin'],$foruminfo['fupadmin'],$windid);
	if ($isGM || pwRights($isBM,'delattach')) {
		$admincheck = 1;
	} else {
		$admincheck = 0;
	}
	if ($groupid != 'guest' && ($admincheck || $attach['uid'] == $winduid)) {
		pwDelatt($attach['attachurl'],$db_ifftp);
		pwFtpClose($ftp);

		$db->update("DELETE FROM pw_attachs WHERE aid=".pwEscape($aid));
		$ifupload = getattachtype($tid);
		$ifaid = $ifupload === false ? 0 : 1;
		if ($pid) {
			$pw_posts = GetPtable('N',$tid);
			$db->update("UPDATE $pw_posts SET aid=".pwEscape($ifaid,false)."WHERE tid=".pwEscape($tid,false)."AND pid=".pwEscape($pid,false));
		} else {
			$pw_tmsgs = GetTtable($tid);
			$db->update("UPDATE $pw_tmsgs SET aid=".pwEscape($ifaid,false)." WHERE tid=".pwEscape($tid,false));
		}
		$ifupload = (int)$ifupload;
		$db->update('UPDATE pw_threads SET ifupload='.pwEscape($ifupload).' WHERE tid='.pwEscape($tid));

		if ($foruminfo['allowhtm'] && $page==1) {
			$StaticPage = L::loadClass('StaticPage');
			$StaticPage->update($tid);
		}
		echo 'success';ajax_footer();
	} else {
		Showmsg('job_attach_right');
	}
} elseif ($action == 'draft') {

	!$_G['maxgraft'] && Showmsg('draft_right');

	if (empty($_POST['step'])) {

		$db_showperpage = 5;
		InitGP(array('page'),'GP',2);
		$page<1 && $page = 1;
		$rt = $db->get_one("SELECT COUNT(*) AS sum FROM pw_draft WHERE uid=".pwEscape($winduid));
		$maxpage = ceil($rt['sum']/$db_showperpage);
		$maxpage && $page > $maxpage && $page = $maxpage;
		$limit = pwLimit(($page-1)*$db_showperpage,$db_showperpage);

		$query = $db->query("SELECT * FROM pw_draft WHERE uid=".pwEscape($winduid).$limit);
		if ($db->num_rows($query) == 0) {
			Showmsg('draft_error');
		}
		$drdb = array();
		while ($rt = $db->fetch_array($query)) {
			$drdb[] = $rt;
		}
		require_once PrintEot('ajax');ajax_footer();

	} elseif ($_POST['step'] == 2) {

		PostCheck();
		InitGP(array('atc_content'),'P');
		!$atc_content && Showmsg('content_empty');
		$atc_content = str_replace('%26','&',$atc_content);
		$rt = $db->get_one("SELECT COUNT(*) AS sum FROM pw_draft WHERE uid=".pwEscape($winduid));
		if ($rt['sum']>=$_G['maxgraft']) {
			Showmsg('draft_full');
		}
		$db->update("INSERT INTO pw_draft SET ".pwSqlSingle(array('uid'=>$winduid,'content'=>$atc_content)));
		Showmsg('save_success');

	} elseif ($_POST['step'] == 3) {

		PostCheck();
		InitGP(array('atc_content','did'),'P');
		!$atc_content && Showmsg('content_empty');
		$db->update('UPDATE pw_draft SET content='.pwEscape($atc_content).' WHERE uid='.pwEscape($winduid).' AND did='.pwEscape($did));
		Showmsg('update_success');

	} else {

		PostCheck();
		InitGP(array('did'));
		$db->update('DELETE FROM pw_draft WHERE uid='.pwEscape($winduid).' AND did='.pwEscape($did));
		Showmsg('delete_success');
	}

} elseif ($action == 'msg') {

	$_G['allowmessege'] == 0 && Showmsg('msg_group_right');
	if ($_G['postpertime'] || $_G['maxsendmsg']) {
		$rp = $db->get_one("SELECT COUNT(*) AS tdmsg,MAX(mdate) AS lastwrite FROM pw_msg WHERE fromuid=".pwEscape($winduid)." AND mdate>".pwEscape($tdtime));
		if ($_G['postpertime'] && $timestamp - $rp['lastwrite'] <= $_G['postpertime']) {
			Showmsg('msg_limit');
		} elseif ($_G['maxsendmsg'] && $rp['tdmsg']>=$_G['maxsendmsg']) {
			Showmsg('msg_num_limit');
		}
	}
	list(,,,$msgq)	= explode("\t",$db_qcheck);

	if (empty($_POST['step'])) {

		InitGP(array('touid','remid','transmit','type'));
		if (is_numeric($remid)) {
			$reinfo = $db->get_one('SELECT m.fromuid,m.touid,m.username,m.type,mc.title,mc.content FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.mid='.pwEscape($remid)." AND m.type='rebox' AND m.touid=".pwEscape($winduid));
			if ($reinfo) {
				$subject = strpos($reinfo['title'],'Re:')===false ? 'Re:'.$reinfo['title'] : $reinfo['title'];
				$atc_content = "[quote]".trim(substrs(preg_replace("/\[quote\](.+?)\[\/quote\]/is", '', $reinfo['content']),100))."[/quote]\n\n";
			}
		} elseif (is_numeric($transmit)) {
			$reinfo = $db->get_one("SELECT m.touid,m.fromuid,mc.title,mc.content FROM pw_msg m LEFT JOIN pw_msgc mc ON m.mid=mc.mid WHERE m.mid=" . pwEscape($transmit));
			if ($winduid == $reinfo['touid'] || $winduid == $reinfo['fromuid']) {
				$subject = $reinfo['title'];
				$atc_content = $reinfo['content'];
			}
		} elseif (is_numeric($touid)) {
			$reinfo = $db->get_one("SELECT uid,username FROM pw_members WHERE uid=".pwEscape($touid));
			if ($type == 'birth') {
				$subject = getLangInfo('writemsg','birth_title');
				$atc_content = getLangInfo('writemsg','birth_content');
			}
		}
		require_once PrintEot('ajax');ajax_footer();

	} else {

		PostCheck(1,$db_gdcheck & 8);
		InitGP(array('msg_title','pwuser','ifsave'),'P');
		InitGP(array('atc_content'),'P',0);
		$atc_content = trim(Char_cv($atc_content));
		if (!$atc_content || !$msg_title || !$pwuser) {
			Showmsg('msg_empty');
		} elseif (strlen($msg_title)>75 || strlen($atc_content)>1500) {
			Showmsg('msg_subject_limit');
		}
		require_once(R_P.'require/bbscode.php');
		$wordsfb = L::loadClass('FilterUtil');
		if (($banword = $wordsfb->comprise($msg_title)) !== false) {
			Showmsg('title_wordsfb');
		}
		if (($banword = $wordsfb->comprise($atc_content, false)) !== false) {
			Showmsg('content_wordsfb');
		}
		$msgq && Qcheck($_POST['qanswer'],$_POST['qkey']);

		$rt = $db->get_one("SELECT uid,banpm,msggroups FROM pw_members WHERE username=".pwEscape($pwuser));
		if (!$rt) {
			$errorname = $pwuser;
			Showmsg('user_not_exists');
		}
		if ($rt['msggroups'] && strpos($rt['msggroups'],",$groupid,") !== false || strpos(",$rt[banpm],",",$windid,") !== false) {
			$errorname = $pwuser;
			Showmsg('msg_refuse');
		}
		require_once(R_P.'require/msg.php');
		$msg = array(
			'toUser'	=> $pwuser,
			'fromUid'	=> $winduid,
			'fromUser'	=> $windid,
			'subject'	=> $msg_title,
			'content'	=> stripslashes($atc_content)
		);
		if ($ifsave) {
			$db->update('INSERT INTO pw_msg SET '.pwSqlSingle(array(
				'touid'		=> $rt['uid'],
				'fromuid'	=> $winduid,
				'username'	=> $pwuser,
				'type'		=> 'sebox',
				'ifnew'		=> 0,
				'mdate'		=> $timestamp
			),false));
			$mid = $db->insert_id();
			$db->update('INSERT INTO pw_msgc SET '.pwSqlSingle(array(
				'mid'		=> $mid,
				'title'		=> $msg_title,
				'content'	=> stripslashes($atc_content)
			),false));
		}
		pwSendMsg($msg);
		Showmsg('send_success');
	}
} elseif ($action == 'usetool') {

	$tooldb = array();
	$i = $j = 0;
	$query  = $db->query("SELECT t.id,t.name,t.filename,t.descrip,u.nums FROM pw_tools t LEFT JOIN pw_usertool u ON t.id=u.toolid  AND u.uid=".pwEscape($winduid)." WHERE t.state='1' AND t.type='1' ORDER BY vieworder");
	while ($rt = $db->fetch_array($query)) {
		$rt['nums']=(int)$rt['nums'];
		$tooldb[$i][$j] = $rt;
		$j++;
		if ($j>1) {
			$i++;$j=0;
		}
	}
	require_once PrintEot('ajax');ajax_footer();

} elseif ($action == 'usertool') {
	//道具帖子类
	InitGP(array('uid'),'GP',2);
	!$uid && Showmsg('undefined_action');
	$i = $j = 0;
	$query  = $db->query("SELECT t.id,t.name,t.filename,t.descrip,u.nums FROM pw_tools t LEFT JOIN pw_usertool u ON t.id=u.toolid  AND u.uid=".pwEscape($winduid)." WHERE state='1' AND type='2' ORDER BY vieworder");
	while ($rt = $db->fetch_array($query)) {
		$rt['nums']=(int)$rt['nums'];
		$tooldb[$i][$j] = $rt;
		$j++;
		if ($j>1) {
			$i++;$j=0;
		}
	}
	require_once PrintEot('ajax');ajax_footer();

} elseif ($action == 'dig') {

	PostCheck();
	!$_G['dig'] && Showmsg("dig_right");
	$read = $db->get_one("SELECT t.author,t.subject,t.dig,f.forumset FROM pw_threads t LEFT JOIN pw_forumsextra f USING(fid) WHERE tid=".pwEscape($tid));
	!$read && Showmsg('data_error');
	$forumset = unserialize($read['forumset']);
	!$forumset['dig'] && Showmsg('forum_dig_allow');
	$rt  = $db->get_one("SELECT uid,digtid FROM pw_memberinfo WHERE uid=".pwEscape($winduid));
	Add_S($rt);
	if (strpos(",$rt[digtid],",",$tid,") === false) {
		$read['dig']++;
		$db->update("UPDATE pw_threads SET dig=dig+1 WHERE tid=".pwEscape($tid));
		if ($rt) {
			strlen($rt['digtid'])>2000 && $rt['digtid'] = '';
			$rt['digtid'] .= ($rt['digtid'] ? ',' : '').$tid;
			$db->update("UPDATE pw_memberinfo SET digtid=".pwEscape($rt['digtid'])."WHERE uid=".pwEscape($winduid));
		} else {
			$db->update("INSERT INTO pw_memberinfo SET ".pwSqlSingle(array('uid'=>$winduid,'digtid'=>$tid)));
		}
		//reflush cache
		$threads = L::loadClass('Threads');
		$threads->delThreads($tid);
		
		require_once(R_P.'require/posthost.php');
		PostHost("http://push.phpwind.net/push.php?type=dig&url=".rawurlencode("$db_bbsurl/read.php?tid=$tid")."&tocharset=$db_charset&title=".rawurlencode($read['subject'])."&bbsname=".rawurlencode($db_bbsname),"");
		Showmsg('dig_success');
	} else {
		Showmsg("dig_limit");
	}
} elseif ($action == 'extend') {

	InitGP(array('type'));
	if ($type == 'pwcode') {
		$code  = array();
		$query = $db->query("SELECT * FROM pw_windcode");
		while ($rt = $db->fetch_array($query)) {
			$rt['descrip'] = str_replace("\n","|",$rt['descrip']);
			$code[] = $rt;
		}
	} else {
		@include_once(D_P.'data/bbscache/setform.php');
		InitGP(array('id'),'GP',2);
		$setform = array();
		if (isset($setformdb[$id])) {
			$setform = $setformdb[$id];
		}
	}
	require_once PrintEot('ajax');ajax_footer();

} elseif ($action == 'sharelink') {

	!$db_ifselfshare && Showmsg("sharelink_colse");

	if (empty($_POST['step'])) {

		require_once PrintEot('ajax');ajax_footer();

	} else {

		PostCheck();
		InitGP(array('linkname','linkurl','linkdescrip','linklogo'),'P');
		(!$linkname || !$linkurl) && Showmsg('sharelink_link_empty');
		!$linkdescrip && $linkdescrip = '';
		!$linklogo && $linklogo = '';
		$linkurl = strtolower($linkurl);
		strncmp($linkurl,'http://',7) != 0 && Showmsg('sharelink_link_error');
		$rs = $db->get_one("SELECT sid FROM pw_sharelinks WHERE username=".pwEscape($windid));
		$rs && Showmsg('sharelink_apply_limit');
		$pwSQL = pwSqlSingle(array(
			'name'		=> $linkname,
			'url'		=> $linkurl,
			'descrip'	=> $linkdescrip,
			'logo'		=> $linklogo,
			'ifcheck'	=> 0,
			'username'	=> $windid
		));
		$db->update("INSERT INTO pw_sharelinks SET $pwSQL");

		require_once(R_P.'require/msg.php');
		$message = array(
			'toUser'	=> $manager,
			'subject'	=> 'sharelink_apply_title',
			'content'	=> 'sharelink_apply_content',
			'other'		=> array(
				'username'		=> $windid,
				'time'			=> get_date($timestamp)
			)
		);
		pwSendMsg($message);

		Showmsg("sharelink_success");
	}
} elseif ($action == 'showface') {

	InitGP(array('page'));
	(!is_numeric($page) || $page < 1) && $page=1;
	$pre_page	= $page-1;
	$next_page	= $page+1;
	$db_perpage = 10;
	$img = @opendir("$imgdir/face");
	$num = $pagenum = 0;
	while ($imgname = @readdir($img)) {
		if ($imgname != "." && $imgname != ".." && $imgname != "" && eregi("\.(gif|jpg|png|bmp)$",$imgname)) {
			$num++;
			if ($num > ($page-1)*$db_perpage && $num <= $page*$db_perpage) {
				$pagenum++;
				$imgname_array[] = $imgname;
			}
		}
	}
	if ($pagenum < $db_perpage) $next_page = $page;
	@closedir($img);
	require_once PrintEot('ajax');ajax_footer();

} elseif ($action == 'newrp') {

	if ($db_replysitemail && getstatus($winddb['userstatus'],6)) {
		include_once(D_P.'data/bbscache/forum_cache.php');
		$rt = $db->get_one("SELECT replyinfo FROM pw_memberinfo WHERE uid=".pwEscape($winduid));
		$replyinfo = explode(',',substr($rt['replyinfo'],1,-1));
		$replyinfo = pwImplode($replyinfo);
		$replydb = array();
		$query = $db->query("SELECT tid,fid,subject,postdate,lastpost FROM pw_threads WHERE tid IN($replyinfo) LIMIT 20");
		if ($db->num_rows($query) == 0) {
			Showmsg('newrp_error');
		}
		while ($rt = $db->fetch_array($query)) {
			$rt['subject'] = substrs($rt['subject'],55);
			$rt['fname'] = $forum[$rt['fid']]['name'];
			$rt['lastpost'] = get_date($rt['lastpost'],'Y-m-d');
			$replydb[] = $rt;
		}
		require_once PrintEot('ajax');ajax_footer();

	} else {
		Showmsg('newrp_error');
	}
} elseif ($action == 'delnewrp') {

	PostCheck();
	!$tid && Showmsg('data_error');
	$rt = $db->get_one("SELECT replyinfo FROM pw_memberinfo WHERE uid=".pwEscape($winduid));
	$rt['replyinfo'] = addslashes(str_replace(",$tid,",',',$rt['replyinfo']));
	$rt['replyinfo'] == ',' && $rt['replyinfo'] = '';
	$db->update("UPDATE pw_memberinfo SET replyinfo='$rt[replyinfo]' WHERE uid=".pwEscape($winduid));
	$db->update("UPDATE pw_threads SET ifmail='0' WHERE tid=".pwEscape($tid));
	if (getstatus($winddb['userstatus'],6) && !$rt['replyinfo']) {
		$db->update("UPDATE pw_members SET userstatus=userstatus&~(1<<5) WHERE uid=".pwEscape($winduid));
	}
	Showmsg('operate_success');

} elseif ($action == 'addfriend') {

	PostCheck();
	InitGP(array('touid'),'GP',2);
	if ($touid == $winduid) {
		Showmsg('friend_self_add_error');
	}
	$friend = $db->get_one("SELECT uid,username,userstatus,icon FROM pw_members WHERE uid=".pwEscape($touid));
	if (!$friend) {
		$errorname = '';
		Showmsg('user_not_exists');
	}
	$rs = $db->get_one("SELECT uid,status FROM pw_friends WHERE uid=".pwEscape($winduid)." AND friendid=".pwEscape($friend['uid']));
	if ($rs) {
		if ($rs['status'] == '1') {
			Showmsg('friend_status_check');
		} else {
			Showmsg('friend_already_exists');
		}
	}
	$friendcheck = getstatus($friend['userstatus'],3,3);
	if ($friendcheck == 2) {
		Showmsg('friend_not_add');
	}
	if (empty($_POST['step'])) {
		if ($friendcheck == 0 || $friendcheck == 1) {
			require_once(R_P.'require/showimg.php');
			list($faceurl) = showfacedesign($friend['icon'],'1','s');
			$query = $db->query("SELECT ftid,name FROM pw_friendtype WHERE uid=".pwEscape($winduid)." ORDER BY ftid");
			$types = array();
			while ($rt = $db->fetch_array($query)) {
				$types[$rt['ftid']] = $rt['name'];
			}
			require_once PrintEot('ajax');ajax_footer();
		}
	} else {
		InitGP(array('friendtype'));

		if ($friendtype > 0) {
			$checkftid = $db->get_value("SELECT ftid FROM pw_friendtype WHERE uid=".pwEscape($winduid)." AND ftid=".pwEscape($friendtype));
			if (empty($checkftid)) Showmsg('friend_type_not_exists');
		}
		if (!$friendcheck) {

			//$pwSQL1 = pwSqlSingle(array(
			//	'uid'		=> $winduid,
			//	'friendid'	=> $friend['uid'],
			//	'joindate'	=> $timestamp,
			//	'status'	=> 0,
			//	'ftid'		=> $friendtype
			//));
			//$db->update("INSERT INTO pw_friends	SET $pwSQL1");
			//$db->update("UPDATE pw_memberdata SET f_num=f_num+1 WHERE uid=".pwEscape($winduid));

			addSingleFriend(true,$winduid,$friend['uid'],$timestamp,0,$friendtype);

			//$pwSQL2 = pwSqlSingle(array(
			//	'uid'		=> $friend['uid'],
			//	'friendid'	=> $winduid,
			//	'joindate'	=> $timestamp,
			//	'status'	=> 0
			//));
			//$db->update("INSERT INTO pw_friends	SET $pwSQL2");
			//$db->update("UPDATE pw_memberdata SET f_num=f_num+1 WHERE uid=".pwEscape($friend['uid']));

			addSingleFriend(true,$friend['uid'],$winduid,$timestamp,0);

			$msg = array(
				'toUser'	=> $friend['username'],
				'subject'	=> 'friend_add_title_1',
				'content'	=> 'friend_add_content_1',
				'other'		=> array(
					'uid'		=> $winduid,
					'username'	=> $windid
				)
			);
			require_once R_P.'require/msg.php';
			pwSendMsg($msg);

			//job sign
			initJob($winduid,"doAddFriend",array('user'=>$friend['username']));
			
			/* add feed */
			//pwAddFeed($friend['uid'],'friend','',array('uid' => $winduid, 'friend' => $windid));
			pwAddFeed($winduid,'friend','',array('uid' => $friend['uid'], 'friend' => $friend['username']));
			

			Showmsg('friend_update_success');

		} elseif ($friendcheck == 1) {
			InitGP(array('checkmsg'),'P');
			if (strlen($checkmsg) > 255) {
				$_strmaxlen = 255;
				Showmsg('string_limit');
			}
			//$pwSQL = pwSqlSingle(array(
			//	'uid'		=> $winduid,
			//	'friendid'	=> $friend['uid'],
			//	'status'	=> 1,
			//	'joindate'	=> $timestamp,
			//	'descrip'	=> $checkmsg,
			//	'ftid'		=> $friendtype
			//));
			//$db->update("INSERT INTO pw_friends	SET $pwSQL");
			addSingleFriend(false,$winduid,$friend['uid'],$timestamp,1,$friendtype,$checkmsg);
			$msg = array(
				'toUser'	=> $friend['username'],
				'subject'	=> 'friend_add_title_2',
				'content'	=> 'friend_add_content_2',
				'other'		=> array(
					'uid'		=> $winduid,
					'username'	=> $windid,
					'msg'		=> stripslashes($checkmsg)
				)
			);
			
			/* add feed */
			pwAddFeed($winduid,'friend','',array('uid' => $friend['uid'], 'friend' => $friend['username']));
			
			require_once R_P.'require/msg.php';
			pwSendMsg($msg);

			//job sign
			initJob($winduid,"doAddFriend",array('user'=>$friend['username']));

			Showmsg('friend_add_check');
		} else {
			Showmsg('undefined_action');
		}
	}

} elseif ($action == 'deletefriend') {
	PostCheck();
	InitGP(array('fuid'),'GP',2);
	$ckuser = $db->get_value("SELECT m.username FROM pw_friends f LEFT JOIN pw_members m ON f.uid=m.uid WHERE f.uid=".pwEscape($fuid)." AND f.friendid=".pwEscape($winduid));
	if ($ckuser) {
		$db->update('DELETE FROM pw_friends WHERE uid='.pwEscape($fuid)." AND friendid=".pwEscape($winduid));
		$db->update("UPDATE pw_memberdata SET f_num=f_num-1 WHERE uid=".pwEscape($fuid)." WHERE f_num>0");
		$ckuser2 = $db->get_value("SELECT friendid FROM pw_friends WHERE uid=".pwEscape($winduid)." AND friendid=".pwEscape($fuid));
		if ($ckfuid2) {
			$db->update('DELETE FROM pw_friends WHERE uid='.pwEscape($winduid)." AND friendid=".pwEscape($fuid));
			$db->update("UPDATE pw_memberdata SET f_num=f_num-1 WHERE uid=".pwEscape($winduid)." WHERE f_num>0");
		}
		$msg = array(
			'toUser'	=> $ckuser,
			'subject'	=> 'friend_delete_title',
			'content'	=> 'friend_delete_content',
			'other'		=> array(
				'uid'		=> $winduid,
				'username'	=> $windid
			)
		);
		require_once R_P.'require/msg.php';
		pwSendMsg($msg);
		Showmsg('friend_delete');
	} else {
		Showmsg('undefined_action');
	}

} elseif ($action == 'showsmile') {

	InitGP(array('subjectid','page','type'));
	$u         = "http://dm.phpwind.net/misc";
	$subjectid = (int)$subjectid;
	(!is_numeric($page) || $page<1) && $page = 1;
	$s         = '300.xml';
	if ($type == 'general') {
		$s = $subjectid ? $subjectid.'_'.$page.'.xml' : '300.xml';
	} elseif ($type == 'magic') {
		$s = $subjectid ? $subjectid.'_'.$page.'.xml' : '200.xml';
	}
	$cachefile = D_P."data/bbscache/myshow_{$s}";
	if (!file_exists($cachefile) || $timestamp - pwFilemtime($cachefile) > 43200) {
		$data = '';
		if ($subjectid) {
			$url = "$u/list/$s?$timestamp";
		} else {
			$url = "$u/menu/$s?$timestamp";
		}
		require_once(R_P.'require/posthost.php');
		$data = PostHost($url);
		if ($data && strpos($data,'<?xml')!==false) {
			writeover($cachefile,$data);
		}
	}
	header("Content-Type: text/xml; charset=UTF-8");
	$data = readover($cachefile);
	echo $data;
	exit;

} elseif ($action == 'honor') {

	!$_G['allowhonor'] && Showmsg('undefined_action');

	if (empty($_POST['step'])) {

		require_once PrintEot('ajax');ajax_footer();

	} else {

		PostCheck();
		InitGP(array('content'),'P');
		$content = str_replace("\n",'',$content);
		strlen($content) > 90 && $content = substrs($content,90);
		if ($winddb['honor'] != stripslashes($content)) {
			$db->update("UPDATE pw_members SET honor=".pwEscape($content)." WHERE uid=".pwEscape($winduid));
			if ($db_modes['o']['ifopen']) {
				$db->update("INSERT INTO pw_owritedata SET"
					. pwSQLSingle(array(
						'uid'		=> $winduid,
						'touid'		=> 0,
						'postdate'	=> $timestamp,
						'isshare'	=> 0,
						'source'	=> 'signature',
						'content'	=> $content
				)));
			}
			require_once(R_P.'require/postfunc.php');
			pwAddFeed($winduid,'honor','',array('honor' => $content));
		}
		echo "success\t".stripslashes($content);ajax_footer();
	}
} elseif ($action == 'readlog') {

	$readlog = explode(',',GetCookie('readlog'));
	@krsort($readlog);
	$tids = array();
	$i = 0;
	foreach ($readlog as $key=>$value) {
		if (is_numeric($value)) {
			$tids[] = $value;
			if (++$i>9) break;
		}
	}
	Cookie('readlog',','.implode(',',$tids).',');
	$tids && $tids = pwImplode($tids);
	!$tids && Showmsg('readlog_data_error');
	include_once(D_P.'data/bbscache/forum_cache.php');
	$readb = array();
	$query = $db->query("SELECT t.tid,t.fid,t.subject,t.author,t.authorid,t.anonymous,f.f_type,f.password,f.allowvisit FROM pw_threads t LEFT JOIN pw_forums f USING(fid) WHERE t.tid IN($tids)");
	while ($rt = $db->fetch_array($query)) {
		if (empty($rt['password']) && $rt['f_type']<>'hidden' && (empty($rt['allowvisit']) || allowcheck($rt['allowvisit'],$groupid,$winddb['groups']))) {
			if ($rt['anonymous'] && !in_array($groupid,array('3','4')) && $rt['authorid']<>$winduid) {
				$rt['author']	= $db_anonymousname;
				$rt['authorid'] = 0;
			}
			$readb[] = $rt;
		}
	}
	require_once PrintEot('ajax');ajax_footer();

} elseif ($action == 'threadlog') {

	$threadlog = explode(',',GetCookie('threadlog'));
	@krsort($threadlog);
	$fids = ',';
	$i = 0;
	foreach ($threadlog as $key=>$value) {
		if (is_numeric($value)) {
			$fids .= $value.',';
			if (++$i>9) break;
		}
	}
	Cookie('threadlog',$fids);
	include_once(D_P.'data/bbscache/forum_cache.php');
	$threaddb = array();
	foreach ($forum as $key => $value) {
		if (in_array($key,$threadlog)) {
			$threaddb[$key] = $value['name'];
		}
	}
	require_once PrintEot('ajax');ajax_footer();

} elseif ($action == 'mgmode') {

	if ($groupid <> 3 && $groupid <> 4) {
		echo 'false';ajax_footer();
	}
	$rightset = $db->get_value("SELECT value FROM pw_adminset WHERE gid=".pwEscape($groupid));
	require_once(R_P.'require/pw_func.php');
//	$rightset = P_unserialize($rightset);
	if (!$rightset || !(is_array($rightset = unserialize($rightset)))) {
		$rightset = array();
	}

	if ($rightset['setforum'] || $rightset['setstyles']) {
		require_once PrintEot('ajax');
	} else {
		echo 'false';
	}
	ajax_footer();

} elseif ($action == 'setstyle') {

	if ($groupid <> 3 && $groupid <> 4) {
		Showmsg('undefined_action');
	}
	$rightset = $db->get_value("SELECT value FROM pw_adminset WHERE gid=".pwEscape($groupid));
	require_once(R_P.'require/pw_func.php');
//	$rightset = P_unserialize($rightset);
	if (!$rightset || !(is_array($rightset = unserialize($rightset)))) {
		$rightset = array();
	}
	!$rightset['setstyles'] && Showmsg('undefined_action');

	if (empty($_POST['step'])) {

		extract(L::style());
		require_once PrintEot('ajax');
		ajax_footer();

	} else {

		PostCheck();
		InitGP(array('set','setskin'));
		if (!is_array($set)) {
			Showmsg('undefined_action');
		}
		foreach ($set as $key => $value) {
			if (in_array($key,array('tablewidth','mtablewidth'))) {
				if (!preg_match('/(%|px|em)/i',$value)) {
					$set[$key] .= 'px';
				}
			}
		}
		$pwSQL = pwSqlSingle(array(
			'bgcolor'		=> $set['bgcolor'],		'linkcolor'		=>$set['linkcolor'],
			'tablecolor'	=> $set['tablecolor'],	'tdcolor'		=> $set['tdcolor'],
			'tablewidth'	=> $set['tablewidth'],	'mtablewidth'	=> $set['mtablewidth'],
			'headcolor'		=> $set['headcolor'],	'headborder'	=> $set['headborder'],
			'headfontone'	=> $set['headfontone'],	'headfonttwo'	=> $set['headfonttwo'],
			'cbgcolor'		=> $set['cbgcolor'],	'cbgborder'		=> $set['cbgborder'],
			'cbgfont'		=> $set['cbgfont'],		'forumcolorone'	=> $set['forumcolorone'],
			'forumcolortwo'	=> $set['forumcolortwo']
		));
		$rs = $db->get_one("SELECT sid FROM pw_styles WHERE name=".pwEscape($setskin)." AND uid='0'");
		if ($rs) {
			$db->update("UPDATE pw_styles SET $pwSQL WHERE name=".pwEscape($setskin)." AND uid='0'");
		} else {
			$db->update("INSERT INTO pw_styles SET $pwSQL");
		}
		require_once(R_P.'admin/cache.php');
		updatecache_sy($setskin);

		Showmsg('operate_success');
	}

} elseif ($action == 'mforder') {

	PostCheck();
	if ($groupid <> 3 && $groupid <> 4) {
		Showmsg('undefined_action');
	}
	$rightset = $db->get_value("SELECT value FROM pw_adminset WHERE gid=".pwEscape($groupid));
	require_once(R_P.'require/pw_func.php');
//	$rightset = P_unserialize($rightset);
	if (!$rightset || !(is_array($rightset = unserialize($rightset)))) {
		$rightset = array();
	}

	if (!$rightset['setforum']) {
		Showmsg('undefined_action');
	}
	$vieworder = 0;
	foreach ($_POST['cate'] as $cid => $fids) {
		$fid_a = explode(',',$fids);
		foreach ($fid_a as $key => $fid) {
			$db->update("UPDATE pw_forums SET vieworder=".pwEscape($key)." WHERE fid=".pwEscape($fid));
		}
		$db->update("UPDATE pw_forums SET vieworder=".pwEscape($vieworder)." WHERE fid=".pwEscape($cid));
		++$vieworder;
	}
	require_once(R_P.'admin/cache.php');
	updatecache_f();

	Showmsg('operate_success');

} elseif ($action == 'mfsetname') {

	PostCheck();
	if ($groupid <> 3 && $groupid <> 4) {
		Showmsg('undefined_action');
	}
	$rightset = $db->get_value("SELECT value FROM pw_adminset WHERE gid=".pwEscape($groupid));
	require_once(R_P.'require/pw_func.php');
//	$rightset = P_unserialize($rightset);
	if (!$rightset || !(is_array($rightset = unserialize($rightset)))) {
		$rightset = array();
	}

	if (!$rightset['setforum']) {
		Showmsg('undefined_action');
	}
	InitGP(array('fids','desc'),'P',0);
	$ifcache = false;

	if (is_array($desc)) {
		foreach ($desc as $fid => $descrip) {
			$descrip = str_replace(array('&#60;','&#61;','<iframe'),array('<','=','&lt;iframe'),$descrip);
			strlen($descrip) > 250 && Showmsg('descrip_long');
			$db->update("UPDATE pw_forums SET descrip=".pwEscape($descrip)."WHERE fid=".pwEscape($fid));
		}
	}
	if (is_array($fids)) {
		foreach ($fids as $fid => $name) {
			$name = str_replace(array('&#60;','&#61;','<iframe'),array('<','=','&lt;iframe'),$name);
			$db->update("UPDATE pw_forums SET name=".pwEscape($name)."WHERE fid=".pwEscape($fid));
		}
		$ifcache = true;
	}
	if ($ifcache) {
		require_once(R_P.'admin/cache.php');
		updatecache_f();
	}
	Showmsg('operate_success');

} elseif ($action == 'shortcut') {

	!$fid && Showmsg('undefined_action');
	$myshortcut = explode(',',$winddb['shortcut']);
	foreach ($myshortcut as $key=>$value){
		if (!$value || !is_numeric($value)) {
			unset($myshortcut[$key]);
		}
	}
	$myshortcut = array_unique($myshortcut);
	if (in_array($fid,$myshortcut)) {
		foreach ($myshortcut as $key=>$value) {
			if (!$value || $value == $fid) {
				unset($myshortcut[$key]);
			}
		}
		$shortcut = ','.implode(',',$myshortcut).',';
		$shortcut .= $shortcut."\t".$winddb['appshortcut'];
		$db->update("UPDATE pw_members SET shortcut=".pwEscape($shortcut)." WHERE uid=".pwEscape($winduid));
		Showmsg("shortcutno");
	} else {
		count($myshortcut)>=6 && Showmsg('shortcut_numlimit');
		require_once(D_P.'data/bbscache/forum_cache.php');
		$forumkeys = array_keys($forum);
		!in_array($fid,$forumkeys) && Showmsg('undefined_action');
		$myshortcut[] = $fid;

		$shortcut = ','.implode(',',$myshortcut).',';
		$shortcut .= $shortcut."\t".$winddb['appshortcut'];
		$db->update("UPDATE pw_members SET shortcut=".pwEscape($shortcut)." WHERE uid=".pwEscape($winduid));
		Showmsg("shortcutok");
	}
} elseif ($action == 'pushto') {

	InitGP(array('fid','seltid'));
	@include_once Pcv(D_P.'data/bbscache/mode_push_config.php');
	$pushs = array();
	if ($groupid=='3' || $groupid=='4' || CkInArray($windid,$manager)) {
		$pushs = $PUSH;
	} elseif ($groupid=='5') {
		foreach ($PUSH as $key=>$value) {
			if (in_array($value['scr'],array('thread','cate'))) {
				$pushs[] = $value;
			}
		}
	}
	if (!$pushs) {
		Showmsg('no_aim_to_push');
	}
	require_once PrintEot('ajax');
	ajax_footer();

} elseif ($action == 'debate') {
	PostCheck();
	$do = GetGP('do');
	if ($do == 'vote') {
		$debate = $db->get_one("SELECT endtime,obvote,revote,judge FROM pw_debates WHERE tid=".pwEscape($tid));
		empty($debate) && Showmsg('data_error');
		if ($debate['judge'] > 0 || $debate['endtime'] < $timestamp) {
			Showmsg('debate_over');
		}
		$standpoint = $db->get_value("SELECT standpoint FROM pw_debatedata WHERE pid='0' AND tid=".pwEscape($tid)."AND authorid=".pwEscape($winduid));
		$standpoint = (int)$standpoint;
		if ($standpoint == 1) {
			Showmsg('debate_voted_Y');
		} elseif ($standpoint == 2) {
			Showmsg('debate_voted_N');
		}
		if (GetGP('q') == 'y') {
			$db->update("UPDATE pw_debates SET obvote=obvote+1 WHERE tid=".pwEscape($tid));
			$debate['obvote']++;
			$standpoint = 1;
		} else {
			$db->update("UPDATE pw_debates SET revote=revote+1 WHERE tid=".pwEscape($tid));
			$debate['revote']++;
			$standpoint = 2;
		}
		$db->update("INSERT INTO pw_debatedata SET".pwSqlSingle(array(
			'pid'			=> 0,
			'tid'			=> $tid,
			'authorid'		=> $winduid,
			'standpoint'	=> $standpoint,
			'postdate'		=> $timestamp,
			'vote'			=> 0,
			'voteids'		=> ''
		)));

		$tmpVotes = $debate['revote']+$debate['obvote'];
		$tmpob = round($debate['obvote']/$tmpVotes,2)*100;
		$tmpre = round($debate['revote']/$tmpVotes,2)*100;

		Showmsg('debate_success');

	} elseif ($do == 'judge') {

		if ($_POST['step']) {
			InitGP(array('judge','debater','umpirepoint'));
			strlen($umpirepoint) > 255 && Showmsg('debate_pointlen');
			$pwSQL['umpirepoint'] = $umpirepoint;
			$debate = $db->get_one("SELECT umpirepoint,debater,judge FROM pw_debates WHERE tid=".pwEscape($tid));
			if (empty($debate['judge'])) {
				$pwSQL['judge'] = ($judge==1 || $judge==3) ? $judge : 2;
			}
			if (empty($debate['debater'])) {
				$rt = $db->get_one("SELECT authorid FROM pw_members m LEFT JOIN pw_debatedata dd ON m.uid=dd.authorid WHERE m.username=".pwEscape($debater)."AND dd.tid=".pwEscape($tid));
				if ($rt) {
					$pwSQL['debater'] = $debater;
				}
			}
			$db->update("UPDATE pw_debates SET".pwSqlSingle($pwSQL)." WHERE tid=".pwEscape($tid));
			Showmsg('debate_judgesuccess');
		} else {
			$debate = $db->get_one("SELECT obvote,revote,obposts,reposts,umpirepoint,debater,judge FROM pw_debates WHERE tid=".pwEscape($tid));
			if (!$debate['debater']) {
				$debater = array();
				$query = $db->query("SELECT dd.authorid,dd.vote,m.username FROM pw_debatedata dd LEFT JOIN pw_members m ON dd.authorid=m.uid WHERE dd.tid=".pwEscape($tid)."ORDER BY dd.vote DESC LIMIT 10");
				while ($rt = $db->fetch_array($query)) {
					$debater[$rt['authorid']]['vote'] += $rt['vote'];
					$debater[$rt['authorid']]['username'] = $rt['username'];
				}
			}
			require_once PrintEot('ajax');ajax_footer();
		}
	} elseif ($do == 'agree') {

		$pid = (int)GetGP('pid');
		$debate = $db->get_one("SELECT endtime,judge FROM pw_debates WHERE tid=".pwEscape($tid));
		empty($debate) && Showmsg('data_error');
		if ($debate['judge'] > 0 || $debate['endtime'] < $timestamp) {
			Showmsg('debate_over');
		}
		$debate = $db->get_one("SELECT authorid,vote,voteids FROM pw_debatedata WHERE pid=".pwEscape($pid)."AND tid=".pwEscape($tid));
		empty($debate) && Showmsg('data_error');
		$debate['authorid'] == $winduid && Showmsg('debate_voteself');
		if (strpos($debate['voteids'],$winduid) !== false) {
			Showmsg('debate_voted');
		}
		$debate['voteids'] .= "$winduid,";
		$db->update("UPDATE pw_debatedata SET vote=vote+1,voteids=".pwEscape($debate['voteids'],false)."WHERE pid=".pwEscape($pid)."AND tid=".pwEscape($tid));
		$vote = $debate['vote']+1;
		Showmsg('debate_agree');
	}
} elseif ($action == 'mutiatt') {

	$out = 'var att = {';
	$del = array();
	$query = $db->query("SELECT aid,name,size,attachurl,uploadtime FROM pw_attachs WHERE tid='0' AND pid='0' AND uid=" . pwEscape($winduid) . ' ORDER BY aid');
	while ($rt = $db->fetch_array($query)) {
		$rt['uploadtime'] = get_date($rt['uploadtime']);
		if (file_exists($attachpath.'/mutiupload/'.$rt['attachurl'])) {
			$out .= "'{$rt[aid]}' : ['$rt[name]', '$rt[size]', '$attachpath/mutiupload/$rt[attachurl]', '$rt[uploadtime]'],";
		} else {
			$del[] = $rt['aid'];
		}
	}
	if ($del) {
		$db->update("DELETE FROM pw_attachs WHERE aid IN(" . pwImplode($del) . ')');
	}
	$out = rtrim($out,',')."}";
	echo "ok\t$out";
	ajax_footer();

} elseif ($action == 'delmutiatt') {

	$aids  = array();
	$query = $db->query("SELECT aid,attachurl FROM pw_attachs WHERE tid='0' AND pid='0' AND uid=" . pwEscape($winduid));
	while ($rt = $db->fetch_array($query)) {
		if (file_exists($attachpath.'/mutiupload/'.$rt['attachurl'])) {
			P_unlink($attachpath.'/mutiupload/'.$rt['attachurl']);
		}
		$aids[] = $rt['aid'];
	}
	$db->update("DELETE FROM pw_attachs WHERE aid IN (" . pwImplode($aids).')');
	echo 'ok';ajax_footer();

} elseif ($action == 'delmutiattone') {
	$aid = (int)GetGP('aid');
	if ($aid <= 0) {
		echo "error";
	} else {
		$db->update("DELETE FROM pw_attachs WHERE aid=".pwEscape($aid)." AND tid='0' AND pid='0' AND uid=".pwEscape($winduid)." LIMIT 1");
		echo "ok";
	}
	ajax_footer();

} elseif ($action == 'msggoto') {

	InitGP(array('from','type'));
	InitGP(array('mid'), 'GP', 2);

	switch ($from) {
		case 'read':
			$sqlwhere = " AND type='rebox' AND touid=" . pwEscape($winduid) . "AND fromuid<>0";break;
		case 'readn':
			$sqlwhere = " AND type='rebox' AND touid=" . pwEscape($winduid) . "AND fromuid=0";break;
		case 'readsnd':
			$sqlwhere = " AND type='sebox' AND fromuid=" . pwEscape($winduid);break;
		case 'readscout':
			$sqlwhere = " AND type='rebox' AND fromuid=" . pwEscape($winduid);break;
		case 'readpub':
			$sqlwhere = " AND type='public' AND togroups LIKE " . pwEscape("%,$winddb[groupid],%");
			$delmsg = $db->get_value('SELECT delmsg FROM pw_memberinfo WHERE uid='.pwEscape($winduid));
			if ($delmsg) {
				$delmsg = explode(',', trim($delmsg,','));
				$sqlwhere .= " AND mid NOT IN(" . pwImplode($delmsg) . ')';
			}
			break;
		default:
			Showmsg('undefined_action');
	}
	if ($type == 'next') {
		$sql = "SELECT MAX(mid) AS mid FROM pw_msg WHERE mid<" . pwEscape($mid);
	} else {
		$sql = "SELECT MIN(mid) AS mid FROM pw_msg WHERE mid>" . pwEscape($mid);
	}
	$rt = $db->get_one($sql.$sqlwhere);
	if ($rt['mid']) {
		echo "ok\t$rt[mid]";
	} else {
		echo 'none';
	}
	ajax_footer();

} elseif ($action == 'playatt') {

	InitGP(array('aid'));

	$rt = $db->get_one("SELECT * FROM pw_attachs WHERE aid=" . pwEscape($aid));
	$a_url = geturl($rt['attachurl']);
	if (empty($a_url) || $rt['needrvrc'] > 0) {
		Showmsg('job_attach_error');
	}
	$width = 314;
	$ext = strtolower(substr(strrchr($rt['attachurl'],'.'),1));
	switch ($ext) {
		case 'mp3':
		case 'wma':	$height = 53;$type = 'wmv';break;
		case 'wmv':	$height = 256;$type = 'wmv';break;
		case 'swf': $height = 256;$type = 'flash';break;
		case 'rm' : $height = 256;$type = 'rm';break;
		default:Showmsg('undefined_action');
	}
	echo "ok\t$a_url[0]\t$width\t$height\t$type";ajax_footer();

} elseif ($action == 'getverify') {

	echo $verifyhash . "\t" . GetVerify($onlineip . $winddb['regdate'] . $fid . $tid);
	ajax_footer();

} elseif ($action == 'changestate') {
	if (GetCookie('hideid') == '1') {
		Cookie('hideid','',0);
		echo "0";ajax_footer();
	} else {
		Cookie('hideid','1');
		echo "1";ajax_footer();
	}
} elseif ($action == 'changeeditor') {
	InitGP(array('editor'));
	if ($editor != getstatus($winddb['userstatus'],11)) {
		$ustatus = 'userstatus=userstatus'.($editor ? '|' : '&~') .'(1<<10)';
		$db->update("UPDATE pw_members SET $ustatus WHERE uid=".pwEscape($winduid));
	}
} elseif ($action == 'pwb_friend') {
	InitGP(array('u'),'P',2);
	if (!$u) Showmsg('undefined_action');
	if ($u != $winduid) Showmsg('undefined_action');
	$friends = getFriends($winduid,0,8,false,1,'s');
	$str = '';
	if ($friends) {
		$friend_online = array();
		foreach ($friends as $key => $value) {
			if ($value['thisvisit']+$db_onlinetime*1.5>$timestamp) {
				$friend_online[] = array('uid'=>$value['uid'],'face'=>$value['face'],'username'=>$value['username']);
			}
		}
		if ($friend_online) {
			$str = pwJsonEncode($friend_online);
		}
	}
	echo "success\t$str";
	ajax_footer();
} elseif ($action == 'pwb_message') {
	InitGP(array('u'),'P',2);
	if (!$u) Showmsg('undefined_action');
	if ($u != $winduid) Showmsg('undefined_action');
	$msgdb = array();
	$query = $db->query("SELECT m.mid,m.fromuid,m.type,m.username as `from`,mc.title FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.type='rebox' AND m.touid=".pwEscape($winduid)." AND ifnew=1 ORDER BY m.mdate DESC ".pwLimit(0,20));
	while ($msginfo = $db->fetch_array($query)) {
		$msginfo['title'] = substrs($msginfo['title'],40);
		$msgdb[] = $msginfo;
	}

	//群发消息
	$pubmsg = getUserPublicMsgRecord($winduid);
	@extract($pubmsg);
	$checkmsg = $readmsg.','.$delmsg;
	$msg_gid = $winddb['groupid'];
	$query2  = $db->query("SELECT m.mid,m.fromuid,m.type,m.username as `from`,mc.title FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.type='public' AND m.togroups LIKE ".pwEscape("%,$msg_gid,%")." AND m.mdate>".pwEscape($winddb['regdate'])." ORDER BY m.mdate DESC LIMIT 20");
	while ($msginfo = $db->fetch_array($query2)) {
		if ($checkmsg && strpos(",$checkmsg,",",$msginfo[mid],") !== false) {
			continue;
		}
		$msginfo['title'] = substrs($msginfo['title'],40);
		$msgdb[] = $msginfo;
	}
	if (empty($msgdb) && $winddb['newpm'] > 0) {
		$db->update("UPDATE pw_members SET newpm='0' WHERE uid=".pwEscape($winduid));
	}
	$str = '';
	if ($msgdb) {
		$str = pwJsonEncode($msgdb);
	}
	echo "success\t$str";
	ajax_footer();
} elseif ($action  == 'readmessage') {

	require_once(R_P.'require/bbscode.php');
	InitGP(array('mid','type'));
	if ($type == 'personal' || $type == 'system') {
		$sqladd = $type == 'personal' ? 'm.fromuid<>0' : 'm.fromuid=0';
		$msginfo = $db->get_one("SELECT m.mid,m.fromuid AS withuid,m.touid,m.username,m.ifnew,m.mdate,mc.title,mc.content FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.mid=".pwEscape($mid)." AND m.type='rebox' AND m.touid=".pwEscape($winduid)."AND ".$sqladd);
		if ($msginfo) {
			$msginfo['content'] = str_replace("\n","<br>",$msginfo['content']);
			$msginfo['content'] = convert($msginfo['content'],$db_windpost);
			if (strpos($msginfo['content'],'[s:') !== false) {
				$msginfo['content'] = showface($msginfo['content']);
			}
			$msginfo['content'] = wordsConvert($msginfo['content']);
			$msginfo['title']   = str_replace('&ensp;$','$', $msginfo['title']);
			$msginfo['content'] = str_replace('&ensp;$','$', $msginfo['content']);
			$msginfo['mdate']   = get_date($msginfo['mdate']);
			if ($msginfo['ifnew'] > 0) {
				$db->update("UPDATE pw_msg SET ifnew=0 WHERE mid=".pwEscape($mid));
				updateNewpm($winduid,'minus');
			}
			$str = '';
			if ($msginfo) {
				$str = pwJsonEncode($msginfo);
			}
			require_once PrintEot('ajax');ajax_footer();
		} else {
			Showmsg('msg_error');
		}
	} elseif ($type == 'public') {
		$msg_gid = $winddb['groupid'];
		$msginfo = $db->get_one("SELECT m.mid,m.fromuid AS withuid,m.touid,m.username,m.ifnew,m.mdate,mc.title,mc.content FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.mid=".pwEscape($mid)." AND m.type='public' AND m.togroups LIKE ".pwEscape("%,$msg_gid,%"));

		if ($msginfo) {
			require_once(R_P.'require/bbscode.php');
			$pubmsg = getUserPublicMsgRecord($winduid);
			@extract($pubmsg);
			$msginfo['content'] = str_replace("\n","<br>",$msginfo['content']);
			$msginfo['content'] = convert($msginfo['content'],$db_windpost);
			if (strpos($msginfo['content'],'[s:') !== false) {
				$msginfo['content'] = showface($msginfo['content']);
			}
			$msginfo['title']   = str_replace('&ensp;$','$', $msginfo['title']);
			$msginfo['content'] = str_replace('&ensp;$','$', $msginfo['content']);
			$msginfo['mdate']   = get_date($msginfo['mdate']);
			$msginfo['content'] = str_replace("\$email",$winddb['email'],$msginfo['content']);
			$msginfo['content'] = str_replace("\$windid",$windid,$msginfo['content']);

			if ($pubmsg) {
				if (strpos(",$readmsg,",",$msginfo[mid],") === false) {
					$readmsg .= $readmsg ? ','.$msginfo['mid'] : $msginfo['mid'];
					if (empty($readmsg)) {
						$readmsg = '';
					} else {
						$temp_msgdb  = explode(',',$readmsg);
						arsort($temp_msgdb);
						$readmsg = implode(',',$temp_msgdb);
					}
					$db->update('UPDATE pw_memberinfo SET readmsg='.pwEscape($readmsg,false).' WHERE uid='.pwEscape($winduid));
				}
			} else {
				$readmsg = $msginfo['mid'];
				$db->update("INSERT INTO pw_memberinfo SET ".pwSqlSingle(array('uid'=>$winduid,'readmsg'=>$readmsg)));
			}
			updateNewpm($winduid,'minus');
			$str = '';
			if ($msginfo) {
				$str = pwJsonEncode($msginfo);
			}
			require_once PrintEot('ajax');ajax_footer();
		} else {
			Showmsg('msg_error');
		}
	} else {
		Showmsg('msg_error');
	}

} elseif ($action == 'unread') {

	PostCheck();
	InitGP(array('mid'));
	$rt = $db->get_one("SELECT mid FROM pw_msg WHERE mid=".pwEscape($mid)." AND type='rebox' AND touid=".pwEscape($winduid)."AND fromuid<>0");
	if ($rt) {
		$db->update('UPDATE pw_msg SET ifnew=1 WHERE mid='.pwEscape($mid));
		updateNewpm($winduid,'add');
		Showmsg('msg_unread_success');
	} else {
		Showmsg('msg_error');
	}

} elseif ($action == 'delmessage') {

	PostCheck();
	InitGP(array('delid','type'));

	!$delid && Showmsg('del_error');

	if ($type == 'personal' || $type == 'system') {
		$newdelid = '';
		$newdelid = $db->get_value("SELECT mid FROM pw_msg WHERE mid=".pwEscape($delid)."  AND type='rebox' AND touid=".pwEscape($winduid));
		if ($newdelid) {
			$db->update("DELETE FROM pw_msg WHERE mid=".pwEscape($newdelid));
			require_once(R_P.'require/msg.php');
			delete_msgc($newdelid);
		}
	} elseif ($type == 'public') {
		$delmsg = $db->get_value('SELECT delmsg FROM pw_memberinfo WHERE uid='.pwEscape($winduid));
		if ($delmsg) {
			$delmsg  = ','.$delmsg.','.$delid.',';
			$delmsg = substr($delmsg,1,-1);
			$temp_delmsg = explode(',',$delmsg);
			arsort($temp_delmsg);
			$delmsg = implode(',',$temp_delmsg);
			$db->update("UPDATE pw_memberinfo SET delmsg=".pwEscape($delmsg)." WHERE uid=".pwEscape($winduid));
		} else {
			$db->pw_update(
				"SELECT * FROM pw_memberinfo WHERE uid=".pwEscape($winduid),
				"UPDATE pw_memberinfo SET delmsg=".pwEscape($delid)." WHERE uid=".pwEscape($winduid),
				"INSERT INTO pw_memberinfo SET ".pwSqlSingle(array('uid'=>$winduid,'delmsg'=>$delid))
			);
		}
	} else {
		Showmsg('del_error');
	}

	$winddb['newpm'] = 0;
	Showmsg('del_success');
} elseif ($action == 'showbottom') {

	extract(L::style());

	$openbarstyle = 'style="display:none"';
	$closebarstyle = '';

	if ($db_appifopen && ($db_appbbs || $db_appo)) {
		$appshortcut = trim($winddb['appshortcut'],',');
		if (!empty($appshortcut) && $db_siteappkey) {
			$appshortcut = explode(',',$appshortcut);
			$bottom_appshortcut = array();
			$appclient = L::loadClass('appclient');
			$bottom_appshortcut = $appclient->userApplist($winduid,$appshortcut,1);
		}
	}
	echo '<!--';require_once PrintEot('bottom');echo '-->';ajax_footer();
} elseif ($action == 'report') {
	!$_G['allowreport'] && Showmsg('report_right');
	InitGP(array('tid','pid'),'GP','2');
	InitGP(array('type'));
	$checkdata = $db->get_one("SELECT * FROM pw_report WHERE type=".pwEscape($type)." AND tid=".pwEscape($tid)." AND pid=".pwEscape($pid));
	$checkdata && Showmsg('have_report');

	if (empty($_POST['step'])) {
		$ch_type = getLangInfo('other',$type);
		require_once PrintEot('ajax');ajax_footer();
	} else {
		InitGP(array('reason'));
		$pwSQL = pwSqlSingle(array(
			'tid'	=> $tid,
			'pid'	=> $pid,
			'uid'	=> $winduid,
			'type'	=> $type,
			'reason'=> $reason
		));
		$db->update("INSERT INTO pw_report SET $pwSQL");
		Showmsg('report_success');
	}
} elseif ($action == 'showfriends') {
	if (empty($_POST['step'])) {
		InitGP(array('recall'));
		$friend = getFriends($winduid);
		if (empty($friend)) Showmsg('no_friend');
		foreach ($friend as $key => $value) {
			$frienddb[$value['ftid']][] = $value;
		}
		$query = $db->query("SELECT * FROM pw_friendtype WHERE uid=".pwEscape($winduid)." ORDER BY ftid");
		$friendtype = array();
		while ($rt = $db->fetch_array($query)) {
			$friendtype[$rt['ftid']] = $rt;
		}
		$no_group_name = getLangInfo('other','no_group_name');
		$friendtype[0] = array('ftid' => 0,'uid' => $winduid,'name' => $no_group_name);
		require_once PrintEot('ajax');ajax_footer();
	} else {
		InitGP(array('selid'));
		if ($selid){
			$query = $db->query("SELECT username FROM pw_members WHERE uid IN (".pwImplode($selid).")");
			while ($rt = $db->fetch_array($query)) {
				$usernamedb[] = $rt['username'];
			}
			$usernamedbs = implode(",",$usernamedb);
			echo $usernamedbs;ajax_footer();
		} else {
			echo '';ajax_footer();
		}
	}
} elseif ($action == 'showcard') {

	InitGP(array('uids'),'GP');
	$uids = explode(',',$uids);
	!$uids && Showmsg('guest_notcard');

	/*app*/
	$db_maxnum = 12;//第三方app一页显示个数

	$db_appsdb = array();
	if ($db_siteappkey && ($db_appbbs || $db_appo)) {
		$app_array = $appsdb = array();
		$appclient = L::loadClass('appclient');
		$app_array = $appclient->userApplist($winduid,$appids,2);
		$appsdb = $appclient->getApplist();
	}
	foreach ($appsdb as $value) {
		foreach ($uids as $val) {
			if ($app_array[$val][$value['appid']]) {
				$value['ifshow'] = 1;
			} else {
				$value['ifshow'] = 0;
			}
			$db_appsdb[$val][$value['appid']]['ifshow'] = $value['ifshow'];
			$db_appsdb[$val][$value['appid']]['appid'] = $value['appid'];
			$db_appsdb[$val][$value['appid']]['name'] = $value['name'];
		}
	}
	/*app*/

	/*个人数据*/
	require_once(R_P.'require/showimg.php');
	@include_once(D_P.'data/bbscache/level.php');
	$userdb = array();
	$app_with_count = array('topic','diary','photo','owrite','group','share');
	$query = $db->query("SELECT m.uid,m.username,m.groupid,m.memberid,m.icon,m.oicq,m.aliww,m.honor,md.lastpost,md.thisvisit,md.f_num,ud.visits,ud.tovisits,ud.diary_lastpost,ud.photo_lastpost,ud.owrite_lastpost,ud.group_lastpost,ud.share_lastpost FROM pw_members m LEFT JOIN pw_memberdata md ON m.uid=md.uid LEFT JOIN pw_ouserdata ud ON m.uid=ud.uid WHERE m.uid IN(" . pwImplode($uids).")");
	while ($rt = $db->fetch_array($query)) {
		list($rt['icon']) = showfacedesign($rt['icon'],1,'s');
		$rt['ismyfriend'] = 0;
		$rt['systitle'] = $rt['groupid'] == '-1' ? '' : $ltitle[$rt['groupid']];
		$rt['memtitle'] = $ltitle[$rt['memberid']];
		foreach ($app_with_count as $key => $value) {
			$posttime = '';
			$rt['appcount'][$value] = getPostnumByType($value,$rt);
		}

		$userdb[$rt['uid']] = $rt;
	}

	$query = $db->query("SELECT friendid FROM pw_friends WHERE uid=" . pwEscape($winduid) . " AND friendid IN(" . pwImplode($uids).") AND status='0'");
	while ($rt = $db->fetch_array($query)) {
		$userdb[$rt['friendid']]['ismyfriend'] = 1;
	}
	/*个人数据*/

	/*帖子、相册、日志、记录*/
	$userinfo = array();
	$pids = '';
	$usercache = L::loadDB('Usercache');
	$userinfo = $usercache->getByUid($uids);

	foreach ($userinfo as $uid => $value) {
		if ($value['photos']) {
			$pids .= $pids ? ','.$value['photos']['value'] : $value['photos']['value'];
			$userinfo[$uid]['photos']['value'] = array();
		}
	}

	/*最新照片*/
	if ($pids) {
		$pids = explode(',',$pids);
		$query = $db->query("SELECT cp.pid,cp.path,ca.aname,ca.lasttime,ca.aid,ca.ownerid FROM pw_cnphoto cp LEFT JOIN pw_cnalbum ca USING(aid) WHERE cp.pid IN(".pwImplode($pids).")");
		while ($rt = $db->fetch_array($query)) {
			$rt['path'] = getphotourl($rt['path'],$rt['ifthumb']);
			$userinfo[$rt['ownerid']]['photos']['value']['aname'] = $rt['aname'];
			$userinfo[$rt['ownerid']]['photos']['value']['aid'] = $rt['aid'];
			$userinfo[$rt['ownerid']]['photos']['value']['postdate'] = $rt['lasttime'];
			$userinfo[$rt['ownerid']]['photos']['value']['photo'][] = $rt;
		}
	}
	/*帖子、相册、日志、记录*/

	/*数据合并*/
	foreach ($uids as $info) {
		$usercachedb[$info] = $userdb[$info];
		$usercachedb[$info]['app'] = $db_appsdb[$info];

		foreach ($userinfo[$info] as $key => $value) {
			$a_url = '';
			if ($key == 'topic') {
				foreach ($value['value']['attimages'] as $k => $val) {
					$a_url = geturl($val['attachurl'],'show',$val['ifthumb']);
					if ($a_url != 'nopic') {
						$userinfo[$info][$key]['attimages'][$k] = is_array($a_url) ? $a_url[0] : $a_url;
					}
				}
			} elseif ($key == 'diary') {
				foreach ($value['value']['attimages'] as $k => $val) {
					$a_url = geturl('diary/'.$val['attachurl'],'show',$val['ifthumb']);
					if ($a_url != 'nopic') {
						$userinfo[$info][$key]['attimages'][$k]  = is_array($a_url) ? $a_url[0] : $a_url;
					}
				}
			}

			$userinfo[$info][$value['value']['postdate']] = $userinfo[$info][$key];
			$userinfo[$info][$value['value']['postdate']]['type'] = $key;
			krsort($userinfo[$info]);
		}
		$usercachedb[$info]['appinfo'] = $userinfo[$info];
	}
	/*数据合并*/
	require_once PrintEot('ajax');ajax_footer();

} elseif ($action == 'addfriendtype') {

	InitGP(array('typename'),'P');
	if (!$winduid) Showmsg('undefined_action');
	if (strlen($typename)<1 || strlen($typename)>20) Showmsg('mode_o_addftype_name_leng');
	$check = $db->get_one("SELECT ftid FROM pw_friendtype WHERE uid=".pwEscape($winduid)." AND name=".pwEscape($typename));
	if ($check) Showmsg('mode_o_addftype_name_exist');
	$count = $db->get_value("SELECT COUNT(*) AS count FROM pw_friendtype WHERE uid=".pwEscape($winduid));
	if ($count>20) Showmsg('mode_o_addftype_length');
	$db->update("INSERT INTO pw_friendtype(uid,name) VALUES(".pwEscape($winduid).",".pwEscape($typename).")");
	$id = $db->insert_id();
	if ($id) {
		echo "success\t$id\t$typename";
		ajax_footer();
	} else {
		Showmsg('undefined_action');
	}

} elseif ($action == 'asearch') {

	InitGP(array('fid','modelid','pcid'));

	if ($modelid) {
		require_once(R_P.'lib/posttopic.class.php');
		$postTopic = new postTopic($pwpost);
		$query = $db->query("SELECT fieldid,name,type,rules,vieworder,textsize FROM pw_topicfield WHERE modelid = ".pwEscape($modelid)." AND ifable='1' AND ifasearch='1' ORDER BY vieworder ASC,fieldid ASC");
		while ($rt = $db->fetch_array($query)) {
			list($rt['name1'],$rt['name2']) = explode('{#}',$rt['name']);
			$rt['searchhtml'] = $postTopic->getASearchHtml($rt['type'],$rt['fieldid'],$rt['textsize'],$rt['rules']);
			$asearchdb[$rt['fieldid']] = $rt;
		}
	} elseif ($pcid) {

		require_once(R_P.'lib/postcate.class.php');
		$postTopic = new postCate($pwpost);
		$query = $db->query("SELECT fieldid,name,type,rules,vieworder,textsize FROM pw_pcfield WHERE pcid = ".pwEscape($pcid)." AND ifable='1' AND ifasearch='1' ORDER BY vieworder ASC,fieldid ASC");
		while ($rt = $db->fetch_array($query)) {
			list($rt['name1'],$rt['name2']) = explode('{#}',$rt['name']);
			$rt['searchhtml'] = $postTopic->getASearchHtml($rt['type'],$rt['fieldid'],$rt['textsize'],$rt['rules']);
			$asearchdb[$rt['fieldid']] = $rt;
		}
	}

	if(empty($asearchdb)) {
		showmsg('topic_search_forum');
	}

	require_once PrintEot('ajax');ajax_footer();
} elseif ($action == 'pcjoin') {
	InitGP(array('tid','thelast','authorid','pcid'),GP,2);

	if ($thelast != 1) {
		//$sign = $db->get_value("SELECT sign FROM pw_postcate WHERE pcid=".pwEscape($pcid));

		$pcvaluetable = GetPcatetable($pcid);
		$fieldvalue = $db->get_one("SELECT objecter,limitnum,payway,deposit,price FROM $pcvaluetable WHERE tid=".pwEscape($tid));
		$membernum = $db->get_value("SELECT SUM(nums) FROM pw_pcmember WHERE tid=".pwEscape($tid));
		$payway = $fieldvalue['payway'];

		if (empty($_POST['step'])) {
			$authorid == $winduid && Showmsg('pcjoin_ownnotjoin');

			$isU = ($fieldvalue['objecter'] == 2 && isFriend($authorid,$winduid) || $fieldvalue['objecter'] == 1) ? 1 : 0;
			$fieldvalue['limitnum'] && $morenum = $fieldvalue['limitnum'] - $membernum;

			require_once PrintEot('ajax');ajax_footer();
		} elseif ($_POST['step'] == '1') {
			PostCheck();
			InitGP(array('nums','phone','mobile','address','zip','message','extra','name'));

			if (!$mobile || !$name) {
				Showmsg('pcjoin_mobile_error');
			}
			if ($fieldvalue['limitnum'] && $fieldvalue['limitnum'] - $membernum < $nums) {
				if ($pcid == 1) {
					Showmsg('pcjoin_pcid_more');
				} elseif ($pcid == 2) {
					Showmsg('pcjoin_more');
				}
			}
			$nums = (int)$nums;
			if ($nums <= 0) {
				if ($pcid == 1) {
					Showmsg('pcjoin_pcid_nums');
				} elseif ($pcid == 2) {
					Showmsg('pcjoin_nums');
				}
			}

			$deposit = !ceil($fieldvalue['deposit']) ? $fieldvalue['price'] : $fieldvalue['deposit'];
			$totalcash = $deposit*$nums;

			$sqlarray = array(
				'tid'		=> $tid,
				'uid'		=> $winduid,
				'pcid'		=> $pcid,
				'username'	=> $windid,
				'nums'		=> $nums,
				'totalcash'	=> $totalcash,
				'name'		=> $name,
				'zip'		=> $zip,
				'message'	=> $message,
				'phone'		=> $phone,
				'mobile'	=> $mobile,
				'address'	=> $address,
				'extra'		=> $extra,
				'jointime'	=> $timestamp
			);
			$db->update("INSERT INTO pw_pcmember SET " . pwSqlSingle($sqlarray));

			$pcmid = $db->insert_id();
			$nextto = 'pcjoin';

			Showmsg('pcjoin_nextstep');
		}
	} elseif ($thelast == 1) {
		InitGP(array('deposit','nums','totalcash','pcmid','payway'));
		$alipayurl = "trade.php?action=pcalipay&tid=$tid&pcmid=$pcmid&pcid=$pcid";

		require_once PrintEot('ajax');ajax_footer();
	}
} elseif ($action == 'pcmember') {
	InitGP(array('page','tid','jointype','payway','ifend','pcid'));

	$isadminright = $jointype == 3 ? 0 : 1;
	require_once(R_P.'lib/postcate.class.php');
	$postCate = new postCate($data);
	list(,$isviewright) = $postCate->getViewright($pcid,$tid);

	$memberdb = array();
	$count = $sum = $paysum = 0;
	$query = $db->query("SELECT ifpay,nums FROM pw_pcmember WHERE tid=".pwEscape($tid));
	while ($rt = $db->fetch_array($query)) {
		$count ++;
		if ($rt['ifpay']) {
			$paysum += $rt['nums'];
		}
		$sum += $rt['nums'];
	}

	$page < 1 && $page = 1;
	$numofpage = ceil($count/$db_perpage);
	if ($numofpage && $page > $numofpage) {
		$page = $numofpage;
	}
	$start = ($page-1)*$db_perpage;
	$limit = pwLimit($start,$db_perpage);
	$pages = numofpage($count,$page,$numofpage,"read.php?tid=$tid&",null, "pw_ajax.php?action=$action&tid=$tid&jointype=$jointype&payway=$payway&");

	$i = $pcid = 0;
	$query = $db->query("SELECT pcmid,uid,pcid,username,nums,totalcash,phone,mobile,address,extra,ifpay,jointime FROM pw_pcmember WHERE tid=".pwEscape($tid)." ORDER BY (uid=".pwEscape($winduid).") DESC,ifpay ASC,pcmid DESC $limit");
	while ($rt = $db->fetch_array($query)) {
		if ($i == 0) {
			$pcid = $rt['pcid'];
		}
		$i++;
		$memberdb[] = $rt;
	}

	require_once PrintEot('ajax');ajax_footer();
} elseif ($action == 'pcdel') {
	InitGP(array('pcmid','pcid','tid','jointype','payway'));
	$read = $db->get_one("SELECT authorid,subject,fid FROM pw_threads WHERE tid=".pwEscape($tid));
	$foruminfo = $db->get_one('SELECT forumadmin,fupadmin FROM pw_forums WHERE fid='.pwEscape($read['fid']));
	$isGM = CkInArray($windid,$manager);
	$isBM = admincheck($foruminfo['forumadmin'],$foruminfo['fupadmin'],$windid);
	require_once(R_P.'lib/postcate.class.php');
	$post = array();
	$postCate = new postCate($post);
	$isadminright = $postCate->getAdminright($pcid,$read['authorid']);
	if ($isadminright != 1) {
		echo "noright\t";
		ajax_footer();
	}

	$db->UPDATE("DELETE FROM pw_pcmember WHERE pcmid=".pwEscape($pcmid));

	echo "success\t$jointype\t$tid\t$payway";
	ajax_footer();
} elseif ($action == 'pcshow') {
	InitGP(array('pcmid','pcid','tid'));
	if (!$pcmid) Showmsg('undefined_action');

	$read = $db->get_one("SELECT authorid,subject,fid FROM pw_threads WHERE tid=".pwEscape($tid));
	$foruminfo = $db->get_one('SELECT forumadmin,fupadmin FROM pw_forums WHERE fid='.pwEscape($read['fid']));
	$isGM = CkInArray($windid,$manager);
	$isBM = admincheck($foruminfo['forumadmin'],$foruminfo['fupadmin'],$windid);
	require_once(R_P.'lib/postcate.class.php');
	$post = array();
	$postCate = new postCate($post);
	$isadminright = $postCate->getAdminright($pcid,$read['authorid']);

	$pcmember = $db->get_one("SELECT uid,username,name,zip,message,nums,totalcash,phone,mobile,address,extra,ifpay FROM pw_pcmember WHERE pcmid=".pwEscape($pcmid));

	require_once PrintEot('ajax');ajax_footer();
} elseif ($action == 'pcpay') {
	InitGP(array('pcmid','pcid','tid','jointype','payway'));
	if (!$pcmid) Showmsg('undefined_action');

	$read = $db->get_one("SELECT authorid,subject,fid FROM pw_threads WHERE tid=".pwEscape($tid));
	$foruminfo = $db->get_one('SELECT forumadmin,fupadmin FROM pw_forums WHERE fid='.pwEscape($read['fid']));
	$isGM = CkInArray($windid,$manager);
	$isBM = admincheck($foruminfo['forumadmin'],$foruminfo['fupadmin'],$windid);
	require_once(R_P.'lib/postcate.class.php');
	$post = array();
	$postCate = new postCate($post);
	$isadminright = $postCate->getAdminright($pcid,$read['authorid']);
	if ($isadminright != 1) {
		Showmsg('pcpay_noright');
	}

	if ($_POST['step'] == 2) {
		PostCheck();
		$db->update("UPDATE pw_pcmember SET ifpay=1 WHERE pcmid=".pwEscape($pcmid));
		echo "success\t$jointype\t$tid\t$payway";
		ajax_footer();
	}

	require_once PrintEot('ajax');ajax_footer();
} elseif ($action == 'pcmodify') {
	InitGP(array('pcid','pcmid','tid','jointype','ifpay','payway'),GP,2);

	$pcvaluetable = GetPcatetable($pcid);
	$fieldvalue = $db->get_one("SELECT endtime,limitnum,deposit,price FROM $pcvaluetable WHERE tid=".pwEscape($tid));
	if ($fieldvalue['endtime'] < $timestamp) {
		Showmsg('joinpc_error');
	}
	$rt = $db->get_one("SELECT nums,phone,mobile,address,extra,name,zip,message,ifpay FROM pw_pcmember WHERE pcmid=".pwEscape($pcmid)." AND uid=".pwEscape($winduid));
	!$ifpay && $ifpay = $rt['ifpay'];

	if (empty($_POST['step'])) {

		//$sign = $db->get_value("SELECT sign FROM pw_postcate WHERE pcid=".pwEscape($pcid));
		$rt['extra'] && $checked = 'checked';

		require_once PrintEot('ajax');ajax_footer();
	} elseif ($_POST['step'] == 2) {

		PostCheck();
		InitGP(array('nums','phone','mobile','address','extra','name','zip','message'));

		$nums = (int)$nums;
		if ($ifpay) {
			$nums = $rt['nums'];
		}
		if ($nums <= 0 && !$ifpay) {
			echo "numserror\t";
			ajax_footer();
		}

		$membernum = $db->get_value("SELECT SUM(nums) FROM pw_pcmember WHERE tid=".pwEscape($tid));
		if ($fieldvalue['limitnum'] && $fieldvalue['limitnum'] + $rt['nums'] - $membernum < $nums) {

			if ($pcid == 1) {
				echo "pcjoin_pcid_more\t";
			} elseif ($pcid == 2) {
				echo "pcjoin_more\t";
			}
			ajax_footer();
		}

		$deposit = !$fieldvalue['deposit'] ? $fieldvalue['price'] : $fieldvalue['deposit'];
		$totalcash = $deposit*$nums;
		$sqlarray = array(
			'phone'		=> $phone,
			'mobile'	=> $mobile,
			'address'	=> $address,
			'extra'		=> $extra,
			'totalcash'	=> $totalcash,
			'name'		=> $name,
			'zip'		=> $zip,
			'message'	=> $message
		);
		$nums && $sqlarray['nums'] = $nums;

		$db->update("UPDATE pw_pcmember SET " . pwSqlSingle($sqlarray)." WHERE pcmid=".pwEscape($pcmid)." AND uid=".pwEscape($winduid));
		echo "success\t$jointype\t$tid\t$payway";
		ajax_footer();
	}
} elseif ($action == 'pcalipay') {
	InitGP(array('pcmid','tid','pcid'),GP,2);
	if (empty($_POST['step'])) {
		$pcvaluetable = GetPcatetable($pcid);

		$fieldvalue = $db->get_one("SELECT pv.endtime,pv.price,pv.deposit,pv.payway,pm.nums,pm.phone,pm.mobile,pm.address,pm.extra,pm.ifpay,pm.totalcash FROM $pcvaluetable pv LEFT JOIN pw_pcmember pm ON pv.tid=pm.tid WHERE pm.tid=".pwEscape($tid)." AND pm.pcmid=".pwEscape($pcmid)." AND pm.uid=".pwEscape($winduid));

		!$fieldvalue && Showmsg('undefined_action');
		$fieldvalue['ifpay'] && Showmsg('pcjoin_payed');
		$fieldvalue['endtime'] < $timestamp && Showmsg('pcjoin_end');
		$nums = $fieldvalue['nums'];
		$deposit = !$fieldvalue['deposit'] ? $fieldvalue['price'] : $fieldvalue['deposit'];
		$totalcash = $fieldvalue['totalcash'];
		$alipayurl = "trade.php?action=pcalipay&tid=$tid&pcmid=$pcmid&pcid=$pcid";
	}

	require_once PrintEot('ajax');ajax_footer();
} elseif ($action == 'pcsendmsg') {

	InitGP(array('tid','pcid'));
	$read = $db->get_one("SELECT authorid,subject,fid FROM pw_threads WHERE tid=".pwEscape($tid));
	$foruminfo = $db->get_one('SELECT forumadmin,fupadmin FROM pw_forums WHERE fid='.pwEscape($read['fid']));
	$isGM = CkInArray($windid,$manager);
	$isBM = admincheck($foruminfo['forumadmin'],$foruminfo['fupadmin'],$windid);
	require_once(R_P.'lib/postcate.class.php');
	$post = array();
	$postCate = new postCate($post);
	$isadminright = $postCate->getAdminright($pcid,$read['authorid']);

	if (!$isadminright) {
		Showmsg('pcexport_none');
	}

	if (empty($_POST['step'])) {
		$sum = $db->get_value("SELECT SUM(nums) as nums FROM pw_pcmember WHERE tid=".pwEscape($tid));
		!$sum && Showmsg('pcsendmsg_fail');

		require_once PrintEot('ajax');ajax_footer();
	} elseif ($_POST['step'] == 2) {
		PostCheck();
		InitGP(array('subject','atc_content','tid','ifsave'));
		require_once(R_P.'require/common.php');

		$msg_title   = trim($subject);
		$atc_content = trim($atc_content);
		if (empty($atc_content) || empty($msg_title)) {
			Showmsg('msg_empty');
		} elseif (strlen($msg_title) > 75 || strlen($atc_content) > 1500) {
			Showmsg('msg_subject_limit');
		}
		require_once(R_P.'require/bbscode.php');
		$wordsfb = L::loadClass('FilterUtil');
		if (($banword = $wordsfb->comprise($msg_title)) !== false) {
			Showmsg('title_wordsfb');
		}
		if (($banword = $wordsfb->comprise($atc_content, false)) !== false) {
			Showmsg('content_wordsfb');
		}

		$query = $db->query("SELECT uid FROM pw_pcmember WHERE tid=".pwEscape($tid)." GROUP BY uid");
		while ($rt = $db->fetch_array($query)) {
			$uiddb[] = $rt['uid'];
		}

		$ifuids = $sqladd = $msglog = array();
		$uids = pwImplode($uiddb);
		if ($uids) {
			$query = $db->query("SELECT uid,username,newpm,banpm,msggroups FROM pw_members WHERE uid IN($uids)");
			while ($rt = $db->fetch_array($query)) {
				if ($rt['msggroups'] && strpos($rt['msggroups'],",$groupid,") !== false || strpos(",$rt[banpm],",",$windid,") !== false) {
					$errorname = $rt['username'];
					Showmsg('msg_refuse');
				}
				if ($edmid && in_array($rt['username'],$pwuser)) {
					$pwSQL = pwSqlSingle(array(
						'm.mdate'	=> $timestamp,
						'mc.title'	=> $msg_title,
						'mc.content'=> $atc_content
					));
					$db->update("UPDATE pw_msg m LEFT JOIN pw_msgc mc USING(mid) SET $pwSQL WHERE m.mid=".pwEscape($edmid)." AND m.fromuid=".pwEscape($winduid)." AND m.ifnew='1'");
					continue;
				}
				$sqladd[] = array($rt['uid'],$winduid,$windid,'rebox','1',$timestamp);
				$rt['uid'] <> $winduid && $msglog[$s_num] = $rt['uid'];
				$s_num++;

				if ($ifsave) {
					$sqladd[] = array($rt['uid'],$winduid,$rt['username'],'sebox','0',$timestamp);
					$s_num++;
				}
				$ifuids[] = $rt['uid'];
			}
		}
		if ($sqladd) {
			$db->update("INSERT INTO pw_msg(touid,fromuid,username,type,ifnew,mdate) VALUES ".pwSqlMulti($sqladd));
			$mid = $db->insert_id();
			$mc_sql = $ml_sql = array();
			for ($i = 0; $i < $s_num; $i++) {
				$mc_sql[] = array($mid,$msg_title,$atc_content);
				if (isset($msglog[$i])) {
					$ml_sql[] = array($mid,$winduid,$msglog[$i],$timestamp,'send');
					$ml_sql[] = array($mid,$msglog[$i],$winduid,$timestamp,'receive');
				}
				$mid++;
			}
			if ($mc_sql) {
				$db->update("REPLACE INTO pw_msgc(mid,title,content) VALUES ".pwSqlMulti($mc_sql));
			}
			if ($ml_sql) {
				$db->update("REPLACE INTO pw_msglog(mid,uid,withuid,mdate,mtype) VALUES ".pwSqlMulti($ml_sql));
			}
		}
		if ($ifuids) {
			updateNewpm($ifuids,'add');
		}

		Showmsg('send_success');
	}
} elseif ($action == 'pcifalipay') {
	$trade = $db->get_one("SELECT tradeinfo FROM pw_memberinfo WHERE uid=".pwEscape($winduid));
	$trade = unserialize($trade['tradeinfo']);
	if(!$trade['alipay']) {
		echo 'fail';
	}
	ajax_footer();
} elseif ($action == 'pcdelimg') {
	InitGP(array('fieldname','pctype'));
	InitGP(array('tid','id'),GP,2);
	if (!$tid || !$id || !$fieldname || !$pctype) {
		echo 'fail';ajax_footer();
	}
	$id = (int)$id;
	if ($pctype == 'topic') {
		$tablename = GetTopcitable($id);
	} elseif ($pctype == 'postcate') {
		$tablename = GetPcatetable($id);
	}

	$fieldname = $db->get_value("SELECT fieldname FROM pw_pcfield WHERE fieldname=" . pwEscape($fieldname));
	if (!$fieldname || !$tablename) {
		echo 'fail';ajax_footer();
	}
	$path = $db->get_value("SELECT $fieldname FROM $tablename WHERE tid=". pwEscape($tid));

	if (strpos($path,'..') !== false) {
		echo 'fail';ajax_footer();
	}
	$lastpos = strrpos($path,'/') + 1;
	$s_path = substr($path, 0, $lastpos) . 's_' . substr($path, $lastpos);

	if (!file_exists("$attachpath/$path")) {
		if (pwFtpNew($ftp,$db_ifftp)) {
			$ftp->delete($path);
			$ftp->delete($s_path);
			pwFtpClose($ftp);
		}
	} else {
		P_unlink("$attachdir/$path");
		if (file_exists("$attachdir/$s_path")) {
			P_unlink("$attachdir/$s_path");
		}
	}

	$db->update("UPDATE $tablename SET $fieldname='' WHERE tid=". pwEscape($tid));

	echo 'success';

	ajax_footer();

} elseif ($action == 'poplogin'){
	InitGP(array('tid','page','toread','fpage','anchor'));
	$jumpurl = $db_bbsurl.'/read.php?tid='.$tid;
	($page>1) && $jumpurl.='&page='.$page;
	($fpage) && $jumpurl.='&fpage='.$fpage;
	($toread) && $jumpurl.='&toread='.$toread;
	$jumpurl.="#".$anchor;
	require_once PrintEot('poplogin');
	ajax_footer();
} elseif ($action == 'pingpage') {
	require_once(R_P.'require/showimg.php');

	InitGP(array('page','fid','tid','pid'), null, 2);
	$perpage = 10;

	//权限
	$foruminfo = $db->get_one('SELECT * FROM pw_forums f LEFT JOIN pw_forumsextra fe USING(fid) WHERE f.fid='.pwEscape($fid));
	!$foruminfo && Showmsg('data_error');
	$isGM = $isBM = $admincheck = 0;
	if ($groupid != 'guest') {
		$isGM = CkInArray($windid,$manager);
		$isBM = admincheck($foruminfo['forumadmin'],$foruminfo['fupadmin'],$windid);
		$admincheck = ($isGM || $isBM) ? 1 : 0;
	}

	$count = $db->get_value("SELECT COUNT(*) FROM pw_pinglog WHERE  fid=".pwEscape($fid)." AND tid=".pwEscape($tid)." AND pid=".pwEscape($pid)."  AND ifhide=0");
	$total = ceil($count / $perpage);
	if ($page < 2) $page = 2;
	$offset = ($page - 1) * $perpage;

	$ping_db = array();
	$query = $db->query("SELECT a.*,b.uid,b.icon FROM pw_pinglog a LEFT JOIN pw_members b ON a.pinger=b.username WHERE a.fid=".pwEscape($fid)." AND a.tid=".pwEscape($tid)." AND a.pid=".pwEscape($pid)." AND ifhide=0 ORDER BY a.pingdate DESC LIMIT $offset,$perpage");
	while ($rt = $db->fetch_array($query)) {
		list($rt['pingtime'],$rt['pingdate']) = getLastDate($rt['pingdate']);
		$rt['record'] = $rt['record'] ? $rt['record'] : "-";
		if ($rt['point'] > 0) $rt['point'] = "+" . $rt['point'];
		$tmp = showfacedesign($rt['icon'],true);
		$rt['icon'] = $tmp[0];
		$ping_db[] = $rt;
	}

	require_once PrintEot('ajax');
	ajax_footer();
} elseif ($action == 'delpinglog') {
	InitGP(array('fid','tid','pid','pingid'), null, 2);

	//权限
	$foruminfo = $db->get_one('SELECT * FROM pw_forums f LEFT JOIN pw_forumsextra fe USING(fid) WHERE f.fid='.pwEscape($fid));
	!$foruminfo && Showmsg('data_error');
	$isGM = $isBM = $admincheck = 0;
	if ($groupid != 'guest') {
		$isGM = CkInArray($windid,$manager);
		$isBM = admincheck($foruminfo['forumadmin'],$foruminfo['fupadmin'],$windid);
		$admincheck = ($isGM || $isBM) ? 1 : 0;
	}

	$pingdata = $db->get_one("SELECT * FROM pw_pinglog WHERE id=".pwEscape($pingid));
	!$pingdata && Showmsg('data_error');
	!($admincheck || ($_G['markable'] && $pingdata['pinger'] == $windid)) && Showmsg('data_error');

	$db->update("UPDATE pw_pinglog SET ifhide=1 WHERE id=".pwEscape($pingid)." LIMIT 1");
	if ($db->affected_rows()) {
		echo "success";
		require_once R_P . 'require/pingfunc.php';
		update_markinfo($fid, $tid, $pid);
		 # memcache reflesh
		 if ($db_memcache) {
			 $threads = L::loadClass('Threads');
			 $threads->delThreads($tid);
		 }
	} else {
		echo "data_error";
	}
	ajax_footer();
} elseif ($action == 'clearpinglog') {
	InitGP(array('fid','tid','pid'), null, 2);

	$pingdata = $db->get_one("SELECT id FROM pw_pinglog WHERE fid=".pwEscape($fid)." AND tid=".pwEscape($tid)." AND pid=".pwEscape($pid)." AND pinger=".pwEscape($windid));
	$user_has_ping = $pingdata ? true : false;

	$pid = $pid ? $pid : "tpc";

	require_once PrintEot('ajax');
	ajax_footer();
} elseif ($action == 'doclearpinglog') {
	InitGP(array('fid','tid','pid'), null, 2);
	$db->update("UPDATE pw_pinglog SET ifhide=1 WHERE fid=".pwEscape($fid)." AND tid=".pwEscape($tid)." AND pid=".pwEscape($pid)." AND pinger=".pwEscape($windid));
	if ($db->affected_rows()) {
		echo "清空评分动态成功!";
		require_once R_P . 'require/pingfunc.php';
		update_markinfo($fid, $tid, $pid);
	} else {
		echo "没有需要清空的评分动态";
	}
	ajax_footer();

} elseif ($action == 'showuserbinding') {

	$arr = array();
	$query = $db->query("SELECT m.uid,m.username FROM pw_userbinding u1 LEFT JOIN pw_userbinding u2 ON u1.id=u2.id LEFT JOIN pw_members m ON u2.uid=m.uid WHERE u1.uid=" . pwEscape($winduid));
	while ($rt = $db->fetch_array($query)) {
		$rt['uid'] && $rt['uid'] != $winduid && $arr[] = $rt;
	}
	require_once PrintEot('ajax');
	ajax_footer();

} elseif ($action == 'switchuser') {

	InitGP(array('uid'));
	$db_logintype = 2;
	require_once(R_P . 'require/checkpass.php');

	$id = $db->get_value("SELECT id FROM pw_userbinding WHERE uid=" . pwEscape($winduid));

	$user = $db->get_one("SELECT s.password,m.uid,m.safecv FROM pw_userbinding s LEFT JOIN pw_members m ON s.uid=m.uid WHERE s.id=" . pwEscape($id) . ' AND s.uid=' . pwEscape($uid));

	$logininfo = checkpass($user['uid'], $user['password'], $user['safecv'], 1);
	if (!is_array($logininfo)) {
		switch ($logininfo) {
			case 'login_forbid':
			case 'login_pwd_error':
				Showmsg('switchuser_error');
			default:
				Showmsg($logininfo);
		}
	}
	list($winduid, $groupid, $windpwd, $showmsginfo) = $logininfo;
	
	$cktime = 7*24*3600;
	(int)$keepyear && $cktime = 31536000;
	$cktime != 0 && $cktime += $timestamp;
	Cookie("winduser",StrCode($winduid."\t".$windpwd."\t".$user['safecv']),$cktime);
	Cookie("ck_info",$db_ckpath."\t".$db_ckdomain);
	//Cookie("ucuser",'cc',$cktime);
	Cookie('lastvisit','',0);

	echo "ok\t$showmsginfo";

	ajax_footer();
	
} elseif ($action == 'jobpop') {
	if(!$db_job_isopen || !$winduid){
		exit;
	}
	initGP(array("v","job"));
	if($job == "cookie"){
		$v = (in_array($v,array(0,1))) ? $v : 0;/*设置cookie*/
		Cookie("jobpop",$v);
	}elseif($job == "show" || !getCookie("jobpop")){/*强制显示或自定义显示*/
		Cookie("jobpop",0);
		$jobService = L::loadclass("job");/*任务服务类*/
		$lists =  $jobService->jobDisplayController($winduid,$groupid,'list');
		$appliedHTML = $jobService->buildApplieds($winduid,$groupid);
		
		require PrintEot('jobpop');ajax_footer ();exit;
	}
}

function isFriend($uid,$friend) {
	global $db;
	if ($db->get_value("SELECT uid FROM pw_friends WHERE uid=" . pwEscape($uid) . ' AND friendid=' . pwEscape($friend) . " AND status='0'")) {
		return true;
	}
	return false;
}

function getPostnumByType($type,$rt) {
	global $timestamp;
	$posttime = '';
	if ($type == 'topic') {
		if ($timestamp - $rt['lastpost'] < 604800) {
			$posttime = get_date($rt['lastpost'],'m-d');
		}
	} else {
		if ($timestamp - $rt[$type.'_lastpost'] < 604800) {
			$posttime = get_date($rt[$type.'_lastpost'],'m-d');
		}
	}
	return $posttime;
}


function getfavor($tids) {
	$tids  = explode('|',$tids);
	$tiddb = array();
	foreach ($tids as $key => $t) {
		if ($t) {
			$v = explode(',',$t);
			foreach ($v as $k => $v1) {
				$tiddb[$key][$v1] = $v1;
			}
		}
	}
	return $tiddb;
}
function makefavor($tiddb) {
	$newtids = $ex = '';
	$k = 0;
	ksort($tiddb);
	foreach ($tiddb as $key => $val) {
		$new_tids = '';
		rsort($val);
		if ($key != $k) {
			$s = $key - $k;
			for ($i = 0; $i < $s; $i++) {
				$newtids .= '|';
			}
		}
		foreach ($val as $k => $v) {
			is_numeric($v) && $new_tids .= $new_tids ? ','.$v : $v;
		}
		$newtids .= $ex.$new_tids;
		$k  = $key + 1;
		$ex = '|';
	}
	return $newtids;
}

function getNum($fid){
	if ($forum[$fid]['type'] = 'category'){
		$fidnum = 1;
	} elseif ($forum[$fid]['type'] = 'forum'){
		$fidnum = 2;
	} elseif ($forum[$fid]['type'] = 'sub'){
		$fup = $forum[$fid]['fup'];
		if ($forum[$fup]['type'] = 'forum'){
			$fidnum = 3;
		}else{
			$fidnum = 4;
		}
	}
	return $fidnum;
}

function getphotourl($path,$thumb = false) {
	global $imgpath;
	if (!$path) {
		return "$imgpath/apps/nophoto.gif";
	}
	$lastpos = strrpos($path,'/') + 1;
	$thumb && $path = substr($path, 0, $lastpos) . 's_' . substr($path, $lastpos);
	list($path) = geturl($path, 'show');
	if ($path == 'imgurl' || $path == 'nopic') {
		return "$imgpath/apps/nophoto.gif";
	}
	return $path;
}

function addSingleFriend($updatemem,$winduid,$frienduid,$timestamp,$status,$friendtype=0,$checkmsg=''){
	global $db;
	$friend = $db->get_one("SELECT * FROM pw_friends WHERE uid=".pwEscape($winduid)." AND friendid=".pwEscape($frienduid));
	if(!$friend){
		$pwSQL = pwSqlSingle(array(
			'uid'		=> $winduid,
			'friendid'	=> $frienduid,
			'joindate'	=> $timestamp,
			'status'	=> $status,
			'descrip'	=> $checkmsg,
			'ftid'		=> $friendtype
		));
		$db->update("INSERT INTO pw_friends SET $pwSQL");
	}
	($updatemem) && $db->update("UPDATE pw_memberdata SET f_num=f_num+1 WHERE uid=".pwEscape($winduid));
}
?>