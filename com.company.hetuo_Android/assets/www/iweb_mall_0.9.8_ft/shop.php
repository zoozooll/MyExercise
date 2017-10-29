<?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require("foundation/asession.php");
require("configuration.php");
require("includes.php");
require("foundation/fstring.php");

/* URL信息处理 */
$shop_id = intval($_GET['shopid']);
if(!$shop_id) {
	exit("非法请求！");
}

/* 用户信息处理 */
if(get_sess_user_id()) {
	$USER['login'] = 1;
	$USER['user_name'] = get_sess_user_name();
	$USER['user_id'] = get_sess_user_id();
	$USER['user_email'] = get_sess_user_email();
	$USER['shop_id'] = get_sess_shop_id();
} else {
	$USER['login'] = 0;
	$USER['user_name'] = '';
	$USER['user_id'] = '';
	$USER['user_email'] = '';
	$USER['shop_id'] = '';
}

if(!$_GET['app']) { $_GET['app']='index'; }

//当前可访问的应用工具
$appArray = array(
	'index'		=> 'shop/index.php',
	'intro'		=> 'shop/intro.php',
	'products'	=> 'shop/products.php',
	'credit'	=> 'shop/credit.php',
	'map'		=> 'shop/map.php',
	'groupbuy'	=> 'shop/groupbuy.php',
	'plugin'	=> 'shop/plugin.php',
);

$appId = getAppId();
$apptarget = $appArray[$appId];

if(isset($apptarget)) {
	require($apptarget);
} else {
	echo 'no pages!';
}
?>