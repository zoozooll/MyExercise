<?php
class Services extends PbModel {
 	var $name = "Service";
 	var $validate = array(
	'content' => array( 'required' => true),
	'email' => array( 'required' => true)
	);
 	
 	function Services()
 	{
 		$this->validate['content']['message'] = L("content_cant_be_empty");
 		$this->validate['email']['message'] = L("please_input_email");
		parent::__construct();
 	}
 	
 	function formatResult($result)
 	{
 		global $_PB_CACHE;
 		if (!empty($result)) {
 			for($i=0; $i<count($result); $i++){
 				$result[$i]['typename'] = $_PB_CACHE['service_type'][$result[$i]['type_id']];
 				$result[$i]['submitdate'] = date("Y-m-d H:i", $result[$i]['created']);
 				if (!empty($result[$i]['revert_date'])) {
 					$result[$i]['revertdate'] = date("Y-m-d H:i", $result[$i]['revert_date']);
 				}
 			}
 		}
 		return $result;
 	}
 }