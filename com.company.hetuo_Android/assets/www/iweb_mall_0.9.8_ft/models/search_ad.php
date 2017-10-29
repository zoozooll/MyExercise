<?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require("foundation/asession.php");
require("configuration.php");
require("includes.php");
require("foundation/module_areas.php");

//引入语言包
$i_langpackage = new indexlp;
$m_langpackage = new moduleslp;

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

/* 定义文件表 */
$t_shop_info = $tablePreStr."shop_info";
$t_category = $tablePreStr."category";
$t_goods = $tablePreStr."goods";
$t_index_images = $tablePreStr."index_images";
$t_brand = $tablePreStr."brand";
$t_article = $tablePreStr."article";
$t_areas = $tablePreStr."areas";

/* 数据库操作 */
dbtarget('r',$dbServs);
$dbo=new dbex();

/* 处理系统分类 */
$sql_category = "select * from `$t_category` order by sort_order asc,cat_id asc";
$result_category = $dbo->getRs($sql_category);
$CATEGORY = array();
foreach($result_category as $v) {
	$CATEGORY[$v['parent_id']][$v['cat_id']] = $v;
}

$sql_brand = "select * from $t_brand where is_show=1";
$brand_rs = $dbo->getRs($sql_brand);

$areas_info = get_areas_info($dbo,$t_areas);

//引入语言包
$i_langpackage = new indexlp;

	$header['title'] = '高级搜索';
	$header['keywords'] = '高级搜索';

$header['description'] = $SYSINFO['sys_description'];

$nav_selected=1;
?>