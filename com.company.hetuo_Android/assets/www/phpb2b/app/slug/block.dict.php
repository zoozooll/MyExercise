<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 273 $
 */
function smarty_block_dict($params, $content, &$smarty) {
	if ($content === null) return;
	$conditions = array();
	if (class_exists("Dicts")) {
		$dict = new Dicts();
		$dict_controller = new Dict();
	}else{
		uses("dict");
		$dict = new Dicts();
		$dict_controller = new Dict();
	}
	if(isset($params['type'])) {
		$type = explode(",", $params['type']);
		$type = array_unique($type);
		foreach ($type as $val) {
			switch ($val) {
				case 'commend':
					$conditions[] = "if_commend>0";
					break;
				default:
					break;
			}
		}
	}	
	if (isset($params['id'])) {
		$conditions[] = "id=".$params['id'];
	}
	if (!empty($params['typeid'])) {
		$conditions[] = "type_id='".$params['typeid']."'";
	}
	$orderby = null;
	if (isset($params['orderby'])) {
		$orderby = " ORDER BY ".trim($params['orderby'])." ";
	}else{
		$orderby = " ORDER BY id DESC";
	}
	$dict->setCondition($conditions);
	$row = $col = 0;
	if (isset($params['row'])) {
		$row = $params['row'];
	}
	if (isset($params['col'])) {
		$col = $params['col'];
	}
	$dict->setLimitOffset($row, $col);
	$sql = "SELECT id,word,word AS name,word_name,if_commend,digest FROM {$dict->table_prefix}dicts ".$dict->getCondition()."{$orderby}".$dict->getLimitOffset();
	$result = $dict->dbstuff->GetArray($sql);
	$return = null;
	if (!empty($result)) {
		$i_count = count($result);
		for ($i=0; $i<$i_count; $i++){
			if (isset($params['titlelen'])) {
	    		$result[$i]['name'] = mb_substr($result[$i]['word'], 0, $params['titlelen']);
	    	}
	    	$url = $dict_controller->rewrite($result[$i]['id'], $result[$i]['word']);
			$return.= str_replace(array("[field:title]", "[field:fulltitle]", "[field:id]", "[link:title]", "[link:url]"), array($result[$i]['name'], $result[$i]['word'], $result[$i]['id'], "<a title=\"".$result[$i]['word']."\" href=\"".$url."\" target=\"_blank\">".$result[$i]['name']."</a>", $url), $content);
		}
	}
	return $return;
}
?>