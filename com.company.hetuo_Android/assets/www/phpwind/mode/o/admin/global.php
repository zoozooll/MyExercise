<?php
!function_exists('adminmsg') && exit('Forbidden');

@include_once(D_P.'data/bbscache/o_config.php');

if (empty($_POST['step'])) {
	ifcheck($o_invite,'invite');
	ifcheck($o_browseopen,'browseopen');
	for ($i=0;$i<10;$i++) {
		${'browse_'.$i} = ($o_browse & pow(2,$i)) ? 'CHECKED' : '';
		${'indexset_'.$i} = ($o_indexset & pow(2,$i)) ? 'CHECKED' : '';
	}

	require_once PrintMode('global');
} else {
	InitGP(array('config','browse','indexset'),'GP',2);

	$config['browse'] = !empty($browse) ? intval(array_sum($browse)) : 0;
	$config['indexset'] = !empty($indexset) ? intval(array_sum($indexset)) : 0;
	$updatecache = false;
	foreach ($config as $key => $value) {
		if (${'o_'.$key} != $value) {
			$db->pw_update(
				'SELECT hk_name FROM pw_hack WHERE hk_name=' . pwEscape("o_$key"),
				'UPDATE pw_hack SET ' . pwSqlSingle(array('hk_value' => $value, 'vtype' => 'string')) . ' WHERE hk_name=' . pwEscape("o_$key"),
				'INSERT INTO pw_hack SET ' . pwSqlSingle(array('hk_name' => "o_$key", 'vtype' => 'string', 'hk_value' => $value))
			);
			$updatecache = true;
		}
	}
	$updatecache && updatecache_conf('o',true);
	adminmsg('operate_success');
}
?>