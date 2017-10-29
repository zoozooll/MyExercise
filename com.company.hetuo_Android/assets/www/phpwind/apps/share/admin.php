<?php
!function_exists('adminmsg') && exit('Forbidden');

@include_once(D_P.'data/bbscache/o_config.php');

if (empty($action)) {
	if (empty($_POST['step'])) {
		$creategroup = ''; $num = 0;
		foreach ($ltitle as $key => $value) {
			if ($key != 1 && $key != 2 && $key !='6' && $key !='7' && $key !='3') {
				$num++;
				$htm_tr = $num % 4 == 0 ? '' : '';
				$g_checked = strpos($o_share_groups,",$key,") !== false ? 'checked' : '';
				$creategroup .= "<li><input type=\"checkbox\" name=\"groups[]\" value=\"$key\" $g_checked>$value</li>$htm_tr";
			}
		}
		$creategroup && $creategroup = "<ul class=\"list_A list_120 cc\">$creategroup</ul>";
		ifcheck($db_share_open,'share_open');
		ifcheck($o_share_gdcheck,'share_gdcheck');
		ifcheck($o_share_qcheck,'share_qcheck');
		require_once(R_P.'require/credit.php');
		!is_array($creditset = unserialize($o_share_creditset)) && $creditset = array();
		
		$creditlog = array();
		!is_array($share_creditlog = unserialize($o_share_creditlog)) && $share_creditlog = array();
		foreach ($share_creditlog as $key => $value) {
			foreach ($value as $k => $v) {
				$creditlog[$key][$k] = 'CHECKED';
			}
		}
		require_once PrintApp('share');
	} else {
		InitGP(array('config','groups','share_open','creditset','creditlog'),'GP',2);

		require_once(R_P.'admin/cache.php');
		setConfig('db_share_open', $share_open);
		updatecache_c();

		$config['share_groups'] = is_array($groups) ? ','.implode(',',$groups).',' : '';
		$updatecache = false;
		
		$config['share_creditset'] = '';
		if (is_array($creditset) && !empty($creditset)) {
			foreach ($creditset as $key => $value) {
				foreach ($value as $k => $v) {
					$creditset[$key][$k] = round($v,($k=='rvrc' ? 1 : 0));
				}
			}
			$config['share_creditset'] = addslashes(serialize($creditset));
		}
		is_array($creditlog) && !empty($creditlog) && $config['share_creditlog'] = addslashes(serialize($creditlog));
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
} elseif ($action == 'share') {
	if (empty($job)) {
		require_once PrintApp('share');
	} elseif ($job == 'list') {
		InitGP(array('type','ifhidden','username','postdate_s','postdate_e','ordertype','page','lines'));
		if (empty($type) && empty($username) && empty($postdate_s) && empty($postdate_e)) {
			adminmsg('noenough_condition',"$basename&action=share");
		}
		$postdate_s && !is_numeric($postdate_s) && $postdate_s = PwStrtoTime($postdate_s);
		$postdate_e && !is_numeric($postdate_e) && $postdate_e = PwStrtoTime($postdate_e);
		$sql = $urladd = '';
		if ($type) {
			$sql .= $sql ? ' AND' : '';
			$sql .= ' type='.pwEscape($type);
			$urladd .= '&type='.$type;
		}
		if ($ifhidden != -1) {
			$sql .= $sql ? ' AND' : '';
			$sql .= ' ifhidden='.pwEscape($ifhidden);
			$urladd .= '&ifhidden='.$ifhidden;
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
		$count = $db->get_value("SELECT COUNT(*) AS count FROM pw_share WHERE $sql");
		empty($count) && adminmsg('share_not_exist',"$basename&action=share");
		!is_numeric($lines) && $lines=30;
		$page < 1 && $page = 1;
		$numofpage = ceil($count/$lines);
		if ($numofpage && $page > $numofpage) {
			$page = $numofpage;
		}
		$pages=numofpage($count,$page,$numofpage,"$basename&action=share&job=list$urladd&");
		$start = ($page-1)*$lines;
		$limit = pwLimit($start,$lines);
		$query = $db->query("SELECT * FROM pw_share WHERE $sql "."ORDER BY postdate $ordertype ".$limit);
		while ($rt = $db->fetch_array($query)) {
			$rt['postdate'] = $rt['postdate'] ? get_date($rt['postdate']) : '-';
			$temp = unserialize($rt['content']);
			$rt['link']	= $temp['link'];
			if ($rt['type']=='user') {
				$rt['title']= "<a href=\"$rt[link]\" target=\"_blank\">".$temp['user']['username']."</a>";
			} elseif ($rt['type']=='photo') {
				$belong	= getLangInfo('app','photo_belong');
				$rt['image']	= $temp['photo']['image'];
				$temp_uid	= $temp['photo']['uid'];
				$rt['title']= $belong."<a href=\"u.php?uid=$temp_uid\" target=\"_blank\">".$temp['photo']['username']."</a>";
			} elseif ($rt['type']=='album') {
				$belong	= getLangInfo('app','photo_belong');
				$temp_uid	= $temp['album']['uid'];
				$rt['title']= $belong."<a href=\"u.php?uid=$temp_uid\" target=\"_blank\">".$temp['album']['username']."</a>";
			} elseif ($rt['type']=='group') {
				$rt['title']= "<a href=\"$rt[link]\" target=\"_blank\">".$temp['group']['name']."</a>";
			} elseif ($rt['type']=='diary') {
				$rt['title']= "<a href=\"$rt[link]\" target=\"_blank\">".$temp['diary']['subject']."</a>";
			} else {
				$rt['title']= "<a href=\"$rt[link]\" target=\"_blank\">".substrs($rt['link'],40)."</a>";
			}
			$sharedb[] = $rt;
		}
		require_once PrintApp('share');
	} elseif ($job == 'delete') {
		InitGP(array('selid','type','ifhidden','username','postdate_s','postdate_e','ordertype','page','lines'));
		empty($selid) && adminmsg("no_share_selid","$basename&action=share");
		require_once("mode/o/require/core.php");
		foreach ($selid as $key => $id) {
			$sharedb = $db->get_one("SELECT uid FROM pw_share WHERE id=".pwEscape($id));
			if (empty($sharedb)) {
				adminmsg('data_error',"$basename&action=share");
			}
			$uids[] = $sharedb['uid'];
			$db->update("DELETE FROM pw_share WHERE id=".pwEscape($id));
			if ($affected_rows = delAppAction('share',$id)) {
				countPosts("-$affected_rows");
			}
		}
		$uids = array_unique($uids);
		updateUserAppNum($uids,'share','recount');
		adminmsg('operate_success',"$basename&action=share&job=list&type=$type&username=".rawurlencode($username)."&ifhidden=$ifhidden&postdate_s=$postdate_s&postdate_e=$postdate_e&ordertype=$ordertype&lines=$lines&page=$page&");
	}
}
?>