<?php
/*
The Name: 视频播放插件
The URI: http://www.phpb2b.com/
Description: 视频播放插件，采用JW Player<a href="http://www.phpb2b.net/">Support Forum</a>.
Version: 1.0.0
Author: PB_TEAM
Author URI: http://www.phpb2b.com/
*/
if(!defined('IN_PHPB2B')) exit('Not A Valid Entry Point');
/**
 * 处理一些其他操作
 */
if (isset($_POST['save'])) {
	pb_submit_check("pluginvar");//检查提交的必要参数
}elseif(!defined("IN_PBADMIN")){
	//显示模板
	$plugin->display("show");//显示模板文件image_roll(.html)
}
?>