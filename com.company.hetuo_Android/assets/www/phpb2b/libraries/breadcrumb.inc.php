<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1393 $
 */
function bread_compare($a, $b){
    if ($a['displayorder'] == $b['displayorder']) return 0;
    return ($a['displayorder'] < $b['displayorder']) ? -1 : 1;
}

function bread_array($breads, $seperate = " &gt; ")
{
	$bread = array();
    uasort($breads, "bread_compare");
    foreach ($breads as $key=>$val){
        if(!empty($val['url'])) {
            if(isset($val['params'])) $bread[] = "<a href='".$val['url'].queryString($val['params'])."' target='_self' title='".$val['title']."'>".$val['title']."</a>";
            else $bread[] = "<a href='".$val['url']."' target='_self' title='".$val['title']."'>".$val['title']."</a>";
        }else {
            $bread[] = $val['title'];
        }
    }
    return implode($seperate, $bread);
}

function queryString($q, $extra = array(), $escape = false) {
	if (empty($q) && empty($extra)) {
		return null;
	}
	$join = '&';
	if ($escape === true) {
		$join = '&amp;';
	}
	$out = '';

	if (is_array($q)) {
		$q = array_merge($extra, $q);
	} else {
		$out = $q;
		$q = $extra;
	}
	$out .= http_build_query($q, null, $join);
	if (isset($out[0]) && $out[0] != '?') {
		$out = '?' . $out;
	}
	return $out;
}
?>