<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/module_news.php");
require_once("../foundation/module_admin_logs.php");
//语言包引入
$a_langpackage=new adminlp;

//权限管理
$right=check_rights("news_del");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');	
}
/* get */
$article_id=get_args('article_id');
if($article_id){
	$id=implode(",", $article_id);
}else{
	$id = intval(get_args('id'));
}

if(!$id) {
	action_return(0,$a_langpackage->a_error,'-1');
}

//数据表定义区
$t_article = $tablePreStr."article";
$t_admin_log = $tablePreStr."admin_log";
//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$sql = "delete from `$t_article` where article_id in($id)";

if($dbo->exeUpdate($sql)) {
	admin_log($dbo,$t_admin_log,"删除文章：$id");
	action_return();
} else {
	action_return(0,$a_langpackage->a_del_lose,'-1');
}
?>