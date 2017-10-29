<?php
!function_exists('readover') && exit('Forbidden');

/**
 *
 * PHPWind 积分操作统一入口
 * author:sky_hold@163.com
 *
 */
Class PwCredit {

	var $cType = array();
	var $cUnit = array();
	var $Field = array();
	var $cLog  = array();
	var $setUser = array();
	var $getUser = array();

	function PwCredit() {
		$this->cType = array(
			'money'		=> $GLOBALS['db_moneyname'],
			'rvrc'		=> $GLOBALS['db_rvrcname'],
			'credit'	=> $GLOBALS['db_creditname'],
			'currency'	=> $GLOBALS['db_currencyname']
		);
		$this->cUnit = array(
			'money'		=> $GLOBALS['db_moneyunit'],
			'rvrc'		=> $GLOBALS['db_rvrcunit'],
			'credit'	=> $GLOBALS['db_creditunit'],
			'currency'	=> $GLOBALS['db_currencyunit']
		);
		foreach ($GLOBALS['_CREDITDB'] as $key => $value) {
			$this->cType[$key] = $value[0];
			$this->cUnit[$key] = $value[1];
		}
		$this->Field = array('postnum', 'digests');
	}

	function creditset($f_set,$d_set) {
		if (!is_array($f_set)) $f_set = unserialize($f_set);
		if (!is_array($d_set)) $d_set = unserialize($d_set);

		foreach ($d_set as $key => $value) {
			foreach ($value as $k => $v) {
				isset($f_set[$key][$k]) && $f_set[$key][$k] !== '' && $v = $f_set[$key][$k];
				if (!in_array($key,array('Digest','Post','Reply'))) {
					$v = -$v;
				}
				$d_set[$key][$k] = $v;
			}
		}
		return $d_set;
	}

	function check($t) {
		return (isset($this->cType[$t]) || in_array($t,$this->Field)) ? true : false;
	}

	function setMdata($uid,$field,$point) {
		if ($this->check($field)) {
			$this->setUser[$uid][$field] += $point;
		}
	}

	function get($uid,$cType = 'ALL') {
		global $db;
		$getv = false;
		if (isset($this->cType[$cType])) {
			if (isset($this->getUser[$uid][$cType])) return $this->getUser[$uid][$cType];
			if (is_numeric($cType)) {
				$getv = $db->get_value('SELECT value FROM pw_membercredit WHERE uid=' . pwEscape($uid) . ' AND cid=' . pwEscape($cType));
				empty($getv) && $getv = 0;
			} else {
				$getv = $db->get_value("SELECT $cType FROM pw_memberdata WHERE uid=" . pwEscape($uid));
				$cType == 'rvrc' && $getv = intval($getv/10);
			}
			$this->getUser[$uid][$cType] = $getv;
		}
		if (in_array($cType,array('ALL','COMMON','CUSTOM'))) {
			$getv = array();
			if ($cType != 'CUSTOM') {
				$getv = $db->get_one("SELECT money,FLOOR(rvrc/10) AS rvrc,credit,currency FROM pw_memberdata WHERE uid=" . pwEscape($uid));
			}
			if ($GLOBALS['_CREDITDB'] && $cType != 'COMMON') {
				$query = $db->query("SELECT cid,value FROM pw_membercredit WHERE uid=" . pwEscape($uid));
				while ($rt = $db->fetch_array($query)) {
					$getv[$rt['cid']] = $rt['value'];
				}
			}
			$this->getUser[$uid] = $getv;
		}
		return $getv;
	}

	function set($uid, $cType, $point, $operate = true) {
		if (!isset($this->cType[$cType]) || empty($point)) {
			return false;
		}
		$arr = array(
			$uid => array($cType => $point)
		);
		if ($operate) {
			$this->runsql($arr);
		} else {
			$this->array_add($arr);
		}
		return true;
	}

	function sets($uid, $setv, $operate = true) {
		if (empty($setv) || !is_array($setv)) {
			return false;
		}
		if ($operate) {
			$this->runsql(array($uid => $setv));
		} else {
			$this->array_add(array($uid => $setv));
		}
		return true;
	}

	function setus($u_array, $setv, $operate = true) {
		if (empty($u_array) || !is_array($u_array) || empty($setv) || !is_array($setv)) {
			return false;
		}
		$arr = array();
		foreach ($u_array as $uid) {
			$arr[$uid] = $setv;
		}
		if ($operate) {
			$this->runsql($arr);
		} else {
			$this->array_add($arr);
		}
		return true;
	}

	function runsql($setArr = null, $isAdd = true) {
		global $db,$uc_server,$uc_syncredit;
		$setUser = isset($setArr) ? $setArr : $this->setUser;
		$retv = array();
		if ($uc_server && $uc_syncredit) {
			require_once(R_P . 'uc_client/uc_client.php');
			$retv = uc_credit_add($setUser, $isAdd);
		}
		$cacheUids = $cacheCredits = array();
		foreach ($setUser as $uid => $setv) {
			$sql = '';
			foreach ($setv as $cid => $v) {
				if ($this->check($cid) && ($v <> 0 || !$isAdd)) {
					if (isset($retv[$uid][$cid])) {
						if ($uc_server == 1) {
							continue;
						}
						$act = 'set';
						$v = $retv[$uid][$cid];
					} else {
						$act = $isAdd ? 'add' : 'set';
					}
					if (is_numeric($cid)) {
						$v = intval($v);
						$db->pw_update(
							"SELECT uid FROM pw_membercredit WHERE uid=" . pwEscape($uid) . ' AND cid=' . pwEscape($cid),
							"UPDATE pw_membercredit SET " . ($act == 'add' ? 'value=value+' : 'value=') . pwEscape($v) .  ' WHERE uid=' . pwEscape($uid) . ' AND cid=' . pwEscape($cid),
							"INSERT INTO pw_membercredit SET " . pwSqlSingle(array('uid' => $uid, 'cid' => $cid, 'value' => $v))
						);
					} else {
						$cid == 'rvrc' && $v *= 10;
						$sql .= ($act == 'add' ? ",$cid=$cid+" : ",$cid=") . intval($v);
					}
				}
			}
			if ($sql) {
				$db->update("UPDATE pw_memberdata SET " . ltrim($sql, ',') . " WHERE uid=" . pwEscape($uid),0);
			}
			unset($this->getUser[$uid]);
			$cacheUids[] = 'UID_'.$uid;
			$cacheCredits[] = 'UID_CREDIT_'.$uid;
		}
		if ($cacheUids) {
			$_cache = getDatastore();
			$_cache->delete($cacheUids);
			$_cache->delete($cacheCredits);/*积分*/
		}
		$this->writeLog();
		!isset($setArr) && $this->setUser = array();
	}

	function addLog($logtype, $setv, $log) {
		global $db_creditlog,$db_ifcredit,$timestamp;
		list($lgt) = explode('_', $logtype);
		$credit_pop = '';
		$uid = $log['uid'];

		foreach ($setv as $key => $affect) {
			if (isset($this->cType[$key]) && $affect<>0 && ($lgt == 'main' || isset($db_creditlog[$lgt][$key]))) {
				$log['username'] = Char_cv($log['username']);
				$log['cname']	 = $this->cType[$key];
				$log['affect']	 = $affect;
				$log['affect'] > 0 && $log['affect'] = '+'.$log['affect'];
				$log['descrip'] = Char_cv(strip_tags(getLangInfo('creditlog',$logtype,$log)));
				$credit_pop .= $key.":".$log['affect'].'|';
				$this->cLog[] = array($log['uid'], $log['username'], $key, $affect, $timestamp, $logtype, $log['ip'], $log['descrip']);
			}
		}
		if ($db_ifcredit && $credit_pop) {//Credit Changes Tips
			$credit_pop = $logtype.'|'.$credit_pop;
			$GLOBALS['db']->update("UPDATE pw_memberdata SET creditpop=".pwEscape($credit_pop)." WHERE uid=".pwEscape($uid),0);
		}
	}

	function writeLog() {
		if (!empty($this->cLog)) {
			$GLOBALS['db']->update("INSERT INTO pw_creditlog (uid,username,ctype,affect,adddate,logtype,ip,descrip) VALUES ".pwSqlMulti($this->cLog,false));
		}
		$this->cLog = array();
	}

	function array_add($u_a) {
		if (empty($u_a)) return false;
		foreach ($u_a as $uid => $setv) {
			foreach ($setv as $key => $value) {
				if (isset($this->cType[$key]) && is_numeric($value) && $value <> 0) {
					$this->setUser[$uid][$key] += $value;
					isset($this->getUser[$uid][$key]) && $this->getUser[$uid][$key] += $value;
				}
			}
		}
	}
}

$credit = new PwCredit();

?>