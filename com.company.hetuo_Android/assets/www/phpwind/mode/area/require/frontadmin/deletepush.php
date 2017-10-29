<?php
!defined('M_P') && exit('Forbidden');

InitGP(array('pushid'),'',2);

$pushdata	= $invokeService->getPushDataById($pushid);
$invokeService->deletePushData($pushid);

$invokeService->updateCacheDataPiece($pushdata['invokepieceid'],$pushdata['fid'],$pushdata['loopid']);
echo "success";

ajax_footer();
?>