<?php
class Tradefields extends PbModel {
 	var $name = "Tradefield";
 	
	function &getInstance() {
		static $instance = array();
		if (!$instance) {
			$instance[0] =& new Tradefields();
		}
		return $instance[0];
	}
	
	function replace($datas)
	{
		if (!empty($datas)) {
			$keys = array_keys($datas);
			$keys = "(".implode(",", $keys).")";
			$values = "('".implode("','", $datas)."')";
			$sql = "REPLACE INTO {$this->table_prefix}tradefields ".$keys." VALUES ".$values;
			return $this->dbstuff->Execute($sql);
		}
	}
}
?>