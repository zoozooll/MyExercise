<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

/* 默认配置系统信息 */
$SYSINFO['sys_name'] = 'iweb Mall';
$SYSINFO['sys_title'] = 'iweb Mall';
$SYSINFO['sys_keywords'] = 'iweb mall';
$SYSINFO['sys_description'] = 'iweb mall';
$SYSINFO['sys_company'] = '聚易开放式技术实验室';
$SYSINFO['sys_copyright'] = 'Copyright © 2005-2009';
$SYSINFO['sys_icp'] = '鲁ICP备01000010号';
$SYSINFO['sys_registerinfo'] = '';
$SYSINFO['sys_kftelphone'] = '0531-';
$SYSINFO['sys_kfqq'] = '';
$SYSINFO['email_send'] = 'false';
$SYSINFO['sys_smtpserver'] = 'mail.qq.com';
$SYSINFO['sys_smtpserverport'] = '25';
$SYSINFO['sys_smtpusermail'] = '';
$SYSINFO['sys_smtpuser'] = '';
$SYSINFO['sys_smtppass'] = '';
$SYSINFO['session'] = 'iweb_';
$SYSINFO['lp'] = 'zh';
$SYSINFO['web'] = 'http://localhost/iweb_mall/';
$SYSINFO['url_r'] = 'false';
$SYSINFO['im_enable'] = 'false';
$SYSINFO['seller_page'] = '10';
$SYSINFO['search_page'] = '10';
$SYSINFO['product_page'] = '10';
$SYSINFO['article_page'] = '10';
$SYSINFO['height1'] = '84';
$SYSINFO['width1'] = '84';
$SYSINFO['height2'] = '300';
$SYSINFO['width2'] = '300';
$SYSINFO['templates'] = 'default';
$SYSINFO['template_mode'] = 'debug';
$SYSINFO['offline'] = 'true';
$SYSINFO['off_info'] = '站点维护中。。。';
$SYSINFO['timezone'] = '8';
$SYSINFO['sys_countjs'] = '';
$SYSINFO['map'] = 'false';
$SYSINFO['map_key'] = '';
$SYSINFO['sys_logo'] = '';
$SYSINFO['sys_domain'] = '0';
$SYSINFO['sys_smtptest'] = '';
$SYSINFO['version'] = 'v0.9.8';
if(file_exists($webRoot."/cache/setting.php")) {
	include($webRoot."/cache/setting.php");
}
?>