<?php
/*
The Name: 视频展播插件
The URI: http://www.phpb2b.com/
Description: 视频调用插件
Version: 1.0.0
Author: PB_TEAM
Author URI: http://www.phpb2b.com
*/
if(!defined('IN_PHPB2B')) exit('Not A Valid Entry Point');
$pb_plugin_name = "vcastr";//必须的参数，即为文件夹的名称
require(LIB_PATH.'xml.class.php');
$file = PHPB2B_ROOT.'plugins/vcastr/video.xml';
if (defined("IN_PBADMIN")) {
	$xml = file_get_contents($file);
	$data = XML_unserialize($xml);
	$video_list = array();
	if (!empty($data)) {
		foreach ($data['vcastr']['item'] as $val) {
			if (!empty($val)) {
				$video_list[$val['item_url']] = $val['item_title'];
			}
		}
	}
	$smarty->assign("data", $video_list);
}
/**
 * 处理一些其他操作
 */
if (isset($_POST['save'])) {
	pb_submit_check("pluginvar");//检查提交的必要参数
	$source = $_POST['source'];
	$title = $_POST['title'];
	buildxml($source, $title, '');
}elseif(!defined("IN_PBADMIN")){
	//显示模板
	$plugin->display("play");//显示模板文件image_roll(.html)
}
function buildxml($source, $title,  $encoding =''){
	$temp_str = '';
	global $pb_plugin_name;
	$filename = PHPB2B_ROOT."plugins".DS.$pb_plugin_name.DS."video.xml";
	$result = array_combine($source, $title);
	$rs = array_filter($result,'filter');
	if(empty($encoding)){
		$encoding = 'utf-8';
	}
	$temp_str .=  "<?xml version=\"1.0\" encoding=\"$encoding\"?>\n";
	$temp_str .= "\t\t<vcastr>\n";
	foreach($rs as $key=>$val){
		$url = htmlentities($key,ENT_QUOTES);
		$temp_str .= "\t\t<item item_url=\"".$url."\" item_title=\"".$val."\" />\n";
	}
	$temp_str .= "\t\t</vcastr>\n";
	$fp = @fopen($filename,"w+") or die(sprintf("建立文件1%失败",$filename));
	@fwrite($fp,$temp_str);
	@fclose($fp);;
}
 function filter($var){
 	if($var == ''||$var == '请输入视频地址'||$var =='请输入视频标题'){
 		return false;
 	}
 	return true;
 }
?>