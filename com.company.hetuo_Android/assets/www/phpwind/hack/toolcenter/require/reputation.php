<?php
!function_exists('readover') && exit('Forbidden');

/****

@name:清零卡
@type:会员类
@effect:可将自已负威望清零

****/

if ($tooldb['type'] != 2) {
	Showmsg('tooluse_type_error');  // 判断道具类型是否设置错误
}
$rt = $db->get_one("SELECT rvrc FROM pw_memberdata WHERE uid=".pwEscape($winduid));

if ($rt['rvrc'] < 0) {
	$db->update("UPDATE pw_memberdata SET rvrc=0 WHERE uid=".pwEscape($winduid));
	$db->update("UPDATE pw_usertool SET nums=nums-1 WHERE uid=".pwEscape($winduid)."AND toolid=".pwEscape($toolid));
	$logdata = array(
		'type'		=>	'use',
		'nums'		=>	'',
		'money'		=>	'',
		'descrip'	=>	'tool_1_descrip',
		'uid'		=>	$winduid,
		'username'	=>	$windid,
		'ip'		=>	$onlineip,
		'time'		=>	$timestamp,
		'toolname'	=>	$tooldb['name'],
		'from'		=>	'',
	);
	writetoollog($logdata);
	Showmsg('toolmsg_1_success');
} else {
	Showmsg('toolmsg_1_failed');
}
?>