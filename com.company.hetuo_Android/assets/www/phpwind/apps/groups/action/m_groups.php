<?php
!defined('A_P') && exit('Forbidden');
!$winduid && Showmsg('not_login');

$isGM = CkInArray($windid,$manager);
!$isGM && $groupid==3 && $isGM=1;

if ($db_question && ($o_share_qcheck || $o_groups_qcheck)) {
	$qkey = array_rand($db_question);
}

require_once(R_P.'require/showimg.php');
//TODO 我的群组没了?

!$db_groups_open && Showmsg('groups_close');

InitGP(array('a'));

$current_tab_id = empty($a) ? 'index' : ($a == 'create' ? 'index' : $a);
$db_perpage = 20;

if (empty($a)) {

	InitGP(array('u','space'), null, 2);
	!$u && $u = $winduid;
	if ($space == 1 && defined('F_M')) {
		$userdb = $db->get_one("SELECT index_privacy FROM pw_ouserdata WHERE uid=" . pwEscape($u));
		list($isU, $privacy) = pwUserPrivacy($u,$userdb);
		if ($groupid == 3 || $isU == 2 || $isU != 2 && $privacy['index']) {
			$SpaceShow = 1;
		}
		if (!$SpaceShow) Showmsg('mode_o_index_right');
	}

	if ($u == $winduid && $space != 1) {

		$username = $windid;
		$group_own = $group_other = $apply = $feeds = array();
		$count = 0;
		$query = $db->query("SELECT cm.ifadmin,cm.addtime,c.id,c.cname,c.cnimg,c.admin,c.createtime,cm2.uid FROM pw_cmembers cm LEFT JOIN pw_colonys c ON cm.colonyid=c.id LEFT JOIN pw_members cm2 ON c.admin=cm2.username WHERE cm.uid=" . pwEscape($u) . " ORDER BY cm.ifadmin DESC");
		while ($rt = $db->fetch_array($query)) {
			if ($rt['cnimg']) {
				list($rt['cnimg']) = geturl("cn_img/$rt[cnimg]",'lf');
			} else {
				$rt['cnimg'] = $pwModeImg.'/groupnopic.gif';
			}
			empty($rt['addtime']) && $rt['addtime'] = $rt['createtime'];
			$rt['addtime'] = get_date($rt['addtime'], 'Y-m-d');
			if ($rt['ifadmin'] == '-1') {
				$apply[] = $rt;
			} elseif($rt['admin'] == $windid) {
				$count++;
				$colonyids[] = $rt['id'];
				$group_own[] = $rt;
			} else {
				$count++;
				$colonyids[] = $rt['id'];
				$group_other[] = $rt;
			}
		}
		if ($colonyids) {
			$query = $db->query("SELECT f.*,m.username,m.groupid,c.id,c.cname FROM pw_feed f LEFT JOIN pw_members m ON f.uid=m.uid LEFT JOIN pw_colonys c ON f.typeid=c.id WHERE f.type IN('colony_post','colony_photo','colony') AND f.typeid IN(" . pwImplode($colonyids) . ') ORDER BY f.timestamp DESC LIMIT 30');
			while ($rt = $db->fetch_array($query)) {
				$rt['descrip'] = parseFeed($rt['descrip']);
				if ($rt['groupid'] == 6 && $db_shield && $groupid != 3) {
					$rt['descrip'] = appShield('ban_feed');
				}
				$key = get_date($rt['timestamp'],'y-m-d');
				$groupdb[$rt['id']] = $rt['cname'];
				$feeds[$key][$rt['id']][] = $rt;
			}
		}
	} else {

		$space  = 1;
		$friend = $db->get_one("SELECT uid,username,icon FROM pw_members WHERE uid=".pwEscape($u));
		$username = $friend['username'];

		$count	= $db->get_value("SELECT COUNT(DISTINCT c.id) AS count FROM pw_cmembers cm LEFT JOIN pw_colonys c ON cm.colonyid=c.id WHERE cm.uid=".pwEscape($u)." AND cm.ifadmin <> '-1'");

		if ($count) {
			$page = (int)GetGP('page');
			$pageurl = $basename.'u='.$u."&space=1&";
			list($pages,$limit) = pwLimitPages($count,$page,"$pageurl");
			$query	= $db->query("SELECT DISTINCT c.id,c.classid,c.cname,c.cnimg,c.admin,c.members,c.descrip,cm.addtime,c.createtime FROM pw_cmembers cm LEFT JOIN pw_colonys c ON cm.colonyid=c.id WHERE cm.uid=".pwEscape($u)." AND cm.ifadmin <> '-1' ORDER BY cm.colonyid DESC $limit");
			while ($rt = $db->fetch_array($query)) {
				if ($rt['cnimg']) {
					list($rt['cnimg']) = geturl("cn_img/$rt[cnimg]",'lf');
				} else {
					$rt['cnimg'] = $pwModeImg.'/groupnopic.gif';
				}
				$rt['addtime'] = get_date($rt['addtime'], 'Y-m-d');
				$rt['createtime'] = get_date($rt['createtime'], 'Y-m-d');
				$group[] = $rt;
			}
		}
	}

//	require_once(M_P.'require/header.php');
	if ($space == 1 && defined('F_M')) {
		$spaceurl = $baseUrl;
		require_once(R_P.'require/credit.php');
		list($userdb,$ismyfriend,$friendcheck,$usericon,$usercredit,$totalcredit,$appcount,$p_list) = getAppleftinfo($u);
		require_once PrintEot('header');
		require_once PrintEot('user_groups');
		footer();
	} else {
		//require_once PrintEot('m_groups');
		list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_groups",true);
	}
//	footer();

} elseif ($a == 'my') {

	InitGP(array('page'), '', 2);
	$page < 1 && $page = 1;
	$total = $db->get_one("SELECT COUNT(*) AS sum,SUM(cm.username=c.admin) AS creates FROM pw_cmembers cm LEFT JOIN pw_colonys c ON cm.colonyid=c.id WHERE cm.ifadmin<>'-1' AND cm.uid=" . pwEscape($winduid));
	list($pages, $limit) = pwLimitPages($total['sum'], $page, "{$basename}a=my&");

	$group = array();
	$query = $db->query("SELECT c.id,c.cname,c.cnimg,c.admin,c.members FROM pw_cmembers cm LEFT JOIN pw_colonys c ON cm.colonyid=c.id WHERE cm.ifadmin<>'-1' AND cm.uid=" . pwEscape($winduid) . ' ORDER BY (cm.username=c.admin) DESC ' . $limit);
	while ($rt = $db->fetch_array($query)) {
		if ($rt['cnimg']) {
			list($rt['cnimg']) = geturl("cn_img/$rt[cnimg]",'lf');
		} else {
			$rt['cnimg'] = $pwModeImg.'/groupnopic.gif';
		}
		$group[$rt['id']] = $rt;
	}
	$total['adds'] = $total['sum'] - $total['creates'];
	list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_groups",true);

} elseif ($a == 'all') {

	InitGP(array('page', 'cid', 'friends', 'members'), null, 2);
	InitGP(array('keyword'));

	$atc_name = getLangInfo('app','group');
	$cMembers = $group = array();
	$sqlsel   = $sqltab = '';

	if ($cid) {
		$sqlsel .= ' AND c.classid=' . pwEscape($cid);
	}
	if ($members) {
		$sqlsel .= ' AND c.members>=' . pwEscape($members);
	}
	if ($keyword) {
		$sqlsel .= ' AND c.cname LIKE ' . pwEscape("%" . $keyword . "%");
	}
	if ($friends) {
		$friends = getFriends($winduid);
		unset($friends[$winduid]);
		$uids = $friends ? array_keys($friends) : array(0);
		$sqltab .= ' LEFT JOIN pw_cmembers cm ON c.id=cm.colonyid';
		$sqlsel .= ' AND cm.uid IN(' . pwImplode($uids) . ')';
	}
	$total = $db->get_value("SELECT COUNT(DISTINCT c.id) AS sum FROM pw_colonys c {$sqltab} WHERE 1 {$sqlsel}");

	if ($total) {
		list($pages, $limit) = pwLimitPages($total, $page, "{$basename}a=all&");
		$query = $db->query("SELECT DISTINCT c.id,c.classid,c.cname,c.members,c.cnimg,c.createtime FROM pw_colonys c {$sqltab} WHERE 1 {$sqlsel} ORDER BY c.id DESC $limit");
		while ($rt = $db->fetch_array($query)) {
			if ($rt['cnimg']) {
				list($rt['cnimg']) = geturl("cn_img/$rt[cnimg]", 'lf');
			} else {
				$rt['cnimg'] = $pwModeImg.'/groupnopic.gif';
			}
			$rt['createtime'] = get_date($rt['createtime'], 'Y-m-d');
			$group[$rt['id']] = $rt;
		}
	}
	$colonyids = pwImplode(array_keys($group));
	if ($colonyids) {
		$query = $db->query("SELECT id,ifadmin,colonyid FROM pw_cmembers WHERE colonyid IN ($colonyids) AND uid=" . pwEscape($winduid,false));
		while ($rt = $db->fetch_array($query)) {
			$cMembers[$rt['colonyid']] = $rt['ifadmin'];
		}
	}
	$u = $winduid;
	$username = $windid;

	$o_cate = array();
	include_once(D_P . 'data/bbscache/forum_cache.php');
        if(is_array($o_classdb)){
            foreach ($o_classdb as $key => $value) {
                    $o_cate[$forum[$key]['fup']][$key] = $value;
            }
        }
//	require_once(M_P.'require/header.php');
//	require_once PrintEot('m_groups');
//	footer();
	list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_groups",true);

} elseif ($a == 'friend') {

	InitGP(array('page','cid'), null, 2);

	$friends = getFriends($winduid);
	unset($friends[$winduid]);
	$friends = is_array($friends) ? array_keys($friends) : array();
	$group = array();
	$pages = '';

	if (count($friends)) {

		$total = $db->get_value("SELECT COUNT(DISTINCT c.id) AS count FROM pw_cmembers cm LEFT JOIN pw_colonys c ON cm.colonyid=c.id WHERE cm.uid IN(" . pwImplode($friends) . ") AND cm.ifadmin <> '-1'");
		list($pages,$limit) = pwLimitPages($total,$page,"{$basename}a=friend&");

		$friends[] = $winduid;
		$query = $db->query("SELECT c.id,c.cname,c.cnimg,c.admin,SUM(cm.uid='$winduid') AS ifadd FROM pw_cmembers cm LEFT JOIN pw_colonys c ON cm.colonyid=c.id WHERE cm.uid IN(" . pwImplode($friends) . ") AND cm.ifadmin<>'-1' GROUP BY cm.colonyid HAVING(SUM(cm.uid!='$winduid') > 0) ORDER BY cm.colonyid DESC $limit");
		while ($rt = $db->fetch_array($query)) {
			if ($rt['cnimg']) {
				list($rt['cnimg']) = geturl("cn_img/$rt[cnimg]",'lf');
			} else {
				$rt['cnimg'] = $pwModeImg.'/groupnopic.gif';
			}
			$rt['friends'] = array();
			$group[$rt['id']] = $rt;
		}
		if ($group) {
			$query = $db->query("SELECT uid,username,colonyid FROM pw_cmembers WHERE uid IN (" . pwImplode($friends) . ') AND colonyid IN(' . pwImplode(array_keys($group)) . ") AND ifadmin<>'-1'");
			while ($rt = $db->fetch_array($query)) {
				$num = $group[$rt['colonyid']]['ifadd'] ? 2 : 3;
				if ($rt['uid'] != $winduid && count($group[$rt['colonyid']]['friends']) < $num) {
					$group[$rt['colonyid']]['friends'][$rt['uid']] = $rt['username'];
				}
			}
		}
	}
	$u	= $winduid;
	$username = $windid;

//	require_once(M_P.'require/header.php');
//	require_once PrintEot('m_groups');
//	footer();
	list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_groups",true);

} elseif ($a == 'create') {

	banUser();
	!$o_newcolony && Showmsg('colony_reglimit');

	if ($groupid != 3 && $o_groups && strpos($o_groups,",$groupid,") === false) {
		Showmsg('colony_groupright');
	}

	require_once(R_P.'require/credit.php');
	$o_groups_creditset = unserialize($o_groups_creditset);
	$o_groups_creditset['Creategroup'] = @array_diff($o_groups_creditset['Creategroup'],array(0));
	$costs = '';
	if (!empty($o_groups_creditset['Creategroup'])) {
		foreach ($o_groups_creditset['Creategroup'] as $key => $value) {
			if ($value > 0) {
				$moneyname = $credit->cType[$key];
				if ($value > $credit->get($winduid,$key)) {
                    $GLOBALS['o_createmoney'] = $value;
					Showmsg('colony_creatfailed');
				}
				$unit = $credit->cUnit[$key];
				$value>0 && $costs .= $value.$unit.$moneyname.",";
			}
		}
	}
	$costs = trim($costs,",");
	if ($o_allowcreate && $o_allowcreate <= $db->get_value("SELECT COUNT(*) AS sum FROM pw_colonys WHERE admin=" . pwEscape($windid))) {
		Showmsg('colony_numlimit');
	}
	if (empty($_POST['step'])) {

		$u = $winduid;
		$username = $windid;
		$o_cate = array();
		include_once(D_P . 'data/bbscache/forum_cache.php');
		if(is_array($o_classdb)){
			foreach ($o_classdb as $key => $value) {
					$o_cate[$forum[$key]['fup']][$key] = $value;
			}
		}
		if (empty($o_cate)) {
			Showmsg('没有设置群组分类与版块的关联,无法创建群组');
		}
//		require_once(M_P.'require/header.php');
//		require_once PrintEot('m_groups');
//		footer();
		list($isheader,$isfooter,$tplname,$isleft) = array(true,true,"m_groups",true);

	} else {

		require_once(R_P.'require/postfunc.php');
		PostCheck(1,$o_groups_gdcheck,$o_groups_qcheck);
		InitGP(array('cname','descrip'),'P');
		InitGP(array('cid'), 'P', 2);
		(!$cname || strlen($cname) > 20) && Showmsg('colony_emptyname');
		(!$descrip || strlen($descrip) > 255) && Showmsg('colony_descrip');
		!$cid && Showmsg('colony_class');

		require_once(R_P . 'require/bbscode.php');
		$wordsfb = L::loadClass('FilterUtil');
		if (($banword = $wordsfb->comprise($cname)) !== false) {
			Showmsg('title_wordsfb');
		}
		if (($banword = $wordsfb->comprise($descrip)) !== false) {
			Showmsg('title_wordsfb');
		}

		$rt = $db->get_one("SELECT id FROM pw_colonys WHERE cname=".pwEscape($cname));
		$rt['id'] > 0 && Showmsg('colony_samename');
		/*
		$credit->addLog('hack_cycreate',array($o_moneytype => -$o_createmoney),array(
			'uid'		=> $winduid,
			'username'	=> $windid,
			'ip'		=> $onlineip,
			'cnname'	=> stripslashes($cname)
		));
		$credit->set($winduid,$o_moneytype,-$o_createmoney);
		*/
		//积分变动
		if (!empty($o_groups_creditset['Creategroup'])) {
			$creditset = getCreditset($o_groups_creditset['Creategroup'],false);
			$credit->sets($winduid,$creditset,true);
			updateMemberid($winduid);
		}

		if ($creditlog = unserialize($o_groups_creditlog)) {
			addLog($creditlog['Creategroup'],$windid,$winduid,'groups_Creategroup');
		}

		$db->update("INSERT INTO pw_colonys SET " . pwSqlSingle(array(
				'cname'		=> $cname,
				'classid'	=> $cid,
				'admin'		=> $windid,
				'members'	=> 1,
				'ifcheck'	=> 1,
				'createtime'=> $timestamp,
				'descrip'	=> $descrip
		)));
		$cyid = $db->insert_id();

		$db->update("INSERT INTO pw_cmembers SET " . pwSqlSingle(array(
				'uid'		=> $winduid,
				'username'	=> $windid,
				'ifadmin'	=> 1,
				'colonyid'	=> $cyid,
				'addtime'	=> $timestamp
		)));

		$db->update("UPDATE pw_cnclass SET cnsum=cnsum+1 WHERE fid=" . pwEscape($cid));

		pwAddFeed($winduid, 'colony', '', array(
			'lang'		=> 'colony_create',
			'colonyid'	=> $cid,
			'cname'		=> $cname,
			'link'		=> "{$db_bbsurl}/{#APPS_BASEURL#}q=group&cyid=$cyid"
		));
		updateUserAppNum($winduid,'group');
		refreshto("{$baseUrl}q=group&cyid=$cyid&a=set",'colony_regsuccess');
	}
}
?>