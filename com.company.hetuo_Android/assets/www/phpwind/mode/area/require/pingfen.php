<?php
!defined('M_P') && exit('Forbidden');
require_once(R_P.'require/credit.php');
require_once R_P . 'require/pingfunc.php';
$mcredit	= $db->get_one('SELECT credit FROM pw_memberinfo WHERE uid='.pwEscape($winduid));
$_G['markset'] = unserialize($_G['markset']);

$creditdb = explode("|",$mcredit['credit']);
$rcreditdb = array();
$rcreditnum = 0;
foreach ($creditdb as $value) {
	$creditvalue = explode("\t",$value);
	$rcreditdb[$creditvalue['2']]['pingdate'] = $creditvalue['0'];
	$rcreditdb[$creditvalue['2']]['pingnum'] = $creditvalue['1'];
	$rcreditdb[$creditvalue['2']]['pingtype'] = $creditvalue['2'];
	$rcreditnum += $creditvalue['1'];
}

$i = 0;
foreach ($_G['markset'] as $key => $value) {
	if ($value['markctype']) {
		if (!$i) {
			$minper = $value['marklimit'][0];
			$maxper = $value['marklimit'][1];
			$maxcredit = $value['maxcredit'];
			if ($rcreditdb[$key]['pingdate'] >= $tdtime) {
				$leavepoint = $jscredit[$key]['leavepoint'] = abs($value['maxcredit'] - $rcreditdb[$key]['pingnum']);
				//$leavepoint == 0 && Showmsg('masigle_nopoint');
			} else {
				$leavepoint = $jscredit[$key]['leavepoint'] = $value['maxcredit'];
			}
			$one = $key;
		}

		$jscredit[$key]['minper'] = $value['marklimit'][0];
		$jscredit[$key]['maxper'] = $value['marklimit'][1];
		$jscredit[$key]['maxcredit'] = $value['maxcredit'];
		if ($rcreditdb[$key]['pingdate'] >= $tdtime) {
			$jscredit[$key]['leavepoint']  = abs($value['maxcredit'] - $rcreditdb[$key]['pingnum']);
			//$jscredit[$key]['leavepoint']  == 0 && Showmsg('masigle_nopoint');
		} else {
			$jscredit[$key]['leavepoint']  = $value['maxcredit'];
		}
		$credittype[] = $key;
		$i++;
	}
}

$mright = array();
if ($winddb['groups']) {
	$gids = array();
	foreach (explode(',',$winddb['groups']) as $key=>$gid) {
		is_numeric($gid) && $gids[] = $gid;
	}
	if ($gids) {
		$gids = pwImplode($gids);
		require_once(R_P.'require/pw_func.php');
		$query = $db->query("SELECT gid,rkey,rvalue FROM pw_permission WHERE uid='0' AND fid='0' AND gid IN($gids) AND rkey IN ('markset','markable') AND type='basic'");
		while ($rt = $db->fetch_array($query)) {
			$mright[$rt['gid']][$rt['rkey']] = $rt['rvalue'];
		}

		foreach ($mright as $key=>$p) {

			if (is_array($p) && $p['markable']) {
				$p['markable'] > $_G['markable'] && $_G['markable'] = $p['markable'];

				$p['markset'] = unserialize($p['markset']);

				if ($p['markset'][$one]['markctype']) {

					is_numeric($p['markset'][$one]['marklimit'][0]) && $p['markset'][$one]['marklimit'][0] < $minper && $minper = $p['markset'][$one]['marklimit'][0];
					is_numeric($p['markset'][$one]['marklimit'][1]) && $p['markset'][$one]['marklimit'][1] > $maxper && $maxper = $p['markset'][$one]['marklimit'][1];
					is_numeric($p['markset'][$one]['maxcredit']) && $p['markset'][$one]['maxcredit'] > $maxcredit && $maxcredit = $p['markset'][$one]['maxcredit'];

					if ($rcreditdb[$one]['pingdate'] >= $tdtime) {
						$leavepoint = $jscredit[$one]['leavepoint'] = abs($maxcredit - $rcreditdb[$one]['pingnum']);
						//$leavepoint == 0 && Showmsg('masigle_nopoint');
					} else {
						$leavepoint = $jscredit[$one]['leavepoint'] = $maxcredit;
					}

				}

				foreach ($credittype as $value) {

					if ($p['markset'][$value]['markctype']) {
						is_numeric($p['markset'][$value]['marklimit'][0]) && $p['markset'][$value]['marklimit'][0] < $jscredit[$value]['minper'] && $jscredit[$value]['minper'] = $p['markset'][$value]['marklimit'][0];
						is_numeric($p['markset'][$value]['marklimit'][1]) && $p['markset'][$value]['marklimit'][1] > $jscredit[$value]['maxper'] && $jscredit[$value]['maxper'] = $p['markset'][$value]['marklimit'][1];
						is_numeric($p['markset'][$value]['maxcredit']) && $p['markset'][$value]['maxcredit'] > $jscredit[$value]['maxcredit'] && $jscredit[$value]['maxcredit'] = $p['markset'][$value]['maxcredit'];

						if ($rcreditdb[$value]['pingdate'] >= $tdtime) {
							$jscredit[$value]['leavepoint']  = abs($jscredit[$value]['maxcredit'] - $rcreditdb[$value]['pingnum']);
							//$jscredit[$value]['leavepoint']  == 0 && Showmsg('masigle_nopoint');
						} else {
							$jscredit[$value]['leavepoint']  = $jscredit[$value]['leavepoint'];
						}

					}
				}
			}
		}
	}
}

$creditcheck = $jscredit;

if ($step == 2) {
	$creditselect = '';
	foreach ($credittype as $key => $cid) {
		if ($creditcheck[$cid]['minper'] && $creditcheck[$cid]['maxper']) {
			if (isset($credit->cType[$cid])) {
				$creditselect .= '<option value="'.$cid.'">'.$credit->cType[$cid].'</option>';
			}
		}
	}

	$reason_sel = '';
	$reason_a = explode("\n",$db_adminreason);
	foreach ($reason_a as $k => $v) {
		if ($v = trim($v)) {
			$reason_sel .= "<option value=\"$v\">$v</option>";
		} else {
			$reason_sel .= "<option value=\"\">-------</option>";
		}
	}
} elseif ($step == 3) {
	InitGP(array('cids','addpoints','ifmsg','atc_content'),'P');
	if (strlen($atc_content) > 100) Showmsg('showping_content_too_long');
	if (count($cids)>5) Showmsg('push_count_too_long');
	foreach ($cids as $key=>$cid) {
		$addpoint = (int)$addpoints[$key];
		if (!in_array($cid,$credittype) || !isset($credit->cType[$cid]) || $addpoint == 0) continue;
		$name = $credit->cType[$cid];
		$unit = $credit->cUnit[$cid];
		foreach ($mright as $key=>$p) {
			if (is_array($p) && $p['markable']) {
				$p['markset'] = unserialize($p['markset']);
				if ($p['markset'][$cid]['markctype']) {
					is_numeric($p['markset'][$cid]['marklimit'][0]) && $p['markset'][$cid]['marklimit'][0] < $_G['markset'][$cid]['marklimit'][0] && $_G['markset'][$cid]['marklimit'][0] = $p['markset'][$cid]['marklimit'][0];
					is_numeric($p['markset'][$cid]['marklimit'][1]) && $p['markset'][$cid]['marklimit'][1] > $_G['markset'][$cid]['marklimit'][1] && $_G['markset'][$cid]['marklimit'][1] = $p['markset'][$cid]['marklimit'][1];
					!$p['markset'][$cid]['markdt'] && $_G['markset'][$cid]['markdt'] = 0;//正负->负 扣除积分权限
				}
			}
		}
		if ($addpoint > $_G['markset'][$cid]['marklimit'][1] || $addpoint < $_G['markset'][$cid]['marklimit'][0]) {
			$GLOBALS['maxper'] = $_G['markset'][$cid]['marklimit'][1];
			$GLOBALS['minper'] = $_G['markset'][$cid]['marklimit'][0];
			Showmsg('masigle_creditlimit');
		}
		if ($mcredit['credit']) {

			$creditdb = explode("|",$mcredit['credit']);
			$rcreditdb = array();
			$rcreditnum = $j = 0;
			foreach ($creditdb as $value) {
				$creditvalue = explode("\t",$value);
				$rcreditdb[$creditvalue['2']]['pingdate'] = $creditvalue['0'];
				$rcreditdb[$creditvalue['2']]['pingnum'] = $creditvalue['1'];
				$rcreditdb[$creditvalue['2']]['pingtype'] = $creditvalue['2'];
				$rcreditnum += $creditvalue['1'];

				if ($creditvalue['2'] == $cid) {
					if ($creditvalue[0] < $tdtime) {
						$creditvalue[0] = $tdtime;
						$creditvalue[1] = abs($addpoint);
						if ($creditvalue[1] > $creditcheck[$creditvalue['2']]['maxcredit']) {
							$GLOBALS['leavepoint'] = $creditcheck[$creditvalue['2']]['maxcredit'];
							Showmsg('masigle_point');
						}
					} else {
						if ($creditvalue[1] + abs($addpoint) > $creditcheck[$creditvalue['2']]['maxcredit']) {
							$GLOBALS['leavepoint'] = $creditcheck[$creditvalue['2']]['maxcredit'];
							Showmsg('masigle_point');
						} else {
							$creditvalue[0] = $timestamp;
							$creditvalue[1]+= abs($addpoint);
						}
					}
					$rcreditdb[$creditvalue['2']]['pingdate'] = $creditvalue['0'];
					$rcreditdb[$creditvalue['2']]['pingnum'] = $creditvalue['1'];
					$rcreditdb[$creditvalue['2']]['pingtype'] = $creditvalue['2'];
					$j ++;
				}
			}

			$newcreditdb = '';
			foreach ($rcreditdb as $value) {
				$newcreditdb .= $newcreditdb ? '|'.implode("\t",$value) : implode("\t",$value);
			}
			if (!$j) {
				$creditvalue[0] = $tdtime;
				$creditvalue[1] = abs($addpoint);
				if ($creditvalue[1] > $_G['markset'][$cid]['maxcredit']) {
					$GLOBALS['leavepoint'] = $_G['markset'][$cid]['maxcredit'];
					Showmsg('masigle_point');
				}
				$newcreditdb = $creditvalue[0]."\t".$creditvalue[1]."\t".$cid;
				$mcredit['credit'] && $newcreditdb .= '|'.$mcredit['credit'];
			}
			$db->update('UPDATE pw_memberinfo SET credit='.pwEscape($newcreditdb,false).' WHERE uid='.pwEscape($winduid));
		} else {
			$creditvalue[0] = $tdtime;
			$creditvalue[1] = abs($addpoint);
			if ($creditvalue[1] > $_G['markset'][$cid]['maxcredit']) {
				$GLOBALS['maxper'] = $_G['markset'][$cid]['marklimit'][1];
				$GLOBALS['minper'] = $_G['markset'][$cid]['marklimit'][0];
				Showmsg('masigle_creditlimit');
			}
			$newcreditdb = $creditvalue[0]."\t".$creditvalue[1]."\t".$cid;

			if (!$mcredit) {

				$db->update("INSERT INTO pw_memberinfo SET " .pwSqlSingle(array('uid'=>$winduid,'credit'=>$newcreditdb),false));
			} elseif (!$mcredit['credit']) {

				$db->update('UPDATE pw_memberinfo SET credit='.pwEscape($newcreditdb,false).' WHERE uid='.pwEscape($winduid));
			}
		}
		!$read['subject'] && $read['subject'] = substrs(strip_tags(convert($read['content'])),35);
		$credit->addLog('credit_showping',array($cid => $addpoint),array(
			'uid'		=> $read['authorid'],
			'username'	=> $read['author'],
			'ip'		=> $onlineip,
			'operator'	=> $windid,
			'tid'		=> $tid,
			'subject'	=> $read['subject'],
			'reason'	=> $atc_content
		));
		$credit->set($read['authorid'], $cid, $addpoint, false);

		$db->update("UPDATE pw_threads SET ifmark=ifmark+".pwEscape($addpoint)." WHERE tid=".pwEscape($tid));
		$rpid = 0;

		$pwSQL = pwSqlSingle(array(
			'fid'	=> $fid,
			'tid'	=> $tid,
			'pid'	=> $rpid,
			'name'	=> $name,
			'point'	=> $addpoint,
			'pinger'=> $windid,
			'record'=> $atc_content,
			'pingdate'=> $timestamp,
		));
		$db->update("INSERT INTO pw_pinglog SET $pwSQL");
		update_markinfo($fid, $tid, $rpid);

		$threadobj = L::loadClass("threads");
		$threadobj->clearTmsgsByThreadId($tid);


		if (!$read['anonymous']) {
			$msg = array(
				'toUser'	=> $read['author'],
				'fromUid'	=> $winduid,
				'fromUser'	=> $windid,
				'subject'	=> 'ping_title',
				'content'	=> 'ping_content',
				'other'		=> array(
					'manager'	=> $windid,
					'fid'		=> $read['fid'],
					'tid'		=> $tid,
					'pid'		=> 'tpc',
					'subject'	=> $read['subject'],
					'postdate'	=> get_date($read['postdate']),
					'forum'		=> strip_tags($foruminfo['name']),
					'affect'    => "$name:$addpoint",
					'admindate'	=> get_date($timestamp),
					'reason'	=> stripslashes($atc_content)
				)
			);
			pwSendMsg($msg);
		}
		if ($gp_gptype == 'system'){
			require_once(R_P.'require/writelog.php');
			$log = array(
				'type'      => 'credit',
				'username1' => $read['author'],
				'username2' => $windid,
				'field1'    => $fid,
				'field2'    => '',
				'field3'    => '',
				'descrip'   => 'credit_descrip',
				'timestamp' => $timestamp,
				'ip'        => $onlineip,
				'tid'		=> $tid,
				'forum'		=> strip_tags($foruminfo['name']),
				'subject'	=> $read['subject'],
				'affect'	=> "$name:$addpoint",
				'reason'	=> $atc_content
			);
			writelog($log);
		}
	}
	if ($ifmsg) {
		$msgdb = array(
			'toUser'	=> $read['author'],
			'subject'	=> 'pushto_title',
			'content'	=> 'pushto_content',
			'other'		=> array(
				'manager'	=> $windid,
				'fid'		=> $read['fid'],
				'tid'		=> $read['tid'],
				'subject'	=> $read['subject'],
				'postdate'	=> get_date($read['postdate']),
				'forum'		=> strip_tags($forum[$fid]['name']),
				'admindate'	=> get_date($timestamp),
				'reason'	=> stripslashes($atc_content)
			)
		);
		pwSendMsg($msgdb);
	}
	$credit->runsql();
}
?>