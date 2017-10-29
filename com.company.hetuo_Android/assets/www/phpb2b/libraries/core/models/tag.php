<?php
class Tags extends PbModel {
	var $name = "Tag";
 	var $tag;
 	var $exist_id = array();
 	var $inserted_id = array();
 	var $id;
 	

 	function Tags()
 	{
		parent::__construct();
 	}
 	
 	function Add($title, $closed = 0)
 	{
 		$title = pb_strip_spaces(strip_tags($title));
 		$exists = $this->checkTagExists($title);
 		if ($exists) {
 			$this->dbstuff->Execute("UPDATE ".$this->table_prefix."tags SET numbers=numbers+1 WHERE id=".$this->id);
 			return false;
 		}else{
 			$sql = "INSERT INTO ".$this->table_prefix."tags (name,closed,created) VALUE ('".$title."','".$closed."',".$this->timestamp.")";
 			return $this->dbstuff->Execute($sql);
 		}
 	}
 	
	function &getInstance() {
		static $instance = array();
		if (!$instance) {
			$instance[0] =& new tags();
		}
		return $instance[0];
	} 	

	function checkTagExists($title)
	{
		$sql = "SELECT id FROM ".$this->table_prefix."tags WHERE name='".$title."'";
		$result = $this->dbstuff->GetRow($sql);
		if (!empty($result)) {
			$this->id = $result['id'];
			return true;
		}else{
			return false;
		}
	}

	function setTagId($tags)
	{
		global $current_adminer_id;
		$tmp_exist_tag = array();
		if (empty($tags) || !$tags) {
			return;
		}
		$words = str_replace(array("，"), ",", trim($tags));
		if (strstr($words, ",")) {
			$words = explode(",", $words);
		}else{
			$words = explode(" ", $words);
		}
		$tmp_str = "('".implode("','", $words)."')";
		$result = $this->dbstuff->GetArray("SELECT id,name FROM {$this->table_prefix}tags WHERE name IN ".$tmp_str);
		if (!empty($result)) {
			foreach ($result as $key=>$val){
				$this->exist_id[] = $val['id'];
				$tmp_exist_tag[] = $val['name'];
			}
		}
		$not_exist_tag = array_diff($words, $tmp_exist_tag);
		if (!empty($not_exist_tag)) {
			$tmp_str = array();
			if (!empty($current_adminer_id)) {
				$member_id = $current_adminer_id;
			}elseif (isset($_SESSION['member_id'])){
				$member_id = $_SESSION['member_id'];
			}
			foreach ($not_exist_tag as $val2) {
				if(!empty($member_id)){
					$tmp_str[] = "('".$member_id."','".$val2."',1,".$this->timestamp.",".$this->timestamp.")";
				}else{
					$tmp_str[] = "(0,'".$val2."',1,".$this->timestamp.",".$this->timestamp.")";
				}
			}
			if(!empty($tmp_str)) {
				$this->dbstuff->Execute("INSERT INTO {$this->table_prefix}tags (member_id,name,numbers,created,modified) VALUES ".implode(",", $tmp_str));
			}
			$result = $this->dbstuff->GetArray("SELECT id,name FROM {$this->table_prefix}tags WHERE name IN ('".implode("','", $not_exist_tag)."')");
			foreach ($result as $val3) {
				$this->inserted_id[] = $val3['id'];
			}
		}
		return $this->getTagId();
	}

	function getTagId()
	{
		$ids = array_merge($this->exist_id, $this->inserted_id);
		$ids = implode(",", $ids);
		if (empty($ids) || !$ids) {
			return '';
		}
		return $ids;
	}
	
	function getTagsByIds($tag_ids, $extra = false)
	{
		$return = array();
		if(empty($tag_ids) || !$tag_ids) return;
		$sql = "SELECT id,name FROM {$this->table_prefix}tags WHERE id IN (".$tag_ids.")";
		$result = $this->dbstuff->GetArray($sql);
		if (!empty($result)) {
			foreach ($result as $val){
				$return[$val['id']] = $val['name'];
			}
			if($extra) $this->tag = implode(" ", $return);
		}
		return $return;
	}
}