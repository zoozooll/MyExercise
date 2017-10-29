<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require 'foundation/module_order.php';

//语言包引入
$m_langpackage=new moduleslp;

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

//定义文件表
$t_order_info = $tablePreStr."order_info";

// 处理post变量

$order_id = intval(get_args('id'));
$post['pay_message'] = short_check(get_args('pay_message'));
$post['pay_id'] = intval(get_args('pay_id'));
$post['pay_name'] = short_check(get_args('pay_name'));
$post['pay_status']=1;

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();
$update=upd_order_info($dbo,$t_order_info,$post,$order_id);
if($update) {
	action_return(1,$m_langpackage->m_edit_success,'modules.php?app=user_my_order');
} else {
	action_return(0,$m_langpackage->m_edit_fail,'modules.php?app=user_my_order');
}
exit;
?>