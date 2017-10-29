<?php
!function_exists('readover') && exit('Forbidden');

/****

@name:清零卡
@type:会员类
@effect:可将自已所有负分清零

****/

if($tooldb['type']!=2){
	Showmsg('tooluse_type_error');  // 判断道具类型是否设置错误
}
$rt = $db->get_one("SELECT postnum,digests,rvrc,money,credit FROM pw_memberdata WHERE uid=".pwEscape($winduid));
$sqladd = '';
if($rt['postnum'] < 0){
	$sqladd = "postnum=0";
}
if($rt['digests'] < 0){
	$sqladd .= $sqladd ? ",digests=0" : "digests=0";
}
if($rt['rvrc'] < 0){
	$sqladd .= $sqladd ? ",rvrc=0" : "rvrc=0";
}
if($rt['money'] < 0){
	$sqladd .= $sqladd ? ",money=0" : "money=0";
}
if($rt['credit'] < 0){
	$sqladd .= $sqladd ? ",credit=0" : "credit=0";
}
if($sqladd){
	$db->update("UPDATE pw_memberdata SET $sqladd WHERE uid=".pwEscape($winduid));
	$db->update("UPDATE pw_usertool SET nums=nums-1 WHERE uid=".pwEscape($winduid)."AND toolid=".pwEscape($toolid));
	$logdata=array(
		'type'		=>	'use',
		'nums'		=>	'',
		'money'		=>	'',
		'descrip'	=>	'tool_2_descrip',
		'uid'		=>	$winduid,
		'username'	=>	$windid,
		'ip'		=>	$onlineip,
		'time'		=>	$timestamp,
		'toolname'	=>	$tooldb['name'],
	);
	writetoollog($logdata);
	Showmsg('toolmsg_2_success');
} else{
	Showmsg('toolmsg_2_failed');
}
?>