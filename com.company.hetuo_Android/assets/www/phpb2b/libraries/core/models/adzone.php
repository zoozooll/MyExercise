<?php
class Adzones extends PbModel {
 	var $name = "Adzone";
 	
 	function Adzones()
 	{
 		parent::__construct();
 	}
 	
 	function updateBreathe($id)
 	{
 		global $smarty;
 		$result = $this->read("*", $id);
 		if (!empty($result) && $result['style']==1) {
 			$tmp_arr = array();
 			$xml_template = PHPB2B_ROOT. "examples".DS."breathe.xml";
 			$cache_datafile = DATA_PATH."appcache/Breathe-".$id.".xml";
 			$ad_result = $this->dbstuff->GetArray("SELECT * FROM ".$this->table_prefix."adses WHERE adzone_id=".$id);
 			if (!empty($ad_result)) {
 				for($i=0; $i<count($ad_result); $i++) {
 					$tmp_arr[$i]['link'] = (!empty($ad_result[$i]['target_url']))?$ad_result[$i]['target_url']:URL;
 					$tmp_arr[$i]['image'] = $ad_result[$i]['source_url'];
 				}
 			}
 			$data = $tmp_arr;
 			setvar("Items", $data);
 			$xml_data = $smarty->fetch("file:".$xml_template);
 			file_put_contents($cache_datafile, $xml_data);
 		}
 	}
}
?>