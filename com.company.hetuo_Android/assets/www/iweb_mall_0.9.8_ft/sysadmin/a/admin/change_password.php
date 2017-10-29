<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
require_once("../foundation/module_admin_logs.php");

//语言包引入
$a_langpackage=new adminlp;
//权限管理
$right=check_rights("pass_update");
if(!$right){
	action_return(0,$a_langpackage->a_privilege_mess,'m.php?app=error');
}
// post
$password = md5(short_check(get_args('password')));
$new_password = md5(short_check(get_args('new_password')));

if(empty($password)) {
	action_return(0, $a_langpackage->a_oldpasswd_null,'-1');
}

if(empty($new_password)){
	action_return(0, $a_langpackage->a_newpasswd_null,'-1');
}

//数据表定义区
$t_admin_user = $tablePreStr."admin_user";
$t_admin_log = $tablePreStr."admin_log";

//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;
$sql="select * from `$t_admin_user` where admin_id='$admin_id'";
$admin_info=$dbo->getRow($sql);

if(empty($admin_info)){
	action_return(0, $a_langpackage->a_passwdedit_fail,'-1');
}

if($password != $admin_info['admin_password']){
	action_return(0, $a_langpackage->a_oldpasswd_inputerr,'-1');
}

//定义写操作
dbtarget('w',$dbServs);
$sql="update `$t_admin_user` set `admin_password`='$new_password' where admin_id='$admin_id'";

if($dbo->exeUpdate($sql)) {
	admin_log($dbo,$t_admin_log,$sn = '修改管理员密码');
	action_return(1,$a_langpackage->a_amend_suc,'-1');
} else {
	action_return(0,$a_langpackage->a_amend_lose,'-1');
}
?>