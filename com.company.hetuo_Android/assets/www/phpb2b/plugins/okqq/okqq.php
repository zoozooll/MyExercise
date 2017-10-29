<?php
/*
The Name: QQ客服1
The URI: http://www.phpb2b.com/
Description: 调用QQ客服代码
Version: 1.0.0
Author: PB_TEAM
Author URI: http://www.phpb2b.com
*/
if(!defined('IN_PHPB2B')) exit('Not A Valid Entry Point');
$pb_plugin_name = "okqq";//必须的参数，即为文件夹的名称
/**
 * 处理一些其他操作
 */
if (isset($_POST['save'])) {
	pb_submit_check("pluginvar");//检查提交的必要参数
}elseif(!defined("IN_PBADMIN")){
	$plugin->display("show");
}
?>