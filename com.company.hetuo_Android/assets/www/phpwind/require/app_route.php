<?php
!function_exists('readover') && exit('Forbidden');
InitGP ( array ( 'q' , 'u') );
$tplapps = array ('m_photos', 'm_write', 'm_article', 'm_diary', 'm_galbum', 'm_galbum', 'm_group', 'm_groups', 'm_share', 'm_hot', 'm_comment', 'm_ajax' );
if ($m == 'o') {
	define ( 'F_M', TRUE );
	# 圈子
	if ($m && $pwServer ['HTTP_HOST'] == $db_modedomain[$m]) {
		$temp_baseUrl = "mode.php?";
		$temp_basename = $temp_baseUrl."q=".$q."&";
	} else {
		$temp_baseUrl = 'mode.php?m=' . $m.'&';
		$temp_basename = $temp_baseUrl . 'q='.$q.'&';
	}
	$db_menuinit .= ",'td_userinfomore' : 'menu_userinfomore'";
} else {
	$temp_baseUrl = 'apps.php?';
	$temp_basename = $temp_baseUrl . 'q='.$q.'&';
}

if ($space == 1 && defined('F_M') && !in_array($q,array('group','galbum'))) {
//	if($u==$winduid){
//		$baseUrl = $temp_baseUrl."u=".$u."&";
//		$basename = $temp_basename."u=".$u."&";
//	}else{
		$baseUrl = $temp_baseUrl."space=1&u=".$u."&";
		$basename = $temp_basename."space=1&u=".$u."&";
//	}
} else {

	/*** userapp **/
	if ($db_siteappkey && ($db_appbbs || $db_appo)) {
		$app_array = array();
		$appclient = L::loadClass('appclient');
		$app_array = $appclient->userApplist($winduid);
	}
	/*** userapp **/

	if (!empty($u) && $u != $winduid) {
		$basename = $temp_basename. "u=".$u."&";
	} else {
		$basename = $temp_basename;
	}
	$baseUrl = $temp_baseUrl;
}
if (file_exists ( R_P . 'require/app_core.php' )) {
	require_once (R_P . 'require/app_core.php');
}

$pwModeImg = "$imgpath/apps";
list ( $app, $route ) = app_specialRoute ( $q );
$appdir = $app;
list ( $basePath, $baseFile ) = app_router ( $app );

@include_once (D_P . 'data/bbscache/o_config.php');
if ($groupid != 3 && $o_share_groups && strpos ( $o_share_groups, ",$groupid," ) === false) {
	$shareGM = 1;
}

extract(L::style(null, $skinco));


if($space == 1 && !in_array($q,array('group','galbum'))){
	$pwModeCss = $imgpath.'/apps/browse-style.css';
}else{
	$pwModeCss = $imgpath.'/apps/app-style.css';
}

list ( $_Navbar, $_LoginInfo ) = pwNavBar ();

require_once Pcv ( $baseFile );

if ($m == "o") {
	$isheader && require_once PrintEot ( 'header' );
	$isleft && include_once PrintEot ( 'm_appleft' );
	$tplname && include_once PrintEot ( $tplname );
	$isfooter && footer ();
} else {
	$cssForum = TRUE;
	unset ( $pwModeCss );
	$isheader && require_once R_P . 'require/header.php';
	$tplname && include_once PrintEot ( $tplname );
	$isfooter && footer ();
}

unset ( $_Navbar, $pwModeCss );

function app_router($app) {
	(empty ( $app ) || (! is_dir ( $basePath = A_P . $app ))) && Showmsg ( 'undefined_action' );
	$baseFile = $basePath . '/index.php';
	if (! file_exists ( $baseFile )) {
		Showmsg ( "包含文件不存在，请创建index.php" );
	}
	return array ($basePath, $baseFile );
}

function app_specialRoute($route) {
	if (in_array ( $route, array ("groups", "group", "galbum" ) )) {
		return array ('groups', $route );
	}
	if (in_array ( $route, array ("share", "sharelink" ) )) {
		return array ('share', $route );
	}
	return array ($route, $route );
}
?>
