<?php
!function_exists('adminmsg') && exit('Forbidden');
$basename = "$admin_file?adminjob=unituser";

require_once(R_P.'require/credit.php');

if (!$action) {

	require_once PrintEot('unituser');

} elseif ($_POST['action'] == "unit") {

	InitGP(array('uids','newuid'),'P');
	if (!$uids) {
		adminmsg('unituser_username_empty');
	}
	if (!$newuid) {
		adminmsg('unituser_newname_empty');
	}
	$touser = $db->get_one("SELECT username FROM pw_members WHERE uid=".pwEscape($newuid));
	Add_S($touser);
	if (!$touser['username']) {
		adminmsg('unituser_newname_error');
	}
	$oldinfo = array();
	$uids = explode(',',$uids);
	foreach ($uids as $key => $val) {
		if (is_numeric($val)) {
			if ($val == $newuid) {
				adminmsg('unituser_samename');
			}
			$rt = $db->get_one("SELECT m.uid,m.username,md.postnum,md.digests,md.rvrc,md.money,md.credit,md.currency,mi.deposit,mi.ddeposit FROM pw_members m LEFT JOIN pw_memberdata md ON m.uid=md.uid LEFT JOIN pw_memberinfo mi ON m.uid=mi.uid WHERE m.uid=".pwEscape($val));
			if (!$rt['uid']) {
				adminmsg('unituser_username_error');
			} else {
				$oldinfo[] = $rt;
			}
		}
	}
	$ptable_a = array('pw_posts');

	if ($db_plist && count($db_plist)>1) {
		foreach ($db_plist as $key => $value) {
			if($key == 0) continue;
			$ptable_a[] = 'pw_posts'.$key;
		}
	}
	$postnum = $digests = $rvrc = $money = $credits = $currency = $deposit = $ddeposit = 0;
	foreach ($oldinfo as $key => $value) {
		$postnum  += $value['postnum'];
		$digests  += $value['digests'];
		$rvrc     += $value['rvrc'];
		$money    += $value['money'];
		$credits   += $value['credit'];
		$currency += $value['currency'];
		$deposit  += $value['deposit'];
		$ddeposit += $value['ddeposit'];

		$creditdb = $credit->get($value['uid'],'CUSTOM');
		foreach ($creditdb as $k => $val) {
			$db->pw_update(
				"SELECT uid FROM pw_membercredit WHERE uid=".pwEscape($newuid)."AND cid=".pwEscape($k),
				"UPDATE pw_membercredit SET value=value+".pwEscape($val[1])."WHERE uid=".pwEscape($newuid)."AND cid=".pwEscape($k),
				"INSERT INTO pw_membercredit SET".pwSqlSingle(array('uid'=>$newuid,'cid'=>$k,'value'=>$val[1]))
			);
		}

		$db->update("UPDATE pw_threads SET ".pwSqlSingle(array('author'=>$touser['username'],'authorid'=>$newuid))."WHERE authorid=".pwEscape($value['uid']));
		foreach ($ptable_a as $val) {
			$db->update("UPDATE $val SET ".pwSqlSingle(array('author'=>$touser['username'],'authorid'=>$newuid))."WHERE authorid=".pwEscape($value['uid']));
		}
		$db->update("UPDATE pw_attachs SET uid=".pwEscape($newuid)."WHERE uid=".pwEscape($value['uid']));
		$db->update("DELETE FROM pw_members WHERE uid=".pwEscape($value['uid']));
		$db->update("DELETE FROM pw_memberdata WHERE uid=".pwEscape($value['uid']));
		$db->update("DELETE FROM pw_memberinfo WHERE uid=".pwEscape($value['uid']));
		$db->update("DELETE FROM pw_msg WHERE type='rebox' AND touid=".pwEscape($value['uid'])."OR type='sebox' AND fromuid=".pwEscape($value['uid']));
		$db->update("DELETE FROM pw_msglog WHERE uid='$value[uid]'");
	}
	$db->update("UPDATE pw_memberdata SET postnum=postnum+".pwEscape($postnum).',digests=digests+'.pwEscape($digests).',rvrc=rvrc+'.pwEscape($rvrc).',money=money+'.pwEscape($money).',credit=credit+'.pwEscape($credits).',currency=currency+'.pwEscape($currency).'WHERE uid='.pwEscape($newuid));
	$db->update("UPDATE pw_memberinfo SET deposit=deposit+".pwEscape($deposit).',ddeposit=ddeposit+'.pwEscape($ddeposit).'WHERE uid='.pwEscape($newuid));
	adminmsg('operate_success');
}
?>