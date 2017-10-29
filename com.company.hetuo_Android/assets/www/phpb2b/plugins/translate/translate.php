<?php
/*
The Name: 翻译插件
The URI: http://www.phpb2b.com/
Description: Google翻译插件
Version: 1.0.0
Author: PB_TEAM
Author URI: http://www.phpb2b.com
*/
if(!defined('IN_PHPB2B')) exit('Not A Valid Entry Point');
$pb_plugin_name = "translate";//必须的参数，即为文件夹的名称
/**
 * 处理一些其他操作
 */
if (isset($_POST['save'])) {
	pb_submit_check("pluginvar");//检查提交的必要参数
}elseif(!defined("IN_PBADMIN")){
	//显示模板
	$plugin->display("show");//显示模板文件
}
?>