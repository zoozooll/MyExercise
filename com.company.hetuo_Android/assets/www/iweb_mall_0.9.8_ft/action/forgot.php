<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}
$m_langpackage=new moduleslp;
// 数据表定义区
$t_users = $tablePreStr."users";

$user_id = intval(get_args('user_id'));
$code = short_check(get_args('code'));
$user_passwd = md5(short_check(get_args('user_new_passwd')));

dbtarget('w',$dbServs);
$dbo=new dbex();
$sql = "update `$t_users` set user_passwd='$user_passwd',forgot_check_code='' where user_id='$user_id' and forgot_check_code='$code'";

if($dbo->exeUpdate($sql)) {
	action_return(1,$m_langpackage->m_editpassword_success);
} else {
	action_return(0,$m_langpackage->m_editpassword_fail,"modules.php?app=user_forgot&uid=".$user_id."&ucode=".$code);
}
?>