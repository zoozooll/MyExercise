<?php
/*
The Name: 百度sitemap
The URI: http://www.phpb2b.com/
Description: 使用百度互联网论坛收录开放协议能提高网站流量
Version: 1.0.0
Author: PB_TEAM
Author URI: http://www.phpb2b.com
*/
if(!defined('IN_PHPB2B')) exit('Not A Valid Entry Point');
$pb_plugin_name = "baidusitemap";//必须的参数，即为文件夹的名称
/**
 * 处理一些其他操作
 */
if (isset($_POST['save'])) {
	//生成googlesitemap
	pb_submit_check("pluginvar");//检查提交的必要参数
	buildsitemap($_POST['pluginvar']['loc'], '', $_POST['pluginvar']['webmaster'], $_POST['pluginvar']['updateperi'], $_POST['pluginvar']['updatetime']);
}


function buildsitemap($loc,$encoding = '', $webmaster = '', $updateperi = '', $updatetime = '') {
	/*****************
	* $loc			url地址 符号要转义
	符号 	& 	&amp;
	单引号 	' 	&apos;
	双引号 	" 	&quot;
	大于 	> 	&gt;
	小于 	< 	&lt;
	* $webmaster    网站负责人email
	* $updateperi	更新频率 以小时为单位
	* $updatetime	文件最近修改时间
	*******************/
	$s='';
	$filename = PHPB2B_ROOT."sitemap_baidu.xml";
	if(empty($encoding)){
		$encoding = "UTF-8";
	}

	$s = "<?xml version=\"1.0\" encoding=\"$encoding\"?>\n";

	$s .= "<document xmlns:bbs=\"http://www.baidu.com/search/bbs_sitemap.xsd\">\n";

	$loc = htmlentities($loc,ENT_QUOTES);

	$s .= "\t\t<webSite>\n\t\t\t<loc>$loc</webSite>\n";

	if(!empty($webmaster)){
		$s .= "\t\t\t<webMaster>$webmaster</webMaster>\n";
	}

	if(!empty($updateperi)){
		$s .= "\t\t\t<updatePeri>$updateperi</updatePeri>\n";
	}

	if(!empty($updatetime)){
		$s .= "\t\t\t<updatetime>$updatetime</updatetime>\n";
	}

	$s .= "\t</document>\n";

	$fp = @fopen($filename,"w+") or die(sprintf("建立文件1%失败",$filename));
	@fwrite($fp,$s);
	@fclose($fp);
}
?>