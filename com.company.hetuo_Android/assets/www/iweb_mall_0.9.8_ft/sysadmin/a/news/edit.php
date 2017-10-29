<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_news.php");
require_once("../foundation/module_admin_logs.php");
//语言包引入
$a_langpackage=new adminlp;

//权限管理
$right=check_rights("news_edit");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');	
}
$ctime = new time_class;

/* post 数据处理 */
$post['title'] = short_check(get_args('title'));
$post['cat_id'] = intval(get_args('cat_id'));
$post['is_link'] = intval(get_args('is_link'));
$post['is_show'] = intval(get_args('is_show'));
$post['link_url'] = short_check(get_args('link_url'));
$post['content'] = big_check(get_args('content'));
$post['admin_id'] = $_SESSION['admin_id'];
$post['add_time'] = $ctime->long_time();

$article_id = intval(get_args('article_id'));

if(!$article_id) {
	action_return(0,$a_langpackage->a_error,'-1');
	exit;
}

if(empty($post['title'])) {
	action_return(0,$a_langpackage->a_title_null,'-1');
	exit;
}

//数据表定义区
$t_article = $tablePreStr."article";
$t_admin_log = $tablePreStr."admin_log";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

if(update_news_info($dbo,$t_article,$post,$article_id)) {
	admin_log($dbo,$t_admin_log,"修改文章：$article_id");
	action_return(1,$a_langpackage->a_amend_suc,'m.php?app=news_edit&id='.$article_id);
} else {
	action_return(0,$a_langpackage->a_amend_lose,'-1');
}
?>