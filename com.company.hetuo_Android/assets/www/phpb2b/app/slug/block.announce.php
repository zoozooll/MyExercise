<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 830 $
 */
function smarty_block_announce($params, $content, &$smarty) {
	if ($content === null) return;
	$conditions = array();
	if (!class_exists("Announcements")) {
		uses("announcement");
		$announce = new Announcements();
		$announce_controller = new Announcement();
	}else{
	    $announce = new Announcements();
		$announce_controller = new Announcement();
	}
	$i_count = 1;
	if (isset($params['row'])) {
		$i_count = intval($params['row']);
	}
	if (isset($params['typeid'])) {
		$conditions[] = "announcetype_id=".$params['typeid'];
	}
	if (isset($params['type'])) {
		$type = explode(",", $params['type']);
		$type = array_unique($type);
		foreach ($type as $val) {
			switch ($val) {
				case 'new':
					$conditions[] = "display_expiration>".$announce->timestamp;
					break;
				default:
					break;
			}
		}		
	}
	$announce->setCondition($conditions);
	$row = $col = 0;
	$orderby = null;
	if (isset($params['orderby'])) {
		$orderby = " ORDER BY ".trim($params['orderby'])." ";
	}else{
		$orderby = " ORDER BY id DESC ";
	}
	if (isset($params['row'])) {
		$row = $params['row'];
	}
	if (isset($params['col'])) {
		$col = $params['col'];
	}
	$announce->setLimitOffset($row, $col);	
	$orderby = " ORDER BY display_order ASC,id DESC";
	$sql = "SELECT id,subject AS title,message,message AS content FROM {$announce->table_prefix}announcements ".$announce->getCondition()."{$orderby}".$announce->getLimitOffset()."";
	$result = $announce->dbstuff->GetArray($sql);
	$return = $style = null;
	if (!empty($result)) {
		for ($i=0; $i<$i_count; $i++){
			$result[$i]['title'] = strip_tags($result[$i]['title']);
			if(isset($result[$i]['message'])) $result[$i]['content'] = strip_tags($result[$i]['message']);
			if (isset($params['titlelen'])) {
	    		$result[$i]['title'] = mb_substr($result[$i]['title'], 0, $params['titlelen']);
	    	}		
	    	if (isset($params['infolen'])) {
	    		if(isset($result[$i]['content'])) $result[$i]['content'] = mb_substr($result[$i]['content'],0, $params['infolen']);
	    	}
			$url = $announce_controller->rewrite($result[$i]['id'], $result[$i]['title']);
			if(!empty($result[$i]['title'])) {
				$return.= str_replace(array("[link:title]", "[field:title]", "[field:content]"), array($url, $result[$i]['title'], pb_strip_spaces($result[$i]['content'])), $content);
			}
		}
	}else{
		return;
	}
	return $return;
}
?>