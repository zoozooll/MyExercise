<?php
!defined('P_W') && exit('Forbidden');

class PW_STopicBlockDB extends BaseDB {
	var $_tableName = "pw_stopicblock";

	function add($fieldsData) {
		$fieldsData	= $this->_checkData($fieldsData);
		if (!$fieldsData) return null;
		$this->_db->update("INSERT INTO ".$this->_tableName." SET " . $this->_getUpdateSqlString($fieldsData));
		return $this->_db->insert_id();
	}

	function replace($fieldsData) {
		$fieldsData	= $this->_checkData($fieldsData);
		if (!$fieldsData) return null;
		$this->_db->update("REPLACE INTO ".$this->_tableName." SET " . $this->_getUpdateSqlString($fieldsData));
		return $this->_db->insert_id();
	}

	function delete($block_id) {
		$this->_db->update("DELETE FROM ".$this->_tableName." WHERE block_id=". intval($block_id) ." LIMIT 1");
		return $this->_db->affected_rows();
	}

	function update($block_id, $updateData) {
		$updateData	= $this->_checkData($updateData);
		if (!$updateData) return null;
		$this->_db->update("UPDATE ".$this->_tableName." SET " . $this->_getUpdateSqlString($updateData) . " WHERE block_id=". intval($block_id) ." LIMIT 1");
		return $this->_db->affected_rows();
	}

	function get($block_id) {
		$data = $this->_db->get_one("SELECT * FROM ".$this->_tableName." WHERE block_id=".intval($block_id));
		if (!$data) return null;
		return $this->_unserializeData($data);
	}
	function gets() {
		$query = $this->_db->query("SELECT * FROM {$this->_tableName} ");
		$result = array ();
		while ( $rt = $this->_db->fetch_array ( $query ) ) {
			$result[$rt['block_id']] = $this->_unserializeData($rt);
		}
		return $result;
	}
	function getStruct() {
		return array('block_id','name','tagcode','begin','loops','end','config','replacetag');
	}

	function _checkData($data){
		if (!is_array($data) || !count($data)) return null;
		$data = $this->_checkAllowField($data,$this->getStruct());
		$data = $this->_serializeData($data);
		return $data;
	}
	function _serializeData($data) {
		if (isset($data['config']) && is_array($data['config'])) $data['config'] = serialize($data['config']);
		if (isset($data['replacetag'])&& is_array($data['replacetag'])) $data['replacetag'] = serialize($data['replacetag']);
		return $data;
	}

	function _unserializeData($data) {
		if ($data['config']) $data['config'] = unserialize($data['config']);
		if ($data['replacetag']) $data['replacetag'] = unserialize($data['replacetag']);
		return $data;
	}
}
?>