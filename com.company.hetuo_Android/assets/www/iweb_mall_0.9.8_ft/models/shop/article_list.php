<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("foundation/module_shop.php");
require("foundation/module_users.php");

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
$t_article = $tablePreStr."article";
$t_article_cat = $tablePreStr."article_cat";

$cat_id = intval(get_args('id'));

$sql = "SELECT * FROM `$t_article_cat` order by sort_order asc";
$article_cat = $dbo->getRs($sql);
if(!$article_cat) {
	exit("没有分类！");
}

foreach ($article_cat as $val){
	if($val['cat_id']==$cat_id){
		$cat_name=$val['cat_name'];
	}
}

$sql = "SELECT * FROM `$t_article` WHERE is_show=1";
if($cat_id){
	$sql.= " and cat_id=$cat_id";
}
$result = $dbo->fetch_page($sql,$SYSINFO['article_page']);
if(!$result) {
	exit("没有资讯！");
}

$header['title'] = $cat_name;
$header['keywords'] = $cat_name;
$header['description'] = sub_str(strip_tags($cat_name),100);
/*导航位置*/
$nav_selected=5;
?>