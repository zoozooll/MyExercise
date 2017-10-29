<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1388 $
 */
define('IN_PHPB2B', TRUE);
define('PHPB2B_ROOT', substr(dirname(__FILE__), 0, -9));
define('MAGIC_QUOTES_GPC', function_exists("get_magic_quotes_gpc") && get_magic_quotes_gpc());
if (!defined('DIRECTORY_SEPARATOR')) {
	define('DIRECTORY_SEPARATOR','/');
}
define('DS', DIRECTORY_SEPARATOR);
require(PHPB2B_ROOT. 'configs'.DS.'config.inc.php');

/**
 * PHPB2B Debug Level
 * Myabe 0-5
 */
if(!isset($debug)) $debug = 0;
require(PHPB2B_ROOT. 'libraries'.DS.'global.func.php');
require(PHPB2B_ROOT. 'configs'.DS.'paths.php');
if (isset($_GET['app_lang']) && is_file(PHPB2B_ROOT.'languages'.DS.$_GET['app_lang'].DS."template.inc.php")) {
	$app_lang = $_GET['app_lang'];
	usetcookie("lang", $app_lang);
}
if (isset($_COOKIE[$cookiepre.'lang'])) {
	$app_lang = $_COOKIE[$cookiepre.'lang'];
}
if(!isset($app_lang) || !file_exists(PHPB2B_ROOT.'languages'.DS.$app_lang.DS."template.inc.php")) $app_lang = "zh-cn";
define('CACHE_PATH', PHPB2B_ROOT."data".DS."cache".DS.$app_lang.DS);
require(PHPB2B_ROOT.'languages'.DS.$app_lang.DS.'template.inc.php');
require(PHPB2B_ROOT.'languages'.DS.$app_lang.DS.'message.inc.php');
$msg = null;
if (!defined("LOCALE_PATH")) {
	define("LOCALE_PATH", APP_PATH.DS.'locale'.DS.$app_lang.DS);
}
$httpHost = pb_getenv('HTTP_HOST');
if(!defined('URL')) {
	if (!empty($absolute_uri)) {
		define('URL', $absolute_uri);	
	}else{
		$s = $uri = null;
		if (pb_getenv('HTTPS')) {
			$s ='s';
		}
		$uri = $_SERVER['REQUEST_URI']?$_SERVER['REQUEST_URI']:($_SERVER['PHP_SELF']?$_SERVER['PHP_SELF']:$_SERVER['SCRIPT_NAME']);
		define('URL', htmlspecialchars('http://'.$s.$_SERVER['HTTP_HOST'].substr($uri, 0, strrpos($uri, '/')+1)));	
	}
}
$time_start = getmicrotime();
$time_stamp = time();
$date_line = date("Y-m-d H:i:s", $time_stamp);
$includes = array(
	SOURCE_PATH. 'adodb'.DS.'adodb.inc.php',
	PHPB2B_ROOT. 'libraries'.DS.'smarty.pb.class.php',
	LIB_PATH. 'core/object.php',
	LIB_PATH. 'core/model.php',
	LIB_PATH. 'core/controller.php',
	LIB_PATH. 'core/view.php',
);
foreach ($includes as $inc) {
	if (!file_exists($inc)) {
		trigger_error(sprintf(L("file_not_exists", "msg", $inc)));
	}else{
		require($inc);
	}
}
$pdb = ADONewConnection($database);
$smarty = new MySmarty();
$connected = $pdb->PConnect($dbhost,$dbuser,$dbpasswd,$dbname);
if(!$connected or empty($connected)) {
	$msg = L("db_conn_error", 'msg', $pdb->ErrorMsg());
	$msg.= "<br />".L("db_conn_error_no", 'msg', $pdb->ErrorNo());
	if (!file_exists(DATA_PATH. "install.lock")) {
		$msg.="<br /><a href='install/install.php'>".L("please_reinstall_program", "msg")."</a>";
	}
	header_sent($msg);
	exit;
}
if($dbcharset && mysql_get_server_info() > '4.1') {
	$pdb->Execute("SET NAMES '{$dbcharset}'");
}
if (!file_exists(CACHE_PATH. "cache_setting.php")) {
	require_once(LIB_PATH. "cache.class.php");
	$cache = new Caches();
	if($cache->cacheAll()){
		$msg.="<a href='index.php'>".L("cached_and_refresh")."</a>";
		header_sent($msg);
		exit;
	}
}
$cachelost = (include CACHE_PATH. 'cache_setting.php') ? '' : 'settings';
$phpb2b_auth_key = md5($_PB_CACHE['setting']['auth_key'].pb_getenv('HTTP_USER_AGENT'));
$php_self = pb_getenv('PHP_SELF');
$base_script = basename($php_self);
list($basefilename) = explode('.', $base_script);
if($headercharset) {
    @header('Content-Type: text/html; charset='.$charset);
}
//timezone
$time_offset = isset($_PB_CACHE['setting']['time_offset'])?$_PB_CACHE['setting']['time_offset']:0;
$date_format = isset($_PB_CACHE['setting']['date_format'])?$_PB_CACHE['setting']['date_format']:"Y-m-d";
$time_now = array('time' => gmdate("{$date_format} H:i", $time_stamp + 3600 * $time_offset),
	'offset' => ($time_offset >= 0 ? ($time_offset == 0 ? '' : '+'.$time_offset) : $time_offset));

if(PHP_VERSION > '5.1') {
	@date_default_timezone_set('Etc/GMT'.($time_offset > 0 ? '-' : '+').(abs($time_offset)));
}else{
	@putenv("TZ=GMT".$time_now['offset']);
}
$viewhelper = new PbView();
$conditions = null;
$pb_userinfo = pb_get_member_info();
if ($pb_userinfo) {
	$pb_user = $pb_userinfo;
	$pb_user = pb_addslashes($pb_user);
	uaAssign($pb_userinfo);
}
uaAssign(array('SiteUrl'=>URL, 'Charset'=>$charset, 'AppLanguage'=>$app_lang));
uaAssign($_PB_CACHE['setting']);
$pre_length = strlen($cookiepre);
foreach($_COOKIE as $key => $val) {
	if(substr($key, 0, $pre_length) == $cookiepre) {
		$_UCOOKIE[(substr($key, $pre_length))] = MAGIC_QUOTES_GPC ? $val : pb_addslashes($val);
	}
}
$pre_refer = empty($_SERVER['HTTP_REFERER'])?'':$_SERVER['HTTP_REFERER'];
if($gzipcompress && function_exists('ob_gzhandler')) {
	ob_start('ob_gzhandler');
} else {
	$gzipcompress = 0;
	ob_start();
}
?>