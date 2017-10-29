<?php
!function_exists('readover') && exit('Forbidden');
/*
 * ElementUpdate class
 * @copyright PHPWind
 * @author xiaolang
 */

class ElementUpdate{
	var $db;
	var $ifcache;
	var $cachenum;
	var $mark;
	var $updatelist;
	var $updatetype;
	var $judge;
	var $special;

	function ElementUpdate($mark=null){
		global $db,$db_ifpwcache,$db_cachenum;

		$this->db 			= $db;
		$this->ifcache		= $db_ifpwcache;
		$this->cachenum 	= $db_cachenum ? $db_cachenum : 20;
		$this->updatelist 	= array();
		$this->updatetype 	= array();
		$this->mark			= $mark;
	}

	/**
	 * user sort update
	 *
	 * @param array $winddb
	 */
	function userSortUpdate($winddb){
		global $timestamp,$tdtime,$montime,$_CREDITDB;
		$usersort_judge = array();
		include(D_P.'data/bbscache/usersort_judge.php');
		$winddb['lastpost']<$tdtime && $winddb['todaypost'] = 0;
		$winddb['lastpost']<$montime && $winddb['monthpost'] = 0;
		$sorttype = array('money','rvrc','credit','currency','todaypost','monthpost','postnum','monoltime','onlinetime','digests','f_num');
		if ($_CREDITDB) {
			$query = $this->db->query("SELECT cid,value FROM pw_membercredit WHERE uid=".pwEscape($winddb['uid']));
			while ($rt = $this->db->fetch_array($query)) {
				if (!$rt['value']) continue;
				$winddb[$rt['cid']] = $rt['value'];
			}
			foreach ($_CREDITDB as $key => $val) {
				is_numeric($key) &&	$sorttype[] = $key;
			}
		}
		$change = $marks = array();
		foreach ($sorttype as $value) {
			if ($winddb[$value] > $usersort_judge[$value]) {
				$marks [] = $value;
				if ($value == 'rvrc') {
					$winddb[$value] = floor($winddb[$value]/10);
				} elseif($value == 'onlinetime' || $value == 'monoltime') {
					$winddb[$value] = floor($winddb[$value]/3600);
				}
				$change[] = array('usersort',$value,$winddb['uid'],$winddb[$value],$winddb['username'],$timestamp);
			}
		}
		$rand_array	= array('todaypost','monthpost','monoltime');
		$rand_key	= array_rand($rand_array);
		$rand_mark	= $rand_array[$rand_key];
		if (!in_array($rand_mark,$marks)) {
			$marks[]= $rand_mark;
		}
		if ($marks && $change) {
			$this->db->update("REPLACE INTO pw_elements(type,mark,id,value,addition,time) VALUES ".pwSqlMulti($change,false));
			$sortlist = array();
			$dellist = array();
			$query = $this->db->query("SELECT * FROM pw_elements WHERE type='usersort' AND mark IN (".pwImplode($marks).") ORDER BY mark,value DESC");
			while ($rt = $this->db->fetch_array($query)) {
				if (($rt['mark']=='todaypost' && $rt['time']<$tdtime) || (in_array($rt['mark'],array('monthpost','monoltime')) && $rt['time']<$montime) ) {
					$dellist[] = $rt['eid'];
					continue;
				}
				$sortlist[$rt['mark']][] = $rt;
			}
			$judge = $usersort_judge;
			foreach ($sortlist as $key => $value) {
				if (count($value)>$this->cachenum) {
					$tem = array_pop($value);
					$dellist[] = $tem['eid'];
				}
				if (count($value)==$this->cachenum) {
					$tem = end($value);
					$judge[$key] = $tem['value'];
				} else {
					$judge[$key] = '0';
				}
			}
			if ($dellist) {
				$this->db->update("DELETE FROM pw_elements WHERE eid IN (".pwImplode($dellist).")");
			}
			if ($judge!=$usersort_judge) {
				writeover(D_P.'data/bbscache/usersort_judge.php',"<?php\r\n\$usersort_judge=".pw_var_export($judge).";\r\n?>");
			}
		}
	}

	/**
	 * hot favor update
	 *
	 * @param int $tid
	 * @param int $fid
	 * @return
	 */
	function hotfavorUpdate($tid,$fid){

		if (!($this->ifcache & 1024) || !$tid || !$fid) {
			return false;
		}
		$eid = $this->db->get_value("SELECT eid FROM pw_elements WHERE type='hotfavor' AND mark=".pwEscape($fid)." AND id=".pwEscape($tid));

		if ($eid) {
			$this->db->update("UPDATE pw_elements SET value=value+1 WHERE eid=".pwEscape($eid));
		} else {
			$rt = $this->db->get_one("SELECT favors FROM pw_threads WHERE tid=".pwEscape($tid));
			$rs = $this->db->get_one("SELECT value,eid FROM pw_elements WHERE type='hotfavor' ORDER BY value ASC");

			if ($rt['favors'] > $rs['value']) {
				$this->db->update("DELETE FROM pw_elements WHERE eid=".pwEscape($rs['eid']));
				$favors = array(
					'id' => $tid,
					'mark' => $fid,
					'value' => $rt['favors'],
					'type' => 'hotfavor',
				);
				$this->db->update("REPLACE INTO pw_elements SET".pwSqlSingle($favors,false));
			}
		}
		return true;
	}

	/**
	 * new favor update
	 *
	 * @param int $tid
	 * @param int $fid
	 * @return
	 */
	function newfavorUpdate($tid,$fid){
		global $timestamp,$winduid,$windid;
		if (!$tid || !$fid) {
			return false;
		}

		$eid = $this->db->get_value("SELECT eid FROM pw_elements WHERE type='newfavor' AND mark=".pwEscape($fid)." AND id=".pwEscape($tid));

		if ($eid) {
			$this->db->update("UPDATE pw_elements SET value=value+1 WHERE eid=".pwEscape($eid));
		} else {
			$count = $this->db->get_value("SELECT COUNT(*) as count FROM pw_elements WHERE type='newfavor' AND mark=".pwEscape($fid));
			$rt = $this->db->get_one("SELECT favors FROM pw_threads WHERE tid=".pwEscape($tid));

			if ($count < 20) {
				$favors = array(
					'id'	=> $tid,
					'mark'	=> $fid,
					'value' => $rt['favors'],
					'type'	=> 'newfavor',
					'addition'	=> $winduid.'|'.$windid,
					'time'	=> $timestamp
				);
				$this->db->update("REPLACE INTO pw_elements SET".pwSqlSingle($favors,false));
			} else {
				$rs = $this->db->get_one("SELECT eid FROM pw_elements WHERE type='newfavor' AND mark=".pwEscape($fid)." ORDER BY time ASC");
				$favors = array(
					'id'	=> $tid,
					'mark'	=> $fid,
					'value' => $rt['favors'],
					'type'	=> 'newfavor',
					'addition'	=> $winduid.'|'.$windid,
					'time'	=> $timestamp
				);
				$this->db->update("UPDATE pw_elements SET".pwSqlSingle($favors,false)." WHERE eid=".pwEscape($rs['eid']));
			}
		}
		return true;
	}

	/**
	 * hits sort update
	 *
	 * @param array $threaddb
	 * @param int $fid
	 * @return
	 */
	function hitSortUpdate($threaddb,$fid){
		if (!($this->ifcache & 112)) {
			return false;
		}
		if (!$this->judge['hitsort']) {
			$hitsort_judge = array();
			include(D_P.'data/bbscache/hitsort_judge.php');
			$this->judge['hitsort'] = $hitsort_judge;
		}
		foreach ($threaddb as $thread) {
			$thread['postdate'] = PwStrtoTime($thread['postdate']);
			if ($this->ifcache & 16) {
				if ($thread['hits']>$hitsort_judge['hitsort'][$fid]) {
					$this->updatelist[] = array('hitsort',$fid,$thread['tid'],$thread['hits'],'',0);
					$this->updatetype['hitsort'] = 1;
				}
			}
			if ($this->ifcache & 32 && $thread['postdate']>24*3600) {
				if ($thread['hits']>$hitsort_judge['hitsortday'][$fid]) {
					$this->updatelist[] = array('hitsortday',$fid,$thread['tid'],$thread['hits'],$thread['postdate'],0);
					$this->updatetype['hitsortday'] = 1;
				}
			}

			if ($this->ifcache & 64 && $thread['postdate']>7*24*3600) {
				if ($thread['hits']>$hitsort_judge['hitsortweek'][$fid]) {
					$this->updatelist[] = array('hitsortweek',$fid,$thread['tid'],$thread['hits'],$thread['postdate'],0);
					$this->updatetype['hitsortweek'] = 1;
				}
			}
		}
		return true;
	}
	/**
	 * reply sort update
	 *
	 * @param int $tid
	 * @param int $fid
	 * @param string $postdate
	 * @param int $replies
	 * @return
	 */
	function replySortUpdate($tid,$fid,$postdate,$replies){
		global $timestamp;
		if (!($this->ifcache & 14)) {
			return false;
		}
		$special = (int) $this->special;
		if (!$this->judge['replysort']) {
			$replysort_judge = array();
			include Pcv(D_P.'data/bbscache/replysort_judge_'.$special.'.php');
			$this->judge['replysort'] = $replysort_judge;
		}

		if ($this->ifcache & 2) {
			if ($replies>$replysort_judge['replysort'][$fid]) {
				$this->updatelist[] = array('replysort',$fid,$tid,$replies,'',$special);
				$this->updatetype['replysort'] 	= 1;
			}
		}
		if ($this->ifcache & 4 && $postdate>$timestamp-24*3600) {
			if ($replies>$this->judge['replysort']['replysortday'][$fid]) {
				$this->updatelist[] = array('replysortday',$fid,$tid,$replies,$postdate,$special);
				$this->updatetype['replysortday'] = 1;
			}
		}

		if ($this->ifcache & 8 && $postdate>$timestamp-7*24*3600) {
			if ($replies>$this->judge['replysort']['replysortweek'][$fid]) {
				$this->updatelist[] = array('replysortweek',$fid,$tid,$replies,$postdate,$special);
				$this->updatetype['replysortweek'] = 1;
			}
		}
		return true;
	}
	/**
	 * new subject update
	 *
	 * @param int $tid
	 * @param int $fid
	 * @param string $postdate
	 * @return
	 */
	function newSubjectUpdate($tid,$fid,$postdate){
		if (!($this->ifcache & 128)) {
			return false;
		}
		$this->updatelist[] = array('newsubject',$fid,$tid,$postdate,'','0');
		$this->updatetype['newsubject'] = 1;
		return true;
	}
	/**
	 * new reply update
	 *
	 * @param int $tid
	 * @param int $fid
	 * @param string $postdate
	 * @return
	 */
	function newReplyUpdate($tid,$fid,$postdate){
		if (!($this->ifcache & 256)) {
			return false;
		}
		$this->updatelist[] = array('newreply',$fid,$tid,$postdate,'','0');
		$this->updatetype['newreply'] = 1;
		return true;
	}
	/**
	 * new reply update
	 *
	 * @param int $tid
	 * @param int $fid
	 * @param string $postdate
	 * @return
	 */
	function newPicUpdate($aid,$fid,$tid,$addition,$ifthumb = 0,$atc_content){
		if (!($this->ifcache & 512)) {
			return false;
		}
		$ifthumb = (int)$ifthumb;
		$atc_content = substrs(stripWindCode($atc_content),30);
		$additions = array('0' => $addition,'1' => $atc_content);
		$addition = addslashes(serialize($additions));
		$this->updatelist[] = array('newpic',$fid,$tid,$aid,$addition,$ifthumb);
		$this->updatetype['newpic'] = 1;
		return true;
	}

	function setMark($mark){
		$this->mark = $mark;
	}

	function setUpdateList($updatelist){
		$this->updatelist = $updatelist;
	}

	function setUpdateType($updatetype){
		$this->updatetype = $updatetype;
	}

	function setJudge($key,$value){
		$this->judge[$key] = $value;
	}
	/**
	 * update list
	 *
	 * @return
	 */
	function updateSQL(){
		global $timestamp;
		if (!$this->updatelist || !$this->updatetype || !$this->mark) return false;
		$special = (int) $this->special;
		$judges = array();$todaytime = $weektime = '';
		foreach ($this->updatetype as $key=>$val) {
			if (in_array($key,array('replysort','replysortday','replysortweek')) && $this->judge['replysort']) {
				$judges['replysort'] = $this->judge['replysort'];
			}
			if (in_array($key,array('hitsort','hitsortday','hitsortweek')) && $this->judge['hitsort']) {
				$judges['hitsort'] = $this->judge['hitsort'];
			}
			if (strpos($key,'day') && !$todaytime) {
				$todaytime = $timestamp-24*3600;
			} elseif (strpos($key,'week') && !$weektime) {
				$weektime = $timestamp-7*24*3600;
			}
		}
		$this->db->update("REPLACE INTO pw_elements (type,mark,id,value,addition,special) VALUES ".pwSqlMulti($this->updatelist,false));
		$sortlist	= array();
		$dellis		= array();
		$query = $this->db->query("SELECT eid,type,value,addition FROM pw_elements WHERE type IN (".pwImplode(array_keys($this->updatetype)).") AND mark=".pwEscape($this->mark)." AND special=".pwEscape($special)." ORDER BY type,value DESC");
		while ($rt = $this->db->fetch_array($query)) {
			if (strpos($rt['type'],'day') && $rt['addition'] && $rt['addition'] < $todaytime) {
				$dellist[] = $rt['eid'];
			} elseif (strpos($rt['type'],'week') && $rt['addition'] && $rt['addition'] < $weektime) {
				$dellist[] = $rt['eid'];
			} else {
				$sortlist[$rt['type']][] = $rt;
			}
		}

		foreach ($sortlist as $key => $value) {
			if (count($value)>$this->cachenum) {
				$tem = array_slice($value,$this->cachenum);
				foreach ($tem as $val) {
					$dellist[] = $val['eid'];
				}
			}
			if (in_array($key,array('replysort','replysortday','replysortweek'))) {
				$judgetype = 'replysort';
				array_splice($value,$this->cachenum);
			} elseif (in_array($key,array('hitsort','hitsortday','hitsortweek'))) {
				$judgetype = 'hitsort';
				array_splice($value,$this->cachenum);
			} else {
				$judgetype = '';
			}
			if ($judgetype && count($value)==$this->cachenum) {
				$tem = end($value);
				$judges[$judgetype][$key][$this->mark] = $tem['value'];
			} else {
				$judges[$judgetype][$key][$this->mark] = '0';
			}
		}
		if ($dellist) {
			$this->db->update("DELETE FROM pw_elements WHERE eid IN (".pwImplode($dellist).")");
		}
		if ($judges) {
			foreach ($judges as $key => $value) {
				if ($key == 'replysort') {
					if ($value!=$this->judge['replysort']) {
						writeover(D_P.'data/bbscache/replysort_judge_'.$special.'.php',"<?php\r\n\$replysort_judge=".pw_var_export($value).";\r\n?>");
					}
				} elseif ($key == 'hitsort') {
					writeover(D_P.'data/bbscache/hitsort_judge.php',"<?php\r\n\$hitsort_judge=".pw_var_export($value).";\r\n?>");
				}
			}
		}
		return true;
	}
/*
	function newFeedUpdate() {
		global $db_modes,$timestamp,$db;
		if ($db_modes['o']['ifopen'] == 0) {
			return false;
		}

		if ($timestamp - pwFilemtime(D_P.'data/bbscache/feed_cache.php') > 3600 || !file_exists(D_P.'data/bbscache/feed_cache.php')){
			$query = $db->query("SELECT * FROM pw_feed ORDER BY timestamp DESC LIMIT 10");
			while ($rt = $db->fetch_array($query)) {
				$feeddb[] = $rt;
			}
			if ($feeddb){
				writeover(D_P.'data/bbscache/feed_cache.php',"<?php\r\n\$feedcache=".pw_var_export($feeddb).";\r\n?>");
			}
		}
	}
*/
}
?>