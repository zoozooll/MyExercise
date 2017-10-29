<?php
!defined('P_W') && exit('Forbidden');
class PW_InvokePieceDB extends BaseDB {
	var $_tableName = "pw_invokepiece";

	function getDataById($id) {
		$temp = $this->_db->get_one("SELECT * FROM ".$this->_tableName." WHERE id=".pwEscape($id));
		if (!$temp) return array();
		return $this->_unserializeData($temp);
	}
	function getDatasByIds($ids) {
		$temp	= array();
		if (is_array($ids) && $ids) {
			$query	= $this->_db->query("SELECT * FROM ".$this->_tableName." WHERE id IN(".pwImplode($ids).")");
			while ($rt = $this->_db->fetch_array($query)) {
				$temp[$rt['id']]	= $this->_unserializeData($rt);
			}
		}

		return $temp;
	}

	function getDatasByInvokeName($invokename) {
		$temp = array();
		$query = $this->_db->query("SELECT * FROM ".$this->_tableName." WHERE invokename=".pwEscape($invokename));
		while ($rt = $this->_db->fetch_array($query)) {
			$temp[$rt['id']]	= $this->_unserializeData($rt);
		}
		return $temp;
	}

	function getDatasByInvokeNames($names) {
		if (!is_array($names) || !$names) return null;
		$temp = array();
		$query = $this->_db->query("SELECT * FROM ".$this->_tableName." WHERE invokename IN(".pwImplode($names).")");
		while ($rt = $this->_db->fetch_array($query)) {
			$temp[]	= $this->_unserializeData($rt);
		}
		return $temp;
	}

	function getDataByInvokeNameAndTitle($invokename,$title) {
		$temp = $this->_db->get_one("SELECT * FROM ".$this->_tableName." WHERE invokename=".pwEscape($invokename)."AND title=".pwEscape($title));
		if (!$temp) return array();
		return $this->_unserializeData($temp);
	}

	function updateById($id,$array) {
		$array	= $this->_checkData($array);
		if (!$array) return null;
		$this->_db->update("UPDATE ".$this->_tableName." SET ".pwSqlSingle($array,false)." WHERE id=".pwEscape($id));
	}

	function insertData($array) {
		$array	= $this->_checkData($array);
		if (!$array || !$array['invokename'] || !$array['action'] || !$array['title'] || !$array['num'] || !$array['param']) {
			return null;
		}
		if (!$array['func'] || !$array['cachetime']) {
			$pw_stamp	= L::loadDB('stamp');
			$block	= $pw_stamp->getDefaultBlockByStamp($array['action']);
			$array['func']	= $block['func'];
			$array['cachetime'] = $block['cachetime'];
			$array['rang']	= $block['rang'];
		}
		$this->_db->update("INSERT INTO ".$this->_tableName." SET ".pwSqlSingle($array,false));
		return $this->_db->insert_id();
	}
	function replaceData($array) {
		$array	= $this->_checkData($array);
		if (!$array || !$array['invokename'] || !$array['action'] || !$array['title'] || !$array['num'] || !$array['param']) {
			return null;
		}
		if (!$array['func'] || !$array['cachetime']) {
			$pw_stamp	= L::loadDB('stamp');
			$block	= $pw_stamp->getDefaultBlockByStamp($array['action']);
			$array['func']	= $block['func'];
			$array['cachetime'] = $block['cachetime'];
			$array['rang']	= $block['rang'];
		}
		$this->_db->update("REPLACE INTO ".$this->_tableName." SET ".pwSqlSingle($array,false));
		return $this->_db->insert_id();
	}
	function deleteByInvokeName($name) {
		$this->_db->update("DELETE FROM ".$this->_tableName." WHERE invokename=".pwEscape($name));
	}

	function deleteByInvokeNames($names){
		if (!is_array($names) || !$names) return null;
		$this->_db->update("DELETE FROM ".$this->_tableName." WHERE invokename IN(".pwImplode($names).")");
	}

	function insertDatas($array) {
		if (!is_array($array)) return false;
		foreach ($array as $key=>$value) {
			if (is_array($value)) {
				$this->insertData($value);
			}
		}
	}

	function deleteById($id) {
		$this->_db->update("DELETE FROM ".$this->_tableName." WHERE id=".pwEscape($id));
	}

	function getDatesByLike($rule) {
		$query = $this->_db->query("SELECT * FROM ".$this->_tableName." WHERE `invokename` LIKE ".pwEscape($rule));
		while ($rt = $this->_db->fetch_array($query)) {
			$rt	= $this->_unserializeData($rt);
			$temp[] = $rt;
		}
		return $temp;
	}

	function updateInvokeNameKey() {
		$count = 0;
		$query = $this->_db->query("SHOW KEYS FROM ".$this->_tableName);
		while ($rt = $this->_db->fetch_array($query)) {
			$rt['Key_name '] == 'invokename' && $count++;
		}
		if ($count<2) {
			$this->_db->query('ALTER TABLE `pw_invokepiece` DROP INDEX `invokename` ,ADD UNIQUE `invokename` ( `invokename` , `title` )');
		}
	}

	function getStruct() {
		return array('id','invokename','title','action','func','num','rang','param','cachetime');
	}
	function _checkData($data){
		if (!is_array($data) || !count($data)) return null;
		$data = $this->_checkAllowField($data,$this->getStruct());
		$data = $this->_serializeData($data);
		return $data;
	}
	function _serializeData($data) {
		if (isset($data['param']) && is_array($data['param'])) $data['param'] = serialize($data['param']);
		return $data;
	}

	function _unserializeData($data) {
		if ($data['param']) $data['param'] = unserialize($data['param']);
		return $data;
	}
}
?>