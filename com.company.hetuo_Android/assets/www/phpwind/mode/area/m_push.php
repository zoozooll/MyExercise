<?php
!defined('P_W') && exit('Forbidden');

define('AREA_SCR','push');

$action = GetGP('action');
$allow_actions = array(
	'default',
	'pushto',
);
$action = in_array($action, $allow_actions) ? $action : 'default';

$thisBaseName	= $basename.'q=push';

include Pcv(M_P."require/push/$action.php");
?>