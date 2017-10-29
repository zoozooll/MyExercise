<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

	//语言包引入
	$a_langpackage=new adminlp;

$admin_name = short_check(get_args('admin_name'));
$admin_password = short_check(get_args('admin_password'));

if(empty($admin_name)) {
	action_return(0, $a_langpackage->a_name_null,'-1');
}

if(empty($admin_password)){
	action_return(0, $a_langpackage->a_pw_null,'-1');
}

//if(strtolower(get_args('verifyCode')) != strtolower($_SESSION['verifyCode'])) {
//	action_return(0, "验证码错误!",'-1');
//}

//数据表定义区
$t_admin_user = $tablePreStr."admin_user";
$t_admin_group = $tablePreStr."admin_group";
//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;
$sql="select * from `$t_admin_user` where admin_name='$admin_name'";
$admin_info=$dbo->getRow($sql);

if(empty($admin_info)){
	action_return(0,$a_langpackage->a_name_pw,'-1');
}

if($admin_password != $admin_info['admin_password']){
	action_return(0,$a_langpackage->a_name_pw,'-1');
}
if ($admin_info['group_id']) {
	$sql = "SELECT group_name FROM $t_admin_group WHERE id = '{$admin_info['group_id']}'";
	$group_info = $dbo->getRow($sql);
}else{
	$group_info['group_name']= $a_langpackage->a_super_admin;
}
$_SESSION['admin_name'] = $admin_info['admin_name'];
$_SESSION['admin_id'] = $admin_info['admin_id'];
$_SESSION['admin_last_login'] = $admin_info['last_login'];
$_SESSION['group_name']=$group_info['group_name'];

//定义写操作
dbtarget('w',$dbServs);
$dbo=new dbex;

$time = $ctime->long_time();
$last_ip = $_SERVER['REMOTE_ADDR'];
$sql="update `$t_admin_user` set last_login='$time',last_ip='$last_ip' where admin_id='".$admin_info['admin_id']."'";
$dbo->exeUpdate($sql);

//管理组权限
if($admin_info['admin_id']==1) {
	$_SESSION['rights'] = "all_priviilege";
} else {
	$group_id = $admin_info['group_id'];
	if($admin_info['group_id']){
		$sql_group="select * from `$t_admin_group` where id='$group_id'";
		$group_info=$dbo->getRow($sql_group);
		$_SESSION['rights']=$group_info['rights'];
	}else{
		$_SESSION['rights']="";
	}
}
action_return();
?>