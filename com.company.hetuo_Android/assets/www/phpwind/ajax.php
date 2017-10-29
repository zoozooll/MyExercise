<?php
define('AJAX','1');
require_once('global.php');
require_once(R_P.'lib/forum.class.php');
require_once(R_P.'lib/post.class.php');

$groupid == 'guest' && Showmsg('not_login');
empty($fid) && Showmsg('undefined_action');

$pwforum = new PwForum($fid);
$pwpost  = new PwPost($pwforum);
$pwpost->forumcheck();
$pwpost->postcheck();

list($uploadcredit,$uploadmoney,$downloadmoney,$downloadimg) = explode("\t", $pwforum->forumset['uploadset']);

if ($groupid == 6 || getstatus($winddb['userstatus'],1)) {
	$pwSQL = '';
	$flag  = 0;
	$bandb = $delban = array();
	$query = $db->query("SELECT * FROM pw_banuser WHERE uid=".pwEscape($winduid));
	while ($rt = $db->fetch_array($query)) {
		if ($rt['type'] == 1 && $timestamp - $rt['startdate'] > $rt['days']*86400) {
			$delban[] = $rt['id'];
		} elseif ($rt['fid'] == 0 || $rt['fid'] == $fid) {
			$bandb[$rt['fid']] = $rt;
		} else {
			$flag = 1;
		}
	}
	$delban && $db->update('DELETE FROM pw_banuser WHERE id IN('.pwImplode($delban).')');
	($groupid == 6 && !isset($bandb[0])) && $pwSQL .= "groupid='-1',";
	if (getstatus($winddb['userstatus'],1) && !isset($bandb[$fid]) && !$flag) {
		$pwSQL .= 'userstatus=userstatus&(~1),';
	}
	if ($pwSQL = rtrim($pwSQL,',')) {
		$db->update('UPDATE pw_members SET $pwSQL WHERE uid='.pwEscape($winduid));
		$_cache = getDatastore();
		$_cache->delete('UID_'.$winduid);
	}
	if ($bandb) {
		$bandb = current($bandb);
		if ($bandb['type'] == 1) {
			$s_date = get_date($bandb['startdate']);
			$e_date = $bandb['startdate'] + $bandb['days']*86400;
			$e_date = get_date($e_date);
			Showmsg('ban_info1');
		} else {
			if ($bandb['type'] == 3) {
				Cookie('force',$winduid);
				Showmsg('ban_info3');
			} else {
				Showmsg('ban_info2');
			}
		}
	}
}
if (GetCookie('force') && $winduid != GetCookie('force')) {
	$force = GetCookie('force');
	$bandb = $db->get_one('SELECT type FROM pw_banuser WHERE uid='.pwEscape($force).' AND fid=0');
	if ($bandb['type'] == 3) {
		Showmsg('ban_info3');
	} else {
		Cookie('force','',0);
	}
}
$userlastptime = $groupid != 'guest' ?  $winddb['lastpost'] : GetCookie('userlastptime');
$tdtime  >= $winddb['lastpost'] && $winddb['todaypost'] = 0;
$montime >= $winddb['lastpost'] && $winddb['monthpost'] = 0;

if ($_G['postlimit'] && $winddb['todaypost'] >= $_G['postlimit']) {
	Showmsg('post_gp_limit');
}
list(,,$postq)	= explode("\t",$db_qcheck);
InitGP(array('action'));

if ($action == 'modify') {

	InitGP(array('pid','article'));

	require_once(R_P . 'lib/postmodify.class.php');
	if ($pid && is_numeric($pid)) {
		$postmodify = new replyModify($tid, $pid, $pwpost);
	} else {
		$postmodify = new topicModify($tid, 0, $pwpost);
	}
	$atcdb = $postmodify->init();

	if (empty($atcdb) || $atcdb['fid'] != $fid) {
		Showmsg('illegal_tid');
	}
	if (!$pwpost->isGM && !pwRights($pwpost->isBM, 'deltpcs')) {
		if ($groupid == 'guest' || $atcdb['authorid'] != $winduid) {
			Showmsg('modify_noper');
		} elseif ($atcdb['locked']%3 > 0) {
			Showmsg('modify_locked');
		}
	}
	if ($winduid != $atcdb['authorid'] && $groupid != 3 && $groupid != 4) {
		$authordb = $db->get_one('SELECT groupid FROM pw_members WHERE uid='.pwEscape($atcdb['authorid']));
		if (($authordb['groupid'] == 3 || $authordb['groupid'] == 4)) {
			Showmsg('modify_admin');
		}
	}
	if ($_G['edittime'] && ($timestamp - $atcdb['postdate']) > $_G['edittime'] * 60) {
		Showmsg('modify_timelimit');
	}
	if (empty($_POST['step'])) {

		$atcdb['anonymous'] && $atcdb['author'] = $db_anonymousname;
		$atc_content = str_replace(array('<','>','&nbsp;'),array('&lt;','&gt;',' '),$atcdb['content']);
		if (strpos($atc_content,$db_bbsurl) !== false) {
			$atc_content = str_replace('p_w_picpath',$db_picpath,$atc_content);
			$atc_content = str_replace('p_w_upload',$db_attachname,$atc_content);
		}
		$atc_title = $atcdb['subject'];
		require_once PrintEot('ajax');ajax_footer();

	} else {

		PostCheck(1,($db_gdcheck & 4) && $winddb['postnum'] < $db_postgd,$winddb['postnum'] < $postq);

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
		$postdata->setIfcheck();
		$postmodify->execute($postdata);

		extract(L::style());

		$aids = array();
		if ($atcdb['attachs']) {
			$aids = attachment($atc_content);
		}
		$leaveword = $atcdb['leaveword'] ? leaveword($atcdb['leaveword']) : '';
		$content   = convert($postdata->data['content'] . $leaveword, $db_windpost);

		if (strpos($content,'[p:') !== false || strpos($content,'[s:') !== false) {
			$content = showface($content);
		}
		if ($atcdb['ifsign'] < 2) {
			$content = str_replace("\n",'<br />',$content);
		}
		if ($postdata->data['ifwordsfb'] == 0) {
			$content = addslashes(wordsConvert(stripslashes($content)));
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
/*		if (!$postdata->getIfcheck()) {
			Showmsg('post_verify');
		}*/
		if (!$postdata->getIfcheck()) {
			if ($postdata->filter->filter_weight == 2) {
				$banword = implode(',',$postdata->filter->filter_word);
				$pinfo = 'post_word_check';
			} elseif ($postdata->linkCheckStrategy) {
				$pinfo = 'post_link_check';
			} else {
				$pinfo = 'post_check';
			}
			Showmsg($pinfo);
		}
		echo "success\t".stripslashes($atcdb['icon']."&nbsp;".$atc_title)."\t".str_replace(array("\r","\t"), array("",""), stripslashes($content));
		ajax_footer();
	}
} elseif ($action == 'quote') {

	if (!$pwpost->admincheck && !$pwforum->allowreply($pwpost->user, $pwpost->groupid)) {
		Showmsg('reply_forum_right');
	}
	if (!$pwforum->foruminfo['allowrp'] && !$pwpost->admincheck && $_G['allowrp'] == 0) {
		Showmsg('reply_group_right');
	}
	InitGP(array('pid','article','page'));

	if ($article == '0') {
		$pw_tmsgs = GetTtable($tid);
		$S_sql = ',tm.ifsign,tm.content,m.uid,m.groupid,m.userstatus';
		$J_sql = "LEFT JOIN $pw_tmsgs tm ON tm.tid=t.tid LEFT JOIN pw_members m ON t.authorid=m.uid";
	} else {
		$S_sql = $J_sql = '';
	}
	$tpcarray = $db->get_one("SELECT t.fid,t.locked,t.ifcheck,t.author,t.authorid,t.subject,t.postdate,t.ifshield,t.anonymous,t.ptable $S_sql FROM pw_threads t $J_sql WHERE t.tid=".pwEscape($tid));
	$pw_posts = GetPtable($tpcarray['ptable']);

	if ($tpcarray['fid'] != $fid) {
		Showmsg('illegal_tid');
	}
	if ($pwforum->forumset['lock'] && !$pwpost->isGM && $timestamp - $tpcarray['postdate'] > $pwforum->forumset['lock'] * 86400 && !pwRights($pwpost->isBM,'replylock')) {
		Showmsg('forum_locked');
	}
	if (!$pwpost->isGM && !$pwpost->isBM && !$tpcarray['ifcheck']) {
		Showmsg('reply_ifcheck');
	}
	if (!$pwpost->isGM && $tpcarray['locked']%3<>0 && !pwRights($pwpost->isBM,'replylock')) {
		Showmsg('reply_lockatc');
	}

	require_once(R_P.'require/bbscode.php');
	if ($article == '0') {
		$atcarray = $tpcarray;
	} else {
		!is_numeric($pid) && Showmsg('illegal_tid');
		$atcarray = $db->get_one("SELECT p.author,p.subject,p.postdate,p.content,p.ifshield,p.anonymous,m.uid,m.groupid,m.userstatus FROM $pw_posts p LEFT JOIN pw_members m ON p.authorid=m.uid WHERE p.pid=".pwEscape($pid));
	}
	if ($atcarray['ifshield'] == '1') {
		$atcarray['content'] = shield('shield_article');
	} elseif ($atcarray['ifshield'] == '2') {
		$atcarray['content'] = shield('shield_del_article');
	} elseif ($pwforum->forumBan($atcarray)) {
		$atcarray['content'] = shield('ban_article');
	}
	$old_author = $atcarray['anonymous'] ? $db_anonymousname : $atcarray['author'];
	$replytitle = $atcarray['subject'];
	$wtof_oldfile = get_date($atcarray['postdate']);
	$old_content = $atcarray['content'];
	$old_content = preg_replace("/\[hide=(.+?)\](.+?)\[\/hide\]/is",getLangInfo('post','hide_post'),$old_content);
	$old_content = preg_replace("/\[post\](.+?)\[\/post\]/is",getLangInfo('post','post_post'),$old_content);
	$old_content = preg_replace("/\[sell=(.+?)\](.+?)\[\/sell\]/is",getLangInfo('post','sell_post'),$old_content);
	$old_content = preg_replace("/\[quote\](.*)\[\/quote\]/is","",$old_content);

	$bit_content = explode("\n",$old_content);
	if (count($bit_content) > 5) {
		$old_content = "$bit_content[0]\n$bit_content[1]\n$bit_content[2]\n$bit_content[3]\n$bit_content[4]\n.......";
	}
	if (strpos($old_content,$db_bbsurl) !== false) {
		$old_content = str_replace('p_w_picpath',$db_picpath,$old_content);
		$old_content = str_replace('p_w_upload',$db_attachname,$old_content);
	}
	$old_content = preg_replace("/\<(.+?)\>/is","",$old_content);
	$atc_content = "[quote]".($article==0 ? getLangInfo('post','info_post_1') : getLangInfo('post','info_post_2'))."\n{$old_content} [url={$db_bbsurl}/job.php?action=topost&tid=$tid&pid=$pid][img]{$imgpath}/back.gif[/img][/url]\n[/quote]\n";

	$replytitle =='' ? $atc_title = 'Re:'.$tpcarray['subject'] : $atc_title = 'Re:'.$replytitle;
	require_once PrintEot('ajax');ajax_footer();

} elseif ($action == 'subject') {

	(!$pwpost->isGM && !pwRights($pwpost->isBM, 'deltpcs')) && Showmsg('undefined_action');
	$atcdb = $db->get_one('SELECT authorid,subject FROM pw_threads WHERE tid=' . pwEscape($tid));
	empty($atcdb) && Showmsg('illegal_tid');
	if ($winduid != $atcdb['authorid'] && $groupid != 3 && $groupid != 4) {
		$authordb = $db->get_one('SELECT groupid FROM pw_members WHERE uid='.pwEscape($atcdb['authorid']));
		if (($authordb['groupid'] == 3 || $authordb['groupid'] == 4)) {
			Showmsg('modify_admin');
		}
	}
	if (empty($_POST['step'])) {

		$atcdb['subject'] = str_replace(array("&lt;","&gt;","\t"),array('<','>',''),$atcdb['subject']);
		echo "success\t".$atcdb['subject'];
		ajax_footer();

	} else {

		PostCheck();
		InitGP(array('atc_content'),'P');
		$atc_content = html_entity_decode(urldecode($atc_content));
		!$atc_content && Showmsg('content_empty');
		if (!$atc_content || strlen($atc_content) > $db_titlemax) {
			Showmsg('postfunc_subject_limit');
		}
		$wordsfb = L::loadClass('FilterUtil');
		if (($banword = $wordsfb->comprise($atc_content)) !== false) {
			Showmsg('title_wordsfb');
		}

		$db->update('UPDATE pw_threads SET subject=' . pwEscape($atc_content) . ' WHERE tid=' . pwEscape($tid));

		//临时修改，待改进
		$threads = L::loadClass('Threads');
		$threads->delThreads($tid);
		$rt = $db->get_one('SELECT titlefont FROM pw_threads WHERE tid='.pwEscape($tid));
		if ($rt['titlefont']) {
			$detail = explode("~",$rt['titlefont']);
			$detail[0] && $atc_content = "<font color=$detail[0]>$atc_content</font>";
			$detail[1] && $atc_content = "<b>$atc_content</b>";
			$detail[2] && $atc_content = "<i>$atc_content</i>";
			$detail[3] && $atc_content = "<u>$atc_content</u>";
		}
		echo "success\t".str_replace("\t","",stripslashes($atc_content));ajax_footer();
	}
}
?>