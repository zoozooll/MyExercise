<?php
!defined('M_P') && exit('Forbidden');
InitGP(array('invokename'));
include_once(D_P.'data/bbscache/forum_cache.php');

$invoke = $invokeService->getInvokeByName($invokename,$cateid);
$invokepieces = $invokeService->getInvokePieceByInvokeName($invokename);
include areaLoadFrontView($action);
ajax_footer();
?>