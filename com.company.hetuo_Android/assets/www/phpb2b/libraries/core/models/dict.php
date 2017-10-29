<?php
class Dicts extends PbModel {
 	
 	var $name = "Dicts";
 	var $info;

 	function Dicts()
 	{
 		parent::__construct();
 	}
 	
 	function getInfo($id, $name = null)
 	{
 		if (!empty($name)) {
 			$result = $this->dbstuff->GetRow("SELECT d.*,dp.name as typename FROM {$this->table_prefix}dicts d LEFT JOIN {$this->table_prefix}dicttypes dp ON d.dicttype_id=dp.id WHERE d.word='".$name."'");
 		}elseif(!empty($id)){
 			$result = $this->dbstuff->GetRow("SELECT d.*,dp.name as typename FROM {$this->table_prefix}dicts d LEFT JOIN {$this->table_prefix}dicttypes dp ON d.dicttype_id=dp.id WHERE d.id='".$id."'");
 		}else{
 			return false;
 		}
 		return $result;
 	}
}
?>