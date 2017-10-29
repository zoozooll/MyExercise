<?php
!function_exists('readover') && exit('Forbidden');

/****

@name:加亮道具
@type:帖子类
@effect:可以将自己的帖子标题加亮显示

****/

if($tooldb['type']!=1){
	Showmsg('tooluse_type_error');  // 判断道具类型是否设置错误
}

if(!$_POST['step']){
	require_once PrintHack('index');footer();
} else{
	if($tpcdb['authorid'] != $winduid){
		Showmsg('tool_authorlimit');
	}
	InitGP(array('title1','title2','title3','title4','title5','title6'));
	$titlefont = "$title1~$title2~$title3~$title4~$title5~$title6~";
	$db->update("UPDATE pw_threads SET titlefont=".pwEscape($titlefont).",toolinfo=".pwEscape($tooldb['name'],false)."WHERE tid=".pwEscape($tid));
	$db->update("UPDATE pw_usertool SET nums=nums-1 WHERE uid=".pwEscape($winduid)."AND toolid=".pwEscape($toolid));
	$logdata=array(
		'type'		=>	'use',
		'nums'		=>	'',
		'money'		=>	'',
		'descrip'	=>	'tool_3_descrip',
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
}
?>