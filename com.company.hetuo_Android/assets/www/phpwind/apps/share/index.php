<?php
!defined('A_P') && exit('Forbidden');
if ($route == "share") {
	require_once Pcv($basePath . '/action/m_share.php');
} elseif ($route == "sharelink") {
	require_once Pcv($basePath . '/action/m_sharelink.php');
}
?>