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
$post['shop_notice'] = big_check(get_args('shop_notice'));
$shop_id = intval(get_args('shop_id'));

if(update_shop_info($dbo,$t_shop_info,$post,$shop_id)) {
	action_return(1,$m_langpackage->m_edit_success);
} else {
	action_return(0,$m_langpackage->m_edit_fail,'-1');
}
exit;
?>