<?php 
/*
	class.Session.php
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/
class Session {

	public function __construct() {
		$this -> sesstionStar();
	}

    private function sesstionStar() {
		if(!session_id()) {
			session_start();
		}
    	if(function_exists('session_cache_limiter')) {
    	    session_cache_limiter("private, must-revalidate");
        }	
    }

	public function __set($name, $value) {
	    $_SESSION[md5(__WEB.$name)] = encode($value);
	}
	
	public function __get($name) {
		if(isset($_SESSION[md5(__WEB.$name)])) {
			return decode($_SESSION[md5(__WEB.$name)]);
		} 
		return NULL;
	}
	
	public function __unset($name) {
		unset($_SESSION[md5(__WEB.$name)]);
	}
		
	public function clean() {
		session_unset();
		foreach ($_SESSION as $key=>$value) {
			unset ($_SESSION[$key]);
		}
	}
	
}
?>