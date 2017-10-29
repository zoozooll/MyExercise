<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1286 $
 */
function smarty_block_market($params, $content, &$smarty) {
	if ($content === null) return;
	$conditions = array();
	if (class_exists("Markets")) {
		$market = new Markets();
		$market_controller = new Market();
	}else{
		uses("market");
		$market = new Markets();
		$market_controller = new Market();
	}
	if(isset($params['type'])) {
		$type = explode(",", $params['type']);
		$type = array_unique($type);
		foreach ($type as $val) {
			switch ($val) {
				case 'image':
					$conditions[] = "picture!=''";
					break;
				case 'commend':
					$conditions[] = "if_commend>0";
					break;
				default:
					break;
			}
		}
	}	
	if (isset($params['areaid'])) {
		$conditions[] = "area_id1=".$params['areaid']." OR area_id2=".$params['areaid']." OR area_id3=".$params['areaid'];
	}	
	if (isset($params['industryid'])) {
		$conditions[] = "industry_id1=".$params['industryid']." OR industry_id2=".$params['industryid']." OR industryid3=".$params['industryid'];
	}
	if (isset($params['id'])) {
		$conditions[] = "id=".$params['id'];
	}
	$orderby = null;
	if (isset($params['orderby'])) {
		$orderby = " ORDER BY ".trim($params['orderby'])." ";
	}else{
		$orderby = " ORDER BY id DESC";
	}
	$market->setCondition($conditions);
	$row = $col = 0;
	if (isset($params['row'])) {
		$row = $params['row'];
	}
	if (isset($params['col'])) {
		$col = $params['col'];
	}
	$market->setLimitOffset($row, $col);
	$sql = "SELECT id,name,name as fullname,picture FROM {$market->table_prefix}markets ".$market->getCondition()."{$orderby}".$market->getLimitOffset();
	$result = $market->dbstuff->GetArray($sql);
	$return = null;
	if (!empty($result)) {
		$i_count = count($result);
		for ($i=0; $i<$i_count; $i++){
	    	$url = $market_controller->rewrite($result[$i]['id'], $result[$i]['name']);
			if (isset($params['titlelen'])) {
	    		$result[$i]['name'] = mb_substr($result[$i]['name'], 0, $params['titlelen']);
	    	}
	    	$image_type = isset($params['imagetype'])?trim($params['imagetype']):"middle";
	    	$img = (empty($result[$i]['picture']))?pb_get_attachmenturl('', '', $image_type):pb_get_attachmenturl($result[$i]['picture'], '', $image_type);
			$return.= str_replace(array("[field:title]", "[field:fulltitle]", "[field:id]", "[link:title]", "[link:url]", "[img:src]"), array($result[$i]['name'], $result[$i]['fullname'], $result[$i]['id'], $url, '<a href="'.$url.'" title="'.$result[$i]['fullname'].'">'.$result[$i]['name'].'</a>', $img), $content);
		}
	}
	return $return;
}
?>