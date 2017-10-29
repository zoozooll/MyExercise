<?php
!defined('P_W') && exit('Forbidden');
class PW_StampDB extends BaseDB {
	var $_tableName = "pw_stamp";

	function getDataByStamp($stamp){
		return $this->_db->get_one("SELECT * FROM ".$this->_tableName." WHERE stamp=".pwEscape($stamp));
	}
	function getDataBySid($sid){
		return $this->_db->get_one("SELECT * FROM ".$this->_tableName." WHERE sid=".pwEscape($sid));
	}
	function getBlocksByStamp($stamp){
		$sid = $this->_db->get_value("SELECT sid FROM ".$this->_tableName." WHERE stamp=".pwEscape($stamp));
		$pw_block = L::loadDB('block');
		return $pw_block->getDatasBySid($sid);
	}
	function getDefaultBlockByStamp($stamp){
		$stampdata = $this->getDataByStamp($stamp);
		if ($stampdata['init']) {
			$pw_block = L::loadDB('block');
			return $pw_block->getData($stampdata['init']);
		}
	}
}
?>