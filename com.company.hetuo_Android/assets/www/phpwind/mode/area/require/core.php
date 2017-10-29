<?php
!defined('M_P') && exit('Forbidden');

function checkEditAdmin($name,$cid) {
	if (isGM($name)) return true;
	if (!$name) return false;
	$area_editadmin	= L::config('area_editadmin','area_config');
	if (!$area_editadmin) return false;
	$indexadmin = isset($area_editadmin['index']) ? explode(',',$area_editadmin['index']) : array();//special
	$area_editadmin = explode(',',$area_editadmin[$cid]);
	return (in_array($name,array_merge($area_editadmin,$indexadmin))) ? true : false;
}

function areaLoadFrontView($action) {
	if (!defined('AREA_SCR')) return false;
	
	if (file_exists(M_P."template/front/".AREA_SCR."/$action.htm")) {
		return M_P."template/front/".AREA_SCR."/$action.htm";
	}
	return false;
}

function updateAreaStaticRefreshTime($timeToUpdate=0) {
	global $db;

	require_once(R_P.'admin/cache.php');
	$update	= array('area_static_next','string',$timeToUpdate,'');
	$db->update("REPLACE INTO pw_hack VALUES (".pwImplode($update).')');
	updatecache_conf('area',true);
}

?>