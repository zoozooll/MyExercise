<?php
class Orders extends PbModel {
 	var $name = "Orders";
 	
	function checkOrders($id = null, $status = null)
	{
		if(is_array($id)){
			$checkId = "id IN (".implode(",",$id).")";
		}else {
			$checkId = "id=".$id;
		}
		$sql = "UPDATE ".$this->getTable()." SET status='".$status."' WHERE ".$checkId;
		$return = $this->dbstuff->Execute($sql);
		if($return){
			return true;
		}else {
			return false;
		}
	}
	
	function Add($data)
	{
		if (empty($data) || !is_array($data)) {
			return false;
		}else{
			extract($data);
			$result = $this->dbstuff->Execute("INSERT INTO {$this->table_prefix}orders (member_id,cache_username,content,total_price,created,modified) VALUE ('".$member_id."','".$username."','".$content."','".$total_price."',".$this->timestamp.",".$this->timestamp.")");
			if ($result) {
				$last_order_id = $this->dbstuff->GetOne("SELECT LAST_INSERT_ID() AS last_order_id");
				return $last_order_id;
			}else{
				return false;
			}
		}
	}
}
?>