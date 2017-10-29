<?php
!function_exists('readover') && exit('Forbidden');

$db->update("DELETE pw_msg, pw_msgc FROM pw_msg LEFT JOIN pw_msgc ON pw_msg.mid = pw_msgc.mid WHERE pw_msg.mdate<".pwEscape($timestamp-2592000)."AND pw_msg.type='public'");

$db_plist && count($db_plist)>1 && $db->update("DELETE FROM pw_pidtmp");
?>