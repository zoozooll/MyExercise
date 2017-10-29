<?php
class Brands extends PbModel {
 	
 	var $name = "Brand";

 	function Brands()
 	{
 		parent::__construct();
 	}

	function formatResult($result)
	{
		if(!empty($result)){
			$count = count($result);
			for ($i=0; $i<$count; $i++){
				$result[$i]['pubdate'] = @date("Y-m-d", $result[$i]['submit_time']);
				$result[$i]['image'] = pb_get_attachmenturl($result[$i]['picture'], '', 'middle');
			}
			return $result;
		}else{
			return null;
		}
	}

 	function getInfo($id)
	{
		$sql = "SELECT b.*,m.username,c.name AS companyname FROM {$this->table_prefix}brands b LEFT JOIN {$this->table_prefix}members m ON m.id=b.member_id LEFT JOIN {$this->table_prefix}companies c ON c.member_id=b.member_id WHERE b.id=".$id;
		$result = $this->dbstuff->GetRow($sql);
		return $result;
	}
}
?>