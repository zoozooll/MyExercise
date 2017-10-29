<?php
!function_exists('adminmsg') && exit('Forbidden');
$basename = "$admin_file?adminjob=pushdata";

$push = L::loadClass('push');
InitGP(array('action','step'));

if (empty($action)) {
	$query = GetGP('query');
	if (is_string($query)) {
		$query = unserialize(base64_decode($query));
	}
	$query = Char_cv($query);
	$pushdata = $where = array();
	if (!$query['uid'] && $query['username']) {
		$query['uid'] = $GLOBALS['db']->get_value("SELECT uid FROM pw_members WHERE username=".pwEscape($query['username'],false));
		if (!$query['uid']) {
			adminmsg('operate_error');
		}
	}
	if ($query['pushid']) {
		if (is_array($query['pushid'])) {
			$where[] = "pushid IN (".pwImplode($query['pushid'],false).")";
		} else {
			$where[] = "pushid=".pwEscape($query['pushid'],false);
		}
	}
	if ($query['ifshow']) {
		$where[] = "ifshow=".pwEscape($query['ifshow'],false);
	}
	if ($query['uid']) {
		$where[] = "uid=".pwEscape($query['uid'],false);
	}
	if ($query['keyword']) {
		$where[] = "subject LIKE ".pwEscape('%'.$query['keyword'].'%',false);
	}
	if (isset($query['imgsrc'])) {
		if ($query['imgsrc']) {
			$where[] = "imgsrc!=''";
		} else {
			$where[] = "imgsrc=''";
		}
	}
	if ($query['stime']) {
		if (!is_numeric($query['stime'])) {
			$query['stime'] = PwStrtoTime($query['stime']);
		}
		$where[] = "stime>".pwEscape($query['stime'],false);
	}
	$where = $where ? ' WHERE '.implode(' AND ',$where) : '';

	$page = getGP('page');
	(!is_numeric($page) || $page < 1) && $page = 1;
	$limit = pwLimit(($page-1)*$db_perpage,$db_perpage);
	$count = $db->get_value("SELECT COUNT(*) FROM pw_pushdata WHERE $where");
	$pages = numofpage($count,$page,ceil($count/$db_perpage),"$basename&query=".base64_encode(serialize($query)).'&');

	$rs = $db->query("SELECT * FROM pw_pushdata $where ORDER BY id DESC $limit");
	while ($rt = $db->fetch_array($rs)) {
		$pushdata[] = $rt;
	}

	include PrintEot('pushdata');exit;
} elseif ($action == 'repush') {
	InitGP(array('selid'),'P');
	if (!$selid = checkselid($selid)) {
		$basename = "javascript:history.go(-1);";
		adminmsg('operate_error');
	}
	$stime = $timestamp;
	$etime = $stime+7*8640;
	$db->update("UPDATE pw_pushdata SET ifshow=1,stime=".pwEscape($stime,false).",etime=".pwEscape($etime,false)."WHERE id IN (".pwImplode($selid,false).")");

	adminmsg('operate_success');
} elseif ($action == 'delete') {
	InitGP(array('selid'),'P');
	if (!$selid = checkselid($selid)) {
		$basename = "javascript:history.go(-1);";
		adminmsg('operate_error');
	}
	$db->update("DELETE FROM pw_pushdata WHERE id IN (".pwImplode($selid,false).")");

	adminmsg('operate_success');
} else {
	adminmsg('operate_error');
}
?>