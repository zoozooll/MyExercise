<?php
define('SCR','message');
require_once('global.php');
$secondurl = 'message.php';

/**
* 用户组权限判断
*/
$groupid == 'guest' && Showmsg('not_login');
$_G['maxmsg'] == 0 && Showmsg('msg_group_right');

if (!$pw_seoset && $pw_seoset = L::loadClass('seoset')) {
	$webPageTitle = $pw_seoset->getPageTitle();
	$metaDescription = $pw_seoset->getPageMetadescrip();
	$metaKeywords = $pw_seoset->getPageMetakeyword();
}

$msginfo['mdate'] = '';
InitGP(array('action','page'));
if (!$action) $action = 'receivebox';
$db_menuinit .= ",'td_userinfomore' : 'menu_userinfomore'";

$pubmsg = $readmsg = $delmsg = $pages = '';
if (in_array($action,array('receivebox','readpub','del','clear','readn','read','unread'))) {
	$pubmsg = getUserPublicMsgRecord($winduid);
	@extract($pubmsg);
}
$msg_gid = $winddb['groupid'];
$msgdb   = array();
$permsg  = 10;
$db_windpost['checkurl'] = 0;
require_once(R_P.'require/showimg.php');
list($faceurl) = showfacedesign($winddb['icon'],1,'s');
$pro_tab = 'message';
if ($action == 'receivebox') {
	/**
	* 收件箱
	*/
	InitGP(array('type'));
	$type != 'public' && $type != 'notify' && $type != 'newmsg' && $type = 'base';
	$pubnew = $prnew = 0;
	$nmsgdb = $pmsgdb = $new_pmsgdb = $new_msgdb_notify = $new_msgdb = array();
	$newdel = $newread = '';
	$orderby = $type == 'newmsg' ? "m.ifnew DESC, " : "";

	$query  = $db->query("SELECT m.mid,m.fromuid,m.togroups,m.username,m.type,m.mdate,mc.title FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.type='public' AND m.togroups LIKE ".pwEscape("%,$msg_gid,%")." AND m.mdate>".pwEscape($winddb['regdate'])." ORDER BY m.mdate DESC LIMIT 20");
	while ($msginfo = $db->fetch_array($query)) {
		if ($delmsg && strpos(",$delmsg,",",$msginfo[mid],") !== false) {
			$newdel .= $newdel ? ','.$msginfo['mid'] : $msginfo['mid'];
			continue;
		}
		if ($readmsg && strpos(",$readmsg,",",$msginfo[mid],") !== false) {
			$newread .= $newread ? ','.$msginfo['mid'] : $msginfo['mid'];
			$msginfo['ifnew'] = 0;
		} else {
			$pubnew = 2;
			$msginfo['ifnew'] = 1;
		}
		$msginfo['title'] = substrs($msginfo['title'],50);
		$msginfo['mdate'] = get_date($msginfo['mdate']);
		$msginfo['from']  = $msginfo['username'];
		$pmsgdb[] = $msginfo;
		$msginfo['ifnew'] && $new_pmsgdb[] = $msginfo;
	}
	$type == 'newmsg' && $new_pmsgdb = array_slice($new_pmsgdb,0,10);
	if ($readmsg || $delmsg) {
		$newread = $newread ? msgsort($newread) : '';
		$newdel  = $newdel  ? msgsort($newdel)  : '';
		if ($readmsg != $newread || $delmsg != $newdel) {
			$db->update('UPDATE pw_memberinfo SET ' . pwSqlSingle(array('readmsg' => $newread, 'delmsg' => $newdel)) . 'WHERE uid=' . pwEscape($winduid));
		}
	}
	if ($type == 'notify' || $type == 'newmsg') {
		$msgcount = $db->get_value("SELECT COUNT(*) FROM pw_msg m WHERE m.touid=".pwEscape($winduid)."AND m.type='rebox' AND fromuid=0");
		(!is_numeric($page) || $page<1 || $msgcount <= $permsg) && $page = 1;
		$msgstart = 0;
		if ($msgcount > $permsg) {
			$numofpage = ceil($msgcount/$permsg);
			$page > $numofpage && $page = $numofpage>0 ? $numofpage : 1;
			$msgstart = ($page-1)*$permsg;
			$notify_pages = numofpage($msgcount,$page,$numofpage,"message.php?action=$action&type=notify&");
		}
		$query = $db->query("SELECT m.mid,m.fromuid,m.touid,m.username,m.ifnew,m.mdate,mc.title FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.type='rebox' AND m.touid=".pwEscape($winduid)." AND fromuid=0 ORDER BY $orderby m.mdate DESC ".pwLimit($msgstart,$permsg));
		while ($msginfo = $db->fetch_array($query)) {
			$msginfo['title'] = substrs($msginfo['title'],50);
			$msginfo['mdate'] = get_date($msginfo['mdate']);
			$msginfo['from']  = $msginfo['username'];
			$msginfo['to'] = $windid;
			$msgdb_notify[] = $msginfo;
			$msginfo['ifnew'] && $new_msgdb_notify[] = $msginfo;
		}
		$type == 'newmsg' && $new_msgdb_notify = array_slice($new_msgdb_notify,0,10);
	}
	if ($type == 'base' || $type == 'public' || $type == 'newmsg') {
		$msgcount = $db->get_value("SELECT COUNT(*) FROM pw_msg m WHERE m.touid=".pwEscape($winduid)."AND m.type='rebox' AND fromuid<>0");
		if ($msgcount) {
			$contl = number_format(($msgcount/$_G['maxmsg'])*100,3);
		} else {
			$msgcount = $contl = 0;
		}
		$msgstart = $msgcount > $_G['maxmsg'] ? $msgcount - $_G['maxmsg'] : 0;
		$msgtoall = $msgcount > $_G['maxmsg'] ? $_G['maxmsg'] : $msgcount;
		(!is_numeric($page) || $page<1 || $msgtoall <= $permsg) && $page = 1;
		if ($msgtoall > $permsg) {
			$numofpage = ceil($msgtoall/$permsg);
			$page > $numofpage && $page = $numofpage>0 ? $numofpage : 1;
			$msgstart += ($page-1)*$permsg;
			$base_pages = numofpage($msgtoall,$page,$numofpage,"message.php?action=$action&");
		}
		$query = $db->query("SELECT m.mid,m.fromuid,m.touid,m.username,m.ifnew,m.mdate,mc.title FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.type='rebox' AND m.touid=".pwEscape($winduid)." AND fromuid<>0 ORDER BY $orderby m.mdate DESC ".pwLimit($msgstart,$permsg));
		while ($msginfo = $db->fetch_array($query)) {
			$msginfo['title'] = substrs($msginfo['title'],50);
			$msginfo['mdate'] = get_date($msginfo['mdate']);
			$msginfo['from']  = $msginfo['username'];
			$msginfo['to'] = $windid;
			$msgdb[] = $msginfo;
			$msginfo['ifnew'] && $new_msgdb[] = $msginfo;
		}
		$type == 'newmsg' && $new_msgdb = array_slice($new_msgdb,0,10);
	}
	if ($type == 'newmsg' && !$new_msgdb && $page == 1) {
		updateNewpm($winduid,'recount');
	}

} elseif ($action == 'sendbox') {
	/**
	* 发件箱
	*/
	$msgstart = 0;
	(!is_numeric($page) || $page<1 || $_G['maxmsg']<=$permsg) && $page = 1;
	if ($_G['maxmsg'] > $permsg) {
		$rt = $db->get_one("SELECT COUNT(*) AS sum FROM pw_msg WHERE type='sebox' AND fromuid=".pwEscape($winduid));
		$numofpage = ceil($rt['sum']/$permsg);
		$page > $numofpage && $page = $numofpage>0 ? $numofpage : 1;
		$msgstart += ($page-1)*$permsg;
		$pages = numofpage($rt['sum'],$page,$numofpage,"message.php?action=$action&");
	}
	$query = $db->query("SELECT m.mid,m.fromuid,m.touid,m.username,m.ifnew,m.mdate,mc.title FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.type='sebox' AND m.fromuid=".pwEscape($winduid)." ORDER BY m.mdate DESC ".pwLimit($msgstart,$permsg));
	while ($msginfo = $db->fetch_array($query)) {
		$msginfo['title'] = substrs($msginfo['title'],50);
		$msginfo['mdate'] = get_date($msginfo['mdate']);
		$msginfo['from']  = $windid;
		$msginfo['to']    = $msginfo['username'];
		$msgdb[] = $msginfo;
	}

} elseif ($action == 'scout') {
	/**
	* 消息跟踪
	*/
	$msgstart = 0;
	(!is_numeric($page) || $page<1 || $_G['maxmsg']<=$permsg) && $page = 1;
	if ($_G['maxmsg'] > $permsg) {
		$rt = $db->get_one("SELECT COUNT(*) AS sum FROM pw_msg WHERE type='rebox' AND fromuid=".pwEscape($winduid));
		$numofpage = ceil($rt['sum']/$permsg);
		$page > $numofpage && $page = $numofpage>0 ? $numofpage : 1;
		$msgstart += ($page-1)*$permsg;
		$pages = numofpage($rt['sum'],$page,$numofpage,"message.php?action=$action&");
	}
	$query = $db->query("SELECT m.mid,m.fromuid,m.touid,m.username,m.ifnew,m.mdate,mc.title,me.username AS toname FROM pw_msg m LEFT JOIN pw_msgc mc ON m.mid=mc.mid LEFT JOIN pw_members me ON me.uid=m.touid WHERE m.type='rebox' AND m.fromuid=".pwEscape($winduid)." ORDER BY m.mdate DESC ".pwLimit($msgstart,$permsg));
	while ($msginfo = $db->fetch_array($query)) {
		$msginfo['title'] = substrs($msginfo['title'],50);
		$msginfo['mdate'] = get_date($msginfo['mdate']);
		$msginfo['from']  = $windid;
		$msginfo['to'] = $msginfo['toname'];
		$msgdb[] = $msginfo;
	}

} elseif ($action == 'readpub') {

	require_once(R_P.'require/bbscode.php');
	InitGP(array('mid'));
	$msginfo = $db->get_one("SELECT m.mid,m.fromuid AS withuid,m.touid,m.username,m.ifnew,m.mdate,mc.title,mc.content FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.mid=".pwEscape($mid)." AND m.type='public' AND m.togroups LIKE ".pwEscape("%,$msg_gid,%"));
	if ($msginfo) {
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
		$if_newmsg = 0;
		if ($pubmsg) {
			if (strpos(",$readmsg,",",$msginfo[mid],") === false) {
				$readmsg .= $readmsg ? ','.$msginfo['mid'] : $msginfo['mid'];
				$readmsg = msgsort($readmsg);
				$db->update('UPDATE pw_memberinfo SET readmsg='.pwEscape($readmsg,false).' WHERE uid='.pwEscape($winduid));
				$if_newmsg = 1;
			}
		} else {
			$readmsg = $msginfo['mid'];
			$db->update("INSERT INTO pw_memberinfo SET ".pwSqlSingle(array('uid'=>$winduid,'readmsg'=>$readmsg)));
			$if_newmsg = 1;
		}
		$if_newmsg == 1 && updateNewpm($winduid,'minus');
	} else {
		Showmsg('msg_error');
	}
//TODO read统一
} elseif ($action == 'read') {

	require_once(R_P.'require/bbscode.php');
	InitGP(array('mid'));
	$msginfo = $db->get_one("SELECT m.mid,m.fromuid AS withuid,m.touid,m.username,m.ifnew,m.mdate,mc.title,mc.content FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.mid=".pwEscape($mid)." AND m.type='rebox' AND m.touid=".pwEscape($winduid)."AND m.fromuid<>0");
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
	} else {
		Showmsg('msg_error');
	}
} elseif ($action == 'readn') {

	require_once(R_P.'require/bbscode.php');
	InitGP(array('mid'));
	$msginfo = $db->get_one("SELECT m.mid,m.fromuid AS withuid,m.touid,m.username,m.ifnew,m.mdate,mc.title,mc.content FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.mid=".pwEscape($mid)." AND m.type='rebox' AND m.touid=".pwEscape($winduid)."AND fromuid=0");
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
	} else {
		Showmsg('msg_error');
	}

} elseif ($action == 'unread') {

	PostCheck();
	InitGP('mid');
	$rt = $db->get_one("SELECT mid FROM pw_msg WHERE mid=".pwEscape($mid)." AND type='rebox' AND touid=".pwEscape($winduid)."AND fromuid<>0");
	if ($rt) {
		$db->update('UPDATE pw_msg SET ifnew=1 WHERE mid='.pwEscape($mid));
		updateNewpm($winduid,'add');
	} else {
		Showmsg('msg_error');
	}
	refreshto('message.php','msg_unread');

} elseif ($action == 'readsnd') {

	require_once(R_P.'require/bbscode.php');
	InitGP(array('mid'));
	$msginfo = $db->get_one("SELECT m.mid,m.fromuid,m.touid AS withuid,m.username,m.ifnew,m.mdate,mc.title,mc.content FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.mid=".pwEscape($mid)." AND m.type='sebox' AND m.fromuid=".pwEscape($winduid));
	if ($msginfo) {
		$msginfo['content'] = str_replace("\n","<br>",$msginfo['content']);
		$msginfo['content'] = convert($msginfo['content'],$db_windpost);
		if (strpos($msginfo['content'],'[s:')!==false) {
			$msginfo['content'] = showface($msginfo['content']);
		}
		$msginfo['content'] = wordsConvert($msginfo['content']);
		$msginfo['title']   = str_replace('&ensp;$','$', $msginfo['title']);
		$msginfo['content'] = str_replace('&ensp;$','$', $msginfo['content']);
		$msginfo['mdate']   = get_date($msginfo['mdate']);
	} else {
		Showmsg('msg_error');
	}

} elseif ($action == 'readscout') {

	require_once(R_P.'require/bbscode.php');
	InitGP(array('mid'));
	$msginfo = $db->get_one("SELECT m.mid,m.fromuid,m.touid AS withuid,m.ifnew,m.mdate,mc.title,mc.content,mb.username FROM pw_msg m LEFT JOIN pw_msgc mc ON m.mid=mc.mid LEFT JOIN pw_members mb ON m.touid=mb.uid WHERE m.mid=".pwEscape($mid)." AND m.type='rebox' AND m.fromuid=".pwEscape($winduid));
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
	} else {
		Showmsg('msg_error');
	}
} elseif ($action == 'chatlog') {

	require_once(R_P.'require/bbscode.php');
	InitGP(array('withuid','page'));
	(!is_numeric($page) || $page<1) && $page = 1;
	$rt = $db->get_one("SELECT COUNT(*) AS sum FROM pw_msglog WHERE uid=".pwEscape($winduid)." AND withuid=".pwEscape($withuid));
	$numofpage = ceil($rt['sum']/$permsg);

	$m = $db->get_one("SELECT username FROM pw_members WHERE uid=".pwEscape($withuid));
	$u_name = array('send' => $windid, 'receive' => $m['username']);

	if ($numofpage > 1) {
		$pages = numofpage($rt['sum'],$page,$numofpage,"message.php?action=$action&withuid=$withuid&");
		$limit = pwLimit(($page-1)*$permsg,$permsg);
	} else {
		$pages = $limit = '';
	}
	$query = $db->query("SELECT ml.*,mc.title,mc.content FROM pw_msglog ml LEFT JOIN pw_msgc mc ON ml.mid=mc.mid WHERE ml.uid=".pwEscape($winduid)." AND ml.withuid=".pwEscape($withuid)." ORDER BY ml.mdate DESC $limit");
	while ($rt = $db->fetch_array($query)) {
		$rt['mdate'] = get_date($rt['mdate']);
		$rt['content'] = str_replace("\n","<br>",$rt['content']);
		$rt['content'] = convert($rt['content'],$db_windpost);
		if (strpos($rt['content'],'[s:') !== false) {
			$rt['content'] = showface($rt['content']);
		}
		$rt['content'] = wordsConvert($rt['content']);
		$msgdb[] = $rt;
	}
	$msgdb = array_reverse($msgdb);

} elseif ($action == 'write') {
	/**
	* 写短信
	*/
	$_G['allowmessege'] == 0 && Showmsg('msg_group_right');

	if ($_G['postpertime'] || $_G['maxsendmsg']) {
		$rp = $db->get_one('SELECT COUNT(*) AS tdmsg,MAX(mdate) AS lastwrite FROM pw_msg WHERE fromuid='.pwEscape($winduid).' AND mdate>'.pwEscape($tdtime));
		if ($_G['postpertime'] && $timestamp - $rp['lastwrite'] <= $_G['postpertime']) {
			Showmsg('msg_limit');
		} elseif ($_G['maxsendmsg'] && $rp['tdmsg']>=$_G['maxsendmsg']) {
			Showmsg('msg_num_limit');
		}
	}
	list(,,,$msgq) = explode("\t",$db_qcheck);

	InitGP(array('remid','edmid','touid','transmit'));

	if (empty($_POST['step'])) {

		require_once(R_P.'require/forum.php');
		$msgid = $subject = $atc_content = '';
		if (is_numeric($remid)) {
			$reinfo = $db->get_one('SELECT m.fromuid,m.touid,m.username,m.type,mc.title,mc.content FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.mid='.pwEscape($remid)." AND m.type='rebox' AND m.touid=".pwEscape($winduid));
			if ($reinfo) {
				$msgid = "value=\"$reinfo[username]\"";
				$subject = strpos($reinfo['title'],'Re:')===false ? 'Re:'.$reinfo['title'] : $reinfo['title'];
				$atc_content = "[quote]".trim(substrs(preg_replace("/\[quote\](.+?)\[\/quote\]/is", '', $reinfo['content']),100))."[/quote]\n\n";
			}
		} elseif (is_numeric($edmid)) {
			$edinfo = $db->get_one("SELECT m.touid,m.type,mc.title,mc.content,mb.username FROM pw_msg m LEFT JOIN pw_msgc mc ON m.mid=mc.mid LEFT JOIN pw_members mb ON m.touid=mb.uid WHERE m.mid=".pwEscape($edmid)." AND m.fromuid=".pwEscape($winduid)." AND m.ifnew='1' AND m.type='rebox'");
			if ($edinfo) {
				$msgid = "value=\"$edinfo[username]\"";
				$subject = $edinfo['title'];
				$atc_content = $edinfo['content'];
			}
		} elseif (is_numeric($touid)) {
			$reinfo = $db->get_one("SELECT username FROM pw_members WHERE uid=".pwEscape($touid));
			$msgid  = "value=\"$reinfo[username]\"";
		} elseif (is_numeric($transmit)) {
			$reinfo = $db->get_one("SELECT m.touid,m.fromuid,mc.title,mc.content FROM pw_msg m LEFT JOIN pw_msgc mc ON m.mid=mc.mid WHERE m.mid=" . pwEscape($transmit));
			if ($winduid == $reinfo['touid'] || $winduid == $reinfo['fromuid']) {
				$subject = $reinfo['title'];
				$atc_content = $reinfo['content'];
			}
		}
	} else {

		PostCheck(1,$db_gdcheck & 8,$msgq);
		InitGP(array('msg_title','atc_content','pwuser','ifsave'),'P');
		$msg_title   = trim($msg_title);
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
		$uids = array();
		$pwuser = explode(',',$pwuser);
		empty($pwuser) && $pwuser = array();
		$pwuser = array_unique(array_diff($pwuser,array('')));
		if ($pwuser) {
			$query = $db->query("SELECT uid FROM pw_members WHERE username IN (".pwImplode($pwuser).")");
			while ($rt = $db->fetch_array($query)) {
				$uids[] = $rt['uid'];
			}
		}
		if (!$uids) {
			$errorname = $pwuser;
			$errorname = implode($pwuser,',');
			Showmsg('user_not_exists');
		} elseif ($w_num = count($uids) > 9) {
			Showmsg('msg_send_limit');
		}

		$s_num = 0;
		$atc_content	= autourl($atc_content);
		$uids = pwImplode($uids);
		$ifuids = $sqladd = $msglog = array();
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

		//job sign
		initJob($winduid,"doSendMessage",array('user'=>$pwuser));

		if ($ifuids) {
			updateNewpm($ifuids,'add');
		}
		if ($w_num > 9) {
			Showmsg('msg_send_limit');
		} else {
			refreshto("message.php?action=receivebox",'operate_success');
		}
	}
} elseif ($action == 'clear') {

	PostCheck();
	$delids = array();
	$query  = $db->query("SELECT mid FROM pw_msg WHERE type='rebox' AND touid=".pwEscape($winduid)." UNION SELECT mid FROM pw_msg WHERE type='sebox' AND fromuid=".pwEscape($winduid));
	while ($rt = $db->fetch_array($query)) {
		$delids[] = $rt['mid'];
	}
	if ($delids) {
		$delids = pwImplode($delids);
		$db->update("DELETE FROM pw_msg WHERE mid IN($delids)");
		require_once(R_P.'require/msg.php');
		delete_msgc($delids);
	}
	updateNewpm($winduid,'recount');

	refreshto('message.php','del_success',2,true);
/**
} elseif ($action == 'doread') {
	PostCheck();
	InitGP(array('delid'));
	$delids = array();
	if (is_array($delid)) {
		foreach ($delid as $value) {
			is_numeric($value) && $delids[] = $value;
		}
	}

	if ($delids) {
		$delids=pwImplode($delids);
		$ids = array();
		$query = $db->query("SELECT mid FROM pw_msg WHERE mid IN($delids) AND type='rebox' AND touid=".pwEscape($winduid)."AND ifnew=1");
		while ($rt = $db->fetch_array($query)) {
			$ids[] = $rt['mid'];
		}
		if ($ids) {
			$ids = pwImplode($ids);
			$db->update("UPDATE pw_msg SET ifnew=0 WHERE mid IN($ids)");
			$db->update("UPDATE pw_members SET newpm='0' WHERE uid IN($ids)"));
		}
		getusermsg($winduid);
	}

	refreshto("message.php?action=$towhere",'operate_success');
*/
} elseif ($action == 'del') {

	PostCheck();
	InitGP(array('pdelid','delid','towhere','pdelids','delids','type'));
	if (!is_numeric($delids)) {
		$delids = '';
		if (is_array($delid)) {
			foreach ($delid as $value) {
				is_numeric($value) && $delids.=$value.',';
			}
			$delids && $delids=substr($delids,0,-1);
		}
	}
	if ($towhere == 'receivebox') {
		if (!is_numeric($pdelids)) {
			$pdelids = '';
			if (is_array($pdelid)) {
				foreach ($pdelid as $value) {
					is_numeric($value) && $pdelids.=$value.',';
				}
				$pdelids && $pdelids = substr($pdelids,0,-1);
			}
		}
	} else {
		$pdelids = '';
	}
	!$delids && !$pdelids && Showmsg('del_error');

	if ($delids) {
		$delall = false;
		if ($towhere == 'receivebox') {
			$sql = " AND type='rebox' AND touid=".pwEscape($winduid);
		} elseif ($towhere == 'sendbox') {
			$sql = " AND type='sebox' AND fromuid=".pwEscape($winduid);
		} else {
			$sql = " AND type='rebox' AND fromuid=".pwEscape($winduid)."AND ifnew=1";
			$delall = true;
		}

		$newdelids = array();
		$query = $db->query("SELECT mid,touid FROM pw_msg WHERE mid IN($delids) $sql");
		while ($rt = $db->fetch_array($query)) {
			$newdelids[] = $rt['mid'];
			if ($towhere == 'scout') {
				$touiddb[] = $rt['touid'];
			}
		}
		if ($newdelids) {
			$newdelids = pwImplode($newdelids);
			$db->update("DELETE FROM pw_msg WHERE mid IN($newdelids)");
			if ($delall) {
				$db->update("DELETE FROM pw_msgc WHERE mid IN($newdelids)");
				$db->update("DELETE FROM pw_msglog WHERE mid IN($newdelids)");
			} else {
				require_once(R_P.'require/msg.php');
				delete_msgc($newdelids);
			}
		}

	}
	if ($pdelids) {
		if ($pubmsg) {
			$msginfo = '';
			$readmsg = ','.$readmsg.',';
			$delmsg  = $delmsg ? ','.$delmsg.',' : ',';
			$deliddb = explode(',',$pdelids);
			foreach ($deliddb as $key=>$val) {
				if (strpos("$readmsg",",$val,") !== false) {
					$readmsg = str_replace(",$val,",",",$readmsg);
				}
				if (strpos($delmsg,",$val,") === false) {
					$delmsg .= $val.',';
				}
			}
			$readmsg = msgsort(substr($readmsg,1,-1));
			$delmsg  = msgsort(substr($delmsg,1,-1));
			$db->update("UPDATE pw_memberinfo SET ".pwSqlSingle(array('readmsg'=>$readmsg,'delmsg'=>$delmsg))." WHERE uid=".pwEscape($winduid));
		} else {
			$delmsg  = msgsort($delids);
			$db->update("INSERT INTO pw_memberinfo SET ".pwSqlSingle(array('uid'=>$winduid,'delmsg'=>$delmsg)));
		}
	}
	if ($towhere == 'scout') {
		updateNewpm($touiddb,'recount');
	} else {
		updateNewpm($winduid,'recount');
	}
	if ($type) {
		$refreshto = "message.php?action=$towhere&type=$type";
	} else {
		$refreshto = "message.php?action=$towhere";
	}
	refreshto($refreshto,'operate_success');

} elseif ($action == 'down') {

	$downids = $pdownids = array();
	InitGP(array('delid','pdelid','towhere'));
	if (is_array($delid)) {
		foreach ($delid as $value) {
			is_numeric($value) && $downids[] = $value;
		}
		$downids && $downids = pwImplode($downids);
	}
	if (is_array($pdelid) && $towhere == 'receivebox') {
		foreach ($pdelid as $value) {
			is_numeric($value) && $pdownids[] = $value;
		}
		$pdownids && $pdownids = pwImplode($pdownids);
	}
	!$downids && !$pdownids && Showmsg('sel_error');

	if ($pdownids) {
		$query = $db->query("SELECT * FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.mid IN($pdownids) AND m.type='public' AND m.togroups LIKE ".pwEscape("%,$msg_gid,%"));
		while ($msginfo = $db->fetch_array($query)) {
			$msgdb[] = $msginfo;
		}
	}
	if ($downids) {
		if ($towhere == 'receivebox') {
			$sql = " AND m.type='rebox' AND m.touid=".pwEscape($winduid);
		} elseif ($towhere == 'sendbox' || $towhere == 'scout') {
			$sql = " AND m.fromuid=".pwEscape($winduid);
		} else {
			Showmsg('undefined_action');
		}
		$query = $db->query("SELECT * FROM pw_msg m LEFT JOIN pw_msgc mc USING(mid) WHERE m.mid IN($downids) $sql");
		while ($msginfo = $db->fetch_array($query)) {
			$msgdb[] = $msginfo;
		}
	}
	if ($msgdb) {
		$content = "$db_bbsname   URL:$db_bbsurl\r\n\r\n";
		$content.= $windid.' Message Download '.get_date($timestamp)."\r\n\r\n";
		foreach ($msgdb as $key=>$msginfo) {
			$content .= "================================================================================\r\n";
			$content .= "Author :\t".$msginfo['username']."\r\n";
			$content .= "Date :\t" .get_date($msginfo['mdate']). "\r\n";
			$content .= "Title :\t" .$msginfo['title']. "\r\n";
			$content .= "--------------------------------------------------------------------------------\r\n";
			$content .= "Content：\r\n ".$msginfo['content']."\r\n\r\n";
		}
		$filename = 'Message-'.$windid.'-'.get_date($timestamp,'Y-m-d').'.txt';

		ob_end_clean();
		header('Last-Modified: '.gmdate('D, d M Y H:i:s').' GMT');
		header('Pragma: no-cache');
		header('Content-Encoding: none');
		header('Content-Disposition: attachment; filename='.$filename);
		header('Content-Length: ' . strlen($content));
		header('Content-type: txt');
		echo $content;
		exit;
	} else {
		Showmsg('undefined_action');
	}
} elseif ($action == 'banned') {

	if (empty($_POST['step'])) {

		include_once(D_P.'data/bbscache/level.php');
		$userdb = $db->get_one("SELECT banpm,msggroups FROM pw_members WHERE uid=" . pwEscape($winduid));

		if ($_G['msggroup']) {
			include_once(D_P.'data/bbscache/level.php');
			$usergroup = '';
			foreach ($ltitle as $key => $value) {
				if ($key != 1 && $key != 2) {
					if ($userdb['msggroups'] && strpos($userdb['msggroups'], ','.$key.',') !== false) {
						$checked = 'checked';
					} else {
						$checked = '';
					}
					$usergroup .= "<div style=\"width:32%;float:left\"><input type=\"checkbox\" name=\"msggroups[]\" value=\"$key\" $checked>$value</div>";
				}
			}
		}
	} else {

		PostCheck();
		InitGP(array('banidinfo'), 'P');
		InitGP(array('msggroups'), 'P', 2);
		$groups = '';
		if ($_G['msggroup'] && $msggroups) {
			foreach ($msggroups as $key => $val) {
				if (is_numeric($val)) {
					$groups .= $groups ? ','.$val : $val;
				}
			}
			$groups && $groups = ",$groups,";
		}
		$pwSQL = pwSqlSingle(array(
			'banpm'		=> $banidinfo,
			'msggroups'	=> $groups
		));
		$db->update("UPDATE pw_members SET $pwSQL WHERE uid=".pwEscape($winduid));

		refreshto("message.php?action=banned",'operate_success');
	}
} elseif ($action == 'dellog') {

	PostCheck();
	InitGP(array('selid','withuid'));

	$delids = $selids = array();
	if (is_array($selid)) {
		foreach ($selid as $value) {
			is_numeric($value) && $selids[] = $value;
		}
		$selids && $selids = pwImplode($selids);
	}
	!$selids && Showmsg('sel_error');

	$newdelids = array();
	$query = $db->query("SELECT mid FROM pw_msglog WHERE mid IN($selids) AND uid=".pwEscape($winduid).' AND withuid='.pwEscape($withuid));
	while ($rt = $db->fetch_array($query)) {
		$newdelids[] = $rt['mid'];
	}
	if ($newdelids) {
		$newdelids = pwImplode($newdelids);
		require_once(R_P.'require/msg.php');
		$db->update("DELETE FROM pw_msglog WHERE mid IN ($newdelids) AND uid=".pwEscape($winduid));
		delete_msgc($newdelids);
	}

	refreshto("message.php?action=chatlog&withuid=$withuid",'operate_success');
}


require_once PrintEot('message');
footer();

function autourl($message) {
	global $db_autoimg,$db_cvtimes,$code_htm,$codeid;
	$code_htm = array();
	$codeid = 0;
	if (strpos($message,"[code]") !== false && strpos($message,"[/code]") !== false) {
		$message = preg_replace("/\[code\](.+?)\[\/code\]/eis","code_check('\\1')",$message,$db_cvtimes);
	}
	if ($db_autoimg == 1) {
		$message = preg_replace(array(
				"/(?<=[^\]a-z0-9-=\"'\\/])((https?|ftp):\/\/|www\.)([a-z0-9\/\-_+=.~!%@?#%&;:$\\│]+\.gif)/i",
				"/(?<=[^\]a-z0-9-=\"'\\/])((https?|ftp):\/\/|www\.)([a-z0-9\/\-_+=.~!%@?#%&;:$\\│]+\.jpg)/i"
			), array(
				"[img]\\1\\3[/img]",
				"[img]\\1\\3[/img]"
			), ' '.$message
		);
	}
	$message = preg_replace(
		array(
			"/(?<=[^\]a-z0-9-=\"'\\/])((https?|ftp|gopher|news|telnet|mms|rtsp):\/\/|www\.)([a-z0-9\/\-_+=.~!%@?#%&;:$\\│]+)/i",
			"/(?<=[^\]a-z0-9\/\-_.~?=:.])([_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4}))/i"
		),
		array(
			"[url]\\1\\3[/url]",
			"[email]\\0[/email]"
		),' '.$message
	);

	if ($code_htm) {
		foreach($code_htm as $key => $value){
			$message = str_replace("<\twind_phpcode_$key\t>",$value,$message);
		}
	}
	return $message;
}

function code_check($code){
	global $code_htm,$codeid;
	$codeid++;
	$code_htm[$codeid] = '[code]'.str_replace('\\"','"',$code).'[/code]';
	return "<\twind_phpcode_$codeid\t>";
}
/**
 * 统计用户短消息的数目
 *

function getusermsg($winduid) {
	global $db,$winddb,$msg_gid,$readmsg,$delmsg;

	$count = $db->get_value("SELECT COUNT(*) FROM pw_msg WHERE touid=".pwEscape($winduid)." AND ifnew='1' AND type='rebox'");

	$checkmsg = $readmsg.','.$delmsg;
	$query = $db->query("SELECT mid FROM pw_msg WHERE type='public' AND togroups LIKE ".pwEscape("%,$msg_gid,%")." AND mdate>".pwEscape($winddb['regdate'])." ORDER BY mdate DESC LIMIT 20");
	$pub_count = 0;
	while ($rt = $db->fetch_array($query)) {
		if (strpos(",$checkmsg,",",$rt[mid],") === false) {
			$pub_count++;
		}
	}
	$count = $count + $pub_count;
	$db->update("UPDATE pw_members SET newpm=".pwEscape($count)." WHERE uid=".pwEscape($winduid));

}
 */
function msgsort($msgstr) {
	if (empty($msgstr)) {
		return '';
	}
	$msgdb  = explode(',',$msgstr);
	arsort($msgdb);
	$newmsg = implode(',',$msgdb);
	return $newmsg;
}
?>