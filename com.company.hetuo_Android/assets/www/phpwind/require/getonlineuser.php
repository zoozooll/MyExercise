<?php
!function_exists('readover') && exit('Forbidden');

function GetOnlineUser() {
	global $db_online,$db;
	$onlineuser = array();

	if ($db_online == 1) {
		$query = $db->query("SELECT username,uid FROM pw_online WHERE uid>0");
		while ($rt = $db->fetch_array($query)) {
			$onlineuser[$rt['uid']] = $rt['username'];
		}
	} else {
		$onlinedb = openfile(D_P.'data/bbscache/online.php');
		if (count($onlinedb) == 1) {
			$onlinedb = array();
		} else {
			unset($onlinedb[0]);
		}
		foreach ($onlinedb as $key => $value) {
			if (trim($value)) {
				$dt = explode("\t",$value);
				$onlineuser[$dt[8]] = $dt[0];
			}
		}
	}
	return $onlineuser;
}
?>