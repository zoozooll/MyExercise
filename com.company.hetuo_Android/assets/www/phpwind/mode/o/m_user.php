<?php
!defined('M_P') && exit('Forbidden');
require_once(R_P.'require/credit.php');
define('O_P','user');

$isGM = CkInArray($windid,$manager);
!$isGM && $groupid==3 && $isGM=1;

if ($db_question && $o_share_qcheck) {
	$qkey = array_rand($db_question);
}
if(!$winduid) ObHeader('index.php?m=o');
if(!$_G['allowprofile']) Showmsg('not_login');
InitGP(array('u','username'));
if ($username) {
	$sqladd = 'm.username='.pwEscape($username);
} else{
	$u = (int) $u;
	$u = $u ? $u : $winduid;
	$sqladd = 'm.uid='.pwEscape($u);
}


$spaceurl = $basename."space=1&u=$u&";

include_once(D_P.'data/bbscache/level.php');
require_once(R_P.'require/showimg.php');

$userdb = $db->get_one("SELECT m.uid,m.username,m.email,m.groupid,m.memberid,m.icon,m.gender,m.regdate,m.introduce,m.oicq,m.msn,m.yahoo,m.site,m.location,m.honor,m.bday,m.medals,m.userstatus,md.thisvisit,md.onlinetime,md.postnum,md.digests,md.rvrc,md.money,md.credit,md.currency,md.lastvisit,md.lastpost,md.todaypost,md.onlineip,md.f_num,ud.index_privacy,ud.profile_privacy,ud.info_privacy,ud.credit_privacy,ud.owrite_privacy,ud.msgboard_privacy,ud.visits,ud.tovisits,ud.tovisit,ud.whovisit,ud.diarynum,ud.photonum,ud.owritenum,ud.groupnum,ud.sharenum,ud.diary_lastpost,ud.photo_lastpost,ud.owrite_lastpost,ud.group_lastpost,ud.share_lastpost FROM pw_members m LEFT JOIN pw_memberdata md ON m.uid=md.uid LEFT JOIN pw_ouserdata ud ON m.uid=ud.uid WHERE ".$sqladd);
if (empty($userdb)) {
	$errorname = $username ? $username : 'UID:'.$u;
	Showmsg('user_not_exists');
}
$u = $userdb['uid'];
list($isU,$privacy) = pwUserPrivacy($u,$userdb);
if ($groupid == 3 || $isU == 2 || $isU !=2 && $privacy['index']) {
	$SpaceShow = 1;
}
$ismyfriend = isFriend($winduid,$u);
$friendcheck = getstatus($userdb['userstatus'],3,3);
$userdb['honor'] = substrs($userdb['honor'],90);
//$usericon = showfacedesign($userdb['icon'],true);
list($usericon,,,,,,, $imglen) = showfacedesign($userdb['icon'], 1, 'm');

$onlinetime = isset($userdb['onlinetime']) ? $userdb['onlinetime'] : 0;

if ($userdb['onlinetime']) {
	$userdb['onlinetime'] = floor($userdb['onlinetime']/3600);
} else {
	$userdb['onlinetime'] = 0;
}
$systitle = $userdb['groupid']=='-1' ? '' : $ltitle[$userdb['groupid']];
$memtitle = $ltitle[$userdb['memberid']];
$usercredit = array(
	'postnum'	 => $userdb['postnum'],
	'digests'	 => $userdb['digests'],
	'rvrc'		 => $userdb['rvrc'],
	'money'		 => $userdb['money'],
	'credit'	 => $userdb['credit'],
	'currency'	 => $userdb['currency'],
	'onlinetime' => $onlinetime
);
foreach ($credit->get($userdb['uid'],'CUSTOM') as $key => $value) {
	$usercredit[$key] = $value;
}
$p_list = $db_plist && count($db_plist)>1 ? $db_plist : array();

$totalcredit = CalculateCredit($usercredit,unserialize($db_upgrade));
$userdb['rvrc'] = floor($userdb['rvrc']/10);
if (!$userdb['todaypost'] || $userdb['lastpost'] < $tdtime) $userdb['todaypost'] = 0;
$averagepost = round($userdb['postnum']/(ceil(($timestamp - $userdb['regdate'])/(3600*24))), 2);
$userdb['regdate'] = get_date($userdb['regdate'],'Y-m-d');
$userdb['lastvisit'] = get_date($userdb['lastvisit'],'Y-m-d');
$userdb['onlineip']= explode('|',$userdb['onlineip']);
$userdb['introduce'] = nl2br($userdb['introduce']);
$all_userinfo = array('gender','bday','location','digests','todaypost','oicq','yahoo','msn','email','onlinetime','regdate','lastvisit','onlineip','uid');
$need_userinfo = array('digests','todaypost','onlinetime','regdate','lastvisit','uid');
foreach ($all_userinfo as $key => $value) {
	if (!in_array($value,$need_userinfo)) {
		if(empty($userdb[$value]))continue;
		if ($value == 'email') {
			if (!getstatus($userdb['userstatus'],7) && !CkInArray($windid,$manager) && $userdb['uid'] != $winduid) continue;
		} elseif ($value == 'onlineip') {
			if (!$isGM && $groupid != '3' && $winduid != $userdb['uid']) continue;
			$userdb[$value] = $userdb[$value][0];
		} elseif ($value == 'gender') {
			$userdb[$value] = getLangInfo('other','gender_'.$userdb[$value]);
		}
	}
	$user_profile[$value] = array(getLangInfo('other','profile_'.$value),$userdb[$value]);
}
$userdb['site']  = $userdb['site'] ? '<a href="'.$userdb['site'].'" target="_blank" >'.$userdb['site'].'</a>' : '';
//用户app统计
$app_with_count = array('topic','diary','photo','owrite','group','share');
foreach ($app_with_count as $key => $value) {
	$postnum = $posttime = '';
	$appcount[$value] = getPostnumByType($value);
}

list($latest_action_time,$latest_action_date) = getLastDate($userdb['thisvisit']);

if ($privacy['index']) {
	InitGP(array('f_type'));
	$feeds = array();
	$addwhere = '';
	if (in_array($f_type,array('friend','share','photo','write','colony','post','diary'))) {
		$addwhere = ' AND f.type='.pwEscape($f_type);
	}
	if (!$db_dopen) {
		$addwhere .= " AND f.type!='diary'";
	}
	if (!$db_phopen) {
		$addwhere .= " AND f.type!='photo'";
	}
	if (!$db_share_open) {
		$addwhere .= " AND f.type!='share'";
	}
	if (!$db_groups_open) {
		$addwhere .= " AND f.type!='colony' AND f.type!='colony_post' AND f.type!='colony_photo'";
	}
	$query = $db->query("SELECT f.*,m.username,m.groupid,m.icon FROM pw_feed f LEFT JOIN pw_members m ON f.uid=m.uid WHERE f.uid= ".pwEscape($u)." $addwhere ORDER BY f.timestamp DESC LIMIT 30");
	while ($rt = $db->fetch_array($query)) {
		$rt['descrip'] = parseFeed($rt['descrip']);
		if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
			$rt['descrip'] = appShield('ban_feed');
		}
		$key = get_date($rt['timestamp'],'y-m-d');
		list($rt['faceurl']) = showfacedesign($rt['icon'],'1','s');
		$rt['appicon'] = getAppIcon($rt['type']);
		$feeds[$key][] = $rt;
	}
}
$friends = getFriends($u,0,18,'',1);
//$sendfriends = getFriends($winduid,0,0,false,1);


//留言板
if ($privacy['msgboard']) {
	$boards = array();
	require_once(R_P.'require/bbscode.php');
	$wordsfb = L::loadClass('FilterUtil');
	$query = $db->query("SELECT o.*,m.icon as face,m.groupid FROM pw_oboard o LEFT JOIN pw_members m ON o.uid=m.uid WHERE o.touid=".pwEscape($u)." ORDER BY o.id DESC ".pwLimit(0,15));
	while ($rt = $db->fetch_array($query)) {
		$rt['postdate']	= get_date($rt['postdate']);
		list($rt['face'])	=  showfacedesign($rt['face'],1,'m');
		if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
			$rt['title'] = appShield('ban_feed');
		} elseif (!$wordsfb->equal($rt['ifwordsfb'])) {
			$rt['title'] = $wordsfb->convert($rt['title'], array(
				'id'	=> $rt['id'],
				'type'	=> 'oboard',
				'code'	=> $rt['ifwordsfb']
			));
		}
		if (strpos($rt['title'],'[s:') !== false) {
			$rt['title'] = showface($rt['title']);
		}
		if (strpos($rt['title'],'[url') !== false) {
			$rt['title'] = convert($rt['title'],$db_windpost);
		}
		$boardids[] = $rt['id'];
		$boards[] = $rt;
	}
	if (!empty($boardids)) {
		$commentdb = getCommentDb('board',$boardids);
	}
}


$whovisit = unserialize($userdb['whovisit']);
$tovisit = unserialize($userdb['tovisit']);
is_array($whovisit) || $whovisit = array();
is_array($tovisit) || $tovisit = array();

if ($isU != 2 && $winduid) {
	if (!isset($whovisit[$winduid]) || $timestamp - $whovisit[$winduid] > 900) {
		$whovisit[$winduid] = $timestamp;
		$userdb['visits']++;
		arsort($whovisit);
		if (count($whovisit) > 9) array_pop($whovisit);
		$db->pw_update(
			"SELECT uid FROM pw_ouserdata WHERE uid=".pwEscape($u),
			"UPDATE pw_ouserdata SET visits=".pwEscape($userdb['visits'],false).",whovisit=".pwEscape(serialize($whovisit),false)."WHERE uid=".pwEscape($u),
			"INSERT INTO pw_ouserdata SET " . pwSqlSingle(array(
				'uid'		=> $u,
				'visits'	=> $userdb['visits'],
				'whovisit'	=> serialize($whovisit)
			))
		);
	}

	$windVisit = $db->get_value("SELECT tovisit FROM pw_ouserdata WHERE uid=".pwEscape($winduid,false));
	$windVisit = unserialize($windVisit);

	is_array($windVisit) || $windVisit = array();
	if (!isset($windVisit[$u]) || $timestamp - $windVisit[$u] > 900) {
		$windVisit[$u] = $timestamp;
		arsort($windVisit);
		if (count($windVisit) > 9) array_pop($windVisit);
		$db->update("UPDATE pw_ouserdata SET tovisit=".pwEscape(serialize($windVisit),false).",tovisits=tovisits + 1 WHERE uid=".pwEscape($winduid));
	}
}
$visituids = array_merge(array_keys($whovisit),array_keys($tovisit));
if ($visituids) {
	$query = $db->query("SELECT m.uid,m.username,m.icon,m.honor,md.thisvisit FROM pw_members m LEFT JOIN pw_memberdata md ON m.uid=md.uid WHERE m.uid IN (".pwImplode($visituids,false).")");
	while($rt = $db->fetch_array($query)) {
		list($rt['icon']) = showfacedesign($rt['icon'],1,'m');
		if($db_showonline && $rt['thisvisit']+$db_onlinetime*1.5>$timestamp){
			$rt['thisvisit'] = 1;
		} else {
			$rt['thisvisit'] = 0;
		}
		if (isset($whovisit[$rt['uid']])) {
			list($whovisit[$rt['uid']]) = getLastDate($whovisit[$rt['uid']],'2');
			$whovisit[$rt['uid']] = array('visittime'=>$whovisit[$rt['uid']]) + $rt;
		}
		if (isset($tovisit[$rt['uid']])) {
			list($tovisit[$rt['uid']]) = getLastDate($tovisit[$rt['uid']],'2');
			$tovisit[$rt['uid']] = array('visittime'=>$tovisit[$rt['uid']]) + $rt;
		}
	}
}

function getAppIcon($type) {
	switch ($type){
		case 'record':
			$icon = 'record.png';
			break;
		case 'diary':
			$icon = 'posts.png';
			break;
		case 'photo':
			$icon = 'albums.png';
			break;
		case 'share':
			$icon = 'share.png';
			break;
		case 'colony':
			$icon = 'groups.png';
			break;
		case 'post':
			$icon = 'posts.png';
			break;
		case 'friend':
			$icon = 'groups.png';
			break;
		default:
			$icon = 'posts.png';
			break;
	}
	return $icon;
}

if (GetCookie('o_invite') && $db_modes['o']['ifopen']==1){
	Cookie('o_invite','');
}
require_once(M_P.'require/header.php');
require_once PrintEot('m_user');
footer();
?>