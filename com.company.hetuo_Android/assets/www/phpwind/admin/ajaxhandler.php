<?php
!function_exists('adminmsg') && exit('Forbidden');

if($action == "guide"){
	initGP(array("guideshow"));
	$guideshow = ($guideshow == 1) ? 1 : 0;
	setConfig ( 'db_guideshow', $guideshow );
	updatecache_c ();
	exit;
}
?>