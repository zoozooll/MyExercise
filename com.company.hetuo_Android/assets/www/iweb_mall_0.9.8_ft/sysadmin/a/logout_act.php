<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
session_destroy();
echo '<script>top.location.href="login.php";</script>';
exit;
?>