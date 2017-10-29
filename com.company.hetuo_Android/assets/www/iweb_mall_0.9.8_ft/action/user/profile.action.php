<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require("foundation/module_users.php");

//语言包引入
$m_langpackage=new moduleslp;

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

//定义文件表
$t_user_info = $tablePreStr."user_info";

// 处理post变量
$post['user_truename'] = short_check(get_args('user_truename'));
$post['user_birthday'] = intval(get_args('Y')) . '-' .intval(get_args('M')) . '-' .intval(get_args('D'));
$post['user_gender'] = intval(get_args('user_gender'));
$post['user_marry'] = intval(get_args('user_marry'));
$post['user_mobile'] = short_check(get_args('user_mobile'));
$post['user_telphone'] = short_check(get_args('user_telphone'));
$post['user_zipcode'] = short_check(get_args('user_zipcode'));
$post['user_address'] = short_check(get_args('user_address'));
$post['user_qq'] = short_check(get_args('user_qq'));
$post['user_msn'] = short_check(get_args('user_msn'));
$post['user_skype'] = short_check(get_args('user_skype'));
$post['user_country'] = intval(get_args('country'));
$post['user_province'] = intval(get_args('province'));
$post['user_city'] = intval(get_args('city'));
$post['user_district'] = intval(get_args('district'));

if(update_user_info($dbo,$t_user_info,$post,$user_id)) {
	action_return(1);
} else {
	action_return(0,$m_langpackage->m_edit_fail,'-1');
}
exit;
?>