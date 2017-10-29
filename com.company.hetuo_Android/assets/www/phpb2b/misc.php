<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1136 $
 */
define('CURSCRIPT', 'attachment');
require("libraries/common.inc.php");
require("share.inc.php");
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	switch ($do) {
		case "download":
			uses("attachment");
			$attachment = new Attachments();
			if (empty($_GET['aid'])) {
				flash();
			}
			$attach_id = authcode(rawurldecode($_GET['aid']), "DECODE");
			if (empty($attach_id)) {
				flash();
			}
			require(LIB_PATH. "download.class.php");
			require(LIB_PATH. "js.class.php");
			$download = new Downloads('exe,js',false);
			$download->attach_filename = rawurlencode($attachment->getAttachFileName($attach_id));
			$filename = $attachment->file_url;
			if(!$download->downloadfile($filename))
			{
				die($download->geterrormsg());
			}else{
				JS::Close();
			}
			break;
		default:
			break;
	}
}
if(empty($_GET['id'])){
	$picture_src = URL."images/watermark.png";
}
if (isset($_GET['source'])) {
	$file_source = trim(rawurldecode($_GET['source']));
	$picture_src = URL.$attachment_url.$file_source;
}
setvar("img_src", $picture_src);
render("attachment");
?>