<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require 'foundation/module_users.php';

//语言包引入
$m_langpackage=new moduleslp;

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

//定义文件表
$t_user_address = $tablePreStr."user_address";

// 处理post变量
$post['user_id'] = $user_id;
$post['to_user_name'] = short_check(get_args('to_user_name'));
$post['mobile'] = short_check(get_args('mobile'));
$post['telphone'] = short_check(get_args('telphone'));
$post['full_address'] = short_check(get_args('full_address'));
$post['zipcode'] = short_check(get_args('zipcode'));
$post['user_country'] = intval(get_args('country'));
$post['user_province'] = intval(get_args('province'));
$post['user_city'] = intval(get_args('city'));
$post['user_district'] = intval(get_args('district'));
$post['email'] = short_check(get_args('email'));
$address_id = intval(get_args('address_id'));
//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();
if(empty($address_id)){
	$insert_id=insert_user_address($dbo,$t_user_address,$post);
	if($insert_id) {
		action_return(1,$m_langpackage->m_add_success);
	} else {
		action_return(0,$m_langpackage->m_add_fail);
	}
}else{
	$update=update_address($dbo,$t_user_address,$post,$address_id);
	if($update) {
		action_return(1,$m_langpackage->m_edit_success);
	} else {
		action_return(0,$m_langpackage->m_edit_fail);
	}
}
exit;
?>