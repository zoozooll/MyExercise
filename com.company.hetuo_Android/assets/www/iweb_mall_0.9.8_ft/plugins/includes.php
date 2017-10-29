<?php
$IWEB_SHOP_IN = true;
include(dirname(__file__)."/../api/base_support.php");
if(!isset($api_includes['plugin_url']))
{
	include_once(dirname(__file__)."/ActionUrl.php");
	$api_includes['plugin_url']=true;
}
?>