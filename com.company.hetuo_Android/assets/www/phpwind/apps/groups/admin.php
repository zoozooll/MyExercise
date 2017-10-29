<?php
!function_exists('adminmsg') && exit('Forbidden');

@include_once(D_P.'data/bbscache/o_config.php');

if (empty($action)) {

	if (empty($_POST['step'])) {

		require_once(R_P.'require/credit.php');
		ifcheck($db_groups_open,'groups_open');
		ifcheck($o_groups_gdcheck,'groups_gdcheck');
		ifcheck($o_groups_p_gdcheck,'groups_p_gdcheck');
		ifcheck($o_groups_qcheck,'groups_qcheck');
		ifcheck($o_groups_p_qcheck,'groups_p_qcheck');
		ifcheck($o_remove,'remove');
		ifcheck($o_newcolony,'newcolony');
		ifcheck($o_virement,'virement');
		$moneyname = $credit->cType[$o_moneytype];

		$creategroup = ''; $num = 0;
		foreach ($ltitle as $key => $value) {
			if ($key != 1 && $key != 2 && $key !='6' && $key !='7' && $key !='3') {
				$num++;
				$htm_tr = $num % 4 == 0 ? '' : '';
				$g_checked = strpos($o_groups,",$key,") !== false ? 'checked' : '';
				$creategroup .= "<li><input type=\"checkbox\" name=\"groups[]\" value=\"$key\" $g_checked>$value</li>$htm_tr";
			}
		}
		$creategroup && $creategroup = "<ul class=\"list_A list_120 cc\">$creategroup</ul>";

		!is_array($creditset = unserialize($o_groups_creditset)) && $creditset = array();

		$creditlog = array();
		!is_array($groups_creditlog = unserialize($o_groups_creditlog)) && $groups_creditlog = array();
		foreach ($groups_creditlog as $key => $value) {
			foreach ($value as $k => $v) {
				$creditlog[$key][$k] = 'CHECKED';
			}
		}

		require_once PrintApp('groups');

	} else {

		InitGP(array('config','creditset','creditlog'),'GP',0);
		InitGP(array('groups','groups_open'),'GP',2);

		require_once(R_P.'admin/cache.php');
		setConfig('db_groups_open', $groups_open);
		updatecache_c();

		foreach ($config as $key => $value) {
			switch ($key) {
				case 'moneytype':
					$config[$key] = Char_cv($value);break;
				case 'rate':
					$config[$key] = (double)$value;break;
				default:
					$config[$key] = (int)$value;
			}
		}
		$config['groups'] = is_array($groups) ? ','.implode(',',$groups).',' : '';

		$updatecache = false;

		$config['groups_creditset'] = '';
		if (is_array($creditset) && !empty($creditset)) {
			foreach ($creditset as $key => $value) {
				foreach ($value as $k => $v) {
					$creditset[$key][$k] = $v === '' ? (in_array($key, array('Post','Reply','Delete','Deleterp')) ? '' : 0) : round($v, ($k=='rvrc' ? 1 : 0));
				}
			}
			$config['groups_creditset'] = addslashes(serialize($creditset));
		}

		is_array($creditlog) && !empty($creditlog) && $config['groups_creditlog'] = addslashes(serialize($creditlog));

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
		adminmsg('operate_success',$j_url);
	}

} elseif ($action == 'setting') {

	!is_array($config = $_POST['config']) && $config = array();
	foreach ($config as $key => $value) {
		if ($value) {
			$isint = false;
			if ($_POST['step'] == 'basic') {
				if ($key == 'name' || $key == 'moneytype') {
					$config[$key] = Char_cv($value);
				} elseif ($key == 'rate') {
					$config[$key] = (double)$value;
				} else {
					$isint = true;
				}
			} else {
				$isint = true;
			}
			$isint && $config[$key] = (int)$value;
		}
	}
	if ($_POST['step'] == 'basic') {
		!is_array($groups = $_POST['groups']) && $groups = array();
		$config['groups'] = ','.implode(',',$groups).',';
	}
	$updatecache = false;
	foreach ($config as $key => $value) {
		if (${'cn_'.$key} != $value) {
			$db->pw_update(
				"SELECT hk_name FROM pw_hack WHERE hk_name=" . pwEscape("cn_$key"),
				"UPDATE pw_hack SET hk_value=" . pwEscape($value) . "WHERE hk_name=" . pwEscape("cn_$key"),
				"INSERT INTO pw_hack SET hk_name=" . pwEscape("cn_$key") . ",hk_value=" . pwEscape($value)
			);
			$updatecache = true;
		}
	}
	$j_url = '';
	if ($_POST['step'] == 'updatecache') {
		$updatecache = true;
		$j_url = "$basename&action=cache";
	} elseif ($_POST['step'] == 'photo') {
		$j_url = "$basename&action=photo";
	}
	$updatecache && updatecache_cnc();
	adminmsg('operate_success',$j_url);

} elseif ($action == 'class') {

	$classdb = $isclass = array();
	$query = $db->query("SELECT * FROM pw_cnclass");
	while ($rt = $db->fetch_array($query)) {
		$classdb[$rt['fid']] = $rt;
		if ($rt['ifopen']) {
			$isclass[] = $rt['fid'];
		}
	}

	if (empty($_POST['step'])) {

		$o_classdb = array();
		foreach ($forum as $key => $value) {
			if ($value['type'] == 'forum' && !$value['cms'] && isset($forum[$value['fup']]) && $forum[$value['fup']]['type'] == 'category') {
				$o_classdb[$value['fup']][] = $value['fid'];
			}
		}
		require_once PrintApp('groups');

	} else {

		InitGP(array('selid','cname'));
		empty($selid) && $selid = array();
		empty($isclass) && $isclass = array();
		if ($delclass = array_diff($isclass, $selid)) {
			$db->update("UPDATE pw_cnclass SET ifopen=0,cname='' WHERE fid IN (" . pwImplode($delclass) . ')');
		}
		if ($addclass = array_diff($selid, $isclass)) {
			$pwSQL = array();
			foreach ($addclass as $key => $value) {
				!$cname[$value] && $cname[$value] = strip_tags($forum[$value]['name']);
				$pwSQL[] = array($value,$cname[$value],1);
			}
			$db->update("REPLACE INTO pw_cnclass (fid,cname,ifopen) VALUES " . pwSqlMulti($pwSQL));
		}
		if ($upclass = array_intersect($selid,$isclass)) {
			foreach ($upclass as $key => $value) {
				!$cname[$value] && $cname[$value] = strip_tags($forum[$value]['name']);
				if ($cname[$value] != $classdb[$value]['cname']) {
					$db->update("UPDATE pw_cnclass SET cname=" . pwEscape($cname[$value]) . ' WHERE fid=' . pwEscape($value));
				}
			}
		}
		updatecache_cnc();
		adminmsg('operate_success', "$basename&action=class");

	}
} elseif ($action == 'credit') {
	
	InitGP(array('fid'));
	$f = $db->get_one("SELECT creditset FROM pw_cnclass WHERE fid=" . pwEscape($fid));
	!$f && adminmsg('operate_fail');
	
	require_once(R_P . 'require/credit.php');

	if (empty($_POST['step'])) {
		
		$creditset = unserialize($f['creditset']);
		require_once PrintApp('groups');

	} else {
		
		InitGP(array('creditset'), 'P');
		
		foreach ($creditset as $key => $value) {
			foreach ($value as $k => $v) {
				if (is_numeric($v)) {
					$creditset[$key][$k] = round($v, $k == 'rvrc' ? 1 : 0);
				} else {
					$creditset[$key][$k] = '';
				}
			}
		}
		$creditset = $creditset ? serialize($creditset) : '';
		$db->update("UPDATE pw_cnclass SET creditset=" . pwEscape($creditset) . ' WHERE fid=' . pwEscape($fid));

		adminmsg('operate_success', "$basename&action=credit&fid=$fid");
	}

} elseif ($action == 'colony') {

	!is_array($o_classdb) && $o_classdb = array();

	InitGP(array('cid','cname','admin'));
	$sqladd = $pageadd = '';
	if($cid) {
		$pageadd .= "&cid=$cid";
		$sqladd .= ' classid='.pwEscape($cid);
	}
	if($cname) {
		$pageadd .= '&cname=$cname';
		$cname = str_replace('*','%',$cname);
		$sqladd .= ($sqladd ? ' AND' : '').' cname LIKE '.pwEscape($cname);
	}
	if($admin) {
		$pageadd .= '&admin=$admin';
		$admin = str_replace('*','%',$admin);
		$sqladd .= ($sqladd ? ' AND' : '').' admin LIKE '.pwEscape($admin);
	}

	$options = "<option value=\"0\">不分类</option>";
	foreach ($o_classdb as $key => $value) {
		$options .= "<option value=\"$key\"" . ($key == $cid ? ' selected' : '') . ">$value</option>";
	}
	$sqladd = $sqladd ? ' WHERE '.$sqladd : '';
	$pages = '';
	$db_perpage = 20;
	$colonys = array();
	InitGP(array('page'),'GP',2);
	$page < 1 && $page = 1;
	$id = ($page-1) * $db_perpage;
	$query = $db->query("SELECT id,cname,admin,classid FROM pw_colonys $sqladd " . pwLimit($id,$db_perpage));
	while ($rt = $db->fetch_array($query)) {
		$rt['cname'] = trim($rt['cname']);
		$rt['classname'] = $rt['classid'] ? $o_classdb[$rt['classid']] : '';
		$colonys[] = $rt;
	}
	$db->free_result($query);
	$count = $db->get_value('SELECT COUNT(*) FROM pw_colonys'.$sqladd);
	if ($count > $db_perpage) {
		$pages = numofpage($count,$page,ceil($count/$db_perpage),"$basename&action=colony$pageadd&");
	}
	require_once PrintApp('groups');

} elseif ($action == 'editcolony') {

	require_once(R_P. 'require/credit.php');
	InitGP(array('cyid'));
	$colony = $db->get_one("SELECT * FROM pw_colonys WHERE id=" . pwEscape($cyid));
	!$colony && adminmsg('undefined_action');
	/*
	$g_credits = array();
	$g_all = $credit->cType;
	$query = $db->query("SELECT * FROM pw_credits WHERE type='group' AND id=" . pwEscape($cyid));
	while ($rt = $db->fetch_array($query)) {
		$g_credits[$rt['cid']] = $rt;
		$g_all[$rt['cid']] = $rt['name'];
	}
	*/

	if (empty($_POST['step'])) {
		
		is_array($creditset = unserialize($colony['creditset'])) || $creditset = array();
		$options = '';
		foreach ($o_classdb as $key => $value) {
			$options .= "<option value=\"$key\"" . ($key == $colony['classid'] ? ' selected' : '') . ">$value</option>";
		}
		$ifcheck_0 = $ifcheck_1 = $ifcheck_2 = $ifopen_Y = $ifopen_N = $albumopen_Y = $albumopen_N = '';
		${'ifcheck_'.$colony['ifcheck']} = 'selected';
		${'ifopen_'.($colony['ifopen'] ? 'Y' : 'N')} = 'checked';
		${'albumopen_'.($colony['albumopen'] ? 'Y' : 'N')} = 'checked';
		require_once PrintApp('groups');

	} else {

		$basename .= "&action=$action&cyid=$cyid";
		InitGP(array('cname','annouce','descrip','admin'/*,'credit_name','credit_unit','credit_desc','cdiy_name','cdiy_unit','cdiy_desc', 'creditset'*/),'P');
		!$cname && Showmsg('colony_emptyname');
		strlen($cname) > 20 && Showmsg('colony_cnamelimit');
		strlen($descrip) > 255 && Showmsg('colony_descriplimit');

		require_once(R_P.'require/bbscode.php');
		$wordsfb = L::loadClass('FilterUtil');
		foreach (array($cname, $annouce, $descrip) as $key => $value) {
			if (($banword = $wordsfb->comprise($value)) !== false) {
				Showmsg('content_wordsfb');
			}
		}
		strlen($annouce) > 255 && Showmsg('colony_annoucelimit');
		$annouce = explode("\n",$annouce,5);
		end($annouce);
		$annouce[key($annouce)] = str_replace(array("\r","\n"),'',current($annouce));
		$annouce = implode("\r\n",$annouce);

		if ($colony['cname'] != stripcslashes($cname) && $db->get_value("SELECT id FROM pw_colonys WHERE cname=" . pwEscape($cname))) {
			Showmsg('colony_samename');
		}
		InitGP(array('cid','ifcheck','ifopen','albumopen'), 'P', 2);
		/*
		if (is_array($creditset) && !empty($creditset)) {
			foreach ($creditset as $key => $value) {
				foreach ($value as $k => $v) {
					if (is_numeric($v)) {
						$creditset[$key][$k] = round($v,($k == 'rvrc' ? 1 : 0));
					} else {
						$creditset[$key][$k] = '';
					}
				}
			}
			$creditset = serialize($creditset);
		} else {
			$creditset = '';
		}
		$delcid = array();
		foreach ($g_credits as $key => $value) {
			if (!isset($credit_name[$key])) {
				$delcid[] = $key;
			} elseif ($credit_name[$key] && ($value['name'] <> $credit_name[$key] || $value['unit'] <> $credit_unit[$key] || $value['description'] <> $credit_desc[$key])) {
				$db->update("UPDATE pw_credits SET " . pwSqlSingle(array(
						'name'			=> $credit_name[$key],
						'unit'			=> $credit_unit[$key],
						'description'	=> $credit_desc[$key]
					))
				. " WHERE cid=" . pwEscape($key));
			}
		}
		if (!empty($delcid)) {
			$delcid = pwImplode($delcid);
			$db->update("DELETE FROM pw_credits WHERE cid IN($delcid)");
			$db->update("DELETE FROM pw_membercredit WHERE cid IN($delcid)");
		}
		if (is_array($cdiy_name)) {
			$pwSQL = array();
			foreach ($cdiy_name as $key => $value) {
				if ($value) {
					$pwSQL[] = array($value, $cdiy_unit[$key], $cdiy_desc[$key], 'group', $cyid);
				}
			}
			$pwSQL && $db->update("INSERT INTO pw_credits (name,unit,description,type,id) VALUES " . pwSqlMulti($pwSQL));
		}
		*/

		$pwSQL = array(
			'cname'		=> $cname,
			'ifcheck'	=> $ifcheck,
			'albumopen'	=> $albumopen,
			'annouce'	=> $annouce,
			'ifopen'	=> $ifopen,
			'descrip'	=> $descrip,
			//'creditset'	=> $creditset
		);
		if ($admin != $colony['admin']) {
			$rt = $db->get_one("SELECT m.uid,c.id AS ifcyer FROM pw_members m LEFT JOIN pw_cmembers c ON m.uid=c.uid AND c.colonyid=" . pwEscape($cyid) . " WHERE m.username=" . pwEscape($admin));
			if (empty($rt)) {
				$errorname = $admin;
				adminmsg('user_not_exists');
			}
			if ($rt['ifcyer']) {
				$db->update("UPDATE pw_cmembers SET ifadmin=1 WHERE colonyid=" . pwEscape($cyid) . ' AND uid=' . pwEscape($rt['uid']));
			} else {
				$db->update("INSERT INTO pw_cmembers SET " . pwSqlSingle(array(
					'uid' => $rt['uid'],
					'username' => $admin,
					'ifadmin' => 1,
					'colonyid' => $cyid,
					'addtime' => $timestamp
				)));
			}
			$pwSQL['admin'] = $admin;
		}
		if ($cid != $colony['classid'] && isset($o_classdb[$cid])) {
			$db->update("UPDATE pw_argument a LEFT JOIN pw_threads t ON a.tid=t.tid SET t.fid=" . pwEscape($cid) . " WHERE a.cyid=" . pwEscape($cyid));
			$db->update("UPDATE pw_argument a LEFT JOIN pw_posts p ON a.tid=p.tid SET p.fid=" . pwEscape($cid) . " WHERE a.cyid=" . pwEscape($cyid));
			if ($db_plist && count($db_plist)>1) {
				foreach ($db_plist as $key => $value) {
					if ($key == 0) continue;
					$pw_posts = GetPtable($key);
					$db->update("UPDATE pw_argument a LEFT JOIN $pw_posts p ON a.tid=p.tid SET p.fid=" . pwEscape($cid) . " WHERE a.cyid=" . pwEscape($cyid));
				}
			}
			//分类的统计数更新
			$db->update("UPDATE pw_cnclass SET cnsum=cnsum-1 WHERE fid=".pwEscape($colony['classid'])." AND cnsum>0");
			$db->update("UPDATE pw_cnclass SET cnsum=cnsum+1 WHERE fid=".pwEscape($cid));

			require_once(R_P . 'require/updateforum.php');
			updateforum($cid);
			updateforum($colony['classid']);
			$pwSQL['classid'] = $cid;
		}
		$db->update("UPDATE pw_colonys SET " . pwSqlSingle($pwSQL) . ' WHERE id=' . pwEscape($cyid));
		

		adminmsg('operate_success',"$basename&action=editcolony");
	}
} elseif ($action == 'delcolony') {

	InitGP(array('cyid'),'',2);
	$rt = $db->get_one("SELECT classid,cnimg FROM pw_colonys WHERE id=" . pwEscape($cyid));
	if (!empty($rt)) {
		Delcnimg($rt['cnimg']);
		pwFtpClose($ftp);
		//updateUserAppNum($rt['uid'],'group','recount');
		$db->update("UPDATE pw_cmembers a LEFT JOIN pw_ouserdata o ON a.uid=o.uid SET o.groupnum=o.groupnum-1 WHERE a.colonyid=" . pwEscape($cyid) . ' AND o.groupnum>0');
		$db->update("DELETE FROM pw_argument WHERE cyid=" . pwEscape($cyid));
		$db->update("DELETE FROM pw_cmembers WHERE colonyid=" . pwEscape($cyid));
		$db->update("DELETE FROM pw_colonys  WHERE id=" . pwEscape($cyid));
		$db->update("UPDATE pw_cnclass SET cnsum=cnsum-1 WHERE fid=" . pwEscape($rt['classid']) . ' AND cnsum>0');
	}
	adminmsg('operate_success',"$basename&action=colony");

} elseif ($action == 'log') {

	if ($_POST['step'] != 'del') {

		require_once GetLang('logtype');
		InitGP(array('keyword','page'));
		$db_perpage = 20;
		$logdb = array();
		$pages = $sqladd = $addpages = '';
		if ($keyword) {
			$sqladd = " AND descrip LIKE ".pwEscape("%$keyword%");
			$addpages = "&keyword=".rawurlencode($keyword);
		}
		(int)$page<1 && $page = 1;
		$id = ($page-1)*$db_perpage;
		$query = $db->query("SELECT id,type,field2,field3,username1,timestamp,descrip FROM pw_forumlog WHERE type LIKE 'cy\_%' $sqladd".pwLimit($id,$db_perpage));
		while ($rt = $db->fetch_array($query)) {
			$rt['timestamp'] = get_date($rt['timestamp']);
			$rt['descrip'] = str_replace(array('[b]','[/b]'),array('<b>','</b>'),$rt['descrip']);
			$logdb[] = $rt;
		}
		$db->free_result($query);
		$count = $db->get_value("SELECT COUNT(*) FROM pw_forumlog WHERE type LIKE 'cy\_%' $sqladd");
		if ($count > $db_perpage) {
			require_once(R_P.'require/forum.php');
			$pages = numofpage($count,$page,ceil($count/$db_perpage),"$basename&action=log$addpages&");
		}
		require_once PrintApp('groups');

	} else {

		InitGP(array('selid'),'P',1);
		if (!($selid = checkselid($selid))) {
			$basename = 'javascript:history.go(-1);';
			adminmsg('operate_error');
		}
		$selid && $db->update("DELETE FROM pw_forumlog WHERE type LIKE 'cy\_%' AND id IN($selid)");
		adminmsg('operate_success',"$basename&action=log");
	}
} elseif ($action == 'cache') {

	if (empty($_POST['step'])) {

		require_once PrintApp('groups');

	} elseif ($_POST['step'] == 'updatecache') {

		$db->update("UPDATE pw_cnclass SET cnsum='0'");
		$query = $db->query("SELECT classid,COUNT(*) AS sum FROM pw_colonys WHERE classid>0 GROUP BY classid");
		while ($rt = $db->fetch_array($query)) {
			$db->update("UPDATE pw_cnclass SET cnsum=" . pwEscape($rt['sum']) . ' WHERE fid=' . pwEscape($rt['classid']));
		}
		$j_url = "$basename&action=cache";
		adminmsg('operate_success',$j_url);

	} elseif ($_POST['step'] == 'delcolony') {

		$query = $db->query("SELECT id,cnimg FROM pw_colonys WHERE classid<1");
		while ($rt = $db->fetch_array($query,MYSQL_NUM)) {
			Delcnimg($rt[1]);
			$db->update("UPDATE pw_cmembers a LEFT JOIN pw_ouserdata o ON a.uid=o.uid SET o.groupnum=o.groupnum-1 WHERE a.colonyid=" . pwEscape($rt[0],false) . ' AND o.groupnum>0');
			$db->update("DELETE FROM pw_argument WHERE cyid=" . pwEscape($rt[0],false));
			$db->update("DELETE FROM pw_cmembers WHERE colonyid=" . pwEscape($rt[0],false));
			$db->update("DELETE FROM pw_colonys  WHERE id=" . pwEscape($rt[0],false));
		}
		pwFtpClose($ftp);
		adminmsg('operate_success',"$basename&action=cache");
	}
} elseif ($action == 'argument') {

	InitGP(array('step'));

	if ($step == 'list') {

		InitGP(array('page','cid','author','ckauthor','keyword','ktype','ttype','ckkeyword','postdate_s','postdate_e','orderby','sc','perpage','cname'));
		
		$addpage = $sqltab = $sql = '';

		$tpre = 't';
		if ($ttype == '1') {
			InitGP(array('ttable'));
			$sqltab = 'pw_threads t';
			$pw_tmsgs = 'pw_tmsgs' . $ttable;
			$tpre = 'tm';
			$addpage .= "ttable=$ttable&";
		} else {
			InitGP(array('ptable'));
			$sqltab = GetPtable($ptable) . ' t';
		}
		if ((int)$cid > 0) {
			$sql .= " AND t.fid=" . pwEscape($cid);
			$addpage .= "cid=$cid&";
		}
		if ($cname){
			$addpage .= "cname=$cname&";
			$sql .= " AND c.cname LIKE " . pwEscape(str_replace('*', '%', $cname));
		}
		if ($author) {
			$addpage .= "author=$author&";
			if ($ckauthor) {
				$addpage .= "ckauthor=$ckauthor&";
				$u_sql = "username=" . pwEscape($author);
			} else {
				$author = str_replace('*', '%', $author);
				$u_sql = "username LIKE " . pwEscape($author);
			}
			$authorids = array();
			$query = $db->query("SELECT uid FROM pw_members WHERE $u_sql LIMIT 30");
			while ($rt = $db->fetch_array($query)) {
				$authorids[] = $rt['uid'];
			}
			if (!$authorids) {
				adminmsg('author_nofind', $basename . '&action=argument');
			}
			$sql .= " AND t.authorid IN(" . pwImplode($authorids) . ")";
		}
		if ($keyword) {
			$addpage .= "keyword=$keyword&ktype=$ktype&ckkeyword=$ckkeyword&";

			if ($ckkeyword) {
				$k_sql = " = " . pwEscape($keyword);
			} else {
				$k_sql = " LIKE " . pwEscape(str_replace('*', '%', $keyword));
			}
			if ($ktype == 'subject') {
				$sql .= " AND t.subject" . $k_sql;
			} else {
				$ttype == '1' && $sqltab .= " LEFT JOIN $pw_tmsgs tm ON t.tid=tm.tid";
				$sql .= " AND {$tpre}.content" . $k_sql;
			}
		}
		if ($postdate_s) {
			!is_numeric($postdate_s) && $postdate_s = PwStrtoTime($postdate_s);
			$sql .= " AND t.postdate>".pwEscape($postdate_s);
			$addpage .= "postdate_s=$postdate_s&";
		}
		if ($postdate_e) {
			!is_numeric($postdate_e) && $postdate_e = PwStrtoTime($postdate_e);
			$sql .= " AND t.postdate<".pwEscape($postdate_e);
			$addpage .= "postdate_e=$postdate_e&";
		}
		$sql_orderby = ($orderby == 'postdate') ? 'ORDER BY t.postdate' : 'ORDER BY t.authorid';
		$sc != 'ASC' && $sc = 'DESC';
		!$perpage && $perpage = $db_perpage;
		(int)$page<1 && $page = 1;
		$limit = pwLimit(($page-1)*$perpage,$perpage);

		$query = $db->query("SELECT t.*,a.*,c.cname FROM $sqltab LEFT JOIN pw_argument a ON t.tid=a.tid LEFT JOIN pw_colonys c ON a.cyid=c.id WHERE 1 $sql AND a.tid IS NOT NULL $sql_orderby $sc $limit");
		$argumentdb = array();
		while ($rt = $db->fetch_array($query)) {
			$rt['delid'] = isset($rt['pid']) ? $rt['pid'] : $rt['tid'];
			!$rt['subject'] && $rt['subject'] = substrs($rt['content'], 30);
			$rt['postdate'] = get_date($rt['postdate'],'Y-m-d');
			$argumentdb[] = $rt;
		}
		$db->free_result($query);
		@extract($db->get_one("SELECT COUNT(*) AS count FROM $sqltab LEFT JOIN pw_argument a ON t.tid=a.tid AND a.tid IS NOT NULL LEFT JOIN pw_colonys c ON a.cyid=c.id WHERE 1 $sql AND a.tid IS NOT NULL"));
		if ($count > $perpage) {
			$pages = numofpage($count,$page,ceil($count/$perpage), "$basename&action=argument&step=list&ttype=$ttype&orderby=$orderby&sc=$sc&perpage=$perpage&$addpage");
		}
	} elseif ($step == 'delete') {

		InitGP(array('ttype', 'delid', 'ttable', 'ptable'));
		require_once(R_P.'require/updateforum.php');

		if ($ttype == '1') {

			!$delid && adminmsg('operate_error');
			$pw_tmsgs = 'pw_tmsgs' . $ttable;
			$fidarray = $delaids = $specialdb = array();
			$delids = pwImplode($delid);
			/**
			* 删除帖子
			*/
			$db_guestread && require_once(R_P.'require/guestfunc.php');
			$ptable_a = $delnum = array();
			$query = $db->query("SELECT t.tid,t.fid,t.authorid,t.replies,t.postdate,t.special,t.ptable,tm.aid,t.ifupload FROM pw_threads t LEFT JOIN $pw_tmsgs tm ON tm.tid=t.tid WHERE t.tid IN($delids)");
			while (@extract($db->fetch_array($query))) {
				if (!in_array($fid, $fidarray)) {
					$fidarray[] = $fid;
				}
				$delnum[$authorid]++;
				$ptable_a[$ptable] = 1;
				if ($aid) {
					$attachs = unserialize(stripslashes($aid));
					foreach ($attachs as $key => $value) {
						is_numeric($key) && $delaids[] = $key;
						P_unlink("$attachdir/$value[attachurl]");
						$value['ifthumb'] && P_unlink("$attachdir/thumb/$value[attachurl]");
					}
				}
				switch ($special) {
					case 1:
					case 2:
					case 3:
					case 4:
						$specialdb[$special][] = $tid;break;
				}
				$pw_posts = GetPtable($ptable);
				if ($ifupload) {
					$query2 = $db->query("SELECT aid FROM $pw_posts WHERE tid=".pwEscape($tid));
					while (@extract($db->fetch_array($query2))) {
						if ($aid) {
							$attachs = unserialize(stripslashes($aid));
							foreach ($attachs as $key => $value) {
								is_numeric($key) && $delaids[] = $key;
								P_unlink("$attachdir/$value[attachurl]");
								$value['ifthumb'] && P_unlink("$attachdir/thumb/$value[attachurl]");
							}
						}
					}
				}
				$htmurl = $db_htmdir.'/'.$fid.'/'.date('ym',$postdate).'/'.$tid.'.html';
				if (file_exists(R_P.$htmurl)) {
					P_unlink(R_P.$htmurl);
				}
				$db_guestread && clearguestcache($tid,$replies);

				//统计用户的回复数
				$query3 = $db->query("SELECT authorid FROM $pw_posts WHERE tid=".pwEscape($tid));
				while ($rt3 = $db->fetch_array($query3)) {
					$delnum[$rt3['authorid']]++;
				}
			}
			if (isset($specialdb[1])) {
				$pollids = pwImplode($specialdb[1]);
				$db->update("DELETE FROM pw_polls WHERE tid IN($pollids)");
			}
			if (isset($specialdb[2])) {
				$actids = pwImplode($specialdb[2]);
				$db->update("DELETE FROM pw_activity WHERE tid IN($actids)");
				$db->update("DELETE FROM pw_actmember WHERE actid IN($actids)");
			}
			if (isset($specialdb[3])) {
				$rewids = pwImplode($specialdb[3]);
				$db->update("DELETE FROM pw_reward WHERE tid IN($rewids)");
			}
			if (isset($specialdb[4])) {
				$tradeids = pwImplode($specialdb[4]);
				$db->update("DELETE FROM pw_trade WHERE tid IN($tradeids)");
			}
			if ($delaids) {
				$delaids = pwImplode($delaids);
				$db->update("DELETE FROM pw_attachs WHERE aid IN($delaids)");
			}

			# $db->update("DELETE FROM pw_threads WHERE tid IN ($delids)");
			# ThreadManager
                        $threadManager = L::loadClass("threadmanager");
			$threadManager->deleteByThreadIds($fid,$delids);

			$db->update("DELETE FROM pw_argument WHERE tid IN($delids)");

			foreach ($ptable_a as $key => $val) {
				$pw_posts = GetPtable($key);
				$db->update("DELETE FROM $pw_posts WHERE tid IN ($delids)");
			}
			$db->update("DELETE FROM $pw_tmsgs WHERE tid IN ($delids)");
			delete_tag($delids);
			/**
			* 数据更新
			*/
			foreach ($fidarray as $fid) {
				updateforum($fid);
			}
			foreach ($delnum as $key => $value){
				$db->update("UPDATE pw_memberdata SET postnum=postnum-".pwEscape($value)."WHERE uid=".pwEscape($key));
			}
			P_unlink(D_P.'data/bbscache/c_cache.php');

		} else {

			!$delid && adminmsg('operate_error');
			$pw_posts = GetPtable($ptable);
			$fidarray = $tidarray = $delnum = $delaids = array();
			$delids = pwImplode($delid);

			$query = $db->query("SELECT aid,tid,fid,authorid FROM $pw_posts WHERE pid IN ($delids)");
			while (@extract($db->fetch_array($query))) {
				$fidarray[$fid]	= 1;
				$tidarray[]		= $tid;
				if ($aid) {
					$attachs = unserialize(stripslashes($aid));
					foreach ($attachs as $key => $value) {
						is_numeric($key) && $delaids[] = $key;
						P_unlink("$attachdir/$value[attachurl]");
						$value['ifthumb'] && P_unlink("$attachdir/thumb/$value[attachurl]");
					}
				}
				$delnum[$authorid]++;
			}
			/**
			* 删除帖子
			*/
			if ($tidarray) {
				$dtids = array_unique($tidarray);
				$query = $db->query("SELECT tid,fid,postdate,ifupload FROM pw_threads WHERE tid IN(" . pwImplode($dtids) . ")");
				while (@extract($db->fetch_array($query))) {
					$htmurl = $db_htmdir.'/'.$fid.'/'.date('ym',$postdate).'/'.$tid.'.html';
					if (file_exists(R_P . $htmurl)) {
						P_unlink(R_P . $htmurl);
					}
				}
			}
			if ($delaids) {
				$delaids = pwImplode($delaids);
				$db->update("DELETE FROM pw_attachs WHERE aid IN($delaids)");
			}
			$db->update("DELETE FROM $pw_posts WHERE pid IN ($delids)");

			$tidarray = array_count_values($tidarray);
			foreach ($tidarray as $key => $value) {
				$db->update("UPDATE pw_threads SET replies=replies-".pwEscape($value)." WHERE tid=" . pwEscape($key));
			}
			/**
			* 数据更新
			*/
			foreach ($fidarray as $fid => $v) {
				updateforum($fid);
			}
			foreach ($delnum as $key => $value){
				$db->update("UPDATE pw_memberdata SET postnum=postnum-" . pwEscape($value) . " WHERE uid=" . pwEscape($key));
			}
			P_unlink(D_P.'data/bbscache/c_cache.php');
		}
		adminmsg('operate_success',"$basename&action=argument&");

	} else {

		$t_table = $p_table = '';
		if ($db_tlist) {
			$t_list = $db_tlist;
			$t_table = '<select name="ttable">';
			foreach ($t_list as $key => $value) {
				$name = !empty($value['2']) ? $value['2'] : ($key == 0 ? 'tmsgs' : 'tmsgs'.$key);
				$t_table .= "<option value=\"$key\">$name</option>";
			}
			$t_table .= '</select>';
		}
		if ($db_plist && count($db_plist)>1) {
			$p_table = "<select name=\"ptable\">";
			foreach ($db_plist as $key => $val) {
				$name = $val ? $val : ($key != 0 ? getLangInfo('other','posttable').$key : getLangInfo('other','posttable'));
				$p_table .= "<option value=\"$key\"" . ($value == $db_ptable ? ' selected' : '') . ">$name</option>";
			}
			$p_table .= '</select>';
		}
		empty($o_classdb) && $o_classdb = array();
		foreach ($o_classdb as $key => $value) {
			$classoption .= '<option value="'.$key.'">'.$value.'</option>';
		}
	}
	require_once PrintApp('groups');

} elseif ($action == 'album') {

	if ($job == 'list') {

		InitGP(array('aname','owner','cid','crtime_s','crtime_e','lasttime_s','lasttime_e','private','lines','orderway','ordertype','page','cname'));
//		if (empty($aname) && empty($owner) && empty($cid) && empty($crtime_s) && empty($crtime_e) && empty($lasttime_s) && empty($lasttime_e) && $private == '-1' && empty($cname)) {
//			adminmsg('noenough_condition',"$basename&action=album");
//		}
		$encode_aname = rawurlencode($aname);
		$encode_owner = rawurlencode($owner);
		$crtime_s && !is_numeric($crtime_s) && $crtime_s = PwStrtoTime($crtime_s);
		$crtime_e && !is_numeric($crtime_e) && $crtime_e = PwStrtoTime($crtime_e);
		$lasttime_s && !is_numeric($lasttime_s) && $lasttime_s = PwStrtoTime($lasttime_s);
		$lasttime_e && !is_numeric($lasttime_e) && $lasttime_e = PwStrtoTime($lasttime_e);
		$sql = "c.atype='1'";
		$sqltab = $urladd = '';
		if ($cname) {
			$cname = str_replace('*','%',$cname);
			$sql .= ' AND cl.cname LIKE '.pwEscape($cname);
			$urladd .= '&cname='.rawurlencode($cname);
		}
		if ($aname) {
			$aname = str_replace('*','%',$aname);
			$sql .= ' AND c.aname LIKE '.pwEscape($aname);
			$urladd .= '&aname='.rawurlencode($aname);
		}
		if ($owner) {
			$owner = str_replace('*','%',$owner);
			$sql .= ' AND c.owner LIKE '.pwEscape($owner);
			$urladd .= '&owner='.rawurlencode($owner);
		}
		if ($cid) {
			$sql .= ' AND cl.classid LIKE '.pwEscape($cid);
			$urladd .= '&cid='.rawurlencode($cid);
		}
		if ($crtime_s) {
			$sql .= ' AND c.crtime>='.pwEscape($crtime_s);
			$urladd .= "&crtime_s=$crtime_s";
		}
		if ($crtime_e) {
			$sql .= ' AND c.crtime<='.pwEscape($crtime_e);
			$urladd .= "&crtime_e=$crtime_e";
		}
		if ($lasttime_s) {
			$sql .= ' AND c.lasttime>='.pwEscape($lasttime_s);
			$urladd .= "&lasttime_s=$lasttime_s";
		}
		if ($lasttime_e) {
			$sql .= ' AND c.lasttime<='.pwEscape($lasttime_e);
			$urladd .= "&lasttime_e=$lasttime_e";
		}
		if ($private > -1) {
			$sql .= ' AND c.private='.pwEscape($private);
			$urladd .= "&private=$private";
		}
		$sql_orderway = $orderway == 'crtime' ? 'c.crtime' : 'c.lasttime';
		$ordertype = $ordertype == 'asc' ? 'asc' : 'desc';
		$urladd .= "&orderway=$orderway&ordertype=$ordertype&lines=$lines";
		$count = $db->get_value("SELECT COUNT(*) AS count FROM pw_cnalbum c LEFT JOIN pw_colonys cl ON c.ownerid=cl.id WHERE $sql");
		empty($count) && adminmsg('album_not_exist',"$basename&action=album");
		!is_numeric($lines) && $lines=30;
		$page < 1 && $page = 1;
		$numofpage = ceil($count/$lines);
		if ($numofpage && $page > $numofpage) {
			$page = $numofpage;
		}
		$pages = numofpage($count,$page,$numofpage,"$basename&action=$action&job=list$urladd&");
		$start = ($page-1)*$lines;
		$limit = pwLimit($start,$lines);
		$query = $db->query("SELECT c.aid,c.aname,c.private,c.ownerid,c.owner,c.photonum,c.lasttime,c.lastpid,c.crtime,cl.cname,cl.admin FROM pw_cnalbum c LEFT JOIN pw_colonys cl ON c.ownerid=cl.id WHERE $sql ORDER BY $sql_orderway $ordertype ".$limit);
		while ($rt = $db->fetch_array($query)) {
			$rt['s_aname'] = substrs($rt['aname'],30);
			$rt['lasttime'] = $rt['lasttime'] ? get_date($rt['lasttime']) : '-';
			$rt['crtime'] 	= $rt['crtime'] ? get_date($rt['crtime']) : '-';
			$albumdb[] = $rt;
		}

		require_once PrintApp('groups');

	} elseif ($job == 'delete') {

		InitGP(array('selid','aname','owner','cid','crtime_s','crtime_e','lasttime_s','lasttime_e','private','lines','orderway','ordertype','cname'));
		empty($selid) && adminmsg("no_album_selid");
		require_once(R_P . 'require/app_core.php');

		$query = $db->query("SELECT ownerid,COUNT(*) AS sum FROM pw_cnalbum WHERE aid IN(" . pwImplode($selid) . ") AND atype='1' GROUP BY ownerid");
		while ($rt = $db->fetch_array($query)) {
			$db->update("UPDATE pw_colonys SET albumnum=albumnum-'$rt[sum]' WHERE id=" . pwEscape($rt['ownerid']));
		}

		foreach ($selid as $key => $aid) {
			$query = $db->query("SELECT cn.pid,cn.path,cn.ifthumb,ca.ownerid FROM pw_cnphoto cn LEFT JOIN pw_cnalbum ca ON cn.aid=ca.aid WHERE cn.aid=" . pwEscape($aid));
			if (($num = $db->num_rows($query)) > 0) {
				$affected_rows = 0;
				while ($rt = $db->fetch_array($query)) {
					$uids[] = $rt['ownerid'];
					pwDelatt($rt['path'], $db_ifftp);
					if ($rt['ifthumb']) {
						$lastpos = strrpos($rt['path'],'/') + 1;
						pwDelatt(substr($rt['path'], 0, $lastpos) . 's_' . substr($rt['path'], $lastpos), $db_ifftp);
					}
					$affected_rows += delAppAction('photo',$rt['pid'])+1;//TODO 效率？
				}
				pwFtpClose($ftp);
				countPosts("-$affected_rows");
			}
		}
		$db->update("DELETE FROM pw_cnphoto WHERE aid IN(" . pwImplode($selid) . ')');
		$db->update("DELETE FROM pw_cnalbum WHERE aid IN(" . pwImplode($selid) . ')');
		$uids = array_unique($uids);
		updateUserAppNum($uids,'photo','recount');
		adminmsg('operate_success',"$basename&action=album&job=list&aname=" . rawurlencode($aname). "&cname=".rawurlencode($cname)."&owner=" .rawurlencode($owner). "&cid=$cid&crtime_s=$crtime_s&crtime_e=$crtime_e&lasttime_s=$lasttime_s&lasttime_e=$lasttime_e&private=$private&lines=$lines&orderway=$orderway&ordertype=$ordertype&page=$page&");

	} elseif ($job == 'edit') {

		InitGP(array('aid'));
		$album = $db->get_one("SELECT aid,aname,aintro,private FROM pw_cnalbum WHERE aid=".pwEscape($aid));
		empty($album) && Showmsg('album_not_exist',"$basename&action=albums");

		if (empty($_POST['step'])) {

			InitGP(array('cname','aname','owner','cid','crtime_s','crtime_e','lasttime_s','lasttime_e','private','lines','orderway','ordertype','page'));
			${'private_'.$album['private']} = 'selected';
			require_once PrintApp('groups');

		} else {

			InitGP(array('aname','aintro','private'));
			InitGP(array('url_cname','url_aname','url_owner','url_cid','url_crtime_s','url_crtime_e','url_lasttime_s','url_lasttime_e','url_private','url_lines','url_orderway','url_ordertype','url_page'));
			$db->update("UPDATE pw_cnalbum SET ".pwSqlSingle(array('aname' => $aname,'aintro' => $aintro, 'private' => $private))." WHERE aid=".pwEscape($aid));
			adminmsg('operate_success',"$basename&action=album&job=list&&cname=".rawurlencode($url_cname)."&aname=".rawurlencode($url_aname)."&owner=".rawurlencode($url_owner)."&cid=$url_cid&crtime_s=$url_crtime_s&crtime_e=$url_crtime_e&lasttime_s=$url_lasttime_s&lasttime_e=$url_lasttime_e&private=$url_private&lines=$url_lines&orderway=$url_orderway&ordertype=$url_ordertype&page=$url_page&");
		}
	}

	require_once PrintApp('groups');

} elseif ($action == 'photos') {

	if ($job == 'list') {

		require_once(R_P . 'require/app_core.php');
		InitGP(array('cid','cname','aid','aname','uploader','pintro','uptime_s','uptime_e','orderway','ordertype','lines','page'));
//		if (empty($cid) && empty($cname) && empty($aid) && empty($aname) && empty($uploader) && empty($pintro) && empty($uptime_s) && empty($uptime_e)) {
//			adminmsg('noenough_condition',"$basename&action=photos");
//		}
		$uptime_s && $uptime_s = PwStrtoTime($uptime_s);
		$uptime_e && $uptime_e = PwStrtoTime($uptime_e);

		$urladd = '';
		$sql = "ca.atype='1'";

		if	($cid) {
			$sql .= ' AND c.classid='.pwEscape($cid);
			$urladd .= '&cid='.$cid;
		}

		if ($cname) {
			$urladd .= '&cname='.rawurlencode($cname);
			$cname = str_replace('*','%',$cname);
			$sql .= ' AND c.cname LIKE '.pwEscape($cname);
		}

		if ($aid) {
			$sql .= ' AND ca.aid ='.pwEscape($aid);
			$urladd .= '&aid='.$aid;
		}
		if ($aname) {
			$urladd .= '&aname='.rawurlencode($aname);
			$aname = str_replace('*','%',$aname);
			$sql .= ' AND ca.aname LIKE '.pwEscape($aname);
		}
		if ($uploader) {
			$uploader = str_replace('*','%',$uploader);
			$sql .= ' AND cp.uploader LIKE '.pwEscape($uploader);
			$urladd .= '&uploader='.rawurlencode($uploader);
		}
		if ($pintro) {
			$pintro = str_replace('*','%',$pintro);
			$sql .= ' AND cp.pintro LIKE '.pwEscape($pintro);
			$urladd .= '&pintro='.rawurlencode($pintro);
		}
		if ($uptime_s) {
			$sql .= ' AND cp.uptime>='.pwEscape($uptime_s);
			$urladd .= "&uptime_s=$uptime_s";
		}
		if ($uptime_e) {
			$uptime_e_sql = $uptime_e + 24*3600;
			$sql .= ' AND cp.uptime<='.pwEscape($uptime_e_sql);
			$urladd .= "&uptime_e=$uptime_e";
		}
		switch ($orderway) {
			case 'uptime' :
				$sql_orderway = 'cp.uptime';break;
			case 'hits' :
				$sql_orderway = 'cp.hits';break;
			case 'c_num' :
				$sql_orderway = 'cp.c_num';break;
			default:
				$sql_orderway = '';break;
		}

		$ordertype = $ordertype == 'asc' ? 'asc' : 'desc';
		$sqladd = $sql_orderway ? "ORDER BY $sql_orderway $ordertype" : '';
		$urladd .= $sql_orderway ? "&orderway=$orderway&ordertype=$ordertype" : '';
		$count = $db->get_value("SELECT COUNT(*) AS count FROM pw_cnphoto cp LEFT JOIN pw_cnalbum ca ON cp.aid=ca.aid LEFT JOIN pw_colonys c ON ca.ownerid=c.id WHERE $sql");
		empty($count) && adminmsg('no_photos',"$basename&action=photos");
		!is_numeric($lines) && $lines=30;
		$page < 1 && $page = 1;
		$numofpage = ceil($count/$lines);
		if ($numofpage && $page > $numofpage) {
			$page = $numofpage;
		}
		$pages = numofpage($count,$page,$numofpage,"$basename&action=$action&job=list&lines=$lines$urladd&");
		$start = ($page-1)*$lines;
		$limit = pwLimit($start,$lines);
		$query = $db->query("SELECT cp.pid,cp.aid,cp.path,cp.uploader,cp.uptime,cp.ifthumb,cp.hits,cp.c_num,ca.aname,ca.ownerid,c.cname FROM pw_cnphoto cp LEFT JOIN pw_cnalbum ca ON cp.aid=ca.aid LEFT JOIN pw_colonys c ON ca.ownerid=c.id WHERE ".$sql." ".$sqladd." ".$limit);
		$cnpho = array();
		while ($rt = $db->fetch_array($query)) {
			$rt['s_aname']	= substrs($rt['aname'],10);
			$rt['path']	= getphotourl($rt['path'], $rt['ifthumb']);
			$rt['uptime']	= get_date($rt['uptime']);
			$cnpho[] = $rt;
		}
		require_once PrintApp('groups');

	} elseif ($job == 'delete') {

		InitGP(array('aid','cid','cname','aname','uploader','pintro','uptime_s','uptime_e','orderway','ordertype','lines','page','selid'));
		require_once(R_P . 'require/app_core.php');

		foreach ($selid as $key => $pid) {
			$photo = $db->get_one("SELECT cp.path,ca.aid,ca.lastphoto,ca.lastpid,ca.ownerid FROM pw_cnphoto cp LEFT JOIN pw_cnalbum ca ON cp.aid=ca.aid WHERE cp.pid=" . pwEscape($pid) . " AND ca.atype='1'");
			if (empty($photo)) {
				adminmsg('data_error',"$basename&action=photos");
			}
			$uids[] = $photo['ownerid'];
			$db->update("DELETE FROM pw_cnphoto WHERE pid=" . pwEscape($pid));

			$pwSQL = array();
			if ($photo['path'] == $photo['lastphoto']) {
				$pwSQL['lastphoto'] = $db->get_value("SELECT path FROM pw_cnphoto WHERE aid=" . pwEscape($photo['aid']) . " ORDER BY pid DESC LIMIT 1");
			}
			if (strpos(",$photo[lastpid],",",$pid,") !== false) {
				$pwSQL['lastpid'] = implode(',',getLastPid($photo['aid']));
			}
			$upsql = $pwSQL ? ',' . pwSqlSingle($pwSQL) : '';
			$db->update("UPDATE pw_cnalbum SET photonum=photonum-1{$upsql} WHERE aid=" . pwEscape($photo['aid']));

			pwDelatt($photo['path'], $db_ifftp);
			$lastpos = strrpos($photo['path'],'/') + 1;
			pwDelatt(substr($photo['path'], 0, $lastpos) . 's_' . substr($photo['path'], $lastpos), $db_ifftp);
			pwFtpClose($ftp);

			$affected_rows = delAppAction('photo',$pid) + 1;
			countPosts("-$affected_rows");
		}
		$uids = array_unique($uids);
		updateUserAppNum($uids,'photo','recount');
		adminmsg('operate_success',"$basename&action=photos&job=list&aid=$aid&cid=$cid&cname=".rawurlencode($cname)."&aname=".rawurlencode($aname)."&uploader=".rawurlencode($uploader)."&pintro=".rawurlencode($pintro)."&uptime_s=$uptime_s&uptime_e=$uptime_e&orderway=$orderway&ordertype=$ordertype&lines=$lines&page=$page&");
	} else {
		require_once PrintApp('groups');
	}
}

function Delcnimg($filename) {
	return pwDelatt("cn_img/$filename",$GLOBALS['db_ifftp']);
}

function updatecache_cnc() {
	global $db;
	$classdb = array();
	$query = $db->query('SELECT fid,cname FROM pw_cnclass WHERE ifopen=1');
	while ($rt = $db->fetch_array($query)) {
		$classdb[$rt['fid']] = $rt['cname'];
	}
	$classdb = serialize($classdb);
	$db->pw_update(
		"SELECT hk_name FROM pw_hack WHERE hk_name='o_classdb'",
		'UPDATE pw_hack SET ' . pwSqlSingle(array('hk_value' => $classdb, 'vtype' => 'array')) . " WHERE hk_name='o_classdb'",
		'INSERT INTO pw_hack SET ' . pwSqlSingle(array('hk_name' => 'o_classdb', 'vtype' => 'array', 'hk_value' => $classdb))
	);
	updatecache_conf('o',true);
}
?>