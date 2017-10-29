<?php
class Productprices extends PbModel {
 	
 	var $name = "Productprice";

 	function Productprices()
 	{
 		parent::__construct();
 	}
 	 	function getInfo($id)
	{
		$sql = "SELECT pp.*,m.username,c.name AS companyname,p.name AS productname,b.name AS brandname FROM {$this->table_prefix}productprices pp LEFT JOIN {$this->table_prefix}members m ON m.id=pp.member_id LEFT JOIN {$this->table_prefix}companies c ON c.member_id=pp.member_id LEFT JOIN {$this->table_prefix}products p ON p.id=pp.product_id LEFT JOIN  {$this->table_prefix}brands b ON b.id=pp.brand_id  WHERE pp.id=".$id;
		$result = $this->dbstuff->GetRow($sql);
		return $result;
	}
}
?>