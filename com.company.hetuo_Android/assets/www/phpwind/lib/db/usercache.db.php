<?php
!defined('P_W') && exit('Forbidden');

/**
 *	type
 *		topic
 *		photo
 *		write
 *		diary
 */
class PW_UsercacheDB extends BaseDB {

	var $_tableName = "pw_usercache";
	var $now = 0;

	function PW_UsercacheDB() {
		parent::BaseDB();
		$this->now = $GLOBALS['timestamp'];
	}

	function update($uid,$type,$typeid,$value) {
		$data = array(
			'uid'	=> $uid,
			'type'	=> $type,
			'typeid'=> $typeid,
			'expire'=> $this->now + 608400,
			'value'	=> $this->_serialize($value)
		);

		$this->_db->update("REPLACE INTO ".$this->_tableName." SET ".pwSqlSingle($data,false));
	}

	function delete($uid,$type=null,$typeid=null) {
		$this->_db->update("DELETE FROM ".$this->_tableName." WHERE uid=".pwEscape($uid,false).($type ? "AND type=".pwEscape($type,false) : "").($typeid ? "AND typeid=".pwEscape($typeid,false) : ""));
	}

	function delByType($type,$ids = array()) {
		$uids = $tids = array();
		foreach ($ids as $key => $value) {
			$uids[] = $key;
			$tids  += (array)$value;
		}
		if ($uids && $tids) {
			$this->_db->update("DELETE FROM " . $this->_tableName . " WHERE uid IN (" . pwImplode($uids,false) . ") AND type=" . pwEscape($type,false) . " AND typeid IN (" . pwImplode($tids,false) . ")");
		} else {
			$this->_db->update("DELETE FROM ".$this->_tableName." WHERE type=".pwEscape($type,false));
		}
	}

	function getByUid($uids) {#$uids array|int
		$data = array();
		if (is_array($uids)) {
			$query = $this->_db->query("SELECT uid,type,value,typeid FROM ".$this->_tableName." WHERE uid IN (".pwImplode($uids,false).") AND expire>".pwEscape($this->now,false));
			while ($rt = $this->_db->fetch_array($query)) {
				$value = $this->_unserialize($rt['value']);
				$data[$rt['uid']][$rt['type']] = array('value'=>$value,'id'=>$rt['typeid']);
			}
		} else {
			$query = $this->_db->query("SELECT type,value,typeid FROM ".$this->_tableName." WHERE uid=".pwEscape($uids,false));
			while ($rt = $this->_db->fetch_array($query)) {
				$value = $this->_unserialize($rt['value']);
				$data[$rt['type']] = array('value'=>$value,'id'=>$rt['typeid']);
			}
		}
		return $data;
	}

	function get($uid,$type) {
		$rt = $this->_db->get_one("SELECT value,typeid FROM ".$this->_tableName." WHERE uid=".pwEscape($uid,false)."AND type=".pwEscape($type,false));
		$value = $this->_unserialize($rt['value']);
		return array('value'=>$value,'id'=>$rt['typeid']);
	}

	function getStruct() {
		return array('uid','type','typeid','expire','value');
	}

	function _checkData($data) {
		if (!is_array($data) || !count($data)) return null;
		$data = $this->_checkAllowField($data,$this->getStruct());
		return $data;
	}
}

?>