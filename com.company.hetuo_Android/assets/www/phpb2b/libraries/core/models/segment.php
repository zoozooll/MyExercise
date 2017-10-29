<?php
class Segments extends PbModel {
 	var $name = "Segments";
 	var $ids = null;
 	var $target_ids = null;

 	function Segments()
 	{
 		parent::__construct();
 	}
 	
 	function array_diff($array_1, $array_2) {
 		$array_2 = array_flip($array_2);
 		foreach ($array_1 as $key => $item) {
 			if (isset($array_2[$item])) {
 				unset($array_1[$key]);
 			}
 		}
 		return $array_1;
 	}
 	
 	function setIds($str, $type_name = 'trades', $add_new = false, $target_id = 0)
 	{
		require(LOCALE_PATH. 'wordsegment.class.php');
		$ws = new WordSegment();
		$ws->zhcode($str);
	    $title = $ws->getResult();
 		$values = $exist_keys = $not_exist_keys = $key_result = array();
 		if (empty($title) || !is_array($title)){
 			return false;
 		}
 		$titles = implode("','", $title);
 		$key_result = $this->dbstuff->GetArray("SELECT id,title FROM {$this->table_prefix}keywords WHERE title IN ('{$titles}') AND type_name='".$type_name."' AND target_id='".$target_id."'");
 		if(!empty($key_result)){
	 		foreach ($key_result as $val){
	 			$exist_keys[] = $val['title'];
	 		}
 		}
 		$not_exist_keys = $this->array_diff($title, $exist_keys);
 		if (!empty($not_exist_keys)) {
 			foreach ($not_exist_keys as $valx) {
 				$values[] = "('{$valx}',{$target_id},'{$type_name}')";
 			}
	 		if($add_new && !empty($values)) {
	 			$values = implode(",", $values);
	 			$this->dbstuff->Execute("INSERT INTO {$this->table_prefix}keywords (title,target_id,type_name) VALUES ".$values);
	 		}
 		}
 	}
 	
 	function getIds()
 	{
 		return $this->ids;
 	}
}
?>