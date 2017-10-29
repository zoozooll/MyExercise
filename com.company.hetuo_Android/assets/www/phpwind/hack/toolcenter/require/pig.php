<?php
!function_exists('readover') && exit('Forbidden');

/****

@name:猪头卡
@type:会员类
@effect:使用后让对方头像变为猪头，卡片效果持续24小时或到对方使用还原卡为止．

****/
InitGP(array('uid'),'GP',2);
if($tooldb['type']!=2){
	Showmsg('tooluse_type_error');  // 判断道具类型是否设置错误
}
if(!$uid){
	Showmsg('不存在变为猪头的对像');
}
$rt = $db->get_one("SELECT MAX(time) AS tooltime FROM pw_toollog WHERE touid=".pwEscape($uid)."AND filename='defend'");


if($rt && $rt['tooltime']>$timestamp-3600*48){
	Showmsg('该会员已经使用了护身符,不能对其使用猪头术');
}
$rt     = $db->get_one("SELECT icon FROM pw_members WHERE uid=".pwEscape($uid));
$user_a = explode('|',addslashes($rt['icon']));
if($user_a[4]){
	Showmsg('已经变为猪头，此猪头卡失效');
}
$db->update("UPDATE pw_usertool SET nums=nums-1 WHERE uid=".pwEscape($winduid)."AND toolid=".pwEscape($toolid));
if(empty($rt['icon'])){
	$userface="||0||1";
} else{
	$userface="$user_a[0]|$user_a[1]|$user_a[2]|$user_a[3]|1";
}
$db->update("UPDATE pw_members SET icon=".pwEscape($userface)."WHERE uid=".pwEscape($uid));
$db->pw_update(
	"SELECT uid FROM pw_memberinfo WHERE uid=".pwEscape($uid),
	"UPDATE pw_memberinfo SET tooltime=".pwEscape($timestamp)."WHERE uid=".pwEscape($uid),
	"INSERT INTO pw_memberinfo SET " . pwSqlSingle(array('uid'=>$uid,'tooltime'=>$timestamp))
);
$logdata = array(
	'type'		=>	'use',
	'descrip'	=>	'tool_18_descrip',
	'uid'		=>	$winduid,
	'username'	=>	$windid,
	'ip'		=>	$onlineip,
	'time'		=>	$timestamp,
	'toolname'	=>	$tooldb['name'],
);
writetoollog($logdata);

Showmsg('toolmsg_success');
?>