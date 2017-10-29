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

$sql = "SELECT * FROM `$t_article_cat` order by sort_order asc";
$article_cat = $dbo->getRs($sql);
if(!$article_cat) {
	exit("没有分类！");
}

$sql = "SELECT * FROM `$t_article` WHERE is_show=1 and article_id='$article_id'";
$article_info = $dbo->getRow($sql);
if(!$article_info) {
	exit("不存在此资讯！");
}

foreach ($article_cat as $val){
	if($val['cat_id']==$article_info['cat_id']){
		$cat_name=$val['cat_name'];
	}
}

$sql="SELECT * FROM $t_article WHERE article_id < $article_id ORDER BY article_id DESC LIMIT 1";
$up_article=$dbo->getRow($sql);
$sql="SELECT * FROM $t_article WHERE article_id > $article_id ORDER BY article_id ASC LIMIT 1";
$down_article=$dbo->getRow($sql);

if($article_info['is_link'] && $article_info['link_url']) {
	echo "<script>location.href = '".$article_info['link_url']."'</script>";
	exit;
}

$header['title'] = $article_info['title'];
$header['keywords'] = $article_info['title'];
$header['description'] = sub_str(strip_tags($article_info['content']),100);
$nav_selected=5;
?>