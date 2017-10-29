<?php
!defined('P_W') && exit('Forbidden');
//@include_once(Pcv(D_P.'data/bbscache/mode_index_forum_'.$m.'_config.php'));
@include_once(D_P.'data/bbscache/forum_cache.php');
$forumoptions = '<option value="0">--</option>';
foreach ($forum as $key => $value) {
	if ($value['type'] == 'forum') {
		$forumoptions .= '<option value="'.$value['fid'].'">'.$value['name'].'</option>';
	}
}
$forumoptions .= '</select>';
if (empty($_POST['step'])) {
	$index_forum_config = unserialize($db->get_value("SELECT cache FROM pw_cache WHERE name='areaindexforumconfig'"));
	empty($index_forum_config) && $index_forum_config = array();
	foreach ($index_forum_config as $key => $value) {
		for ($i = 0; $i < 3;$i++) {
			$fidoption = '';
			$fid = 'fid'.($i+1);
			$fidoption = '<select name="'.$fid.'['.$key.']"><option value="0">--</option>';
			foreach ($forum as $k => $v) {
				if ($v['type'] == 'forum'){
					if ($value[$i]) {
						$selected = $value[$i] == $k ? 'selected' : '';
						$fidoption .= '<option value="'.$k.'" '.$selected.'>'.$v['name'].'</option>';
					} else {
						$fidoption .= '<option value="'.$k.'">'.$v['name'].'</option>';
					}
				}
			}
			$fidoption .= '</select>';
			$fidoptions[$key][$i] = $fidoption;
		}
	}
} else {
	InitGP(array('fid1','fid2','fid3','old_level','level','new_fid1','new_fid2','new_fid3','new_level'));
	foreach ($old_level as $key => $value){
		$fiddb = array();
		if ($level[$key] == 0) continue;
		$fiddb = array($fid1[$value],$fid2[$value],$fid3[$value]);
		$fiddb = array_diff($fiddb,array(0));
		if ($key != $level[$key]) {
			$config[$level[$key]] = $fiddb;
		} else {
			$config[$value] = $fiddb;
		}
	}
	foreach ($new_level as $key => $value) {
		$fiddb = array();
		if ($value == 0) continue;
		$fiddb = array($new_fid1[$key],$new_fid2[$key],$new_fid3[$key]);
		$fiddb = array_diff($fiddb,array(0));
		$config[$value] = $fiddb;
	}
	krsort($config);
	//update_index_forum($config);
	writeover(D_P.'data/bbscache/mode_index_forum_'.$m.'_config.php',"<?php\r\n\$index_forum_config = ".pw_var_export($config)."\r\n?>");
	P_unlink(D_P.'data/bbscache/mode_'.$m.'_index_forum.php');
	$config = serialize($config);
	$db->pw_update(
		"SELECT * FROM pw_cache WHERE name='areaindexforumconfig'",
		"UPDATE pw_cache SET cache=".pwEscape($config,false)."WHERE name='areaindexforumconfig'",
		"INSERT INTO pw_cache SET name='areaindexforumconfig',cache=".pwEscape($config,false)
	);
	adminmsg('operate_success');
}
function update_index_forum($config){
	global $m;
	@include_once Pcv(D_P.'data/bbscache/mode_'.$m.'_index_forum.php');
	foreach ($index_forum as $key => $value) {
		$config_fids = array_keys($config);
		if (!in_array($key,$config_fids)) {
			unset($index_forum[$key]);
			continue;
		}
		foreach ($value as $k => $v) {
			if (!in_array($k,$config[$key])){
				unset($index_forum[$key][$k]);
			}
		}
	}
	writeover(Pcv(D_P.'data/bbscache/mode_'.$m.'_index_forum.php'),"<?php\r\n\$index_forum=".pw_var_export($index_forum).";\r\n?>");
}

include PrintMode('indexforum');exit;