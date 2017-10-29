<?php
class Industries extends PbModel {
	var $name = "Industry";
	var $info;

 	function Industries()
 	{
		parent::__construct();
 	}
 	
 	function setInfo($id)
 	{
 		$result = $this->dbstuff->GetRow("SELECT * FROM {$this->table_prefix}industries WHERE id=".$id);
 		if (!($result) || empty($result)) {
 			return null;
 		}else {
 			$this->info = $result;
 			return $result;
 		}
 	}
 	
 	function getInfo()
 	{
 		return $this->info;
 	}

	function getCacheIndustry($module = 0)
	{
		$data = include(CACHE_PATH. "industry.php");
		return $data;
	}
	
	function getIndustry()
	{
		include(CACHE_PATH. "cache_industry.php");
		return $_PB_CACHE['industry'];
	}
	
	function getSubIndustry($id, $extra = false)
	{
		$result = $return = array();
		$result = $this->dbstuff->GetArray("SELECT id,name,url FROM {$this->table_prefix}industries i WHERE parent_id='".$id."' ORDER BY display_order ASC");
		if (!$result || empty($result)) {
			if ($extra) {
				$row = $this->dbstuff->GetRow("SELECT id,level,parent_id FROM {$this->table_prefix}industries i WHERE id='".$id."'");
				if (!$row || empty($row)) {
					$result = $this->dbstuff->GetArray("SELECT id,name,url FROM {$this->table_prefix}industries WHERE parent_id='0' ORDER BY display_order ASC");
				}else{
					$result = $this->dbstuff->GetArray("SELECT id,name,url FROM {$this->table_prefix}industries WHERE parent_id='".$row['parent_id']."' ORDER BY display_order ASC");
				}
			}
		}
		if (!empty($result)) {
			foreach ($result as $key=>$val) {
				$return[$val['id']] = $val['name'];
			}
		}
		return $return;
	}
	
	function getMinalId()
	{
		$args = func_get_args();
		if (!empty($args)) {
			foreach ($args as $key=>$val) {
				if($val==0) return intval($args[$key-1]);
			}
		}else {
			return false;
		}
	}	
	
 	function disSubOptions($parent_id, $level)
 	{
 		return;
 	}
 	
 	function getTypeOptions()
 	{
 		require(CACHE_PATH. "cache_industry.php");
 		$this->typeOptions = '';
 		$this->dbstuff->setFetchMode(ADODB_FETCH_ASSOC);
 		$this->params['data'] = $this->dbstuff->GetArray("SELECT id,parent_id,name,level FROM ".$this->table_prefix."industries ORDER BY level ASC,display_order ASC");
 		$tmp_arr = array();
 		foreach ($this->params['data'] as $key=>$val) {
 			$tmp_arr[$val['id']] = $this->params['data'][$key];
 		}
 		unset($key, $val);
 		foreach ($_PB_CACHE['industry'][1] as $key=>$val) {
 			$this->typeOptions.='<option value="'.$key.'" class="option-level0">';
 			$this->typeOptions.=str_repeat('&nbsp;&nbsp;', 0) . $val;
 			$this->typeOptions.='</option>' . "\n";
 			foreach ($_PB_CACHE['industry'][2] as $key2=>$val2) {
 				if ($tmp_arr[$key2]['parent_id'] == $key) {
		 			$this->typeOptions.='<option value="'.$key2.'" class="option-level1">';
		 			$this->typeOptions.=str_repeat('&nbsp;&nbsp;', 1) . $val2;
		 			$this->typeOptions.='</option>' . "\n";
		 			foreach ($_PB_CACHE['industry'][3] as $key3=>$val3) {
		 				if ($tmp_arr[$key3]['parent_id'] == $key2) {
				 			$this->typeOptions.='<option value="'.$key3.'" class="option-level2">';
				 			$this->typeOptions.=str_repeat('&nbsp;&nbsp;', 2) . $val3;
				 			$this->typeOptions.='</option>' . "\n";
		 				}
		 			}
 				}
 			}
 		}
 		return $this->typeOptions;
 	}
 	
 	function updateCache()
 	{
 		return true;
 	}
}
?>