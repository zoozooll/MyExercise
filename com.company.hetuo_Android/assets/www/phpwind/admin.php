<?php
/**
*
*  Copyright (c) 2003-09  PHPWind.net. All rights reserved.
*  Support : http://www.phpwind.net
*  This software is the proprietary information of PHPWind.com.
*
*/
error_reporting(E_ERROR | E_PARSE);
set_magic_quotes_runtime(0);

define('R_P',strpos(__FILE__,DIRECTORY_SEPARATOR)!==FALSE ? substr(__FILE__,0,strrpos(__FILE__,DIRECTORY_SEPARATOR)).'/' : './');
define('D_P',R_P);
require_once(R_P.'admin/admincp.php');
$basename = $admin_file.'?adminjob='.$adminjob;
if (!$adminjob) {
	require_once(R_P.'admin/index.php');
} elseif($adminjob == 'notice'){//计划任务进程
	require_once(R_P.'admin/notice.php');
} elseif ($adminjob == 'admin') {//提示信息、桌面快捷显示
	require_once(R_P.'admin/admininfo.php');
} elseif ($adminjob == 'adminrecord' && $db_adminrecord == '1') {//操作记录
	require_once(R_P.'admin/adminrecord.php');
} elseif ($adminjob == 'search') {//后台搜索
	require_once(R_P.'admin/search.php');
} elseif ($adminjob == 'hack' && $rightset['hackcenter'] == 1) {//插件管理
	if (!$db_hackdb[$hackset] || !is_dir(R_P.'hack/'.$hackset.'/') || !file_exists(R_P.'hack/'.$hackset.'/admin.php')) {
		adminmsg('hack_error',$admin_file.'?adminjob=hackcenter');
	}
	define('H_P',R_P.'hack/'.$hackset.'/');
	$basename = $admin_file.'?adminjob=hack&hackset='.$hackset;
	require_once Pcv(H_P.'admin.php');
} elseif ($adminjob == 'mode' && $admintype && $rightset[$admintype] == 1) {//模式管理
	list($m,$adminjob) = explode('_',$admintype);
	if (!isset($db_modes[$m]) || !is_dir(R_P."mode/$m") || !file_exists(R_P."mode/$m/admin/$adminjob.php")) {
		adminmsg('mode_admin_error');
	}
	define('M_P',R_P."mode/$m/");
	$pwModeImg = "mode/$m/images";
	$basename = "$admin_file?adminjob=mode&admintype=$admintype";
	if (file_exists(M_P.'require/core.php')) {
		include_once(M_P.'require/core.php');
	}
	require_once Pcv(M_P.'admin/'.$adminjob.'.php');
} elseif ($adminjob == 'apps' && $admintype && $rightset[$admintype] == 1){//基础性app管理
	list(,$adminname) = explode('_',$admintype);
	if (!is_dir(R_P."apps/$adminname") || !file_exists(R_P."apps/$adminname/admin.php")) {
		adminmsg('app_admin_error');
	}
	define('A_P',R_P."apps/$adminname/");
	$appdir = $adminname;
	$pwAppImg = "mode/$adminname/images";
	$basename = "$admin_file?adminjob=apps&admintype=$admintype";
	require_once Pcv(A_P.'admin.php');
} elseif ($adminjob == 'content' && (($rightset['tpccheck'] && ($type=='tpc' || $type=='post')) || ((int)$rightset['message'] == 1 && $type == 'message'))) {
	require_once(R_P.'admin/content.php');
} elseif (managerRight($adminjob) || adminRight($adminjob,$admintype)) {
	require_once Pcv(R_P.'admin/'.$adminjob.'.php');
} else {
	adminmsg('undefine_action');
}

function managerRight($adminjob) {
	return If_manager && in_array($adminjob,array('rightset','manager','ystats','diyoption','optimize','modepage','sphinx','app','ajaxhandler'));
}
function adminRight($adminjob,$admintype){
	$temp = $admintype ? $admintype : $adminjob;
	return adminRightCheck($temp);
}
function getdirname($path=null){
	if (!empty($path)) {
		if (strpos($path,'\\')!==false) {
			return substr($path,0,strrpos($path,'\\')).'/';
		} elseif (strpos($path,'/')!==false) {
			return substr($path,0,strrpos($path,'/')).'/';
		}
	}
	return './';
}
?>