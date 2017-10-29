<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 953 $
 */
define('CURSCRIPT', 'rss');
require("libraries/common.inc.php");
require("share.inc.php");
uses("setting");
$setting = new Settings();
include_once(PHPB2B_ROOT. 'libraries/feedcreator.class.php');
$rss = new UniversalFeedCreator();
$rss->useCached();
$rss->title = $_PB_CACHE['setting']['site_name'];
$rss->description = $setting->getValue("site_description");
$rss->link = URL;
$rss->syndicationURL = URL."rss.php";

//announce
$announce = $pdb->GetArray("SELECT * FROM {$tb_prefix}announcements ORDER BY display_order ASC,id DESC LIMIT 0,5");
if (!empty($announce)) {
	foreach ($announce as $key=>$val) {
		$item = new FeedItem(); 
	    $item->title = $val['subject']; 
	    $item->link = URL."announce.php?id=".$val['id']; 
	    $item->description = $val['message']; 
	    $item->date = $val['created']; 
	    $item->source = URL."announce.php?id=".$val['id']; 
	    $item->author = $setting->getValue("company_name");   
	    $rss->addItem($item);
	}
}

$image = new FeedImage();
$image->title = $setting->getValue("site_banner_word");
$image->url = URL."images/logo.gif";
$image->link = URL;
$image->description = $setting->getValue("site_description");
$rss->image = $image;
$rss->saveFeed("RSS1.0", "data/appcache/rss.xml");
?>