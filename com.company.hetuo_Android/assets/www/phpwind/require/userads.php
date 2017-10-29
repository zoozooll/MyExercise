<?php
!function_exists('readover') && exit('Forbidden');

PwNewDB();
$a_sql	= $u ? "uid=".pwEscape($u) : "username=".pwEscape($a);
$u_db	= $db->get_one("SELECT uid AS u FROM pw_members WHERE $a_sql");// UB

if ($u_db) {
	$u		= $u_db['u'];
	$u_db	= $db->get_one("SELECT adsips FROM pw_memberinfo WHERE uid=".pwEscape($u,false));
	if ($u_db) {
		$u_db['adsips'] = strlen($u_db['adsips']) > 15000 ? '' : $u_db['adsips']."\t";
		if (strpos("\t".$u_db['adsips']."\t","\t".$onlineip."\t")===false) {
			$db->update("UPDATE pw_memberinfo SET adsips=".pwEscape($u_db['adsips'].$onlineip)." WHERE uid=".pwEscape($u,false));
			$db->update("UPDATE pw_memberdata SET credit=credit+1 WHERE uid=".pwEscape($u,false));
		}
	} else {
		$db->update("INSERT INTO pw_memberinfo SET ".pwSqlSingle(array('uid'=>$u,'adsips'=>$onlineip)));
		$db->update("UPDATE pw_memberdata SET credit=credit+1 WHERE uid=".pwEscape($u,false));
	}
	if ($winduid && $db_ads==2) {
		$pwSQL = pwSqlSingle(array(
				'uid'		=> $winduid,
				'friendid'	=> $uid,
				'joindate'	=> $timestamp,
				'status'	=> 0
			));
	}
}
Cookie('userads','',0);
?>