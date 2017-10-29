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

$k = short_check(get_args('k'));
$ucat_id = intval(get_args('ucat_id'));

//数据表定义区
$t_goods = $tablePreStr."goods";
$t_brand = $tablePreStr."brand";
$t_goods_types = $tablePreStr."goods_types";
$t_shop_category = $tablePreStr."shop_category";

//读写分离定义方法
$dbo = new dbex;
dbtarget('r',$dbServs);

$sql = "select * from `$t_goods` where shop_id='$shop_id'";
if($k && $k!=$m_langpackage->m_goods_keyword) {
	$sql .= " and goods_name like '%$k%' ";
}
if($ucat_id) {
	$sql .= " and ucat_id='$ucat_id' ";
}

$sql .= " order by sort_order asc,goods_id desc";

$result = $dbo->fetch_page($sql,10);
$rowset = get_shop_category_list($dbo,$t_shop_category,$shop_id);
$shop_category_info = array();
if($rowset) {
	foreach($rowset as $value) {
		$shop_category_info[$value['shop_cat_id']] = $value['shop_cat_name'];
	}
}

$typeinfo = get_goods_type($dbo,$t_goods_types);

$shop_category = get_shop_category_list($dbo,$t_shop_category,$shop_id);
$html_shop_category = html_format_shop_category($shop_category,$ucat_id);
?>