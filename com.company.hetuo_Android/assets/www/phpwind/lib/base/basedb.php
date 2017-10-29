<?php
!defined ('P_W') && exit('Forbidden');
class BaseDB {
	var $_db = null;

	function BaseDB() {
		$this->_db = $GLOBALS['db'];
	}

	function _getConnection() {
		//global
		return $GLOBALS['db'];
	}

	function _getUpdateSqlString($arr) {
		//global
		return pwSqlSingle($arr);
	}

	function _getAllResultFromQuery($query) {
		$result = array ();
		while ( $rt = $this->_db->fetch_array ( $query ) ) {
			$result [] = $rt;
		}
		return $result;
	}

	function _checkAllowField($fieldData,$allowFields) {
		foreach ($fieldData as $key=>$value) {
			if (!in_array($key,$allowFields)) {
				unset($fieldData[$key]);
			}
		}
		return $fieldData;
	}

	function _addSlashes($var) {
		//global
		return pwEscape($var);
	}

	function _getImplodeString($string,$strip=true) {
		return pwImplode($string,$strip);
	}

	function _serialize($value) {
		if (!is_string($value) && !is_numeric($value)) {
			$value = serialize($value);
		}
		return $value;
	}

	function _unserialize($value) {
		if ($value && is_array($tmpValue = unserialize($value))) {
			$value = $tmpValue;
		}
		return $value;
	}
}

?>