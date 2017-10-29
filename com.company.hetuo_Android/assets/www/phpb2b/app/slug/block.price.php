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
function smarty_block_price($params, $content, &$smarty) {
	if ($content === null) return;
	$conditions = array();
	if (class_exists("Productprice")) {
		$productprices = new Productprices();
		$productprice_controller = new Productprice();
	}else{
		uses("productprice");
		$productprices = new Productprices();
		$productprice_controller = new Productprice();
	}
	if (isset($params['productid'])) {
		$conditions[] ="p.product_id='".intval($params['productid'])."'";
	}
	if (isset($params['companyid'])) {
		$conditions[] = "p.company_id=".$params['companyid'];
	}
	if (isset($params['memberid'])) {
		$conditions[] = "p.member_id=".$params['memberid'];
	}
	if (isset($params['typeid'])) {
		$conditions[] = "p.type_id='".$params['typeid']."'";
	}
	if (!empty($params['areaid'])) {
		$conditions[] = "p.area_id='".$params['areaid']."'";
	}
	$orderby = null;
	if (isset($params['orderby'])) {
		$orderby = " ORDER BY ".trim($params['orderby'])." ";
	}else{
		$orderby = " ORDER BY id DESC ";
	}
	$productprices->setCondition($conditions);
	$row = $col = 0;
	if (isset($params['row'])) {
		$row = $params['row'];
	}
	if (isset($params['col'])) {
		$col = $params['col'];
	}
	$sql = "SELECT id,title,description,price,units,currency,created FROM {$productprices->table_prefix}productprices  p ".$productprices->getCondition()."{$orderby}".$productprices->getLimitOffset();
	$result = $productprices->dbstuff->GetArray($sql);
	$return = null;
	if (!empty($result)) {
		$i_count = count($result);
		for ($i=0; $i<$i_count; $i++){
			$url = $productprice_controller->rewrite($result[$i]['id'], $result[$i]['title']);
			if (isset($params['titlelen'])) {
				$result[$i]['title'] = mb_substr($result[$i]['title'], 0, $params['titlelen']);
			}
			$return.= str_replace(array("[link:title]", "[link:url]", "[field:title]", "[field:pubdate]", "[field:id]"), array($url, '<a href="'.$url.'" title="'.$result[$i]['title'].'">'.$result[$i]['title'].'</a>', $result[$i]['title'],  @date("m/d", $result[$i]['created']), $result[$i]['id']), $content);
			if (isset($params['col'])) {
				if ($i%$params['col']==0) {
					$return.="<br />";
				}
			}
		}
	}
	return $return;
}
?>