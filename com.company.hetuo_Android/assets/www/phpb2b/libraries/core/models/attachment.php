<?php
class Attachments extends PbModel {
 	var $name = "Attachment";
 	var $info = null;
 	var $file_url = null;

 	function Attachments()
 	{
		parent::__construct();
 	}
 	
	function &getInstance() {
		static $instance = array();
		if (!$instance) {
			$instance[0] =& new Attachments();
		}
		return $instance[0];
	}
 	
 	function Add($attach_info)
 	{
 		$result = $this->save($attach_info);
 		$key = $this->table_name."_id";
 		$last_tradeid = $this->$key;
 		return $last_tradeid;
 	}
 	
 	function getAttachLink($id)
 	{
 		global $attachment_url;
 		$return = null;
 		$attach_res = $this->dbstuff->GetRow("SELECT * FROM ".$this->table_prefix."attachments WHERE id=".$id);
 		if (!empty($attach_res)) {
 			$title = (empty($attach_res['description']))?$attach_res['id']:$attach_res['description'];
 			$return = L("attachments", "tpl").'<a href="../'.$attachment_url.$attach_res['attachment'].'" target="_blank">'.$title.'</a>';
 		}
 		return $return;
 	}
 	
 	function getAttachFileName($id)
 	{
 		global $attachment_url;
 		$return = null;
 		$attach_res = $this->dbstuff->GetRow("SELECT id,title,description,attachment FROM ".$this->table_prefix."attachments WHERE id=".$id);
 		if (!empty($attach_res)) {
 			$this->info = $attach_res;
 			$this->file_url = $attachment_url.$attach_res['attachment'];
 			$return = (empty($attach_res['title']))?$attach_res['id']:$attach_res['description'];
 		}
 		return $return;
 	}
}
?>