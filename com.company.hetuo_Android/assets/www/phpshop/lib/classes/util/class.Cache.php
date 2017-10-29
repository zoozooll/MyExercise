<?php 
/*
	class.Cache.php
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
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
