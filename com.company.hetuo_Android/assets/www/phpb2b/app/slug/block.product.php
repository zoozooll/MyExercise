<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 578 $
 */
function smarty_block_product($params, $content, &$smarty) {
	if ($content === null) return;
	$conditions = array();
	if (class_exists("Products")) {
		$product = new Products();
		$product_controller = new Product();
	}else{
		uses("product");
		$product = new Products();
		$product_controller = new Product();
	}
	$conditions[] = "p.status=1 AND p.state=1";
	if(isset($params['type'])) {
		$type = explode(",", $params['type']);
		$type = array_unique($type);
		foreach ($type as $val) {
			switch ($val) {
				case 'image':
					$conditions[] = "p.picture!=''";
					break;
				case 'commend':
					$conditions[] = "p.ifcommend>0";
					break;
				case 'brand':
					$conditions[] = "p.brand_id>0";
					break;
				case 'hot':
					$orderbys[] = "clicked DESC";
					break;
				default:
					break;
			}
		}
	}
	if (isset($params['state'])) {
		$conditions[] ="p.state='".intval($params['state'])."'";
	}
	if (isset($params['companyid'])) {
		$conditions[] = "p.company_id=".$params['companyid'];
	}
	if (isset($params['memberid'])) {
		$conditions[] = "p.member_id=".$params['memberid'];
	}
	if (isset($params['sortid'])) {
		$conditions[] = "p.sort_id='".$params['sortid']."'";
	}
	if (isset($params['typeid'])) {
		$conditions[] = "p.producttype_id=".$params['typeid'];
	}
	if (isset($params['brandid'])) {
		$conditions[] = "p.brand_id=".$params['brandid'];
	}	
	if (!empty($params['industryid'])) {
		$conditions[] = "p.industry_id1='".$params['industryid']."' OR p.industry_id2='".$params['industryid']."' OR p.industry_id3='".$params['industryid']."'";
	}
	if (!empty($params['areaid'])) {
		$conditions[] = "p.area_id1='".$params['areaid']."' OR p.area_id2='".$params['areaid']."' OR p.area_id3='".$params['areaid']."'";
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
	$product->setCondition($conditions);
	$row = $col = 0;
	if (isset($params['row'])) {
		$row = $params['row'];
	}
	if (isset($params['col'])) {
		$col = $params['col'];
	}
	$product->setLimitOffset($row, $col);
	$sql = "SELECT p.id as productid,p.id,p.name as productname,p.clicked AS hits,p.name,p.price,picture,created,cache_companyname as companyname FROM {$product->table_prefix}products p ".$product->getCondition()."{$orderby}".$product->getLimitOffset();
	$result = $product->dbstuff->GetArray($sql);
	$return = null;
	if (!empty($result)) {
		$i_count = count($result);
		for ($i=0; $i<$i_count; $i++){
			$url = $product_controller->rewrite($result[$i]['productid'], $result[$i]['productname']);
			if (isset($params['titlelen'])) {
	    		$result[$i]['productname'] = mb_substr($result[$i]['productname'], 0, $params['titlelen']);
	    		$result[$i]['companyname'] = mb_substr($result[$i]['companyname'], 0, $params['titlelen']);
	    	}			
			$return.= str_replace(array("[link:title]", "[link:url]", "[field:title]", "[img:src]", "[field:fulltitle]", "[field:pubdate]", "[field:id]", "[img:thumb]", "[field:company]","[field:price]","[field:hits]"), array($url, '<a href="'.$url.'" title="'.$result[$i]['name'].'">'.$result[$i]['productname'].'</a>', $result[$i]['productname'], "attachment/".$result[$i]['picture'].".small.jpg", $result[$i]['productname'], @date("m/d", $result[$i]['created']), $result[$i]['productid'], "attachment/".$result[$i]['picture'].".small.jpg", $result[$i]['companyname'],$result[$i]['price'], number_format($result[$i]['hits'])), $content);
		}
	}
	return $return;
}
?>