<?php 
/*
	class.Cookie.php
	author: �޳� 
	email:yemasky@msn.com
	��Ȩ���У���������ǼǺţ�2009SR06466
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
*/
class Cookie {
	public function __construct() {
	}
	public static function setCookie($name, $value = NULL, $time = NULL, $path = "", $domain = "") {
		if($time != NULL) {
			$time = time() + $time;
		}
		setcookie(md5(__WEB.$name), encode($value), $time, $path, $domain); 
	}
	
	public function __set($name, $value) {
		setcookie(md5(__WEB.$name), encode($value)); 
    }
	
	public function __get($name) {
		if(isset($_COOKIE[md5(__WEB.$name)])) {
			return decode($_COOKIE[md5(__WEB.$name)]);
		}
    }

	public function __isset($name) {
		return isset($_COOKIE[$name]);
    }
	
	public function __unset($name) {
		setcookie(md5(__WEB.$name), '', time() - 3600); 
	}
	
	public function delSimpleCookie($name) {
		setcookie($name, '', time() - 3600); 
	}
	
	public function setSimpleCookie($name, $value = NULL, $time = NULL, $path = "", $domain = "") {
	    if(empty($name)) {
			return false;
		}
		if($time != NULL) {
			$time = time() + $time;
		}
		setcookie($name, $value, $time, $path, $domain); 
    }
	
	public function getSimpleCookie($name) {
		if(isset($_COOKIE[$name])) {
	    	return $_COOKIE[$name];
		}
    }
}
?>