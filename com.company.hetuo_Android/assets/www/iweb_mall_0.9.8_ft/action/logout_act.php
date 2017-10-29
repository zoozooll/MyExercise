<?php
//会员退出
session_destroy();
unset_cookie('iweb_login');
echo "<script>top.location.href='login.php'</script>";
?>