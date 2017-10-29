<?php
!function_exists('readover') && exit('Forbidden');

/****

@name:时空卡
@type:帖子类
@effect:帖子中使用，让帖子发布到12小时后,使其12小时内不沉。

****/

if($tooldb['type']!=1){
	Showmsg('tooluse_type_error');  // 判断道具类型是否设置错误
}

$db->update("UPDATE pw_threads SET lastpost=lastpost+43200,toolinfo=".pwEscape($tooldb['name'],false)."WHERE tid=".pwEscape($tid));
# memcache refresh
$threadList = L::loadClass("threadlist");
$threadList->updateThreadIdsByForumId($fid,$tid);
$db->update("UPDATE pw_usertool SET nums=nums-1 WHERE uid=".pwEscape($winduid)."AND toolid=".pwEscape($toolid));
$logdata=array(
	'type'		=>	'use',
	'descrip'	=>	'tool_22_descrip',
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