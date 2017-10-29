<?php 
/*
	class.Cache.php
	author: �޳� 
	email:yemasky@msn.com
	��Ȩ���У���������ǼǺţ�2009SR06466
	�κ�ý�塢��վ�����δ������Э����Ȩ�����޸ı�����
*/
 
 class Cache {
 
 	public static function fetch($cacheID, $display = false, $filepath = __CACHE) {
 		$_contents = File::readFile($cacheID, $filepath);
 		if ($display) {
 			echo unserialize(base64_decode($_contents));
			return;
 		}
 		return unserialize(base64_decode($_contents));
 	}
 	
 	public static function isValid($cacheID, $cacheTime, $filepath = __CACHE) {
 		$_cacheFile = $filepath . $cacheID;
		if (!is_writable($_cacheFile)) {
			return false;
 		}
		clearstatcache(); //clearn filemtime function cache
		$now = time();
		$fileMTime = filemtime($_cacheFile);
		return ($now - $fileMTime) < $cacheTime; 
 	}
 	
 	public static function cachePage($cacheID, $contents, $filepath = __CACHE) {
		$contents = base64_encode(serialize($contents));
		File::creatFile($cacheID, $contents, $filepath);
		// Win32 can't rename over top another file
		if (strtoupper(substr(PHP_OS, 0, 3)) == 'WIN' 
		    && file_exists ($filepath.$cacheID)) {
		} 
        @chmod ($filepath.$cacheID, 0755);
        return true;
 	}
	
	public static function getCacheTime($cacheID, $filepath = __CACHE) {
		return filemtime($filepath . $cacheID);
	}
	
 }
 ?>
