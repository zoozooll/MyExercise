<?php
!defined('P_W') && exit('Forbidden');

class PwForum {

	var $db;
	var $fid;
	var $name;
	var $foruminfo = array();
	var $forumset = array();
	var $creditset = array();

	function PwForum($fid) {
		$this->fid		= $fid;
		$this->db		=& $GLOBALS['db'];
		$this->foruminfo= L::forum($fid);
		$this->name		= $this->foruminfo['name'];
		$this->forumset	= $this->foruminfo['forumset'];
		$this->creditset= $this->foruminfo['creditset'];
	}

	function isForum($allowcate = false) {
		if (empty($this->foruminfo) || !$allowcate && $this->foruminfo['type'] == 'category') {
			return false;
		}
		return true;
	}

	function isBM($username) {
		if (!$username) {
			return false;
		}
		if ($this->foruminfo['forumadmin'] && strpos($this->foruminfo['forumadmin'], ",$username,") !== false) {
			return true;
		}
		if ($this->foruminfo['fupadmin'] && strpos($this->foruminfo['fupadmin'], ",$username,") !== false) {
			return true;
		}
		return false;
	}

	function forumcheck($user,$groupid) {
		if ($this->foruminfo['f_type'] == 'former' && $groupid == 'guest' && $_COOKIE) {
			Showmsg('forum_former');
		}
		if (!empty($this->foruminfo['style']) && file_exists(D_P."data/style/{$this->foruminfo[style]}.php")) {
			$GLOBALS['skin'] = $this->foruminfo['style'];
		}
		$pwdcheck = GetCookie('pwdcheck');

		if ($this->foruminfo['password'] != '' && ($groupid == 'guest' || $pwdcheck[$this->fid] != $this->foruminfo['password'] && !CkInArray($user['username'], $GLOBALS['manager']))) {
			require_once(R_P.'require/forumpw.php');
		}
		if (!$this->allowvisit($user, $groupid)) {
			Showmsg('forum_jiami');
		}
		if (!$this->foruminfo['cms'] && $this->foruminfo['f_type'] == 'hidden' && !$this->foruminfo['allowvisit']) {
			Showmsg('forum_hidden');
		}
	}

	function creditcheck($user, $groupid) {
		if ($this->foruminfo['allowvisit']) {
			return;
		}
		$check = 1;
		$this->forumset['rvrcneed']		= intval($this->forumset['rvrcneed']);
		$this->forumset['moneyneed']	= intval($this->forumset['moneyneed']);
		$this->forumset['creditneed']	= intval($this->forumset['creditneed']);
		$this->forumset['postnumneed']	= intval($this->forumset['postnumneed']);

		if ($this->forumset['rvrcneed'] && intval($user['rvrc'] / 10) < $this->forumset['rvrcneed']) {
			$check = 0;
		} elseif ($this->forumset['moneyneed'] && $user['money'] < $this->forumset['moneyneed']) {
			$check = 0;
		} elseif ($this->forumset['creditneed'] && $user['credit'] < $this->forumset['creditneed']) {
			$check = 0;
		} elseif ($this->forumset['postnumneed'] && $user['postnum'] < $this->forumset['postnumneed']) {
			$check = 0;
		}
		if (!$check) {
			if ($groupid == 'guest') {
				Showmsg('forum_guestlimit');
			} else {
				$GLOBALS['forumset'] = $this->forumset;
				Showmsg('forum_creditlimit');
			}
		}
	}

	function allowvisit($user, $groupid) {
		return $this->allowcheck($this->foruminfo['allowvisit'], $groupid, $user['groups'], $user['visit']);
	}

	function allowpost($user, $groupid) {
		return $this->allowcheck($this->foruminfo['allowpost'], $groupid, $user['groups'], $user['post']);
	}

	function allowreply($user, $groupid) {
		return $this->allowcheck($this->foruminfo['allowrp'], $groupid, $user['groups'], $user['reply']);
	}

	function allowupload($user, $groupid) {
		return $this->allowcheck($this->foruminfo['allowupload'], $groupid, $user['groups']);
	}

	function allowtime($hours = null) {
		global $timestamp,$db_timedf;
		!$hours && $hours = gmdate('G',$timestamp + $db_timedf*3600);
		return $this->allowcheck($this->forumset['allowtime'], $hours, '');
	}

	function allowcheck($allowgroup, $groupid, $groups, $allowforum = '') {
		if (empty($allowgroup) || strpos($allowgroup,",$groupid,") !== false) {
			return true;
		}
		if ($groups) {
			foreach (explode(',', trim($groups,',')) as $value) {
				if (strpos($allowgroup,",$value,") !== false) {
					return true;
				}
			}
		}
		if ($allowforum && strpos(",$allowforum,",",$this->fid,") !== false) {
			return true;
		}
		return false;
	}

	function getUpForum() {
		global $forum,$fpage;
		isset($forum) || include(D_P.'data/bbscache/forum_cache.php');
		$upforum = array();
		$upforum[] = array(strip_tags($this->foruminfo['name']), "thread.php?fid={$this->fid}".($fpage>1 ? "&page=$fpage" : ''));
		$fup = $this->foruminfo['fup'];
		while ($fup > 0 && isset($forum[$fup]) && $forum[$fup]['type'] != 'category') {
			$upforum[] = array(strip_tags($forum[$fup]['name']), "thread.php?fid=$fup");
			$fup = $forum[$fup]['fup'];
		}
		return array_reverse($upforum);
	}

	function getTitle() {
		$upforum = $this->getUpForum();
		$headguide = array();
		foreach ($upforum as $key => $value) {
			if ($value[0]) {
				$value[1] && $value[0] = "<a href=\"$value[1]\">$value[0]</a>";
				$headguide[] = $value[0];
			}
		}
		$guidename = implode(' &raquo; ',$headguide);
		krsort($headguide);
		return array($guidename, strip_tags(implode('|',$headguide)).' - ');
	}

	function headguide($guidename,$onmouseover=true) {
		global $db_menu,$db_bbsname,$db_bfn,$imgpath,$db_menu,$db_mode,$db_bbsurl;
		if ($db_mode == 'bbs' && $db_bfn == 'index.php') {
			$db_bfn_temp = $db_bbsurl."/index.php?m=bbs";
		} else {
			$db_bfn_temp = $db_bfn;
		}
		if ($db_menu && $onmouseover) {
			$headguide = "<img style=\"cursor:pointer\" id=\"td_cate\" src=\"$imgpath/" . L::style('stylepath') . "/thread/home.gif\" onClick=\"return pwForumList(false,false,null,this);\" align=\"absmiddle\" /> <a href=\"$db_bfn_temp\" title=\"$db_bbsname\">$db_bbsname</a>" ;
		} else {
			$headguide = "<a href=\"$db_bfn\" title=\"$db_bbsname\">$db_bbsname</a>" ;
		}
		if (!is_array($guidename)) {
			return $headguide . ' &raquo; ' . $guidename;
		}
		foreach ($guidename as $key => $value) {
			if ($value[1]) {
				$headguide .= ' &raquo; <a href="'.$value[1].'">'.$value[0].'</a>';
			} else {
				$headguide .= ' &raquo; '.$value[0];
			}
		}
		return $headguide;
	}

	function isOpen() {
		return (!$this->foruminfo['allowvisit'] && $this->foruminfo['f_type'] != 'hidden' && !$this->foruminfo['password'] && !$this->foruminfo['forumsell']);
	}

	function forumBan($udb) {
		$retu = $uids = array();
		if (isset($udb['groupid']) && isset($udb['userstatus'])) {
			if ($udb['groupid'] == 6) {
				$retu[$udb['uid']] = 1;
			} elseif (getstatus($udb['userstatus'], 1) && ($rt = $this->db->get_one("SELECT uid FROM pw_banuser WHERE uid=" . pwEscape($udb['uid']) . " AND fid=" . pwEscape($this->fid)))) {
				$retu[$udb['uid']] = 2;
			}
		} else {
			foreach ($udb as $key => $u) {
				if ($u['groupid'] == 6) {//是否全局禁言
					$retu[$u['uid']] = 1;
				} elseif (getstatus($u['userstatus'], 1)) {//是否版块禁言
					$uids[] = $u['uid'];
				}
			}
			if ($uids) {
				$uids = pwImplode($uids);
				$query = $this->db->query("SELECT uid FROM pw_banuser WHERE uid IN ($uids) AND fid=" . pwEscape($this->fid));
				while ($rt = $this->db->fetch_array($query)) {
					$retu[$rt['uid']] = 2;
				}
			}
		}
		return $retu;
	}

	function lastinfo($type, $action = '+', $lastpost = array()) {
		global $db_htmdir,$R_url;
		$lp = $topicadd = $fupadd = '';

		if ($action == '+' || $action == '-') {
			if ($type == 'topic') {
				$topicadd = "tpost=tpost{$action}'1',article=article{$action}'1',topic=topic{$action}'1' ";
				$fupadd   = "tpost=tpost{$action}'1',article=article{$action}'1',subtopic=subtopic{$action}'1' ";
			} else {
				$topicadd = "tpost=tpost{$action}'1',article=article{$action}'1' ";
				$fupadd   = "tpost=tpost{$action}'1',article=article{$action}'1' ";
			}
		}
		if ($lastpost) {
			$newurl = "read.php?tid=$lastpost[tid]&page=e#a";
			if ($this->foruminfo['allowhtm']) {
				$htmurl = $db_htmdir.'/'.$this->fid.'/'.date('ym',$lastpost['t_date']).'/'.$lastpost['tid'].'.html';
				if (file_exists(R_P . $htmurl)) {
					$newurl = "$R_url/$htmurl";
				}
			}
			$lp = "lastpost=" . pwEscape($lastpost['subject']."\t".$lastpost['author']."\t".$lastpost['lastpost']."\t".$newurl);
		}
		if ($topicadd || $lp) {
			$sql = trim($topicadd . ',' . $lp, ',');
			$this->db->update("UPDATE pw_forumdata SET $sql WHERE fid=".pwEscape($this->fid));
		}
		if ($this->foruminfo['type'] == 'sub' || $this->foruminfo['type'] == 'sub2') {
			!$this->isOpen() && $lp = '';
			if ($lp || $fupadd) {
				$sql = trim($fupadd . ',' . $lp, ',');
				$this->db->update("UPDATE pw_forumdata SET $sql WHERE fid=" . pwEscape($this->foruminfo['fup']));
				if ($this->foruminfo['type'] == 'sub2') {
					$rt1 = $this->db->get_one("SELECT fup FROM pw_forums WHERE fid=" . pwEscape($this->foruminfo['fup']));
					$this->db->update("UPDATE pw_forumdata SET $sql WHERE fid=" . pwEscape($rt1['fup']));
				}
			}
		}
	}
}
?>