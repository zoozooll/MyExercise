<?php
/*
	func.Common.php
	author: 罗驰 
	email:yemasky@msn.com
	版权所有，国家软件登记号：2009SR06466
	任何媒体、网站或个人未经本人协议授权不得修改本程序
*/
require_once("func.php");
if(!defined("INC_FUNC_COMMON")) {
 	define("INC_FUNC_COMMON", "1");
	
define("__MODEL_EMPTY", "");
define("__MODEL_EXCEPTION", "Exception");
date_default_timezone_set("Asia/Chongqing");
function __autoload($class) {
	$len = strlen($class)-1;
	for($loop = $len; $loop>=0; $loop--) {
		if($class[$loop] >= 'A' && $class[$loop] <= 'Z') {
			break;
		}
	}
	$classpath = __ROOT_PATH . "lib/classes/"; 
	switch(substr($class, $loop)) {
		case "Object":
			include_once($classpath . "object/class." . $class . ".php");
		break;
		case "Action":
			include_once($classpath . "action/class." . $class . ".php");
		break;
		case "Dao":
			include_once($classpath . "dao/class." . $class . ".php");
		break;
		case "Tool":
			include_once($classpath . "Tool/class." . $class . ".php");
		break;
		default:
			include_once($classpath . "util/class." . $class . ".php");
		break;
	}
}

function getDateTime($d = 8) {
	return date("Y-m-d H:i:s", strtotime("$d HOUR"));//GMT+8
}

function getHis() {
	return date("His");
}

function getDateTimeId($l = 6, $d = 8) {
	$time = date("YmdHis", strtotime("+$d HOUR"));//GMT+8
	$time .= trim(substr(microtime(), 2, $l));
	return $time;
}

function logError($message, $model="", $level="ERROR") {
	writeLog("#error.log", $message);
}

function logDebug($message, $model="", $level="DEBUG") {
	writeLog("#debug.log", $message);
}

function writeLog($filename, $msg) {
	$fp = fopen(__ROOT_LOGS_PATH.$filename, "a+");
	$msg = getDateTime() . " >>> " . $_SERVER['REQUEST_URI'] . ' >> ' . $msg;
	fwrite($fp, "$msg\r\n");
	fclose($fp);
}

function redirect($url, $status = '302', $time = 0) {
	if(is_numeric($url)) {
		header("Content-type: text/html; charset=".__CHARSET);
		echo "<script>history.go('$url')</script>";
		flush();
	} else {
		if(headers_sent()) {
			echo "<meta http-equiv=refresh content=\"$time; url=$url\">"; 
			echo "<script type='text/javascript'>location.href='$url';</script>";
		} else {
			if($status == '302') {
				header("HTTP/1.1 302 Moved Temporarily");
				header("Location: $url");
				exit;
			}
			header("Cache-Control: no-cache, must-revalidate"); 
			header("Expires: Mon, 26 Jul 1997 05:00:00 GMT"); 
			header("HTTP/1.1 301 Moved Permanently");
			header("Location: $url");
		}
	}
	exit;
}

function checkNum($id) {
	if(!is_numeric($id)) {
		throw new Exception("parameter error: no the num -> ".$id);
	}
}

function keyED($string, $key) {
	$key_length = strlen($key);
	$string_length = strlen($string);

	$rndkey = $box = array();
	$result = '';

	for($i = 0; $i <= 255; $i++) {
		$rndkey[$i] = ord($key[$i % $key_length]);
		$box[$i] = $i;
	}

	for($j = $i = 0; $i < 256; $i++) {
		$j = ($j + $box[$i] + $rndkey[$i]) % 256;
		$tmp = $box[$i];
		$box[$i] = $box[$j];
		$box[$j] = $tmp;
	}

	for($a = $j = $i = 0; $i < $string_length; $i++) {
		$a = ($a + 1) % 256;
		$j = ($j + $box[$a]) % 256;
		$tmp = $box[$a];
		$box[$a] = $box[$j];
		$box[$j] = $tmp;
		$result .= chr(ord($string[$i]) ^ ($box[($box[$a] + $box[$j]) % 256]));
	}
	return $result;
}

function encode($string, $key = __KEY) {
	$key = md5($key);
	$string = substr(md5($string.$key), 0, 8).$string;
	return str_replace('+', ' ', base64_encode(keyED($string, $key)));
}

function decode($string, $key = __KEY) {
	$key = md5($key);
	$result = keyED(base64_decode(str_replace(' ', '+', $string)), $key);
	if(substr($result, 0, 8) == substr(md5(substr($result, 8).$key), 0, 8)) {
		return substr($result, 8);
	} else {
		return NULL;
	}
}

function errorLog($msg) {
	logError("[err]$msg;request ip:".onLineIp().";url:".getUrl().";ReferUrl:".getReferUrl().";time:".Common::getCommonTime());
	//throw new Exception("[err]$msg;request ip:".onLineIp().";url:".getUrl().";ReferUrl:".getReferUrl().";time:".getDateTime());
}
function getMicrotime(){ 
	list($usec, $sec) = explode(" ",microtime()); 
	return ((float)$usec + (float)$sec); 
} 

function onLineIp() {
	if(isset($_SERVER['HTTP_CLIENT_IP'])) {
		 $onlineip = $_SERVER['HTTP_CLIENT_IP'];
	} elseif(isset($_SERVER['HTTP_X_FORWARDED_FOR'])) {
		 $onlineip = $_SERVER['HTTP_X_FORWARDED_FOR'];
	} else {
		 $onlineip = $_SERVER['REMOTE_ADDR'];
	}
	return $onlineip;
}
function getHost() {
	return $_SERVER['HTTP_HOST'];   
}
function getUrl() {
	if($_SERVER["SERVER_PORT"] == 80) {
		return 'http://'.$_SERVER['HTTP_HOST'].$_SERVER['REQUEST_URI'];   
	}
	return 'http://'.$_SERVER['HTTP_HOST'].":".$_SERVER["SERVER_PORT"].$_SERVER['REQUEST_URI'];   
}
function getReferUrl() {
	if(($url = $_SERVER['HTTP_REFERER']) == NULL) {
		return '/';
	}
	return $url;
}

function conutf8($v, $e = 'GBK', $c = 'UTF-8') {
	return iconv($e, $c, $v);
}
function alert($mess, $go = -1) {
	if(!headers_sent()) {
		header("Content-type: text/html; charset=".__CHARSET);
	}
	$script = $go == 0 ? "" : "history.go(".$go.");";
	echo "<script language='javascript'>alert('".$mess."');$script</script>";
	flush();
	if(!empty($script)) {exit;}
}	
}
?>
