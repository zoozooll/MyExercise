<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 889 $
 */
function smarty_block_newstype($params, $content, &$smarty) {
	if ($content === null) return;
	$return = null;
	if(isset($params['id'])){
		require(CACHE_PATH. "cache_type.php");
		$title = $_PB_CACHE['newstype'][$params['id']];
		if (!empty($title)) {
			$url = "news/list.php?typeid=".$params['id'];
			$return.= str_replace(array("[field:title]", "[field:id]", "[link:title]"), array($title, $params['id'], $url), $content);
		}else{
			$return.= L("unknown", "tpl");
		}
	}else{
		$conditions = array();
		$orderby = array();
		if (class_exists("Newstype")) {
			$newstype = new Newstypes();
			$newstype_controller = new Newstype();
		}else{
		    uses("newstype");
		    $newstype = new Newstypes();
			$newstype_controller = new Newstype();
		}
		if(isset($params['exclude'])){
			$conditions[] = "id NOT IN (".$params['exclude'].")";
		}
		if(isset($params['include'])){
			$conditions[] = "id IN(".$params['include'].")";
		}
		if (isset($params['level'])) {
			$conditions[] = "level_id='".$params['level']."'";
		}
		if (isset($params['parentid'])) {
			$conditions[] = "parent_id='".$params['parentid']."'";
		}
		if (isset($params['orderby'])) {
			$orderby[] = trim($params['orderby']);
		}else{
			$orderby[] = "id DESC ";
		}		
		$newstype->setOrderby($orderby);
		$newstype->setCondition($conditions);
		$row = $col = 0;
		if (isset($params['row'])) {
			$row = $params['row'];
		}
		if (isset($params['col'])) {
			$col = $params['col'];
		}
		$newstype->setLimitOffset($row, $col);
		$sql = "SELECT * FROM {$newstype->table_prefix}newstypes ".$newstype->getCondition().$newstype->getOrderby().$newstype->getLimitOffset();
		$result = $newstype->dbstuff->GetArray($sql);
		$return = $link_title = null;	
		if (!empty($result)) {
		$i_count = count($result);
		for ($i=0; $i<$i_count; $i++){
			$result[$i]['title'] =$result[$i]['name'];
			if (isset($params['titlelen'])) {
	    		$result[$i]['title'] = mb_substr($result[$i]['name'], 0, $params['titlelen']);
	    	}
	    	$url = "news/list.php?typeid=".$result[$i]['id'];
			$return.= str_replace(array("[field:title]", "[field:fulltitle]", "[field:id]", "[link:title]", "[link:url]"), array($result[$i]['title'], $result[$i]['name'], $result[$i]['id'], "<a title=\"".$result[$i]['name']."\" href=\"".$url."\" target=\"_blank\">".$result[$i]['title']."</a>", $url), $content);
		}
	}	
	}
	return $return;
}
?>