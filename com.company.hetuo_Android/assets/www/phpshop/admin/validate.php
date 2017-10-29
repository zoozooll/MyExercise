<?php 
/*
	validate.php 
	author: 罗驰 
	email:yemasky@msn.com￥
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
*/
require_once("../etc/define.php");

$authnum = strtolower(Valiimage::validateCode(4));
$objSession = new Session;
$objSession -> glo_sessiontime = getHis();
$objSession -> sessionvalidate = $authnum;
$site_font_path = __ROOT_PATH . "font/font.ttf";
Valiimage::generateValidationImage($authnum, 80, 32, 5, 8);
?>
