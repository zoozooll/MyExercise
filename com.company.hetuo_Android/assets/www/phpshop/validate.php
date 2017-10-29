<?php 
/*
	validate.php 
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/
require_once("etc/define.php");

$authnum = strtolower(Valiimage::validateCode(4));
$objCookie = new Cookie;
$objCookie -> cookietime = getHis();
$objCookie -> cookievalidate = $authnum;
$site_font_path = __ROOT_PATH . "font/font.ttf";
Valiimage::generateValidationImage($authnum, 90, 35, 5, 8);
?>
