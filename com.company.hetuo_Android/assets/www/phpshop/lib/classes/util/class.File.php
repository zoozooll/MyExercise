<?php
/*
	class.Flie.php
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/
class File {
	public static function deleteFile($file, $pathfile = __CACHE) {
		if(!file_exists($pathfile.$file)) {
			return true;
		}
		if(!unlink($pathfile.$file)) {
			throw new Exception(".error: can't delete file:".$pathfile.$file);
		}
	}
	
	public static function moveFile($srcFile, $dstFile, $mode = 0777, $destExistIngore=false) {
		if(file_exists($dstFile)) {
			if($destExistIngore == false) {
				return false;
			}
			self::deleteFile($dstFile);
		}
		if(!copy($srcFile, $dstFile)) {
			throw new Exception(".error: can't copy file:$srcFile to $dstFile.");
		}
		chmod($dstFile, $mode);
		return self::deleteFile($srcFile);
	}
	
	public static function createDir($dir = __CACHE) {
		if(is_dir($dir)) {
			return true;	
		}
		if(!mkdir($dir)) {
			throw new Exception(".error: can't create dir $dir.");
		}
	}
	
	public static function creatFile($filename, $contant, $dir = __CACHE) {
		if(!is_dir($dir)){
			self::createDir($dir);
		}
		$filename = $dir.$filename;
		if (!($fp = fopen($filename, 'wb'))) {
			throw new Exception(".error: can't write $filename.");
 		}
		flock($fp, 2);
		fwrite($fp, stripslashes(str_replace("\x0d\x0a", "\x0a", addslashes($contant))));
		fclose($fp);
	}
	
	public static function readFile($filename, $dir = __CACHE) {
		$filename = $dir.$filename;
		if (!($fp = fopen($filename, 'rb'))) {
			throw new Exception(".error: can't open file:$filename");
		}
		flock($fp, LOCK_SH);
		if(!($content = fread($fp, filesize($filename)))) {
			throw new Exception(".error: can't read file:$filename");
		}
		fclose($fp);
		return $content;
	}
	public static function readContents($filename, $dir = __CACHE) {
		if(!($content = file_get_contents($dir.$filename))) {
			throw new Exception(".error: can't get contents file:$filename");
		}
		return $content;
	}
	public static function isFile($filename, $dir = __CACHE) {
		return file_exists($dir.$filename);
	}

	public static function getFileCreatedTime($filename, $dir = __CACHE) {
		if(self::isFile($filename, $dir)) {
			return filemtime($dir.$filename);
		}
		return 0;
	}
	public static function getDir($dir) {
		$arrDir = array();
		if ($handle = opendir($dir)) {
			while (false !== ($file = readdir($handle))) {
				if ($file != "." && $file != ".." && !is_file($dir . $file)) {
					$arrDir[$file] = $file;
				}
			}
			closedir($handle);
		}
		return $arrDir;
	}
	public static function getAllFile($dir) {
		$arrFile = array();
		if ($handle = opendir($dir)) {
			while (false !== ($file = readdir($handle))) {
				if ($file != "." && $file != ".." && is_file($dir . $file)) {
					$arrFile[$file] = $file;
				}
			}
			closedir($handle);
		}
		return $arrFile;
	}
}
?>