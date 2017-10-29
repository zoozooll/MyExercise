<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 121 $
 */
define('CURSCRIPT', 'redirect');
require("libraries/common.inc.php");
require("share.inc.php");
$redirect = array(
"office-room/offer"=>"office-room/offer.php?do=edit",
"office-room/company"=>"office-room/company.php",
"office-room/product"=>"office-room/product.php?do=edit",
"office-room"=>"office-room/",
);
if (isset($_GET['url'])) {
	$do = trim($_GET['url']);
	pheader("location:".$redirect[$do]);
}
if (isset($_GET['code'])) {
	$code = trim($_GET['code']);
	$msg = Redirect($code);
	$dir = $smarty->template_dir.$theme_name.DS;
	if (file_exists($dir.$code.".html")) {
		setvar("msg", $msg);
		render($code);
		exit;
	}else{
		flash("{$code} {$msg}", '', 0);
		exit(0);
	}
}
function Redirect($code) {
	$codes = array(
	100 => "Continue",
	101 => "Switching Protocols",
	200 => "OK",
	201 => "Created",
	202 => "Accepted",
	203 => "Non-Authoritative Information",
	204 => "No Content",
	205 => "Reset Content",
	206 => "Partial Content",
	300 => "Multiple Choices",
	301 => "Moved Permanently",
	302 => "Found",
	303 => "See Other",
	304 => "Not Modified",
	305 => "Use Proxy",
	307 => "Temporary Redirect",
	400 => "Bad Request",
	401 => "Unauthorized",
	402 => "Payment Required",
	403 => "Forbidden",
	404 => "Not Found",
	405 => "Method Not Allowed",
	406 => "Not Acceptable",
	407 => "Proxy Authentication Required",
	408 => "Request Time-out",
	409 => "Conflict",
	410 => "Gone",
	411 => "Length Required",
	412 => "Precondition Failed",
	413 => "Request Entity Too Large",
	414 => "Request-URI Too Large",
	415 => "Unsupported Media Type",
	416 => "Requested range not satisfiable",
	417 => "Expectation Failed",
	500 => "Internal Server Error",
	501 => "Not Implemented",
	502 => "Bad Gateway",
	503 => "Service Unavailable",
	504 => "Gateway Time-out"
	);
	return $codes[$code];
}
?>