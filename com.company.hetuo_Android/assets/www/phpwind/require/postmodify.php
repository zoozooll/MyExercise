<?php
!function_exists('readover') && exit('Forbidden');
//require_once(R_P.'require/updateforum.php');

//主题分类
//$t_typedb = $t_subtypedb = array();
$t_per = 0;
//$t_exits = 0;$t_sub_exits = 0;
$t_db = (array)$foruminfo['topictype'];
$tdbJson = array();
if ($t_db) {
	foreach ($t_db as $key => $value) {
		$tdbJson[$value['id']]['name'] = strip_tags($value['name']);
		$tdbJson[$value['id']]['upid'] = $value['upid'];
		if ($value['upid'] != 0) {
			$tdbJson[$value['upid']]['sub'][] = $value['id'];
		}
	}
}
$tdbJson = pwJsonEncode($tdbJson);
/*
if ($t_db) {
	foreach ($t_db as $value) {
		if ($value['upid'] == 0) {
			$t_typedb[$value['id']] = $value;
		} else {
			$t_subtypedb[$value['upid']][$value['id']] = strip_tags($value['name']);
		}
		$t_exits = 1;
	}
}
if ($t_subtypedb) {
	$t_subtypedb = pwJsonEncode($t_subtypedb);
	$t_sub_exits = 1;
}
*/
$t_per = $pwforum->foruminfo['t_type'];

$db_forcetype = $t_db && $t_per=='2' && $article==0 && !$pwpost->admincheck ? 1 : 0; // 是否需要强制主题分类

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
if ($postmodify->type == 'topic') {
	$ifmailck = $atcdb['ifmail'] > 1 ? 'checked' : '';
	list($magicid,$magicname) = explode("\t", $atcdb['magic']);
	$type	 = $atcdb['type'];
	$special = $atcdb['special'];
	$modelid = $atcdb['modelid'];
	$pcid = $atcdb['special'] > 20 ? $atcdb['special'] - 20 : 0;
	$isCheck_hiddenPost = ($atcdb['shares']) ? "checked" : "";
	$isCheck_anonymous = ($atcdb['anonymous']) ? "checked" : "";
} else {
	$special = $modelid = $pcid = 0;
}

if ($pcid > 0 || $modelid > 0) {
	$db_forcetype = 0;
}

if ($modelid) {//分类主题
	require_once(R_P.'lib/posttopic.class.php');
	$postTopic = new postTopic($pwpost);
	if ($postTopic) {
		$postTopic->postCheck();
	}
	$topichtml = $postTopic->getTopicHtml($modelid);
}

if ($pcid > 0) {//团购活动
	require_once(R_P.'lib/postcate.class.php');
	$postCate = new postCate($pwpost);
	if ($postCate) {
		$postCate->postCheck();
	}
	$topichtml = $postCate->getCateHtml($pcid);
}
$page = floor($article/$db_readperpage) + 1;

if (!$pwpost->isGM && !pwRights($pwpost->isBM, 'deltpcs')) {
	if ($groupid == 'guest' || $atcdb['authorid'] != $winduid) {
		Showmsg('modify_noper');
	} elseif ($atcdb['locked'] % 3 > 0) {
		Showmsg('modify_locked');
	}
}
if ($winduid != $atcdb['authorid'] && $groupid != 3 && $groupid != 4) {
	$authordb = $db->get_one("SELECT groupid FROM pw_members WHERE uid=" . pwEscape($atcdb['authorid']));
	if (($authordb['groupid'] == 3 || $authordb['groupid'] == 4)) {
		Showmsg('modify_admin');
	}
}
if ($_G['edittime'] && ($timestamp - $atcdb['postdate']) > $_G['edittime'] * 60) {
	Showmsg('modify_timelimit');
}

$hideemail = 'disabled';
$icon = (int)$icon;

if (empty($_POST['step'])) {

	$attach = '';
	if ($atcdb['attachs']) {
		foreach ($atcdb['attachs'] as $key => $value) {
			list($value['attachurl'],) = geturl($value['attachurl'],'lf');
			$attach .= "'$key' : ['$value[name]', '$value[size]', '$value[attachurl]', '$value[type]', '$value[special]', '$value[needrvrc]', '$value[ctype]', '$value[descrip]'],";
		}
		$attach = rtrim($attach,',');
	}
	if ($postmodify->type == 'topic') {
		if ($pwforum->foruminfo['cms']) {
			include_once(R_P.'require/c_search.php');
			list($tids,$kname) = search_tid($tid);
		}
		if ($t_db[$type]['upid']) {
			$ptype = $t_db[$type]['upid'];
			$psubtype = $type;
		} else {
			$ptype = $type;
		}
		if ($special && file_exists(R_P . "lib/special/post_{$special}.class.php")) {
			require_once Pcv(R_P . "lib/special/post_{$special}.class.php");
			$postSpecial = new postSpecial($pwpost);
			$set = $postSpecial->resetInfo($tid, $atcdb);
		}
		list($tags) = explode("\t", $atcdb['tags']);
	}
	//empty($subject) && $subject = ' ';

	$htmcheck = $atcdb['ifsign'] < 2 ? '' : 'checked';
	//$htmlpost = !$htmlpost &&  ? 'checked' : ''; //TODO 回复隐藏
	!$ifanonymous && $atcdb['anonymous'] && $ifanonymous = 'checked';
	!$htmlatt && $atcdb['ifhide'] && $htmlatt = 'checked';
	$atc_title = $atcdb['subject'];
	$replayorder = bindec(getstatus($atcdb['tpcstatus'],4).getstatus($atcdb['tpcstatus'],3));
	$replayorder_asc = $replayorder_desc = $replayorder_default = '';
	if ($replayorder == '1') {
		$replayorder_asc = 'checked';
	}else if($replayorder == '2') {
		$replayorder_desc = 'checked';
	}else{
		$replayorder_default = 'checked';
	}
	empty($atc_title) && $atc_title = ' ';
	$atc_content = str_replace(array('<','>'),array('&lt;','&gt;'), $atcdb['content']);

	if (strpos($atc_content,$db_bbsurl) !== false) {
		$atc_content = str_replace('p_w_picpath',$db_picpath,$atc_content);
		$atc_content = str_replace('p_w_upload',$db_attachname,$atc_content);
	}
	list($guidename, $forumtitle) = $pwforum->getTitle();
	$guide_subject = $atcdb['subject'] ? $atcdb['subject'] : $atcdb['tsubject'];
	if (trim($guide_subject)) {
		$guidename .= " &raquo; <a href=\"read.php?tid=$tid\">$guide_subject</a>";
	}
	$db_metakeyword = str_replace(array('|',' - '),',',$forumtitle).'phpwind';
	$db_metadescrip = substrs(strip_tags(str_replace('"','&quot;',$atc_content)),50);

	require_once(R_P.'require/header.php');
	$msg_guide = $pwforum->headguide($guidename);
	require_once PrintEot('post');footer();

} elseif ($_POST['step'] == 1) {

	if (!$pwpost->isGM) {
		if ($winduid != $atcdb['authorid'] && !pwRights($pwpost->isBM,'modother')) {
			Showmsg('modify_del_right');
		} elseif ($_G['allowdelatc'] == 0) {
			Showmsg('modify_group_right');
		}
	}
	$pw_posts = GetPtable('N', $tid);
	$rt = $db->get_one("SELECT COUNT(*) AS count FROM $pw_posts WHERE tid=".pwEscape($tid)." AND ifcheck='1'");
	$count = $rt['count'] + 1;
	if ($article == 0 && !$admincheck && $count > 1) {
		Showmsg('modify_replied');
	}
	$rs = $db->get_one("SELECT replies,topped,tpcstatus FROM pw_threads WHERE tid=".pwEscape($tid));
	$thread_tpcstatus = $rs['tpcstatus'];
	if ($rs['replies'] != $rt['count']) {
		$db->update("UPDATE pw_threads SET replies=".pwEscape($rt['count'])."WHERE tid=".pwEscape($tid));
	}
	require_once(R_P.'require/credit.php');
	$creditset = $credit->creditset($creditset,$db_creditset);
	if ($atcdb['aid']) {
		require_once(R_P.'require/updateforum.php');
		delete_att($atcdb['aid']);
		pwFtpClose($ftp);
	}
	if ($article == 0) {
		$deltype  = 'deltpc';
		$deltitle = substrs($subject,28);
		if ($count == 1) {
			$db->update("DELETE FROM $pw_tmsgs WHERE tid=".pwEscape($tid));
			# $db->update("DELETE FROM pw_threads WHERE tid=".pwEscape($tid));
			# ThreadManager
                        $threadManager = L::loadClass("threadmanager");
			$threadManager->deleteByThreadId($fid,$tid);

			P_unlink(R_P."$db_htmdir/$fid/".date('ym',$postdate)."/$tid.html");
		} else {
			$rt = $db->get_one("SELECT * FROM $pw_posts WHERE tid=".pwEscape($tid)."ORDER BY postdate LIMIT 1");
			if ($count == 2) {
				$lastpost	= $rt['postdate'];
				$lastposter	= $rt['author'];
			} else {
				$lt = $db->get_one("SELECT postdate,author FROM $pw_posts WHERE tid=".pwEscape($tid)."ORDER BY postdate DESC LIMIT 1");
				$lastpost	= $lt['postdate'];
				$lastposter	= $lt['author'];
			}
			$count -= 2;
			$db->update("DELETE FROM $pw_posts WHERE pid=".pwEscape($rt['pid']));
			$pwSQL = $rt['subject'] ? array('subject'=>$rt['subject']) : array();
			$pwSQL += array(
				'icon'		=> $rt['icon'],
				'author'	=> $rt['author'],
				'authorid'	=> $rt['authorid'],
				'postdate'	=> $rt['postdate'],
				'lastpost'	=> $lastpost,
				'lastposter'=> $lastposter,
				'replies'	=> $count
			);
			$db->update("UPDATE pw_threads SET ".pwSqlSingle($pwSQL,false)." WHERE tid=".pwEscape($tid));
                        # memcache reflesh
                        $threadList = L::loadClass("threadlist");
                        $threadList->updateThreadIdsByForumId($fid,$tid);
			$db->update("UPDATE $pw_tmsgs SET " . pwSqlSingle(array(
				'aid'		=> $rt['aid'],				'userip'	=> $rt['userip'],
				'ifsign'	=> $rt['ifsign'],			'ipfrom'	=> $rt['ipfrom'],
				'alterinfo'	=> $rt['alterinfo'],		'ifconvert'	=> $rt['ifconvert'],
				'content'	=> $rt['content']
			),false) . " WHERE tid=".pwEscape($tid));
		}
		$msg_delrvrc  = abs($creditset['Delete']['rvrc']);
		$msg_delmoney = abs($creditset['Delete']['money']);
		$credit->addLog('topic_Delete',$creditset['Delete'],array(
			'uid'		=> $authorid,
			'username'	=> $author,
			'ip'		=> $onlineip,
			'fname'		=> strip_tags($forum[$fid]['name']),
			'operator'	=> $windid
		));
		$credit->sets($authorid,$creditset['Delete'],false);

		if ($thread_tpcstatus && getstatus($thread_tpcstatus, 1)) {
			$db->update("DELETE FROM pw_argument WHERE tid=".pwEscape($tid));
		}
	} else {
		$deltype  = 'delrp';
		$deltitle = $subject ? substrs($subject,28) : substrs($content,28);
		$db->update("DELETE FROM $pw_posts WHERE pid=".pwEscape($pid));
		$db->update("UPDATE pw_threads SET replies=replies-1 WHERE tid=".pwEscape($tid));
		$msg_delrvrc  = abs($creditset['Deleterp']['rvrc']);
		$msg_delmoney = abs($creditset['Deleterp']['money']);
		$credit->addLog('topic_Deleterp',$creditset['Deleterp'],array(
			'uid'		=> $authorid,
			'username'	=> $author,
			'ip'		=> $onlineip,
			'fname'		=> strip_tags($forum[$fid]['name']),
			'operator'	=> $windid
		));
		$credit->sets($authorid,$creditset['Deleterp'],false);
	}
	$credit->setMdata($authorid,'postnum',-1);
	$credit->runsql();

	if ($db_guestread) {
		require_once(R_P.'require/guestfunc.php');
		clearguestcache($tid,$rs['replies']);
	}
	P_unlink(D_P.'data/bbscache/c_cache.php');
	require_once R_P . 'require/updateforum.php';
	updateforum($fid);
	if ($rs['topped']) {
		updatetop();
	}
	$msg_delrvrc = floor($msg_delrvrc/10);
	require_once(R_P.'require/writelog.php');
	$log = array(
		'type'      => 'delete',
		'username1' => $author,
		'username2' => $windid,
		'field1'    => $fid,
		'field2'    => '',
		'field3'    => '',
		'descrip'   => $deltype.'_descrip',
		'timestamp' => $timestamp,
		'ip'        => $onlineip,
		'tid'		=> $tid,
		'forum'		=> $pwforum->foruminfo['name'],
		'subject'	=> $deltitle,
		'affect'	=> "{$db_rvrcname}：-{$msg_delrvrc}，{$db_moneyname}：-{$msg_delmoney}",
		'reason'	=> 'edit delete article!'
	);
	writelog($log);
	if ($pwforum->foruminfo['allowhtm'] && $article<=$db_readperpage ) {
		$StaticPage = L::loadClass('StaticPage');
		$StaticPage->update($tid);
	}
	if ($deltype == 'delrp') {
		refreshto("read.php?tid=$tid",'enter_thread');
	} else {
		refreshto("thread.php?fid=$fid",'enter_thread');
	}
} elseif ($_POST['step'] == 2) {

	InitGP(array('atc_title','atc_content'), 'P', 0);
	InitGP(array('replayorder','atc_anonymous','atc_newrp','atc_tags','atc_hideatt','magicid','magicname','atc_enhidetype','atc_credittype','flashatt'),'P');
	InitGP(array('atc_iconid','atc_hide','atc_requireenhide','atc_rvrc','atc_requiresell','atc_money', 'atc_usesign', 'atc_html', 'p_type', 'p_sub_type', 'atc_convert', 'atc_autourl'), 'P', 2);

	require_once(R_P . 'require/bbscode.php');
	if ($postmodify->type == 'topic') {
		$postdata = new topicPostData($pwpost);
		$postdata->initData($postmodify);
		$postdata->setWtype($p_type, $p_sub_type, $t_per, $t_db, $db_forcetype);
		$postdata->setTags($atc_tags);
		$postdata->setMagic($magicid,$magicname);
		$postdata->setIfmail(0, $atc_newrp);
		$postdata->setStatus('3',decbin($replayorder));
	} else {
		$postdata = new replyPostData($pwpost);
		$postdata->initData($postmodify);
	}

	$postdata->setTitle($atc_title);
	$postdata->setContent($atc_content);

	$postdata->setConvert($atc_convert, $atc_autourl);
	$postdata->setAnonymous($atc_anonymous);
	$postdata->setHideatt($atc_hideatt);
	$postdata->setIconid($atc_iconid);
	$postdata->setIfsign($atc_usesign, $atc_html);

	$postdata->setHide($atc_hide);
	$postdata->setEnhide($atc_requireenhide, $atc_rvrc, $atc_enhidetype);
	$postdata->setSell($atc_requiresell, $atc_money, $atc_credittype);

	if ($special && file_exists(R_P . "lib/special/post_{$special}.class.php")) {
		require_once Pcv(R_P . "lib/special/post_{$special}.class.php");
		$postSpecial = new postSpecial($pwpost);
		$postSpecial->modifyData($tid);
	}
	if ($postmodify->hasAtt()) {
		InitGP(array('keep','oldatt_special','oldatt_needrvrc'), 'P', 2);
		InitGP(array('oldatt_ctype','oldatt_desc'), 'P');
		$postmodify->initAttachs($keep, $oldatt_special, $oldatt_needrvrc, $oldatt_ctype, $oldatt_desc);
	}
	require_once(R_P . 'lib/upload/attupload.class.php');
	if (PwUpload::getUploadNum() || $flashatt) {
		$postdata->att = new AttUpload($winduid, $flashatt);
		$postdata->att->check();
		$postdata->att->transfer();
		$postdata->att->setReplaceAtt($postmodify->replacedb);
		PwUpload::upload($postdata->att);
	}
	$postmodify->execute($postdata);

	if ($postSpecial) {
		$postSpecial->updateData($tid);
	}
	if ($postTopic) {//分类主题
		$postTopic->initData();
		$postTopic->insertData($tid,$fid);
	}
	if ($postCate) {//团购活动
		$postCate->initData();
		$postCate->insertData($tid,$fid);
	}

	if ($postdata->getIfcheck()) {
		if ($postdata->filter->filter_weight == 3) {
			$pinfo = 'enter_words';
			$banword = implode(',',$postdata->filter->filter_word);
		} elseif($prompts = $pwpost->getprompt()){
			isset($prompts['allowhide'])   && $pinfo = "post_limit_hide";
			isset($prompts['allowsell'])   && $pinfo = "post_limit_sell";
			isset($prompts['allowencode']) && $pinfo = "post_limit_encode";
		}else{
			$pinfo = 'enter_thread';
		}
	} else {
		if ($postdata->filter->filter_weight == 2) {
			$banword = implode(',',$postdata->filter->filter_word);
			$pinfo = 'post_word_check';
		} elseif ($postdata->linkCheckStrategy) {
			$pinfo = 'post_link_check';
		}  else {
			$pinfo = 'post_check';
		}
	}
	refreshto("read.php?tid=$tid&page=$page&toread=1#$pid", $pinfo);
}
?>