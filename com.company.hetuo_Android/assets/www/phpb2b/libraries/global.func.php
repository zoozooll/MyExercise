<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1241 $
 */
function da($arr_str, $exit = false)
{
	echo "<pre>";
	print_r($arr_str);
	echo "</pre>";
	($exit)?exit:'';
}

function pb_getenv($key) {
	if ($key == 'HTTPS') {
		if (isset($_SERVER['HTTPS'])) {
			return (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off');
		}
		return (strpos(pb_getenv('SCRIPT_URI'), 'https://') === 0);
	}

	if ($key == 'SCRIPT_NAME') {
		if (pb_getenv('CGI_MODE') && isset($_ENV['SCRIPT_URL'])) {
			$key = 'SCRIPT_URL';
		}
	}

	$val = null;
	if (isset($_SERVER[$key])) {
		$val = $_SERVER[$key];
	} elseif (isset($_ENV[$key])) {
		$val = $_ENV[$key];
	} elseif (getenv($key) !== false) {
		$val = getenv($key);
	}

	if ($key === 'REMOTE_ADDR' && $val === pb_getenv('SERVER_ADDR')) {
		$addr = pb_getenv('HTTP_PC_REMOTE_ADDR');
		if ($addr !== null) {
			$val = $addr;
		}
	}

	if ($val !== null) {
		return $val;
	}

	switch ($key) {
		case 'SCRIPT_FILENAME':
			if (defined('SERVER_IIS') && SERVER_IIS === true) {
				return str_replace('\\\\', '\\', pb_getenv('PATH_TRANSLATED'));
			}
			break;
		case 'DOCUMENT_ROOT':
			$name = pb_getenv('SCRIPT_NAME');
			$filename = pb_getenv('SCRIPT_FILENAME');
			$offset = 0;
			if (!strpos($name, '.php')) {
				$offset = 4;
			}
			return substr($filename, 0, strlen($filename) - (strlen($name) + $offset));
			break;
		case 'PHP_SELF':
			return str_replace(pb_getenv('DOCUMENT_ROOT'), '', pb_getenv('SCRIPT_FILENAME'));
			break;
		case 'CGI_MODE':
			return (PHP_SAPI === 'cgi');
			break;
		case 'HTTP_BASE':
			$host = pb_getenv('HTTP_HOST');
			if (substr_count($host, '.') !== 1) {
				return preg_replace('/^([^.])*/i', null, pb_getenv('HTTP_HOST'));
			}
			return '.' . $host;
			break;
	}
	return null;
}

function pb_strcomp($str1,$str2)
{
	if (strcmp(trim($str1),trim($str2)) == 0) {
		return true;
	}else {
		return false;
	}
}

function pb_radom($len=6,$recycle=1){
	$str = 'ABCDEFGHJKMNPQRSTUVWXYabcdefghjkmnpqrstuvwxy';
	$str.= '123456789';
	$str = str_repeat($str,$recycle);
	return substr(str_shuffle($str),0,$len);
}

function setvar($name,$var)
{
	global $smarty;
	$smarty->assign($name,$var);
}

function uaAssign($names)
{
	global $smarty;
	if (is_array($names)) {
		foreach ($names as $smt_key=>$smt_val) {
			$smarty->assign($smt_key,$smt_val);
		}
	}
}

function pheader($string, $replace = true, $http_response_code = 0) {
	$string = str_replace(array("\r", "\n"), array('', ''), $string);
	if(empty($http_response_code) || PHP_VERSION < '4.3' ) {
		@header($string, $replace);
	} else {
		@header($string, $replace, $http_response_code);
	}
	if(preg_match('/^\s*location:/is', $string)) {
		exit();
	}
}

function flash($message_title = '', $back_url = '', $pause = 3, $extra = '')
{
	global $smarty;
	if (empty($back_url)) {
		if (defined('CURSCRIPT')) {	
			$back_url = CURSCRIPT. ".php";
		}elseif (isset($_SERVER['HTTP_REFERER'])){
			$back_url = $_SERVER['HTTP_REFERER'];
		}else{
			$back_url = "javascript:;";
		}
	}
	$return = $smarty->flash($message_title, $back_url, $pause, $extra);
}


function pb_create_folder($dir)
{
	return is_dir($dir) or (pb_create_folder(dirname($dir)) and mkdir($dir, 0777));
}

function pb_get_cache($models, $path = '')
{
	if (is_array($models)) {
		foreach ($models as $model) {
			$cache_file = $path?CACHE_PATH.$path."cache_".$model.".php":CACHE_PATH."cache_".$model.".php";
			if (file_exists($cache_file)) {
				include $cache_file;
			}
		}
	}else{
		$cache_file = $path?CACHE_PATH.$path."cache_".$models.".php":CACHE_PATH."cache_".$models.".php";
		if (file_exists($cache_file)) {
			include $cache_file;
		}
	}
}

function render($filename = null, $exit = false)
{
	global $smarty, $viewhelper, $theme_name, $time_start, $cache_id;
	$return = false;
	$htmlize = false;
	$allowed_file = array("index","post","industry","area");
	$allowed_params = array("id");
	$file_info = pathinfo(pb_getenv('PHP_SELF'));
	$tmp_themename = '';
	$smarty->assign('position', $viewhelper->getPosition());
	$smarty->assign('page_title', $viewhelper->getTitle());
	$tpl_file = $theme_name.DS.$filename.$smarty->tpl_ext;
	if (in_array($file_info['filename'], $allowed_file)) {
		$dir_name = str_replace(array("\\", "//"), "/", $file_info['dirname']);
		$dir_name = explode("/", $dir_name);
		$dir_name = array_filter($dir_name);
		$i_count = count($dir_name);
		switch ($i_count) {
			case 1:
				$dir_name = '';
				break;
			default:
				$sub_dir = strrchr($file_info['dirname'], "/")."/";
				$dir_name = $sub_dir;
				break;
		}
		$smarty->cache_dir = $smarty->cache_dir.$dir_name.DS;
		$smarty->cache_lifetime = 60*60;
		if (!is_dir($smarty->cache_dir)) {
			pb_create_folder($smarty->cache_dir);
		}
		if (!empty($cache_id)) {
			$cache_id = substr(md5($cache_id.$dir_name.$file_info['filename']), 0, 5);
		}else{
			$cache_id = substr(md5($dir_name.$file_info['filename']), 0, 5);
		}
	}else{
		$smarty->caching = false;
	}
	if ($theme_name=='blue' || !$smarty->template_exists($tpl_file)) {
		$tmp_themename = 'default';
		$tpl_file = 'default'.DS.$filename.$smarty->tpl_ext;
	}
	$smarty->assign('ThemeName', $tmp_themename?$tmp_themename:$theme_name);
	if (!empty($viewhelper->metaDescription)) {
		$smarty->assign("metadescription", $viewhelper->metaDescription);		
	}
	if (!empty($viewhelper->metaKeyword)) {
		$smarty->assign("metakeywords", $viewhelper->metaKeyword);
	}elseif (!empty($viewhelper->metaDescription)){
		$viewhelper->setMetaKeyword($viewhelper->metaDescription);
		$smarty->assign("metakeywords", $viewhelper->metaKeyword);
	}
	$return = $smarty->display($tpl_file, $cache_id);
	if ($exit) {
		exit;
	}
	if (!$htmlize) {
		return $return;
	}
	uses('htmlcache');
	$htmlcache = new Htmlcache();
	$htmls_path = PHPB2B_ROOT.$htmlcache->archiver_dir.DS;
	$htmls_path.=$dir_name;
	if (!in_array($file_info['filename'], $allowed_file)) {
		return;
	}
	switch ($file_info['filename']) {
		case "detail":
			$htmls_path.="detail".DS;
			$htmls_path.=date("Ymd").DS;
			$file_name = $_GET['id'].$htmlcache->file_ext;
			break;
		default:
			$file_name = $file_info['filename'].$htmlcache->file_ext;
			break;
	}
	$htmlcache->setTargetPath($htmls_path);
	$htmlcache->write($file_name);
	return $return;
}

function template($filename = null, $exit = false)
{
	global $smarty;
	$return = false;
	$return = $smarty->display($filename.$smarty->tpl_ext);
	if ($exit) {
		exit;
	}
	return $return;
}

function pb_check_email($email){
	$return = false;
	if(strstr($email, '@') && strstr($email, '.')){
		if(eregi("^([_a-z0-9]+([\._a-z0-9-]+)*)@([a-z0-9]{2,}(\.[a-z0-9-]{2,})*\.[a-z]{2,3})$", $email)){
			$return = true;
		}
	}
	return $return;
}

function usetcookie($var, $value, $life_time = 0, $prefix = 1) {
	global $cookiepre, $cookiepath, $time_stamp, $cookiedomain;
	return setcookie(($prefix ? $cookiepre : '').$var, $value,
	$life_time ? $time_stamp + $life_time : 0, $cookiepath,
	$cookiedomain, $_SERVER['SERVER_PORT'] == 443 ? 1 : 0);
}

function uclearcookies() {
	return usetcookie('auth', '', -86400 * 365);
}

function fileext($filename) {
	return substr(($t=strrchr($filename,'.'))!==false?".".$t:'',1);
}

function pb_htmlspecialchar($string) {
	if(is_array($string)) {
		foreach($string as $key => $val) {
			$string[$key] = pb_htmlspecialchar($val);
		}
	} else {
		$string = preg_replace('/&amp;((#(\d{3,5}|x[a-fA-F0-9]{4})|[a-zA-Z][a-z0-9]{2,5});)/', '&\\1',
		str_replace(array('&', '"', '<', '>'), array('&amp;', '&quot;', '&lt;', '&gt;'), $string));
	}
	return $string;
}

function pb_get_client_ip($type = "long")
{
	if(getenv('HTTP_CLIENT_IP') && strcasecmp(getenv('HTTP_CLIENT_IP'), 'unknown')) {
		$onlineip = getenv('HTTP_CLIENT_IP');
	} elseif(getenv('HTTP_X_FORWARDED_FOR') && strcasecmp(getenv('HTTP_X_FORWARDED_FOR'), 'unknown')) {
		$onlineip = getenv('HTTP_X_FORWARDED_FOR');
	} elseif(getenv('REMOTE_ADDR') && strcasecmp(getenv('REMOTE_ADDR'), 'unknown')) {
		$onlineip = getenv('REMOTE_ADDR');
	} elseif(isset($_SERVER['REMOTE_ADDR']) && $_SERVER['REMOTE_ADDR'] && strcasecmp($_SERVER['REMOTE_ADDR'], 'unknown')) {
		$onlineip = $_SERVER['REMOTE_ADDR'];
	}
	preg_match("/[\d\.]{7,15}/", $onlineip, $onlineipmatches);
	$onlineip = $onlineipmatches[0] ? $onlineipmatches[0] : 'unknown';
	if($onlineip=='unknown') return $onlineip;
	if($type=="long"){
		return pb_ip2long($onlineip);
	}else{
		return $onlineip;
	}
}

function pb_ip2long($ip)
{
	return sprintf("%u",ip2long($ip));
}

function pb_addslashes($string) {
	if(is_array($string)) {
		foreach($string as $key => $val) {
			$string[$key] = pb_addslashes($val);
		}
	} else {
		$string = addslashes($string);
	}
	return $string;
}

function stripslashes_deep($value)
{
    if(isset($value)) {
        $value = is_array($value) ?
            array_map('stripslashes_deep', $value) :
            stripslashes($value);
    }
    return $value;
}

if (!function_exists('getmicrotime')) {
	function getmicrotime() {
		list($usec, $sec) = explode(' ', microtime());
		return ((float)$usec + (float)$sec);
	}
}

function pb_get_host($http = true)
{
	if ( isset( $_SERVER['HTTPS'] ) && ( strtolower( $_SERVER['HTTPS'] ) != 'off' ) ) {
		$ul_protocol = 'https';
	}else{
		$ul_protocol = 'http';
	}
	if($http) {
		$return = $ul_protocol."://".$_SERVER['HTTP_HOST'];
	} else {
		$return = $_SERVER['HTTP_HOST'];
	}
	return $return;
}

function uses() {
	$args = func_get_args();
	foreach($args as $arg) {
		$class_name = strtolower($arg);
		require(LIB_PATH . "core/controllers/".$class_name. '_controller.php');
		require(LIB_PATH . "core/models/".$class_name. '.php');
	}
}

function pb_strip_spaces($string)
{
	$str = preg_replace('#\s+#', ' ', trim($string));
	return $str;
}

function pb_get_member_info()
{
	global $cookiepre;
	$user = array();
	if (!empty($_COOKIE[$cookiepre."auth"])) {
		list($user['pb_userid'], $user['pb_username'], $user['pb_userpasswd'], $user['is_admin']) = explode("\t", authcode($_COOKIE[$cookiepre."auth"], 'DECODE'));
		return $user;
	}else{
		return false;
	}
}

function authcode($string, $operation = "ENCODE", $key = '', $expire = 0) {
	global $phpb2b_auth_key;
	$ckey_length = 4;
	$key = md5($key ? $key : $phpb2b_auth_key);
	$keya = md5(substr($key, 0, 16));
	$keyb = md5(substr($key, 16, 16));
	$keyc = $ckey_length ? ($operation == 'DECODE' ? substr($string, 0, $ckey_length): substr(md5(microtime()), -$ckey_length)) : '';

	$cryptkey = $keya.md5($keya.$keyc);
	$key_length = strlen($cryptkey);

	$string = $operation == 'DECODE' ? base64_decode(substr($string, $ckey_length)) : sprintf('%010d', $expire ? $expire + time() : 0).substr(md5($string.$keyb), 0, 16).$string;
	$string_length = strlen($string);

	$result = '';
	$box = range(0, 255);

	$rndkey = array();
	for($i = 0; $i <= 255; $i++) {
		$rndkey[$i] = ord($cryptkey[$i % $key_length]);
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

	if($operation == 'DECODE') {
		if((substr($result, 0, 10) == 0 || substr($result, 0, 10) - time() > 0) && substr($result, 10, 16) == substr(md5(substr($result, 26).$keyb), 0, 16)) {
			return substr($result, 26);
		} else {
			return '';
		}
	} else {
		return $keyc.str_replace('=', '', base64_encode($result));
	}
}

function __L() {
	global $arrMessage, $arrTemplate;
	$args = func_get_args();
	$return = ($args[1] == "msg")?$arrMessage['_'.$args[0]]:$arrTemplate['_'.$args[0]];
	if (!empty($args[2])) {
		$return = preg_replace(array("/{[0-9]+}/i", "/%[a-zA-Z]/"), $args[2], $return);
	}
	return (!empty($return))?$return:$args[0];
}
function L($key, $type = "msg", $extra = ''){
	return call_user_func_array("__L", array($key, $type, $extra));
}

if (!function_exists('file_get_contents')) {
	function file_get_contents($filename, $incpath = false, $resource_context = null)
	{
		if (false === $fh = fopen($filename, 'rb', $incpath)) {
			trigger_error('file_get_contents() failed to open stream: No such file or directory', E_USER_WARNING);
			return false;
		}
		clearstatcache();
		if ($fsize = @filesize($filename)) {
			$data = fread($fh, $fsize);
		} else {
			$data = '';
			while (!feof($fh)) {
				$data .= fread($fh, 8192);
			}
		}

		fclose($fh);
		return $data;
	}
}

if (!function_exists('file_put_contents')) {
	function file_put_contents($filename, $data) {
		$f = @fopen($filename, 'w');
		if (!$f) {
			return false;
		} else {
			$bytes = fwrite($f, $data);
			fclose($f);
			return $bytes;
		}
	}
}

if (!function_exists('http_build_query')) {
	function http_build_query($data, $prefix = null, $argSep = null, $baseKey = null) {
		if (empty($argSep)) {
			$argSep = ini_get('arg_separator.output');
		}
		if (is_object($data)) {
			$data = get_object_vars($data);
		}
		$out = array();

		foreach ((array)$data as $key => $v) {
			if (is_numeric($key) && !empty($prefix)) {
				$key = $prefix . $key;
			}
			$key = urlencode($key);

			if (!empty($baseKey)) {
				$key = $baseKey . '[' . $key . ']';
			}

			if (is_array($v) || is_object($v)) {
				$out[] = http_build_query($v, $prefix, $argSep, $key);
			} else {
				$out[] = $key . '=' . urlencode($v);
			}
		}
		return implode($argSep, $out);
	}
}

function formhash() {
	global $time_stamp, $pb_userinfo, $smarty, $phpb2b_auth_key;
	if(!defined("FORMHASH")) {
		$hashadd = defined('IN_PBADMIN') ? 'Only For PHPB2B Admin' : '';
		if($pb_userinfo){
			$formhash = substr(md5(substr($time_stamp, 0, -7).'|'.$pb_userinfo['pb_userid'].'|'.$phpb2b_auth_key.'|'.$hashadd), 8, 8);
		}else{
			$formhash = substr(md5(substr($time_stamp, 0, -7).'|'.$phpb2b_auth_key.'|'.$hashadd), 8, 8);
		}
		define("FORMHASH", $formhash);
		$smarty->assign("formhash", $formhash);
	}
	return FORMHASH;
}

function pb_submit_check($var) {
	global $_POST;
	$referer = pb_getenv('HTTP_REFERER');
	if(!empty($_POST[$var]) && $_SERVER['REQUEST_METHOD'] == 'POST') {
		if((empty($referer) || preg_replace("/https?:\/\/([^\:\/]+).*/i", "\\1", $referer) == preg_replace("/([^\:]+).*/", "\\1", $_SERVER['HTTP_HOST'])) && $_POST['formhash'] == formhash()) {
			return true;
		} else {
			die(L("invalid_submit"));
		}
	} else {
		return false;
	}
}

function parse_highlight($highlight) {
	if($highlight) {
		$colorarray = array('#000000', 'red', 'orange', 'yellow', 'green', 'cyan', 'blue', 'purple', 'gray');
		$string = sprintf('%02d', $highlight);
		$stylestr = sprintf('%03b', $string[0]);
		$style = ' style="';
		$style .= $stylestr[0] ? 'font-weight: bold;' : '';
		$style .= $stylestr[1] ? 'font-style: italic;' : '';
		$style .= $stylestr[2] ? 'text-decoration: underline;' : '';
		$style .= $string[1] ? 'color: '.$colorarray[$string[1]] : '';
		$style .= '"';
	} else {
		$style = '';
	}
	return $style;
}

function pb_get_attachmenturl($src, $path = '', $scope = '', $force = false)
{
	global $attachment_dir, $attachment_url;
	$default_thumb_img = 'images/nopicture_small.gif';
	if (!empty($scope)) {
		$default_thumb_img = 'images/nopicture_'.$scope.'.gif';
	}
	if ($force) {
		$default_thumb_img = 'images/nopicture_'.$force.'.gif';
	}
	$img =  $src ? $attachment_url.$src : $default_thumb_img;
	if ($scope && ($img!=$default_thumb_img)) {
		$img.=".{$scope}.jpg";
	}
	return $path.$img;
}


function capt_check($capt_name)
{
	global $_POST, $_PB_CACHE, $smarty;
	$capt_require = array(
	"capt_logging",
	"capt_register",
	"capt_post_free",
	"capt_add_market",
	"capt_login_admin",
	"capt_apply_friendlink",
	"capt_service"
	);
	if (in_array($capt_name, $capt_require)) {
		$t = decbin($_PB_CACHE['setting']['capt_auth']);
		$capt_auth = sprintf("%07d", $t);
		$key = array_search($capt_name, $capt_require);
		if($capt_auth[$key]){
			if (!empty($_POST['data'])) {
				include(LIB_PATH. "captcha/securimage.php");
				$img = new Securimage();
				$post_code = trim($_POST['data'][$capt_name]);
				if(!$img->check($post_code)){
					flash('invalid_capt', null, 0);
				}
			}
			$smarty->assign("ifcapt", true);
		}else{
			$smarty->assign("ifcapt", false);
		}
	}
}


function am() {
	$r = array();
	$args = func_get_args();
	foreach ($args as $a) {
		if (!is_array($a)) {
			$a = array($a);
		}
		$r = array_merge($r, $a);
	}
	return $r;
}

function header_sent($msg)
{
	global $charset;
	echo '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset='.$charset.'" />
</head>
<body>
<div style="font:normal normal normal 12px/1.2em Arial Black;">
'.$msg.'
</div>
</body>
</html>';
}

if (!function_exists("array_combine")) {
	function array_combine($arr1, $arr2) {
		$out = array();

		$arr1 = array_values($arr1);
		$arr2 = array_values($arr2);

		foreach($arr1 as $key1 => $value1) {
			$out[(string)$value1] = $arr2[$key1];
		}

		return $out;
	}
}

function check_proxy(){
	$v = getenv("HTTP_VIA");
	$f = getenv("HTTP_X_FORWARDED_FOR");
	$c = getenv("HTTP_XROXY_CONNECTION");
	$o = getenv("HTTP_PRAGMA");
	if ( ($v=="")&&($f=="")&&($c=="")&&($o=="") ) return false;
	return true;
}
?>