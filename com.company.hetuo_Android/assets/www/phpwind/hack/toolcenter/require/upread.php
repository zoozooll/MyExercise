<?php
!function_exists('readover') && exit('Forbidden');

/****

@name:提前道具
@type:帖子类
@effect:可以把自己发表的帖子提前到帖子所在版块的第一页

****/

if($tooldb['type']!=1){
	Showmsg('tooluse_type_error');  // 判断道具类型是否设置错误
}
if($tpcdb['authorid'] != $winduid){
	Showmsg('tool_authorlimit');
}
$db->update("UPDATE pw_threads SET lastpost=".pwEscape($timestamp).",toolinfo=".pwEscape($tooldb['name'],false)."WHERE tid=".pwEscape($tid));
# memcache refresh
$threadList = L::loadClass("threadlist");
$threadList->updateThreadIdsByForumId($fid,$tid);
$db->update("UPDATE pw_usertool SET nums=nums-1 WHERE uid=".pwEscape($winduid)."AND toolid=".pwEscape($toolid));
$logdata=array(
	'type'		=>	'use',
	'nums'		=>	'',
	'money'		=>	'',
	'descrip'	=>	'tool_7_descrip',
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