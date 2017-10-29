<?php
if (isset($_GET['ajax'])) {
	define('AJAX','1');
}
require_once('global.php');

!$windid && Showmsg('not_login');
InitGP(array('action'));

$template = 'ajax_operate';

if (empty($_POST['step']) && !defined('AJAX')) {
	require_once(R_P.'require/header.php');
	$template = 'operate';
}

if ($action == 'showping') {

	require_once(R_P.'require/msg.php');
	require_once(R_P.'require/forum.php');
	require_once(R_P.'require/bbscode.php');
	require_once R_P . 'require/pingfunc.php';
	InitGP(array('selid','pid','page'));

	if (empty($selid) && empty($pid)) {
		Showmsg('selid_illegal');
	}
	$jump_pid = $pid ? $pid : $selid[0];
	empty($selid) && $selid = array($pid);
	!is_array($selid) && $selid = array($selid);
	$pids = $atcdb = array();
	$ptpc = '';
	foreach ($selid as $key => $val) {
		if (is_numeric($val)) {
			$pids[] = $val;
		} else {
			$ptpc = 1;
		}
	}
	$pw_tmsgs = GetTtable($tid);
	$atc = $db->get_one("SELECT t.fid,t.author,t.authorid,t.postdate,t.subject,t.anonymous,t.ptable,tm.ifmark FROM pw_threads t LEFT JOIN $pw_tmsgs tm ON tm.tid=t.tid WHERE t.tid=".pwEscape($tid));
	$fid = $atc['fid'];
	$ptpc && $atcdb['tpc'] = $atc;
	$pw_posts = GetPtable($atc['ptable']);

	if ($pids) {
		$pids = pwImplode($pids);
		$query = $db->query("SELECT pid,fid,author,authorid,postdate,subject,ifmark,anonymous,content FROM $pw_posts WHERE pid IN($pids) AND tid=".pwEscape($tid));
		while ($rt = $db->fetch_array($query)) {
			if (!$rt['subject']) {
				$rt['subject'] = 'RE:'.$atc['subject'];
			}
			$atcdb[$rt['pid']] = $rt;
		}
	}
	empty($atcdb) && Showmsg('data_error');

	if (!($foruminfo = L::forum($fid))) {
		Showmsg('data_error');
	}
	(!$foruminfo || $foruminfo['type'] == 'category') && Showmsg('data_error');
	wind_forumcheck($foruminfo);

	$admincheck = admincheck($foruminfo['forumadmin'], $foruminfo['fupadmin'], $windid);

	require_once(R_P.'require/credit.php');
	$mcredit = $db->get_one('SELECT credit FROM pw_memberinfo WHERE uid='.pwEscape($winduid));
	$_G['markset'] = unserialize($_G['markset']);

	$rcreditdb = array();
	if ($mcredit['credit']) {
		$creditdb = explode("|", $mcredit['credit']);
		foreach ($creditdb as $value) {
			$creditvalue = explode("\t",$value);
			if ($creditvalue['0'] >= $tdtime) {
				$rcreditdb[$creditvalue['2']]['pingdate'] = $creditvalue['0'];
				$rcreditdb[$creditvalue['2']]['pingnum'] = $creditvalue['1'];
				$rcreditdb[$creditvalue['2']]['pingtype'] = $creditvalue['2'];
			}
		}
	}
	$markset = $credittype = array();
	foreach ($_G['markset'] as $key => $value) {
		if ($value['markctype']) {
			$markset[$key]['minper']	= $value['marklimit'][0];
			$markset[$key]['maxper']	= $value['marklimit'][1];
			$markset[$key]['maxcredit']	= $value['maxcredit'];
			$markset[$key]['markdt']	= $value['markdt'];
			if (isset($rcreditdb[$key])) {
				$markset[$key]['leavepoint'] = abs($value['maxcredit'] - $rcreditdb[$key]['pingnum']);
			} else {
				$markset[$key]['leavepoint'] = $value['maxcredit'];
			}
			$credittype[] = $key;
		}
	}

	if ($winddb['groups']) {
		$gids = array();
		foreach (explode(',',$winddb['groups']) as $key => $gid) {
			is_numeric($gid) && $gids[] = $gid;
		}
		if ($gids) {
			$gids = pwImplode($gids);
			$mright = array();
			$query = $db->query("SELECT gid,rkey,rvalue FROM pw_permission WHERE uid='0' AND fid='0' AND gid IN($gids) AND rkey IN ('markset','markable') AND type='basic'");
			while ($rt = $db->fetch_array($query)) {
				$mright[$rt['gid']][$rt['rkey']] = $rt['rvalue'];
			}
			foreach ($mright as $key => $p) {
				if (is_array($p) && $p['markable']) {
					$p['markable'] > $_G['markable'] && $_G['markable'] = $p['markable'];
					$p['markset'] = (array)unserialize($p['markset']);
					foreach ($p['markset'] as $k => $v) {
						if ($v['markctype'] && in_array($k, $credittype)) {
							is_numeric($v['marklimit'][0]) && $v['marklimit'][0] < $markset[$k]['minper'] && $markset[$k]['minper'] = $v['marklimit'][0];
							is_numeric($v['marklimit'][1]) && $v['marklimit'][1] > $markset[$k]['maxper'] && $markset[$k]['maxper'] = $v['marklimit'][1];
							is_numeric($v['maxcredit']) && $v['maxcredit'] > $markset[$k]['maxcredit'] && $markset[$k]['maxcredit'] = $v['maxcredit'];

							if (isset($rcreditdb[$k])) {
								$markset[$k]['leavepoint'] = abs($markset[$k]['maxcredit'] - $rcreditdb[$k]['pingnum']);
							} else {
								$markset[$k]['leavepoint'] = $markset[$k]['maxcredit'];
							}
							!$v['markdt'] && $markset[$k]['markdt'] = 0;//正负->负 扣除积分权限
						}
					}
				}
			}
		}
	}
	$jscredit = pwJsonEncode($markset);

	if ((!$admincheck && !$_G['markable']) || !$credittype ) {
		Showmsg('no_markright');
	}
	$anonymous = 0;
	foreach ($atcdb as $pid => $atc) {
		if ($db_pingtime && $timestamp-$atc['postdate']>$db_pingtime*3600 && $gp_gptype!='system') {
			Showmsg('pingtime_over');
		}
		if ($winduid == $atc['authorid'] && !CkInArray($windid,$manager)) {
			Showmsg('masigle_manager');
		}
		$has_ping = $db->get_one("SELECT * FROM pw_pinglog WHERE fid=".pwEscape($fid)." AND tid=".pwEscape($tid)." AND pid=" . pwEscape(intval($pid)) . " AND pinger=".pwEscape($windid)." LIMIT 1");
		if ($_POST['step'] == 1 && $_G['markable'] < 2 && $has_ping) {
			Showmsg('no_markagain');
		}
		if ($_POST['step'] > 1 && !$has_ping) {
			Showmsg('have_not_showping');
		}
		$atc['anonymous'] && $anonymous++;
	}

	$count = count($atcdb);

	if (empty($_POST['step'])) {

		$creditselect = '';
		foreach ($credittype as $key => $cid) {
			if ($markset[$cid]['minper'] && $markset[$cid]['maxper']) {
				if (isset($credit->cType[$cid])) {
					$creditselect .= '<option value="'.$cid.'">'.$credit->cType[$cid].'</option>';
				}
			}
		}

		$reason_sel = '';
		$reason_a = explode("\n",$db_adminreason);
		foreach ($reason_a as $k => $v) {
			if ($v = trim($v)) {
				$reason_sel .= "<option value=\"$v\">$v</option>";
			} else {
				$reason_sel .= "<option value=\"\">-------</option>";
			}
		}
		if ($anonymous == $count && $groupid!='3') {
			$check_Y = 'disabled';
			$check_N = 'checked';
		} else {
			$check_Y = 'checked';
			$check_N = '';
		}
		require_once PrintEot($template);footer();

	} elseif ($_POST['step'] == 1) {

		PostCheck();
		InitGP(array('cid','addpoint','ifmsg','atc_content'),'P');

		$add_c = $tmp = array();
		if (is_array($cid)) {
			foreach ($cid as $key => $value) {
				if ($value && isset($credit->cType[$value]) && is_numeric($addpoint[$key]) && $addpoint[$key] <> 0) {
					!in_array($value, $credittype) && Showmsg('masigle_credit_right');
					$tmp[$value] += intval($addpoint[$key]);
				}
			}
			foreach ($tmp as $key => $value) {
				if (!$value) continue;
				if ($value > $markset[$key]['maxper'] || $value < $markset[$key]['minper']) {
					$GLOBALS['maxper'] = $markset[$key]['maxper'];
					$GLOBALS['minper'] = $markset[$key]['minper'];
					Showmsg('masigle_creditlimit');
				}
				$add_c[$key] = $value;
			}
		}
		empty($add_c) && Showmsg('member_credit_error');
	
		if (strlen($atc_content) > 100) Showmsg('showping_content_too_long');
		
		foreach ($add_c as $key => $value) {
			$allpoint = abs($value) * $count;
			if (isset($rcreditdb[$key])) {
				if ($rcreditdb[$key]['pingnum'] + $allpoint > $markset[$key]['maxcredit']) {
					$GLOBALS['leavepoint'] = $markset[$key]['maxcredit'];
					Showmsg('masigle_point');
				}
				$rcreditdb[$key]['pingdate'] = $timestamp;
				$rcreditdb[$key]['pingnum'] += $allpoint;
			} else {
				if ($allpoint > $markset[$key]['maxcredit']) {
					$GLOBALS['leavepoint'] = $markset[$key]['maxcredit'];
					Showmsg('masigle_point');
				}
				$rcreditdb[$key] = array(
					'pingdate'	=> $timestamp,
					'pingnum'	=> $allpoint,
					'pingtype'	=> $key
				);
			}
			if ($markset[$key]['markdt'] && $allpoint > 0) {
				$credit->get($winduid, $key) < $allpoint && Showmsg('credit_enough');
				$credit->set($winduid, $key, -$allpoint, false);
			}
		}
		$newcreditdb = '';
		foreach ($rcreditdb as $value) {
			$newcreditdb .= ($newcreditdb ? '|' : '') . implode("\t",$value);
		}
		if (!$mcredit) {
			$db->update("INSERT INTO pw_memberinfo SET " .pwSqlSingle(array('uid' => $winduid, 'credit' => $newcreditdb),false));
		} else {
			$db->update('UPDATE pw_memberinfo SET credit=' . pwEscape($newcreditdb, false).' WHERE uid=' . pwEscape($winduid));
		}
		$singlepoint = array_sum($add_c);

		foreach ($atcdb as $pid => $atc) {
			!$atc['subject'] && $atc['subject'] = substrs(strip_tags(convert($atc['content'])),35);
			$credit->addLog('credit_showping', $add_c, array(
				'uid'		=> $atc['authorid'],
				'username'	=> $atc['author'],
				'ip'		=> $onlineip,
				'operator'	=> $windid,
				'tid'		=> $tid,
				'subject'	=> $atc['subject'],
				'reason'	=> $atc_content
			));
			$credit->sets($atc['authorid'], $add_c, false);

			if (!is_numeric($pid)) {
				$db->update("UPDATE pw_threads SET ifmark=ifmark+" . pwEscape($singlepoint)." WHERE tid=" . pwEscape($tid));
				$rpid = 0;
			} else {
				$rpid = $pid;
			}
			$pwSQL = array();
			$affect = '';

			foreach ($add_c as $key => $value) {
				$pwSQL[] = array(
					'fid'	=> $fid,
					'tid'	=> $tid,
					'pid'	=> $rpid,
					'name'	=> $credit->cType[$key],
					'point'	=> $value,
					'pinger'=> $windid,
					'record'=> $atc_content,
					'pingdate'=> $timestamp,
				);
				$affect .= ($affect ? ',' : '') . $credit->cType[$key] . ':' . $value;
			}
			$db->update("INSERT INTO pw_pinglog (fid,tid,pid,name,point,pinger,record,pingdate) VALUES " . pwSqlMulti($pwSQL));
			update_markinfo($fid, $tid, $rpid);
//			$_cache = getDatastore();
//			$_cache->delete('UID_'.$atc['authorid']);

			$threadobj = L::loadClass("threads");
			$threadobj->clearTmsgsByThreadId($tid);

			$atcdb[$pid]['ifmark'] = $ifmark;

			if ($ifmsg && !$atc['anonymous']) {
				$msg = array(
					'toUser'	=> $atc['author'],
					'fromUid'	=> $winduid,
					'fromUser'	=> $windid,
					'subject'	=> 'ping_title',
					'content'	=> 'ping_content',
					'other'		=> array(
						'manager'	=> $windid,
						'fid'		=> $atc['fid'],
						'tid'		=> $tid,
						'pid'		=> $pid,
						'subject'	=> $atc['subject'],
						'postdate'	=> get_date($atc['postdate']),
						'forum'		=> strip_tags($foruminfo['name']),
						'affect'    => $affect,
						'admindate'	=> get_date($timestamp),
						'reason'	=> stripslashes($atc_content)
					)
				);
				pwSendMsg($msg);
			}
			if ($gp_gptype == 'system'){
				require_once(R_P.'require/writelog.php');
				$log = array(
					'type'      => 'credit',
					'username1' => $atc['author'],
					'username2' => $windid,
					'field1'    => $fid,
					'field2'    => '',
					'field3'    => '',
					'descrip'   => 'credit_descrip',
					'timestamp' => $timestamp,
					'ip'        => $onlineip,
					'tid'		=> $tid,
					'forum'		=> strip_tags($foruminfo['name']),
					'subject'	=> $atc['subject'],
					'affect'	=> $affect,
					'reason'	=> $atc_content
				);
				writelog($log);
			}
		}
		$credit->runsql();

		if ($db_autoban && $singlepoint < 0) {
			require_once(R_P.'require/autoban.php');
			foreach ($atcdb as $pid => $atc) {
				autoban($atc['authorid']);
			}
		}
		if ($foruminfo['allowhtm'] && $page==1) {
			$StaticPage = L::loadClass('StaticPage');
			$StaticPage->update($tid);
		}
		if (defined('AJAX')) {
			echo "success";
			ajax_footer();
		} else {
			refreshto("read.php?tid=$tid&page=$page#$jump_pid",'operate_success');
		}
	} else {

		PostCheck();
		$groupid == 'guest' && Showmsg('not_login');
		InitGP(array('ifmsg','atc_content'));

		foreach ($atcdb as $pid => $atc) {
			$rpid = $pid == 'tpc' ? '0' : $pid; // delete pinglog
			$pingdata = $db->get_one('SELECT * FROM pw_pinglog WHERE fid='.pwEscape($fid).' AND tid='.pwEscape($tid).' AND pid='.pwEscape($rpid).' AND pinger='.pwEscape($windid).' ORDER BY pingdate DESC LIMIT 1');
			$db->update('DELETE FROM pw_pinglog WHERE fid='.pwEscape($fid).' AND tid='.pwEscape($tid).' AND pid='.pwEscape($rpid).' AND pinger='.pwEscape($windid).' ORDER BY pingdate DESC LIMIT 1');
			update_markinfo($fid, $tid, $rpid);
//			$_cache = getDatastore();
//			$_cache->delete('UID_'.$atc['authorid']);

			$threadobj = L::loadClass("threads");
			$threadobj->clearTmsgsByThreadId($tid);

			$cName = $pingdata['name'];
			$addpoint = $pingdata['point'];
			foreach ($credit->cType as $k => $v) {
				if ($v == $cName) {
					$cid = $k;break;
				}
			}

			!$atc['subject'] && $atc['subject'] = substrs(strip_tags(convert($atc['content'])),35);
			$addpoint = $addpoint>0 ? -$addpoint : abs($addpoint);

			$credit->addLog('credit_delping',array($cid => $addpoint),array(
				'uid'		=> $atc['authorid'],
				'username'	=> $atc['author'],
				'ip'		=> $onlineip,
				'operator'	=> $windid,
				'tid'		=> $tid,
				'subject'	=> $atc['subject'],
				'reason'	=> $atc_content
			));
			$credit->set($atc['authorid'],$cid,$addpoint);

			if (!is_numeric($pid)) {
				$db->update('UPDATE pw_threads SET ifmark=ifmark+'.pwEscape($addpoint).' WHERE tid='.pwEscape($tid));
			}

			if ($ifmsg) {
				$msg = array(
					'toUser'	=> $atc['author'],
					'fromUid'	=> $winduid,
					'fromUser'	=> $windid,
					'subject'	=> 'delping_title',
					'content'	=> 'delping_content',
					'other'		=> array(
						'manager'	=> $windid,
						'fid'		=> $atc['fid'],
						'tid'		=> $tid,
						'pid'		=> $pid,
						'subject'	=> $atc['subject'],
						'postdate'	=> get_date($atc['postdate']),
						'forum'		=> strip_tags($foruminfo['name']),
						'affect'    => "{$cName}:$addpoint",
						'admindate'	=> get_date($timestamp),
						'reason'	=> stripslashes($atc_content)
					)
				);
				pwSendMsg($msg);
			}
			if ($gp_gptype == 'system'){
				require_once(R_P.'require/writelog.php');
				$log = array(
					'type'      => 'credit',
					'username1' => $atc['author'],
					'username2' => $windid,
					'field1'    => $atc['fid'],
					'field2'    => '',
					'field3'    => '',
					'descrip'   => 'creditdel_descrip',
					'timestamp' => $timestamp,
					'ip'        => $onlineip,
					'tid'		=> $tid,
					'forum'		=> strip_tags($foruminfo['name']),
					'subject'	=> $atc['subject'],
					'affect'	=> "$name:$addpoint",
					'reason'	=> $atc_content
				);
				writelog($log);
			}
		}

		if ($foruminfo['allowhtm'] && $page==1) {
			$StaticPage = L::loadClass('StaticPage');
			$StaticPage->update($tid);
		}
		if (defined('AJAX')) {
			echo "success";
			ajax_footer();
		} else {
			refreshto("read.php?tid=$tid&page=$page#$jump_pid",'operate_success');
		}
	}

} elseif ($action == 'share') {

	if (empty($_POST['step'])) {//TODO X!

		InitGP(array('type','id'));
		@include_once(D_P.'data/bbscache/o_config.php');

		$atc_name = getLangInfo('other','share_'.$type);
		$friend = getFriends($winduid) ? getFriends($winduid) : array();
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
		$share_type_des = getLangInfo('other','share_des_'.$type);
		if ($type == 'diary') {
			$diarydb = $db->get_one("SELECT did,uid,username,subject,content,privacy FROM pw_diary WHERE did=".pwEscape($id));
			if ($diarydb['privacy'] == 2) {
				showmsg('share_privacy');
			}
			$did = $diarydb['did'];
			$subject = $diarydb['subject'];
			$uid = $diarydb['uid'];
			$username = $diarydb['username'];
			$title = "[url=$db_bbsurl/mode.php?m=o&q=diary&space=1&u=$uid&did=$did] $subject [/url]";
			require_once(R_P.'require/bbscode.php');
			$diarydb['content'] = strip_tags(convert($diarydb['content'],$db_windpost));
			$descrip = substrs($diarydb['content'],50);
		} elseif ($type == 'group') {
			$colonydb = $db->get_one("SELECT c.id,c.cname,c.admin,c.descrip,m.uid FROM pw_colonys c LEFT JOIN pw_members m ON c.admin=m.username WHERE c.id=".pwEscape($id));
			$subject = $diarydb['cname'];
			$uid = $colonydb['uid'];
			$username = $colonydb['admin'];
			$cyid = $colonydb['id'];
			$title = "[url=$db_bbsurl/mode.php?m=o&q=group&cyid=$cyid] $colonydb[cname] [/url]";
			$descrip = $colonydb['descrip'];
		} elseif ($type == 'album') {
			$albumdb = $db->get_one("SELECT aid,ownerid,owner,lastphoto,aintro FROM pw_cnalbum WHERE atype='0' AND aid=" . pwEscape($id));
			$photourl	= getphotourl($albumdb['lastphoto']);
			$uid = $albumdb['ownerid'];
			$username = $albumdb['owner'];
			$aid =  $albumdb['aid'];
			$title = "[url=$db_bbsurl/mode.php?m=o&q=photos&space=1&a=album&u=$albumdb[ownerid]&aid=$aid] $username [/url]";
			$descrip = $albumdb['aintro'];
		} elseif ($type == 'photo') {
			$photodb = $db->get_one("SELECT p.pid,p.path as basepath,p.pintro,p.ifthumb,a.ownerid,a.owner FROM pw_cnphoto p LEFT JOIN pw_cnalbum a ON p.aid=a.aid WHERE p.pid=" . pwEscape($id) . " AND a.atype='0'");
			$photourl = getphotourl($photodb['basepath'],$photodb['ifthumb']);
			$uid = $photodb['ownerid'];
			$username = $photodb['owner'];
			$title = "[url=$db_bbsurl/mode.php?m=o&q=photos&space=1&a=view&u=$photodb[ownerid]&pid=$photodb[pid]] $photodb[owner] [/url]";
			$descrip = $photodb['pintro'];
		} elseif ($type == 'user') {
			$userdb = $db->get_one("SELECT uid, username FROM pw_members WHERE uid=".pwEscape($id));
			$uid = $userdb['uid'];
			$username = $userdb['username'];
			$title = "[url=$db_bbsurl/u.php?action=show&uid=$userdb[uid]] $userdb[username] [/url]";
		} elseif ($type == 'topic') {
			$pw_tmsgs = GetTtable($id);
			$topicdb = $db->get_one("SELECT t.tid,t.subject,t.anonymous,t.ifshield,t.authorid,t.author,t.postdate,tm.content FROM pw_threads t LEFT JOIN $pw_tmsgs tm ON t.tid=tm.tid WHERE t.tid=".pwEscape($id));
			if($topicdb['ifshield'] == 1){
				echo getLangInfo('other','share_shield_tpc',$log);ajax_footer();
			}
			$tid = $topicdb['tid'];
			$uid = $topicdb['authorid'];
			$username = ($topicdb['anonymous'] == 1) ? $db_anonymousname : $topicdb['author'];
            $isAnonymous = ($topicdb['anonymous'] == 1) ? true : false;
			$subject = $topicdb['subject'];
			$postdate = get_date($topicdb['postdate']);
			$title = "[url=$db_bbsurl/read.php?tid=$tid]$topicdb[subject][/url]";
			require_once(R_P.'require/bbscode.php');
			$topicdb['content'] = strip_tags(convert($topicdb['content'],$db_windpost));
			$descrip = ($topicdb['ifshield'] == 1) ? "" : stripWindCode(substrs($topicdb['content'],100,'N'));
			$attimages = array();
			$query = $db->query("SELECT attachurl,ifthumb FROM pw_attachs WHERE tid=".pwEscape($topicdb['tid'],false)." AND type='img' LIMIT 4");
			while ($rt = $db->fetch_array($query)) {
				$a_url = geturl($rt['attachurl'],'show',$rt['ifthumb']);
				if ($a_url != 'nopic') {
					$attimages[$rt['attachurl']] = is_array($a_url) ? $a_url[0] : $a_url;
				}
			}


		} elseif ($type == 'reply') {

			InitGP(array('tid'));
			$pw_posts = GetPtable('N',$tid);

			$replydb = $db->get_one("SELECT p.pid,p.tid,p.anonymous,p.ifshield,p.subject as psubject,p.author,p.authorid,p.postdate,p.content,t.subject as tsubject FROM $pw_posts p LEFT JOIN pw_threads t ON p.tid=t.tid WHERE p.pid=".pwEscape($id));

			$uid = $replydb['authorid'];
			$subject = $replydb['psubject'] ? $replydb['psubject'] : 'Re:'.$replydb['tsubject'];
			$username = ($replydb['anonymous'] == 1) ? $db_anonymousname : $replydb['author'];
			$isAnonymous = ($replydb['anonymous'] == 1) ? true : false;
			$postdate = get_date($replydb['postdate']);
			require_once(R_P.'require/bbscode.php');
			$replydb['content'] = strip_tags(convert($replydb['content'],$db_windpost));
			$descrip = ($replydb['ifshield'] == 1) ? "" : stripWindCode(substrs($replydb['content'],100,'N'));

			$attimages = array();
			$query = $db->query("SELECT attachurl FROM pw_attachs WHERE uid=".pwEscape($uid,false)." AND pid=".pwEscape($id,false)." AND type='img' LIMIT 5");
			while ($rt = $db->fetch_array($query)) {
				$a_url = geturl($rt['attachurl'],'show');
				if ($a_url != 'nopic') {
					$attimages[$rt['attachurl']] = is_array($a_url) ? $a_url[0] : $a_url;
				}
			}
			//推荐
			$title = "[url=$db_bbsurl/job.php?action=topost&tid=$tid&pid=$id]{$subject}[/url]";
		}
		require_once PrintEot('ajax_operate');ajax_footer();

	} else {

		InitGP(array('sendtoname','subject','atc_content','touid'),'P');

		require_once(R_P.'require/postfunc.php');
		require_once(R_P.'require/bbscode.php');

		$wordsfb = L::loadClass('FilterUtil');
		if (($banword = $wordsfb->comprise($subject)) !== false) {
			Showmsg('title_wordsfb');
		}
		if (($banword = $wordsfb->comprise($atc_content)) !== false) {
			Showmsg('content_wordsfb');
		}
		require_once(R_P.'require/msg.php');

		$uids = array();
		if ($sendtoname) {
			$rt = $db->get_one('SELECT uid FROM pw_members WHERE username='.pwEscape($sendtoname));
			if (!$rt) {
				$errorname = $sendtoname;
				Showmsg('user_not_exists');
			}
			$uids[] = $rt['uid'];
		}
		if (is_array($touid)) {
			foreach ($touid as $key => $value) {
				if (is_numeric($value)) {
					$uids[] = $value;
				}
			}
		}

		!$uids && Showmsg('msg_empty');
		if (!$subject || !$atc_content) {
			Showmsg('tofriend_msgerror');
		}

		$uids = pwImplode($uids);

		$query = $db->query("SELECT username FROM pw_members WHERE uid IN($uids)");
		while ($rt = $db->fetch_array($query)) {
			$msgdb = array(
				'toUser'	=> $rt['username'],
				'fromUid'	=> $winduid,
				'fromUser'	=> $windid,
				'subject'	=> stripslashes($subject),
				'content'	=> stripslashes($atc_content),
			);
			pwSendMsg($msgdb);
		}
		Showmsg('operate_success');
	}
} elseif ($action == 'report') {

	!$_G['allowreport'] && Showmsg('report_right');
	InitGP(array('pid','page'),'GP',2);
	$rt  = $db->get_one("SELECT tid FROM pw_report WHERE uid=".pwEscape($winduid).' AND tid='.pwEscape($tid).' AND pid='.pwEscape($pid));
	$rt && Showmsg('have_report');

	if (empty($_POST['step'])) {

		require_once PrintEot($template);footer();

	} else {

		PostCheck();
		InitGP(array('ifmsg','type','reason'),'P');

		$pwSQL = pwSqlSingle(array(
			'tid'	=> $tid,
			'pid'	=> $pid,
			'uid'	=> $winduid,
			'type'	=> $type,
			'reason'=> $reason
		));
		$db->update("INSERT INTO pw_report SET $pwSQL");

		if ($ifmsg) {
			if ($pid > 0) {
				$pw_posts = GetPtable('N',$tid);
				$sqlsel = "t.content as subject,t.postdate,";
				$sqltab = "$pw_posts t";
				$sqladd = 'WHERE t.pid='.pwEscape($pid);
			} else {
				$sqlsel = "t.subject,t.postdate,";
				$sqltab = "pw_threads t";
				$sqladd = 'WHERE t.tid='.pwEscape($tid);
			}
			$rs = $db->get_one("SELECT $sqlsel t.fid,f.forumadmin FROM $sqltab LEFT JOIN pw_forums f USING(fid) $sqladd");

			if ($rs['forumadmin']) {
				include_once(D_P.'data/bbscache/forum_cache.php');
				require_once(R_P.'require/msg.php');
				$admin_a = explode(',',$rs['forumadmin']);
				$iftpc = $pid ? '0' : '1';
				$msg = array(
					'toUser'	=> $admin_a,
					'fromUid'	=> $winduid,
					'fromUser'	=> $windid,
					'subject'	=> 'report_title',
					'content'	=> 'report_content_'.$type.'_'.$iftpc,
					'other'		=> array(
						'fid'		=> $rs['fid'],
						'tid'		=> $tid,
						'pid'		=> $pid,
						'postdate'	=> get_date($rs['postdate']),
						'forum'		=> $forum[$rs['fid']]['name'],
						'subject'	=> $rs['subject'],
						'admindate'	=> get_date($timestamp),
						'reason'	=> stripslashes($reason)
					)
				);
				pwSendMsg($msg);
			}
		}
		if (defined('AJAX')) {
			Showmsg('report_success');
		} else {
			refreshto("read.php?tid=$tid&page=$page",'report_success');
		}
	}
} else {
	Showmsg('undefined_action');
}

function getphotourl($path,$thumb = false) {
	global $pwModeImg;
	if (!$path) {
		return "$pwModeImg/nophoto.gif";
	}
	$lastpos = strrpos($path,'/') + 1;
	$thumb && $path = substr($path, 0, $lastpos) . 's_' . substr($path, $lastpos);
	list($path) = geturl($path, 'show');
	if ($path == 'imgurl' || $path == 'nopic') {
		return "$pwModeImg/nophoto.gif";
	}
	return $path;
}
?>