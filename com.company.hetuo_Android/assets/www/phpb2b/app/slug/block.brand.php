<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision$
 */
function smarty_block_brand($params, $content, &$smarty) {
   if ($content === null) return;
	$conditions = array();
	if (class_exists("Brands")) {
		$brand = new Brands();
		$brand_controller = new Brand();
	}else{
		uses("brand");
		$brand = new Brands();
		$brand_controller = new Brand();
	}
		if(isset($params['type'])) {
		$type = explode(",", $params['type']);
		$type = array_unique($type);
		foreach ($type as $val) {
			switch ($val) {
				case 'image':
					$conditions[] = "picture!=''";
					break;
				case 'hot':
					$orderbys[] = "hits DESC";
					break;
				case 'commend':
					$conditions[] = "if_commend='1'";
					break;
				default:
					break;
			}
		}
	}
    if (isset($params['companyid'])) {
		$conditions[] = "b.company_id=".$params['companyid'];
	}
	if (isset($params['memberid'])) {
		$conditions[] = "b.member_id=".$params['memberid'];
	}
	if (isset($params['typeid'])) {
		$conditions[] = "b.type_id=".$params['typeid'];
	}
	if (isset($params['removal'])) {
		$conditions[] = "b.id!=".$params['removal'];
	}
	$orderby = null;
	if (!empty($orderbys)) {
		$orderby.=" ORDER BY ".implode(",", $orderbys);
	}
	if (isset($params['orderby'])) {
		$orderby = " ORDER BY ".implode(",", array(trim($params['orderby']), $orderby))." ";
	}
	if(empty($orderby)){
		$orderby = " ORDER BY id DESC ";
	}
	$brand->setCondition($conditions);
	$row = $col = 0;
	if (isset($params['row'])) {
		$row = $params['row'];
	}
	if (isset($params['col'])) {
		$col = $params['col'];
	}
	$brand->setLimitOffset($row, $col);
	$sql = "SELECT id ,name,picture,alias_name,description FROM {$brand->table_prefix}brands b ".$brand->getCondition()."{$orderby}".$brand->getLimitOffset();
	$result = $brand->dbstuff->GetArray($sql);
	$return = null;
	if (!empty($result)) {
		$i_count = count($result);
		for ($i=0; $i<$i_count; $i++){
			$url = $brand_controller->rewrite($result[$i]['id'], $result[$i]['name']);
			if (isset($params['titlelen'])) {
	    		$result[$i]['name'] = mb_substr($result[$i]['name'], 0, $params['titlelen']);
	    	}			
			$return.= str_replace(array("[link:title]", "[link:url]", "[field:title]", "[img:src]", "[field:fulltitle]", "[field:id]", "[img:thumb]"), array($url, '<a href="'.$url.'" title="'.$result[$i]['alias_name'].'">'.$result[$i]['name'].'</a>', $result[$i]['name'], "attachment/".$result[$i]['picture'].".small.jpg", $result[$i]['name'], $result[$i]['id'], "attachment/".$result[$i]['picture'].".small.jpg"), $content);
		}
	}
	return $return;	
}
?>