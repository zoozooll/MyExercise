<?php
class Industry extends PbController {
	var $name = "Industry";
	var $names = array();
	
	function getNames()
	{
		return $this->names;
	}
	
	function setNames()
	{
		if(func_num_args()<1) return;
		$return  = array();
		require(CACHE_PATH. "cache_industry.php");
		$args = func_get_args();
		foreach ($args as $key=>$val) {
			$return[] = isset($_PB_CACHE['industry'][$val])?$_PB_CACHE['industry'][$val]:'';
		}
		$this->names = $return;
	}	
	
	function &getInstance() {
		static $instance = array();
		if (!$instance) {
			$instance[0] =& new Industry();
		}
		return $instance[0];
	}
}
?>