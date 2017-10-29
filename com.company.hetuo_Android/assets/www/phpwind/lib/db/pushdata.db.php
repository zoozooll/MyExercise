<?php
!defined('P_W') && exit('Forbidden');
class PW_PushDataDB extends BaseDB {
	var $_tableName = "pw_pushdata";

	function getEffectData($invokepieceid,$fid=0,$loopid=0,$num = 10){
		return $this->_getDatas('effect',$invokepieceid,$fid,$loopid,$num);
	}
	function getOverdueData($invokepieceid,$fid=0,$loopid=0,$num = 10){
		return $this->_getDatas('overdue',$invokepieceid,$fid,$loopid,$num);
	}
	function getDelayData($invokepieceid,$fid=0,$loopid=0,$num = 10) {
		return $this->_getDatas('delay',$invokepieceid,$fid,$loopid,$num);
	}
	function countEffect($invokepieceid,$fid,$loopid){
		return $this->_countByInvokepiece('effect',$invokepieceid,$fid,$loopid);
	}
	function countOverdue($invokepieceid,$fid,$loopid){
		return $this->_countByInvokepiece('overdue',$invokepieceid,$fid,$loopid);
	}
	function countDelay($invokepieceid,$fid,$loopid){
		return $this->_countByInvokepiece('delay',$invokepieceid,$fid,$loopid);
	}
	function getDataById($id){
		$temp	= $this->_db->get_one("SELECT * FROM ".$this->_tableName." WHERE id=".pwEscape($id));
		if (!$temp) return array();
		return $this->_unserializeData($temp);
	}

	function insertData($array){
		!$array['offset'] && $array['offset'] = 0;
		if (!isset($array['invokepieceid']) || !isset($array['fid']) || !isset($array['loopid'])) {
			return null;
		}
		$temp	= $this->_getDataByOffset($array['offset'],$array['invokepieceid'],$array['fid'],$array['loopid']);
		if ($temp) {
			global $timestamp;
			$this->update($temp['id'],array('endtime'=>$timestamp));
		}
		return $this->_insertData($array);
	}
	function update($id,$array){
		$array	= $this->_checkData($array);
		if (!$array) return null;
		$this->_db->update("UPDATE ".$this->_tableName." SET ".pwSqlSingle($array,false)." WHERE id=".pwEscape($id));
	}
	function delete($id){
		$this->_db->update("DELETE FROM ".$this->_tableName." WHERE id=".pwEscape($id));
	}
	function deleteByPiecesId($id) {
		$this->_db->update("DELETE FROM ".$this->_tableName." WHERE invokepieceid=".pwEscape($id));
	}
	
	function increaseOffset($invokepieceid,$offset) {
		$this->_db->update("UPDATE ".$this->_tableName." SET offset=offset+1 WHERE invokepieceid=".pwEscape($invokepieceid)."AND offset>=".pwEscape($offset).$this->_getAddSqlByType('effect'));
		//TODO 更新操作
	}
	function decrementOffset($invokepieceid,$offset) {
		$offset < 1 && $offset = 0;
		$this->_db->update("UPDATE ".$this->_tableName." SET offset=offset-1 WHERE invokepieceid=".pwEscape($invokepieceid)."AND offset>=".pwEscape($offset).$this->_getAddSqlByType('effect'));
		//TODO 更新操作
	}
	function deleteOverdues($invokepieceid,$fid,$loopid){
		$sqladd	= $this->_getAddSqlByType('overdue');
		$this->_db->update("DELETE FROM ".$this->_tableName." WHERE invokepieceid=".pwEscape($invokepieceid)." AND fid=".pwEscape($fid)." AND loopid=".pwEscape($loopid)." $sqladd");
	}

	function getHaveDelays($invokepieces,$fid=0) {
		global $timestamp;
		if (!is_array($invokepieces) || !$invokepieces) return array();
		$temp_invokepieces = array_keys($invokepieces);
		$temp = array();
		$query = $this->_db->query("SELECT invokepieceid,fid,loopid FROM pw_pushdata WHERE starttime>".pwEscape($timestamp)." AND fid = ".pwEscape($fid)." AND invokepieceid IN(".pwImplode($temp_invokepieces).") GROUP BY invokepieceid,fid,loopid");
		while ($rt = $this->_db->fetch_array($query)) {
			$key = md5($rt['invokepieceid'].$rt['fid'].$rt['loopid']);
			$temp[$key] = $rt;
		}
		return $temp;
	}
	/*
	 * private functions
	 */
	function _countByInvokepiece($type,$invokepieceid,$fid,$loopid){
		$sqladd	= $this->_getAddSqlByType($type);
		return $this->_db->get_value("SELECT COUNT(*) AS count FROM ".$this->_tableName." WHERE invokepieceid=".pwEscape($invokepieceid)." AND fid=".pwEscape($fid)." AND loopid=".pwEscape($loopid)." $sqladd");
	}
	function _getDatas($type,$invokepieceid,$fid=0,$loopid=0,$num = 10){
		$temp	= array();
		$sqladd	= $this->_getAddSqlByType($type);
		list($start,$num)	= $this->_parseNum($num);
		$query	= $this->_db->query("SELECT * FROM ".$this->_tableName." WHERE invokepieceid=".pwEscape($invokepieceid)." AND fid=".pwEscape($fid)." AND loopid=".pwEscape($loopid)." $sqladd ORDER BY offset ASC,starttime DESC ".pwLimit($start,$num));
		while ($rt = $this->_db->fetch_array($query)) {
			if ($rt['offset'] >= $num) continue;
			$rt	= $this->_unserializeData($rt);
			$rt = $this->_colorTitle($rt);
			$temp[$rt['id']] = $rt;
		}
		return $temp;
	}

	function _colorTitle($rt) {
		global $timestamp;
		if ($rt['titlecss'] && $rt['data']['title'] && (!$rt['titlecss']['endtime'] || $rt['titlecss']['endtime']>$timestamp)) {
			if ($rt['titlecss']['color']) $rt['data']['title'] = "<font color=".$rt['titlecss']['color'].">".$rt['data']['title']."</font>";
			if ($rt['titlecss']['b']) $rt['data']['title'] = "<b>".$rt['data']['title']."</b>";
			if ($rt['titlecss']['i']) $rt['data']['title'] = "<i>".$rt['data']['title']."</i>";
			if ($rt['titlecss']['u']) $rt['data']['title'] = "<u>".$rt['data']['title']."</u>";
		}
		return $rt;
	}
	function _getAddSqlByType($type){
		global $timestamp;
		$sqladd	= '';
		if ($type=='effect') {
			$sqladd	= ' AND starttime<'.pwEscape($timestamp).' AND (endtime>'.pwEscape($timestamp).' OR endtime=0)';
		} else if ($type=='overdue') {
			$sqladd	= ' AND endtime<'.pwEscape($timestamp).' AND endtime<>0';
		} else if ($type=='delay') {
			$sqladd	= ' AND starttime>'.pwEscape($timestamp);
		}
		return $sqladd;
	}

	function _parseNum($num){
		$num_temp	= explode(',',$num);
		if (count($num_temp)==2) {
			$start	= $num_temp[0];
			$num	= $num_temp[1];
		} else {
			$start	= 0;
			$num	= $num_temp[0];
		}
		return array($start,$num);
	}

	function _insertData($array){
		$array	= $this->_checkData($array);
		if (!$array || !$array['invokepieceid'] || !$array['data']) {
			return null;
		}
		$this->_db->update("INSERT INTO ".$this->_tableName." SET ".pwSqlSingle($array,false));
		return $this->_db->insert_id();
	}

	function _getDataByOffset($offset,$invokepieceid,$fid=0,$loopid=0){
		global $timestamp;
		$temp	= $this->_db->get_one("SELECT * FROM ".$this->_tableName." WHERE invokepieceid=".pwEscape($invokepieceid)." AND fid=".pwEscape($fid)." AND loopid=".pwEscape($loopid)." AND offset=".pwEscape($offset)." AND (endtime>".pwEscape($timestamp)." OR endtime=0)");
		if (!$temp) return array();
		return $this->_unserializeData($temp);
	}

	function getStruct(){
		return array('id','invokename','invokepieceid','fid','loopid','editor','starttime','endtime','offset','data','titlecss');
	}

	function _checkData($data){
		if (!is_array($data) || !count($data)) return false;
		$data = $this->_checkAllowField($data,$this->getStruct());
		$data = $this->_serializeData($data);
		return $data;
	}
	function _serializeData($array) {
		if ($array['data'] && is_array($array['data'])) {
			foreach ($array['data'] as $key=>$value) {
				if ($key == 'tagrelate') continue;
				$array['data'][$key] = stripslashes($value);
			}
			$array['data']	= serialize($array['data']);
		}
		if ($array['titlecss'] && is_array($array['titlecss'])) {
			$array['titlecss'] = serialize($array['titlecss']);
		}
		return $array;
	}
	function _unserializeData($data) {
		if ($data['data']) $data['data'] = unserialize($data['data']);
		if ($data['titlecss']) $data['titlecss'] = unserialize($data['titlecss']);
		return $data;
	}
}
?>