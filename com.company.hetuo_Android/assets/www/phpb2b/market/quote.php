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
define('CURSCRIPT', 'quote');
require("../libraries/common.inc.php");
require("../share.inc.php");
require(PHPB2B_ROOT.'libraries/page.class.php');
require(CACHE_PATH. "cache_area.php");
require(CACHE_PATH. "cache_typeoption.php");
uses("quote");
$page = new Pages();
$page->displaypg = 25;
$quote = new Quotes();
$condition = null;
$conditions = array();
setvar("Measuries", $_PB_CACHE['measuring']);
setvar("Monetaries", $_PB_CACHE['monetary']);
$tpl_file = "quote";
$viewhelper->setTitle(L("price_quotes", "tpl"));
$viewhelper->setPosition(L("price_quotes", "tpl"), "market/quote.php");
if (isset($_GET['id'])) {
	$id = intval($_GET['id']);
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	switch ($do) {
		case "chart":
			include_once LIB_PATH. 'ofc/chart_object.php';
			include_once LIB_PATH. 'time.class.php';
			if (empty($_GET['pn'])) {
				flash("pls_input_product_name", "quote.php");
			}
			$info = $pdb->GetRow("SELECT * FROM ".$tb_prefix."products WHERE name='".trim($_GET['pn']."'"));
			if (empty($info)) {
				flash("product_quote_not_exists", "quote.php", 0);
			}
			$date_from = $date_to = 0;
			if (!empty($_GET['ds'])) {
				$date_from = Times::dateConvert($_GET['ds']);
			}
			if (!empty($_GET['de'])) {
				$date_to = Times::dateConvert($_GET['de']);
			}
			$product_id = $info['id'];
			$quote->mkCacheData($date_from, $date_to, 1);
			$info['pubdate'] = date("Y-m-d", $info['created']);
			$viewhelper->setPosition($info['name']);
			$viewhelper->setTitle($info['name']);
			$label_y = $pdb->GetRow("select max(max_price) as price_max,min(min_price) as price_min from ".$tb_prefix."quotes WHERE product_id=".$product_id." AND created BETWEEN $date_from AND $date_to");
			$y_max = (!empty($label_y['price_max']))?$label_y['price_max']:100;
			// set the Y max
			$info['max_price'] = $label_y['price_max'];
			$info['min_price'] = $label_y['price_min'];
			setvar("item", $info);
			setvar("chart_image", open_flash_chart_object( 700, 278, URL.'api.php?do=get_cache_quote&item='.$quote->cache_datafile, false));
			$tpl_file = "quote.detail";
			render($tpl_file, true);
			break;
	
		default:
			break;
	}
}
if (isset($_GET['catid'])) {
	$type_id = intval($_GET['catid']);
	$conditions[] = "Quote.type_id='".$type_id."'";
}
$quote->setCondition($conditions);
$amount = $quote->findCount(null, $conditions);
$page->setPagenav($amount);
$joins[] = "LEFT JOIN {$tb_prefix}markets m ON m.id=Quote.market_id";
$joins[] = "LEFT JOIN {$tb_prefix}products p ON p.id=Quote.product_id";
$fields = "Quote.id,m.name AS marketname,Quote.title,Quote.units,Quote.currency,Quote.created AS pubdate,p.name AS productname,ROUND((Quote.min_price+Quote.max_price)/2,2) AS price,Quote.area_id1,Quote.area_id2";
$result = $quote->findAll($fields, $joins, $conditions,"Quote.id DESC",$page->firstcount,$page->displaypg);
setvar("Items", $result);
/**types**/
$quote->findIt("quotetypes");
if (!empty($quote->params['data'])) {
	setvar("Categories", $quote->params['data'][2]);
}
/**types**/
$passed_time = date("Y-m-d",mktime(0,0,0,date("m")-12,date("d"),date("Y")));
uaAssign(array("ByPages"=>$page->getPagenav(), "Areas"=>$_PB_CACHE['area'], "QuoteSearchFrom"=>$passed_time, "QuoteSearchTo"=>date("Y-m-d")));
render($tpl_file);
?>