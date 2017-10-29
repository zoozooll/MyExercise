<?php
!function_exists('readover') && exit('Forbidden');

/****

@name:沉淀卡
@type:帖子类
@effect:帖子中使用，让帖子丢到12小时前,回复也不再上来．

****/

if($tooldb['type']!=1){
	Showmsg('tooluse_type_error');  // 判断道具类型是否设置错误
}

$db->update("UPDATE pw_threads SET toolinfo=".pwEscape($tooldb['name'],false).",locked='3',lastpost=lastpost-43200 WHERE tid='$tid'");
# memcache refresh
$threadList = L::loadClass("threadlist");
$threadList->updateThreadIdsByForumId($fid,$tid);
$db->update("UPDATE pw_usertool SET nums=nums-1 WHERE uid=".pwEscape($winduid)."AND toolid=".pwEscape($toolid));
$logdata = array(
	'type'		=>	'use',
	'descrip'	=>	'tool_17_descrip',
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