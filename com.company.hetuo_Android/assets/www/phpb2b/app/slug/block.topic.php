<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 129 $
 */
function smarty_block_topic($params, $content, &$smarty) {
	if ($content === null) return;
	$conditions = array();
	if (class_exists("Topics")) {
		$topic = new Topics();
	}else{
		uses("topic");
		$topic = new Topics();
	}
	if (isset($params['id'])) {
		$conditions[] = "id=".$params['id'];
	}	
	if(isset($params['type'])) {
		$type = explode(",", $params['type']);
		$type = array_unique($type);
		foreach ($type as $val) {
			switch ($val) {
				case 'image':
					$conditions[] = "picture!=''";
					break;
				case 'news':
					$url_fix = "news/list.php?";
				default:
					break;
			}
		}
	}
	$orderby = null;
	if (isset($params['orderby'])) {
		$orderby = " ORDER BY ".trim($params['orderby'])." ";
	}else{
		$orderby = " ORDER BY id DESC";
	}
	$topic->setCondition($conditions);
	$row = $col = 0;
	if (isset($params['row'])) {
		$row = $params['row'];
	}
	if (isset($params['col'])) {
		$col = $params['col'];
	}
	$topic->setLimitOffset($row, $col);
	$sql = "SELECT id,title,picture FROM {$topic->table_prefix}topics ".$topic->getCondition()."{$orderby}".$topic->getLimitOffset()."";
	$result = $topic->dbstuff->GetArray($sql);
	$return = null;
	if (!empty($result)) {
		$i_count = count($result);
		for ($i=0; $i<$i_count; $i++){
			if (isset($params['titlelen'])) {
	    		$result[$i]['title'] = mb_substr($result[$i]['title'], 0, $params['titlelen']);
	    	}
	    	$url = $url_fix."topicid=".$result[$i]['id'];
			$return.= str_replace(array("[field:title]", "[field:id]", "[img:src]", "[link:title]"), array( $result[$i]['title'], $result[$i]['id'], "attachment/".$result[$i]['picture'], $url), $content);
		}
	}
	return $return;
}
?>