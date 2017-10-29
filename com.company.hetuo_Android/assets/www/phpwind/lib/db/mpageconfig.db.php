<?php
!defined('P_W') && exit('Forbidden');
class PW_MPageConfigDB extends BaseDB {
	var $_tableName = "pw_mpageconfig";

	function getConfig($db_mode,$SCR,$fid=0){
		$sqladd	= $fid ? " AND fid=".pwEscape($fid) : '';
		$data = $this->_db->get_value("SELECT config FROM ".$this->_tableName." WHERE mode=".pwEscape($db_mode)."AND scr=".pwEscape($SCR).$sqladd);
		$data = unserialize($data);
		return $data;
	}
	function getInvokes($db_mode,$SCR,$fid=0){
		$sqladd	= $fid ? " AND fid=".pwEscape($fid) : '';
		$data = $this->_db->get_value("SELECT invokes FROM ".$this->_tableName." WHERE mode=".pwEscape($db_mode)."AND scr=".pwEscape($SCR).$sqladd);
		$data = unserialize($data);
		return $data;
	}
	function getInvokesByMode($mode){
		$temp = array();
		$query	= $this->_db->query("SELECT mode,scr,fid,invokes FROM ".$this->_tableName." WHERE mode=".pwEscape($mode));
		while ($rt = $this->_db->fetch_array($query)) {
			$rt	= $this->_unserializeData($rt);
			if ($rt['invokes']) {
				$temp[]	= $rt;
			}
		}
		return $temp;
	}
	function insertData($array){
		$array	= $this->_checkData($array);
		if (!$array || !$array['scr'] || !$array['mode'] || !$array['config']) {
			return null;
		}
		$this->_db->update("REPLACE INTO ".$this->_tableName." SET ".pwSqlSingle($array,false));
		return $this->_db->insert_id();
	}

	function deleteDataByParam($mode,$scr='',$fid=false){
		$sqladd	= '';
		if ($scr) $sqladd = ' AND scr='.pwEscape($scr);
		if (is_numeric($fid)) $sqladd = ' AND fid='.pwEscape($fid);
		$this->_db->update("DELETE FROM ".$this->_tableName." WHERE mode=".pwEscape($mode).$sqladd);
	}

	function getAreaFid($fid){
		$area_cateinfo = array();
		include(D_P.'data/bbscache/area_config.php');
		$temp_fid = 0;
		if ($fid && isset($area_cateinfo[$fid]) && isset($area_cateinfo[$fid]['tpl'])) {
			$temp_fid = $fid;
		}
		return $temp_fid;
	}

	function getStruct(){
		return array('id','mode','scr','fid','invokes','config');
	}

	function _checkData($data){
		if (!is_array($data) || !count($data)) return null;
		$data = $this->_checkAllowField($data,$this->getStruct());
		$data = $this->_serializeData($data);
		return $data;
	}
	function _serializeData($data) {
		if (isset($data['config']) && is_array($data['config'])) $data['config'] = serialize($data['config']);
		return $data;
	}
	function _unserializeData($data) {
		if ($data['config']) $data['config'] = unserialize($data['config']);
		return $data;
	}
}
?>