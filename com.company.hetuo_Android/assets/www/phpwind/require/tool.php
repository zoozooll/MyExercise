<?php
!function_exists('readover') && exit('Forbidden');

/*
* 检查道具是否启用和用户是否拥有使用道具的权限
*/
function CheckUserTool($uid,$tooldb) {
	global $db,$groupid,$credit;

	if (!$tooldb['state']) {
		Showmsg('tool_close');
	}
	$condition = unserialize($tooldb['conditions']);
	if ($condition['group'] && strpos($condition['group'],",$groupid,") === false) {
		Showmsg('tool_grouplimit');
	}
	$userdb   = $db->get_one("SELECT postnum,digests,rvrc,money,credit FROM pw_memberdata WHERE uid=".pwEscape($uid));
	require_once(R_P.'require/credit.php');
	$creditdb = $credit->get($uid,'CUSTOM');
	foreach ($condition['credit'] as $key => $value) {
		if ($value) {
			if (is_numeric($key)) {
				$creditdb[$key] < $value && Showmsg('tool_creditlimit');
			} elseif ($userdb[$key] < $value) {
				Showmsg('tool_creditlimit');
			}
		}
	}
}

function writetoollog($log) {
	global $db,$db_bbsurl;
	$log['type']    = getLangInfo('toollog',$log['type']);
	$log['filename']= Char_cv($log['filename']);
	$log['username']= Char_cv($log['username']);
	$log['descrip'] = Char_cv(getLangInfo('toollog',$log['descrip'],$log));

	$db->update("INSERT INTO pw_toollog SET " . pwSqlSingle(array(
		'type'		=> $log['type'],
		'filename'	=> $log['filename'],
		'nums'		=> $log['nums'],
		'money'		=> $log['money'],
		'descrip'	=> $log['descrip'],
		'uid'		=> $log['uid'],
		'touid'		=> $log['touid'],
		'username'	=> $log['username'],
		'ip'		=> $log['ip'],
		'time'		=> $log['time']
	)));
}
?>