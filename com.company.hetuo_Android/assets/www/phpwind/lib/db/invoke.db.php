<?php
!defined('P_W') && exit('Forbidden');
class PW_InvokeDB extends BaseDB {
	var $_tableName = "pw_invoke";

	function getDataByName($name,$cateid=0) {
		$temp	= $this->_db->get_one("SELECT * FROM ".$this->_tableName." WHERE name=".pwEscape($name));
		if ($temp['loops']) {
			$temp['loops'] = unserialize($temp['loops']);
			$temp['loops'] = $this->stripFidByCateid($temp['loops'],$cateid);
		}
		return $temp;
	}
	function getDatasByNames($names,$cateid=0,$type='') {
		if (!is_array($names) || !$names) return null;
		$sql_add = '';
		if ($type) {
			$sql_add .= 'AND t.type='.pwEscape($type);
		}
		$temp	= array();
		$query	= $this->_db->query("SELECT i.*,t.image,t.type FROM ".$this->_tableName." i LEFT JOIN pw_tpl t USING(tplid) WHERE i.name IN(".pwImplode($names).") $sql_add ORDER BY i.id");
		while ($rt = $this->_db->fetch_array($query)) {
			if ($rt['loops']) {
				$rt['loops'] = unserialize($rt['loops']);
				$rt['loops'] = $this->stripFidByCateid($rt['loops'],$cateid);
			}
			unset($rt['parsecode']);
			$temp[$rt['name']] = $rt;
		}
		return $temp;
	}

	function stripFidByCateid($fids,$cateid) {
		global $SCR,$fid;
		$cateid	= (int)$cateid;
		if ($SCR == 'cate' && !$cateid) {
			$cateid = $fid;
		}
		if ($cateid) {
			$temp_forum = getForumCache();
			foreach ($fids as $key=>$value) {
				if (!isset($temp_forum[$value]) || getCateid($value) != $cateid) {
					unset($fids[$key]);
				}
			}
		}
		return $fids;
	}
	function getDataById($id) {
		$temp	= $this->_db->get_one("SELECT * FROM ".$this->_tableName." WHERE id=".pwEscape($id));
		if (!$temp) return array();
		return $this->_unserializeData($temp);
	}
	function getByTplId($tplid) {
		$query = $this->_db->query("SELECT * FROM ".$this->_tableName." WHERE tplid=".$this->_addSlashes($tplid));
		while ($rt = $this->_db->fetch_array($query)) {
			$rt	= $this->_unserializeData($rt);
			$temp[] = $rt;
		}
		return $temp;
	}
	function getDatas($type,$limit='') {
		if ($type) {
			$sqladd = $this->_getSqlAdd($type,true);
			if (!$sqladd) return array();
		} else {
			$sqladd = '';
		}
		$temp = array();
		$query = $this->_db->query("SELECT i.*,t.image FROM ".$this->_tableName." i LEFT JOIN pw_tpl t USING(tplid) $sqladd $limit");
		while ($rt = $this->_db->fetch_array($query)) {
			if ($rt['ifloop']) {
				$rt['pwcode'] = Char_cv('<loop id="'.$rt['name'].'"><pw id="'.$rt['name'].'" /></loop>');
			} else {
				$rt['pwcode'] = Char_cv('<pw id="'.$rt['name'].'" />');
			}
			$rt	= $this->_unserializeData($rt);
			$temp[] = $rt;
		}
		return $temp;
	}
	function updateByName($name,$array) {
		$array	= $this->_checkData($array);
		if (!$array) {
			return null;
		}
		$this->_db->update("UPDATE ".$this->_tableName." SET ".pwSqlSingle($array,false)." WHERE name=".pwEscape($name));
	}

	function count($type='') {
		if ($type) {
			$sqladd = $this->_getSqlAdd($type);
			if (!$sqladd) return 0;
		} else {
			$sqladd = '';
		}
		return $this->_db->get_value("SELECT COUNT(*) AS count FROM ".$this->_tableName." $sqladd");
	}

	function insertData($array) {
		$array	= $this->_checkData($array);
		if (!$array || !$array['name'] || !$array['tplid']) {
			return null;
		}
		$this->_db->update("INSERT INTO ".$this->_tableName." SET ".pwSqlSingle($array,false));
		return $this->_db->insert_id();
	}
	function replaceData($array) {
		$array	= $this->_checkData($array);
		if (!$array || !$array['name'] || !$array['tplid']) {
			return null;
		}
		$this->_db->update("REPLACE INTO ".$this->_tableName." SET ".pwSqlSingle($array,false));
		return $this->_db->insert_id();
	}
	function deleteByName($name){
		$this->_db->update("DELETE FROM ".$this->_tableName." WHERE name=".pwEscape($name));
	}
	function deleteByNames($names){
		if (!is_array($names) || !$names) return null;
		$this->_db->update("DELETE FROM ".$this->_tableName." WHERE name IN(".pwImplode($names).")");
	}
	function getDatesByLike($rule) {
		$query = $this->_db->query("SELECT * FROM ".$this->_tableName." WHERE `name` LIKE ".pwEscape($rule));
		while ($rt = $this->_db->fetch_array($query)) {
			$rt	= $this->_unserializeData($rt);
			$temp[] = $rt;
		}
		return $temp;
	}
	function getDatesByNames_2($names) {
		if (!is_array($names) || !$names) return null;
		$temp	= array();
		$query	= $this->_db->query("SELECT * FROM ".$this->_tableName." WHERE name IN(".pwImplode($names).")");
		while ($rt = $this->_db->fetch_array($query)) {
			$rt	= $this->_unserializeData($rt);
			$temp[] = $rt;
		}
		return $temp;
	}

	/*
	 * private functions
	 */
	function _getSqlAdd($type,$join=false) {
		$sqladd = '';
		$pw_tpl = L::loadDB('tpl');
		if ($type) {
			$tplids = $pw_tpl->getTplIdsByType($type);
			if ($tplids) {
				$field 	= $join ? 'i.tplid':'tplid';
				$sqladd = " WHERE $field IN (".pwImplode($tplids).")";
			}
		}
		return $sqladd;
	}
	function getStruct() {
		return array('id','name','tplid','tagcode','parsecode','ifloop','loops','descrip');
	}

	function _checkData($data){
		if (!is_array($data) || !count($data)) return null;
		$data = $this->_checkAllowField($data,$this->getStruct());
		$data = $this->_serializeData($data);
		if ($array['descrip'] && strlen($array['descrip'])>255) {
			return null;
		}
		return $data;
	}
	function _serializeData($data) {
		if (isset($data['loops']) && is_array($data['loops'])) $data['loops'] = serialize($data['loops']);
		return $data;
	}

	function _unserializeData($data) {
		if ($data['loops']) $data['loops'] = unserialize($data['loops']);
		return $data;
	}
}
?>