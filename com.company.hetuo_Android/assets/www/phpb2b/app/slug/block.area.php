<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 914 $
 */
function smarty_block_area($params, $content, &$smarty) {
	if ($content === null) return;
	$conditions = array();
	if (class_exists("Areas")) {
		$area = new Areas();
		$area_controller = new Area();
	}else{
		uses("area");
		$area = new Areas();
		$area_controller = new Area();
	}
	if(isset($params['typeid'])) {
		$conditions[] = "areatype_id=".$params['typeid'];
	}
	if (isset($params['id'])) {
		$conditions[] = "id=".$params['id'];
	}
	if (isset($params['topid'])) {
		$conditions[] = "top_parentid='".$params['topid']."'";
	}
	if (isset($params['level'])) {
		$conditions[] = "level=".$params['level'];
	}
	if (isset($params['parentid'])) {
		$conditions[] = "parent_id='".$params['parentid']."'";
	}
	if (isset($params['exclude'])) {
		$conditions[] = "id NOT IN (".$params['exclude'].")";
	}
	if (isset($params['include'])) {
		$conditions[] = "id IN (".$params['include'].")";
	}
	$area->setCondition($conditions);
	$row = $col = 0;
	$orderby = null;
	if (isset($params['orderby'])) {
		$orderby = " ORDER BY ".trim($params['orderby'])." ";
	}else{
		$orderby = " ORDER BY level ASC,id ASC";
	}
	if (isset($params['row'])) {
		$row = $params['row'];
	}
	if (isset($params['col'])) {
		$col = $params['col'];
	}
	$limit = $area->setLimitOffset($row, $col);
	if (!isset($params['row']) && !isset($params['col'])) {
		$limit = null;
	}
	$sql = "SELECT id,name,name as areaname,url,alias_name,highlight,url FROM {$area->table_prefix}areas a ".$area->getCondition()."{$orderby}{$limit}";
	$result = $area->dbstuff->GetArray($sql);
	$return = null;
	if (!empty($result)) {
		$i_count = count($result);
		for ($i=0; $i<$i_count; $i++){
			if (isset($params['titlelen'])) {
	    		$result[$i]['name'] = mb_substr($result[$i]['name'], 0, $params['titlelen']);
	    	}
	    	if (!empty($result[$i]['url'])) {
	    		$url = $result[$i]['url'];
	    	}else{
	    		$url = $area_controller->rewriteUrl("area", "special/area.php", $result[$i]['id'], $result[$i]['areaname']);
	    	}
			$return.= str_replace(array("[field:title]", "[field:fulltitle]", "[field:id]", "[link:url]"), array($result[$i]['name'], $result[$i]['areaname'], $result[$i]['id'], $url), $content);
		}
	}
	return $return;
}
?>