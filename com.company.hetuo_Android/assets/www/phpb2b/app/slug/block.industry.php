<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1293 $
 */
function smarty_block_industry($params, $content, &$smarty) {
	if ($content === null) return;
	$conditions = array();
	if (class_exists("Industries")) {
		$industry = new Industries();
		$industry_controller = new Industry();
	}else{
		uses("industry");
		$industry = new Industries();
		$industry_controller = new Industry();
	}
	if(!empty($params['typeid'])) {
		$conditions[] = "indusrytype_id=".$params['typeid'];
	}
	if (!empty($params['id'])) {
		$conditions[] = "id=".$params['id'];
	}
	if (!empty($params['topid'])) {
		$conditions[] = "top_parentid='".$params['topid']."'";
	}
	if (!empty($params['level'])) {
		$conditions[] = "level=".$params['level'];
	}
	if (!empty($params['parentid'])) {
			$conditions[] = "parent_id='".$params['parentid']."'";
	}
	if (!empty($params['topparentid'])) {
		$conditions[] = "top_parentid='".$params['topparentid']."'";
	}
	if (!empty($params['exclude'])) {
		$conditions[] = "id NOT IN (".$params['exclude'].")";
	}
	if (!empty($params['include'])) {
		$conditions[] = "id IN (".$params['include'].")";
	}
	$orderby = null;
	if (isset($params['orderby'])) {
		$orderby = " ORDER BY ".trim($params['orderby'])." ";
	}else{
		$orderby = " ORDER BY display_order ASC,id ASC";
	}
	$industry->setCondition($conditions);
	$row = $col = 0;
	if (isset($params['row'])) {
		$row = $params['row'];
	}
	if (isset($params['col'])) {
		$col = $params['col'];
	}
	$limit = $industry->setLimitOffset($row, $col);
	if (!isset($params['row']) && !isset($params['col'])) {
		$limit = null;
	}
	$sql = "SELECT id,name,name as industryname,alias_name,highlight,url FROM {$industry->table_prefix}industries i ".$industry->getCondition()."{$orderby}{$limit}";
	$result = $industry->dbstuff->GetArray($sql);
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
	    		$url = $industry_controller->rewriteUrl("industry", "special/industry.php", $result[$i]['id'], $result[$i]['industryname']);
	    	}
			$return.= str_replace(array("[field:title]", "[field:fulltitle]", "[field:id]", "[field:style]", "[link:url]"), array($result[$i]['name'], $result[$i]['industryname'], $result[$i]['id'], parse_highlight($result[$i]['highlight']), $url), $content);
		}
	}
	return $return;
}
?>