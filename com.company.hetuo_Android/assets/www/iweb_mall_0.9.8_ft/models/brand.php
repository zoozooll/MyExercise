<?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require("foundation/asession.php");
require("configuration.php");
require("includes.php");
require_once("foundation/fstring.php");

require_once("foundation/module_areas.php");

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

//引入语言包
$i_langpackage=new indexlp;

/* 定义文件表 */
$t_shop_info = $tablePreStr."shop_info";
$t_users = $tablePreStr."users";
$t_goods = $tablePreStr."goods";
$t_areas = $tablePreStr."areas";

/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();


/* 产品处理 */
$sql_best = "SELECT * FROM $t_goods WHERE is_on_sale=1 AND is_best=1 order by pv desc limit 4";
$sql_hot = "SELECT * FROM $t_goods WHERE is_on_sale=1 AND is_hot=1 order by pv desc limit 10";
$goods_best = $dbo->getRs($sql_best);
$goods_hot = $dbo->getRs($sql_hot);

/* 地区信息 */
$areainfo = get_areas_kv($dbo,$t_areas);

/* 列表处理 */

$k = short_check(get_args('k'));

$sql = "SELECT * FROM `$t_shop_info` as a, `$t_users` as b WHERE a.user_id=b.user_id and open_flg=0";
if($k) {
	$sql .= " and a.shop_name LIKE '%$k%' ";
}
$sql .= " ORDER BY shop_creat_time DESC";

$result = $dbo->fetch_page($sql,$SYSINFO['seller_page']);

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

$search_type = 'brand';

$header['title'] = $i_langpackage->i_s_company." - ".$SYSINFO['sys_title'];
$header['keywords'] = $i_langpackage->i_s_company.','.$SYSINFO['sys_keywords'];
$header['description'] = $SYSINFO['sys_description'];
/*导航位置*/
$nav_selected=3;
?>