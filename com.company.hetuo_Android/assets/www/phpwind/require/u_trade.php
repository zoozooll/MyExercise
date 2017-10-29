<?php
!function_exists('readover') && exit('Forbidden');

InitGP(array('type'));
(!is_numeric($page) || $page < 1) && $page = 1;
$db_perpage = 20;

!$isU && $type = 'onsale';

if (empty($type)) {
	$total = $db->get_value("SELECT COUNT(*) FROM pw_tradeorder WHERE buyer=".pwEscape($uid));
	$numofpage = ceil($total/$db_perpage);
	$pages = numofpage($total,$page,$numofpage,"u.php?action=trade&uid=$uid&");
	$limit = pwLimit(($page-1)*$db_perpage,$db_perpage);
	$trade = array();
	$query = $db->query("SELECT td.*,t.icon,m.username FROM pw_tradeorder td LEFT JOIN pw_trade t ON td.tid=t.tid LEFT JOIN pw_members m ON td.seller=m.uid WHERE td.buyer=".pwEscape($uid).' ORDER BY td.oid DESC '.$limit);
	while ($rt = $db->fetch_array($query)) {
		$rt['icon'] = goodsicon($rt['icon']);
		$trade[] = $rt;
	}
	require_once PrintEot('u');
	footer();

} elseif ($type == 'onsale') {
	$total = $db->get_value("SELECT COUNT(*) FROM pw_trade WHERE uid=".pwEscape($uid));
	$numofpage = ceil($total/$db_perpage);
	$pages = numofpage($total,$page,$numofpage,"u.php?action=trade&uid=$uid&");
	$limit = pwLimit(($page-1)*$db_perpage,$db_perpage);

	$trade = array();
	$query = $db->query("SELECT * FROM pw_trade WHERE uid=".pwEscape($uid).' ORDER BY tid DESC '.$limit);
	while ($rt = $db->fetch_array($query)) {
		$rt['icon'] = goodsicon($rt['icon']);
		$trade[] = $rt;
	}

	require_once PrintEot('u');
	footer();

} elseif ($type == 'saled') {
	$total = $db->get_value("SELECT COUNT(*) FROM pw_tradeorder WHERE seller=".pwEscape($uid));
	$numofpage = ceil($total/$db_perpage);
	$pages = numofpage($total,$page,$numofpage,"u.php?action=trade&uid=$uid&");
	$limit = pwLimit(($page-1)*$db_perpage,$db_perpage);
	$trade = array();
	$query = $db->query("SELECT td.*,t.icon,m.username FROM pw_tradeorder td LEFT JOIN pw_trade t ON td.tid=t.tid LEFT JOIN pw_members m ON td.buyer=m.uid WHERE td.seller=".pwEscape($uid).' ORDER BY td.oid DESC '.$limit);
	while ($rt = $db->fetch_array($query)) {
		$rt['icon'] = goodsicon($rt['icon']);
		$trade[] = $rt;
	}
	require_once PrintEot('u');
	footer();
}

function goodsicon($icon) {
	global $attachpath,$imgpath;
	if (empty($icon)) {
		return $imgpath.'/goods_none.gif';
	}
	if (file_exists($attachpath.'/thumb/'.$icon)) {
		return $attachpath.'/thumb/'.$icon;
	}
	if (file_exists($attachpath.'/'.$icon)) {
		return $attachpath.'/'.$icon;
	}
	return $imgpath.'/goods_none.gif';
}
?>