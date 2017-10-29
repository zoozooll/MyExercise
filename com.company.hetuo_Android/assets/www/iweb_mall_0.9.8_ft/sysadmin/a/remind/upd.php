<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//语言包引入
$a_langpackage=new adminlp;

require("../foundation/module_remind.php");
require_once("../foundation/module_admin_logs.php");

//权限管理
$right=check_rights("remind_update");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}

$id = short_check(get_args('id'));
$post['remind_type']=short_check(get_args('remind_type'));
$post['remind_name']=short_check(get_args('remind_name'));
$post['remind_tpl']=short_check(get_args('remind_tpl'));
$post['enable']=short_check(get_args('enable'));

//数据表定义区
$t_remind = $tablePreStr."remind";
$t_admin_log = $tablePreStr."admin_log";

//定义读操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$remind=update_remind($dbo,$t_remind,$post,$id);
//echo $sql;
if($remind){
	/** 添加log */
	$admin_log ="更新提醒设置内容";
	admin_log($dbo,$t_admin_log,$admin_log);

	action_return(1,$a_langpackage->a_upd_suc,'-1');
} else {
	action_return(0,$a_langpackage->a_upd_lose,'-1');
}

?>