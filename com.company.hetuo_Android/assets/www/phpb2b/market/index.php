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
define('CURSCRIPT', 'index');
require("../libraries/common.inc.php");
require("../share.inc.php");
uses("market","trade","area");
$market = new Markets();
$market_controller = new Market();
$area = new Areas();
$trade = new Trade();
setvar("AreaItems", $area->getLevelAreas());
$latest_commend_markets = $pdb->GetArray("SELECT * FROM ".$tb_prefix."markets WHERE if_commend='1' AND status='1' AND picture!=''");
$urls = $infos = $images = array();
if (!empty($latest_commend_markets)) {
	while (list($key, $val) = each($latest_commend_markets)) {
		$urls[] = $market_controller->rewrite($val['id'], $val['name']);
		$infos[] = $val['name'];
		$images[] = pb_get_attachmenturl($val['picture']);
	}
	$item['url'] = implode("|", $urls);
	$item['info'] = implode("|", $infos);
	$item['image'] = implode("|", $images);
	setvar("flashvar", $item);
}
render("market.index");
?>