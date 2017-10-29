<?php
!function_exists('readover') && exit('Forbidden');

function updatecache_i($fid,$aidin=null) {
	global $db,$db_windpost,$timestamp,$forum;
	require_once(R_P.'require/bbscode.php');
	include(D_P.'data/bbscache/forum_cache.php');

	$sql_where = empty($aidin) ? "fid=".pwEscape($fid) : "aid IN ($aidin)";
	$F_ffid = false; $aid = $aidcache = 0; $aids = '';
	$query = $db->query("SELECT aid,startdate,enddate,content FROM pw_announce WHERE $sql_where AND ifopen='1' AND (enddate=0 OR enddate>=".pwEscape($timestamp).") ORDER BY vieworder,startdate DESC");
	while ($rt = $db->fetch_array($query)) {
		if ($rt['startdate']<=$timestamp) {
			if ($F_ffid) {
				continue;
			} elseif (!$rt['enddate']) {
				$F_ffid = true;
			}
		}
		if (!$aid && $rt['startdate']<=$timestamp && (!$rt['enddate'] || $rt['enddate']>=$timestamp)) {
			$aid = $rt['aid'];
			if ($rt['content']!=convert($rt['content'],$db_windpost,2)) {
				$db->update("UPDATE pw_announce SET ifconvert='1' WHERE aid=".pwEscape($aid));
			}
		} else {
			$aids .= ",$rt[aid]";
		}
	}
	if ($aids) {
		$aids = substr($aids,1);
		$aidcache = $timestamp;
	}
	$db->update("UPDATE pw_forumdata SET ".pwSqlSingle(array('aid'=>$aid,'aids'=>$aids,'aidcache'=>$aidcache))."WHERE fid=".pwEscape($fid));
}
?>