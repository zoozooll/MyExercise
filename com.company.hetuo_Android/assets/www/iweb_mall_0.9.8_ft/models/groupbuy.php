<?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require("foundation/asession.php");
require("configuration.php");
require("includes.php");
require_once("foundation/fstring.php");
require("foundation/flefttime.php");

/* 用户信息处理 */
require 'foundation/alogin_cookie.php';
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

//引入语言包
$i_langpackage = new indexlp;
$s_langpackage=new shoplp;

/* 定义文件表 */
$t_shop_info = $tablePreStr."shop_info";
$t_user_info = $tablePreStr."user_info";
$t_users = $tablePreStr."users";
$t_shop_category = $tablePreStr."shop_category";
$t_goods = $tablePreStr."goods";
$t_shop_groupbuy = $tablePreStr."groupbuy";
$t_areas = $tablePreStr."areas";

/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

/* 产品处理 */
$sql_best = "SELECT * FROM $t_goods WHERE is_on_sale=1 AND is_best=1 order by pv desc limit 4";
$sql_hot = "SELECT * FROM $t_goods WHERE is_on_sale=1 AND is_hot=1 order by pv desc limit 10";
$goods_best = $dbo->getRs($sql_best);
$goods_hot = $dbo->getRs($sql_hot);

/* 浏览记录 */
$getcookie = get_hisgoods_cookie();
$goodshistory = array();
if($getcookie) {
	arsort($getcookie);
	$getcookie = array_keys($getcookie);
	$gethisgoodsid = implode(",",array_slice($getcookie, 0, 4));
	$sql = "select is_set_image,goods_id,goods_name,goods_thumb,goods_price from $t_goods where goods_id in ($gethisgoodsid)";
	$goodshistory = $dbo->getRs($sql);
}


$header['title'] = "团购展示 "." - ".$SYSINFO['sys_title'];
$header['keywords'] = "团购展示 ".','.$SYSINFO['sys_keywords'];
$header['description'] = $SYSINFO['sys_description'];

/* 时间处理 */
$now_time = new time_class();
$now_time = $now_time -> short_time();

$sql = "SELECT b.*,g.* FROM `$t_shop_groupbuy` b left join `$t_goods` g on b.goods_id = g.goods_id";
$sql .= " WHERE b.recommended = 0 and g.lock_flg =0";
$sql .= " and b.start_time <'$now_time' and '$now_time' < b.end_time";
$result = $dbo->fetch_page($sql,$SYSINFO['product_page']);


$nav_selected =6;
?>