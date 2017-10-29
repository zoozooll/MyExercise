<?php
/*
The Name: 谷歌地图
The URI: http://www.phpb2b.com/
Description: 谷歌地图
Version: 1.0.0
Author: PB_TEAM
Author URI: http://www.phpb2b.com
*/
if(!defined('IN_PHPB2B')) exit('Not A Valid Entry Point');
$pb_plugin_name = "googlemap";//必须的参数，即为文件夹的名称
/**
 * 处理一些其他操作
 */
if (isset($_POST['save'])) {
	pb_submit_check("pluginvar");//检查提交的必要参数
}elseif(!defined("IN_PBADMIN")){
	//显示地图模板
	if(isset($width)){
		setvar("width", $width);
	}
	if(isset($height)){
		setvar("height", $height);
	}
	if (!empty($params['lat']) && is_numeric($params['lat'])) {
		setvar("lat", $params['lat']);
	}
	if (!empty($params['lng']) && is_numeric($params['lng'])) {
		setvar("lng", $params['lng']);
	}
	if (!empty($params['title'])) {
		setvar("title", $params['title']);
	}
	$plugin->display("show");
}
setvar("zoomlevels", $levels = range(0,17));
?>