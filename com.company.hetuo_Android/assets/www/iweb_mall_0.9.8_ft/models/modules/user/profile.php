<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require("foundation/module_users.php");
require("foundation/module_areas.php");

//引入语言包
$m_langpackage=new moduleslp;

//数据表定义区
$t_users = $tablePreStr."users";
$t_user_info = $tablePreStr."user_info";
$t_areas = $tablePreStr."areas";

$dbo=new dbex;
//读写分离定义方法
dbtarget('r',$dbServs);

$user_info = get_user_info($dbo,$t_user_info,$user_id);
// 用户生日
if($user_info['user_birthday']) {
	$Y = substr($user_info['user_birthday'],0,4);
	$M = substr($user_info['user_birthday'],5,2);
	$D = substr($user_info['user_birthday'],8,2);
} else {
	$Y = $M = $D = 0;
}
// 用户所选国家， 如果没选默认为1（中国）
$user_info['user_country'] = $user_info['user_country'] ? $user_info['user_country'] : 1;

$areas_info = get_areas_info($dbo,$t_areas);
//print_r($areas_info[2]);

if($user_info['user_gender']==0) { $user_gender0='checked'; } else { $user_gender0=''; }
if($user_info['user_gender']==1) { $user_gender1='checked'; } else { $user_gender1=''; }
if($user_info['user_gender']==2) { $user_gender2='checked'; } else { $user_gender2=''; }

if($user_info['user_marry']==0) { $user_marry0='checked'; } else { $user_marry0=''; }
if($user_info['user_marry']==1) { $user_marry1='checked'; } else { $user_marry1=''; }
if($user_info['user_marry']==2) { $user_marry2='checked'; } else { $user_marry2=''; }
?>