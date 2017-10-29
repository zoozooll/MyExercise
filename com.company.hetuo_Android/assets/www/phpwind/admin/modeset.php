<?php
!defined('P_W') && exit('Forbidden');
$basename = "$admin_file?adminjob=modeset";

if (!$action) {
	
	if (empty($db_modes['bbs']['m_name'])) {
		$db_modes['bbs'] = array(
			'm_name'	=> getLangInfo('all','mode_bbs_mname'),
			'ifopen'	=> 1,
			'title'		=> getLangInfo('all','mode_bbs_title')
		);
		setConfig('db_modes', $db_modes);
		updatecache_c();
	}

	if ($fp = opendir(R_P.'mode')) {
		if (!$db_mode && $db_mode!='bbs') {
			$defalutchecked = '';
		} else {
			$defalutchecked = 'checked="checked"';
		}
		$uninstall = $infodb = array();
		while (($modedir = readdir($fp))) {
			if (strpos($modedir,'.')===false) {
				$modename = $modedir;
				if (array_key_exists($modename,$db_modes)) {
					continue;
				}
				$modeuse = $db_mode == $modename ? 1 : 0;
				if (function_exists('file_get_contents')) {
					$filedata = @file_get_contents(R_P."mode/$modedir/info.xml");
				} else {
					$filedata = readover(R_P."mode/$modedir/info.xml");
				}
				if (preg_match('/\<modename\>(.+?)\<\/modename\>\s+\<descrip\>(.+?)\<\/descrip\>/is',$filedata,$infodb)) {
					$infodb[1] && $modename = Char_cv(str_replace(array("\n"),'',$infodb[1]));
					$modedescrip = Char_cv(str_replace(array("\n"),'',$infodb[2]));
				}
				$uninstall[] = array($modename,$modedescrip,$modedir);
			}
		}
		closedir($fp);
	}
	$dm_array = array(
		'area' => array()
	);
	if (isset($db_modes['area'])) {
		$query = $db->query("SELECT fid,name FROM pw_forums WHERE type='category'");
		while ($rt = $db->fetch_array($query)) {
			$dm_array['area']['cate_'.$rt['fid']] = $rt['name'];
		}
	}

} elseif ($action == 'install') {

	require_once(R_P.'require/sql_deal.php');
	InitGP('mode',null,'1');
	if ($mode && !array_key_exists($mode,$db_modes)) {
		!file_exists(R_P.'mode/'.$mode) && adminmsg('mode_no_directory');
		!file_exists(R_P.'mode/'.$mode.'/info.xml') && adminmsg('mode_no_info');
		if (function_exists('file_get_contents')) {
			$filedata = @file_get_contents(R_P."mode/$mode/info.xml");
		} else {
			$filedata = readover(R_P."mode/$mode/info.xml");
		}
		$sqlarray = file_exists(R_P."mode/$mode/sql.txt") ? FileArray($mode,'mode') : array();
		!empty($sqlarray) && SQLCreate($sqlarray);

		$params = xml2array($filedata);
		if (!$db_modes || !is_array($db_modes)) {
			$db_modes = array();
		}
		$m_name = Char_cv($params['modename']);
		$db_modes[$mode] = array(
			'm_name'	=> $m_name,
			'ifopen'	=> 1,
			'title'		=> $m_name
		);
		setConfig('db_modes', $db_modes);
	
		if ($params['pages']['item']) {
			$items = $params['pages']['item'];
			$pages = array();
			foreach ($items as $value) {
				!$value['scr'] && $value['scr'] = 'public';
				$pages[$value['scr']] = array('name' => $value['name'], 'template' => $value['template']);
			}
			if ($pages) {
				$db_modepages[$mode] = $pages;
			} else {
				$db_modepages[$mode] = '';
			}
			setConfig('db_modepages', $db_modepages);
		}
		if ((int) $params['ifpwcache']) {
			$params['ifpwcache'] = (int)$params['ifpwcache'];
			$rt = $db->get_one("SELECT db_name FROM pw_config WHERE db_name='db_ifpwcache'");
			if (!empty($rt)) {
				$db->update("UPDATE pw_config SET db_value=db_value|".$params['ifpwcache'].",vtype='string' WHERE db_name='db_ifpwcache'");
			} else {
				$db->update("INSERT INTO pw_config SET db_name='db_ifpwcache',vtype='string',db_value=".pwEscape($params['ifpwcache']));
			}
		}
		$fp = opendir(D_P.'data/tplcache/');
		while ($filename = readdir($fp)) {
			if($filename=='..' || $filename=='.' || strpos($filename,'.htm')===false) continue;
			@unlink(Pcv(D_P.'data/tplcache/'.$filename));
		}
		closedir($fp);
		$navMenu = L::loadClass('navmenu');
		$navMenu->settype('main');
		$navlists = array(
			'nkey'	=> $mode,
			'type'	=> 'main',
			'pos'	=> '-1',
			'title'	=> strip_tags($m_name),
			'style'	=> '',
			'link'	=> ($db_modedomain[$mode] ? $db_modedomain[$mode] : 'index.php?m='.$mode),
			'alt'	=> '',
			'target'=> $target,
			'view'	=> 0,
			'upid'	=> 0,
			'isshow'=> 1
		);
		$navMenu->update($mode,$navlists);
		$navMenu->cache();
	}
	updatecache_c();
	adminmsg('operate_success');
} elseif ($action == 'fourmtypecache') {
	InitGP('m',null,'1');
	!array_key_exists($m,$db_modes) && adminmsg('mode_have_noopen');
	$fp = opendir(D_P.'data/bbscache/');
	while ($filename = readdir($fp)) {
		if($filename=='..' || $filename=='.') continue;
		if (strpos($filename,'mode_'.$m) !==false) {
			P_unlink(Pcv(D_P.'data/bbscache/'.$filename));
		}
	}
	closedir($fp);
	$fp = opendir(D_P.'data/tplcache/');
	while ($filename = readdir($fp)) {
		if($filename=='..' || $filename=='.' || strpos($filename,'.htm')===false) continue;
		if (strpos($filename,$m.'_') === 0) {
			P_unlink(Pcv(D_P.'data/tplcache/'.$filename));
		}
	}
	closedir($fp);
	$fp = opendir($attachdir.'/mini/');
	while ($filename = readdir($fp)) {
		if($filename=='..' || $filename=='.') continue;
		P_unlink(Pcv($attachdir.'/mini/'.$filename));
	}
	closedir($fp);
	$pw_cachedata = L::loadDB('cachedata');
	$pw_cachedata->truncate();
	adminmsg('operate_success');

} elseif ($action == 'uninstall') {

	InitGP('m',null,'1');
	!array_key_exists($m,$db_modes) && adminmsg('mode_have_noopen');
	require_once(R_P.'require/sql_deal.php');
	$sqlarray = file_exists(R_P."mode/$m/sql.txt") ? FileArray($m,'mode') : array();
	!empty($sqlarray) && SQLDrop($sqlarray);

	$fp = opendir(D_P.'data/tplcache/');
	while ($filename = readdir($fp)) {
		if ($filename == '..' || $filename == '.' || strpos($filename,'.htm') === false) continue;
		if (strpos($filename,$m.'_') === 0) {
			P_unlink(Pcv(D_P.'data/tplcache/'.$filename));
		}
	}
	$pw_cachedata = L::loadDB('cachedata');
	$pw_cachedata->truncate();
	closedir($fp);
	unset($db_modes[$m]);
	setConfig('db_modes', $db_modes);
	unset($db_modepages[$m]);
	setConfig('db_modepages', $db_modepages);

	if ($m == $db_mode) {
		setConfig('db_mode', 'bbs');
	}
	$navMenu = L::loadClass('navmenu');
	$navMenu->settype('main');
	$navMenu->del($m);
	$navMenu->cache();
	adminmsg('operate_success');

} elseif ($action == 'set') {

	InitGP(array('defaultmode','ifopen','domain','title'),'P',1);

	$temp = array();
	foreach ($domain as $key => $value) {
		if ($value/* && array_key_exists($key,$db_modes)*/) {
			if (preg_match('/^[a-z0-9\.\-]+?$/i',$value)) {
				$temp[$key] = strtolower($value);
			} else {
				adminmsg('mode_domain_error');
			}
		}
	}

	if ($db_modedomain != $temp) {
		$db_modedomain = $temp;
		setConfig('db_modedomain', $db_modedomain);
	}
	if (!$defaultmode || array_key_exists($defaultmode,$db_modes)) {
		setConfig('db_mode', $defaultmode);
		if ($defaultmode && !in_array($defaultmode,$ifopen)) {
			$ifopen[] = $defaultmode;
		}
	}

	foreach ($db_modes as $key=>$value) {
		if (in_array($key,$ifopen)) {
			$db_modes[$key]['ifopen'] = 1;
		} else {
			$db_modes[$key]['ifopen'] = 0;
		}
		$title[$key] = stripcslashes($title[$key]);
		$db_modes[$key]['title'] = $title[$key] ? $title[$key] : $db_modes[$key]['m_name'];
	}
	$navMenu = L::loadClass('navmenu');
	$navMenu->settype('main');
	$tmpSel = empty($defaultmode) ? 'bbs' : $defaultmode;
	foreach ($db_modes as $key=>$value) {
		$link = $db_modedomain[$key] ? 'http://'.$db_modedomain[$key] :($tmpSel == $key ? "./" : 'index.php?m='.$key);
		$target = $key == 'o' ? 1 : 0;
		$navlists = array(
			'nkey'	=> $key,
			'link'	=> $link,
			'target'=> $target,
			'isshow'=> $value['ifopen']
		);
		$navMenu->update($key,$navlists);
	}
	setConfig('db_modes', $db_modes);
	$navMenu->cache();
	//updatecache_c();
	adminmsg('operate_success');
}
include PrintEot('modeset');exit;

function xml2array($content){
	$temp_array = array();
	if (preg_match('/<modename>(.*?)<\/modename>/i',$content,$match)) {
		$temp_array['modename'] = $match['1'];
	}
	if (preg_match('/<ifpwcache>(\d+)<\/ifpwcache>/i',$content,$match)) {
		$temp_array['ifpwcache'] = $match['1'];
	}
	$match_1 = $match_2 = array();
	if (preg_match('/<pages>([^\x00]*?)<\/pages>/i',$content,$match_1)) {
		if(preg_match_all('/<item>[^\x00]*?<name>(.*?)<\/name>[^\x00]*?<template>(.*?)<\/template>[^\x00]*?<scr>(.*?)<\/scr>[^\x00]*?<\/item>/i',$match_1[1],$match_2)) {
			foreach ($match_2[1] as $key => $value) {
				$temp_array['pages']['item'][$key]['name']		= $value;
				$temp_array['pages']['item'][$key]['template']	= $match_2[2][$key];
				$temp_array['pages']['item'][$key]['scr']		= $match_2[3][$key];
			}
		}
	}
	return $temp_array;
}

function pwArrayConvert($array,$to_encoding,$from_encoding,$ifmb=true){
	if (is_array($array)) {
		foreach ($array as $key=>$value) {
			$array[$key] = pwArrayConvert($value,$to_encoding,$from_encoding,$ifmb);
		}
	} else {
		$array = pwConvert($array,$to_encoding,$from_encoding,$ifmb);
	}
	return $array;
}

?>