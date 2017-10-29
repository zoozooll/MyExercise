<?php
class Markets extends PbModel {
 	var $name = "Market";

 	function Markets()
 	{
 		parent::__construct();
 	}
 	
 	function Add()
 	{
 		global $_PB_CACHE;
 		if (isset($this->params['data']['market']['name'])) {
 			$this->params['data']['market']['created'] = $this->params['data']['market']['modified'] = $this->timestamp;
 			$this->params['data']['market']['ip_address'] = pb_get_client_ip('str');
 			$this->params['data']['market']['status'] = 0;
 			return $this->save($this->params['data']['market']);
 		}
 		return false;
 	}
}
?>