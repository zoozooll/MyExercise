<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/module_users.php");
require_once("../foundation/module_admin_logs.php");

//语言包引入
$a_langpackage=new adminlp;

//权限管理
$right=check_rights("user_update");
$shop_right=check_rights("shop_update");
if(!$right and !$shop_right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}

$post['email_check'] = intval(get_args('email_check'));
$post['rank_id'] = intval(get_args('rank_id'));
$post['user_email'] = short_check(get_args('user_email'));
if(get_args('password')) {
	$post['user_passwd'] = md5(get_args('password'));
}
$user_id = intval(get_args('user_id'));
if(!$user_id) { exit($a_langpackage->a_error);}

//数据表定义区
$t_users = $tablePreStr."users";
$t_admin_log = $tablePreStr."admin_log";

//定义写操作
$dbo = new dbex;
dbtarget('w',$dbServs);

if(update_user_info($dbo,$t_users,$post,$user_id)) {
	admin_log($dbo,$t_admin_log,$sn = '修改用户信息');
	action_return(1,$a_langpackage->a_amend_suc,'m.php?app=member_reinfo&id='.$user_id);
} else {
	action_return(0,$a_langpackage->a_amend_lose,'-1');
}
?>