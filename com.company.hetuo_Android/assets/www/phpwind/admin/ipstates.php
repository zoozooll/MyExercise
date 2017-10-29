<?php
! function_exists ( 'adminmsg' ) && exit ( 'Forbidden' );
$basename = "$admin_file?adminjob=ipban&job=ipstates";

if ($action != 'submit' && $action != 'ipIndex') {

	ifcheck ( $db_ipstates, 'ipstates' );
	include PrintEot ( 'ipstates' );

} elseif ($_POST ['action'] == "submit") {

	InitGP ( array ('ipstates' ), 'P' );
	setConfig ( 'db_ipstates', $ipstates );

	$navMenu = L::loadClass ( 'navmenu' );
	$navMenu->settype ( 'bbs_navinfo' );
	$navMenu->setshow ( 'sort_ipstate', $ipstates );
	$navMenu->cache ();
	adminmsg ( 'operate_success' );
} elseif ($action == "ipIndex") {
	$ipTable = L::loadClass('IPTable');
	$ipTable->createIpIndex ();
	adminmsg ( 'operate_success' );
}
?>