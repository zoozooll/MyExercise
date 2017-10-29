<?php
!defined('P_W') && exit('Forbidden');

InitGP(array('action','step'));

$area_cateinfo = array();$area_catetpl = $area_indextpl = '';
include_once(D_P.'data/bbscache/area_config.php');
if (empty($action)) {
	if (empty($step)) {
		$catedb = array();
		$query = $db->query("SELECT fid,name,vieworder,forumadmin FROM pw_forums WHERE cms!='1' AND type='category' ORDER BY vieworder");
		while ($forums = $db->fetch_array($query)) {
			$temp_tpl = getForumTpl($area_cateinfo[$forums['fid']]['tpl']);
			$forums['paths']= getCssPaths($temp_tpl);
			$forums['css']	= getForumCssPath($forums['fid']);
			$catedb[$forums['fid']] = $forums;
		}
		$indexpaths = getCssPaths($area_indextpl);
		$indexcss	= $area_indexcss ? $area_indexcss : 'default';

	} elseif ($step == 2) {
		InitGP(array('csspath'),'P');
		$area = array();
		foreach ($csspath as $key=>$value) {
			if ($key=='index') {
				if (checkCssPath($area_indextpl,$value)) {
					$area[] = array ('area_indexcss','string',$value,'');
				}
			} elseif ($key && is_numeric($key)) {
				$temp_tpl = getForumTpl($area_cateinfo[$key]['tpl']);
				if (checkCssPath($temp_tpl,$value)) {
					$area_cateinfo[$key]['css'] = $value;
				}
			}
		}
		$cateinfo = serialize($area_cateinfo);
		$area[]	= array ('area_cateinfo','array',$cateinfo,'');

		$db->update("REPLACE INTO pw_hack VALUES ".pwSqlMulti($area,false));
		updatecache_conf('area',true);

		adminmsg('operate_success');
	}
}

function getForumTpl($dir){
	global $area_catetpl;
	$dir = Pcv($dir);
	if ($dir) {
		return $dir;
	}
	if ($area_catetpl) {
		return $area_catetpl;
	}
	return 'default';
}

function getForumCssPath($fid){
	global $area_cateinfo;
	if (isset($area_cateinfo[$fid]) && isset($area_cateinfo[$fid]['css'])) {
		return $area_cateinfo[$fid]['css'];
	}
	return 'default';
}

function getCssPaths($dir){
	$dir = Pcv($dir);
	!$dir && $dir = 'default';
	$imgPath = R_P.'mode/area/themes/'.$dir.'/images';
	$temp	= array();
	if ($fp1 = opendir($imgPath)) {
		while ($imgdir = readdir($fp1)) {
			if (strpos($imgdir,'.')!==false) continue;
			$temp[] = $imgdir;
		}
		closedir($fp1);
	}
	return $temp;
}

function checkCssPath($dir,$imgdir){
	$dir	= Pcv($dir);
	!$dir && $dir = 'default';
	$imgdir	= Pcv($imgdir);
	return is_dir(R_P.'mode/area/themes/'.$dir.'/images/'.$imgdir);
}

include PrintMode('edittpl');exit;
?>