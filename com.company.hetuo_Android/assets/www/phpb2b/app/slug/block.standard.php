<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 615 $
 */
function smarty_block_standard($params, $content, &$smarty) {
	if ($content === null) return;
	$conditions = array();
	if (class_exists("Standards")) {
		$standard = new Standards();
		$standard_controller = new Standard();
	}else{
	    uses("standard");
	    $standard = new Standards();
		$standard_controller = new Standard();
	}
	$orderby = array();
	if (isset($params['typeid'])) {
		$conditions[] = "type_id=".$params['typeid'];
	}
	if (isset($params['id'])) {
		$conditions[] = "id='".$params['id']."'";
	}
	if (isset($params['orderby'])) {
		$orderby[] = trim($params['orderby']);
	}else{
		$orderby[] = "id DESC ";
	}
	$standard->setOrderby($orderby);
	$standard->setCondition($conditions);
	$row = $col = 0;
	if (isset($params['row'])) {
		$row = $params['row'];
	}
	if (isset($params['col'])) {
		$col = $params['col'];
	}
	$standard->setLimitOffset($row, $col);
	$sql = "SELECT * FROM {$standard->table_prefix}standards ".$standard->getCondition().$standard->getOrderby().$standard->getLimitOffset();
	$result = $standard->dbstuff->GetArray($sql);
	$return = null;
	if (!empty($result)) {
		$i_count = count($result);
		for ($i=0; $i<$i_count; $i++){
			$url = $standard_controller->rewrite($result[$i]['id'], $result[$i]['title']);
			if (isset($params['titlelen'])) {
	    		$result[$i]['titl'] = mb_substr($result[$i]['title'], 0, $params['titlelen']);
	    	}			
			$return.= str_replace(array("[link:title]", "[link:url]", "[field:title]", "[field:fulltitle]", "[field:pubdate]", "[field:id]"), array($url, '<a href="'.$url.'" title="'.$result[$i]['title'].'">'.$result[$i]['title'].'</a>', $result[$i]['title'], $result[$i]['title'], @date("m/d", $result[$i]['created']), $result[$i]['id']), $content);
		}
	}
	return $return;
}
?>