<?php
/*
The Name: 图片轮换插件
The URI: http://www.phpb2b.com/
Description: 这是一个图片轮换的插件， 可以用在图片轮换上技术支持见<a href="http://www.phpb2b.net/">Support Forum</a>.
Version: 2.0.0
Author: PB_TEAM
Author URI: http://www.phpb2b.com
*/
if(!defined('IN_PHPB2B')) exit('Not A Valid Entry Point');
/**
 * 处理一些其他操作
 */
$default_images = array(
array("link"=>"member.php", "image"=>"plugins/hello/image1.jpg"),
array("link"=>"member.php", "image"=>"plugins/hello/image2.jpg"),
array("link"=>"member.php", "image"=>"plugins/hello/image3.jpg"),
array("link"=>"member.php", "image"=>"plugins/hello/image4.jpg"),
array("link"=>"member.php", "image"=>"plugins/hello/image5.jpg"),
);
$xmldata_file = PHPB2B_ROOT."plugins/hello/template/imageroll.xml";
$default_xml_file = PHPB2B_ROOT."plugins/hello/imageroll.xml";
$cache_datafile = DATA_PATH."appcache/plugin-imageroll.xml";
if (defined("IN_PBADMIN")) {
	require(LIB_PATH. "xml.class.php");
	if (!file_exists($cache_datafile)) {
		$image_lists = XML_unserialize(file_get_contents($default_xml_file));
	}else{
		$image_lists = XML_unserialize(file_get_contents($cache_datafile));
	}
	if (isset($image_lists['data']['channel']['item'])) {
		$data = $image_lists['data']['channel']['item'];
	}else{
		$data = $default_images;
	}
	$smarty->assign("data", $data);
}
if (isset($_POST['save'])) {
	pb_submit_check("pluginvar");//检查提交的必要参数
	$tmp_arr = array();
	for($i=0; $i<count($_POST['image']); $i++){
		if (!empty($_POST['image'][$i])) {
			$tmp_arr[$i]['link'] = $_POST['link'][$i];
			$tmp_arr[$i]['image'] = $_POST['image'][$i];
		}
	}
	if (empty($tmp_arr)) {
		$data = $default_images;
	}else{
		$data = $tmp_arr;
	}
	setvar("Items", $data);
	$xml_data = $smarty->fetch("file:".$xmldata_file);
	file_put_contents($cache_datafile, $xml_data);
}elseif(!defined("IN_PBADMIN")){
	//显示模板
	if (!file_exists($cache_datafile)) {
		$smarty->assign("flashvars", "xml=plugins/hello/imageroll.xml");
	}
	$plugin->display("image_roll");//显示模板文件image_roll(.html)
}
?>