<?php
!defined('A_P') && exit('Forbidden');
if ($route == "groups") {
	require_once Pcv($basePath . '/action/m_groups.php');
} elseif ($route == "group") {
	require_once Pcv($basePath . '/action/m_group.php');
} elseif ($route == "galbum") {
	require_once Pcv($basePath . '/action/m_galbum.php');
}
?>