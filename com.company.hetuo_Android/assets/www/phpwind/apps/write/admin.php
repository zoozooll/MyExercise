<?php
!function_exists('adminmsg') && exit('Forbidden');

@include_once(D_P.'data/bbscache/o_config.php');
if (empty($action)) {
	if (empty($_POST['step'])) {
		
		require_once(R_P.'require/credit.php');
		!is_array($creditset = unserialize($o_write_creditset)) && $creditset = array();
		
		$creditlog = array();
		!is_array($write_creditlog = unserialize($o_write_creditlog)) && $write_creditlog = array();
		foreach ($write_creditlog as $key => $value) {
			foreach ($value as $k => $v) {
				$creditlog[$key][$k] = 'CHECKED';
			}
		}
		require_once PrintApp('write');
		
	} else {
		
		InitGP(array('creditset','creditlog'),'GP');
		
		$updatecache = false;
		$config['write_creditset'] = '';
		if (is_array($creditset) && !empty($creditset)) {
			foreach ($creditset as $key => $value) {
				foreach ($value as $k => $v) {
					$creditset[$key][$k] = round($v,($k=='rvrc' ? 1 : 0));
				}
			}
			$config['write_creditset'] = addslashes(serialize($creditset));
		}
		is_array($creditlog) && !empty($creditlog) && $config['write_creditlog'] = addslashes(serialize($creditlog));
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
		adminmsg('operate_success');
	}
} elseif ($action == 'writes') {
	if (empty($job)) {
		require_once PrintApp('write');
	} elseif ($job == 'list') {
		InitGP(array('content','username','postdate_s','postdate_e','ordertype','page','lines'));
		if (empty($content) && empty($username) && empty($postdate_s) && empty($postdate_e)) {
			adminmsg('noenough_condition',"$basename&action=writes");
		}
		
		$postdate_s && !is_numeric($postdate_s) && $postdate_s = PwStrtoTime($postdate_s);
		$postdate_e && !is_numeric($postdate_e) && $postdate_e = PwStrtoTime($postdate_e);
		$sql = $urladd = '';
		if ($content) {
			$content = str_replace('*','%',$content);
			$sql .= $sql ? ' AND' : '';
			$sql .= ' o.content LIKE '.pwEscape($content);
			$urladd .= '&content='.rawurlencode($content);
		}
		if ($username) {
			$username = str_replace('*','%',$username);
			$sql .= $sql ? ' AND' : '';
			$sql .= ' m.username LIKE '.pwEscape($username);
			$urladd .= '&username='.rawurlencode($username);
		}
		if ($postdate_s) {
			$sql .= $sql ? ' AND' : '';
			$sql .= ' o.postdate>='.pwEscape($postdate_s);
			$urladd .= "&postdate_s=$postdate_s";
		}
		if ($postdate_e) {
			$sql .= $sql ? ' AND' : '';
			$sql .= ' o.postdate<='.pwEscape($postdate_e);
			$urladd .= "&postdate_e=$postdate_e";
		}
		$ordertype = $ordertype == 'asc' ? 'asc' : 'desc';
		$urladd .= "&ordertype=$ordertype&lines=$lines";
		$count = $db->get_value("SELECT COUNT(*) AS count FROM pw_owritedata o LEFT JOIN pw_members m ON o.uid=m.uid WHERE $sql");
		empty($count) && adminmsg('write_not_exist',"$basename&action=writes");
		!is_numeric($lines) && $lines=30;
		$page < 1 && $page = 1;
		$numofpage = ceil($count/$lines);
		if ($numofpage && $page > $numofpage) {
			$page = $numofpage;
		}
		$pages=numofpage($count,$page,$numofpage,"$basename&action=writes&job=list$urladd&");
		$start = ($page-1)*$lines;
		$limit = pwLimit($start,$lines);
		$query = $db->query("SELECT o.id,o.uid,m.username,o.postdate,o.source,o.content,o.c_num FROM pw_owritedata o LEFT JOIN pw_members m ON o.uid=m.uid WHERE $sql "."ORDER BY postdate $ordertype ".$limit);
		while ($rt = $db->fetch_array($query)) {
			$rt['s_content'] = substrs($rt['content'],40);
			$rt['postdate'] = $rt['postdate'] ? get_date($rt['postdate']) : '-';
			$writedb[] = $rt;
		}
		require_once PrintApp('write');
	} elseif ($job == 'delete') {
		InitGP(array('selid','content','username','postdate_s','postdate_e','ordertype','page','lines'));
		empty($selid) && adminmsg("no_write_selid","$basename&action=writes");
		require_once("mode/o/require/core.php");
		foreach ($selid as $key => $id) {
			$writedb = $db->get_one("SELECT uid FROM pw_owritedata WHERE id=".pwEscape($id));
			if (empty($writedb)) {
				adminmsg('data_error',"$basename&action=writes");
			}
			$uids[] = $writedb['uid'];
			
			$db->update("DELETE FROM pw_owritedata WHERE id=".pwEscape($id));
			$affected_rows = delAppAction('write',$id)+1;
			countPosts("-$affected_rows");
		}
		$uids = array_unique($uids);
		updateUserAppNum($uids,'owrite','recount');
		adminmsg('operate_success',"$basename&action=writes&job=list&content=".rawurlencode($content)."&username=".rawurlencode($username)."&postdate_s=$postdate_s&postdate_e=$postdate_e&ordertype=$ordertype&lines=$lines&page=$page&");
		
	}
}
?>