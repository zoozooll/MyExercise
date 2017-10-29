<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

/* 公共信息处理 header, left, footer */
require("foundation/module_shop.php");
require("foundation/module_users.php");
require("foundation/flefttime.php");
//引入语言包
$s_langpackage=new shoplp;

/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

/* 定义文件表 */
$t_shop_info = $tablePreStr."shop_info";
$t_user_info = $tablePreStr."user_info";
$t_users = $tablePreStr."users";
$t_shop_category = $tablePreStr."shop_category";
$t_goods = $tablePreStr."goods";
$t_shop_groupbuy = $tablePreStr."groupbuy";

/* 商铺信息处理 */
$SHOP = get_shop_info($dbo,$t_shop_info,$shop_id);
if(!$SHOP) { exit("没有此商铺！");}
$sql = "select rank_id,user_name,last_login_time from $t_users where user_id='".$SHOP['user_id']."'";
$ranks = $dbo->getRow($sql);
$SHOP['rank_id'] = $ranks[0];

$header['title'] = "团购展示 - ".$SHOP['shop_name'];
$header['keywords'] = $SHOP['shop_management'];
$header['description'] = sub_str(strip_tags($SHOP['shop_intro']),100);
/* 时间处理 */
$now_time = new time_class();
$now_time = $now_time -> short_time();
$sql = "SELECT b.*,g.* FROM `$t_shop_groupbuy` b left join `$t_goods` g on b.goods_id = g.goods_id";
$sql .= " WHERE b.shop_id='$shop_id' and b.recommended = 0 and g.lock_flg =0";
$sql .= " and b.start_time <'$now_time' and '$now_time' < b.end_time";
$result = $dbo->fetch_page($sql,$SYSINFO['product_page']);
?>