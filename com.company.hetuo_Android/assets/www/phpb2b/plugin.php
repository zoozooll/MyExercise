<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 135 $
 */
define('CURSCRIPT', 'plugin');
require("libraries/common.inc.php");
if (isset($_GET['name'])) {
	$name = htmlspecialchars(trim($_GET['name']));
	$plugin_info = $pdb->GetRow($sql = "SELECT * FROM {$tb_prefix}plugins WHERE name='".$name."'");
	if (!$plugin_info OR empty($plugin_info)) {
		die(L("plugin_not_exists"));
	}else{
		if (!class_exists("Plugin")) {
			uses("plugin");
			$plugin = new Plugin($name);
		}else{
		    $plugin = new Plugin($name);
		}
		/**
		 * Sends http headers
		 */
		header('Expires: ' . gmdate('D, d M Y H:i:s') . ' GMT'); // rfc2616 - Section 14.21
		header('Last-Modified: ' . gmdate('D, d M Y H:i:s') . ' GMT');
		header('Cache-Control: no-store, no-cache, must-revalidate, pre-check=0, post-check=0, max-age=0'); // HTTP/1.1
		header('Pragma: no-cache'); // HTTP/1.0
		if (!defined('IS_TRANSFORMATION_WRAPPER')) {
			// Define the charset to be used
			header('Content-Type: text/html; charset=' . $charset);
		}
		echo '<?xml version="1.0" encoding="'.$charset.'"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="'.$app_lang.'" lang="'.$app_lang.'">
<head>
<meta http-equiv="Content-Type" content="text/html; charset='.$charset.'" />
<meta name="keywords" content="" />
<meta name="description" content="'.mb_substr(htmlspecialchars($plugin_info['description']), 0, 5).'" />
<title>'.implode(" - ", array($name, $plugin_info['title'])).'</title>
</head>
<body>';
		include(APP_PATH. "slug".DS."function.plugin.php");
		smarty_function_plugin(array("name"=>$name));
		echo '</body></html>';
	}
}else{
	flash("plugin_not_exists", URL);
}
?>