<?php 
/*
	chenklogin.php 
	author: 罗驰  *
	email:yemasky@msn.com %
	版权所有，国家软件登记号：2009SR06466%
	任何媒体、网站或个人未经本人协议授权不得修改本程序%
*/
require_once("../etc/define.php");
if(!defined('__WEB') or !defined('__CACHE')) {
	alert('not define web and cache.');
	redirect("login.php"); 
}
$objAction = new AdminAction();

$objAction -> execute('checkloin');

?>
