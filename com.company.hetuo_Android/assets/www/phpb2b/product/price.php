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
define('CURSCRIPT', 'price');
require("../libraries/common.inc.php");
require("../share.inc.php");
require(LIB_PATH. 'page.class.php');
require(CACHE_PATH. "cache_typeoption.php");
require(CACHE_PATH. "cache_type.php");
require(CACHE_PATH. "cache_area.php");
uses("productprice","productcategory");
$price = new Productprices();
$page = new Pages();
$productcategory = new Productcategories();
$conditions = array();
setvar("PriceTypes", $_PB_CACHE['price_type']);
setvar("Productcategory",$_PB_CACHE['productcategory']);
setvar("Measuries", $_PB_CACHE['measuring']);
setvar("Monetaries", $_PB_CACHE['monetary']);
setvar("Areas",am($_PB_CACHE['area'][1], $_PB_CACHE['area'][2], $_PB_CACHE['area'][3]));
$tpl_file = "price";
$viewhelper->setTitle(L("product_prices", "tpl"));
$viewhelper->setPosition(L("product_prices", "tpl"), "product/price.php");
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	switch ($do) {
		case "search":
			if (isset($_GET['catid'])) {
				$cat_id = intval($_GET['catid']);
			}
			if (isset($_GET['typeid'])) {
				$type_id = intval($_GET['typeid']);
			}
			if (isset($_GET['areaid'])) {
				$area_id = intval($_GET['areaid']);
			}
			$viewhelper->setTitle(L("prices", "tpl"));
			$viewhelper->setPosition(L("prices", "tpl"), "product/price.php");
			$tpl_file = "price.list";
			if($type_id){
				$viewhelper->setTitle($_PB_CACHE['price_type'][$type_id]);
				$conditions[] = "Productprice.type_id=".$type_id;
			}
			if($cat_id){
				$viewhelper->setTitle($cat_name = $pdb->GetOne("SELECT name FROM ".$tb_prefix."productcategories WHERE id=".$cat_id));
				$viewhelper->setPosition($cat_name, "product/price.php?do=search&catid=".$cat_id);
				$conditions[] = "Productprice.category_id=".$cat_id;
			}
			if($area_id){
				$viewhelper->setTitle($area_name = $pdb->GetOne("SELECT name FROM ".$tb_prefix."areas WHERE id=".$area_id));
				$viewhelper->setPosition($area_name);
				$conditions[] = "Productprice.area_id=".$area_id;
			}
			$amount = $price->findCount(null, $conditions, null);
			$joins[] = "LEFT JOIN {$tb_prefix}members m ON Productprice.member_id=m.id";
			$joins[] = "LEFT JOIN {$tb_prefix}companies c ON Productprice.company_id=c.id";
			$joins[] = "LEFT JOIN {$tb_prefix}productcategories pc ON Productprice.category_id=pc.id";
			$page->setPagenav($amount);
			$fields = "Productprice.*,m.username AS username,c.name AS companyname,pc.name AS categoryname";
			$result = $price->findAll($fields, $joins, $conditions,"Productprice.id DESC",$page->firstcount,$page->displaypg);
			uaAssign(array("ByPages"=>$page->pagenav));
			setvar("Prices",$result);
			render($tpl_file, true);
			break;
		default:
			break;
	}
}
if (isset($_GET['id'])) {
	$id = intval($_GET['id']);
	$result = $price->read("*", $id);
	if (!empty($result)) {
		$viewhelper->setTitle($result['title']);
		$viewhelper->setPosition($result['title']);
		setvar("item", $result);
		$tpl_file = "price.detail";
	}else{
		flash("data_not_exists");
	}
}
/**types**/
$price->findIt("productcategories");
if (!empty($price->params['data'])) {
	setvar("Items", $price->params['data'][1]);
}
/**types**/
$alphabet_sorts = $cats = array();
foreach ($price->params['result'] as $key1=>$val1) {
	$tmp_str = L10n::getinitial($val1['name']);
	$alphabet_sorts[$tmp_str][$val1['id']] = $val1['name'];
	if($val1['parent_id'] == 0){
		$cats[$val1['id']]['id'] = $val1['id'];
		$cats[$val1['id']]['title'] = $val1['name'];
		foreach ($price->params['result'] as $key2=>$val2) {
			if ($val2['parent_id'] == $val1['id']) {
				$cats[$val1['id']]['child'][$val2['id']]['id'] = $price->params['result'][$key2]['id'];
				$cats[$val1['id']]['child'][$val2['id']]['title'] = $price->params['result'][$key2]['name'];
				foreach ($price->params['result'] as $key3=>$val3) {
					if ($val3['parent_id'] == $val2['id']) {
						$cats[$val1['id']]['child'][$val2['id']]['child'][$val3['id']] = $price->params['result'][$key3];
					}
				}
			}
		}
	}
}
ksort($alphabet_sorts);
if (!empty($alphabet_sorts)) {
	unset($alphabet_sorts['~']);
	setvar("AlphabetSorts", $alphabet_sorts);
}
setvar("Cats", $cats);
render($tpl_file);
?>