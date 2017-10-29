<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/acheck_shop_creat.php");
require("foundation/ashop_news_category.php");
require("foundation/module_category.php");
require("foundation/module_type.php");

//引入语言包
$m_langpackage=new moduleslp;

$ucat_id = intval(get_args('ucat_id'));
$ctime=new time_class;
$now_time=$ctime->long_time();

//数据表定义区
$t_goods = $tablePreStr."goods";
$t_brand = $tablePreStr."brand";
$t_goods_types = $tablePreStr."goods_types";
$t_shop_category = $tablePreStr."shop_category";
$t_groupbuy = $tablePreStr."groupbuy";
$t_groupbuy_log = $tablePreStr."groupbuy_log";


//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$sql="select a.*,b.* from $t_groupbuy as a,$t_goods as b where a.shop_id='$shop_id' and a.goods_id=b.goods_id ";
$result = $dbo->fetch_page($sql,10);

$shop_category = get_shop_category_list($dbo,$t_shop_category,$shop_id);
$html_shop_category = html_format_shop_category($shop_category,$ucat_id);
?>