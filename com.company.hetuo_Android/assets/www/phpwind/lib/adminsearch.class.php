<?php
!function_exists('readover') && exit('Forbidden');
/*
 * backstage search class
 * @copyright PHPWind
 * @author xiaolang
 */
class AdminSearch {
	var $keyword;
	var $purview = array();
	var $lang = array();
	var $result	= array();
	function AdminSearch($keyword){
		global $admin_file;
		require GetLang('purview');
		require GetLang('search');
		foreach ($purview as $key => $value) {
			if (!adminRightCheck($key)) {
				unset($purviewp[$key]);
				unset($search[$key]);
			}
		}
		$this->purview = &$purview;
		$this->lang	= &$search;
		$this->keyword	= $keyword;
	}
	function search(){
		if (!$this->keyword) return false;
		$this->searchPurview();
		$this->searchLang();
		return $this->result;
	}
	function searchPurview(){
		foreach ($this->purview as $key=>$value) {
			if ($this->_strpos($value[0],$this->keyword)!==false) {
				$this->initResult($key);
			}
		}
	}
	function searchLang(){
		foreach ($this->lang as $key=>$value) {
			foreach ($value as $k=>$val) {
				if (is_array($val)) {
					foreach ($val as $v) {
						if ($this->_strpos($v,$this->keyword)!==false) {
							$this->initResult($key);
							if (!isset($this->result[$key]['lang'][$k])) {
								$this->result[$key]['lang'][$k] = array($val['name']);
							}
							$this->result[$key]['lang'][$k][] = $this->redColorKeyword($v);
						}
					}
				} else {
					if ($this->_strpos($val,$this->keyword)!==false) {
						$this->initResult($key);
						$this->result[$key]['lang'][] = $this->redColorKeyword($val);
					}
				}
			}
		}
	}
	function initResult($key){
		if (array_key_exists($key,$this->purview) && !isset($this->result[$key])) {
			$this->result[$key] = array('name'=>$this->purview[$key][0],'url'=>$this->purview[$key][1],'lang'=>array());
		}
	}
	function redColorKeyword($text){
		return str_replace($this->keyword,'<font color="red">'.$this->keyword.'</font>',$text);
	}
	function _strpos($string,$find) {
		if (function_exists('stripos')) {
			return stripos($string,$find);
		}
		return strpos($string,$find);
	}

}
?>