<?php
!defined('P_W') && exit('Forbidden');

class PW_AttachsDB extends BaseDB {

	var $_tableName = "pw_attachs";

	function add($fieldData) {
		$fieldData = $this->_checkData($fieldData);
		$this->_db->update("INSERT INTO ".$this->_tableName. " SET " . $this->_getUpdateSqlString ( $fieldData ) );
		return $this->_db->insert_id();
	}

	function delete($aids) {
		if (empty($aids)) return false;
		if (is_array($aids)) {
			$this->_db->update("DELETE FROM ".$this->_tableName." WHERE aid IN (". $this->_getImplodeString($aids).")");
		} else {
			$this->_db->update("DELETE FROM ".$this->_tableName." WHERE aid=".intval($aids));
		}
		return true;
	}

	function delByTid($tid,$pid=null) {
		$this->_db->update("DELETE FROM ".$this->_tableName." WHERE tid=".intval($tid).(!is_null($pid) ? " AND pid=".intval($pid) : ""));
		return true;
	}

	function getByTid($tid,$pid=null) {
		if (is_array($tid)) {
			$sql = "SELECT * FROM ".$this->_tableName." WHERE tid IN (".$this->_getImplodeString($tid).")";
		} else {
			$sql = "SELECT * FROM ".$this->_tableName." WHERE tid=".intval($tid);
		}
		if (!is_null($pid)) {
			if (is_array($pid)) {
				$sql .= " AND pid IN (".$this->_getImplodeString($pid).")";
			} else {
				$sql .= " AND pid=".intval($pid);
			}
		}
		$data = array();
		$query = $this->_db->query($sql);
		while($rt = $this->_db->fetch_array($query)) {
			$data[$rt['aid']] = $rt;
		}
		return $data;
	}

	function nextImgByUid($uid,$aid) {
		return $this->_db->get_value("SELECT aid FROM pw_attachs WHERE uid=".intval($uid)." AND type='img' AND aid<".intval($aid)." ORDER BY aid DESC LIMIT 1");
	}

	function prevImgByUid($uid,$aid) {
		return $this->_db->get_value("SELECT aid FROM pw_attachs WHERE uid=".intval($uid)." AND type='img' AND aid>".intval($aid)." ORDER BY aid LIMIT 1");
	}

	function get($aid) {
		$data = $this->_db->get_one("SELECT * FROM ".$this->_tableName." WHERE aid=".intval($aid));
		if (!$data) return null;
		return $data;
	}

	function gets($params) {
		$params = $this->_checkData($params);
		$data = $where = array();
		foreach ($params as $key=>$value) {
			if (is_array($value)) {
				$where[] = "$key IN (".$this->_getImplodeString($value).")";
			} else {
				$where[] = "$key=".$this->_addSlashes($value);
			}
		}
		$query = $this->_db->query("SELECT * FROM ".$this->_tableName.($where ? " WHERE ".implode(' AND ',$where) : ""));
		while($rt = $this->_db->fetch_array($query)) {
			$data[$rt['aid']] = $rt;
		}
		return $data;
	}

	function groupByTidAndPid($step,$num = 5000) {
		$step = (int)$step;
		$num = (int)$num;
		$start = $step*$num;
		$end = $start + $num;
		$result = array();
		$query = $this->_db->query("SELECT tid, pid FROM `pw_attachs` WHERE aid >".pwEscape($start)." && aid <".pwEscape($end)." GROUP BY tid, pid");
		while ($rt = $this->_db->fetch_array($query)) {
			$result[] = $rt;
		}
		return $result;
	}

	function updateById($aids,$data) {
		if (empty($aids) || empty($data)) return false;
		$data = $this->_checkData($data);
		if (is_array($aids)) {
			$this->_db->update("UPDATE pw_attachs SET " . pwSqlSingle($data) . ' WHERE aid IN(' . $this->_getImplodeString($aids) . ')');
		} else {
			$this->_db->update("UPDATE pw_attachs SET " . pwSqlSingle($data) . ' WHERE aid=' . intval($aids));
		}
		return true;
	}

	/**
	 * 例子:
	 *   updateByTid($tids,$data);
	 *   updateByTid($tids,$pid,$data);
	 */
	function updateByTid($tids,$p1,$p2=null) {
		if (empty($tids)) return false;
		if (is_null($p2)) {
			$data = $p1;
			$pid = null;
		} else {
			$data = $p2;
			$pid = $p1;
		}
		if (empty($data)) return false;
		$data = $this->_checkData($data);
		if (is_array($tids)) {
			$this->_db->update("UPDATE pw_attachs SET " . pwSqlSingle($data) . ' WHERE tid IN(' . $this->_getImplodeString($tids) . ')' . (!is_null($pid) ? " AND pid=".intval($pid) : ""));
		} else {
			$this->_db->update("UPDATE pw_attachs SET " . pwSqlSingle($data) . ' WHERE tid=' . intval($tids) . (!is_null($pid) ? " AND pid=".intval($pid) : ""));
		}
		return true;
	}

	function increaseField($aid, $fieldName, $step = 1) {
		if (! in_array ( $fieldName, array ('hits' ) ))
			return 0;
		$step = intval ( $step );
		if ($step == 0)
			return 0;
		$step = $step > 0 ? "+" . $step : $step;
		$this->_db->update("UPDATE " . $this->_tableName . " SET $fieldName=$fieldName" . $step . " WHERE aid=" . intval($aid) . " LIMIT 1" );
		return $this->_db->affected_rows();
	}

	function getTableStructs($type) {
		$query = $this->_db->query("show table status like ".pwEscape($this->_tableName));
		$data = $this->_db->fetch_array($query);
		if (isset($data[$type])) {
			return $data[$type];
		}
		return null;
	}

	function getStruct() {
		return array('aid','fid','uid','tid','pid','did','name','type','size','attachurl','hits','needrvrc','special','ctype','uploadtime','descrip','ifthumb');
	}

	function _checkData($data) {
		if (!is_array($data) || !count($data)) return null;
		$data = $this->_checkAllowField($data,$this->getStruct());
		return $data;
	}
}

?>