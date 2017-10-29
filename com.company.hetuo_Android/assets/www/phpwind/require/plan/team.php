<?php
!function_exists('readover') && exit('Forbidden');

require_once(R_P.'require/credit.php');
@include_once(D_P.'data/bbscache/tm_config.php');

$gids = 0;
if (!empty($_tmconf['group'])) {
	$gids = implode(',',$_tmconf['group']);
}

$admindb = array();
$query = $db->query("SELECT m.uid,m.username,m.groupid,md.monthpost,md.monoltime,md.lastvisit,md.lastpost FROM pw_members m LEFT JOIN pw_memberdata md USING(uid) WHERE groupid IN($gids) ORDER BY groupid");

while ($rs = $db->fetch_array($query)) {
	$rs['lastvisit'] < $montime && $rs['monoltime'] = 0;
	$rs['lastpost']  < $montime && $rs['monthpost'] = 0;
	$admindb[$rs['username']] = array(
		'uid'		=> $rs['uid'],
		'groupid'	=> $rs['groupid'],
		'monoltime'	=> round($rs['monoltime']/3600),
		'monthpost'	=> $rs['monthpost'],
		'total'		=> 0,
		'arouse'	=> 0,
	);
}
$query = $db->query("SELECT COUNT(*) AS count,username2 AS manager FROM pw_adminlog WHERE timestamp>'$montime' GROUP BY username2");

while ($rs = $db->fetch_array($query)) {
	if (isset($admindb[$rs['manager']])) {
		$admindb[$rs['manager']]['total'] = $rs['count'];
	}
}
foreach ($admindb as $key=>$value) {
	$gid = $value['groupid'];
	$admindb[$key]['assess'] = $value['total'] * $_tmconf['param']['opr'] + $value['monoltime'] * $_tmconf['param']['oltime'] + $value['monthpost'] * $_tmconf['param']['post'];
	$admindb[$key]['wages'] = $_tmconf['wages'][$gid];
	foreach ($admindb[$key]['wages'] as $k=>$v) {
		$admindb[$key]['wages'][$k] += round($admindb[$key]['assess'] * $_tmconf['bonus'][$k]);
	}
	$admindb[$key]['assess'] < $_tmconf['eligibility'] && $admindb[$key]['arouse'] = 1;
}

$msg_a	 = array();
$datef	 = get_date($timestamp,'Y - m');
$msgdata = Char_cv($_tmconf['msgdata']);
$arousemsg = Char_cv($_tmconf['arousemsg']);

foreach ($admindb as $username => $value) {
	$uid = $value['uid'];
	$addcredit = '';
	foreach ($value['wages'] as $k => $v) {
		if (empty($v) || !is_numeric($v)) continue;
		$addcredit .= ($addcredit ? ',' : '')."[color=#0000ff]{$v}[/color]".$credit->cType[$k];
	}
	$credit->addLog('hack_teampay',$value['wages'],array(
		'uid'		=> $uid,
		'username'	=> $username,
		'ip'		=> $onlineip,
		'datef'		=> $datef
	));
	$credit->sets($uid,$value['wages'],false);

	if ($addcredit) {
		if ($_tmconf['arouse'] && $value['arouse'] || $_tmconf['ifmsg']) {
			$msg_a[] = array($uid,'0','SYSTEM','rebox','1',$timestamp,$_tmconf['msgtitle'], str_replace(array('$username','$db_bbsname','$credit','$time'),array($username,$db_bbsname,$addcredit, get_date($timestamp)),($_tmconf['arouse'] && $value['arouse']) ? $arousemsg : $msgdata));
		}
	}
}
$credit->runsql();

if ($msg_a) {
	require_once(R_P.'require/msg.php');
	send_msgc($msg_a);
}
?>