<?php
!function_exists('adminmsg') && exit('Forbidden');

require_once(R_P.'require/forum.php');

if ($admintype == 'article') {

	require_once(R_P.'require/updateforum.php');
	$basename = "$admin_file?adminjob=superdel&admintype=article";

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

	if (empty($action)) {
		$p_table = $t_table = '';
		if ($db_plist && count($db_plist)>1) {
			foreach ($db_plist as $key => $val) {
				$name = $val ? $val : ($key != 0 ? getLangInfo('other','posttable').$key : getLangInfo('other','posttable'));
				$p_table .= "<option value=\"$key\">".$name."</option>";
			}
			$p_table = str_replace("<option value=\"$db_ptable\">","<option value=\"$db_ptable\" selected>",$p_table);
		}
		if ($db_tlist) {
			$tlistdb = $db_tlist;
			foreach ($tlistdb as $key=>$val) {
				$name = !empty($val['2']) ? $val['2'] : ($key == 0 ? 'tmsgs' : 'tmsgs'.$key);
				$t_table .= "<option value=\"$key\">$name</option>";
			}
		}
		include PrintEot('superdel');exit;

	} elseif ($action == 'deltpc') {

		InitGP(array('ttable'));
		if ($ttable == 'auto') {
			$rt = $db->get_one("SELECT MAX(tid) AS mtid FROM pw_threads");
			$pw_tmsgs = GetTtable($rt['mtid']);
		} else {
			$pw_tmsgs = $ttable>0 ? 'pw_tmsgs'.$ttable : 'pw_tmsgs';
		}
		InitGP(array('fid','ifkeep','pstarttime','pendtime','lstarttime','lendtime','author','keyword','userip','lines'));
		InitGP(array('tstart','tend','hits','replies','tcounts','counts','page','sphinx','sphinxRange'),'GP',2);
		if (empty($_POST['step'])) {

			$_POST['pstarttime'] && $pstarttime = PwStrtoTime($pstarttime);
			$_POST['pendtime']   && $pendtime   = PwStrtoTime($pendtime);
			$_POST['lstarttime'] && $lstarttime = PwStrtoTime($lstarttime);
			$_POST['lendtime']   && $lendtime   = PwStrtoTime($lendtime);

			if ($fid=='-1' && !$pstarttime && !$pendtime && !$tcounts && !$counts && !$lstarttime && !$lendtime && !$hits && !$replies && !$author && !$keyword && !$userip && !$tstart && !$tend) {
				adminmsg('noenough_condition');
			}
			if (is_numeric($fid) && $fid > 0) {
				$sql .= " AND t.fid=".pwEscape($fid);
			}
			if ($ifkeep) {
				$sql.=" AND t.topped=0 AND t.digest=0";
			}
			if ($pstarttime) {
				$sql.=" AND t.postdate>".pwEscape($pstarttime);
			}
			if ($pendtime) {
				$sql.=" AND t.postdate<".pwEscape($pendtime);
			}
			if ($lstarttime) {
				$sql.=" AND t.lastpost>".pwEscape($lstarttime);
			}
			if ($lendtime) {
				$sql.=" AND t.lastpost<".pwEscape($lendtime);
			}
			if ($tstart) {
				$sql.=" AND t.tid>".pwEscape($tstart);
			}
			if ($tend) {
				$sql.=" AND t.tid<".pwEscape($tend);
			}
			$hits    && $sql.=" AND t.hits<".pwEscape($hits);
			$replies && $sql.=" AND t.replies<".pwEscape($replies);
			if ($tcounts) {
				$sql.=" AND char_length(tm.content)>".pwEscape($tcounts);
			} elseif ($counts) {
				$sql.=" AND char_length(tm.content)<".pwEscape($counts);
			}
			if ($author) {
				$authorarray = explode(",",$author);
				foreach ($authorarray as $value) {
					$value = str_replace('*','%',$value);
					$authorwhere .= " OR username LIKE ".pwEscape($value,false);
				}
				$authorwhere = substr_replace($authorwhere,"",0,3);
				$query = $db->query("SELECT uid FROM pw_members WHERE $authorwhere");
				while ($rt = $db->fetch_array($query)) {
					$authorids[] = $rt['uid'];
				}
				if ($authorids) {
					$sql .= " AND t.authorid IN(".pwImplode($authorids).")";
				} else {
					adminmsg('author_nofind');
				}
			}
			if ($keyword) {
				$keyword = trim($keyword);
				$keywordarray = explode(",",$keyword);
				foreach ($keywordarray as $value) {
					$value = str_replace('*','%',$value);
					$keywhere .= 'OR';
					$keywhere .= " tm.content LIKE ".pwEscape("%$value%")."OR t.subject LIKE ".pwEscape("%$value%");
				}
				$keywhere = substr_replace($keywhere,"",0,3);
				$sql .= " AND ($keywhere) ";
			}
			if ($userip) {
				$userip	 = str_replace('*','%',$userip);
				$sql	.= " AND (tm.userip LIKE ".pwEscape($userip).') ';
				$ip_add  = ',tm.userip';
			}
			$sql .= " AND tm.tid!=''";

			if (!is_numeric($lines))$lines=100;
			if( $sphinx && $keyword && $db_sphinx['isopen'] == 1 && strpos($keyword,'*') === false ){
				$keyword = trim($keyword);
				$index = ($sphinxRange == 1) ? 'threadsindex' : 'tmsgsindex';
				$sphinxServer = L::loadclass("search");
				$result  = $sphinxServer->sphinxSearch($keyword,"OR",$index,$digest,$fid,$exclude,"lastpost","DESC",$authorids,$pstarttime,$pendtime,"",$lines);
				if($result === false){
					adminmsg('search_keyword_empty');
				}
				$count = $result[0];
				$query = $db->query("SELECT t.*,tm.userip FROM pw_threads t LEFT JOIN $pw_tmsgs tm ON tm.tid=t.tid WHERE tm.tid in (".$result[1].") ORDER BY tid DESC");
			}else{
				$rs = $db->get_one("SELECT COUNT(*) AS count FROM pw_threads t LEFT JOIN $pw_tmsgs tm ON tm.tid=t.tid WHERE $sql LIMIT 1");
				$count = $rs['count'];
				$start = ($page-1)*$lines;
				$limit = pwLimit($start,$lines);
				$query = $db->query("SELECT t.*,tm.userip FROM pw_threads t LEFT JOIN $pw_tmsgs tm ON tm.tid=t.tid WHERE $sql ORDER BY tid DESC $limit");
			}
			$page < 1 && $page = 1;
			$numofpage = ceil($count/$lines);
			if ($numofpage && $page > $numofpage) {
				$page = $numofpage;
			}
			$pages=numofpage_t($count,$page,$numofpage,"$admin_file?adminjob=superdel&admintype=article&action=$action&fid=$fid&ifkeep=$ifkeep&pstarttime=$pstarttime&pendtime=$pendtime&lstarttime=$lstarttime&lendtime=$lendtime&tstart=$tstart&tend=$tend&hits=$hits&replies=$replies&author=".rawurlencode($author)."&keyword=".rawurlencode($keyword)."&userip=$userip&lines=$lines&ttable=$ttable&tcounts=$tcounts&counts=$counts&sphinx=$sphinx&sphinxRange=$sphinxRange&");
			$delid = $topicdb = array();
			include(D_P.'data/bbscache/forum_cache.php');
			while ($topic = $db->fetch_array($query)) {
				if ($_POST['direct']) {
					$delid[$topic['tid']] = $topic['fid'];
				} else {
					$topic['forumname'] = $forum[$topic['fid']]['name'];
					$topic['postdate'] = get_date($topic['postdate']);
					$topic['lastpost'] = get_date($topic['lastpost']);
					$topicdb[]=$topic;
				}
			}
			if (!$_POST['direct']) {
				include PrintEot('superdel');exit;
			}
		}
		if ($_POST['step'] == 2 || $_POST['direct']) {

			if (!$_POST['direct']) {
				InitGP(array('delid'),'P');
			}
			!$delid && adminmsg('operate_error');
			$delids = $delaids = $specialdb = array();
			$fidarray = array();
			foreach ($delid as $key => $value) {
				is_numeric($key) && $delids[] = $key;
				if (!in_array($value,$fidarray)) {
					$fidarray[] = $value;
				}
			}
			$delids = pwImplode($delids);
			/**
			* 删除帖子
			*/

			$db_guestread && require_once(R_P.'require/guestfunc.php');
			$ptable_a = $delnum = $pcdb = array();
			$query = $db->query("SELECT t.tid,t.fid,t.authorid,t.replies,t.postdate,t.special,t.ptable,tm.aid,t.ifupload FROM pw_threads t LEFT JOIN $pw_tmsgs tm ON tm.tid=t.tid WHERE $sql AND t.tid IN($delids)");
			while (@extract($db->fetch_array($query))) {
				//delete pcid
				if ($read['special'] > 20) {
					$pcdb[$read['special']][] = $read['tid'];
				}
				//delete pcid
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
				while($rt3 = $db->fetch_array($query3)){
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

			//delete pcid
			if ($pcdb) {
				_delPcTopic($pcdb);
			}
			//delete pcid

			# $db->update("DELETE FROM pw_threads WHERE tid IN ($delids)");
			# ThreadManager
            $threadManager = L::loadClass("threadmanager");
			$threadManager->deleteByThreadIds($fid,$delids);

			if($delids) {
				$db->update("DELETE FROM pw_feed WHERE type='post' AND typeid IN(".$delids.")");
			}

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
			adminmsg('operate_success',"$admin_file?adminjob=superdel&admintype=article&action=$action&fid=$_POST[fid]&ifkeep=$ifkeep&pstarttime=$pstarttime&pendtime=$pendtime&lstarttime=$lstarttime&lendtime=$lendtime&tstart=$tstart&tend=$tend&hits=$_POST[hits]&replies=$_POST[replies]&author=".rawurlencode($author)."&keyword=".rawurlencode($keyword)."&userip=$userip&lines=$lines&ttable=$ttable&tcounts=$tcounts&counts=$counts&page=$page");

		}
	} elseif ($action == 'delrpl') {

		InitGP(array('ptable'));
		is_numeric($ptable) && $dbptable = $ptable;
		$pw_posts = GetPtable($dbptable);

		InitGP(array('fid','tid','author','keyword','tcounts','counts','userip','nums'));
		InitGP(array('pstart','pend','page','sphinx','sphinx_range'),'GP',2);

		if (empty($_POST['step'])) {
			if (!$counts && !$tcounts && $fid=='-1' && !$keyword && !$tid && !$author && !$userip && !$pstart && !$pend) {
				adminmsg('noenough_condition');
			}
			if (is_numeric($fid) && $fid > 0) {
				$sql .= " AND fid=".pwEscape($fid);
			}
			if ($tid) {
				$tids = array();
				$tid_array = explode(",",$tid);
				foreach ($tid_array as $value) {
					if (is_numeric($value)) {
						$tids[] = $value;
					}
				}
				$tids && $sql.=" AND tid IN(".pwImplode($tids).")";
			}
			if ($pstart) {
				$sql.=" AND pid>".pwEscape($pstart);
			}
			if ($pend) {
				$sql.=" AND pid<".pwEscape($pend);
			}
			if ($author) {
				$authorarray=explode(",",$author);
				foreach ($authorarray as $value) {
					$value=addslashes(str_replace('*','%',$value));
					$authorwhere.=" OR username LIKE ".pwEscape($value);
				}
				$authorwhere=substr_replace($authorwhere,"",0,3);
				$authorids = array();
				$query=$db->query("SELECT uid FROM pw_members WHERE $authorwhere");
				while ($rt=$db->fetch_array($query)) {
					$authorids[] = $rt['uid'];
				}
				if ($authorids) {
					$sql .= " AND authorid IN(".pwImplode($authorids).")";
				} else {
					adminmsg('author_nofind');
				}
			}
			if ($keyword) {
				$keyword=trim($keyword);
				$keywordarray=explode(",",$keyword);
				foreach ($keywordarray as $value) {
					$value=str_replace('*','%',$value);
					$keywhere.=" OR content LIKE ".pwEscape("%$value%");
				}
				$keywhere=substr_replace($keywhere,"",0,3);
				$sql.=" AND ($keywhere) ";
			}
			if ($userip) {
				$userip=str_replace('*','%',$userip);
				$sql.=" AND (userip LIKE ".pwEscape($userip).")";
			}

			if ($tcounts) {
				$sql.=" AND char_length(content)>".pwEscape($tcounts);
			} elseif ($counts) {
				$sql.=" AND char_length(content)<".pwEscape($counts);
			}
			$nums = is_numeric($nums) ? $nums : 20;
			if( $sphinx && $keyword && $db_sphinx['isopen'] == 1 && strpos($keyword,'*') === false ){
				$index=$ptable ? 'posts1index' : 'postsindex';
				$sphinx = L::loadclass("search");
				$result  = $sphinx->sphinxSearch($keyword,"OR",$index,$digest,$fid,$exclude,"lastpost","DESC",$authorids,$pstart,$pend,"",$nums);
				if($result === false){
					adminmsg('search_keyword_empty');
				}
				$count = $result[0];
				$query = $db->query("SELECT fid,pid,tid,author,authorid,content,postdate,userip FROM $pw_posts WHERE pid in (".$result[1].")  ORDER BY postdate DESC ");
			}else{
				$rt    = $db->get_one("SELECT COUNT(*) AS sum FROM $pw_posts WHERE $sql");
				$count = $rt['sum'];
				$page < 1 && $page = 1;
				$limit = pwLimit(($page-1)*$nums,$nums);
				$sql  .= ' ORDER BY postdate DESC ';
				$sql  .= $_POST['direct'] ? " LIMIT $nums" : $limit;
				$query = $db->query("SELECT fid,pid,tid,author,authorid,content,postdate,userip FROM $pw_posts WHERE $sql");
			}
			$pages = numofpage_t($count,$page,ceil($count/$nums),"$admin_file?adminjob=superdel&admintype=article&action=$action&fid=$fid&tid=$tid&pstart=$pstart&pend=$pend&author=".rawurlencode($author)."&keyword=".rawurlencode($keyword)."&userip=$userip&tcounts=$tcounts&counts=$counts&nums=$nums&ptable=$ptable&sphinx=$sphinx&");
			$delid = $postdb = array();
			while ($post = $db->fetch_array($query)) {
				if ($_POST['direct']) {
					$delid[$post['pid']] = $post['fid'].'_'.$post['tid'];
				} else {
					$post['delid']	   = $post['fid'].'_'.$post['tid'];
					$post['forumname'] = $forum[$post['fid']]['name'];
					$post['postdate']  = get_date($post['postdate']);
					$post['content']   = substrs($post['content'],30);
					$postdb[] = $post;
				}
			}
			if (!$_POST['direct']) {
				include PrintEot('superdel');exit;
			}
		}
		if ($_POST['step'] == 2 || $_POST['direct']) {
			if (!$_POST['direct']) {
				InitGP(array('delid'),'P');
			}
			!$delid && adminmsg('operate_error');
			$delids = $delaids = $dtids = array();
			$fidarray = $tidarray = $delnum = array();
			foreach ($delid as $key=>$value) {
				is_numeric($key) && $delids[] = $key;
				list($dfid,$dtid) = explode('_',$value);
				$tidarray[] = $dtid;
				$dtids[] = $dtid;
				if (!in_array($dfid,$fidarray)) {
					$fidarray[]=$dfid;
				}
			}
			$delids = pwImplode($delids);
//			$dtids	= pwImplode($dtids);
			/**
			* 删除帖子
			*/
			$query = $db->query("SELECT tid,fid,postdate,ifupload FROM pw_threads WHERE tid IN(".pwImplode($dtids).")");
			while (@extract($db->fetch_array($query))) {
				$htmurl = $db_htmdir.'/'.$fid.'/'.date('ym',$postdate).'/'.$tid.'.html';
				if (file_exists(R_P.$htmurl)) {
					P_unlink(R_P.$htmurl);
				}
			}

			$query = $db->query("SELECT aid,authorid FROM $pw_posts WHERE pid IN ($delids)");
			while (@extract($db->fetch_array($query))) {
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
			if ($delaids) {
				$delaids = pwImplode($delaids);
				$db->update("DELETE FROM pw_attachs WHERE aid IN($delaids)");
			}
			$db->update("DELETE FROM $pw_posts WHERE pid IN ($delids)");

			$tidarray=array_count_values($tidarray);
			foreach ($tidarray as $key=>$value) {
				$db->update("UPDATE pw_threads SET replies=replies-".pwEscape($value)."WHERE tid=".pwEscape($key));
			}
			/**
			* 数据更新
			*/
			foreach ($fidarray as $fid) {
				updateforum($fid);
			}
			foreach ($delnum as $key => $value){
				$db->update("UPDATE pw_memberdata SET postnum=postnum-".pwEscape($value)."WHERE uid=".pwEscape($key));
			}

			$threads = L::loadClass('Threads');
			$threads->delThreads($dtids);

			P_unlink(D_P.'data/bbscache/c_cache.php');
			adminmsg('operate_success',"$admin_file?adminjob=superdel&admintype=article&action=$action&fid=$_POST[fid]&tid=$_POST[tid]&pstart=$pstart&pend=$pend&author=".rawurlencode($author)."&keyword=".rawurlencode($keyword)."&userip=$userip&tcounts=$tcounts&counts=$counts&nums=$nums&ptable=$ptable&page=$page");
		}
	} elseif ($action == 'view') {

		InitGP(array('tid','pid'));

		$pw_posts = GetPtable('N',$tid);
		$rt = $db->get_one("SELECT COUNT(*) AS sum FROM $pw_posts WHERE tid=".pwEscape($tid).'AND pid<'.pwEscape($pid));
		$page = ceil(($rt['sum']+1.5)/$db_readperpage);

		ObHeader("read.php?tid=$tid&page=$page#$pid");
	}
} elseif ($admintype == 'delmember') {

	$basename="$admin_file?adminjob=superdel&admintype=delmember";

	if (empty($action)) {

		$groupselect = "<option value='-1'>" . getLangInfo('all','reg_member') . "</option>";
		$query = $db->query("SELECT gid,gptype,grouptitle FROM pw_usergroups WHERE gid>2 AND gptype<>'member' ORDER BY gid");
		while ($group = $db->fetch_array($query)) {
			$groupselect .= "<option value=$group[gid]>$group[grouptitle]</option>";
		}
		include PrintEot('superdel');exit;

	} elseif ($action == 'del') {

		InitGP(array('groupid','schname','schemail','postnum','onlinetime','userip','regdate','schlastvisit','orderway','asc','lines','item'));
		InitGP(array('page'),'GP',2);

		if (empty($_POST['step'])) {

			if (!$schname && !$schemail && !$groupid && $regdate=='all' && $schlastvisit='all') {
				adminmsg('noenough_condition');
			}
			is_array($item) && $item = array_sum($item);
			for($i=0; $i<4; $i++){
				${'check_'.$i} = ($item & pow(2,$i)) ? 'checked' : '';
			}
			if ($groupid != '-1') {
				if ($groupid == '3' && !If_manager) {
					adminmsg('manager_right');
				} elseif (($groupid == '4' || $groupid == '5') && $admin_gid != 3) {
					adminmsg('admin_right');
				}
				$sql = "m.groupid=".pwEscape($groupid);
			} else {
				$sql = "m.groupid='-1'";
			}
			if ($schname != '') {
				$schname = str_replace('*','%',$schname);
				$sql .= " AND (m.username LIKE ".pwEscape($schname).")";
			}
			if ($schemail != '') {
				$schemail = str_replace('*','%',$schemail);
				$sql .= " AND (m.email LIKE ".pwEscape($schemail).")";
			}
			if ($postnum) {
				$sql .= " AND md.postnum<".pwEscape($postnum);
			}
			if ($onlinetime) {
				$sql .= " AND md.onlinetime<".pwEscape($onlinetime);
			}
			if ($userip) {
				$userip = str_replace('*','%',$userip);
				$sql .= " AND (md.onlineip LIKE ".pwEscape("$userip%").")";
			}
			if ($regdate != 'all') {
				$schtime = $timestamp-$regdate;
				$sql .= " AND m.regdate<".pwEscape($schtime);
			}
			if ($schlastvisit != 'all') {
				$schtime = $timestamp - $schlastvisit;
				$sql .= " AND md.thisvisit<".pwEscape($schtime);
			}
			$order = '';
			if ($orderway) {
				!in_array($orderway,array('regdate','lastvisit','postnum')) && $orderway = 'uid';
				$order = " ORDER BY ".($orderway == 'regdate' ? "m.uid " : "md.$orderway ");
				$asc && $order .= $asc;
			}
			$rs = $db->get_one("SELECT COUNT(*) AS count FROM pw_members m LEFT JOIN pw_memberdata md ON md.uid=m.uid WHERE $sql");
			$count = $rs['count'];
			if (!is_numeric($lines)) $lines = 100;
			$page < 1 && $page = 1;
			$numofpage = ceil($count/$lines);
			if ($numofpage && $page > $numofpage) {
				$page = $numofpage;
			}
			$pages = numofpage($count,$page,$numofpage, "$admin_file?adminjob=superdel&admintype=delmember&action=$action&groupid=$groupid&schname=" . rawurlencode($schname)."&schemail=$schemail&postnum=$postnum&onlinetime=$onlinetime&regdate=$regdate&schlastvisit=$schlastvisit&orderway=$orderway&asc=$asc&lines=$lines&");
			$start = ($page-1)*$lines;
			$limit = "LIMIT $start,$lines";
			$delid = $schdb = array();
			$query = $db->query("SELECT m.uid,m.username,m.email,m.groupid,m.regdate,md.thisvisit,md.postnum,md.onlineip FROM pw_members m LEFT JOIN pw_memberdata md ON md.uid=m.uid WHERE $sql $order $limit");
			while ($sch = $db->fetch_array($query)) {
				if ($_POST['direct']) {
					$delid[] = $sch['uid'];
				} else {
					strpos($sch['onlineip'],'|') && $sch['onlineip'] = substr($sch['onlineip'], 0, strpos($sch['onlineip'],'|'));
					if ($sch['groupid'] == '-1') {
						$sch['group'] = getLangInfo('all','reg_member');
					} else {
						$sch['group'] = $ltitle[$sch['groupid']];
					}
					$sch['regdate']   = get_date($sch['regdate']);
					$sch['thisvisit'] = get_date($sch['thisvisit']);
					$schdb[] = $sch;
				}
			}
			if (!$_POST['direct']) {
				include PrintEot('superdel');exit;
			}
		}
		if ($_POST['step'] == 2 || $_POST['direct']) {
			@set_time_limit(300);
			InitGP(array('item'),'P');
			$item = array_sum($item);
			!$item && adminmsg('operate_error');
			if (!$_POST['direct']) {
				InitGP(array('delid'),'P');
			}
			!$delid && adminmsg('operate_error');
			$delids = pwImplode($delid);

			if ($item & 2) {
				$delarticle = L::loadClass('DelArticle');
				$delarticle->delTopicByUids($delid);
			}
			if ($item & 4) {
				$delarticle = L::loadClass('DelArticle');
				$delarticle->delReplyByUids($delid);
			}
			if ($item & 8) {
				$middb = array();
				$query = $db->query("SELECT mid FROM pw_msg WHERE fromuid IN($delids) OR touid IN($delids)");
				while ($rt = $db->fetch_array($query)) {
					$middb[] = $rt['mid'];
				}
				$mids = pwImplode($middb,false);
				if ($mids) {
					$db->update("DELETE FROM pw_msg WHERE mid IN ($mids)");
					require_once(R_P.'require/msg.php');
					delete_msgc($mids);
				}
			}
			if ($item & 1) {
				$ucuser = L::loadClass('Ucuser');
				$ucuser->delete($delid);
			}
			adminmsg('operate_success', "$admin_file?adminjob=superdel&admintype=delmember&action=$action&groupid=$groupid&schname=" . rawurlencode($schname)."&schemail=$schemail&postnum=$postnum&onlinetime=$onlinetime&regdate=$regdate&schlastvisit=$schlastvisit&orderway=$orderway&asc=$asc&lines=$lines&page=$page");
		}
	}
} elseif ($admintype == 'message') {

	$basename = "$admin_file?adminjob=superdel&admintype=message";

	if (empty($action)) {

		include PrintEot('superdel');exit;

	} elseif ($action == 'del') {

		InitGP(array('keepnew','fromuser','touser','msgdate','keyword','lines','page'));
		if (empty($_POST['step'])) {

			if (!$type && !$keepnew && !$fromuser && !$touser && !$msgdate) {
				adminmsg('noenough_condition');
			}
			if ($type!='all') {
				$sql = "m.type=".pwEscape($type);
			} else {
				$sql = '1 ';
			}
			if ($keepnew) {
				$sql .= " AND m.ifnew='0'";
			}
			$mc_tab = '';
			if ($keyword) {
				$keyword = trim($keyword);
				$keywordarray = explode(",",$keyword);
				foreach ($keywordarray as $value) {
					$value = str_replace('*','%',$value);
					$keywhere .= 'OR';
					$keywhere .= " mc.content LIKE ".pwEscape("%$value%")." OR mc.title LIKE ".pwEscape("%$value%");
				}
				$keywhere = substr_replace($keywhere,"",0,3);
				$sql .= " AND ($keywhere) ";
				$mc_tab = ' LEFT JOIN pw_msgc mc ON m.mid=mc.mid';
			}
			if ($fromuser) {
				if ($fromuser == 'SYSTEM') {
					$sql .= " AND fromuid='0'";
				} else {
					$fromuser = str_replace('*','%',$fromuser);
					$rt = $db->get_one("SELECT uid,username,groupid FROM pw_members WHERE username LIKE ".pwEscape($fromuser));
					if (!$rt) {
						$errorname = $fromuser;
						adminmsg('user_not_exists');
					} elseif (CkInArray($rt['username'],$manager) && !If_manager) {
						adminmsg('msg_managerright');
					} elseif ($rt['groupid'] == 3 && $admin_gid != 3) {
						adminmsg('msg_adminright');
					}
					if ($type == 'rebox' || $type == 'sebox') {
						$sql .= " AND m.type=".pwEscape($type)."AND m.fromuid=".pwEscape($rt['uid']);
					} else {
						$sql .= " AND m.fromuid=".pwEscape($rt['uid']);
					}
				}
			}
			if ($touser) {
				$touser = str_replace('*','%',$touser);
				$rt = $db->get_one("SELECT uid,username,groupid FROM pw_members WHERE username LIKE ".pwEscape($touser));
				if (!$rt) {
					$errorname = $touser;
					adminmsg('user_not_exists');
				} elseif (CkInArray($rt['username'],$manager) && !If_manager) {
					adminmsg('msg_managerright');
				} elseif ($rt['groupid'] == 3 && $admin_gid != 3) {
					adminmsg('msg_adminright');
				}
				if ($type == 'rebox' || $type == 'sebox') {
					$sql .= " AND m.type=".pwEscape($type)."AND m.touid=".pwEscape($rt['uid']);
				} else {
					$sql .= " AND m.touid=".pwEscape($rt['uid']);
				}
			}
			if ($msgdate) {
				$schtime = $timestamp-$msgdate*24*3600;
				$sql .= " AND m.mdate<".pwEscape($schtime);
			}

			$rs = $db->get_one("SELECT COUNT(*) AS count FROM pw_msg m{$mc_tab} WHERE $sql");
			$count = $rs['count'];
			if (!is_numeric($lines)) $lines = 100;
			(!is_numeric($page) || $page < 1) && $page = 1;
			$numofpage = ceil($count/$lines);
			if ($numofpage && $page > $numofpage) {
				$page = $numofpage;
			}
			$pages=numofpage($count,$page,$numofpage,"$admin_file?adminjob=superdel&admintype=message&action=$action&type=$type&keepnew=$keepnew&msgdate=$msgdate&fromuser=".rawurlencode($fromuser)."&touser=".rawurlencode($touser)."&lines=$lines&");
			$start = ($page-1)*$lines;
			$limit = pwLimit($start,$lines);
			$delid = $messagedb = array();
			$query = $db->query("SELECT m.*,mc.title,m1.username as fromuser,m2.username as touser FROM pw_msg m LEFT JOIN pw_msgc mc ON m.mid=mc.mid LEFT JOIN pw_members m1 ON m1.uid=m.fromuid LEFT JOIN pw_members m2 ON m2.uid=m.touid WHERE $sql ORDER BY mid DESC $limit");
			while ($message = $db->fetch_array($query)) {
				if ($_POST['direct']) {
					$delid[] = $message['mid'];
				} else {
					!$message['fromuser'] && $message['fromuser'] = $message['username'];
					$message['date'] = get_date($message['mdate']);
					if ($message['type']=='public' && $message['togroups']) {
						$togroups = explode(',',$message['togroups']);
						foreach ($togroups as $key=>$gid) {
							$gid && $message['touser'].=$message['touser'] ? ','.$ltitle[$gid] : $ltitle[$gid];
						}
					}
					$messagedb[] = $message;
				}
			}
			if (!$_POST['direct']) {
				include PrintEot('superdel');exit;
			}
		}
		if ($_POST['step'] == 2 || $_POST['direct']) {
			if (!$_POST['direct']) {
				InitGP(array('delid'),'P');
			}
			!$delid && adminmsg('operate_error');
			foreach ($delid as $value) {
				is_numeric($value) && $delids[] = $value;
			}
			$delids = pwImplode($delids);
			$query = $db->query("SELECT touid FROM pw_msg WHERE mid IN($delids)");
			while ($rt = $db->fetch_array($query)) {
				$uiddb[] = $rt['touid'];
			}
			$uiddb = array_unique($uiddb);
			$db->update("DELETE FROM pw_msg WHERE mid IN ($delids)");
			require_once(R_P.'require/msg.php');
			delete_msgc($delids);
			$type != 'public' && updateNewpm($uiddb,'recount');
			adminmsg('operate_success',"$admin_file?adminjob=superdel&admintype=message&action=$action&type=$type&keepnew=$keepnew&msgdate=$msgdate&fromuser=".rawurlencode($fromuser)."&touser=".rawurlencode($touser)."&lines=$lines&page=$page");

		}
	} elseif ($action == 'msglog') {

		$pwServer['REQUEST_METHOD'] != 'POST' && PostCheck($verify);

		InitGP(array('msgdate','lines','delnum'));
		!$delnum && $delnum = 0;
		$sql = "1";
		if ($msgdate) {
			$schtime = $timestamp-$msgdate*24*3600;
			$sql .= " AND mdate<".pwEscape($schtime);
		}
		$lines < 1 && $lines = 100;
		$delids = array();

		$query = $db->query("SELECT DISTINCT mid FROM pw_msglog WHERE $sql LIMIT $lines");
		while ($rt = $db->fetch_array($query)) {
			$delids[] = $rt['mid'];
		}
		require_once(R_P.'require/msg.php');

		if ($delids) {
			$delids = pwImplode($delids);
			$db->update("DELETE FROM pw_msglog WHERE mid IN($delids)");
			$delnum += $db->affected_rows();
			delete_msgc($delids);
			adminmsg('msglog_delete_step', EncodeUrl("$basename&action=$action&msgdate=$msgdate&lines=$lines&delnum=$delnum"));
		} else {
			delete_msgc();
			adminmsg('operate_success');
		}
	}
}

function numofpage_t($count,$page,$numofpage,$url,$max=null,$ajaxurl='') {
	global $tablecolor;
	$total = $numofpage;
	if (!empty($max)) {
		$max = (int)$max;
		$numofpage > $max && $numofpage = $max;
	}
	if ($numofpage <= 1 || !is_numeric($page)) {
		return '';
	} else {
		list($url,$mao) = explode('#',$url);
		$mao && $mao = '#'.$mao;
		$pages = "<div class=\"pages\"><a href=\"javascript:;\" class=\"b\" onclick=\"superdel.submit(1);return false;\" >&laquo;</a>";
		for ($i = $page-3; $i <= $page-1; $i++) {
			if($i<1) continue;
			$pages .= "<a href=\"javascript:;\" onclick=\"superdel.submit($i);return false;\">$i</a>";
		}
		$pages .= "<b>$page</b>";
		if ($page < $numofpage) {
			$flag = 0;
			for ($i = $page+1; $i <= $numofpage; $i++) {
				$pages .= "<a href=\"javascript:;\" onclick=\"superdel.submit($i);return false;\">$i</a>";
				$flag++;
				if ($flag == 4) break;
			}
		}
		$pages .= "<a href=\"javascript:;\" class=\"b\" onclick=\"superdel.submit($numofpage);return false;\">&raquo;</a><span class=\"pagesone\"><span>Pages: $page/$total&nbsp; &nbsp; &nbsp;Go </span><input type=\"text\" size=\"3\" onkeydown=\"javascript: if(event.keyCode==13){superdel.submit(this.value); return false;}\"></span></div>";
		return $pages;
	}
}
//delete pcid
function _delPcTopic($pcdb){
	global $db;
	foreach ($pcdb as $key => $value) {
		$pcids =  pwImplode($value);
		$key = $key > 20 ? $key - 20 : 0;
		$pcvaluetable = GetPcatetable($key);
		$db->update("DELETE FROM $pcvaluetable WHERE tid IN($pcids)");
	}
}
//delete pcid
?>