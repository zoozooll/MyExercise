<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/module_users.php");
require_once("../foundation/module_admin_logs.php");

//语言包引入
$a_langpackage=new adminlp;

//权限管理
$right=check_rights("user_unlock");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}


$user_id=get_args('user_id');
if($user_id){
	$user_id=implode(",", $user_id);
}else{
	$user_id = intval(get_args('id'));
}

if(!$user_id) { exit($a_langpackage->a_error);}

//数据表定义区
$t_users = $tablePreStr."users";
$t_admin_log = $tablePreStr."admin_log";

//定义写操作
$dbo = new dbex;
dbtarget('w',$dbServs);

$sql="update `$t_users` set locked='0' where user_id in($user_id)";

if($dbo->exeUpdate($sql)) {
	admin_log($dbo,$t_admin_log,$sn = '解锁用户');
	action_return(1,$a_langpackage->a_amend_suc,'-1');
} else {
	action_return(0,$a_langpackage->a_amend_lose,'-1');
}
?>