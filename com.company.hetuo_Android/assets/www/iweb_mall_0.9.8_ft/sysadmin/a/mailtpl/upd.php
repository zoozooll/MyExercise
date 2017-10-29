<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require("../foundation/module_mailtpl.php");
require_once("../foundation/module_admin_logs.php");

//语言包引入
$a_langpackage=new adminlp;

//权限管理
$right=check_rights("email_update");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
$id = short_check(get_args('id'));
$post['tpl_title'] = short_check(get_args('tpl_title'));
$post['tpl_content'] = urldecode(get_args('tpl_content'));
$post['last_modify'] = $ctime->long_time();

//数据表定义区
$t_mailtpl = $tablePreStr."mailtpl";
$t_admin_log = $tablePreStr."admin_log";

//定义读操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$mailtpl=update_mailtpl($dbo,$t_mailtpl,$post,$id);

if($mailtpl){
	/** 添加log */
	$admin_log ="修改邮件模板内容";
	admin_log($dbo,$t_admin_log,$admin_log);

	action_return(1,$a_langpackage->a_upd_suc,'-1');
} else {
	action_return(0,$a_langpackage->a_upd_lose,'-1');
}

?>