<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require("foundation/module_shop.php");

//语言包引入
$m_langpackage=new moduleslp;

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

//定义文件表
$t_shop_info = $tablePreStr."shop_info";

// 处理post变量
$post['shop_name'] = short_check(get_args('shop_name'));
$post['shop_address'] = long_check(get_args('shop_address'));
$post['shop_template'] = short_check(get_args('shop_template'));
$post['shop_country'] = intval(get_args('country'));
$post['shop_province'] = intval(get_args('province'));
$post['shop_city'] = intval(get_args('city'));
$post['shop_district'] = intval(get_args('district'));
$post['shop_intro'] = big_check(get_args('shop_intro'));
$post['shop_management'] = short_check(get_args('shop_management'));
$post['user_id'] = $user_id;
$post['shop_id'] = $user_id;
$post['shop_creat_time'] = $ctime->long_time();

if(insert_shop_info($dbo,$t_shop_info,$post)) {
	set_sess_shop_id($post['shop_id']);
	action_return(1,$m_langpackage->m_shopcreate_success);
} else {
	action_return(0,$m_langpackage->m_shopcreate_fail,'-1');
}
exit;
?>