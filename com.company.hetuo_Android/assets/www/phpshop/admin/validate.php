<?php 
/*
	validate.php 
	author: �޳� 
	email:yemasky@msn.com��
	��Ȩ���У���������ǼǺţ�2009SR06466 ����
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
*/
require_once("../etc/define.php");

$authnum = strtolower(Valiimage::validateCode(4));
$objSession = new Session;
$objSession -> glo_sessiontime = getHis();
$objSession -> sessionvalidate = $authnum;
$site_font_path = __ROOT_PATH . "font/font.ttf";
Valiimage::generateValidationImage($authnum, 80, 32, 5, 8);
?>
