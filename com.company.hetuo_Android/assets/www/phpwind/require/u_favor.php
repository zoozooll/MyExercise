<?php
!function_exists('readover') && exit('Forbidden');

InitGP(array('job','type'));

if (empty($job)) {

	include_once(D_P.'data/bbscache/forum_cache.php');
	$typeid = $favordb = array();
	$favor  = $db->get_one("SELECT tids,type FROM pw_favors WHERE uid=".pwEscape($userdb['uid']));
	Add_S($favor);
	if ($favor['type']) {
		$typeid = explode(',',$favor['type']);
	}
	if ($type == 'forum') {
		$fids = $db->get_value("SELECT shortcut FROM pw_members WHERE uid=".pwEscape($userdb['uid']));
		list($fids) = explode("\t",$fids);
		if ($fids = trim($fids,',')) {
			$fids = pwImplode(explode(',',$fids));
			$query = $db->query("SELECT f.fid,f.name,fd.tpost,fd.topic,fd.article FROM pw_forums f LEFT JOIN pw_forumdata fd USING(fid) WHERE f.fid IN($fids)");
			while ($rt = $db->fetch_array($query)) {
				$favordb[] = $rt;
			}
		}
	} else {
		InitGP(array('page'));
		!is_numeric($type) && $type = 'all';
		$tids = $ptids = array();
		if ($tiddb = getfavor($favor['tids'])) {
			if ($type == 'all') {
				foreach ($tiddb as $key => $val) {
					if ($val) {
						$tids += $val;
					}
				}
			} elseif ($tiddb[$type]) {
				$tids = $tiddb[$type];
			}
			(int)$page<1 && $page = 1;
			$start_limit = intval(($page-1) * 10);
			$count = count($tids);
			$numofpage = ceil($count/10);
			$numofpage < 1 && $numofpage = 1;
			$page > $numofpage && $page = $numofpage;
			$pages = numofpage($count,$page,$numofpage,"u.php?action=favor&type=$type&uid=$uid&");
		}
		if ($tids) {
			$tids = pwImplode($tids);
			$tidarray = array();
			$fidoff = $isU ? array(0) : getFidoff($groupid);

			$query = $db->query("SELECT fid,tid,subject,postdate,author,authorid,anonymous FROM pw_threads WHERE tid IN($tids) AND fid NOT IN(".pwImplode($fidoff).") ORDER BY postdate DESC");
			while ($rt = $db->fetch_array($query)) {
				$rt['subject']	= substrs($rt['subject'],45);
				$rt['postdate']	= get_date($rt['postdate']);
				$keyvalue		= get_key($rt['tid'],$tiddb);
				if ($rt['anonymous'] && !in_array($groupid,array('3','4')) && $rt['authorid'] != $winduid) {
					$rt['author']	= $db_anonymousname;
					$rt['authorid'] = 0;
				}
				$type == 'all' && $tidarray[$keyvalue][$rt['tid']] = $rt['tid'];
				$rt['forum']	= $forum[$rt['fid']]['name'];
				$rt['sel']		= $typeid[$keyvalue-1];
				$favordb[]		= $rt;
			}
			$favordb = array_slice($favordb,$start_limit,10);
		}
	}
	require_once PrintEot('u');footer();

} elseif ($job == 'delforum') {

	PostCheck();
	!$isU && Showmsg('space_over_right');
	InitGP(array('selid'),'P');
	!$selid && Showmsg('sel_error');
	$rs = $db->get_one("SELECT shortcut FROM pw_members WHERE uid=".pwEscape($winduid));
	$fids = array();
	list($rs['shortcut'],$rt['appshortcut']) = explode("/t",$rs['shortcut']);
	$shortcut = explode(',',$rs['shortcut']);
	foreach ($shortcut as $key => $val) {
		if ($val && !in_array($val,$selid)) {
			$fids[] = $val;
		}
	}
	$shortcut = implode(',',$fids);
	$shortcut && $shortcut = ','.$shortcut.',';
	$rt['appshortcut'] && $shortcut .= "\t".$rt['appshortcut'];
	$db->update("UPDATE pw_members SET shortcut=".pwEscape($shortcut)." WHERE uid=".pwEscape($winduid));

	refreshto("u.php?action=favor&type=forum",'operate_success');

} elseif ($job == 'clear') {

	PostCheck();
	!$isU && Showmsg('space_over_right');
	InitGP(array('selid'),'P');
	!$selid && Showmsg('sel_error');
	$rs = $db->get_one("SELECT tids FROM pw_favors WHERE uid=".pwEscape($winduid));
	if ($rs) {
		$tiddb = getfavor($rs['tids']);
		foreach ($selid as $key => $tid) {
			foreach ($tiddb as $k => $v) {
				if (in_array($tid,$v)) {
					unset($tiddb[$k][$tid]);
				}
			}
		}
		foreach ($tiddb as $key => $val) {
			if (empty($val)) {
				unset($tiddb[$key]);
			}
		}
		$newtids = makefavor($tiddb);
		$db->update("UPDATE pw_favors SET tids=".pwEscape($newtids)."WHERE uid=".pwEscape($winduid));
		refreshto("u.php?action=favor",'operate_success');
	} else {
		Showmsg('job_favor_del');
	}
} elseif ($job == 'change') {

	PostCheck();
	!$isU && Showmsg('space_over_right');
	InitGP(array('selid'),'P');
	!$selid && Showmsg('sel_error');
	$rs = $db->get_one("SELECT tids FROM pw_favors WHERE uid=".pwEscape($winduid));
	if ($rs) {
		$tiddb = getfavor($rs['tids']);
		foreach ($selid as $key => $tid) {
			if (!is_numeric($tid)) continue;
			foreach ($tiddb as $k => $v) {
				if (in_array($tid,$v)) {
					unset($tiddb[$k][$tid]);
				}
			}
			$tiddb[$type][$tid] = $tid;
		}
		foreach ($tiddb as $key => $val) {
			if (empty($val)) {
				unset($tiddb[$key]);
			}
		}
		$newtids = makefavor($tiddb);
		$db->update("UPDATE pw_favors SET tids=".pwEscape($newtids)."WHERE uid=".pwEscape($winduid));
	}
	refreshto("u.php?action=favor",'operate_success');

} elseif ($job == 'addtype') {

	PostCheck();
	!$isU && Showmsg('space_over_right');
	(!$type || strlen($type)>20) && Showmsg('favor_cate_error');
	strpos($type,',') !== false && Showmsg('favor_cate_limit');
	$favor   = $db->get_one("SELECT type FROM pw_favors WHERE uid=".pwEscape($winduid));
	$newtype = $favor['type'];
	$newtype.= $newtype ? ",".stripslashes($type) : stripslashes($type);
	$newtype = addslashes(Char_cv($newtype));
	if ($favor) {
		$db->update("UPDATE pw_favors SET type=".pwEscape($newtype)."WHERE uid=".pwEscape($winduid));
	} else{
		$db->update("INSERT INTO pw_favors SET".pwSqlSingle(array('uid'=>$winduid,'type'=>$newtype)));
	}
	refreshto("u.php?action=favor",'operate_success');

} elseif ($job == 'deltype') {

	PostCheck();
	!$isU && Showmsg('space_over_right');
	(int)$type<1 && Showmsg('type_error');
	$tnum  = $type-1;
	$rs    = $db->get_one("SELECT tids,type FROM pw_favors WHERE uid=".pwEscape($winduid));
	$tiddb = getfavor($rs['tids']);
	$typedb= explode(',',$rs['type']);
	Add_S($typedb);
	unset($typedb[$tnum]);
	if ($tiddb[$type]) {
		foreach ($tiddb[$type] as $key => $val) {
			$tiddb['0'][$val] = $val;
		}
	}
	unset($tiddb[$type]);
	$newtids = makefavor($tiddb);
	$newtype = Char_cv(implode(',',$typedb));
	$db->update("UPDATE pw_favors SET ".pwSqlSingle(array('tids'=>$newtids,'type'=>$newtype))."WHERE uid=".pwEscape($winduid));
	refreshto("u.php?action=favor",'operate_success');
}

function getfavor($tids) {
	$tids  = explode('|',$tids);
	$tiddb = array();
	foreach ($tids as $key => $t) {
		if ($t) {
			$v = explode(',',$t);
			foreach ($v as $k => $v1) {
				$tiddb[$key][$v1] = $v1;
			}
		}
	}
	return $tiddb;
}

function makefavor($tiddb) {
	$newtids = $ex = '';
	$k = 0;
	ksort($tiddb);
	foreach ($tiddb as $key => $val) {
		$new_tids = '';
		rsort($val);
		if ($key != $k) {
			$s = $key - $k;
			for ($i = 0; $i < $s; $i++) {
				$newtids .= '|';
			}
		}
		foreach ($val as $k => $v) {
			is_numeric($v) && $new_tids .= $new_tids ? ','.$v : $v;
		}
		$newtids .= $ex.$new_tids;
		$k  = $key + 1;
		$ex = '|';
	}
	return $newtids;
}

function get_key($tid,$tiddb) {
	foreach ($tiddb as $key => $value) {
		if (in_array($tid,$value)) {
			return $key;
		}
	}
	return false;
}
?>