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
define('CURSCRIPT', 'post');
require("../libraries/common.inc.php");
require("../share.inc.php");
require(LIB_PATH. "validation.class.php");
$validate = new Validation();
if (session_id() == '' ) { 
	require_once(LIB_PATH. "session_php.class.php");
	$session = new PbSessions();
}
uses("trade","member","tradefield","tag","typeoption");
$tag = new Tags();
$offer = new Tradefields();
$typeoption = new Typeoption();
$member = new Members();
$trade = new Trades();
$trade_controller = new Trade();
$tradefield = new Tradefields();
$expires = $trade_controller->getOfferExpires();
setvar("TradeTypes",$trade_controller->getTradeTypes());
setvar("Genders", $typeoption->get_cache_type("gender",null,array("0", "-1")));
setvar("PhoneTypes", $typeoption->get_cache_type("phone_type"));
setvar("ImTypes", $typeoption->get_cache_type("im_type"));
$if_visit_post = $_PB_CACHE['setting']['vis_post'];
if(!$if_visit_post){
	$smarty->flash('visitor_forbid', URL, 0);
}
capt_check("capt_post_free");
if (isset($_POST['visit_post'])) {
	pb_submit_check('visit_post');
	$trade->setParams();
	$tradefield->setParams();
	$if_title_exists = $trade->findByTitle($trade->params['data']['trade']['title']);
	if (!empty($if_title_exists)) {
		$trade->validationErrors[] = L("semilar_offer_post");
	}
	if (!$validate->notEmpty($trade->params['data']['trade']['title'])) {
		$trade->validationErrors[] = L("title_cant_be_empty");
	}
	$trade->params['expire_days'] = $_POST['expire_days'];
	$trade->params['data']['trade']['tag_ids'] = $tag->setTagId($_POST['data']['tag']);;
	$if_check = $_PB_CACHE['setting']['vis_post_check'];
	$msg = null;
	$words = $pdb->GetArray("SELECT * FROM {$tb_prefix}words");
	if (!empty($words)) {
		foreach ($words as $word_val) {
			if(!empty($word_val['title'])){
				str_replace($word_val['title'], "*", $trade->params['data']['trade']['title']);
				str_replace($word_val['title'], "*", $trade->params['data']['trade']['content']);
			}
		}
		$item['forbid_word'] = implode("\r\n", $tmp_str);
	}
	if ($if_check) {
		$trade->params['data']['trade']['status'] = 0;
		$msg = 'pls_wait_for_check';
	}else{
		$trade->params['data']['trade']['status'] = 1;
		$msg = 'success';
	}
	if (!empty($trade->validationErrors)) {
		setvar("item", am($trade->params['data']['trade'], $tradefield->params['data']['tradefield']));
		setvar("Errors", $validate->show($trade));
	}else{
		$result = $trade->Add();
		if ($result) {
			$smarty->flash($msg, URL);
		}else{
			$smarty->flash();
		}
	}
}
formhash();
$viewhelper->setPosition(L("free_release_offer", "tpl"));
setvar("OfferExpires", $expires);
setvar("sid",md5(uniqid($time_stamp)));
render("offer.freepost");
?>