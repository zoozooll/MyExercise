<?php
/*
	class.Service.php
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/

class Service {

	public static function setEtag($pname) {
		header("Etag: ".$pname); 
	} 
	
	public static function getEtag() {
		return $_SERVER['HTTP_IF_NONE_MATCH'];
	} 

	public static function sendEtag($pname) {
		header("Etag: ".$pname, true, 304); 
        exit;
	}  
	
    public static function etag($pname) {
		if(isset($_SERVER['HTTP_IF_NONE_MATCH'])) {
			if($_SERVER['HTTP_IF_NONE_MATCH'] == $pname) {
				 header("Etag: ".$pname, true, 304); 
				 exit;
			} 
		}
        header("Etag: ".$pname); 
    } 
	
	public static function flushClient() {
		flush();
	}

}
?>