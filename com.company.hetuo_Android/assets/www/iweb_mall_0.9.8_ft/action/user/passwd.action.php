<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require("foundation/module_users.php");

//语言包引入
$m_langpackage=new moduleslp;

//定义文件表
$t_users = $tablePreStr."users";

// 处理post变量
$user_passwd = short_check(get_args('user_passwd'));
$user_new_passwd = short_check(get_args('user_new_passwd'));

if(empty($user_passwd) || empty($user_new_passwd)) {
	action_return(0,$m_langpackage->m_oldpassword_notnone,'-1');
	exit;
}

//数据库操作
dbtarget('r',$dbServs);
$dbo=new dbex();
$user_info = get_user_info_item($dbo,array('user_passwd'),$t_users,$user_id);

if(md5($user_passwd) == $user_info['user_passwd']) {
	//数据库操作
	dbtarget('w',$dbServs);
	$dbo=new dbex();
	if(update_user_info($dbo,$t_users,array('user_passwd'=>md5($user_new_passwd)),$user_id)) {
		action_return(1,$m_langpackage->m_editpassword_success);
	} else {
		action_return(0,$m_langpackage->m_oprate_fail,'-1');
	}
} else {
	action_return(0,$m_langpackage->m_oldpassword_notture,'-1');
}
exit;
?>