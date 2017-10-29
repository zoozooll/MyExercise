<?php
!function_exists('readover') && exit('Forbidden');

/****

@name:还原卡
@type:会员类
@effect:清除猪头卡的效果．

****/
InitGP(array('uid'),'GP',2);
if($tooldb['type']!=2){
	Showmsg('tooluse_type_error');  // 判断道具类型是否设置错误
}
if(!$uid){
	Showmsg('tooluse_nopig');
}
$rt   = $db->get_one("SELECT icon,username FROM pw_members WHERE uid=".pwEscape($uid));
$user_a=explode('|',addslashes($rt['icon']));
if(empty($user_a[4])){
	Showmsg('tooluse_nousepig');
}else{
	$userface="$user_a[0]|$user_a[1]|$user_a[2]|$user_a[3]";
}
$db->update("UPDATE pw_members SET icon=".pwEscape($userface)."WHERE uid=".pwEscape($uid));
$db->update("UPDATE pw_usertool SET nums=nums-1 WHERE uid=".pwEscape($winduid)."AND toolid=".pwEscape($toolid));
$logdata=array(
	'type'		=>	'use',
	'descrip'	=>	'tool_19_descrip',
	'uid'		=>	$winduid,
	'username'	=>	$windid,
	'toname'	=>	$rt['username'],
	'ip'		=>	$onlineip,
	'time'		=>	$timestamp,
	'toolname'	=>	$tooldb['name'],
);
writetoollog($logdata);
Showmsg('toolmsg_success');
?>