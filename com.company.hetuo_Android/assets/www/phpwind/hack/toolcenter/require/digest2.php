<?php
!function_exists('readover') && exit('Forbidden');

/****

@name:精华II道具
@type:帖子类
@effect:可以将自己的帖子加为精华II

****/

if($tooldb['type']!=1){
	Showmsg('tooluse_type_error');  // 判断道具类型是否设置错误
}
if($tpcdb['authorid'] != $winduid){
	Showmsg('tool_authorlimit');
}
$db->update("UPDATE pw_threads SET digest='2',toolinfo=".pwEscape($tooldb['name'])."WHERE tid=".pwEscape($tid));
$db->update("UPDATE pw_memberdata SET digests=digests+1 WHERE uid=".pwEscape($winduid));
$db->update("UPDATE pw_usertool SET nums=nums-1 WHERE uid=".pwEscape($winduid)."AND toolid=".pwEscape($toolid));
$logdata=array(
	'type'		=>	'use',
	'descrip'	=>	'tool_10_descrip',
	'uid'		=>	$winduid,
	'username'	=>	$windid,
	'ip'		=>	$onlineip,
	'time'		=>	$timestamp,
	'toolname'	=>	$tooldb['name'],
	'subject'	=>	substrs($tpcdb['subject'],15),
	'tid'		=>	$tid,
);
writetoollog($logdata);
Showmsg('toolmsg_success');
?>