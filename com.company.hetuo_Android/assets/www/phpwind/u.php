<?php
define('SCR','u');
require_once('global.php');

InitGP(array('uid','username','action'));
if ($action != 'show'){
	!$winduid && Showmsg('not_login');
}
$sqlsel = $sqltab = '';
$isU = false;
!$uid && !$username && $uid = $winduid;
if ($db_modes['o']['ifopen'] && !in_array($action,array('show','friend'))) {
	redirectULink($action,$uid,$username);
}
switch ($action) {
	case 'show' :
		@include_once(D_P.'data/bbscache/customfield.php');
		!is_array($customfield) && $customfield = array();
		foreach ($customfield as $key => $value) {
			$customfield[$key]['id'] = $value['id'] = (int)$value['id'];
			$value['type'] == 3 && $customfield[$key]['options'] = explode("\n",$value['options']);
			$sqlsel .= ",mb.field_$value[id]";
		}
		$sqlsel.= ',mb.tooltime,mb.customdata';
		$sqltab = ' LEFT JOIN pw_memberinfo mb ON m.uid=mb.uid';
		break;
}
if ($uid) {
	$sql = 'm.uid='.pwEscape($uid);
	$uid == $winduid && $isU = true;
} else {
	$sql = 'm.username='.pwEscape($username);
	$username == $windid && $isU = true;
}

if (!$isU && !$_G['allowprofile']) {
	$sqlsel .= ',f.uid AS isfriend';
	$sqltab .= ' LEFT JOIN pw_friends f ON m.uid=f.uid AND f.friendid='.pwEscape($winduid);
}

$userdb = $db->get_one("SELECT m.uid,m.username,m.email,m.groupid,m.memberid,m.icon,m.gender,m.regdate,m.signature,m.introduce,m.oicq,m.msn,m.yahoo,m.site,m.honor,m.bday,m.medals,m.userstatus,md.thisvisit,md.onlinetime,md.postnum,md.digests,md.rvrc,md.money,md.credit,md.currency,md.lastvisit,md.lastpost,md.todaypost,md.onlineip{$sqlsel} FROM pw_members m LEFT JOIN pw_memberdata md ON m.uid=md.uid{$sqltab} WHERE $sql");
if (empty($userdb)) {
	$errorname = '';
	Showmsg('user_not_exists');
}
$userdb['honor'] = substrs($userdb['honor'],90);

if (!$isU && !$_G['allowprofile'] && !$userdb['isfriend']) {
	Showmsg('profile_right');
}

$uid = $userdb['uid'];
include_once(D_P.'data/bbscache/level.php');
require_once(R_P.'require/showimg.php');
require_once(R_P.'require/header.php');

$usericon = showfacedesign($userdb['icon'],true);
$systitle = $userdb['groupid'] == '-1' ? '' : $ltitle[$userdb['groupid']];
$p_list = $db_plist && count($db_plist)>1 ? $db_plist : array();

if (empty($action)) {

	require_once(R_P.'require/forum.php');
	$feed = $topicdb = $postdb = array();
	$frienddb = array($userdb['uid']);
	$fidoff = array(0);

	if ($isU) {
		$query = $db->query("SELECT friendid FROM pw_friends WHERE uid=".pwEscape($userdb['uid']));
		while ($rt = $db->fetch_array($query)) {
			$frienddb[] = $rt['friendid'];
		}
	} else {
		$fidoff = getFidoff($groupid);
	}
	$query = $db->query('SELECT f.*,m.username FROM pw_feed f LEFT JOIN pw_members m ON f.uid=m.uid WHERE f.uid IN('.pwImplode($frienddb).') ORDER BY timestamp DESC LIMIT 10');
	while ($rt = $db->fetch_array($query)) {
		$rt['descrip'] = descriplog($rt['descrip']);
		$feed[] = $rt;
	}
	$sqloff = pwImplode($fidoff);

	$query = $db->query('SELECT tid,subject,postdate,hits,replies FROM pw_threads WHERE authorid='.pwEscape($userdb['uid'])." AND postdate>$timestamp-604800 AND ifcheck='1' AND fid NOT IN($sqloff) ORDER BY tid DESC LIMIT 5");
	while ($rt = $db->fetch_array($query)) {
		$rt['postdate'] = get_date($rt['postdate']);
		$topicdb[] = $rt;
	}

	$pw_posts = GetPtable($db_ptable);
	$query = $db->query("SELECT p.pid,p.postdate,t.tid,t.subject FROM $pw_posts p LEFT JOIN pw_threads t USING(tid) WHERE p.authorid=".pwEscape($userdb['uid'])." AND p.postdate>$timestamp-604800 AND p.fid NOT IN($sqloff) ORDER BY p.postdate DESC LIMIT 5");
	while ($rt = $db->fetch_array($query)) {
		$rt['postdate'] = get_date($rt['postdate']);
		$postdb[] = $rt;
	}

	require_once PrintEot('u');
	footer();

} elseif ($action == 'feed') {

	InitGP(array('page'));
	require_once(R_P.'require/forum.php');
	$feed		= array();
	$frienddb	= array($userdb['uid']);
	(!is_numeric($page) || $page<1) && $page = 1;
	$limit = pwLimit(($page-1)*$db_perpage,$db_perpage);

	if ($isU) {
		$query = $db->query("SELECT friendid FROM pw_friends WHERE uid=".pwEscape($userdb['uid']));
		while ($rt = $db->fetch_array($query)) {
			$frienddb[] = $rt['friendid'];
		}
	}
	$count = $db->get_value("SELECT COUNT(*) AS count FROM pw_feed WHERE uid IN(".pwImplode($frienddb).")");
	$pages = numofpage($count,$page,ceil($count/$db_perpage),"u.php?action=$action&uid=$uid&");

	$query = $db->query('SELECT f.*,m.username FROM pw_feed f LEFT JOIN pw_members m ON f.uid=m.uid WHERE f.uid IN('.pwImplode($frienddb).") ORDER BY timestamp DESC $limit");
	while ($rt = $db->fetch_array($query)) {
		$rt['descrip'] = descriplog($rt['descrip']);
		$feed[] = $rt;
	}
	require_once PrintEot('u');
	footer();

} elseif ($action == 'show') {

	if(!$winduid && !$_G['allowprofile']) Showmsg('not_login');
	include_once(D_P.'data/bbscache/md_config.php');
	require_once(R_P.'require/credit.php');
	require_once(R_P.'require/forum.php');
	require_once(R_P.'require/postfunc.php');
	$customdata = $custominfo = $colonydb = array();

	$user_icon = explode('|',$userdb['icon']);
	if($user_icon[4] && $userdb['tooltime'] < $timestamp-86400){
		$userdb['icon'] = "$user_icon[0]|$user_icon[1]|$user_icon[2]|$user_icon[3]|0";
		$db->update("UPDATE pw_members SET icon=".pwEscape($userdb['icon'],false)." WHERE uid=".pwEscape($userdb['uid']));



		$usericon = showfacedesign($userdb['icon'],true);
	}

	$query = $db->query("SELECT cy.id,cy.cname FROM pw_cmembers c LEFT JOIN pw_colonys cy ON cy.id=c.colonyid WHERE c.uid=".pwEscape($userdb['uid']));
	while ($rt = $db->fetch_array($query)) {
		$colonydb[] = $rt;
	}

	if ($md_ifopen && $userdb['medals']) {
		include_once(D_P.'data/bbscache/medaldb.php');
		$query = $db->query("SELECT id,awardee,level FROM pw_medalslogs WHERE awardee=".pwEscape($userdb['username'],false)." AND action='1' AND state='0' AND timelimit>0 AND $timestamp-awardtime>timelimit*2592000");
		if ($db->num_rows($query)) {
			include_once(R_P.'require/msg.php');
			$reason = Char_cv(getLangInfo('other','medal_reason'));
			$ids = $medals = $medalslog = array();
			while ($rt = $db->fetch_array($query)) {
				$ids[]			= $rt['id'];
				$medals[]		= $rt['level'];
				$medalslog[]	= array($rt['awardee'],'SYSTEM',$timestamp,$rt['level'],2,$reason);
				$message		= array(
					'toUser'	=> $rt['awardee'],
					'subject'	=> 'metal_cancel',
					'content'	=> 'metal_cancel_text',
					'other'		=> array('medalname' => $_MEDALDB[$rt['level']]['name'])
				);
				pwSendMsg($message);
			}
			if ($ids) {
				$db->update("INSERT INTO pw_medalslogs(awardee,awarder,awardtime,level,action,why) VALUES ".pwSqlMulti($medalslog,false));
				$db->update("UPDATE pw_medalslogs SET state='1' WHERE id IN(".pwImplode($ids).")");
				$userdb['medals'] = explode(',',$userdb['medals']);
				$userdb['medals'] = array_diff($userdb['medals'],$medals);
				$userdb['medals'] = implode(',',$userdb['medals']);
				$db->update("UPDATE pw_members SET medals=".pwEscape($userdb['medals'],false)." WHERE uid=".pwEscape($userdb['uid'],false));
				$db->update('DELETE FROM pw_medaluser WHERE uid='.pwEscape($userdb['uid'],false).' AND mid IN('.pwImplode($medals).')');
				updatemedal_list();
			}
		}
		$userdb['medals'] = explode(',',$userdb['medals']);
	}
	$usercredit = array(
		'postnum'	 => $userdb['postnum'],
		'digests'	 => $userdb['digests'],
		'rvrc'		 => $userdb['rvrc'],
		'money'		 => $userdb['money'],
		'credit'	 => $userdb['credit'],
		'currency'	 => $userdb['currency'],
		'onlinetime' => $userdb['onlinetime']
	);
	foreach ($credit->get($userdb['uid'],'CUSTOM') as $key => $value) {
		$usercredit[$key] = $value;
	}
	$totalcredit = CalculateCredit($usercredit,unserialize($db_upgrade));
	$newmemberid = getmemberid($totalcredit);

	if ($userdb['memberid'] <> $newmemberid) {
		$userdb['memberid'] = $newmemberid;
		$db->update("UPDATE pw_members SET memberid=".pwEscape($newmemberid,false)."WHERE uid=".pwEscape($userdb['uid']));
	}
	if ($db_autoban) {
		require_once(R_P.'require/autoban.php');
		autoban($userdb['uid']);
	}
	if ($userdb['groupid'] == '6' || getstatus($userdb['userstatus'],1)) {
		$pwSQL = '';
		$isBan = false;
		$bandb = $delban = array();
		$query = $db->query("SELECT * FROM pw_banuser WHERE uid=".pwEscape($userdb['uid']));
		while ($rt = $db->fetch_array($query)) {
			if ($rt['type'] == 1 && $timestamp - $rt['startdate'] > $rt['days']*86400) {
				$delban[] = $rt['id'];
			} elseif ($rt['fid'] == 0) {
				$rt['startdate'] = get_date($rt['startdate']);
				$bandb = $rt;
			} else {
				$isBan = true;
				$rt['startdate'] = get_date($rt['startdate']);
				$bandb[] = $rt;
			}
		}
		$delban && $db->update('DELETE FROM pw_banuser WHERE id IN('.pwImplode($delban).')');
		($userdb['groupid'] == '6' && !$bandb) && $pwSQL .= "groupid='-1',";
		(getstatus($userdb['userstatus'],1) && !$isBan) && $pwSQL .= 'userstatus=userstatus&(~1),';
		if ($pwSQL = rtrim($pwSQL,',')) {
			$db->update("UPDATE pw_members SET $pwSQL WHERE uid=".pwEscape($userdb['uid']));
		}
		if ($isBan) {
			include_once(D_P.'data/bbscache/forum_cache.php');
		}
	}

	if ($db_modes['o']['ifopen']) {
		redirectULink($action,$uid,$username);
	}

	if (!getstatus($userdb['userstatus'],7) && !CkInArray($windid,$manager)) {
		$userdb['email'] = "<img src=\"$imgpath/email.gif\" border=\"0\">";
	}
	if (getstatus($userdb['userstatus'],9) && $db_signwindcode) {
		require_once(R_P.'require/bbscode.php');
		if ($_G['imgwidth'] && $_G['imgheight']) {
			$db_windpic['picwidth']  = $_G['imgwidth'];
			$db_windpic['picheight'] = $_G['imgheight'];
		}
		$_G['fontsize'] && $db_windpic['size'] = $_G['fontsize'];
		$userdb['signature'] = convert($userdb['signature'],$db_windpic,2);
	}
	$userdb['signature'] = str_replace("\n","<br>",$userdb['signature']);
	$db_union[7] && list($customdata,$custominfo) = Getcustom($userdb['customdata']);

	$userdb['rvrc'] = floor($userdb['rvrc']/10);
	if ($db_ifonlinetime && $userdb['onlinetime']) {
		$userdb['onlinetime'] = floor($userdb['onlinetime']/3600);
	} else {
		$userdb['onlinetime'] = 0;
	}
	if (!$userdb['todaypost'] || $userdb['lastpost'] < $tdtime) $userdb['todaypost'] = 0;
	$averagepost = floor($userdb['postnum']/(ceil(($timestamp - $userdb['regdate'])/(3600*24))));
	$userdb['regdate'] = get_date($userdb['regdate'],'Y-m-d');
	$userdb['lastvisit'] = get_date($userdb['lastvisit'],'Y-m-d');
	$userdb['onlineip']= explode('|',$userdb['onlineip']);
	$_cache = getDatastore();
	$_cache->delete('UID_'.$userdb['uid']);
	require_once PrintEot('u');
	footer();

} elseif ($action == 'topic') {

	InitGP(array('type','page','ordertype'));
	!in_array($ordertype,array('lastpost','postdate')) && $ordertype = 'postdate';

	(!is_numeric($page) || $page < 1) && $page = 1;
	include_once(D_P.'data/bbscache/forum_cache.php');

	switch ($type) {
		case 'digest':
			$sql = " AND digest>'0'";break;
		case 'poll':
			$sql = " AND special='1'";break;
		case 'sale':
			$sql = " AND special='4'";break;
		default:
			$sql = '';
	}
	$fidoff = $isU ? array(0) : getFidoff($groupid);
	$sql .= ' AND fid NOT IN('.pwImplode($fidoff).')';

 	$limit = pwLimit(($page-1)*$db_perpage,$db_perpage);
	$rt = $db->get_one("SELECT COUNT(*) AS count FROM pw_threads WHERE authorid=".pwEscape($userdb['uid'])." $sql");
	$pages = numofpage($rt['count'],$page,ceil($rt['count']/$db_perpage),"u.php?action=$action&uid=$uid&ordertype=$ordertype&");

	$threaddb = array();
	$query = $db->query("SELECT tid,fid,subject,postdate,lastpost,lastposter,replies,hits,titlefont FROM pw_threads WHERE authorid=".pwEscape($userdb['uid'])." $sql ORDER BY $ordertype DESC $limit");
	while ($rt = $db->fetch_array($query)) {
		$rt['subject'] = substrs($rt['subject'],45);
		$rt['forum'] = strip_tags($forum[$rt['fid']]['name']);
		$rt['postdate'] = get_date($rt['postdate'],'Y-m-d');
		$rt['lastpost'] = get_date($rt['lastpost'],'Y-m-d');
		$rt['encode_lastposter'] = rawurlencode($rt['lastposter']);
		$threaddb[] = $rt;
	}
	require_once PrintEot('u');
	footer();

} elseif ($action == 'post') {

	include_once(D_P.'data/bbscache/forum_cache.php');
	require_once(R_P.'require/forum.php');

	InitGP(array('ptable','page'));
	(!is_numeric($page) || $page < 1) && $page = 1;
	$limit = pwLimit(($page-1)*$db_perpage,$db_perpage);
	!isset($ptable) && $ptable = $db_ptable;
	$pw_posts = GetPtable($ptable);

	$fidoff = $isU ? array(0) : getFidoff($groupid);
	$sqloff = ' AND p.fid NOT IN('.pwImplode($fidoff).')';

	$count = $db->get_value("SELECT COUNT(*) AS count FROM $pw_posts p WHERE authorid=".pwEscape($userdb['uid'])." $sqloff");
	$nurl = "u.php?action=post&uid=$uid&";
	if ($p_list) {
		
		$p_table = "";
		foreach ($p_list as $key => $val) {
			$name = $val ? $val : ($key != 0 ? getLangInfo('other','posttable').$key : getLangInfo('other','posttable'));
			$p_table .= "<li id=\"up_post$key\"><a href=\"{$nurl}ptable=$key\">".$name."</a></li>";
		}
		$nurl .= "ptable=$ptable&";
	}
	$pages = numofpage($count,$page,ceil($count/$db_perpage),$nurl);
	$isGM = CkInArray($windid,$manager);
	$postdb = array();
	$query = $db->query("SELECT p.pid,p.postdate,t.tid,t.fid,t.subject,t.authorid,t.author,t.titlefont,t.anonymous FROM $pw_posts p LEFT JOIN pw_threads t USING(tid) WHERE p.authorid=".pwEscape($userdb['uid'])." $sqloff ORDER BY p.postdate DESC $limit");
	while ($rt = $db->fetch_array($query)){
		$rt['subject']	= substrs($rt['subject'],45);
		if ($rt['anonymous'] && $rt['authorid'] != $winduid && !$isGM) {
			$rt['author'] = $db_anonymousname;
			$rt['authorid'] = 0;
		}
		$rt['forum']	= strip_tags($forum[$rt['fid']]['name']);
		$rt['postdate']	= get_date($rt['postdate'],'Y-m-d');
		$postdb[]		= $rt;
	}
	require_once PrintEot('u');
	footer();

} elseif (in_array($action,array('favor','friend','trade'))) {

	require_once Pcv(R_P.'require/u_'.$action.'.php');

}

function Getcustom($data,$unserialize=true,$strips=null) {
	global $db_union;
	$customdata = array();
	if (!$data || ($unserialize ? !is_array($data=unserialize($data)) : !is_array($data))) {
		$data = array();
	} elseif (!is_array($custominfo = unserialize($db_union[7]))) {
		$custominfo = array();
	}
	if (!empty($data) && !empty($custominfo)) {
		foreach ($data as $key => $value) {
			if (!empty($strips)) {
				$customdata[stripslashes(Char_cv($key))] = stripslashes(Char_cv($value));
			} elseif ($custominfo[$key] && $value) {
				$customdata[$key] = $value;
			}
		}
	}
	return array($customdata,$custominfo);
}

function updatemedal_list(){
	global $db;
	$query = $db->query("SELECT uid FROM pw_medaluser GROUP BY uid");
	$medaldb = '<?php die;?>0';
	while($rt=$db->fetch_array($query)){
		$medaldb .= ','.$rt['uid'];
	}
	writeover(D_P.'data/bbscache/medals_list.php',$medaldb);
}

function getFidoff($gid) {
	global $db;
	$fidoff = array(0);
	$query = $db->query("SELECT fid FROM pw_forums WHERE type<>'category' AND (password!='' OR forumsell!='' OR allowvisit!='' AND allowvisit NOT LIKE '%,$gid,%')");
	while ($rt = $db->fetch_array($query)) {
		$fidoff[] = $rt['fid'];
	}
	return $fidoff;
}

function redirectULink($action,$uid,$username) {
	global $winduid,$windid;
	$addurl = $uid ? 'u='.$uid : 'username='.$username;
	$baseurl = 'mode.php?m=o&'.$addurl;
	$uid != $winduid && $username != $windid && $ifspace = '&space=1';
	switch ($action) {
		case 'topic' :
			$baseurl .= $ifspace.'&q=article';
			break;
		case 'post':
			$baseurl .= $ifspace.'&q=article&see=post';
			break;
		case 'favor':
			$baseurl .= $ifspace.'&q=share&a=my&see=postfavor';
			break;
		case 'friend':
			$baseurl .= $ifspace.'&q=friend';
			break;
		case 'trade' :
			$baseurl .= $ifspace.'&q=article&see=trade';
			break;
		default:
			$baseurl = 'mode.php?m=o&q=user&'.$addurl;
			break;
	}
	ObHeader($baseurl);
}
?>