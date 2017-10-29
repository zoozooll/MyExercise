<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 860 $
 */
define('CURSCRIPT', 'brand');
require("libraries/common.inc.php");
require(CACHE_PATH. "cache_type.php");
require(CACHE_PATH. "cache_brand.php");
require("share.inc.php");
uses("brand", "brandtype");
$brand = new Brands();
$brandtype = new Brandtypes();
/**types**/
$brandtype->findIt("brandtypes");
if (!empty($brandtype->params['data'])) {
	setvar("Items", $brandtype->params['data'][1]);
}
/**types**/
$alphabet_sorts = $latest_brands = array();
if (isset($_PB_CACHE['brand'])) {
	foreach ($_PB_CACHE['brand'] as $key=>$val) {
		if (!empty($val)) {
			foreach ($val as $key1=>$val1) {
				$alphabet_sorts[$key][$val1['id']] = $val1['name'];
				$_PB_CACHE['brand'][$key][$key1]['img'] = pb_get_attachmenturl($val1['picture'], null, 'small');
				$_PB_CACHE['brand'][$key][$key1]['title'] = $val1['name'];
			}
		}
	}
	setvar("LatestBrands", $_PB_CACHE['brand']);
}
setvar("AlphabetSorts", $alphabet_sorts);
render("brand.index");
?>