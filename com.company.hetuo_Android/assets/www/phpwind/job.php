<?php
define('SCR','job');
require_once('global.php');

InitGP(array('action'));

if ($action == 'sign') {

	Cookie('newpic',$timestamp);
	refreshto("$db_bfn",'operate_success');

} elseif ($action == 'preview') {

	require_once(R_P.'require/bbscode.php');
	require_once(R_P.'require/header.php');
	InitGP(array('pcid','modelid'),'P',2);
	$fielddb = $data = array();
	$atc_content = Char_cv(stripslashes(GetGP('atc_content','P')));
	$pcinfo = Char_cv(stripslashes(GetGP('pcinfo','P')));

	if ($modelid > 0) {
		$query = $db->query("SELECT fieldid,fieldname FROM pw_topicfield WHERE modelid=".pwEscape($modelid));
		while ($rt = $db->fetch_array($query)) {
			$fielddb[$rt['fieldid']] = $rt['fieldname'];
		}

		$pcdb = getPcviewdata($pcinfo,'topic');
		require_once(R_P.'lib/posttopic.class.php');
		$postTopic = new postTopic($data);
		$topicvalue = $postTopic->getTopicvalue($modelid,$pcdb);

	} elseif ($pcid > 0) {
		$query = $db->query("SELECT fieldid,fieldname FROM pw_pcfield WHERE pcid=".pwEscape($pcid));
		while ($rt = $db->fetch_array($query)) {
			$fielddb[$rt['fieldname']] = $rt['fieldid'];
		}

		$pcdb = getPcviewdata($pcinfo,'postcate');
		require_once(R_P.'lib/postcate.class.php');
		$postCate = new postCate($data);
		list(,$topicvalue) = $postCate->getCatevalue($pcid,$pcdb);
	}
	$atc_content = wordsConvert($atc_content);
	$atc_content = convert($atc_content,$db_windpost);
	$preatc = str_replace("\n","<br>",$atc_content);
	require_once PrintEot('preview');footer();

} elseif ($action == 'redirect') {

	InitGP(array('goto'));
	InitGP(array('aid'),'GP',2);

	$pw_attachs = L::loadDB('attachs');
	$attachs = $pw_attachs->get($aid);
	if ($attachs) {
		if ($goto == 'next') {
			$aid = $pw_attachs->nextImgByUid($attachs['uid'],$attachs['aid']);
		} elseif ($goto == 'pre') {
			$aid = $pw_attachs->prevImgByUid($attachs['uid'],$attachs['aid']);
		}
		$aid = intval($aid);
		ObHeader("show.php?action=pic&aid=$aid");
	} else {
		Showmsg('pic_not_exists');
	}
} elseif ($action == 'previous') {

	$rt = $db->get_one('SELECT fid,postdate,lastpost FROM pw_threads WHERE tid='.pwEscape($tid));
	$rt || Showmsg('data_error');
	$fid = (int)$rt['fid'];
	$goto = Char_cv(GetGP('goto'));
	if ($goto == "next") {
		$tid = $db->get_value("SELECT tid FROM pw_threads WHERE fid=".pwEscape($fid,false)."AND ifcheck='1' AND topped='0' AND lastpost<".pwEscape($rt['lastpost'],false)."ORDER BY lastpost DESC LIMIT 1");
	} else {
		$tid = $db->get_value("SELECT tid FROM pw_threads WHERE fid=".pwEscape($fid,false)."AND ifcheck='1' AND topped='0' AND lastpost>".pwEscape($rt['lastpost'],false)."ORDER BY lastpost ASC LIMIT 1");
	}
	if ($tid) {
		ObHeader("read.php?tid=$tid");
	} else {
		ObHeader("thread.php?fid=$fid");
	}
} elseif ($action == 'mutiupload') {

	if (empty($_POST['step'])) {

		$_G['uploadtype'] && $db_uploadfiletype = $_G['uploadtype'];
		$db_uploadfiletype = !empty($db_uploadfiletype) ? (is_array($db_uploadfiletype) ? $db_uploadfiletype : unserialize($db_uploadfiletype)) : array();
		$filetype = '';
		foreach ($db_uploadfiletype as $key => $value) {
			$filetype .= ($filetype ? ',' : '') . $key . ':' . $value;
		}
		$pwServer['HTTP_USER_AGENT'] = 'Shockwave Flash';
		$swfhash = GetVerify($winduid);

		echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?><swf><filetype>{$filetype}</filetype><uid>{$winduid}</uid><step>2</step><verify>{$swfhash}</verify></swf>";

	} else {

		InitGP(array('uid','verify'),'P');
		$uid = intval($uid);
		$swfhash = GetVerify($uid);
		checkVerify('swfhash');
		if (!$db_allowupload) {
			Showmsg('upload_close');
		}
		$rt = $db->get_one("SELECT groupid,memberid FROM pw_members WHERE uid=" . pwEscape($uid));
		(!$rt) && Showmsg('not_login');
		$groupid = $rt['groupid'] == '-1' ? $rt['memberid'] : $rt['groupid'];
		if (file_exists(D_P."data/groupdb/group_$groupid.php")) {
			require_once Pcv(D_P."data/groupdb/group_$groupid.php");
		} else {
			require_once(D_P.'data/groupdb/group_1.php');
		}
		if ($_G['allowupload'] == 0) {
			Showmsg('upload_group_right');
		}
		$_G['allownum'] = 15;
		$uploadnum = $db->get_value("SELECT COUNT(*) AS sum FROM pw_attachs WHERE tid=0 AND pid='0' AND uid=" . pwEscape($uid));
		if ($uploadnum >= $_G['allownum']) {
			Showmsg('upload_num_error');
		}
		$_G['uploadtype'] && $db_uploadfiletype = $_G['uploadtype'];
		$db_uploadfiletype = !empty($db_uploadfiletype) ? (is_array($db_uploadfiletype) ? $db_uploadfiletype : unserialize($db_uploadfiletype)) : array();
		$attachdir .= '/mutiupload';
		require_once(R_P . 'lib/upload/mutiupload.class.php');
		$mutiupload = new MutiUpload($uid);
		PwUpload::upload($mutiupload);
		exit;
	}

} elseif ($action == 'mutiuploadphoto') {

	@include_once(D_P.'data/bbscache/o_config.php');

	if (empty($_POST['step'])) {
		$filetype = '';
		$db_uploadfiletype = array();
		$db_uploadfiletype['gif'] = $db_uploadfiletype['jpg'] = $db_uploadfiletype['jpeg'] = $db_uploadfiletype['bmp'] = $db_uploadfiletype['png'] = $o_maxfilesize;

		foreach ($db_uploadfiletype as $key => $value) {
			$filetype .= ($filetype ? ',' : '') . $key . ':' . $value;
		}

		$pwServer['HTTP_USER_AGENT'] = 'Shockwave Flash';
		$swfhash = GetVerify($winduid);

		echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?><swf><filetype>{$filetype}</filetype><uid>{$winduid}</uid><windid>{$windid}</windid><step>2</step><verify>{$swfhash}</verify></swf>";

	} else {

		//验证码
		//require_once(R_P.'require/postfunc.php');
		require_once("mode/o/require/core.php");
		InitGP(array('uid','verify'),'P');
		InitGP(array('aid','filenames','photoid'),'G');
		$swfhash = GetVerify($uid);

		//$windid = pwConvert($windid,$db_charset,'utf-8');
		$windid = $db->get_value("SELECT username FROM pw_members WHERE uid=".pwEscape($uid));
		$filenames = pwConvert($filenames,$db_charset,'utf-8');
		$filenames = addslashes($filenames);

		checkVerify('swfhash');

		$rt = $db->get_one("SELECT aname,photonum,ownerid,private,lastphoto,atype FROM pw_cnalbum WHERE aid=" . pwEscape($aid));
		if (empty($rt)) {
			Showmsg('undefined_action');
		}
		if ($rt['atype'] == 1 ) {
			$colony = $db->get_one("SELECT c.cname,cm.id AS ifcyer FROM pw_colonys c LEFT JOIN pw_cmembers cm ON c.id=cm.colonyid AND cm.uid=" . pwEscape($uid) . " WHERE c.id=" . pwEscape($rt['ownerid']));
		} else {
			$uid != $rt['ownerid'] && Showmsg('colony_phototype');
		}
		$o_maxphotonum && $rt['photonum'] >= $o_maxphotonum && Showmsg('colony_photofull');

		require_once(R_P . 'lib/upload/photoupload.class.php');
		$img = new PhotoUpload($aid);
		PwUpload::upload($img);
		pwFtpClose($ftp);

		if (!$photos = $img->getAttachs()) {
			Showmsg('colony_uploadnull');
		}
		$photoNum = count($photos);
		$pid = $img->getNewID();
		$lastpid = getLastPid($aid, 4);
		array_unshift($lastpid, $pid);

		if ($rt['atype'] == 1) {
			$cyid = $rt['ownerid'];
			$feedText = '';
			foreach ($photos as $value) {
				$feedText .= "[url=$db_bbsurl/{#APPS_BASEURL#}space=1&q=galbum&a=view&cyid=$cyid&pid=$pid][img]" . getphotourl($value['path'], $value['ifthumb'])."[/img][/url]&nbsp;";
			}
			pwAddFeed($uid, 'colony_photo', $cyid, array(
				'lang'	=> 'colony_photo',
				'cyid'	=> $cyid,
				'num'	=> $photoNum,
				'colony_name'	=> $colony['cname'],
				'text'	=> $feedText
			));
		} elseif (!$rt['private']) {
			$feedText = "[url=$db_bbsurl/{#APPS_BASEURL#}space=1&q=photos&a=album&aid=$aid&u=$uid]{$rt[aname]}[/url]\n";
			foreach ($photos as $value) {
				$feedText .= "[url=$db_bbsurl/mode.php?m=o&space=1&q=photos&a=view&pid=$pid&u=$uid][img]" . getphotourl($value['path'], $value['ifthumb'])."[/img][/url]&nbsp;";
			}
			pwAddFeed($uid, 'photo', $pid, array('num' => $photoNum, 'text' => $feedText));

			//会员资讯缓存
			$usercache = L::loadDB('Usercache');
			$usercachedata = $usercache->get($uid,'photos');
			$usercachedata = explode(',',$usercachedata['value']);
			is_array($usercachedata) || $usercachedata = array();
			if (count($usercachedata) >= 4) array_pop($usercachedata);
			array_unshift($usercachedata,$pid);
			$usercachedata = implode(',',$usercachedata);
			$usercache->update($uid,'photos',$pid,$usercachedata);
		}

		$db->update("UPDATE pw_cnalbum SET photonum=photonum+".pwEscape($photoNum,false) . ",lasttime=" . pwEscape($timestamp,false) . ',lastpid=' . pwEscape(implode(',',$lastpid)) . (!$rt['lastphoto'] ? ',lastphoto='.pwEscape($img->getLastPhoto()) : '') . " WHERE aid=" . pwEscape($aid));

		countPosts("+$photoNum");

		//积分变动
		require_once(R_P.'require/credit.php');
		$o_photos_creditset = unserialize($o_photos_creditset);
		$creditset = getCreditset($o_photos_creditset['Uploadphoto'],true,$photoNum);
		$creditset = array_diff($creditset,array(0));
		if (!empty($creditset)) {
			$credit->sets($uid,$creditset,true);
			updateMemberid($uid);
		}
		if ($creditlog = unserialize($o_photos_creditlog)) {
			addLog($creditlog['Uploadphoto'],$windid,$uid,'photos_Uploadphoto');
		}
		exit;
	}
} elseif ($action == 'uploadicon') {

	if (empty($_GET['step'])) {

		list($db_upload,$db_imglen,$db_imgwidth,$db_imgsize) = explode("\t",$db_upload);
		InitGP(array('uid','verify'));
		$swfhash = GetVerify($uid);
		checkVerify('swfhash');

		require_once(R_P . 'lib/upload/faceupload.class.php');
		$face = new FaceUpload($uid);
		PwUpload::upload($face);
		$uploaddb = $face->getAttachs();

		echo $db_bbsurl.'/'.$attachpath.'/'.$uploaddb['fileuploadurl'].'?'.$timestamp;exit;

	} else {

		require_once(R_P . 'lib/upload.class.php');
		$ext = strtolower(substr(strrchr($_GET['filename'],'.'),1));
		$udir = str_pad(substr($winduid,-2),2,'0',STR_PAD_LEFT);

		$source = PwUpload::savePath(0, "{$winduid}_tmp.$ext", "upload/$udir/");
		if (!in_array(strtolower($ext), array('gif','jpg','jpeg','png','bmp')) || !file_exists($source)) {
			Showmsg('undefined_action');
		}
		$data = $_SERVER['HTTP_RAW_POST_DATA'] ? $_SERVER['HTTP_RAW_POST_DATA'] : file_get_contents('php://input');

		if ($data) {

			InitGP(array('from'));
			require_once(R_P . 'require/showimg.php');

			$filename  = "{$winduid}.jpg";
			$normalDir = "upload/$udir/";
			$middleDir = "upload/middle/$udir/";
			$smallDir  = "upload/small/$udir/";
			$img_w = $img_h = 0;

			$middleFile = PwUpload::savePath($db_ifftp, $filename, "$middleDir", 'm_');
			PwUpload::createFolder(dirname($middleFile));
			writeover($middleFile, $data);

			require_once(R_P.'require/imgfunc.php');
			if (!$img_size = GetImgSize($middleFile,'jpg')) {
				P_unlink($middleFile);
				Showmsg('upload_content_error');
			}

			$normalFile = PwUpload::savePath($db_ifftp, "{$winduid}.$ext", "$normalDir");
			PwUpload::createFolder(dirname($normalFile));
			list($w, $h) = explode("\t", $db_fthumbsize);
			if ($db_iffthumb && MakeThumb($source, $normalFile, $w, $h)) {
				P_unlink($source);
			} elseif (!PwUpload::movefile($source, $normalFile)) {
				Showmsg('undefined_action');
			}
			list($img_w, $img_h) = getimagesize($normalFile);

			$smallFile = PwUpload::savePath($db_ifftp, $filename, "$smallDir", 's_');
			$s_ifthumb = 0;
			PwUpload::createFolder(dirname($smallFile));
			if (MakeThumb($middleFile, $smallFile, 48, 48)) {
				$s_ifthumb = 1;
			}
			if ($db_ifftp) {
				PwUpload::movetoftp($normalFile, $normalDir . "{$winduid}.$ext");
				PwUpload::movetoftp($middleFile, $middleDir . $filename);
				$s_ifthumb && PwUpload::movetoftp($smallFile, $smallDir . $filename);
			}
			pwFtpClose($GLOBALS['ftp']);

			$user_a = explode('|',$winddb['icon']);
			$user_a[2] = $img_w;
			$user_a[3] = $img_h;
			$usericon = setIcon("$udir/{$winduid}.$ext", 3, $user_a);

			$db->update("UPDATE pw_members SET icon=" . pwEscape($usericon,false) . " WHERE uid=" . pwEscape($winduid));

			//job sign
			initJob($winduid,"doUpdateAvatar");

			refreshto($from == 'reg' ? 'index.php' : 'profile.php?action=modify&info_type=face','upload_icon_success');

		} else {
			Showmsg('upload_icon_fail');
		}
	}
} elseif ($action == 'download') {

	set_time_limit(300);
	$aid = (int)GetGP('aid');
	empty($aid) && Showmsg('job_attach_error');

	$pw_attachs = L::loadDB('attachs');

	$attach = $pw_attachs->get($aid);
	!$attach && Showmsg('job_attach_error');
	if (empty($attach['attachurl']) || strpos($attach['attachurl'],'..') !== false) {
		Showmsg('job_attach_error');
	}
	$fid = $attach['fid']; $aid = $attach['aid']; $tid = $attach['tid']; $pid = $attach['pid'];
	$fid = $db->get_value('SELECT fid FROM pw_threads WHERE tid='.pwEscape($tid,false));
	$fid || Showmsg('data_error');
	if (!$windid && GetCookie('winduser') && $ol_offset) {
		$userdb = explode("\t",getuserdb(D_P."data/bbscache/online.php",$ol_offset));
		if ($userdb && $userdb[2] == $onlineip) {
			$winddb = $db->get_one("SELECT m.uid,m.username,m.groupid,m.memberid,m.groups,md.money,md.rvrc FROM pw_members m LEFT JOIN pw_memberdata md USING(uid) WHERE m.uid=".pwEscape($userdb['8']));
			$winduid  = $winddb['uid'];
			$groupid  = $winddb['groupid'];
			$groupid == '-1' && $groupid = $winddb['memberid'];
			$userrvrc = round($winddb['rvrc']/10,1);
			$windid	  = $winddb['username'];
			if (file_exists(D_P."data/groupdb/group_$groupid.php")) {
				require_once Pcv(D_P."data/groupdb/group_$groupid.php");
			} else {
				require_once(D_P."data/groupdb/group_1.php");
			}
		}
		define('FX',1);
	}
	if (!($foruminfo = L::forum($fid))) {
		$foruminfo	= $db->get_one("SELECT f.*,fe.creditset,fe.forumset,fe.commend FROM pw_forums f LEFT JOIN pw_forumsextra fe ON f.fid=fe.fid WHERE f.fid=".pwEscape($fid));
		if ($foruminfo) {
			$foruminfo['creditset'] = unserialize($foruminfo['creditset']);
			$foruminfo['forumset'] = unserialize($foruminfo['forumset']);
			$foruminfo['commend'] = unserialize($foruminfo['commend']);
		}
	}
	!$foruminfo && Showmsg('data_error');
	require_once(R_P.'require/forum.php');
	wind_forumcheck($foruminfo);
	if ($groupid == '3' || admincheck($foruminfo['forumadmin'],$foruminfo['fupadmin'],$windid)) {#获取管理权限
		$admincheck = 1;
	} else {
		$admincheck = 0;
	}
	if ($foruminfo['allowdownload'] && !allowcheck($foruminfo['allowdownload'],$groupid,$winddb['groups']) && !$admincheck) {#版块权限判断
		Showmsg('job_attach_forum');
	}
	if (!$foruminfo['allowdownload'] && $_G['allowdownload']==0 && !$admincheck) {#用户组权限判断
		Showmsg('job_attach_group');
	}
	if (!$attach_url && !$db_ftpweb && !is_readable("$attachdir/".$attach['attachurl'])) {
		Showmsg('job_attach_error');
	}
	$fgeturl = geturl($attach['attachurl']);
	!$fgeturl[0] && Showmsg('job_attach_error');

	$filename = basename("$attachdir/".$attach['attachurl']);
	$fileext  = substr(strrchr($attach['attachurl'],'.'),1);
	$filesize = 0;

	if (strpos($pwServer['HTTP_USER_AGENT'], 'MSIE')!==false && $fileext == 'torrent') {
		$attachment = 'inline';
	} else {
		$attachment = 'attachment';
	}
	$attach['name'] = trim(str_replace('&nbsp;',' ',$attach['name']));
	if ($db_charset == 'utf-8') {
		if (function_exists('mb_convert_encoding')) {
			$attach['name'] = mb_convert_encoding($attach['name'], "gbk",'utf-8');
		} else {
			require_once(R_P.'m/chinese.php');
			$chs  = new Chinese('UTF8','gbk');
			$attach['name'] = $chs->Convert($attach['name']);
		}
	}

	$credit = $uploadcredit = $downloadmoney = null;

	if ($_G['allowdownload'] == 1) {
		$forumset = $foruminfo['forumset'];
		list($uploadcredit,,$downloadmoney,) = explode("\t",$forumset['uploadset']);
		if ($downloadmoney) {
			require_once(R_P.'require/credit.php');
			if ($downloadmoney > 0 && $credit->get($winduid, $uploadcredit) < $downloadmoney) {
				$creditname = $credit->cType[$uploadcredit];
				Showmsg('download_money_limit');
			}
			$credit->addLog('topic_download',array($uploadcredit => -$downloadmoney),array(
				'uid'		=> $winduid,
				'username'	=> $windid,
				'ip'		=> $onlineip,
				'fname'		=> $foruminfo['name']
			));
			if (!$credit->set($winduid, $uploadcredit, -$downloadmoney, false)) {
				Showmsg('undefined_action');
			}
		}
	}
	if ($attach['needrvrc'] > 0 && !$admincheck) {
		!$windid && Showmsg('job_attach_special');
		require_once(R_P.'require/credit.php');
		if ($attach['special'] == '2') {
			if (!$ifbuy = $db->get_one("SELECT uid FROM pw_attachbuy WHERE aid=" . pwEscape($aid) . " AND uid=" . pwEscape($winduid))) {
				!$attach['ctype'] && $attach['ctype'] = 'money';
				$usercredit = $credit->get($winduid, $attach['ctype']);
				$creditName = $credit->cType[$attach['ctype']];
				$db_sellset['price'] > 0 && $attach['needrvrc'] = min($attach['needrvrc'], $db_sellset['price']);
				if ($usercredit < $attach['needrvrc']) {
					$needrvrc = $attach['needrvrc'];
					Showmsg(($downloadmoney > 0 && $uploadcredit == $attach['ctype']) ? 'job_attach_sale_download' : 'job_attach_sale');
				}
				$db->update("INSERT INTO pw_attachbuy SET " . pwSqlSingle(array(
					'aid'	=> $aid,
					'uid'	=> $winduid,
					'ctype'	=> $attach['ctype'],
					'cost'	=> $attach['needrvrc']
				)));
				$credit->addLog('topic_attbuy',array($attach['ctype'] => -$attach['needrvrc']),array(
					'uid'		=> $winduid,
					'username'	=> $windid,
					'ip'		=> $onlineip
				));
				$credit->set($winduid, $attach['ctype'], -$attach['needrvrc'], false);

				if ($db_sellset['income'] < 1 || ($income = $db->get_value("SELECT SUM(cost) AS sum FROM pw_attachbuy WHERE aid=" . pwEscape($aid))) < $db_sellset['income']) {
					$username = $db->get_value("SELECT username FROM pw_members WHERE uid=".pwEscape($attach['uid'],false));
					$credit->addLog('topic_attsell',array($attach['ctype'] => $attach['needrvrc']),array(
						'uid'		=> $attach['uid'],
						'username'	=> $username,
						'ip'		=> $onlineip,
						'buyer'		=> $windid
					));
					$credit->set($attach['uid'], $attach['ctype'], $attach['needrvrc'], false);
				}
			}
		} else {
			!$attach['ctype'] && $attach['ctype'] = 'rvrc';
			$usercredit = $credit->get($winduid,$attach['ctype']);
			if ($usercredit < $attach['needrvrc']) {
				$needrvrc = $attach['needrvrc'];
				$creditName = $credit->cType[$attach['ctype']];echo 'aaaaaaaa';
				Showmsg(($downloadmoney > 0 && $uploadcredit == $attach['ctype']) ? 'job_attach_rvrc_download' : 'job_attach_rvrc');
			}
		}
	}
	if (isset($credit) && $credit->setUser) {
		$credit->runsql();
	}
	$pw_attachs->increaseField($aid,'hits');

	if ($db_attachhide && $attach['size']>$db_attachhide && $attach['type'] == 'zip' && !defined('FX')) {
		ObHeader($fgeturl[0]);
	} elseif ($fgeturl[1] == 'Local') {
		$fgeturl[0] = R_P.$fgeturl[0];
		$filesize   = filesize($fgeturl[0]);
	}
	$ctype = '';
	switch($fileext) {
		case "pdf"	: $ctype = "application/pdf"; break;
		case "rar"	:
		case "zip"	: $ctype = "application/zip"; break;
		case "doc"	: $ctype = "application/msword"; break;
		case "xls"	: $ctype = "application/vnd.ms-excel"; break;
		case "ppt"	: $ctype = "application/vnd.ms-powerpoint"; break;
		case "gif"	: $ctype = "image/gif"; break;
		case "png"	: $ctype = "image/png"; break;
		case "jpeg"	:
		case "jpg"	: $ctype = "image/jpeg"; break;
		case "wav"	: $ctype = "audio/x-wav"; break;
		case "mpeg"	:
		case "mpg"	:
		case "mpe"	: $ctype = "video/x-mpeg"; break;
		case "mov"	: $ctype = "video/quicktime"; break;
		case "avi"	: $ctype = "video/x-msvideo"; break;
		case "txt"	: $ctype = "text/plain"; break;
		default		: $ctype = "application/octet-stream";
	}
	ob_end_clean();
	header('Last-Modified: '.gmdate('D, d M Y H:i:s',$timestamp+86400).' GMT');
	header('Expires: '.gmdate('D, d M Y H:i:s',$timestamp+86400).' GMT');
	header('Cache-control: max-age=86400');
	header('Content-Encoding: none');
	header("Content-Disposition: $attachment; filename=\"{$attach['name']}\"");
	header("Content-type: $ctype");
	header("Content-Transfer-Encoding: binary");
	$filesize && header("Content-Length: $filesize");
	$i = 1;
	while (!@readfile($fgeturl[0])) {
		if (++$i > 3) break;
	}
	exit;

} elseif ($action == 'showimg') {

	InitGP(array('verify','pid','aid'));
	if ($verify != md5("showimg{$tid}{$pid}{$fid}{$aid}{$db_hash}")) {
		Showmsg('undefined_action');
	}
	if (function_exists('file_get_contents')) {
		$rs = $db->get_one('SELECT attachurl FROM pw_attachs WHERE aid='.pwEscape($aid).' AND tid='.pwEscape($tid).' AND fid='.pwEscape($fid));
		if ($rs) {
			$fgeturl = geturl($rs['attachurl']);
			if ($fgeturl[0]) {
				echo file_get_contents($fgeturl[0]);exit;
			}
		}
	}
	Showmsg('job_attach_error');

} elseif ($action == 'deldownfile') {

	PostCheck();
	InitGP(array('aid','page'));
	empty($aid) && Showmsg('job_attach_error');

	$pw_attachs = L::loadDB('attachs');
	$attach = $pw_attachs->get($aid);
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
		$pw_attachs->delete($aid);
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
		if ($foruminfo['allowhtm'] && $page == 1) {
			$StaticPage = L::loadClass('StaticPage');
			$StaticPage->update($tid);
			empty($j_p) && $j_p="read.php?tid=$tid";
			refreshto($j_p,'operate_success');
		} else {
			refreshto("read.php?tid=$tid&page=$page",'operate_success');
		}
	} else {
		Showmsg('job_attach_right');
	}
} elseif ($action == 'viewtody') {

	$wind_in = 'viewtody';
	if ($db_today == 0) {
		Showmsg('job_viewtody_close');
	}
	InitGP(array('page'),'GP',2);
	require_once(R_P.'require/header.php');
	$check_admin="N";
	if (CkInArray($windid,$manager)) $check_admin="Y";
	$page < 1 && $page = 1;
	$filename = D_P.'data/bbscache/today.php';
	$dbtdsize = 100+1;
	$seed = $page*$db_perpage;$count=0;
	if ($fp = @fopen($filename,"rb")) {
		flock($fp,LOCK_SH);
		$node=fread($fp,$dbtdsize);
		$nodedb=explode("\t",$node);/*头结点在第二个数据段*/
		$nodefp=$dbtdsize*$nodedb[1];
		fseek($fp,$nodefp,SEEK_SET);
		$todayshow=fseeks($fp,$dbtdsize,$seed);/*传回数组*/
		fseek($fp,0,SEEK_END);
		$count=floor(ftell($fp)/$dbtdsize)-1;
		fclose($fp);
	}
	if ($count%$db_perpage==0) {
		$numofpage = $count/$db_perpage;  //$numofpage为 一共多少页
	} else {
		$numofpage = floor($count/$db_perpage)+1;
	}
	if ($page > $numofpage)
		$page = $numofpage;

	$pagemin = min(($page-1)*$db_perpage , $count-1);
	$pagemax = min($pagemin+$db_perpage-1, $count-1);
	$pages = numofpage($count,$page,$numofpage,"job.php?action=viewtody&");

	$inbbsdb = array();
	for ($i = $pagemin; $i <= $pagemax; $i++) {
		if (!trim($todayshow[$i]))
			continue;
		list($inbbs['user'],$null1,$null2,$inbbs['rgtime'],$inbbs['logintime'],$inbbs['intime'],$inbbs['ip'],$inbbs['post'],$inbbs['rvrc'],$null) = explode("\t",$todayshow[$i]);
		$inbbs['rawuser']	= rawurlencode($inbbs['user']);
		$inbbs['rvrc']		= floor($inbbs['rvrc']/10);
		$inbbs['rgtime']	= get_date($inbbs['rgtime']);
		$inbbs['logintime']	= get_date($inbbs['logintime']);
		$inbbs['intime']	= get_date($inbbs['intime']);
		if ($check_admin == "N") {
			$inbbs['ip'] = "secret";
		}
		$inbbsdb[] = $inbbs;
	}

	require_once PrintEot('todayinbbs');footer();

} elseif ($action == 'buytopic') {

	PostCheck();
	InitGP(array('pid', 'page'));
	$tpcs = array();
	if ($pid == 'tpc') {
		$table = GetTtable($tid);
		$tpcs  = $db->get_one("SELECT t.author,t.authorid,t.subject,tm.userip,tm.content,tm.buy FROM pw_threads t LEFT JOIN $table tm ON tm.tid=t.tid WHERE t.tid=".pwEscape($tid));
		$where = '';
	} elseif (is_numeric($pid)) {
		$table = GetPtable('N',$tid);
		$tpcs  = $db->get_one("SELECT author,authorid,subject,userip,content,buy FROM $table WHERE pid=".pwEscape($pid).' AND tid='.pwEscape($tid));
		$where = ' AND pid='.pwEscape($pid);
	}
	!$tpcs && Showmsg('illegal_tid');

	!$tpcs['subject'] && $tpcs['subject'] = preg_replace('/\[.*?\]/i','',substrs($tpcs['content'],25));
	$tpcs['content'] = substr($tpcs['content'],strpos($tpcs['content'],'[sell=')+6);
	$cost = substr($tpcs['content'],0,strpos($tpcs['content'],']'));
	list($creditvalue,$credittype) = explode(',',$cost);

	if (empty($windid) || $winduid == $tpcs['authorid']) {
		Showmsg('undefined_action');
	}
	if ($tpcs['buy'] && strpos(','.$tpcs['buy'].',',','.$windid.',') !== false) {
		Showmsg('job_havebuy');
	}
	$count	 = count(explode(',',$tpcs['buy']));
	$creditvalue = (int)$creditvalue;
	if ($creditvalue < 0) {
		$creditvalue = 0;
	} elseif ($db_sellset['price'] && $creditvalue > $db_sellset['price']) {
		$creditvalue = $db_sellset['price'];
	}
	require_once(R_P.'require/credit.php');
	!isset($credit->cType[$credittype]) && $credittype = 'money';

	if ($credit->get($winduid,$credittype) < $creditvalue) {
		$creditname = $credit->cType[$credittype];
		Showmsg('job_buy_noenough');
	}
	$credit->addLog('topic_buy',array($credittype => -$creditvalue),array(
		'uid'		=> $winduid,
		'username'	=> $windid,
		'ip'		=> $onlineip,
		'tid'		=> $tid,
		'subject'	=> $tpcs['subject']
	));
	$credit->set($winduid,$credittype,-$creditvalue,false);

	$creditvalue>10 && $creditvalue = $creditvalue*0.9;
	$income = $creditvalue * $count;
	if (!$db_sellset['income'] || $income < $db_sellset['income']) {
		$credit->addLog('topic_sell',array($credittype => $creditvalue),array(
			'uid'		=> $tpcs['authorid'],
			'username'	=> $tpcs['author'],
			'ip'		=> $tpcs['userip'],
			'tid'		=> $tid,
			'subject'	=> $tpcs['subject'],
			'buyer'		=> $windid
		));
		$credit->set($tpcs['authorid'],$credittype,$creditvalue,false);
	}
	$credit->runsql();
	$tpcs['buy'] .= ($tpcs['buy'] ? "," : '').$windid;
	$db->update("UPDATE $table SET buy=".pwEscape($tpcs['buy'],false)." WHERE tid=".pwEscape($tid).$where);

	$threadObj = L::loadClass("threads");
	$threadObj->clearThreadByThreadId($tid);
	$threadObj->clearTmsgsByThreadId($tid);
	refreshto("read.php?tid=$tid&page=$page#post_$pid",'operate_success');

} elseif ($action == 'vote') {

	PostCheck();
	$rt = $db->get_one('SELECT t.fid,t.tid,t.postdate,t.lastpost,t.state,t.locked,t.ifcheck,p.* FROM pw_polls p LEFT JOIN pw_threads t ON p.tid=t.tid WHERE p.tid='.pwEscape($tid));
	!$rt && Showmsg('data_error');
	@extract($rt);
	/**
	* 得到版块基本信息,版块权限验证
	*/

	if (!($foruminfo = L::forum($fid))) {
		Showmsg('data_error');
	}
	require_once(R_P.'require/forum.php');
	wind_forumcheck($foruminfo);

	/*
	*  获取管理权限
	*/
	if ($groupid == '3' || $groupid == '4' || admincheck($foruminfo['forumadmin'],$foruminfo['fupadmin'],$windid)) {
		$admincheck = 1;
	} else {
		$admincheck = 0;
	}
	/*
	*用户组权限验证
	*/
	$_G['allowvote'] == 0 && Showmsg('job_vote_right');
	if ((!$admincheck && $locked > 0 ) || ($foruminfo['forumset']['lock'] && $timestamp - $postdate > (int)$foruminfo['forumset']['lock'] * 86400 && !$admincheck)) {
		Showmsg('job_vote_lock');
	} elseif ($state || ($timelimit && $timestamp - $postdate > $timelimit * 86400)) {
		Showmsg('job_vote_close');
	}
	if (!$admincheck && $regdatelimit && $winddb['regdate'] > $regdatelimit) {
		$regdatelimit = get_date($regdatelimit,'Y-m-d');
		Showmsg('job_vote_regdatelimit');
	}
	InitGP(array('voteid','voteaction'),'P');
	$votearray = unserialize($voteopts);

	if (empty($voteid) || !is_array($voteid)) {
		Showmsg('job_vote_sel');
	}
	$voteid = array_unique($voteid);
	$v_sum  = count($voteid);
	if ($multiple && $v_sum > $mostvotes || !$multiple && $v_sum != 1) {
		Showmsg('job_vote_num');
	}
	if ($multiple && $leastvotes > 0 && $v_sum < $leastvotes) {
		Showmsg('job_vote_leastnum');
	}
	if (!$admincheck && $creditlimit){
		checkCreditLimit($creditlimit);
	}
	if (!$admincheck && $postnumlimit && $winddb['postnum'] < $postnumlimit) {
		Showmsg('job_vote_postnum');
	}
	$sql_1 = '';
	if ($groupid == 'guest') {
		$windid  = $onlineip;
		$winduid = 0;
		$sql_1	 = ' AND username=' . pwEscape($windid);
	}
	$sqlw = 'tid=' . pwEscape($tid) . ' AND uid=' . pwEscape($winduid) . $sql_1;

	$sql_2 = ',voters=voters+1';
	if ($voteaction == 'modify') {
		if ($_G['edittime'] && ($timestamp - $postdate) > $_G['edittime'] * 60) {
			Showmsg('modify_timelimit');
		} elseif (!$modifiable) {
			Showmsg('vote_not_modify');
		}
		$query = $db->query("SELECT vote FROM pw_voter WHERE $sqlw");
		while ($rt = $db->fetch_array($query)) {
			if (isset($votearray[$rt['vote']])) {
				$votearray[$rt['vote']][1]--;
			}
			$sql_2 = '';
		}
		$db->update("DELETE FROM pw_voter WHERE $sqlw");
	} else {
		if ($db->get_value("SELECT COUNT(*) AS SUM FROM pw_voter WHERE $sqlw") > 0) {
			Showmsg('job_havevote');
		}
	}
	$pwSQL = array();
	foreach ($voteid as $k => $id) {
		if (isset($votearray[$id])) {
			$votearray[$id][1]++;
			$pwSQL[] = array($tid, $winduid, $windid, $id, $timestamp);
		}
	}
	$voteopts = serialize($votearray);
	$db->update('UPDATE pw_polls SET voteopts='.pwEscape($voteopts,false)."$sql_2 WHERE tid=" . pwEscape($tid));
	if ($pwSQL) {
		$db->update("INSERT INTO pw_voter (tid,uid,username,vote,time) VALUES " . pwSqlMulti($pwSQL,false));
	}
	if ($locked < 3 && $lastpost < $timestamp) {
		$db->update('UPDATE pw_threads SET lastpost='.pwEscape($timestamp).' WHERE tid='.pwEscape($tid));
		# memcache refresh
       	$threadList = L::loadClass("threadlist");
		$threadList->updateThreadIdsByForumId($fid,$tid);
	}
	if ($foruminfo['allowhtm'] == 1) {
		$StaticPage = L::loadClass('StaticPage');
		$StaticPage->update($tid);
	}
	empty($j_p) && $j_p = "read.php?tid=$tid";
	refreshto($j_p,'operate_success');

} elseif ($action == 'reward') {

	!$winduid && Showmsg('undefined_action');
	InitGP(array('pid','type'));
	$rs = $db->get_one('SELECT r.*,t.fid,t.author,t.authorid,t.ptable,t.subject,t.postdate,t.special,t.state FROM pw_reward r LEFT JOIN pw_threads t USING(tid) WHERE r.tid='.pwEscape($tid));

	if (empty($rs) || $rs['special']<>3 || $rs['state']<>0) {
		Showmsg('illegal_tid');
	}
	$pw_posts = GetPtable($rs['ptable']);
	$authorid = $rs['authorid'];
	$author	  = $rs['author'];
	$fid	  = $rs['fid'];
	$authorid!= $winduid && Showmsg('reward_noright');

	$rt = $db->get_one("SELECT tid,fid,author,authorid,ifreward FROM $pw_posts WHERE pid=".pwEscape($pid));
	if ($rt['tid'] != $tid || $rt['authorid'] == $authorid) {
		Showmsg('illegal_tid');
	}

	if (empty($_POST['step'])) {

		require_once(R_P.'require/header.php');
		${'sel_'.$type} = 'checked';
		require_once PrintEot('reward');footer();

	} else {

		PostCheck();
		require_once(R_P.'require/credit.php');
		include_once(D_P.'data/bbscache/forum_cache.php');

		if ($type == '1') {
			$db->update("UPDATE pw_threads SET state='1' WHERE tid=".pwEscape($tid));
			$db->update('UPDATE pw_reward SET '.pwSqlSingle(array('author'=>$rt['author'],'pid'=>$pid)).' WHERE tid='.pwEscape($tid));
			$db->update("UPDATE $pw_posts SET ifreward='2' WHERE pid=".pwEscape($pid));
			$credit->addLog('reward_answer',array($rs['cbtype'] => $rs['cbval']),array(
				'uid'		=> $rt['authorid'],
				'username'	=> $rt['author'],
				'ip'		=> $onlineip,
				'fname'		=> $forum[$fid]['name']
			));
			$credit->set($rt['authorid'],$rs['cbtype'],$rs['cbval'],false); /*最佳答案者加分*/
			$credit->addLog('reward_return',array($rs['cbtype'] => $rs['cbval']),array(
				'uid'		=> $authorid,
				'username'	=> $author,
				'ip'		=> $onlineip,
				'fname'		=> $forum[$fid]['name']
			));
			$credit->set($authorid,$rs['cbtype'],$rs['cbval'],false); /*悬赏者返分*/
			return_value($rt['tid'],$rs['catype'],$rs['caval']);
		} else {
			$rt['ifreward'] && Showmsg('reward_helped');
			$rs['caval'] < 1 && Showmsg('reward_help_error');
			$db->update('UPDATE pw_reward SET caval=caval-1 WHERE tid='.pwEscape($rt['tid']));
			$db->update("UPDATE $pw_posts SET ifreward='1' WHERE pid=".pwEscape($pid));
			$credit->addLog('reward_active',array($rs['catype'] => 1),array(
				'uid'		=> $rt['authorid'],
				'username'	=> $rt['author'],
				'ip'		=> $onlineip,
				'fname'		=> $forum[$fid]['name']
			));
			$credit->set($rt['authorid'],$rs['catype'],1,false);  /*热心助人者加分*/
		}
		$credit->runsql();

		if ($_POST['ifmsg']) {
			require_once(R_P.'require/msg.php');

			if ($type == '1') {
				$affect = $credit->cType[$rs['cbtype']].":".$rs['cbval'];
			} else {
				$affect = $credit->cType[$rs['catype']].":1";
			}
			$msg = array(
				'toUser'		=>$rt['author'],
				'subject'	=> 'reward_title_'.$type,
				'content'	=> 'reward_content_'.$type,
				'other'		=> array(
					'fid'		=> $fid,
					'tid'		=> $rt['tid'],
					'subject'	=> $rs['subject'],
					'postdate'	=> get_date($rs['postdate']),
					'forum'		=> $forum[$fid]['name'],
					'affect'    => $affect,
					'admindate'	=> get_date($timestamp),
					'reason'	=> 'None'
				)
			);
			pwSendMsg($msg);
		}
		refreshto("read.php?tid=$rt[tid]&page=$page",'operate_success');
	}

} elseif ($action == 'endreward') {

	!$winduid && Showmsg('undefined_action');
	require_once(R_P.'require/forum.php');
	InitGP(array('ifmsg','type'));

	$rt = $db->get_one('SELECT r.*,t.fid,t.author,t.authorid,t.postdate,t.fid,t.subject,t.ptable,t.special,t.state,f.forumadmin,f.fupadmin FROM pw_reward r LEFT JOIN pw_threads t ON r.tid=t.tid LEFT JOIN pw_forums f ON t.fid=f.fid WHERE r.tid='.pwEscape($tid));

	if (empty($rt) || $rt['special']<>3 || $rt['state']<>0) {
		Showmsg('illegal_tid');
	}
	$fid	  = $rt['fid'];
	$authorid = $rt['authorid'];
	$author	  = $rt['author'];
	$pw_posts = GetPtable($rt['ptable']);

	if ($groupid!='3' && $groupid!='4' && !admincheck($rt['forumadmin'],$rt['fupadmin'],$windid)) {
		Showmsg('mawhole_right');
	}
	if (empty($_POST['step'])) {

		require_once(R_P.'require/header.php');
		require_once PrintEot('reward');footer();

	} else {

		PostCheck();
		require_once(R_P.'require/credit.php');
		include_once(D_P.'data/bbscache/forum_cache.php');

		if ($type == '1') {
			$db->update("UPDATE pw_threads SET state='2' WHERE tid=".pwEscape($tid));
			$credit->addLog('reward_return',array($rt['cbtype'] => $rt['cbval']*2),array(
				'uid'		=> $authorid,
				'username'	=> $author,
				'ip'		=> $onlineip,
				'fname'		=> $forum[$fid]['name']
			));
			$credit->set($authorid,$rt['cbtype'],$rt['cbval']*2);
		} else {
			if ($timestamp < $rt['timelimit'] && $groupid!='3' && $groupid!='4') {
				Showmsg('reward_time_limit');
			}
			$db->update("UPDATE pw_threads SET state='3' WHERE tid=".pwEscape($tid));
		}
		return_value($tid,$rt['catype'],$rt['caval']);

		if ($ifmsg) {
			require_once(R_P.'require/msg.php');

			if ($type == '1') {
				$affect = $credit->cType[$rt['cbtype']].":".($rt['cbval']*2);
			} else {
				$affect = '';
			}
			$msg = array(
				'toUser'	=> $rt['author'],
				'subject'	=> 'endreward_title_'.$type,
				'content'	=> 'endreward_content_'.$type,
				'other'		=> array(
					'manager'	=> $windid,
					'fid'		=> $fid,
					'tid'		=> $tid,
					'subject'	=> $rt['subject'],
					'postdate'	=> get_date($rt['postdate']),
					'forum'		=> $forum[$fid]['name'],
					'affect'    => $affect,
					'admindate'	=> get_date($timestamp),
					'reason'	=> 'None'
				)
			);
			pwSendMsg($msg);
		}
		refreshto("read.php?tid=$tid",'operate_success');
	}
} elseif ($action == 'rewardmsg') {

	!$winduid && Showmsg('undefined_action');
	$rt = $db->get_one('SELECT r.timelimit,t.fid,t.subject,t.authorid,t.postdate,t.special,t.state,f.forumadmin FROM pw_reward r LEFT JOIN pw_threads t ON r.tid=t.tid LEFT JOIN pw_forums f ON t.fid=f.fid WHERE r.tid='.pwEscape($tid));

	if (empty($rt) || $rt['timelimit'] > $timestamp || $rt['special']<>3 || $rt['state']<>0) {
		Showmsg('illegal_tid');
	}
	$rt['authorid'] != $winduid && Showmsg('reward_noright');
	!$rt['forumadmin'] && Showmsg('reward_no_forumadmin');
	require_once(R_P.'require/msg.php');
	include_once(D_P.'data/bbscache/forum_cache.php');

	$admin_db = explode(',',substr($rt['forumadmin'],1,-1));
	$ifsend = $db->get_one("SELECT g.mid FROM pw_msg g LEFT JOIN pw_msgc mc ON g.mid=mc.mid LEFT JOIN pw_members m ON g.touid=m.uid WHERE g.type='rebox' AND g.fromuid=".pwEscape($winduid,false)."AND m.username=".pwEscape($admin_db[0],false)."AND mc.title LIKE '%:$tid)%'");
	$ifsend && Showmsg('reward_have_sendmsg');
	$msg = array(
		'toUser'	=> $admin_db,
		'fromUid'	=> $winduid,
		'fromUser'	=> $windid,
		'subject'	=> 'rewardmsg_title',
		'content'	=> 'rewardmsg_content',
		'other'		=> array(
			'fid'		=> $rt['fid'],
			'tid'		=> $tid,
			'subject'	=> $rt['subject'],
			'postdate'	=> get_date($rt['postdate']),
			'forum'		=> $forum[$rt['fid']]['name'],
			'admindate'	=> get_date($timestamp),
			'reason'	=> "None"
		)
	);
	pwSendMsg($msg);
	Showmsg('rewardmsg_success');

} elseif ($action == 'taglist') {

	!$db_iftag && Showmsg('tag_closed');
	require_once(R_P.'require/header.php');
	$query = $db->query("SELECT * FROM pw_tags WHERE ifhot='0' ORDER BY num DESC LIMIT 100");
	$tagdb = array();
	while ($rt = $db->fetch_array($query)) {
		$tagdb[] = $rt;
	}
	require_once PrintEot('tag');footer();

} elseif ($action == 'tag') {

	!$db_iftag && Showmsg('tag_closed');
	InitGP(array('tagname','page'));
	$metakeyword = strip_tags($tagname);
	$db_metakeyword = $metakeyword;
	$subject = $metakeyword.' - ';

	require_once(R_P.'require/header.php');
	include_once(D_P.'data/bbscache/forum_cache.php');

	$rs = $db->get_one('SELECT tagid,num FROM pw_tags WHERE tagname='.pwEscape($tagname));
	(!is_numeric($page) || $page<1) && $page = 1;
	$limit = pwLimit(($page-1)*$db_readperpage,$db_readperpage);
	$pages = numofpage($rs['num'],$page,ceil($rs['num']/$db_readperpage), "job.php?action=tag&tagname=".rawurlencode($tagname)."&");
	$query = $db->query('SELECT * FROM pw_tagdata tg LEFT JOIN pw_threads t USING(tid) WHERE tg.tagid='.pwEscape($rs['tagid']).$limit);
	$tiddb = array();
	while ($rt = $db->fetch_array($query)) {
		if ($rt['titlefont']) {
			$titledetail=explode("~",$rt['titlefont']);
			if ($titledetail[0]) $rt['subject'] = "<font color=$titledetail[0]>$rt[subject]</font>";
			if ($titledetail[1]) $rt['subject'] = "<b>$rt[subject]</b>";
			if ($titledetail[2]) $rt['subject'] = "<i>$rt[subject]</i>";
			if ($titledetail[3]) $rt['subject'] = "<u>$rt[subject]</u>";
		}
		if ($rt['special']==1) {
			$p_status = $rt['locked'] == 0 ? 'vote' : 'votelock';
		} elseif ($rt['locked']>0) {
			$p_status = $rt['locked'] == 1 ? 'topiclock' : 'topicclose';
		} else {
			$p_status = $rt['replies']>=10 ? 'topichot' : 'topicnew';
		}
		$rt['status'] = "<img src='$imgpath/$stylepath/thread/".$p_status.".gif' border=0>";

		$rt['forumname'] = $forum[$rt['fid']]['name'];
		$rt['postdate'] = get_date($rt['postdate'],"Y-m-d");
		$rt['lastpost'] = get_date($rt['lastpost']);
		$rt['lastposterraw'] = rawurlencode($rt['lastposter']);
		$rt['anonymous'] && $rt['author']!=$windid && $groupid!='3' && $rt['author']=$db_anonymousname;

		$tiddb[] = $rt;
	}
	require_once PrintEot('tag');footer();

} elseif ($action == 'topost') {

	InitGP(array('pid'));
	if($pid == 'tpc' && is_numeric($tid)) {
		ObHeader("read.php?tid=$tid#tpc");
	}
	$page = '';
	if (!is_numeric($tid) || !is_numeric($pid)) {
		Showmsg('data_error');
	}

	$pw_posts = GetPtable('N',$tid);
	$postdb = $db->get_one("SELECT p.postdate,p.ifreward,t.special,f.forumset FROM $pw_posts p LEFT JOIN pw_threads t ON p.tid=t.tid LEFT JOIN pw_forumsextra f ON p.fid=f.fid WHERE p.pid=".pwEscape($pid));
	!$postdb && Showmsg('data_error');
	$postdate  = $postdb['postdate'];
	$forumset  = unserialize($postdb['forumset']);
	$special   = $postdb['special'];
	$ifreward  = $postdb['ifreward'];

	if ($winddb['p_num']) {
		$db_readperpage = $winddb['p_num'];
	} elseif ($forumset['readnum']) {
		$db_readperpage = $forumset['readnum'];
	}

	if ($special) {
		if ($ifreward == 1) {
			$postsnum = $db->get_value("SELECT COUNT(*) FROM $pw_posts WHERE tid=".pwEscape($tid)." AND postdate<=".pwEscape($postdate)." AND ifreward<>'0'");
			$postsnumadd = $db->get_value("SELECT COUNT(*) FROM $pw_posts WHERE tid=".pwEscape($tid)." AND postdate>".pwEscape($postdate)." AND ifreward='2'");
			$postsnum = $postsnum + $postsnumadd;
		} elseif ($ifreward == 2) {
			$page = 1;
		} else {
			$postsnum = $db->get_value("SELECT COUNT(*) FROM $pw_posts WHERE tid=".pwEscape($tid)." AND postdate<=".pwEscape($postdate));
			$postsnumadd = $db->get_value("SELECT COUNT(*) FROM $pw_posts WHERE tid=".pwEscape($tid)." AND postdate>".pwEscape($postdate)." AND ifreward<>'0'");
			$postsnum = $postsnum + $postsnumadd;
		}
	} else {
		$postsnum = $db->get_value("SELECT COUNT(*) FROM $pw_posts WHERE tid=".pwEscape($tid)." AND postdate<=".pwEscape($postdate));
	}
	empty($page) && $page = ceil(($postsnum+1)/$db_readperpage);
	ObHeader("read.php?tid=$tid&page=$page#$pid");

} elseif ($action == 'birth') {

	require_once(R_P.'require/header.php');
	require_once(R_P.'require/birth.php');
	require_once PrintEot('birth');footer();

} elseif ($action == 'erasecookie') {

	checkVerify('loginhash');
	$cookiepre = CookiePre().'_';
	foreach ($_COOKIE as $key => $value) {
		if (strpos($key,$cookiepre)===0) {
			Cookie(substr($key,strlen($cookiepre)),'',0);
		}
	}
	$referer = strpos($pwServer['HTTP_REFERER'],$db_bbsurl)===0 ? $pwServer['HTTP_REFERER'] : $db_bbsurl.'/'.$db_bfn;
	ObHeader($referer);
} elseif ($action == 'pcexport') {
	InitGP(array('tid','pcid'),G,2);
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

	$memberdb = array();
	$query = $db->query("SELECT username,mobile,phone,address,nums,ifpay,totalcash,name,zip,message FROM pw_pcmember WHERE tid=".pwEscape($tid));
	while ($rt = $db->fetch_array($query)) {
		if ($rt['ifpay'] == 1) {
			$rt['ifpay'] = getLangInfo('other','pc_payed');
		} else {
			$rt['ifpay'] = getLangInfo('other','pc_paying');
		}
		if ($db_charset == 'utf-8' || $db_charset == 'big5'){
			foreach($rt as $key => $value) {
				$rt[$key] = pwConvert($value,'gbk',$db_charset);
			}
		}
		$memberdb[] = $rt;
	}

	$titledb = array(getLangInfo('other','pc_id')."\t",getLangInfo('other','pc_username')."\t",getLangInfo('other','pc_name')."\t",getLangInfo('other','pc_mobile')."\t",getLangInfo('other','pc_phone')."\t",getLangInfo('other','pc_address')."\t",getLangInfo('other','pc_zip')."\t",getLangInfo('other','pc_nums')."\t",getLangInfo('other','pc_totalcash')."\t",getLangInfo('other','pc_message')."\t",getLangInfo('other','pc_ifpay')."\t\n");

	header("Content-type:application/vnd.ms-excel");
	header("Content-Disposition:attachment;filename=$read[subject].xls");
	header("Pragma: no-cache");
	header("Expires: 0");

	foreach ($titledb as $key => $value) {

		if ($db_charset == 'utf-8' || $db_charset == 'big5'){
			$value = pwConvert($value,'gbk',$db_charset);
		}
		echo $value;

	}

	$i = 0;
	foreach ($memberdb as $val) {
		$i++;
		$val['message'] = str_replace("\n","",$val['message']);
		echo "$i\t";
		echo "$val[username]\t";
		echo "$val[name]\t";
		echo "$val[mobile]\t";
		echo "$val[phone]\t";
		echo "$val[address]\t";
		echo "$val[zip]\t";
		echo "$val[nums]\t";
		echo "$val[totalcash]\t";
		echo "$val[message]\t";
		echo "$val[ifpay]\t\n";
	}
	exit;
} elseif ($action == 'pcjoin') {
	InitGP(array('tid'),G,2);
	if (!$winduid) Showmsg('not_login');
}

function fseeks($fp,$dbtdsize,$seed) {
	$break = $num = 0;
	while ($break!=1 && $num<$seed) {
		$num++;
		$sdata=fread($fp,$dbtdsize);
		$sdb=explode("\t",$sdata);
		$sdbnext=$sdb[2]*$dbtdsize;
		if ($sdbnext!='NULL') {
			fseek($fp,$sdbnext,SEEK_SET);
		} else {
			$break = 1;
		}
		$todayshow[] = $sdata;
	}
	return $todayshow;
}

function return_value($tid,$rw_a_name,$rw_a_val) {
	global $db,$pw_posts,$authorid,$author,$onlineip,$forum,$fid,$credit;

	if ($rw_a_val < 1) {
		return ;
	}
	$p_a = $u_a = array();
	$query = $db->query("SELECT pid,author,authorid FROM $pw_posts WHERE tid=".pwEscape($tid)." AND ifreward='0' AND authorid!=".pwEscape($authorid)." GROUP BY authorid ORDER BY postdate ASC LIMIT $rw_a_val");
	while ($user = $db->fetch_array($query)) {
		$credit->addLog('reward_active',array($rw_a_name => 1),array(
			'uid'		=> $user['authorid'],
			'username'	=> $user['author'],
			'ip'		=> $onlineip,
			'fname'		=> $forum[$fid]['name']
		));
		$p_a[] = $user['pid'];
		$u_a[] = $user['authorid'];
		$rw_a_val--;
	}
	$p_a && $db->update("UPDATE $pw_posts SET ifreward='1' WHERE pid IN(" . pwImplode($p_a) . ')');
	$u_a && $credit->setus($u_a,array($rw_a_name => 1),false);
	if ($rw_a_val > 0) {
		$credit->addLog('reward_return',array($rw_a_name => $rw_a_val),array(
			'uid'		=> $authorid,
			'username'	=> $author,
			'ip'		=> $onlineip,
			'fname'		=> $forum[$fid]['name']
		));
		$credit->set($authorid,$rw_a_name,$rw_a_val,false);
	}
}

function getuserdb($filename,$offset) {
	global $db_olsize;
	if (!$offset || $offset%($db_olsize+1)!=0) {
		return false;
	} else {
		$fp = fopen($filename,"rb");
		flock($fp,LOCK_SH);
		fseek($fp,$offset);
		$Checkdata = fread($fp,$db_olsize);
		fclose($fp);
		return $Checkdata;
	}
}

function checkCreditLimit($creditlimit) {
	global $winddb,$winduid,$db;
	$creditlimit = unserialize($creditlimit);
	foreach ($creditlimit as $key => $value) {
		if (in_array($key,array('money','rvrc','credit','currency'))) {
			$winddb[$key] < $value && Showmsg('job_vote_creditlimit');
		} else {
			$user_credit = $db ->get_value("SELECT value FROM pw_membercredit WHERE uid=".pwEscape($winduid)." AND cid=".pwEscape($key));
			$user_credit < $value && Showmsg('job_vote_creditlimit');
		}
	}
}

function getPcviewdata ($pcinfo,$pctype) {
	global $fielddb;
	foreach(explode("{|}",$pcinfo) as $val) {
		if (strpos($val,'topic[') !== false || strpos($val,'postcate[') !== false) {
			$name = $value = $type = $fieldid = '';
			$fieldb = array();
			$val = str_replace('&#41;',')',$val);
			list($name,$value,$type) = explode("(|)",$val);

			if ($pctype == 'topic') {
				preg_match("/topic\[(\d+)\]/",$name,$fieldata);
				$fieldid = $fieldata['1'];
				if ($fieldid) {
					if ($fieldid != $newid) {
						strpos($type,'calendar_') !== false && $value = PwStrtoTime($value);
						$pcdb[$fielddb[$fieldid]] = $value;
					} elseif ($fieldid == $newid) {
						$pcdb[$fielddb[$fieldid]] .= ','.$value;
					}
				}
				$newid = $fieldid;
			} elseif ($pctype == 'postcate') {
				preg_match("/postcate\[(.+?)\]/",$name,$fieldata);
				$fieldname = $fieldata['1'];
				if ($fieldname) {
					if ($fielddb[$fieldname] != $newid) {
							strpos($type,'calendar_') !== false && $value = PwStrtoTime($value);
							$pcdb[$fieldname] = $value;
					} elseif ($fielddb[$fieldname] == $newid) {
						$pcdb[$fieldname] .= ','.$value;
					}
					$newid = $fielddb[$fieldname];
				}
			}
		}
	}

	return $pcdb;
}
?>