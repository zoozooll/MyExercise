<?php
/*
	class.Service.php
	author: �޳� 
	email:yemasky@msn.com
	��Ȩ���У���������ǼǺţ�2009SR06466
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
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