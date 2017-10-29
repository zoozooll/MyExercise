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
function smarty_block_keyword($params, $content, &$smarty) {
	if ($content === null) return;
	$conditions = array();
	if (!class_exists("Keywords")) {
		uses("keyword");
		$keyword = new Keywords();
		$keyword_controller = new Keyword();
	}else{
		$keyword = new Keywords();
		$keyword_controller = new Keyword();
	}
	$conditions[] = "status=1";
	if (isset($params['typeid'])) {
	        $conditions[] = "type='".intval($params['typeid'])."'";
	}
	if (isset($params['orderby'])) {
		$orderby = " ORDER BY ".trim($params['orderby'])." ";
	}else{
		$orderby = " ORDER BY id DESC";
	}
	$keyword->setCondition($conditions);
	$row = $col = 0;
	$orderby = null;
	if (isset($params['row'])) {
		$row = $params['row'];
	}
	if (isset($params['col'])) {
		$col = $params['col'];
	}
	$keyword->setLimitOffset($row, $col);
	$sql = "SELECT id,name,name AS title,name AS fulltitle FROM {$keyword->table_prefix}keywords ".$keyword->getCondition()."{$orderby}".$keyword->getLimitOffset();
	$result = $keyword->dbstuff->GetArray($sql);
	$return = null;
	if (!empty($result)) {
		$i_count = count($result);
		for ($i=0; $i<$i_count; $i++){
			if (isset($params['titlelen'])) {
	    		$result[$i]['title'] = mb_substr($result[$i]['title'], 0, $params['titlelen']);
	    	}
	    	$url = $keyword_controller->rewrite($result[$i]['id'], $result[$i]['title']);
			$return.= str_replace(array("[field:title]", "[field:fulltitle]", "[field:id]", "[link:title]"), array($result[$i]['title'], $result[$i]['fulltitle'], $result[$i]['id'], $url), $content);
		}
	}
	return $return;
}
?>