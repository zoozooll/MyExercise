<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//引入模块公共方法文件
require 'foundation/module_groupbuy.php';



//语言包引入
$m_langpackage=new moduleslp;

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

//定义文件表
$t_groupbuy = $tablePreStr."groupbuy";

/* post 数据处理 */
$post['group_name'] =short_check(get_args('groupbuy_name'));
$post['start_time'] = short_check(get_args('start_time'));
$post['end_time'] = short_check(get_args('end_time'));
$post['group_desc'] = long_check(get_args('groupbuy_explain'));
$post['goods_id'] = intval(get_args('goods_id'));
$post['min_quantity'] = intval(get_args('min_quantity'));
$post['spec_price'] = floatval(get_args('spec_price'));
$post['shop_id']=$shop_id;

//数据库操作
dbtarget('w',$dbServs);
$dbo=new dbex();

$insert_id=insert_groupbuy($dbo,$t_groupbuy,$post);
if($insert_id) {
	action_return(1,$m_langpackage->m_add_success,'-1');
} else {
	action_return(0,$m_langpackage->m_add_fail,'-1');
}

exit;
?>