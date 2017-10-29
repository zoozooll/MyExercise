<?php
/* Iweb产品配置文件 */
if(!$IWEB_SHOP_IN) {
	die("Hacking attempt");
}

ini_set("date.timezone","UTC");

//站点配置
$webRoot = str_replace("\\","/",dirname(__FILE__))."/";
$adminEmail = 'admin@admin.com';

//缓存更新延时设置,单位为秒
$cache_update_delay_time="0";

//语言包参数，目前参数值zh,en
$langPackagePara = 'ft';
$langPackageBasePath = 'langpackage/'.$langPackagePara.'/';

//支持库配置
$baseLibsPath = 'iweb_mini_lib/';

//plugins位置文件
$pluginOpsition = array("index.html");

// session 前缀
global $session_prefix;
$session_prefix = 'iweb_';

// web访问根目录
$baseUrl = 'http://localhost:8081/iweb_mall_0.9.8_ft/';

// url_rewrite 是否开启
$url_rewrite = false;

// IM 是否开启
$im_enable = false;
?>