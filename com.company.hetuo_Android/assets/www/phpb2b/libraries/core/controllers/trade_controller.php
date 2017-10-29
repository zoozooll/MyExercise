<?php
class Trade extends PbController {
	var $name = "Trade";
	var $info;
	var $type_names;
	var $type_info;
	var $types;
 	
 	function getOfferExpires()
 	{
 		require(CACHE_PATH. "cache_typeoption.php");
 		return $_PB_CACHE['offer_expire'];
 	}
 	
 	function rewrite($id, $typeid = 0, $title = null)
 	{
		global $rewrite_able, $rewrite_compatible;
		$url = null;
		if ($rewrite_able) {
			if ($rewrite_compatible && !empty($title)) {
				$url = $this->getModulenameById($typeid)."/".rawurlencode($title)."/";
			}else{
				$url = $this->getModulenameById($typeid)."/detail/".$id.".html";
			}
		}else{
			if ($rewrite_compatible && !empty($title)) {
				$url = "offer/detail.php?title=".rawurlencode($title);
			}else{
				$url = "offer/detail.php?id=".$id;
			}
		}
		return $url; 		
 	}
 	
 	function getModulenameById($typeid)
 	{
 		$module_name = null;
 		switch ($typeid) {
 			case 1:
 				$module_name = "buy";
 				break;
 			case 2:
 				$module_name = "sell";
				break;
 			default:
 				$module_name = "offer";
 				break;
 		}
 		return $module_name;
 	}
	
	function setInfoById($id)
	{
		$_this = & Trades::getInstance();
		$this->info = $_this->getInfoById($id);
	}
	
	function &getInstance() {
		static $instance = array();
		if (!$instance) {
			$instance[0] =& new Trade();
		}
		return $instance[0];
	} 	 	
	
	function getInfoById()
	{
		return $this->info;
	}
	
	function setTypeInfo($typeid)
	{
		$types = $this->getTradeTypes();
		if (in_array($typeid, array_keys($types))) {
			$this->type_info['name'] = $this->types[$typeid];
		}else{
			$this->type_info['name'] = L("offer", 'tpl');
		}
	}
	
	function getTypeInfo()
	{
		return $this->type_info;
	}
	
 	function getTradeTypes()
 	{
		require(CACHE_PATH. "cache_type.php");
		$this->types = $_PB_CACHE['offertype'];
		return $_PB_CACHE['offertype'];
 	}

	function getTradeTypeNames(){
		return $this->type_names;
	}

	function setTradeTypeNames(){
		require(CACHE_PATH. "cache_type.php");
		$this->type_names = $_PB_CACHE['offertype'];
	}

 	function Expired($expire_time)
 	{
 		$tmp_day = mktime(0,0,0,date("m") ,date("d"),date("Y"));
 		if ($tmp_day > $expire_time) {
 			return true;
 		}else {
 			return false;
 		}
 	}	
}
?>