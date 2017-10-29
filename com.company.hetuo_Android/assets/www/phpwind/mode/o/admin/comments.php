<?php
!function_exists('adminmsg') && exit('Forbidden');

@include_once(D_P.'data/bbscache/o_config.php');

if (empty($action)) {
	if (empty($job)) {
		require_once PrintMode('comments');
	} elseif ($job == 'list') {
		InitGP(array('type','typeid','title','username','postdate_s','postdate_e','ordertype','page','lines'));
		if (empty($type) && empty($title) && empty($username) && empty($postdate_s) && empty($postdate_e)) {
			adminmsg('noenough_condition');
		}
		$postdate_s && !is_numeric($postdate_s) && $postdate_s = PwStrtoTime($postdate_s);
		$postdate_e && !is_numeric($postdate_e) && $postdate_e = PwStrtoTime($postdate_e);
		$sql = $urladd = '';
		if ($type) {
			$sql .= $sql ? ' AND' : '';
			$sql .= ' type='.pwEscape($type);
			$urladd .= '&type='.$type;
		}
		if ($typeid) {
			$sql .= $sql ? ' AND' : '';
			$sql .= ' typeid='.pwEscape($typeid);
			$urladd .= '&typeid='.$typeid;
		}
		if ($title) {
			$title = str_replace('*','%',$title);
			$sql .= $sql ? ' AND' : '';
			$sql .= ' title LIKE '.pwEscape($title);
			$urladd .= '&title='.rawurlencode($title);
		}
		if ($username) {
			$username = str_replace('*','%',$username);
			$sql .= $sql ? ' AND' : '';
			$sql .= ' username LIKE '.pwEscape($username);
			$urladd .= '&username='.rawurlencode($username);
		}
		if ($postdate_s) {
			$sql .= $sql ? ' AND' : '';
			$sql .= ' postdate>'.pwEscape($postdate_s);
			$urladd .= "&postdate_s=$postdate_s";
		}
		if ($postdate_e) {
			$sql .= $sql ? ' AND' : '';
			$sql .= ' postdate<'.pwEscape($postdate_e);
			$urladd .= "&postdate_e=$postdate_e";
		}
		$ordertype = $ordertype == 'asc' ? 'asc' : 'desc';
		$urladd .= "&ordertype=$ordertype&lines=$lines";
		$count = $db->get_value("SELECT COUNT(*) AS count FROM pw_comment WHERE $sql");
		empty($count) && adminmsg('comment_not_exist');
		!is_numeric($lines) && $lines=30;
		$page < 1 && $page = 1;
		$numofpage = ceil($count/$lines);
		if ($numofpage && $page > $numofpage) {
			$page = $numofpage;
		}
		$pages=numofpage($count,$page,$numofpage,"$basename&job=list$urladd&");
		$start = ($page-1)*$lines;
		$limit = pwLimit($start,$lines);
		$query = $db->query("SELECT id,uid,username,title,type,postdate FROM pw_comment WHERE $sql "."ORDER BY postdate $ordertype ".$limit);
		while ($rt = $db->fetch_array($query)) {
			$rt['s_title'] = substrs($rt['title'],40);
			$rt['ch_type'] = getLangInfo('other',$rt['type']);
			$rt['postdate'] = $rt['postdate'] ? get_date($rt['postdate']) : '-';
			$commentdb[] = $rt;
		}
		require_once PrintMode('comments');
		
	} elseif ($job == 'delete') {
		InitGP(array('selid','type','typeid','title','username','postdate_s','postdate_e','ordertype','page','lines'));
		empty($selid) && adminmsg("no_album_selid");
		if(!function_exists('countPosts')){
			require_once (R_P . 'mode/o/require/core.php');
		}
		foreach ($selid as $key => $id) {
			$thiscomm = $db->get_one("SELECT uid,type,typeid FROM pw_comment WHERE id=".pwEscape($id));
			$updatenum = 0;
			$db->update("DELETE FROM pw_comment WHERE id=".pwEscape($id));
			$updatenum += $db->affected_rows();
			$db->update("DELETE FROM pw_comment WHERE upid=".pwEscape($id));
			$updatenum += $db->affected_rows();
			list($app_table,$app_filed) = getCommTypeTable($thiscomm['type']);
			if ($updatenum && $app_table && $thiscomm['typeid']) {
				$db->update("UPDATE $app_table SET c_num=c_num-".pwEscape($updatenum)." WHERE $app_filed=".pwEscape($thiscomm['typeid']));
			}
			countPosts("-$updatenum");
		}
		adminmsg('operate_success',"$basename&job=list&title=".rawurlencode($title)."&username=".rawurlencode($username)."&type=$type&typeid=$typeid&postdate_s=$postdate_s&postdate_e=$postdate_e&ordertype=$ordertype&lines=$lines&page=$page&");
	}
}
?>