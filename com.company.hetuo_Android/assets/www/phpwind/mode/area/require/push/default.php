<?php
!defined('M_P') && exit('Forbidden');
define('AJAX','1');

$weeknum = get_date($timestamp,'w');
$weeknum < 1 && $weeknum = 7;
$weektime = $tdtime-($weeknum-1)*86400;
$cachenum = 20;

$weekname	= 'fweek';
$monthname	= 'fmonth';
$sqladd		= '';
$fidoff		= array();
$ifview		= false;
include_once(D_P.'data/bbscache/forumcache.php');

$query = $db->query("SELECT fid,password,allowvisit FROM pw_forums WHERE password!='' OR allowvisit!='' OR f_type='hidden'");
while ($rt = $db->fetch_array($query)) {
	$fidoff[] = $rt['fid'];
	if ($fid == $rt['fid'] && !$rt['password'] && strpos($rt['allowvisit'],",$groupid,")!==false) {
		$ifview = true;
	}
}

if ($fid && (!in_array($fid,$fidoff) || $ifview)) {
	include_once(D_P.'data/bbscache/forum_cache.php');
	$sqladd		 = " AND fid=".pwEscape($fid);
	$weekname	.= '_'.$fid;
	$monthname	.= '_'.$fid;
	$forumcache = str_replace("<option value=\"$fid\">","<option value=\"$fid\" selected>",$forumcache);
} elseif ($fidoff) {
	$sqladd		 = " AND fid NOT IN(".pwImplode($fidoff).")";
}

require_once(R_P.'require/header.php');
$threaddb = array(array(),array(),array());

$query = $db->query("SELECT tid,author,authorid,subject,dig FROM pw_threads WHERE postdate>".pwEscape($tdtime)." AND dig>0 $sqladd ORDER BY dig DESC LIMIT $cachenum");
while ($rt = $db->fetch_array($query)) {
	$rt['subject'] = substrs($rt['subject'],25);
	$threaddb[0][] = $rt;
}
$shtdb = array();
$query = $db->query("SELECT * FROM pw_cache WHERE name IN(".pwImplode(array($weekname,$monthname)).")");
while ($rt = $db->fetch_array($query)) {
	$shtdb[$rt['name']] = $rt;
}

if(!isset($shtdb[$weekname]) || $timestamp-$shtdb[$weekname]['time']>21600 || $shtdb[$weekname]['time']<$weektime){
	$query = $db->query("SELECT tid,author,authorid,subject,dig FROM pw_threads WHERE postdate>".pwEscape($weektime)." AND dig>0 $sqladd ORDER BY dig DESC LIMIT $cachenum");
	while ($rt = $db->fetch_array($query)) {
		$rt['subject'] = substrs($rt['subject'],25);
		$threaddb[1][] = $rt;
	}
	$cache = $threaddb[1] ? addslashes(serialize($threaddb[1])) : '';
	$pwSQL = pwSqlSingle(array(
		'name'	=> $weekname,
		'cache'	=> $cache,
		'time'	=> $timestamp
	));
	$db->update("REPLACE INTO pw_cache	SET $pwSQL");
} else {
	$shtdb[$weekname]['cache'] && $threaddb[1] = unserialize($shtdb[$weekname]['cache']);
}
if (!isset($shtdb[$monthname]) || $timestamp-$shtdb[$monthname]['time']>86400 || $shtdb[$monthname]['time']<$montime) {
	$query = $db->query("SELECT tid,author,authorid,subject,dig FROM pw_threads WHERE postdate>".pwEscape($montime)." AND dig>0 $sqladd ORDER BY dig DESC LIMIT $cachenum");
	while ($rt = $db->fetch_array($query)) {
		$rt['subject'] = substrs($rt['subject'],25);
		$threaddb[2][] = $rt;
	}
	$cache = $threaddb[2] ? addslashes(serialize($threaddb[2])) : '';
	$pwSQL = pwSqlSingle(array(
		'name'	=> $monthname,
		'cache'	=> $cache,
		'time'	=> $timestamp
	));
	$db->update("REPLACE INTO pw_cache SET $pwSQL");
} else {
	$shtdb[$monthname]['cache'] && $threaddb[2] = unserialize($shtdb[$monthname]['cache']);
}
include areaLoadFrontView($action);

ajax_footer();
?>