<?php
class Expoes extends PbModel {
 	var $name = "Expo";
 	var $info;

 	function Expoes()
 	{
 		parent::__construct();
 	}
 	
 	function checkExist($id, $extra = false)
 	{
 		$id = intval($id);
 		$info = $this->dbstuff->GetRow("SELECT * FROM {$this->table_prefix}expoes WHERE id={$id}");
 		if (empty($info) or !$info) {
 			return false;
 		}else{
 			if ($extra) {
 				$info['begin_date'] = (!$info['begin_time'])?@date("Y-m-d", $this->timestamp):@date("Y-m-d", $info['begin_time']);
 				$info['end_date'] = (!$info['end_time'])?@date("Y-m-d", $this->timestamp):@date("Y-m-d", $info['end_time']);
 				$this->info = $info;
 			}
 			return true;
 		}
 	}
}
?>