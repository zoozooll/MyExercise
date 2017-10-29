<?php
/**
 * PHPB2B :  Opensource B2B Script (http://www.phpb2b.com/)
 * Copyright (C) 2007-2010, Ualink. All Rights Reserved.
 * 
 * Licensed under The Languages Packages Licenses.
 * Support : phpb2b@hotmail.com
 * 
 * @version $Revision: 1334 $
 */
/**
 * 以下变量请根据空间商提供的账号参数修改,如有疑问,请联系服务器提供商
 */
$dbhost = 'localhost';					// 数据库服务器
$dbuser = 'root';						// 数据库用户名
$dbpasswd = '123456';					// 数据库密码
$dbname = 'phpb2b';				// 数据库名
$pconnect = 0;							// 数据库持久连接 0=关闭, 1=打开

/**
 * 如您对 cookie 作用范围有特殊要求, 或登录不正常, 请修改下面变量, 否则请保持默认
 */
$cookiepre = 'H4w_';					// cookie 前缀
$cookiedomain = ''; 					// cookie 作用域
$cookiepath = '/';						// cookie 作用路径

/**
 * 表名前缀, 同一数据库安装多个PHPB2B请修改此处
 */
$tb_prefix = 'pb_';

/**
 * 数据库类型及编码设置
 */
$database = 'mysql';					// 网站数据库类型，请勿修改
$dbcharset = 'utf8';					// MySQL 字符集, 可选 'gbk', 'big5', 'utf8', 'latin1', 留空为按照默认字符集设定

/**
 * 网站编码的相关配置信息
 */
$charset = 'utf-8';						// 网站页面默认字符集, 可选 'gbk', 'big5', 'utf-8'
$headercharset = 0;						// 强制网站页面使用默认字符集，可避免部分服务器空间页面出现乱码，一般无需开启。 0=关闭 1= 开启

/**
 * 系统管理员 Email和ID编号
 */
$admin_email = 'administrator@yourdomain.com';
$administrator_id = '1';

/**
 * 域名及地址的相关配置
 */
$absolute_uri = 'http://localhost:8081/phpb2b/';
$gzipcompress = false; 			// use GZIP output buffering if possible (true|false)
$admin_runquery = false;			// 后台是否允许管理员执行SQL语句[出于安全考虑,默认不允许执行]
$subdomain_support = 0;			// 是否支持二级域名,如果允许的话,空间主页链接则变为二级域名
$topleveldomain_support = 0;		// 是否支持顶级域名,如果支持的话,企业访问时会考虑解析顶级域名库[比较耗费资源,请解析到space目录]
$rewrite_able = 0;					// 是否支持网址静态化
$rewrite_compatible = 0;			// 是否支持URL中的中文字符，如果支持，则不会调用urlencode
$attachment_url = 'attachment/';
$attachment_dir = 'attachment';

/**
 * 控制台的相关设置
 */
$app_lang = 'zh-cn';
$cfg_checkip = 0;
?>