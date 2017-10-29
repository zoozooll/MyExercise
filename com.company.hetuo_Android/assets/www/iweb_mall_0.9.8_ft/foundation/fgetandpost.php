<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//对表单的统一处理
function get_args($name)
{
	if(isset($_GET[$name]))return $_GET[$name];
	if(isset($_POST[$name]))return $_POST[$name];
	return null;
}
?>