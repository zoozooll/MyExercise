<?php
class Areas extends PbModel {
	var $name = "Area";

	function Areas()
	{
		parent::__construct();
	}
	
 	function setInfo($id)
 	{
 		$result = $this->dbstuff->GetRow("SELECT * FROM {$this->table_prefix}areas WHERE id=".$id);
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
	
	function &getInstance() {
		static $instance = array();
		if (!$instance) {
			$instance[0] =& new Areas();
		}
		return $instance[0];
	}
	
	function getSubArea($id, $extra = false)
	{
		$return = $result = array();
		$result = $this->dbstuff->GetArray("SELECT id,name,url FROM {$this->table_prefix}areas WHERE parent_id='".$id."' ORDER BY display_order ASC");
		if (!$result || empty($result)) {
			if ($extra) {
				$row = $this->dbstuff->GetRow("SELECT id,level,parent_id FROM {$this->table_prefix}areas WHERE id=".$id);
				if (!$row || empty($row)) {
					return null;
				}else{
					$result = $this->dbstuff->GetArray("SELECT id,name,url FROM {$this->table_prefix}areas WHERE parent_id='".$row['parent_id']."' ORDER BY display_order ASC");
				}
			}
		}else{
			$result = $this->dbstuff->GetArray("SELECT id,name,url FROM {$this->table_prefix}areas WHERE parent_id=0 ORDER BY display_order ASC");
		}
		if (!empty($result)) {
			foreach ($result as $key=>$val) {
				$return[$val['id']] = $val['name'];
			}
		}
		return $return;
	}	
	
	function getCacheArea()
	{
		include(CACHE_PATH. "cache_area.php");
		return $_PB_CACHE['area'];
	}
	
	function getLevelAreas()
	{
		$return = array();
		include(CACHE_PATH. "cache_area.php");
		$data = $this->dbstuff->CacheGetArray("SELECT id,name,url,parent_id FROM {$this->table_prefix}areas where parent_id>0");
		$result = $_PB_CACHE['area'][1];
		if (!empty($result)) {
			foreach ($result as $key=>$val) {
				$return[$key]['id'] = $key;
				$return[$key]['name'] = $val;
				$url = $this->dbstuff->GetOne("SELECT url FROM {$this->table_prefix}areas WHERE id=".$key);
				if (empty($url)) {
					$return[$key]['url'] = "special/area.php?id=".$key;
				}else{
					$return[$key]['url'] = $url;
				}
				if (!empty($data)) {
					foreach ($data as $level_val) {
						if($level_val['parent_id'] == $key){
							$return[$key]['sub'][$level_val['id']]['id'] = $level_val['id'];
							$return[$key]['sub'][$level_val['id']]['name'] = $level_val['name'];
							$return[$key]['sub'][$level_val['id']]['url'] = (empty($level_val['url']))?"special/area.php?id=".$level_val['id']:$level_val['url'];
						}
					}
				}
			}
			return $return;
		}else{
			return false;
		}
	}
	
 	function disSubOptions($data, $parent_id)
 	{	
 		return;
 	}
 	
 	function getTypeOptions()
 	{
 		require(CACHE_PATH. "cache_area.php");
 		$this->typeOptions = '';
 		$this->dbstuff->setFetchMode(ADODB_FETCH_ASSOC);
 		$this->params['data'] = $this->dbstuff->GetArray("SELECT id,parent_id,name,level FROM ".$this->table_prefix."areas ORDER BY display_order ASC");
 		$tmp_arr = array();
 		foreach ($this->params['data'] as $key=>$val) {
 			$tmp_arr[$val['id']] = $this->params['data'][$key];
 		}
 		unset($key, $val);
 		foreach ($_PB_CACHE['area'][1] as $key=>$val) {
 			$this->typeOptions.='<option value="'.$key.'" class="option-level0">';
 			$this->typeOptions.=str_repeat('&nbsp;&nbsp;', 0) . $val;
 			$this->typeOptions.='</option>' . "\n";
 			foreach ($_PB_CACHE['area'][2] as $key2=>$val2) {
 				if ($tmp_arr[$key2]['parent_id'] == $key) {
		 			$this->typeOptions.='<option value="'.$key2.'" class="option-level1">';
		 			$this->typeOptions.=str_repeat('&nbsp;&nbsp;', 1) . $val2;
		 			$this->typeOptions.='</option>' . "\n";
		 			foreach ($_PB_CACHE['area'][3] as $key3=>$val3) {
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
}
?>