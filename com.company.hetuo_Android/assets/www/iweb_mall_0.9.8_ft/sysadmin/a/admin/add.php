<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_admin_logs.php");


//语言包引入
$a_langpackage=new adminlp;
//权限管理
$right=check_rights("add_admin");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
/* post 数据处理 */
$post['admin_name'] = short_check(get_args('admin_name'));
$post['admin_email'] = short_check(get_args('admin_email'));
$post['admin_password'] = md5(short_check(get_args('admin_password')));
$post['group_id'] = get_args('group');
//print_r($post);
if(empty($post['admin_name'])) {
	action_return(0,$a_langpackage->a_adminnamenot_null,'-1');
	exit;
}

//数据表定义区
$t_admin_user = $tablePreStr."admin_user";
$t_admin_log = $tablePreStr."admin_log";

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$item_sql = get_insert_item($post);
$sql = "insert `$t_admin_user` $item_sql";

if($dbo->exeUpdate($sql)) {
	admin_log($dbo,$t_admin_log,$sn = '添加管理员');
	action_return(1,$a_langpackage->a_add_suc);
} else {
	action_return(0,$a_langpackage->a_add_lose,'-1');
}
?>