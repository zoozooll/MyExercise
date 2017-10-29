<?php
!defined('P_W') && exit('Forbidden');
class PW_cacheDataDB extends BaseDB {
	var $_tableName = "pw_cachedata";

	function getDataByInvokepieceId($invokepieceid,$fid=0,$loopid=0){
		$temp = $this->_db->get_one("SELECT invokepieceid,fid,loopid,data,cachetime FROM ".$this->_tableName." WHERE invokepieceid=".pwEscape($invokepieceid)."AND fid=".pwEscape($fid)." AND loopid=".pwEscape($loopid));
		if (!$temp) return array();
		return $this->_unserializeData($temp);
	}
	function insertData($array){
		$array = $this->_checkData($array);
		if (!$array || !$array['invokepieceid'] || !$array['fid'] || !$array['loopid']) {
			return null;
		}
		$this->_db->update("REPLACE INTO ".$this->_tableName." SET ".pwSqlSingle($array,false));
		return $this->_db->insert_id();
	}
	function deleteData($invokepieceid,$fid=0,$loopid=0){
		$sqladd = '';
		if ($fid) {
			$sqladd .= " AND fid=".pwEscape($fid);
		}
		if ($loopid) {
			$sqladd .= " AND loopid=".pwEscape($loopid);
		}
		$this->_db->update("DELETE FROM ".$this->_tableName." WHERE invokepieceid=".pwEscape($invokepieceid)." $sqladd");
	}
	function truncate(){
		$this->_db->query("TRUNCATE ".$this->_tableName."");
	}
	function updates($array){
		foreach ($array as $key=>$value) {
			$array[$key] = $this->_serializeData($value);
		}
		$this->_db->update("REPLACE INTO ".$this->_tableName." (invokepieceid,fid,loopid,data,cachetime) VALUES " . pwSqlMulti($array,false));
	}
	function getDatasByInvokepieceids($invokepieceids){
		$temp_0 = $temp_1 = $invokepieceids_0 = $invokepieceids_1 = array();
		foreach ($invokepieceids as $key=>$value) {
			if ($value==0) {
				$invokepieceids_0[] = $key;
			} else {
				$invokepieceids_1[] = $key;
			}
		}
		$temp_0	= $this->commonGetDatas($invokepieceids_0);
		$temp_1 = $this->specialGetDatas($invokepieceids_1);
		return $temp_0+$temp_1;
	}
	/*
	 * 普通模块
	 */
	function commonGetDatas($invokepieceids){
		if (!is_array($invokepieceids) || !$invokepieceids) return array();
		$temp	= array();
		$query	= $this->_db->query("SELECT * FROM ".$this->_tableName." WHERE invokepieceid IN(".pwImplode($invokepieceids).")");
		while ($rt = $this->_db->fetch_array($query)) {
			$rt	= $this->_unserializeData($rt);
			$key = $rt['loopid'] ? $rt['invokepieceid']."_".$rt['loopid'] : $rt['invokepieceid'];
			$temp[$key] = $rt;
		}
		return $temp;
	}
	/*
	 * 频道页通用模块
	 */
	function specialGetDatas($invokepieceids){
		global $fid;
		$temp_fid = $fid ? $fid : '';
		if (!is_array($invokepieceids) || !$invokepieceids) return array();
		$temp	= array();
		$query	= $this->_db->query("SELECT * FROM ".$this->_tableName." WHERE invokepieceid IN(".pwImplode($invokepieceids).") AND fid=".pwEscape($temp_fid));
		while ($rt = $this->_db->fetch_array($query)) {
			$rt	= $this->_unserializeData($rt);
			$key = $rt['loopid'] ? $rt['invokepieceid']."_".$rt['loopid'] : $rt['invokepieceid'];
			$temp[$key] = $rt;
		}
		return $temp;
	}

	/*
	 * private functions
	 */
	function getStruct(){
		return array('id','invokepieceid','fid','loopid','data','cachetime');
	}
	function _checkData($data){
		if (!is_array($data) || !count($data)) return null;
		$data = $this->_checkAllowField($data,$this->getStruct());
		$data = $this->_serializeData($data);
		return $data;
	}
	function _serializeData($data) {
		if (isset($data['data']) && is_array($data['data'])) $data['data'] = serialize($data['data']);
		return $data;
	}

	function _unserializeData($data) {
		if ($data['data']) $data['data'] = unserialize($data['data']);
		return $data;
	}
}
?>