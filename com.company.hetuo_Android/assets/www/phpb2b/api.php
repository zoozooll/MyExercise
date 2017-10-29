<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1267 $
 */
define('CURSCRIPT', 'api');
require("libraries/common.inc.php");
require("share.inc.php");
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	switch ($do) {
		case "get_cache_quote":
			if (!empty($_GET['item']) && is_file(DATA_PATH."tmp/".$_GET['item'])) {	
				include(DATA_PATH."tmp/".$_GET['item']);
				exit(0);
			}
			break;
	
		default:
			break;
	}
}
exit;
?>