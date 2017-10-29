<?php
!function_exists('readover') && exit('Forbidden');

/****

@name:生日卡
@type:会员类
@effect:对特定用户使用。

****/
InitGP(array('uid'),'GP',2);
if($tooldb['type']!=2){
	Showmsg('tooluse_type_error');  // 判断道具类型是否设置错误
}
if(!$uid){
	Showmsg('tooluse_nobirther');
}
require_once(R_P.'require/msg.php');
$men = $db->get_one("SELECT username FROM pw_members WHERE uid=".pwEscape($uid));
if(!$men['username']){
	Showmsg('tooluse_nobirther');
}
$db->update("UPDATE pw_usertool SET nums=nums-1 WHERE uid=".pwEscape($winduid)."AND toolid=".pwEscape($toolid));
$msg = array(
	'toUser'	=> $men['username'],
	'fromUid'	=> $winduid,
	'fromUser'	=> $windid,
	'subject'	=> 'birth_title',
	'content'	=> 'birth_content',
);
pwSendMsg($msg);

$logdata = array(
	'type'		=>	'use',
	'descrip'	=>	'tool_16_descrip',
	'uid'		=>	$winduid,
	'username'	=>	$windid,
	'toname'	=>	$men['username'],
	'ip'		=>	$onlineip,
	'time'		=>	$timestamp,
	'toolname'	=>	$tooldb['name'],
	'subject'	=>	$subject,
);

writetoollog($logdata);

Showmsg("已向您的好友发送了生日贺卡");
?>