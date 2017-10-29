<?php
!defined('M_P') && exit('Forbidden');
include_once(D_P.'data/bbscache/forum_cache.php');
require_once(M_P.'require/invokeconfig.php');

InitGP(array('invokepieceid','loopid'));

$invokepiece = $invokeService->getInvokePieceByInvokeId($invokepieceid);

$tempfid = $fid;
if ($invokepiece['rang']!='fid') {
	$tempfid = 0;
}
$param	= $invokepiece['param'];

$pushs	= $invokeService->getPushDataEffect($invokepieceid,$tempfid,$loopid,$invokepiece['num']);
$custom = array();
foreach ($pushs as $value) {
	$custom[$value['id']] = $value['data'];
}

include areaLoadFrontView($action);
ajax_footer();
?>