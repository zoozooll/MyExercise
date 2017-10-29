<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1138 $
 */
function smarty_block_help($params, $content, &$smarty) {
	if ($content === null) return;
	$conditions = array();
	if (class_exists("Helps")) {
		$help = new Helps();
	}else{
		uses("help");
		$help = new Helps();
	}
	if(!empty($params['typeid'])) {
		$conditions[] = "helptype_id=".$params['typeid'];
	}
	if (!empty($params['id'])) {
		$conditions[] = "id=".$params['id'];
	}
	if (isset($params['orderby'])) {
		$orderby = " ORDER BY ".trim($params['orderby'])." ";
	}else{
		$orderby = " ORDER BY id DESC";
	}
	$help->setCondition($conditions);
	$row = $col = 0;
	$orderby = null;
	if (isset($params['row'])) {
		$row = $params['row'];
	}
	if (isset($params['col'])) {
		$col = $params['col'];
	}
	$help->setLimitOffset($row, $col);
	$sql = "SELECT id,title,title as fulltitle,highlight FROM {$help->table_prefix}helps ".$help->getCondition()."{$orderby}".$help->getLimitOffset()."";
	$result = $help->dbstuff->GetArray($sql);
	$return = null;
	if (!empty($result)) {
		$i_count = count($result);
		for ($i=0; $i<$i_count; $i++){
			if (isset($params['titlelen'])) {
	    		$result[$i]['title'] = mb_substr($result[$i]['title'], 0, $params['titlelen']);
	    	}
			$return.= str_replace(array("[field:title]", "[field:fulltitle]", "[field:id]", "[field:style]"), array($result[$i]['title'], $result[$i]['fulltitle'], $result[$i]['id'], parse_highlight($result[$i]['highlight'])), $content);
		}
	}
	return $return;
}
?>