<?php
!defined('P_W') && exit('Forbidden');

InitGP(array('action','step'));

$area_cateinfo = array();$area_catetpl = $area_indextpl = '';
include_once(D_P.'data/bbscache/area_config.php');
if (empty($action)) {
	if (empty($step)) {
		$cid = GetGP('cid');
		if (is_numeric($cid) && $area_cateinfo[$cid]['tpl']) {
			$area_catetpl = $area_cateinfo[$cid]['tpl'];
		}
		$tplLib = array();
		$tplPath = R_P.'mode/area/themes/';
		if ($fp1 = opendir($tplPath)) {
			while ($tpldir = readdir($fp1)) {
				if (in_array($tpldir,array('.','..','admin'))) continue;
				if ($fp2 = opendir($tplPath.$tpldir)) {
					while ($file = readdir($fp2)) {
						if ($file == "." && $file == "..") continue;
						switch ($file) {
							case 'index.htm' :
								if ($area_indextpl != $tpldir) $tplLib['index'][] = $tpldir;
								break;
							case 'cate.htm' :
								if ($area_catetpl != $tpldir) $tplLib['cate'][] = $tpldir;
								break;
						}
					}
				}
			}
			closedir($fp);
		}
	} elseif ($step == 2) {
		InitGP(array('index','cate','cid'));
		$pw_mpageconfig	= L::loadDB('mpageconfig');
		$area = array();
		@include(D_P.'data/bbscache/area_haveused_tpl.php');
		$haveused = $haveused ? $haveused : array();

		if ((!$cid || $cid=='index') && $index) {
			$area[] = array ('area_indextpl','string',$index,'');
			$area[] = array ('area_indexcss','string','default','');
			$pw_mpageconfig->deleteDataByParam('area','index');
			$fp = opendir(D_P.'data/tplcache/');
			while ($filename = readdir($fp)) {
				if($filename=='..' || $filename=='.' || strpos($filename,'.htm')===false) continue;
				if (strpos($filename,'area_') === 0) {
					P_unlink(Pcv(D_P.'data/tplcache/'.$filename));
				}
			}
			closedir($fp);
		}
		if ($cate) {
			if (is_numeric($cid)) {
				if ($cate != $area_catetpl) {
					$area_cateinfo[$cid]['tpl'] = $cate;
					$area_cateinfo[$cid]['css'] = 'default';
				} else {
					unset($area_cateinfo[$cid]);
				}
				$cateinfo = serialize($area_cateinfo);
				$area[] = array ('area_cateinfo','array',$cateinfo,'');
				$pw_mpageconfig->deleteDataByParam('area','cate',$cid);
			} else {
				$area[] = array ('area_catetpl','string',$cate,'');
				$pw_mpageconfig->deleteDataByParam('area','cate');
			}
		}
		if ($area) {
			$db->update("REPLACE INTO pw_hack VALUES ".pwSqlMulti($area,false));
			updateAreaStaticRefreshTime();
		}
		if ($index && !in_array($index,$haveused)) {
			$invokeservice = L::loadClass('InvokeService');
			$invokeservice->inportDefaultConfig($index);
			$haveused[] = $index;
		}
		if ($cate && $index != $cate && !in_array($cate,$haveused)) {
			$invokeservice = L::loadClass('InvokeService');
			$invokeservice->inportDefaultConfig($cate);
			$haveused[] = $cate;
		}

		writeover(D_P.'data/bbscache/area_haveused_tpl.php',"<?php\r\n\$haveused=".pw_var_export($haveused).";\r\n?>");

		if ($cid) {
			adminmsg('operate_success',"$admin_file?adminjob=mode&admintype=area_edittpl");
		} else {
			adminmsg('operate_success');
		}
	}
} elseif ($action == 'config') {
	$themeconfig = L::loadClass('ThemeConfig');
	$themes = $themeconfig->getThemes();

	$ajax_basename = EncodeUrl($basename);
} elseif ($action == 'import') {
	define('AJAX',1);
	InitGP(array('theme'));
	$invokeservice = L::loadClass('invokeservice');
	$invokeservice->inportDefaultConfig($theme);
	echo getLangInfo('msg','operate_success')."\treload";
	ajax_footer();
} elseif ($action == 'export') {
	InitGP(array('theme'));
	$invokeservice = L::loadClass('invokeservice');
	$invokeservice->exportThemeConfig($theme);
}

include PrintMode('selecttpl');exit;
?>