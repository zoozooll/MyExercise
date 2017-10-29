<?php
!function_exists('adminmsg') && exit('Forbidden');
$basename="$admin_file?adminjob=maketemplate";

if (!$action) {
	InitGP(array('page','ftype'));
	$pw_tpl	= L::loadDB('tpl');
	$count	= $pw_tpl->countByType($ftype);

	$tpltype	= L::loadClass('tpltype');
	$ftypes	= $tpltype->getDatas();
	if ($ftype && !array_key_exists($ftype,$ftypes)) {
		$ftype	= '';
	}
	(!is_numeric($page) || $page < 1) && $page = 1;
	$limit	= pwLimit(($page-1)*$db_perpage,$db_perpage);
	$pages	= numofpage($count,$page,ceil($count/$db_perpage),"$basename&ftype=$ftype&");

	$tpls	= $pw_tpl->getDatas($ftype,$limit);
	$ajax_basename = EncodeUrl($basename);
} elseif ($action == 'creatinvoke') {
	define('AJAX',1);
	InitGP(array('invokename'),'GP',1);
	if (!$invokename) {
		adminmsg('invokename_empty');
	} elseif (strlen($invokename)>50) {
		adminmsg('invokename_toolang');
	} elseif (!preg_match("/^[".chr(0xa1)."-".chr(0xff)."\w]*?$/",$invokename)) {
		adminmsg('invokename_error');
	}
	//require_once(R_P.'lib/parsetpl.class.php');
	$pw_invoke = L::loadDB('invoke');
	if ($pw_invoke->getDataByName($invokename)) {
		adminmsg('invokename_repeat');
	}
	adminmsg('success');
}

include PrintEot('maketemplate');exit;
?>