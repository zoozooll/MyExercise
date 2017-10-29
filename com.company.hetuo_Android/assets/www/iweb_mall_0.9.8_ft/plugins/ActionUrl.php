<?php
include(dirname(__file__)."/../rooturl.php");
function do_action($path)
{
	$plugin=self_id($path);
	return Root()."/plugins/plugin.php?do=".$plugin."_iweb_";
}
function self_url($path)
{
	$plugin=self_id($path);
	return Root()."/plugins/".$plugin."/";
}
function self_id($path)
{
	$plugin=preg_replace("/.*plugins[\/\\\]{1}([^\/\\\]*).*/i","$1",$path);
	return $plugin;
}
function plugin_check_right($path,$right)
{
	$right_code=self_id($path)."_".$right;
	if(strpos(",".get_sess_rights(),$right_code)) return true;
	else return false;
}
?>