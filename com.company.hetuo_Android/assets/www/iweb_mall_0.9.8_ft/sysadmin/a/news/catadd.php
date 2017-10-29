<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_news.php");
require_once("../foundation/module_admin_logs.php");
//语言包引入
$a_langpackage=new adminlp;

//权限管理
$right=check_rights("news_catlist_add");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');	
}	
/* post 数据处理 */
$post['cat_name'] = short_check(get_args('cat_name'));
$post['parent_id'] = '0';
$post['sort_order'] = intval(get_args('sort_order'));
//print_r($post);
//exit;
if(empty($post['cat_name'])) {
	action_return(0,$a_langpackage->a_title_null,'-1');
	exit;
}

//数据表定义区
$t_article_cat = $tablePreStr."article_cat";
$t_admin_log = $tablePreStr."admin_log";
//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$article_id = insert_news_info($dbo,$t_article_cat,$post);

if($article_id) {
	admin_log($dbo,$t_admin_log,"新增文章分类：$article_id");
	action_return(1,$a_langpackage->a_add_suc);
} else {
	action_return(0,$a_langpackage->a_add_lose,'-1');
}
?>