<?php
class Stats extends PbModel {
 	var $name = "Stat";

 	function Stats()
 	{
		parent::__construct();
 	}

 	function Add($stat_name)
 	{
        $sql = "update {$this->table_prefix}stats set sc=sc+1 where sb='$stat_name'";
        $result = $this->dbstuff->Execute($sql);
 	}
}
?>