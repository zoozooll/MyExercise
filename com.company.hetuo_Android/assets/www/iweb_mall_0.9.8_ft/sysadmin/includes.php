<?php
/* 公共包含文件 */
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

error_reporting(0);

//数据库配置及连接文件
require_once($webRoot.$baseLibsPath."conf/dbconf.php");
require_once($webRoot.$baseLibsPath."fdbtarget.php");
require_once($webRoot.$baseLibsPath."libs_inc.php");
require_once($webRoot."langpackage/".$langPackagePara."/adminlp.php");

//库支持文件
//表操作类
require($webRoot.$baseLibsPath."cdbex.class.php");
//过滤函数
require($webRoot."foundation/freq_filter.php");
//应用工具控制函数
require($webRoot."foundation/fmain_target.php");
//时间函数
require($webRoot."foundation/ctime.class.php");
//文件上传函数
require($webRoot."foundation/cupload.class.php");

//封装session,cookie,get,post文件
//require($webRoot."foundation/fsession.php");
//require($webRoot."foundation/fcookie.php");
require($webRoot."foundation/fgetandpost.php");
require($webRoot."foundation/fstring.php");
require($webRoot."foundation/fsqlitem_set.php");
require($webRoot."foundation/asystem_info.php");
require($webRoot."foundation/module_rights.php");

$ctime = new time_class($SYSINFO['timezone']);
?>