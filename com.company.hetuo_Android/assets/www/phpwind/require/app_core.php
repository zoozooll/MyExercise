<?php
!defined('P_W') && exit('Forbidden');
$searchUrl = $basename.'q=friend&type=find&';
/**
 * 获取单个好友的信息
 *
 * @param int $friendid
 * @return array
 */
function getOneFriend($friendid){
	global $db,$winduid;
	!$winduid && Showmsg('not_login');
	$friendid = (int) $friendid;
	$friend = $db->get_one("SELECT m.uid,m.username,m.icon,m.honor,md.f_num FROM pw_friends f LEFT JOIN pw_members m ON f.friendid=m.uid LEFT JOIN pw_memberdata md ON f.friendid=md.uid WHERE f.uid=".pwEscape($winduid)." AND f.friendid=".pwEscape($friendid)." AND f.status=0");
	if ($friend) {
		require_once(R_P.'require/showimg.php');
		list($friend['face']) = showfacedesign($friend['icon'],1);
		$friend['honor'] = substrs($friend['honor'],90);
		return $friend;
	} else {
		return false;
	}
}

function isFriend($uid,$friend) {
	global $db;
	if ($db->get_value("SELECT uid FROM pw_friends WHERE uid=" . pwEscape($uid) . ' AND friendid=' . pwEscape($friend) . " AND status='0'")) {
		return true;
	}
	return false;
}

function getOneInfo($uid){
	global $db;
	$uid = (int) $uid;
	if (!$uid) Showmsg('undefined_action');
	$oneinfo = $db->get_one("SELECT m.uid,m.username,m.email,m.groupid,m.memberid,m.icon,m.gender,m.regdate,m.honor,m.bday,m.medals,m.userstatus,md.thisvisit,md.onlinetime,md.postnum,md.digests,md.rvrc,md.money,md.credit,md.lastvisit,md.lastpost,md.todaypost,md.f_num FROM pw_members m LEFT JOIN pw_memberdata md ON m.uid=md.uid WHERE m.uid=".pwEscape($uid));
	if ($oneinfo) {
		require_once(R_P.'require/showimg.php');
		list($oneinfo['face']) = showfacedesign($oneinfo['icon'],1);
		$oneinfo['honor'] = substrs($oneinfo['honor'],90);
		return $oneinfo;
	} else {
		return false;
	}
}
/**
 * 获取相册最后上传的图片
 *
 * @param int $aid 相册id
 * @param int $num 获取数量
 * @return array
 */
function getLastPid($aid, $num = 5) {
	global $db;
	$lastpid = array();
	$query = $db->query("SELECT pid FROM pw_cnphoto WHERE aid=" . pwEscape($aid) . " ORDER BY pid DESC LIMIT $num");
	while ($rt = $db->fetch_array($query)) {
		$lastpid[] = $rt['pid'];
	}
	return $lastpid;
}

/**
 * 获取相片的真实路径
 *
 * @param string $path
 * @param bool $thumb
 * @return string
 */
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

/**
 * share应用动态解析
 */
function parseFeed($message){
	global $m;
	$message = descriplog($message);
	if (strpos($message,'{#APPS_BASEURL#}') !== false) {
		$baseurl = $m == 'o' ? 'mode.php?m=o&' : 'apps.php?';
		$message = str_replace('{#APPS_BASEURL#}',$baseurl,$message);
	}
	if (strpos($message,'[/SHARE]')!==false || strpos($message,'[/share]')!==false) {
		$message = preg_replace("/\[share\]([\w\.]+),([^\[,]+?),([\d]{1,10})\[\/share\]/eis","shareEvea('\\1','\\2','\\3')",$message);
	}
	if (strpos($message,'[/IMG]')!==false || strpos($message,'[/img]')!==false) {
		$message = preg_replace("/\[img\](https?)?([^\[]+?)\[\/img\]/is","<img src=\"\\1\\2\" width=\"80px\" height=\"80px\" />",$message);
	}
	return $message;
}

function parseFeedRead($message) {
	global $m;
	$message = str_replace(array("\r","\n"),array('',''),$message);
	$message = descriplog($message);
	if (strpos($message,'{#APPS_BASEURL#}') !== false) {
		$baseurl = $m == 'o' ? 'mode.php?m=o&' : 'apps.php?';
		$message = str_replace('{#APPS_BASEURL#}',$baseurl,$message);
	}
	if (strpos($message,'[/SHARE]')!==false || strpos($message,'[/share]')!==false) {
		$message = preg_replace("/\[share\]([\w\.]+),([^\[,]+?),([\d]{1,10})\[\/share\]/eis","",$message);
	}
	if (strpos($message,'[/IMG]')!==false || strpos($message,'[/img]')!==false) {
		$message = preg_replace("/\[img\](https?)?([^\[]+?)\[\/img\]/is","",$message);
	}
	return $message;
}

function shareEvea($type,$hash,$id){
	global $db_mode,$pwModeImg;
	static $share_js = 0;
	if (strpos($type,'.')) {
		$src = "$pwModeImg/share_v.png";
	} elseif ($type == 'music') {
		$src = "$pwModeImg/musicplay.gif";
	} elseif ($type == 'flash') {
		$src = "$pwModeImg/musicplay.gif";
	} else {
		$src = '';
	}
	if ($src) {
		return (++$share_js==1 ? '<script id="share_js" src="apps/share/js/share.js"></script>' : '').'<img style="cursor: pointer;" src="'.$src.'" onclick="javascript:showShare(\''.$type.'\',\''.$hash.'\',this,\''.$id.'\')"/>';
	} else {
		return '';
	}
}

/**
 * 获取各个评论类型的数据库表名称
 *
 * @param string $type
 * @return string
 */
function getCommTypeTable($type){
	switch ($type) {
		case 'share':
			$app_table = 'pw_share';
			$id_filed = 'id';
			break;
		case 'write':
			$app_table = 'pw_owritedata';
			$id_filed = 'id';
			break;
		case 'photo':
			$app_table = 'pw_cnphoto';
			$id_filed = 'pid';
			break;
		case 'board':
			$app_table = 'pw_oboard';
			$id_filed = 'id';
			break;
		case 'diary':
			$app_table = 'pw_diary';
			$id_filed = 'did';
			break;
		default:
			return false;
	}
	return array($app_table,$id_filed);
}

/**
 * 确认是否开启该评论
 *
 * @param string $type
 * @return bool
 */
function checkCommType($type){
	return in_array($type,array('share','write','photo','board','diary'));
}

function delAppAction($type,$typeid){
	delFeed($type,$typeid);
	return delComment($type,$typeid);
}

function delComment($type,$typeid){
	global $db;
	$affected_rows = 0;
	if (checkCommType($type)){
		$db->update("DELETE FROM pw_comment WHERE type=".pwEscape($type)." AND typeid=".pwEscape($typeid));
		$affected_rows = $db->affected_rows();
	}
	return $affected_rows;
}
function delFeed($type,$typeid){
	global $db;
	$db->update("DELETE FROM pw_feed WHERE type=".pwEscape($type)." AND typeid=".pwEscape($typeid));
	$affected_rows = $db->affected_rows();
	return $affected_rows;
}
/**
 * 取得某UID用户对当前登录用户的隐私处理
 *
 *	$isU:	2 自己	1 朋友	0 非已非友
 *	$privacy:	各各隐私设置针对当前UID是否生效
 * @param  int $uid
 */
function pwUserPrivacy($uid,$userdb) {
	global $winduid;
	$isU = $uid == $winduid ? 2 : 0;
	if (!$isU) {
		$isU = isFriend($uid,$winduid) ? 1 : 0;
	}
	$privacy['index'] = ((int)$userdb['index_privacy'] <= $isU) ? true : false;
	if ($privacy['index']) {
		$privacy['profile']	= ((int)$userdb['profile_privacy'] <= $isU) ? true : false;
		$privacy['info']	= ((int)$userdb['info_privacy'] <= $isU) ? true : false;
		$privacy['credit']	= ((int)$userdb['credit_privacy'] <= $isU) ? true : false;
		$privacy['owrite']	= ((int)$userdb['owrite_privacy'] <= $isU) ? true : false;
		$privacy['msgboard']= ((int)$userdb['msgboard_privacy'] <= $isU) ? true : false;
		$privacy['photos']	= ((int)$userdb['photos_privacy'] <= $isU) ? true : false;
		$privacy['diary']	= ((int)$userdb['diary_privacy'] <= $isU) ? true : false;
	} else {
		$privacy['profile']	= false;
		$privacy['info']	= false;
		$privacy['credit']	= false;
		$privacy['owrite']	= false;
		$privacy['msgboard']= false;
		$privacy['photos']	= false;
		$privacy['diary']	= false;
	}
	return array($isU,$privacy);
}

/**
 * 圈子中各种用户发表信息统计，记录、分享、群组、相册、回复等
 *
 * @param string $exp 表达式，包含+或-符号
 */
function countPosts($exp='+1') {
	global $db;
	$num = intval(trim($exp,'+-'));
	if (strpos($exp,'+') !== false) {
		$db->update("UPDATE pw_bbsinfo SET o_post=o_post+".pwEscape($num,false).",o_tpost=o_tpost+".pwEscape($num,false));
	} else {
		$db->update("UPDATE pw_bbsinfo SET o_post=o_post-".pwEscape($num,false).",o_tpost=o_tpost-".pwEscape($num,false));
	}
}

function getAppClassName($type){
	if ($type=='share') {
		return 'i-o';
	} elseif ($type=='board') {
		return 'i-b';
	} elseif ($type=='friend') {
		return 'i-d';
	} elseif ($type=='photo') {
		return 'i-j';
	} elseif ($type=='post') {
		return 'i-a';
	} elseif ($type=='write') {
		return 'i-k';
	} elseif ($type=='diary') {
		return 'i-t';
	} elseif ($type=='colony'){
		return 'i-e';
	} else {
		return 'i-a';
	}
}

function banUser(){
	global $db,$groupid,$winduid,$timestamp;

	if ($groupid == 6) {
		$pwSQL = '';
		$flag  = 0;
		$bandb = $delban = array();
		$query = $db->query("SELECT * FROM pw_banuser WHERE uid=".pwEscape($winduid)." AND fid='0'");
		while ($rt = $db->fetch_array($query)) {
			if ($rt['type'] == 1 && $timestamp - $rt['startdate'] > $rt['days']*86400) {
				$delban[] = $rt['id'];
			} else {
				$bandb = $rt;
			}
		}
		$delban && $db->update('DELETE FROM pw_banuser WHERE id IN('.pwImplode($delban).')');
		($groupid == 6 && !isset($bandb)) && $pwSQL .= "groupid='-1',";
		if ($pwSQL = rtrim($pwSQL,',')) {
			$db->update("UPDATE pw_members SET $pwSQL WHERE uid=".pwEscape($winduid));
			$_cache = getDatastore();
			$_cache->delete('UID_'.$winduid);
		}
		if ($bandb) {
			if ($bandb['type'] == 1) {
				global $s_date,$e_date;
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
		$bandb = $db->get_one("SELECT type FROM pw_banuser WHERE uid=".pwEscape($force)." AND fid='0'");
		if ($bandb['type'] == 3) {
			Showmsg('ban_info3');
		} else {
			Cookie('force','',0);
		}
	}
	/**
	* 需要验证用户只有通过管理员验证后才能发帖
	*/
	if ($groupid == '7') {
		Showmsg('post_check');
	}
}

function appShield($code) {
	$code = getLangInfo('other',$code);
	return "<span style=\"color:black;background-color:#ffff66\">$code</span>";
}
/**
 * 取得某个操作实际要增加的积分值
 *
 * @param array $creditset 特定动作所要更新的积分的绝对值
 * @param boolen $type true:加 false:减
 * @param int $num 积分增加倍数
 */
function getCreditset($creditset,$type = true,$num = 1) {
	empty($creditset) && $creditset = array();
	if ($type == false) {
		foreach ($creditset as $key => $value) {
			$creditset[$key] = -$value * $num;
		}
	} else {
		foreach ($creditset as $key => $value) {
			$creditset[$key] = $value * $num;
		}
	}
	return $creditset;
}

function updateMemberid($uid,$isown = true){
	global $db,$winddb,$creditset,$db_upgrade,$credit,$lneed;
	$lneed || $lneed = L::config('lneed', 'level');
	if ($isown == true) {
		$userdb = $winddb;
	} else {
		$userdb = $db->get_one("SELECT m.memberid,md.rvrc,md.money,md.credit,md.currency,md.postnum,md.digests,md.onlinetime FROM pw_members m LEFT JOIN pw_memberdata md ON m.uid=md.uid WHERE m.uid=".pwEscape($uid));
	}
	$userdb['rvrc']   += $creditset['rvrc'];
	$userdb['money']  += $creditset['money'];
	$userdb['credit'] += $creditset['credit'];
	$userdb['currency'] += $creditset['currency'];

	$usercredit = array(
		'postnum'	=> $userdb['postnum'],
		'digests'	=> $userdb['digests'],
		'rvrc'		=> $userdb['rvrc'],
		'money'		=> $userdb['money'],
		'credit'	=> $userdb['credit'],
		'currency'	=> $userdb['currency'],
		'onlinetime'=> $userdb['onlinetime'],
	);
	$upgradeset = unserialize($db_upgrade);

	foreach ($upgradeset as $key => $val) {
		if (is_numeric($key) && $val) {
			foreach ($credit->get($uid,'CUSTOM') as $key => $value) {
				$usercredit[$key] = $value;
			}
			break;
		}
	}
	$memberid = getmemberid(CalculateCredit($usercredit,$upgradeset));
	if ($userdb['memberid'] != $memberid) {
		$db->update("UPDATE pw_members SET memberid=".pwEscape($memberid)." WHERE uid=".pwEscape($uid));
		$_cache = getDatastore();
		$_cache->delete('UID_'.$uid);
	}
}

function addLog($creditlog,$username,$uid,$logtype){
	global $db,$creditset,$credit,$timestamp,$db_ifcredit,$onlineip;
	$credit_pop = '';
	$cLog = array();
	empty($creditset) && $creditset = array();
	foreach ($creditset as $key => $affect) {

		if (isset($credit->cType[$key]) && $affect<>0 && isset($creditlog[$key])) {

			$log['username'] = Char_cv($username);
			$log['cname']	 = $credit->cType[$key];
			$log['affect']	 = $affect;
			$log['affect'] > 0 && $log['affect'] = '+'.$log['affect'];
			$log['descrip'] = Char_cv(getLangInfo('creditlog',$logtype,$log));

			$credit_pop .= $key.":".$log['affect'].'|';
			$cLog[] = array($uid, $log['username'], $key, $affect, $timestamp, $logtype, $onlineip, $log['descrip']);
		}
	}
	if ($db_ifcredit && $credit_pop) {//Credit Changes Tips
		$credit_pop = $logtype.'|'.$credit_pop;
		$db->update("UPDATE pw_memberdata SET creditpop=".pwEscape($credit_pop)." WHERE uid=".pwEscape($uid),0);
	}
	if (!empty($cLog)) {
		$db->update("INSERT INTO pw_creditlog (uid,username,ctype,affect,adddate,logtype,ip,descrip) VALUES ".pwSqlMulti($cLog,false));
	}
	$cLog = array();
}

/**
 * 用户app统计更新
 * @param (int|array) $uids 需要更新的用户uid
 * @param string $action recount:重新统计,add:指定用户应用数加1,minus:指定用户应用数减1
 */
function updateUserAppNum($uids,$type,$action='add',$num=0){
	global $db,$timestamp;
	if (empty($uids)) return false;
	!is_array($uids) && $uids = array($uids);
	!in_array($type,array('diary','photo','owrite','group','share')) && Showmsg('app_type_worong');
	$keyname = $type.'num';
	$num = intval($num);

	if ($action == 'recount') {
		if ($type == 'diary') {
			$query = $db->query("SELECT uid,COUNT(*) as count FROM pw_diary WHERE uid IN (".pwImplode($uids).") GROUP BY uid");
		} elseif ($type == 'photo') {
			$query = $db->query("SELECT ca.ownerid as uid,COUNT(*) as count FROM pw_cnphoto cn LEFT JOIN pw_cnalbum ca ON cn.aid=ca.aid WHERE ca.atype='0' AND ca.ownerid IN (".pwImplode($uids).") GROUP BY ca.ownerid");
		} elseif ($type == 'owrite') {
			$query = $db->query("SELECT uid,COUNT(*) as count FROM pw_owritedata WHERE uid IN (".pwImplode($uids).") GROUP BY uid");
		} elseif ($type == 'group') {
			 $query = $db->query("SELECT uid,COUNT(*) as count FROM pw_cmembers WHERE uid IN (".pwImplode($uids).") AND ifadmin!= '-1' GROUP BY uid");
		} elseif ($type == 'share') {
			$query = $db->query("SELECT uid, COUNT(*) as count FROM pw_share WHERE uid IN (".pwImplode($uids).") GROUP BY uid");
		}
		while ($rt = $db->fetch_array($query)) {
			$uid = $rt['uid'];
			$count = $rt['count'];
			$db->pw_update(
				"SELECT * FROM pw_ouserdata WHERE uid=".pwEscape($uid),
				"UPDATE pw_ouserdata SET ".pwSqlSingle(array($keyname => $count))." WHERE uid=".pwEscape($uid),
				"INSERT INTO pw_ouserdata SET ".pwSqlSingle(array('uid' => $uid,$keyname => $count))
			);
		}
	} elseif ($action == 'add') {
		$lastpost_keyname = $type.'_lastpost';
		$num = $num < 1 ? 1 : $num;
		$query = $db->query("SELECT uid FROM pw_members WHERE uid IN (".pwImplode($uids).")");
		while ($rt = $db->fetch_array($query)) {
			$uid = $rt['uid'];
			$db->pw_update(
				"SELECT * FROM pw_ouserdata WHERE uid=".pwEscape($uid),
				"UPDATE pw_ouserdata SET $keyname = $keyname + $num,$lastpost_keyname = '$timestamp' WHERE uid=".pwEscape($uid),
				"INSERT INTO pw_ouserdata SET ".pwSqlSingle(array('uid' => $uid,$keyname => $num,$lastpost_keyname => $timestamp))
			);
		}
	} elseif ($action == 'minus') {
		$num < 1 && $num = 1;
		$db->update("UPDATE pw_ouserdata SET {$keyname}={$keyname}-" . pwEscape($num). " WHERE uid IN(" . pwImplode($uids) . ") AND {$keyname}>=" . pwEscape($num));
		$db->update("UPDATE pw_ouserdata SET {$keyname}=0 WHERE uid IN(" . pwImplode($uids) . ") AND {$keyname}<" . pwEscape($num));
		/*
		$query = $db->query("SELECT uid FROM pw_members WHERE uid IN (".pwImplode($uids).")");
		while ($rt = $db->fetch_array($query)) {
			$uid = $rt['uid'];
			$keyvalue = $db->get_value("SELECT $keyname FROM pw_ouserdata WHERE uid=".pwEscape($uid));
			if ($keyvalue < $num) {
				$db->update("UPDATE pw_ouserdata SET $keyname = '0' WHERE uid=".pwEscape($uid));
			} else {
				$db->update("UPDATE pw_ouserdata SET $keyname = $keyname - $num WHERE uid=".pwEscape($uid));
			}
		}
		*/
	}
}

/**
 * 获取评论内容
 */

function getCommentDb($type,$typeids){
	global $db,$groupid,$db_shield;
	if(!checkCommType($type)) Showmsg('undefined_action');
	$wordsfb = L::loadClass('FilterUtil');
	$query = $db->query("SELECT c.id,c.uid,c.username,c.title,c.postdate,c.typeid,c.ifwordsfb,m.icon as face,m.groupid FROM pw_comment c LEFT JOIN pw_members m ON c.uid=m.uid WHERE c.type=".pwEscape($type)." AND c.typeid IN (".pwImplode($typeids).") ORDER BY postdate ASC");
	while ($rt = $db->fetch_array($query)) {
		$rt['postdate'] = get_date($rt['postdate']);
		list($rt['face'])	=  showfacedesign($rt['face'],1,'m');
		if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
			$rt['title'] = getLangInfo('other','ban_comment');
		} elseif (!$wordsfb->equal($rt['ifwordsfb'])) {
			$rt['title'] = $wordsfb->convert($rt['title'], array(
				'id'	=> $rt['id'],
				'type'	=> 'comments',
				'code'	=> $rt['ifwordsfb']
			));
		}
		$commentdb[$rt['typeid']][] = $rt;
	}
	return $commentdb;
}

function getCommentDbByTypeid($type,$typeid,$page,$url) {
	global $db,$groupid,$db_shield,$db_perpage,$db_windpost;
	if(!checkCommType($type)) Showmsg('undefined_action');
	$wordsfb = L::loadClass('FilterUtil');
	$commentdb = $subcommentdb = array();
	$count = $db->get_value("SELECT COUNT(*) FROM pw_comment WHERE type=".pwEscape($type)." AND typeid=".pwEscape($typeid)." AND upid='0'");
	$numofpage = ceil($count/$db_perpage);
	$start = ($page-1) * $db_perpage;
	$limit = pwLimit($start,$db_perpage);
	$query = $db->query("SELECT c.id,c.uid,c.username,c.title,c.postdate,c.typeid,c.upid,c.ifwordsfb,m.icon as face,m.groupid FROM pw_comment c LEFT JOIN pw_members m ON c.uid=m.uid WHERE c.type=".pwEscape($type)." AND c.typeid=".pwEscape($typeid)." AND upid='0' ORDER BY postdate DESC $limit");
	while ($rt = $db->fetch_array($query)) {
		$rt['postdate'] = get_date($rt['postdate']);
		list($rt['face'])	=  showfacedesign($rt['face'],1,'m');
		if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
			$rt['title'] = getLangInfo('other','ban_comment');
		} elseif (!$wordsfb->equal($rt['ifwordsfb'])) {
			$rt['title'] = $wordsfb->convert($rt['title'], array(
				'id'	=> $rt['id'],
				'type'	=> 'comments',
				'code'	=> $rt['ifwordsfb']
			));
		}
		if (strpos($rt['title'],'[s:') !== false) {
			$rt['title'] = showface($rt['title']);
		}
		if (strpos($rt['title'],'[url') !== false) {
			$rt['title'] = convert($rt['title'],$db_windpost);
		}
		$commentids[] = $rt['id'];
		$commentdb[$rt['id']] = $rt;
	}
	if ($commentids) {
		$query = $db->query("SELECT c.id,c.uid,c.username,c.title,c.postdate,c.typeid,c.upid,c.ifwordsfb,m.icon as face,m.groupid FROM pw_comment c LEFT JOIN pw_members m ON c.uid=m.uid WHERE c.type=".pwEscape($type)." AND c.typeid=".pwEscape($typeid)." AND upid IN (".pwImplode($commentids).") ORDER BY postdate ASC");
		while ($rt = $db->fetch_array($query)) {
			$rt['postdate'] = get_date($rt['postdate']);
			list($rt['face'])	=  showfacedesign($rt['face'],1,'m');
			if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
				$rt['title'] = getLangInfo('other','ban_comment');
			} elseif (!$wordsfb->equal($rt['ifwordsfb'])) {
				$rt['title'] = $wordsfb->convert($rt['title'], array(
					'id'	=> $rt['id'],
					'type'	=> 'comments',
					'code'	=> $rt['ifwordsfb']
				));
			}
			$subcommentdb[$rt['upid']][$rt['id']] = $rt;
		}
	}
	$pages = numofpage($count,$page,$numofpage,$url);
	return array($commentdb,$subcommentdb,$pages);
}


/**
 * 获取个人空间左侧栏信息
 */
function getAppleftinfo($u,$type=false) {
	global $db,$db_plist,$winduid,$db_upgrade,$credit;
	$userdb = array();
	$userdb = $db->get_one("SELECT m.uid,m.username,m.email,m.groupid,m.icon,md.rvrc,md.money,md.credit,md.currency,md.digests,md.postnum,md.lastpost,md.onlinetime,ud.diarynum,ud.photonum,ud.owritenum,ud.groupnum,ud.sharenum,ud.diary_lastpost,ud.photo_lastpost,ud.owrite_lastpost,ud.group_lastpost,ud.share_lastpost FROM pw_members m LEFT JOIN pw_memberdata md ON m.uid=md.uid LEFT JOIN pw_ouserdata ud ON m.uid=ud.uid WHERE m.uid=".pwEscape($u));

	$ismyfriend = isFriend($winduid,$u);
	$friendcheck = getstatus($userdb['userstatus'],3,3);
//	$usericon = showfacedesign($userdb['icon'],true);
	list($usericon) = showfacedesign($userdb['icon'], 1, 'm');
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
	$totalcredit = CalculateCredit($usercredit, unserialize($db_upgrade));
	$userdb['rvrc'] /= 10;
	$app_with_count = array('topic','diary','photo','owrite','group','share');
	foreach ($app_with_count as $key => $value) {
		$postnum = $posttime = '';
		$appcount[$value] = getPostnumByType($value,$userdb,true);
	}
	$p_list = $db_plist && count($db_plist)>1 ? $db_plist : array();

	return array($userdb,$ismyfriend,$friendcheck,$usericon,$usercredit,$totalcredit,$appcount,$p_list);
}

function getPostnumByType($type,$userdb=array(),$a=false) {
	global $timestamp;
	$posttime = '';
	if ($a == false) {
		global $userdb;
	}
	if ($type == 'topic') {
		$postnum = $userdb['postnum'];
		if ($timestamp - $userdb['lastpost'] < 604800) {
			$posttime = get_date($userdb['lastpost'],'m-d');
		}
	} else {
		$postnum = $userdb[$type.'num'] ? $userdb[$type.'num'] : '0';
		if ($timestamp - $userdb[$type.'_lastpost'] < 604800) {
			$posttime = get_date($userdb[$type.'_lastpost'],'m-d');
		}
	}
	return array($postnum,$posttime);
}

function pwLimitPages($count,$page,$pageurl) {
	global $db_perpage,$db_maxpage;
	//require_once (R_P.'require/forum.php');
	$numofpage = ceil($count/$db_perpage);
	$numofpage = ($db_maxpage && $numofpage > $db_maxpage) ? $db_maxpage : $numofpage;
	$page < 1 ? $page = 1 : ($page > $numofpage ? $page = $numofpage : null);
	$pages = numofpage($count,$page,$numofpage,$pageurl,$db_maxpage);
	$limit = pwLimit(($page-1) * $db_perpage,$db_perpage);
	return array($pages,$limit);
}
?>