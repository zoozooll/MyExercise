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
define('CURSCRIPT', 'space');
require("libraries/common.inc.php");
$do = null;
$space_actions = array(
"intro", 
"home", 
"product", 
"offer", 
"hr", 
"news", 
"album", 
"index", 
"contact", 
"feedback"
);
$userid = 0;
if(isset($_GET['userid'])) {
	$userid = $_GET['userid'];
}
if ($subdomain_support) {
	$hosts = explode($subdomain_support, pb_getenv('HTTP_HOST'));
	if(($hosts[0]!="www")){
	    $userid = trim($hosts[0]);
	}
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if($do=="" || $do=="index" || !in_array($do, $space_actions)) {
    	$do = "home";
	}
}else{
	$do = "home";
}
require("space/common.inc.php");
require("space/".$do.".php");
?>
