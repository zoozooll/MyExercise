<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1187 $
 */
function smarty_block_friendlink($params, $content, &$smarty) {
	if ($content === null) return;
	$conditions = array();
	$conditions[] = "status='1'";
	if (!class_exists("Friendlinks")) {
		uses("friendlink");
		$friendlink = new Friendlinks();
	}else{
	    $friendlink = new Friendlinks();
	}
	$show_logo = false;
	if (isset($params['type'])) {
		if ($params['type']=='image') {
			$conditions[] = "logo!=''";
		}
	}
	if (isset($params['typeid'])) {
		$conditions[] = "friendlinktype_id='".$params['typeid']."'";
	}
	if (isset($params['seperate'])) {
		$seperate = $params['seperate'];
	}else{
		$seperate = " | ";
	}
	if (isset($params['exclode'])) {
		$conditions[] = "id NOT IN (".$params['exclode'].")";
	}
	$friendlink->setCondition($conditions);
	$result = $friendlink->dbstuff->CacheGetArray("SELECT *,logo AS image FROM ".$friendlink->table_prefix."friendlinks ".$friendlink->getCondition()." ORDER BY priority ASC");
	$return = null;
	if (!empty($result)) {
		$i_count = count($result);
		for ($i=0; $i<$i_count; $i++){
			$url = $result[$i]['url'];			
			$return.= str_replace(array("[link:title]", "[link:url]", "[field:title]", "[field:target]", "[field:style]", "[field:tip]", "[field:logo]", "[img:src]"), array($url, '<a href="'.$url.'" title="'.$result[$i]['title'].'">'.$result[$i]['title'].'</a>', $result[$i]['title'], $result[$i]['target'], $result[$i]['style'], $result[$i]['tip'], $result[$i]['image'], $result[$i]['image']), $content);
		}
	}
	return $return;
}
?>