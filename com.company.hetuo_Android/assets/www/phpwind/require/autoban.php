<?php
!function_exists('readover') && exit('Forbidden');

function autoban($uid) {
	global $db,$db_banby,$db_banmax,$db_bantype,$db_banlimit,$timestamp;
	$rt = $db->get_one('SELECT m.groupid,m.memberid,md.postnum,md.rvrc,md.money FROM pw_members m LEFT JOIN pw_memberdata md ON md.uid=m.uid WHERE m.uid='.pwEscape($uid));
	if ($rt['groupid'] == '-1' || $rt['groupid'] == '6') {
		switch ($db_banby) {
			case 1:$banby = $rt['postnum']; break;
			case 2:$banby = $rt['rvrc']/10;break;
			case 3:$banby = $rt['money'];break;
			default:$banby = $rt['postnum'];
		}
		if ($rt['groupid'] == '-1') {
			if ($banby < $db_banmax) {
				$db->update("UPDATE pw_members SET groupid='6' WHERE uid=".pwEscape($uid));
				$pwSQL = pwSqlSingle(array(
					'uid'		=> $uid,
					'fid'		=> 0,
					'type'		=> $db_bantype,
					'startdate'	=> $timestamp,
					'days'		=> $db_banlimit,
					'admin'		=> 'autoban',
					'reason'	=> ''
				));
				$db->update("REPLACE INTO pw_banuser SET $pwSQL");
			}
		} elseif ($banby >= $db_banmax) {
			$bandb = $db->get_one("SELECT id FROM pw_banuser WHERE uid=".pwEscape($uid)." AND fid='0'");
			if (!$bandb) {
				$db->update("UPDATE pw_members SET groupid='-1' WHERE uid=".pwEscape($uid));
			} elseif ($bandb['type'] == 1 && $timestamp-$bandb['startdate']>$bandb['days']*86400) {
				$db->update("UPDATE pw_members SET groupid='-1' WHERE uid=".pwEscape($uid));
				$db->update("DELETE FROM pw_banuser WHERE id=".pwEscape($bandb['id']));
			}
		}
		$_cache = getDatastore();
		$_cache->delete('UID_'.$uid);
	}
}
?>