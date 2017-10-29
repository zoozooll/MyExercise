<?php
header("content-type:text/html;charset=utf-8");
require($webRoot."/foundation/asession.php");
require($webRoot."/configuration.php");
require($webRoot."/includes.php");
$is_admin=get_sess_admin();
if($is_admin=='')
{
	echo "<script type='text/javascript'>top.location.href='".$siteDomain."sysadmin/login.php';</script>";
	exit();
}
?>