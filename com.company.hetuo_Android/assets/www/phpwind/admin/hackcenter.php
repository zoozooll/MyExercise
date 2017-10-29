<?php
!function_exists('adminmsg') && exit('Forbidden');
require_once(R_P.'require/sql_deal.php');
$basename="$admin_file?adminjob=hackcenter";

if (!$action) {

	$installdb = $uninstalldb = array();
	foreach ($db_hackdb as $key => $value) {
		$value[0] = htmlspecialchars($value[0]);
		${$value[1].'_'.$value[2]} = 'SELECTED';
		$value[4] = EncodeUrl("$basename&action=delete&id=$value[1]");
		if (file_exists(R_P."/hack/$key/index.php")) {
			$installdb['index'][$key] = $value;
		} else {
			$installdb['noindex'][$key] = $value;
		}
	}
	if ($fp = opendir(R_P.'hack')) {
		$infodb = array();
		while (($hackdir = readdir($fp))) {
			if (strpos($hackdir,'.')===false && empty($db_hackdb[$hackdir])) {
				$hackname = $hackdir;
				$hackopen = 0;
				if (function_exists('file_get_contents')) {
					$filedata = @file_get_contents(R_P."hack/$hackdir/info.xml");
				} else {
					$filedata = readover(R_P."hack/$hackdir/info.xml");
				}
				if (preg_match('/\<hackname\>(.+?)\<\/hackname\>\s+\<ifopen\>(.+?)\<\/ifopen\>/is',$filedata,$infodb)) {
					$infodb[1] && $hackname = Char_cv(str_replace(array("\n"),'',$infodb[1]));
					$hackopen = (int)$infodb[2];
				}
				$hackurl = EncodeUrl("$basename&action=add&hackdir=$hackdir&hackname=".rawurlencode($hackname)."&hackopen=$hackopen");
				$uninstalldb[] = array($hackname,$hackdir,$hackopen,$hackurl);
			}
		}
		closedir($fp);
	}
	unset($db_hackdb);
	include PrintEot('hackcenter');exit;

} elseif ($action == 'edit') {

	InitGP(array('hackname'),'GP',0);
	//InitGP(array('hackopen'),'GP',2);
	//$navMenu = L::loadClass('navmenu');
	//$navMenu->settype('bbs_navinfo');

	!is_array($hackname) && $hackname = array();
	foreach ($hackname as $key => $value) {
		$value = str_replace(array("\t","\n","\r",'  '),array('&nbsp; &nbsp; ','<br />','','&nbsp; '),$value);
		if ($value && $db_hackdb[$key][1] == $key && ($db_hackdb[$key][0] != $value || $db_hackdb[$key][2] != $hackopen[$key])) {
			/*
			if ($hackopen[$key] == 0) {
				$navMenu->setshow('hack_'.$key,0);
				$navMenu->setupnav('hack_'.$key,'hack');
			} elseif ($hackopen[$key] == 1) {
				$navMenu->setshow('hack_'.$key,1);
				$navMenu->setupnav('hack_'.$key,'hack');
			} elseif ($hackopen[$key] == 2) {
				$navMenu->setshow('hack_'.$key,1);
				$navMenu->setupnav('hack_'.$key);
			}
			*/
			$db_hackdb[$key] = array(stripslashes($value),$key/*,$hackopen[$key]*/);
		}
	}
	setConfig('db_hackdb', $db_hackdb);
	updatecache_c();
	//$navMenu->cache();
	adminmsg('operate_success');

} elseif ($action == 'delete') {

	InitGP(array('id'));
	empty($db_hackdb[$id]) && adminmsg('hackcenter_del');
	unset($db_hackdb[$id]);
	$sqlarray = file_exists(R_P."hack/$id/sql.txt") ? FileArray($id) : array();
	!empty($sqlarray) && SQLDrop($sqlarray);
	setConfig('db_hackdb', $db_hackdb);

	$navMenu = L::loadClass('navmenu');
	$navMenu->settype('bbs_navinfo');
	$navMenu->del('hack_'.$id);
	$navMenu->cache();

	adminmsg('operate_success');

} elseif ($action == 'add') {

	InitGP(array('hackdir','hackname','hackopen'),'G');
	!empty($db_hackdb[$hackdir]) && adminmsg('hackcenter_sign_exists');
	$sqlarray = file_exists(R_P."hack/$hackdir/sql.txt") ? FileArray($hackdir) : array();
	!empty($sqlarray) && SQLCreate($sqlarray);
	$db_hackdb[$hackdir] = array($hackname, $hackdir, $hackopen);
	setConfig('db_hackdb', $db_hackdb);

	$navMenu = L::loadClass('navmenu');
	$navMenu->settype('bbs_navinfo');
	$navMenu->update('hack_'.$hackdir,array('title'=>strip_tags($hackname),'link'=>'hack.php?H_name='.$hackdir));
	if ($hackopen == 0) {
		$navMenu->setshow('hack_'.$hackdir,0);
		$navMenu->setupnav('hack_'.$hackdir,'hack');
	} elseif ($hackopen == 1) {
		$navMenu->setshow('hack_'.$hackdir,1);
		$navMenu->setupnav('hack_'.$hackdir,'hack');
	} elseif ($hackopen == 2) {
		$navMenu->setshow('hack_'.$hackdir,1);
		$navMenu->setupnav('hack_'.$hackdir);
	}
	$navMenu->cache();

	adminmsg('operate_success');
}
?>