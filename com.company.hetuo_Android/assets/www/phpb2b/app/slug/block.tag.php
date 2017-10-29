<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1024 $
 */
function smarty_block_tag($params, $content, &$smarty) {
	if ($content === null) return;
	$conditions = array();
	if (!class_exists("Tags")) {
		uses("tag");
		$tag = new Tags();
		$tag_controller = new Tag();
	}else{
		$tag = new Tags();
		$tag_controller = new Tag();
	}
	$conditions[] = "closed=0";
	if (isset($params['typeid'])) {
		$conditions[] = "type='".intval($params['typeid'])."'";
	}
	$orderby = null;
	if (isset($params['orderby'])) {
		$orderby = " ORDER BY ".trim($params['orderby'])." ";
	}else{
		$orderby = " ORDER BY id DESC";
	}
	$tag->setCondition($conditions);
	$row = $col = 0;
	if (isset($params['row'])) {
		$row = $params['row'];
	}
	if (isset($params['col'])) {
		$col = $params['col'];
	}
	$tag->setLimitOffset($row, $col);
	$sql = "SELECT id,name,name AS title,name AS fulltitle FROM {$tag->table_prefix}tags ".$tag->getCondition()."{$orderby}".$tag->getLimitOffset();
	$result = $tag->dbstuff->GetArray($sql);
	$return = $link_title = null;
	if (!empty($result)) {
		$i_count = count($result);
		for ($i=0; $i<$i_count; $i++){
			if (isset($params['titlelen'])) {
	    		$result[$i]['title'] = mb_substr($result[$i]['title'], 0, $params['titlelen']);
	    	}
	    	$url = $tag_controller->rewrite($result[$i]['id'], $result[$i]['title']);
			$return.= str_replace(array("[field:title]", "[field:fulltitle]", "[field:id]", "[link:url]", "[link:title]"), array($result[$i]['title'], $result[$i]['fulltitle'], $result[$i]['id'], $url, '<a href="'.$url.'">'.$result[$i]['title'].'</a>'), $content);
		}
	}
	return $return;
}
?>