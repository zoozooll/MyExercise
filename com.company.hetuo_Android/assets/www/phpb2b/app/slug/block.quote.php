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
function smarty_block_quote($params, $content, &$smarty) {
	if ($content === null) return;
	$conditions = array();
	if (class_exists("Quotes")) {
		$quote = new Quotes();
		$quote_controller = new Quote();
	}else{
		uses("quote");
		$quote = new Quotes();
		$quote_controller = new Quote();
	}
	if (!empty($params['productid'])) {
		$conditions[] ="q.product_id='".intval($params['productid'])."'";
	}
	if (!empty($params['marketid'])) {
		$conditions[] = "q.market_id=".$params['marketid'];
	}
	if (!empty($params['areaid'])) {
		$conditions[] = "q.area_id='".$params['areaid']."' OR p.area_id1='".$params['areaid']."' OR p.area_id2='".$params['areaid']."' OR OR p.area_id3='".$params['areaid']."'";
	}
	$orderby = null;
	if (isset($params['orderby'])) {
		$orderby = " ORDER BY ".trim($params['orderby'])." ";
	}else{
		$orderby = " ORDER BY id DESC ";
	}
	$quote->setCondition($conditions);
	$row = $col = 0;
	if (isset($params['row'])) {
		$row = $params['row'];
	}
	if (isset($params['col'])) {
		$col = $params['col'];
	}
	$sql = "SELECT id,title,content,max_price,min_price,created FROM {$quote->table_prefix}quotes q ".$quote->getCondition()."{$orderby}".$quote->getLimitOffset();
	$result = $quote->dbstuff->GetArray($sql);
	$return = null;
	if (!empty($result)) {
		$i_count = count($result);
		for ($i=0; $i<$i_count; $i++){
			$url = $quote_controller->rewrite($result[$i]['id'], $result[$i]['title']);
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