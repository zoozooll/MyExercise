<?php
!function_exists('readover') && exit('Forbidden');
$wind_in = 'medal';
include_once(D_P.'data/bbscache/md_config.php');
include_once(D_P.'data/bbscache/medaldb.php');
include_once(R_P.'require/showimg.php');
!$md_ifopen && Showmsg('medal_close');

$_cache = getDatastore();
$userdb = $db->get_one("SELECT medals,icon FROM pw_members WHERE uid=".pwEscape($winduid));
if ($userdb['medals']) {
	$userdb['medals'] = explode(',',$userdb['medals']);
} else{
	$userdb['medals'] = '';
}
$userface = showfacedesign($userdb['icon'],'','m');
InitGP(array('action'));

if (!$action) {

	if ($userdb['medals']) {
		$ifunset = 0;
		foreach ($userdb['medals'] as $key => $val) {
			if (!array_key_exists($val,$_MEDALDB)) {
				unset($userdb['medals'][$key]);
				$ifunset = 1;
			}
		}
		if ($ifunset) {
			$newmedals = implode(',',$userdb['medals']);
			$db->update("UPDATE pw_members SET medals=".pwEscape($newmedals)."WHERE uid=".pwEscape($winduid));
			$_cache->delete('UID_'.$winduid);
			!$newmedals && updatemedal_list();
		}
	}
	require_once PrintHack('index');footer();

} elseif ($action == 'list') {

	$groupid == 'guest' && Showmsg('not_login');
	if (!file_exists(D_P.'data/bbscache/medals_list.php')) {
		updatemedal_list();
	}
	$uids = substr(readover(D_P.'data/bbscache/medals_list.php'),12);
	if ($uids) {
		InitGP(array('page'));
		(!is_numeric($page) || $page < 1) && $page = 1;
		$limit = pwLimit(($page-1)*$db_perpage,$db_perpage);
		$rt    = $db->get_one("SELECT COUNT(*) AS sum FROM pw_members WHERE uid IN($uids)");
		$pages = numofpage($rt['sum'],$page,ceil($rt['sum']/$db_perpage),"$basename&action=list&");

		$listdb=array();
		$query = $db->query("SELECT uid,username,medals FROM pw_members WHERE uid IN($uids) ORDER BY uid $limit");
		while ($rt = $db->fetch_array($query)) {
			$medals = '';
			$md_a = explode(',',$rt['medals']);
			foreach ($md_a as $key => $value) {
				if ($value) {
					if (strpos($md_groups,",$groupid,") !== false) {
						$medals .= "<a href=\"$basename&action=award&type=2&pwuser=".rawurlencode($rt[username])."&medal=$value\" target=\"_blank\"><img src=\"$hkimg/{$_MEDALDB[$value][picurl]}\" title=\"{$_MEDALDB[$value][name]}\"></a> ";
					} else {
						$medals .= "<img src=\"$hkimg/{$_MEDALDB[$value][picurl]}\" title=\"{$_MEDALDB[$value][name]}\"> ";
					}
				}
			}
			$rt['medals'] = $medals;
			$listdb[] = $rt;
		}
	}
	require_once PrintHack('index');footer();

} elseif ($action == 'award') {

	if (strpos($md_groups,",$groupid,") === false) {
		Showmsg('medal_groupright');
	}
	if (!$_POST['step']) {

		InitGP(array('type','pwuser','medal'));
		if ($type == 2) {
			$type_2 = "checked";
			$type_1 = "";
		} else {
			$type_1 = "checked";
			$type_2 = "";
		}
		require_once PrintHack('index');footer();

	} elseif ($_POST['step'] == "2") {

		InitGP(array('pwuser','reason','medal','type','timelimit'),null,'1');
		strpos($pwuser,',') && $pwuser = explode(',',$pwuser);
		$medal  = (int)$medal;
		!$medal && Showmsg('medal_nomedal');
		$reason = Char_cv($reason);
		!$reason && Showmsg('medal_noreason');
		$timelimit = (int)$timelimit;
		require_once(R_P.'require/msg.php');
		if (is_array($pwuser)) {
			foreach ($pwuser as $key => $val) {
				if (!$val) {
					unset($pwuser[$key]);
				} else {
					$pwuser[$key] = $val;
				}
			}
			$pwuser = pwImplode($pwuser);
		} else {
			$pwuser = "'".$pwuser."'";
		}
		!$pwuser && Showmsg('username_empty');

		$rs = $db->query("SELECT uid,username,medals FROM pw_members WHERE username IN($pwuser)");

		$awardusers = $medaluser = array();
		while ($rt = $db->fetch_array($rs)){
			Add_S($rt);
			if ($type == 1) {
				if ($rt['medals'] && strpos(",$rt[medals],",",$medal,") !== false) {
					$erroruser = $rt['username'];
					Showmsg('medal_alreadyhave');
				} elseif ($rt['medals']) {
					$rt['medals'] = "$rt[medals],$medal";
				} else{
					$rt['medals'] = $medal;
				}
				$medaluser[] = array($rt['uid'],$medal);
			} elseif ($type == 2) {
				if (!$rt['medals'] || strpos(",$rt[medals],",",$medal,") === false) {
					$erroruser = $rt['username'];
					Showmsg('medal_none');
				} else {
					$rt['medals'] = substr(str_replace(",$medal,",',',",$rt[medals],"),1,-1);
				}
				$medaluser[] = $rt['uid'];
			} else {
				Showmsg('illegal_request');
			}
			$awardusers[]	= $rt;
		}
		!count($awardusers) && Showmsg('medal_nouser');
		$insertlogs = array();
		foreach ($awardusers as $rt) {
			if ($type == 1) {
				if ($md_ifmsg) {
					$message = array(
						'toUser'	=> $rt['username'],
						'subject'	=> 'metal_add',
						'content'	=> 'metal_add_content',
						'other'		=> array(
							'mname'		=> $_MEDALDB[$medal]['name'],
							'windid'	=> $windid,
							'reason'	=> stripslashes($reason)
						)
					);
					pwSendMsg($message);
				}
			} elseif ($type == 2) {
				if($md_ifmsg){
					$message = array(
						'toUser'	=> $rt['username'],
						'subject'	=> 'metal_cancel',
						'content'	=> 'metal_cancel_content',
						'other'		=> array(
							'mname'		=> $_MEDALDB[$medal]['name'],
							'windid'	=> $windid,
							'reason'	=> stripslashes($reason)
						)
					);
					pwSendMsg($message);
				}
				$timelimit = 0;
				$db->update("UPDATE pw_medalslogs SET state='1' WHERE awardee=".pwEscape($rt['username'],false)."AND level=".pwEscape($medal));
			} else {
				Showmsg('illegal_request');
			}
			$rt['medals'] == ',' && $rt['medals'] = '';
			$db->update("UPDATE pw_members SET medals=".pwEscape($rt['medals'],false)."WHERE uid=".pwEscape($rt['uid'],false));
			$_cache->delete('UID_'.$rt['uid']);
			$insertlogs[] = array($rt['username'],$windid,$timestamp,$timelimit,$medal,$type,$reason);
		}
		if ($medaluser) {
			if ($type == 1) {
				$db->update("INSERT INTO pw_medaluser(uid,mid) VALUES ".pwSqlMulti($medaluser));
			} elseif ($type == 2) {
				$db->update('DELETE FROM pw_medaluser WHERE mid='.pwEscape($medal).' AND uid IN('.pwImplode($medaluser).')');
			}
		}
		if (count($insertlogs)) {
			$db->update("INSERT INTO pw_medalslogs(awardee,awarder,awardtime,timelimit,level,action,why) VALUES".pwSqlMulti($insertlogs));
		}
		updatemedal_list();
		refreshto("$basename&action=list",'operate_success');
	}
} elseif ($action == 'log') {

	$groupid == 'guest' && Showmsg('not_login');

	if (!$_GET['job']) {

		InitGP(array('page'));
		(!is_numeric($page) || $page < 1) && $page = 1;
		$limit = pwLimit(($page-1)*$db_perpage,$db_perpage);
		$rt    = $db->get_one("SELECT COUNT(*) AS sum FROM pw_medalslogs WHERE action<>3");
		$pages = numofpage($rt['sum'],$page,ceil($rt['sum']/$db_perpage),"$basename&action=log&");

		$logdb = array();
		$query = $db->query("SELECT * FROM pw_medalslogs WHERE action<>3 ORDER BY id DESC $limit");
		while($rt = $db->fetch_array($query)){
			$rt['awardtime'] = get_date($rt['awardtime'],'y-m-d H:i');
			$logdb[] = $rt;
		}
		require_once PrintHack('index');footer();

	} elseif ($_GET['job'] == 'del') {

		$groupid != '3' && Showmsg('medal_dellog');
		$id = (int)GetGP('id');
		$rt = $db->get_one("SELECT id,state,action,timelimit FROM pw_medalslogs WHERE id=".pwEscape($id));
		if ($rt['action'] == 1 && $rt['state'] == 0 && $rt['timelimit'] > 0) {
			Showmsg('medallog_del_error');
		}
		$db->update("DELETE FROM pw_medalslogs WHERE id=".pwEscape($id));
		refreshto("$basename&action=log",'operate_success');

	} else {
		Showmsg('illegal_request');
	}
} elseif ($action == 'apply') {

	!$md_ifapply && Showmsg('medal_appclose');
	if (strpos($md_appgroups,",$groupid,") === false) {
		Showmsg('medal_appgroupright');
	}
	$appcheck = $db->get_one("SELECT id FROM pw_medalslogs WHERE awardee=".pwEscape($windid)." AND action=3");
	$appcheck && Showmsg('medal_haveapp');

	if (!$_POST['step']) {

		$id = (int)GetGP('id');
		require_once PrintHack('index');footer();

	} elseif ($_POST['step'] == 2) {

		InitGP(array('reason','medal','timelimit'));
		!$reason && Showmsg('medal_noreason');
		$medal  = (int)$medal;
		!$medal && Showmsg('medal_nomedal');
		$reason = Char_cv($reason);
		$timelimit = (int)$timelimit;
		$userdb['medals'] && in_array($medal,$userdb['medals']) && Showmsg('medal_alreadyhaveself');
		$db->update("INSERT INTO pw_medalslogs SET " . pwSqlSingle(array(
			'awardee'	=> $windid,
			'awardtime'	=> $timestamp,
			'timelimit'	=> $timelimit,
			'level'		=> $medal,
			'action'	=> 3,
			'why'		=> $reason
		)));

		require_once(R_P.'require/msg.php');
		$message = array(
			'toUser'	=> $manager,
			'subject'	=> 'medal_apply_title',
			'content'	=> 'medal_apply_content',
			'other'		=> array(
				'username'	=> $windid,
				'time'		=> get_date($timestamp),
				'medal'		=> $_MEDALDB[$medal]['name']
			)
		);
		pwSendMsg($message);

		refreshto($basename,'operate_success');

	} else {
		Showmsg('illegal_request');
	}
} elseif ($action == 'approve') {

	!$md_ifapply && Showmsg('medal_appclose');
	if (strpos($md_groups,",$groupid,") === false) {
		Showmsg('medal_groupright');
	}
	$job = Char_cv(GetGP('job'));

	if (!$job) {

		InitGP(array('page'));
		(!is_numeric($page) || $page < 1) && $page = 1;
		$limit = pwLimit(($page-1)*$db_perpage,$db_perpage);
		$rt    = $db->get_one("SELECT COUNT(*) AS sum FROM pw_medalslogs WHERE action=3");
		$pages = numofpage($rt['sum'],$page,ceil($rt['sum']/$db_perpage),"$basename&action=approve&");

		$appdb = array();
		$query = $db->query("SELECT * FROM pw_medalslogs WHERE action=3 ORDER BY id ASC $limit");
		while($rt = $db->fetch_array($query)){
			$rt['awardtime'] = get_date($rt['awardtime'],'y-m-d H:i');
			$appdb[] = $rt;
		}
		require_once PrintHack('index');footer();

	} elseif ($job == 'pass') {

		$id = GetGP('id');
		if (is_array($id)) {
			foreach ($id as $key => $val) {
				$val = (int)$val;
				if ($val) {
					$id[$key] = $val;
				} else {
					unset($id[$key]);
				}
			}
			if (count($id)) {
				$id = pwImplode($id);
			} else {
				Showmsg('medal_iderror');
			}
		} else {
			$id = (int)$id;
			!$id && Showmsg('medal_iderror');
		}
		require_once(R_P.'require/msg.php');
		$medaluser = array();
		$rs = $db->query("SELECT l.level,l.why,m.uid,m.username,m.medals FROM pw_medalslogs l LEFT JOIN pw_members m ON l.awardee=m.username WHERE l.id IN($id)");
		while ($rt = $db->fetch_array($rs)) {
			$medal 	= $rt['level'];
			$reason = $rt['why'];
			if ($rt['medals'] && strpos(",$rt[medals],",",$medal,") !== false) {
				continue;
			} elseif ($rt['medals']) {
				$medals = "$rt[medals],$medal";
			} else {
				$medals = $medal;
			}
			$medaluser[] = array($rt['uid'],$medal);
			if ($md_ifmsg) {
				$message = array(
					'toUser'	=> $rt['username'],
					'subject'	=> 'metal_add',
					'content'	=> 'metal_add_content',
					'other'		=> array(
						'mname'		=> $_MEDALDB[$medal]['name'],
						'windid'	=> $windid,
						'reason'	=> $reason
					)
				);
				pwSendMsg($message);
			}
			$medals == ',' && $medals = '';
			$db->update("UPDATE pw_members SET medals=".pwEscape($medals,false)."WHERE uid=".pwEscape($rt['uid'],false));
			$_cache->delete('UID_'.$rt['uid']);
			if ($medaluser) {
				$db->update("INSERT INTO pw_medaluser(uid,mid) VALUES ".pwSqlMulti($medaluser));
			}
		}
		$db->free_result();
		unset($medal,$medals,$reason);
		$db->update("UPDATE pw_medalslogs SET " . pwSqlSingle(array(
			'awarder'	=> $windid,
			'awardtime'	=> $timestamp,
			'action'	=> 1
		)) . " WHERE id IN($id)");

		updatemedal_list();
		refreshto("$basename&action=approve",'operate_success');

	} elseif ($job == 'del') {

		$id = GetGP('id');
		require_once(R_P.'require/msg.php');
		if (is_array($id)) {
			foreach($id as $key => $val) {
				$val = (int)$val;
				if ($val) {
					$id[$key] = $val;
				} else {
					unset($id[$key]);
				}
			}
			if (count($id)) {
				$id = pwImplode($id);
				if ($md_ifmsg) {
					$query = $db->query("SELECT awardee,level,why FROM pw_medalslogs WHERE id IN($id)");
					while ($rt = $db->fetch_array($query)) {
						$medal = $rt['level'];
						$reason = $rt['why'];
						$message = array(
							'toUser'	=> $rt['awardee'],
							'subject'	=> 'metal_refuse',
							'content'	=> 'metal_refuse_content',
							'other'		=> array(
								'mname'		=> $_MEDALDB[$medal]['name'],
								'windid'	=> $windid,
								'reason'	=> $reason
							)
						);
						pwSendMsg($message);
					}
				}
				$db->update("DELETE FROM pw_medalslogs WHERE id IN($id)");
			} else {
				Showmsg('medal_iderror');
			}
		} else {
			$id = (int)$id;
			!$id && Showmsg('medal_iderror');
			if ($md_ifmsg) {
				$rt = $db->get_one("SELECT awardee,level,why FROM pw_medalslogs WHERE id=".pwEscape($id));
				!$rt && Showmsg('medal_iderror');
				$medal = $rt['level'];
				$reason = $rt['why'];
				$message = array(
					'toUser'	=> $rt['awardee'],
					'subject'	=> 'metal_refuse',
					'content'	=> 'metal_refuse_content',
					'other'		=> array(
						'mname'		=> $_MEDALDB[$medal]['name'],
						'windid'	=> $windid,
						'reason'	=> $reason
					)
				);
				pwSendMsg($message);
			}
			$db->update("DELETE FROM pw_medalslogs WHERE id=".pwEscape($id));
		}
		refreshto("$basename&action=approve",'operate_success');
	} else {
		Showmsg('illegal_request');
	}
} else {
	Showmsg('illegal_request');
}

function updatemedal_list(){
	global $db;
	$query = $db->query("SELECT uid FROM pw_medaluser GROUP BY uid");
	$medaldb = '<?php die;?>0';
	while ($rt = $db->fetch_array($query)) {
		$medaldb .= ','.$rt['uid'];
	}
	writeover(D_P.'data/bbscache/medals_list.php',$medaldb);
}

?>