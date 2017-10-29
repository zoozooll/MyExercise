<?php
!defined('M_P') && exit('Forbidden');
if(!$winduid) ObHeader('index.php?m=o');
$SCR = 'm_home';
$element = L::loadClass('element');

require_once(R_P.'require/showimg.php');
list($faceurl) = showfacedesign($winddb['icon'],1,'m');

$feeds =  array();
$frienddb = $feed_frienddb = array($winduid);
$query = $db->query("SELECT friendid,iffeed FROM pw_friends WHERE uid=".pwEscape($winduid)."AND status=0");
while ($rt = $db->fetch_array($query)) {
	if ($rt['iffeed'] == '1') {
		$feed_frienddb[] = $rt['friendid'];
	}
	$frienddb[] = $rt['friendid'];
}
InitGP('f_type');
if (!empty($frienddb)) {
	if (!empty($feed_frienddb)){
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
			$addwhere .= " AND f.type!='colony'";
		}
		$query = $db->query('SELECT f.*,m.username,m.groupid,icon FROM pw_feed f LEFT JOIN pw_members m ON f.uid=m.uid WHERE f.uid IN('.pwImplode($feed_frienddb).") $addwhere ORDER BY timestamp DESC LIMIT 30");
		while ($rt = $db->fetch_array($query)) {
			$rt['descrip'] = parseFeed($rt['descrip']);
			if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
				$rt['descrip'] = appShield('ban_feed');
			}
			list($rt['faceurl']) = showfacedesign($rt['icon'],'1','s');
			$key = get_date($rt['timestamp'],'y-m-d');
			$feeds[$key][] = $rt;
		}
	}

	if (count($frienddb)>10) {
		srand((float) microtime() * 10000000);
		$temp_frienddb = array_rand($frienddb,10);
	} else {
		$temp_frienddb = $frienddb;
	}
	$query = $db->query('SELECT friendid FROM pw_friends WHERE uid IN('.pwImplode($temp_frienddb).') AND status=0 LIMIT 30');
	$mfriends = array();
	while ($rt = $db->fetch_array($query)) {
		if ($rt['friendid'] != $winduid && !in_array($rt['friendid'],$frienddb)) {
			isset($mfriends[$rt['friendid']]) ? $mfriends[$rt['friendid']]++ : $mfriends[$rt['friendid']] = 1;
		}
	}
	arsort($mfriends);
	if (count($mfriends)>6) {
		$i = 0;
		$temp_friend = array();
		foreach ($mfriends as $key => $value) {
			$temp_friend[$key] = $value;
			$i++;
			if ($i==6) {
				break;
			}
		}
		$mfriends = $temp_friend;
		unset($temp_friend);
	}
	$mfriends = array_keys($mfriends);
	if (!$mfriends) {
		@include_once(D_P.'data/bbscache/mode_o_mosort.php');
		if (!$mosort || $mosort['time']<$timestamp-86400) {
			$mosort = array();
			if ($db_ifpwcache & 1) {
				$sql = "SELECT m.uid,m.username,m.icon as face FROM pw_elements e LEFT JOIN pw_members m ON e.id=m.uid WHERE e.type='usersort' AND e.mark='monthpost' ORDER BY e.value DESC ".pwLimit(6);
			} else {
				$montime = PwStrtoTime(get_date($timestamp,'Y-m').'-1');
				$sql = "SELECT m.uid,m.username,m.icon as face FROM pw_memberdata md LEFT JOIN pw_members m USING(uid) WHERE md.lastpost>".pwEscape($montime)." AND md.postnum>0 ORDER BY md.monthpost DESC ".pwLimit(6);
			}
			$query = $db->query($sql);
			while ($rt = $db->fetch_array($query)) {
				list($rt['face'])	= showfacedesign($rt['face'],'1','m');
				$rt['sub_username'] = substrs($rt['username'],7);
				$mosort[] = $rt;
			}
			$mosort['time'] = $timestamp;
			writeover(D_P.'data/bbscache/mode_o_mosort.php',"<?php\r\n\$mosort=".pw_var_export($mosort).";\r\n?>");
		}
		unset($mosort['time']);
	} else {
		$query = $db->query('SELECT uid,username,icon as face FROM pw_members WHERE uid IN('.pwImplode($mfriends).')');
		while ($rt = $db->fetch_array($query)) {
			$rt['sub_username'] = substrs($rt['username'],7);
			list($rt['face'])	= showfacedesign($rt['face'],'1','m');
			$mosort[] = $rt;
		}
	}
	$mfriends = $mosort;

	/*member's birth*/

	$sqladd = "WHERE uid IN(".pwImplode($frienddb)." ) AND uid !=".pwEscape($winduid);
	list($nyear,$nmonth,$nday) = explode('-',get_date($timestamp,'Y-n-j'));
	if ($nday + 7 <= 31) {
		$sqladd .= " AND MONTH(bday)=".pwEscape($nmonth)." AND DAYOFMONTH(bday)>=".pwEscape($nday)." AND DAYOFMONTH(bday)<=".pwEscape($nday+7);
	} else {
		$sqladd .= " AND ((MONTH(bday)=".pwEscape($nmonth)." AND DAYOFMONTH(bday)>=".pwEscape($nday)." ) OR (MONTH(bday)=".pwEscape($nmonth+1)." AND DAYOFMONTH(bday)<=".pwEscape($nday-24)."))";
	}
	$query = $db->query("SELECT uid,username,bday,gender,icon,MONTH(bday) as month,DAYOFMONTH(bday) as day FROM pw_members $sqladd LIMIT 5");

	while ($rt = $db->fetch_array($query)) {
		if ($rt['gender']==1) {
			$rt['genderinfo'] = getLangInfo('other','men');
		} elseif ($rt['gender']==2) {
			$rt['genderinfo'] = getLangInfo('other','women');
		} else {
			$rt['genderinfo'] = '';
		}
		list($rt['face']) = showfacedesign($rt['icon'],1,'m');
		$rt['age'] = $nyear - substr($rt['bday'],0,strpos($rt['bday'],'-'));
		if ($rt['day'] == $nday) {
			$rt['title'] = $rt['username'].$rt['genderinfo'].getLangInfo('other','indexbirth',array('age'=>$rt['age']));
		} else {
			$rt['title'] = $rt['username'].$rt['genderinfo'].getLangInfo('other','indexbirth2',array('age'=>$rt['age']));
		}
		$friendbith[] = $rt;

	}

	/*member's birth*/
}
if (!$db_toolbar) {
	$pwForumList = array();
	include_once(D_P.'data/bbscache/forumlist_cache.php');
	if ($pwForumAllList && $GLOBALS['groupid'] == 3) {
		$pwForumList = array_merge($pwForumList,$pwForumAllList);
	}
}

/*** userapp **/

if ($db_siteappkey && $db_appo) {
	$app_array = array();
	$appclient = L::loadClass('appclient');
	$app_array = $appclient->userApplist($winduid);
}
/*** userapp **/

//消息数目统计
$notify_message_num = $private_message_num = 0;
$query = $db->query("SELECT mid,fromuid FROM pw_msg WHERE touid=".pwEscape($winduid)."AND type='rebox' AND ifnew=1");
while ($rt = $db->fetch_array($query)) {
	if ($rt['fromuid'] == 0) {
		$notify_message_num++;
	} elseif ($rt['fromuid'] <>0) {
		$private_message_num++;
	}
}

//好友论坛最新主题
$query = $db->query("SELECT t.tid,t.author,t.authorid,t.subject,t.postdate,m.icon FROM pw_threads t LEFT JOIN pw_members m ON t.authorid=m.uid WHERE t.authorid IN(".pwImplode($frienddb).") ORDER BY t.postdate DESC LIMIT 5");
while ($rt = $db->fetch_array($query)) {
	list($rt['faceurl']) = showfacedesign($rt['icon'],'1','s');
	list($rt['posttime'],$rt['postdate']) = getLastDate($rt['postdate']);
	$rt['subject'] = substrs($rt['subject'],35);
	$friend_new_topic[] = $rt;
}
(empty($winddb['honor']) || !$_G['allowhonor']) && $winddb['honor'] = getLangInfo('other','whattosay');

$hotforum = forumSortpost(12);
function forumSortpost($num) {
	global $db,$tdtime;
	$element = L::loadClass('element');
	$temp	= $element->forumSort('article',$num);
	return $temp;
}

require_once(M_P.'require/header.php');
require_once PrintEot('m_home');
footer();
?>