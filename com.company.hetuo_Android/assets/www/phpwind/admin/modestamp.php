<?php
!defined('P_W') && exit('Forbidden');
require_once(R_P.'admin/cache.php');
$basename = "$admin_file?adminjob=modestamp";
if (!file_exists(D_P.'data/bbscache/mode_stamp.php')) {
	updateStampCache();
}
include_once(D_P.'data/bbscache/mode_stamp.php');
if (!$action) {
	include PrintEot('modestamp');exit;
} elseif ($action == 'addstamp') {
	InitGP(array('name','stamp'),'P');
	(!$name || !$stamp) && adminmsg('stamp_namestamp_must');
	(strlen($name)>30 || strlen($stamp)>30) && adminmsg('stamp_namestamp_toolang');
	$query = $db->get_one("SELECT * FROM pw_stamp WHERE stamp=".pwEscape($stamp));
	$query && adminmsg("stamp_have_exist");
	$db->update("INSERT INTO pw_stamp(name,stamp) VALUES(".pwImplode(array($name,$stamp)).")");
	updateStampCache();
	adminmsg('operate_success');
} elseif ($action == 'editstamp') {
	InitGP(array('step','stamp'));
	if ($step == 2) {
		if ($mode_stamp[$stamp]['iflock']==1) {
			//adminmsg('stamp_system_lock');
		}
		InitGP(array('name','stamp','init','sid'),'P');
		(!$name || !$stamp) && adminmsg('stamp_namestamp_must');
		$init	= (int)$init;
		(strlen($name)>30 || strlen($stamp)>30) && adminmsg('stamp_namestamp_toolang');
		$query = $db->get_one("SELECT * FROM pw_stamp WHERE stamp=".pwEscape($stamp)." AND sid <>".pwEscape($sid));
		$query && adminmsg("stamp_have_exist");
		$db->update("UPDATE pw_stamp SET ".pwSqlSingle(array('name'=>$name,'stamp'=>$stamp,'init'=>$init))." WHERE sid=".pwEscape($sid));
		updateStampCache();
		adminmsg('operate_success');
	} else {
		$stamp = $mode_stamp[$stamp];
		$sid = $stamp['sid'];
		$blocks = array();
		$query = $db->query("SELECT * FROM pw_block WHERE sid=".pwEscape($sid));
		while ($rs = $db->fetch_array($query)) {
			$blocks[] = $rs;
		}
		include PrintEot('modestamp');exit;
	}
} elseif ($action == 'delstamp') {
	InitGP(array('sid'),null,2);
	$thisstamp = array();
	foreach ($mode_stamp as $key=>$value) {
		if ($value['sid'] == $sid) {
			$thisstamp = $value;
			break;
		}
	}
	if ($thisstamp['iflock'] == 1) {
		//adminmsg('stamp_system_lock');
	}
	$db->update("DELETE FROM pw_stamp WHERE sid=".pwEscape($sid));
	$db->update("DELETE FROM pw_block WHERE sid=".pwEscape($sid));
	updateStampCache();
	updateBlockCache();
	adminmsg('operate_success');
} elseif ($action == 'addblock') {
	InitGP(array('name','function','sid','rang'),'P');
	InitGP(array('ifextra','cachetime'),'P',2);
	(!$name || !$function) && adminmsg('block_namefunction_must');
	(strlen($name)>30 || strlen($function)>30) && adminmsg('block_lenth_toolang');
	$function = strtolower($function);
	$db->update("INSERT INTO pw_block(sid,function,name,rang,cachetime) VALUES(".pwImplode(array($sid,$function,$name,$rang,$cachetime)).")");
	updateBlockCache();
	adminmsg('operate_success',$basename."&action=viewblock&sid=".$sid);
} elseif ($action == 'viewblock') {
	InitGP(array('sid'));
	foreach ($mode_stamp as $key=>$value) {
		if ($value['sid'] == $sid) {
			$stamp = $value;
			break;
		}
	}
	$blocks = array();
	$query = $db->query("SELECT * FROM pw_block WHERE sid=".pwEscape($sid)." ORDER BY function, bid");
	while ($rs = $db->fetch_array($query)) {
		$blocks[] = $rs;
	}
	include PrintEot('modestamp');exit;
} elseif ($action == 'editblock') {
	InitGP(array('step','bid','sid'),null,2);
	require_once(D_P.'data/bbscache/mode_block.php');
	if ($step == 2) {
		if ($mode_block[$bid]['iflock'] == 1) {
			//adminmsg('block_system_lock');
		}
		InitGP(array('name','function','rang'),'P');
		InitGP(array('ifextra','cachetime'),'P',2);
		(!$name || !$function) && adminmsg('block_namefunction_must');
		(strlen($name)>30 || strlen($function)>30) && adminmsg('block_lenth_toolang');
		$function = strtolower($function);
		$db->update("UPDATE pw_block SET ".pwSqlSingle(array('name'=>$name,'function'=>$function,'rang'=>$rang,'cachetime'=>$cachetime,'ifextra'=>$ifextra))." WHERE bid=".pwEscape($bid));
		updateBlockCache();
		adminmsg('operate_success',$basename."&action=viewblock&sid=".$sid);
	} else {
		$block 	= $db->get_one("SELECT * FROM pw_block WHERE bid=".pwEscape($bid));
		$sid 	= $block['sid'];
		foreach ($mode_stamp as $key=>$value) {
			if ($value['sid'] == $sid) {
				$stamp = $value;
				break;
			}
		}
		ifcheck($block['ifextra'],'ifextra');
		include PrintEot('modestamp');exit;
	}
} elseif ($action == 'delblock') {
	InitGP(array('bid'),null,2);
	require_once(D_P.'data/bbscache/mode_block.php');
	if ($mode_block[$bid]['iflock'] == 1) {
		//adminmsg('block_system_lock');
	}
	$db->update("DELETE FROM pw_block WHERE bid=".pwEscape($bid));
	updateBlockCache();
	adminmsg('operate_success');
} elseif ($action == 'setdefault') {
	InitGP(array('bid','sid'),null,2);
	$db->update("UPDATE pw_stamp SET init=".pwEscape($bid)."WHERE sid=".pwEscape($sid));
	updateStampCache();
	adminmsg('operate_success',$basename."&action=viewblock&sid=".$sid);
}

?>