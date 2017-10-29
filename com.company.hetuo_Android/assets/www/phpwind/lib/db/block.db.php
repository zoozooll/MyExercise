<?php
!defined('P_W') && exit('Forbidden');
class PW_BlockDB extends BaseDB{
	function getData($bid){
		return $this->_db->get_one("SELECT * FROM pw_block WHERE bid=".pwEscape($bid));
	}
	function getDatasBySid($sid){
		$temp	= array();
		$query	= $this->_db->query("SELECT * FROM pw_block WHERE sid=".pwEscape($sid));
		while ($rt = $this->_db->fetch_array($query)) {
			$temp[] = $rt;
		}
		return $temp;
	}
}
?>