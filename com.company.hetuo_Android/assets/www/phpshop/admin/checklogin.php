<?php 
/*
	chenklogin.php 
	author: �޳�  *
	email:yemasky@msn.com %
	��Ȩ���У���������ǼǺţ�2009SR06466%
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����%
*/
require_once("../etc/define.php");
if(!defined('__WEB') or !defined('__CACHE')) {
	alert('not define web and cache.');
	redirect("login.php"); 
}
$objAction = new AdminAction();

$objAction -> execute('checkloin');

?>
