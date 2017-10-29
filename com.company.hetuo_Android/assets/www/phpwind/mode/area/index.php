<?php
!defined('M_P') && exit('Forbidden');


InitGP(array('cateid'),'GP',2);
$cateid>0 && ObHeader("cate.php?cateid=$cateid");

include_once(D_P.'data/bbscache/area_config.php');

$area_need_dynamic = area_static_deal('isNeedDynamic');
$area_refresh_static = false;
if (!$area_need_dynamic) {
	$area_index_content = area_static_deal('getStaticContent');
}

require_once(M_P.'require/header.php');

if (!$area_need_dynamic) {
	echo $area_index_content;
	footer();
	exit;
}

$first_notice = array_slice($notice_A,0,7);

function area_static_deal($op, $params=array()) {
	global $area_static_ifon, $area_static_next, $area_static_step;
	global $timestamp, $db_htmdir, $area_need_dynamic, $area_refresh_static;
	
	$area_data_file = R_P . $db_htmdir . '/area_index.htm';
	
	if ($op == "isNeedDynamic") {
		$area_need_dynamic = isset($_GET['getdynamic']) && $_GET['getdynamic'];
		if (!$area_static_ifon) $area_need_dynamic = true;
		return $area_need_dynamic;
	} elseif ($op == "getStaticContent") {
		if ($area_need_dynamic) return ;
		
		$area_refresh_static = $timestamp >= $area_static_next || !file_exists($area_data_file);
		if ($area_refresh_static) {
			$area_need_dynamic = true;
		} else {
			$area_index_content = file_get_contents($area_data_file);
			if ('' == $area_index_content) $area_need_dynamic = true;
		}
		return $area_index_content;
	} elseif ($op == "saveStaticContent") {
		if ("" == $params['content']) return ;
		writeover($area_data_file, $params['content']);
		
		updateAreaStaticRefreshTime($timestamp + $area_static_step * 60);
	}
}


?>