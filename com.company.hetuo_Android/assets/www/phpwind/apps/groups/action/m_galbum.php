<?php
!defined('A_P') && exit('Forbidden');
!$winduid && Showmsg('not_login');

$a_key = 'galbum';

if ($db_question && $o_share_qcheck) {
	$qkey = array_rand($db_question);
}


InitGP(array('a'));
InitGP(array('cyid'), null, 2);

$colony = $db->get_one("SELECT c.*,cm.id AS ifcyer,cm.ifadmin FROM pw_colonys c LEFT JOIN pw_cmembers cm ON c.id=cm.colonyid AND cm.uid=" . pwEscape($winduid) . ' WHERE c.id=' . pwEscape($cyid));
empty($colony) && Showmsg('data_error');
$colony['createtime'] = get_date($colony['createtime']);
$atc_name = getLangInfo('app','group');
$title = "$colony[cname] ($db_bbsurl/{$baseUrl}q=group&cyid=$cyid)";
$descrip = $colony['descrip'];

if ($colony['cnimg']) {
	list($cnimg,$colony['imgtype']) = geturl("cn_img/$colony[cnimg]",'lf');
} else {
	$cnimg = $pwModeImg.'/groupnopic.gif';
}
if ($colony['banner']) {
	list($colony['banner']) = geturl("cn_img/$colony[banner]",'lf');
}
$ifadmin = ($colony['ifadmin'] == '1' || $colony['admin'] == $windid || $groupid == 3);

if (empty($a)) {

	if (!$colony['albumopen'] && !$ifadmin && (!$colony['ifcyer'] || $colony['ifadmin'] == '-1')) {
		Showmsg('colony_cnmenber');
	}
	$photonum = $db->get_value("SELECT SUM(photonum) AS photonum FROM pw_cnalbum WHERE ownerid=".pwEscape($cyid));
	InitGP(array('page'), null, 2);
	$db_perpage = 10;
	list($pages,$limit) = pwLimitPages($colony['albumnum'],$page,"{$basename}&a=$a&");
	$album = array();
	$query = $db->query("SELECT aid,aname,photonum,lastphoto,private,lasttime FROM pw_cnalbum WHERE atype='1' AND ownerid=" . pwEscape($cyid) . " ORDER BY aid DESC $limit");
	while ($rt = $db->fetch_array($query)) {
		$rt['sub_aname'] = substrs($rt['aname'],18);
		$rt['lasttime'] = get_date($rt['lasttime'],'Y-m-d');
		$rt['forbidden'] = ($colony['albumopen'] && $rt['private'] && !$ifadmin && (!$colony['ifcyer'] || $colony['ifadmin'] == '-1'));
		$rt['lastphoto'] = $rt['forbidden'] ? "$pwModeImg/n.gif" : getphotourl($rt['lastphoto']);
		$album[] = $rt;
	}

//	require_once(M_P.'require/header.php');
//	require_once PrintEot('m_galbum');
//	footer();
	list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_galbum",true);

} elseif ($a == 'album') {

	if (!$colony['albumopen'] && !$ifadmin && (!$colony['ifcyer'] || $colony['ifadmin'] == '-1')) {
		Showmsg('colony_cnmenber');
	}

	InitGP(array('aid'), null, 2);

	$cnpho = array();
	$album = $db->get_one("SELECT aname,aintro,ownerid,photonum,private FROM pw_cnalbum WHERE atype='1' AND aid=" . pwEscape($aid));
	if (empty($album)) {
		Showmsg('data_error');
	}
	if ($colony['albumopen'] && $album['private'] && !$ifadmin && (!$colony['ifcyer'] || $colony['ifadmin'] == '-1')) {
		Showmsg('colony_cnmenber');
	}

	$query = $db->query("SELECT c.pid,c.path,c.ifthumb,c.pintro,c.uptime,m.groupid FROM pw_cnphoto c LEFT JOIN pw_members m ON c.uploader=m.username WHERE c.aid=" . pwEscape($aid) . ' ORDER BY c.pid ' . pwLimit(0, $o_maxphotonum));
	while ($rt = $db->fetch_array($query)) {
		$rt['path'] = getphotourl($rt['path'], $rt['ifthumb']);
		if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
			$rt['path'] = $pwModeImg.'/banuser.gif';
		}
		$rt['uptime'] = get_date($rt['uptime']);
		$rt['sub_pintro'] = substrs($rt['pintro'],25);
		$cnpho[] = $rt;
	}
//	require_once(M_P.'require/header.php');
//	require_once PrintEot('m_galbum');
//	footer();
	list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_galbum",true);

} elseif ($a == 'view') {

	if (!$colony['albumopen'] && !$ifadmin && (!$colony['ifcyer'] || $colony['ifadmin'] == '-1')) {
		Showmsg('colony_cnmenber');
	}

	InitGP(array('pid'), null, 2);

	$db->update("UPDATE pw_cnphoto SET hits=hits+1 WHERE pid=" . pwEscape($pid));

	//$photo = $db->get_one("SELECT p.pid,p.aid,p.pintro,p.path as basepath,p.ifthumb,p.uploader,p.uptime,p.hits,a.aname,a.atype, a.private,a.ownerid,a.photonum,m.groupid FROM pw_cnphoto p LEFT JOIN pw_cnalbum a ON p.aid=a.aid LEFT JOIN pw_members m ON p.uploader=m.username WHERE p.pid=" . pwEscape($pid));
        # album photos
	$nearphoto = array();
        $register = array('db_shield'=>$db_shield,"groupid"=>$groupid,"pwModeImg"=>$pwModeImg);
        require_once R_P.'lib/showpicture.class.php';
        $sp = new PW_ShowPicture($register);
        list($photo,$nearphoto,$prePid,$nextPid) = $sp->getGroupsPictures($pid,$aid);

        empty($photo) && Showmsg('data_error');
	if ($colony['albumopen'] && $photo['private'] && !$ifadmin && (!$colony['ifcyer'] || $colony['ifadmin'] == '-1')) {
		Showmsg('colony_cnmenber');
	}
	$aid = $photo['aid'];
	$photo['uptime'] = get_date($photo['uptime']);
	$photo['path'] = getphotourl($photo['basepath']);
	if ($photo['groupid'] == 6 && $db_shield && $groupid != 3) {
		$photo['path'] = $pwModeImg.'/banuser.gif';
		$photo['pintro'] = appShield('ban_photo_pintro');
	}
	$num = $db->get_value("SELECT COUNT(*) AS sum FROM pw_cnphoto WHERE aid=" . pwEscape($photo['aid']) . ' AND pid<=' . pwEscape($pid));

//	$nearphoto = array();
//	$up_photo = $db->get_one("SELECT p.pid,p.path,p.ifthumb,m.groupid FROM pw_cnphoto p LEFT JOIN pw_cnalbum a ON p.aid=a.aid LEFT JOIN pw_members m ON p.uploader=m.username WHERE p.pid<".pwEscape($pid)." AND p.aid=".pwEscape($photo['aid'])." ORDER BY pid DESC");
//	if ($up_photo) {
//		$up_photo['path'] = getphotourl($up_photo['path'],$up_photo['ifthumb']);
//		if ($up_photo['groupid'] == 6 && $db_shield && $groupid != 3) {
//			$up_photo['path'] = $pwModeImg.'/banuser.gif';
//		}
//		$nearphoto[] = $up_photo;
//	} else {
//		$nearphoto[] = array('pid'=>'begin','path'=>'images/apps/pbegin.jpg');
//	}
//	$thumb_basepath = getphotourl($photo['basepath'],$photo['ifthumb']);
//
//	if ($photo['groupid'] == 6 && $db_shield && $groupid != 3) {
//		$thumb_basepath = $pwModeImg.'/banuser.gif';
//	}
//	$nearphoto[] = array('pid'=>$pid,'path'=>$thumb_basepath);
//
//	$next_photo = $db->get_one("SELECT p.pid,p.path,p.ifthumb,m.groupid FROM pw_cnphoto p LEFT JOIN pw_cnalbum a ON p.aid=a.aid LEFT JOIN pw_members m ON p.uploader=m.username WHERE p.pid>".pwEscape($pid)." AND p.aid=".pwEscape($photo['aid'])." ORDER BY pid");
//	if ($next_photo) {
//		$next_photo['path'] = getphotourl($next_photo['path'],$next_photo['ifthumb']);
//		if ($next_photo['groupid'] == 6 && $db_shield && $groupid != 3) {
//			$next_photo['path'] = $pwModeImg.'/banuser.gif';
//		}
//		$nearphoto[] = $next_photo;
//	} else {
//		$nearphoto[] = array('pid'=>'end','path'=>'images/apps/pend.jpg');
//	}

//	require_once(M_P.'require/header.php');
//	require_once PrintEot('m_galbum');
//	footer();
	list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_galbum",true);

} elseif ($a == 'editphoto') {

	if (!$ifadmin && (!$colony['ifcyer'] || $colony['ifadmin'] == '-1')) {
		Showmsg('colony_cnmenber');
	}

	banUser();
	InitGP(array('pid'), null, 2);

	$photo = $db->get_one("SELECT p.aid,p.pintro,a.ownerid FROM pw_cnphoto p LEFT JOIN pw_cnalbum a ON p.aid=a.aid WHERE pid=" . pwEscape($pid));
	if (empty($photo)) {
		Showmsg('data_error');
	}
	if (empty($_POST['step'])) {

		$options = '';
		$query = $db->query("SELECT aid,aname FROM pw_cnalbum WHERE atype='1' AND ownerid=" . pwEscape($cyid) . ' ORDER BY aid DESC');
		while ($rt = $db->fetch_array($query)) {
			$options .= "<option value=\"$rt[aid]\"" . (($rt['aid'] == $photo['aid']) ? ' selected' : '') . ">$rt[aname]</option>";
		}
//		require_once(M_P.'require/header.php');
//		require_once PrintEot('m_galbum');
//		footer();
		list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_galbum",true);

	} else {

		InitGP(array('pintro'),'P');
		InitGP(array('aid'), null, 2);
		!$aid && Showmsg('colony_albumclass');

		require_once(R_P.'require/postfunc.php');
		require_once(R_P.'require/bbscode.php');

		$wordsfb = L::loadClass('FilterUtil');
		if (($banword = $wordsfb->comprise($pintro)) !== false) {
			Showmsg('post_wordsfb');
		}
		$pwSQL = array('pintro' => $pintro);

		$ischage = false;
		if ($aid != $photo['aid'] && $winduid == $db->get_value("SELECT ownerid FROM pw_cnalbum WHERE aid=" . pwEscape($aid))) {
			$pwSQL['aid'] = $aid;
			$ischage = true;
		}
		$db->update("UPDATE pw_cnphoto SET " . pwSqlSingle($pwSQL) . ' WHERE pid=' . pwEscape($pid));

		if ($ischage) {
			$phnum = array();
			$query = $db->query("SELECT aid,COUNT(*) AS sum FROM pw_cnphoto WHERE aid IN(" . pwImplode(array($aid,$photo['aid'])) . ') GROUP BY aid');
			while ($rt = $db->fetch_array($query)) {
				$phnum[$rt['aid']] = $rt['sum'];
			}
			$db->update("UPDATE pw_cnalbum SET " . pwSqlSingle(array('photonum' => $phnum[$aid] ? $phnum[$aid] : 0, 'lastpid' => implode(',',getLastPid($aid)))) . ' WHERE aid=' . pwEscape($aid));

			$db->update("UPDATE pw_cnalbum SET " . pwSqlSingle(array('photonum' => $phnum[$photo['aid']] ? $phnum[$photo['aid']] : 0, 'lastpid' => implode(',',getLastPid($photo['aid'])))) . ' WHERE aid=' . pwEscape($photo['aid']));
		}

		refreshto("{$basename}a=view&cyid=$cyid&pid=$pid",'operate_success');
	}
} elseif ($a == 'upload') {

	if (!$ifadmin && (!$colony['ifcyer'] || $colony['ifadmin'] == '-1')) {
		Showmsg('colony_cnmenber');
	}

	banUser();
	InitGP(array('aid', 'job'));

	if (empty($_POST['step'])) {

		$extra_url = $options = '';
		$query = $db->query("SELECT aid,aname FROM pw_cnalbum WHERE atype='1' AND ownerid=" . pwEscape($cyid) . ' ORDER BY aid DESC');
		while ($rt = $db->fetch_array($query)) {
			$options .= "<option value=\"$rt[aid]\"" . (($aid && $rt['aid'] == $aid) ? ' selected' : '') . ">$rt[aname]</option>";
		}
		$aid && $extra_url = '&aid=' . $aid;
//		require_once(M_P.'require/header.php');
//		require_once PrintEot('m_galbum');
//		footer();
		list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_galbum",true);

	} else {

		InitGP(array('pintro'),'P');
		!$aid && Showmsg('colony_albumclass');

		PostCheck(1,$o_photos_gdcheck,$o_photos_qcheck);
		empty($pintro) && $pintro = array();

		require_once(R_P.'require/bbscode.php');
		$wordsfb = L::loadClass('FilterUtil');
		foreach ($pintro as $k => $v) {
			if (($banword = $wordsfb->comprise($v)) !== false) {
				Showmsg('content_wordsfb');
			}
		}
		$rt = $db->get_one("SELECT aname,photonum,ownerid,lastphoto FROM pw_cnalbum WHERE atype='1' AND aid=" . pwEscape($aid));
		if (empty($rt)) {
			Showmsg('undefined_action');
		} elseif ($cyid <> $rt['ownerid']) {
			Showmsg('colony_phototype');
		}
		$rt['photonum'] >= $o_maxphotonum && Showmsg('colony_photofull');

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

		$db->update("UPDATE pw_cnalbum SET photonum=photonum+" . pwEscape($photoNum) . ",lasttime=" . pwEscape($timestamp) . ',lastpid=' . pwEscape(implode(',',$lastpid)) . (!$rt['lastphoto'] ? ',lastphoto='.pwEscape($img->getLastPhoto()) : '') . " WHERE aid=" . pwEscape($aid));
		countPosts("+$photoNum");

		$feedText = '';
		foreach ($photos as $value) {
			$feedText .= "[url=$db_bbsurl/{#APPS_BASEURL#}q=galbum&a=view&cyid=$cyid&pid=$pid][img]".getphotourl($value['path'], $value['ifthumb'])."[/img][/url]&nbsp;";
		}
		pwAddFeed($winduid, 'colony_photo', $cyid, array(
			'lang'	=> 'colony_photo',
			'cyid'	=> $cyid,
			'num'	=> $photoNum,
			'colony_name'	=> $colony['cname'],
			'text'	=> $feedText
		));

		//积分变动
		require_once(R_P.'require/credit.php');
		$o_groups_creditset = unserialize($o_groups_creditset);
		$creditset = getCreditset($o_groups_creditset['Uploadphoto']);
		$creditset = array_diff($creditset,array(0));
		if (!empty($creditset)) {
			$credit->sets($winduid,$creditset,true);
			updateMemberid($winduid);
			addLog($creditlog,$windid,$winduid,'groups_Uploadphoto');
		}

		if ($creditlog = unserialize($o_groups_creditlog)) {
			addLog($creditlog['Post'],$windid,$winduid,'groups_Uploadphoto');
		}
		refreshto("{$basename}a=view&cyid=$cyid&pid=$pid",'operate_success');
	}
} elseif ($a == 'selalbum') {

	if (!$ifadmin && (!$colony['ifcyer'] || $colony['ifadmin'] == '-1')) {
		Showmsg('colony_cnmenber');
	}
	InitGP(array('page', 'selaid'), null, 2);
	$db_perpage = 10;
	$total = $db->get_value("SELECT COUNT(*) FROM pw_cnalbum WHERE atype='0' AND ownerid=" . pwEscape($winduid));
	list($pages,$limit) = pwLimitPages($total, $page, "{$basename}&a=$a&");

	$album = array();
	$query = $db->query("SELECT aid,aname,photonum,lastphoto FROM pw_cnalbum WHERE atype='0' AND ownerid=" . pwEscape($winduid) . " ORDER BY aid DESC $limit");
	while ($rt = $db->fetch_array($query)) {
		$rt['sub_aname'] = substrs($rt['aname'],18);
		$rt['lastphoto'] = getphotourl($rt['lastphoto']);
		$album[] = $rt;
	}
	$url_extra = $selaid ? "&selaid=$selaid" : '';

	list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_galbum",true);

} elseif ($a == 'selphoto') {
	if (!$ifadmin && (!$colony['ifcyer'] || $colony['ifadmin'] == '-1')) {
		Showmsg('colony_cnmenber');
	}
	InitGP(array('aid', 'selaid'));
	$album = $db->get_one("SELECT aname,ownerid,photonum FROM pw_cnalbum WHERE atype='0' AND aid=" . pwEscape($aid));
	if (empty($album) || $album['ownerid'] != $winduid) {
		Showmsg('data_error');
	}
	if (empty($_POST['step'])) {

		$options = '';
		$query = $db->query("SELECT aid,aname FROM pw_cnalbum WHERE atype='1' AND ownerid=" . pwEscape($cyid) . ' ORDER BY aid DESC');
		while ($rt = $db->fetch_array($query)) {
			$options .= "<option value=\"$rt[aid]\"" . ($rt['aid'] == $selaid ? ' selected' : '') . ">$rt[aname]</option>";
		}

		$cnpho = array();
		$query = $db->query("SELECT pid,path,ifthumb FROM pw_cnphoto WHERE aid=" . pwEscape($aid) . ' ORDER BY pid ' . pwLimit(0, $o_maxphotonum));
		while ($rt = $db->fetch_array($query)) {
			$rt['path'] = getphotourl($rt['path'], $rt['ifthumb']);
			$cnpho[] = $rt;
		}
		list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_galbum",true);

	} else {
		InitGP(array('selid'));
		if (!$selid || !is_array($selid)) {
			Showmsg('colony_select_photo');
		}
		
		if (empty($selaid)) { 
			Showmsg('colony_albumclass');
		}
		
		$selalbum = $db->get_one("SELECT aname,photonum,ownerid,lastphoto FROM pw_cnalbum WHERE atype='1' AND aid=" . pwEscape($selaid));
		if (empty($selalbum)) {
			Showmsg('undefined_action');
		} elseif ($cyid <> $selalbum['ownerid']) {
			Showmsg('colony_phototype');
		}
		$selalbum['photonum'] >= $o_maxphotonum && Showmsg('colony_photofull');
		require_once(R_P . 'lib/upload.class.php');

		$savedir = 'photo/';
		if ($o_mkdir == '2') {
			$savedir .= 'Day_' . date('ymd') . '/';
		} elseif ($o_mkdir == '3') {
			$savedir .= 'Cyid_' . $selaid . '/';
		} else {
			$savedir .= 'Mon_'.date('ym') . '/';
		}
		$lastphoto = '';
		$i = 1;
		$photos = array();
		$query = $db->query("SELECT * FROM pw_cnphoto WHERE aid=" . pwEscape($aid) . ' AND pid IN(' . pwImplode($selid) . ')');
		while ($rt = $db->fetch_array($query)) {
			if (file_exists($attachdir . '/' . $rt['path'])) {
				$ext = strtolower(substr(strrchr($rt['path'],'.'),1));
				$prename  = randstr(4) . $timestamp . substr(md5($timestamp . ($i++) . randstr(8)),10,15);
				$filename = $selaid . "_$prename." . $ext;
				PwUpload::createFolder($attachdir . '/' . $savedir);
				$ifthumb = 0;
				if (!@copy($attachdir . '/' . $rt['path'], $attachdir . '/' . $savedir . $filename)) {
					continue;
				}
				if ($rt['ifthumb']) {
					$lastpos = strrpos($rt['path'],'/') + 1;
					$path = $attachdir . '/' . substr($rt['path'], 0, $lastpos) . 's_' . substr($rt['path'], $lastpos);
					if (copy($path, $attachdir . '/' . $savedir . 's_' . $filename)) {
						$ifthumb = 1;
					}
				}
				$path = $savedir . $filename;
			} else {
				$path = $rt['path'];
				$ifthumb = $rt['ifthumb'];
			}
			$photos[] = array(
				'aid'		=> $selaid,
				'pintro'	=> '',
				'path'		=> $path,
				'uploader'	=> $windid,
				'uptime'	=> $timestamp,
				'ifthumb'	=> $ifthumb
			);
			$lastphoto = $path;
		}

		if ($photos) {

			$db->update("INSERT INTO pw_cnphoto (aid,pintro,path,uploader,uptime,ifthumb) VALUES " . pwSqlMulti($photos));
			$pid = $db->insert_id();

			$photoNum = count($photos);
			$lastpid = getLastPid($selaid, 4);
			array_unshift($lastpid, $pid);

			$db->update("UPDATE pw_cnalbum SET photonum=photonum+" . pwEscape($photoNum) . ",lasttime=" . pwEscape($timestamp) . ',lastpid=' . pwEscape(implode(',',$lastpid)) . (!$selalbum['lastphoto'] ? ',lastphoto='.pwEscape($lastphoto) : '') . " WHERE aid=" . pwEscape($selaid));
			countPosts("+$photoNum");

			$feedText = '';
			foreach ($photos as $value) {
				$feedText .= "[url=$db_bbsurl/{#APPS_BASEURL#}space=1&q=galbum&a=view&cyid=$cyid&pid=$pid][img]" . getphotourl($value['path'], $value['ifthumb'])."[/img][/url]&nbsp;";
			}
			pwAddFeed($winduid, 'colony_photo', $cyid, array(
				'lang'	=> 'colony_photo',
				'cyid'	=> $cyid,
				'num'	=> $photoNum,
				'colony_name'	=> $colony['cname'],
				'text'	=> $feedText
			));
			//积分变动
			require_once(R_P.'require/credit.php');
			$o_groups_creditset = unserialize($o_groups_creditset);
			$creditset = getCreditset($o_groups_creditset['Uploadphoto']);
			$creditset = array_diff($creditset,array(0));
			if (!empty($creditset)) {
				$credit->sets($winduid,$creditset,true);
				updateMemberid($winduid);
				addLog($creditlog,$windid,$winduid,'groups_Uploadphoto');
			}
			if ($creditlog = unserialize($o_groups_creditlog)) {
				addLog($creditlog['Post'],$windid,$winduid,'groups_Uploadphoto');
			}
			refreshto("{$basename}a=view&cyid=$cyid&pid=$pid",'operate_success');

		} else {
			refreshto("{$basename}a=album&cyid=$cyid&aid=$selaid",'operate_success');
		}
	}

} elseif ($a == 'delphoto') {

	if (!$ifadmin && (!$colony['ifcyer'] || $colony['ifadmin'] == '-1')) {
		Showmsg('colony_cnmenber');
	}

	define('AJAX','1');
	InitGP(array('pid'), null, 2);
	$photo = $db->get_one("SELECT cp.uploader,cp.path,ca.aid,ca.lastphoto,ca.lastpid,m.uid FROM pw_cnphoto cp LEFT JOIN pw_cnalbum ca ON cp.aid=ca.aid LEFT JOIN pw_members m ON cp.uploader=m.username WHERE cp.pid=" . pwEscape($pid) . " AND ca.atype='1' AND ca.ownerid=" . pwEscape($cyid));
	if (empty($photo) || (!$ifadmin && $photo['uploader'] != $windid)) {
		Showmsg('data_error');
	}
	$db->update("DELETE FROM pw_cnphoto WHERE pid=" . pwEscape($pid));

	$pwSQL = array();
	if ($photo['path'] == $photo['lastphoto']) {
		$pwSQL['lastphoto'] = $db->get_value("SELECT path FROM pw_cnphoto WHERE aid=" . pwEscape($photo['aid']) . " ORDER BY pid DESC LIMIT 1");
	}
	if (strpos(",$photo[lastpid],",",$pid,") !== false) {
		$pwSQL['lastpid'] = implode(',',getLastPid($photo['aid']));
	}
	$upsql = $pwSQL ? ',' . pwSqlSingle($pwSQL) : '';
	$db->update("UPDATE pw_cnalbum SET photonum=photonum-1{$upsql} WHERE aid=" . pwEscape($photo['aid']));

	pwDelatt($photo['path'], $db_ifftp);
	$lastpos = strrpos($photo['path'],'/') + 1;
	pwDelatt(substr($photo['path'], 0, $lastpos) . 's_' . substr($photo['path'], $lastpos), $db_ifftp);
	pwFtpClose($ftp);
	$affected_rows = delAppAction('photo',$pid)+1;
	countPosts("-$affected_rows");

	//积分变动
	require_once(R_P.'require/credit.php');
	$o_groups_creditset = unserialize($o_groups_creditset);
	$creditset = getCreditset($o_groups_creditset['Deletephoto'],false);
	$creditset = array_diff($creditset,array(0));
	if (!empty($creditset)) {
		require_once(R_P.'require/postfunc.php');
		$credit->sets($photo['uid'],$creditset,true);
		updateMemberid($photo['uid'],false);
	}

	if ($creditlog = unserialize($o_groups_creditlog)) {
		addLog($creditlog['Deletephoto'],$photo['uploader'],$photo['uid'],'groups_Deletephoto');
	}

	echo 'ok';ajax_footer();

} elseif ($a == 'delalbum') {

	if (!$ifadmin && (!$colony['ifcyer'] || $colony['ifadmin'] == '-1')) {
		Showmsg('colony_cnmenber');
	}

	define('AJAX', 1);
	define('F_M',true);
	InitGP(array('aid'), null, 2);
	!$ifadmin && Showmsg('undefined_action');
	$album = $db->get_one("SELECT * FROM pw_cnalbum WHERE aid=" . pwEscape($aid) . " AND atype='1'");

	if (empty($album) || $album['ownerid'] != $cyid) {
		Showmsg('data_error');
	}

	if (empty($_POST['step'])) {
		require_once PrintEot('m_ajax');
		ajax_footer();

	} else {

		$query = $db->query("SELECT pid,path,ifthumb FROM pw_cnphoto WHERE aid=" . pwEscape($aid));
		if (($num = $db->num_rows($query)) > 0) {
			$affected_rows = 0;
			while ($rt = $db->fetch_array($query)) {
				pwDelatt($rt['path'], $db_ifftp);
				if ($rt['ifthumb']) {
					$lastpos = strrpos($rt['path'],'/') + 1;
					pwDelatt(substr($rt['path'], 0, $lastpos) . 's_' . substr($rt['path'], $lastpos), $db_ifftp);
				}
				$affected_rows += delAppAction('photo',$rt['pid'])+1;
			}
			pwFtpClose($ftp);
			countPosts("-$affected_rows");
		}
		$db->update("DELETE FROM pw_cnphoto WHERE aid=" . pwEscape($aid));
		$db->update("DELETE FROM pw_cnalbum WHERE aid=" . pwEscape($aid));
		$db->update("UPDATE pw_colonys SET albumnum=albumnum-1 WHERE id=" . pwEscape($cyid));

		echo getLangInfo('msg','operate_success') . "\tjump\t{$basename}cyid=$cyid";
		ajax_footer();
	}
} elseif ($a == 'edit') {

	if (!$ifadmin && (!$colony['ifcyer'] || $colony['ifadmin'] == '-1')) {
		Showmsg('colony_cnmenber');
	}

	banUser();
	InitGP(array('aid'), null, 2);
	empty($aid) && Showmsg('data_error');
	!$ifadmin && Showmsg('undefined_action');

	$rt = $db->get_one("SELECT aid,aname,aintro,private FROM pw_cnalbum WHERE aid=" . pwEscape($aid) . " AND atype='1' AND ownerid=" . pwEscape($cyid));
	if (empty($rt)) {
		Showmsg('data_error');
	}
	if (empty($_POST['step'])) {

		$extra_url = '&a=album&aid=' . $aid;
		${'select_'.$rt['private']} = 'selected';
//		require_once(M_P.'require/header.php');
//		require_once PrintEot('m_galbum');
//		footer();
		list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_galbum",true);

	} else {

		InitGP(array('aname','aintro'),'P');
		InitGP(array('private'),'P',2);
		!$aname && Showmsg('colony_aname_empty');

		require_once(R_P.'require/bbscode.php');
		$wordsfb = L::loadClass('FilterUtil');
		if (($banword = $wordsfb->comprise($aname)) !== false) {
			Showmsg('title_wordsfb');
		}
		if (($banword = $wordsfb->comprise($aintro)) !== false) {
			Showmsg('post_wordsfb');
		}

		$db->update("UPDATE pw_cnalbum SET " . pwSqlSingle(array('aname' => $aname, 'aintro' => $aintro, 'private' => $private ? 1 : 0)) . ' WHERE aid=' . pwEscape($aid));

		refreshto("{$basename}cyid=$cyid&a=album&aid=$aid",'operate_success');
	}
} elseif ($a == 'create') {

	!$ifadmin && Showmsg('undefined_action');
	banUser();

	if (empty($_POST['step'])) {

		$rt = array();
		$extra_url = '';
		$select_0 = 'selected';
//		require_once(M_P.'require/header.php');
//		require_once PrintEot('m_galbum');
//		footer();
		list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_galbum",true);

	} else {

		require_once(R_P.'require/postfunc.php');
		InitGP(array('aname','aintro'),'P');
		InitGP(array('private'),'P',2);
		!$aname && Showmsg('colony_aname_empty');
		if ($o_albumnum > 0 && $o_albumnum <= $colony['albumnum']) {
			Showmsg('colony_album_num2');
		}

		require_once(R_P.'require/bbscode.php');
		$wordsfb = L::loadClass('FilterUtil');
		if (($banword = $wordsfb->comprise($aname)) !== false) {
			Showmsg('title_wordsfb');
		}
		if (($banword = $wordsfb->comprise($aintro)) !== false) {
			Showmsg('post_wordsfb');
		}

		/*
		if ($o_camoney) {
			require_once(R_P.'require/credit.php');
			if ($o_camoney > $credit->get($winduid,$o_moneytype)) {
				Showmsg('colony_moneylimit2');
			}
			$credit->addLog('hack_cyalbum',array($o_moneytype => -$o_camoney),array(
				'uid'		=> $winduid,
				'username'	=> $windid,
				'ip'		=> $onlineip,
				'aname'		=> $aname
			));
			$credit->set($winduid,$o_moneytype,-$o_camoney);
		}
		*/
		require_once(R_P.'require/credit.php');
		$o_groups_creditset = unserialize($o_groups_creditset);
		$o_groups_creditset['Createalbum'] = array_diff($o_groups_creditset['Createalbum'],array(0));
		if (!empty($o_groups_creditset['Createalbum'])) {
			foreach ($o_groups_creditset['Createalbum'] as $key => $value) {
				if ($value > 0) {
					$moneyname = $credit->cType[$key];
					$moneyvalue = $value;
					if ($value > $credit->get($winduid,$key)) {
						Showmsg('colony_moneylimit2');
					}
				}
			}
			//积分变动
			$creditset = getCreditset($o_groups_creditset['Createalbum'],false);
			$credit->sets($winduid,$creditset,true);
			updateMemberid($winduid);
		}

		if ($creditlog = unserialize($o_groups_creditlog)) {
			addLog($creditlog['Createalbum'],$windid,$winduid,'groups_Createalbum');
		}
		$db->update("INSERT INTO pw_cnalbum SET " . pwSqlSingle(array(
				'aname'		=> $aname,			'aintro'	=> $aintro,
				'atype'		=> 1,				'private'	=> $private ? 1 : 0,
				'ownerid'	=> $cyid,			'owner'		=> $colony['cname'],
				'lasttime'	=> $timestamp,		'crtime'	=> $timestamp
		)));
		$db->update("UPDATE pw_colonys SET albumnum=albumnum+1 WHERE id=" . pwEscape($cyid));

		refreshto("{$basename}cyid=$cyid",'operate_success');
	}
} elseif ($a == 'getallowflash') {

	define('AJAX', 1);
	define('F_M',true);
	InitGP(array('aid'));
	$aid = (int)$aid;
	if ($aid) {
		$photonums = $db->get_value("SELECT photonum FROM pw_cnalbum WHERE atype='1' AND aid=" . pwEscape($aid));
		$photonums >= $o_maxphotonum && Showmsg('colony_photofull');
		$allowmutinum = $o_maxphotonum - $photonums;
	}
	echo "ok\t$allowmutinum";
	ajax_footer();
}
?>