<?php 
/*
	validate.php 
	author: �޳� 
	email:yemasky@msn.com
	��Ȩ���У���������ǼǺţ�2009SR06466
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
*/
require_once("etc/define.php");

$authnum = strtolower(Valiimage::validateCode(4));
$objCookie = new Cookie;
$objCookie -> cookietime = getHis();
$objCookie -> cookievalidate = $authnum;
$site_font_path = __ROOT_PATH . "font/font.ttf";
Valiimage::generateValidationImage($authnum, 90, 35, 5, 8);
?>
