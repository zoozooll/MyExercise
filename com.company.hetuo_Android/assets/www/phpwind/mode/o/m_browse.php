<?php
!defined('M_P') && exit('Forbidden');

$tab = GetGP('tab');
if(!$winduid) ObHeader('index.php?m=o');
require_once(R_P.'require/showimg.php');

!$o_browseopen && Showmsg('browse_close');

$thisbase = $basename;
$basename = $basename.'space=1&';
if ($tab == 'w' && $o_browse & 4) {
	$newWrite = browseWrite(15);
} elseif ($tab == 'p' && $db_phopen && $o_browse & 16) {
	$albumdb = browseAlbum(10);
} elseif ($tab == 's' && $db_share_open && $o_browse & 8) {
	$newShares = browseShare(20);
} elseif ($tab == 'd' && $db_dopen && $o_browse & 64) {
	list($newDiarys,$diarytype) = browseDiary(20);
} elseif ($tab == 't' && $o_browse & 128) {
	$newsubject = browseNewSubject(30);
} elseif ($tab == 'g' && $db_groups_open && $o_browse & 32) {
	$reGroups = browseGroup(15);
	$cMembers = array();
	$colonyids = pwImplode(array_keys($reGroups));
	if ($colonyids) {
		$query = $db->query("SELECT id,ifadmin,colonyid FROM pw_cmembers WHERE colonyid IN ($colonyids) AND uid=".pwEscape($winduid,false));
		while ($rt = $db->fetch_array($query)) {
			$cMembers[$rt['colonyid']] = 1;
		}
	}
} elseif ($tab == 'r' && $o_browse & 256) {
	$newreply = browseNewreply(30);

} else {
	$tab = 'h';

	$pwCacheFile = D_P.'data/bbscache/o_browse_cache.php';
	if (pwFilemtime($pwCacheFile) + 60 < $timestamp) {

		$element = L::loadClass('element');
		if ($o_browse & 128) {
			$newsubject = browseNewSubject(10);
		}
		if ($o_browse & 256) {
			$newreply = browseNewreply(10);
		}

		if ($o_browse & 1) {
			$newPics = array();
			$result = $element->newPic(0,5);
			foreach ($result as $val) {
				$newPics[] = array(
					'url' => $val['url'],
					'title' => substrs($val['title'],30),
					'image' => $val['image']
				);
			}

			$newThreads = array();
			$result = $element->replySortWeek(0,9);
			foreach ($result as $val) {
				$newThreads[] = array(
					'url' => $val['url'],
					'title' => substrs($val['title'],30),
					'author' => $val['addition']['author'],
					'authorid' => $val['addition']['authorid'],
					'postdate'	=> get_date($val['addition']['postdate']),
					'fid' => $val['addition']['fid'],
					'replies' => $val['addition']['replies']
				);
			}
		}

		if ($o_browse & 2) {
			$members = array();
			$result = $element->userSort('todaypost',8,true);
			foreach ($result as $val) {
				$members[] = array(
					'url' => $val['url'],
					'title'	=> $val['title'],
					'value'	=> $val['value'],
					'image' => $val['image']
				);
			}
		}


		if ($o_browse & 4) {
			$newWrite = browseWrite(6);
		}
		if ($db_share_open && $o_browse & 8) {
			$newShares = browseShare(5);
		}
		if ($db_groups_open && $o_browse & 32) {
			$reGroups = browseGroup(5);
		}
		if ($db_phopen && $o_browse & 16) {
			$albumdb = browseAlbum(5);
		}
		if ($db_dopen && $o_browse & 64) {
			list($newDiarys,$diarytype) = browseDiary(6);
		}	writeover($pwCacheFile,"<?php\r\n\$newPics=".pw_var_export($newPics).";\r\n\$newThreads=".pw_var_export($newThreads).";\r\n\$members=".pw_var_export($members).";\r\n\$reGroups=".pw_var_export($reGroups).";\r\n\$newsubject=".pw_var_export($newsubject).";\r\n\$newreply=".pw_var_export($newreply).";\r\n\$newShares=".pw_var_export($newShares).";\r\n\$newDiarys=".pw_var_export($newDiarys).";\r\n\$diarytype=".pw_var_export($diarytype).";\r\n\$newWrite=".pw_var_export($newWrite).";\r\n\$albumdb=".pw_var_export($albumdb).";\r\n\$smphoto=".pw_var_export($smphoto).";\r\n?>");
	} else {
		include_once Pcv($pwCacheFile);
	}
}
require_once(M_P.'require/header.php');
require_once PrintEot('m_browse');footer();

function browseNewSubject($num) {
	global $db,$forum;
	require_once(D_P."data/bbscache/forum_cache.php");
	$element = L::loadClass('element');

	$article = array();
	$result = $element->newSubject(0,$num);
	foreach ($result as $val) {
		$temp_fid	= $val['addition']['fid'];
		$article[] = array(
			'url' => $val['url'],
			'title' => substrs($val['title'],40),
			'author' => $val['addition']['author'],
			'authorid' => $val['addition']['authorid'],
			'postdate'	=> get_date($val['addition']['postdate']),
			'fid' => $temp_fid,
			'replies' => $val['addition']['replies'],
			'hits' => $val['addition']['hits'],
			'forum'	=> strip_tags($forum[$temp_fid]['name'])
		);
	}
	return $article;
}

function browseNewreply($num) {
	global $db,$forum;
	require_once(D_P."data/bbscache/forum_cache.php");
	$element = L::loadClass('element');

	$article = array();
	$result = $element->newReply(0,$num);
	!is_array($result) && $result = array();
	foreach ($result as $val) {
		$temp_fid	= $val['addition']['fid'];
		$article[] = array(
			'url' => $val['url'],
			'title' => substrs($val['title'],40),
			'author' => $val['addition']['author'],
			'authorid' => $val['addition']['authorid'],
			'postdate'	=> get_date($val['addition']['postdate']),
			'fid' => $temp_fid,
			'replies' => $val['addition']['replies'],
			'hits' => $val['addition']['hits'],
			'forum'	=> strip_tags($forum[$temp_fid]['name'])
		);
	}
	return $article;
}



function browseWrite($num) {
	global $db,$db_shield,$groupid;
	$newWrite = array();
	$sqladd = '';

	$db_shield && $groupid != 3 && $sqladd = " AND m.groupid<>6 ";

	$query = $db->query("SELECT wd.*,m.username,m.icon,m.groupid FROM pw_owritedata wd LEFT JOIN pw_members m ON wd.uid=m.uid left join pw_ouserdata as o ON m.uid=o.uid WHERE o.owrite_privacy = 0 AND wd.touid='' $sqladd ORDER BY wd.id DESC LIMIT $num");
	while ($rt = $db->fetch_array($query)) {
		$rt['postdate'] = get_date($rt['postdate']);
		list($rt['icon']) = showfacedesign($rt['icon'],1);
		$newWrite[] = $rt;
	}
	return $newWrite;
}

function browseShare($num) {
	global $db,$m,$db_shield,$groupid;
	$newShares = array();
	$sqladd = '';
	$db_shield && $groupid != 3 && $sqladd = " AND m.groupid<>6 ";
	$query = $db->query("SELECT s.*,m.groupid,m.icon FROM pw_share s LEFT JOIN pw_members m ON s.uid=m.uid WHERE s.ifhidden=0 $sqladd ORDER BY s.id DESC LIMIT $num");
	while ($rt = $db->fetch_array($query)) {
		$rt['postdate'] = get_date($rt['postdate'],'m-d H:s');
		$temp = unserialize($rt['content']);
		if ($temp['video']) {
			$rt['host'] = $temp['video']['host'];
			$rt['hash'] = $temp['video']['hash'];
		}

		$rt['link']	= $temp['link'];
		if (strpos($rt['link'],'{#APPS_BASEURL#}') !== false) {
			$baseurl = $m == 'o' ? 'mode.php?m=o&' : 'apps.php?';
			$rt['link'] = str_replace('{#APPS_BASEURL#}',$baseurl,$rt['link']);
		}
		if ($rt['type']=='user') {
			$rt['image']	= $temp['user']['image'];
			$rt['title']= "<a href=\"$rt[link]\" target=\"_blank\">".$temp['user']['username']."</a>";
		} elseif ($rt['type']=='photo') {
			$belong	= getLangInfo('app','photo_belong');
			$rt['image'] = $temp['photo']['image'];
			$temp_uid	= $temp['photo']['uid'];
			$rt['title']= $belong."<a href=\"u.php?uid=$temp_uid\" target=\"_blank\">".$temp['photo']['username']."</a>";
		} elseif ($rt['type']=='album') {
			$belong	= getLangInfo('app','photo_belong');
			$rt['image']	= $temp['album']['image'];
			$temp_uid	= $temp['album']['uid'];
			$rt['title']= $belong."<a href=\"u.php?uid=$temp_uid\" target=\"_blank\">".$temp['album']['username']."</a>";
		} elseif ($rt['type']=='group') {
			$rt['image']	= $temp['group']['image'];
			$rt['title']= "<a href=\"$rt[link]\" target=\"_blank\">".$temp['group']['name']."</a>";
		} elseif ($rt['type']=='diary') {
			$rt['title']= "<a href=\"$rt[link]\" target=\"_blank\">".$temp['diary']['subject']."</a>";
		} elseif ($rt['type']=='topic'){
			$rt['title']= "<a href=\"$rt[link]\" target=\"_blank\">".$temp['topic']['subject']."</a>";
			$rt['abstract']= $temp['topic']['abstract'];
			$rt['imgs']= unserialize($temp['topic']['imgs']);
		} else {
			$rt['title']= "<a href=\"$rt[link]\" target=\"_blank\">".substrs($rt['link'],40)."</a>";
		}

		$rt['descrip'] = $temp['descrip'];
		$rt['type_name']= getLangInfo('app',$rt['type']);
		list($rt['icon']) = showfacedesign($rt['icon'],1);
		unset($rt['content']);
		$newShares[] = $rt;
	}
	return $newShares;
}

function browseDiary($num) {
	global $db,$db_shield,$groupid;
	$newDiarys = $diarytype = array();
	$uids = '';
	$sqladd = '';
	$db_shield && $groupid != 3 && $sqladd = " AND m.groupid<>6 ";
	$query = $db->query("SELECT d.did,d.dtid,d.uid,d.username,d.subject,d.r_num,d.c_num,d.postdate,m.icon,m.groupid FROM pw_diary d LEFT JOIN pw_members m ON d.uid=m.uid WHERE d.privacy!=2 $sqladd ORDER BY d.did DESC LIMIT $num");
	while ($rt = $db->fetch_array($query)) {
		$rt['postdate'] = get_date($rt['postdate'],'m-d H:s');
		$rt['subject'] = substrs($rt['subject'],45);
		$rt['url'] = "mode.php?m=o&q=diary&space=1&u=$rt[uid]&did=$rt[did]";
		if (strpos(','.$uids.',',','.$rt['uid'].',') === false) {
			$uids .= $uids ? ','.$rt['uid'] : $rt['uid'];
		}
		list($rt['face']) = showfacedesign($rt['icon'],1);
		$newDiarys[$rt['did']] = $rt;
	}


	$query = $db->query("SELECT dtid,name FROM pw_diarytype WHERE uid IN(" .pwEscape($uids). ")");
	while ($rt = $db->fetch_array($query)) {
		$diarytype[$rt['dtid']] = $rt;
	}
	return array($newDiarys,$diarytype);
}

function browseGroup($num) {
	global $db,$pwModeImg;
	$reGroups = array();
	$query = $db->query("SELECT id,classid,cname,members,cnimg,createtime,descrip,admin FROM pw_colonys ORDER BY members DESC LIMIT $num");
	while ($rt = $db->fetch_array($query)) {
		if ($rt['cnimg']) {
			list($rt['cnimg']) = geturl("cn_img/$rt[cnimg]",'lf');
		} else {
			$rt['cnimg'] = $pwModeImg.'/nophoto.gif';
		}
		$rt['createtime'] = get_date($rt['createtime'], 'Y-m-d');
		$reGroups[$rt['id']] = $rt;
	}

	return $reGroups;
}

function browseAlbum($num) {
	global $db,$db_shield,$groupid,$pwModeImg;
	$lastpid = $albumdb = $smphoto = array();
	$sqladd = '';
	$db_shield && $groupid != 3 && $sqladd = " AND m.groupid<>6 ";
	$query = $db->query("SELECT c.aid,c.aname,c.photonum,c.ownerid,c.owner,c.lastphoto,c.lastpid,c.lasttime,m.groupid FROM pw_cnalbum c LEFT JOIN pw_members m ON c.ownerid=m.uid WHERE c.atype='0' AND c.private='0' $sqladd ORDER BY c.aid DESC LIMIT $num");
	while ($rt = $db->fetch_array($query)) {
		$rt['sub_aname'] = substrs($rt['aname'],18);
		$rt['lasttime']		= get_date($rt['lasttime']);
		$rt['lastphoto']	= getphotourl($rt['lastphoto']);
		if ($rt['lastpid']) {
			$lastpid = array_merge($lastpid,explode(',',$rt['lastpid']));
		}
		$albumdb[] =  $rt;
	}
	/*
	if ($lastpid) {
		$query = $db->query('SELECT c.pid,c.aid,c.path,m.groupid FROM pw_cnphoto c LEFT JOIN pw_members m ON c.uploader=m.username ORDER BY c.pid DESC LIMIT 5');
		while ($rt = $db->fetch_array($query)) {
			$rt['path']	= getphotourl($rt['path']);
			if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
				$rt['path'] = $pwModeImg.'/banuser.gif';
			}
			$smphoto[$rt['aid']][] = $rt;
		}
	}
	*/
	return $albumdb;
}
?>