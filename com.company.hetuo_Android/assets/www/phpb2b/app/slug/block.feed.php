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
function smarty_block_feed($params, $content, &$smarty) {
	if ($content === null) return;
	$conditions = array();
	if (class_exists("Services")) {
		$service = new Services();
		$service_controller = new Service();
	}else{
	    uses("service");
	    $service = new Services();
		$service_controller = new Service();
	}
	$orderby = array();
	require(CACHE_PATH. "cache_type.php");
	if(isset($params['type'])) {
		$conditions[] = "type='".$params['type']."'";
	}
	if (isset($params['typeid'])) {
		$conditions[] = "type_id='".$params['typeid']."'";
	}
	if (isset($params['id'])) {
		$conditions[] = "id='".$params['id']."'";
	}
	if (isset($params['orderby'])) {
		$orderby[] = trim($params['orderby']);
	}else{
		$orderby[] = "id DESC ";
	}
	$service->setOrderby($orderby);
	$service->setCondition($conditions);
	$row = $col = 0;
	if (isset($params['row'])) {
		$row = $params['row'];
	}
	if (isset($params['col'])) {
		$col = $params['col'];
	}
	$service->setLimitOffset($row, $col);
	$sql = "SELECT * FROM {$service->table_prefix}feeds  ".$service->getCondition().$service->getOrderby().$service->getLimitOffset();
	$result = $service->dbstuff->GetArray($sql);
	$return = $link_title = null;
	if (!empty($result)) {
		$i_count = count($result);
		for ($i=0; $i<$i_count; $i++){
			$data = unserialize($result[$i]['data']);
			foreach ($data as $val) {
				$link_title.=$val;
			}
			$return.= str_replace(array("[link:title]"), array($link_title), $content);
		}
	}
	return $return;
}
?>