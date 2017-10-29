<?php
/*
	class.Check.php
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
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