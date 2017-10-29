<?php
$IWEB_SHOP_IN = true;
include(dirname(__file__)."/../foundation/fgetandpost.php");
$do=get_args("do");
$action=explode("_iweb_",$do);
if(count($action)==2)
{
	include("Actions.php");
	$actions=Actions::getInstance($action[0]);
	$actions->doAction($action[1]);
}
?>