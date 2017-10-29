<?php
/*
	class.Check.php
	author: �޳� 
	email:yemasky@msn.com
	��Ȩ���У���������ǼǺţ�2009SR06466
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
*/

class Check {
	public static function checkEmail($email)  {
		return preg_match("/^(.+?)@([a-z0-9\.]+)\.([a-z]){2,5}$/i", $email);
	}

	public static function checkUser($v, $name)  {
		if(preg_match("/$v/", $name)) {
			return false;
		}
	}
	
	public static function checkIp($v, $ip)  {
		if(preg_match("/$v/", $ip)) {
			return false;
		}
	}
	
	public static function checkUrlTpye($url, $arrType = NULL) {
		$arrType = $arrType == NULL ? 
				   array("jpg" => "jpg", "gif" => "gif", 
				   "png" => "png", "bmp" => "bmp", "php" => "php", 
				   "html" => "html", "htm" => "htm", 
				   "asp" => "asp", "jsp" => "jsp", "aspx" => "aspx") : $arrType;
		$arrUrlType = explode(".", $url);
		return $arrType[strtolower($arrUrlType[count($arrUrlType) - 1])];
	}
	
}
?>