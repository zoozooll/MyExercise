<?php
!defined('P_W') && exit('Forbidden');
include_once(D_P.'data/bbscache/area_config.php');

if ($action == 'doset') {
	InitGP(array('area_static'));
	$area_static['ifon'] = intval($area_static['ifon']);
	$area_static['step'] = intval($area_static['step']);
	if ($area_static['ifon'] && !$area_static['step']) adminmsg('请填写正确的静态页面刷新时间');

	$update	= array('area_static_ifon','string',$area_static['ifon'],'');
	$db->update("REPLACE INTO pw_hack VALUES (".pwImplode($update).')');
	$update	= array('area_static_step','string',$area_static['step'],'');
	$db->update("REPLACE INTO pw_hack VALUES (".pwImplode($update).')');
	updatecache_conf('area',true);

	$fp = opendir(D_P.'data/tplcache/');
	while ($filename = readdir($fp)) {
		if($filename=='..' || $filename=='.' || strpos($filename,'.htm')===false) continue;
		if (strpos($filename,'area_') === 0) {
			P_unlink(Pcv(D_P.'data/tplcache/'.$filename));
		}
	}
	closedir($fp);
	adminmsg('operate_success');
} elseif ($action == 'dorefresh') {
	if (!$area_static_ifon) adminmsg('还没有开启门户首页静态化，无需刷新');
	updateAreaStaticRefreshTime();
	adminmsg('operate_success');
} else {
	$if_on_checked = $area_static_ifon ? "checked" : "";
	$if_noton_checked = $area_static_ifon ? "" : "checked";
}

include PrintMode('configarea');
exit;

?>