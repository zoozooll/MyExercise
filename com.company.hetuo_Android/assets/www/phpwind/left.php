<?php
require_once('global.php');
include_once(D_P.'data/bbscache/forum_cache.php');

extract(L::style());

$catedb = $forumdb = array();
foreach ($forum as $key => $value) {
	if ($value['type'] == 'category' && $value['cms']!=1) {
		$catedb[$key] = $value;
	} elseif ($value['type'] == 'forum' && $value['cms']!=1 && ($value['f_type']!='hidden' || $groupid=='3')) {
		$forumdb[$value['fup']][$key] = $value;
	}
}
require_once PrintEot('left');
?>