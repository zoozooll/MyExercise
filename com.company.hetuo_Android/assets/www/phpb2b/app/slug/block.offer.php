<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1221 $
 */
function smarty_block_offer($params, $content, &$smarty, &$repeat) {
	if ($content === null) return;
	global $_PB_CACHE;
	$conditions[] = "t.status='1'";
	//old version has no setting1,so @
	@require(CACHE_PATH. "cache_setting1.php");
	if (!class_exists("Trades")) {
		uses("trade");
		$trade = new Trades();
		$trade_controller = new Trade();
	}else{
	    $trade = new Trades();
	    $trade_controller = new Trade();
	}
	if ($_PB_CACHE['setting1']['offer_expire_method']) {
		switch ($_PB_CACHE['setting1']['offer_expire_method']) {
			case "2":
				$conditions[] = "t.expire_time>".$trade->timestamp;
				break;
			case "3":
				$conditions[] = "t.expire_time>".$trade->timestamp;
				break;
			default:
				break;
		}
	}
	if (isset($params['type'])) {
		$type = explode(",", $params['type']);
		$type = array_unique($type);
		foreach ($type as $val) {
			switch ($val) {
				case 'image':
					$conditions[] = "t.picture!=''";
					break;
				case 'urgent':
					$conditions[] = "t.if_urgent='1'";
					break;
				case 'company':
					$conditions[] = "t.company_id>0";
					break;
				case 'commend':
					$conditions[] = "t.if_commend>0";
					break;
				default:
					break;
			}
		}
	}
	if (isset($params['industryid'])) {
		$conditions[] = "t.industry_id1='".$params['industryid']."' OR t.industry_id2='".$params['industryid']."' OR t.industry_id3='".$params['industryid']."'";
	}
	if (isset($params['areaid'])) {
		$conditions[] = " (t.area_id1='".$params['areaid']."' OR t.area_id2='".$params['areaid']."' OR t.area_id3='".$params['areaid']."')";
	}
	if(isset($params['expday'])){
		$conditions[] = "t.expire_time<'".($params['expday']*86400+$trade->timestamp)."'";
	}
	if(isset($params['subday'])){
		$conditions[] = "t.submit_time>'".($trade->timestamp-$params['expireday']*86400)."'";
	}
	if (isset($params['typeid'])) {
		if(!empty($params['typeid'])){
			if (strpos($params['typeid'], ",")>0) {
				$tmp_ids = explode(",", $params['typeid']);
				$conditions[] = "t.type_id in ('".implode("','", $tmp_ids)."')";
			}else{
				$conditions[] = "t.type_id='".$params['typeid']."'";
			}
		}
	}
	if (isset($params['urgent'])) {
		$conditions[] = "t.if_urgent='1'";
	}
	if (isset($params['memberid'])) {
		$conditions[] = "t.member_id='".$params['memberid']."'";
	}
	if (isset($params['cash'])) {
		$conditions[] = "t.require_point>0";
	}
	$trade->setCondition($conditions);
	$row = $col = 0;
	$orderby = null;
	if (isset($params['orderby'])) {
		$orderby = " ORDER BY ".trim($params['orderby'])." ";
	}else{
		$orderby = " ORDER BY modified DESC";
	}
	if (isset($params['row'])) {
		$row = $params['row'];
	}
	if (isset($params['col'])) {
		$col = $params['col'];
	}
	$trade->setLimitOffset($row, $col);
	$sql = "SELECT id,type_id,title as fulltitle,title,content,content as fullcontent,created,submit_time,picture FROM {$trade->table_prefix}trades t ".$trade->getCondition()."{$orderby}".$trade->getLimitOffset();
	if ($_PB_CACHE['setting']['label_cache']) {
		$result = $trade->dbstuff->CacheGetArray($sql);
	}else{
		$result = $trade->dbstuff->GetArray($sql);
	}
	$return = $style = $h3_style = $link_title = null;
	$offer_typenames = $trade_controller->getTradeTypes();	
	if (!empty($result)) {
		$i_count = count($result);
		for ($i=0; $i<$i_count; $i++){
			$style = null;
			$url = $trade_controller->rewrite($result[$i]['id'], $result[$i]['type_id'], $result[$i]['title']);
			$result[$i]['title'] = strip_tags($result[$i]['title']);
			$result[$i]['content'] = strip_tags($result[$i]['content']);
			if (isset($params['titlelen'])) {
	    		$result[$i]['title'] = mb_substr($result[$i]['title'], 0, $params['titlelen']);
	    	}		
	    	if (isset($params['infolen'])) {
	    		$result[$i]['content'] = mb_substr($result[$i]['content'], 0, $params['infolen']);
	    	}
	    	if (isset($params['titlestart'])) {
	    		$result[$i]['title'] = $params['titlestart'].$result[$i]['title'];
	    	}
	    	if (isset($params['magic']))  {
	    		if ($i==0) {
	    			if(!empty($result[$i]['picture'])){
	    				$style = " style=\"height:70px; background:url(".URL."attachment/".$result[$i]['picture'].".small.jpg".") no-repeat; padding:0 0 0 90px; overflow:hidden; width:120px;\"";
	    				$h3_style = " style=\"padding:0 0 0 5px;\"";
	    			}
	    			$link_title = "<h3".$h3_style."><a href='{$url}'>".$result[$i]['title']."</a></h3>".$result[$i]['content'];
	    		}else{
	    			$link_title = "<a href='{$url}'>".$result[$i]['title']."</a>";
	    		}
			}
			$return.= str_replace(array("[field:title]", "[field:fulltitle]","[field:typename]", "[link:title]", "[link:url]", "[field:id]", "[field:pubdate]", "[img:thumb]", "[img:src]", "[field:content]", "[field:style]", "[field:url]", "[field:typeid]"), array($result[$i]['title'], $result[$i]['fulltitle'], $offer_typenames[$result[$i]['type_id']], $url, '<a href="'.$url.'" title="'.$result[$i]['fulltitle'].'">'.$result[$i]['title'].'</a>',$result[$i]['id'], @date("m/d", $result[$i]['submit_time']), "attachment/".$result[$i]['picture'].".small.jpg", "attachment/".$result[$i]['picture'], $result[$i]['content'], $style, $link_title, $result[$i]['type_id']), $content);
		}
	}
	return $return;
}
?>