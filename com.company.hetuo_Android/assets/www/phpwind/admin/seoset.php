<?php
! defined ( 'P_W' ) && exit ( 'Forbidden' );

InitGP(array('mode'),'GP');
$db_names = array('db_bbstitle','db_metadescrip','db_metakeyword');
if (empty($mode) || $mode == 'bbs') {
	$menu_bbs = 'class="current"';
	$menu_area = '';
}elseif($mode == 'area'){
	$db_names = array('db_areaTitle','db_areaMetadescrip','db_areaMetakeyword');
	$menu_area = 'class="current"';
	$menu_bbs = '';
}

if ($action == 'update') {
	include(D_P.'data/bbscache/forum_cache.php');
	InitGP(array('contents','forums'),'p');
	foreach ($forums as $key => $value) {
		$forums[$key]['title'] = $value['title'] = Strip_Space(Char_cv(strip_tags($value['title'])));
		$forums[$key]['descrip'] = $value['descrip'] = Strip_Space(Char_cv(strip_tags($value['descrip'])));
		$forums[$key]['keywords'] = $value['keywords'] = Strip_Space(Char_cv(strip_tags($value['keywords'])));
		if ($forum[$key]['title'] != $value['title'] 
				|| $forum[$key]['descrip'] != $value['descrip'] 
				|| $forum[$key]['keywords'] != $value['keywords']) {
			$db->update("UPDATE pw_forums SET title=".pwEscape($value['title']).",metadescrip=".pwEscape($value['descrip']).",keywords=".pwEscape($value['keywords'])." WHERE fid = " . pwEscape($key));
		}
	}
	updatecache_f();
	$db_bbstitle = array('index'=>Strip_Space(Char_cv(strip_tags($contents['title_index']))),
						 'thread'=>Strip_Space(Char_cv(strip_tags($contents['title_thread']))),
						 'read'=>Strip_Space(Char_cv(strip_tags($contents['title_read']))));
	$db_metadescrip = array('index'=>Strip_Space(Char_cv(strip_tags($contents['metadesc_index']))),
					        'thread'=>Strip_Space(Char_cv(strip_tags($contents['metadesc_thread']))),
					        'read'=>Strip_Space(Char_cv(strip_tags($contents['metadesc_read']))));
	$db_metakeyword = array('index'=>Strip_Space(Char_cv(strip_tags($contents['metakeyword_index']))),
							'thread'=>Strip_Space(Char_cv(strip_tags($contents['metakeyword_thread']))),
							'read'=>Strip_Space(Char_cv(strip_tags($contents['metakeyword_read']))));
	$config = array();
	$config[] = array('db_name'=>$db_names[0],'vtype'=>'array','db_value'=>serialize($db_bbstitle));
	$config[] = array('db_name'=>$db_names[1],'vtype'=>'array','db_value'=>serialize($db_metadescrip));
	$config[] = array('db_name'=>$db_names[2],'vtype'=>'array','db_value'=>serialize($db_metakeyword));
	$sql = "REPLACE INTO pw_config (db_name,vtype,db_value) VALUES ". pwSqlMulti($config);
	$db->update($sql);
	updatecache_c();
	$basename = $basename . '&mode=' . $mode;
	adminmsg('operate_success');
	
}else{
	if (!file_exists(D_P.'data/bbscache/config.php') 
			|| !isset($db_bbstitle) || !isset($db_metadescrip) || !isset($db_metakeyword)) {
		$sql = "SELECT * FROM pw_config WHERE db_name IN ( ". pwImplode($db_names) ." ) ";
		$query = $db->query($sql);
		while ($rt = $db->fetch_array($query)) {
			${$rt['db_name']} = unserialize($rt['db_value']);
		}
	}
	
	$config['title'] = is_array(${$db_names[0]}) ? ${$db_names[0]} : array('index'=>${$db_names[0]},'thread'=>${$db_names[0]},'read'=>${$db_names[0]});
	$config['metadescrip'] = is_array(${$db_names[1]}) ? ${$db_names[1]} : array('index'=>${$db_names[1]},'thread'=>${$db_names[1]},'read'=>${$db_names[1]});
	$config['metakeyword'] = is_array(${$db_names[2]}) ? ${$db_names[2]} : array('index'=>${$db_names[2]},'thread'=>${$db_names[2]},'read'=>${$db_names[2]});
	
	#get forums
	$sql = "SELECT fid,fup,name,type,title,metadescrip,keywords FROM pw_forums ORDER BY vieworder"; 
	$query = $db->query($sql);
	while ($rt = $db->fetch_array($query)) {
		$rt['name'] = Quot_cv(strip_tags($rt['name']));
		if ($rt['type'] == 'category') {
			$categorys[] = $rt;
		} elseif ($rt['type'] == 'forum') {
			$forums[] = $rt;
		} elseif ($rt['type'] == 'sub') {
			$subForums1[] = $rt;
		} else {
			$subForums2[] = $rt;
		}
	}
	
	if (empty($mode) || $mode == 'bbs') {
		$forumList = array();
		foreach ($categorys as $k1 => $category) {
			foreach ($forums as $k2 => $forum) {
				if ($forum['fup'] == $category['fid']) {
					$forum['limage1'] = '&nbsp;';
					$forum['limage'] = '&nbsp;';
					$forumList[$category['fid']][] = $forum;
					foreach ($subForums1 as $k3 => $subf1) {
						if ($subf1['fup'] == $forum['fid']) {
							$subf1['limage1'] = '&nbsp;&nbsp;';
							$subf1['limage'] = '&nbsp;&nbsp;;';
							$forumList[$category['fid']][] = $subf1;
							foreach ($subForums2 as $k4 => $subf2) {
								if ($subf2['fup'] == $subf1['fid']) { 
									$subf2['limage1'] = '&nbsp;&nbsp;&nbsp;';
									$subf2['limage'] = '&nbsp;&nbsp;&nbsp;';
									$forumList[$category['fid']][] = $subf2;
								}
							}
						}
					}
				}
			}
		}	
	}
	unset($forums,$subForums1,$subForums2);
	include PrintEot ( 'seoset' );
}
function Strip_Space($v){
	return trim(preg_replace('([\s| ]+)',' ',$v));
}
exit ();
?>