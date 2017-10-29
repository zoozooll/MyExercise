<?php
class Templets extends PbModel {
 	var $name = "Templet";

 	function Templets()
 	{
 		parent::__construct();
 	}
 	
	function &getInstance() {
		static $instance = array();
		if (!$instance) {
			$instance[0] =& new Templets();
		}
		return $instance[0];
	}
	
	function getInstalled($membergroup_id = null, $membertype_id = null){
		global $_GET;
		$installed = $conditions = $free_result = array();
		if (isset($_GET['type']) && $_GET['type'] == "system") {
			$conditions[] = "t.type='".$_GET['type']."'";
			$this->setCondition($conditions);
			$condition = $this->getCondition();
		}else{
			$conditions[] = "t.type='user'";
			if (!empty($membergroup_id)) {
				$conditions[] = "INSTR(t.require_membergroups,'[".$membergroup_id."]')>0";
			}
			if (!empty($membertype_id)) {
				$conditions[] = "INSTR(t.require_membertype,'[".$membertype_id."]')>0";
			}
			$this->setCondition($conditions);
			$condition = $this->getCondition();
			//get free templets
			if(!defined("IN_PBADMIN")){
				$free_result = $this->dbstuff->GetArray("SELECT * FROM {$this->table_prefix}templets WHERE type='user' AND require_membergroups='0' AND status='1'");
			}
		}
		$sql = "SELECT t.* FROM {$this->table_prefix}templets t {$condition} ORDER BY t.id DESC";
		$request_result = $this->dbstuff->GetArray($sql);
		$result = array_merge($request_result, $free_result);
		if (!empty($result)) {
			$count = count($result);
			for($i=0; $i<$count; $i++){
				$result[$i]['picture'] = '';
				foreach ( array('png', 'gif', 'jpg', 'jpeg') as $ext ) {
					if (file_exists(PHPB2B_ROOT .$result[$i]['directory']."screenshot.".$ext)) {
						$result[$i]['picture'] = URL.$result[$i]['directory']."screenshot.".$ext;
						break;
					}
				}
				$result[$i]['available'] = 1;
			}
		}
		return $result;
	}

	function exchangeDefault($id)
	{
		$this->dbstuff->Execute("UPDATE {$this->table_prefix}templets SET is_default=0 WHERE type='system' AND id!='".$id."'");
		$this->dbstuff->Execute("UPDATE {$this->table_prefix}templets SET is_default=1 WHERE type='system' AND id='".$id."'");
	}
}
?>