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
if(!defined('IN_PHPB2B')) exit('Not A Valid Entry Point');
uses("typeoption");
$typeoption = new Typeoption();
$manage_types = $main_markets = null;
if (!empty($company->info['main_market'])) {
	$main_market = $typeoption->get_cache_type("main_market");
	foreach (explode(",",$company->info['main_market']) as $market) {
		$main_markets .= $main_market[$market]."&nbsp;&nbsp;";
	}
}
if (!empty($company->info['manage_type'])) {
	$manage_type = $typeoption->get_cache_type("manage_type");
	foreach (explode(",",$company->info['manage_type']) as $m_type) {
		$manage_types .= $manage_type[$m_type]."&nbsp;&nbsp;";
	}
}
$company->info['found_year'] = (!empty($company->info['found_date']))?(date("Y", $company->info['found_date'])):'';
$company->info['manage_type'] = $manage_types;
$company->info['main_market'] = $main_markets;
$company->info['ecnomy'] = $typeoption->get_cache_type("economic_type", $company->info['property']);
$company->info['reg_fund'] = $typeoption->get_cache_type("reg_fund", $company->info['reg_fund']);
$company->info['year_annual'] = $typeoption->get_cache_type("year_annual", $company->info['year_annual']);
setvar("COMPANY", $company->info);
$space->render("intro");
?>