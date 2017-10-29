<?php
define ( 'SCR', 'mode' );
require_once ('global.php');

$m = GetGP('m');

selectMode($m);
#模式首页SEO设置
if (!$pw_seoset) {
	$pw_seoset = L::loadClass('seoset');
}
$pw_seoset->set_mode($db_mode);
$webPageTitle = $pw_seoset->getPageTitle();
$metaDescription = $pw_seoset->getPageMetadescrip();
$metaKeywords = $pw_seoset->getPageMetakeyword();

InitGP ( array ('q', 'space' ) );

/*APP 应用跳转*/
if (!$db_modes['o']['ifopen'] && $m == 'o') {
	if ($q == 'user') {
		InitGP(array('u'));
		ObHeader ( "u.php?uid=$u");
	} elseif ($q == 'app') {
		InitGP ( array ('id' ), 'G', 2 );
		ObHeader ( "apps.php?id=$id" );
	} elseif ($q == 'friend') {
		ObHeader ( "u.php?action=friend" );
	} elseif (!in_array($q ,array('user', 'friend', 'browse','invite','board','myapp','home') )) {
		$QUERY_STRING = substr($pwServer['QUERY_STRING'],4);
		ObHeader ( "apps.php?".$QUERY_STRING);
	}
	Showmsg ( 'undefined_action' );
}

if (strpos ( $q, '..' ) !== false)
	Showmsg ( 'undefined_action' );

if($m == 'o') $pwModeImg = "$imgpath/apps";

# app
if ($m == 'o' && $q && ! in_array ( $q, array ('user', 'friend', 'browse','invite','board','myapp','home','app' ) )) {
	require_once 'apps.php';
} else {

	empty ( $q ) && $q = 'home';
	if(!$q && $m == 'o'){
		require_once M_P . 'require/header.php';
	}
	if ($m && $pwServer ['HTTP_HOST'] == $db_modedomain[$m]) {
		$baseUrl = "mode.php";
		$basename = "mode.php?";
	} else {
		$baseUrl = "mode.php?m=$m";
		$basename = "mode.php?m=$m&";
	}

	if (file_exists ( M_P . "m_{$q}.php" )) {
		@include_once Pcv(D_P . 'data/bbscache/' . $db_mode . '_config.php');
		//current user
		$tname = ($q != "user" && isset($winddb['username'])) ? $winddb['username'].' - ' : '';
		isset($o_navinfo['KEY'.$q]) && $webPageTitle = strip_tags($o_navinfo['KEY'.$q]['html']).' - '.$tname.$webPageTitle;
		unset($tname);
		if ($groupid != 3 && $o_share_groups && strpos ( $o_share_groups, ",$groupid," ) === false) {
			$shareGM = 1;
		}
		if (file_exists ( M_P . 'require/core.php' )) {
			require_once (M_P . 'require/core.php');
		}
		require_once Pcv ( M_P . "m_{$q}.php" );
	} else {
		Showmsg ( 'undefined_action' );
	}
}

?>
