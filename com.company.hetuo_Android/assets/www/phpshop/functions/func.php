<?php
if( !defined('DEFINE_PHP_FUNC') ){
define('DEFINE_PHP_FUNC','YES');

//web  
define('__WEB','phpshop');
define('__IMG','product/');
/// physical path ///
define('__ROOT_TPLS_TPATH',__ROOT_PATH.'tpls/');
define('__ROOT_TEMPLATES_TPATH', __ROOT_TPLS_TPATH);
define('__PRODUCT_IMG',__ROOT_PATH.'product/');
define('__PRODUCT_XML',__PRODUCT_IMG.'xml/');
//cache
define('__CACHE',__ROOT_PATH.'cache/');
define('__ROOT_LOGS_PATH',__CACHE.'logs/');
define('__COMMSITE',__CACHE.'site/');
define('__COMMLAGUAGE',__CACHE.'etc/laguage/');

// web charset, language
define('__CHARSET','utf-8');
define('__LANGUAGE','zh-CN');

//debug
define('__Debug',true);
}
?>