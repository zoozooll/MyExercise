<?php
!function_exists('readover') && exit('Forbidden');

/****

@name:透视镜
@type:会员类
@effect:查看用户IP.

****/
InitGP(array('uid'),'GP',2);
if($tooldb['type']!=2){
	Showmsg('tooluse_type_error');  // 判断道具类型是否设置错误
}
if(!$uid){
	Showmsg('tooluse_noiper');
}
$ipdb='';
$rt  = $db->get_one("SELECT onlineip FROM pw_memberdata WHERE uid=".pwEscape($uid));
$ipdb=explode('|',$rt['onlineip']);
$db->update("UPDATE pw_usertool SET nums=nums-1 WHERE uid=".pwEscape($winduid)."AND toolid=".pwEscape($toolid));
$logdata=array(
	'type'		=>	'use',
	'descrip'	=>	'tool_20_descrip',
	'uid'		=>	$winduid,
	'username'	=>	$windid,
	'ip'		=>	$onlineip,
	'time'		=>	$timestamp,
	'toolname'	=>	$tooldb['name'],
);
writetoollog($logdata);
Showmsg($ipdb[0]);
?>