<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1387 $
 */
session_start();
error_reporting(E_ALL & ~E_NOTICE);
@set_time_limit(1000);
@set_magic_quotes_runtime(0);
@ini_set('magic_quotes_sybase', 0);
if (isset($_GET['act'])) {
	if($_GET['act'] == "phpinfo"){
		die(phpinfo());
	}
}
define('TIME', time());
define('MAGIC_QUOTES_GPC', get_magic_quotes_gpc());
define('PHPB2B_ROOT', substr(dirname(__FILE__), 0, -7));
define('JSMIN_AS_LIB', true); // prevents auto-run on include
require '../data/phpb2b_version.php';
require '../configs/config.inc.php';
define('IN_PHPB2B',true);
if (!defined('DIRECTORY_SEPARATOR')) {
	define('DIRECTORY_SEPARATOR','/');
}
define('DS', DIRECTORY_SEPARATOR);
if(!defined('LIB_PATH')) define('LIB_PATH',PHPB2B_ROOT.'libraries'.DS);
require '../libraries/global.func.php';
require '../libraries/func.sql.php';
require "../libraries/db_mysql.inc.php";
require "../libraries/json_config.php";
require "../libraries/core/object.php";
require "../libraries/file.class.php";
$app_lang = 'zh-cn';
if (isset($_GET['app_lang'])) {
	$_SESSION['lang'] = $_COOKIE['lang'] = $app_lang = $_GET['app_lang'];
}
if (isset($_SESSION['lang'])) {
	$app_lang = $_SESSION['lang'];
}
if (!defined('CACHE_PATH')) {
	define('CACHE_PATH', PHPB2B_ROOT."data".DS."cache".DS.$app_lang.DS);
}
require "../languages/".$app_lang."/template.install.inc.php";
extract($arrTemplate);
$db = new DB_Sql();
$file_cls = new Files();
$pb_protocol = 'http';
if ( isset( $_SERVER['HTTPS'] ) && ( strtolower( $_SERVER['HTTPS'] ) != 'off' ) ) {
	$pb_protocol = 'https';
}
$PHP_SELF = isset($_SERVER['SCRIPT_NAME']) ? $_SERVER['SCRIPT_NAME'] : preg_replace("/(.*)\.php(.*)/i", "\\1.php", $_SERVER['PHP_SELF']);
$BASESCRIPT = basename($PHP_SELF);
list($BASEFILENAME) = explode('.', $BASESCRIPT);
$install_url = htmlspecialchars($pb_protocol."://".$_SERVER['HTTP_HOST'].preg_replace("/\/+(api|wap)?\/*$/i", '', substr($PHP_SELF, 0, strrpos($PHP_SELF, '/'))).'/');
$siteUrl = substr($install_url,0,-(strlen($BASEFILENAME)+1));
$time_stamp = TIME;
if($_REQUEST)
{
	if(!MAGIC_QUOTES_GPC)
	{
		$_REQUEST = pb_addslashes($_REQUEST);
		if($_COOKIE) $_COOKIE = pb_addslashes($_COOKIE);
	}
	extract($_REQUEST, EXTR_SKIP);
}
if(!isset($_GET['step'])) {
	$step = '1';
}else{
	$step = intval($_GET['step']);
}
if (isset($_GET['do'])) {
	$do = trim($_GET['do']);
	if($do == "complete"){
		include "step".$step.".inc.php";
		exit;
	}
}
if(file_exists(PHPB2B_ROOT.'data/install.lock')) {
	header_sent(L("install_locked", "tpl"));
	exit;
}
if (!file_exists("../languages/".$app_lang."/LICENSE")) {
	header_sent(L("license_not_exists"));
	exit;
}
$backupdir = pb_radom(6);
$db_error = false;
switch($step)
{
	case '1':
	include "step".$step.".inc.php";

	break;
	case '2':
	$license = file_get_contents("../languages/".$app_lang."/LICENSE");
	include "step".$step.".inc.php";
	break;

	case '3':
	$gd_support = '';
	if(extension_loaded('gd'))
	{
		if(function_exists('imagepng')) $gd_support .= 'png';
		if(function_exists('imagejpeg')) $gd_support .= ' jpg';
		if(function_exists('imagegif')) $gd_support .= ' gif';
	}
	$is_right = (phpversion() >= '4.3.0' && extension_loaded('mysql')) ? 1 : 0;
	include "step".$step.".inc.php";
	break;
	case '4':
	$files = file("chmod.txt");
	$files = array_filter($files);
	$writablefile = $no_writablefile = null;
	foreach($files as $file)
	{
		$file = str_replace('*','',$file);
		$file = trim($file);
		if(!is_writable('../'.$file)){
			$no_writablefile .= $file.' '."&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&times;<br>";
		}else{
			$writablefile .= $file.' '.'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&radic;<br>';
		}
	}

	include "step".$step.".inc.php";
	break;

	case '5':

	include "step".$step.".inc.php";
	break;

	case '6':
	$dbhost = $_POST['dbhost'];
	$dbuser = $_POST['dbuser'];
	$dbpasswd = $_POST['dbpw'];
	$dbname = $_POST['dbname'];
	$tablepre = $_POST['tablepre'];
	$username = $_POST['username'];
	$password = $_POST['password'];
	$createdb = $_POST['db']['create'];
	$email = $_POST['email'];
	$passwordkey = $_POST['password_key'];
	$conn = @mysql_connect($dbhost, $dbuser, $dbpasswd);
	if(!$conn){
		$db_error = true;
	}
	include "step".$step.".inc.php";
	break;

	case '7':
	$sitename = $_POST['sitename'];
	if(isset($_POST['testdata'])){
		$testdata = $_POST['testdata'];
	}
	$configs['dbhost'] = $dbhost = $_POST['dbhost'];
	$configs['dbuser'] = $dbuser = $_POST['dbuser'];
	$configs['dbpasswd'] = $dbpasswd = $_POST['dbpw'];
	$configs['dbname'] = $dbname = $_POST['dbname'];
	$configs['tb_prefix'] = $tb_prefix = $_POST['tablepre'];
	$configs['dbcharset'] = $dbcharset;
	$configs['pconnect'] = $pconnect;
	$username = $_POST['username'];
	$password = $_POST['password'];
	$createdb = $_POST['createdb'];
	$configs['admin_email'] = $email = $_POST['email'];
	$passwordkey = $_POST['password_key'];
	if (empty($passwordkey)) {
		$passwordkey = pb_radom(16);
	}
	$configs['absolute_uri'] = $siteurl = $_POST['siteurl'];
	if(empty($passwordkey)){
		$passwordkey = pb_radom(16);
	}
	if(empty($sitename)){
		$sitename = L("a_new_b2b_site", "tpl");
	}
	if (empty($sitetitle)) {
		$sitetitle = L("a_new_b2b_title", "tpl");
	}
	$conn = @mysql_connect($dbhost, $dbuser, $dbpasswd);
	if($conn){
		$version = mysql_get_server_info();
		$set_names = "SET NAMES '$dbcharset'";
		$set_modes = "SET sql_mode=''";
		config_edit($configs);
		if($version > '4.1' && $charset)
		{
			mysql_query($set_names, $conn);
		}
		if($version > '5.0')
		{
			mysql_query($set_modes, $conn);
		}
		if(!@mysql_select_db($dbname))
		{
			if ($createdb==1) {
				if(mysql_get_server_info() > '4.1') {
					mysql_query("CREATE DATABASE IF NOT EXISTS"
					." $dbname DEFAULT CHARACTER SET $dbcharset;");
				} else {
					mysql_query("CREATE DATABASE IF NOT EXISTS $dbname;");
				}
				mysql_close();
			}else{
				$db_error = true;
				break;
			}
		} else {
			$sqldump = null;
			$conn = $db->connect($dbname,$dbhost,$dbuser,$dbpasswd);
			if($version > '4.1' && $charset)
			{
				$db->query($set_names);
			}
			if($version > '5.0')
			{
				$db->query($set_modes);
			}
			$tables = $db->table_names();
			if(!empty($tables)){
				foreach ($tables as $names) {
					if(!function_exists("stripos")){
                          function stripos($str,$needle) {
                                return strpos(strtolower($str),strtolower($needle));
                                     }
                           }
						if(stripos($names['table_name'],$tb_prefix) ===0){
							$sqldump.=data2sql($names['table_name']);
						}
					}
				
				pb_create_folder(PHPB2B_ROOT. DS. "data".DS."backup_".$backupdir);
				$file_path = PHPB2B_ROOT. DS. "data".DS."backup_".$backupdir.DS.date('ymd').'_'.pb_radom().".sql";
				if(trim($sqldump)) {
					file_put_contents($file_path ,$sqldump);
					unset($sqldump);
				}
			}
			$db->free();
		}
		ob_start();
		$schema_path = "data/schemas/".$app_lang."/";
		if(file_exists($schema_path. "mysql.sql"))
		{
			$conn = $db->connect($dbname,$dbhost,$dbuser,$dbpasswd);
			if($version > '4.1' && $charset)
			{
				$db->query($set_names);
			}
			if($version > '5.0')
			{
				$db->query($set_modes);
			}
			$sqls = file_get_contents($schema_path. "mysql.sql");
			sql_run($sqls);
			@touch(PHPB2B_ROOT.'./data/install.lock');
			$must_sql_data = file_get_contents($schema_path. "mysql.data.sql");
			sql_run($must_sql_data);
			if(!empty($testdata)){
				$source = "data/attachment/sample";
				$dest ="../attachment/sample";
				$sqls = file_get_contents($schema_path. "mysql.sample.sql");
				sql_run($sqls);
				dir_copy($source,$dest,1);
			}
			$db->query("REPLACE INTO {$tb_prefix}settings (variable, valued) VALUES ('install_dateline', '".$time_stamp."')");
			$db->query("REPLACE INTO {$tb_prefix}settings (variable, valued) VALUES ('site_name', '$sitename')");
			$db->query("REPLACE INTO {$tb_prefix}settings (variable, valued) VALUES ('site_title', '".htmlspecialchars($sitetitle)." - Powered By ".$arrTemplate['_software_name']."')");
	
			$db->query("REPLACE INTO {$tb_prefix}settings (variable, valued) VALUES ('backup_dir', '".$backupdir."')");
			$db->query("REPLACE INTO {$tb_prefix}settings (variable, valued) VALUES ('site_url', '".$siteurl."')");
			$db->query("REPLACE INTO {$tb_prefix}settings (variable, valued) VALUES ('watertext', '".$siteurl."')");
			$db->query("REPLACE INTO {$tb_prefix}settings (variable, valued) VALUES ('auth_key', '$passwordkey')");
			$aminer_id = 1;
			$db->query("REPLACE INTO {$tb_prefix}members (id,username, userpass,email,membertype_id,membergroup_id,created,modified,status) VALUES ({$aminer_id},'{$username}','".md5($password)."','{$email}',2,9,".$time_stamp.",".$time_stamp.",'1')");
			$db->query("REPLACE INTO {$tb_prefix}adminfields (member_id,last_name,created,modified) VALUES ('{$aminer_id}','".L("administrator")."',".$time_stamp.",".$time_stamp.")");	
			$db->free();
			require(PHPB2B_ROOT. "libraries".DS.'adodb'.DS.'adodb.inc.php');
			require(PHPB2B_ROOT. "libraries".DS."cache.class.php");
			$cache = new Caches();
			$pdb = &NewADOConnection($database);
			$conn = $pdb->PConnect($dbhost,$dbuser,$dbpasswd,$dbname);
			if($dbcharset && mysql_get_server_info() > '4.1') {
				$pdb->Execute("SET NAMES '{$dbcharset}'");
			}
			$cache->writeCache("setting", "setting");
			$cache->writeCache("setting1", "setting1");
			$cache->writeCache("industry", "industry");
			$cache->writeCache("area", "area");
			$cache->writeCache("membergroup", "membergroup");
			$cache->writeCache("userpage", "userpage");
			$cache->writeCache("trusttype", "trusttype");
			$cache->writeCache("form", "form");
			$cache->updateTypevars();
			$cache->updateTypes();
			$cache->updateIndexCache();
			header("Location:install.php?step={$step}&do=complete");
		}
		else
		{
			$db_error = true;
			break;
		}
	}else{
		$db_error = true;
		break;
	}
	break;
}
function config_edit($configs) {
	global $dbcharset, $app_lang;
	if (!is_array($configs)) {
		return;
	}
	extract($configs);
	$configfile = PHPB2B_ROOT. 'configs'.DS.'config.inc.php';
	$configfiles = file_get_contents($configfile);
	$configfiles = trim($configfiles);
	$configfiles = preg_replace("/[$]dbhost\s*\=\s*[\"'].*?[\"'];/is", "\$dbhost = '$dbhost';", $configfiles);
	$configfiles = preg_replace("/[$]app_lang\s*\=\s*[\"'].*?[\"'];/is", "\$app_lang = '$app_lang';", $configfiles);
	$configfiles = preg_replace("/[$]dbuser\s*\=\s*[\"'].*?[\"'];/is", "\$dbuser = '$dbuser';", $configfiles);
	$configfiles = preg_replace("/[$]dbpasswd\s*\=\s*[\"'].*?[\"'];/is", "\$dbpasswd = '$dbpasswd';", $configfiles);
	$configfiles = preg_replace("/[$]dbname\s*\=\s*[\"'].*?[\"'];/is", "\$dbname = '$dbname';", $configfiles);
	$configfiles = preg_replace("/[$]admin_email\s*\=\s*[\"'].*?[\"'];/is", "\$admin_email = '$admin_email';", $configfiles);
	$configfiles = preg_replace("/[$]tb_prefix\s*\=\s*[\"'].*?[\"'];/is", "\$tb_prefix = '$tb_prefix';", $configfiles);
	$configfiles = preg_replace("/[$]cookiepre\s*\=\s*[\"'].*?[\"'];/is", "\$cookiepre = '".pb_radom(3)."_';", $configfiles);
	$configfiles = preg_replace("/[$]absolute_uri\s*\=\s*[\"'].*?[\"'];/is", "\$absolute_uri = '".$absolute_uri."';", $configfiles);
	if(file_put_contents($configfile, $configfiles)){
		return true;
	}else{
		return false;
	}
}
function dir_copy($source, $destination, $child){
     if(!is_dir($destination)){  
     	mkdir($destination,0777);  
     }  
     $handle=dir($source);  
     while($entry=$handle->read()) {  
         if(!in_array($entry, array('.', '..', '.svn'))){  
             if(is_dir($source."/".$entry)){  
                 if($child)    {
                 	dir_copy($source."/".$entry,$destination."/".$entry,$child);  
                 }
             }else{  
                 copy($source."/".$entry,$destination."/".$entry);  
             }  
         }  
     }
     return true;  
}
function showLanguages()
{
	global $app_lang;
	$return = array();
	$path = '../languages/';
	$handle = opendir($path);
	while(false !== $file=(readdir($handle))){
		$dir = $path.$file;
		if(is_dir($dir) && !in_array($file, array('.', '..', '.svn'))){
			require($dir."/template.install.inc.php");
			$tmp = "<option value='".$file."'";
			if($app_lang==$file) {
				$tmp.=" selected='selected'";
			}elseif ($_GET['app_lang'] == $file){
				$tmp.=" selected='selected'";
			}
			$tmp.=">".$arrTemplate['_language_name']."</option>";
			$return[] = $tmp;
		}
	}
	if (!empty($return)) {
		return implode("\r\n", $return);
	}else{
		return false;
	}
	closedir($handle);
}
?>