<?php
class Area extends PbController {
	var $name = "Area";
	var $names = array();
	
	function getNames()
	{
		return $this->names;
	}
	
	function &getInstance() {
		static $instance = array();
		if (!$instance) {
			$instance[0] =& new Area();
		}
		return $instance[0];
	}
	
	function setNames()
	{
		if(func_num_args()<1) return;
		$return  = array();
		require(CACHE_PATH. "cache_area.php");
		$args = func_get_args();
		foreach ($args as $key=>$val) {
			$return[] = isset($_PB_CACHE['area'][$val]) ? $_PB_CACHE['area'][$val] : '';
		}
		$this->names = $return;
	}
}
?>