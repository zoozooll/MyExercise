<?php
!defined('A_P') && exit('Forbidden');
!$winduid && Showmsg('not_login');

$isGM = CkInArray($windid,$manager);
!$isGM && $groupid==3 && $isGM=1;

if ($db_question && ($o_share_qcheck || $o_photos_qcheck)) {
	$qkey = array_rand($db_question);
}

require_once(R_P.'require/showimg.php');

InitGP(array('a','u','space','s'));
InitGP(array('page'),'GP',2);
$db_perpage = 20;

!$db_phopen && Showmsg('photos_close');

$u = $u ? $u : $winduid;
if ($u && $u != $winduid) {
	$userdb = $db->get_one("SELECT m.username,m.icon,o.index_privacy,o.photos_privacy FROM pw_members m LEFT JOIN pw_ouserdata o ON m.uid=o.uid WHERE m.uid=".pwEscape($u));
//	$thisbase = $basename;
	$username = $userdb['username'];
	(empty($a) || $a == 'friend') && $a = 'own';
	$a_key = 'index';
} else {
	$username = $windid;
//	$thisbase = $basename;
	$a_key = $a == 'friend' ? 'index' : 'own';
}
if ($space == 1 && defined('F_M')) {
	if (!$userdb) {
		$userdb = $db->get_one("SELECT m.username,m.icon,o.index_privacy,o.photos_privacy FROM pw_members m LEFT JOIN pw_ouserdata o ON m.uid=o.uid WHERE m.uid=".pwEscape($u));
	}
	if(!in_array($a,array('view','next','pre','album','viewalbum'))) $a = 'own';
}
if ($u != $winduid || ($space == 1 && defined('F_M')))	{
	if (empty($userdb)) {
		$errorname = '';
		Showmsg('user_not_exists');
	}
	list($isU,$privacy) = pwUserPrivacy($u,$userdb);
	if ($groupid == 3 || $isU == 2 || $isU !=2 && $privacy['index']) {
		$SpaceShow = 1;
	}
	if (!$privacy['photos'] && !$isGM || !$SpaceShow) {
		Showmsg('mode_o_photos_right');
	}
}

if (empty($a) || $a == 'own') {

	$sqladd  = '';
	if ($u != $winduid) {
		$sqladd .= ' AND private!=2';
	}
	$count = $db->get_value("SELECT COUNT(*) AS sum FROM pw_cnalbum WHERE atype='0' AND ownerid=" . pwEscape($u)."$sqladd");

	if ($count) {
		list($pages,$limit) = pwLimitPages($count,$page,"{$basename}space=$space&u=$u&a=$a&");
		$lastpid = $albumdb = $smphoto = array();
		$query = $db->query("SELECT c.aid,c.aname,c.photonum,c.ownerid,c.owner,c.lastphoto,c.lastpid,c.lasttime,c.private,m.groupid FROM pw_cnalbum c LEFT JOIN pw_members m ON c.ownerid=m.uid WHERE c.atype='0' AND c.ownerid=" . pwEscape($u) . " $sqladd ORDER BY c.aid DESC $limit");
		while ($rt = $db->fetch_array($query)) {
			$rt['sub_aname'] = substrs($rt['aname'],18);
			$rt['lasttime']		= get_date($rt['lasttime']);
			$rt['lastphoto']	= getphotourl($rt['lastphoto']);
			if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
				$rt['lastphoto'] = $pwModeImg.'/banuser.gif';
			}
			if ($rt['lastpid']) {
				$lastpid = array_merge($lastpid,explode(',',$rt['lastpid']));
			}
			$albumdb[] =  $rt;
		}
		if ($lastpid) {
			$query = $db->query('SELECT c.pid,c.aid,c.path,m.groupid FROM pw_cnphoto c LEFT JOIN pw_members m ON c.uploader=m.username ORDER BY c.pid DESC LIMIT 6');
			while ($rt = $db->fetch_array($query)) {
				$rt['path']	= getphotourl($rt['path']);
				if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
					$rt['path'] = $pwModeImg.'/banuser.gif';
				}
				$smphoto[$rt['aid']][] = $rt;
			}
		}
	}

} elseif ($a == 'friend') {

	$sum = $db->get_value("SELECT COUNT(*) AS sum FROM pw_friends f LEFT JOIN pw_cnalbum c ON c.atype='0' AND f.friendid=c.ownerid WHERE f.uid=" . pwEscape($winduid) . ' AND f.uid!=f.friendid AND c.aid IS NOT NULL AND c.private!=2');

	if ($sum) {
		list($pages,$limit) = pwLimitPages($sum,$page,"{$basename}&a=$a&");
		$lastpid = $albumdb = $smphoto = array();
		$query = $db->query("SELECT c.aid,c.aname,c.private,c.photonum,c.ownerid,c.owner,c.lastphoto,c.lastpid,c.lasttime,m.groupid FROM pw_friends f LEFT JOIN pw_cnalbum c ON c.atype='0' AND c.ownerid=f.friendid LEFT JOIN pw_members m ON c.ownerid=m.uid WHERE f.uid=" . pwEscape($winduid) . " AND f.uid!=f.friendid AND c.aid IS NOT NULL AND c.private!=2 ORDER BY c.aid DESC $limit");
		while ($rt = $db->fetch_array($query)) {
			$rt['sub_aname'] = substrs($rt['aname'],18);
			$rt['lasttime']		= get_date($rt['lasttime']);
			$rt['lastphoto']	= getphotourl($rt['lastphoto']);
			if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
				$rt['lastphoto'] = $pwModeImg.'/banuser.gif';
			}
			if ($rt['lastpid']) {
				$lastpid = array_merge($lastpid,explode(',',$rt['lastpid']));
			}
			$albumdb[] =  $rt;
		}
		if ($lastpid) {
			$query = $db->query("SELECT c.pid,c.aid,c.path,m.groupid FROM pw_cnphoto c LEFT JOIN pw_members m ON c.uploader=m.username WHERE c.pid IN(" . pwImplode($lastpid) . ') ORDER BY c.pid DESC');
			while ($rt = $db->fetch_array($query)) {
				$rt['path']	= getphotourl($rt['path']);
				if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
					$rt['path'] = $pwModeImg.'/banuser.gif';
				}
				$smphoto[$rt['aid']][] = $rt;
			}
		}
	}
	//require_once(R_P.'require/app_header.php');
	//require_once PrintEot('m_photos');
	//footer();
	list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_photos",true);

} elseif ($a == 'album') {

	InitGP(array('aid','viewpwd','checkpwd'), null, 2);

	$cnpho = array();
	$album = $db->get_one("SELECT c.aname,c.aintro,c.private,c.albumpwd,c.ownerid,c.owner,c.lastphoto,c.photonum,m.groupid FROM pw_cnalbum c LEFT JOIN pw_members m ON c.ownerid=m.uid WHERE c.atype='0' AND c.aid=" . pwEscape($aid));

	$isown = $album['ownerid'] == $winduid ? '1' : '0';

	if ($checkpwd) {
		if (empty($album)) {
			echo "data_error";
			ajax_footer();
		}
		if (!$viewpwd) {
			echo "empty";
			ajax_footer();
		}
		$viewpwd && $viewpwd = md5($viewpwd);
		if ($album['albumpwd'] == $viewpwd) {
			echo "success";
		} else {
			echo "fail";
		}
		ajax_footer();
	}
	empty($album) && Showmsg('data_error');
	$album['lastphoto']	= getphotourl($album['lastphoto']);
	if ($album['groupid'] == 6 && $db_shield && $groupid != 3) {
		$album['lastphoto'] = $pwModeImg.'/banuser.gif';
		$album['aintro'] = appShield('ban_album_aintro');
	}
	if ($album['ownerid'] <> $winduid && $album['private'] == 1 && !isFriend($album['ownerid'],$winduid) && $groupid != 3) {
		Showmsg('mode_o_photos_private_1');
	}
	if ($album['ownerid'] <> $winduid && $album['private'] == 2 && $groupid != 3) {
		Showmsg('mode_o_photos_private_2');
	}
	$viewpwd && $viewpwd = md5($viewpwd);
	if ($album['ownerid'] <> $winduid && $album['private'] == 3 && $viewpwd != $album['albumpwd'] && $groupid != 3) {
		Showmsg('mode_o_photos_private_3');
	}

	if ($album['photonum']) {
		list($pages,$limit) = pwLimitPages($album['photonum'],$page,"{$basename}a=$a&aid=$aid&");
		$query = $db->query("SELECT c.pid,c.path,c.ifthumb,c.uptime,m.groupid,c.pintro,c.c_num FROM pw_cnphoto c LEFT JOIN pw_members m ON c.uploader=m.username WHERE c.aid=" . pwEscape($aid) . " ORDER BY c.pid $limit");
		while ($rt = $db->fetch_array($query)) {
			$rt['path']	= getphotourl($rt['path'], $rt['ifthumb']);
			if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
				$rt['path'] = $pwModeImg.'/banuser.gif';
			}
			$rt['sub_pintro'] = substrs($rt['pintro'],25);
			$rt['uptime']	= get_date($rt['uptime']);
			$cnpho[] = $rt;
		}
	}
	$u = $album['ownerid'];
	$username = $album['owner'];

} elseif ($a == 'view') {
	InitGP(array('pid'));
	$db->update("UPDATE pw_cnphoto SET hits=hits+1 WHERE pid=" . pwEscape($pid));
	//$photo = $db->get_one("SELECT p.pid,p.aid,p.pintro,p.path as basepath,p.uploader,p.uptime,p.hits,p.c_num,p.ifthumb,a.aname,a.private, a.ownerid,a.owner,a.photonum,m.groupid FROM pw_cnphoto p LEFT JOIN pw_cnalbum a ON p.aid=a.aid LEFT JOIN pw_members m ON p.uploader=m.username WHERE p.pid=" . pwEscape($pid) . " AND a.atype='0'");

        # album photos
	$nearphoto = array();
        $register = array('db_shield'=>$db_shield,"groupid"=>$groupid,"pwModeImg"=>$pwModeImg);
        require_once R_P.'lib/showpicture.class.php';
        $sp = new PW_ShowPicture($register);
        list($photo,$nearphoto,$prePid,$nextPid) = $sp->getPictures($pid,$aid);

	$isown = $photo['ownerid'] == $winduid ? '1' : '0';
	empty($photo) && Showmsg('data_error');

	if ($photo['ownerid'] <> $winduid && $photo['private'] == 1 && !isFriend($photo['ownerid'],$winduid) && $groupid != 3) {
		Showmsg('mode_o_photos_private');
	}

	$photo['uptime'] = get_date($photo['uptime']);
	$photo['path'] = getphotourl($photo['basepath']);
	list($photo['w'],$photo['h']) = getimagesize($photo['path']);
	if ($photo['groupid'] == 6 && $db_shield && $groupid != 3) {
		$photo['path'] = $pwModeImg.'/banuser.gif';
		$photo['pintro'] = appShield('ban_photo_pintro');
	}
	$u = $photo['ownerid'];
	$username = $photo['owner'];
	$aid = $photo['aid'];
	$num = $db->get_value("SELECT COUNT(*) AS sum FROM pw_cnphoto WHERE aid=" . pwEscape($photo['aid']) . ' AND pid<=' . pwEscape($pid));

//	$up_photo = $db->get_one("SELECT p.pid,p.path,p.ifthumb,m.groupid FROM pw_cnphoto p LEFT JOIN pw_cnalbum a ON p.aid=a.aid LEFT JOIN pw_members m ON p.uploader=m.username WHERE p.pid<".pwEscape($pid)." AND  a.ownerid=".pwEscape($u)." AND p.aid=".pwEscape($aid)." ORDER BY pid DESC");
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
//	if ($photo['groupid'] == 6 && $db_shield && $groupid != 3) {
//		$thumb_basepath = $pwModeImg.'/banuser.gif';
//	}
//	$nearphoto[] = array('pid'=>$pid,'path'=>$thumb_basepath);
//
//	$next_photo = $db->get_one("SELECT p.pid,p.path,p.ifthumb,m.groupid FROM pw_cnphoto p LEFT JOIN pw_cnalbum a ON p.aid=a.aid LEFT JOIN pw_members m ON p.uploader=m.username WHERE p.pid>".pwEscape($pid)." AND  a.ownerid=".pwEscape($u)." AND p.aid=".pwEscape($aid)." ORDER BY pid");
//	if ($next_photo) {
//		$next_photo['path'] = getphotourl($next_photo['path'],$next_photo['ifthumb']);
//		if ($next_photo['groupid'] == 6 && $db_shield && $groupid != 3) {
//			$next_photo['path'] = $pwModeImg.'/banuser.gif';
//		}
//		$nearphoto[] = $next_photo;
//	} else {
//		$nearphoto[] = array('pid'=>'end','path'=>'images/apps/pend.jpg');
//	}

	$page = (int)GetGP('page');
	$page < 1 && $page = 1;
	$url = $basename.'a=view&pid='.$pid.'&';
	require_once(R_P.'require/bbscode.php');
	list($commentdb,$subcommentdb,$pages) = getCommentDbByTypeid('photo',$pid,$page,$url);
	$comment_type = 'photo';
	$comment_typeid = $pid;

} elseif ($a == 'next') {
	define('AJAX',1);
	InitGP(array('pid','aid'), null, 2);
	if ($aid) {
		$next_photo = $db->get_one("SELECT c.pid,c.path,c.ifthumb,m.groupid FROM pw_cnphoto c LEFT JOIN pw_members m ON c.uploader=m.username WHERE c.pid>".pwEscape($pid)." AND  c.aid=".pwEscape($aid)." ORDER BY c.pid");
		if ($next_photo) {
			$next_photo['path'] = getphotourl($next_photo['path'],$next_photo['ifthumb']);
			if ($next_photo['groupid'] == 6 && $db_shield && $groupid != 3) {
				$next_photo['path'] = $pwModeImg.'/banuser.gif';
			}
			unset($next_photo['ifthumb']);
			$pid = pwJsonEncode($next_photo);
			echo "ok\t$pid";
		} else {
			echo "end";
		}
	} else {
		$pid = $db->get_value("SELECT MIN(b.pid) AS pid FROM pw_cnphoto a LEFT JOIN pw_cnphoto b ON a.aid=b.aid AND a.pid<b.pid WHERE a.pid=" . pwEscape($pid));
		echo "ok\t$pid";
	}

	ajax_footer();
} elseif ($a == 'pre') {
	define('AJAX',1);
	InitGP(array('pid','aid'), null, 2);
	if ($aid) {
		$next_photo = $db->get_one("SELECT c.pid,c.path,c.ifthumb,m.groupid FROM pw_cnphoto c LEFT JOIN pw_members m ON c.uploader=m.username WHERE c.pid<".pwEscape($pid)." AND  c.aid=".pwEscape($aid)." ORDER BY c.pid DESC");
		if ($next_photo) {
			$next_photo['path'] = getphotourl($next_photo['path'],$next_photo['ifthumb']);
			if ($next_photo['groupid'] == 6 && $db_shield && $groupid != 3) {
				$next_photo['path'] = $pwModeImg.'/banuser.gif';
			}
			unset($next_photo['ifthumb']);
			$pid = pwJsonEncode($next_photo);
			echo "ok\t$pid";
		} else {
			echo "begin";
		}
	} else {
		$pid = $db->get_value("SELECT MAX(b.pid) AS pid FROM pw_cnphoto a LEFT JOIN pw_cnphoto b ON a.aid=b.aid AND a.pid>b.pid WHERE a.pid=" . pwEscape($pid));
		echo "ok\t$pid";
	}
	ajax_footer();
} elseif ($a == 'editphoto') {
	banUser();

	InitGP(array('pid'), null, 2);

	$photo = $db->get_one("SELECT p.aid,p.pintro,a.ownerid,p.path,a.lastphoto FROM pw_cnphoto p LEFT JOIN pw_cnalbum a ON p.aid=a.aid WHERE pid=" . pwEscape($pid));
	if (empty($photo) || (!$isGM && ($photo['ownerid'] <> $winduid))) {
		Showmsg('data_error');
	}
	if (empty($_POST['step'])) {

		$options = '';
		$query = $db->query("SELECT aid,aname FROM pw_cnalbum WHERE atype='0' AND ownerid=" . pwEscape($photo['ownerid']) . ' ORDER BY aid DESC');
		while ($rt = $db->fetch_array($query)) {
			$options .= "<option value=\"$rt[aid]\"" . (($rt['aid'] == $photo['aid']) ? ' selected' : '') . ">$rt[aname]</option>";
		}

	} else {
		require_once(R_P.'require/postfunc.php');
		InitGP(array('pintro'),'P');
		InitGP(array('aid'), null, 2);
		!$aid && Showmsg('colony_albumclass');

		if (strlen($pintro)>255) Showmsg('album_pintro_toolang');

		require_once(R_P.'require/bbscode.php');
		$wordsfb = L::loadClass('FilterUtil');
		if (($banword = $wordsfb->comprise($pintro)) !== false) {
			Showmsg('content_wordsfb');
		}

		$pwSQL = array('pintro' => $pintro);

		$ischage = false;
		if ($aid != $photo['aid'] && ($isGM || $winduid == $db->get_value("SELECT ownerid FROM pw_cnalbum WHERE aid=" . pwEscape($aid)))) {
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

			if ($photo['path'] == $photo['lastphoto']) {
				$lastphoto = $db->get_value("SELECT path FROM pw_cnphoto WHERE aid=" . pwEscape($photo['aid']) . " ORDER BY pid DESC LIMIT 1");
			}

			$db->update("UPDATE pw_cnalbum SET " . pwSqlSingle(array('photonum' => $phnum[$aid] ? $phnum[$aid] : 0, 'lastpid' => implode(',',getLastPid($aid)))) . ' WHERE aid=' . pwEscape($aid));

			$db->update("UPDATE pw_cnalbum SET " . pwSqlSingle(array('photonum' => $phnum[$photo['aid']] ? $phnum[$photo['aid']] : 0, 'lastpid' => implode(',',getLastPid($photo['aid'])),'lastphoto'=>$lastphoto)) . ' WHERE aid=' . pwEscape($photo['aid']));
		}

		refreshto("{$basename}a=view&pid=$pid",'operate_success');
	}
} elseif ($a == 'delphoto') {

	define('AJAX','1');
	InitGP(array('pid'), null, 2);

	$isGM = CkInArray($windid,$manager);
	!$isGM && $groupid==3 && $isGM=1;

	if ($isGM) {
		$whereadd = '';
	} else {
		$whereadd = " AND ca.ownerid=" . pwEscape($winduid);
	}
	$photo = $db->get_one("SELECT cp.path,cp.uploader,ca.aid,ca.lastphoto,ca.lastpid,m.uid FROM pw_cnphoto cp LEFT JOIN pw_cnalbum ca ON cp.aid=ca.aid LEFT JOIN pw_members m ON cp.uploader=m.username WHERE cp.pid=" . pwEscape($pid) . " AND ca.atype='0' $whereadd");

	if (empty($photo)) {
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

	$affected_rows = delAppAction('photo',$pid) + 1;
	countPosts("-$affected_rows");

	//积分变动
	require_once(R_P.'require/credit.php');
	$o_photos_creditset = unserialize($o_photos_creditset);
	$creditset = getCreditset($o_photos_creditset['Deletephoto'],false);
	$creditset = array_diff($creditset,array(0));
	if (!empty($creditset)) {
		require_once(R_P.'require/postfunc.php');
		$credit->sets($photo['uid'],$creditset,true);
		updateMemberid($photo['uid'],false);
	}

	if ($creditlog = unserialize($o_photos_creditlog)) {
		addLog($creditlog['Deletephoto'],$photo['uploader'],$photo['uid'],'photos_Deletephoto');
	}

	updateUserAppNum($photo['uid'],'photo','minus');

	echo 'ok'."\t".$photo['aid'];ajax_footer();

} elseif ($a == 'setcover') {
	define('AJAX','1');
	InitGP(array('pid'), null, 2);
	$whereadd = $groupid == 3 ? '' : " AND ca.ownerid=" . pwEscape($winduid);
	$photo = $db->get_one("SELECT cp.path,ca.aid,ca.lastphoto,ca.lastpid FROM pw_cnphoto cp LEFT JOIN pw_cnalbum ca ON cp.aid=ca.aid WHERE cp.pid=" . pwEscape($pid) . " AND ca.atype='0' $whereadd");

	if (empty($photo)) {
		Showmsg('data_error');
	}
	$db->update("UPDATE pw_cnalbum SET lastphoto=".pwEscape($photo['path']).' WHERE aid='.pwEscape($photo['aid']));
	echo 'ok'."\t".$photo['aid'];ajax_footer();
} elseif ($a == 'upload') {
	banUser();

	InitGP(array('aid'),null,2);
	InitGP(array('job'));

	$options = '<option value=""></option>';
	$query = $db->query("SELECT aid,aname FROM pw_cnalbum WHERE atype='0' AND ownerid=" . pwEscape($winduid) . ' ORDER BY aid DESC');
	while ($rt = $db->fetch_array($query)) {
		$options .= "<option value=\"$rt[aid]\"" . (($aid && $rt['aid'] == $aid) ? ' selected' : '') . ">$rt[aname]</option>";
	}
	if (!$options) {
		refreshto("{$basename}a=create&tips=1",'album_error');
	}

	if ($aid > 0) {
		
		$photonums = $db->get_value("SELECT photonum FROM pw_cnalbum WHERE atype='0' AND aid=" . pwEscape($aid));
		$o_maxphotonum && $photonums >= $o_maxphotonum && Showmsg('colony_photofull');
		$allowmutinum = $o_maxphotonum - $photonums;
	}
	if (empty($job)) {

		if (empty($_POST['step'])) {
			//require_once(M_P.'require/header.php');
			if(!$s) {
				//require_once PrintEot('m_photos');
				list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_photos",true);
				//require_once app_render('m_photos');
				//footer();
			} else {
				//require_once PrintEot('m_photos_bottom');
				list($isheader,$isfooter,$tplname,$isleft) = array(true,false,"m_photos",true);
				//exit;
			}

		} else {

			//require_once(R_P.'require/postfunc.php');
			PostCheck(1,$o_photos_gdcheck,$o_photos_qcheck);
			InitGP(array('pintro'),'P');
			empty($pintro) && $pintro = array();

			require_once(R_P.'require/bbscode.php');
			$wordsfb = L::loadClass('FilterUtil');
			foreach ($pintro as $k => $v) {
				if (($banword = $wordsfb->comprise($v)) !== false) {
					Showmsg('content_wordsfb');
				}
			}
			if (!$aid) {
				$albumcheck = $db->get_one("SELECT aid FROM pw_cnalbum WHERE atype='0' AND ownerid=".pwEscape($winduid).pwLimit(1));
				if ($albumcheck) {
					Showmsg('colony_albumclass');
				} else {
					$db->update("INSERT INTO pw_cnalbum SET " . pwSqlSingle(array(
						'aname'		=> getLangInfo('app','defaultalbum'),		'atype'		=> 0,
						'ownerid'	=> $winduid,		'owner'		=> $windid,
						'lasttime'	=> $timestamp,		'crtime'	=> $timestamp
					)));
					$aid = $db->insert_id();
				}
			}
			!$aid && Showmsg('colony_albumclass');

			$rt = $db->get_one("SELECT aname,photonum,ownerid,private,lastphoto FROM pw_cnalbum WHERE atype='0' AND aid=" . pwEscape($aid));

			if (empty($rt)) {
				Showmsg('undefined_action');
			} elseif ($winduid != $rt['ownerid']) {
				Showmsg('colony_phototype');
			}
			$uploadNum = 0;
			foreach($_FILES as $k=>$v){
				(isset($v['name']) && $v['name'] != "") && $uploadNum++;
			}
			$o_maxphotonum && ($rt['photonum']+$uploadNum) > $o_maxphotonum && Showmsg('colony_photofull');

			require_once(R_P . 'lib/upload/photoupload.class.php');
			$img = new PhotoUpload($aid);
			PwUpload::upload($img);
			pwFtpClose($ftp);

			if (!$photos = $img->getAttachs()) {
				refreshto("{$basename}a=upload",'colony_uploadnull');
			}
			$photoNum = count($photos);
			$pid = $img->getNewID();
			$lastpid = getLastPid($aid, 4);
			array_unshift($lastpid, $pid);

			if (!$rt['private']) {
				$feedText = "[url=$db_bbsurl/{$basename}space=1&a=album&aid=$aid&u=$winduid]{$rt[aname]}[/url]\n";
				foreach ($photos as $value) {
					$feedText .= "[url=$db_bbsurl/{#APPS_BASEURL#}q=photos&space=1&a=view&pid=$pid&u=$winduid][img]".getphotourl($value['path'], $value['ifthumb'])."[/img][/url]&nbsp;";
				}
				pwAddFeed($winduid, 'photo', $pid, array('num' => $photoNum, 'text' => $feedText));
				//会员资讯缓存
				$usercache = L::loadDB('Usercache');
				$usercachedata = $usercache->get($winduid,'photos');
				$usercachedata = explode(',',$usercachedata['value']);
				is_array($usercachedata) || $usercachedata = array();
				if (count($usercachedata) >=4) array_pop($usercachedata);
				array_unshift($usercachedata,$pid);
				$usercachedata = implode(',',$usercachedata);
				$usercache->update($winduid,'photos',$pid,$usercachedata);
			}
			$db->update("UPDATE pw_cnalbum SET photonum=photonum+".pwEscape($photoNum,false).",lasttime=" . pwEscape($timestamp,false) . ',lastpid=' . pwEscape(implode(',',$lastpid)) . (!$rt['lastphoto'] ? ',lastphoto='.pwEscape($img->getLastPhoto()) : '') . " WHERE aid=" . pwEscape($aid));
			countPosts("+$photoNum");

			//积分变动
			require_once(R_P.'require/credit.php');
			$o_photos_creditset = unserialize($o_photos_creditset);
			$creditset = getCreditset($o_photos_creditset['Uploadphoto'],true,$photoNum);
			$creditset = array_diff($creditset,array(0));
			if (!empty($creditset)) {
				$credit->sets($winduid,$creditset,true);
				updateMemberid($winduid);
			}
			if ($creditlog = unserialize($o_photos_creditlog)) {
				addLog($creditlog['Uploadphoto'],$windid,$winduid,'photos_Uploadphoto');
			}
			updateUserAppNum($winduid,'photo','add',$photoNum);
			refreshto("{$basename}a=view&pid=$pid",'operate_success',2,true);
		}
	} elseif ($job == 'flash') {
		//require_once(M_P.'require/header.php');
		//require_once PrintEot('m_photos');
		list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_photos",true);
		//footer();
	}
} elseif ($a == 'create') {

	InitGP(array('step','tips'),null,2);
	InitGP(array('checkpwd'));
	banUser();

	/*
	* 用户组创建相册权限
	*/
	if ($groupid != 3 && $o_photos_groups && strpos($o_photos_groups,",$groupid,") === false) {
		createfail($checkpwd,'photos_group_right');
		Showmsg('photos_group_right');
	}
	if (empty($step)) {

		$rt = array();
		$select_0 = 'selected';
		//require_once(M_P.'require/header.php');
		//require_once PrintEot('m_photos');
		list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_photos",true);
		//footer();

	} else {

		require_once(R_P.'require/postfunc.php');
		PostCheck(1,$o_photos_gdcheck,$o_photos_qcheck);
		InitGP(array('aname','aintro','pwd','repwd','private'));

		if(!$aname) {
			createfail($checkpwd,'colony_aname_empty');
			Showmsg('colony_aname_empty');
		}
		if (strlen($aname)>24) {
			createfail($checkpwd,'colony_aname_toolang');
			Showmsg('colony_aname_toolang');
		}
		if (strlen($aintro)>255) {
			createfail($checkpwd,'colony_aintro_toolang');
			Showmsg('colony_aintro_toolang');
		}

		$private = (int)$private;
		if ($private == 3 && !$pwd) {
			createfail($checkpwd,'photo_password_add');
			Showmsg('photo_password_add');
		}
		if ($private == 3 && $pwd) {
			if (strlen($pwd) < 3 || strlen($pwd) > 15) {
				createfail($checkpwd,'photo_password_minlimit');
				Showmsg('photo_password_minlimit');
			}
			$S_key = array("\\",'&',' ',"'",'"','/','*',',','<','>',"\r","\t","\n",'#','%','?');
			if (str_replace($S_key,'',$pwd) != $pwd) {
				createfail($checkpwd,'illegal_password');
				Showmsg('illegal_password');
			}

			if ($pwd != $repwd) {
				createfail($checkpwd,'password_confirm');
				Showmsg('password_confirm');
			}
			$pwd = md5($pwd);
		}

		require_once(R_P.'require/bbscode.php');
		$wordsfb = L::loadClass('FilterUtil');
		if (($banword = $wordsfb->comprise($aname)) !== false) {
			Showmsg('title_wordsfb');
		}
		if (($banword = $wordsfb->comprise($aintro)) !== false) {
			Showmsg('content_wordsfb');
		}

		if ($o_albumnum2 > 0 && $o_albumnum2 <= $db->get_value("SELECT COUNT(*) AS count FROM pw_cnalbum WHERE atype='0' AND ownerid=" . pwEscape($winduid))) {
			createfail($checkpwd,$o_albumnum2,'limit_num');
			Showmsg('colony_album_num2');
		}

		require_once(R_P.'require/credit.php');
		$o_photos_creditset = unserialize($o_photos_creditset);
		$o_photos_creditset['Createalbum'] = @array_diff($o_photos_creditset['Createalbum'],array(0));
		if (!empty($o_photos_creditset['Createalbum'])) {
			foreach ($o_photos_creditset['Createalbum'] as $key => $value) {
				if ($value > 0) {
					$moneyname = $credit->cType[$key];
					if ($value > $credit->get($winduid,$key)) {
						createfail($checkpwd,'colony_moneylimit2');
						Showmsg('colony_moneylimit2');
					}
				}
			}
			//积分变动
			$creditset = getCreditset($o_photos_creditset['Createalbum'],false);
			$credit->sets($winduid,$creditset,true);
			updateMemberid($winduid);
		}

		if ($creditlog = unserialize($o_photos_creditlog)) {
			addLog($creditlog['Createalbum'],$windid,$winduid,'photos_Createalbum');
		}
		/*
		if ($o_camoney) {
			require_once R_P.'require/credit.php';
			if ($o_camoney > $credit->get($winduid,$o_moneytype)) {
				$moneyname = $credit->cType[$o_moneytype];
				createfail($checkpwd,'colony_moneylimit2');
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
		$db->update("INSERT INTO pw_cnalbum SET " . pwSqlSingle(array(
				'aname'		=> $aname,			'aintro'	=> $aintro,
				'atype'		=> 0,				'private'	=> $private,
				'ownerid'	=> $winduid,		'owner'		=> $windid,
				'lasttime'	=> $timestamp,		'crtime'	=> $timestamp,
				'albumpwd'	=> $pwd
		)));
		$aid = $db->insert_id();
		//countPosts('+1');
		if ($checkpwd) {
			echo "success\t$aid";
			ajax_footer();
		}
		refreshto("{$basename}a=own",'operate_success');
	}
} elseif ($a == 'delalbum') {

	define('AJAX', 1);
	define('F_M',true);
	InitGP(array('aid'), null, 2);
	$album = $db->get_one("SELECT * FROM pw_cnalbum WHERE aid=" . pwEscape($aid) . " AND atype='0'");

	if (empty($album) || $album['ownerid'] != $winduid && $groupid != 3) {
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
				$affected_rows += delAppAction('photo',$rt['pid'])+1;//TODO 效率？
			}
			pwFtpClose($ftp);
			countPosts("-$affected_rows");
		}
		$db->update("DELETE FROM pw_cnphoto WHERE aid=" . pwEscape($aid));
		$db->update("DELETE FROM pw_cnalbum WHERE aid=" . pwEscape($aid));
		updateUserAppNum($album['ownerid'],'photo','minus',$album['photonum']);

		if($album['ownerid'] != $winduid){
			echo getLangInfo('msg','operate_success') . "\tjump\t{$basename}a=friend";
		} else {
			echo getLangInfo('msg','operate_success') . "\tjump\t{$basename}a=own";
		}
		ajax_footer();
	}
} elseif ($a == 'editalbum') {

	define('AJAX', 1);
	define('F_M',true);
	banUser();
	InitGP(array('aid'));
	empty($aid) && Showmsg('data_error');

	$rt = $db->get_one("SELECT aid,aname,aintro,atype,private,albumpwd,ownerid FROM pw_cnalbum WHERE aid=" . pwEscape($aid));
	if (empty($rt) || $rt['atype'] <> 0 || ($rt['ownerid'] <> $winduid && $groupid != 3)) {
		Showmsg('data_error');
	}
	if (empty($_POST['step'])) {
		${'select_'.$rt['private']} = 'selected';
		require_once PrintEot('m_ajax');ajax_footer();

	} else {

		require_once(R_P.'require/postfunc.php');
		InitGP(array('aname','aintro','pwd','repwd'),'P');
		InitGP(array('private'),'P',2);
		!$aname && Showmsg('colony_aname_empty');
		if (strlen($aname)>24) Showmsg('colony_aname_toolang');
		if (strlen($aintro)>255) Showmsg('colony_aintro_toolang');

		if ($private == 3 && !$pwd && !$rt['albumpwd']) {
			Showmsg('photo_password_add');
		}

		if ($pwd) {
			if (strlen($pwd) < 3 || strlen($pwd) > 15) {
				Showmsg('photo_password_minlimit');
			}
			$S_key = array("\\",'&',' ',"'",'"','/','*',',','<','>',"\r","\t","\n",'#','%','?');
			if (str_replace($S_key,'',$pwd) != $pwd) {
				Showmsg('illegal_password');
			}

			if ($pwd != $repwd) {
				Showmsg('password_confirm');
			}
			$pwd = md5($pwd);
		}

		require_once(R_P.'require/bbscode.php');
		$wordsfb = L::loadClass('FilterUtil');
		if (($banword = $wordsfb->comprise($aname)) !== false) {
			Showmsg('title_wordsfb');
		}
		if (($banword = $wordsfb->comprise($aintro)) !== false) {
			Showmsg('content_wordsfb');
		}

		if ($private == 3 && !$pwd && $rt['albumpwd']) {
			$pwd = $rt['albumpwd'];
		}
		$db->update("UPDATE pw_cnalbum SET " . pwSqlSingle(array('aname' => $aname, 'aintro' => $aintro, 'private' => $private, 'albumpwd' => $pwd)) . ' WHERE aid=' . pwEscape($aid));

		refreshto("{$basename}a=own",'operate_success');
	}
} elseif ($a == 'viewalbum') {
	define('AJAX', 1);
	define('F_M',true);
	InitGP(array('aid'));
	$aid = (int)$aid;
	empty($aid) && Showmsg('data_error');
	require_once PrintEot('m_ajax');ajax_footer();
} elseif ($a == 'createajax') {
	define('AJAX', 1);
	define('F_M',true);
	banUser();
	InitGP(array('job'));
	require_once PrintEot('m_ajax');ajax_footer();
} elseif ($a == 'getallowflash') {
	define('AJAX', 1);
	define('F_M',true);
	InitGP(array('aid'));
	$aid = (int)$aid;
	if ($aid) {
		$photonums = $db->get_value("SELECT photonum FROM pw_cnalbum WHERE atype='0' AND aid=" . pwEscape($aid));
		$o_maxphotonum && $photonums >= $o_maxphotonum && Showmsg('colony_photofull');
		if ($o_maxphotonum) {
			$allowmutinum = $o_maxphotonum - $photonums;
		} else {
			$allowmutinum = 'infinite';
		}
	}
	echo "ok\t$allowmutinum";
	ajax_footer();
}

//require_once(M_P.'require/header.php');
if ($space == 1 && defined('F_M')) {
	//$basename .= "space=1&u=$u&";
	$spaceurl = $baseUrl;
	require_once(R_P.'require/credit.php');
	list($userdb,$ismyfriend,$friendcheck,$usericon,$usercredit,$totalcredit,$appcount,$p_list) = getAppleftinfo($u);
	//list($isheader,$isfooter,$tplname,$isuserspace) = array(true,true,"user_photos",true);
	require_once PrintEot('header');
	require_once PrintEot('user_photos');
	footer();
} else {

	if (!$s) {
		//require_once PrintEot('m_photos');
		list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_photos",true);
	} else {
		//require_once PrintEot('m_photos_bottom');
		list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_photos_bottom",true);
	}
}
//footer();
function createfail($checkpwd,$showinfo='',$type='fail') {
	if ($checkpwd) {
		$showinfo = 'fail' == $type && ''!= $showinfo ? getLangInfo('msg',$showinfo) : $showinfo;
		echo "$type\t$showinfo";
		ajax_footer();
	}
	return false;
}

?>