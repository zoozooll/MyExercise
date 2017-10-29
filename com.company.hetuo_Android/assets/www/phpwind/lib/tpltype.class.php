<?php
!defined('P_W') && exit('Forbidden');
class PW_TplType{
	var $db;
	function PW_TplType(){
		global $db;
		$this->db	= &$db;
	}
	function getDatas(){
		$temp	= array();
		$query	= $this->db->query("SELECT * FROM pw_tpltype");
		while ($rt = $this->db->fetch_array($query)) {
			$temp[$rt['type']] = $rt;
		}
		return $temp;
	}
}
?>