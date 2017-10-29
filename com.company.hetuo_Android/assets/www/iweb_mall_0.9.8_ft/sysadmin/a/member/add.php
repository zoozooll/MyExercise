<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

require_once("../foundation/module_users.php");

	//语言包引入
	$a_langpackage=new adminlp;
	

$post['email_check'] = intval(get_args('email_check'));
$post['user_email'] = short_check(get_args('user_email'));
if(get_args('password')) {
	$post['user_passwd'] = md5(get_args('password'));
}
$user_id = intval(get_args('user_id'));
if(!$user_id) { exit($a_langpackage->a_error);}

//数据表定义区
$t_users = $tablePreStr."users";

//定义写操作
$dbo = new dbex;
dbtarget('w',$dbServs);

if(update_user_info($dbo,$t_users,$post,$user_id)) {
	action_return(1,$a_langpackage->a_amend_suc,'m.php?app=member_reinfo&id='.$user_id);
} else {
	action_return(0,$a_langpackage->a_amend_lose,'-1');
}
?>