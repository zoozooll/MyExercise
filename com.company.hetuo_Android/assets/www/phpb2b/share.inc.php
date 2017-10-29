<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1025 $
 */
/**
 * Themes
 */
$theme_name = "default";
$style_name = (isset($_PB_CACHE['setting']['theme']) && !empty($_PB_CACHE['setting']['theme']))?$_PB_CACHE['setting']['theme']:"default";
require(PHPB2B_ROOT.'languages'.DS.$app_lang.DS.'template.site.inc.php');
$ADODB_CACHE_DIR = DATA_PATH.'dbcache';
if (!empty($_PB_CACHE['setting']['main_cache'])) {
	if ($_PB_CACHE['setting']['main_cache_lifetime']>0) {
		$smarty->caching = 2;
		$smarty->cache_lifetime = $_PB_CACHE['setting']['main_cache_lifetime'];
		if ($_PB_CACHE['setting']['main_cache_check']) {
			$smarty->compile_check = true;
		}
	}
}
if (!empty($_COOKIE[$cookiepre.'latest_search'])) {
	$latest_search_keywords = explode(",", $_COOKIE[$cookiepre.'latest_search']);
	if (is_array($latest_search_keywords)) {
		$tmp_str = null;
		arsort($latest_search_keywords);
		$latest_search_keywords = array_unique($latest_search_keywords);
		foreach ($latest_search_keywords as $val) {
			$tmp_str.='<a href="offer/list.php?do=search&q='.urlencode($val).'" title="'.$val.'">'.$val.'</a>';
		}
	}
}else{
	$latest_search_keywords = $pdb->GetArray("SELECT name FROM {$tb_prefix}tags WHERE closed=0 ORDER BY id DESC LIMIT 0,3");
	$tmp_str = null;
	foreach ($latest_search_keywords as $val) {
		$tmp_str.='<a href="offer/list.php?do=search&q='.urlencode($val['name']).'" title="'.$val['name'].'">'.$val['name'].'</a>';
	}
}
setvar("SearchHistory", $tmp_str);
$smarty->flash_layout = $theme_name."/flash";
$smarty->assign("theme_img_path", "templates/".$theme_name."/");
$smarty->assign("theme_style_path", "templates/".$style_name."/");
$smarty->assign('ThemeName', $theme_name);
$smarty->setCompileDir("template".DS.$theme_name.DS);
if (!empty($arrTemplate)) {
    $smarty->assign($arrTemplate);
}
?>