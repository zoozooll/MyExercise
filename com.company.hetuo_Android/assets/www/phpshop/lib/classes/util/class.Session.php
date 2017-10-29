<?php 
/*
	class.Session.php
	author: �޳� 
	email:yemasky@msn.com
	��Ȩ���У���������ǼǺţ�2009SR06466
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
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