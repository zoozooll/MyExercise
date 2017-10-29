<?php
!function_exists('adminmsg') && exit('Forbidden');
$basename="$admin_file?adminjob=tpccheck";
include_once(D_P.'data/bbscache/forumcache.php');
include_once(R_P.'require/forum.php');

if ($admin_gid == 5) {
	list($allowfid,$forumcache) = GetAllowForum($admin_name);
	$sql = $allowfid ? "fid IN($allowfid)" : '0';
} else {
	include(D_P.'data/bbscache/forumcache.php');
	list($hidefid,$hideforum) = GetHiddenForum();
	if ($admin_gid == 3) {
		$forumcache .= $hideforum;
		$sql = '1';
	} else {
		$sql = $hidefid ? "fid NOT IN($hidefid)" : '1';
	}
}
$action = GetGP('action');

if (!$action) {
	if (!$_POST['step']) {

		InitGP(array('fid','username','uid','page'));
		if (is_numeric($fid)) {
			$sql .= " AND fid=" . pwEscape($fid);
		} elseif ($sql == '1') {
			$fids = array();
			foreach ($forum as $key => $value) {
				$fids[] = $key;
			}
			$fids && $sql .= " AND fid IN(" . pwImplode($fids) . ")";
		}
		$sql .= " AND ifcheck='0'";

		if ($username) {
			$rt  = $db->get_one("SELECT uid FROM pw_members WHERE username=".pwEscape($username));
			$uid = $rt['uid'];
		}
		is_numeric($uid) && $sql .= "AND authorid=" . pwEscape($uid);

		(!is_numeric($page) || $page < 1) && $page = 1;
		$limit = pwLimit(($page-1)*$db_perpage,$db_perpage);
		$rt    = $db->get_one("SELECT COUNT(*) AS sum FROM pw_threads WHERE $sql");
		$pages = numofpage($rt['sum'],$page,ceil($rt['sum']/$db_perpage),"$basename&fid=$fid&uid=$uid&");

		$checkdb = array();
		$query = $db->query("SELECT tid,fid,subject,author,authorid,postdate FROM pw_threads WHERE $sql ORDER BY postdate DESC $limit");
		while ($rt = $db->fetch_array($query)) {
			$rt['subject']  = substrs($rt['subject'],35);
			$rt['name']     = $forum[$rt['fid']]['name'];
			$rt['postdate'] = get_date($rt['postdate']);
			$checkdb[]      = $rt;
		}
		include PrintEot('tpccheck');exit;

	} elseif ($_POST['step'] == 2) {

		InitGP(array('selid'),'P');

		if (!$selids = checkselid($selid)) {
			$basename = "javascript:history.go(-1);";
			adminmsg('operate_error');
		}

		if ($type == 'pass') {

			$fids  = array();
			$query = $db->query("SELECT fid FROM pw_threads WHERE $sql AND tid IN($selids)");
			while ($rt = $db->fetch_array($query)) {
				$fids[$rt['fid']] ++;
			}
			foreach ($fids as $key => $value) {
				$rt = $db->get_one("SELECT tid,author,postdate,subject FROM pw_threads WHERE fid=" . pwEscape($key) . " ORDER BY lastpost DESC LIMIT 1");
				$lastpost = $rt['subject']."\t".$rt['author']."\t".$rt['postdate']."\t"."read.php?tid=$rt[tid]&page=e#a";
				$db->update("UPDATE pw_forumdata"
					. " SET topic=topic+" . pwEscape($value)
						. ',article=article+' . pwEscape($value)
						. ',tpost=tpost+' . pwEscape($value)
						. ',lastpost=' . pwEscape($lastpost)
					. ' WHERE fid=' . pwEscape($key));
			}
			if ($selids) {
				$db->update("UPDATE pw_threads SET ifcheck='1' WHERE $sql AND tid IN($selids)");
				$threadIds = explode("','",trim($selids,"'"));
				if($threadIds){
					$threads = L::loadClass('Threads');
					$threads->delThreads($threadIds);
				}
			}
			foreach($fids as $fid){
				$threadList = L::loadClass("threadlist");
				$threadList->refreshThreadIdsByForumId($fid);
			}
		} else {
			
			$delarticle = L::loadClass('DelArticle');
			if (!$sqlby = $delarticle->sqlFormatByIds($selid)) {
				$basename = "javascript:history.go(-1);";
				adminmsg('operate_error');
			}
			$readdb = $delarticle->getTopicDb("$sql AND tid $sqlby");
			
			//积分操作
			require_once(R_P.'require/credit.php');
			$creditOpKey = "Delete";
			$forumInfos = array();
			foreach ($readdb as $tpcData) {
				if (!isset($forumInfos[$tpcData['fid']])) $forumInfos[$tpcData['fid']] = L::forum($tpcData['fid']);
				$foruminfo = $forumInfos[$tpcData['fid']];
				$creditset = $credit->creditset($foruminfo['creditset'],$db_creditset);

				$credit->addLog("topic_$creditOpKey", $creditset[$creditOpKey], array(
					'uid' => $tpcData['authorid'],
					'username' => $tpcData['author'],
					'ip' => $onlineip,
					'fname' => strip_tags($foruminfo['name']),
					'operator' => $windid,
				));
				$credit->sets($tpcData['authorid'],$creditset[$creditOpKey],false);
			}
			$credit->runsql();
			
			$delarticle->delTopic($readdb);
		}
		adminmsg('operate_success');
	}
} else {
	$basename="$admin_file?adminjob=tpccheck&action=postcheck";
	if (!$_POST['step']) {

		InitGP(array('fid','username','uid','page','ptable'));
		$sql .= " AND ifcheck='0'";
		is_numeric($fid) && $sql .= " AND fid=".pwEscape($fid);
		if ($username) {
			$rt  = $db->get_one("SELECT uid FROM pw_members WHERE username=".pwEscape($username));
			$uid = $rt['uid'];
		}
		is_numeric($uid) && $sql .= "AND authorid=".pwEscape($uid);
		$sql .= " ORDER BY postdate DESC";

		if ($db_plist && count($db_plist)>1) {
			!isset($ptable) && $ptable = $db_ptable;
			foreach ($db_plist as $key => $val) {
				$name = $val ? $val : ($key != 0 ? getLangInfo('other','posttable').$key : getLangInfo('other','posttable'));
				$p_table .= "<option value=\"$key\">".$name."</option>";
			}
			$p_table  = str_replace("<option value=\"$ptable\">","<option value=\"$ptable\" selected>",$p_table);
			$pw_posts = GetPtable($ptable);
		} else {
			$pw_posts = 'pw_posts';
		}
		(!is_numeric($page) || $page < 1) && $page = 1;
		$limit = pwLimit(($page-1)*$db_perpage,$db_perpage);
		$rt    = $db->get_one("SELECT COUNT(*) AS sum FROM $pw_posts WHERE $sql");
		$pages = numofpage($rt['sum'],$page,ceil($rt['sum']/$db_perpage),"$basename&fid=$fid&uid=$uid&");

		$postdb=array();
		$query = $db->query("SELECT pid,tid,fid,subject,author,authorid,ifcheck,postdate,content FROM $pw_posts WHERE $sql $limit");
		while ($rt = $db->fetch_array($query)) {
			if ($rt['subject']) {
				$rt['subject'] = substrs($rt['subject'],35);
			} else {
				$rt['subject'] = substrs($rt['content'],35);
			}
			$rt['name']     = $forum[$rt['fid']]['name'];
			$rt['postdate'] = get_date($rt['postdate']);
			$postdb[]       = $rt;
		}
		include PrintEot('postcheck');exit;

	} elseif ($_POST['step'] == 2) {

		InitGP(array('selid','ptable'),'P');
		if (!$selid = checkselid($selid)) {
			$basename = "javascript:history.go(-1);";
			adminmsg('operate_error');
		}
		$pw_posts = GetPtable($ptable);
		if ($type == 'pass') {
			$fids  = $tids = array();
			$query = $db->query("SELECT fid,tid FROM $pw_posts WHERE $sql AND pid IN($selid)");
			while ($rt = $db->fetch_array($query)) {
				$tids[$rt['tid']] ++;
				$fids[$rt['fid']] ++;
			}
			foreach ($tids as $key => $value) {
				$rt = $db->get_one("SELECT postdate,author FROM $pw_posts WHERE tid=" . pwEscape($key) . " ORDER BY postdate DESC LIMIT 1");
				$db->update("UPDATE pw_threads SET replies=replies+".pwEscape($value) . ",lastpost=" . pwEscape($rt['postdate'],false) . ",lastposter =" . pwEscape($rt['author'],false) . "WHERE tid=" . pwEscape($key));
				# memcache refresh
				$threadList = L::loadClass("threadlist");
				$threadList->updateThreadIdsByForumId($fid,$tid);
			}
			foreach ($fids as $key => $value) {
				$db->update("UPDATE pw_forumdata SET article=article+".pwEscape($value).",tpost=tpost+".pwEscape($value,false)."WHERE fid=".pwEscape($key));
			}
			$db->update("UPDATE $pw_posts SET ifcheck='1' WHERE $sql AND pid IN($selid)");
			$threadIds = explode(",",$selid);
			if($threadIds){
				$threads = L::loadClass('Threads');
				$threads->delThreads($threadIds);
			}
		} else {
			require_once(R_P.'require/credit.php');
			$creditOpKey = "Deleterp";
			$forumInfos = array();
			$_tids = $_pids = $deluids = array();
			$query = $db->query("SELECT fid,tid,pid,aid,author,authorid FROM $pw_posts WHERE $sql AND pid IN($selid)");
			while ($rt = $db->fetch_array($query)) {
				//积分操作
				if (!isset($forumInfos[$rt['fid']])) $forumInfos[$rt['fid']] = L::forum($rt['fid']);
				$foruminfo = $forumInfos[$rt['fid']];
				$creditset = $credit->creditset($foruminfo['creditset'],$db_creditset);
				$credit->addLog("topic_$creditOpKey", $creditset[$creditOpKey], array(
					'uid' => $rt['authorid'],
					'username' => $rt['author'],
					'ip' => $onlineip,
					'fname' => strip_tags($foruminfo['name']),
					'operator' => $windid,
				));
				$credit->sets($rt['authorid'],$creditset[$creditOpKey],false);
				
				$deluids[$rt['authorid']] = isset($deluids[$rt['authorid']]) ? $deluids[$rt['authorid']] + 1 : 1;
				if ($rt['aid']) {
					$_tids[$rt['tid']] = $rt['tid'];
					$_tids[$rt['pid']] = $rt['pid'];
				}
			}
			$credit->runsql();

			if ($_tids && $_pids) {
				$pw_attachs = L::loadDB('attachs');
				$attachdb = $pw_attachs->getByTid($_tids,$_pids);
				require_once(R_P.'require/updateforum.php');
				delete_att($attachdb);
				pwFtpClose($ftp);
			}
			foreach ($deluids as $uid => $value) {
				$db->update("UPDATE pw_memberdata SET postnum=postnum-" . pwEscape($value) . " WHERE uid=" . pwEscape($uid));
			}
			$db->update("DELETE FROM $pw_posts WHERE $sql AND pid IN($selid)");
		}
		adminmsg('operate_success');
	}
}
?>