<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
$m_langpackage=new moduleslp;
$user_name = short_check(get_args('user_name')); //用户名已经记录了

if(empty($user_name)) {
	action_return(0, $m_langpackage->m_username_notnone,'-1');
}

include("foundation/module_users.php");

//数据表定义区
$t_users = $tablePreStr."users";

//定义读操作
dbtarget('r',$dbServs);
$dbo=new dbex;
$sql="select * from $t_users where user_id='$user_id'";
$user_info=$dbo->getRow($sql);

if($user_info['user_name']){
	action_return(0, $m_langpackage->m_username_isexied,'-1');
}

//定义写操作
dbtarget('w',$dbServs);
$sql="update $t_users set user_name='$user_name' where user_id='$user_id'";
if($dbo->exeUpdate($sql)) {
	set_sess_user_name($user_name);
	action_return(1,$m_langpackage->m_edit_success);
} else {
	action_return(1,'',"modules.php?app=set_name");
}
?>